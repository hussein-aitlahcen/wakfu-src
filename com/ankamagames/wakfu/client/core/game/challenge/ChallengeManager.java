package com.ankamagames.wakfu.client.core.game.challenge;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage.*;
import gnu.trove.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;

public class ChallengeManager implements ChallengeEventListener
{
    private final TIntObjectHashMap<ChallengeDataModel> m_challengeModels;
    private final TIntObjectHashMap<ChallengeData> m_challengesData;
    private static final Logger m_logger;
    private static final ScenarioBinaryStorable m_scenarioStorableInstance;
    private static final ChallengeManager m_instance;
    
    public ChallengeData loadChallenge(final int challengeId) {
        return this.loadChallenge(challengeId, GameDate.getNullDate());
    }
    
    private ChallengeData loadChallengeFromSerialization(final int challengeId, final GameDateConst startDate) {
        final ChallengeDataModel model = this.getChallengeDataModel(challengeId);
        if (model == null) {
            ChallengeManager.m_logger.error((Object)("Le challenge " + challengeId + " n'existe pas, impossible de le lancer."));
            return null;
        }
        final ChallengeData challengeData = ChallengeData.fromDataModel(model);
        this.m_challengesData.put(challengeId, challengeData);
        challengeData.activateClock(startDate);
        challengeData.setRanking((short)(-1));
        return challengeData;
    }
    
    public ChallengeData loadChallenge(final int challengeId, final GameDateConst startDate) {
        return this.loadChallengeFromSerialization(challengeId, startDate);
    }
    
    private ChallengeManager() {
        super();
        this.m_challengeModels = new TIntObjectHashMap<ChallengeDataModel>();
        this.m_challengesData = new TIntObjectHashMap<ChallengeData>();
    }
    
    public static ChallengeManager getInstance() {
        return ChallengeManager.m_instance;
    }
    
    public ChallengeData getChallengeData(final int challengeId) {
        return this.m_challengesData.get(challengeId);
    }
    
    public void addDataModel(final int id, final ChallengeDataModel model) {
        this.m_challengeModels.put(id, model);
    }
    
    public ChallengeDataModel getChallengeDataModel(final int id) {
        ChallengeDataModel model = this.m_challengeModels.get(id);
        if (model == null) {
            final BinaryStorable bs = SimpleBinaryStorage.getInstance().getById(id, ChallengeManager.m_scenarioStorableInstance);
            if (bs != null) {
                this.addChallengeModelFromBinaryForm((ScenarioBinaryStorable)bs);
            }
            model = this.m_challengeModels.get(id);
        }
        return model;
    }
    
    public ChallengeGoalData getFirstLoadedGoal(final int challengeId) {
        final ChallengeDataModel model = this.getChallengeDataModel(challengeId);
        final ChallengeData data = this.getChallengeData(challengeId);
        if (data == null) {
            return null;
        }
        final ArrayList<ChallengeGoalData> goals = model.getGoals();
        for (int i = 0, size = goals.size(); i < size; ++i) {
            final ChallengeGoalData goal = goals.get(i);
            if (data.getGoalStatus(goal) == 1) {
                return goal;
            }
        }
        return null;
    }
    
    public ChallengeGoalData getLastCompletedGoal(final int challengeId) {
        final ChallengeDataModel model = this.getChallengeDataModel(challengeId);
        final ChallengeData data = this.getChallengeData(challengeId);
        if (data == null) {
            return null;
        }
        final ArrayList<ChallengeGoalData> goals = model.getGoals();
        for (int i = goals.size() - 1; i >= 0; --i) {
            final ChallengeGoalData goal = goals.get(i);
            if (data.getGoalStatus(goal) == 2) {
                return goal;
            }
        }
        return null;
    }
    
    public boolean removeChallenge(final int scenarioId) {
        final ChallengeData data = this.m_challengesData.remove(scenarioId);
        if (data != null) {
            data.setTarget(null);
            ChallengeViewManager.INSTANCE.removeChallenge(scenarioId);
            this.onChallengeChanged(data);
            return true;
        }
        return false;
    }
    
    public void clean() {
        AreaChallengeInformation.getInstance().cleanCurrentChallenge();
        AreaChallengeInformation.getInstance().cleanChallengeInZone();
        this.m_challengesData.clear();
    }
    
