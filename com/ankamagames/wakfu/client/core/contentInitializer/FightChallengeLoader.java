package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.common.game.fightChallenge.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public class FightChallengeLoader implements ContentInitializer
{
    private static final Logger m_logger;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new FightChallengeBinaryData(), new LoadProcedure<FightChallengeBinaryData>() {
            @Override
            public void load(final FightChallengeBinaryData data) {
                final int id = data.getId();
                final short dropWeight = (short)data.getDropWeight();
                SimpleCriterion criterion;
                try {
                    criterion = CriteriaCompiler.compileBoolean(data.getDropCriterion());
                }
                catch (Exception e) {
                    FightChallengeLoader.m_logger.error((Object)("Probl\u00e8me \u00e0 la compilation d'un crit\u00e8re dans le challenge d'id " + id + " : " + data.getDropCriterion()));
                    criterion = null;
                }
                if (criterion == null) {
                    criterion = ConstantBooleanCriterion.TRUE;
                }
                final int stateId = data.getStateId();
                final int listenedEffectSuccessId = data.getListenedEffectSuccess();
                final int listenedEffectFailureId = data.getListenedEffectFailure();
                final int gfxId = data.getGfxId();
                final boolean base = data.isBase();
                final FightChallengeDefinition definition = new FightChallengeDefinition(id, dropWeight, criterion, stateId, listenedEffectSuccessId, listenedEffectFailureId, gfxId, base);
                definition.addAllIncompatibleChallenges(data.getIncompatibleChallenges());
                definition.addAllIncompatibleMonsters(data.getIncompatibleMonsters());
                for (final FightChallengeBinaryData.Reward reward : data.getRewards()) {
                    final int rewardId = reward.getId();
                    SimpleCriterion rewardCriterion;
                    try {
                        rewardCriterion = CriteriaCompiler.compileBoolean(reward.getCriterion());
                    }
                    catch (Exception e2) {
                        FightChallengeLoader.m_logger.error((Object)("Probl\u00e8me \u00e0 la compilation d'un crit\u00e8re dans le challenge d'id " + id + " : " + data.getDropCriterion()));
                        rewardCriterion = null;
                    }
                    if (rewardCriterion == null) {
                        rewardCriterion = ConstantBooleanCriterion.TRUE;
                    }
                    final short xpLevel = reward.getXpLevel();
                    final short dropLevel = reward.getDropLevel();
                    definition.addReward(rewardId, rewardCriterion, xpLevel, dropLevel);
                }
                FightChallengeManager.INSTANCE.registerChallenge(definition);
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightChallengeLoader.class);
    }
}
