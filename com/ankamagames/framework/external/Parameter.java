package com.ankamagames.framework.external;

public class Parameter
{
    private String m_name;
    
    public Parameter(final String name) {
        super();
        this.m_name = "";
        if (name == null) {
            throw new IllegalArgumentException("Nom de param\u00e8tre ne peut \u00eatre null.");
        }
        this.m_name = name;
    }
    
    public final String getName() {
        return this.m_name;
    }
}
