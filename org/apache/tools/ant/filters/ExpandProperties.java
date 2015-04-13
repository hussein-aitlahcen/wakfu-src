package org.apache.tools.ant.filters;

import org.apache.tools.ant.types.*;
import java.util.*;
import org.apache.tools.ant.property.*;
import org.apache.tools.ant.*;
import java.io.*;

public final class ExpandProperties extends BaseFilterReader implements ChainableReader
{
    private static final int EOF = -1;
    private char[] buffer;
    private int index;
    private PropertySet propertySet;
    
    public ExpandProperties() {
        super();
    }
    
    public ExpandProperties(final Reader in) {
        super(in);
    }
    
    public void add(final PropertySet propertySet) {
        if (this.propertySet != null) {
            throw new BuildException("expandproperties filter accepts only one propertyset");
        }
        this.propertySet = propertySet;
    }
    
    public int read() throws IOException {
        if (this.index > -1) {
            if (this.buffer == null) {
                final String data = this.readFully();
                final Project project = this.getProject();
                GetProperty getProperty;
                if (this.propertySet == null) {
                    getProperty = PropertyHelper.getPropertyHelper(project);
                }
                else {
                    final Properties props = this.propertySet.getProperties();
                    getProperty = new GetProperty() {
                        public Object getProperty(final String name) {
                            return props.getProperty(name);
                        }
                    };
                }
                final Object expanded = new ParseProperties(project, PropertyHelper.getPropertyHelper(project).getExpanders(), getProperty).parseProperties(data);
                this.buffer = ((expanded == null) ? new char[0] : expanded.toString().toCharArray());
            }
            if (this.index < this.buffer.length) {
                return this.buffer[this.index++];
            }
            this.index = -1;
        }
        return -1;
    }
    
    public Reader chain(final Reader rdr) {
        final ExpandProperties newFilter = new ExpandProperties(rdr);
        newFilter.setProject(this.getProject());
        newFilter.add(this.propertySet);
        return newFilter;
    }
}
