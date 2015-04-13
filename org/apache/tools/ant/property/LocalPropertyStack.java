package org.apache.tools.ant.property;

import org.apache.tools.ant.*;
import java.util.*;

public class LocalPropertyStack
{
    private final LinkedList<Map<String, Object>> stack;
    
    public LocalPropertyStack() {
        super();
        this.stack = new LinkedList<Map<String, Object>>();
    }
    
    public void addLocal(final String property) {
        if (!this.stack.isEmpty()) {
            this.stack.getFirst().put(property, NullReturn.NULL);
        }
    }
    
    public void enterScope() {
        this.stack.addFirst(new HashMap<String, Object>());
    }
    
    public void exitScope() {
        this.stack.removeFirst().clear();
    }
    
    public LocalPropertyStack copy() {
        final LocalPropertyStack ret = new LocalPropertyStack();
        ret.stack.addAll(this.stack);
        return ret;
    }
    
    public Object evaluate(final String property, final PropertyHelper helper) {
        for (final Map<String, Object> map : this.stack) {
            final Object ret = map.get(property);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }
    
    public boolean setNew(final String property, final Object value, final PropertyHelper propertyHelper) {
        final Map<String, Object> map = this.getMapForProperty(property);
        if (map == null) {
            return false;
        }
        final Object currValue = map.get(property);
        if (currValue == NullReturn.NULL) {
            map.put(property, value);
        }
        return true;
    }
    
    public boolean set(final String property, final Object value, final PropertyHelper propertyHelper) {
        final Map<String, Object> map = this.getMapForProperty(property);
        if (map == null) {
            return false;
        }
        map.put(property, value);
        return true;
    }
    
    private Map<String, Object> getMapForProperty(final String property) {
        for (final Map<String, Object> map : this.stack) {
            if (map.get(property) != null) {
                return map;
            }
        }
        return null;
    }
}
