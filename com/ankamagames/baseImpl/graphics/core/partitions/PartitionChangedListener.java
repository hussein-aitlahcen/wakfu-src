package com.ankamagames.baseImpl.graphics.core.partitions;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public interface PartitionChangedListener<Target extends IsoWorldTarget, Partition extends AbstractPartition>
{
    void partitionChanged(Target p0, Partition p1, Partition p2);
}
