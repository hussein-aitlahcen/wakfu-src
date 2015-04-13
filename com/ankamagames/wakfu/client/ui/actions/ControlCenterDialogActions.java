package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.console.command.commonIn.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.console.command.world.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.havenWorld.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class ControlCenterDialogActions
{
    public static final String PACKAGE = "wakfu.controlCenter";
    private static final Logger m_logger;
    private static int GUID;
    
    public static void openCloseShortcutBar(final Event event) {
        UIMessage.send((short)16404);
    }
    
    public static void openCloseStateBar(final Event event) {
        UIMessage.send((short)16407);
    }
    
    public static void openCloseEventsCalendarDialog(final Event event) {
        if (WakfuGameEntity.getInstance().hasFrame(UIEventsCalendarFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIEventsCalendarFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIEventsCalendarFrame.getInstance());
        }
    }
    
    public static void setMode(final Event event, final String index) {
        PropertiesProvider.getInstance().setPropertyValue("controlCenterDisplayMode", index);
    }
    
    public static void dropCommand(final Event event, final String index) {
        if (event instanceof DropEvent) {
            final DropEvent dropEvent = (DropEvent)event;
        }
    }
    
    public static void openInventory(final Event event) {
        if (WakfuGameEntity.getInstance().hasFrame(UIEquipmentFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIEquipmentFrame.getInstance());
        }
        else {
            UIEquipmentFrame.getInstance().openEquipment();
        }
    }
    
    public static void openCharacterSheet(final Event event) {
        final UICharacterSheetFrame bookFrame = UICharacterSheetFrame.getInstance();
        if (!WakfuGameEntity.getInstance().hasFrame(bookFrame)) {
            WakfuGameEntity.getInstance().pushFrame(bookFrame);
        }
        else {
            WakfuGameEntity.getInstance().removeFrame(bookFrame);
        }
    }
    
    public static void openSpellsPage(final Event event) {
        UISpellsPageFrame.getInstance().loadUnloadSpellsPage();
    }
    
    public static void openCloseGuildManagement(final Event e) {
        if (!WakfuGameEntity.getInstance().hasFrame(UIGuildManagementFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIGuildManagementFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().removeFrame(UIGuildManagementFrame.getInstance());
        }
    }
    
    public static void openCloseDimensionBagRoomManager(final Event e) {
        if (!WakfuGameEntity.getInstance().hasFrame(UIDimensionalBagRoomManagerFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIDimensionalBagRoomManagerFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().removeFrame(UIDimensionalBagRoomManagerFrame.getInstance());
        }
    }
    
    public static void openCloseMiniMap(final Event e) {
        throw new UnsupportedOperationException("il n'y a plus de minimap.");
    }
    
    public static void openCloseGlobalMap(final Event e) {
        MapCommand.openCloseMapDialog();
    }
    
    public static void openCloseCraftDialog(final Event event) {
        if (!WakfuGameEntity.getInstance().hasFrame(UICraftFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UICraftFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().removeFrame(UICraftFrame.getInstance());
        }
    }
    
    public static void openOptionsDialog(final Event event) {
        UIMessage.send((short)16400);
    }
    
    public static void cycleProgressBarDisplayMode(final Event e, final ProgressText pt) {
        cycleProgressBarDisplayMode(pt, false);
    }
    
    public static void cycleProgressBarDisplayModeFull(final Event e, final ProgressText pt) {
        cycleProgressBarDisplayMode(pt, true);
    }
    
    private static void cycleProgressBarDisplayMode(final ProgressText pt, final boolean displayPercentage) {
        int ordinal = pt.getDisplayValue().ordinal();
        final ProgressText.ProgressBarDisplayValue[] values = ProgressText.ProgressBarDisplayValue.values();
        ProgressText.ProgressBarDisplayValue value;
        do {
            value = values[(ordinal + 1) % values.length];
            ++ordinal;
        } while (!displayPercentage && value == ProgressText.ProgressBarDisplayValue.CURRENT_PERCENTAGE);
        WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.HP_DISPLAY_TYPE_KEY, value.ordinal());
        WakfuSoundManager.getInstance().playGUISound(600004L);
    }
    
    public static void clickHp(final MouseEvent e, final ProgressText pt) {
        if (e.getButton() == 1) {
            cycleProgressBarDisplayMode(pt, true);
        }
    }
    
    public static void openCloseSmallWeatherDialog(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UIWeatherInfoSmallFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIWeatherInfoSmallFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIWeatherInfoSmallFrame.getInstance());
        }
    }
    
    public static void openCloseWeatherDialog(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UIWeatherInfoFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIWeatherInfoFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIWeatherInfoFrame.getInstance());
        }
    }
    
    public static void openCloseEcosystemEquilibriumDialog(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UIEcosystemEquilibriumFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIEcosystemEquilibriumFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIEcosystemEquilibriumFrame.getInstance());
        }
    }
    
    public static void openAchievementsDialog(final Event e, final AchievementView achievementView) {
        final boolean quest = AchievementsViewManager.INSTANCE.isQuest(WakfuGameEntity.getInstance().getLocalPlayer().getId(), achievementView.getId());
        UIAchievementsFrame.getInstance().loadAchievementDialog(quest, achievementView);
    }
    
    public static void openCloseAchievementsDialog(final Event e) {
        UIAchievementsFrame.getInstance().loadUnloadAchievementsDialog(false);
    }
    
    public static void openCloseAlmanach(final Event e) {
        if (!WakfuGameEntity.getInstance().hasFrame(UIAlmanachFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIAlmanachFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().removeFrame(UIAlmanachFrame.getInstance());
        }
    }
    
    public static void openCloseQuestsDialog(final Event e) {
        UIAchievementsFrame.getInstance().loadUnloadAchievementsDialog(true);
    }
    
    public static void openCloseProtectorView(final Event e) {
        if (!WakfuGameEntity.getInstance().hasFrame(UIProtectorViewFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIProtectorViewFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().removeFrame(UIProtectorViewFrame.getInstance());
        }
    }
    
    public static void openClosePassport(final Event e) {
        if (!WakfuGameEntity.getInstance().hasFrame(UIPassportFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UIPassportFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().removeFrame(UIPassportFrame.getInstance());
        }
    }
    
    public static void openCloseCitizen(final Event e) {
        if (!WakfuGameEntity.getInstance().hasFrame(UINationFrame.getInstance())) {
            WakfuGameEntity.getInstance().pushFrame(UINationFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().removeFrame(UINationFrame.getInstance());
        }
    }
    
    public static void onMiniMapItemOver(final MapItemEvent e) {
        UIControlCenterContainerFrame.getInstance().displayMiniMapPopup(e);
    }
    
    public static void onMiniMapItemOut(final MapItemEvent e) {
        MasterRootContainer.getInstance().getPopupContainer().hide();
    }
    
    public static void openRunningEffectDescription(final ItemEvent e) {
        final RunningEffectFieldProvider re = (RunningEffectFieldProvider)e.getItemValue();
        if (re == null) {
            return;
        }
        final RunningEffectFieldProvider copy = re.getCopy();
        final String id = "runningEffectDetailDialog" + re.getCommonEffectContainer().getEffectContainerId();
        if (Xulor.getInstance().isLoaded(id)) {
            Xulor.getInstance().unload(id);
        }
        else {
            Xulor.getInstance().load(id, Dialogs.getDialogPath("runningEffectDetailDialog"), 16L, (short)10000);
            PropertiesProvider.getInstance().setLocalPropertyValue("describedRunningEffect", copy, id);
        }
    }
    
    public static void openCloseOsamodasSymbiotDialog(final Event event) {
        UIMessage.send((short)16422);
    }
    
    public static void openCloseDimensionalBag(final Event e) {
        EnterLeaveDimensionalBagCommand.enterLeaveDimensionalBag();
    }
    
    public static void openCloseGazette(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UIGazetteFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIGazetteFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIGazetteFrame.getInstance());
        }
    }
    
    public static void openCloseDungeonListDialog(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UIDungeonFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIDungeonFrame.getInstance());
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UIDungeonFrame.getInstance());
        }
    }
    
    public static void openCloseTutorialBookDialog(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UITutorialBookFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().removeFrame(UITutorialBookFrame.INSTANCE);
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UITutorialBookFrame.INSTANCE);
        }
    }
    
    public static void openHavenWorldCatalog(final Event e) {
    }
    
    public static void leaveHavenWorld(final Event e) {
        final LeaveHavenWorldRequestMessage leaveHavenWorldRequestMessage = new LeaveHavenWorldRequestMessage();
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(leaveHavenWorldRequestMessage);
    }
    
    public static void openCompanionsManagement(final Event e) {
        if (WakfuGameEntity.getInstance().hasFrame(UICompanionsManagementFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().removeFrame(UICompanionsManagementFrame.INSTANCE);
        }
        else {
            WakfuGameEntity.getInstance().pushFrame(UICompanionsManagementFrame.INSTANCE);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ControlCenterDialogActions.class);
        ControlCenterDialogActions.GUID = 0;
    }
}
