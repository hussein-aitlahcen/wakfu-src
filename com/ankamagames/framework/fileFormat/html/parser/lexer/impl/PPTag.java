package com.ankamagames.framework.fileFormat.html.parser.lexer.impl;

import com.ankamagames.framework.fileFormat.html.parser.lexer.*;
import com.ankamagames.framework.fileFormat.html.parser.datasource.*;
import com.ankamagames.framework.fileFormat.html.parser.*;

public class PPTag implements TokenProcPlugin
{
    private PageSource page;
    private ETagType cAttr;
    private TokenTagImpl tkWorking;
    private char ch;
    private String str_tagName;
    
    public PPTag() {
        super();
        this.page = null;
        this.cAttr = ETagType.NOT_PARSED;
        this.tkWorking = null;
        this.ch = '0';
        this.str_tagName = "";
    }
    
    public void setPageSource(final PageSource ps) {
        this.page = ps;
    }
    
    public void initValue() {
        this.cAttr = ETagType.NOT_PARSED;
        this.str_tagName = "";
        this.ch = '0';
    }
    
    @Override
    public String getEntryString() {
        return null;
    }
    
    @Override
    public TokenTag parse() throws CommonException {
        this.initValue();
        (this.tkWorking = new TokenTagImpl()).initValue();
        do {
            if (this.cAttr == ETagType.NOT_PARSED) {
                this.ch = this.page.getNextChar();
                if (this.ch != '<') {
                    continue;
                }
                this.cAttr = ETagType.TAG_START;
            }
            else if (this.cAttr == ETagType.TAG_START) {
                this.ch = this.page.getNextChar();
                this.parseTagName();
            }
            else {
                if (this.cAttr == ETagType.TAG_END) {
                    this.tkWorking.setTagName(this.str_tagName);
                    return this.tkWorking;
                }
                continue;
            }
        } while (this.cAttr != ETagType.TAG_END);
        this.tkWorking.setTagName(this.str_tagName);
        return this.tkWorking;
    }
    
    private void parseTagName() {
        char nextChar;
        do {
            if (this.cAttr == ETagType.TAG_START) {
                if (this.ch == '/') {
                    this.tkWorking.setCloseTag(true);
                }
                else if (Character.isLetterOrDigit(this.ch)) {
                    this.cAttr = ETagType.TAG_NAME;
                    this.str_tagName += this.ch;
                }
            }
            else if (this.cAttr == ETagType.TAG_NAME) {
                if (Character.isLetterOrDigit(this.ch)) {
                    this.cAttr = ETagType.TAG_NAME;
                    this.str_tagName += this.ch;
                }
                else if (this.str_tagName.length() > 0) {
                    if (this.ch == ' ') {
                        this.cAttr = ETagType.TAG_NAME_END;
                        this.parseAttrName();
                        break;
                    }
                    if (this.ch == '>') {
                        this.cAttr = ETagType.TAG_END;
                        break;
                    }
                    if (this.ch == '/') {
                        this.tkWorking.setCloseTag(true);
                    }
                }
            }
            nextChar = this.page.getNextChar();
            this.ch = nextChar;
        } while (nextChar != '\uffff' && this.cAttr != ETagType.TAG_NAME_END && this.cAttr != ETagType.TAG_END);
    }
    
