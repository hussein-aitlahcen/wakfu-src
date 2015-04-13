package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class PropertyExistsCondition extends Condition implements PropertyEventListener
{
    public static final String TAG = "propertyExists";
    private boolean m_exists;
    private String m_name;
    private boolean m_local;
    public static final int LOCAL_HASH;
    public static final int NAME_HASH;
    
    @Override
    public String getTag() {
        return "propertyExists";
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public boolean getLocal() {
        return this.m_local;
    }
    
    public void setName(final String name) {
        if ((this.m_name != null && !this.m_name.equalsIgnoreCase(name)) || (name != null && !name.equalsIgnoreCase(this.m_name))) {
            this.m_name = name;
            this.setNeedsToPreProcess();
        }
    }
    
    public void setLocal(final boolean local) {
        if (this.m_local != local) {
            this.m_local = local;
            this.setNeedsToPreProcess();
        }
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        return this.m_exists;
    }
    
    @Override
    public boolean preProcess(final int delta) {
        final boolean ret = super.preProcess(delta);
        final boolean exists = this.m_exists;
        final Property p = PropertiesProvider.getInstance().getProperty(this.m_name, this.m_local ? this.getElementMap() : null);
        this.m_exists = (p != null && p.isValueInitialized());
        if (exists != this.m_exists) {
            this.fireConditionChanged(true);
        }
        return ret;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        final PropertyExistsCondition c = (PropertyExistsCondition)source;
        c.m_name = this.m_name;
        c.m_local = this.m_local;
    }
    
    @Override
    public void onPropertyEvent(final PropertyEvent event) {
        final Property p = event.getProperty();
        if (p != null && p.getName().equalsIgnoreCase(this.m_name) && (!this.m_local || p.getElementMap() == this.m_elementMap)) {
            switch (event.getType()) {
                case PROPERTY_INIT: {
                    this.fireConditionChanged(this.m_exists = true);
                    break;
                }
                case PROPERTY_REMOVED: {
                    this.m_exists = false;
                    this.fireConditionChanged(true);
                    break;
                }
            }
        }
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_exists = false;
        this.m_local = false;
        PropertiesProvider.getInstance().addPropertyEventListener(this);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        PropertiesProvider.getInstance().removePropertyEventListener(this);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == PropertyExistsCondition.LOCAL_HASH) {
            this.setLocal(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != PropertyExistsCondition.NAME_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setName(cl.convertToString(value, this.m_elementMap));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == PropertyExistsCondition.LOCAL_HASH) {
            this.setLocal(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != PropertyExistsCondition.NAME_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setName(String.valueOf(value));
        }
        return true;
    }
    
    static {
        LOCAL_HASH = "local".hashCode();
        NAME_HASH = "name".hashCode();
    }
}
