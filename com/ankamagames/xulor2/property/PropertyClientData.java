package com.ankamagames.xulor2.property;

import java.lang.reflect.*;
import com.ankamagames.xulor2.core.factory.*;
import com.ankamagames.xulor2.core.renderer.*;

public class PropertyClientData<T>
{
    private static final String CONTENT_KEY = "content";
    private T m_element;
    private Method m_setter;
    private Method m_appender;
    private Method m_prepender;
    private Factory<?> m_factory;
    private String m_attribute;
    private int m_attributeHash;
    private String m_fieldName;
    private ResultProvider m_resultProvider;
    private boolean m_modified;
    private Property.AsynchronousPropertyChange m_propertyChange;
    
    public PropertyClientData(final T element, final Factory<?> factory, final String attribute, final String propertyField, final ResultProvider result) {
        super();
        this.m_fieldName = null;
        this.m_resultProvider = null;
        this.m_modified = false;
        this.m_propertyChange = null;
        this.m_element = element;
        this.m_factory = factory;
        this.m_attribute = attribute;
        this.m_attributeHash = ((attribute != null) ? attribute.hashCode() : 0);
        this.m_setter = factory.guessSetter(attribute);
        this.m_appender = factory.guessAppender(attribute);
        this.m_prepender = factory.guessPrepender(attribute);
        this.m_fieldName = propertyField;
        this.m_resultProvider = result;
    }
    
    public PropertyClientData(final T element, final Factory<?> factory, final String attribute, final ResultProvider result) {
        super();
        this.m_fieldName = null;
        this.m_resultProvider = null;
        this.m_modified = false;
        this.m_propertyChange = null;
        this.m_element = element;
        this.m_factory = factory;
        this.m_attribute = attribute;
        this.m_attributeHash = ((attribute != null) ? attribute.hashCode() : 0);
        this.m_resultProvider = result;
    }
    
    public int getAttributeHash() {
        return this.m_attributeHash;
    }
    
    public String getAttribute() {
        return this.m_attribute;
    }
    
    public void setAttribute(final String attribute) {
        this.m_attribute = attribute;
        this.m_attributeHash = ((this.m_attribute != null) ? attribute.hashCode() : 0);
    }
    
    public T getElement() {
        return this.m_element;
    }
    
    public void setElement(final T element) {
        this.m_element = element;
    }
    
    public Factory<?> getFactory() {
        return this.m_factory;
    }
    
    public void setFactory(final Factory<?> factory) {
        this.m_factory = factory;
    }
    
    public String getFieldName() {
        return this.m_fieldName;
    }
    
    public void setFieldName(final String propertyField) {
        this.m_fieldName = propertyField;
    }
    
    public ResultProvider getResultProvider() {
        return this.m_resultProvider;
    }
    
    public void setResultProvider(final ResultProvider result) {
        this.m_resultProvider = result;
    }
    
    public boolean getContentProperty() {
        return this.m_element instanceof ContentClient && "content".equalsIgnoreCase(this.m_attribute);
    }
    
    public Method getSetter() {
        return this.m_setter;
    }
    
    public Method getAppender() {
        return this.m_appender;
    }
    
    public Method getPrepender() {
        return this.m_prepender;
    }
    
    public boolean isModified() {
        return this.m_modified;
    }
    
    public void setModified(final boolean modified) {
        this.m_modified = modified;
    }
    
    Property.AsynchronousPropertyChange getPropertyChange() {
        return this.m_propertyChange;
    }
    
    void setPropertyChange(final Property.AsynchronousPropertyChange propertyChange) {
        this.m_propertyChange = propertyChange;
    }
    
    @Override
    public String toString() {
        return "(PropertyClientData Element:" + this.m_element + " attribute=" + this.m_attribute + " field=" + this.m_fieldName + ")";
    }
}
