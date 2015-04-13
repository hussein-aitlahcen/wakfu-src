package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;

@XulorActionsTag
public class SeedSpreaderDialogActions extends HavenWorldDialogsActions
{
    private static final Logger m_logger;
    public static final String PACKAGE = "wakfu.seedSpreader";
    private static FakeItem m_draggedItem;
    
    public static void dropSeed(final DropEvent dropEvent) {
        if (!(dropEvent.getValue() instanceof Item)) {
            return;
        }
        UISeedSpreaderFrame.getInstance().resetSlot();
        final Item item = (Item)dropEvent.getValue();
        final boolean isInInventories = WakfuGameEntity.getInstance().getLocalPlayer().getBags().contains(item) != null;
        if (!isInInventories) {
            return;
        }
        EquipmentDialogActions.onDropItem();
        final boolean shiftPressed = dropEvent.hasShift();
        final boolean isDefaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
        final short messageId = 19342;
        if (item.getQuantity() > 1 && ((shiftPressed && !isDefaultSplitMode) || (!shiftPressed && isDefaultSplitMode))) {
            SplitStackDialogActions.setMaxQuantity(item.getQuantity());
            SplitStackDialogActions.setItem(item);
            SplitStackDialogActions.setMessageType((short)19342);
            final UIItemMessage message = new UIItemMessage();
            message.setItem(item);
            message.setX((short)dropEvent.getScreenX());
            message.setY((short)dropEvent.getScreenY());
            message.setId(16821);
            Worker.getInstance().pushMessage(message);
        }
        else {
            final UIItemMessage message = new UIItemMessage();
            message.setLongValue(item.getUniqueId());
            message.setQuantity(item.getQuantity());
            message.setId(19342);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static FakeItem getDraggedItem() {
        return SeedSpreaderDialogActions.m_draggedItem;
    }
    
    public static void setDraggedItem(final FakeItem draggedItem) {
        SeedSpreaderDialogActions.m_draggedItem = draggedItem;
    }
    
    public static void removeSeed(final DragEvent dragEvent) {
        SeedSpreaderDialogActions.m_draggedItem = (FakeItem)dragEvent.getValue();
        UIMessage.send((short)19343);
    }
    
    public static void closeWindow(final Event e) {
        if (UISeedSpreaderFrame.getInstance().isSeedSpreaderDirty()) {
            final String msgText = WakfuTranslator.getInstance().getString("question.seedSpreaderClose");
            final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        unloadDialog();
                    }
                }
            });
        }
        else {
            unloadDialog();
        }
    }
    
    private static void unloadDialog() {
        Xulor.getInstance().unload("seedSpreaderDialog");
    }
    
    public static void valid(final Event e) {
        UIMessage.send((short)19344);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SeedSpreaderDialogActions.class);
    }
}
