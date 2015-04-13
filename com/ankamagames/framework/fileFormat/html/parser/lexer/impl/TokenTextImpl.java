package com.ankamagames.framework.fileFormat.html.parser.lexer.impl;

import com.ankamagames.framework.fileFormat.html.parser.datasource.*;
import com.ankamagames.framework.fileFormat.html.parser.*;

public class TokenTextImpl implements TokenText
{
    private PageSource page;
    private TokenTag parentTokenTag;
    private String valueText;
    private int index;
    
    TokenTextImpl() {
        super();
        this.page = null;
        this.parentTokenTag = null;
        this.valueText = null;
        this.index = -1;
        this.valueText = "aaa";
    }
    
    void setIndex(final int idx) {
        this.index = idx;
    }
    
    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
    
    @Override
    public String toString() {
        return "[Text]" + this.valueText;
    }
    
    @Override
    public String getValueText() {
        return this.valueText;
    }
    
    public void setValueText(final String valTxt) {
        this.valueText = valTxt;
    }
    
    @Override
    public int getEndPosition() {
        return 0;
    }
    
    @Override
    public PageSource getPage() {
        return null;
    }
    
    @Override
    public int getStartPosition() {
        return 0;
    }
    
    @Override
    public String toHtml() {
        return null;
    }
    
    @Override
    public int getIndex() {
        return this.index;
    }
}
