package org.apache.tools.ant.property;

import org.apache.tools.ant.*;

public class LocalProperties extends InheritableThreadLocal<LocalPropertyStack> implements PropertyHelper.PropertyEvaluator, PropertyHelper.PropertySetter
{
    public static synchronized LocalProperties get(final Project project) {
        LocalProperties l = project.getReference("ant.LocalProperties");
        if (l == null) {
            l = new LocalProperties();
            project.addReference("ant.LocalProperties", l);
            PropertyHelper.getPropertyHelper(project).add(l);
        }
        return l;
    }
    
    protected synchronized LocalPropertyStack initialValue() {
        return new LocalPropertyStack();
    }
    
    private LocalPropertyStack current() {
        return this.get();
    }
    
    public void addLocal(final String property) {
        this.current().addLocal(property);
    }
    
    public void enterScope() {
        this.current().enterScope();
    }
    
    public void exitScope() {
        this.current().exitScope();
    }
    
    public void copy() {
        this.set(this.current().copy());
    }
    
    public Object evaluate(final String property, final PropertyHelper helper) {
        return this.current().evaluate(property, helper);
    }
    
    public boolean setNew(final String property, final Object value, final PropertyHelper propertyHelper) {
        return this.current().setNew(property, value, propertyHelper);
    }
    
    public boolean set(final String property, final Object value, final PropertyHelper propertyHelper) {
        return this.current().set(property, value, propertyHelper);
    }
}