    @Override
    public void loadActions(final TIntArrayList actions, final int challengeId) {
        ChallengeData data = this.getChallengeData(challengeId);
        if (data == null) {
            data = this.loadChallenge(challengeId);
            if (data == null) {
                ChallengeManager.m_logger.error((Object)("eRreur lors du chargemetn du challenge " + challengeId));
                return;
            }
        }
        data.activateActions(actions);
        this.onChallengeChanged(data);
    }
    
    @Override
    public boolean updateVars(final byte varId, final long varNewValue, final int challengeId) {
        ChallengeData data = this.getChallengeData(challengeId);
        if (data == null) {
            data = this.loadChallenge(challengeId);
            if (data == null) {
                ChallengeManager.m_logger.error((Object)("eRreur lors du chargemetn du challenge " + challengeId));
                return false;
            }
        }
        final boolean change = data.setVar(varId, varNewValue);
        this.onChallengeChanged(data);
        return change;
    }
    
    @Override
    public void onChallengeChanged(final ChallengeData challengeData) {
        final AbstractChallengeView view = ChallengeViewManager.INSTANCE.getChallengeView(challengeData.getId());
        if (view != null) {
            view.updateProperty();
        }
    }
    
    @Override
    public void onChallengeFinished(final ChallengeData challengeData) {
        final AbstractChallengeView view = ChallengeViewManager.INSTANCE.getChallengeView(challengeData.getId());
        if (view != null) {
            view.updateProperty();
        }
    }
    
    public void completeAction(final int scenarioId, final int actionId) {
        final ChallengeData data = this.getChallengeData(scenarioId);
        if (data != null && !data.isFinished()) {
            data.completeAction(actionId);
        }
        else {
            ChallengeManager.m_logger.error((Object)"On essaie de terminer un but sur un challenge pas charg\u00e9 dans le client");
        }
        this.onChallengeChanged(data);
    }
    
