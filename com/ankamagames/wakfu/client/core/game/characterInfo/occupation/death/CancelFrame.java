package com.ankamagames.wakfu.client.core.game.characterInfo.occupation.death;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;

final class CancelFrame implements MessageFrame
{
    public static final CancelFrame INSTANCE;
    private MessageBoxControler m_deadMsgBoxController;
    private boolean m_chooseToReturnToPhoenix;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        this.m_deadMsgBoxController = null;
        this.m_chooseToReturnToPhoenix = false;
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (this.m_deadMsgBoxController != null) {
            this.m_deadMsgBoxController.messageBoxClosed(4, null);
        }
        this.m_deadMsgBoxController = null;
        this.m_chooseToReturnToPhoenix = false;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message.getId() != 19992) {
            return true;
        }
        this.displayMessageBox();
        return false;
    }
    
    public void displayMessageBox() {
        String message;
        if (WakfuGameEntity.getInstance().getLocalPlayer().getRespawnPointHandler().getSelectedPhoenix() == -1) {
            message = WakfuTranslator.getInstance().getString("death.resurrectMessage");
        }
        else {
            message = WakfuTranslator.getInstance().getString("death.backToPhoenixMessage");
        }
        final MessageBoxData data = new MessageBoxData(102, 1, message, 41L);
        final ArrayList<String> buttons = new ArrayList<String>();
        buttons.add(WakfuTranslator.getInstance().getString("death.waitForRaise"));
        data.setCustomMessages(buttons);
        data.setIconUrl(WakfuMessageBoxConstants.getMessageBoxIconUrl(0));
        data.setType(1);
        this.m_deadMsgBoxController = Xulor.getInstance().msgBox(data);
        if (this.m_deadMsgBoxController == null) {
            return;
        }
        this.m_deadMsgBoxController.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                switch (type) {
                    case 8: {
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new ReturnToPhoenixRequestMessage());
                        CancelFrame.this.m_chooseToReturnToPhoenix = true;
                    }
                    default: {}
                }
            }
        });
    }
    
    public boolean isChooseToReturnToPhoenix() {
        return this.m_chooseToReturnToPhoenix;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        INSTANCE = new CancelFrame();
    }
}
