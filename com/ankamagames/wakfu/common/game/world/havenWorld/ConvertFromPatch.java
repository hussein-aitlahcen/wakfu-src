package com.ankamagames.wakfu.common.game.world.havenWorld;

import com.ankamagames.wakfu.common.game.havenWorld.*;
import org.jetbrains.annotations.*;

public interface ConvertFromPatch
{
    void fromPatch(PartitionPatch p0, PartitionPatch p1, PartitionPatch p2, PartitionPatch p3);
    
    void setAttachedBuilding(@NotNull AbstractBuildingStruct[] p0);
}
