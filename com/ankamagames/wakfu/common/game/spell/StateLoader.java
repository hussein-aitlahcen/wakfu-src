package com.ankamagames.wakfu.common.game.spell;

import org.jetbrains.annotations.*;

public interface StateLoader
{
    @Nullable
    State loadState(int p0);
}
