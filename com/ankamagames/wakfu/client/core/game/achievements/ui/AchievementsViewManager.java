package com.ankamagames.wakfu.client.core.game.achievements.ui;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import java.util.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import gnu.trove.*;

public class AchievementsViewManager
{
    private static final Logger m_logger;
    public static final AchievementsViewManager INSTANCE;
    private final LongObjectLightWeightMap<AchievementsView> m_achievementsViews;
    private final LongObjectLightWeightMap<QuestsView> m_questsViews;
    private final LongObjectLightWeightMap<TIntObjectHashMap<AchievementGoalView>> m_objectives;
    private final LongObjectLightWeightMap<TIntObjectHashMap<AchievementView>> m_achievements;
    private final LongObjectLightWeightMap<TIntObjectHashMap<AchievementCategoryView>> m_categories;
    private int m_compassedObjectiveId;
    
    private AchievementsViewManager() {
        super();
        this.m_achievementsViews = new LongObjectLightWeightMap<AchievementsView>();
        this.m_questsViews = new LongObjectLightWeightMap<QuestsView>();
        this.m_objectives = new LongObjectLightWeightMap<TIntObjectHashMap<AchievementGoalView>>();
        this.m_achievements = new LongObjectLightWeightMap<TIntObjectHashMap<AchievementView>>();
        this.m_categories = new LongObjectLightWeightMap<TIntObjectHashMap<AchievementCategoryView>>();
    }
    
    public void initialize(final long characterId, final ClientAchievementsContext context) {
        this.m_achievementsViews.put(characterId, new AchievementsView(characterId, context));
        this.m_questsViews.put(characterId, new QuestsView(characterId, context));
    }
    
    private AchievementGoalView createObjectiveView(final long characterId, final Objective objective) {
        TIntObjectHashMap<AchievementGoalView> map = this.m_objectives.get(characterId);
        if (map == null) {
            this.m_objectives.put(characterId, map = new TIntObjectHashMap<AchievementGoalView>());
        }
        final AchievementGoalView objectiveView = new AchievementGoalView(characterId, objective);
        map.put(objective.getId(), objectiveView);
        return objectiveView;
    }
    
    public AchievementGoalView getObjective(final long characterId, final int objectiveId) {
        final TIntObjectHashMap<AchievementGoalView> map = this.m_objectives.get(characterId);
        if (map == null) {
            return null;
        }
        return map.get(objectiveId);
    }
    
    private AchievementView createAchievementView(final long characterId, final int achievementId, final int rootCategoryId, final int gfxId) {
        TIntObjectHashMap<AchievementView> map = this.m_achievements.get(characterId);
        if (map == null) {
            this.m_achievements.put(characterId, map = new TIntObjectHashMap<AchievementView>());
        }
        final AchievementView achievementView = new AchievementView(characterId, achievementId, rootCategoryId, gfxId);
        map.put(achievementId, achievementView);
        final Achievement achievement = AchievementContextManager.INSTANCE.getContext(characterId).getAchievement(achievementId);
        final ArrayList<Objective> objectives = achievement.getObjectives();
        for (int i = 0, size = objectives.size(); i < size; ++i) {
            this.createObjectiveView(characterId, objectives.get(i));
        }
        return achievementView;
    }
    
    public AchievementView getAchievement(final long characterId, final int achievementId) {
        final TIntObjectHashMap<AchievementView> map = this.m_achievements.get(characterId);
        if (map == null) {
            return null;
        }
        final AchievementView achievementView = map.get(achievementId);
        if (achievementView == null) {
            AchievementsViewManager.m_logger.warn((Object)("On essaye de r\u00e9cup\u00e9rer un achievement qui n'existe pas : id=" + achievementId), (Throwable)new Exception(""));
        }
        return achievementView;
    }
    
    AchievementsHistoryCategoryView createHistoryCategoryView(final long characterId) {
        TIntObjectHashMap<AchievementCategoryView> map = this.m_categories.get(characterId);
        if (map == null) {
            this.m_categories.put(characterId, map = new TIntObjectHashMap<AchievementCategoryView>());
        }
        final AchievementsHistoryCategoryView categoryView = new AchievementsHistoryCategoryView(characterId);
        map.put(categoryView.getId(), categoryView);
        return categoryView;
    }
    
    AchievementStandardCategoryView createCategoryView(final long characterId, final Category category, final AchievementStandardCategoryView parentCategory) {
        final int rootCategoryId = (parentCategory == null) ? category.getId() : parentCategory.getRootCategoryId();
        TIntObjectHashMap<AchievementCategoryView> map = this.m_categories.get(characterId);
        if (map == null) {
            this.m_categories.put(characterId, map = new TIntObjectHashMap<AchievementCategoryView>());
        }
        final AchievementStandardCategoryView categoryView = new AchievementStandardCategoryView(characterId, category, rootCategoryId);
        map.put(category.getId(), categoryView);
        final ArrayList<Achievement> achievements = category.getAchievements();
        if (achievements != null) {
            for (final Achievement achievement : achievements) {
                final AchievementView achievementView = this.createAchievementView(characterId, achievement.getId(), categoryView.getRootCategoryId(), achievement.getGfxId());
                categoryView.addAchievement(achievementView);
                if (parentCategory != null) {
                    parentCategory.addAchievement(achievementView);
                }
            }
        }
        final ArrayList<Category> children = category.getChildren();
        if (children != null) {
            for (final Category subCategory : children) {
                final AchievementStandardCategoryView achievementSubCategoryView = this.createCategoryView(characterId, subCategory, categoryView);
                categoryView.addChildren(achievementSubCategoryView);
            }
        }
        return categoryView;
    }
    
