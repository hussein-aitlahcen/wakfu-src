package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.bookcase.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.xulor2.event.*;

@XulorActionsTag
public class BookcaseDialogActions
{
    public static final String PACKAGE = "wakfu.bookcase";
    private static long m_draggedItemId;
    
    public static void swapPosition(final byte index1, final byte index2) {
        final UIMessage msg = new UIMessage();
        msg.setId(19318);
        msg.setByteValue(index1);
        msg.setShortValue(index2);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void displayBook(final ItemEvent e) {
        final Item bookItem = (Item)e.getItemValue();
        final UIItemMessage uiMessage = new UIItemMessage();
        uiMessage.setItem(bookItem);
        uiMessage.setId(19319);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void addBook(final Item item, final short index) {
        final UIItemMessage uiMessage = new UIItemMessage();
        uiMessage.setItem(item);
        uiMessage.setShortValue(index);
        uiMessage.setId(19316);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void removeBook(final short index) {
        final UIItemMessage uiMessage = new UIItemMessage();
        uiMessage.setShortValue(index);
        uiMessage.setId(19317);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void removeBook(final ItemEvent e) {
        final RenderableContainer renderable = e.getTarget();
        final short index = (short)renderable.getCollection().getTableIndex(renderable);
        removeBook(index);
    }
    
    public static void dropItem(final DropEvent e, final BookcaseView bookcaseView) {
        if (!(e.getValue() instanceof Item)) {
            return;
        }
        final Item item = (Item)e.getValue();
        if (WakfuGameEntity.getInstance().getLocalPlayer().getBags().contains(item) != null) {
            EquipmentDialogActions.onDropItem();
            final RenderableContainer rc = e.getDroppedInto().getRenderableParent();
            final short index = (short)rc.getCollection().getTableIndex(rc);
            addBook(item, index);
        }
        else if (bookcaseView.getBookcase().getBag().contains(item)) {
            final RenderableContainer rc2 = e.getDroppedInto().getRenderableParent();
            final byte index2 = (byte)rc2.getCollection().getTableIndex(rc2);
            final RenderableContainer rc3 = e.getDropped().getRenderableParent();
            final byte index3 = (byte)rc3.getCollection().getTableIndex(rc3);
            swapPosition(index2, index3);
        }
    }
    
    public static void dragItem(final DragEvent dragEvent, final BookcaseView bookcaseView) {
        final Item item = (Item)dragEvent.getValue();
        BookcaseDialogActions.m_draggedItemId = item.getUniqueId();
    }
    
    public static long getDraggedItemId() {
        return BookcaseDialogActions.m_draggedItemId;
    }
    
    public static void setDraggedItemId(final long draggedItemId) {
        BookcaseDialogActions.m_draggedItemId = draggedItemId;
    }
    
    static {
        BookcaseDialogActions.m_draggedItemId = -1L;
    }
}
