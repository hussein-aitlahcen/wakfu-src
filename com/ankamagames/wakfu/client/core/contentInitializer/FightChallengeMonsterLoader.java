package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.fightChallenge.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public class FightChallengeMonsterLoader implements ContentInitializer
{
    private static final Logger m_logger;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new FightChallengeMonsterBinaryData(), new LoadProcedure<FightChallengeMonsterBinaryData>() {
            @Override
            public void load(final FightChallengeMonsterBinaryData data) {
                final int id = data.getId();
                final short randomRolls = data.getRandomRolls();
                final short forcedRolls = data.getForcedRolls();
                final int[] forcedChallenges = data.getForcedChallenges();
                final FightChallengeMonsterDefinition definition = new FightChallengeMonsterDefinition(id, randomRolls, forcedRolls);
                for (final int challengeId : forcedChallenges) {
                    final FightChallenge challenge = FightChallengeManager.INSTANCE.getChallenge(challengeId);
                    if (challenge != null) {
                        definition.registerChallenge(challenge);
                    }
                }
                FightChallengeManager.INSTANCE.addMonsterDefinition(definition);
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightChallengeMonsterLoader.class);
    }
}
