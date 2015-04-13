package com.ankamagames.framework.kernel.gameStats;

public final class LinkedPropertyNode extends SimplePropertyNode
{
    public LinkedPropertyNode(final String name, final Node parent, final MergeMode mergeMode) {
        super(name, parent, mergeMode);
    }
    
    @Override
    public long getValue() {
        this.m_parent.update();
        return this.m_value;
    }
}
