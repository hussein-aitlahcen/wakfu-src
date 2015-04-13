package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;
import java.util.*;

public class ClientGameEventParameterList extends ParameterList
{
    public ClientGameEventParameterList(final String name, final Parameter... parameters) {
        super(name, parameters);
    }
    
    public ClientGameEventParameterList(final Parameter... parameters) {
        super(parameters);
    }
    
    @Override
    public final Parameter[] getRawParameters() {
        final ArrayList<Parameter> raw = new ArrayList<Parameter>();
        for (int i = 0; i < this.getParametersCount(); ++i) {
            final Parameter parameter = this.getParameter(i);
            raw.add(new Parameter(parameter.getName()));
        }
        return raw.toArray(new Parameter[raw.size()]);
    }
}
