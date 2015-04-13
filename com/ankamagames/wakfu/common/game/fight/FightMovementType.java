package com.ankamagames.wakfu.common.game.fight;

import org.jetbrains.annotations.*;

public enum FightMovementType
{
    STANDARD, 
    TELEPORT, 
    TELEPORT_WITH_ANIMATION, 
    USE_GATE_TELEPORT, 
    ON_RAILS, 
    SMOOTH_MOVE_ON_AREA;
    
    @Nullable
    public static FightMovementType fromOrdinal(final int ordinal) {
        for (final FightMovementType type : values()) {
            if (type.ordinal() == ordinal) {
                return type;
            }
        }
        return null;
    }
}
