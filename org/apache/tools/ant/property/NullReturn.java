package org.apache.tools.ant.property;

public final class NullReturn
{
    public static final NullReturn NULL;
    
    public String toString() {
        return "null";
    }
    
    static {
        NULL = new NullReturn();
    }
}
