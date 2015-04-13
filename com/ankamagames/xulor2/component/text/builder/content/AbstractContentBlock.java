package com.ankamagames.xulor2.component.text.builder.content;

import com.ankamagames.xulor2.component.text.builder.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.text.*;

public abstract class AbstractContentBlock extends AbstractLineSubBlock
{
    private BlockType m_type;
    protected AbstractDocumentPart m_documentPart;
    private int m_startIndex;
    private int m_endIndex;
    protected Alignment5 m_alignment;
    
    public AbstractContentBlock() {
        super();
        this.m_type = BlockType.NONE;
        this.m_startIndex = 0;
        this.m_endIndex = 0;
        this.m_alignment = null;
    }
    
    public abstract int getLength();
    
    public BlockType getType() {
        return this.m_type;
    }
    
    protected void setType(final BlockType type) {
        this.m_type = type;
    }
    
    public AbstractDocumentPart getDocumentPart() {
        return this.m_documentPart;
    }
    
    public void setDocumentPart(final AbstractDocumentPart documentPart) {
        this.m_documentPart = documentPart;
    }
    
    public int getStartIndex() {
        return this.m_startIndex;
    }
    
    public void setStartIndex(final int startIndex) {
        this.m_startIndex = startIndex;
    }
    
    public int getEndIndex() {
        return this.m_endIndex;
    }
    
    public void setEndIndex(final int endIndex) {
        this.m_endIndex = endIndex;
    }
    
    public Alignment5 getAlignment() {
        if (this.m_alignment == null && this.m_documentPart != null) {
            return this.m_documentPart.getAlignment();
        }
        return this.m_alignment;
    }
    
    public void setAlignment(final Alignment5 alignment) {
        this.m_alignment = alignment;
    }
    
    public abstract int getContentBlockPartIndexFromCoordinates(final TextRenderer p0, final int p1);
    
    public abstract int getContentBlockPartLeftFromIndex(final TextRenderer p0, final int p1);
    
    public abstract int getContentBlockPartRightFromIndex(final TextRenderer p0, final int p1);
    
    public enum BlockType
    {
        NONE, 
        TEXT, 
        IMAGE;
    }
}
