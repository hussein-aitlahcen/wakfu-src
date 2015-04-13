package com.ankamagames.xulor2.property;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import gnu.trove.*;

public class PropertiesProvider implements com.ankamagames.framework.reflect.PropertiesProvider
{
    private static final Logger m_logger;
    private static final PropertiesProvider m_instance;
    private final HashMap<String, Property> m_globalProperties;
    private final ArrayList<Property> m_properties;
    private final ArrayList<Property> m_needProcess;
    private final ArrayList<Property> m_toAddToProcess;
    private final ArrayList<PropertyEventListener> m_listeners;
    
    private PropertiesProvider() {
        super();
        this.m_globalProperties = new HashMap<String, Property>();
        this.m_properties = new ArrayList<Property>();
        this.m_needProcess = new ArrayList<Property>();
        this.m_toAddToProcess = new ArrayList<Property>();
        this.m_listeners = new ArrayList<PropertyEventListener>();
    }
    
    public static PropertiesProvider getInstance() {
        return PropertiesProvider.m_instance;
    }
    
    public void addPropertyEventListener(final PropertyEventListener listener) {
        if (listener != null) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removePropertyEventListener(final PropertyEventListener listener) {
        if (listener != null) {
            this.m_listeners.remove(listener);
        }
    }
    
    public void addProperty(final Property property) {
        if (!property.isLocal()) {
            this.m_globalProperties.put(property.getName(), property);
        }
        synchronized (this.m_properties) {
            this.m_properties.add(property);
        }
        this.dispatchEvent(PropertyEvent.PropertyEventType.PROPERTY_ADDED, property);
    }
    
    public boolean onProcess() {
        final int num = this.m_toAddToProcess.size();
        if (num == 0) {
            return false;
        }
        synchronized (this.m_toAddToProcess) {
            this.m_needProcess.addAll(this.m_toAddToProcess);
            this.m_toAddToProcess.clear();
        }
        for (int i = 0; i < num; ++i) {
            this.m_needProcess.get(i).onProcess();
        }
        this.m_needProcess.clear();
        return true;
    }
    
    public void addToProcessList(final Property property) {
        synchronized (this.m_toAddToProcess) {
            if (property != null && !this.m_toAddToProcess.contains(property)) {
                this.m_toAddToProcess.add(property);
            }
        }
    }
    
    public void removeProperty(final String name) {
        this.removeProperty(this.m_globalProperties.get(name));
    }
    
    public void removeProperty(final Property p) {
        if (p == null) {
            return;
        }
        if (!p.isLocal()) {
            this.m_globalProperties.remove(p.getName());
        }
        synchronized (this.m_properties) {
            this.m_properties.remove(p);
        }
        this.dispatchEvent(PropertyEvent.PropertyEventType.PROPERTY_REMOVED, p);
    }
    
    @Override
    public void setPropertyValue(final String name, final Object value) {
        this.setPropertyValue(name, value, false);
    }
    
    public void setPropertyValue(final String name, final Object value, final boolean force) {
        Property property = this.m_globalProperties.get(name);
        if (property == null) {
            property = new Property(name, null);
            this.addProperty(property);
        }
        property.setValue(value, force);
    }
    
    public void setLocalPropertyValue(final String name, final Object value, final String elementMapId) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(elementMapId);
        if (map != null) {
            this.setLocalPropertyValue(name, value, map);
        }
    }
    
    public void setLocalPropertyValue(final String name, final Object value, final ElementMap elementMap) {
        this.setLocalPropertyValue(name, value, elementMap, false);
    }
    
    public void setLocalPropertyValue(final String name, final Object value, final ElementMap elementMap, final boolean force) {
        if (elementMap == null) {
            this.setPropertyValue(name, value);
        }
        else {
            Property property = elementMap.getProperty(name);
            if (property == null) {
                property = new Property(name, elementMap);
                this.addProperty(property);
                elementMap.addProperty(property);
            }
            property.setValue(value, force);
        }
    }
    
    public void prependPropertyValue(final String name, final Object value) {
        final Property property = this.m_globalProperties.get(name);
        if (property == null) {
            this.setPropertyValue(name, value);
        }
        else {
            property.prependValue(value);
        }
    }
    
