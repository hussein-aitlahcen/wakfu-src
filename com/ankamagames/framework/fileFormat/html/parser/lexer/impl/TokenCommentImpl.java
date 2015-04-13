package com.ankamagames.framework.fileFormat.html.parser.lexer.impl;

import com.ankamagames.framework.fileFormat.html.parser.*;
import com.ankamagames.framework.fileFormat.html.parser.datasource.*;

public class TokenCommentImpl implements TokenComment
{
    private String strComment;
    private int index;
    
    public TokenCommentImpl() {
        super();
        this.index = -1;
    }
    
    @Override
    public String toString() {
        return "[Comment] " + this.strComment;
    }
    
    @Override
    public String getCommentText() {
        return this.strComment;
    }
    
    void setCommentText(final String str) {
        this.strComment = str;
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
