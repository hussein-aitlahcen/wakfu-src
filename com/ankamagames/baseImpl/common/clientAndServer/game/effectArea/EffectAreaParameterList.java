package com.ankamagames.baseImpl.common.clientAndServer.game.effectArea;

import com.ankamagames.framework.external.*;

public class EffectAreaParameterList extends ParameterList
{
    public EffectAreaParameterList(final String name, final Parameter... parameters) {
        super(name, parameters);
    }
    
    public EffectAreaParameterList(final Parameter... parameters) {
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
