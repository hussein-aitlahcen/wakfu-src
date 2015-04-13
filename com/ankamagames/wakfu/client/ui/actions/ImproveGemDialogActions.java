package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class ImproveGemDialogActions
{
    public static final String PACKAGE = "wakfu.improveGem";
    
    public static void openItemDetailWindow(final ItemEvent e, final Window w) {
        if (e.getItemValue() instanceof Item) {
            openItemDetailWindow((Item)e.getItemValue());
        }
        else if (e.getItemValue() instanceof ReferenceItem) {
            PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", e.getItemValue(), "improveGemDialog");
        }
    }
    
    public static void openItemDetailWindow(final Item item) {
        PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", item, "improveGemDialog");
    }
    
    public static void inventoryDragItem(final Event event, final InventoryContent item, final String index) {
        final AbstractUIMessage msg = new UIMessage();
        msg.setId(16846);
        msg.setLongValue(item.getUniqueId());
        msg.setByteValue(Byte.valueOf(index));
        Worker.getInstance().pushMessage(msg);
    }
    
    public static boolean validateDrop(final DragNDropContainer source, final Object sourceValue, final DragNDropContainer destination, final Object destValue, final Object value) {
        return false;
    }
    
    public static void inventoryDropOutItem(final Event event) {
    }
    
    public static void dropItem(final DropEvent dropEvent, final String index) {
        final Object value = dropEvent.getValue();
        final Item item = (Item)value;
        final UIItemMessage message = new UIItemMessage();
        message.setItem(item);
        message.setQuantity((short)(-1));
        message.setByteValue(Byte.valueOf(index));
        message.setId(16844);
        Worker.getInstance().pushMessage(message);
        EquipmentDialogActions.onDropItem();
    }
    
    public static void itemDropOut(final DropOutEvent dropOutEvent, final String gemmed) {
        removeIngredient((Item)dropOutEvent.getValue(), gemmed);
    }
    
    private static void removeIngredient(final Item item, final String index) {
        final UIItemMessage msg = new UIItemMessage();
        msg.setItem(item);
        msg.setQuantity((short)1);
        msg.setByteValue(Byte.valueOf(index));
        msg.setId(16845);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void removeIngredient(final ItemEvent itemEvent, final String gemmed) {
        removeIngredient((Item)itemEvent.getItemValue(), gemmed);
    }
    
    public static void addIngredient(final ItemEvent itemEvent, final String index) {
        final UIItemMessage message = new UIItemMessage();
        message.setItem((Item)itemEvent.getItemValue());
        message.setQuantity((short)1);
        message.setByteValue(Byte.valueOf(index));
        message.setId(16844);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void startCraft(final Event event) {
        if (!PropertiesProvider.getInstance().getBooleanProperty("craftRunning")) {
            final UIMessage message = new UIMessage();
            message.setId(16840);
            Worker.getInstance().pushMessage(message);
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
    
    public static void buyHammer(final Event e) {
        UIImproveGemFrame.getInstance().buyHammerArticle();
    }
}
