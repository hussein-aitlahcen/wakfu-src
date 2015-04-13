package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public interface WakfuEffect extends Effect
{
    public static final byte HMI_ACTION_START_ON_APPLICATION = 0;
    public static final byte HMI_ACTION_START_ON_EXECUTION = 1;
    public static final byte HMI_ACTION_START_ON_UNAPPLICATION = 2;
    public static final byte HMI_ACTION_END_IMMEDIATELY = 10;
    public static final byte HMI_ACTION_END_ON_EXECUTION = 11;
    public static final byte HMI_ACTION_END_ON_UNAPPLICATION = 12;
    public static final byte STANDARD_EFFECT = 0;
    public static final byte WORLD_EFFECT = 1;
    public static final byte FIGHT_EFFECT = 2;
    
    byte getMaxTargetsCount();
    
    float[] getRawParams();
    
    int getParamsCount();
    
    int getParam(int p0, short p1, RoundingMethod p2);
    
    float getParam(int p0, short p1);
    
    float getParam(int p0);
    
    boolean recomputeAreaOfEffectDisplay();
    
    boolean notifyInChat();
    
    boolean notifyInChatForCaster();
    
    boolean notifyInChatForTarget();
    
    boolean notifyInChatWithCasterName();
    
    WakfuEffect clone();
    
    boolean isAnUsableEffect();
    
    boolean isUsableInFight();
    
    boolean isUsableInWorld();
    
    int getContainerMinLevel();
    
    int getContainerMaxLevel();
    
    SimpleCriterion getConditions();
    
    float getExecutionProbability(short p0);
    
    float getExecutionProbability();
    
    void addActionToExecuteOnApplication(HMIAction p0);
    
    void addActionToExecuteOnUnApplication(HMIAction p0);
    
    void addActionToExecuteOnExecution(HMIAction p0);
    
    void addActionToStopOnUnApplication(HMIAction p0);
    
    void addActionToStopOnExecution(HMIAction p0);
    
    Iterator<HMIAction> getActionsToExecuteOnApplication();
    
    Iterator<HMIAction> getActionsToExecuteOnExecution();
    
    AreaOfEffect getEmptyCellNeededAreaOfEffect();
    
    float getMaxExecutionIncr();
    
    int getScriptFileId();
    
    void setScriptFileId(int p0);
    
    void setIsDurationInCasterTurn(boolean p0);
    
    List<HMIAction> getActionsOrder();
    
    boolean isActionToExecuteOnUnapplication(HMIAction p0);
    
    boolean isActionToStopOnUnapplication(HMIAction p0);
    
    boolean isActionToExecuteOnApplication(HMIAction p0);
    
    boolean isActionToExecuteOnExecution(HMIAction p0);
    
    boolean isActionToStopOnExecution(HMIAction p0);
    
    void setTriggerListenerType(TriggerListenerType p0);
    
    void setTriggerTargetType(TriggerTargetType p0);
    
    void setTriggerCasterType(TriggerCasterType p0);
    
    void setDisplayInSpellDescription(boolean p0);
    
    boolean isDisplayInSpellDescription();
    
    boolean isDisplayInStateBar();
    
    void setDisplayInStateBar(boolean p0);
    
    boolean isCriterionGrayable();
    
    boolean doNotNotify();
    
    boolean hasProperty(RunningEffectPropertyType p0);
}
