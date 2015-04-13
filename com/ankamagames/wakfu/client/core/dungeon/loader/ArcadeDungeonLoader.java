package com.ankamagames.wakfu.client.core.dungeon.loader;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.game.dungeon.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.*;

public class ArcadeDungeonLoader implements ContentInitializer
{
    private static final Logger m_logger;
    public static final ArcadeDungeonLoader INSTANCE;
    private TIntIntHashMap m_worldToDungeonId;
    
    public ArcadeDungeonLoader() {
        super();
        this.m_worldToDungeonId = new TIntIntHashMap();
    }
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new ArcadeDungeonBinaryData(), new LoadProcedure<ArcadeDungeonBinaryData>() {
            @Override
            public void load(final ArcadeDungeonBinaryData bs) {
                DungeonLadderDefinitionManager.INSTANCE.add(new DungeonLadderDefinition(bs.getWorldId(), DungeonLadderType.SURVIVAL, (short)32767));
                ArcadeDungeonLoader.this.m_worldToDungeonId.put(bs.getWorldId(), bs.getId());
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return null;
    }
    
    public DungeonDefinition getDungeonFromWorld(final int worldId) {
        return this.getDungeon(this.m_worldToDungeonId.get(worldId));
    }
    
    public DungeonDefinition getDungeon(final int dungeonId) {
        final DungeonDefinition dungeon = new DungeonDefinition(dungeonId);
        try {
            BinaryDocumentManager.getInstance().forId(dungeonId, new ArcadeDungeonBinaryData(), new LoadProcedure<ArcadeDungeonBinaryData>() {
                @Override
                public void load(final ArcadeDungeonBinaryData data) throws Exception {
                    dungeon.setRoundScore(data.getScoreRoundBase(), data.getScoreRoundIncr());
                    for (final ArcadeDungeonBinaryData.Challenge challenge : data.getChallenges()) {
                        dungeon.addChallenge(ArcadeDungeonLoader.this.getChallenge(challenge.getId(), challenge.getRatio()));
                    }
                    for (final ArcadeDungeonBinaryData.RankDef rank : data.getRanks()) {
                        dungeon.addRank(rank, ArrayUtils.isEmpty(data.getChallenges()));
                    }
                    for (final ArcadeDungeonBinaryData.RewardList rewardList : data.getRewardsList()) {
                        dungeon.addRewards(rewardList);
                    }
                }
            });
        }
        catch (Exception e) {
            ArcadeDungeonLoader.m_logger.error((Object)("Probl\u00e8me avec le donjon " + dungeonId), (Throwable)e);
            return null;
        }
        return dungeon;
    }
    
    private ChallengeDefinition getChallenge(final int challengeId, final float ratio) throws Exception {
        final ArcadeChallengeBinaryData data = new ArcadeChallengeBinaryData();
        if (BinaryDocumentManager.getInstance().getId(challengeId, data)) {
            return new ChallengeDefinition(data.getId(), ratio);
        }
        throw new Exception("Pas de d\u00e9fi avec id=" + challengeId);
    }
    
    public String getEventName(final int eventId) {
        return WakfuTranslator.getInstance().getString(113, eventId, new Object[0]);
    }
    
    public String getEventDesc(final int eventId) {
        return WakfuTranslator.getInstance().getString(118, eventId, new Object[0]);
    }
    
    public String getBonusName(final int bonusId) {
        return WakfuTranslator.getInstance().getString(112, bonusId, new Object[0]);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ArcadeDungeonLoader.class);
        INSTANCE = new ArcadeDungeonLoader();
    }
}
