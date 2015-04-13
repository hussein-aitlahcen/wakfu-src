package com.ankamagames.wakfu.client.ui.systemMessage;

public class AdminSystemMessage extends AbstractSystemMessage
{
    public AdminSystemMessage() {
        super(WakfuSystemMessageManager.SystemMessageType.ADMIN_INFO, true, "adminMessageContainer", (short)10000);
    }
    
    @Override
    protected void applyTweens(final boolean in, final String id) {
    }
    
    @Override
    protected void clean(final String id) {
    }
}
