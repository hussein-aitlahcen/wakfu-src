package com.ankamagames.framework.kernel.gameStats.intelligentNodes;

import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.gameStats.*;
import java.nio.*;

public class TrackMaxPropertyNode extends ContainerNode
{
    private final LinkedPropertyNode m_value;
    private final LinkedPropertyNode m_max;
    private final LinkedPropertyNode m_maxTime;
    
    public TrackMaxPropertyNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
        this.m_value = this.addChild(new LinkedPropertyNode("value", this, mergeMode));
        this.m_max = this.addChild(new LinkedPropertyNode("max", this, mergeMode));
        this.m_maxTime = this.addChild(new LinkedPropertyNode("maxTime", this, mergeMode));
    }
    
    @Override
    public long getValue() {
        return this.m_value.getValue();
    }
    
    public void setValue(final long value) {
        this.m_value.setValue(value);
        if (value > this.m_max.getValue()) {
            this.m_max.setValue(value);
            this.m_maxTime.setValue(System.currentTimeMillis());
        }
    }
    
    @Override
    public void serialize(final ByteArray buffer) {
        this.serializeBase(buffer, NodeType.FUSIONING_TRACK_MAX);
        buffer.putLong(this.m_value.getValue());
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final String source) {
        throw new UnsupportedOperationException("Un TrackMaxPropertyNode doit \u00eatre d\u00e9s\u00e9rialis\u00e9 en tant que FusioningTrackMaxPropertyNode");
    }
}
