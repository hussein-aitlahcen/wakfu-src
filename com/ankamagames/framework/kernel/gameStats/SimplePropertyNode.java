package com.ankamagames.framework.kernel.gameStats;

import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class SimplePropertyNode extends Node
{
    protected long m_value;
    
    protected SimplePropertyNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
    }
    
    @Override
    public final Map<String, Node> getDirectChildren() {
        return Collections.emptyMap();
    }
    
    @Override
    public final void clear() {
        this.m_value = 0L;
    }
    
    @Override
    public final boolean hasValue() {
        return true;
    }
    
    @Override
    public long getValue() {
        return this.m_value;
    }
    
    public void setValue(final long value) {
        this.m_value = value;
    }
    
    @Override
    public void serialize(final ByteArray buffer) {
        this.serializeBase(buffer, NodeType.FUSIONING_SIMPLE_PROPERTY);
        buffer.putLong(this.m_value);
    }
    
    @Override
    public void unserialize(final ByteBuffer buffer, final String source) {
        throw new UnsupportedOperationException("Un SimplePropertyNode doit \u00eatre d\u00e9s\u00e9rialis\u00e9 en tant que FusioningSimplePropertyNode");
    }
}
