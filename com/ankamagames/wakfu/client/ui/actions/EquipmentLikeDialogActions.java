package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.item.xp.*;
import com.ankamagames.wakfu.common.game.item.xp.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

@XulorActionsTag
public class EquipmentLikeDialogActions extends CompanionsEmbeddedActions
{
    protected static Runnable m_openPetDetailRunner;
    private static FieldProvider m_itemSource;
    public static final int DIRECTION_INDEX_MIN = 0;
    public static final int DIRECTION_INDEX_MAX = 7;
    
    public static void openItemDetailWindow(final ItemEvent itemClickEvent, final Window window) {
        if (itemClickEvent.getItemValue() instanceof ItemDisplayerImpl.ItemPlaceHolder) {
            return;
        }
        Item item;
        if (itemClickEvent.getItemValue() instanceof BagView) {
            final Bag bag = ((BagView)itemClickEvent.getItemValue()).getBag();
            final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(bag.getReferenceId());
            item = new Item(bag.getUid());
            item.initializeWithReferenceItem(refItem);
        }
        else if (itemClickEvent.getItemValue() instanceof QuestItemView) {
            item = ((QuestItemView)itemClickEvent.getItemValue()).getDefaultItem();
        }
        else {
            item = (Item)itemClickEvent.getItemValue();
        }
        if (item == null) {
            return;
        }
        final ElementMap elementMap = window.getElementMap();
        if (elementMap == null) {
            return;
        }
        if (itemClickEvent.getButton() == 3) {
            if (item.hasPet()) {
                final PetDetailDialogView petDetailDialogView = new PetDetailDialogView(item);
                if (EquipmentLikeDialogActions.m_openPetDetailRunner == null) {
                    addPetDetailOpenRunner(petDetailDialogView, window);
                }
            }
            else {
                showItemPopupMenu(item, window);
            }
            final List list = (List)elementMap.getElement("itemList");
            if (list != null) {
                list.setSelectedValue(null);
            }
        }
        else if (itemClickEvent.getButton() == 1) {
            final FieldProvider itemSource = EquipmentLikeDialogActions.m_itemSource;
            EquipmentLikeDialogActions.m_itemSource = (FieldProvider)itemClickEvent.getItemValue();
            final PetDetailDialogView pDDV = (PetDetailDialogView)PropertiesProvider.getInstance().getObjectProperty("pet");
            PetDetailDialogView petDetailDialogView2 = null;
            ItemXpControllerLevelModifier controller = null;
            if (item.hasXp()) {
                item = item.getClone();
                controller = new ItemXpControllerLevelModifier(item);
            }
            else if (item.hasPet()) {
                petDetailDialogView2 = new PetDetailDialogView(item);
                if (EquipmentLikeDialogActions.m_openPetDetailRunner == null) {
                    addPetDetailOpenRunner(petDetailDialogView2, window);
                }
            }
            PropertiesProvider.getInstance().setLocalPropertyValue("itemLevelController", controller, elementMap);
            PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", item, elementMap);
            PropertiesProvider.getInstance().setPropertyValue("pet", petDetailDialogView2);
            if (itemSource != null) {
                PropertiesProvider.getInstance().firePropertyValueChanged(itemSource, itemSource.getFields());
            }
            if (petDetailDialogView2 != null) {
                UIPetFrame.getInstance().scheduleDateUpdateProcess();
            }
            else {
                UIPetFrame.getInstance().tryToUnScheduleDateUpdateProcess();
            }
            if (pDDV != null && UIPetFrame.getInstance().noPetReferencesDisplayed()) {
                pDDV.clean();
            }
            final List list2 = (List)elementMap.getElement("itemList");
            if (list2 != null) {
                list2.setSelectedValue(item);
            }
        }
    }
    
    public static void changeDirection(final MouseEvent mouseEvent, final AnimatedElementViewer objViewer, final String jump) {
        changeDirection(mouseEvent, objViewer, Integer.valueOf(jump));
    }
    
