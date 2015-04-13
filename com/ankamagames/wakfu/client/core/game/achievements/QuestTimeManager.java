package com.ankamagames.wakfu.client.core.game.achievements;

import java.util.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.wakfu.client.core.game.time.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;

public class QuestTimeManager implements TimeTickListener
{
    public static final QuestTimeManager INSTANCE;
    private final ArrayList<AchievementView> m_achievements;
    
    public QuestTimeManager() {
        super();
        this.m_achievements = new ArrayList<AchievementView>();
    }
    
    public void addAchievement(final AchievementView view) {
        synchronized (this.m_achievements) {
            this.m_achievements.add(view);
        }
    }
    
    public void start() {
        TimeManager.INSTANCE.addListener(this);
    }
    
    public void stop() {
        TimeManager.INSTANCE.removeListener(this);
    }
    
    @Override
    public void tick() {
        synchronized (this.m_achievements) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer == null) {
                return;
            }
            final ClientAchievementsContext achievementsContext = localPlayer.getAchievementsContext();
            final GameDateConst now = WakfuGameCalendar.getInstance().getDate();
            for (int i = this.m_achievements.size() - 1; i >= 0; --i) {
                final AchievementView achievementView = this.m_achievements.get(i);
                achievementView.updateTimeDisplay();
                final Achievement achievement = achievementsContext.getAchievement(achievementView.getId());
                if (achievement.getPeriodStartDate() == null && !AchievementHelper.isAchievementInCooldown(achievement, now) && !AchievementHelper.isAchievementRunning(achievement, now)) {
                    this.m_achievements.remove(i);
                }
            }
        }
        FollowedQuestsManager.INSTANCE.onTimeTick();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("QuestTimeManager");
        sb.append("{m_achievements=").append(this.m_achievements);
        sb.append('}');
        return sb.toString();
    }
    
    static {
        INSTANCE = new QuestTimeManager();
    }
}
