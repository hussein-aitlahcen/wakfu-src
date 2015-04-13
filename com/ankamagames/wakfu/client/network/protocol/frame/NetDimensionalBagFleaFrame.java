package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class NetDimensionalBagFleaFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static NetDimensionalBagFleaFrame m_instance;
    
    public static NetDimensionalBagFleaFrame getInstance() {
        return NetDimensionalBagFleaFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 10114: {
                final DimensionalBagAllFleasContentMessage msg = (DimensionalBagAllFleasContentMessage)message;
                final List<RawMerchantItemInventory> serializedFleas = msg.getSerializedFleas();
                final TByteArrayList fleasSize = msg.getFleaSize();
                final TIntArrayList refItems = msg.getRefItems();
                final DimensionalBagFlea flea = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag().getDimensionalBagFlea();
                flea.clear();
                for (int i = 0; i < serializedFleas.size(); ++i) {
                    final MerchantInventory inventory = flea.fromRawInventory(serializedFleas.get(i));
                    final byte fleaSize = fleasSize.get(i);
                    inventory.setSize(fleaSize);
                    final int refItemId = refItems.get(i);
                    final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(refItemId);
                    inventory.setFleaTitle((referenceItem != null) ? referenceItem.getName() : "<UNKNOWN>");
                    inventory.setGfxId(referenceItem.getGfxId());
                    UIBrowseDimensionalBagFleaFrame.getInstance().addFleaRefItem(inventory.getUid(), new ObjectPair((F)fleaSize, (S)referenceItem));
                }
                return false;
            }
            case 10116: {
                final DimensionalBagFleaContentMessage msg2 = (DimensionalBagFleaContentMessage)message;
                final RawMerchantItemInventory serializedFlea = msg2.getSerializedFlea();
                final DimensionalBagFlea flea2 = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag().getDimensionalBagFlea();
                UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag().setCurrentMerchantInventory(flea2.fromRawInventory(serializedFlea));
                return false;
            }
            case 5234: {
                final DimensionalBagView bag = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag();
                final FleaBuyResultMessage buyResultMessage = (FleaBuyResultMessage)message;
                switch (buyResultMessage.getError()) {
                    case 0: {
                        final Item obtainedItem = new Item();
                        if (!obtainedItem.fromRaw(buyResultMessage.getSerializedItem())) {
                            NetDimensionalBagFleaFrame.m_logger.error((Object)"On essaie d'acheter un item qui n'existe pas ?");
                        }
                        NetDimensionalBagFleaFrame.m_logger.info((Object)("[FLEA_BUY_RESULT_MESSAGE] : container=" + buyResultMessage.getContainer() + ", item=" + obtainedItem.getName()));
                        final ClientBagContainer container = WakfuGameEntity.getInstance().getLocalPlayer().getBags();
                        try {
                            container.get(buyResultMessage.getContainer()).add(obtainedItem);
                        }
                        catch (InventoryCapacityReachedException e) {
                            NetDimensionalBagFleaFrame.m_logger.error((Object)("Exception lev\u00e9e lors de l'ajout d'un item(" + obtainedItem.getName() + ") dans un container(" + buyResultMessage.getContainer() + ") : "), (Throwable)e);
                        }
                        catch (ContentAlreadyPresentException e2) {
                            NetDimensionalBagFleaFrame.m_logger.error((Object)("Exception lev\u00e9e lors de l'ajout d'un item(" + obtainedItem.getName() + ") dans un container(" + buyResultMessage.getContainer() + ") : "), (Throwable)e2);
                        }
                        break;
                    }
                    case 2: {
                        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.flea.buy.inventoryfull"), 3);
                        break;
                    }
                    case 3: {
                        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.flea.buy.inventoryLocked"), 3);
                        break;
                    }
                    case 1: {
                        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.flea.buy"), 3);
                        break;
                    }
                }
                final MerchantInventory currentMerchantInventory = bag.getCurrentMerchantInventory();
                if (currentMerchantInventory == null) {
                    UIBrowseDimensionalBagFleaFrame.getInstance().unloadFleaDialog();
                    return false;
                }
                currentMerchantInventory.notifyChanges();
                return false;
            }
            case 10108: {
                final DimensionalBagView bag = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag();
                final FleaLockMessage lockMessage = (FleaLockMessage)message;
                NetDimensionalBagFleaFrame.m_logger.info((Object)("[FLEA_LOCK_MESSAGE] merchantInventory=" + lockMessage.getMerchantInventoryUid() + ", lock=" + lockMessage.isLocked()));
                bag.setMerchantInventoryLocked(lockMessage.getMerchantInventoryUid(), lockMessage.isLocked());
                return false;
            }
            case 5236: {
                final DimensionalBagView bag = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag();
                final MerchantItemRemoveMessage removeMessage = (MerchantItemRemoveMessage)message;
                final MerchantInventory mi = ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)bag.getDimensionalBagFlea()).getInventoryWithUid(removeMessage.getMerchantInventoryUid());
                final byte newSize = (byte)(mi.getSize() - 1);
                mi.setSize(newSize);
                PropertiesProvider.getInstance().firePropertyValueChanged(bag, "fleaList");
                final MerchantInventory inventory2 = bag.getCurrentMerchantInventory();
                if (inventory2 != null) {
                    final long id = removeMessage.getItemUniqueId();
                    NetDimensionalBagFleaFrame.m_logger.info((Object)("[MERCHANT_ITEM_REMOVE_MESSAGE] itemUid=" + id));
                    final ObjectPair<MerchantInventory, MerchantInventoryItem> removed = bag.getDimensionalBagFlea().removeItemWithUniqueId(id);
                    if (removed == null) {
                        NetDimensionalBagFleaFrame.m_logger.error((Object)"Remove failed...");
                        return false;
                    }
                    if (WakfuGameEntity.getInstance().hasFrame(UIBrowseDimensionalBagFleaFrame.getInstance())) {
                        final DimensionalBagView browsingDimentionalBag = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag();
                        if (inventory2.isEmpty() && inventory2.equals(browsingDimentionalBag.getCurrentMerchantInventory())) {
                            browsingDimentionalBag.clearCurrentMerchantInventoryView();
                        }
                        final long uid = mi.getUid();
                        ObjectPair refItem = UIBrowseDimensionalBagFleaFrame.getInstance().getFleaRefItem(uid);
                        refItem = new ObjectPair((F)newSize, (S)refItem.getSecond());
                        UIBrowseDimensionalBagFleaFrame.getInstance().addFleaRefItem(uid, refItem);
                    }
                    inventory2.notifyChanges();
                }
                return false;
            }
            case 5232: {
                final DimensionalBagView bag = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag();
                final MerchantItemAddMessage addMessage = (MerchantItemAddMessage)message;
                final MerchantInventory mi = ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)bag.getDimensionalBagFlea()).getInventoryWithUid(addMessage.getMerchantInventoryUid());
                final byte newSize = (byte)(mi.getSize() + 1);
                mi.setSize(newSize);
                if (WakfuGameEntity.getInstance().hasFrame(UIBrowseDimensionalBagFleaFrame.getInstance())) {
                    final long uid2 = mi.getUid();
                    ObjectPair refItem2 = UIBrowseDimensionalBagFleaFrame.getInstance().getFleaRefItem(uid2);
                    refItem2 = new ObjectPair((F)newSize, (S)refItem2.getSecond());
                    UIBrowseDimensionalBagFleaFrame.getInstance().addFleaRefItem(uid2, refItem2);
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(bag, "fleaList");
                if (bag.getCurrentMerchantInventory() == null) {
                    return false;
                }
                final MerchantInventory inventory2 = ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)bag.getDimensionalBagFlea()).getInventoryWithUid(addMessage.getMerchantInventoryUid());
                if (inventory2 != null) {
                    NetDimensionalBagFleaFrame.m_logger.info((Object)("[MERCHANT_ITEM_ADD_MESSAGE] itemUid=" + addMessage.getItem().item.uniqueId));
                    final MerchantInventoryItem merchantItem = MerchantInventoryItem.checkout();
                    merchantItem.fromRaw(addMessage.getItem());
                    try {
                        ((ContiguousArrayInventory<MerchantInventoryItem, R>)inventory2).insertAt(merchantItem, addMessage.getPosition());
                    }
                    catch (Exception e3) {
                        NetDimensionalBagFleaFrame.m_logger.error((Object)"Exception lev\u00e9e lors de l'ajout de l'item marchand", (Throwable)e3);
                    }
                    inventory2.notifyChanges();
                }
                else {
                    NetDimensionalBagFleaFrame.m_logger.error((Object)("L'inventaire marchand uid=" + addMessage.getMerchantInventoryUid() + " est introuvable"));
                }
                return false;
            }
            case 5238: {
                final DimensionalBagView bag = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag();
                final MerchantItemUpdateMessage updateMessage = (MerchantItemUpdateMessage)message;
                NetDimensionalBagFleaFrame.m_logger.info((Object)("[MERCHANT_ITEM_UPDATE_MESSAGE] itemUid=" + updateMessage.getUniqueId()));
                final ObjectPair<MerchantInventory, MerchantInventoryItem> content = bag.getDimensionalBagFlea().getItemWithUniqueId(updateMessage.getUniqueId());
                if (content != null) {
                    final MerchantInventory inventory3 = content.getFirst();
                    final MerchantInventoryItem item = content.getSecond();
                    final PackType packType = updateMessage.getPackType();
                    final int price = updateMessage.getPrice();
                    final short quantity = updateMessage.getQuantity();
                    if (item.getQuantity() != quantity) {
                        inventory3.updateQuantity(item.getUniqueId(), (short)(quantity - item.getQuantity()));
                    }
                    if (item.getPrice() != price) {
                        inventory3.setPrice(item, price);
                    }
                    if (item.getPackType() != packType) {
                        inventory3.setPackType(item, packType);
                    }
                    inventory3.notifyChanges();
                }
                else {
                    NetDimensionalBagFleaFrame.m_logger.error((Object)("L'item (ID=" + updateMessage.getUniqueId() + ") n'existe pas/plus dans la brocante brows\u00e9e."));
                }
                return false;
            }
            case 10118: {
                final MerchantInventoryAddedMessage msg3 = (MerchantInventoryAddedMessage)message;
                final RawMerchantItemInventory serializedInventory = msg3.getSerializedFlea();
                final byte inventorySize = msg3.getFleaSize();
                final int inventoryItemRefId = msg3.getRefItem();
                final DimensionalBagView bagView = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag();
                final MerchantInventory inventory4 = bagView.getDimensionalBagFlea().fromRawInventory(serializedInventory);
                inventory4.setSize(inventorySize);
                final AbstractReferenceItem referenceItem2 = ReferenceItemManager.getInstance().getReferenceItem(inventoryItemRefId);
                inventory4.setFleaTitle((referenceItem2 != null) ? referenceItem2.getName() : "<UNKNOWN>");
                inventory4.setGfxId(referenceItem2.getGfxId());
                UIBrowseDimensionalBagFleaFrame.getInstance().addFleaRefItem(inventory4.getUid(), new ObjectPair((F)inventorySize, (S)referenceItem2));
                return false;
            }
            case 10120: {
                final MerchantInventoryRemovedMessage msg4 = (MerchantInventoryRemovedMessage)message;
                final long merchantInventoryUid = msg4.getMerchantInventoryUid();
                final DimensionalBagView bagView2 = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag();
                bagView2.getDimensionalBagFlea().removeMerchantInventory(merchantInventoryUid);
                UIBrowseDimensionalBagFleaFrame.getInstance().removeFleaRefItem(merchantInventoryUid);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetDimensionalBagFleaFrame.class);
        NetDimensionalBagFleaFrame.m_instance = new NetDimensionalBagFleaFrame();
    }
}
