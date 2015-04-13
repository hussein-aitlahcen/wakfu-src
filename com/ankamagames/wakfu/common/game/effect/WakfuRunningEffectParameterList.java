package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.framework.external.*;
import java.util.*;

public class WakfuRunningEffectParameterList extends ParameterList
{
    public WakfuRunningEffectParameterList(final String name, final WakfuRunningEffectParameter... parameters) {
        super(name, (Parameter[])parameters);
    }
    
    public WakfuRunningEffectParameterList(final WakfuRunningEffectParameter... parameters) {
        super((Parameter[])parameters);
    }
    
    @Override
    public final Parameter[] getRawParameters() {
        final ArrayList<Parameter> raw = new ArrayList<Parameter>();
        for (int i = 0; i < this.getParametersCount(); ++i) {
            final Parameter parameter = this.getParameter(i);
            raw.add(new Parameter(parameter.getName() + " (base)"));
            raw.add(new Parameter(parameter.getName() + " (incr)"));
        }
        final Parameter[] result = new Parameter[raw.size()];
        return raw.toArray(result);
    }
}
