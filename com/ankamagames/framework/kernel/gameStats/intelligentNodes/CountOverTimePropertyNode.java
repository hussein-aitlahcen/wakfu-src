package com.ankamagames.framework.kernel.gameStats.intelligentNodes;

import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.gameStats.*;
import java.nio.*;

public class CountOverTimePropertyNode extends ContainerNode
{
    private final LinkedList<Long> m_times;
    private LinkedPropertyNode m_overLastHour;
    private LinkedPropertyNode m_overLastTenMinutes;
    
    public CountOverTimePropertyNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
        this.m_times = new LinkedList<Long>();
        this.m_overLastHour = this.addChild(new LinkedPropertyNode("overLastHour", this, mergeMode));
        this.m_overLastTenMinutes = this.addChild(new LinkedPropertyNode("overLastTenMinutes", this, mergeMode));
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_times.clear();
    }
    
    @Override
    public void update() {
        final long now = System.currentTimeMillis();
        while (!this.m_times.isEmpty() && now - this.m_times.peek() > 3600000L) {
            this.m_times.poll();
        }
        int i = 0;
        for (final Long time : this.m_times) {
            if (now - time <= 600000L) {
                break;
            }
            ++i;
        }
        this.m_overLastHour.setValue(this.m_times.size());
        this.m_overLastTenMinutes.setValue(this.m_times.size() - i);
    }
    
    public final void addOccurence() {
        this.m_times.addLast(System.currentTimeMillis());
    }
    
    @Override
    public void serialize(final ByteArray buffer) {
        this.update();
        this.serializeBase(buffer, NodeType.FUSIONING_COUNT_OVER_TIME);
        buffer.putLong(this.m_overLastHour.getValue());
        buffer.putLong(this.m_overLastTenMinutes.getValue());
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final String source) {
        throw new UnsupportedOperationException("Un CountOverTimePropertyNode doit \u00eatre d\u00e9s\u00e9rialis\u00e9 en tant que FusioningCountOverTimePropertyNode");
    }
}
