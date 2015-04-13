package org.apache.tools.ant.types;

import org.apache.tools.ant.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.util.regexp.*;
import org.apache.tools.ant.types.resources.*;
import java.util.*;
import java.io.*;

public class PropertySet extends DataType implements ResourceCollection
{
    private boolean dynamic;
    private boolean negate;
    private Set<String> cachedNames;
    private List<PropertyRef> ptyRefs;
    private List<PropertySet> setRefs;
    private Mapper mapper;
    private boolean noAttributeSet;
    
    public PropertySet() {
        super();
        this.dynamic = true;
        this.negate = false;
        this.ptyRefs = new ArrayList<PropertyRef>();
        this.setRefs = new ArrayList<PropertySet>();
        this.noAttributeSet = true;
    }
    
    public void appendName(final String name) {
        final PropertyRef r = new PropertyRef();
        r.setName(name);
        this.addPropertyref(r);
    }
    
    public void appendRegex(final String regex) {
        final PropertyRef r = new PropertyRef();
        r.setRegex(regex);
        this.addPropertyref(r);
    }
    
    public void appendPrefix(final String prefix) {
        final PropertyRef r = new PropertyRef();
        r.setPrefix(prefix);
        this.addPropertyref(r);
    }
    
    public void appendBuiltin(final BuiltinPropertySetName b) {
        final PropertyRef r = new PropertyRef();
        r.setBuiltin(b);
        this.addPropertyref(r);
    }
    
    public void setMapper(final String type, final String from, final String to) {
        final Mapper m = this.createMapper();
        final Mapper.MapperType mapperType = new Mapper.MapperType();
        mapperType.setValue(type);
        m.setType(mapperType);
        m.setFrom(from);
        m.setTo(to);
    }
    
    public void addPropertyref(final PropertyRef ref) {
        this.assertNotReference();
        this.setChecked(false);
        this.ptyRefs.add(ref);
    }
    
    public void addPropertyset(final PropertySet ref) {
        this.assertNotReference();
        this.setChecked(false);
        this.setRefs.add(ref);
    }
    
    public Mapper createMapper() {
        this.assertNotReference();
        if (this.mapper != null) {
            throw new BuildException("Too many <mapper>s!");
        }
        this.mapper = new Mapper(this.getProject());
        this.setChecked(false);
        return this.mapper;
    }
    
    public void add(final FileNameMapper fileNameMapper) {
        this.createMapper().add(fileNameMapper);
    }
    
    public void setDynamic(final boolean dynamic) {
        this.assertNotReference();
        this.dynamic = dynamic;
    }
    
    public void setNegate(final boolean negate) {
        this.assertNotReference();
        this.negate = negate;
    }
    
    public boolean getDynamic() {
        if (this.isReference()) {
            return this.getRef().dynamic;
        }
        this.dieOnCircularReference();
        return this.dynamic;
    }
    
    public Mapper getMapper() {
        if (this.isReference()) {
            return this.getRef().mapper;
        }
        this.dieOnCircularReference();
        return this.mapper;
    }
    
    private Hashtable<String, Object> getAllSystemProperties() {
        final Hashtable<String, Object> ret = new Hashtable<String, Object>();
        final Enumeration<?> e = System.getProperties().propertyNames();
        while (e.hasMoreElements()) {
            final String name = (String)e.nextElement();
            ret.put(name, System.getProperties().getProperty(name));
        }
        return ret;
    }
    
    public Properties getProperties() {
        final Properties result = new Properties();
        result.putAll(this.getPropertyMap());
        return result;
    }
    
    private Map<String, Object> getPropertyMap() {
        if (this.isReference()) {
            return this.getRef().getPropertyMap();
        }
        this.dieOnCircularReference();
        final Mapper myMapper = this.getMapper();
        final FileNameMapper m = (myMapper == null) ? null : myMapper.getImplementation();
        final Map<String, Object> effectiveProperties = this.getEffectiveProperties();
        final Set<String> propertyNames = this.getPropertyNames(effectiveProperties);
        final Map<String, Object> result = new HashMap<String, Object>();
        for (String name : propertyNames) {
            final Object value = effectiveProperties.get(name);
            if (value != null) {
                if (m != null) {
                    final String[] newname = m.mapFileName(name);
                    if (newname != null) {
                        name = newname[0];
                    }
                }
                result.put(name, value);
            }
        }
        return result;
    }
    
    private Map<String, Object> getEffectiveProperties() {
        final Project prj = this.getProject();
        final Map<String, Object> result = (prj == null) ? this.getAllSystemProperties() : prj.getProperties();
        for (final PropertySet set : this.setRefs) {
            result.putAll(set.getPropertyMap());
        }
        return result;
    }
    
    private Set<String> getPropertyNames(final Map<String, Object> props) {
        Set<String> names;
        if (this.getDynamic() || this.cachedNames == null) {
            names = new HashSet<String>();
            this.addPropertyNames(names, props);
            for (final PropertySet set : this.setRefs) {
                names.addAll(set.getPropertyMap().keySet());
            }
            if (this.negate) {
                final HashSet<String> complement = new HashSet<String>(props.keySet());
                complement.removeAll(names);
                names = complement;
            }
            if (!this.getDynamic()) {
                this.cachedNames = names;
            }
        }
        else {
            names = this.cachedNames;
        }
        return names;
    }
    
