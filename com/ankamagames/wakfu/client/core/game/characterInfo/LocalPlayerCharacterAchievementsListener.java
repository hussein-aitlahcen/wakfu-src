package com.ankamagames.wakfu.client.core.game.characterInfo;

import org.apache.log4j.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;
import com.ankamagames.wakfu.common.game.aptitude.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.reflect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.client.core.game.steam.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.achievement.*;
import gnu.trove.*;

public final class LocalPlayerCharacterAchievementsListener implements AchievementsContextEventListener
{
    private static final Logger m_logger;
    private final long m_characterId;
    private final ClientAchievementsContext m_context;
    private AudioSource m_currentSource;
    private AchievementEventType m_currentAchievementEvent;
    private final ArrayList<Runnable> m_loadActivatedAchievementDialogList;
    private Runnable m_cleanUpSound;
    
    public LocalPlayerCharacterAchievementsListener(final long characterId, final ClientAchievementsContext context) {
        super();
        this.m_currentAchievementEvent = AchievementEventType.NONE;
        this.m_loadActivatedAchievementDialogList = new ArrayList<Runnable>();
        this.m_cleanUpSound = new Runnable() {
            @Override
            public void run() {
                LocalPlayerCharacterAchievementsListener.this.m_currentSource = null;
                LocalPlayerCharacterAchievementsListener.this.m_currentAchievementEvent = AchievementEventType.NONE;
            }
        };
        this.m_characterId = characterId;
        this.m_context = context;
    }
    
