package com.ankamagames.xulor2.component.text.document;

import com.ankamagames.xulor2.component.text.document.part.*;

public class SinglePartTextDocument extends TextDocument
{
    public SinglePartTextDocument() {
        super();
        this.addEmptyTextPart();
    }
    
    public TextDocumentPart getPart() {
        return (TextDocumentPart)this.getPartAt(0);
    }
    
    @Override
    public String getRawText() {
        return this.getPart().serialize();
    }
    
    @Override
    public void setRawText(final String rawText) {
        this.clearSelectionIndices();
        this.getPart().setText(rawText);
    }
    
    @Override
    public void appendRawText(final String rawText) {
        this.getPart().setText(this.getPart().getText() + rawText);
    }
}
