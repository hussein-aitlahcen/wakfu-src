package com.ankamagames.framework.external;

public final class DefaultParameterList extends ParameterList
{
    public DefaultParameterList(final String name, final Parameter... parameters) {
        super(name, parameters);
    }
    
    public DefaultParameterList(final Parameter... parameters) {
        super(parameters);
    }
    
    @Override
    public final Parameter[] getRawParameters() {
        final Parameter[] parameters = new Parameter[this.getParametersCount()];
        for (int i = 0; i < this.getParametersCount(); ++i) {
            parameters[i] = this.getParameter(i);
        }
        return parameters;
    }
}
