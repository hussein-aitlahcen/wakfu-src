package com.ankamagames.wakfu.common.game.fight.time.timescore;

import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.fight.time.buff.*;
import org.jetbrains.annotations.*;

public interface TimeScoreGauges
{
    int getTimePointGap();
    
    int getTimeScore(long p0);
    
    boolean isFighterAllowedToSelectBonus(long p0);
    
    List<WakfuEffect> getEffectsAvailableForSelection(long p0, int p1);
    
    void setTimePointGap(int p0);
    
    void updateTimeScore(long p0, int p1);
    
    boolean selectEffect(long p0, int p1, int p2);
    
    void executeTimePointEffects(BasicCharacterInfo p0, EffectContext p1);
    
    TIntObjectHashMap<TimelineBuffList<WakfuEffect>> getBuffListsByTeamId();
    
    WakfuEffect getEffectToBeAppliedForPlayer(long p0);
    
    boolean hasEffectToBeExecuted(long p0);
    
    int addTurnRemainingSeconds(long p0, int p1);
    
    int getAverageRemainingSecondsByTurn(long p0);
    
    void cancelFighterChoosePermission(long p0);
    
    void resetRemainingSeconds(long p0);
    
    List<WakfuEffect> getTeamEffects(int p0);
    
    @Nullable
    WakfuEffect getEffectById(int p0);
    
    void setTeamEffects(int p0, List<WakfuEffect> p1);
    
    void removeEffectToBeApplied(long p0);
    
    int size();
    
    byte[] serialize(int p0);
    
    void unserialize(byte[] p0);
}
