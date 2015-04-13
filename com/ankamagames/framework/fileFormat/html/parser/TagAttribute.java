package com.ankamagames.framework.fileFormat.html.parser;

public class TagAttribute
{
    private String attrName;
    private String attrValue;
    
    public TagAttribute(final String attrName, final String attrValue) {
        super();
        this.attrName = null;
        this.attrValue = null;
        this.attrName = attrName;
        this.attrValue = attrValue;
    }
    
    public String getAttrValue() {
        return this.attrValue;
    }
    
    public void setAttrValue(final String attrValue) {
        this.attrValue = attrValue;
    }
    
    public String getAttrName() {
        return this.attrName;
    }
}
