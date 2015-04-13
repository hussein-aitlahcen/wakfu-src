package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.chat.*;

public class GetPrivateContactCommand implements Command
{
    private static final Logger m_logger;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (args.get(2) != null && args.get(2).length() > 0) {
            execute(args.get(2).charAt(0) == '1');
        }
    }
    
    public static void execute(final boolean forward) {
        final String privateContact = ChatManager.getInstance().getLastPrivateContact(forward);
        if (privateContact != null && privateContact.length() > 0) {
            final int id = ChatWindowManager.getInstance().getCurrentWindow().getWindowId();
            final String currentChatDialogId = UIChatFrameHelper.getDialogIdFromWindowId(id);
            final TextEditor chatTextEditor = (TextEditor)Xulor.getInstance().getEnvironment().getElementMap(currentChatDialogId).getElement(((id == 0) ? "textEditorRenderableContainer." : "") + "chatInput");
            if (chatTextEditor == null) {
                return;
            }
            final ChatView view0 = ChatWindowManager.getInstance().getCurrentWindow().getCurrentView();
            view0.setFieldValue("input", "/w \"" + privateContact + "\" ");
            PropertiesProvider.getInstance().firePropertyValueChanged(view0, "input");
            if (!chatTextEditor.equals(FocusManager.getInstance().getFocused())) {
                FocusManager.getInstance().setFocused(chatTextEditor);
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetPrivateContactCommand.class);
    }
}
