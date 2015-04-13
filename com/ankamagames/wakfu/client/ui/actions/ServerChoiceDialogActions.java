package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.proxyGroup.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.message.server.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;

@XulorActionsTag
public class ServerChoiceDialogActions
{
    public static final String PACKAGE = "wakfu.serverChoice";
    
    public static void backToAuthentication(final Event event) {
        WakfuGameEntity.getInstance().removeFrame(UIServerChoiceFrame.INSTANCE);
        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("loginLock", true);
        WakfuGameEntity.getInstance().pushFrame(UIAuthentificationFrame.getInstance());
    }
    
    public static void selectServer(final Event event, final WakfuServerView reference) {
        PropertiesProvider.getInstance().setPropertyValue("serverChoice.selectedServerReference", reference);
        PropertiesProvider.getInstance().setPropertyValue("serverChoice.overServerReference", null);
        UIServerChoiceFrame.INSTANCE.setSelectedServerDirty(true);
    }
    
    public static void forceAccount(final Event event, final WakfuServerView reference) {
        final MessageBoxData data = new MessageBoxData(102, 1, "Saisissez l'ID de compte \u00e0 forcer :", 65542L);
        final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 2) {
                    final UIServerReferenceMessage message = new UIServerReferenceMessage();
                    message.setServerReference(reference);
                    message.setForcedAccountId(Long.parseLong(userEntry));
                    message.setId(16490);
                    Worker.getInstance().pushMessage(message);
                }
            }
        });
    }
    
    public static void filterServerList(final Event event) {
        UIServerChoiceFrame.INSTANCE.toggleServerFiltered();
    }
    
    public static void validServer(final Event event, final WakfuServerView reference) {
        final UIServerReferenceMessage message = new UIServerReferenceMessage();
        message.setServerReference(reference);
        message.setId(16490);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void validServerDoubleClick(final Event event, final WakfuServerView reference) {
        if (event != null && reference != null) {
            final UIServerReferenceMessage message = new UIServerReferenceMessage();
            message.setServerReference(reference);
            message.setId(16490);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void mouseEnterOnServer(final Event event, final WakfuServerView serverPreference) {
        PropertiesProvider.getInstance().setPropertyValue("serverChoice.overServerReference", serverPreference);
    }
    
    public static void mouseExitOffServer(final Event event) {
        PropertiesProvider.getInstance().setPropertyValue("serverChoice.overServerReference", null);
    }
    
    public static void startScrollLeft(final Event e) {
        UIServerChoiceFrame.INSTANCE.setScrollMode(ListScroller.ScrollMode.LEFT);
    }
    
    public static void startScrollRight(final Event e) {
        UIServerChoiceFrame.INSTANCE.setScrollMode(ListScroller.ScrollMode.RIGHT);
    }
    
    public static void stopScroll(final Event e) {
        UIServerChoiceFrame.INSTANCE.setScrollMode(ListScroller.ScrollMode.STOPPED);
    }
    
    public static void openSteamLinkAccountUrlInBrowser(final Event e) {
    }
}
