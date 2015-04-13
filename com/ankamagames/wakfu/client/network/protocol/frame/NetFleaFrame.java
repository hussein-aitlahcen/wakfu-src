package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class NetFleaFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static NetFleaFrame m_instance;
    
    public static NetFleaFrame getInstance() {
        return NetFleaFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final DimensionalBagView bag = UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag();
        switch (message.getId()) {
            case 10106: {
                final FleaContentMessage contentMessage = (FleaContentMessage)message;
                NetFleaFrame.m_logger.info((Object)("[DIMENSIONAL_BAG_FLEA_CONTENT_MESSAGE] ownerId=" + contentMessage.getBagOwnerId()));
                bag.getDimensionalBagFlea().setBaseBuyDuty(contentMessage.getBaseBuyDuty());
                bag.getDimensionalBagFlea().setNationBuyDuty(contentMessage.getNationBuyDuty());
                bag.setCurrentMerchantInventory(bag.getDimensionalBagFlea().fromRawInventory(contentMessage.getSerializedFlea()));
                return false;
            }
            case 5234: {
                final FleaBuyResultMessage buyResultMessage = (FleaBuyResultMessage)message;
                switch (buyResultMessage.getError()) {
                    case 0: {
                        final Item obtainedItem = new Item();
                        if (!obtainedItem.fromRaw(buyResultMessage.getSerializedItem())) {
                            NetFleaFrame.m_logger.error((Object)"On essaie d'acheter un item qui n'existe pas ?");
                        }
                        NetFleaFrame.m_logger.info((Object)("[FLEA_BUY_RESULT_MESSAGE] : container=" + buyResultMessage.getContainer() + ", item=" + obtainedItem.getName()));
                        final ClientBagContainer container = WakfuGameEntity.getInstance().getLocalPlayer().getBags();
                        try {
                            container.get(buyResultMessage.getContainer()).add(obtainedItem);
                        }
                        catch (InventoryCapacityReachedException e) {
                            NetFleaFrame.m_logger.error((Object)("Exception lev\u00e9e lors de l'ajout d'un item(" + obtainedItem.getName() + ") dans un container(" + buyResultMessage.getContainer() + ") : "), (Throwable)e);
                        }
                        catch (ContentAlreadyPresentException e2) {
                            NetFleaFrame.m_logger.error((Object)("Exception lev\u00e9e lors de l'ajout d'un item(" + obtainedItem.getName() + ") dans un container(" + buyResultMessage.getContainer() + ") : "), (Throwable)e2);
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
                bag.getCurrentMerchantInventory().notifyChanges();
                return false;
            }
            case 10108: {
                final FleaLockMessage lockMessage = (FleaLockMessage)message;
                NetFleaFrame.m_logger.info((Object)("[FLEA_LOCK_MESSAGE] merchantInventory=" + lockMessage.getMerchantInventoryUid() + ", lock=" + lockMessage.isLocked()));
                bag.setMerchantInventoryLocked(lockMessage.getMerchantInventoryUid(), lockMessage.isLocked());
                return false;
            }
            case 5236: {
                final MerchantItemRemoveMessage removeMessage = (MerchantItemRemoveMessage)message;
                NetFleaFrame.m_logger.info((Object)("[MERCHANT_ITEM_REMOVE_MESSAGE] itemUid=" + removeMessage.getItemUniqueId()));
                final ObjectPair<MerchantInventory, MerchantInventoryItem> removed = bag.getDimensionalBagFlea().removeItemWithUniqueId(removeMessage.getItemUniqueId());
                if (removed == null) {
                    NetFleaFrame.m_logger.error((Object)"Remove failed...");
                }
                bag.getCurrentMerchantInventory().notifyChanges();
                return false;
            }
            case 5232: {
                final MerchantItemAddMessage addMessage = (MerchantItemAddMessage)message;
                NetFleaFrame.m_logger.info((Object)("[MERCHANT_ITEM_ADD_MESSAGE] itemUid=" + addMessage.getItem().item.uniqueId));
                final MerchantInventory inventory = ((AbstractMerchantInventoryCollection<IT, MerchantInventory>)bag.getDimensionalBagFlea()).getInventoryWithUid(addMessage.getMerchantInventoryUid());
                if (inventory != null) {
                    final MerchantInventoryItem merchantItem = MerchantInventoryItem.checkout();
                    merchantItem.fromRaw(addMessage.getItem());
                    try {
                        ((ContiguousArrayInventory<MerchantInventoryItem, R>)inventory).insertAt(merchantItem, addMessage.getPosition());
                    }
                    catch (Exception e3) {
                        NetFleaFrame.m_logger.error((Object)"Exception lev\u00e9e lors de l'ajout de l'item marchand", (Throwable)e3);
                    }
                    inventory.notifyChanges();
                }
                else {
                    NetFleaFrame.m_logger.error((Object)("L'inventaire marchand uid=" + addMessage.getMerchantInventoryUid() + " est introuvable"));
                }
                return false;
            }
            case 5238: {
                final MerchantItemUpdateMessage updateMessage = (MerchantItemUpdateMessage)message;
                NetFleaFrame.m_logger.info((Object)("[MERCHANT_ITEM_UPDATE_MESSAGE] itemUid=" + updateMessage.getUniqueId()));
                final ObjectPair<MerchantInventory, MerchantInventoryItem> content = bag.getDimensionalBagFlea().getItemWithUniqueId(updateMessage.getUniqueId());
                if (content != null) {
                    final MerchantInventory inventory2 = content.getFirst();
                    final MerchantInventoryItem item = content.getSecond();
                    final PackType packType = updateMessage.getPackType();
                    final int price = updateMessage.getPrice();
                    final short quantity = updateMessage.getQuantity();
                    if (item.getQuantity() != quantity) {
                        inventory2.updateQuantity(item.getUniqueId(), (short)(quantity - item.getQuantity()));
                    }
                    if (item.getPrice() != price) {
                        inventory2.setPrice(item, price);
                    }
                    if (item.getPackType() != packType) {
                        inventory2.setPackType(item, packType);
                    }
                    inventory2.notifyChanges();
                }
                else {
                    NetFleaFrame.m_logger.error((Object)("L'item (ID=" + updateMessage.getUniqueId() + ") n'existe pas/plus dans la brocante brows\u00e9e."));
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
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetFleaFrame.class);
        NetFleaFrame.m_instance = new NetFleaFrame();
    }
}
