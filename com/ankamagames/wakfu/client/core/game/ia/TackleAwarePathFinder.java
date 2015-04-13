package com.ankamagames.wakfu.client.core.game.ia;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class TackleAwarePathFinder extends PathFinder
{
    private float m_ennemyTackleCost;
    private float m_negativeZoneCost;
    private Fight m_fight;
    private FightMap m_fightMap;
    private byte m_moverTeamId;
    private final TLongHashSet m_ennemies;
    private int m_negativeZonesCount;
    
    public TackleAwarePathFinder() {
        super();
        this.m_ennemyTackleCost = 0.0f;
        this.m_negativeZoneCost = 0.0f;
        this.m_moverTeamId = -1;
        this.m_ennemies = new TLongHashSet();
        this.m_negativeZonesCount = 0;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_ennemyTackleCost = 0.0f;
        this.m_negativeZoneCost = 0.0f;
        this.m_fight = null;
        this.m_fightMap = null;
        this.m_moverTeamId = -1;
        this.m_ennemies.clear();
        this.m_negativeZonesCount = 0;
    }
    
    public void reset() {
        this.m_fight = null;
        this.m_fightMap = null;
        this.m_moverTeamId = -1;
        this.m_negativeZonesCount = 0;
    }
    
    public void initialize(final Fight fight, final byte moverTeamId) {
        this.m_fight = fight;
        this.m_fightMap = this.m_fight.getFightMap();
        this.m_moverTeamId = moverTeamId;
    }
    
    @Override
    protected float computeNodeWeight(final PathFinderNode currentNode, final CellPathData currentNodeData, final PathFinderNode nextNode, final CellPathData nextNodeData, final byte movementDirection) {
        float weight = super.computeNodeWeight(currentNode, currentNodeData, nextNode, nextNodeData, movementDirection);
        final int minX = currentNodeData.m_x - this.m_moverPhysicalRadius - 1;
        final int maxX = currentNodeData.m_x + this.m_moverPhysicalRadius + 1;
        final int minY = currentNodeData.m_y - this.m_moverPhysicalRadius - 1;
        final int maxY = currentNodeData.m_y + this.m_moverPhysicalRadius + 1;
        this.m_ennemies.clear();
        this.m_negativeZonesCount = 0;
        this.insertMalusZoneOnCell(currentNodeData.m_x, currentNodeData.m_y);
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                this.insertEnemyOnCell(x, y);
            }
        }
        weight += this.m_ennemies.size() * this.m_ennemyTackleCost + this.m_negativeZonesCount * this.m_negativeZoneCost;
        this.m_ennemies.clear();
        this.m_negativeZonesCount = 0;
        return weight;
    }
    
    private void insertEnemyOnCell(final int x, final int y) {
        if (!this.m_fightMap.isInMap(x, y)) {
            return;
        }
        final EffectUser user = this.m_fight.getCharacterInfoAtPosition(x, y);
        if (user == null) {
            return;
        }
        if (user.getEffectUserType() != 20) {
            return;
        }
        final CharacterInfo character = (CharacterInfo)user;
        if (character.getTeamId() == this.m_moverTeamId) {
            return;
        }
        if (character.isInvisibleForLocalPlayer()) {
            return;
        }
        this.m_ennemies.add(user.getId());
    }
    
    private void insertMalusZoneOnCell(final int x, final int y) {
        if (!this.m_fightMap.isInMap(x, y)) {
            return;
        }
        final Collection<BasicEffectArea> areas = this.m_fight.getActiveEffectAreas();
        if (areas.isEmpty()) {
            return;
        }
        for (final BasicEffectArea area : areas) {
            final EffectAreaType type = EffectAreaType.getTypeFromId(area.getType());
            boolean takeInAccount = false;
            switch (type) {
                case GLYPH:
                case AURA:
                case BATTLEGROUND_BORDER: {
                    takeInAccount = true;
                    break;
                }
                default: {
                    takeInAccount = false;
                    break;
                }
            }
            if (!takeInAccount) {
                continue;
            }
            if (!area.contains(x, y, (short)0) || !this.hasNegativeEffect(area)) {
                continue;
            }
            ++this.m_negativeZonesCount;
        }
    }
    
    private boolean hasNegativeEffect(final BasicEffectArea area) {
        final GrowingArray<Effect> effects = area.getEffects();
        for (int i = 0, n = effects.size(); i < n; ++i) {
            final Effect effect = effects.get(i);
            final WakfuRunningEffect def = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId());
            if (def != null) {
                if (def.getRunningEffectStatus() == RunningEffectStatus.NEGATIVE) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void setEnemyTackleCost(final float enemyTackleCost) {
        this.m_ennemyTackleCost = enemyTackleCost;
    }
    
    public void setNegativeZoneCost(final float negativeZoneCost) {
        this.m_negativeZoneCost = negativeZoneCost;
    }
}
