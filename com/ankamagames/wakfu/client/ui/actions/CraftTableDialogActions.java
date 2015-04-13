package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.craft.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.framework.reflect.*;

@XulorActionsTag
public class CraftTableDialogActions
{
    public static final String PACKAGE = "wakfu.craftTable";
    
    public static void openItemDetailWindow(final ItemEvent e, final Window window) {
        EquipmentLikeDialogActions.openItemDetailWindow(e, window);
    }
    
    public static void inventoryDragItem(final Event event, final Item item) {
        final UIDragItemToCraftMessage uiDragItemToCraftMessage = new UIDragItemToCraftMessage(item.getReferenceId());
        Worker.getInstance().pushMessage(uiDragItemToCraftMessage);
    }
    
    public static void inventoryDropOutItem(final Event event) {
        UICraftTableFrame.getInstance().highLightIngredientSlots(false);
    }
    
    public static void dropIngredient(final DropEvent dropEvent) {
        final Object value = dropEvent.getValue();
        if (value instanceof IngredientView) {
            removeIngredient((IngredientView)value);
        }
    }
    
    public static void dropItem(final DropEvent dropEvent) {
        final Object value = dropEvent.getValue();
        if (value instanceof IngredientView) {
            removeIngredient((IngredientView)value);
        }
        final boolean shiftPressed = dropEvent.hasShift();
        final boolean isDefaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
        final Item item = (Item)value;
        if (item.getQuantity() > 1 && ((shiftPressed && !isDefaultSplitMode) || (!shiftPressed && isDefaultSplitMode))) {
            SplitStackDialogActions.setMaxQuantity(item.getQuantity());
            SplitStackDialogActions.setMessageType((short)16844);
            SplitStackDialogActions.setItem(item);
            final UIItemMessage message = new UIItemMessage();
            message.setItem(item);
            message.setX((short)dropEvent.getScreenX());
            message.setY((short)dropEvent.getScreenY());
            message.setId(16821);
            Worker.getInstance().pushMessage(message);
        }
        else {
            final UIItemMessage message = new UIItemMessage();
            message.setItem(item);
            message.setQuantity((short)(-1));
            message.setId(16844);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void itemDropOut(final DropOutEvent dropOutEvent) {
        removeIngredient((IngredientView)dropOutEvent.getValue());
    }
    
    private static void removeIngredient(final IngredientView ingredientView) {
        removeIngredient(ingredientView, (short)(-1));
    }
    
    private static void removeIngredient(final IngredientView ingredientView, final short quantity) {
        final UIDropOutIngredientFromCraftMessage uiDropItemToCraftMessage = new UIDropOutIngredientFromCraftMessage(ingredientView);
        uiDropItemToCraftMessage.setShortValue(quantity);
        Worker.getInstance().pushMessage(uiDropItemToCraftMessage);
    }
    
    public static void removeIngredient(final ItemEvent itemEvent) {
        removeIngredient((IngredientView)itemEvent.getItemValue(), (short)1);
    }
    
    public static void addIngredient(final ItemEvent itemEvent) {
        final UIItemMessage message = new UIItemMessage();
        message.setItem((Item)itemEvent.getItemValue());
        message.setQuantity((short)1);
        message.setId(16844);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void selectRecipe(final MouseEvent itemEvent, final Window window, final RecipeView recipe) {
        if (!"recipeBackground".equals(itemEvent.getTarget().getId())) {
            return;
        }
        if (itemEvent.getButton() == 3) {
            CraftDialogsActions.openProducedItemDescription(itemEvent, recipe, window);
            return;
        }
        final UISelectRecipeMessage uiSelectRecipeMessage = new UISelectRecipeMessage(recipe);
        Worker.getInstance().pushMessage(uiSelectRecipeMessage);
    }
    
    public static void startCraft(final MouseEvent event, final ProgressBar bar) {
        final boolean craftRunning = PropertiesProvider.getInstance().getBooleanProperty("craftRunning");
        if (event.getButton() != 1) {
            final ToggleButton button = event.getTarget();
            button.setSelected(craftRunning);
            return;
        }
        if (craftRunning) {
            UIMessage.send((short)16850);
        }
        else {
            Worker.getInstance().pushMessage(new UIStartCraftMessage(bar, event.getCurrentTarget()));
        }
    }
    
    public static boolean setCurrentCraftNumber(final Event event, final TextEditor textEditor) {
        final String s = textEditor.getText();
        if (s.length() == 0) {
            return true;
        }
        final int number = Integer.parseInt(s);
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setIntValue(number);
        uiMessage.setId(16848);
        Worker.getInstance().pushMessage(uiMessage);
        return true;
    }
    
    public static boolean setCurrentCraftMaxNumber(final Event event) {
        UIMessage.send((short)16849);
        return true;
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
                if (((ReferenceItem)item.getReferenceItem()).isItemUsedInCraft()) {
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
