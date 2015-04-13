package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.travel.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.travel.provider.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UIZaapFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIZaapFrame m_instance;
    private final ZaapTicketOfficeFieldProvider m_zaapFieldProvider;
    private TravelMachine m_zaap;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UIZaapFrame() {
        super();
        this.m_zaapFieldProvider = new ZaapTicketOfficeFieldProvider();
    }
    
    public static UIZaapFrame getInstance() {
        return UIZaapFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (UIFrameMouseKey.isKeyOrMouseMessage(message)) {
            return false;
        }
        switch (message.getId()) {
            case 19312: {
                final UIMessage msg = (UIMessage)message;
                final long destinationZaapId = msg.getLongValue();
                final int cost = msg.getIntValue();
                final MessageBoxControler controller = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("zaap.confirmTravel", cost), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                if (controller != null) {
                    controller.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                final LocalPlayerCharacter user = WakfuGameEntity.getInstance().getLocalPlayer();
                                final ZaapTravelProvider provider = TravelHelper.getProvider(UIZaapFrame.this.m_zaap.getTravelType());
                                provider.travel(user, UIZaapFrame.this.m_zaap, destinationZaapId);
                            }
                        }
                    });
                    return false;
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public void initialize(final TravelMachine zaap) {
        this.m_zaap = zaap;
        if (zaap.getTravelType() == TravelType.ZAAP) {
            this.m_zaapFieldProvider.initialise(zaap.getId());
        }
        else if (zaap.getTravelType() == TravelType.ZAAP_OUT_ONLY) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            this.m_zaapFieldProvider.initialiseForZaapOut(zaap.getId(), ((HavenWorldTravelMachine)zaap).getTravelCost(localPlayer.getGuildHandler().getGuildId()));
        }
        else {
            UIZaapFrame.m_logger.error((Object)"Type de voyage inconnu, on ne peut pas initialiser le provider");
        }
        WakfuGameEntity.getInstance().pushFrame(this);
    }
    
    public void hideDialog() {
        final Widget w = (Widget)Xulor.getInstance().getLoadedElement("zaapDialog");
        if (w != null) {
            w.setVisible(false);
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
                    if (id.equals("zaapDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIZaapFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("ticketOffice", this.m_zaapFieldProvider);
            PropertiesProvider.getInstance().setPropertyValue("boatTicketOffice", false);
            Xulor.getInstance().load("zaapDialog", Dialogs.getDialogPath("zaapDialog"), 1L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.zaap", ZaapDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("zaapDialog");
            PropertiesProvider.getInstance().removeProperty("ticketOffice");
            Xulor.getInstance().removeActionClass("wakfu.zaap");
            this.m_zaap = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIZaapFrame.class);
        UIZaapFrame.m_instance = new UIZaapFrame();
    }
}
