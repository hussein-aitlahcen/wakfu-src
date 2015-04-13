package com.ankamagames.wakfu.client.ui.protocol.message.worldScene;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.*;

public class UIWorldSceneMouseMessage extends UIMessage
{
    private int m_mouseButton;
    private int m_mouseX;
    private int m_mouseY;
    
    public UIWorldSceneMouseMessage(final int type, final int mouseX, final int mouseY, final int mouseButton) {
        super();
        this.m_mouseX = mouseX;
        this.m_mouseY = mouseY;
        this.m_mouseButton = mouseButton;
        this.setId(type);
    }
    
    public UIWorldSceneMouseMessage() {
        super();
    }
    
    public static void sendPressed(final int mouseX, final int mouseY, final int mouseButton) {
        send((short)19998, mouseX, mouseY, mouseButton);
    }
    
    public static void sendReleased(final int mouseX, final int mouseY, final int mouseButton) {
        send((short)19992, mouseX, mouseY, mouseButton);
    }
    
    public static void sendMove(final int mouseX, final int mouseY) {
        send((short)19994, mouseX, mouseY, 0);
    }
    
    public static void sendMoveExtended(final int mouseX, final int mouseY) {
        send((short)19995, mouseX, mouseY, 0);
    }
    
    private static void send(final short type, final int mouseX, final int mouseY, final int mouseButton) {
        final UIWorldSceneMouseMessage msg = new UIWorldSceneMouseMessage(type, mouseX, mouseY, mouseButton);
        assert msg.getId() == type;
        Worker.getInstance().pushMessage(msg);
    }
    
    public UIWorldSceneMouseMessage duplicate() {
        final UIWorldSceneMouseMessage msg = new UIWorldSceneMouseMessage();
        msg.setId(this.getId());
        msg.m_mouseX = this.m_mouseX;
        msg.m_mouseY = this.m_mouseY;
        msg.m_mouseButton = this.m_mouseButton;
        return msg;
    }
    
    public boolean isButtonLeft() {
        return this.m_mouseButton == 1;
    }
    
    public boolean isButtonMiddle() {
        return this.m_mouseButton == 2;
    }
    
    public boolean isButtonRight() {
        return this.m_mouseButton == 3;
    }
    
    public int getMouseX() {
        return this.m_mouseX;
    }
    
    public int getMouseY() {
        return this.m_mouseY;
    }
    
    private static boolean isButtonDown(final int mouseButton) {
        return ((WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene()).isButtonDown(mouseButton);
    }
    
    public static boolean isLeftButtonDown() {
        return isButtonDown(1);
    }
    
    public static boolean isRightButtonDown() {
        return isButtonDown(3);
    }
}
