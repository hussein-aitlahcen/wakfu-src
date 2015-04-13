package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.xulor2.core.renderer.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;

public class ValueReplacer extends NonGraphicalElement implements ResultProvider
{
    public static final String TAG = "ValueReplacer";
    public static final String SIZE_KEY = "size";
    public static final String CONCAT_KEY = "concat";
    protected ResultProviderParent m_parent;
    private String m_key;
    private String m_value;
    public static final int VALUE_HASH;
    public static final int KEY_HASH;
    
    public ValueReplacer() {
        super();
        this.m_key = null;
        this.m_value = null;
    }
    
    @Override
    public String getTag() {
        return "ValueReplacer";
    }
    
    public String getKey() {
        return this.m_key;
    }
    
    public void setKey(final String key) {
        this.m_key = key;
    }
    
    public String getValue() {
        return this.m_value;
    }
    
    public void setValue(final String value) {
        this.m_value = value;
    }
    
    @Override
    public void setResultProviderParent(final ResultProviderParent parent) {
        this.m_parent = parent;
    }
    
    @Override
    public Object getResult(final Object object) {
        if (this.m_key == null) {
            return null;
        }
        if (this.m_key.equalsIgnoreCase("size")) {
            if (object instanceof Collection) {
                return ((Collection)object).size();
            }
            if (object instanceof Object[]) {
                return ((Object[])object).length;
            }
            return 0;
        }
        else {
            if (this.m_key.equalsIgnoreCase("concat")) {
                return this.m_value + ((object != null) ? object.toString() : "");
            }
            return null;
        }
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final ValueReplacer replacer = (ValueReplacer)source;
        replacer.setKey(this.m_key);
        replacer.setValue(this.m_value);
        super.copyElement(replacer);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ValueReplacer.VALUE_HASH) {
            this.setValue(cl.convertToString(value, this.m_elementMap));
        }
        else {
            if (hash != ValueReplacer.KEY_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setKey(cl.convertToString(value, this.m_elementMap));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ValueReplacer.VALUE_HASH) {
            this.setValue(String.valueOf(value));
        }
        else {
            if (hash != ValueReplacer.KEY_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setKey(String.valueOf(value));
        }
        return true;
    }
    
    static {
        VALUE_HASH = "value".hashCode();
        KEY_HASH = "key".hashCode();
    }
}
