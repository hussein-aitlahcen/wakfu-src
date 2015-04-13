package com.ankamagames.baseImpl.common.clientAndServer.game.effectArea;

import com.ankamagames.framework.ai.targetfinder.*;

public interface EffectAreaActionListener
{
    void onEffectAreaAdded(BasicEffectArea p0);
    
    void onEffectAreaApplication(BasicEffectArea p0, Target p1);
    
    void onEffectAreaUnapplication(BasicEffectArea p0, Target p1);
    
    void onEffectAreaRemoved(BasicEffectArea p0);
    
    void onEffectAreaExecuted(BasicEffectArea p0);
    
    void onEffectAreaPreExecution(BasicEffectArea p0, Target p1);
    
    void onEffectAreaPositionChanged(BasicEffectArea p0);
}
