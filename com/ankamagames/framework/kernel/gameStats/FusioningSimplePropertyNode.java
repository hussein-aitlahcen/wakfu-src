package com.ankamagames.framework.kernel.gameStats;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import java.util.*;

public class FusioningSimplePropertyNode extends SimplePropertyNode
{
    private final Map<String, Long> m_values;
    
    public FusioningSimplePropertyNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
        if (mergeMode == MergeMode.REPLACE) {
            this.m_values = null;
        }
        else {
            this.m_values = new HashMap<String, Long>(5);
        }
    }
    
    @Override
    public void setValue(final long value) {
        throw new UnsupportedOperationException("Les donn\u00e9es d'un noeud fusionnant ne peuvent provenir que d'une d\u00e9s\u00e9rialisation");
    }
    
    @Override
    public void serialize(final ByteArray buffer) {
        throw new UnsupportedOperationException("Un noeud fusionnant ne peut pas \u00eatre s\u00e9rialis\u00e9");
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final String source) {
        final long value = buffer.getLong();
        switch (this.m_mergeMode) {
            case REPLACE: {
                this.m_value = value;
                break;
            }
            case AVERAGE: {
                this.m_values.put(source, value);
                this.m_value = 0L;
                for (final Long val : this.m_values.values()) {
                    this.m_value += val;
                }
                this.m_value /= this.m_values.size();
                break;
            }
            case SUM: {
                this.m_values.put(source, value);
                this.m_value = 0L;
                for (final Long val : this.m_values.values()) {
                    this.m_value += val;
                }
                break;
            }
        }
    }
}
