package org.apache.tools.ant;

import java.util.*;
import java.text.*;
import org.apache.tools.ant.property.*;

public class PropertyHelper implements GetProperty
{
    private static final PropertyEvaluator TO_STRING;
    private static final PropertyExpander DEFAULT_EXPANDER;
    private static final PropertyExpander SKIP_DOUBLE_DOLLAR;
    private static final PropertyEvaluator FROM_REF;
    private Project project;
    private PropertyHelper next;
    private final Hashtable<Class<? extends Delegate>, List<Delegate>> delegates;
    private Hashtable<String, Object> properties;
    private Hashtable<String, Object> userProperties;
    private Hashtable<String, Object> inheritedProperties;
    
    protected PropertyHelper() {
        super();
        this.delegates = new Hashtable<Class<? extends Delegate>, List<Delegate>>();
        this.properties = new Hashtable<String, Object>();
        this.userProperties = new Hashtable<String, Object>();
        this.inheritedProperties = new Hashtable<String, Object>();
        this.add(PropertyHelper.FROM_REF);
        this.add(PropertyHelper.TO_STRING);
        this.add(PropertyHelper.SKIP_DOUBLE_DOLLAR);
        this.add(PropertyHelper.DEFAULT_EXPANDER);
    }
    
    public static Object getProperty(final Project project, final String name) {
        return getPropertyHelper(project).getProperty(name);
    }
    
    public static void setProperty(final Project project, final String name, final Object value) {
        getPropertyHelper(project).setProperty(name, value, true);
    }
    
    public static void setNewProperty(final Project project, final String name, final Object value) {
        getPropertyHelper(project).setNewProperty(name, value);
    }
    
    public void setProject(final Project p) {
        this.project = p;
    }
    
    public Project getProject() {
        return this.project;
    }
    
    public void setNext(final PropertyHelper next) {
        this.next = next;
    }
    
    public PropertyHelper getNext() {
        return this.next;
    }
    
    public static synchronized PropertyHelper getPropertyHelper(final Project project) {
        PropertyHelper helper = null;
        if (project != null) {
            helper = project.getReference("ant.PropertyHelper");
        }
        if (helper != null) {
            return helper;
        }
        helper = new PropertyHelper();
        helper.setProject(project);
        if (project != null) {
            project.addReference("ant.PropertyHelper", helper);
        }
        return helper;
    }
    
    public Collection<PropertyExpander> getExpanders() {
        return this.getDelegates(PropertyExpander.class);
    }
    
    public boolean setPropertyHook(final String ns, final String name, final Object value, final boolean inherited, final boolean user, final boolean isNew) {
        if (this.getNext() != null) {
            final boolean subst = this.getNext().setPropertyHook(ns, name, value, inherited, user, isNew);
            if (subst) {
                return true;
            }
        }
        return false;
    }
    
    public Object getPropertyHook(final String ns, String name, final boolean user) {
        if (this.getNext() != null) {
            final Object o = this.getNext().getPropertyHook(ns, name, user);
            if (o != null) {
                return o;
            }
        }
        if (this.project != null && name.startsWith("toString:")) {
            name = name.substring("toString:".length());
            final Object v = this.project.getReference(name);
            return (v == null) ? null : v.toString();
        }
        return null;
    }
    
    public void parsePropertyString(final String value, final Vector<String> fragments, final Vector<String> propertyRefs) throws BuildException {
        parsePropertyStringDefault(value, fragments, propertyRefs);
    }
    
    public String replaceProperties(final String ns, final String value, final Hashtable<String, Object> keys) throws BuildException {
        return this.replaceProperties(value);
    }
    
    public String replaceProperties(final String value) throws BuildException {
        final Object o = this.parseProperties(value);
        return (String)((o == null || o instanceof String) ? o : o.toString());
    }
    
    public Object parseProperties(final String value) throws BuildException {
        return new ParseProperties(this.getProject(), this.getExpanders(), this).parseProperties(value);
    }
    
    public boolean containsProperties(final String value) {
        return new ParseProperties(this.getProject(), this.getExpanders(), this).containsProperties(value);
    }
    
    public boolean setProperty(final String ns, final String name, final Object value, final boolean verbose) {
        return this.setProperty(name, value, verbose);
    }
    
    public boolean setProperty(final String name, final Object value, final boolean verbose) {
        for (final PropertySetter setter : this.getDelegates(PropertySetter.class)) {
            if (setter.set(name, value, this)) {
                return true;
            }
        }
        synchronized (this) {
            if (this.userProperties.containsKey(name)) {
                if (this.project != null && verbose) {
                    this.project.log("Override ignored for user property \"" + name + "\"", 3);
                }
                return false;
            }
            if (this.project != null && verbose) {
                if (this.properties.containsKey(name)) {
                    this.project.log("Overriding previous definition of property \"" + name + "\"", 3);
                }
                this.project.log("Setting project property: " + name + " -> " + value, 4);
            }
            if (name != null && value != null) {
                this.properties.put(name, value);
            }
            return true;
        }
    }
    
