package com.ankamagames.xulor2.core;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.form.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;

public class Environment
{
    private static Logger m_logger;
    private final HashMap<String, ElementMap> m_elementMaps;
    private ElementMap m_currentElementMap;
    private HashMap<String, Form> m_forms;
    private ArrayList<Form> m_currentForms;
    
    public Environment() {
        super();
        this.m_elementMaps = new HashMap<String, ElementMap>();
        this.m_currentElementMap = null;
        this.m_forms = null;
        this.m_currentForms = null;
    }
    
    @Deprecated
    public PropertiesProvider getPropertiesProvider() {
        return PropertiesProvider.getInstance();
    }
    
    public void addElementMap(final ElementMap map, final String id) {
        this.m_elementMaps.put(id, map);
    }
    
    public ElementMap createElementMap(final String elementMapId) {
        final ElementMap elementMap = new ElementMap(elementMapId, this);
        this.addElementMap(elementMap, elementMapId);
        return elementMap;
    }
    
    public void changeElementMapName(final String oldId, final String newId) {
        if (oldId != null && newId != null) {
            final ElementMap map = this.m_elementMaps.remove(oldId);
            this.m_elementMaps.put(newId, map);
        }
    }
    
    public EventDispatcher getElement(final String path) {
        if (path == null) {
            return null;
        }
        final int endIndex = path.indexOf(46);
        final String mapId = (endIndex < 0) ? path : path.substring(0, endIndex);
        final ElementMap map = this.m_elementMaps.get(mapId);
        if (map != null) {
            return map.getElement(path.substring(endIndex + 1));
        }
        return null;
    }
    
    public void removeElementMap(final String elementMapId) {
        if (this.m_elementMaps != null) {
            final ElementMap map = this.m_elementMaps.remove(elementMapId);
            if (map == this.m_currentElementMap) {
                this.m_currentElementMap = null;
            }
            if (map != null) {
                map.clear();
            }
        }
    }
    
    public ElementMap getElementMap(final String id) {
        return this.m_elementMaps.get(id);
    }
    
    public ElementMap getCurrentElementMap() {
        return this.m_currentElementMap;
    }
    
    public void setCurrentElementMap(final ElementMap elementMap) {
        this.m_currentElementMap = elementMap;
    }
    
    public Form[] getCurrentForms() {
        if (this.m_currentForms == null) {
            return null;
        }
        final Form[] forms = new Form[this.m_currentForms.size()];
        return this.m_currentForms.toArray(forms);
    }
    
    public Form getCurrentForm() {
        if (this.m_currentForms != null) {
            return this.m_currentForms.get(this.m_currentForms.size() - 1);
        }
        return null;
    }
    
    public Form getForm(final String formId) {
        if (this.m_forms == null) {
            return null;
        }
        return this.m_forms.get(formId);
    }
    
    public Collection<Form> getForms() {
        if (this.m_forms == null) {
            return null;
        }
        return this.m_forms.values();
    }
    
    public void openForm(final String formId, final Form form) {
        if (this.m_forms == null) {
            this.m_forms = new HashMap<String, Form>();
            this.m_currentForms = new ArrayList<Form>();
        }
        this.m_forms.put(formId, form);
        this.m_currentForms.add(form);
    }
    
    public void closeForm(final String formId) {
        if (this.m_forms == null) {
            return;
        }
        final Form form = this.m_forms.get(formId);
        this.m_currentForms.remove(form);
    }
    
    public void removeForm(final String formId) {
        final Form form = this.m_forms.remove(formId);
        this.m_currentForms.remove(form);
    }
    
    public void removeForm(final Form form) {
        String key = null;
        for (final Map.Entry<String, Form> entry : this.m_forms.entrySet()) {
            if (entry.getValue() == form) {
                key = entry.getKey();
                break;
            }
        }
        this.m_forms.remove(key);
        this.m_currentForms.remove(form);
    }
    
    static {
        Environment.m_logger = Logger.getLogger((Class)Environment.class);
    }
}
