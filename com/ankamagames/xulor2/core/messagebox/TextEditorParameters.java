package com.ankamagames.xulor2.core.messagebox;

import com.ankamagames.xulor2.util.*;

public final class TextEditorParameters
{
    private int m_maxCharacters;
    private int m_maxWidth;
    private boolean m_multiline;
    private Dimension m_prefSize;
    
    public void setMaxCharacters(final int maxCharacters) {
        this.m_maxCharacters = maxCharacters;
    }
    
    public void setMaxWidth(final int maxWidth) {
        this.m_maxWidth = maxWidth;
    }
    
    public void setMultiline(final boolean multiline) {
        this.m_multiline = multiline;
    }
    
    public void setPrefSize(final Dimension prefSize) {
        this.m_prefSize = prefSize;
    }
    
    public int getMaxCharacters() {
        return this.m_maxCharacters;
    }
    
    public int getMaxWidth() {
        return this.m_maxWidth;
    }
    
    public boolean isMultiline() {
        return this.m_multiline;
    }
    
    public Dimension getPrefSize() {
        return this.m_prefSize;
    }
    
    @Override
    public String toString() {
        return "TextEditorParameters{m_maxCharacters=" + this.m_maxCharacters + ", m_maxWidth=" + this.m_maxWidth + ", m_multiline=" + this.m_multiline + ", m_prefSize=" + this.m_prefSize + '}';
    }
}
