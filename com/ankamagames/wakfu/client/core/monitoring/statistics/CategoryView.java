package com.ankamagames.wakfu.client.core.monitoring.statistics;

import com.ankamagames.framework.reflect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;

public final class CategoryView implements FieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String ELEMENTS_FIELD = "elements";
    public static final String[] FIELDS;
    private final String m_name;
    private final List<ElementView> m_elements;
    private final ArrayList<String> m_requests;
    
    public CategoryView(final String name) {
        super();
        this.m_elements = new ArrayList<ElementView>();
        this.m_requests = new ArrayList<String>();
        this.m_name = name;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public List<ElementView> getElements() {
        return this.m_elements;
    }
    
    public void addRequest(final String request) {
        this.m_requests.add(request);
    }
    
    public ArrayList<String> getRequests() {
        return this.m_requests;
    }
    
    public void addElement(final ElementView elem) {
        this.m_elements.add(elem);
    }
    
    @Override
    public String[] getFields() {
        return CategoryView.FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("elements")) {
            return this.m_elements;
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public void update() {
        for (final ElementView elem : this.m_elements) {
            elem.update();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "elements", "name");
    }
    
    static {
        FIELDS = new String[] { "name", "elements" };
    }
}
