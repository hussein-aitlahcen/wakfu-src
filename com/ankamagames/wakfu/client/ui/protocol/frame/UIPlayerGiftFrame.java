package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.message.playerGift.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gift.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.gift.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UIPlayerGiftFrame implements MessageFrame
{
    private static UIPlayerGiftFrame m_instance;
    protected static Logger m_logger;
    
    public static UIPlayerGiftFrame getInstance() {
        return UIPlayerGiftFrame.m_instance;
    }
    
    public void setNetEnableGifts(final boolean enable) {
        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("giftLock", enable);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16623: {
                if (WakfuGameEntity.getInstance().getLocalPlayer().isActiveProperty(WorldPropertyType.GIFT_MANAGE_DISABLED)) {
                    ChatWorldPropertyTypeErrorManager.writeChatErrorMessage(WorldPropertyType.GIFT_MANAGE_DISABLED.getId());
                    return false;
                }
                final UIPlayerGiftPackageMessage msg = (UIPlayerGiftPackageMessage)message;
                GiftManager.getInstance().setSelectedPackage(msg.getPackage());
                return false;
            }
            case 16622: {
                if (WakfuGameEntity.getInstance().getLocalPlayer().isActiveProperty(WorldPropertyType.GIFT_MANAGE_DISABLED)) {
                    ChatWorldPropertyTypeErrorManager.writeChatErrorMessage(WorldPropertyType.GIFT_MANAGE_DISABLED.getId());
                    return false;
                }
                final UIPlayerGiftMessage msg2 = (UIPlayerGiftMessage)message;
                final GiftItem gift = msg2.getGift();
                final boolean consumeAll = msg2.getBooleanValue();
                GiftManager.getInstance().requestConsume(gift, consumeAll);
                this.setNetEnableGifts(false);
                final GiftConsumeRequestMessage netMessage = new GiftConsumeRequestMessage();
                netMessage.setGiftGuid(gift.getGuid());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                return false;
            }
            case 16620: {
                if (WakfuGameEntity.getInstance().getLocalPlayer().isActiveProperty(WorldPropertyType.GIFT_MANAGE_DISABLED)) {
                    ChatWorldPropertyTypeErrorManager.writeChatErrorMessage(WorldPropertyType.GIFT_MANAGE_DISABLED.getId());
                    return false;
                }
                if (Xulor.getInstance().isLoaded("playerGiftDialog")) {
                    Xulor.getInstance().unload("playerGiftDialog");
                    NetEnabledWidgetManager.INSTANCE.destroyGroup("giftLock");
                }
                else {
                    NetEnabledWidgetManager.INSTANCE.createGroup("giftLock");
                    Xulor.getInstance().load("playerGiftDialog", Dialogs.getDialogPath("playerGiftDialog"), 32769L, (short)10000);
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
            PropertiesProvider.getInstance().setPropertyValue("playerGift", GiftManager.getInstance());
            Xulor.getInstance().putActionClass("wakfu.playerGift", PlayerGiftDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            PropertiesProvider.getInstance().setPropertyValue("playerGift", null);
            Xulor.getInstance().unload("playerGiftDialog");
            Xulor.getInstance().removeActionClass("wakfu.playerGift");
        }
    }
    
    static {
        UIPlayerGiftFrame.m_instance = new UIPlayerGiftFrame();
        UIPlayerGiftFrame.m_logger = Logger.getLogger((Class)UIPlayerGiftFrame.class);
    }
}
