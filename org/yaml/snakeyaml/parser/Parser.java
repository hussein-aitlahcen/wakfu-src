package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.events.*;

public interface Parser
{
    boolean checkEvent(Event.ID p0);
    
    Event peekEvent();
    
    Event getEvent();
}
