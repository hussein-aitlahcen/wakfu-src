package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item.bind.*;
import com.ankamagames.wakfu.client.core.game.item.bind.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.item.ui.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.game.item.bind.*;
import com.ankamagames.wakfu.client.core.game.item.operation.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.client.core.game.events.listeners.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.rent.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.elements.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class NetItemsFrame implements MessageFrame
{
    private static NetItemsFrame m_instance;
    private static final Logger m_logger;
    
    public static NetItemsFrame getInstance() {
        return NetItemsFrame.m_instance;
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
            case 5200: {
                final ItemInventoryResetMessage msg = (ItemInventoryResetMessage)message;
                final byte[] serializedCharacterInfo = msg.getSerializedInventories();
                final LocalPlayerCharacter localPlayer = HeroesManager.INSTANCE.getHero(msg.getCharacterId());
                localPlayer.emptyEquipment();
                localPlayer.emptyBags();
                localPlayer.fromBuild(serializedCharacterInfo);
                PropertiesProvider.getInstance().firePropertyValueChanged(localPlayer, localPlayer.getFields());
                PropertiesProvider.getInstance().firePropertyValueChanged(localPlayer.getBags(), localPlayer.getBags().getFields());
                if (msg.isNeedAck()) {
                    final ItemEquipmentAckCancelMessage cancel = new ItemEquipmentAckCancelMessage();
                    cancel.setCharacterId(localPlayer.getId());
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(cancel);
                }
                return false;
            }
            case 15992: {
                final ItemBindMessage msg2 = (ItemBindMessage)message;
                final long itemId = msg2.getItemId();
                final Item item = WakfuGameEntity.getInstance().getLocalPlayer().getFromEquipmentOrInventory(itemId);
                final ItemBindControllerUINotifier controler = new ItemBindControllerUINotifier(item);
                controler.bind(msg2.getData());
                return false;
            }
            case 5300: {
                final ItemIdCacheUpdateMessage msg3 = (ItemIdCacheUpdateMessage)message;
                if (msg3.getSquashing()) {
                    ItemUIDsManager.getInstance().removeAll();
                }
                final ArrayList<Long> ids = msg3.getUniqueIds();
                for (final long id : ids) {
                    ItemUIDsManager.getInstance().addUId(id);
                }
                return false;
            }
            case 5208: {
                final ItemPickedUpMessage msg4 = (ItemPickedUpMessage)message;
                final ArrayList<Long> itemIds = msg4.getItemIds();
                final TByteArrayList canPickUpList = msg4.getCanPickUpList();
                final ArrayList<Long> destinationIds = msg4.getDestinationIds();
                final ArrayList<Item> items = new ArrayList<Item>();
                for (int i = 0; i < itemIds.size(); ++i) {
                    final FloorItem floorItem = FloorItemManager.getInstance().getFloorItem(itemIds.get(i));
                    if (floorItem != null && floorItem.getItem() != null) {
                        final Item item2 = floorItem.getItem();
                        if (canPickUpList.get(i) != 1 || destinationIds.get(i) == -1L) {
                            final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("item.lootImpossible.inventoryFull", item2.getQuantity(), item2.getName()));
                            chatMessage.setPipeDestination(3);
                            ChatManager.getInstance().pushMessage(chatMessage);
                        }
                        else {
                            items.add(item2);
                            try {
                                final ClientBagContainer bags = WakfuGameEntity.getInstance().getLocalPlayer().getBags();
                                final AbstractBag abstractBag = bags.get(destinationIds.get(i));
                                abstractBag.add(item2);
                            }
                            catch (Exception e) {
                                NetItemsFrame.m_logger.error((Object)("ceci ne devrait pas arriver puisque c est le server qui nous envoie la destination : " + e));
                            }
                        }
                    }
                }
                if (!items.isEmpty()) {
                    ItemFeedbackHelper.sendChatItemsAddedMessage(items);
                }
                return false;
            }
            case 5212: {
                final ItemQuantityUpdateMessage msg5 = (ItemQuantityUpdateMessage)message;
                final LocalPlayerCharacter localPlayer2 = HeroUtils.getHeroWithItemUidInBags(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), msg5.getItemId());
                final Item item3 = localPlayer2.getBags().getItemFromInventories(msg5.getItemId());
                if (item3 != null) {
                    if (item3.getQuantity() > -msg5.getQuantity()) {
                        item3.updateQuantity(msg5.getQuantity());
                    }
                    else {
                        localPlayer2.getBags().removeItemFromBags(item3.getUniqueId());
                        localPlayer2.getShortcutBarManager().removeShortcutItemWithUniqueId(item3.getUniqueId(), true);
                    }
                }
                return false;
            }
            case 5251: {
                final ItemRentInfoUpdateMessage msg6 = (ItemRentInfoUpdateMessage)message;
                final LocalPlayerCharacter localPlayer2 = WakfuGameEntity.getInstance().getLocalPlayer();
                final Item item3 = localPlayer2.getFromEquipmentOrInventory(msg6.getItemId());
                if (item3 == null) {
                    NetItemsFrame.m_logger.error((Object)("Item non poss\u00e9d\u00e9 " + msg6.getItemId()));
                    return false;
                }
                final RentInfo rentInfo = item3.getRentInfo();
                if (rentInfo == null) {
                    NetItemsFrame.m_logger.error((Object)("L'item n'a pas d'info de location " + item3));
                    return false;
                }
                rentInfo.fromRaw(msg6.getRawRentInfo());
                return false;
            }
            case 5210: {
                final ItemSpawnInInventoryMessage msg7 = (ItemSpawnInInventoryMessage)message;
                final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(msg7.getItemRefId());
                final LocalPlayerCharacter localPlayer = HeroUtils.getHeroWithBagUid(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), msg7.getDestinationId());
                final AbstractBag targetBag = localPlayer.getBags().get(msg7.getDestinationId());
                final Item item4 = new Item(msg7.getItemId());
                item4.initializeWithReferenceItem(refItem);
                item4.setQuantity(msg7.getQuantity());
                if (msg7.getBindType() != ItemBindType.NOT_BOUND) {
                    item4.setBind(ItemBindFactory.create(msg7.getBindType(), msg7.getBindId()));
                }
                try {
                    final short position = msg7.getDestinationPosition();
                    if (position != -1) {
                        if (targetBag.canAdd(item4, position)) {
                            targetBag.addAt(item4, position);
                            ItemFeedbackHelper.sendChatItemAddedMessage(item4);
                        }
                        else {
                            NetItemsFrame.m_logger.error((Object)"Erreur : impossible d'ajouter l'item \u00e0 l'endroit d\u00e9sir\u00e9 par le serveur");
                        }
                    }
                    else if (targetBag.canAdd(item4)) {
                        targetBag.add(item4);
                        ItemFeedbackHelper.sendChatItemAddedMessage(item4);
                    }
                    else {
                        NetItemsFrame.m_logger.error((Object)"Erreur : impossible d'ajouter l'item \u00e0 l'endroit d\u00e9sir\u00e9 par le serveur");
                    }
                }
                catch (InventoryCapacityReachedException e2) {
                    NetItemsFrame.m_logger.warn((Object)e2.getMessage());
                }
                catch (ContentAlreadyPresentException e3) {
                    NetItemsFrame.m_logger.warn((Object)e3.getMessage());
                }
                catch (PositionAlreadyUsedException e4) {
                    NetItemsFrame.m_logger.warn((Object)e4.getMessage());
                }
                return false;
            }
            case 11118: {
                final BagOperationsMessage msg8 = (BagOperationsMessage)message;
                try {
                    BagOperationComputer.applyOperations(msg8.getOperations());
                }
                catch (InventoryCapacityReachedException e5) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Trop d'item", (Throwable)e5);
                }
                catch (ContentAlreadyPresentException e6) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Duplication", (Throwable)e6);
                }
                catch (PositionAlreadyUsedException e7) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Mauvaise position", (Throwable)e7);
                }
                return false;
            }
            case 11120: {
                final InventoryToEquipmentResultMessage msg9 = (InventoryToEquipmentResultMessage)message;
                final Item equippedItem = msg9.getEquippedItem();
                final byte equippedPosition = msg9.getEquippedPosition();
                final LocalPlayerCharacter player = HeroesManager.INSTANCE.getHero(msg9.getCharacterId());
                final ItemEquipment equipment = player.getEquipmentInventory();
                player.beginRefreshDisplayEquipment();
                player.beginUpdateItemEffects();
                Item oldItem = ((ArrayInventoryWithoutCheck<Item, R>)equipment).getFromPosition(equippedPosition);
                if (oldItem != null && !oldItem.isActive()) {
                    oldItem = ((InactiveItem)oldItem).getBaseItem();
                }
                if (oldItem != null) {
                    final EquipmentPosition[] linked = oldItem.getReferenceItem().getItemType().getLinkedPositions();
                    ((ArrayInventoryWithoutCheck<Item, R>)equipment).remove(oldItem);
                    for (int j = 0; j < linked.length; ++j) {
                        equipment.removeAt(linked[j].m_id);
                    }
                }
                final EquipmentPosition[] linked = equippedItem.getReferenceItem().getItemType().getLinkedPositions();
                equipment.removeAt(equippedPosition);
                for (int j = 0; j < linked.length; ++j) {
                    equipment.removeAt(linked[j].m_id);
                }
                try {
                    ((ArrayInventoryWithoutCheck<Item, R>)equipment).addAt(equippedItem, equippedPosition);
                    for (int j = 0; j < linked.length; ++j) {
                        ((ArrayInventoryWithoutCheck<Item, R>)equipment).addAt(equippedItem.getInactiveCopy(), linked[j].m_id);
                    }
                    BagOperationComputer.applyOperations(msg9.getOperations());
                    UIEquipmentFrame.getInstance().refreshPlayerEquimentSlots();
                }
                catch (InventoryCapacityReachedException e8) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Trop d'item", (Throwable)e8);
                }
                catch (ContentAlreadyPresentException e9) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Duplication", (Throwable)e9);
                }
                catch (PositionAlreadyUsedException e10) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Mauvaise position", (Throwable)e10);
                }
                player.endUpdateItemEffects();
                player.endRefreshDisplayEquipment();
                return false;
            }
            case 11130: {
                final EquipCostumeResultMessage msg10 = (EquipCostumeResultMessage)message;
                final int itemRefId = msg10.getItemRefId();
                final LocalPlayerCharacter player2 = HeroesManager.INSTANCE.getHero(msg10.getCharacterId());
                final ItemEquipment equipment2 = player2.getEquipmentInventory();
                final byte position2 = EquipmentPosition.COSTUME.getId();
                player2.beginRefreshDisplayEquipment();
                player2.beginUpdateItemEffects();
                final Item oldItem = ((ArrayInventoryWithoutCheck<Item, R>)equipment2).getFromPosition(position2);
                if (oldItem != null) {
                    final EquipmentPosition[] linked = oldItem.getReferenceItem().getItemType().getLinkedPositions();
                    equipment2.removeAt(position2);
                    for (int j = 0; j < linked.length; ++j) {
                        equipment2.removeAt(linked[j].m_id);
                    }
                }
                if (itemRefId > 0) {
                    final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(itemRefId);
                    final Item item2 = Item.newInstance(referenceItem);
                    try {
                        ((ArrayInventoryWithoutCheck<Item, R>)equipment2).addAt(item2, position2);
                    }
                    catch (InventoryException e20) {
                        NetItemsFrame.m_logger.error((Object)"Probl\u00e8me \u00e0 l'\u00e9quipement d'un costume");
                    }
                }
                UIEquipmentFrame.getInstance().refreshPlayerEquimentSlots();
                player2.endUpdateItemEffects();
                player2.endRefreshDisplayEquipment();
                return false;
            }
            case 11122: {
                final EquipmentToInventoryResultMessage msg11 = (EquipmentToInventoryResultMessage)message;
                final byte equippedPosition2 = msg11.getEquippedPosition();
                final LocalPlayerCharacter player2 = HeroesManager.INSTANCE.getHero(msg11.getCharacterId());
                final ItemEquipment equipment2 = player2.getEquipmentInventory();
                final Item oldItem2 = ((ArrayInventoryWithoutCheck<Item, R>)equipment2).getFromPosition(equippedPosition2);
                final EquipmentPosition[] linked2 = oldItem2.getReferenceItem().getItemType().getLinkedPositions();
                player2.beginRefreshDisplayEquipment();
                player2.beginUpdateItemEffects();
                equipment2.removeAt(equippedPosition2);
                for (int k = 0; k < linked2.length; ++k) {
                    equipment2.removeAt(linked2[k].m_id);
                }
                try {
                    BagOperationComputer.applyOperations(msg11.getOperations());
                    UIEquipmentFrame.getInstance().refreshPlayerEquimentSlots();
                }
                catch (InventoryCapacityReachedException e11) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Trop d'item", (Throwable)e11);
                }
                catch (ContentAlreadyPresentException e12) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Duplication", (Throwable)e12);
                }
                catch (PositionAlreadyUsedException e13) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Mauvaise position", (Throwable)e13);
                }
                player2.endUpdateItemEffects();
                player2.endRefreshDisplayEquipment();
                return false;
            }
            case 5222: {
                final AddBagResultMessage addBagResultMessage = (AddBagResultMessage)message;
                final RawBag rawBag = addBagResultMessage.getBag();
                try {
                    BagOperationComputer.applyOperations(addBagResultMessage.getOperations());
                }
                catch (InventoryCapacityReachedException e14) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Trop d'item", (Throwable)e14);
                }
                catch (ContentAlreadyPresentException e15) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Duplication", (Throwable)e15);
                }
                catch (PositionAlreadyUsedException e16) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Mauvaise position", (Throwable)e16);
                }
                final LocalPlayerCharacter concerned = HeroesManager.INSTANCE.getHero(addBagResultMessage.getCharacterId());
                if (concerned == null) {
                    return false;
                }
                final ClientBagContainer bags2 = concerned.getBags();
                final Bag bag = new Bag(0L, 0, BagInventoryContentChecker.INSTANCE, (short)0, bags2);
                if (!bag.fromRaw(rawBag)) {
                    NetItemsFrame.m_logger.error((Object)("Erreur lors de la r\u00e9cup\u00e9ration du bag uniqueId=" + rawBag.uniqueId + ", on ignore les bags restants"));
                    return false;
                }
                bags2.addContainer(bag);
                bag.addObserver(ClientEventLocalPlayerInventoryListener.INSTANCE);
                concerned.reloadItemEffects();
                UIEquipmentFrame.getInstance().refreshBagDisplay(bag.getUid());
                return false;
            }
            case 5224: {
                final RemoveBagResultMessage removeBagResultMessage = (RemoveBagResultMessage)message;
                final long id2 = removeBagResultMessage.getRemovedBagUid();
                try {
                    BagOperationComputer.applyOperations(removeBagResultMessage.getOperations());
                }
                catch (InventoryCapacityReachedException e17) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Trop d'item", (Throwable)e17);
                }
                catch (ContentAlreadyPresentException e18) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Duplication", (Throwable)e18);
                }
                catch (PositionAlreadyUsedException e19) {
                    NetItemsFrame.m_logger.error((Object)"D\u00e9Synchro d'inventaire : Mauvaise position", (Throwable)e19);
                }
                final BasicCharacterInfo source = HeroUtils.getHeroWithBagUid(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), id2);
                source.getBags().removeContainerById(id2);
                source.reloadItemEffects();
                UIEquipmentFrame.getInstance().refreshBagDisplay(id2);
                return false;
            }
            case 5228: {
                final ChangeBagPositionResultMessage msg12 = (ChangeBagPositionResultMessage)message;
                final ClientBagContainer bags3 = HeroUtils.getHeroWithBagUid(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), msg12.getBagUid1()).getBags();
                final AbstractBag bag2 = bags3.getFromUid(msg12.getBagUid1());
                if (bag2 == null) {
                    NetItemsFrame.m_logger.error((Object)("Le sac " + msg12.getBagUid1() + " n'existe pas"));
                    return false;
                }
                bag2.setPosition(msg12.getPosition1());
                UIEquipmentFrame.getInstance().refreshBagDisplay(bag2.getUid());
                if (msg12.isSwapBags()) {
                    final AbstractBag bag3 = bags3.getFromUid(msg12.getBagUid2());
                    if (bag3 == null) {
                        NetItemsFrame.m_logger.error((Object)("Le sac " + msg12.getBagUid2() + " n'existe pas"));
                        return false;
                    }
                    bag3.setPosition(msg12.getPosition2());
                    UIEquipmentFrame.getInstance().refreshBagDisplay(bag3.getUid());
                }
                return false;
            }
            case 5226: {
                final EquipmentPositionLockedMessage msg13 = (EquipmentPositionLockedMessage)message;
                final EquipmentPosition position3 = msg13.getPosition();
                final boolean locked = msg13.isLocked();
                WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory().lockPosition(position3, locked);
                return false;
            }
            case 4186: {
                final DisassembleResultMessage msg14 = (DisassembleResultMessage)message;
                this.onDisassembleResultMessage(msg14);
                return false;
            }
            case 4188: {
                final RandomItemResultMessage msg15 = (RandomItemResultMessage)message;
                this.onRandomItemResultMessage(msg15);
                return false;
            }
            case 13010: {
                final RollRandomElementsResultMessage msg16 = (RollRandomElementsResultMessage)message;
                this.onRollRandomElementsResultMessage(msg16);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void onRollRandomElementsResultMessage(final RollRandomElementsResultMessage msg) {
        final long itemId = msg.getItemId();
        final MultiElementsInfo multiElementsInfo = msg.getMultiElementsInfo();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Item item = HeroUtils.getItemFromHeroBagOrInventory(localPlayer.getClientId(), msg.getItemId());
        if (item == null) {
            NetItemsFrame.m_logger.error((Object)"On a re\u00e7u une notif de roll d'\u00e9l\u00e9ment sur un item inconnu");
        }
        if (multiElementsInfo == null) {
            final UISelectItemElementsFrame selectItemElementsFrame = UISelectItemElementsFrame.getInstance();
            selectItemElementsFrame.setConcernedItem(item);
            WakfuGameEntity.getInstance().pushFrame(selectItemElementsFrame);
            return;
        }
        item.setMultiElementsInfo(multiElementsInfo);
        item.resetCache();
        PropertiesProvider.getInstance().firePropertyValueChanged(item, "effect", "caracteristic", "effectAndCaracteristic");
    }
    
    private void onRandomItemResultMessage(final RandomItemResultMessage message) {
        final int referenceId = message.getReferenceId();
        if (WakfuGameEntity.getInstance().hasFrame(UIBlindBoxFrame.getInstance())) {
            UIBlindBoxFrame.getInstance().onRandomItemResult(referenceId);
        }
    }
    
    private void onDisassembleResultMessage(final DisassembleResultMessage msg) {
        final int gemsCount = msg.getGemsCount();
        final int disassembleCount = msg.getTotalDisassembleCount();
        if (WakfuGameEntity.getInstance().hasFrame(UIRecycleFrame.getInstance())) {
            UIRecycleFrame.getInstance().onRecycleResult(gemsCount > 0);
        }
        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("recycle.resultMessage", gemsCount, disassembleCount), 4);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        NetItemsFrame.m_instance = new NetItemsFrame();
        m_logger = Logger.getLogger((Class)NetItemsFrame.class);
    }
}
