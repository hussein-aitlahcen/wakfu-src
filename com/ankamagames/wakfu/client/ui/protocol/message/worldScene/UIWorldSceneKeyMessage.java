package com.ankamagames.wakfu.client.ui.protocol.message.worldScene;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import java.awt.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class UIWorldSceneKeyMessage extends UIMessage
{
    private KeyEvent m_keyEvent;
    
    UIWorldSceneKeyMessage(final int type, final KeyEvent keyEvent) {
        super();
        this.m_keyEvent = keyEvent;
        this.setId(type);
    }
    
    public static void sendPressed(final KeyEvent keyEvent) {
        send((short)19990, keyEvent);
    }
    
    public static void sendReleased(final KeyEvent keyEvent) {
        send((short)19991, keyEvent);
    }
    
    private static void send(final short type, final KeyEvent keyEvent) {
        final UIWorldSceneKeyMessage msg = new UIWorldSceneKeyMessage(type, keyEvent);
        assert msg.getId() == type;
        Worker.getInstance().pushMessage(msg);
    }
    
    public KeyEvent getKeyEvent() {
        return this.m_keyEvent;
    }
}
