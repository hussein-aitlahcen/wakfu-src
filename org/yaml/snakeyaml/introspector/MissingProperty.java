package org.yaml.snakeyaml.introspector;

public class MissingProperty extends Property
{
    public MissingProperty(final String name) {
        super(name, Object.class);
    }
    
    public Class<?>[] getActualTypeArguments() {
        return (Class<?>[])new Class[0];
    }
    
    public void set(final Object object, final Object value) throws Exception {
    }
    
    public Object get(final Object object) {
        return object;
    }
}
