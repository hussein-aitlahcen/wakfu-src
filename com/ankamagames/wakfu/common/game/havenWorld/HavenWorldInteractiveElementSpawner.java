package com.ankamagames.wakfu.common.game.havenWorld;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import org.jetbrains.annotations.*;

public interface HavenWorldInteractiveElementSpawner
{
    MapInteractiveElement requestSummonInteractiveElement(long p0, long p1, Point3 p2, Item p3);
    
    @Nullable
    MapInteractiveElement requestSpawnInteractiveElement(long p0, int p1, long p2, long p3, Point3 p4);
}
