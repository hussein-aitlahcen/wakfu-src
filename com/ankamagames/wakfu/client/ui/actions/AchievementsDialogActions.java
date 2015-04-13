package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.ui.protocol.message.achievement.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

@XulorActionsTag
public class AchievementsDialogActions
{
    public static final String PACKAGE = "wakfu.achievements";
    
    public static void selectCategory(final ItemEvent e) {
        final UISelectAchievementContainerMessage uiSelectAchievementContainerMessage = new UISelectAchievementContainerMessage((AchievementCategoryView)e.getItemValue());
        Worker.getInstance().pushMessage(uiSelectAchievementContainerMessage);
    }
    
    public static void displayHistory(final Event e) {
        final UISelectAchievementContainerMessage uiSelectAchievementContainerMessage = new UISelectAchievementContainerMessage((AchievementCategoryView)null);
        Worker.getInstance().pushMessage(uiSelectAchievementContainerMessage);
    }
    
    public static void selectAchievement(final ItemEvent e) {
        final UISelectAchievementMessage uiSelectAchievementMessage = new UISelectAchievementMessage((AchievementView)e.getItemValue());
        Worker.getInstance().pushMessage(uiSelectAchievementMessage);
    }
    
    public static void mouseOverAchievementQuickList(final ItemEvent e) {
        PropertiesProvider.getInstance().setPropertyValue("overQuickAchievement", e.getItemValue());
    }
    
    public static void mouseOutAchievementQuickList(final ItemEvent e) {
        PropertiesProvider.getInstance().setPropertyValue("overQuickAchievement", PropertiesProvider.getInstance().getObjectProperty("selectedAchievement"));
    }
    
    public static void changeRewardBackgroundAndPopup(final MouseEvent event, final PopupElement popupElement, final Widget widget) {
        if (event.getType() == Events.MOUSE_ENTERED) {
            widget.setStyle("itemSelectedBackground");
            XulorActions.popup(event, popupElement);
        }
        else if (event.getType() == Events.MOUSE_EXITED) {
            widget.setStyle("itemBackground");
            XulorActions.closePopup(event, popupElement);
        }
    }
    
    public static void changeAchievementFilter(final SelectionChangedEvent event, final UIAchievementsFrame.AchievementFilter filter) {
        if (event.isSelected()) {
            UIAchievementsFrame.getInstance().setCurrentFilter(filter);
        }
    }
    
    public static void selectLastCompletedAchievement(final Event event) {
        final UISelectAchievementMessage uiSelectAchievementMessage = new UISelectAchievementMessage((AchievementView)null);
        Worker.getInstance().pushMessage(uiSelectAchievementMessage);
    }
    
    public static void selectPreviousAchievementQuickList(final Event event) {
        final AchievementView quickListAchievement = UIAchievementsFrame.getInstance().getDisplayedAchievementsView().getSelectedRootCategory().getPreviousQuickListAchievement();
        if (quickListAchievement == null) {
            return;
        }
        final UISelectAchievementMessage uiSelectAchievementMessage = new UISelectAchievementMessage(quickListAchievement);
        Worker.getInstance().pushMessage(uiSelectAchievementMessage);
    }
    
    public static void selectNextAchievementQuickList(final Event event) {
        final AchievementView quickListAchievement = UIAchievementsFrame.getInstance().getDisplayedAchievementsView().getSelectedRootCategory().getNextQuickListAchievement();
        if (quickListAchievement == null) {
            return;
        }
        final UISelectAchievementMessage uiSelectAchievementMessage = new UISelectAchievementMessage(quickListAchievement);
        Worker.getInstance().pushMessage(uiSelectAchievementMessage);
    }
    
    public static void goBackCategory(final Event event) {
        final UISelectAchievementContainerMessage uiSelectAchievementContainerMessage = new UISelectAchievementContainerMessage(UIAchievementsFrame.getInstance().getDisplayedAchievementsView().getSelectedRootCategory());
        Worker.getInstance().pushMessage(uiSelectAchievementContainerMessage);
    }
    
    public static boolean followAchievement(final SelectionChangedEvent event, final AchievementView view) {
        final UIMessage msg = new UIMessage();
        msg.setId(16138);
        msg.setIntValue(view.getId());
        msg.setBooleanValue(event.isSelected());
        Worker.getInstance().pushMessage(msg);
        return true;
    }
    
    public static void share(final Event e, final AchievementView achievement) {
        final UIMessage msg = new UIMessage();
        msg.setId(16139);
        msg.setIntValue(achievement.getId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void switchCharacter(final Event e, final PlayerCharacter character) {
        final UIMessage msg = new UIMessage();
        msg.setId(19459);
        msg.setLongValue(character.getId());
        Worker.getInstance().pushMessage(msg);
    }
}
