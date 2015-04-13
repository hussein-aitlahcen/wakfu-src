package com.ankamagames.framework.fileFormat.html.parser.lexer.impl;

import com.ankamagames.framework.fileFormat.html.parser.lexer.*;
import com.ankamagames.framework.fileFormat.html.parser.datasource.*;
import com.ankamagames.framework.fileFormat.html.parser.*;

public class PPText implements TokenProcPlugin
{
    private String parsedText;
    private PageSource page;
    private TokenTag parentTokenTag;
    
    public PPText() {
        super();
        this.parsedText = null;
        this.page = null;
        this.parentTokenTag = null;
    }
    
    public void setPageSource(final PageSource ps) {
        this.page = ps;
    }
    
    public void setParentTokenTag(final TokenTag pTokenTag) {
        this.parentTokenTag = pTokenTag;
    }
    
    @Override
    public String getEntryString() {
        return null;
    }
    
    @Override
    public TokenText parse() throws CommonException {
        final StringBuffer sb = new StringBuffer();
        this.parsedText = "";
        for (char ch = this.page.getCurChar(); ch != '\uffff' && ch != '\0'; ch = this.page.getCurChar()) {
            if (ch == '<') {
                if (this.parentTokenTag == null) {
                    this.parsedText = sb.toString();
                    break;
                }
                final String tagName = this.parentTokenTag.getTagName();
                if (tagName.equalsIgnoreCase("script")) {
                    final int startIndex = this.page.getCurrentCursorPosition() + 2;
                    final String strPreCon = this.page.getSubString(startIndex, startIndex + "/script".length());
                    if (strPreCon.indexOf("script") > -1 || strPreCon.indexOf("SCRIPT") > -1) {
                        this.parsedText = sb.toString();
                        break;
                    }
                    sb.append(ch);
                    this.page.getNextChar();
                }
            }
            else {
                sb.append(ch);
                this.page.getNextChar();
            }
        }
        final TokenTextImpl tti = new TokenTextImpl();
        tti.setValueText(this.parsedText);
        return tti;
    }
}
