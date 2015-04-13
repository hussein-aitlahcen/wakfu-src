package com.ankamagames.framework.fileFormat.html.parser.lexer.impl;

import java.util.*;
import com.ankamagames.framework.fileFormat.html.parser.lexer.*;
import com.ankamagames.framework.fileFormat.html.parser.datasource.*;
import com.ankamagames.framework.fileFormat.html.parser.*;

public class LexerImpl implements Lexer
{
    private Hashtable<String, TokenProcPlugin> tpTable;
    private PageSource page;
    private int currentIndex;
    private TokenTag latestTag;
    private boolean isIgnoredMode;
    PPIgnoreTagValue ppIgrTagVal;
    PPTag ppTag;
    PPText ppText;
    PPComment ppComment;
    
    public LexerImpl(final PageSource pageSource) {
        super();
        this.tpTable = null;
        this.page = null;
        this.currentIndex = 0;
        this.latestTag = null;
        this.isIgnoredMode = false;
        this.ppIgrTagVal = null;
        this.ppTag = null;
        this.ppText = null;
        this.ppComment = null;
        this.currentIndex = 0;
        this.page = pageSource;
        this.tpTable = new Hashtable<String, TokenProcPlugin>();
        this.initProcessor();
    }
    
    private void initProcessor() {
        this.ppIgrTagVal = new PPIgnoreTagValue();
        (this.ppTag = new PPTag()).setPageSource(this.page);
        this.ppText = new PPText();
        this.ppComment = new PPComment();
    }
    
    @Override
    public Token getNextToken() {
        ++this.currentIndex;
        char ch = '\0';
        while (this.page.hasNextChar()) {
            ch = this.page.getCurChar();
            if (this.isIgnoredMode) {
                final TokenIgnoreTagValueImpl ignoredTagValue = this.getIgnoredTagValue();
                ignoredTagValue.setIndex(this.currentIndex);
                return ignoredTagValue;
            }
            if (ch == '<') {
                final char chNext = this.page.getChar(this.page.getCurrentCursorPosition() + 1);
                if (chNext == '!') {
                    final TokenCommentImpl tComment = this.getTokenComment();
                    tComment.setIndex(this.currentIndex);
                    return tComment;
                }
                final TokenTagImpl tToken = this.getTag();
                if (tToken != null) {
                    if (!tToken.isClosedTag()) {
                        this.latestTag = tToken;
                        if (this.isIgnoreValueTag(tToken)) {
                            this.isIgnoredMode = true;
                        }
                    }
                    else if (this.latestTag != null && this.latestTag.getTagName().equalsIgnoreCase(tToken.getTagName())) {
                        this.latestTag = null;
                    }
                }
                tToken.setIndex(this.currentIndex);
                return tToken;
            }
            else if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t') {
                this.page.getNextChar();
            }
            else {
                if (this.latestTag == null) {
                    final TokenTextImpl tText = this.getTokenText(null);
                    tText.setIndex(this.currentIndex);
                    return tText;
                }
                if (this.latestTag.getTagName().equalsIgnoreCase("script") || this.latestTag.getTagName().equalsIgnoreCase("style")) {
                    final TokenIgnoreTagValueImpl ignoredTagValue = this.getIgnoredTagValue();
                    ignoredTagValue.setIndex(this.currentIndex);
                    return ignoredTagValue;
                }
                final TokenTextImpl tText = this.getTokenText(null);
                tText.setIndex(this.currentIndex);
                return tText;
            }
        }
        return null;
    }
    
    private boolean isIgnoreValueTag(final TokenTag tag) {
        final String tagName = tag.getTagName();
        return tagName.equalsIgnoreCase("script") || tagName.equalsIgnoreCase("style");
    }
    
    private TokenIgnoreTagValueImpl getIgnoredTagValue() {
        this.isIgnoredMode = false;
        this.ppIgrTagVal.setPageSource(this.page);
        this.ppIgrTagVal.setParentTag(this.latestTag);
        TokenIgnoreTagValueImpl igrTkVal = null;
        try {
            igrTkVal = (TokenIgnoreTagValueImpl)this.ppIgrTagVal.parse();
        }
        catch (CommonException e) {
            e.printStackTrace();
        }
        return igrTkVal;
    }
    
    public TokenTagImpl getTag() {
        this.ppTag.setPageSource(this.page);
        TokenTagImpl token = null;
        try {
            token = (TokenTagImpl)this.ppTag.parse();
        }
        catch (CommonException e) {
            e.printStackTrace();
        }
        return token;
    }
    
    public TokenTextImpl getTokenText(final TokenTag latestTag) {
        this.ppText.setPageSource(this.page);
        this.ppText.setParentTokenTag(latestTag);
        TokenTextImpl tText = null;
        try {
            tText = (TokenTextImpl)this.ppText.parse();
        }
        catch (CommonException e) {
            e.printStackTrace();
        }
        return tText;
    }
    
    private TokenCommentImpl getTokenComment() {
        this.ppComment.setPageSource(this.page);
        Token token = null;
        try {
            token = this.ppComment.parse();
        }
        catch (CommonException e) {
            e.printStackTrace();
        }
        return (TokenCommentImpl)token;
    }
    
    @Override
    public PageSource getPage() {
        return this.page;
    }
    
    @Override
    public boolean hasNextToken() {
        return this.page.hasNextChar();
    }
    
    @Override
    public void addTokepProcPluging(final TokenProcPlugin tpp) {
    }
}
