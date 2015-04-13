package com.ankamagames.framework.fileFormat.html.parser.lexer.impl;

import com.ankamagames.framework.fileFormat.html.parser.lexer.*;
import com.ankamagames.framework.fileFormat.html.parser.datasource.*;
import com.ankamagames.framework.fileFormat.html.parser.*;

public class PPIgnoreTagValue implements TokenProcPlugin
{
    private PageSource page;
    private TokenTag parentTag;
    
    public PPIgnoreTagValue() {
        super();
        this.page = null;
        this.parentTag = null;
    }
    
    void setPageSource(final PageSource ps) {
        this.page = ps;
    }
    
    void setParentTag(final TokenTag pTag) {
        this.parentTag = pTag;
    }
    
    @Override
    public String getEntryString() {
        return null;
    }
    
    @Override
    public Token parse() throws CommonException {
        final TokenIgnoreTagValueImpl retVal = new TokenIgnoreTagValueImpl();
        final StringBuffer sbResult = new StringBuffer();
        char ch;
        while ((ch = this.page.getCurChar()) != '\uffff') {
            if (ch == '<') {
                if (this.parentTag == null) {
                    retVal.setValue(sbResult.toString());
                    return retVal;
                }
                final String tagName = this.parentTag.getTagName();
                if (tagName.equalsIgnoreCase(this.parentTag.getTagName())) {
                    final int startIndex = this.page.getCurrentCursorPosition() + 2;
                    final String strPreCon = this.page.getSubString(startIndex, startIndex + ("/" + this.parentTag.getTagName()).length());
                    if (strPreCon.indexOf(this.parentTag.getTagName().toLowerCase()) > -1 || strPreCon.indexOf(this.parentTag.getTagName().toUpperCase()) > -1) {
                        retVal.setParentTag(this.parentTag);
                        retVal.setValue(sbResult.toString());
                        return retVal;
                    }
                    sbResult.append(ch);
                    this.page.getNextChar();
                }
                else {
                    this.page.getNextChar();
                }
            }
            else {
                sbResult.append(ch);
                this.page.getNextChar();
            }
        }
        return retVal;
    }
}
