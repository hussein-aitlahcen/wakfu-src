package com.ankamagames.wakfu.client.ui.systemMessage;

public class NotificationSystemMessage extends AbstractSystemMessage
{
    public NotificationSystemMessage() {
        super(WakfuSystemMessageManager.SystemMessageType.SYS_INFO, true, "notificationMessageContainer", (short)10000);
    }
    
    @Override
    protected void applyTweens(final boolean in, final String id) {
    }
    
    @Override
    protected void clean(final String id) {
    }
}
