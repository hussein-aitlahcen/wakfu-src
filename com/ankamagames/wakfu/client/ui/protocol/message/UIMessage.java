package com.ankamagames.wakfu.client.ui.protocol.message;

import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class UIMessage extends AbstractUIMessage
{
    public UIMessage() {
        super(WakfuGameEntity.getInstance());
    }
    
    public UIMessage(final short id) {
        super(WakfuGameEntity.getInstance());
        this.setId(id);
    }
    
    public static void send(final short messageID) {
        Worker.getInstance().pushMessage(new UIMessage(messageID));
    }
    
    public static void send(final short messageID, final Object value) {
        final AbstractUIMessage message = new UIMessage(messageID);
        message.setObjectValue(value);
        Worker.getInstance().pushMessage(message);
    }
}
