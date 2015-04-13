package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.graphics.chat.*;

@XulorActionsTag
public class ChatOptionsDialogActions
{
    protected static final Logger m_logger;
    public static final String PACKAGE = "wakfu.chatOptions";
    
    public static void createView(final Event event) {
        final ComboBox comboBox = (ComboBox)event.getCurrentTarget().getElementMap().getElement("windowsComboBox");
        try {
            final ChatWindowManager manager = ChatWindowManager.getInstance();
            manager.getWindow((int)comboBox.getSelectedValue()).createView();
            PropertiesProvider.getInstance().firePropertyValueChanged(ChatWindowManager.getInstance(), ChatWindowManager.FIELDS);
            ChatWindowManager.getInstance().saveChatConfiguration();
        }
        catch (Exception e) {
            ChatOptionsDialogActions.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    public static void selectView(final ItemEvent event) {
        PropertiesProvider.getInstance().setPropertyValue("chat.editedView", event.getItemValue());
    }
    
    public static void transferView(final ListSelectionChangedEvent e) {
        final ChatView chatView = (ChatView)PropertiesProvider.getInstance().getObjectProperty("chat.editedView");
        final int windowId = (int)e.getRenderableContainer().getItemValue();
        final int windowFromId = ChatWindowManager.getInstance().getWindowIdFromView(chatView);
        if (windowId == windowFromId) {
            return;
        }
        ChatWindowManager.getInstance().transferChatView(chatView, windowFromId, windowId);
        PropertiesProvider.getInstance().firePropertyValueChanged("chat.editedView", "windowId");
        ChatWindowManager.getInstance().saveChatConfiguration();
    }
    
    public static void changeChannel(final ListSelectionChangedEvent e) {
        final ChatView chatView = (ChatView)PropertiesProvider.getInstance().getObjectProperty("chat.editedView");
        final ChatPipeWrapper chatPipeWrapper = (ChatPipeWrapper)e.getRenderableContainer().getItemValue();
        ChatWindowManager.getInstance().saveChatConfiguration();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChatOptionsDialogActions.class);
    }
}
