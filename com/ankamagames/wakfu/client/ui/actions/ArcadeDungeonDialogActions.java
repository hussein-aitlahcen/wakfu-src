package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.dungeon.arcade.*;
import com.ankamagames.wakfu.common.game.dungeon.rewards.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

@XulorActionsTag
public class ArcadeDungeonDialogActions
{
    public static final String PACKAGE = "wakfu.arcadeDungeon";
    
    public static void closeArcadeDungeonDetailSmallDialog(final Event e) {
        PropertiesProvider.getInstance().setPropertyValue("arcadeDungeonDetailsVisible", false);
    }
    
    public static void onArcadeDungeonCheckBoxOver(final Event e, final Widget arcadeDungeonDetails) {
        arcadeDungeonDetails.setVisible(true);
    }
    
    public static void onArcadeDungeonCheckBoxOut(final Event e, final Widget arcadeDungeonDetails) {
        final ToggleButton checkBox = e.getTarget();
        if (!checkBox.getSelected()) {
            arcadeDungeonDetails.setVisible(false);
        }
    }
    
    public static void openCloseArcadeDungeonSmallDialog(final SelectionChangedEvent e, final Widget arcadeDungeonDetail) {
        arcadeDungeonDetail.setVisible(e.isSelected());
    }
    
    public static void showItemDetails(final ItemEvent event, final Container cont) {
        final Object itemValue = event.getItemValue();
        AbstractReferenceItem referenceItem;
        if (itemValue instanceof RewardView) {
            final RewardView entry = (RewardView)itemValue;
            final Reward reward = entry.getReward();
            if (reward.getType() != Reward.Type.Item) {
                final PopupElement popup = (PopupElement)cont.getElementMap().getElement("rewardPopup");
                if (event.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
                    PropertiesProvider.getInstance().setPropertyValue("desc.reward", entry.getDescription());
                    XulorActions.popup(popup, event.getTarget());
                }
                else if (event.getType() == Events.ITEM_OUT) {
                    XulorActions.closePopup(event, popup);
                    PropertiesProvider.getInstance().setPropertyValue("desc.reward", null);
                }
                return;
            }
            referenceItem = ReferenceItemManager.getInstance().getReferenceItem(reward.getValue());
        }
        else {
            referenceItem = (ReferenceItem)itemValue;
        }
        final PopupElement popup2 = (PopupElement)cont.getElementMap().getElement("itemDetailPopup");
        if (event.getType() == Events.ITEM_OVER && !MasterRootContainer.getInstance().isDragging()) {
            PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", referenceItem);
            PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
            XulorActions.popup(popup2, event.getTarget());
        }
        else if (event.getType() == Events.ITEM_OUT) {
            PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", null);
            PropertiesProvider.getInstance().setPropertyValue("isFromShortcut", false);
            PropertiesProvider.getInstance().setPropertyValue("isFromEquipedShortcut", false);
            XulorActions.closePopup(event, popup2);
        }
    }
}
