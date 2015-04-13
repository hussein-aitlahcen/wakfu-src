package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public interface ClientGameEvent extends Parameterized
{
    public static final String[] NO_PROPERTIES = new String[0];
    
    int getId();
    
    String[] getProperties();
}