    @Override
    public void onObjectiveCompleted(final Objective objective) {
        if (!objective.getAchievement().isVisible(HeroesManager.INSTANCE.getHero(this.m_characterId))) {
            return;
        }
        if (AchievementsViewManager.INSTANCE.hasCompassedObjectiveId(objective.getId())) {
            MapManager.getInstance().removeCompassPointAndPositionMarker();
        }
        final AchievementGoalView objectiveView = AchievementsViewManager.INSTANCE.getObjective(this.m_characterId, objective.getId());
        objectiveView.updateCompletion();
        final AchievementView achievementView = AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, objective.getAchievement().getId());
        achievementView.updateObjectivesField();
        if (objective.isFeedback() && AchievementEventType.OBJECTIVE_COMPLETE.isGreaterThan(this.m_currentAchievementEvent)) {
            this.m_currentAchievementEvent = AchievementEventType.OBJECTIVE_COMPLETE;
            if (this.m_currentSource != null) {
                this.m_currentSource.stopAndRelease();
            }
            this.m_currentSource = WakfuSoundManager.getInstance().playGuiSoundAndFadeMusic(600074L, 0.4f, 100, 1500, 2200);
            this.setupSoundCleanUp(2200);
        }
    }
    
    @Override
    public void onAchievementCompleted(final Achievement achievement) {
        if (!achievement.isVisible(HeroesManager.INSTANCE.getHero(this.m_characterId))) {
            return;
        }
        final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
        for (final long characterId : HeroesManager.INSTANCE.getHeroesInParty(WakfuGameEntity.getInstance().getLocalPlayer().getClientId())) {
            final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(characterId);
            context.getAchievement(achievement.getId());
        }
        if (AchievementEventType.ACHIEVEMENT_COMPLETE.isGreaterThan(this.m_currentAchievementEvent)) {
            this.m_currentAchievementEvent = AchievementEventType.ACHIEVEMENT_COMPLETE;
            if (this.m_currentSource != null) {
                this.m_currentSource.stopAndRelease();
            }
            this.m_currentSource = WakfuSoundManager.getInstance().playGuiSoundAndFadeMusic(600075L, 0.4f, 100, 1500, 3300);
            this.setupSoundCleanUp(3300);
        }
        FollowedQuestsManager.INSTANCE.onAchievementFinished(achievement.getId());
        final AchievementView achievementView = AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, achievement.getId());
        achievementView.updateStatesField();
        FollowedQuestsManager.INSTANCE.onFollowedListChanged();
        QuestConfigManager.INSTANCE.removeConfig(achievement.getId());
        if (!achievement.isNotifyOnPass()) {
            return;
        }
        final boolean isQuest = AchievementsViewManager.INSTANCE.isQuest(this.m_characterId, achievement.getId());
        FieldProvider value = null;
        String chatMessageString = null;
        String title;
        NotificationMessageType notificationType;
        if (achievement.getCategory().hasParentInHierarchy(83)) {
            title = WakfuTranslator.getInstance().getString("notification.questExploration");
            notificationType = NotificationMessageType.ACHIEVEMENTS;
        }
        else if (isQuest) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.append(WakfuTranslator.getInstance().getString(62, achievement.getId(), new Object[0]));
            sb.append(" ");
            sb.openText().addColor(Color.GREEN.getRGBtoHex());
            sb.append(WakfuTranslator.getInstance().getString("quest.result.success"));
            sb.closeText();
            title = sb.finishAndToString();
            notificationType = NotificationMessageType.QUEST_SUCCESS;
            value = AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, achievement.getId());
            final TextWidgetFormater chatSb = new TextWidgetFormater();
            chatSb.b().addColor(Color.RED.getRGBtoHex()).append(WakfuTranslator.getInstance().getString(62, achievement.getId(), new Object[0]))._b();
            final String achievementName = chatSb.finishAndToString();
            chatMessageString = WakfuTranslator.getInstance().getString("quest.completedChatMessage", achievementName);
        }
        else {
            title = WakfuTranslator.getInstance().getString("notification.achievementReward");
            notificationType = NotificationMessageType.ACHIEVEMENTS;
            UIAchievementsFrame.getInstance().onAchievementUnlocked(achievement);
        }
        if (isAchievementTimed(this.m_context, achievement)) {
            this.addAchievementToTimeManager(achievement);
        }
        final TextWidgetFormater f = new TextWidgetFormater();
        final ArrayList<Reward> rewards = achievement.getRewards();
        if (rewards.isEmpty()) {
            return;
        }
        boolean first = true;
        for (final Reward reward : rewards) {
            final RewardType rewardType = reward.getType();
            if (first) {
                first = false;
            }
            else {
                f.append(", ");
            }
            if (rewardType == AchievementRewardType.GIVE_PET_EQUIPMENT || rewardType == AchievementRewardType.GIVE_PLAYER_ITEM) {
                final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(reward.getParameters()[0]);
                if (referenceItem == null) {
                    continue;
                }
                final Item defaultItem = ReferenceItemManager.getInstance().getDefaultItem(referenceItem.getId());
                f.append(UIChatFrame.getItemFormatedForChatLinkString(defaultItem));
            }
            else if (rewardType == AchievementRewardType.GIVE_PLAYER_TITLE) {
                final int titleId = reward.getParameters()[0];
                f.append(WakfuTranslator.getInstance().getString(34, titleId, new Object[0]));
            }
            else if (rewardType == AchievementRewardType.GIVE_PLAYER_EMOTE) {
                final int emoteId = reward.getParameters()[0];
                f.append(WakfuTranslator.getInstance().getString(80, emoteId, new Object[0]));
            }
            else if (rewardType == AchievementRewardType.GIVE_PLAYER_LANDMARK) {
                final int landmarkId = reward.getParameters()[0];
                f.append(WakfuTranslator.getInstance().getString("landmark.gfx"));
            }
            else if (rewardType == AchievementRewardType.GIVE_PLAYER_XP) {
                final int xpGained = reward.getParameters()[0];
                f.append(WakfuTranslator.getInstance().getString("xpGain", xpGained));
            }
            else if (rewardType == AchievementRewardType.GIVE_PLAYER_XP_IN_PERCENT) {
                final int xpGained = reward.getParameters()[0];
                f.append(WakfuTranslator.getInstance().getString("xpPercentGain", xpGained));
            }
            else if (rewardType == AchievementRewardType.GIVE_PLAYER_APTITUDE_POINTS) {
                final byte aptitudeTypeId = (byte)reward.getParameters()[0];
                final int nbPoints = reward.getParameters()[1];
                final AptitudeType aptitudeType = AptitudeType.getFromId(aptitudeTypeId);
                final String key = "aptitude.points.gain." + aptitudeType;
                f.append(WakfuTranslator.getInstance().getString(key, nbPoints));
            }
            else if (rewardType == AchievementRewardType.GIVE_KAMAS) {
                final long kamaQuantity = reward.getParameters()[0];
                f.append(WakfuTranslator.getInstance().getString("kama.shortGain", kamaQuantity));
            }
            else {
                if (rewardType != AchievementRewardType.GIVE_GUILD_POINTS) {
                    continue;
                }
                final int guildPoints = reward.getParameters()[0];
                f.append(WakfuTranslator.getInstance().getString("guild.pointsGain", guildPoints));
            }
        }
        final String message = WakfuTranslator.getInstance().getString("achievement.unlockedChatMessage", f.finishAndToString());
        final TextWidgetFormater notifMessage = new TextWidgetFormater();
        if (isQuest) {
            notifMessage.b().append(WakfuTranslator.getInstance().getString(62, achievement.getId(), new Object[0]))._b().newLine();
        }
        notifMessage.append(message);
        final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, notifMessage.finishAndToString(), notificationType, -1, value);
        Worker.getInstance().pushMessage(uiNotificationMessage);
        if (chatMessageString != null) {
            final ChatMessage chatMessage = new ChatMessage(chatMessageString);
            chatMessage.setPipeDestination(4);
            ChatManager.getInstance().pushMessage(chatMessage);
        }
    }
    
    @Override
    public void onAchievementActivated(final Achievement achievement) {
        final LocalPlayerCharacter localPlayer = HeroesManager.INSTANCE.getHero(this.m_characterId);
        if (!achievement.isVisible(localPlayer)) {
            return;
        }
        final AchievementCategoryView category = AchievementsViewManager.INSTANCE.getCategory(this.m_characterId, achievement.getCategory().getId());
        category.updateAchievementListProperty();
        if (isAchievementTimed(this.m_context, achievement)) {
            this.addAchievementToTimeManager(achievement);
        }
        if (AchievementsViewManager.INSTANCE.isQuest(this.m_characterId, achievement.getId())) {
            if (achievement.getDisplayOnActivationDelay() != -1) {
                this.loadActivatedAchievementDialog(achievement);
            }
            FollowedQuestsManager.INSTANCE.onAchievementActivated(achievement.getId());
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.b().addColor(Color.RED.getRGBtoHex()).append(WakfuTranslator.getInstance().getString(62, achievement.getId(), new Object[0]))._b();
            final String achievementName = sb.finishAndToString();
            final String message = WakfuTranslator.getInstance().getString("quest.activatedChatMessage", achievementName);
            final ChatMessage chatMessage = new ChatMessage(message);
            chatMessage.setPipeDestination(4);
            ChatManager.getInstance().pushMessage(chatMessage);
            PropertiesProvider.getInstance().setPropertyValue("followedQuestsDisplay", true);
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    AchievementUIHelper.displayFollowedAchievements();
                }
            }, 5000L, 1);
            FollowedQuestsManager.INSTANCE.onFollowedListChanged();
            if (localPlayer.hasProperty(WorldPropertyType.ACHIEVEMENT_AUTO_COMPASS) || achievement.isAutoCompass()) {
                final short instanceId = localPlayer.getInstanceId();
                final ArrayList<Objective> objectives = achievement.getObjectives();
                for (int i = 0, size = objectives.size(); i < size; ++i) {
                    final Objective objective = objectives.get(i);
                    if (!objective.isCompleted()) {
                        if (objective.isPositionFeedback()) {
                            if (instanceId == objective.getWorldId()) {
                                AchievementsViewManager.INSTANCE.compassObjective(this.m_characterId, objective.getId());
                                break;
                            }
                        }
                    }
                }
            }
        }
        final AchievementView achievementView = AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, achievement.getId());
        achievementView.updateStatesField();
    }
    
    private void loadActivatedAchievementDialog(final Achievement achievement) {
        final int delay = achievement.getDisplayOnActivationDelay() * 1000;
        if (delay < 0) {
            return;
        }
        final String dialogId = "questActivatedDialog" + achievement.getId();
        final Runnable process = new Runnable() {
            @Override
            public void run() {
                synchronized (LocalPlayerCharacterAchievementsListener.this.m_loadActivatedAchievementDialogList) {
                    if (LocalPlayerCharacterAchievementsListener.this.canDisplayActivatedAchievement()) {
                        LocalPlayerCharacterAchievementsListener.this.doDisplayAchievementActivatedDialog(dialogId, achievement);
                    }
                    else {
                        UINotificationPanelFrame.getInstance().addListener(new UINotificationPanelFrame.NotificationEventListener() {
                            @Override
                            public void onNotificationsEmpty() {
                                LocalPlayerCharacterAchievementsListener.this.doDisplayAchievementActivatedDialog(dialogId, achievement);
                                UINotificationPanelFrame.getInstance().removeListener(this);
                            }
                            
                            @Override
                            public void onNotificationListChanged() {
                                if (LocalPlayerCharacterAchievementsListener.this.canDisplayActivatedAchievement()) {
                                    LocalPlayerCharacterAchievementsListener.this.doDisplayAchievementActivatedDialog(dialogId, achievement);
                                    UINotificationPanelFrame.getInstance().removeListener(this);
                                }
                            }
                        });
                    }
                    LocalPlayerCharacterAchievementsListener.this.m_loadActivatedAchievementDialogList.remove(this);
                }
            }
        };
        synchronized (this.m_loadActivatedAchievementDialogList) {
            this.m_loadActivatedAchievementDialogList.add(process);
            ProcessScheduler.getInstance().schedule(process, delay, 1);
        }
    }
    
    private boolean canDisplayActivatedAchievement() {
        return UINotificationPanelFrame.getInstance().displayListEmpty() || (!UINotificationPanelFrame.getInstance().isDisplayed(NotificationMessageType.QUEST_SUCCESS) && !UINotificationPanelFrame.getInstance().isDisplayed(NotificationMessageType.QUEST_FAILURE));
    }
    
    private void doDisplayAchievementActivatedDialog(final String dialogId, final Achievement achievement) {
        Xulor.getInstance().load(dialogId, Dialogs.getDialogPath("questActivatedDialog"), (short)10000);
        PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, achievement.getId()), dialogId);
    }
    
    @Override
    public void onAchievementReset(final Achievement achievement) {
        if (!achievement.isVisible(HeroesManager.INSTANCE.getHero(this.m_characterId))) {
            return;
        }
        final AchievementCategoryView category = AchievementsViewManager.INSTANCE.getCategory(this.m_characterId, achievement.getCategory().getId());
        category.updateAchievementListProperty();
        if (achievement.isShareable()) {
            final AchievementShareRequestMessage netMsg = new AchievementShareRequestMessage(achievement.getId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        }
        final ArrayList<Objective> objectives = achievement.getObjectives();
        for (int i = 0, size = objectives.size(); i < size; ++i) {
            final AchievementGoalView objective = AchievementsViewManager.INSTANCE.getObjective(this.m_characterId, objectives.get(i).getId());
            objective.updateProgression();
        }
    }
    
    @Override
    public void onVarUpdated(final Variable variable, final long delta) {
        if (variable.isExportForSteam()) {
            final SteamClientContext context = SteamClientContext.INSTANCE;
            if (context.isInit()) {
                final int value = context.getUserStatsHandler().getIntStat(variable.getName());
                context.setStat(variable.getName(), (int)(value + delta));
            }
        }
        final TIntArrayList objectives = this.m_context.getObjectivesByVariable(variable.getId());
        if (objectives == null) {
            LocalPlayerCharacterAchievementsListener.m_logger.warn((Object)("On essaye de mettre \u00e0 jour la variable " + variable.getName() + " alors qu'elle n'est li\u00e9e \u00e0 aucun objectif"));
            return;
        }
        for (int i = 0, size = objectives.size(); i < size; ++i) {
            final AchievementGoalView objective = AchievementsViewManager.INSTANCE.getObjective(this.m_characterId, objectives.get(i));
            objective.updateProgression();
        }
    }
    
    @Override
    public void onAchievementFollowed(final int achievementId, final boolean followed) {
        FollowedQuestsManager.INSTANCE.onAchievementFollowChanged(achievementId);
        if (followed) {
            UIAchievementsFrame.highlightFollowedAchievement(achievementId);
        }
    }
    
    @Override
    public void onInitialize(final ClientAchievementsContext context) {
        QuestActivationManager.INSTANCE.cleanUp();
        AchievementsViewManager.INSTANCE.initialize(this.m_characterId, context);
        QuestTimeManager.INSTANCE.start();
        FollowedQuestsManager.INSTANCE.init();
        final TIntObjectIterator<Achievement> it = context.getAchievementIterator();
        while (it.hasNext()) {
            it.advance();
            final Achievement achievement = it.value();
            if (isAchievementTimed(this.m_context, achievement)) {
                this.addAchievementToTimeManager(achievement);
            }
            if (AchievementsViewManager.INSTANCE.isQuest(this.m_characterId, achievement.getId()) && achievement.isActive() && !achievement.isComplete()) {
                FollowedQuestsManager.INSTANCE.onAchievementActivated(achievement.getId());
            }
        }
        FollowedQuestsManager.INSTANCE.onFollowedListChanged();
    }
    
    private void addAchievementToTimeManager(final Achievement achievement) {
        final AchievementView achievementView = AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, achievement.getId());
        QuestTimeManager.INSTANCE.addAchievement(achievementView);
    }
    
    private static boolean isAchievementTimed(final AchievementsContext context, final AchievementInstance achievement) {
        final boolean inactive = !context.isAchievementActive(achievement.getId());
        if (inactive) {
            return false;
        }
        final boolean periodicQuest = achievement.getPeriodStartDate() != null;
        final boolean runningQuest = !achievement.isComplete() && achievement.getStartTime() != 0L;
        final boolean coolDownQuest = achievement.getLastCompletedTime() != 0L;
        return periodicQuest || runningQuest || coolDownQuest;
    }
    
    @Override
    public void onAchievementFailed(final Achievement achievement) {
        if (!achievement.isVisible(HeroesManager.INSTANCE.getHero(this.m_characterId))) {
            return;
        }
        final AchievementView achievementView = AchievementsViewManager.INSTANCE.getAchievement(this.m_characterId, achievement.getId());
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(WakfuTranslator.getInstance().getString(62, achievement.getId(), new Object[0]));
        sb.append(" ");
        sb.openText().addColor(Color.RED.getRGBtoHex());
        sb.append(WakfuTranslator.getInstance().getString("quest.result.fail"));
        sb.closeText();
        final String title = sb.finishAndToString();
        final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, "", NotificationMessageType.QUEST_FAILURE, -1, achievementView);
        Worker.getInstance().pushMessage(uiNotificationMessage);
        final TextWidgetFormater chatSb = new TextWidgetFormater();
        chatSb.b().addColor(Color.RED.getRGBtoHex()).append(WakfuTranslator.getInstance().getString(62, achievement.getId(), new Object[0]))._b();
        final String achievementName = chatSb.finishAndToString();
        final String chatMessageString = WakfuTranslator.getInstance().getString("quest.failedChatMessage", achievementName);
        final ChatMessage chatMessage = new ChatMessage(chatMessageString);
        chatMessage.setPipeDestination(4);
        ChatManager.getInstance().pushMessage(chatMessage);
        FollowedQuestsManager.INSTANCE.onFollowedListChanged();
        QuestConfigManager.INSTANCE.removeConfig(achievement.getId());
    }
    
    @Override
    public void onAchievementActivationRequired(final Achievement achievement, final long inviterId) {
        QuestActivationManager.INSTANCE.activateQuest(achievement.getId(), inviterId);
    }
    
    @Override
    public void onTryToActivate(final int id) {
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new AchievementTryToActivateRequestMessage(id));
    }
    
    private void setupSoundCleanUp(final int delay) {
        ProcessScheduler.getInstance().remove(this.m_cleanUpSound);
        ProcessScheduler.getInstance().schedule(this.m_cleanUpSound, delay, 1);
    }
    
    public void cleanUp() {
        synchronized (this.m_loadActivatedAchievementDialogList) {
            for (int i = this.m_loadActivatedAchievementDialogList.size() - 1; i >= 0; --i) {
                ProcessScheduler.getInstance().remove(this.m_loadActivatedAchievementDialogList.get(i));
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)LocalPlayerCharacterAchievementsListener.class);
    }
    
    private enum AchievementEventType
    {
        NONE((byte)0), 
        OBJECTIVE_COMPLETE((byte)1), 
        ACHIEVEMENT_COMPLETE((byte)2);
        
        private final byte m_value;
        
        private AchievementEventType(final byte value) {
            this.m_value = value;
        }
        
        public boolean isGreaterThan(final AchievementEventType type) {
            return this.m_value > type.m_value;
        }
    }
}
