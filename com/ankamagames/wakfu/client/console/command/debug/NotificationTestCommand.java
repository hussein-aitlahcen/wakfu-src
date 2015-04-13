package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

public class NotificationTestCommand implements Command
{
    protected static final Logger m_logger;
    private Runnable m_runnable;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (this.m_runnable == null) {
            this.m_runnable = new Runnable() {
                @Override
                public void run() {
                    final String title = WakfuTranslator.getInstance().getString("notification.skillLevelUpTitle", "Catin");
                    final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.skillLevelUpText", "Catin", 10), NotificationMessageType.CRAFT);
                    final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.CRAFT);
                    Worker.getInstance().pushMessage(uiNotificationMessage);
                }
            };
            ProcessScheduler.getInstance().schedule(this.m_runnable, 5000L, 25);
            manager.trace("Test started.");
        }
        else {
            ProcessScheduler.getInstance().remove(this.m_runnable);
            this.m_runnable = null;
            manager.trace("Test stopped.");
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NotificationTestCommand.class);
    }
}
