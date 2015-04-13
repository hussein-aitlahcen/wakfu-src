package com.ankamagames.xulor2.nongraphical;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;

public class Data extends NonGraphicalElement
{
    public static final String TAG = "data";
    private Object m_value;
    public static final int VALUE_HASH;
    
    @Override
    public String getTag() {
        return "data";
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public void setValue(final Object value) {
        this.m_value = value;
    }
    
    @Override
    public Object getElementValue() {
        return this.m_value;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final Data d = (Data)source;
        super.copyElement(source);
        d.m_value = this.m_value;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Data.VALUE_HASH) {
            this.setValue(cl.convertToString(value, this.m_elementMap));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Data.VALUE_HASH) {
            this.setValue(value);
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        VALUE_HASH = "value".hashCode();
    }
}
