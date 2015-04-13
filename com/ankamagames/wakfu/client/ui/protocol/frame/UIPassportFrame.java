package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.playerTitle.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.time.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.secret.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.core.*;

public class UIPassportFrame implements MessageFrame
{
    private static UIPassportFrame m_instance;
    private static final Logger m_logger;
    private final TimeTickListener m_tickListener;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UIPassportFrame() {
        super();
        this.m_tickListener = new TimeTickListener() {
            @Override
            public void tick() {
                final ClientCitizenComportment citizenComportment = (ClientCitizenComportment)WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment();
                citizenComportment.getView().updatePvpTimer();
            }
        };
    }
    
    public static UIPassportFrame getInstance() {
        return UIPassportFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17684: {
                final UIMessage msg = (UIMessage)message;
                final short previousTitle = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentTitle();
                final short newTitle = msg.getShortValue();
                if (previousTitle != newTitle) {
                    WakfuGameEntity.getInstance().getLocalPlayer().setCurrentTitle(newTitle);
                    final SelectPlayerTitleRequest req = new SelectPlayerTitleRequest();
                    req.setCurrentTitle(newTitle);
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(req);
                }
                return false;
            }
            default: {
                return true;
            }
        }
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
                    if (id.equals("passportDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIPassportFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            TimeManager.INSTANCE.addListener(this.m_tickListener);
            PropertiesProvider.getInstance().setPropertyValue("secrets", new SecretsView());
            PropertiesProvider.getInstance().setPropertyValue("passportViewMode", 0);
            PropertiesProvider.getInstance().setPropertyValue("passportCategory", 0);
            final EventDispatcher e = Xulor.getInstance().load("passportDialog", Dialogs.getDialogPath("passportDialog"), 1L, (short)10000);
            final PvpLadderEntryView view = PvpLadderEntryView.getOrCreate(WakfuGameEntity.getInstance().getLocalPlayer(), false);
            PropertiesProvider.getInstance().setLocalPropertyValue("pvpLadderEntry", view, e.getElementMap());
            Xulor.getInstance().putActionClass("wakfu.passport", PassportDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().unload("passportDialog");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            TimeManager.INSTANCE.removeListener(this.m_tickListener);
            PropertiesProvider.getInstance().removeProperty("secrets");
            Xulor.getInstance().removeActionClass("wakfu.passport");
        }
    }
    
    static {
        UIPassportFrame.m_instance = new UIPassportFrame();
        m_logger = Logger.getLogger((Class)UIPassportFrame.class);
    }
}
