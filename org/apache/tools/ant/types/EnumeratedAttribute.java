package org.apache.tools.ant.types;

import org.apache.tools.ant.*;

public abstract class EnumeratedAttribute
{
    protected String value;
    private int index;
    
    public abstract String[] getValues();
    
    protected EnumeratedAttribute() {
        super();
        this.index = -1;
    }
    
    public static EnumeratedAttribute getInstance(final Class<? extends EnumeratedAttribute> clazz, final String value) throws BuildException {
        if (!EnumeratedAttribute.class.isAssignableFrom(clazz)) {
            throw new BuildException("You have to provide a subclass from EnumeratedAttribut as clazz-parameter.");
        }
        EnumeratedAttribute ea = null;
        try {
            ea = (EnumeratedAttribute)clazz.newInstance();
        }
        catch (Exception e) {
            throw new BuildException(e);
        }
        ea.setValue(value);
        return ea;
    }
    
    public final void setValue(final String value) throws BuildException {
        final int idx = this.indexOfValue(value);
        if (idx == -1) {
            throw new BuildException(value + " is not a legal value for this attribute");
        }
        this.index = idx;
        this.value = value;
    }
    
    public final boolean containsValue(final String value) {
        return this.indexOfValue(value) != -1;
    }
    
    public final int indexOfValue(final String value) {
        final String[] values = this.getValues();
        if (values == null || value == null) {
            return -1;
        }
        for (int i = 0; i < values.length; ++i) {
            if (value.equals(values[i])) {
                return i;
            }
        }
        return -1;
    }
    
    public final String getValue() {
        return this.value;
    }
    
    public final int getIndex() {
        return this.index;
    }
    
    public String toString() {
        return this.getValue();
    }
}
