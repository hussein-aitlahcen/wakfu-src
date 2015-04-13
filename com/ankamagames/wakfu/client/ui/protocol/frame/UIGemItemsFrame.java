package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gems.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import com.ankamagames.xulor2.core.messagebox.*;

public class UIGemItemsFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final UIGemItemsFrame INSTANCE;
    Item m_currentItem;
    private GemItemsDragNDropListener m_dndListener;
    private GemEquipmentInventoryObserver m_equipmentInventoryObserver;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIGemItemsFrame getInstance() {
        return UIGemItemsFrame.INSTANCE;
    }
    
    public void setCurrentItem(final Item item) {
        this.m_currentItem = item;
        PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", item, "gemItemDialog");
    }
    
    public long getCurrentItemId() {
        return (this.m_currentItem != null) ? this.m_currentItem.getUniqueId() : -1L;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("gemItemDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIGemItemsFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("gemItemDialog", Dialogs.getDialogPath("gemItemDialog"), 17L, (short)10000);
            PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", this.m_currentItem, "gemItemDialog");
            Xulor.getInstance().putActionClass("wakfu.gemItems", GemItemsDialogActions.class);
            EquipmentDialogActions.addListener(this.m_dndListener = new GemItemsDragNDropListener());
            this.m_equipmentInventoryObserver = new GemEquipmentInventoryObserver();
            WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory().addObserver(this.m_equipmentInventoryObserver);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeActionClass("wakfu.gemItems");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            if (Xulor.getInstance().isLoaded("gemItemDialog")) {
                Xulor.getInstance().unload("gemItemDialog");
            }
            EquipmentDialogActions.removeListener(this.m_dndListener);
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer != null) {
                localPlayer.getEquipmentInventory().removeObserver(this.m_equipmentInventoryObserver);
            }
            this.m_dndListener = null;
            this.m_equipmentInventoryObserver = null;
            this.m_currentItem = null;
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16870: {
                final UISocketGemMessage msg = (UISocketGemMessage)message;
                final Item gem = msg.getGem();
                final Item item = msg.getItem();
                final byte index = msg.getByteValue();
                if (this.m_currentItem == null) {
                    return false;
                }
                if (this.m_currentItem.getQuantity() != 1) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.cantSocketGemOnStackedItem"), 3);
                    return false;
                }
                if (!this.m_currentItem.hasGems()) {
                    return false;
                }
                final Gems gems = this.m_currentItem.getGems();
                if (gem.getLevel() < this.m_currentItem.getLevel()) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.gemIsTooLowLevel"), 3);
                    return false;
                }
                if (!gems.canGem(gem.getReferenceItem(), index)) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.cantSocketGem"), 3);
                    return false;
                }
                final String msgBoxMsg = WakfuTranslator.getInstance().getString("question.socketGem.emptySlot");
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(msgBoxMsg, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final GemRequestMessage netMsg = new GemRequestMessage();
                            netMsg.setGemItemId(gem.getUniqueId());
                            netMsg.setGemmedItemId(UIGemItemsFrame.this.m_currentItem.getUniqueId());
                            netMsg.setIndex(index);
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
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
    
    static {
        m_logger = Logger.getLogger((Class)UIGemItemsFrame.class);
        INSTANCE = new UIGemItemsFrame();
    }
}
