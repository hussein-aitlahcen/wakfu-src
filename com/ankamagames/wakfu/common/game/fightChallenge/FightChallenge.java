package com.ankamagames.wakfu.common.game.fightChallenge;

import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;

public interface FightChallenge extends Dropable
{
    public static final short REWARD_XP_STATE_ID = 2214;
    public static final short REWARD_DROP_STATE_ID = 2428;
    
    int getId();
    
    short getDropWeight();
    
    int getGfxId();
    
    SimpleCriterion getCriterion();
    
    int getStateId();
    
    int getListenedEffectSuccessId();
    
    int getListenedEffectFailureId();
    
    ArrayList<FightChallengeReward> getRewards();
    
    boolean isChallengeIncompatible(int p0);
    
    boolean isMonsterIncompatible(int p0);
    
    boolean isBaseChallenge();
}
