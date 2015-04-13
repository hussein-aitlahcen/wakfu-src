package com.ankamagames.xulor2.component.text.builder.selection;

import com.ankamagames.xulor2.component.text.builder.*;

public class SelectionBlock extends AbstractLineSubBlock
{
    private boolean m_isCursor;
    
    public SelectionBlock() {
        super();
        this.m_isCursor = false;
    }
    
    public boolean isCursor() {
        return this.m_isCursor;
    }
    
    public void setCursor(final boolean isCursor) {
        this.m_isCursor = isCursor;
    }
}
