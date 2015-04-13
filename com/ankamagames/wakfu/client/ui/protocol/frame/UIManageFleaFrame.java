package com.ankamagames.wakfu.client.ui.protocol.frame;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.script.*;
import com.ankamagames.framework.script.events.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.message.Merchant.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.merchant.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.tax.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class UIManageFleaFrame extends UIMarketTaxFrame
{
    private static final Logger m_logger;
    private static UIManageFleaFrame m_instance;
    private EditableRenderableCollection.CollectionContentLoadedListener m_collectionContentLoadedListener;
    private List m_list;
    private MerchantDisplay m_merchantDisplay;
    private DialogUnloadListener m_dialogUnloadListener;
    private short m_destinationPosition;
    
    public static UIManageFleaFrame getInstance() {
        return UIManageFleaFrame.m_instance;
    }
    
    private UIManageFleaFrame() {
        super();
        this.m_merchantDisplay = null;
    }
    
    public void setMerchantDisplay(final MerchantDisplay merchantDisplay) {
        this.m_merchantDisplay = merchantDisplay;
    }
    
    @Override
    protected void addMerchantItem(final Item copy, final short quantity) {
        super.addMerchantItem(copy, quantity);
        this.m_merchantItem.setDuration(AuctionDuration.SEVENTY_TWO_HOURS);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17300:
            case 17302:
            case 17304:
            case 17307:
            case 17309: {
                this.computeMessage(message);
                return false;
            }
            case 17305:
            case 17308: {
                if (this.m_merchantDisplay.isMarketregistered()) {
                    final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("market.question.unregister"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                    messageBoxControler.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                UIManageFleaFrame.this.computeMessage(message);
                            }
                        }
                    });
                }
                else {
                    this.computeMessage(message);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void apply() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final DimensionalBagFlea flea = localPlayer.getOwnedDimensionalBag().getDimensionalBagFlea();
        Item newItem = null;
        boolean dealOk = true;
        final MerchantInventory merchantInventory = ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)flea).getInventoryWithUid(this.m_merchantDisplay.getMerchantInventoryUid());
        if (merchantInventory == null) {
            UIManageFleaFrame.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer l'inventaire marchand uid=" + this.m_merchantDisplay.getMerchantInventoryUid()));
            return;
        }
        AbstractBag bag = null;
        if (this.m_merchantItem.getQuantity() == -1 || this.m_merchantItem.getQuantity() == this.m_item.getQuantity()) {
            if (this.m_destinationPosition == -1) {
                if (merchantInventory.add(this.m_item) != null) {
                    bag = localPlayer.getBags().removeItemFromBags(this.m_item);
                    this.m_destinationPosition = merchantInventory.getPosition(this.m_item.getUniqueId());
                }
                else {
                    UIManageFleaFrame.m_logger.error((Object)"L'objet n a pas pu etre ajout\u00e9 dans le sac marchand");
                    dealOk = false;
                }
            }
            else if (merchantInventory.insertAt(this.m_item, this.m_destinationPosition, this.m_merchantItem.getPackType().qty, this.m_merchantItem.getPrice()) != null) {
                bag = localPlayer.getBags().removeItemFromBags(this.m_item);
            }
            else {
                UIManageFleaFrame.m_logger.error((Object)"L'objet n a pas pu etre inser\u00e9 dans le sac marchand");
                dealOk = false;
            }
            if (bag == null && dealOk) {
                merchantInventory.removeWithUniqueId(this.m_item.getUniqueId());
                dealOk = false;
            }
        }
        else {
            newItem = this.m_item.getCopy(false);
            final AbstractBag container = localPlayer.getBags().getFirstContainerWith(this.m_item.getUniqueId());
            if (container != null) {
                newItem.setQuantity(this.m_merchantItem.getQuantity());
                if (this.m_destinationPosition == -1) {
                    if (merchantInventory.add(newItem) != null) {
                        container.updateQuantity(this.m_item.getUniqueId(), (short)(-this.m_merchantItem.getQuantity()));
                        this.m_destinationPosition = merchantInventory.getPosition(newItem.getUniqueId());
                    }
                    else {
                        UIManageFleaFrame.m_logger.error((Object)"L'objet n a pas pu etre ajout\u00e9 dans le sac marchand");
                        dealOk = false;
                    }
                }
                else if (merchantInventory.insertAt(newItem, this.m_destinationPosition, this.m_merchantItem.getPackType().qty, this.m_merchantItem.getPrice()) != null) {
                    container.updateQuantity(this.m_item.getUniqueId(), (short)(-this.m_merchantItem.getQuantity()));
                }
                else {
                    UIManageFleaFrame.m_logger.error((Object)"L'objet n a pas pu etre inser\u00e9 dans le sac marchand");
                    dealOk = false;
                }
            }
            else {
                UIManageFleaFrame.m_logger.error((Object)("L'objet ne se trouve dans aucun des inventaires : id : " + this.m_item.getUniqueId()));
                dealOk = false;
            }
        }
        if (dealOk) {
            final long merchantItemId = (newItem != null) ? newItem.getUniqueId() : this.m_item.getUniqueId();
            final AbstractMerchantInventoryItem merchantItem = ((ArrayInventory<AbstractMerchantInventoryItem, R>)merchantInventory).getWithUniqueId(merchantItemId);
            if (merchantItem != null) {
                merchantInventory.setPrice(merchantItem, this.m_merchantItem.getPrice());
                merchantInventory.setPackType(merchantItem, this.m_merchantItem.getPackType());
                final AddItemMerchantMessage addMsg = new AddItemMerchantMessage();
                addMsg.setSourceItemUid(this.m_item.getUniqueId());
                addMsg.setPosition((byte)this.m_destinationPosition);
                if (newItem != null) {
                    addMsg.setResultantItemUid(newItem.getUniqueId());
                }
                addMsg.setQuantity(this.m_merchantItem.getQuantity());
                addMsg.setDestinationMerchantInventoryUid(merchantInventory.getUid());
                addMsg.setPackSize(this.m_merchantItem.getPackType().qty);
                addMsg.setPrice(this.m_merchantItem.getPrice());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(addMsg);
                ScriptEventManager.getInstance().fireEvent(new FleaModifiedScriptEvent(FleaModifiedScriptEvent.FleaAction.Added, this.m_item.getReferenceId(), this.m_merchantItem.getQuantity(), 0));
            }
        }
        else {
            Xulor.getInstance().unload("splitStackDialog");
        }
    }
    
    private void computeMessage(final Message message) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            UIManageFleaFrame.m_logger.error((Object)"Pas de local player");
            return;
        }
        final DimensionalBagFlea flea = localPlayer.getOwnedDimensionalBag().getDimensionalBagFlea();
        switch (message.getId()) {
            case 17308: {
                UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag().getCurrentMerchantInventory().setFleaMarketRegistered(false);
                if (this.localPlayerHasEnoughMoney()) {
                    this.apply();
                }
                Xulor.getInstance().unload("merchantTaxDialog");
            }
            case 17309: {
                Xulor.getInstance().unload("merchantTaxDialog");
            }
            case 17304: {
                final UIMerchantMessage msg = (UIMerchantMessage)message;
                final MerchantInventory merchantInventory = ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)flea).getInventoryWithUid(this.m_merchantDisplay.getMerchantInventoryUid());
                if (merchantInventory == null) {
                    UIManageFleaFrame.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer l'inventaire marchand uid=" + this.m_merchantDisplay.getMerchantInventoryUid()));
                    return;
                }
                if (merchantInventory.isFull()) {
                    Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.flea.full"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 102, 1);
                    return;
                }
                final Item item = msg.getItem();
                if (item.isBound() || item.isRent() || (item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE) != null && !item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE).isValid(WakfuGameEntity.getInstance().getLocalPlayer(), WakfuGameEntity.getInstance().getLocalPlayer().getPosition(), item, WakfuGameEntity.getInstance().getLocalPlayer().getEffectContext()))) {
                    UIManageFleaFrame.m_logger.warn((Object)("L'objet " + item.getReferenceId() + " n'a pas le droit d'\u00eatre mis en vente"));
                    final String errorMsg = WakfuTranslator.getInstance().getString("flea.addItem.unauthorized");
                    final ChatMessage chatErrorMsg = new ChatMessage(errorMsg);
                    chatErrorMsg.setPipeDestination(3);
                    ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg);
                    return;
                }
                this.m_item = item;
                this.addMerchantItem(item.getCopy(false), (msg.getQuantity() == -1) ? item.getQuantity() : msg.getQuantity());
                Xulor.getInstance().load("merchantTaxDialog", Dialogs.getDialogPath("merchantTaxDialog"), 33025L, (short)30000);
                this.m_destinationPosition = msg.getDestinationPosition();
            }
            case 17307: {
                final UIMerchantMessage msg = (UIMerchantMessage)message;
                final MerchantInventory merchantInventory = ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)flea).getInventoryWithUid(this.m_merchantDisplay.getMerchantInventoryUid());
                if (merchantInventory == null) {
                    UIManageFleaFrame.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer l'inventaire marchand uid=" + this.m_merchantDisplay.getMerchantInventoryUid()));
                    return;
                }
                final MerchantInventoryItem merchantInventoryItem = msg.getMerchantItem();
                try {
                    merchantInventory.removeWithUniqueId(merchantInventoryItem.getUniqueId());
                    if (!((ContiguousArrayInventory<MerchantInventoryItem, R>)merchantInventory).insertAt(merchantInventoryItem, msg.getDestinationPosition())) {
                        merchantInventory.removeWithUniqueId(merchantInventoryItem.getUniqueId());
                        UIManageFleaFrame.m_logger.error((Object)"L'objet n a pas pu etre inser\u00e9 dans le sac marchand");
                    }
                }
                catch (InventoryCapacityReachedException e) {
                    UIManageFleaFrame.m_logger.error((Object)"Exception", (Throwable)e);
                }
                catch (ContentAlreadyPresentException e2) {
                    UIManageFleaFrame.m_logger.error((Object)"Exception", (Throwable)e2);
                }
                catch (LastPositionAlreadyUsedException e3) {
                    UIManageFleaFrame.m_logger.error((Object)"Exception", (Throwable)e3);
                }
                catch (PositionAlreadyUsedException e4) {
                    UIManageFleaFrame.m_logger.error((Object)"Exception", (Throwable)e4);
                }
            }
            case 17305: {
                final UIMerchantMessage msg = (UIMerchantMessage)message;
                final MerchantInventory merchantInventory = ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)flea).getInventoryWithUid(this.m_merchantDisplay.getMerchantInventoryUid());
                if (merchantInventory == null) {
                    UIManageFleaFrame.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer l'inventaire marchand uid=" + this.m_merchantDisplay.getMerchantInventoryUid()));
                    return;
                }
                UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag().getCurrentMerchantInventory().setFleaMarketRegistered(false);
                boolean berror = false;
                AbstractBag bag = null;
                Item newItem = null;
                final MerchantInventoryItem merchantItem = msg.getMerchantItem();
                if (msg.getQuantity() == -1 || msg.getQuantity() == merchantItem.getQuantity()) {
                    if (msg.getContainerId() != -1L) {
                        final AbstractBag container = localPlayer.getBags().get(msg.getContainerId());
                        if (container != null) {
                            try {
                                if (container.canAdd(merchantItem.getItem(), msg.getDestinationPosition())) {
                                    if (container.addAt(merchantItem.getItem(), msg.getDestinationPosition())) {
                                        if (!container.contains(merchantItem.getItem())) {
                                            merchantItem.getItem().release();
                                        }
                                        bag = container;
                                    }
                                    else {
                                        berror = true;
                                    }
                                }
                            }
                            catch (Exception e5) {
                                UIManageFleaFrame.m_logger.error((Object)("Impossible d'ajouter l'item a la position : position : " + msg.getDestinationPosition()), (Throwable)e5);
                                berror = true;
                            }
                        }
                    }
                    else {
                        bag = localPlayer.getBags().addItemToBags(merchantItem.getItem());
                    }
                    if (bag != null && !((ContiguousArrayInventory<MerchantInventoryItem, R>)merchantInventory).remove(merchantItem)) {
                        if (localPlayer.getBags().removeItemFromBags(merchantItem.getItem()) == null) {
                            UIManageFleaFrame.m_logger.error((Object)"On vient d'ajouter un item aux inventaire, mais une erreur et survenu lors de la transaction et on arrive pas a le retirer");
                        }
                        berror = true;
                    }
                }
                else {
                    newItem = merchantItem.getItem().getCopy(false);
                    newItem.setQuantity(msg.getQuantity());
                    if (msg.getContainerId() != -1L) {
                        final AbstractBag container = localPlayer.getBags().get(msg.getContainerId());
                        if (container != null) {
                            try {
                                if (container.canAdd(newItem, msg.getDestinationPosition())) {
                                    if (container.addAt(newItem, msg.getDestinationPosition())) {
                                        if (!container.contains(newItem)) {
                                            newItem.release();
                                        }
                                        bag = container;
                                    }
                                    else {
                                        berror = true;
                                    }
                                }
                            }
                            catch (Exception e5) {
                                UIManageFleaFrame.m_logger.error((Object)("Impossible d'ajouter l'item a la position : position : " + msg.getDestinationPosition()), (Throwable)e5);
                                berror = true;
                            }
                        }
                    }
                    else {
                        bag = localPlayer.getBags().addItemToBags(newItem);
                    }
                    if (bag != null) {
                        merchantInventory.updateQuantity(merchantItem.getUniqueId(), (short)(-msg.getQuantity()));
                    }
                }
                if (!berror && bag != null) {
                    final RemoveItemMerchantMessage netMsg = new RemoveItemMerchantMessage();
                    netMsg.setSourceMerchantInventoryUid(merchantInventory.getUid());
                    netMsg.setItemUid(merchantItem.getItem().getUniqueId());
                    netMsg.setDestinationContainer(bag.getUid());
                    if (newItem != null) {
                        netMsg.setQuantity(msg.getQuantity());
                        netMsg.setNewItemUid(newItem.getUniqueId());
                        netMsg.setDestinationPosition(bag.getPosition(newItem.getUniqueId()));
                    }
                    else {
                        netMsg.setDestinationPosition(msg.getDestinationPosition());
                    }
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
                    ScriptEventManager.getInstance().fireEvent(new FleaModifiedScriptEvent(FleaModifiedScriptEvent.FleaAction.Removed, merchantItem.getItem().getReferenceId(), msg.getQuantity(), 0));
                }
            }
            default: {
                super.onMessage(message);
            }
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            ProtectorUtils.requestTaxUpdate();
            this.m_taxContext = TaxContext.FLEA_ADD_ITEM_CONTEXT;
            final DimensionalBagView bag = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag();
            bag.getDimensionalBagFlea().clear();
            PropertiesProvider.getInstance().setPropertyValue("fleaDimensionalBag", bag);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("fleaDialog") || id.equals("equipmentDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIManageFleaFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            UIEquipmentFrame.getInstance().openEquipment();
            final Window w = (Window)Xulor.getInstance().load("fleaDialog", Dialogs.getDialogPath("fleaDialog"), 32769L, (short)10000);
            this.m_list = (List)w.getElementMap().getElement("fleaList");
            final Window window = (Window)w.getElementMap().getElement("dimensionalBagWindow");
            this.m_collectionContentLoadedListener = new EditableRenderableCollection.CollectionContentLoadedListener() {
                @Override
                public void onContentLoaded() {
                    XulorUtil.setWidgetInScreen(window);
                }
            };
            this.m_list.addListContentListener(this.m_collectionContentLoadedListener);
            UIEquipmentFrame.getInstance().ensureInventoryLinkedWindowLayout(w);
            PropertiesProvider.getInstance().setPropertyValue("editableDimensionalBag", bag);
            WakfuGameEntity.getInstance().removeFrame(UIWorldInteractionFrame.getInstance());
            WakfuGameEntity.getInstance().getNetworkEntity().pushFrame(NetFleaFrame.getInstance());
            Xulor.getInstance().putActionClass("wakfu.dimensionalBagFlea", DimensionalBagFleaDialogActions.class);
            super.onFrameAdd(frameHandler, isAboutToBeAdded);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            WakfuGameEntity.getInstance().getLocalPlayer().cancelCurrentOccupation(false, true);
            this.m_list.removeListContentLoadListener(this.m_collectionContentLoadedListener);
            this.m_list = null;
            this.m_collectionContentLoadedListener = null;
            if (WakfuGameEntity.getInstance().hasFrame(UIEquipmentFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(UIEquipmentFrame.getInstance());
            }
            PropertiesProvider.getInstance().removeProperty("fleaDimensionalBag");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("fleaDialog");
            Xulor.getInstance().unload("setPriceDialog");
            Xulor.getInstance().unload("splitStackDialog");
            Xulor.getInstance().unload("merchantTaxDialog");
            Xulor.getInstance().removeActionClass("wakfu.dimensionalBagFlea");
            WakfuGameEntity.getInstance().pushFrame(UIWorldInteractionFrame.getInstance());
            WakfuGameEntity.getInstance().getNetworkEntity().removeFrame(NetFleaFrame.getInstance());
            this.m_merchantDisplay = null;
            super.onFrameRemove(frameHandler, isAboutToBeRemoved);
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
        m_logger = Logger.getLogger((Class)UIManageFleaFrame.class);
        UIManageFleaFrame.m_instance = new UIManageFleaFrame();
    }
}
