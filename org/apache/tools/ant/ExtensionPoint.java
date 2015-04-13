package org.apache.tools.ant;

public class ExtensionPoint extends Target
{
    private static final String NO_CHILDREN_ALLOWED = "you must not nest child elements into an extension-point";
    
    public ExtensionPoint() {
        super();
    }
    
    public ExtensionPoint(final Target other) {
        super(other);
    }
    
    public final void addTask(final Task task) {
        throw new BuildException("you must not nest child elements into an extension-point");
    }
    
    public final void addDataType(final RuntimeConfigurable r) {
        throw new BuildException("you must not nest child elements into an extension-point");
    }
}
