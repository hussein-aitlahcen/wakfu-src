package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.google.common.collect.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;

public class AchievementsLoader implements ContentInitializer
{
    protected static final Logger m_logger;
    public static final AchievementsLoader INSTANCE;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        this.loadVariables();
        this.loadCategories();
        this.loadAchievements();
        this.loadAchievementLists();
        clientInstance.fireContentInitializerDone(this);
    }
    
    private void loadVariables() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new AchievementVariableBinaryData(), new LoadProcedure<AchievementVariableBinaryData>() {
            @Override
            public void load(final AchievementVariableBinaryData data) {
                AchievementsModel.INSTANCE.registerVariable(data.getId(), data.getName(), data.isExportForSteam());
            }
        });
    }
    
    private void loadCategories() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new AchievementCategoryBinaryData(), new LoadProcedure<AchievementCategoryBinaryData>() {
            @Override
            public void load(final AchievementCategoryBinaryData bs) {
                AchievementsModel.INSTANCE.registerCategory(bs.getId(), bs.getParentId(), null);
            }
        });
    }
    
    private void loadAchievements() throws Exception {
        final TIntObjectHashMap<Multimap<Byte, Integer>> mercenaryMap = new TIntObjectHashMap<Multimap<Byte, Integer>>();
        BinaryDocumentManager.getInstance().foreach(new AchievementBinaryData(), new LoadProcedure<AchievementBinaryData>() {
            @Override
            public void load(final AchievementBinaryData bs) {
                if (bs.isMercenary()) {
                    Multimap<Byte, Integer> rankMap = (Multimap<Byte, Integer>)mercenaryMap.get(bs.getCategoryId());
                    if (rankMap == null) {
                        mercenaryMap.put(bs.getCategoryId(), rankMap = (Multimap<Byte, Integer>)ArrayListMultimap.create());
                    }
                    rankMap.put((Object)bs.getMercenaryRank(), (Object)bs.getId());
                }
            }
        });
        BinaryDocumentManager.getInstance().foreach(new AchievementBinaryData(), new LoadProcedure<AchievementBinaryData>() {
            @Override
            public void load(final AchievementBinaryData bs) {
                final int achievementId = bs.getId();
                final boolean isVisible = bs.isVisible();
                final String unlockingCriterionAsString = bs.getCriterion();
                SimpleCriterion unlockingCriterion;
                try {
                    unlockingCriterion = CriteriaCompiler.compileBoolean(unlockingCriterionAsString);
                }
                catch (Exception e) {
                    unlockingCriterion = null;
                    AchievementsLoader.m_logger.error((Object)"", (Throwable)e);
                }
                final String activationCriterionAsString = AchievementsLoader.this.createCriterion(bs, mercenaryMap);
                SimpleCriterion activationCriterion;
                try {
                    activationCriterion = CriteriaCompiler.compileBoolean(activationCriterionAsString);
                }
                catch (Exception e2) {
                    activationCriterion = null;
                    AchievementsLoader.m_logger.error((Object)"", (Throwable)e2);
                }
                GameDateConst periodStartDate = null;
                GameIntervalConst period = null;
                if (bs.getPeriodStartTime() != 0L) {
                    periodStartDate = GameDate.fromLong(bs.getPeriodStartTime());
                    period = GameInterval.fromSeconds(bs.getPeriod());
                }
                final int displayOnActivationDelay = bs.isNeedsUserAccept() ? -1 : bs.getDisplayOnActivationDelay();
                final boolean ok = AchievementsModel.INSTANCE.registerAchievement(achievementId, bs.getCategoryId(), isVisible, bs.isNotifyOnPass(), null, null, unlockingCriterion, bs.getDuration() * 1000, bs.getCooldown() * 1000L, bs.isShareable(), bs.isRepeatable(), bs.isNeedsUserAccept(), bs.getRecommandedLevel(), bs.getRecommandedPlayers(), bs.isFollowable(), displayOnActivationDelay, periodStartDate, period, bs.isActive(), bs.isAutoCompass(), bs.getGfxId(), bs.getMercenaryRank(), activationCriterion, bs.getOrder());
                if (!ok) {
                    AchievementsLoader.m_logger.error((Object)"[STOP] Erreur durant l'enregistrement d'un achievement");
                }
                this.loadGoals(bs, achievementId);
                this.loadRewards(bs, achievementId);
            }
            
            private void loadGoals(final AchievementBinaryData bs, final int achievementId) {
                for (final AchievementBinaryData.AchievementGoal goal : bs.getGoals()) {
                    final boolean feedback = goal.isFeedback();
                    final AchievementBinaryData.AchievementVariableListener[] listeners = goal.getVlisteners();
                    final TIntArrayList variables = new TIntArrayList(listeners.length);
                    for (final AchievementBinaryData.AchievementVariableListener listener : listeners) {
                        variables.add(listener.getVariableIds());
                    }
                    final boolean hasPositionFeedback = goal.hasPositionFeedback();
                    final short x = goal.getPositionX();
                    final short y = goal.getPositionY();
                    final short z = goal.getPositionZ();
                    final short worldId = goal.getPositionWorldId();
                    if (!AchievementsModel.INSTANCE.registerObjective(goal.getId(), achievementId, (int[])(variables.isEmpty() ? null : variables.toNativeArray()), null, null, feedback, hasPositionFeedback, x, y, z, worldId)) {
                        AchievementsLoader.m_logger.error((Object)"[STOP] Erreur durant l'enregistrement d'un objectif");
                    }
                }
            }
            
            private void loadRewards(final AchievementBinaryData bs, final int achievementId) {
                for (final AchievementBinaryData.AchievementReward reward : bs.getRewards()) {
                    final AchievementRewardType type = AchievementRewardType.fromId(reward.getType());
                    if (!AchievementsModel.INSTANCE.registerReward(reward.getId(), achievementId, type, reward.getParams())) {
                        AchievementsLoader.m_logger.error((Object)"[STOP] Erreur durant l'enregistrement d'une r\u00e9compense d'achievement");
                    }
                }
            }
        });
    }
    
    private void loadAchievementLists() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new AchievementListBinaryData(), new LoadProcedure<AchievementListBinaryData>() {
            @Override
            public void load(final AchievementListBinaryData bs) {
                final int id = bs.getId();
                final AchievementListBinaryData.AchievementListData[] srcElements = bs.getElements();
                final AchievementListBinaryData.AchievementListData[] elements = new AchievementListBinaryData.AchievementListData[srcElements.length];
                System.arraycopy(srcElements, 0, elements, 0, srcElements.length);
                Arrays.sort(elements, AchievementListDataComparator.COMPARATOR);
                final int[] achievementIds = new int[elements.length];
                for (int i = 0, size = elements.length; i < size; ++i) {
                    achievementIds[i] = elements[i].getAchievementId();
                }
                final AchievementList achievementList = new AchievementList(id);
                achievementList.addElements(achievementIds);
                AchievementListManager.INSTANCE.addList(achievementList);
            }
        });
    }
    
    private String createCriterion(final AchievementBinaryData bs, final TIntObjectHashMap<Multimap<Byte, Integer>> mercenaryMap) {
        final String activationCriterion = bs.getActivationCriterion();
        final boolean isMercenary = bs.isMercenary();
        if (!isMercenary) {
            return activationCriterion;
        }
        final byte mercenaryRank = bs.getMercenaryRank();
        final int mercenaryItemId = bs.getMercenaryItemId();
        final Multimap<Byte, Integer> map = mercenaryMap.get(bs.getCategoryId());
        if (map == null) {
            return activationCriterion;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("(GetLevel() >= ").append(bs.getRecommandedLevel());
        if (mercenaryItemId > 0) {
            sb.append(" && GetItemQuantity(").append(mercenaryItemId).append(") > 0");
        }
        for (final Byte rank : map.keySet()) {
            if (rank < mercenaryRank) {
                for (final Integer achievementId : map.get((Object)rank)) {
                    sb.append(" && IsAchievementComplete(").append(achievementId).append(')');
                }
            }
        }
        sb.append(')');
        if (!StringUtils.isEmptyOrNull(activationCriterion)) {
            sb.append(" && (");
            sb.append(activationCriterion);
            sb.append(')');
        }
        return sb.toString();
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.achievements");
    }
    
    static {
        m_logger = Logger.getLogger((Class)AchievementsLoader.class);
        INSTANCE = new AchievementsLoader();
    }
    
    static class AchievementListDataComparator implements Comparator<AchievementListBinaryData.AchievementListData>
    {
        static final AchievementListDataComparator COMPARATOR;
        
        @Override
        public int compare(final AchievementListBinaryData.AchievementListData o1, final AchievementListBinaryData.AchievementListData o2) {
            return o1.getOrder() - o2.getOrder();
        }
        
        static {
            COMPARATOR = new AchievementListDataComparator();
        }
    }
}
