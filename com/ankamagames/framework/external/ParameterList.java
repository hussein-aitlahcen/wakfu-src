package com.ankamagames.framework.external;

public abstract class ParameterList
{
    private String m_name;
    private Parameter[] m_parameters;
    
    public ParameterList(final String name, final Parameter... parameters) {
        super();
        this.m_name = "";
        this.m_parameters = null;
        if (name == null) {
            throw new IllegalArgumentException("Nom de liste ne peut \u00eatre null.");
        }
        this.m_name = name;
        this.m_parameters = parameters;
    }
    
    public ParameterList(final Parameter... parameters) {
        this("", parameters);
    }
    
    public final int getParametersCount() {
        return (this.m_parameters != null) ? this.m_parameters.length : 0;
    }
    
    public final String getName() {
        return this.m_name;
    }
    
    public Parameter getParameter(final int index) {
        return this.m_parameters[index];
    }
    
    public abstract Parameter[] getRawParameters();
}
