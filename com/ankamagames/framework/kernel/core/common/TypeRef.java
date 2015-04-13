package com.ankamagames.framework.kernel.core.common;

public final class TypeRef<Type>
{
    private Type m_value;
    
    public TypeRef(final Type value) {
        super();
        this.m_value = value;
    }
    
    public Type get() {
        return this.m_value;
    }
    
    public void set(final Type value) {
        this.m_value = value;
    }
}
