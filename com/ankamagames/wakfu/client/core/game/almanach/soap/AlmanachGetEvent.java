package com.ankamagames.wakfu.client.core.game.almanach.soap;

import com.ankamagames.framework.net.soap.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import org.jetbrains.annotations.*;

public class AlmanachGetEvent extends SoapEntryLoader<GetEvent, GetEventResponse>
{
    public static final AlmanachGetEvent INSTANCE;
    
    private AlmanachGetEvent() {
        super(WakfuConfiguration.getInstance().getString("krosmozSOAPUrl", null), new GetEventResponse());
    }
    
    public void loadEntryFor(@NotNull final GameDateConst date) {
        ((SoapEntryLoader<GetEvent, U>)this).sendRequest(new GetEvent(date));
    }
    
    static {
        INSTANCE = new AlmanachGetEvent();
    }
}
