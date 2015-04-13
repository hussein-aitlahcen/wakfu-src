package com.ankamagames.baseImpl.graphics.core.partitions;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import java.util.*;

public abstract class GameLocalPartitionManager<P extends AbstractPartition<P>, T extends IsoWorldTarget> extends AbstractLocalPartitionManager<P> implements TargetPositionListener<T>
{
    protected final List<PartitionChangedListener<T, P>> m_partitionChangedListeners;
    
    protected GameLocalPartitionManager() {
        super();
        this.m_partitionChangedListeners = new ArrayList<PartitionChangedListener<T, P>>();
    }
    
    protected abstract T getReferenceTarget();
    
    public final boolean containsPartitionListener(final PartitionChangedListener<T, P> listener) {
        return this.m_partitionChangedListeners.contains(listener);
    }
    
    public final void addPartitionListener(final PartitionChangedListener<T, P> listener) {
        this.m_partitionChangedListeners.add(listener);
    }
    
    public final void removePartitionChangedListener(final PartitionChangedListener<T, P> listener) {
        this.m_partitionChangedListeners.remove(listener);
    }
    
    public final void removeAllPartitionChangedListener() {
        this.m_partitionChangedListeners.clear();
    }
    
    public void firePartitionChanged(final T mobile, final P oldPartition, final P newPartition) {
        for (int i = 0; i < this.m_partitionChangedListeners.size(); ++i) {
            this.m_partitionChangedListeners.get(i).partitionChanged(mobile, oldPartition, newPartition);
        }
    }
}
