package com.ankamagames.wakfu.client.alea.highlightingCells;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.effectArea.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;

public class BeaconDisplayZone extends RangeAndEffectDisplayer implements CustomTextureHighlightingProvider
{
    private static final Logger m_logger;
    private static final String ZONE_EFFECT_NAME = "BeaconZoneEffect";
    private static final String RANGE_NAME = "BeaconRangeEffect";
    private BeaconEffectArea m_rollOverBeacon;
    private ObjectPair<ArrayList<Point3>, long[]> m_beaconComputation;
    private static final String TEXTURE_BEACON_RANGE = "beaconRange.tga";
    private static final String TEXTURE_BEACON_EFFECT = "beaconEffect.tga";
    private static final BeaconDisplayZone m_instance;
    
    public static BeaconDisplayZone getInstance() {
        return BeaconDisplayZone.m_instance;
    }
    
    private BeaconDisplayZone() {
        super("BeaconRangeEffect", WakfuClientConstants.RANGE_COLOR, "BeaconZoneEffect", WakfuClientConstants.ZONE_EFFECT_COLOR, null, null, null, null, null, null);
    }
    
    public void refreshBeaconDisplay(final BeaconEffectArea item, final CharacterInfo fighter) {
        this.m_rollOverBeacon = item;
        this.m_beaconComputation = this.m_rollOverBeacon.determineTargetCells(fighter);
        this.selectRange(fighter);
        final Fight fight = fighter.getCurrentFight();
        if (fight != null) {
            final FightMap fightMap = fight.getFightMap();
            if (fightMap != null) {
                final long[] second = this.m_beaconComputation.getSecond();
                if (second != null) {
                    for (int i = 0; i < second.length; ++i) {
                        final long cell = second[i];
                        final int x = PositionValue.getXFromLong(cell);
                        final int y = PositionValue.getYFromLong(cell);
                        final short z = PositionValue.getZFromLong(cell);
                        this.selectZoneEffect(this.m_rollOverBeacon, fighter, new Point3(x, y, fightMap.getCellHeight(x, y)));
                    }
                }
            }
        }
        this.m_beaconComputation = null;
        this.m_rollOverBeacon = null;
    }
    
    @Override
    protected RangeValidity checkValidity(final Point3 target, final CharacterInfo caster) {
        if (this.m_rollOverBeacon == null || this.m_beaconComputation == null) {
            return RangeValidity.INVALID;
        }
        for (final Point3 cell : this.m_beaconComputation.getFirst()) {
            if (cell.equals(target)) {
                return RangeValidity.OK;
            }
        }
        return RangeValidity.INVALID;
    }
    
    @Override
    public void update() {
        try {
            String textureFileName = WakfuConfiguration.getInstance().getString("highLightGfxPath") + "beaconEffect.tga";
            this.m_range.setTexture(textureFileName, HighLightTextureApplication.ISO);
            textureFileName = WakfuConfiguration.getInstance().getString("highLightGfxPath") + "beaconRange.tga";
            this.setTexture(textureFileName, HighLightTextureApplication.ISO);
        }
        catch (Exception e) {
            BeaconDisplayZone.m_logger.error((Object)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)BeaconDisplayZone.class);
        m_instance = new BeaconDisplayZone();
    }
}
