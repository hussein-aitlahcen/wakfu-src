package com.ankamagames.wakfu.client.core.monitoring.statistics;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.property.*;

public abstract class ElementView implements FieldProvider
{
    public static final String TYPE_FIELD = "type";
    public static final String[] FIELDS;
    protected Logger m_logger;
    
    public ElementView() {
        super();
        this.m_logger = Logger.getLogger((Class)ElementView.class);
    }
    
    public abstract ELEMENT_TYPE getType();
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("type")) {
            return this.getType().ordinal();
        }
        return null;
    }
    
    public void update() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, this.getFields());
    }
    
    static {
        FIELDS = new String[] { "type" };
    }
    
    public enum ELEMENT_TYPE
    {
        SIMPLE_ELEMENT, 
        TAB_ELEMENT;
    }
}
