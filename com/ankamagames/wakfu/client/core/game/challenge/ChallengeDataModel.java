package com.ankamagames.wakfu.client.core.game.challenge;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.challenge.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class ChallengeDataModel
{
    private static final Logger m_logger;
    private static final ArrayList<ObjectPair<String, String>> EMPTY;
    private int m_id;
    private final ArrayList<ChallengeGoalData> m_goals;
    private final HashMap<String, Byte> m_authorizedVarsId;
    private final ArrayList<ChallengeRewardModel> m_rewards;
    private ArrayList<ChallengeRewardModel> m_failures;
    private short m_duration;
    private short m_minUsers;
    private short m_maxUsers;
    private int m_zoneId;
    private GameDateConst m_expiryDate;
    private ChallengeCategory m_category;
    private ChallengeUsersType m_userType;
    private boolean m_isAuto;
    private byte m_state;
    private final SimpleCriterion m_joinCriterion;
    private final SimpleCriterion m_rewardEligibilityCriterion;
    private final boolean m_chaos;
    private final ArrayList<ObjectPair<String, String>> m_params;
    
    public ChallengeDataModel(final int id, final byte categoryId, final byte userTypeId, final short duration, final boolean isAuto, final GameDateConst expiryDate, final short minUsers, final short maxUsers, final String joinCriterion, final String rewardEligibilityCriterion, final boolean chaos, final String params) {
        super();
        this.m_goals = new ArrayList<ChallengeGoalData>();
        this.m_authorizedVarsId = new HashMap<String, Byte>();
        this.m_rewards = new ArrayList<ChallengeRewardModel>();
        this.m_failures = null;
        this.m_minUsers = 0;
        this.m_maxUsers = -1;
        this.m_state = 0;
        this.m_id = id;
        this.m_category = ChallengeCategory.fromId(categoryId);
        this.m_userType = ChallengeUsersType.fromId(userTypeId);
        this.m_duration = duration;
        this.m_isAuto = isAuto;
        this.m_expiryDate = expiryDate;
        this.m_minUsers = minUsers;
        this.m_maxUsers = minUsers;
        this.m_chaos = chaos;
        this.m_joinCriterion = this.createCriterion(joinCriterion);
        this.m_rewardEligibilityCriterion = this.createCriterion(rewardEligibilityCriterion);
        this.m_params = ((params == null) ? ChallengeDataModel.EMPTY : ChallengeFormatter.extractInstanceParam(params));
    }
    
    private SimpleCriterion createCriterion(final String criterion) {
        SimpleCriterion crit = null;
        if (criterion != null && criterion.length() != 0) {
            try {
                crit = CriteriaCompiler.compileBoolean(criterion);
            }
            catch (Exception e) {
                ChallengeDataModel.m_logger.warn((Object)("Impossible de charger le criterion " + criterion + " pour le challenge " + this.m_id));
            }
        }
        return crit;
    }
    
    public byte getVarId(final String name) {
        final Byte aByte = this.m_authorizedVarsId.get(name);
        if (aByte != null) {
            return aByte;
        }
        return 0;
    }
    
    public boolean isChaos() {
        return this.m_chaos;
    }
    
    public String getRewardDescription() {
        return "";
    }
    
    public void addVar(final byte id, final String name) {
        this.m_authorizedVarsId.put(name, id);
    }
    
    public void addAction(final ChallengeGoalData group) {
        this.m_goals.add(group);
    }
    
    public String getChallengeTitle() {
        return WakfuTranslator.getInstance().getString(26, this.m_id, new Object[0]);
    }
    
    public String getChallengeDescription() {
        return WakfuTranslator.getInstance().getString(28, this.m_id, new Object[0]);
    }
    
    public String getChallengeLongDescription() {
        return WakfuTranslator.getInstance().getString(24, this.m_id, new Object[0]);
    }
    
    public String getChallengeRequirementsText() {
        return WakfuTranslator.getInstance().getString(29, this.m_id, new Object[0]);
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public ChallengeCategory getCategory() {
        return this.m_category;
    }
    
    public ChallengeUsersType getUserType() {
        return this.m_userType;
    }
    
    public short getDuration() {
        return this.m_duration;
    }
    
    public void addReward(final int rewardId, final int gfxId, final int itemId, final short quantity, final int xp, final int kama, final String criterionCommentary, final byte order) {
        this.m_rewards.add(new ChallengeRewardModel(rewardId, gfxId, itemId, quantity, xp, kama, criterionCommentary, true, order));
        Collections.sort(this.m_rewards);
    }
    
    public void addFailure(final int rewardId, final int gfxId, final int itemId, final short quantity, final int xp, final int kama, final String criterionCommentary, final byte order) {
        if (this.m_failures == null) {
            this.m_failures = new ArrayList<ChallengeRewardModel>();
        }
        this.m_failures.add(new ChallengeRewardModel(rewardId, gfxId, itemId, quantity, xp, kama, criterionCommentary, false, order));
    }
    
    public ArrayList<ChallengeRewardModel> getRewards() {
        return this.m_rewards;
    }
    
    public ArrayList<ChallengeRewardModel> getFailures() {
        return this.m_failures;
    }
    
    public void setState(final byte state) {
        this.m_state = state;
    }
    
    public byte getState() {
        return this.m_state;
    }
    
    @Nullable
    public GameDateConst getExpiryDate() {
        return this.m_expiryDate;
    }
    
    public short getMinUsers() {
        return this.m_minUsers;
    }
    
    public short getMaxUsers() {
        return this.m_maxUsers;
    }
    
    public boolean isAuto() {
        return this.m_isAuto;
    }
    
    public ArrayList<ChallengeGoalData> getGoals() {
        return this.m_goals;
    }
    
    public ChallengeGoalData getGoal(final int id) {
        for (int i = 0, size = this.m_goals.size(); i < size; ++i) {
            final ChallengeGoalData challengeGoalData = this.m_goals.get(i);
            if (challengeGoalData.getId() == id) {
                return challengeGoalData;
            }
        }
        return null;
    }
    
    public SimpleCriterion getJoinCriterion() {
        return this.m_joinCriterion;
    }
    
    public SimpleCriterion getRewardEligibilityCriterion() {
        return this.m_rewardEligibilityCriterion;
    }
    
    public ArrayList<ObjectPair<String, String>> getParams() {
        return this.m_params;
    }
    
    public String getIconUrl() {
        try {
            return String.format(WakfuConfiguration.getInstance().getString("challengeCategoryIconsPath"), this.m_category.getId());
        }
        catch (PropertyException e) {
            ChallengeDataModel.m_logger.error((Object)"Exception", (Throwable)e);
            return null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChallengeDataModel.class);
        EMPTY = new ArrayList<ObjectPair<String, String>>();
    }
}
