package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.core.messagebox.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.guild.storage.*;
import com.ankamagames.wakfu.client.core.game.storageBox.guild.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.storageBox.*;
import com.ankamagames.wakfu.common.game.inventory.action.*;
import com.ankamagames.wakfu.common.rawData.*;

public class UIStorageBoxFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIStorageBoxFrame m_instance;
    private StorageBoxView m_storageBoxBoxView;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIStorageBoxFrame getInstance() {
        return UIStorageBoxFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("storageBoxDialog") || id.equals("equipmentDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIStorageBoxFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            WakfuGameEntity.getInstance().removeFrame(UIWorldInteractionFrame.getInstance());
            UIEquipmentFrame.getInstance().openEquipment();
            if (this.m_storageBoxBoxView != null && this.m_storageBoxBoxView.getSize() > 0) {
                final UIMessage msg = new UIMessage();
                msg.setId(19328);
                msg.setByteValue((byte)0);
                Worker.getInstance().pushMessage(msg);
            }
            final Window w = (Window)Xulor.getInstance().load("storageBoxDialog", Dialogs.getDialogPath("storageBoxDialog"), 17L, (short)10000);
            UIEquipmentFrame.getInstance().ensureInventoryLinkedWindowLayout(w);
            PropertiesProvider.getInstance().setPropertyValue("storageBox", this.m_storageBoxBoxView);
            Xulor.getInstance().putActionClass("wakfu.storageBox", StorageBoxDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            StorageBoxDialogActions.setDraggedItemId(-1L);
            if (this.m_storageBoxBoxView != null) {
                this.m_storageBoxBoxView.clear();
                this.m_storageBoxBoxView = null;
            }
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer != null && localPlayer.getCurrentOccupation() != null) {
                localPlayer.cancelCurrentOccupation(false, true);
            }
            if (WakfuGameEntity.getInstance().hasFrame(UIEquipmentFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(UIEquipmentFrame.getInstance());
            }
            Xulor.getInstance().removeActionClass("wakfu.collectMachine");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            if (Xulor.getInstance().isLoaded("storageBoxDialog")) {
                Xulor.getInstance().unload("storageBoxDialog");
            }
            WakfuGameEntity.getInstance().pushFrame(UIWorldInteractionFrame.getInstance());
            PropertiesProvider.getInstance().removeProperty("storageBox");
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19329: {
                final UIItemMessage msg = (UIItemMessage)message;
                final byte destinationPosition = msg.getDestinationPosition();
                final long itemId = msg.getLongValue();
                final CompartmentView compartmentView = this.m_storageBoxBoxView.getSelectedCompartment();
                if (compartmentView == null) {
                    UIStorageBoxFrame.m_logger.error((Object)"Aucun compartiment s\u00e9lectionn\u00e9 !");
                    return false;
                }
                compartmentView.executeMove(itemId, destinationPosition);
                return false;
            }
            case 19326: {
                final UIItemMessage msg = (UIItemMessage)message;
                final long itemUid = msg.getLongValue();
                final byte destinationPosition2 = msg.getDestinationPosition();
                final CompartmentView compartmentView = this.m_storageBoxBoxView.getSelectedCompartment();
                if (compartmentView == null) {
                    UIStorageBoxFrame.m_logger.error((Object)"Aucun compartiment s\u00e9lectionn\u00e9, impossible de r\u00e9aliser l'action de drop !!!");
                    return false;
                }
                final Item item = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getItemFromInventories(itemUid);
                final GuildStorageOperationStatus result = compartmentView.executeAdd(item, msg.getShortValue(), destinationPosition2);
                switch (result) {
                    case BAD_RIGHTS: {
                        UIStorageBoxFrame.m_logger.warn((Object)"Impossible d'ajouter l'item");
                        final String errorMsg = WakfuTranslator.getInstance().getString("storageBox.addItem.unauthorized");
                        final ChatMessage chatErrorMsg = new ChatMessage(errorMsg);
                        chatErrorMsg.setPipeDestination(3);
                        ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg);
                        return false;
                    }
                    case INVENTORY_ERROR: {
                        UIStorageBoxFrame.m_logger.warn((Object)"Impossible d'ajouter l'item");
                        final String errorMsg = WakfuTranslator.getInstance().getString("storageBox.addItem.unauthorized");
                        final ChatMessage chatErrorMsg = new ChatMessage(errorMsg);
                        chatErrorMsg.setPipeDestination(3);
                        ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg);
                        return false;
                    }
                    default: {
                        return false;
                    }
                }
                break;
            }
            case 19327: {
                final UIItemMessage msg = (UIItemMessage)message;
                final long destination = msg.getDestinationUniqueId();
                final CompartmentView selectedCompartment = this.m_storageBoxBoxView.getSelectedCompartment();
                if (selectedCompartment == null) {
                    UIStorageBoxFrame.m_logger.error((Object)"Aucun compartiment s\u00e9lectionn\u00e9, impossible de r\u00e9aliser l'action de drop !!!");
                    return false;
                }
                selectedCompartment.executeRemove(msg.getItem().getUniqueId(), msg.getQuantity(), destination, msg.getDestinationPosition());
                StorageBoxDialogActions.setDraggedItemId(-1L);
                return false;
            }
            case 19330: {
                final UIMessage msg2 = (UIMessage)message;
                final byte index = msg2.getByteValue();
                final CompartmentView compartmentView2 = this.m_storageBoxBoxView.getSelectedCompartment();
                if (compartmentView2 == null) {
                    UIStorageBoxFrame.m_logger.error((Object)("impossible de retrouver le compartiment d'index=" + index));
                    return false;
                }
                compartmentView2.tryToUnlockCompartment();
                return false;
            }
            case 19328: {
                final UIMessage msg2 = (UIMessage)message;
                final byte index = msg2.getByteValue();
                if (this.m_storageBoxBoxView == null) {
                    return false;
                }
                final CompartmentView compartmentByIndex = this.m_storageBoxBoxView.getCompartmentByIndex(index);
                if (compartmentByIndex == null) {
                    UIStorageBoxFrame.m_logger.error((Object)("impossible de retrouver le compartiment d'index=" + index));
                    return false;
                }
                this.selectCompartment(compartmentByIndex);
                return false;
            }
            case 19332: {
                final UIMessage msg2 = (UIMessage)message;
                final int amount = MathHelper.ensurePositiveInt(msg2.getIntValue());
                if (this.m_storageBoxBoxView.getMoney() < amount) {
                    final String errMsg = WakfuTranslator.getInstance().getString("error.storage.notEnoughMoneyInBox");
                    final MessageBoxData data = new MessageBoxData(102, 0, errMsg, 2L);
                    Xulor.getInstance().msgBox(data);
                    return false;
                }
                final String amountText = WakfuTranslator.getInstance().formatNumber(amount);
                final String msgText = WakfuTranslator.getInstance().getString("question.storage.withdrawConfirm", amountText);
                final MessageBoxData data2 = new MessageBoxData(102, 0, msgText, 24L);
                final MessageBoxControler controler = Xulor.getInstance().msgBox(data2);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            UIStorageBoxFrame.this.m_storageBoxBoxView.withdrawMoney(amount);
                        }
                    }
                });
                return false;
            }
            case 19331: {
                final UIMessage msg2 = (UIMessage)message;
                final int amount = MathHelper.ensurePositiveInt(msg2.getIntValue());
                if (WakfuGameEntity.getInstance().getLocalPlayer().getWallet().getAmountOfCash() < amount) {
                    final String errMsg = WakfuTranslator.getInstance().getString("error.storage.notEnoughMoneyInPlayer");
                    final MessageBoxData data = new MessageBoxData(102, 0, errMsg, 2L);
                    Xulor.getInstance().msgBox(data);
                    return false;
                }
                final String msgText2 = WakfuTranslator.getInstance().getString("question.storage.depositConfirm", amount);
                final MessageBoxData data = new MessageBoxData(102, 0, msgText2, 24L);
                final MessageBoxControler controler2 = Xulor.getInstance().msgBox(data);
                controler2.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            UIStorageBoxFrame.this.m_storageBoxBoxView.depositMoney(amount);
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
    
    public void selectCompartment(final CompartmentView compartmentView) {
        if (compartmentView.isUnlocked() || compartmentView.isSerialized()) {
            compartmentView.select();
        }
        this.m_storageBoxBoxView.setSelectedCompartment(compartmentView);
        this.m_storageBoxBoxView.updateFields();
    }
    
    @Override
    public long getId() {
        return 12L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setGuildStorageBox(final TIntObjectHashMap<Boolean> compartments, final GuildStorageHistory history, final int money) {
        this.m_storageBoxBoxView = new GuildStorageBoxView(compartments, history, money);
        PropertiesProvider.getInstance().setPropertyValue("storageBox", this.m_storageBoxBoxView);
        final UIMessage msg = new UIMessage();
        msg.setId(19328);
        msg.setByteValue((byte)0);
        Worker.getInstance().pushMessage(msg);
    }
    
    public void setStorageBox(final StorageBox storageBox) {
        this.m_storageBoxBoxView = new StorageBoxViewImpl(storageBox.getInfo(), storageBox.getInventory());
    }
    
    public StorageBoxView getStorageBoxBoxView() {
        return this.m_storageBoxBoxView;
    }
    
    public void onMoney(final int money) {
        this.m_storageBoxBoxView.setMoney(money);
    }
    
    public void clearCompartmentContent() {
        final CompartmentView compartmentView = this.m_storageBoxBoxView.getSelectedCompartment();
        compartmentView.clearInventory();
    }
    
    public void onCompartmentContent(final ItemInventoryHandler storageBoxCompartment) {
        final CompartmentView compartmentView = this.m_storageBoxBoxView.getSelectedCompartment();
        compartmentView.setInventory(storageBoxCompartment);
        compartmentView.updateFields();
    }
    
    public void onGuildStorageHistory(final RawGuildStorageHistory rawHistory) {
        final GuildStorageBoxView guildStorageBoxView = (GuildStorageBoxView)this.m_storageBoxBoxView;
        final GuildStorageHistory history = new GuildStorageHistory();
        history.fromRaw(rawHistory);
        guildStorageBoxView.setGuildHistory(history);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIStorageBoxFrame.class);
        UIStorageBoxFrame.m_instance = new UIStorageBoxFrame();
    }
}
