package com.ankamagames.framework.graphics.engine.fx;

public final class Variable extends Annotated
{
    public final VariableType m_type;
    
    public Variable(final String name, final VariableType type) {
        super(name);
        this.m_type = type;
    }
    
    public Variable(final Variable v) {
        this(v.m_name, v.m_type);
    }
    
    public enum VariableType
    {
        Float {
            @Override
            public final int getSize() {
                return 1;
            }
        }, 
        Vector2 {
            @Override
            public final int getSize() {
                return 2;
            }
        }, 
        Vector3 {
            @Override
            public final int getSize() {
                return 3;
            }
        }, 
        Vector4 {
            @Override
            public final int getSize() {
                return 4;
            }
        }, 
        Matrix44 {
            @Override
            public final int getSize() {
                return 16;
            }
        };
        
        public abstract int getSize();
    }
}
