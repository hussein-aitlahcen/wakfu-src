package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.havenWorld.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.framework.reflect.*;

public class UIHavenWorldBidFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIHavenWorldBidFrame m_instance;
    private HavenWorldAuctionView m_auctionView;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIHavenWorldBidFrame getInstance() {
        return UIHavenWorldBidFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19362: {
                final String msgText = WakfuTranslator.getInstance().getString("question.havenWorldBidConfirm", this.m_auctionView.getNextBidValue());
                final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
                final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final HavenWorldBidRequestMessage havenWorldBidRequestMessage = new HavenWorldBidRequestMessage(UIHavenWorldBidFrame.this.m_auctionView.getHavenWorldId(), UIHavenWorldBidFrame.this.m_auctionView.getNextBidValue());
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(havenWorldBidRequestMessage);
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
            if (this.m_auctionView == null) {
                UIHavenWorldBidFrame.m_logger.error((Object)"AuctionView null on ne peut pas afficher le panneau d'achat !");
                return;
            }
            final HavenWorldAuctionView[] arrayView = { this.m_auctionView };
            PropertiesProvider.getInstance().setPropertyValue("havenWorldAuctionList", arrayView);
            PropertiesProvider.getInstance().setPropertyValue("havenWorldAuction", this.m_auctionView);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("havenWorldBidDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIHavenWorldBidFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("havenWorldBidDialog", Dialogs.getDialogPath("havenWorldBidDialog"), 256L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.havenWorldBid", HavenWorldBidDialogActions.class);
            WakfuGameEntity.getInstance().pushFrame(NetHavenWorldAuctionFrame.INSTANCE);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            PropertiesProvider.getInstance().removeProperty("havenWorldAuction");
            PropertiesProvider.getInstance().removeProperty("havenWorldAuctionList");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("havenWorldBidDialog");
            Xulor.getInstance().removeActionClass("wakfu.havenWorldBid");
            WakfuGameEntity.getInstance().removeFrame(NetHavenWorldAuctionFrame.INSTANCE);
        }
    }
    
    public void setAuctionView(final HavenWorldAuctionView auctionView) {
        this.m_auctionView = auctionView;
    }
    
    public void updateAuctionInfo(final long guildId, final String guildName, final int bidValue) {
        this.m_auctionView.setGuildId(guildId);
        this.m_auctionView.setGuildName(guildName);
        this.m_auctionView.setBidValue(bidValue);
        PropertiesProvider.getInstance().firePropertyValueChanged(this.m_auctionView, "currentBid", "nextBid", "guildName", "hasRightToBid");
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIHavenWorldBidFrame.class);
        UIHavenWorldBidFrame.m_instance = new UIHavenWorldBidFrame();
    }
}
