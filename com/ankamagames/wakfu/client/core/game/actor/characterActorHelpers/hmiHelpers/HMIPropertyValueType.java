package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.jetbrains.annotations.*;

public enum HMIPropertyValueType
{
    DURATION;
    
    @Nullable
    public static HMIPropertyValueType getByName(@NotNull final String name) {
        final HMIPropertyValueType[] types = values();
        for (int i = 0; i < types.length; ++i) {
            final HMIPropertyValueType type = types[i];
            if (type.toString().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
