package com.ankamagames.wakfu.common.datas;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public interface CriterionUser extends Target
{
    Breed getBreed();
    
    byte getTeamId();
    
    Point3 getPosition();
    
    long getId();
    
    long getOriginalControllerId();
    
    boolean isSummoned();
    
    boolean isSummonedFromSymbiot();
    
    int getCharacteristicValue(CharacteristicType p0) throws UnsupportedOperationException;
    
    int getCharacteristicMax(CharacteristicType p0);
    
    boolean hasCharacteristic(CharacteristicType p0);
    
    boolean hasProperty(PropertyType p0);
    
    boolean hasState(long p0);
    
    int getStateLevel(long p0);
    
    boolean hasState(long p0, long p1);
    
    boolean hasStateFromLevel(long p0, long p1);
    
    boolean is(CriterionUserType p0);
    
    int getSummoningsCount();
    
    int getSummoningsCount(int p0);
    
    RunningEffectManager getRunningEffectManager();
    
    boolean isOffPlay();
    
    boolean hasStateFromUser(long p0, CriterionUser p1);
    
    boolean hasStateFromUser(long p0, long p1, CriterionUser p2);
    
    boolean isInPlay();
    
    boolean isOutOfPlay();
    
    boolean isOnFight();
}
