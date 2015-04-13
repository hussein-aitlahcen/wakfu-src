package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.travel.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.travel.provider.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UIBoatFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIBoatFrame m_instance;
    private final BoatTicketOfficeFieldProvider m_boatFieldProvider;
    private TravelMachine m_boat;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UIBoatFrame() {
        super();
        this.m_boatFieldProvider = new BoatTicketOfficeFieldProvider();
    }
    
    public static UIBoatFrame getInstance() {
        return UIBoatFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (UIFrameMouseKey.isKeyOrMouseMessage(message)) {
            return false;
        }
        switch (message.getId()) {
            case 19312: {
                final UIMessage msg = (UIMessage)message;
                final long destinationBoatId = msg.getLongValue();
                final int cost = msg.getIntValue();
                final MessageBoxControler controller = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("zaap.confirmTravel", cost), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                if (controller != null) {
                    controller.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                final LocalPlayerCharacter user = WakfuGameEntity.getInstance().getLocalPlayer();
                                final BoatTravelProvider provider = TravelHelper.getProvider(TravelType.BOAT);
                                provider.travel(user, UIBoatFrame.this.m_boat, destinationBoatId);
                                WakfuGameEntity.getInstance().removeFrame(UIBoatFrame.this);
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
    
    public void initialize(final TravelMachine boat) {
        this.m_boat = boat;
        this.m_boatFieldProvider.initialise(boat.getId(), boat);
        WakfuGameEntity.getInstance().pushFrame(this);
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
                        WakfuGameEntity.getInstance().removeFrame(UIBoatFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("ticketOffice", this.m_boatFieldProvider);
            PropertiesProvider.getInstance().setPropertyValue("boatTicketOffice", true);
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
            PropertiesProvider.getInstance().removeProperty("boatTicketOffice");
            Xulor.getInstance().removeActionClass("wakfu.zaap");
            this.m_boat = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIBoatFrame.class);
        UIBoatFrame.m_instance = new UIBoatFrame();
    }
}
