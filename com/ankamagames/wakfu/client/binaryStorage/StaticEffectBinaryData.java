package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class StaticEffectBinaryData implements BinaryData
{
    protected int m_effectId;
    protected int m_actionId;
    protected int m_parentId;
    protected short m_areaOrderingMethod;
    protected int[] m_areaSize;
    protected short m_areaShape;
    protected short m_emptyCellsAreaOrderingMethod;
    protected int[] m_emptyCellsAreaSize;
    protected short m_emptyCellsAreaShape;
    protected int[] m_triggersBeforeComputation;
    protected int[] m_triggersBeforeExecution;
    protected int[] m_triggersForUnapplication;
    protected int[] m_triggersAfterExecution;
    protected int[] m_triggersAfterAllExecutions;
    protected int[] m_triggersNotRelatedToExecutions;
    protected int[] m_triggersAdditionnal;
    protected String m_criticalState;
    protected long[] m_targetValidator;
    protected boolean m_affectedByLocalisation;
    protected int m_durationBase;
    protected float m_durationInc;
    protected boolean m_endsAtEndOfTurn;
    protected boolean m_isDurationInFullTurns;
    protected short m_applyDelayBase;
    protected float m_applyDelayIncrement;
    protected float[] m_params;
    protected float m_probabilityBase;
    protected float m_probabilityInc;
    protected byte m_triggerListenerType;
    protected byte m_triggerTargetType;
    protected byte m_triggerCasterType;
    protected boolean m_storeOnSelf;
    protected short m_maxExecution;
    protected float m_maxExecutionIncr;
    protected byte m_maxTargetCount;
    protected boolean m_isFightEffect;
    protected String m_hmiAction;
    protected short m_containerMinLevel;
    protected short m_containerMaxLevel;
    protected String m_effectCriterion;
    protected String m_effectParentType;
    protected String m_effectContainerType;
    protected boolean m_dontTriggerAnything;
    protected boolean m_isPersonal;
    protected boolean m_isDecursable;
    protected boolean m_notifyInChatForCaster;
    protected boolean m_notifyInChatForTarget;
    protected boolean m_notifyInChatWithCasterName;
    protected int m_scriptFileId;
    protected boolean m_durationInCasterTurn;
    protected int[] m_effectProperties;
    protected boolean m_displayInSpellDescription;
    protected boolean m_displayInStateBar;
    protected boolean m_recomputeAreaOfEffectDisplay;
    protected boolean m_isInTurnInFight;
    protected boolean m_notifyInChat;
    
    public boolean isSelfTrigger() {
        return false;
    }
    
    public boolean isTriggerTargetIsSelf() {
        return false;
    }
    
    public boolean isPrecalculateTriggerTarget() {
        return false;
    }
    
    public boolean isFightEffectParentType() {
        final String containerType = this.getEffectContainerType().trim();
        final boolean isFightEffect = this.isFightEffect();
        final boolean isGuildEffect = this.isGuildEffect();
        if (isGuildEffect) {
            return false;
        }
        if (containerType.startsWith("ITEM")) {
            return isFightEffect && this.isActiveParentType();
        }
        final String parentType = this.getEffectParentType().trim();
        return (parentType.startsWith("SPELL") && isFightEffect) || parentType.startsWith("GROUP") || parentType.startsWith("BOMB") || parentType.startsWith("AREA") || parentType.startsWith("IEP_DESTRUCTIBLE") || (parentType.startsWith("STATE") && isFightEffect) || (parentType.startsWith("TIMELINE") && isFightEffect);
    }
    
    public boolean isGuildEffect() {
        if (this.m_effectProperties == null) {
            return false;
        }
        for (int i = 0; i < this.m_effectProperties.length; ++i) {
            final int effectProperty = this.m_effectProperties[i];
            final RunningEffectPropertyType prop = RunningEffectPropertyType.getPropertyFromId(effectProperty);
            if (prop == RunningEffectPropertyType.GUILD_EFFECT) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isActiveParentType() {
        final String containerType = this.getEffectContainerType().trim();
        final boolean isGuildEffect = this.isGuildEffect();
        if (isGuildEffect) {
            return false;
        }
        if (containerType.startsWith("ITEM")) {
            return containerType.endsWith("_USE");
        }
        final boolean isFightEffect = this.isFightEffect();
        final String parentType = this.getEffectParentType().trim();
        return !parentType.startsWith("SET") && (!parentType.startsWith("SPELL") || isFightEffect) && !parentType.startsWith("PROTECTOR") && !parentType.startsWith("BUILDING") && !parentType.startsWith("ABILITY") && !parentType.startsWith("APTITUDE");
    }
    
    public int getEffectId() {
        return this.m_effectId;
    }
    
    public int getActionId() {
        return this.m_actionId;
    }
    
    public int getParentId() {
        return this.m_parentId;
    }
    
    public short getAreaOrderingMethod() {
        return this.m_areaOrderingMethod;
    }
    
    public int[] getAreaSize() {
        return this.m_areaSize;
    }
    
    public short getAreaShape() {
        return this.m_areaShape;
    }
    
    public short getEmptyCellsAreaOrderingMethod() {
        return this.m_emptyCellsAreaOrderingMethod;
    }
    
    public int[] getEmptyCellsAreaSize() {
        return this.m_emptyCellsAreaSize;
    }
    
    public short getEmptyCellsAreaShape() {
        return this.m_emptyCellsAreaShape;
    }
    
    public int[] getTriggersBeforeComputation() {
        return this.m_triggersBeforeComputation;
    }
    
    public int[] getTriggersBeforeExecution() {
        return this.m_triggersBeforeExecution;
    }
    
    public int[] getTriggersForUnapplication() {
        return this.m_triggersForUnapplication;
    }
    
    public int[] getTriggersAfterExecution() {
        return this.m_triggersAfterExecution;
    }
    
    public int[] getTriggersAfterAllExecutions() {
        return this.m_triggersAfterAllExecutions;
    }
    
    public int[] getTriggersNotRelatedToExecutions() {
        return this.m_triggersNotRelatedToExecutions;
    }
    
    public int[] getTriggersAdditionnal() {
        return this.m_triggersAdditionnal;
    }
    
    public String getCriticalState() {
        return this.m_criticalState;
    }
    
    public long[] getTargetValidator() {
        return this.m_targetValidator;
    }
    
    public boolean isAffectedByLocalisation() {
        return this.m_affectedByLocalisation;
    }
    
    public int getDurationBase() {
        return this.m_durationBase;
    }
    
    public float getDurationInc() {
        return this.m_durationInc;
    }
    
    public boolean isEndsAtEndOfTurn() {
        return this.m_endsAtEndOfTurn;
    }
    
    public boolean isDurationInFullTurns() {
        return this.m_isDurationInFullTurns;
    }
    
    public short getApplyDelayBase() {
        return this.m_applyDelayBase;
    }
    
    public float getApplyDelayIncrement() {
        return this.m_applyDelayIncrement;
    }
    
    public float[] getParams() {
        return this.m_params;
    }
    
    public float getProbabilityBase() {
        return this.m_probabilityBase;
    }
    
    public float getProbabilityInc() {
        return this.m_probabilityInc;
    }
    
    public byte getTriggerListenerType() {
        return this.m_triggerListenerType;
    }
    
    public byte getTriggerTargetType() {
        return this.m_triggerTargetType;
    }
    
    public byte getTriggerCasterType() {
        return this.m_triggerCasterType;
    }
    
    public boolean isStoreOnSelf() {
        return this.m_storeOnSelf;
    }
    
    public short getMaxExecution() {
        return this.m_maxExecution;
    }
    
    public float getMaxExecutionIncr() {
        return this.m_maxExecutionIncr;
    }
    
    public byte getMaxTargetCount() {
        return this.m_maxTargetCount;
    }
    
    public boolean isFightEffect() {
        return this.m_isFightEffect;
    }
    
    public String getHmiAction() {
        return this.m_hmiAction;
    }
    
    public short getContainerMinLevel() {
        return this.m_containerMinLevel;
    }
    
    public short getContainerMaxLevel() {
        return this.m_containerMaxLevel;
    }
    
    public String getEffectCriterion() {
        return this.m_effectCriterion;
    }
    
    public String getEffectParentType() {
        return this.m_effectParentType;
    }
    
    public String getEffectContainerType() {
        return this.m_effectContainerType;
    }
    
    public boolean isDontTriggerAnything() {
        return this.m_dontTriggerAnything;
    }
    
    public boolean isPersonal() {
        return this.m_isPersonal;
    }
    
    public boolean isDecursable() {
        return this.m_isDecursable;
    }
    
    public boolean isNotifyInChatForCaster() {
        return this.m_notifyInChatForCaster;
    }
    
    public boolean isNotifyInChatForTarget() {
        return this.m_notifyInChatForTarget;
    }
    
    public boolean isNotifyInChatWithCasterName() {
        return this.m_notifyInChatWithCasterName;
    }
    
    public int getScriptFileId() {
        return this.m_scriptFileId;
    }
    
    public boolean isDurationInCasterTurn() {
        return this.m_durationInCasterTurn;
    }
    
    public int[] getEffectProperties() {
        return this.m_effectProperties;
    }
    
    public boolean isDisplayInSpellDescription() {
        return this.m_displayInSpellDescription;
    }
    
    public boolean isDisplayInStateBar() {
        return this.m_displayInStateBar;
    }
    
    public boolean isRecomputeAreaOfEffectDisplay() {
        return this.m_recomputeAreaOfEffectDisplay;
    }
    
    public boolean isInTurnInFight() {
        return this.m_isInTurnInFight;
    }
    
    public boolean isNotifyInChat() {
        return this.m_notifyInChat;
    }
    
    @Override
    public void reset() {
        this.m_effectId = 0;
        this.m_actionId = 0;
        this.m_parentId = 0;
        this.m_areaOrderingMethod = 0;
        this.m_areaSize = null;
        this.m_areaShape = 0;
        this.m_emptyCellsAreaOrderingMethod = 0;
        this.m_emptyCellsAreaSize = null;
        this.m_emptyCellsAreaShape = 0;
        this.m_triggersBeforeComputation = null;
        this.m_triggersBeforeExecution = null;
        this.m_triggersForUnapplication = null;
        this.m_triggersAfterExecution = null;
        this.m_triggersAfterAllExecutions = null;
        this.m_triggersNotRelatedToExecutions = null;
        this.m_triggersAdditionnal = null;
        this.m_criticalState = null;
        this.m_targetValidator = null;
        this.m_affectedByLocalisation = false;
        this.m_durationBase = 0;
        this.m_durationInc = 0.0f;
        this.m_endsAtEndOfTurn = false;
        this.m_isDurationInFullTurns = false;
        this.m_applyDelayBase = 0;
        this.m_applyDelayIncrement = 0.0f;
        this.m_params = null;
        this.m_probabilityBase = 0.0f;
        this.m_probabilityInc = 0.0f;
        this.m_triggerListenerType = 0;
        this.m_triggerTargetType = 0;
        this.m_triggerCasterType = 0;
        this.m_storeOnSelf = false;
        this.m_maxExecution = 0;
        this.m_maxExecutionIncr = 0.0f;
        this.m_maxTargetCount = 0;
        this.m_isFightEffect = false;
        this.m_hmiAction = null;
        this.m_containerMinLevel = 0;
        this.m_containerMaxLevel = 0;
        this.m_effectCriterion = null;
        this.m_effectParentType = null;
        this.m_effectContainerType = null;
        this.m_dontTriggerAnything = false;
        this.m_isPersonal = false;
        this.m_isDecursable = false;
        this.m_notifyInChatForCaster = false;
        this.m_notifyInChatForTarget = false;
        this.m_notifyInChatWithCasterName = false;
        this.m_scriptFileId = 0;
        this.m_durationInCasterTurn = false;
        this.m_effectProperties = null;
        this.m_displayInSpellDescription = false;
        this.m_displayInStateBar = false;
        this.m_recomputeAreaOfEffectDisplay = false;
        this.m_isInTurnInFight = false;
        this.m_notifyInChat = false;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_effectId = buffer.getInt();
        this.m_actionId = buffer.getInt();
        this.m_parentId = buffer.getInt();
        this.m_areaOrderingMethod = buffer.getShort();
        this.m_areaSize = buffer.readIntArray();
        this.m_areaShape = buffer.getShort();
        this.m_emptyCellsAreaOrderingMethod = buffer.getShort();
        this.m_emptyCellsAreaSize = buffer.readIntArray();
        this.m_emptyCellsAreaShape = buffer.getShort();
        this.m_triggersBeforeComputation = buffer.readIntArray();
        this.m_triggersBeforeExecution = buffer.readIntArray();
        this.m_triggersForUnapplication = buffer.readIntArray();
        this.m_triggersAfterExecution = buffer.readIntArray();
        this.m_triggersAfterAllExecutions = buffer.readIntArray();
        this.m_triggersNotRelatedToExecutions = buffer.readIntArray();
        this.m_triggersAdditionnal = buffer.readIntArray();
        this.m_criticalState = buffer.readUTF8().intern();
        this.m_targetValidator = buffer.readLongArray();
        this.m_affectedByLocalisation = buffer.readBoolean();
        this.m_durationBase = buffer.getInt();
        this.m_durationInc = buffer.getFloat();
        this.m_endsAtEndOfTurn = buffer.readBoolean();
        this.m_isDurationInFullTurns = buffer.readBoolean();
        this.m_applyDelayBase = buffer.getShort();
        this.m_applyDelayIncrement = buffer.getFloat();
        this.m_params = buffer.readFloatArray();
        this.m_probabilityBase = buffer.getFloat();
        this.m_probabilityInc = buffer.getFloat();
        this.m_triggerListenerType = buffer.get();
        this.m_triggerTargetType = buffer.get();
        this.m_triggerCasterType = buffer.get();
        this.m_storeOnSelf = buffer.readBoolean();
        this.m_maxExecution = buffer.getShort();
        this.m_maxExecutionIncr = buffer.getFloat();
        this.m_maxTargetCount = buffer.get();
        this.m_isFightEffect = buffer.readBoolean();
        this.m_hmiAction = buffer.readUTF8().intern();
        this.m_containerMinLevel = buffer.getShort();
        this.m_containerMaxLevel = buffer.getShort();
        this.m_effectCriterion = buffer.readUTF8().intern();
        this.m_effectParentType = buffer.readUTF8().intern();
        this.m_effectContainerType = buffer.readUTF8().intern();
        this.m_dontTriggerAnything = buffer.readBoolean();
        this.m_isPersonal = buffer.readBoolean();
        this.m_isDecursable = buffer.readBoolean();
        this.m_notifyInChatForCaster = buffer.readBoolean();
        this.m_notifyInChatForTarget = buffer.readBoolean();
        this.m_notifyInChatWithCasterName = buffer.readBoolean();
        this.m_scriptFileId = buffer.getInt();
        this.m_durationInCasterTurn = buffer.readBoolean();
        this.m_effectProperties = buffer.readIntArray();
        this.m_displayInSpellDescription = buffer.readBoolean();
        this.m_displayInStateBar = buffer.readBoolean();
        this.m_recomputeAreaOfEffectDisplay = buffer.readBoolean();
        this.m_isInTurnInFight = buffer.readBoolean();
        this.m_notifyInChat = buffer.readBoolean();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.STATIC_EFFECT.getId();
    }
}
