package org.apache.tools.ant.property;

import org.apache.tools.ant.*;
import java.text.*;
import java.util.*;

public class ParseProperties implements ParseNextProperty
{
    private final Project project;
    private final GetProperty getProperty;
    private final Collection<PropertyExpander> expanders;
    
    public ParseProperties(final Project project, final Collection<PropertyExpander> expanders, final GetProperty getProperty) {
        super();
        this.project = project;
        this.expanders = expanders;
        this.getProperty = getProperty;
    }
    
    public Project getProject() {
        return this.project;
    }
    
    public Object parseProperties(final String value) {
        if (value == null || "".equals(value)) {
            return value;
        }
        final int len = value.length();
        final ParsePosition pos = new ParsePosition(0);
        Object o = this.parseNextProperty(value, pos);
        if (o != null && pos.getIndex() >= len) {
            return o;
        }
        final StringBuffer sb = new StringBuffer(len * 2);
        if (o == null) {
            sb.append(value.charAt(pos.getIndex()));
            pos.setIndex(pos.getIndex() + 1);
        }
        else {
            sb.append(o);
        }
        while (pos.getIndex() < len) {
            o = this.parseNextProperty(value, pos);
            if (o == null) {
                sb.append(value.charAt(pos.getIndex()));
                pos.setIndex(pos.getIndex() + 1);
            }
            else {
                sb.append(o);
            }
        }
        return sb.toString();
    }
    
    public boolean containsProperties(final String value) {
        if (value == null) {
            return false;
        }
        final int len = value.length();
        final ParsePosition pos = new ParsePosition(0);
        while (pos.getIndex() < len) {
            if (this.parsePropertyName(value, pos) != null) {
                return true;
            }
            pos.setIndex(pos.getIndex() + 1);
        }
        return false;
    }
    
    public Object parseNextProperty(final String value, final ParsePosition pos) {
        final int start = pos.getIndex();
        if (start > value.length()) {
            return null;
        }
        final String propertyName = this.parsePropertyName(value, pos);
        if (propertyName == null) {
            return null;
        }
        final Object result = this.getProperty(propertyName);
        if (result != null) {
            return result;
        }
        if (this.project != null) {
            this.project.log("Property \"" + propertyName + "\" has not been set", 3);
        }
        return value.substring(start, pos.getIndex());
    }
    
    private String parsePropertyName(final String value, final ParsePosition pos) {
        for (final PropertyExpander propertyExpander : this.expanders) {
            final String propertyName = propertyExpander.parsePropertyName(value, pos, this);
            if (propertyName == null) {
                continue;
            }
            return propertyName;
        }
        return null;
    }
    
    private Object getProperty(final String propertyName) {
        return this.getProperty.getProperty(propertyName);
    }
}
