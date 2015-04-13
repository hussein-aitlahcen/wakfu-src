package com.ankamagames.wakfu.client.core.game.item.action;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import org.jetbrains.annotations.*;

public interface TargetAPSSelectionListener
{
    void onPositionSelected(@NotNull Point3 p0);
    
    void onCharacterSelected(@NotNull CharacterActor p0);
    
    void onSelectionCanceled(@Nullable Point3 p0);
}
