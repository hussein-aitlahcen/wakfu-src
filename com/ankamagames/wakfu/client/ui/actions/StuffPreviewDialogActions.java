package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.xulor2.core.event.*;

@XulorActionsTag
public class StuffPreviewDialogActions extends EquipmentLikeDialogActions
{
    public static final String PACKAGE = "wakfu.stuffPreview";
    private static long m_draggedItemId;
    
    public static void equipItem(final Item item, final String position) {
        final UIItemMessage uiMessage = new UIItemMessage();
        uiMessage.setItem(item);
        uiMessage.setByteValue(Byte.valueOf(position));
        uiMessage.setId(19368);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void unequipItem(final byte position) {
        final UIItemMessage uiMessage = new UIItemMessage();
        uiMessage.setByteValue(position);
        uiMessage.setId(19369);
        Worker.getInstance().pushMessage(uiMessage);
        setDraggedItemId(-1L);
    }
    
    public static void unequip(final ItemEvent e, final String position) {
        unequipItem(Byte.parseByte(position));
    }
    
    public static void itemDropOut(final DropOutEvent event, final String position) {
        unequipItem(Byte.parseByte(position));
    }
    
    public static void equipmentDropItem(final DropEvent e, final String position) {
        if (!(e.getValue() instanceof Item)) {
            return;
        }
        final Item item = (Item)e.getValue();
        EquipmentDialogActions.onDropItem();
        equipItem(item, position);
    }
    
    public static void dragItem(final DragEvent dragEvent) {
        final Item item = (Item)dragEvent.getValue();
        setDraggedItemId(item.getReferenceId());
    }
    
    public static long getDraggedItemId() {
        return StuffPreviewDialogActions.m_draggedItemId;
    }
    
    public static void setDraggedItemId(final long draggedItemId) {
        StuffPreviewDialogActions.m_draggedItemId = draggedItemId;
    }
    
    public static void changeItemBackground(final MouseEvent event, final String position, final Window window) {
        changeItemBackground(event, null, null, position, window, null);
    }
    
    public static void changeItemBackground(final MouseEvent event, final FieldProvider fieldProvider, Item item, final String position, final Window window, final PopupElement popup) {
        String style = "";
        final Widget w = event.getTarget();
        if (ClientTradeHelper.INSTANCE.getCurrentTrade() != null && PropertiesProvider.getInstance().getObjectProperty("exchange.sourceBag") instanceof Bag) {
            return;
        }
        if (position != null) {
            item = UIStuffPreviewFrame.INSTANCE.getStuffPreview().getFromPosition(Byte.parseByte(position));
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
                style = ItemDisplayerImpl.getBackgroundStyle(item, w.getElementMap().getId());
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
    
    static {
        StuffPreviewDialogActions.m_draggedItemId = -1L;
    }
}
