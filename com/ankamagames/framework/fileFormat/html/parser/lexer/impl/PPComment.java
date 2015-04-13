package com.ankamagames.framework.fileFormat.html.parser.lexer.impl;

import com.ankamagames.framework.fileFormat.html.parser.lexer.*;
import com.ankamagames.framework.fileFormat.html.parser.datasource.*;
import com.ankamagames.framework.fileFormat.html.parser.*;

public class PPComment implements TokenProcPlugin
{
    private PageSource page;
    private boolean isTag;
    
    public PPComment() {
        super();
        this.page = null;
        this.isTag = false;
    }
    
    public void setPageSource(final PageSource ps) {
        this.page = ps;
    }
    
    @Override
    public String getEntryString() {
        return null;
    }
    
    @Override
    public Token parse() throws CommonException {
        this.isTag = false;
        final StringBuffer sb = new StringBuffer();
        final StringBuffer sbx = new StringBuffer();
        boolean sqActive = false;
        boolean dqActive = false;
        char ch;
        while ((ch = this.page.getNextChar()) != '\uffff') {
            sbx.append(ch);
            if (ch == '<') {
                if (!sqActive && !dqActive) {
                    if (this.page.getCurChar() == '!') {
                        this.page.getNextChar();
                        if (this.page.getCurChar() == '-') {
                            while (this.page.getNextChar() == '-') {}
                        }
                        else {
                            this.isTag = true;
                        }
                    }
                    else {
                        sb.append(ch);
                    }
                }
                else {
                    sb.append(ch);
                }
            }
            else if (ch == '\'') {
                sqActive = !sqActive;
                sb.append(ch);
            }
            else if (ch == '\"') {
                dqActive = !dqActive;
                sb.append(ch);
            }
            else if (ch == '>') {
                if (!sqActive && !dqActive) {
                    if (this.isTag) {
                        break;
                    }
                    if (this.page.getChar(this.page.getCurrentCursorPosition() - 2) == '-') {
                        break;
                    }
                    sb.append(ch);
                }
                else {
                    sb.append(ch);
                }
            }
            else if (ch == '-') {
                if (sqActive || dqActive) {
                    sb.append(ch);
                }
                else {
                    int tempIndex = this.page.getCurrentCursorPosition();
                    int iCheck = 0;
                    final int maxCheck = 5;
                    while (this.page.getChar(tempIndex++) != '>' && iCheck++ < maxCheck) {}
                    if (iCheck < maxCheck) {
                        continue;
                    }
                    sb.append(ch);
                }
            }
            else {
                sb.append(ch);
            }
        }
        final TokenCommentImpl tci = new TokenCommentImpl();
        tci.setCommentText(sbx.toString());
        return tci;
    }
}
