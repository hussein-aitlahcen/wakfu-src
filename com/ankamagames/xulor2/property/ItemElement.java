package com.ankamagames.xulor2.property;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;

public class ItemElement extends NonGraphicalElement
{
    private static Logger m_logger;
    public static final String TAG = "Item";
    private String m_field;
    private String m_attribute;
    private int m_attributeHash;
    private ResultProvider m_resultProvider;
    public static final int ATTRIBUTE_HASH;
    public static final int FIELD_HASH;
    
    public ItemElement() {
        super();
        this.m_field = null;
        this.m_attribute = null;
        this.m_attributeHash = 0;
        this.m_resultProvider = null;
    }
    
    @Override
    public void add(final EventDispatcher element) {
        if (element instanceof ResultProvider) {
            this.m_resultProvider = (ResultProvider)element;
        }
        super.add(element);
    }
    
    @Override
    public String getTag() {
        return "Item";
    }
    
    public String getField() {
        return this.m_field;
    }
    
    public void setField(final String field) {
        this.m_field = field;
    }
    
    public int getAttributeHash() {
        return this.m_attributeHash;
    }
    
    public String getAttribute() {
        return this.m_attribute;
    }
    
    public void setAttribute(final String attribute) {
        this.m_attribute = attribute;
        this.m_attributeHash = ((this.m_attribute != null) ? this.m_attribute.hashCode() : 0);
    }
    
    public ResultProvider getResultProvider() {
        return this.m_resultProvider;
    }
    
    public void setCondition(final ResultProvider condition) {
        this.m_resultProvider = condition;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_field = null;
        this.m_attribute = null;
        this.m_resultProvider = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_attributeHash = 0;
    }
    
    @Override
    public void copyElement(final BasicElement i) {
        final ItemElement e = (ItemElement)i;
        super.copyElement(e);
        e.m_attribute = this.m_attribute;
        e.m_attributeHash = this.m_attributeHash;
        e.m_field = this.m_field;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ItemElement.ATTRIBUTE_HASH) {
            this.setAttribute(cl.convertToString(value, this.m_elementMap));
        }
        else {
            if (hash != ItemElement.FIELD_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setField(cl.convertToString(value, this.m_elementMap));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ItemElement.ATTRIBUTE_HASH) {
            this.setAttribute(String.valueOf(value));
        }
        else {
            if (hash != ItemElement.FIELD_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setField(String.valueOf(value));
        }
        return true;
    }
    
    static {
        ItemElement.m_logger = Logger.getLogger((Class)ItemElement.class);
        ATTRIBUTE_HASH = "attribute".hashCode();
        FIELD_HASH = "field".hashCode();
    }
}
