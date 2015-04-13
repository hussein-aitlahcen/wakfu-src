package com.ankamagames.xulor2.core;

import org.apache.log4j.*;
import com.ankamagames.xulor2.nongraphical.*;
import gnu.trove.*;
import org.apache.commons.lang3.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;

public class ElementMap implements Iterable<EventDispatcher>
{
    private static final Logger m_logger;
    public static final char ELEMENT_MAP_SEPARATOR = '.';
    public static final String SUB_ELEMENT_SEPARATOR = "~";
    private static final String PREFIXED_ELEMENT_MAP_SEPARATOR;
    public static final String INDEX_SEPARATOR = "#";
    private String m_id;
    private HashMap<String, EventDispatcher> m_elements;
    private final HashMap<String, Property> m_localProperties;
    private Environment m_environment;
    private HashMap<String, RadioGroup> m_radiogroups;
    private ElementMap m_parent;
    private ArrayList<ElementMap> m_children;
    private THashMap<String, String> m_environmentProperties;
    
    public ElementMap(final String id, final Environment env) {
        super();
        this.m_localProperties = new HashMap<String, Property>();
        this.m_radiogroups = null;
        this.m_parent = null;
        this.m_children = null;
        this.m_environmentProperties = new THashMap<String, String>();
        this.m_id = id;
        this.m_environment = env;
    }
    
    public void add(final String id, final EventDispatcher element) {
        if (this.m_elements == null) {
            this.m_elements = new HashMap<String, EventDispatcher>();
        }
        if (element != null && id != null && this.m_elements.get(id) != element) {
            element.setId(id);
            this.m_elements.put(id, element);
        }
    }
    
    public EventDispatcher getElement(final String id) {
        String[] strings = StringUtils.split(id, ElementMap.PREFIXED_ELEMENT_MAP_SEPARATOR, 2);
        String elementId = id;
        String subElement = null;
        String subElementMapId = null;
        int elementIndex = -1;
        if (strings.length >= 1) {
            elementId = strings[0];
        }
        if (strings.length >= 2) {
            subElementMapId = strings[1];
        }
        strings = StringUtils.split(elementId, "#", 2);
        if (strings.length >= 1) {
            elementId = strings[0];
        }
        if (strings.length >= 2) {
            elementIndex = PrimitiveConverter.getInteger(strings[1], -1);
        }
        strings = StringUtils.split(elementId, "~", 2);
        if (strings.length >= 1) {
            elementId = strings[0];
        }
        if (strings.length >= 2) {
            subElement = strings[1];
        }
        EventDispatcher element = (this.m_elements != null) ? this.m_elements.get(elementId) : null;
        if (element == null && this.m_parent != null) {
            element = this.m_parent.getElement(elementId);
        }
        if (element instanceof RenderableCollection && elementIndex != -1) {
            element = ((RenderableCollection)element).getWidget(subElement, elementIndex);
        }
        if (subElementMapId != null && element instanceof RenderableContainer) {
            final ElementMap innerMap = ((RenderableContainer)element).getInnerElementMap();
            if (innerMap != null) {
                element = innerMap.getElement(subElementMapId);
            }
        }
        return element;
    }
    
    public Environment getEnvironment() {
        return this.m_environment;
    }
    
    public void setEnvironment(final Environment environment) {
        if (environment != this.m_environment) {
            this.m_environment = environment;
            for (int i = this.m_children.size() - 1; i >= 0; --i) {
                this.m_children.get(i).setEnvironment(this.m_environment);
            }
        }
    }
    
    public String getRootId() {
        if (this.m_parent != null) {
            return this.m_parent.getRootId();
        }
        return this.m_id;
    }
    
    public String getId() {
        return this.m_id;
    }
    
    public boolean setId(final String id) {
        if (id == null) {
            return false;
        }
        if (id.equalsIgnoreCase(this.m_id) || Xulor.getInstance().changeLoadId(this.m_id, id)) {
            this.smallIdChange(id);
            return true;
        }
        return false;
    }
    
    private void smallIdChange(final String id) {
        final String old = this.m_id;
        this.m_environment.changeElementMapName(this.m_id, id);
        this.m_id = id;
        if (this.m_children != null) {
            for (int i = this.m_children.size() - 1; i >= 0; --i) {
                final ElementMap child = this.m_children.get(i);
                if (child.getId() != null) {
                    final String newId = this.m_id + child.getId().substring(old.length());
                    child.smallIdChange(newId);
                }
            }
        }
    }
    
    public boolean containsElement(final String id) {
        return id != null && this.getElement(id) != null;
    }
    