    public AchievementCategoryView getCategory(final long characterId, final int categoryId) {
        final TIntObjectHashMap<AchievementCategoryView> map = this.m_categories.get(characterId);
        if (map == null) {
            return null;
        }
        final AchievementCategoryView categoryView = map.get(categoryId);
        if (categoryView == null) {
            AchievementsViewManager.m_logger.warn((Object)("On essaye de r\u00e9cup\u00e9rer une categorie qui n'existe pas : id=" + categoryId), (Throwable)new Exception());
        }
        return categoryView;
    }
    
    public QuestsView getQuestsView(final long characterId) {
        return this.m_questsViews.get(characterId);
    }
    
    public AchievementsView getAchievementsView(final long characterId) {
        return this.m_achievementsViews.get(characterId);
    }
    
    public AsbtractAchievementsView getCorrectAchievementsView(final long characterId, final int achievementId) {
        return this.isQuest(characterId, achievementId) ? ((QuestsView)this.m_questsViews.get(characterId)) : ((AchievementsView)this.m_achievementsViews.get(characterId));
    }
    
    public boolean isQuest(final long characterId, final int achievementId) {
        final TIntObjectHashMap<AchievementView> map = this.m_achievements.get(characterId);
        if (map == null) {
            return false;
        }
        final AchievementView achievementView = map.get(achievementId);
        if (achievementView == null) {
            return false;
        }
        final QuestsView view = this.m_questsViews.get(characterId);
        return view != null && view.getRootCategoryFromId(achievementView.getRootCategoryId()) != null;
    }
    
    public void compassObjective(final long characterId, final int objectiveId) {
        if (this.hasCompassedObjectiveId(objectiveId)) {
            return;
        }
        final int previousObjective = this.m_compassedObjectiveId;
        this.m_compassedObjectiveId = objectiveId;
        final Objective objective = AchievementsModel.INSTANCE.getObjective(this.m_compassedObjectiveId);
        if (objective == null || !objective.isPositionFeedback()) {
            if (MapManager.getInstance().hasCompassPointOfType(7)) {
                MapManager.getInstance().removeCompassPointAndPositionMarker();
            }
            this.m_compassedObjectiveId = -1;
            this.fireCompassChanged(characterId, previousObjective);
            return;
        }
        MapManager.getInstance().addCompassPointAndPositionMarker(7, objective.getX(), objective.getY(), objective.getZ(), objective.getWorldId(), objective, WakfuTranslator.getInstance().getString(64, objectiveId, new Object[0]), true);
        this.fireCompassChanged(characterId, previousObjective);
        this.fireCompassChanged(characterId, this.m_compassedObjectiveId);
    }
    
    private void fireCompassChanged(final long characterId, final int objectiveId) {
        final TIntObjectHashMap<AchievementGoalView> map = this.m_objectives.get(characterId);
        if (map == null) {
            return;
        }
        final AchievementGoalView view = map.get(objectiveId);
        if (view != null) {
            PropertiesProvider.getInstance().firePropertyValueChanged(view, "isCompassed");
        }
    }
    
    public boolean hasCompassedObjectiveId(final int objectiveId) {
        return MapManager.getInstance().hasCompassPointOfType(7) && this.m_compassedObjectiveId == objectiveId;
    }
    
    public void cleanUp() {
        this.m_achievements.clear();
        this.m_categories.clear();
        this.m_objectives.clear();
        this.m_compassedObjectiveId = -1;
        this.m_achievementsViews.clear();
        this.m_questsViews.clear();
    }
    
    public void onPlayerEnterWorld() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final short instanceId = localPlayer.getInstanceId();
        final ClientAchievementsContext achievementsContext = localPlayer.getAchievementsContext();
        final TIntObjectIterator<Achievement> it = achievementsContext.getAchievementIterator();
        while (it.hasNext()) {
            it.advance();
            final Achievement achievement = it.value();
            final int achievementId = achievement.getId();
            final long characterId = localPlayer.getId();
            if (this.isQuest(characterId, achievementId) && achievementsContext.isAchievementActive(achievementId) && achievementsContext.isAchievementRunning(achievementId)) {
                for (final Objective objective : achievement.getObjectives()) {
                    if (!objective.isCompleted() && objective.isPositionFeedback() && instanceId == objective.getWorldId()) {
                        this.compassObjective(characterId, objective.getId());
                    }
                }
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AchievementsViewManager.class);
        INSTANCE = new AchievementsViewManager();
    }
}