    public void setNewProperty(final String ns, final String name, final Object value) {
        this.setNewProperty(name, value);
    }
    
    public void setNewProperty(final String name, final Object value) {
        for (final PropertySetter setter : this.getDelegates(PropertySetter.class)) {
            if (setter.setNew(name, value, this)) {
                return;
            }
        }
        synchronized (this) {
            if (this.project != null && this.properties.containsKey(name)) {
                this.project.log("Override ignored for property \"" + name + "\"", 3);
                return;
            }
            if (this.project != null) {
                this.project.log("Setting project property: " + name + " -> " + value, 4);
            }
            if (name != null && value != null) {
                this.properties.put(name, value);
            }
        }
    }
    
    public void setUserProperty(final String ns, final String name, final Object value) {
        this.setUserProperty(name, value);
    }
    
    public void setUserProperty(final String name, final Object value) {
        if (this.project != null) {
            this.project.log("Setting ro project property: " + name + " -> " + value, 4);
        }
        synchronized (this) {
            this.userProperties.put(name, value);
            this.properties.put(name, value);
        }
    }
    
    public void setInheritedProperty(final String ns, final String name, final Object value) {
        this.setInheritedProperty(name, value);
    }
    
    public void setInheritedProperty(final String name, final Object value) {
        if (this.project != null) {
            this.project.log("Setting ro project property: " + name + " -> " + value, 4);
        }
        synchronized (this) {
            this.inheritedProperties.put(name, value);
            this.userProperties.put(name, value);
            this.properties.put(name, value);
        }
    }
    
    public Object getProperty(final String ns, final String name) {
        return this.getProperty(name);
    }
    
    public Object getProperty(final String name) {
        if (name == null) {
            return null;
        }
        for (final PropertyEvaluator evaluator : this.getDelegates(PropertyEvaluator.class)) {
            final Object o = evaluator.evaluate(name, this);
            if (o == null) {
                continue;
            }
            return (o instanceof NullReturn) ? null : o;
        }
        return this.properties.get(name);
    }
    
    public Object getUserProperty(final String ns, final String name) {
        return this.getUserProperty(name);
    }
    
    public Object getUserProperty(final String name) {
        if (name == null) {
            return null;
        }
        return this.userProperties.get(name);
    }
    
    public Hashtable<String, Object> getProperties() {
        synchronized (this.properties) {
            return new Hashtable<String, Object>(this.properties);
        }
    }
    
    public Hashtable<String, Object> getUserProperties() {
        synchronized (this.userProperties) {
            return new Hashtable<String, Object>(this.userProperties);
        }
    }
    
    public Hashtable<String, Object> getInheritedProperties() {
        synchronized (this.inheritedProperties) {
            return new Hashtable<String, Object>(this.inheritedProperties);
        }
    }
    
    protected Hashtable<String, Object> getInternalProperties() {
        return this.properties;
    }
    
    protected Hashtable<String, Object> getInternalUserProperties() {
        return this.userProperties;
    }
    
    protected Hashtable<String, Object> getInternalInheritedProperties() {
        return this.inheritedProperties;
    }
    
    public void copyInheritedProperties(final Project other) {
        synchronized (this.inheritedProperties) {
            final Enumeration<String> e = this.inheritedProperties.keys();
            while (e.hasMoreElements()) {
                final String arg = e.nextElement().toString();
                if (other.getUserProperty(arg) != null) {
                    continue;
                }
                final Object value = this.inheritedProperties.get(arg);
                other.setInheritedProperty(arg, value.toString());
            }
        }
    }
    
    public void copyUserProperties(final Project other) {
        synchronized (this.userProperties) {
            final Enumeration<String> e = this.userProperties.keys();
            while (e.hasMoreElements()) {
                final Object arg = e.nextElement();
                if (this.inheritedProperties.containsKey(arg)) {
                    continue;
                }
                final Object value = this.userProperties.get(arg);
                other.setUserProperty(arg.toString(), value.toString());
            }
        }
    }
    
    static void parsePropertyStringDefault(final String value, final Vector<String> fragments, final Vector<String> propertyRefs) throws BuildException {
        int prev = 0;
        int pos;
        while ((pos = value.indexOf("$", prev)) >= 0) {
            if (pos > 0) {
                fragments.addElement(value.substring(prev, pos));
            }
            if (pos == value.length() - 1) {
                fragments.addElement("$");
                prev = pos + 1;
            }
            else if (value.charAt(pos + 1) != '{') {
                if (value.charAt(pos + 1) == '$') {
                    fragments.addElement("$");
                    prev = pos + 2;
                }
                else {
                    fragments.addElement(value.substring(pos, pos + 2));
                    prev = pos + 2;
                }
            }
            else {
                final int endName = value.indexOf(125, pos);
                if (endName < 0) {
                    throw new BuildException("Syntax error in property: " + value);
                }
                final String propertyName = value.substring(pos + 2, endName);
                fragments.addElement(null);
                propertyRefs.addElement(propertyName);
                prev = endName + 1;
            }
        }
        if (prev < value.length()) {
            fragments.addElement(value.substring(prev));
        }
    }
    
