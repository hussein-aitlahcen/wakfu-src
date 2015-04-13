package com.ankamagames.xulor2.component.text.builder.content;

import com.ankamagames.xulor2.component.text.document.part.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.text.*;

public class ImageContentBlock extends AbstractContentBlock
{
    public ImageContentBlock() {
        super();
        this.setType(BlockType.IMAGE);
        this.setEndIndex(1);
    }
    
    @Override
    public int getLength() {
        return 1;
    }
    
    @Override
    public void setDocumentPart(final AbstractDocumentPart documentPart) {
        super.setDocumentPart(documentPart);
    }
    
    @Override
    public ImageDocumentPart getDocumentPart() {
        return (ImageDocumentPart)this.m_documentPart;
    }
    
    public Pixmap getPixmap() {
        if (this.m_documentPart != null) {
            return this.getDocumentPart().getPixmap();
        }
        return null;
    }
    
    public int getImageHeight() {
        return this.getDocumentPart().getHeight();
    }
    
    @Override
    public int getHeight() {
        return Math.max(super.getHeight(), this.getDocumentPart().getHeight());
    }
    
    @Override
    public int getWidth() {
        return Math.max(super.getWidth(), this.getDocumentPart().getWidth());
    }
    
    @Override
    public int getContentBlockPartIndexFromCoordinates(final TextRenderer defaultTextRenderer, final int x) {
        return 0;
    }
    
    @Override
    public int getContentBlockPartLeftFromIndex(final TextRenderer defaultTextRenderer, final int index) {
        return 0;
    }
    
    @Override
    public int getContentBlockPartRightFromIndex(final TextRenderer defaultTextRenderer, final int index) {
        return this.getWidth();
    }
}