    public boolean changeElementId(final String oldId, final String newId) {
        if ((oldId == null && newId == null) || (oldId != null && oldId.equalsIgnoreCase(newId))) {
            return true;
        }
        if (this.m_elements == null || this.m_elements.containsKey(newId) || !this.m_elements.containsKey(oldId)) {
            return false;
        }
        final EventDispatcher e = this.m_elements.remove(oldId);
        if (newId != null) {
            this.m_elements.put(newId, e);
        }
        return true;
    }
    
    @Override
    public Iterator<EventDispatcher> iterator() {
        return this.m_elements.values().iterator();
    }
    
    public void removeElement(final String id) {
        if (this.m_elements != null) {
            this.m_elements.remove(id);
        }
    }
    
    public void removeElement(final EventDispatcher ed) {
        if (ed == null || this.m_elements == null) {
            return;
        }
        String id = ed.getId();
        if (id == null) {
            for (final Map.Entry<String, EventDispatcher> entry : this.m_elements.entrySet()) {
                if (entry.getValue() == ed) {
                    id = entry.getKey();
                    break;
                }
            }
        }
        if (id != null) {
            this.m_elements.remove(id);
        }
    }
    
    private void addChild(final ElementMap map) {
        if (this.m_children == null) {
            this.m_children = new ArrayList<ElementMap>(5);
        }
        this.m_children.add(map);
    }
    
    private void removeChild(final ElementMap map) {
        if (this.m_children == null) {
            return;
        }
        this.m_children.remove(map);
    }
    
    public ArrayList<ElementMap> getChildrenElementMaps() {
        return this.m_children;
    }
    
    public void setParentElementMap(final ElementMap parent) {
        if (this.m_parent != null) {
            this.m_parent.removeChild(this);
        }
        this.m_parent = parent;
        if (this.m_parent != null) {
            this.m_parent.addChild(this);
        }
    }
    
    public ElementMap getParentElementMap() {
        return this.m_parent;
    }
    
    public void addProperty(final Property property) {
        this.m_localProperties.put(property.getName(), property);
    }
    
    public Property getProperty(final String name) {
        Property p = this.m_localProperties.get(name);
        if (p == null && this.m_parent != null) {
            p = this.m_parent.getProperty(name);
        }
        return p;
    }
    
    public Collection<Property> getProperties() {
        return this.m_localProperties.values();
    }
    
    public void clear() {
        if (this.m_elements != null) {
            this.m_elements.clear();
            this.m_elements = null;
        }
        if (this.m_radiogroups != null) {
            this.m_radiogroups.clear();
        }
        for (final Property p : this.m_localProperties.values()) {
            PropertiesProvider.getInstance().removeProperty(p);
        }
        if (this.m_children != null) {
            for (int i = this.m_children.size() - 1; i >= 0; --i) {
                this.m_children.get(i).clear();
            }
            this.m_children.clear();
            this.m_children = null;
        }
        this.m_localProperties.clear();
        this.m_id = null;
        this.m_parent = null;
    }
    
    public void putRadioGroup(final String id, final RadioGroup rb) {
        if (this.m_radiogroups == null) {
            this.m_radiogroups = new HashMap<String, RadioGroup>();
        }
        this.m_radiogroups.put(id, rb);
    }
    
    public RadioGroup getRadioGroup(final String id) {
        RadioGroup group = null;
        if (this.m_radiogroups != null) {
            group = this.m_radiogroups.get(id);
        }
        if (group == null && this.m_parent != null) {
            group = this.m_parent.getRadioGroup(id);
        }
        return group;
    }
    
    public boolean radioGroupExists(final String id) {
        boolean exists = false;
        if (this.m_radiogroups != null) {
            exists = this.m_radiogroups.containsKey(id);
        }
        if (!exists && this.m_parent != null) {
            exists = this.m_parent.radioGroupExists(id);
        }
        return exists;
    }
    
    public void removeRadioGroup(final String id) {
        RadioGroup removed = null;
        if (this.m_radiogroups != null) {
            removed = this.m_radiogroups.remove(id);
        }
        if (this.m_parent != null && removed == null) {
            this.m_parent.removeRadioGroup(id);
        }
    }
    
    public void putEnvironmentProperty(final String propertyName, final String value) {
        if (this.m_environmentProperties == null) {
            this.m_environmentProperties = new THashMap<String, String>();
        }
        this.m_environmentProperties.put(propertyName, value);
    }
    
    public String getEnvironmentProperty(final String propertyName, final String defaultValue) {
        String value = this.m_environmentProperties.get(propertyName);
        for (ElementMap parent = this.m_parent; value == null && parent != null; value = parent.getEnvironmentProperty(propertyName, defaultValue), parent = parent.m_parent) {}
        return (value == null) ? defaultValue : value;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ElementMap.class);
        PREFIXED_ELEMENT_MAP_SEPARATOR = new StringBuilder(92).append('.').toString();
    }
}
