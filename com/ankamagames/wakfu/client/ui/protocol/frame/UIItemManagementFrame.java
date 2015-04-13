package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.item.xp.*;
import com.ankamagames.wakfu.common.game.item.xp.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.client.core.game.hero.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.game.item.ui.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.message.resources.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.gems.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.craft.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.common.game.item.gems.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.companion.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class UIItemManagementFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final UIItemManagementFrame m_instance;
    private final ArrayList<String> m_openedItemDetail;
    private String m_lastItemDetailDialogId;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public UIItemManagementFrame() {
        super();
        this.m_openedItemDetail = new ArrayList<String>();
    }
    
    public static UIItemManagementFrame getInstance() {
        return UIItemManagementFrame.m_instance;
    }
    
    private void clean() {
        for (final String itemDetailId : this.m_openedItemDetail) {
            Xulor.getInstance().unload(itemDetailId);
        }
        this.m_openedItemDetail.clear();
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16832: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final int itemId = msg.getIntValue();
                final TIntHashSet craftsUsing = IngredientManager.INSTANCE.getCraftsUsing(itemId);
                if (craftsUsing == null) {
                    return false;
                }
                final int[] array = craftsUsing.toArray();
                Arrays.sort(array);
                final CraftHandler craftHandler = WakfuGameEntity.getInstance().getLocalPlayer().getCraftHandler();
                int craftId = -1;
                for (int i = 0; i < array.length; ++i) {
                    if (craftHandler.contains(array[i])) {
                        craftId = array[i];
                        break;
                    }
                }
                if (!WakfuGameEntity.getInstance().hasFrame(UICraftFrame.getInstance())) {
                    WakfuGameEntity.getInstance().pushFrame(UICraftFrame.getInstance());
                }
                if (craftId != -1) {
                    final UIMessage uiMsg = new UIMessage();
                    uiMsg.setId(16833);
                    uiMsg.setIntValue(craftId);
                    Worker.getInstance().pushMessage(uiMsg);
                }
                final UIMessage uiMsg2 = new UIMessage();
                uiMsg2.setId(16831);
                uiMsg2.setIntValue(itemId);
                Worker.getInstance().pushMessage(uiMsg2);
                return false;
            }
            case 16429: {
                final UIItemSetDetailMessage msg2 = (UIItemSetDetailMessage)message;
                final ItemSet itemSet = msg2.getItem();
                final String parentWindowId = msg2.getParentWindowId();
                if (itemSet != null) {
                    final String dialogPath = "setDetailDialog";
                    final String dialogId = "setDetailDialog_" + itemSet.getId();
                    this.openCloseDetailDialog(msg2, itemSet, parentWindowId, "setDetailDialog", dialogId);
                    return false;
                }
                if (parentWindowId == null) {
                    return false;
                }
                Xulor.getInstance().unload(parentWindowId);
                if (this.m_openedItemDetail.isEmpty()) {
                    WakfuGameEntity.getInstance().removeFrame(this);
                }
                return false;
            }
            case 16430: {
                final UIPetDetailMessage<AbstractPetDetailDialogView> msg3 = (UIPetDetailMessage<AbstractPetDetailDialogView>)message;
                final AbstractPetDetailDialogView view = msg3.getItem();
                if (view == null) {
                    UIItemManagementFrame.m_logger.error((Object)"Impossible de r\u00ef¿½cup\u00ef¿½rer le familier \u00ef¿½ d\u00ef¿½crire");
                    return false;
                }
                final String parentWindowId = msg3.getParentWindowId();
                final String dialogPath2 = "itemDetailDialog";
                final String dialogId2 = this.getItemDetailDialogId(view.getUID());
                this.openCloseDetailDialog(msg3, view, parentWindowId, dialogPath2, dialogId2);
                return false;
            }
            case 16416: {
                final UICraftItemDetailMessage msg4 = (UICraftItemDetailMessage)message;
                final RecipeView recipeView = msg4.getItem();
                final String parentWindowId = msg4.getParentWindowId();
                if (recipeView != null) {
                    final AbstractReferenceItem referenceItem = recipeView.getEffectiveItem();
                    final int refId = (referenceItem != null) ? referenceItem.getId() : -1;
                    final String dialogPath3 = "craftItemDetailDialog";
                    final String dialogId3 = "craftItemDetailDialog_" + refId;
                    this.openCloseDetailDialog(msg4, recipeView, parentWindowId, "craftItemDetailDialog", dialogId3);
                    return false;
                }
                if (parentWindowId == null) {
                    return false;
                }
                Xulor.getInstance().unload(parentWindowId);
                if (this.m_openedItemDetail.isEmpty()) {
                    WakfuGameEntity.getInstance().removeFrame(this);
                }
                return false;
            }
            case 16415: {
                final UIItemDetailMessage msg5 = (UIItemDetailMessage)message;
                Item item = msg5.getItem();
                final String parentWindowId = msg5.getParentWindowId();
                if (item != null) {
                    ItemXpControllerLevelModifier controller = null;
                    if (item.hasXp()) {
                        item = item.getClone();
                        controller = new ItemXpControllerLevelModifier(item);
                    }
                    final String dialogId = "itemDetailDialog_" + item.getUniqueId();
                    final boolean opened = this.openCloseDetailDialog(msg5, item, parentWindowId, "itemDetailDialog", dialogId);
                    if (opened && controller != null) {
                        PropertiesProvider.getInstance().setLocalPropertyValue("itemLevelController", controller, Xulor.getInstance().getEnvironment().getElementMap(dialogId), true);
                    }
                    return false;
                }
                if (parentWindowId == null) {
                    return false;
                }
                Xulor.getInstance().unload(parentWindowId);
                if (this.m_openedItemDetail.isEmpty()) {
                    WakfuGameEntity.getInstance().removeFrame(this);
                }
                return false;
            }
            case 16800: {
                final Fight fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
                if (fight != null && fight.getStatus() != AbstractFight.FightStatus.PLACEMENT) {
                    return false;
                }
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer.isDead()) {
                    final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("action.error.cantWhileDead"));
                    chatMessage.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(chatMessage);
                    return false;
                }
                final UIItemMessage msg6 = (UIItemMessage)message;
                final CharacterView characterfView = msg6.getDestinationCharacter();
                final CharacterInfo destinationCharacter = (characterfView != null) ? characterfView.getCharacterInfo() : localPlayer;
                final Item item2 = msg6.getItem();
                final EquipmentPosition[] possiblePositions = item2.getType().getEquipmentPositions();
                if (possiblePositions.length == 0) {
                    ErrorsMessageTranslator.getInstance().pushMessage(60, 3, new Object[0]);
                    return false;
                }
                final ItemEquipment equipmentInventory = destinationCharacter.getEquipmentInventory();
                byte position = -1;
                if (msg6.getPosition() == -1) {
                    for (final EquipmentPosition pos : possiblePositions) {
                        if (((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).getFromPosition(pos.m_id) == null) {
                            position = pos.m_id;
                            break;
                        }
                    }
                }
                if (msg6.getPosition() == -2) {
                    position = possiblePositions[0].m_id;
                }
                if (msg6.getPosition() == -3 && possiblePositions.length > 1) {
                    position = possiblePositions[1].m_id;
                }
                if (position == -1) {
                    position = possiblePositions[0].m_id;
                }
                if (characterfView != null && characterfView.isCompanion()) {
                    this.equipItemOnCompanion(characterfView, item2, position);
                }
                else {
                    WakfuGameEntity.getInstance().getLocalPlayer().transfertItem(item2, (short)1, msg6.getSourcePosition(), position, msg6.getSourceUniqueId(), msg6.getDestinationUniqueId());
                    PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "actorEquipment");
                }
                return false;
            }
            case 16803: {
                final Fight fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
                if (fight != null && fight.getStatus() != AbstractFight.FightStatus.PLACEMENT) {
                    return false;
                }
                final LocalPlayerCharacter localPlayer = UIEquipmentFrame.getCharacter();
                if (localPlayer.isDead()) {
                    final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("action.error.cantWhileDead"));
                    chatMessage.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(chatMessage);
                    return false;
                }
                final UIItemMessage msg6 = (UIItemMessage)message;
                final CharacterView sourceCharacter = msg6.getSourceCharacter();
                final Item item3 = msg6.getItem();
                final AbstractBag targetInventory = localPlayer.getBags().getFirstContainerWithFreePlaceFor(item3);
                if (!sourceCharacter.isCompanion() && !((ArrayInventoryWithoutCheck<Item, R>)localPlayer.getEquipmentInventory()).canRemove(item3)) {
                    return false;
                }
                if (targetInventory == null) {
                    Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.bagFull"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 102, 1);
                    return false;
                }
                final long destination = targetInventory.getUid();
                if (destination == -1L) {
                    return false;
                }
                try {
                    if (!targetInventory.canAdd(msg6.getItem())) {
                        return false;
                    }
                }
                catch (Exception e) {
                    return false;
                }
                if (sourceCharacter.isCompanion()) {
                    final long companionId = ((CharacteristicCompanionView)sourceCharacter).getCompanionId();
                    final long itemUid = item3.getUniqueId();
                    short destPos = -1;
                    if (destPos == -1) {
                        destPos = targetInventory.getFirstStackableIndeForContent(item3);
                        if (destPos == -1) {
                            destPos = targetInventory.getFirstFreeIndex();
                        }
                    }
                    final RemoveItemFromCompanionEquipmentRequestMessage removeMsg = new RemoveItemFromCompanionEquipmentRequestMessage(companionId, itemUid, destination, destPos);
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(removeMsg);
                }
                else {
                    WakfuGameEntity.getInstance().getLocalPlayer().transfertItem(msg6.getItem(), (short)(-1), msg6.getSourcePosition(), (byte)(-1), 2L, destination);
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "actorEquipment");
                return false;
            }
            case 16804: {
                final LocalPlayerCharacter localPlayer2 = UIEquipmentFrame.getCharacter();
                final Fight fight2 = localPlayer2.getCurrentFight();
                if (fight2 != null && fight2.getStatus() != AbstractFight.FightStatus.PLACEMENT) {
                    return false;
                }
                final UIItemMessage msg6 = (UIItemMessage)message;
                CharacterView destinationView = msg6.getDestinationCharacter();
                if (destinationView == null) {
                    destinationView = HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(HeroUtils.getHeroWithBagUid(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), msg6.getDestinationUniqueId()));
                }
                final CharacterView sourceView = msg6.getSourceCharacter();
                final LocalPlayerCharacter sourceHero = HeroesManager.INSTANCE.getHero(sourceView.getCharacterInfo().getId());
                final Item item4 = msg6.getItem();
                final byte destPos2 = msg6.getDestinationPosition();
                if (destinationView != null && sourceView != null && !sourceView.equals(destinationView)) {
                    final long itemUid2 = item4.getUniqueId();
                    if (!destinationView.isCompanion() && !sourceView.isCompanion()) {
                        sourceHero.transfertItem(item4, msg6.getQuantity(), msg6.getSourcePosition(), destPos2, msg6.getSourceUniqueId(), msg6.getDestinationUniqueId());
                    }
                    else if (destinationView.isCompanion()) {
                        this.equipItemOnCompanion(destinationView, item4, destPos2);
                    }
                    else {
                        final long companionId2 = ((CharacteristicCompanionView)sourceView).getCompanionId();
                        final long bagId = msg6.getDestinationUniqueId();
                        final RemoveItemFromCompanionEquipmentRequestMessage removeMsg2 = new RemoveItemFromCompanionEquipmentRequestMessage(companionId2, itemUid2, bagId, destPos2);
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(removeMsg2);
                    }
                }
                else {
                    sourceHero.transfertItem(item4, msg6.getQuantity(), msg6.getSourcePosition(), destPos2, msg6.getSourceUniqueId(), msg6.getDestinationUniqueId());
                }
                if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                    Xulor.getInstance().unload("splitStackDialog");
                }
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "actorEquipment");
                return false;
            }
            case 16828: {
                final UIItemMessage msg7 = (UIItemMessage)message;
                final Message regenWithItemRequestMessage = new RegenWithItemRequestMessage(msg7.getItem().getReferenceId(), msg7.getDestinationCharacter().getCharacterInfo().getId());
                final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
                if (networkEntity != null) {
                    networkEntity.sendMessage(regenWithItemRequestMessage);
                }
                return false;
            }
            case 16820: {
                final UIItemMessage msg7 = (UIItemMessage)message;
                final Item item = msg7.getItem();
                final LocalPlayerCharacter localPlayer3 = HeroUtils.getHeroWithItemUidInBags(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), item.getUniqueId());
                if (localPlayer3.getCurrentFight() != null) {
                    return false;
                }
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.throwAwayItem", item.getName()), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            if (item.hasPet()) {
                                UIPetFrame.getInstance().unloadPetDetailForItem(item);
                            }
                            if (localPlayer3.getBags().contains(msg7.getItem().getUniqueId())) {
                                localPlayer3.throwAwayItem(item, msg7.getQuantity(), msg7.getSourceUniqueId());
                                final short quantityDropped = (msg7.getQuantity() == -1) ? item.getQuantity() : msg7.getQuantity();
                                ItemFeedbackHelper.sendChatItemRemovedMessage(item, quantityDropped);
                                final Item itemDetailed = (Item)PropertiesProvider.getInstance().getObjectProperty("itemDetail", "equipmentDialog");
                                if (itemDetailed != null && itemDetailed.equals(item)) {
                                    PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", null, "equipmentDialog");
                                    PropertiesProvider.getInstance().setPropertyValue("pet", null);
                                }
                            }
                            if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                                Xulor.getInstance().unload("splitStackDialog");
                            }
                        }
                    }
                });
                return false;
            }
            case 16827: {
                final UIItemMessage msg7 = (UIItemMessage)message;
                final LocalPlayerCharacter localPlayer = HeroesManager.INSTANCE.getHero(msg7.getDestinationCharacter().getCharacterInfo().getId());
                if (localPlayer.getCurrentFight() != null || ClientTradeHelper.INSTANCE.isTradeRunning()) {
                    return false;
                }
                if (localPlayer.isActiveProperty(WorldPropertyType.DELETE_ITEM_DISABLED)) {
                    ChatWorldPropertyTypeErrorManager.writeChatErrorMessage(WorldPropertyType.DELETE_ITEM_DISABLED.getId());
                    return false;
                }
                final Item item5 = msg7.getItem();
                if (item5 != null && item5.getReferenceItem().getCriterion(ActionsOnItem.DROP) != null && item5.getReferenceItem().getCriterion(ActionsOnItem.DELETE) != null && !item5.getReferenceItem().getCriterion(ActionsOnItem.DROP).isValid(localPlayer, localPlayer.getPosition(), item5, localPlayer.getEffectContext()) && !item5.getReferenceItem().getCriterion(ActionsOnItem.DELETE).isValid(localPlayer, localPlayer.getPosition(), item5, localPlayer.getEffectContext())) {
                    ItemFeedbackHelper.sendChatItemUndeletableMessage(item5);
                    return false;
                }
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.deleteItem", item5.getName()), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            if (item5.hasPet()) {
                                UIPetFrame.getInstance().unloadPetDetailForItem(item5);
                            }
                            final ClientBagContainer containerBagContainer = localPlayer.getBags();
                            final AbstractBag source = containerBagContainer.getFirstContainerWith(item5.getUniqueId());
                            if (source != null) {
                                source.remove(item5);
                            }
                            final DeleteItemRequestMessage deleteItemRequestMessage = new DeleteItemRequestMessage();
                            deleteItemRequestMessage.setItemId(item5.getUniqueId());
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(deleteItemRequestMessage);
                            PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", null, "equipmentDialog");
                            PropertiesProvider.getInstance().setPropertyValue("pet", null);
                        }
                    }
                });
                return false;
            }
            case 16712: {
                final UIUseItemMessage useItemMessage = (UIUseItemMessage)message;
                final LocalPlayerCharacter localPlayer = UIEquipmentFrame.getCharacter();
                if (localPlayer.isDead()) {
                    final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("action.error.cantWhileDead"));
                    chatMessage.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(chatMessage);
                    return false;
                }
                final Item item5 = useItemMessage.getItem();
                if (item5 != null && item5.isUsableInWorld()) {
                    if (WakfuGameEntity.getInstance().hasFrame(UIWorldItemUseInteractionFrame.getInstance()) && UIWorldItemUseInteractionFrame.getInstance().getItem() == item5) {
                        WakfuGameEntity.getInstance().removeFrame(UIWorldItemUseInteractionFrame.getInstance());
                        return false;
                    }
                    final AbstractReferenceItem referenceItem = item5.getReferenceItem();
                    switch (((ReferenceItem)referenceItem).getUsageTarget()) {
                        case AUTO_USE: {
                            if (referenceItem.getItemAction() == null && item5.iterator().hasNext()) {
                                final int referenceId = item5.getReferenceId();
                                final boolean isMarketBoostItem = this.isMarketBoostItem(referenceId);
                                if (isMarketBoostItem) {
                                    this.displayMsgBoxIfNecessary(item5, localPlayer);
                                }
                                else {
                                    this.sendItemUseMessage(item5);
                                }
                            }
                            if (runItemAction(localPlayer, item5, localPlayer.getPositionConst())) {
                                final ChatMessage chatMessage2 = new ChatMessage(WakfuTranslator.getInstance().getString("chat.item.selfUse", UIChatFrame.getItemFormatedForChatLinkString(item5)));
                                chatMessage2.setPipeDestination(4);
                                ChatManager.getInstance().pushMessage(chatMessage2);
                                break;
                            }
                            break;
                        }
                        case DISTANCE:
                        case WORLD: {
                            UIWorldItemUseInteractionFrame.getInstance().setSelectedItem(item5);
                            UIWorldItemUseInteractionFrame.getInstance().setCharacter(localPlayer);
                            WakfuGameEntity.getInstance().pushFrame(UIWorldItemUseInteractionFrame.getInstance());
                            break;
                        }
                    }
                }
                return false;
            }
            case 16715: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final long itemId2 = msg.getLongValue();
                final LocalPlayerCharacter localPlayer4 = HeroUtils.getHeroWithItemUidInBags(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), itemId2);
                final Item itemToMerge = localPlayer4.getBags().getItemFromInventories(itemId2);
                if (itemToMerge == null) {
                    return false;
                }
                final short itemSetId = itemToMerge.getReferenceItem().getSetId();
                final ItemSet itemSet2 = ItemSetManager.getInstance().getItemSet(itemSetId);
                if (itemSet2 == null) {
                    UIItemManagementFrame.m_logger.warn((Object)"On essaye de merger un itemSet inexistant");
                    return false;
                }
                if (!ItemSetMergeHelper.isItemSetMergeable(itemSet2)) {
                    UIItemManagementFrame.m_logger.warn((Object)("Le set d'id=" + itemSetId + " n'est pas mergeable."));
                    return false;
                }
                final boolean ok = ItemSetMergeHelper.checkInventoryForMerge(localPlayer4, itemSet2, true);
                if (ok) {
                    final MergeIntoItemSetRequestMessage netMsg = new MergeIntoItemSetRequestMessage();
                    netMsg.setItemSetId(itemSetId);
                    netMsg.setCharacterId(localPlayer4.getId());
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
                }
                return false;
            }
            case 16870: {
                final UISocketGemMessage msg8 = (UISocketGemMessage)message;
                final Item gem = msg8.getGem();
                final Item item5 = msg8.getItem();
                final byte index = msg8.getByteValue();
                if (ClientTradeHelper.INSTANCE.isTradeRunning()) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.impossibleDuringExchange"), 3);
                    return false;
                }
                final LocalPlayerCharacter localPlayer5 = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer5.isOnFight()) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.impossibleDuringFight"), 3);
                    return false;
                }
                if (item5 == null) {
                    return false;
                }
                if (!ItemHelper.checkWePossessTheItem(item5) && !ItemHelper.checkCompanionItem(item5)) {
                    return false;
                }
                if (!item5.hasGems()) {
                    return false;
                }
                if (gem.getReferenceItem().getGemElementType() != GemElementType.GEM) {
                    return false;
                }
                if (item5.getQuantity() != 1) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.cantSocketGemOnStackedItem"), 3);
                    return false;
                }
                final Gems gems = item5.getGems();
                if (gem.getLevel() < item5.getLevel()) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.gemIsTooLowLevel"), 3);
                    return false;
                }
                if (!gems.canGem(gem.getReferenceItem(), index)) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.cantSocketGem"), 3);
                    return false;
                }
                final String msgBoxMsg = WakfuTranslator.getInstance().getString("question.socketGem.emptySlot");
                final MessageBoxControler messageBoxControler2 = Xulor.getInstance().msgBox(msgBoxMsg, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler2.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final GemRequestMessage netMsg = new GemRequestMessage();
                            netMsg.setGemItemId(gem.getUniqueId());
                            netMsg.setGemmedItemId(item5.getUniqueId());
                            netMsg.setIndex(index);
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
                        }
                    }
                });
                return false;
            }
            case 19080: {
                final UIMessage uiMessage = (UIMessage)message;
                final RollRandomElementsRequestMessage netMsg2 = new RollRandomElementsRequestMessage();
                netMsg2.setItemUid(uiMessage.getLongValue());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg2);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean isMarketBoostItem(final int referenceId) {
        final MarketBoostItem[] values = MarketBoostItem.values();
        for (int i = 0; i < values.length; ++i) {
            final MarketBoostItem boostItem = values[i];
            if (referenceId == boostItem.m_refId) {
                return true;
            }
        }
        return false;
    }
    
    private void displayMsgBoxIfNecessary(final Item item, final LocalPlayerCharacter localPlayer) {
        final int referenceId = item.getReferenceId();
        boolean displayMsgBox = false;
        final TIntHashSet unappliedStateId = new TIntHashSet();
        final MarketBoostItem boostItem = MarketBoostItem.getMarketBoostItem(referenceId);
        if (boostItem == null) {
            this.sendItemUseMessage(item);
            return;
        }
        final int[] unappliedStates = boostItem.m_unappliedStates;
        for (int i = 0; i < unappliedStates.length; ++i) {
            final int unappliedState = unappliedStates[i];
            if (localPlayer.hasState(unappliedState)) {
                displayMsgBox = true;
                unappliedStateId.add(unappliedState);
            }
        }
        if (!displayMsgBox || unappliedStateId.isEmpty()) {
            this.sendItemUseMessage(item);
            return;
        }
        String msg;
        if (unappliedStateId.size() == 1) {
            final String stateName = WakfuTranslator.getInstance().getString(8, unappliedStateId.toArray()[0], new Object[0]);
            msg = WakfuTranslator.getInstance().getString("question.willUnapplyState", stateName);
        }
        else {
            final String stateName2 = WakfuTranslator.getInstance().getString(8, unappliedStateId.toArray()[0], new Object[0]);
            final String stateName3 = WakfuTranslator.getInstance().getString(8, unappliedStateId.toArray()[1], new Object[0]);
            msg = WakfuTranslator.getInstance().getString("question.willUnapplyState.2", stateName2, stateName3);
        }
        final MessageBoxControler msgBox = Xulor.getInstance().msgBox(msg, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        msgBox.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    UIItemManagementFrame.this.sendItemUseMessage(item);
                }
            }
        });
    }
    
    private void sendItemUseMessage(final Item item) {
        final PlayerUseItemRequestMessage netMsg = new PlayerUseItemRequestMessage();
        netMsg.setItemUid(item.getUniqueId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
    }
    
    private void equipItemOnCompanion(final CharacterView destinationCharacter, final Item item, final byte position) {
        final CharacteristicCompanionView companionView = (CharacteristicCompanionView)destinationCharacter;
        final long companionId = companionView.getCompanionId();
        final long itemUid = item.getUniqueId();
        final ItemEquipment equipment = companionView.getItemEquipment();
        if (!((ArrayInventoryWithoutCheck<Item, R>)equipment).getContentChecker().checkCriterion(item, companionView.getCharacterInfo(), companionView.getCharacterInfo().getAppropriateContext())) {
            ErrorsMessageTranslator.getInstance().pushMessage(60, 3, new Object[0]);
            return;
        }
        if (item.hasBind() && !item.isBound() && !item.getBind().getType().isOnPickup()) {
            final String msgText = WakfuTranslator.getInstance().getString("item.bound.onEquipQuestion");
            final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        final AddItemToCompanionEquipmentRequestMessage addMsg = new AddItemToCompanionEquipmentRequestMessage(companionId, position, itemUid);
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(addMsg);
                    }
                }
            });
        }
        else {
            final AddItemToCompanionEquipmentRequestMessage addMsg = new AddItemToCompanionEquipmentRequestMessage(companionId, position, itemUid);
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(addMsg);
        }
    }
    
    public static boolean runItemAction(final CharacterInfo character, final Item item, final Point3 castPosition) {
        final AbstractClientItemAction itemAction = (AbstractClientItemAction)item.getReferenceItem().getItemAction();
        if (itemAction == null || character.isOnFight()) {
            return false;
        }
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventItemUsed(item.getReferenceItem()));
        itemAction.setCastPosition(castPosition);
        if (itemAction.needsConfirm(item.getReferenceId())) {
            displayConfirmMsgBox(item, itemAction);
            return false;
        }
        return itemAction.isRunnable(item) && itemAction.run(item);
    }
    
    private static void displayConfirmMsgBox(final Item item, final AbstractClientItemAction itemAction) {
        final MessageBoxControler msgBox = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.activate.restat"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
        msgBox.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    itemAction.run(item);
                }
            }
        });
    }
    
    private boolean openCloseDetailDialog(final AbstractUIDetailMessage msg, final Object item, final String parentWindowId, final String dialogPath, final String dialogId) {
        if (!Xulor.getInstance().isLoaded(dialogId) && this.m_openedItemDetail.size() < 30) {
            final XulorLoadBuilder builder = new XulorLoadBuilder();
            builder.setId(dialogId).setPath(Dialogs.getDialogPath(dialogPath)).setOptions(129L);
            Window itemWindow;
            if (parentWindowId == null) {
                itemWindow = (Window)Xulor.getInstance().loadInto(builder.build());
                itemWindow.addWindowPostProcessedListener(new WindowPostProcessedListener() {
                    @Override
                    public void windowPostProcessed() {
                        itemWindow.setX(Math.min(msg.getX(), MasterRootContainer.getInstance().getWidth() - itemWindow.getWidth()));
                        itemWindow.setY(Math.min(msg.getY(), MasterRootContainer.getInstance().getHeight() - itemWindow.getHeight()));
                        itemWindow.removeWindowPostProcessedListener(this);
                    }
                });
            }
            else {
                builder.setRelativeCascadeElementId((this.m_lastItemDetailDialogId == null) ? parentWindowId : this.m_lastItemDetailDialogId).setRelativePositionElementId(parentWindowId).setControlGroupId(dialogPath);
                itemWindow = (Window)Xulor.getInstance().loadInto(builder.build());
            }
            this.m_lastItemDetailDialogId = dialogId;
            PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", item, itemWindow.getElementMap(), true);
            this.m_openedItemDetail.add(dialogId);
            return true;
        }
        Xulor.getInstance().unload(dialogId);
        return false;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals(UIItemManagementFrame.this.m_lastItemDetailDialogId)) {
                        UIItemManagementFrame.this.m_lastItemDetailDialogId = null;
                    }
                    UIItemManagementFrame.this.m_openedItemDetail.remove(id);
                }
            };
            PropertiesProvider.getInstance().setPropertyValue("pet", null);
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().putActionClass("wakfu.itemDetails", ItemDetailsDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_lastItemDetailDialogId = null;
            this.m_openedItemDetail.clear();
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().removeActionClass("wakfu.itemDetails");
            this.clean();
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public String getItemDetailDialogId(final long uniqueId) {
        return "itemDetailDialog_" + uniqueId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIItemManagementFrame.class);
        m_instance = new UIItemManagementFrame();
    }
    
    private enum MarketBoostItem
    {
        FIOLE_DE_SAGESSE(18351, new int[] { 2760, 2787 }), 
        POTION_DE_SAGESSE(18401, new int[] { 2752, 2787 }), 
        FIOLE_DE_PROSPECTION(18355, new int[] { 2766, 2787 }), 
        POTION_DE_PROSPECTION(18406, new int[] { 2765, 2787 }), 
        POTION_DE_SAGE_FORTUNE(18357, new int[] { 2752, 2760, 2765, 2766 });
        
        final int m_refId;
        final int[] m_unappliedStates;
        
        private MarketBoostItem(final int refId, final int[] unappliedStates) {
            this.m_refId = refId;
            this.m_unappliedStates = unappliedStates;
        }
        
        public static MarketBoostItem getMarketBoostItem(final int referenceId) {
            final MarketBoostItem[] values = values();
            for (int i = 0; i < values.length; ++i) {
                final MarketBoostItem boostItem = values[i];
                if (referenceId == boostItem.m_refId) {
                    return boostItem;
                }
            }
            return null;
        }
    }
}
