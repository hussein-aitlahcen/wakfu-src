package com.ankamagames.wakfu.client.network.event;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.net.netty.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;

public class WakfuNetworkEventsHandler implements NetworkEventsHandler
{
    private static final Logger LOG;
    
    @Override
    public boolean onConnectionClose(final ConnectionCtx connection) {
        WakfuNetworkEventsHandler.LOG.info((Object)"Connexion avec le serveur perdue.");
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                final byte disconnectionReason = WakfuGameEntity.getInstance().getDisconnectionReason();
                if (disconnectionReason == 20) {
                    WakfuProgressMonitorManager.getInstance().done();
                    NetEnabledWidgetManager.INSTANCE.setGroupEnabled("loginLock", true);
                    return;
                }
                String message = null;
                switch (disconnectionReason) {
                    case 1: {
                        message = WakfuTranslator.getInstance().getString("disconnection.spam");
                        break;
                    }
                    case 2: {
                        message = WakfuTranslator.getInstance().getString("disconnection.timeout");
                        break;
                    }
                    case 3: {
                        message = WakfuTranslator.getInstance().getString("disconnection.kickedByReco");
                        break;
                    }
                    case 4: {
                        message = WakfuTranslator.getInstance().getString("disconnection.kickedByAdmin");
                        break;
                    }
                    case 18: {
                        message = WakfuTranslator.getInstance().getString("disconnection.accountBanned");
                        break;
                    }
                    case 5: {
                        message = WakfuTranslator.getInstance().getString("disconnection.bannedByAdmin");
                        break;
                    }
                    case 6: {
                        message = WakfuTranslator.getInstance().getString("disconnection.architectureNotReady");
                        break;
                    }
                    case 7: {
                        message = WakfuTranslator.getInstance().getString("disconnection.sessionDestroyed");
                        break;
                    }
                    case 8: {
                        message = WakfuTranslator.getInstance().getString("disconnection.remoteServerDoesNotAnswer");
                        break;
                    }
                    case 9: {
                        message = WakfuTranslator.getInstance().getString("disconnection.serverShutdown");
                        break;
                    }
                    case 10: {
                        message = WakfuTranslator.getInstance().getString("disconnection.invalidPosition");
                        break;
                    }
                    case 11: {
                        message = WakfuTranslator.getInstance().getString("disconnection.openOfflineFlea");
                        break;
                    }
                    case 13: {
                        message = WakfuTranslator.getInstance().getString("disconnection.unknown");
                        break;
                    }
                    case 12: {
                        message = WakfuTranslator.getInstance().getString("disconnection.serverError");
                        break;
                    }
                    case 14: {
                        message = WakfuTranslator.getInstance().getString("disconnection.synchronisationError");
                        break;
                    }
                    case 17: {
                        message = WakfuTranslator.getInstance().getString("disconnection.timedSessionEnd");
                        break;
                    }
                    case 16: {
                        message = WakfuTranslator.getInstance().getString("disconnection.serverFull");
                        break;
                    }
                    default: {
                        message = WakfuTranslator.getInstance().getString("connection.closed");
                        break;
                    }
                }
                ProcessScheduler.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (WakfuGameEntity.getInstance().isLogged()) {
                            WakfuClientInstance.getInstance().cleanUp();
                            WakfuClientInstance.getInstance().start();
                        }
                        if (disconnectionReason != 0) {
                            Xulor.getInstance().msgBox(message, WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1026L, 1, 1);
                        }
                        WakfuProgressMonitorManager.getInstance().done();
                        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("loginLock", true);
                    }
                }, 20L, 1);
            }
        });
        return true;
    }
    
    @Override
    public boolean onNewConnection(final ConnectionCtx connection) {
        WakfuNetworkEventsHandler.LOG.info((Object)"onNewConnection");
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                final WakfuGameEntity gameEntity = WakfuGameEntity.getInstance();
                gameEntity.pushFrame(NetAuthenticationFrame.getInstance());
                gameEntity.pushFrame(NetConnectionQueueFrame.INSTANCE);
                gameEntity.logon();
            }
        }, 20L, 1);
        return true;
    }
    
    static {
        LOG = Logger.getLogger((Class)WakfuNetworkEventsHandler.class);
    }
}
