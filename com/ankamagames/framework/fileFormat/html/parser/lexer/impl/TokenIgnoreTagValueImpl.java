package com.ankamagames.framework.fileFormat.html.parser.lexer.impl;

import com.ankamagames.framework.fileFormat.html.parser.*;
import com.ankamagames.framework.fileFormat.html.parser.datasource.*;

public class TokenIgnoreTagValueImpl implements TokenIgnoreTagValue
{
    private String strValue;
    private TokenTag pTag;
    private int index;
    
    public TokenIgnoreTagValueImpl() {
        super();
        this.strValue = null;
        this.pTag = null;
        this.index = -1;
    }
    
    @Override
    public String toString() {
        return "[IgnoreTagValue:" + this.pTag.getTagName() + "]" + this.strValue;
    }
    
    void setParentTag(final TokenTag parTag) {
        this.pTag = parTag;
    }
    
    @Override
    public TokenTag getParentTag() {
        return this.pTag;
    }
    
    void setValue(final String val) {
        this.strValue = val;
    }
    
    @Override
    public String getValue() {
        return this.strValue;
    }
    
    @Override
    public int getEndPosition() {
        return 0;
    }
    
    void setIndex(final int idx) {
        this.index = idx;
    }
    
    @Override
    public int getIndex() {
        return this.index;
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
}