    public void setValidRewards(final int scenarioId, final TIntArrayList validRewards) {
        final ChallengeData data = this.m_challengesData.get(scenarioId);
        data.setValidRewards(validRewards);
        data.setTarget(null);
        data.setSuccess(true);
        if (!data.isFinished()) {
            data.setFinished(true);
        }
        PropertiesProvider.getInstance().setPropertyValue("challengeDetailsVisible", false);
        this.onChallengeChanged(data);
        final CharacterActor characterActor = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
        characterActor.setDirection(Direction8.SOUTH_WEST);
        characterActor.setAnimation("AnimEmote-Victoire");
        final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800029);
        system.setTarget(characterActor);
        IsoParticleSystemManager.getInstance().addParticleSystem(system);
    }
    
    public void proposeChallenge(final int challengeId) {
        ChallengeData data = this.getChallengeData(challengeId);
        if (data == null) {
            data = this.loadChallenge(challengeId);
        }
        if (data == null) {
            return;
        }
        data.setProposed(true);
        this.onChallengeChanged(data);
    }
    
    public boolean fromRawScenarioManager(final RawScenarioManager rawScenarioManager) {
        if (rawScenarioManager.currentScenarii != null) {
            ChallengeManager.m_logger.error((Object)"Le client ne devrait pas recevoir les infos compl\u00e8tes de scenarii.");
            return false;
        }
        if (rawScenarioManager.currentChallengeInfo == null) {
            ChallengeManager.m_logger.error((Object)"Pas d'infos de challenges \u00e0 d\u00e9s\u00e9rialiser");
            return false;
        }
        boolean success = true;
        for (final RawScenarioManager.CurrentChallengeInfo.ChallengeInfo rawChallengeContent : rawScenarioManager.currentChallengeInfo.challenges) {
            try {
                final RawChallengeInfo rawChallenge = rawChallengeContent.challenge;
                final int challengeId = rawChallenge.scenarioId;
                final GameDateConst startDate = (rawChallenge.remainingTime != null) ? GameDate.fromLong(rawChallenge.remainingTime.value) : GameDate.getNullDate();
                final ChallengeData data = this.loadChallengeFromSerialization(challengeId, startDate);
                final TIntArrayList actions = new TIntArrayList(rawChallenge.activeGoals.size());
                for (final RawChallengeInfo.ActiveGoal activeGoal : rawChallenge.activeGoals) {
                    actions.add(activeGoal.actionGroupId);
                }
                data.activateActions(actions);
                for (final RawChallengeInfo.ExecutedGoal executedGoal : rawChallenge.executedGoals) {
                    data.completeAction(executedGoal.actionGroupId);
                }
                for (final RawChallengeInfo.GlobalVar globalVar : rawChallenge.globalVars) {
                    data.setVar(globalVar.varId, globalVar.value);
                }
            }
            catch (Exception e) {
                ChallengeManager.m_logger.error((Object)("Exception lors de la d\u00e9s\u00e9rialisation du sc\u00e9narion id=" + rawChallengeContent.challenge.scenarioId), (Throwable)e);
                success = false;
            }
        }
        return success;
    }
    
    public void addChallengeModelFromBinaryForm(final ScenarioBinaryStorable bs) {
        if (!bs.isChallenge()) {
            return;
        }
        final int scenarId = bs.getId();
        final byte scenarType = bs.getType();
        final byte userType = bs.getUserType();
        final boolean chaos = bs.isChaos();
        final short duration = (short)(bs.isChaos() ? 0 : bs.getDuration());
        final GameDateConst expiryDate = bs.getExpirationDate();
        final boolean isAuto = bs.isAutoTrigger();
        final short minUsers = bs.getMinUsers();
        final short maxUsers = bs.getMaxUsers();
        final String joinCriterion = bs.getJoinCriterion();
        final String rewardEligibilityCriterion = bs.getRewardEligibilityCriterion();
        final String params = bs.getParams();
        final ChallengeDataModel model = new ChallengeDataModel(scenarId, scenarType, userType, duration, isAuto, expiryDate, minUsers, maxUsers, joinCriterion, rewardEligibilityCriterion, chaos, params);
        final String[] eventVars = bs.getVarMapping();
        for (int i = 0; i < eventVars.length; ++i) {
            String var = eventVars[i];
            if (var.startsWith("'")) {
                var = var.substring(1, var.length() - 1);
            }
            model.addVar((byte)i, var);
        }
        for (final ScenarioBinaryStorable.ActionGroupStorable group : bs.getActionGroups()) {
            if (!group.isChallengeGoal()) {
                continue;
            }
            final int actionGroupId = group.getId();
            final String targetPosition = group.getTargetPosition();
            Point3 posValue = null;
            if (!targetPosition.equalsIgnoreCase("null")) {
                PositionValue value = null;
                try {
                    value = CriteriaCompiler.compilePosition(targetPosition);
                }
                catch (Exception e) {
                    ChallengeManager.m_logger.error((Object)("Erreur lors de la compilation des crit\u00e8res d'un sc\u00e9nario (sc\u00e9nario " + scenarId + " actionGroupId : " + actionGroupId + ")"), (Throwable)e);
                }
                if (value != null) {
                    posValue = PositionValue.fromLong(value.getLongValue(null, null, null, null));
                }
            }
            final ChallengeGoalData goalData = new ChallengeGoalData(actionGroupId, scenarId, posValue, group.getJaugeVarName(), group.getJaugeMaxValue(), group.isCountDownJauge());
            model.addAction(goalData);
        }
        for (final ScenarioBinaryStorable.RewardStorable reward : bs.getRewards()) {
            final int rewardId = reward.getId();
            final int gfxId = reward.getGfx();
            final String critText = null;
            final boolean success = reward.isSuccess();
            final int itemId = reward.getItemId();
            final short quantity = reward.getItemQty();
            final int xp = reward.getXp();
            final int kama = reward.getKama();
            final byte order = reward.getOrder();
            if (success) {
                model.addReward(rewardId, gfxId, itemId, quantity, xp, kama, critText, order);
            }
            else {
                model.addFailure(rewardId, gfxId, itemId, quantity, xp, kama, critText, order);
            }
        }
        this.addDataModel(scenarId, model);
    }
    
    @Override
    public String toString() {
        final ChallengeData challengeInZone = AreaChallengeInformation.getInstance().getChallengeInZone();
        final String result = "";
        return result;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ChallengeManager.class);
        m_scenarioStorableInstance = new ScenarioBinaryStorable();
        m_instance = new ChallengeManager();
    }
}