    private static void changeDirection(final MouseEvent mouseEvent, final AnimatedElementViewer objViewer, final int jump) {
        final int button = mouseEvent.getButton();
        if (button != 1 && button != 3) {
            return;
        }
        final int delta = (button == 1) ? (-jump) : jump;
        int direction = (objViewer.getDirection() + delta) % 8;
        if (direction < 0) {
            direction = 7;
        }
        objViewer.setDirection(direction);
        objViewer.setDirection(direction);
    }
    
    public static void changeDirection(final MouseEvent mouseEvent, final AnimatedElementViewer objViewer) {
        changeDirection(mouseEvent, objViewer, 1);
    }
    
    private static void addPetDetailOpenRunner(final PetDetailDialogView petDetailDialogView, final Window window) {
        EquipmentLikeDialogActions.m_openPetDetailRunner = new Runnable() {
            @Override
            public void run() {
                EquipmentLikeDialogActions.customizePet(petDetailDialogView, window);
                EquipmentLikeDialogActions.m_openPetDetailRunner = null;
            }
        };
        ProcessScheduler.getInstance().schedule(EquipmentLikeDialogActions.m_openPetDetailRunner, 300L, 1);
    }
    
    public static void cancelPetDetailRunner() {
        ProcessScheduler.getInstance().remove(EquipmentLikeDialogActions.m_openPetDetailRunner);
        EquipmentLikeDialogActions.m_openPetDetailRunner = null;
    }
    
    public static void customizePet(final MouseEvent event) {
        final PetDetailDialogView petDetailDialogView = (PetDetailDialogView)PropertiesProvider.getInstance().getObjectProperty("pet");
        customizePet(petDetailDialogView, null);
    }
    
    public static void customizePet(final PetDetailDialogView petDetailDialogView, final Window window) {
        final UIPetDetailMessage uiPetMessage = new UIPetDetailMessage((T)petDetailDialogView);
        uiPetMessage.setId(19152);
        uiPetMessage.setParentWindowId((window == null) ? "equipmentDialog" : window.getElementMap().getId());
        Worker.getInstance().pushMessage(uiPetMessage);
    }
    
    public static void showItemPopupMenu(final Item item, final Window window) {
        final UIItemDetailMessage msg = new UIItemDetailMessage();
        msg.setId(16415);
        msg.setParentWindowId((window != null) ? window.getElementMap().getId() : null);
        msg.setItem(item);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void changePetItemBackground(final MouseEvent event, final PopupElement popupElement) {
        String style = "";
        final Widget w = (Widget)event.getTarget().getElementMap().getElement("petContainer");
        if (event.getType() == Events.MOUSE_ENTERED) {
            style = "itemSelectedBackground";
        }
        else if (event.getType() == Events.MOUSE_EXITED) {
            style = ((PropertiesProvider.getInstance().getObjectProperty("pet") != null) ? "itemSelectedBackground" : "itemBackground");
        }
        w.setStyle(style);
        if (popupElement != null) {
            if (event.getType() == Events.MOUSE_ENTERED) {
                XulorActions.popup(event, popupElement, w);
            }
            else {
                XulorActions.closePopup(event);
            }
        }
    }
    
    public static void changeItemBackground(final MouseEvent event, final CharacterView characterView, Item item, final String position, final Window window, final PopupElement popup, final boolean displaySet) {
        String style = "";
        final Widget w = event.getTarget();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ItemEquipment equipmentInventory = (characterView == null) ? localPlayer.getEquipmentInventory() : characterView.getItemEquipment();
        if (ClientTradeHelper.INSTANCE.getCurrentTrade() != null && PropertiesProvider.getInstance().getObjectProperty("exchange.sourceBag") instanceof Bag) {
            return;
        }
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
            if (displaySet && item.getReferenceItem().getSetId() != 0) {
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
}
