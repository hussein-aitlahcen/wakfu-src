package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.jetbrains.annotations.*;

public enum HMIFiledProviderType
{
    PRISONER_TAG;
    
    @Nullable
    public static HMIFiledProviderType getByName(@NotNull final String name) {
        final HMIFiledProviderType[] types = values();
        for (int i = 0; i < types.length; ++i) {
            final HMIFiledProviderType type = types[i];
            if (type.toString().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
