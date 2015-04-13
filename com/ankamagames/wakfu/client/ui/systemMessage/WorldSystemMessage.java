package com.ankamagames.wakfu.client.ui.systemMessage;

import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;

public class WorldSystemMessage extends AbstractSystemMessage
{
    public WorldSystemMessage() {
        super(WakfuSystemMessageManager.SystemMessageType.WORLD_INFO, true, "messageContainer", (short)10000);
    }
    
    @Override
    protected void applyTweens(final boolean in, final String id) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(id);
        if (map == null) {
            return;
        }
        this.applyTween((Widget)map.getElement("text"), in);
    }
    
    @Override
    protected void clean(final String id) {
    }
}