    public void add(final Delegate delegate) {
        synchronized (this.delegates) {
            for (final Class<? extends Delegate> key : getDelegateInterfaces(delegate)) {
                List<Delegate> list = this.delegates.get(key);
                if (list == null) {
                    list = new ArrayList<Delegate>();
                }
                else {
                    list = new ArrayList<Delegate>(list);
                    list.remove(delegate);
                }
                list.add(0, delegate);
                this.delegates.put(key, Collections.unmodifiableList((List<? extends Delegate>)list));
            }
        }
    }
    
    protected <D extends Delegate> List<D> getDelegates(final Class<D> type) {
        final List<D> result = (List<D>)this.delegates.get(type);
        return (result == null) ? Collections.emptyList() : result;
    }
    
    protected static Set<Class<? extends Delegate>> getDelegateInterfaces(final Delegate d) {
        final HashSet<Class<? extends Delegate>> result = new HashSet<Class<? extends Delegate>>();
        for (Class<?> c = d.getClass(); c != null; c = c.getSuperclass()) {
            final Class<?>[] ifs = c.getInterfaces();
            for (int i = 0; i < ifs.length; ++i) {
                if (Delegate.class.isAssignableFrom(ifs[i])) {
                    final Class<? extends Delegate> delegateInterface = (Class<? extends Delegate>)ifs[i];
                    result.add(delegateInterface);
                }
            }
        }
        result.remove(Delegate.class);
        return result;
    }
    
    public static Boolean toBoolean(final Object value) {
        if (value instanceof Boolean) {
            return (Boolean)value;
        }
        if (value instanceof String) {
            final String s = (String)value;
            if (Project.toBoolean(s)) {
                return Boolean.TRUE;
            }
            if ("off".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s) || "no".equalsIgnoreCase(s)) {
                return Boolean.FALSE;
            }
        }
        return null;
    }
    
    private static boolean nullOrEmpty(final Object value) {
        return value == null || "".equals(value);
    }
    
    private boolean evalAsBooleanOrPropertyName(final Object value) {
        final Boolean b = toBoolean(value);
        if (b != null) {
            return b;
        }
        return this.getProperty(String.valueOf(value)) != null;
    }
    
    public boolean testIfCondition(final Object value) {
        return nullOrEmpty(value) || this.evalAsBooleanOrPropertyName(value);
    }
    
    public boolean testUnlessCondition(final Object value) {
        return nullOrEmpty(value) || !this.evalAsBooleanOrPropertyName(value);
    }
    
    static {
        TO_STRING = new PropertyEvaluator() {
            private final String PREFIX = "toString:";
            private final int PREFIX_LEN = "toString:".length();
            
            public Object evaluate(final String property, final PropertyHelper propertyHelper) {
                Object o = null;
                if (property.startsWith("toString:") && propertyHelper.getProject() != null) {
                    o = propertyHelper.getProject().getReference(property.substring(this.PREFIX_LEN));
                }
                return (o == null) ? null : o.toString();
            }
        };
        DEFAULT_EXPANDER = new PropertyExpander() {
            public String parsePropertyName(final String s, final ParsePosition pos, final ParseNextProperty notUsed) {
                final int index = pos.getIndex();
                if (s.length() - index < 3 || '$' != s.charAt(index) || '{' != s.charAt(index + 1)) {
                    return null;
                }
                final int start = index + 2;
                final int end = s.indexOf(125, start);
                if (end < 0) {
                    throw new BuildException("Syntax error in property: " + s.substring(index));
                }
                pos.setIndex(end + 1);
                return (start == end) ? "" : s.substring(start, end);
            }
        };
        SKIP_DOUBLE_DOLLAR = new PropertyExpander() {
            public String parsePropertyName(final String s, final ParsePosition pos, final ParseNextProperty notUsed) {
                int index = pos.getIndex();
                if (s.length() - index >= 2 && '$' == s.charAt(index) && '$' == s.charAt(++index)) {
                    pos.setIndex(index);
                }
                return null;
            }
        };
        FROM_REF = new PropertyEvaluator() {
            private final String PREFIX = "ant.refid:";
            private final int PREFIX_LEN = "ant.refid:".length();
            
            public Object evaluate(final String prop, final PropertyHelper helper) {
                return (prop.startsWith("ant.refid:") && helper.getProject() != null) ? helper.getProject().getReference(prop.substring(this.PREFIX_LEN)) : null;
            }
        };
    }
    
    public interface PropertySetter extends Delegate
    {
        boolean setNew(String p0, Object p1, PropertyHelper p2);
        
        boolean set(String p0, Object p1, PropertyHelper p2);
    }
    
    public interface Delegate
    {
    }
    
    public interface PropertyEvaluator extends Delegate
    {
        Object evaluate(String p0, PropertyHelper p1);
    }
}
