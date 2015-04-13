package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.vault.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.vault.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.vault.*;

public class UIVaultFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIVaultFrame m_instance;
    private VaultView m_vaultView;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIVaultFrame getInstance() {
        return UIVaultFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("vaultDialog") || id.equals("equipmentDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIVaultFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            UIEquipmentFrame.getInstance().openEquipment();
            UIWebShopFrame.getInstance().requestLockForUI("vaultDialog");
            final Window w = (Window)Xulor.getInstance().load("vaultDialog", Dialogs.getDialogPath("vaultDialog"), 17L, (short)10000);
            UIEquipmentFrame.getInstance().ensureInventoryLinkedWindowLayout(w);
            PropertiesProvider.getInstance().setPropertyValue("vault", this.m_vaultView);
            Xulor.getInstance().putActionClass("wakfu.vault", VaultDialogActions.class);
            WakfuGameEntity.getInstance().pushFrame(NetVaultFrame.INSTANCE);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new VaultConsultRequestMessage());
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            StorageBoxDialogActions.setDraggedItemId(-1L);
            if (this.m_vaultView != null) {
                this.m_vaultView = null;
            }
            Xulor.getInstance().removeActionClass("wakfu.vault");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            if (Xulor.getInstance().isLoaded("vaultDialog")) {
                Xulor.getInstance().unload("vaultDialog");
            }
            PropertiesProvider.getInstance().removeProperty("vault");
            WakfuGameEntity.getInstance().removeFrame(NetVaultFrame.INSTANCE);
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 19335: {
                final UIItemMessage msg = (UIItemMessage)message;
                final byte destinationPosition = msg.getDestinationPosition();
                final long itemId = msg.getLongValue();
                if (!this.checkPrerequisites()) {
                    return false;
                }
                this.m_vaultView.executeMove(itemId, destinationPosition);
                return false;
            }
            case 19333: {
                final UIItemMessage msg = (UIItemMessage)message;
                final long itemUid = msg.getLongValue();
                final byte destinationPosition2 = msg.getDestinationPosition();
                if (!this.checkPrerequisites()) {
                    return false;
                }
                final Item item = HeroUtils.getItemFromHero(localPlayer.getOwnerId(), itemUid);
                final VaultOperationStatus result = this.m_vaultView.executeAdd(item, msg.getShortValue(), destinationPosition2);
                switch (result) {
                    case BAD_RIGHTS: {
                        UIVaultFrame.m_logger.warn((Object)"Impossible d'ajouter l'item, pas de droits d'abonnement");
                        final String errMsg = WakfuTranslator.getInstance().getString("error.playerNotSubscribed");
                        final MessageBoxData data = new MessageBoxData(102, 0, errMsg, 2L);
                        Xulor.getInstance().msgBox(data);
                        ChatManager.getInstance().pushMessage(errMsg, 3);
                        return false;
                    }
                    case INVENTORY_ERROR: {
                        UIVaultFrame.m_logger.warn((Object)"Impossible d'ajouter l'item, erreur d'inventaire");
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
            case 19334: {
                final UIItemMessage msg = (UIItemMessage)message;
                final long destination = msg.getDestinationUniqueId();
                if (!this.checkPrerequisites()) {
                    return false;
                }
                this.m_vaultView.executeRemove(msg.getItem().getUniqueId(), msg.getQuantity(), destination, msg.getDestinationPosition());
                VaultDialogActions.setDraggedItemId(-1L);
                return false;
            }
            case 19337: {
                final UIMessage msg2 = (UIMessage)message;
                final int amount = MathHelper.ensurePositiveInt(msg2.getIntValue());
                if (this.m_vaultView.getMoney() < amount) {
                    final String errMsg2 = WakfuTranslator.getInstance().getString("error.storage.notEnoughMoneyInBox");
                    final MessageBoxData data2 = new MessageBoxData(102, 0, errMsg2, 2L);
                    Xulor.getInstance().msgBox(data2);
                    return false;
                }
                final String amountText = WakfuTranslator.getInstance().formatNumber(amount);
                final String msgText = WakfuTranslator.getInstance().getString("question.storage.withdrawConfirm", amountText);
                final MessageBoxData data3 = new MessageBoxData(102, 0, msgText, 24L);
                final MessageBoxControler controler = Xulor.getInstance().msgBox(data3);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            UIVaultFrame.this.m_vaultView.withdrawMoney(amount);
                        }
                    }
                });
                return false;
            }
            case 19336: {
                final UIMessage msg2 = (UIMessage)message;
                final int amount = MathHelper.ensurePositiveInt(msg2.getIntValue());
                if (!localPlayer.hasSubscriptionRight(SubscriptionRight.VAULT)) {
                    final String errMsg2 = WakfuTranslator.getInstance().getString("error.playerNotSubscribed");
                    final MessageBoxData data2 = new MessageBoxData(102, 0, errMsg2, 2L);
                    Xulor.getInstance().msgBox(data2);
                    ChatManager.getInstance().pushMessage(errMsg2, 3);
                    return false;
                }
                final ItemTrade trade = ClientTradeHelper.INSTANCE.getCurrentTrade();
                final int availableCash = localPlayer.getWallet().getAmountOfCash() - ((trade == null) ? 0 : trade.getLocalUser().getMoneyExchanged());
                if (availableCash < amount) {
                    final String errMsg3 = WakfuTranslator.getInstance().getString("error.storage.notEnoughMoneyInPlayer");
                    final MessageBoxData data4 = new MessageBoxData(102, 0, errMsg3, 2L);
                    Xulor.getInstance().msgBox(data4);
                    return false;
                }
                final String msgText2 = WakfuTranslator.getInstance().getString("question.storage.depositConfirm", amount);
                final MessageBoxData data4 = new MessageBoxData(102, 0, msgText2, 24L);
                final MessageBoxControler controler2 = Xulor.getInstance().msgBox(data4);
                controler2.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            UIVaultFrame.this.m_vaultView.depositMoney(amount);
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
    
    private boolean checkPrerequisites() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Fight fight = localPlayer.getCurrentFight();
        return (fight == null || fight.getStatus() == AbstractFight.FightStatus.NONE) && ClientTradeHelper.INSTANCE.getCurrentTrade() == null;
    }
    
    @Override
    public long getId() {
        return 13L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public VaultView getVaultView() {
        return this.m_vaultView;
    }
    
    public void clearContent() {
        if (this.m_vaultView != null) {
            this.m_vaultView.clearInventory();
        }
    }
    
    public void onVaultReceived(final RawVault rawVault, final VaultUpgrade upgrade) {
        (this.m_vaultView = new VaultView(upgrade)).onVaultReceived(rawVault);
        PropertiesProvider.getInstance().setPropertyValue("vault", this.m_vaultView);
    }
    
    public void updateVaultAccessibility() {
        boolean accessibility = true;
        final Xulor xulor = Xulor.getInstance();
        accessibility &= !xulor.isLoaded("fleaDialog");
        accessibility &= !xulor.isLoaded("dimensionalBagRoomManagerDialog");
        accessibility &= !xulor.isLoaded("storageBoxDialog");
        accessibility &= !xulor.isLoaded("temporaryTransferInventoryDialog");
        accessibility &= !xulor.isLoaded("marketDialog");
        if (!accessibility && xulor.isLoaded("vaultDialog")) {
            xulor.unload("vaultDialog");
        }
        PropertiesProvider.getInstance().setPropertyValue("vaultAccessibility", accessibility);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIVaultFrame.class);
        UIVaultFrame.m_instance = new UIVaultFrame();
    }
}
