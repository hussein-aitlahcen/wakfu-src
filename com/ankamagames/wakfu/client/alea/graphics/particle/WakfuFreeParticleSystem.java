package com.ankamagames.wakfu.client.alea.graphics.particle;

import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

public class WakfuFreeParticleSystem extends FreeParticleSystem
{
    public WakfuFreeParticleSystem(final boolean isEditable) {
        super(isEditable);
    }
    
    @Override
    public boolean isVisible() {
        Fight localFight;
        try {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer == null) {
                return true;
            }
            localFight = localPlayer.getCurrentOrObservedFight();
        }
        catch (Exception e) {
            return super.isVisible();
        }
        final int fightId = this.getFightId();
        if (localFight != null && localFight.getId() != fightId && localFight.getFightMap().isInsideOrBorder(this.getWorldCellX(), this.getWorldCellY())) {
            return false;
        }
        if (fightId != -1) {
            final IsoWorldTarget target = this.getTarget();
            CharacterInfo characterInfo = null;
            if (target instanceof CharacterInfo) {
                characterInfo = (CharacterInfo)target;
            }
            else if (target instanceof CharacterActor) {
                characterInfo = ((CharacterActor)target).getCharacterInfo();
            }
            if (characterInfo != null) {
                final boolean targetIsOutOfPlay = characterInfo.isOutOfPlay();
                if (targetIsOutOfPlay) {
                    return false;
                }
            }
            return FightVisibilityManager.getInstance().isParticuleVisibleForFight(fightId);
        }
        return super.isVisible();
    }
}