    private void parseAttrName() {
        String strAttrName = "";
        while (this.cAttr != ETagType.TAG_END && (this.ch = this.page.getCurChar()) != '\uffff') {
            if (this.cAttr == ETagType.TAG_NAME_END) {
                this.page.getNextChar();
                if (Character.isLetterOrDigit(this.ch)) {
                    strAttrName += this.ch;
                    this.cAttr = ETagType.ATTR_NAME;
                }
                else {
                    if (this.ch == ' ') {
                        continue;
                    }
                    if (this.ch == '/') {
                        this.tkWorking.setEndClosed(true);
                    }
                    else {
                        if (this.ch == '>') {
                            this.cAttr = ETagType.TAG_END;
                            return;
                        }
                        continue;
                    }
                }
            }
            else if (this.cAttr == ETagType.ATTR_NAME) {
                this.page.getNextChar();
                if (Character.isLetterOrDigit(this.ch)) {
                    strAttrName += this.ch;
                }
                else if (this.ch == ' ') {
                    this.cAttr = ETagType.ATTR_NAME_END;
                }
                else if (this.ch == '=') {
                    this.cAttr = ETagType.ATTR_NAME_END_HAVE_VALUE;
                    this.tkWorking.addAttribute(strAttrName, this.parseAttrValue());
                }
                else {
                    if (this.ch == '>') {
                        this.cAttr = ETagType.TAG_END;
                        this.tkWorking.addAttribute(strAttrName, null);
                        return;
                    }
                    continue;
                }
            }
            else if (this.cAttr == ETagType.ATTR_VALUE_END) {
                if (Character.isLetterOrDigit(this.ch)) {
                    this.cAttr = ETagType.ATTR_NAME;
                    this.parseAttrName();
                }
                else {
                    if (this.ch == '>') {
                        this.page.getNextChar();
                        this.cAttr = ETagType.TAG_END;
                        return;
                    }
                    this.page.getNextChar();
                }
            }
            else {
                if (this.cAttr != ETagType.ATTR_NAME_END) {
                    continue;
                }
                if (this.ch == '=') {
                    this.page.getNextChar();
                    this.cAttr = ETagType.ATTR_NAME_END_HAVE_VALUE;
                    this.tkWorking.addAttribute(strAttrName, this.parseAttrValue());
                }
                else {
                    if (this.ch == '>') {
                        this.page.getNextChar();
                        this.cAttr = ETagType.TAG_END;
                        this.tkWorking.addAttribute(strAttrName, null);
                        return;
                    }
                    if (Character.isLetterOrDigit(this.ch)) {
                        this.tkWorking.addAttribute(strAttrName, null);
                        this.cAttr = ETagType.ATTR_NAME;
                        this.parseAttrName();
                    }
                    else {
                        this.page.getNextChar();
                    }
                }
            }
        }
    }
    
    private String parseAttrValue() {
        boolean isSqOpen = false;
        boolean isDqOpen = false;
        final StringBuffer sb = new StringBuffer();
        while ((this.ch = this.page.getNextChar()) != '\uffff') {
            if (this.cAttr == ETagType.ATTR_NAME_END_HAVE_VALUE) {
                if (this.ch == '\'') {
                    isSqOpen = true;
                    this.cAttr = ETagType.ATTR_VALUE;
                }
                else if (this.ch == '\"') {
                    isDqOpen = true;
                    this.cAttr = ETagType.ATTR_VALUE;
                }
                else {
                    if (this.ch == ' ') {
                        continue;
                    }
                    sb.append(this.ch);
                    this.cAttr = ETagType.ATTR_VALUE;
                }
            }
            else {
                if (this.cAttr != ETagType.ATTR_VALUE) {
                    continue;
                }
                if (this.ch == ' ') {
                    if (!isSqOpen && !isDqOpen) {
                        this.cAttr = ETagType.ATTR_VALUE_END;
                        return sb.toString();
                    }
                    sb.append(this.ch);
                }
                else if (this.ch == '\'') {
                    if (isSqOpen) {
                        this.cAttr = ETagType.ATTR_VALUE_END;
                        return sb.toString();
                    }
                    sb.append(this.ch);
                }
                else if (this.ch == '\"') {
                    if (isDqOpen) {
                        this.cAttr = ETagType.ATTR_VALUE_END;
                        return sb.toString();
                    }
                    sb.append(this.ch);
                }
                else {
                    if (this.ch == '>') {
                        this.cAttr = ETagType.TAG_END;
                        return sb.toString();
                    }
                    sb.append(this.ch);
                }
            }
        }
        return sb.toString();
    }
    
    public enum ETagType
    {
        NOT_PARSED, 
        TAG_START, 
        CLOSED_TAG, 
        TAG_NAME, 
        TAG_NAME_END, 
        ATTR_NAME, 
        ATTR_NAME_END, 
        ATTR_NAME_END_HAVE_VALUE, 
        ATTR_VALUE, 
        ATTR_VALUE_END, 
        TAG_END;
    }
}
