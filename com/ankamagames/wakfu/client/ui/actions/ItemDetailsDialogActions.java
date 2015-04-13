package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.mount.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.common.game.item.xp.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.core.game.item.xp.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.reflect.*;

@XulorActionsTag
public class ItemDetailsDialogActions
{
    public static final String PACKAGE = "wakfu.itemDetails";
    
    public static void mount(final Event event, final Object o) {
        if (o instanceof Item) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final AbstractOccupation currentOccupation = localPlayer.getCurrentOccupation();
            if (currentOccupation != null && currentOccupation.getOccupationTypeId() == 14) {
                localPlayer.finishCurrentOccupation();
            }
            else {
                final StartRidingRequestMessage msg = new StartRidingRequestMessage();
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
            }
        }
    }
    
    public static void mergeSet(final Event event, final Object o) {
        if (o instanceof Item) {
            final ItemXpHolder item = (ItemXpHolder)o;
            final AbstractUIMessage msg = new UIMessage();
            msg.setId(16715);
            msg.setLongValue(item.getUniqueId());
            Worker.getInstance().pushMessage(msg);
            final Button button = event.getTarget();
            final ButtonAppearance appearance = button.getAppearance();
            appearance.exit();
        }
    }
    
    public static void inventorySort(final Event event, final Object o) {
        if (o instanceof BagView) {
            final BagView bagView = (BagView)o;
            final Bag bag = bagView.getBag();
            if (bag == null) {
                return;
            }
            ItemHelper.requestRepack(bag.getPosition());
        }
    }
    
    public static void openSetDetailWindow(final Event event, final EventDispatcher window, final Object o) {
        if (event.getType() == Events.MOUSE_CLICKED) {
            ItemSet itemSet;
            if (o instanceof Item) {
                itemSet = ItemSetManager.getInstance().getItemSet(((Item)o).getReferenceItem().getSetId());
            }
            else if (o instanceof ReferenceItem) {
                itemSet = ItemSetManager.getInstance().getItemSet(((ReferenceItem)o).getSetId());
            }
            else {
                if (!(o instanceof RecipeView)) {
                    return;
                }
                itemSet = ItemSetManager.getInstance().getItemSet(((RecipeView)o).getEffectiveItem().getSetId());
            }
            final AbstractUIDetailMessage msg = new UIItemSetDetailMessage();
            msg.setParentWindowId(window.getElementMap().getId());
            msg.setItem(itemSet);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void openItemDetailWindow(final ItemEvent itemClickEvent) {
        final Object itemValue = itemClickEvent.getItemValue();
        if (!(itemValue instanceof Item)) {
            return;
        }
        final Item item = (Item)itemValue;
        if (item == null) {
            return;
        }
        if (itemClickEvent.getButton() == 3) {
            final UIItemDetailMessage msg = new UIItemDetailMessage();
            msg.setId(16415);
            msg.setParentWindowId(itemClickEvent.getCurrentTarget().getElementMap().getId());
            msg.setItem(item);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void openStuffPreviewWindow(final Event event, final Object o) {
        Item item;
        if (o instanceof PetMarketDetailDialogView) {
            item = ((PetMarketDetailDialogView)o).getPetItem().getItem();
        }
        else {
            if (o instanceof RecipeView) {
                ((ReferenceItem)((RecipeView)o).getEffectiveItem()).previewTradeEntry();
                return;
            }
            item = (Item)o;
        }
        UIStuffPreviewFrame.INSTANCE.equipItem(item);
    }
    
    public static void processText(final Event e, final Widget target, final PopupElement popup) {
        SpellDetailsActions.processText(e, target, popup);
    }
    
    public static void openLinkedCraft(final Event event, final Object o) {
        if (event.getType() == Events.MOUSE_CLICKED) {
            ReferenceItem item;
            if (o instanceof Item) {
                item = (ReferenceItem)((Item)o).getReferenceItem();
            }
            else if (o instanceof ReferenceItem) {
                item = (ReferenceItem)o;
            }
            else {
                if (!(o instanceof RecipeView)) {
                    return;
                }
                item = (ReferenceItem)((RecipeView)o).getEffectiveItem();
            }
            final AbstractUIMessage msg = new UIMessage();
            msg.setId(16832);
            msg.setIntValue(item.getId());
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void closeWindow(final Event event, final EventDispatcher window) {
        if (event.getType() == Events.MOUSE_CLICKED) {
            final AbstractUIDetailMessage msg = new UIItemDetailMessage();
            msg.setId(16415);
            msg.setParentWindowId(window.getElementMap().getId());
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void setItemLevel(final SliderMovedEvent event) {
        final ElementMap map = event.getTarget().getElementMap();
        if (map == null) {
            return;
        }
        final Object propertyItem = PropertiesProvider.getInstance().getObjectProperty("itemDetail", map.getId());
        if (propertyItem == null) {
            return;
        }
        if (propertyItem instanceof ReferenceItem) {
            return;
        }
        if (propertyItem instanceof PetMarketDetailDialogView) {
            return;
        }
        final ItemXpHolder item = (Item)propertyItem;
        if (!item.hasXp()) {
            return;
        }
        final ItemXp xp = item.getXp();
        short levelValue = (short)event.getValue();
        if (levelValue > xp.getXpTable().getMaxLevel()) {
            levelValue = xp.getXpTable().getMaxLevel();
        }
        setItemLevel(levelValue, map);
    }
    
    public static void keyType(final Event event, final TextWidget te) {
        final ElementMap map = event.getTarget().getElementMap();
        if (map == null) {
            return;
        }
        final ItemXpHolder item = (Item)PropertiesProvider.getInstance().getObjectProperty("itemDetail", map.getId());
        if (item == null) {
            return;
        }
        if (!item.hasXp()) {
            return;
        }
        final ItemXp xp = item.getXp();
        if (te.getText().length() == 0) {
            return;
        }
        short levelValue = PrimitiveConverter.getShort(te.getText());
        if (levelValue > xp.getXpTable().getMaxLevel()) {
            levelValue = xp.getXpTable().getMaxLevel();
        }
        setItemLevel(levelValue, map);
    }
    
    public static void restore(final Event event) {
        final ElementMap map = event.getTarget().getElementMap();
        if (map == null) {
            return;
        }
        final ItemXpControllerLevelModifier controller = (ItemXpControllerLevelModifier)PropertiesProvider.getInstance().getObjectProperty("itemLevelController", map.getId());
        controller.restore();
    }
    
    private static void setItemLevel(final short level, final ElementMap map) {
        final ItemXpControllerLevelModifier controller = (ItemXpControllerLevelModifier)PropertiesProvider.getInstance().getObjectProperty("itemLevelController", map.getId());
        controller.setLevel(level);
    }
    
    public static void openGemsDialog(final ItemEvent e, final Object item) {
        if (!WakfuGameEntity.getInstance().hasFrame(UIGemItemsFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIGemItemsFrame.getInstance());
        }
        UIGemItemsFrame.getInstance().setCurrentItem((Item)item);
    }
    
    public static void drop(final DropEvent e, final GemSlotDisplayer displayer) {
        final Item gem = (Item)e.getSourceValue();
        final UISocketGemMessage msg = new UISocketGemMessage();
        msg.setGem(gem);
        msg.setItem(displayer.getHolder());
        msg.setByteValue(displayer.getIndex());
        Worker.getInstance().pushMessage(msg);
        EquipmentDialogActions.onDropItem();
    }
    
    public static void improveGem(final ItemEvent e) {
        final GemSlotDisplayer displayer = (GemSlotDisplayer)e.getItemValue();
        if (displayer.getGemItem() != null) {
            UIImproveGemFrame.getInstance().openUI(displayer.getHolder(), displayer.getIndex());
        }
    }
    
    public static void changeItemBackground(final MouseEvent event, final Item item) {
        changeItemBackground(event, item, null);
    }
    
    public static void changeItemBackground(final MouseEvent event) {
        String style = "";
        final Widget w = event.getTarget();
        if (event.getType() == Events.MOUSE_ENTERED) {
            style = "itemSelectedBackground";
        }
        else if (event.getType() == Events.MOUSE_EXITED) {
            style = "itemBackground";
        }
        w.setStyle(style);
    }
    
    public static void changeItemBackground(final MouseEvent event, final Item item, final PopupElement popup) {
        String style = "";
        final Widget w = event.getTarget();
        if (item != null) {
            if (event.getType() == Events.MOUSE_ENTERED) {
                style = "itemSelectedBackground";
            }
            else if (event.getType() == Events.MOUSE_EXITED) {
                if (UIRecycleFrame.getInstance().containsItem(item.getUniqueId())) {
                    style = "itemSetSelectedBackground";
                }
                else {
                    style = "itemBackground";
                }
            }
            w.setStyle(style);
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
}
