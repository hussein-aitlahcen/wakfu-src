package org.apache.tools.ant.types;

public final class Parameter
{
    private String name;
    private String type;
    private String value;
    
    public Parameter() {
        super();
        this.name = null;
        this.type = null;
        this.value = null;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getValue() {
        return this.value;
    }
}
