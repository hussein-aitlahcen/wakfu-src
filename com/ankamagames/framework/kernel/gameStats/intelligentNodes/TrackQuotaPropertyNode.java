package com.ankamagames.framework.kernel.gameStats.intelligentNodes;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.gameStats.*;
import java.nio.*;

public class TrackQuotaPropertyNode extends ContainerNode
{
    protected static final Logger m_logger;
    private final LinkedPropertyNode m_value;
    private final LinkedPropertyNode m_min;
    private final LinkedPropertyNode m_max;
    private final LinkedPropertyNode m_quotaMin;
    private final LinkedPropertyNode m_quotaMax;
    
    public TrackQuotaPropertyNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
        this.m_value = this.addChild(new LinkedPropertyNode("value", this, mergeMode));
        (this.m_min = this.addChild(new LinkedPropertyNode("min", this, mergeMode))).setValue(Long.MAX_VALUE);
        this.m_max = this.addChild(new LinkedPropertyNode("max", this, mergeMode));
        (this.m_quotaMin = this.addChild(new LinkedPropertyNode("quotaMin", this, mergeMode))).setValue(-1L);
        (this.m_quotaMax = this.addChild(new LinkedPropertyNode("quotaMax", this, mergeMode))).setValue(-1L);
    }
    
    public void setQuotaMin(final long quotaMin) {
        this.m_quotaMin.setValue(quotaMin);
    }
    
    public void setQuotaMax(final long quotaMax) {
        this.m_quotaMax.setValue(quotaMax);
    }
    
    @Override
    public long getValue() {
        return this.m_value.getValue();
    }
    
    public void setValue(final long value, final boolean computeMin) {
        this.m_value.setValue(value);
        if (value > this.m_max.getValue()) {
            this.m_max.setValue(value);
        }
        if (computeMin && value < this.m_min.getValue()) {
            this.m_min.setValue(value);
        }
    }
    
    @Override
    public void serialize(final ByteArray buffer) {
        this.serializeBase(buffer, NodeType.FUSIONING_TRACK_QUOTA);
        buffer.putLong(this.m_value.getValue());
        buffer.putLong(this.m_quotaMin.getValue());
        buffer.putLong(this.m_quotaMax.getValue());
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final String source) {
        throw new UnsupportedOperationException("Un TrackQuotaPropertyNode doit \u00eatre d\u00e9s\u00e9rialis\u00e9 en tant que FusionningQuotaPropertyNode");
    }
    
    static {
        m_logger = Logger.getLogger((Class)TrackQuotaPropertyNode.class);
    }
}
