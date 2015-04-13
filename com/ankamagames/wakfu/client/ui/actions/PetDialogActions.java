package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.message.pet.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;

@XulorActionsTag
public class PetDialogActions
{
    public static final String PACKAGE = "wakfu.pet";
    private static final int DIRECTION_INDEX_MIN = 0;
    private static final int DIRECTION_INDEX_MAX = 7;
    private static ObjectPair<Long, Long> m_draggedItemAndPetId;
    
    public static void buyArticle(final Event e, final Article article) {
        UIWebShopFrame.getInstance().openArticleDialog(article);
    }
    
    public static void validColorPreview(final Event e, final PetDetailDialogView petDetailDialogView) {
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setLongValue(petDetailDialogView.getPetItem().getUniqueId());
        uiMessage.setId(19157);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void cancelColorPreview(final Event e) {
        UIMessage.send((short)19156);
    }
    
    public static void dropColor(final DropEvent e, final PetDetailDialogView petDetailDialogView) {
        if (e.getValue() != null && e.getValue() instanceof Item) {
            EquipmentDialogActions.onDropItem();
            final Item item = (Item)e.getValue();
            if (!petDetailDialogView.getPet().getDefinition().containsColorItem(item.getReferenceId())) {
                return;
            }
            final UIItemMessage uiItemMessage = new UIItemMessage();
            uiItemMessage.setId(19155);
            uiItemMessage.setItem(item);
            uiItemMessage.setLongValue(petDetailDialogView.getPetItem().getUniqueId());
            Worker.getInstance().pushMessage(uiItemMessage);
        }
    }
    
    public static void dragEquipment(final DragEvent e, final PetDetailDialogView petDetailDialogView) {
        if (e.getValue() != null && e.getValue() instanceof Item) {
            final Item petItem = (Item)e.getValue();
            PetDialogActions.m_draggedItemAndPetId.setFirst(petDetailDialogView.getPetItem().getUniqueId());
            PetDialogActions.m_draggedItemAndPetId.setSecond(petItem.getUniqueId());
        }
    }
    
    public static void setPetName(final Event event, final PetDetailDialogView petDetailDialogView, final TextEditor textEditor) {
        if (event.getType() == Events.MOUSE_CLICKED || (event.getType() == Events.KEY_PRESSED && ((KeyEvent)event).getKeyCode() == 10)) {
            final boolean isValid = WordsModerator.getInstance().validateName(textEditor.getText());
            if (isValid) {
                final UIPetMessage message = new UIPetMessage(petDetailDialogView, 19153);
                message.setStringValue(textEditor.getText());
                Worker.getInstance().pushMessage(message);
            }
            else {
                ChatHelper.pushErrorMessage("error.connection.nicknameInvalidContent", new Object[0]);
            }
        }
    }
    
    public static void changeDirection(final MouseEvent mouseEvent, final AnimatedElementViewer objViewer) {
        final int button = mouseEvent.getButton();
        if (button != 1 && button != 3) {
            return;
        }
        final int delta = (button == 1) ? -1 : 1;
        int direction = (objViewer.getDirection() + delta) % 8;
        if (direction < 0) {
            direction = 7;
        }
        objViewer.setDirection(direction);
        objViewer.setDirection(direction);
    }
    
    public static void feedPet(final DropEvent dropEvent) {
        final PetDetailDialogView petDetailDialogView = (PetDetailDialogView)PropertiesProvider.getInstance().getObjectProperty("pet");
        feedPet(dropEvent, petDetailDialogView);
    }
    
    public static void feedPet(final DropEvent dropEvent, final PetDetailDialogView petDetailDialogView) {
        EquipmentDialogActions.onDropItem();
        final Object value = dropEvent.getValue();
        if (value == null || !(value instanceof Item) || petDetailDialogView == null) {
            return;
        }
        final UIFeedPetMessage uiFeedPetMessage = new UIFeedPetMessage(petDetailDialogView, (Item)value);
        Worker.getInstance().pushMessage(uiFeedPetMessage);
    }
    
    public static void openCloseCosmeticsDialog(final Event e, final PetDetailDialogView view) {
        if (WakfuGameEntity.getInstance().hasFrame(UIPetCosmeticsFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIPetCosmeticsFrame.getInstance());
        }
        else {
            UIPetCosmeticsFrame.getInstance().setPetView(view);
            WakfuGameEntity.getInstance().pushFrame(UIPetCosmeticsFrame.getInstance());
        }
    }
    
    public static long getDraggedPetId() {
        final Long first = PetDialogActions.m_draggedItemAndPetId.getFirst();
        return (first == null) ? -1L : first;
    }
    
    public static long getDraggedItemId() {
        final Long second = PetDialogActions.m_draggedItemAndPetId.getSecond();
        return (second == null) ? -1L : second;
    }
    
    public static void resetDraggedItemAndPetId() {
        PetDialogActions.m_draggedItemAndPetId = new ObjectPair<Long, Long>();
    }
    
    static {
        PetDialogActions.m_draggedItemAndPetId = new ObjectPair<Long, Long>();
    }
}
