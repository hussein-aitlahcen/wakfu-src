package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.wakfu.client.core.game.hero.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.kernel.events.*;
import java.util.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.game.item.ui.*;
import com.ankamagames.wakfu.client.ui.protocol.message.exchange.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.ui.protocol.message.Merchant.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.room.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.ui.protocol.message.shortcut.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

@XulorActionsTag
public class EquipmentDialogActions extends EquipmentLikeDialogActions
{
    public static final String PACKAGE = "wakfu.equipment";
    public static final String ANIM_NAME = "_AnimStatique";
    private static long m_itemId;
    protected static final Logger m_logger;
    private static Window m_coloredInventoryWindow;
    private static Image m_image;
    private static final ArrayList<ItemDragNDropListener> m_dragNDropListeners;
    private static ParticleDecorator m_particleDecorator;
    
    public static void addListener(final ItemDragNDropListener l) {
        EquipmentDialogActions.m_dragNDropListeners.add(l);
    }
    
    public static void removeListener(final ItemDragNDropListener l) {
        EquipmentDialogActions.m_dragNDropListeners.remove(l);
    }
    
    public static void inventoryDragItem(final DragEvent dragEvent, final Item item, final CharacterView characterView) {
        final AbstractBag bag = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getFirstContainerWith(item.getUniqueId());
        if (bag != null) {
            inventoryDragItem(dragEvent, ((Bag)bag).getBagView(), characterView);
        }
    }
    
    public static void inventoryDragItem(final DragEvent dragEvent, final Item item, final LocalPlayerCharacter character) {
        final AbstractBag bag = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getFirstContainerWith(item.getUniqueId());
        if (bag != null) {
            inventoryDragItem(dragEvent, ((Bag)bag).getBagView(), HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(character));
        }
    }
    
    public static void inventoryDragItem(final DragEvent dragEvent, final BagView bagView, final LocalPlayerCharacter character) {
        inventoryDragItem(dragEvent, bagView, HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(character));
    }
    
