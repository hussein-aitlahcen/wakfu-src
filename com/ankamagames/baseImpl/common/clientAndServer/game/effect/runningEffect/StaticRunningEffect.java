package com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;

public interface StaticRunningEffect<FX extends Effect, EC extends EffectContainer>
{
    void setId(int p0);
    
    int getId();
    
    void setRunningEffectStatus(RunningEffectStatus p0);
    
    RunningEffectStatus getRunningEffectStatus();
    
    List<List<EffectUser>> determineTargets(FX p0, Target p1, EffectContext<FX> p2, int p3, int p4, short p5);
    
    EffectExecutionResult run(FX p0, EC p1, EffectContext p2, EffectUser p3, int p4, int p5, short p6, EffectUser p7, EffectExecutionParameters p8);
    
    RunningEffect<FX, EC> newInstance(EffectContext p0, AbstractEffectManager<FX> p1);
    
    BitSet getTriggersToExecute();
}
