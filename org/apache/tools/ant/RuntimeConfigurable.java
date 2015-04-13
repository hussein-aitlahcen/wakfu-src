package org.apache.tools.ant;

import java.io.*;
import org.xml.sax.*;
import org.apache.tools.ant.attribute.*;
import org.xml.sax.helpers.*;
import org.apache.tools.ant.util.*;
import org.apache.tools.ant.taskdefs.*;
import java.util.*;

public class RuntimeConfigurable implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Hashtable<String, Object> EMPTY_HASHTABLE;
    private String elementTag;
    private List<RuntimeConfigurable> children;
    private transient Object wrappedObject;
    private transient AttributeList attributes;
    private transient boolean namespacedAttribute;
    private LinkedHashMap<String, Object> attributeMap;
    private StringBuffer characters;
    private boolean proxyConfigured;
    private String polyType;
    private String id;
    
    public RuntimeConfigurable(final Object proxy, final String elementTag) {
        super();
        this.elementTag = null;
        this.children = null;
        this.wrappedObject = null;
        this.namespacedAttribute = false;
        this.attributeMap = null;
        this.characters = null;
        this.proxyConfigured = false;
        this.polyType = null;
        this.id = null;
        this.setProxy(proxy);
        this.setElementTag(elementTag);
        if (proxy instanceof Task) {
            ((Task)proxy).setRuntimeConfigurableWrapper(this);
        }
    }
    
    public synchronized void setProxy(final Object proxy) {
        this.wrappedObject = proxy;
        this.proxyConfigured = false;
    }
    
    public boolean isEnabled(final UnknownElement owner) {
        if (!this.namespacedAttribute) {
            return true;
        }
        final ComponentHelper componentHelper = ComponentHelper.getComponentHelper(owner.getProject());
        final IntrospectionHelper ih = IntrospectionHelper.getHelper(owner.getProject(), EnableAttributeConsumer.class);
        for (int i = 0; i < this.attributeMap.keySet().size(); ++i) {
            final String name = (String)this.attributeMap.keySet().toArray()[i];
            if (name.indexOf(58) != -1) {
                final String componentName = this.attrToComponent(name);
                final String ns = ProjectHelper.extractUriFromComponentName(componentName);
                if (componentHelper.getRestrictedDefinitions(ProjectHelper.nsToComponentName(ns)) != null) {
                    String value = this.attributeMap.get(name);
                    EnableAttribute enable = null;
                    try {
                        enable = (EnableAttribute)ih.createElement(owner.getProject(), new EnableAttributeConsumer(), componentName);
                    }
                    catch (BuildException ex) {
                        throw new BuildException("Unsupported attribute " + componentName);
                    }
                    if (enable != null) {
                        value = owner.getProject().replaceProperties(value);
                        if (!enable.isEnabled(owner, value)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private String attrToComponent(final String a) {
        final int p1 = a.lastIndexOf(58);
        final int p2 = a.lastIndexOf(58, p1 - 1);
        return a.substring(0, p2) + a.substring(p1);
    }
    
    synchronized void setCreator(final IntrospectionHelper.Creator creator) {
    }
    
    public synchronized Object getProxy() {
        return this.wrappedObject;
    }
    
    public synchronized String getId() {
        return this.id;
    }
    
    public synchronized String getPolyType() {
        return this.polyType;
    }
    
    public synchronized void setPolyType(final String polyType) {
        this.polyType = polyType;
    }
    
    public synchronized void setAttributes(final AttributeList attributes) {
        this.attributes = new AttributeListImpl(attributes);
        for (int i = 0; i < attributes.getLength(); ++i) {
            this.setAttribute(attributes.getName(i), attributes.getValue(i));
        }
    }
    
    public synchronized void setAttribute(final String name, final String value) {
        if (name.indexOf(58) != -1) {
            this.namespacedAttribute = true;
        }
        this.setAttribute(name, (Object)value);
    }
    
    public synchronized void setAttribute(final String name, final Object value) {
        if (name.equalsIgnoreCase("ant-type")) {
            this.polyType = ((value == null) ? null : value.toString());
        }
        else {
            if (this.attributeMap == null) {
                this.attributeMap = new LinkedHashMap<String, Object>();
            }
            if (name.equalsIgnoreCase("refid") && !this.attributeMap.isEmpty()) {
                final LinkedHashMap<String, Object> newAttributeMap = new LinkedHashMap<String, Object>();
                newAttributeMap.put(name, value);
                newAttributeMap.putAll((Map<?, ?>)this.attributeMap);
                this.attributeMap = newAttributeMap;
            }
            else {
                this.attributeMap.put(name, value);
            }
            if (name.equals("id")) {
                this.id = ((value == null) ? null : value.toString());
            }
        }
    }
    
    public synchronized void removeAttribute(final String name) {
        this.attributeMap.remove(name);
    }
    
    public synchronized Hashtable<String, Object> getAttributeMap() {
        return (this.attributeMap == null) ? RuntimeConfigurable.EMPTY_HASHTABLE : new Hashtable<String, Object>(this.attributeMap);
    }
    
    public synchronized AttributeList getAttributes() {
        return this.attributes;
    }
    
    public synchronized void addChild(final RuntimeConfigurable child) {
        (this.children = ((this.children == null) ? new ArrayList<RuntimeConfigurable>() : this.children)).add(child);
    }
    
    synchronized RuntimeConfigurable getChild(final int index) {
        return this.children.get(index);
    }
    
    public synchronized Enumeration<RuntimeConfigurable> getChildren() {
        return (this.children == null) ? new CollectionUtils.EmptyEnumeration<RuntimeConfigurable>() : Collections.enumeration(this.children);
    }
    
    public synchronized void addText(final String data) {
        if (data.length() == 0) {
            return;
        }
        this.characters = ((this.characters == null) ? new StringBuffer(data) : this.characters.append(data));
    }
    
    public synchronized void addText(final char[] buf, final int start, final int count) {
        if (count == 0) {
            return;
        }
        this.characters = ((this.characters == null) ? new StringBuffer(count) : this.characters).append(buf, start, count);
    }
    
    public synchronized StringBuffer getText() {
        return (this.characters == null) ? new StringBuffer(0) : this.characters;
    }
    
    public synchronized void setElementTag(final String elementTag) {
        this.elementTag = elementTag;
    }
    
    public synchronized String getElementTag() {
        return this.elementTag;
    }
    
    public void maybeConfigure(final Project p) throws BuildException {
        this.maybeConfigure(p, true);
    }
    
    public synchronized void maybeConfigure(final Project p, final boolean configureChildren) throws BuildException {
        if (this.proxyConfigured) {
            return;
        }
        final Object target = (this.wrappedObject instanceof TypeAdapter) ? ((TypeAdapter)this.wrappedObject).getProxy() : this.wrappedObject;
        final IntrospectionHelper ih = IntrospectionHelper.getHelper(p, target.getClass());
        if (this.attributeMap != null) {
            for (final Map.Entry<String, Object> entry : this.attributeMap.entrySet()) {
                final String name = entry.getKey();
                final Object value = entry.getValue();
                Object attrValue;
                if (value instanceof Evaluable) {
                    attrValue = ((Evaluable)value).eval();
                }
                else {
                    attrValue = PropertyHelper.getPropertyHelper(p).parseProperties(value.toString());
                }
                if (target instanceof MacroInstance) {
                    for (final MacroDef.Attribute attr : ((MacroInstance)target).getMacroDef().getAttributes()) {
                        if (attr.getName().equals(name)) {
                            if (!attr.isDoubleExpanding()) {
                                attrValue = value;
                                break;
                            }
                            break;
                        }
                    }
                }
                try {
                    ih.setAttribute(p, target, name, attrValue);
                }
                catch (UnsupportedAttributeException be) {
                    if (name.equals("id")) {
                        continue;
                    }
                    if (this.getElementTag() == null) {
                        throw be;
                    }
                    throw new BuildException(this.getElementTag() + " doesn't support the \"" + be.getAttribute() + "\" attribute", be);
                }
                catch (BuildException be2) {
                    if (name.equals("id")) {
                        continue;
                    }
                    throw be2;
                }
            }
        }
        if (this.characters != null) {
            ProjectHelper.addText(p, this.wrappedObject, this.characters.substring(0));
        }
        if (this.id != null) {
            p.addReference(this.id, this.wrappedObject);
        }
        this.proxyConfigured = true;
    }
    
    public void reconfigure(final Project p) {
        this.proxyConfigured = false;
        this.maybeConfigure(p);
    }
    
    public void applyPreSet(final RuntimeConfigurable r) {
        if (r.attributeMap != null) {
            for (final String name : r.attributeMap.keySet()) {
                if (this.attributeMap == null || this.attributeMap.get(name) == null) {
                    this.setAttribute(name, r.attributeMap.get(name));
                }
            }
        }
        this.polyType = ((this.polyType == null) ? r.polyType : this.polyType);
        if (r.children != null) {
            final List<RuntimeConfigurable> newChildren = new ArrayList<RuntimeConfigurable>();
            newChildren.addAll(r.children);
            if (this.children != null) {
                newChildren.addAll(this.children);
            }
            this.children = newChildren;
        }
        if (r.characters != null && (this.characters == null || this.characters.toString().trim().length() == 0)) {
            this.characters = new StringBuffer(r.characters.toString());
        }
    }
    
    static {
        EMPTY_HASHTABLE = new Hashtable<String, Object>(0);
    }
    
    private static class EnableAttributeConsumer
    {
        public void add(final EnableAttribute b) {
        }
    }
}
