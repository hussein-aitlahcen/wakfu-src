package com.ankamagames.wakfu.client.alea.highlightingCells;

import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class MovementInformationDisplayZone extends RangeAndEffectDisplayer implements CustomTextureHighlightingProvider
{
    private static final String RANGE_NAME = "MovementInformationRangeEffect";
    
    public MovementInformationDisplayZone() {
        super("MovementInformationRangeEffect", WakfuClientConstants.RANGE_MOVEMENT, null, null, null, null, null, null, null, null);
    }
    
    public boolean refreshMovementInformationDisplay(final CharacterInfo fighter) {
        if (fighter != null && !fighter.isInvisibleForLocalPlayer() && !fighter.hasProperty(FightPropertyType.DISPLAYED_LIKE_A_DECORATION)) {
            this.selectRange(fighter);
            final CharacterSpecificRangeDisplayer specificRangeDisplayer = fighter.getSpecificRangeDisplayer();
            if (specificRangeDisplayer != null) {
                specificRangeDisplayer.addCellsForCharacter(fighter, this.m_range, this.m_rangeWithConstraint, this.m_rangeWithoutLOS);
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected RangeValidity checkValidity(final Point3 target, CharacterInfo fighter) {
        if (fighter == null) {
            return RangeValidity.INVALID;
        }
        final Point3 position = fighter.getPosition();
        final int diff = Math.abs(position.getX() - target.getX()) + Math.abs(position.getY() - target.getY());
        if (fighter.isActiveProperty(FightPropertyType.IS_A_COPY_OF_HIS_CONTROLLER)) {
            fighter = fighter.getController();
            if (fighter == null) {
                return RangeValidity.INVALID;
            }
        }
        if (fighter.isActiveProperty(FightPropertyType.ROOTED) || fighter.isActiveProperty(FightPropertyType.DO_NOT_MOVE_IN_FIGHT)) {
            return RangeValidity.INVALID;
        }
        int mp = fighter.getCharacteristicValue(FighterCharacteristicType.MP);
        if (fighter.isActiveProperty(FightPropertyType.SEVEN_LEAGUE_BOOTS)) {
            mp /= 2;
        }
        if (fighter.isActiveProperty(FightPropertyType.LAME)) {
            mp /= 2;
        }
        if (diff > mp) {
            return RangeValidity.INVALID;
        }
        if (Math.abs(position.getZ() - target.getZ()) > fighter.getJumpCapacity()) {
            return RangeValidity.OK;
        }
        return RangeValidity.OK;
    }
    
    @Override
    public void update() {
    }
}
