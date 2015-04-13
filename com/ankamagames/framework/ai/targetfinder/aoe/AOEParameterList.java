package com.ankamagames.framework.ai.targetfinder.aoe;

import com.ankamagames.framework.external.*;
import java.util.*;

public class AOEParameterList extends ParameterList
{
    public AOEParameterList(final String name, final Parameter... parameters) {
        super(name, parameters);
    }
    
    public AOEParameterList(final Parameter... parameters) {
        super(parameters);
    }
    
    @Override
    public final Parameter[] getRawParameters() {
        final ArrayList<Parameter> raw = new ArrayList<Parameter>();
        for (int i = 0; i < this.getParametersCount(); ++i) {
            final Parameter parameter = this.getParameter(i);
            raw.add(new Parameter(parameter.getName()));
        }
        return raw.toArray(new Parameter[this.getParametersCount()]);
    }
}
