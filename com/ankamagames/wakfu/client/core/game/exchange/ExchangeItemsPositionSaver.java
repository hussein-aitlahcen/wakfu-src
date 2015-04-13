package com.ankamagames.wakfu.client.core.game.exchange;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ExchangeItemsPositionSaver
{
    private static ItemTrade m_trade;
    private static final Logger m_logger;
    protected final TLongObjectHashMap<Object> m_itemSourceContainer;
    protected final TLongShortHashMap m_itemSourcePosition;
    
    public ExchangeItemsPositionSaver(final ItemTrade trade) {
        super();
        this.m_itemSourceContainer = new TLongObjectHashMap<Object>();
        this.m_itemSourcePosition = new TLongShortHashMap();
        ExchangeItemsPositionSaver.m_trade = trade;
    }
    
    public void memorizeItemPosition(final InventoryContent item, final Object source, final short sourcePosition) {
        if (source instanceof Bag) {
            if (WakfuGameEntity.getInstance().getLocalPlayer().getId() != ((ItemExchanger<ContentType, WakfuExchangerUser>)ExchangeItemsPositionSaver.m_trade).getRequester().getId() && WakfuGameEntity.getInstance().getLocalPlayer().getId() != ((ItemExchanger<ContentType, WakfuExchangerUser>)ExchangeItemsPositionSaver.m_trade).getTarget().getId()) {
                ExchangeItemsPositionSaver.m_logger.error((Object)"On essaie de m\u00e9moriser la position d'un item lors d'un \u00e9change qui ne concerne pas le joueur !");
                return;
            }
            final Bag sourceBag = (Bag)source;
            this.m_itemSourceContainer.put(item.getUniqueId(), sourceBag);
            this.m_itemSourcePosition.put(item.getUniqueId(), sourcePosition);
        }
        else if (source instanceof ItemEquipment) {
            if (WakfuGameEntity.getInstance().getLocalPlayer().getId() != ((ItemExchanger<ContentType, WakfuExchangerUser>)ExchangeItemsPositionSaver.m_trade).getRequester().getId() && WakfuGameEntity.getInstance().getLocalPlayer().getId() != ((ItemExchanger<ContentType, WakfuExchangerUser>)ExchangeItemsPositionSaver.m_trade).getTarget().getId()) {
                ExchangeItemsPositionSaver.m_logger.error((Object)"On essaie de m\u00e9moriser la position d'un item lors d'un \u00e9change qui ne concerne pas le joueur !");
                return;
            }
            final ItemEquipment equipment = (ItemEquipment)source;
            this.m_itemSourceContainer.put(item.getUniqueId(), equipment);
            this.m_itemSourcePosition.put(item.getUniqueId(), sourcePosition);
        }
    }
    
    public Object getItemSource(final InventoryContent item) {
        return this.m_itemSourceContainer.get(item.getUniqueId());
    }
    
    public short getItemPosition(final InventoryContent item) {
        return this.m_itemSourcePosition.get(item.getUniqueId());
    }
    
    public Object takeBackItem(final Item item, final short quantity) {
        return this.takeBackItem(item, quantity, true);
    }
    
    public Object takeBackItem(final Item item, final short quantity, boolean mustBeUnmemorized) {
        final WakfuExchangerUser user = ((ItemExchanger<ContentType, WakfuExchangerUser>)ExchangeItemsPositionSaver.m_trade).getUserById(WakfuGameEntity.getInstance().getLocalPlayer().getId());
        final Item itemToRemove = user.getInExchangeList(item.getUniqueId());
        if (itemToRemove != null) {
            mustBeUnmemorized = false;
        }
        final Object targetContainer = this.m_itemSourceContainer.get(item.getUniqueId());
        final Short targetPosition = this.m_itemSourcePosition.get(item.getUniqueId());
        final ClientBagContainer bags = WakfuGameEntity.getInstance().getLocalPlayer().getBags();
        if (bags == null) {
            ExchangeItemsPositionSaver.m_logger.info((Object)"Erreur lors de la r\u00e9cup\u00e9ration des sacs du local player");
            return null;
        }
        if (targetContainer == null || targetPosition == null) {
            ExchangeItemsPositionSaver.m_logger.error((Object)"On cherche a retirer un objet qui n'a pas \u00e9t\u00e9 m\u00e9moris\u00e9 pendant l'\u00e9change");
            return null;
        }
        if (targetContainer instanceof Bag) {
            final Bag targetBag = (Bag)targetContainer;
            boolean addedOk = false;
            final Item itemToAdd = item.getClone();
            itemToAdd.setQuantity(quantity);
            final Bag bag = (Bag)bags.get(targetBag.getUid());
            final boolean bagContainsItem = bag.getWithUniqueId(itemToAdd.getUniqueId()) != null;
            if (bagContainsItem) {
                addedOk = bag.updateQuantity(itemToAdd.getUniqueId(), itemToAdd.getQuantity());
                if (addedOk) {
                    itemToAdd.release();
                }
            }
            else if (bag.canAdd(itemToAdd, targetPosition)) {
                try {
                    addedOk = bag.addAt(itemToAdd, targetPosition);
                }
                catch (Exception e) {
                    ExchangeItemsPositionSaver.m_logger.warn((Object)e.getMessage());
                }
                if (addedOk && !bag.contains(itemToAdd)) {
                    itemToAdd.release();
                }
            }
            if (addedOk) {
                if (mustBeUnmemorized) {
                    this.removeItemPosition(itemToAdd);
                }
                return targetBag;
            }
            if (!addedOk) {
                final AbstractBag replacementBag = bags.addItemToBags(itemToAdd, true);
                if (replacementBag == null) {
                    ExchangeItemsPositionSaver.m_logger.error((Object)"L'item n'a pu etre ajout\u00e9 nulle part. Il n'a pas \u00e9t\u00e9 ajout\u00e9 a l'inventaire");
                    return null;
                }
                ExchangeItemsPositionSaver.m_logger.warn((Object)("L'item a \u00e9t\u00e9 plac\u00e9 dans le premier bag disponible, soit :" + replacementBag.getUid()));
                if (mustBeUnmemorized) {
                    this.removeItemPosition(item);
                }
                return replacementBag;
            }
        }
        else if (targetContainer instanceof ItemEquipment) {
            final ItemEquipment targetEquipment = WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory();
            boolean addedOk = false;
            final Item itemToAdd = item.getClone();
            try {
                addedOk = ((ArrayInventoryWithoutCheck<Item, R>)targetEquipment).addAt(itemToAdd, targetPosition);
                if (itemToAdd.getReferenceItem().getItemType().getLinkedPositions() != null) {
                    for (final EquipmentPosition pos : itemToAdd.getReferenceItem().getItemType().getLinkedPositions()) {
                        final Item tempItem = itemToAdd.getInactiveCopy();
                        addedOk &= ((ArrayInventoryWithoutCheck<Item, R>)targetEquipment).addAt(tempItem, pos.m_id);
                    }
                }
            }
            catch (InventoryCapacityReachedException e2) {
                ExchangeItemsPositionSaver.m_logger.error((Object)"La capacit\u00e9 de l'inventaire de d\u00e9part est d\u00e9pass\u00e9e");
            }
            catch (ContentAlreadyPresentException e3) {
                ExchangeItemsPositionSaver.m_logger.error((Object)"L'objet est d\u00e9ja pr\u00e9sent a cette place");
            }
            catch (PositionAlreadyUsedException e4) {
                ExchangeItemsPositionSaver.m_logger.error((Object)"La position de destination est occup\u00e9e  !");
            }
            if (addedOk) {
                if (mustBeUnmemorized) {
                    this.removeItemPosition(item);
                }
                return targetEquipment;
            }
            final AbstractBag replacementBag2 = bags.addItemToBags(item);
            if (replacementBag2 == null) {
                itemToAdd.release();
                ExchangeItemsPositionSaver.m_logger.error((Object)"L'item n'a pu etre ajout\u00e9 nulle part. Il n'a pas \u00e9t\u00e9 ajout\u00e9 a l'inventaire");
                return null;
            }
            ExchangeItemsPositionSaver.m_logger.warn((Object)("L'item a \u00e9t\u00e9 plac\u00e9 dans le premier bag disponible, soit :" + replacementBag2.getUid()));
            if (mustBeUnmemorized) {
                this.removeItemPosition(item);
            }
            return replacementBag2;
        }
        return null;
    }
    
    public void resetMemorizedPositions() {
        if (WakfuGameEntity.getInstance().getLocalPlayer().getId() != ((ItemExchanger<ContentType, WakfuExchangerUser>)ExchangeItemsPositionSaver.m_trade).getRequester().getId() && WakfuGameEntity.getInstance().getLocalPlayer().getId() != ((ItemExchanger<ContentType, WakfuExchangerUser>)ExchangeItemsPositionSaver.m_trade).getTarget().getId()) {
            ExchangeItemsPositionSaver.m_logger.error((Object)"On veut remettre a z\u00e9ro les positions d'un \u00e9change qui ne nous concerne pas !");
            return;
        }
        this.m_itemSourceContainer.clear();
        this.m_itemSourcePosition.clear();
    }
    
    public void removeItemPosition(final InventoryContent item) {
        if (!this.m_itemSourceContainer.containsKey(item.getUniqueId()) || !this.m_itemSourcePosition.containsKey(item.getUniqueId())) {
            ExchangeItemsPositionSaver.m_logger.error((Object)"L'objet que l'on veut retirer n'est pas m\u00e9moris\u00e9 ! ");
            return;
        }
        this.m_itemSourceContainer.remove(item.getUniqueId());
        this.m_itemSourcePosition.remove(item.getUniqueId());
    }
    
    public void removeAllItems(final ItemTrade trade) {
        if (trade != ExchangeItemsPositionSaver.m_trade) {
            ExchangeItemsPositionSaver.m_logger.error((Object)"On veut retirer les items d'un \u00e9change qui ne nous concerne pas! ");
            return;
        }
        final WakfuExchangerUser localUser = ((ItemExchanger<ContentType, WakfuExchangerUser>)ExchangeItemsPositionSaver.m_trade).getUserById(WakfuGameEntity.getInstance().getLocalPlayer().getId());
        boolean needsEquipmentUpdate = false;
        final TLongObjectIterator<Object> it = this.m_itemSourceContainer.iterator();
        while (it.hasNext()) {
            it.advance();
            final long uniqueId = it.key();
            final Item item = localUser.getInExchangeList(uniqueId);
            final Object target = this.takeBackItem(item, item.getQuantity(), false);
            if (target instanceof ItemEquipment) {
                needsEquipmentUpdate = true;
            }
        }
        if (needsEquipmentUpdate) {
            UIEquipmentFrame.getInstance().refreshPlayerEquimentSlots();
        }
        this.resetMemorizedPositions();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ExchangeItemsPositionSaver.class);
    }
}
