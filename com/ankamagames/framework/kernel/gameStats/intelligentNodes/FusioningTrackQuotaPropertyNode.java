package com.ankamagames.framework.kernel.gameStats.intelligentNodes;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.gameStats.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import java.util.*;

public class FusioningTrackQuotaPropertyNode extends ContainerNode
{
    protected static final Logger m_logger;
    private final Map<String, Long> m_values;
    private final LinkedPropertyNode m_value;
    private final LinkedPropertyNode m_min;
    private final LinkedPropertyNode m_max;
    private final LinkedPropertyNode m_quotaMin;
    private final LinkedPropertyNode m_quotaMax;
    
    public FusioningTrackQuotaPropertyNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
        if (mergeMode == MergeMode.REPLACE) {
            this.m_values = null;
        }
        else {
            this.m_values = new HashMap<String, Long>(5);
        }
        this.m_value = this.addChild(new LinkedPropertyNode("value", this, mergeMode));
        (this.m_min = this.addChild(new LinkedPropertyNode("min", this, mergeMode))).setValue(Long.MAX_VALUE);
        this.m_max = this.addChild(new LinkedPropertyNode("max", this, mergeMode));
        this.m_quotaMin = this.addChild(new LinkedPropertyNode("quotaMin", this, mergeMode));
        this.m_quotaMax = this.addChild(new LinkedPropertyNode("quotaMax", this, mergeMode));
    }
    
    @Override
    public void serialize(final ByteArray buffer) {
        throw new UnsupportedOperationException("Un noeud fusionnant ne peut pas \u00eatre s\u00e9rialis\u00e9");
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final String source) {
        long value = buffer.getLong();
        final long quotaMin = buffer.getLong();
        final long quotaMax = buffer.getLong();
        if (this.m_mergeMode != MergeMode.REPLACE) {
            this.m_values.put(source, value);
            value = 0L;
            for (final long data : this.m_values.values()) {
                value += data;
            }
            if (this.m_mergeMode == MergeMode.AVERAGE) {
                value /= this.m_values.size();
            }
        }
        this.m_value.setValue(value);
        if (value > this.m_max.getValue()) {
            this.m_max.setValue(value);
        }
        else if (value < this.m_min.getValue()) {
            this.m_min.setValue(value);
        }
        this.m_quotaMin.setValue(quotaMin);
        this.m_quotaMax.setValue(quotaMax);
    }
    
    static {
        m_logger = Logger.getLogger((Class)FusioningTrackQuotaPropertyNode.class);
    }
}
