package com.ankamagames.framework.fileFormat.html.parser.lexer.impl;

import com.ankamagames.framework.fileFormat.html.parser.*;
import com.ankamagames.framework.fileFormat.html.parser.datasource.*;
import java.util.*;

public class TokenTagImpl implements TokenTag
{
    private List<TagAttribute> attrs;
    private StringBuffer tagName;
    private StringBuffer rawHtml;
    private boolean isClosed;
    private boolean endClosed;
    private PageSource pageSource;
    private int startIndex;
    private int endIndex;
    private int tokenIndex;
    
    TokenTagImpl() {
        super();
        this.attrs = null;
        this.tagName = null;
        this.rawHtml = null;
        this.isClosed = false;
        this.endClosed = false;
        this.pageSource = null;
        this.startIndex = -1;
        this.endIndex = -1;
        this.tokenIndex = -1;
    }
    
    void initValue() {
        if (this.attrs == null) {
            this.attrs = new ArrayList<TagAttribute>();
        }
        this.attrs.clear();
        this.tagName = new StringBuffer("");
        this.rawHtml = new StringBuffer("");
        this.isClosed = false;
        this.endClosed = false;
        this.pageSource = null;
        this.startIndex = -1;
        this.endIndex = -1;
    }
    
    @Override
    public List<TagAttribute> getAttrs() {
        return this.attrs;
    }
    
    void setAttribute(final List<TagAttribute> attrs) {
        this.attrs = attrs;
    }
    
    void addAttribute(final TagAttribute attr) {
        if (this.attrs == null) {
            this.attrs = new ArrayList<TagAttribute>();
        }
        this.attrs.add(attr);
    }
    
    void addAttribute(final String attrName, final String attrValue) {
        if (this.attrs == null) {
            this.attrs = new ArrayList<TagAttribute>();
        }
        this.attrs.add(new TagAttribute(attrName, attrValue));
    }
    
    @Override
    public String getTagName() {
        return this.tagName.toString();
    }
    
    void setTagName(final String tagName) {
        this.tagName = new StringBuffer(tagName);
    }
    
    void appendTagName(final char ch) {
        this.tagName.append(ch);
    }
    
    @Override
    public boolean isClosedTag() {
        return this.isClosed;
    }
    
    void setCloseTag(final boolean closed) {
        this.isClosed = closed;
    }
    
    @Override
    public boolean isEndClosed() {
        return this.endClosed;
    }
    
    void setEndClosed(final boolean endClosed) {
        this.endClosed = endClosed;
    }
    
    @Override
    public int getEndPosition() {
        return this.endIndex;
    }
    
    void setEndPosition(final int position) {
        this.endIndex = position;
    }
    
    @Override
    public PageSource getPage() {
        return this.pageSource;
    }
    
    void setPageSource(final PageSource ps) {
        this.pageSource = ps;
    }
    
    @Override
    public int getStartPosition() {
        return this.startIndex;
    }
    
    void setStartPosition(final int position) {
        this.endIndex = position;
    }
    
    @Override
    public String toHtml() {
        String strRet = "<";
        if (this.isClosed) {
            strRet += "/";
        }
        strRet += (Object)this.tagName;
        if (this.attrs != null && this.attrs.size() > 0) {
            for (final TagAttribute attr : this.attrs) {
                strRet = strRet + " " + attr.getAttrName() + "=\"" + attr.getAttrValue() + "\"";
            }
        }
        if (this.endClosed) {
            strRet += "/";
        }
        return strRet + ">";
    }
    
    void setToHtml(final String rawHtml) {
        this.rawHtml = new StringBuffer(rawHtml);
    }
    
    @Override
    public String toString() {
        String retString = "[TAG]";
        if (this.isClosed) {
            retString += "/";
        }
        retString = retString + (Object)this.tagName + "(";
        if (this.attrs != null) {
            for (final TagAttribute tAttr : this.attrs) {
                retString = retString + tAttr.getAttrName() + ":" + tAttr.getAttrValue() + " ";
            }
        }
        retString = retString.trim();
        retString += ")";
        return retString;
    }
    
    void setIndex(final int idx) {
        this.tokenIndex = idx;
    }
    
    @Override
    public int getIndex() {
        return this.tokenIndex;
    }
}
