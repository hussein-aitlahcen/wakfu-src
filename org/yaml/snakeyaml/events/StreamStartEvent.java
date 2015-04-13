package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.*;

public final class StreamStartEvent extends Event
{
    public StreamStartEvent(final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
    }
    
    public boolean is(final ID id) {
        return ID.StreamStart == id;
    }
}
