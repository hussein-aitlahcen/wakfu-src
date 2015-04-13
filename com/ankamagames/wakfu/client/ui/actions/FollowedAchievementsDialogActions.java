package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.core.*;

@XulorActionsTag
public class FollowedAchievementsDialogActions
{
    public static final String PACKAGE = "wakfu.followedAchievements";
    private static final long CHARACTER_ID;
    private static Runnable m_fader;
    
    public static void fillList(final RenderableContainer rc, final int id, final boolean fadeIn) {
        final AbstractQuestView qv = (AbstractQuestView)rc.getItemValue();
        if (qv == null) {
            return;
        }
        final int viewId = qv.getId();
        if (viewId == id && !fadeIn) {
            return;
        }
        final ElementMap map = rc.getInnerElementMap();
        addApp((Widget)map.getElement("environmentQuestContainer"), fadeIn);
        final StackList goalsList = (StackList)map.getElement("goalsList");
        final ArrayList<RenderableContainer> renderables = goalsList.getRenderables();
        for (int i = 0, size = renderables.size(); i < size; ++i) {
            final RenderableContainer renderableContainer = renderables.get(i);
            final ElementMap rcMap = renderableContainer.getInnerElementMap();
            addApp((Widget)rcMap.getElement("goalDesc"), fadeIn);
            addApp((Widget)rcMap.getElement("goalValue"), fadeIn);
        }
        addApp((Widget)map.getElement("timeText"), fadeIn);
        addApp((Widget)map.getElement("timeValue"), fadeIn);
        addApp((Widget)map.getElement("rankingText"), fadeIn);
        addApp((Widget)map.getElement("rankingValue"), fadeIn);
    }
    
    private static void addApp(final Widget w, final boolean fadeIn) {
        if (w == null || w.getAppearance() == null) {
            return;
        }
        final Color modulationColor = w.getAppearance().getModulationColor();
        final Color color = fadeIn ? Color.WHITE_ALPHA : Color.WHITE;
        final Color colorA = (modulationColor != null) ? new Color(modulationColor.get()) : Color.WHITE;
        final Color colorB = fadeIn ? Color.WHITE : Color.WHITE_ALPHA;
        final AbstractTween tween = new ModulationColorTween(colorA, colorB, w.getAppearance(), 0, 150, 1, false, TweenFunction.PROGRESSIVE);
        w.getAppearance().removeTweensOfType(ModulationColorTween.class);
        w.getAppearance().addTween(tween);
    }
    
    public static void fadeAll(final boolean fadeIn, final int id) {
        if (fadeIn) {
            FollowedAchievementsDialogActions.m_fader = new Runnable() {
                @Override
                public void run() {
                    FollowedAchievementsDialogActions.doFadeAll(fadeIn, id);
                    FollowedAchievementsDialogActions.m_fader = null;
                }
            };
            ProcessScheduler.getInstance().schedule(FollowedAchievementsDialogActions.m_fader, 150L, 1);
        }
        else {
            if (FollowedAchievementsDialogActions.m_fader != null) {
                ProcessScheduler.getInstance().remove(FollowedAchievementsDialogActions.m_fader);
                FollowedAchievementsDialogActions.m_fader = null;
            }
            doFadeAll(fadeIn, id);
        }
    }
    
    public static void doFadeAll(final boolean fadeIn, final int id) {
        final String dialogId = Xulor.getInstance().isLoaded("followedAchievementsDialog") ? "followedAchievementsDialog" : "verticalFollowedAchievementsDialog";
        final ArrayList<ModulationColorClient> apps = new ArrayList<ModulationColorClient>();
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(dialogId);
        fillList((RenderableContainer)map.getElement("environmentRenderable"), id, fadeIn);
        fillList((RenderableContainer)map.getElement("almanaxRenderable"), id, fadeIn);
        final StackList list = (StackList)map.getElement("achievementsList");
        final ArrayList<RenderableContainer> renderables = list.getRenderables();
        for (int i = 0, size = renderables.size(); i < size; ++i) {
            fillList(renderables.get(i), id, fadeIn);
        }
    }
    
    public static void popup(final Event e, final PopupElement popup, final AbstractQuestView view, final Widget target) {
        PropertiesProvider.getInstance().setPropertyValue("displayedAchievement", view);
        XulorActions.popup(e, popup, target);
    }
    
    public static void openAchievementDialog(final Event e, final AchievementView achievementView) {
        if (achievementView == null) {
            return;
        }
        final boolean quest = AchievementsViewManager.INSTANCE.isQuest(FollowedAchievementsDialogActions.CHARACTER_ID, achievementView.getId());
        UIAchievementsFrame.getInstance().loadAchievementDialog(quest, achievementView);
    }
    
    public static void onClick(final MouseEvent e, final AbstractQuestView view) {
        openQuestDescriptionDialog(e, view);
    }
    
    public static void onMouseEnter(final MouseEvent e, final AbstractQuestView v) {
        fadeAll(false, v.getId());
    }
    
    public static void onMouseExit(final MouseEvent e, final AbstractQuestView v) {
        fadeAll(true, v.getId());
    }
    
    public static void openQuestDescriptionDialog(final Event e, final AbstractQuestView achievementView) {
        if (achievementView == null) {
            return;
        }
        UIAchievementsFrame.getInstance().loadQuestDescription(achievementView);
    }
    
    public static void toggleQuestOpened(final Event e, final AbstractQuestView view) {
        view.setOpened(!view.isOpened());
    }
    
    public static boolean followAchievement(final SelectionChangedEvent event, final AchievementView view) {
        final UIMessage msg = new UIMessage();
        msg.setId(16138);
        msg.setIntValue(view.getId());
        msg.setBooleanValue(event.isSelected());
        Worker.getInstance().pushMessage(msg);
        return true;
    }
    
    public static void compassObjective(final SelectionChangedEvent event, final AchievementGoalView view) {
        if (event.isSelected()) {
            AchievementsViewManager.INSTANCE.compassObjective(FollowedAchievementsDialogActions.CHARACTER_ID, view.getId());
        }
        else if (AchievementsViewManager.INSTANCE.hasCompassedObjectiveId(view.getId())) {
            AchievementsViewManager.INSTANCE.compassObjective(FollowedAchievementsDialogActions.CHARACTER_ID, -1);
        }
    }
    
    static {
        CHARACTER_ID = WakfuGameEntity.getInstance().getLocalPlayer().getId();
    }
}
