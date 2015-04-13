package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.xulor2.event.*;

@XulorActionsTag
public class EquipableDummyDialogActions
{
    public static final String PACKAGE = "wakfu.equipableDummy";
    private static long m_draggedItemId;
    
    public static void equipItem(final Item item) {
        final UIItemMessage uiMessage = new UIItemMessage();
        uiMessage.setItem(item);
        uiMessage.setId(19314);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void unequipItem(final int refId) {
        final UIItemMessage uiMessage = new UIItemMessage();
        uiMessage.setIntValue(refId);
        uiMessage.setId(19315);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void unequip(final ItemEvent e) {
        final Item item = (Item)e.getItemValue();
        unequipItem(item.getReferenceId());
    }
    
    public static void dropItem(final DropEvent e) {
        if (!(e.getValue() instanceof Item)) {
            return;
        }
        final Item item = (Item)e.getValue();
        if (WakfuGameEntity.getInstance().getLocalPlayer().getBags().contains(item) == null) {
            return;
        }
        EquipmentDialogActions.onDropItem();
        equipItem(item);
    }
    
    public static void dragItem(final DragEvent dragEvent) {
        final Item item = (Item)dragEvent.getValue();
        EquipableDummyDialogActions.m_draggedItemId = item.getUniqueId();
    }
    
    public static long getDraggedItemId() {
        return EquipableDummyDialogActions.m_draggedItemId;
    }
    
    public static void setDraggedItemId(final long draggedItemId) {
        EquipableDummyDialogActions.m_draggedItemId = draggedItemId;
    }
    
    static {
        EquipableDummyDialogActions.m_draggedItemId = -1L;
    }
}
