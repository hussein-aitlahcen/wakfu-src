package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.craft.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class MergeGemDialogActions
{
    public static final String PACKAGE = "wakfu.mergeGem";
    
    public static void openItemDetailWindow(final ItemEvent e, final Window w) {
        openItemDetailWindow(e.getItemValue());
    }
    
    public static void openItemDetailWindow(final Object item) {
        PropertiesProvider.getInstance().setLocalPropertyValue("itemDetail", item, UIMergeGemFrame.getInstance().getDialogId());
    }
    
    public static void inventoryDragItem(final Event event, final InventoryContent item, final String gemmed) {
        final AbstractUIMessage msg = new UIMessage();
        msg.setId(16846);
        msg.setLongValue(item.getUniqueId());
        msg.setBooleanValue(Boolean.valueOf(gemmed));
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void inventoryDropOutItem(final Event event, final String gemmed) {
        UIMergeGemFrame.getInstance().highLightIngredientSlots(false);
    }
    
    public static void dropIngredient(final DropEvent dropEvent, final String gemmed) {
        final Object value = dropEvent.getValue();
        if (value instanceof Item) {
            removeIngredient((Item)value, gemmed);
        }
    }
    
    public static void dropItem(final DropEvent dropEvent, final String gemmed) {
        final Object value = dropEvent.getValue();
        final Item item = (Item)value;
        openItemDetailWindow(item);
        final UIItemMessage message = new UIItemMessage();
        message.setItem(item);
        message.setQuantity((short)(-1));
        message.setBooleanValue(Boolean.valueOf(gemmed));
        message.setId(16844);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void itemDropOut(final DropOutEvent dropOutEvent, final String gemmed) {
        removeIngredient((Item)dropOutEvent.getValue(), gemmed);
    }
    
    private static void removeIngredient(final Item item, final String gemmed) {
        final UIItemMessage msg = new UIItemMessage();
        msg.setItem(item);
        msg.setQuantity((short)1);
        msg.setBooleanValue(Boolean.valueOf(gemmed));
        msg.setId(16845);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void removeIngredient(final ItemEvent itemEvent, final String gemmed) {
        removeIngredient((Item)itemEvent.getItemValue(), gemmed);
    }
    
    public static void addIngredient(final ItemEvent itemEvent, final String gemmed) {
        final UIItemMessage message = new UIItemMessage();
        message.setItem((Item)itemEvent.getItemValue());
        message.setQuantity((short)1);
        message.setBooleanValue(Boolean.valueOf(gemmed));
        message.setId(16844);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void startCraft(final Event event, final ProgressBar bar, final String mergeType) {
        final byte gemMergeType = Byte.parseByte(mergeType);
        if (!PropertiesProvider.getInstance().getBooleanProperty("mergeGem.running")) {
            final UIStartCraftMessage msg = new UIStartCraftMessage(bar, event.getCurrentTarget());
            msg.setByteValue(gemMergeType);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static boolean setCurrentCraftNumber(final Event event, final TextEditor textEditor, final String mergeType) {
        final byte gemMergeType = Byte.parseByte(mergeType);
        final String s = textEditor.getText();
        if (s.length() == 0) {
            return true;
        }
        final int number = Integer.parseInt(s);
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setIntValue(number);
        uiMessage.setByteValue(gemMergeType);
        uiMessage.setId(16848);
        Worker.getInstance().pushMessage(uiMessage);
        return true;
    }
    
    public static boolean setCurrentCraftMaxNumber(final Event event, final String mergeType) {
        final byte gemMergeType = Byte.parseByte(mergeType);
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setId(16849);
        uiMessage.setByteValue(gemMergeType);
        Worker.getInstance().pushMessage(uiMessage);
        return true;
    }
}