    public static void inventoryDragItem(final DragEvent dragEvent, final BagView bagView, final CharacterView sourceCharacterView) {
        final Bag bag = bagView.getBag();
        final Object dragEventValue = dragEvent.getValue();
        if (dragEventValue != null && dragEventValue instanceof Item) {
            if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                Xulor.getInstance().unload("splitStackDialog");
            }
            final Item item = (Item)dragEventValue;
            final short inventorySourcePosition = bag.getPosition(item.getUniqueId());
            EquipmentDialogActions.m_itemId = item.getUniqueId();
            SplitStackDialogActions.setSourceUniqueId(bag.getUid());
            SplitStackDialogActions.setSourcePosition((byte)inventorySourcePosition);
            final short breedId = WakfuGameEntity.getInstance().getLocalPlayer().getBreedId();
            final CharacterView view = (sourceCharacterView == null || sourceCharacterView.isCompanion()) ? UICompanionsEmbeddedFrame.getCharacterSheetView(breedId) : sourceCharacterView;
            SplitStackDialogActions.setSourceCharacter(view);
            PropertiesProvider.getInstance().setPropertyValue("exchange.sourceBag", bag);
            PropertiesProvider.getInstance().setPropertyValue("exchange.sourcePosition", inventorySourcePosition);
            onDragItem(item);
            if (WakfuGameEntity.getInstance().hasFrame(UIMarketFrame.getInstance()) && UIMarketFrame.getInstance().getCurrentPageIndex() == 1) {
                UIMarketFrame.getInstance().highLightSellDropZone();
            }
        }
    }
    
    public static void dropItem(final Event dropEvent) {
        PropertiesProvider.getInstance().removeProperty("exchange.sourceBag");
        PropertiesProvider.getInstance().removeProperty("exchange.sourcePosition");
        if (WakfuGameEntity.getInstance().hasFrame(UIMarketFrame.getInstance()) && UIMarketFrame.getInstance().getCurrentPageIndex() == 1) {
            UIMarketFrame.getInstance().stopDropZoneHighlight();
        }
        if (dropEvent instanceof DropEvent) {
            onDropItem();
        }
        else if (dropEvent instanceof DropOutEvent) {
            onDropItem();
        }
    }
    
    public static void onDropItem() {
        UIPetFrame.getInstance().resetPetEquipSlots();
        UIBuildingPanelFrame.getInstance().resetSlot();
        UISeedSpreaderFrame.getInstance().resetSlot();
        final ArrayList<ElementMap> elementMaps = new ArrayList<ElementMap>();
        final Environment e = Xulor.getInstance().getEnvironment();
        for (final String dialogId : UIEquipmentFrame.getInstance().getLoadedDialogs()) {
            final ElementMap equipmentDialog = e.getElementMap(dialogId);
            if (equipmentDialog != null) {
                elementMaps.add(equipmentDialog);
            }
        }
        final ElementMap stuffPreviewDialog = e.getElementMap("stuffPreviewDialog");
        if (stuffPreviewDialog != null) {
            elementMaps.add(stuffPreviewDialog);
        }
        for (final ElementMap elementMap : elementMaps) {
            for (final EquipmentPosition ipos : EquipmentPosition.values()) {
                final Widget container = (Widget)elementMap.getElement("equipDisabled" + ipos.m_id);
                if (container != null && container.getVisible()) {
                    container.setVisible(false);
                }
            }
        }
        for (int i = 0, size = EquipmentDialogActions.m_dragNDropListeners.size(); i < size; ++i) {
            EquipmentDialogActions.m_dragNDropListeners.get(i).onDrop();
        }
    }
    
    public static void onDragItem(final Item item) {
        UIPetFrame.getInstance().highLightPetEquipSlots(item);
        UIBuildingPanelFrame.getInstance().highLightCustomSlot(item);
        UISeedSpreaderFrame.getInstance().highLightCustomSlot(item);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (ClientTradeHelper.INSTANCE.getCurrentTrade() != null) {
            return;
        }
        final EquipmentPosition[] equipmentPositions = item.getReferenceItem().getItemType().getEquipmentPositions();
        if (equipmentPositions.length > 0) {
            final ArrayList<ElementMap> elementMaps = new ArrayList<ElementMap>();
            final Environment e = Xulor.getInstance().getEnvironment();
            for (final String dialogId : UIEquipmentFrame.getInstance().getLoadedDialogs()) {
                final ElementMap equipmentDialog = e.getElementMap(dialogId);
                if (equipmentDialog != null) {
                    elementMaps.add(equipmentDialog);
                }
            }
            final ElementMap stuffPreviewDialog = e.getElementMap("stuffPreviewDialog");
            if (stuffPreviewDialog != null) {
                elementMaps.add(stuffPreviewDialog);
            }
            for (final ElementMap elementMap : elementMaps) {
                for (final EquipmentPosition ipos : EquipmentPosition.values()) {
                    final Widget container = (Widget)elementMap.getElement("equipDisabled" + ipos.m_id);
                    if (container != null) {
                        boolean found = false;
                        if (!localPlayer.isDead()) {
                            for (final EquipmentPosition itemPos : equipmentPositions) {
                                if (itemPos.m_id == ipos.m_id) {
                                    found = true;
                                }
                            }
                        }
                        container.setVisible(true);
                        if (!found) {
                            container.setStyle("itemDisabled");
                        }
                        else {
                            container.setStyle("itemEnabled");
                        }
                    }
                }
            }
        }
        for (int i = 0, size = EquipmentDialogActions.m_dragNDropListeners.size(); i < size; ++i) {
            EquipmentDialogActions.m_dragNDropListeners.get(i).onDrag(item);
        }
    }
    
    public static void equipmentDragItem(final Event event, final String position, final CharacterView characterView) {
        if (event instanceof DragEvent) {
            if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                Xulor.getInstance().unload("splitStackDialog");
            }
            final DragEvent dragEvent = (DragEvent)event;
            if (dragEvent.getValue() != null && dragEvent.getValue() instanceof Item) {
                EquipmentDialogActions.m_itemId = ((Item)dragEvent.getValue()).getUniqueId();
                SplitStackDialogActions.setSourceUniqueId(2L);
                SplitStackDialogActions.setSourcePosition(Byte.valueOf(position));
                SplitStackDialogActions.setSourceCharacter(characterView);
                PropertiesProvider.getInstance().setPropertyValue("exchange.sourceBag", WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory());
                PropertiesProvider.getInstance().setPropertyValue("exchange.sourcePosition", SplitStackDialogActions.getSourcePosition());
                final Item item = (Item)dragEvent.getValue();
                onDragItem(item);
            }
        }
    }
    
    public static void itemDropOut(final DropOutEvent event) {
        itemDropOut(event, null);
    }
    
    public static void itemDropOut(final DropOutEvent event, final CharacterView characterView) {
        dropItem(event);
        if (event.getValue() instanceof ItemDisplayerImpl.ItemPlaceHolder) {
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (ClientTradeHelper.INSTANCE.getCurrentTrade() != null) {
            return;
        }
        if (!WakfuGameEntity.getInstance().getLocalPlayer().hasSubscriptionRight(SubscriptionRight.DROP_ITEM)) {
            final String errorMessage = WakfuTranslator.getInstance().getString("error.playerNotSubscribed");
            final ChatMessage m = new ChatMessage(errorMessage);
            m.setPipeDestination(3);
            ChatManager.getInstance().pushMessage(m);
            return;
        }
        if (event.getValue() instanceof Item) {
            final Item item = (Item)event.getValue();
            if (MasterRootContainer.getInstance().getWidget(event.getScreenX(), event.getScreenY()) == MasterRootContainer.getInstance() && item.getUniqueId() == EquipmentDialogActions.m_itemId) {
                if (characterView != null && characterView.isCompanion()) {
                    final short breedId = WakfuGameEntity.getInstance().getLocalPlayer().getBreedId();
                    final CharacterView destinationCharacterView = UICompanionsEmbeddedFrame.getCharacterSheetView(breedId);
                    final UIItemMessage message = new UIItemMessage();
                    message.setItem(item);
                    message.setPosition(characterView.getItemEquipment().getPosition(item.getUniqueId()));
                    message.setSourceCharacter(characterView);
                    message.setDestinationCharacter(destinationCharacterView);
                    message.setId(16803);
                    Worker.getInstance().pushMessage(message);
                    return;
                }
                final boolean shiftPressed = event.hasShift();
                final boolean isDefaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
                if (item.isBound() || item.isRent() || (item.getReferenceItem().getCriterion(ActionsOnItem.DROP) != null && !item.getReferenceItem().getCriterion(ActionsOnItem.DROP).isValid(localPlayer, localPlayer.getPosition(), item, localPlayer.getEffectContext())) || (item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE) != null && !item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE).isValid(localPlayer, localPlayer.getPosition(), item, localPlayer.getEffectContext()))) {
                    ItemFeedbackHelper.sendChatItemUndroppableMessage(item);
                }
                else if (item.getQuantity() > 1 && ((shiftPressed && !isDefaultSplitMode) || (!shiftPressed && isDefaultSplitMode))) {
                    SplitStackDialogActions.setMaxQuantity(item.getQuantity());
                    SplitStackDialogActions.setItem(item);
                    SplitStackDialogActions.setMessageType((short)16820);
                    final UIItemMessage message = new UIItemMessage();
                    message.setItem(item);
                    message.setX((short)event.getScreenX());
                    message.setY((short)event.getScreenY());
                    message.setId(16821);
                    Worker.getInstance().pushMessage(message);
                }
                else {
                    final UIItemMessage message = new UIItemMessage();
                    message.setSourceUniqueId(SplitStackDialogActions.getSourceUniqueId());
                    message.setItem(item);
                    message.setQuantity((short)(-1));
                    message.setId(16820);
                    Worker.getInstance().pushMessage(message);
                }
            }
        }
    }
    
    public static void dropBag(final Event event) {
        if (!(event instanceof DropEvent)) {
            return;
        }
        onDropItem();
        final DropEvent dropEvent = (DropEvent)event;
        final Object value = dropEvent.getValue();
        if (value == null) {
            return;
        }
        if (value instanceof Item) {
            final Item item = (Item)value;
            if (item.getType().getId() == 218 || item.getType().getId() == 399) {
                final ItemTrade currentTrade = ClientTradeHelper.INSTANCE.getCurrentTrade();
                if (currentTrade != null && item.getUniqueId() == currentTrade.getCurrentDragUniqueId()) {
                    final UIExchangeMoveItemMessage message = new UIExchangeMoveItemMessage();
                    message.setId(16811);
                    message.setExchangeId(currentTrade.getId());
                    message.setItem(item);
                    message.setItemQuantity(item.getQuantity());
                    Worker.getInstance().pushMessage(message);
                    currentTrade.setCurrentDragUniqueId(-1L);
                    return;
                }
                final RenderableContainer rc = dropEvent.getDroppedInto().getRenderableParent();
                final byte destinationPosition = (byte)(rc.getCollection().getTableIndex(rc) + 1);
                final byte sourcePosition = SplitStackDialogActions.getSourcePosition();
                final UIItemMessage message2 = new UIItemMessage();
                message2.setItem(item);
                message2.setSourcePosition(sourcePosition);
                message2.setDestinationPosition(destinationPosition);
                message2.setDestinationCharacter(HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(UIEquipmentFrame.getCharacter()));
                message2.setSourceUniqueId(SplitStackDialogActions.getSourceUniqueId());
                message2.setId(17710);
                Worker.getInstance().pushMessage(message2);
            }
            else {
                final RenderableContainer rc2 = dropEvent.getDroppedInto().getRenderableParent();
                final byte destinationPosition2 = (byte)(rc2.getCollection().getTableIndex(rc2) + 1);
                final byte sourcePosition2 = SplitStackDialogActions.getSourcePosition();
                final AbstractBag bag = WakfuGameEntity.getInstance().getLocalPlayer().getBags().getBagFromPosition(destinationPosition2);
                if (bag != null) {
                    SplitStackDialogActions.setDestinationUniqueId(bag.getUid());
                    int destPos = bag.getFirstStackableIndeForContent(item);
                    if (destPos == -1) {
                        destPos = bag.getFirstFreeIndex();
                    }
                    if (destPos == -1) {
                        Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.bagFull"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 102, 1);
                        return;
                    }
                    SplitStackDialogActions.setDestinationPosition((byte)destPos);
                    final boolean shiftPressed = dropEvent.hasShift();
                    final boolean isDefaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
                    final boolean split = item.getQuantity() > 1 && ((shiftPressed && !isDefaultSplitMode) || (!shiftPressed && isDefaultSplitMode));
                    if (split) {
                        SplitStackDialogActions.setDestinationUniqueId(bag.getUid());
                        SplitStackDialogActions.setMaxQuantity(item.getQuantity());
                        SplitStackDialogActions.setItem(item);
                        SplitStackDialogActions.setMessageType((short)16804);
                        final UIItemMessage message3 = new UIItemMessage();
                        message3.setItem(item);
                        message3.setX((short)dropEvent.getScreenX());
                        message3.setY((short)dropEvent.getScreenY());
                        message3.setId(16821);
                        Worker.getInstance().pushMessage(message3);
                    }
                    else {
                        final UIItemMessage message3 = new UIItemMessage();
                        message3.setSourcePosition(sourcePosition2);
                        message3.setSourceUniqueId(SplitStackDialogActions.getSourceUniqueId());
                        message3.setDestinationUniqueId(bag.getUid());
                        message3.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                        message3.setItem(item);
                        message3.setQuantity((short)(-1));
                        message3.setId(16804);
                        Worker.getInstance().pushMessage(message3);
                    }
                }
            }
        }
        else if (value instanceof BagView) {
            final Bag container = ((BagView)value).getBag();
            final UIMessage message4 = new UIMessage();
            final RenderableContainer rc = dropEvent.getDroppedInto().getRenderableParent();
            message4.setByteValue((byte)(rc.getCollection().getTableIndex(rc) + 1));
            message4.setLongValue(container.getUid());
            message4.setId(17712);
            Worker.getInstance().pushMessage(message4);
        }
    }
    
    public static void equipmentDropItem(final Event event, final String position) {
        equipmentDropItem(event, position, null);
    }
    
    public static void equipmentDropItem(final Event event, final String position, final CharacterView destinationCharacter) {
        SplitStackDialogActions.setDestinationUniqueId(2L);
        SplitStackDialogActions.setDestinationPosition(Byte.valueOf(position));
        if (event instanceof DropEvent) {
            final DropEvent dropEvent = (DropEvent)event;
            if (dropEvent.getValue() != null && dropEvent.getValue() instanceof Item) {
                final Item item = (Item)dropEvent.getValue();
                dropItem(event);
                if (WakfuGameEntity.getInstance().getLocalPlayer().isTemporaryTransferInventoryActive()) {
                    final Property temporaryInventoryCurrentDragItemId = PropertiesProvider.getInstance().getProperty("temporaryInventory.currentDragItemId");
                    if (temporaryInventoryCurrentDragItemId != null && item.getUniqueId() == temporaryInventoryCurrentDragItemId.getLong()) {
                        final UIItemMessage message = new UIItemMessage();
                        message.setId(16825);
                        message.setItem(item);
                        message.setQuantity(item.getQuantity());
                        message.setDestinationUniqueId(SplitStackDialogActions.getDestinationUniqueId());
                        message.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                        Worker.getInstance().pushMessage(message);
                        return;
                    }
                }
                final ItemTrade currentTrade = ClientTradeHelper.INSTANCE.getCurrentTrade();
                if (currentTrade != null) {
                    if (item.getUniqueId() == currentTrade.getCurrentDragUniqueId()) {
                        final UIExchangeMoveItemMessage message2 = new UIExchangeMoveItemMessage();
                        message2.setId(16811);
                        message2.setExchangeId(currentTrade.getId());
                        message2.setItem(item);
                        message2.setItemQuantity(item.getQuantity());
                        Worker.getInstance().pushMessage(message2);
                        currentTrade.setCurrentDragUniqueId(-1L);
                    }
                }
                else if (item.getUniqueId() == EquipmentDialogActions.m_itemId) {
                    final UIItemMessage message = new UIItemMessage();
                    message.setQuantity((short)1);
                    message.setSourceUniqueId(SplitStackDialogActions.getSourceUniqueId());
                    message.setDestinationUniqueId(2L);
                    message.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                    message.setSourcePosition(SplitStackDialogActions.getSourcePosition());
                    message.setDestinationUniqueId(SplitStackDialogActions.getDestinationUniqueId());
                    message.setItem(item);
                    message.setSourceCharacter(SplitStackDialogActions.getSourceCharacter());
                    message.setDestinationCharacter(destinationCharacter);
                    message.setId(16804);
                    Worker.getInstance().pushMessage(message);
                }
            }
        }
    }
    
    public static void equipmentDropItem(final Event event) {
        equipmentDropItem(event, "-1");
    }
    
    public static void inventoryDropItem(final DropEvent event) {
        final Object value = event.getValue();
        if (value == null) {
            return;
        }
        Bag bag = null;
        final ClientBagContainer bagContainer = WakfuGameEntity.getInstance().getLocalPlayer().getBags();
        Item item = null;
        if (value instanceof BagView) {
            final Bag bagDropped = ((BagView)value).getBag();
            if (bagDropped.getUid() == 2L) {
                final UIItemMessage uiItemMessage = new UIItemMessage();
                uiItemMessage.setDestinationPosition((byte)(-1));
                uiItemMessage.setDestinationUniqueId(-1L);
                uiItemMessage.setSourceUniqueId(bagDropped.getUid());
                uiItemMessage.setId(17711);
                Worker.getInstance().pushMessage(uiItemMessage);
            }
            return;
        }
        if (value instanceof Item) {
            item = (Item)event.getValue();
        }
        else if (value instanceof MerchantInventoryItem) {
            item = ((MerchantInventoryItem)event.getValue()).getItem();
        }
        bag = (Bag)bagContainer.getFirstBagWithStackablePlace(item);
        if (bag == null) {
            bag = (Bag)bagContainer.getFirstContainerWithFreePlaceFor(item);
        }
        if (bag != null) {
            inventoryDropItem(event, bag.getBagView(), (byte)bag.getFirstFreeIndex());
        }
    }
    
    public static void inventoryDropItem(final Event event, final BagView bagView) {
        inventoryDropItem(event, bagView, (byte)(-1));
    }
    
    public static void inventoryDropItem(final Event event, final BagView bagView, byte destPos) {
        final Bag bag = bagView.getBag();
        if (event instanceof DropEvent) {
            final DropEvent dropEvent = (DropEvent)event;
            SplitStackDialogActions.setDestinationUniqueId(bag.getUid());
            if (destPos == -1) {
                final RenderableContainer rc = dropEvent.getDroppedInto().getRenderableParent();
                destPos = (byte)rc.getCollection().getTableIndex(rc);
            }
            SplitStackDialogActions.setDestinationPosition(destPos);
            final Object value = dropEvent.getValue();
            if (value == null) {
                return;
            }
            final boolean shiftPressed = dropEvent.hasShift();
            final boolean isDefaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
            if (value instanceof Item) {
                final Item item = (Item)value;
                if (item.equals(SeedSpreaderDialogActions.getDraggedItem())) {
                    return;
                }
                final CharacterView destinationCharacterView = HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(HeroUtils.getHeroWithBagUid(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), bag.getUid()));
                dropItem(event);
                if (StuffPreviewDialogActions.getDraggedItemId() != -1L) {
                    StuffPreviewDialogActions.setDraggedItemId(-1L);
                    return;
                }
                final boolean split = item.getQuantity() > 1 && ((shiftPressed && !isDefaultSplitMode) || (!shiftPressed && isDefaultSplitMode));
                final Property temporaryInventoryCurrentDragItemId = PropertiesProvider.getInstance().getProperty("temporaryInventory.currentDragItemId");
                if (WakfuGameEntity.getInstance().getLocalPlayer().isTemporaryTransferInventoryActive() && temporaryInventoryCurrentDragItemId != null && item.getUniqueId() == temporaryInventoryCurrentDragItemId.getLong()) {
                    if (split) {
                        SplitStackDialogActions.setMaxQuantity(item.getQuantity());
                        SplitStackDialogActions.setItem(item);
                        SplitStackDialogActions.setMessageType((short)16825);
                        final UIItemMessage message = new UIItemMessage();
                        message.setItem(item);
                        message.setX((short)dropEvent.getScreenX());
                        message.setY((short)dropEvent.getScreenY());
                        message.setId(16821);
                        Worker.getInstance().pushMessage(message);
                    }
                    else {
                        final UIItemMessage message = new UIItemMessage();
                        message.setId(16825);
                        message.setItem(item);
                        message.setQuantity(item.getQuantity());
                        message.setDestinationUniqueId(SplitStackDialogActions.getDestinationUniqueId());
                        message.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                        Worker.getInstance().pushMessage(message);
                    }
                    return;
                }
                short messageId = -1;
                if (item.getUniqueId() == CollectMachineDialogActions.getDraggedItemId()) {
                    messageId = 19321;
                    CollectMachineDialogActions.setDraggedItemId(-1L);
                }
                else if (item.getUniqueId() == StorageBoxDialogActions.getDraggedItemId()) {
                    messageId = 19327;
                    StorageBoxDialogActions.setDraggedItemId(-1L);
                }
                else if (item.getUniqueId() == VaultDialogActions.getDraggedItemId()) {
                    messageId = 19334;
                    VaultDialogActions.setDraggedItemId(-1L);
                }
                else {
                    if (item.getUniqueId() == BookcaseDialogActions.getDraggedItemId()) {
                        BookcaseDialogActions.setDraggedItemId(-1L);
                        final RenderableContainer rc2 = dropEvent.getDropped().getRenderableParent();
                        final short index = (short)rc2.getCollection().getTableIndex(rc2);
                        BookcaseDialogActions.removeBook(index);
                        return;
                    }
                    if (item.getUniqueId() == HavenWorldResourcesCollectorDialogActions.getDraggedItemId()) {
                        HavenWorldResourcesCollectorDialogActions.setDraggedItemId(-1L);
                        HavenWorldResourcesCollectorDialogActions.removeItem((FakeItem)item);
                        return;
                    }
                    if (item.getUniqueId() == EquipableDummyDialogActions.getDraggedItemId()) {
                        EquipableDummyDialogActions.setDraggedItemId(-1L);
                        EquipableDummyDialogActions.unequipItem(item.getReferenceId());
                        return;
                    }
                }
                if (messageId != -1) {
                    if (split) {
                        SplitStackDialogActions.setMaxQuantity(item.getQuantity());
                        SplitStackDialogActions.setItem(item);
                        SplitStackDialogActions.setMessageType(messageId);
                        final UIItemMessage message2 = new UIItemMessage();
                        message2.setItem(item);
                        message2.setX((short)dropEvent.getScreenX());
                        message2.setY((short)dropEvent.getScreenY());
                        message2.setId(16821);
                        Worker.getInstance().pushMessage(message2);
                    }
                    else {
                        final UIItemMessage message2 = new UIItemMessage();
                        message2.setId(messageId);
                        message2.setItem(item);
                        message2.setQuantity(item.getQuantity());
                        message2.setDestinationUniqueId(SplitStackDialogActions.getDestinationUniqueId());
                        message2.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                        Worker.getInstance().pushMessage(message2);
                    }
                    return;
                }
                final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
                if (exchanger != null) {
                    if (item.getUniqueId() == exchanger.getCurrentDragUniqueId()) {
                        if (split) {
                            SplitStackDialogActions.setMaxQuantity(item.getQuantity());
                            SplitStackDialogActions.setItem(item);
                            SplitStackDialogActions.setExchangeId(exchanger.getId());
                            SplitStackDialogActions.setMessageType((short)16811);
                            final UIItemMessage message3 = new UIItemMessage();
                            message3.setItem(item);
                            message3.setX((short)dropEvent.getScreenX());
                            message3.setY((short)dropEvent.getScreenY());
                            message3.setId(16821);
                            Worker.getInstance().pushMessage(message3);
                        }
                        else {
                            final UIExchangeMoveItemMessage message4 = new UIExchangeMoveItemMessage();
                            message4.setId(16811);
                            message4.setExchangeId(exchanger.getId());
                            message4.setItem(item);
                            message4.setItemQuantity(item.getQuantity());
                            Worker.getInstance().pushMessage(message4);
                        }
                        exchanger.setCurrentDragUniqueId(-1L);
                    }
                }
                else if (item.getUniqueId() == EquipmentDialogActions.m_itemId) {
                    if (SplitStackDialogActions.getSourcePosition() == SplitStackDialogActions.getDestinationPosition() && bag.getUid() == SplitStackDialogActions.getSourceUniqueId()) {
                        return;
                    }
                    if (split) {
                        SplitStackDialogActions.setDestinationUniqueId(bag.getUid());
                        SplitStackDialogActions.setMaxQuantity(item.getQuantity());
                        SplitStackDialogActions.setItem(item);
                        SplitStackDialogActions.setMessageType((short)16804);
                        final UIItemMessage message3 = new UIItemMessage();
                        message3.setItem(item);
                        message3.setX((short)dropEvent.getScreenX());
                        message3.setY((short)dropEvent.getScreenY());
                        message3.setSourceCharacter(SplitStackDialogActions.getSourceCharacter());
                        message3.setDestinationCharacter(destinationCharacterView);
                        message3.setId(16821);
                        Worker.getInstance().pushMessage(message3);
                    }
                    else {
                        final UIItemMessage message3 = new UIItemMessage();
                        message3.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                        message3.setSourcePosition(SplitStackDialogActions.getSourcePosition());
                        message3.setSourceUniqueId(SplitStackDialogActions.getSourceUniqueId());
                        message3.setDestinationUniqueId(bag.getUid());
                        message3.setSourceCharacter(SplitStackDialogActions.getSourceCharacter());
                        message3.setDestinationCharacter(destinationCharacterView);
                        message3.setItem(item);
                        message3.setQuantity((short)(-1));
                        message3.setId(16804);
                        Worker.getInstance().pushMessage(message3);
                    }
                }
            }
            else if (value instanceof MerchantInventoryItem) {
                final MerchantInventoryItem merchantItem = (MerchantInventoryItem)value;
                if (merchantItem.getQuantity() > 1 && ((shiftPressed && !isDefaultSplitMode) || (!shiftPressed && isDefaultSplitMode))) {
                    SplitStackDialogActions.setMaxQuantity(merchantItem.getQuantity());
                    SplitStackDialogActions.setMerchantItem(merchantItem);
                    SplitStackDialogActions.setMessageType((short)17305);
                    final UIItemMessage message5 = new UIItemMessage();
                    message5.setItem(merchantItem.getItem());
                    message5.setX((short)dropEvent.getScreenX());
                    message5.setY((short)dropEvent.getScreenY());
                    message5.setId(16821);
                    Worker.getInstance().pushMessage(message5);
                }
                else {
                    final UIMerchantMessage uiMsg = new UIMerchantMessage();
                    uiMsg.setMerchantItem(merchantItem);
                    uiMsg.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                    uiMsg.setContainerId(SplitStackDialogActions.getDestinationUniqueId());
                    uiMsg.setId(17305);
                    Worker.getInstance().pushMessage(uiMsg);
                }
            }
            else if (value instanceof BagView) {
                final Bag bagDropped = ((BagView)value).getBag();
                final UIItemMessage uiItemMessage = new UIItemMessage();
                uiItemMessage.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                uiItemMessage.setDestinationUniqueId(bag.getUid());
                uiItemMessage.setSourceUniqueId(bagDropped.getUid());
                uiItemMessage.setId(17711);
                Worker.getInstance().pushMessage(uiItemMessage);
            }
            else if (value instanceof GemItem) {
                final GemItem gem = (GemItem)value;
                final UIItemMessage message5 = new UIItemMessage();
                message5.setDestinationUniqueId(SplitStackDialogActions.getDestinationUniqueId());
                message5.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                message5.setDestinationUniqueId(bag.getUid());
                message5.setItem(gem.getGem());
                message5.setByteValue(gem.getRoomIndex());
                message5.setQuantity((short)1);
                message5.setId(17008);
                Worker.getInstance().pushMessage(message5);
            }
        }
    }
    
    public static void onItemDoubleClick(final ItemEvent event) {
        final Item item = (Item)event.getItemValue();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractBag bag = HeroUtils.getBagFromHeroItem(localPlayer.getOwnerId(), item.getUniqueId());
        if (bag != null) {
            onItemDoubleClick(event, ((Bag)bag).getBagView());
        }
    }
    
    public static void changeItemBackground(final MouseEvent event, final CharacterView characterView, final String position, final Window window) {
        changeItemBackground(event, characterView, null, position, window, null);
    }
    
    public static void changeItemBackground(final MouseEvent event, final Window window, final PopupElement popupElement) {
    }
    
    public static void changeItemBackground(final MouseEvent event, final CharacterView characterView, final Window window, Object item, final PopupElement popupElement) {
        if (item instanceof QuestItemView) {
            item = ((QuestItemView)item).getDefaultItem();
        }
        if (item instanceof Item) {
            changeItemBackground(event, characterView, (Item)item, null, window, popupElement);
        }
    }
    
    public static void changeItemBackground(final MouseEvent event, final PlayerCharacter character, final Window window, Object item, final PopupElement popupElement) {
        if (item instanceof QuestItemView) {
            item = ((QuestItemView)item).getDefaultItem();
        }
        if (item instanceof Item) {
            changeItemBackground(event, HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(character), (Item)item, null, window, popupElement);
        }
    }
    
    public static void changeItemBackground(final MouseEvent event, final CharacterView characterView, Item item, final String position, final Window window, final PopupElement popup) {
        String style = "";
        final Widget w = event.getTarget();
        if (ClientTradeHelper.INSTANCE.getCurrentTrade() != null && PropertiesProvider.getInstance().getObjectProperty("exchange.sourceBag") instanceof Bag) {
            return;
        }
        final LocalPlayerCharacter localPlayer = UIEquipmentFrame.getCharacter();
        final ItemEquipment equipmentInventory = (characterView == null) ? localPlayer.getEquipmentInventory() : characterView.getItemEquipment();
        if (position != null) {
            item = ((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).getFromPosition(Short.parseShort(position));
        }
        if (item != null) {
            boolean in = false;
            if (event.getType() == Events.MOUSE_ENTERED) {
                in = true;
                if (item.getReferenceItem().getSetId() != 0) {
                    style = "itemSetSelectedBackground";
                }
                else {
                    style = "itemSelectedBackground";
                }
            }
            else if (event.getType() == Events.MOUSE_EXITED) {
                style = ItemDisplayerImpl.getBackgroundStyle(item);
            }
            w.setStyle(style);
            if (item.getReferenceItem().getSetId() != 0) {
                final ItemEquipment itemList = equipmentInventory;
                for (final Item itemEquip : itemList) {
                    if (itemEquip.getReferenceItem().getSetId() == item.getReferenceItem().getSetId()) {
                        final short currentPosition = ((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).getPosition(itemEquip);
                        final ElementMap map = window.getElementMap();
                        final RenderableContainer rc = (RenderableContainer)map.getElement("EquipRC" + currentPosition);
                        if (rc == null) {
                            continue;
                        }
                        final Widget currentWidget = (Widget)rc.getInnerElementMap().getElement("Equip" + currentPosition);
                        if (currentWidget == null) {
                            continue;
                        }
                        if (in) {
                            style = "itemSetSelectedBackground";
                        }
                        else {
                            style = ItemDisplayerImpl.getBackgroundStyle(itemEquip, map.getId());
                        }
                        currentWidget.setStyle(style);
                    }
                }
            }
        }
        if (popup != null) {
            if (event.getType() == Events.MOUSE_ENTERED) {
                PopupInfosActions.showPopup(event, item, popup, w);
            }
            else {
                PopupInfosActions.closePopup(event);
            }
        }
    }
    
    public static void onItemDoubleClick(final ItemEvent event, final BagView bagView) {
        if (event.getItemValue() instanceof ItemDisplayerImpl.ItemPlaceHolder) {
            return;
        }
        if (Xulor.getInstance().isLoaded("splitStackDialog")) {
            Xulor.getInstance().unload("splitStackDialog");
        }
        final Item item = (Item)event.getItemValue();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        SplitStackDialogActions.setDestinationPosition((byte)(-1));
        final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
        if (exchanger != null) {
            final AbstractBag bag = HeroUtils.getBagFromHeroItem(localPlayer.getOwnerId(), item.getUniqueId());
            if (bag == null) {
                return;
            }
            final short sourcePosition = bag.getPosition(item.getUniqueId());
            PropertiesProvider.getInstance().setPropertyValue("exchange.sourceBag", bagView.getBag());
            PropertiesProvider.getInstance().setPropertyValue("exchange.sourcePosition", sourcePosition);
            final UIExchangeMoveItemMessage message = new UIExchangeMoveItemMessage();
            message.setId(16810);
            message.setExchangeId(exchanger.getId());
            message.setItem(item);
            message.setItemQuantity(item.getQuantity());
            Worker.getInstance().pushMessage(message);
        }
        else if (Xulor.getInstance().isLoaded("recycleDialog")) {
            if (!item.getType().isRecyclable()) {
                return;
            }
            if (item.getReferenceItem().hasItemProperty(ItemProperty.NOT_RECYCLABLE)) {
                return;
            }
            final UIMessage message2 = new UIMessage();
            message2.setId(19140);
            message2.setLongValue(item.getUniqueId());
            Worker.getInstance().pushMessage(message2);
        }
        else {
            if (Xulor.getInstance().isLoaded("seedSpreaderDialog")) {
                if (ItemDisplayerImpl.isItemUsedInSeedSpreader(item)) {
                    return;
                }
                final UIItemMessage message3 = new UIItemMessage();
                message3.setLongValue(item.getUniqueId());
                message3.setQuantity(item.getQuantity());
                message3.setId(19342);
                Worker.getInstance().pushMessage(message3);
            }
            else if (Xulor.getInstance().isLoaded("havenWorldResourcesCollectorDialog")) {
                if (ItemDisplayerImpl.isItemUsedInHavenWorldResourcesCollector(item)) {
                    return;
                }
                final UIItemMessage message3 = new UIItemMessage();
                message3.setLongValue(item.getUniqueId());
                message3.setQuantity(item.getQuantity());
                message3.setId(19364);
                Worker.getInstance().pushMessage(message3);
                return;
            }
            else if (Xulor.getInstance().isLoaded("storageBoxDialog")) {
                final UIItemMessage message3 = new UIItemMessage();
                message3.setLongValue(item.getUniqueId());
                message3.setShortValue(item.getQuantity());
                message3.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                message3.setId(19326);
                Worker.getInstance().pushMessage(message3);
            }
            else if (Xulor.getInstance().isLoaded("freeCollectMachineDialog")) {
                final UIItemMessage message3 = new UIItemMessage();
                message3.setLongValue(item.getUniqueId());
                message3.setShortValue(item.getQuantity());
                message3.setId(19320);
                Worker.getInstance().pushMessage(message3);
            }
            else if (WakfuGameEntity.getInstance().hasFrame(UIManageFleaFrame.getInstance())) {
                final WakfuAccountInformationHolder customHolder = new WakfuAccountInformationHolder() {
                    @Override
                    public short getInstanceId() {
                        return DimensionalBagFromInstanceManager.INSTANCE.getFromInstanceId();
                    }
                    
                    @Override
                    public WakfuAccountInformationHandler getAccountInformationHandler() {
                        return WakfuGameEntity.getInstance().getLocalPlayer().getAccountInformationHandler();
                    }
                };
                if (!WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(customHolder)) {
                    final String errorMessage = WakfuTranslator.getInstance().getString("error.playerNotSubscribed");
                    final ChatMessage m = new ChatMessage(errorMessage);
                    m.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(m);
                    return;
                }
                final boolean shiftPressed = event.hasShift();
                final boolean defaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
                if (item.getQuantity() > 1 && ((shiftPressed && !defaultSplitMode) || (!shiftPressed && defaultSplitMode))) {
                    SplitStackDialogActions.setMaxQuantity(item.getQuantity());
                    SplitStackDialogActions.setItem(item);
                    SplitStackDialogActions.setMessageType((short)17304);
                    final UIItemMessage uiMsg = new UIItemMessage();
                    uiMsg.setItem(item);
                    uiMsg.setX((short)event.getScreenX());
                    uiMsg.setY((short)event.getScreenY());
                    uiMsg.setId(16821);
                    Worker.getInstance().pushMessage(uiMsg);
                }
                else {
                    final UIMerchantMessage uiMsg2 = new UIMerchantMessage();
                    uiMsg2.setItem(item);
                    uiMsg2.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                    uiMsg2.setId(17304);
                    Worker.getInstance().pushMessage(uiMsg2);
                }
                return;
            }
            else if (item.getReferenceItem().isEquipment() && item.getReferenceItem().getItemAction() == null) {
                addToEquipment(event, bagView.getBag());
                onChangeEquipement(localPlayer);
            }
            else if (item.getReferenceItem().getItemType().getEquipmentPositions().length == 0 || item.getReferenceItem().getItemAction() != null) {
                ShortcutBarDialogActions.useUsableItem(item, true);
            }
            if (item.hasPet()) {
                final boolean mount = item.getPet().getDefinition().hasMount();
                final EquipmentPosition pos = mount ? EquipmentPosition.MOUNT : EquipmentPosition.PET;
                if (WakfuGameEntity.getInstance().getLocalPlayer().getEquipmentInventory().isPositionLocked(pos.m_id)) {
                    return;
                }
                if (EquipmentDialogActions.m_openPetDetailRunner != null) {
                    cancelPetDetailRunner();
                }
            }
            if (!item.getReferenceItem().hasRandomElementEffect() || !item.needRollRandomElementsEffect() || ClientTradeHelper.INSTANCE.isTradeRunning()) {
                return;
            }
            if (item.getQuantity() > 1) {
                ChatHelper.pushErrorMessage("error.rollElements.itemStack", new Object[0]);
                return;
            }
            final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("reroll.itemElementsConfirmation"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        final UIMessage message = new UIMessage();
                        message.setId(19080);
                        message.setLongValue(item.getUniqueId());
                        Worker.getInstance().pushMessage(message);
                        final RenderableContainer currentTarget = event.getCurrentTarget();
                        if (currentTarget != null) {
                            EquipmentDialogActions.addRollElementsParticleToContainer(currentTarget);
                        }
                    }
                }
            });
        }
    }
    
    public static void addRollElementsParticleToContainer(final RenderableContainer rc) {
        if (EquipmentDialogActions.m_particleDecorator == null) {
            EquipmentDialogActions.m_particleDecorator = new ParticleDecorator();
        }
        EquipmentDialogActions.m_particleDecorator.onCheckOut();
        EquipmentDialogActions.m_particleDecorator.setFile("6001057.xps");
        EquipmentDialogActions.m_particleDecorator.setAlignment(Alignment9.NORTH_WEST);
        EquipmentDialogActions.m_particleDecorator.setFollowBorders(true);
        EquipmentDialogActions.m_particleDecorator.setSpeed(500.0f);
        EquipmentDialogActions.m_particleDecorator.setTurnNumber(1);
        final Container cont = (Container)rc.getInnerElementMap().getElement("itemContainer");
        final DecoratorAppearance appearance = cont.getAppearance();
        appearance.add(EquipmentDialogActions.m_particleDecorator);
    }
    
    public static void addToEquipment(final ItemEvent event, final Bag bag) {
        if (ClientTradeHelper.INSTANCE.getCurrentTrade() != null) {
            EquipmentDialogActions.m_logger.error((Object)"Erreur : impossible de modifier l'\u00e9quipement en \u00e9change");
            return;
        }
        final Object value = event.getItemValue();
        if (value != null && value instanceof Item) {
            addToEquipment((Item)value, bag);
        }
    }
    
    public static void addToEquipment(final Item item, final Bag bag) {
        if (item.getType().getId() == 218 || item.getType().getId() == 399) {
            final UIItemMessage message = new UIItemMessage();
            message.setItem(item);
            message.setSourcePosition(SplitStackDialogActions.getSourcePosition());
            message.setDestinationPosition((byte)(-1));
            message.setSourceUniqueId(bag.getUid());
            message.setDestinationCharacter(HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(UIEquipmentFrame.getCharacter()));
            message.setId(17710);
            Worker.getInstance().pushMessage(message);
            return;
        }
        final short breedId = WakfuGameEntity.getInstance().getLocalPlayer().getBreedId();
        final CharacterView localPlayerCharacterSheetView = UICompanionsEmbeddedFrame.getCharacterSheetView(breedId);
        CharacterView destinationCharacter = (CharacterView)PropertiesProvider.getInstance().getObjectProperty("characterSheet", "equipmentDialog");
        if (destinationCharacter == null) {
            destinationCharacter = localPlayerCharacterSheetView;
        }
        final UIItemMessage message2 = new UIItemMessage();
        message2.setItem(item);
        message2.setPosition((short)(-1));
        message2.setSourceUniqueId(bag.getUid());
        message2.setSourceCharacter(localPlayerCharacterSheetView);
        message2.setDestinationCharacter(destinationCharacter);
        message2.setDestinationUniqueId(2L);
        message2.setId(16800);
        Worker.getInstance().pushMessage(message2);
    }
    
    public static void onEquipmentDoubleClick(final ItemEvent event, final CharacterView characterView, final String position) {
        final Item item = (Item)event.getItemValue();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ItemEquipment itemEquipment = characterView.getItemEquipment();
        if (item.hasPet()) {
            final boolean mount = item.getPet().getDefinition().hasMount();
            final EquipmentPosition pos = mount ? EquipmentPosition.MOUNT : EquipmentPosition.PET;
            if (itemEquipment.isPositionLocked(pos.m_id)) {
                return;
            }
            if (EquipmentDialogActions.m_openPetDetailRunner != null) {
                cancelPetDetailRunner();
            }
        }
        final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
        if (exchanger == null) {
            onChangeEquipement(localPlayer);
            unEquip(event, characterView, position);
            return;
        }
        if (characterView.isCompanion()) {
            return;
        }
        PropertiesProvider.getInstance().setPropertyValue("exchange.sourceBag", localPlayer.getEquipmentInventory());
        PropertiesProvider.getInstance().setPropertyValue("exchange.sourcePosition", position);
        final UIExchangeMoveItemMessage message = new UIExchangeMoveItemMessage();
        message.setId(16810);
        message.setExchangeId(exchanger.getId());
        message.setItem(item);
        message.setItemQuantity(item.getQuantity());
        Worker.getInstance().pushMessage(message);
    }
    
    public static void unEquip(final ItemEvent event, final CharacterView sourceCharacterView, final String position) {
        if (ClientTradeHelper.INSTANCE.getCurrentTrade() != null) {
            return;
        }
        final Object value = event.getItemValue();
        if (value instanceof Item) {
            final CharacterView view = HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(HeroUtils.getHeroWithItemInEquipment(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), ((Item)value).getUniqueId()));
            final CharacterView destinationCharacterView = (view != null) ? view : HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(WakfuGameEntity.getInstance().getLocalPlayer());
            final UIItemMessage message = new UIItemMessage();
            message.setItem((Item)value);
            message.setPosition(Short.valueOf(position));
            message.setSourceCharacter(sourceCharacterView);
            message.setDestinationCharacter(destinationCharacterView);
            message.setId(16803);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void closeWindow(final Event event) {
        if (event.getType() == Events.MOUSE_CLICKED) {
            final UIInventoryMessage uiMessage = new UIInventoryMessage();
            uiMessage.setId(16418);
            uiMessage.setStringValue(event.getCurrentTarget().getElementMap().getId());
            Worker.getInstance().pushMessage(uiMessage);
        }
    }
    
    public static void unequipInventory(final ItemEvent itemEvent) {
        final Bag bag = ((BagView)itemEvent.getItemValue()).getBag();
        final UIItemMessage uiItemMessage = new UIItemMessage();
        uiItemMessage.setDestinationPosition((byte)(-1));
        uiItemMessage.setDestinationUniqueId(-1L);
        uiItemMessage.setSourceUniqueId(bag.getUid());
        uiItemMessage.setId(17711);
        Worker.getInstance().pushMessage(uiItemMessage);
    }
    
    public static void minimizeMaximizeDialog(final Event event, final Window window) {
        if (event.getType() == Events.MOUSE_DOUBLE_CLICKED) {
            final Widget container = (Widget)window.getElementMap().getElement("contentWindow");
            final Widget button = (Widget)window.getElementMap().getElement("barCloseButton");
            if (!container.getVisible()) {
                window.setPrefSize(new Dimension(0, 0));
            }
            else {
                window.setPrefSize(new Dimension(0, container.getHeight()));
            }
            container.setVisible(!container.getVisible());
            button.setVisible(!button.getVisible());
        }
    }
    
    public static void showInfos(final Event event, final Window window, final String infos) {
        if (event.getType() == Events.MOUSE_CLICKED) {
            final Widget texte = (Widget)window.getElementMap().getElement(infos + "Info");
            texte.setVisible(!texte.getVisible());
            final Widget bouton = (Widget)window.getElementMap().getElement(infos + "Button");
            if (bouton.getStyle().equals("add")) {
                bouton.setStyle("remove");
            }
            else {
                bouton.setStyle("add");
            }
        }
    }
    
    public static void addToShortcuts(final ItemEvent itemClickEvent) {
        if (itemClickEvent.getItemValue() instanceof ItemDisplayerImpl.ItemPlaceHolder) {
            return;
        }
        if (itemClickEvent.getItemValue() instanceof BagView) {
            return;
        }
        final Item item = (Item)itemClickEvent.getItemValue();
        if (itemClickEvent.getButton() != 1 || !itemClickEvent.hasAlt()) {
            return;
        }
        if (item == null || (item.getReferenceItem().getItemType().getEquipmentPositions().length == 0 && !item.isUsable())) {
            return;
        }
        final UIShortcutMessage message = new UIShortcutMessage();
        message.setItem(item);
        message.setShorcutBarNumber(-1);
        message.setPosition(-1);
        message.setId(16700);
        message.setBooleanValue(true);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void showItemDetailPopup(final ItemEvent itemEvent, final Window window) {
        final Object item = itemEvent.getItemValue();
        PopupInfosActions.showPopup((FieldProvider)item, 500);
    }
    
    public static void showSetDetailPopup(final Event event, final Short setId, final String open, final PopupElement popup) {
        final ItemSet itemSet = ItemSetManager.getInstance().getItemSet(setId);
        if (Boolean.parseBoolean(open)) {
            PopupInfosActions.showPopup(event, itemSet, popup);
        }
        else {
            XulorActions.closePopup(event, popup);
        }
    }
    
    public static void showCharacterDetailPopup(final Event event, final Window window) {
        final PopupElement popup = (PopupElement)window.getElementMap().getElement("characterDetailPopup");
        if (event.getType() == Events.MOUSE_ENTERED && !MasterRootContainer.getInstance().isDragging()) {
            XulorActions.popup(event, popup);
        }
        else if (event.getType() == Events.MOUSE_EXITED) {
            XulorActions.closePopup(event, popup);
        }
    }
    
    public static void changeSetBonusList(final ListSelectionChangedEvent event, final ItemSet set, final TextView tv) {
        if (event.getSelected() && set != null && tv != null) {
            int equipedNumber = 0;
            if (event.getValue() instanceof Integer) {
                equipedNumber = (int)event.getValue();
            }
            tv.setText(set.getItemBonuses(equipedNumber));
        }
    }
    
    public static void selectPet(final Event event) {
        final String id = event.getCurrentTarget().getRenderableParent().getElementMap().getId();
        PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", null, id);
    }
    
    public static void equipmentDragItemPreview(final Event event, final String position) {
        if (event instanceof DragEvent) {
            final DragEvent dragEvent = (DragEvent)event;
            if (dragEvent.getValue() != null && dragEvent.getValue() instanceof Item) {
                EquipmentDialogActions.m_itemId = ((Item)dragEvent.getValue()).getUniqueId();
                SplitStackDialogActions.setSourceUniqueId(2L);
                SplitStackDialogActions.setSourcePosition(Byte.valueOf(position));
                final Item item = (Item)dragEvent.getValue();
                onDragItem(item);
            }
        }
    }
    
    public static void onMouseOverBag(final Event event, final PopupElement popupElement) {
        XulorActions.popup(event, popupElement);
        final Widget w = event.getTarget();
        w.setStyle("itemSelectedBackground");
    }
    
    public static void onMouseOutBag(final Event event, final PopupElement popupElement) {
        XulorActions.closePopup(event, popupElement);
        final Widget w = event.getTarget();
        w.setStyle("itemBackground");
    }
    
    public static void deleteItem(final Event event) {
        final String mapId = event.getCurrentTarget().getElementMap().getId();
        final Item item = (Item)PropertiesProvider.getInstance().getObjectProperty("itemDetail", mapId);
        final CharacterView view = HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(UIEquipmentFrame.getCharacter());
        final UIItemMessage message = new UIItemMessage();
        message.setItem(item);
        message.setDestinationCharacter(view);
        message.setId(16827);
        Worker.getInstance().pushMessage(message);
        final Button b = event.getTarget();
        final ButtonAppearance appearance = b.getAppearance();
        appearance.exit();
    }
    
    public static void regenWith(final Event event) {
        final PlayerCharacter player = UIEquipmentFrame.getCharacter();
        if (!WakfuGameEntity.getInstance().getLocalPlayer().hasSubscriptionRight(SubscriptionRight.FULL_REGEN_WITH_ITEM)) {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("error.playerNotSubscriptionRight"), 3);
            return;
        }
        final String mapId = event.getCurrentTarget().getElementMap().getId();
        final Item item = (Item)PropertiesProvider.getInstance().getObjectProperty("itemDetail", mapId);
        final UIItemMessage message = new UIItemMessage();
        message.setItem(item);
        message.setId(16828);
        message.setDestinationCharacter(HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(player));
        Worker.getInstance().pushMessage(message);
    }
    
    public static void checkFilter(final Event event, final InventoryDisplayModeView inventoryDisplayModeView) {
        checkFilter(event, inventoryDisplayModeView, WakfuGameEntity.getInstance().getLocalPlayer());
    }
    
    public static void checkFilter(final Event event, final InventoryDisplayModeView inventoryDisplayModeView, final LocalPlayerCharacter concerned) {
        concerned.getBags().setCurrentDisplayMode(inventoryDisplayModeView.getInventoryDisplayMode());
    }
    
    public static void onMouseOverFilter(final ItemEvent event) {
        if (((InventoryDisplayModeView)event.getItemValue()).getInventoryDisplayMode().equals(WakfuGameEntity.getInstance().getLocalPlayer().getBags().getCurrentDisplayMode())) {
            return;
        }
        EquipmentDialogActions.m_image = (Image)event.getCurrentTarget().getInnerElementMap().getElement("image");
        ((StaticLayoutData)EquipmentDialogActions.m_image.getLayoutData()).setYOffset(1);
        EquipmentDialogActions.m_image.getContainer().invalidateMinSize();
    }
    
    public static void onMouseOutFilter(final ItemEvent event) {
        if (EquipmentDialogActions.m_image == null) {
            return;
        }
        ((StaticLayoutData)EquipmentDialogActions.m_image.getLayoutData()).setYOffset(-2);
        EquipmentDialogActions.m_image.getContainer().invalidateMinSize();
        EquipmentDialogActions.m_image = null;
    }
    
    public static void useItem(final Event event) {
        final String mapId = event.getCurrentTarget().getElementMap().getId();
        final Item item = (Item)PropertiesProvider.getInstance().getObjectProperty("itemDetail", mapId);
        if (ShortcutBarDialogActions.useUsableItem(item, true)) {
            PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", null, mapId);
            final Button button = event.getTarget();
            button.getAppearance().exit();
        }
    }
    
    public static void onChangeEquipement(final PlayerCharacter player) {
    }
    
    public static void sortInventories(final Event e) {
        ItemHelper.requestRepack();
    }
    
    public static void dropView(final DropOutEvent e) {
        final ShortCharacterView shortCharacterView = (ShortCharacterView)e.getSourceValue();
        if (shortCharacterView.isPlayer()) {
            return;
        }
        final int screenX = e.getScreenX();
        final int screenY = e.getScreenY();
        UIEquipmentFrame.getInstance().loadSecondaryDialog(UICompanionsEmbeddedFrame.getCharacterSheetView(shortCharacterView.getBreedId()), "equipInventWindow", screenX, screenY);
    }
    
    public static void dropHeroView(final DropOutEvent e, final Window w) {
        if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.HEROES_ENABLED)) {
            return;
        }
        final LocalPlayerCharacter lpc = (LocalPlayerCharacter)e.getSourceValue();
        final int screenX = e.getScreenX();
        final int screenY = e.getScreenY();
        final CharacterView view = HeroesCharacterViewManager.INSTANCE.getOrCreateCharacterView(lpc);
        UIEquipmentFrame.getInstance().loadHeroSecondaryDialog(view, "inventoryOnlyDialog", screenX, screenY, w);
    }
    
    public static void vault(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UIVaultFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIVaultFrame.getInstance());
        }
        else if (!WakfuGameEntity.getInstance().getLocalPlayer().hasProperty(WorldPropertyType.CANT_OPEN_VAULT)) {
            WakfuGameEntity.getInstance().pushFrame(UIVaultFrame.getInstance());
        }
        else {
            ChatWorldPropertyTypeErrorManager.writeChatErrorMessage(WorldPropertyType.CANT_OPEN_VAULT.getId());
        }
    }
    
    public static void openCloseCosmeticsDialog(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UICosmeticsFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UICosmeticsFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UICosmeticsFrame.getInstance());
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)EquipmentDialogActions.class);
        m_dragNDropListeners = new ArrayList<ItemDragNDropListener>();
    }
}
