package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import com.ankamagames.framework.ai.targetfinder.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

public interface Effect
{
    boolean isSelfTrigger();
    
    boolean isGlobalTriggerListener();
    
    boolean preCalculateTriggerTarget();
    
    boolean triggerTargetIsSelf();
    
    boolean mustStoreOnCaster();
    
    boolean dontTriggerAnything();
    
    boolean alwaysTrigger();
    
    byte getEffectType();
    
    int getEffectId();
    
    int getActionId();
    
    boolean checkFlags(long p0);
    
    TargetValidator<EffectUser> getTargetValidator();
    
    BitSet getListeningTriggersBeforeComputation();
    
    BitSet getListeningTriggersBeforeExecution();
    
    BitSet getListeningTriggerForUnapplication();
    
    BitSet getListeningTriggersAfterExecution();
    
    BitSet getListeningTriggerAfterAllExecutions();
    
    BitSet getListeningTriggerNotRelatedToExecutions();
    
    boolean mustBeTriggered();
    
    BitSet getExecutionTriggersAdditionnal();
    
    EffectExecutionResult execute(EffectContainer p0, EffectUser p1, EffectContext p2, Constants<? extends StaticRunningEffect> p3, int p4, int p5, short p6, EffectUser p7, EffectExecutionParameters p8, boolean p9);
    
    AreaOfEffect getAreaOfEffect();
    
    boolean isAffectedByLocalisation();
    
    long getFlags();
    
    short getMaximumExecutions();
    
    void setMaximumExecutions(short p0);
    
    boolean isDurationInCasterTurn();
    
    boolean isDurationInTargetTurn();
    
    TriggerTargetType getTriggerTargetType();
    
    TriggerCasterType getTriggerCasterType();
    
    boolean isShouldRecomputeTarget();
}
