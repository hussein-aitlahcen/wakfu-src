package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.chat.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.chat.clientToServer.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class UIModeratorChatFrame implements MessageFrame
{
    private static UIModeratorChatFrame m_instance;
    private static Logger m_logger;
    private String m_remoteUser;
    private long m_sourceId;
    private String m_firstMessage;
    private boolean m_isRunning;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIModeratorChatFrame getInstance() {
        return UIModeratorChatFrame.m_instance;
    }
    
    public void createChat(final String userName, final long sourceId, final String firstMessage) {
        if (!WakfuGameEntity.getInstance().hasFrame(this)) {
            this.m_remoteUser = userName;
            this.m_sourceId = sourceId;
            this.m_firstMessage = firstMessage;
            WakfuGameEntity.getInstance().pushFrame(this);
        }
    }
    
    private void askToCloseRequest() {
        if (this.m_isRunning) {
            final MessageBoxData data = new MessageBoxData(102, 0, WakfuTranslator.getInstance().getString("contactModerator.suretoclose"), 24L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        WakfuGameEntity.getInstance().removeFrame(UIModeratorChatFrame.this);
                    }
                }
            });
        }
        else {
            WakfuGameEntity.getInstance().removeFrame(this);
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19070: {
                this.askToCloseRequest();
                return false;
            }
            case 19071: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final byte closedReason = msg.getByteValue();
                final int idPrivate = ChatWindowManager.getInstance().getModeratorWindowId();
                String translatorKey = "contactModerator.closed";
                switch (closedReason) {
                    case 4: {
                        translatorKey = "contactModerator.closed";
                        break;
                    }
                    case 5: {
                        translatorKey = "contactModerator.disconnected";
                        break;
                    }
                }
                final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString(translatorKey));
                chatMessage.setPipeDestination(3);
                chatMessage.setWindowId(idPrivate);
                ChatManager.getInstance().pushMessage(chatMessage);
                return this.m_isRunning = false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("moderatorChatDialog")) {
                        UIModeratorChatFrame.this.askToCloseRequest();
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("moderatorChatDialog", Dialogs.getDialogPath("moderatorChatDialog"), 40977L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.moderatorChat", ModeratorChatDialogActions.class);
            final Message msg = new UIChatCreationRequestMessage((short)19067, this.m_remoteUser, new Listener(this.m_firstMessage, this.m_sourceId, this.m_remoteUser));
            Worker.getInstance().pushMessage(msg);
            this.m_isRunning = true;
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            UIChatFrameHelper.closeModeratorChatWindow();
            Xulor.getInstance().unload("moderatorChatDialog");
            Xulor.getInstance().removeActionClass("wakfu.moderatorChat");
            if (this.m_isRunning) {
                final Message notifyToOtherSide = new CloseModeratorRequestMessage();
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(notifyToOtherSide);
                this.m_isRunning = false;
            }
        }
    }
    
    static {
        UIModeratorChatFrame.m_instance = new UIModeratorChatFrame();
        UIModeratorChatFrame.m_logger = Logger.getLogger((Class)UIModeratorChatFrame.class);
    }
    
    protected static class Listener implements ChatViewEventListener
    {
        private final String m_message;
        private final long m_sourceId;
        private final String m_sourceName;
        
        protected Listener(final String message, final long sourceId, final String sourceName) {
            super();
            this.m_sourceId = sourceId;
            this.m_message = message;
            this.m_sourceName = sourceName;
        }
        
        @Override
        public void onCreation(final ChatViewManager manager) {
            final int idPrivate = ChatWindowManager.getInstance().getModeratorWindowId();
            final GameDateConst tmp = BaseGameDateProvider.INSTANCE.getDate();
            final String time = "[" + tmp.getHours() + ':' + tmp.getMinutes() + "] ";
            final TextWidgetFormater msg = new TextWidgetFormater();
            msg.openText();
            msg.addColor(new Color(0.84f, 0.27f, 0.29f, 1.0f));
            msg.append(time);
            msg.b().u();
            msg.addId("characterName_" + this.m_sourceId);
            msg.append(this.m_sourceName);
            msg._u()._b();
            msg.append(WakfuTranslator.getInstance().getString("colon")).append(this.m_message);
            msg.closeText();
            final ChatMessage chatMessage = new ChatMessage(msg.finishAndToString());
            chatMessage.setPipeDestination(3);
            chatMessage.setWindowId(idPrivate);
            ChatManager.getInstance().pushMessage(chatMessage);
        }
    }
}