    public void prependLocalPropertyValue(final String name, final Object value, final String elementMapId) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(elementMapId);
        this.prependLocalPropertyValue(name, value, map);
    }
    
    public void prependLocalPropertyValue(final String name, final Object value, final ElementMap elementMap) {
        if (elementMap == null) {
            this.prependPropertyValue(name, value);
        }
        else {
            final Property property = elementMap.getProperty(name);
            if (property == null) {
                this.setLocalPropertyValue(name, value, elementMap);
            }
            else {
                property.prependValue(value);
            }
        }
    }
    
    public void appendPropertyValue(final String name, final Object value) {
        final Property property = this.m_globalProperties.get(name);
        if (property == null) {
            this.setPropertyValue(name, value);
        }
        else {
            property.appendValue(value);
        }
    }
    
    public void appendLocalPropertyValue(final String name, final Object value, final String elementMapId) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(elementMapId);
        this.appendLocalPropertyValue(name, value, map);
    }
    
    public void appendLocalPropertyValue(final String name, final Object value, final ElementMap elementMap) {
        if (elementMap == null) {
            this.appendPropertyValue(name, value);
        }
        else {
            final Property property = elementMap.getProperty(name);
            if (property == null) {
                this.setLocalPropertyValue(name, value, elementMap);
            }
            else {
                property.appendValue(value);
            }
        }
    }
    
    public void setPropertyValue(final String name, final String field, final Object value) {
        final Property property = this.m_globalProperties.get(name);
        if (property != null) {
            property.setFieldValue(field, value);
        }
        else {
            PropertiesProvider.m_logger.error((Object)("La d\u00e9finition d'une valeur de champ est impossible sur la propri\u00e9t\u00e9 " + name));
        }
    }
    
    public void setLocalPropertyValue(final String name, final String field, final Object value, final String elementMapId) {
        this.setLocalPropertyValue(name, field, value, Xulor.getInstance().getEnvironment().getElementMap(elementMapId));
    }
    
    public void setLocalPropertyValue(final String name, final String field, final Object value, final ElementMap elementMap) {
        if (elementMap == null) {
            this.setPropertyValue(name, field, value);
        }
        else {
            final Property property = elementMap.getProperty(name);
            if (property != null) {
                property.setFieldValue(field, value);
            }
            else {
                PropertiesProvider.m_logger.error((Object)("La d\u00e9finition d'une valeur de champ est impossible sur la propri\u00e9t\u00e9 " + name));
            }
        }
    }
    
    public void prependPropertyValue(final String name, final String field, final Object value) {
        final Property property = this.m_globalProperties.get(name);
        if (property == null) {
            this.setPropertyValue(name, value);
        }
        else {
            property.prependValue(value);
        }
    }
    
    public void prependLocalPropertyValue(final String name, final String field, final Object value, final String elementMapId) {
        this.prependLocalPropertyValue(name, field, value, Xulor.getInstance().getEnvironment().getElementMap(elementMapId));
    }
    
    public void prependLocalPropertyValue(final String name, final String field, final Object value, final ElementMap elementMap) {
        if (elementMap == null) {
            this.prependPropertyValue(name, field, value);
        }
        else {
            final Property property = elementMap.getProperty(name);
            if (property != null) {
                property.prependFieldValue(field, value);
            }
            else {
                PropertiesProvider.m_logger.error((Object)("La d\u00e9finition d'une valeur de champ est impossible sur la propri\u00e9t\u00e9 " + name));
            }
        }
    }
    
    public void appendPropertyValue(final String name, final String field, final Object value) {
        final Property property = this.m_globalProperties.get(name);
        if (property != null) {
            property.appendFieldValue(field, value);
        }
        else {
            PropertiesProvider.m_logger.error((Object)("La d\u00e9finition d'une valeur de champ est impossible sur la propri\u00e9t\u00e9 " + name));
        }
    }
    
    public void appendLocalPropertyValue(final String name, final String field, final Object value, final String elementMapId) {
        this.appendLocalPropertyValue(name, field, value, Xulor.getInstance().getEnvironment().getElementMap(elementMapId));
    }
    
    public void appendLocalPropertyValue(final String name, final String field, final Object value, final ElementMap elementMap) {
        if (elementMap == null) {
            this.appendPropertyValue(name, field, value);
        }
        else {
            final Property property = elementMap.getProperty(name);
            if (property != null) {
                property.appendFieldValue(field, value);
            }
            else {
                PropertiesProvider.m_logger.error((Object)("La d\u00e9finition d'une valeur de champ est impossible sur la propri\u00e9t\u00e9 " + name));
            }
        }
    }
    
    @Override
    public void firePropertyValueChanged(final FieldProvider fieldProvider, final String... fields) {
        final IntObjectLightWeightMap valueMap = new IntObjectLightWeightMap();
        synchronized (this.m_properties) {
            for (int size = this.m_properties.size(), i = 0; i < size; ++i) {
                final Property property = this.m_properties.get(i);
                final Object value = property.getValue();
                if (value != null && value.equals(fieldProvider)) {
                    property.fireFieldValueChanged(fields, valueMap);
                }
            }
        }
    }
    
    public void firePropertyValueChanged(final String name, final String field) {
        final Property property = this.m_globalProperties.get(name);
        if (property != null) {
            property.fireFieldValueChanged(field);
        }
        else {
            PropertiesProvider.m_logger.error((Object)("La d\u00e9finition d'une valeur de champ " + field + " est impossible sur la propri\u00e9t\u00e9 " + name));
        }
    }
    
    public void firePropertyValueChanged(final String name, final String field, final ElementMap elementMap) {
        if (elementMap == null) {
            this.firePropertyValueChanged(name, field);
        }
        else {
            final Property property = elementMap.getProperty(name);
            if (property != null) {
                property.fireFieldValueChanged(field);
            }
            else {
                PropertiesProvider.m_logger.error((Object)("La d\u00e9finition d'une valeur de champ est impossible sur la propri\u00e9t\u00e9 " + name));
            }
        }
    }
    
    public String getStringProperty(final String name) {
        final Property prop = this.m_globalProperties.get(name);
        if (prop != null) {
            return prop.getString();
        }
        return null;
    }
    
    public String getStringProperty(final String name, final String elementMapId) {
        return this.getStringProperty(name, Xulor.getInstance().getEnvironment().getElementMap(elementMapId));
    }
    
    public String getStringProperty(final String name, final ElementMap elementMap) {
        Property prop;
        if (elementMap == null) {
            prop = this.m_globalProperties.get(name);
        }
        else {
            prop = elementMap.getProperty(name);
        }
        if (prop != null) {
            return prop.getString();
        }
        return null;
    }
    
    public boolean getBooleanProperty(final String name) {
        final Property prop = this.m_globalProperties.get(name);
        return prop != null && prop.getBoolean();
    }
    
    public boolean getBooleanProperty(final String name, final String elementMapId) {
        return this.getBooleanProperty(name, Xulor.getInstance().getEnvironment().getElementMap(elementMapId));
    }
    
    public boolean getBooleanProperty(final String name, final ElementMap elementMap) {
        Property prop;
        if (elementMap == null) {
            prop = this.m_globalProperties.get(name);
        }
        else {
            prop = elementMap.getProperty(name);
        }
        return prop != null && prop.getBoolean();
    }
    
    public int getIntProperty(final String name) {
        final Property prop = this.m_globalProperties.get(name);
        return (prop != null) ? prop.getInt() : 0;
    }
    
    public int getIntProperty(final String name, final String elementMapId) {
        return this.getIntProperty(name, Xulor.getInstance().getEnvironment().getElementMap(elementMapId));
    }
    
    public int getIntProperty(final String name, final ElementMap elementMap) {
        Property prop;
        if (elementMap == null) {
            prop = this.m_globalProperties.get(name);
        }
        else {
            prop = elementMap.getProperty(name);
        }
        return (prop != null) ? prop.getInt() : 0;
    }
    
    public float getFloatProperty(final String name) {
        final Property prop = this.m_globalProperties.get(name);
        return (prop != null) ? prop.getFloat() : 0.0f;
    }
    
    public float getFloatProperty(final String name, final String elementMapId) {
        return this.getFloatProperty(name, Xulor.getInstance().getEnvironment().getElementMap(elementMapId));
    }
    
    public float getFloatProperty(final String name, final ElementMap elementMap) {
        Property prop;
        if (elementMap == null) {
            prop = this.m_globalProperties.get(name);
        }
        else {
            prop = elementMap.getProperty(name);
        }
        return (prop != null) ? prop.getFloat() : 0.0f;
    }
    
    public double getDoubleProperty(final String name) {
        final Property prop = this.m_globalProperties.get(name);
        return (prop != null) ? prop.getDouble() : 0.0;
    }
    
    public double getDoubleProperty(final String name, final String elementMapId) {
        return this.getDoubleProperty(name, Xulor.getInstance().getEnvironment().getElementMap(elementMapId));
    }
    
    public double getDoubleProperty(final String name, final ElementMap elementMap) {
        Property prop;
        if (elementMap == null) {
            prop = this.m_globalProperties.get(name);
        }
        else {
            prop = elementMap.getProperty(name);
        }
        if (prop != null) {
            return prop.getDouble();
        }
        return 0.0;
    }
    
    public long getLongProperty(final String name) {
        final Property prop = this.m_globalProperties.get(name);
        if (prop != null) {
            return prop.getLong();
        }
        return 0L;
    }
    
    public long getLongProperty(final String name, final String elementMapId) {
        return this.getLongProperty(name, Xulor.getInstance().getEnvironment().getElementMap(elementMapId));
    }
    
    public long getLongProperty(final String name, final ElementMap elementMap) {
        Property prop;
        if (elementMap == null) {
            prop = this.m_globalProperties.get(name);
        }
        else {
            prop = elementMap.getProperty(name);
        }
        if (prop != null) {
            return prop.getLong();
        }
        return 0L;
    }
    
    public Object getObjectProperty(final String name) {
        final Property prop = this.m_globalProperties.get(name);
        if (prop != null) {
            return prop.getValue();
        }
        return null;
    }
    
    public Object getObjectProperty(final String name, final String elementMapId) {
        return this.getObjectProperty(name, Xulor.getInstance().getEnvironment().getElementMap(elementMapId));
    }
    
    public Object getObjectProperty(final String name, final ElementMap elementMap) {
        Property prop;
        if (elementMap == null) {
            prop = this.m_globalProperties.get(name);
        }
        else {
            prop = elementMap.getProperty(name);
        }
        if (prop != null) {
            return prop.getValue();
        }
        return null;
    }
    
    public Property getProperty(final String name) {
        return this.m_globalProperties.get(name);
    }
    
    public Property getProperty(final String name, final String elementMapId) {
        return this.getProperty(name, Xulor.getInstance().getEnvironment().getElementMap(elementMapId));
    }
    
    public Property getProperty(final FieldProvider fp, final ElementMap elementMap) {
        synchronized (this.m_properties) {
            for (int i = 0, m_propertiesSize = this.m_properties.size(); i < m_propertiesSize; ++i) {
                final Property p = this.m_properties.get(i);
                if (p.getValue() == fp && p.getElementMap() == elementMap) {
                    return p;
                }
            }
        }
        return null;
    }
    
    public Property getProperty(final String name, final ElementMap elementMap) {
        if (elementMap == null) {
            return this.getProperty(name);
        }
        return elementMap.getProperty(name);
    }
    
    public void dispatchEvent(final PropertyEvent.PropertyEventType type, final Property p) {
        if (this.m_listeners.size() != 0) {
            final PropertyEvent pe = new PropertyEvent(type, p);
            for (int i = this.m_listeners.size() - 1; i >= 0; --i) {
                this.m_listeners.get(i).onPropertyEvent(pe);
            }
        }
    }
    
    public void foreachProperty(final TObjectProcedure<Property> procedure) {
        synchronized (this.m_properties) {
            for (int i = 0, m_propertiesSize = this.m_properties.size(); i < m_propertiesSize; ++i) {
                procedure.execute(this.m_properties.get(i));
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PropertiesProvider.class);
        m_instance = new PropertiesProvider();
    }
}
