package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.message.craft.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.reflect.*;

@XulorActionsTag
public class RecycleDialogActions
{
    public static final String PACKAGE = "wakfu.recycle";
    
    public static void openItemDetailWindow(final ItemEvent e, final Window window) {
        EquipmentLikeDialogActions.openItemDetailWindow(e, window);
    }
    
    public static void inventoryDragItem(final Event event, final InventoryContent item) {
        final AbstractUIMessage msg = new UIMessage();
        msg.setId(16846);
        msg.setLongValue(item.getUniqueId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void inventoryDropOutItem(final Event event) {
        UIRecycleFrame.getInstance().highLightIngredientSlots(false);
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
        final AbstractUIMessage msg = new UIDropOutIngredientFromCraftMessage(ingredientView);
        msg.setShortValue(quantity);
        Worker.getInstance().pushMessage(msg);
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
    
    public static void startCraft(final Event event, final ProgressBar bar) {
        if (!PropertiesProvider.getInstance().getBooleanProperty("craftRunning")) {
            Worker.getInstance().pushMessage(new UIStartCraftMessage(bar, event.getCurrentTarget()));
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