    private void addPropertyNames(final Set<String> names, final Map<String, Object> props) {
        if (this.isReference()) {
            this.getRef().addPropertyNames(names, props);
        }
        this.dieOnCircularReference();
        for (final PropertyRef r : this.ptyRefs) {
            if (r.name != null) {
                if (props.get(r.name) == null) {
                    continue;
                }
                names.add(r.name);
            }
            else if (r.prefix != null) {
                for (final String name : props.keySet()) {
                    if (name.startsWith(r.prefix)) {
                        names.add(name);
                    }
                }
            }
            else if (r.regex != null) {
                final RegexpMatcherFactory matchMaker = new RegexpMatcherFactory();
                final RegexpMatcher matcher = matchMaker.newRegexpMatcher();
                matcher.setPattern(r.regex);
                for (final String name2 : props.keySet()) {
                    if (matcher.matches(name2)) {
                        names.add(name2);
                    }
                }
            }
            else {
                if (r.builtin == null) {
                    throw new BuildException("Impossible: Invalid PropertyRef!");
                }
                if (r.builtin.equals("all")) {
                    names.addAll(props.keySet());
                }
                else if (r.builtin.equals("system")) {
                    names.addAll(this.getAllSystemProperties().keySet());
                }
                else {
                    if (!r.builtin.equals("commandline")) {
                        throw new BuildException("Impossible: Invalid builtin attribute!");
                    }
                    names.addAll(this.getProject().getUserProperties().keySet());
                }
            }
        }
    }
    
    protected PropertySet getRef() {
        return this.getCheckedRef(PropertySet.class, "propertyset");
    }
    
    public final void setRefid(final Reference r) {
        if (!this.noAttributeSet) {
            throw this.tooManyAttributes();
        }
        super.setRefid(r);
    }
    
    protected final void assertNotReference() {
        if (this.isReference()) {
            throw this.tooManyAttributes();
        }
        this.noAttributeSet = false;
    }
    
    public String toString() {
        if (this.isReference()) {
            return this.getRef().toString();
        }
        this.dieOnCircularReference();
        final StringBuilder b = new StringBuilder();
        final TreeMap<String, Object> sorted = new TreeMap<String, Object>(this.getPropertyMap());
        for (final Map.Entry<String, Object> e : sorted.entrySet()) {
            if (b.length() != 0) {
                b.append(", ");
            }
            b.append(e.getKey());
            b.append("=");
            b.append(e.getValue());
        }
        return b.toString();
    }
    
    public Iterator<Resource> iterator() {
        if (this.isReference()) {
            return this.getRef().iterator();
        }
        this.dieOnCircularReference();
        final Set<String> names = this.getPropertyNames(this.getEffectiveProperties());
        final Mapper myMapper = this.getMapper();
        final FileNameMapper m = (myMapper == null) ? null : myMapper.getImplementation();
        final Iterator<String> iter = names.iterator();
        return new Iterator<Resource>() {
            public boolean hasNext() {
                return iter.hasNext();
            }
            
            public Resource next() {
                final PropertyResource p = new PropertyResource(PropertySet.this.getProject(), iter.next());
                return (m == null) ? p : new MappedResource(p, m);
            }
            
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public int size() {
        return this.isReference() ? this.getRef().size() : this.getProperties().size();
    }
    
    public boolean isFilesystemOnly() {
        if (this.isReference()) {
            return this.getRef().isFilesystemOnly();
        }
        this.dieOnCircularReference();
        return false;
    }
    
    protected synchronized void dieOnCircularReference(final Stack<Object> stk, final Project p) throws BuildException {
        if (this.isChecked()) {
            return;
        }
        if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
        }
        else {
            if (this.mapper != null) {
                DataType.pushAndInvokeCircularReferenceCheck(this.mapper, stk, p);
            }
            for (final PropertySet propertySet : this.setRefs) {
                DataType.pushAndInvokeCircularReferenceCheck(propertySet, stk, p);
            }
            this.setChecked(true);
        }
    }
    
    public static class PropertyRef
    {
        private int count;
        private String name;
        private String regex;
        private String prefix;
        private String builtin;
        
        public void setName(final String name) {
            this.assertValid("name", name);
            this.name = name;
        }
        
        public void setRegex(final String regex) {
            this.assertValid("regex", regex);
            this.regex = regex;
        }
        
        public void setPrefix(final String prefix) {
            this.assertValid("prefix", prefix);
            this.prefix = prefix;
        }
        
        public void setBuiltin(final BuiltinPropertySetName b) {
            final String pBuiltIn = b.getValue();
            this.assertValid("builtin", pBuiltIn);
            this.builtin = pBuiltIn;
        }
        
        private void assertValid(final String attr, final String value) {
            if (value == null || value.length() < 1) {
                throw new BuildException("Invalid attribute: " + attr);
            }
            if (++this.count != 1) {
                throw new BuildException("Attributes name, regex, and prefix are mutually exclusive");
            }
        }
        
        public String toString() {
            return "name=" + this.name + ", regex=" + this.regex + ", prefix=" + this.prefix + ", builtin=" + this.builtin;
        }
    }
    
    public static class BuiltinPropertySetName extends EnumeratedAttribute
    {
        static final String ALL = "all";
        static final String SYSTEM = "system";
        static final String COMMANDLINE = "commandline";
        
        public String[] getValues() {
            return new String[] { "all", "system", "commandline" };
        }
    }
}
