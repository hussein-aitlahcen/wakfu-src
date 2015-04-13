package com.ankamagames.framework.kernel.gameStats.intelligentNodes;

import com.ankamagames.framework.kernel.gameStats.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import java.util.*;

public class FusioningTrackMaxPropertyNode extends ContainerNode
{
    private final Map<String, Long> m_values;
    private final LinkedPropertyNode m_value;
    private final LinkedPropertyNode m_max;
    private final LinkedPropertyNode m_maxTime;
    
    public FusioningTrackMaxPropertyNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
        if (mergeMode == MergeMode.REPLACE) {
            this.m_values = null;
        }
        else {
            this.m_values = new HashMap<String, Long>(5);
        }
        this.m_value = this.addChild(new LinkedPropertyNode("value", this, mergeMode));
        this.m_max = this.addChild(new LinkedPropertyNode("max", this, mergeMode));
        this.m_maxTime = this.addChild(new LinkedPropertyNode("maxTime", this, mergeMode));
    }
    
    @Override
    public void serialize(final ByteArray buffer) {
        throw new UnsupportedOperationException("Un noeud fusionnant ne peut pas \u00eatre s\u00e9rialis\u00e9");
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final String source) {
        long value = buffer.getLong();
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
            this.m_maxTime.setValue(System.currentTimeMillis());
        }
    }
}
