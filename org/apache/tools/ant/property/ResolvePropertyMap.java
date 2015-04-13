package org.apache.tools.ant.property;

import org.apache.tools.ant.*;
import java.util.*;

public class ResolvePropertyMap implements GetProperty
{
    private final Set<String> seen;
    private final ParseProperties parseProperties;
    private final GetProperty master;
    private Map<String, Object> map;
    private String prefix;
    private boolean prefixValues;
    private boolean expandingLHS;
    
    public ResolvePropertyMap(final Project project, final GetProperty master, final Collection<PropertyExpander> expanders) {
        super();
        this.seen = new HashSet<String>();
        this.prefixValues = false;
        this.expandingLHS = true;
        this.master = master;
        this.parseProperties = new ParseProperties(project, expanders, this);
    }
    
    public Object getProperty(final String name) {
        if (this.seen.contains(name)) {
            throw new BuildException("Property " + name + " was circularly " + "defined.");
        }
        try {
            String fullKey = name;
            if (this.prefix != null && (this.expandingLHS || this.prefixValues)) {
                fullKey = this.prefix + name;
            }
            final Object masterValue = this.master.getProperty(fullKey);
            if (masterValue != null) {
                return masterValue;
            }
            this.seen.add(name);
            this.expandingLHS = false;
            return this.parseProperties.parseProperties(this.map.get(name));
        }
        finally {
            this.seen.remove(name);
        }
    }
    
    public void resolveAllProperties(final Map<String, Object> map) {
        this.resolveAllProperties(map, null, false);
    }
    
    public void resolveAllProperties(final Map<String, Object> map, final String prefix) {
        this.resolveAllProperties(map, null, false);
    }
    
    public void resolveAllProperties(final Map<String, Object> map, final String prefix, final boolean prefixValues) {
        this.map = map;
        this.prefix = prefix;
        this.prefixValues = prefixValues;
        for (final String key : map.keySet()) {
            this.expandingLHS = true;
            final Object result = this.getProperty(key);
            final String value = (result == null) ? "" : result.toString();
            map.put(key, value);
        }
    }
}
