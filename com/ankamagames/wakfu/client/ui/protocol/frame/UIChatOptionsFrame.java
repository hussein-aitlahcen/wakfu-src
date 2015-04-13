package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.contentInitializer.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.*;

public class UIChatOptionsFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static UIChatOptionsFrame m_instance;
    private int m_selectedTabIndex;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UIChatOptionsFrame() {
        super();
        this.m_selectedTabIndex = 0;
    }
    
    public static UIChatOptionsFrame getInstance() {
        return UIChatOptionsFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 18110: {
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.restoreChatOptions"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            UIChatOptionsFrame.this.restoreChatOptions();
                        }
                    }
                });
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public void restoreChatOptions() {
        WakfuGameEntity.getInstance().removeFrame(UIChatFrame.getInstance());
        WakfuClientInstance.getInstance().getGamePreferences().restoreChatOptions();
        ChatWindowManager.getInstance().cleanAndDeletePreferences();
        try {
            ChatInitializer.initializeChatFromPreferences();
        }
        catch (Exception e) {
            UIChatOptionsFrame.m_logger.error((Object)"immpossible de r\u00e9tablir les param\u00e8tres par d\u00e9faut de chat !", (Throwable)e);
        }
        WakfuGameEntity.getInstance().pushFrame(UIChatFrame.getInstance());
    }
    
    @Override
    public long getId() {
        return 0L;
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
                    if (id.equals("chatOptionsDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIChatOptionsFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("chat.editedView", null);
            PropertiesProvider.getInstance().setPropertyValue("chat.viewOver", null);
            PropertiesProvider.getInstance().setPropertyValue("chat.colorEditedObject", null);
            final EventDispatcher eventDispatcher = Xulor.getInstance().load("chatOptionsDialog", Dialogs.getDialogPath("chatOptionsDialog"), 256L, (short)26000);
            if (eventDispatcher != null) {
                ((TabbedContainer)eventDispatcher.getElementMap().getElement("tabbedContainer")).setSelectedTabIndex(this.m_selectedTabIndex);
            }
            Xulor.getInstance().putActionClass("wakfu.chatOptions", ChatOptionsDialogActions.class);
            UIChatFrame.getInstance().selectChatOptions(true);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            UIChatFrame.getInstance().selectChatOptions(false);
            this.m_selectedTabIndex = 0;
            PropertiesProvider.getInstance().removeProperty("chat.editedView");
            PropertiesProvider.getInstance().removeProperty("chat.viewOver");
            PropertiesProvider.getInstance().removeProperty("chat.colorEditedObject");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("chatOptionsDialog");
            Xulor.getInstance().removeActionClass("wakfu.chatOptions");
        }
    }
    
    public void setSelectedTabIndex(final int i) {
        this.m_selectedTabIndex = i;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIChatOptionsFrame.class);
        UIChatOptionsFrame.m_instance = new UIChatOptionsFrame();
    }
}
