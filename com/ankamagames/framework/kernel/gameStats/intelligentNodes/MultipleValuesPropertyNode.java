package com.ankamagames.framework.kernel.gameStats.intelligentNodes;

import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.gameStats.*;
import java.nio.*;

public class MultipleValuesPropertyNode extends ContainerNode
{
    private final LinkedPropertyNode m_total;
    private final LinkedPropertyNode m_min;
    private final LinkedPropertyNode m_max;
    
    public MultipleValuesPropertyNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
        this.m_total = this.addChild(new LinkedPropertyNode("total", this, mergeMode));
        this.m_min = this.addChild(new LinkedPropertyNode("min", this, mergeMode));
        this.m_max = this.addChild(new LinkedPropertyNode("max", this, mergeMode));
    }
    
    public void setValues(final TLongArrayList values) {
        long total = 0L;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (int i = 0; i < values.size(); ++i) {
            final long value = values.getQuick(i);
            total += value;
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }
        this.m_total.setValue(total);
        this.m_min.setValue(min);
        this.m_max.setValue(max);
    }
    
    @Override
    public void serialize(final ByteArray buffer) {
        this.serializeBase(buffer, NodeType.MULTIPLE_VALUES);
        buffer.putLong(this.m_total.getValue());
        buffer.putLong(this.m_min.getValue());
        buffer.putLong(this.m_max.getValue());
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final String source) {
        if (this.m_mergeMode != MergeMode.REPLACE) {
            MultipleValuesPropertyNode.m_logger.warn((Object)"ATTENTION! Le noeud MultipleValuesPropertyNode ne supporte pas d'autre mode de fusion que REPLACE pour l'instant");
        }
        this.m_total.setValue(buffer.getLong());
        this.m_min.setValue(buffer.getLong());
        this.m_max.setValue(buffer.getLong());
    }
}
