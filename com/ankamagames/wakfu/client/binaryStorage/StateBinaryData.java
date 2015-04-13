package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class StateBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_maxLevel;
    protected int[] m_endTrigger;
    protected int[] m_duration;
    protected float[] m_durationInc;
    protected boolean m_endsAtEndOfTurn;
    protected boolean m_isDurationInFullTurns;
    protected boolean m_inTurnInFight;
    protected boolean m_isReplacable;
    protected boolean m_isTransmigrable;
    protected String m_hmiActions;
    protected String m_applyCriterion;
    protected boolean m_isCumulable;
    protected boolean m_durationInCasterTurn;
    protected boolean m_durationInRealTime;
    protected int[] m_effectIds;
    protected int[] m_stateImmunities;
    protected boolean m_stateShouldBeSaved;
    protected boolean m_decursable;
    protected byte m_stateType;
    protected byte m_statePowerType;
    protected boolean m_isReapplyEvenAtMaxLevel;
    protected boolean m_timelineVisible;
    protected int m_gfxId;
    protected boolean m_displayCasterName;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public int[] getEndTrigger() {
        return this.m_endTrigger;
    }
    
    public int[] getDuration() {
        return this.m_duration;
    }
    
    public float[] getDurationInc() {
        return this.m_durationInc;
    }
    
    public short getDurationTurnTable() {
        return (short)this.m_duration[0];
    }
    
    public int getDurationMs() {
        return (this.m_duration.length == 0) ? 0 : (1000 * this.m_duration[1]);
    }
    
    public float getDurationTurnTableInc() {
        return this.m_durationInc[0];
    }
    
    public int getDurationMsInc() {
        return (this.m_durationInc.length == 0) ? 0 : ((int)(1000.0f * this.m_durationInc[1]));
    }
    
    public boolean isEndsAtEndOfTurn() {
        return this.m_endsAtEndOfTurn;
    }
    
    public boolean isDurationInFullTurns() {
        return this.m_isDurationInFullTurns;
    }
    
    public boolean isInTurnInFight() {
        return this.m_inTurnInFight;
    }
    
    public boolean isReplacable() {
        return this.m_isReplacable;
    }
    
    public boolean isTransmigrable() {
        return this.m_isTransmigrable;
    }
    
    public String getHmiActions() {
        return this.m_hmiActions;
    }
    
    public String getApplyCriterion() {
        return this.m_applyCriterion;
    }
    
    public boolean isCumulable() {
        return this.m_isCumulable;
    }
    
    public boolean isDurationInCasterTurn() {
        return this.m_durationInCasterTurn;
    }
    
    public boolean isDurationInRealTime() {
        return this.m_durationInRealTime;
    }
    
    public int[] getEffectIds() {
        return this.m_effectIds;
    }
    
    public int[] getStateImmunities() {
        return this.m_stateImmunities;
    }
    
    public boolean isStateShouldBeSaved() {
        return this.m_stateShouldBeSaved;
    }
    
    public boolean isDecursable() {
        return this.m_decursable;
    }
    
    public byte getStateType() {
        return this.m_stateType;
    }
    
    public byte getStatePowerType() {
        return this.m_statePowerType;
    }
    
    public boolean isReapplyEvenAtMaxLevel() {
        return this.m_isReapplyEvenAtMaxLevel;
    }
    
    public boolean isTimelineVisible() {
        return this.m_timelineVisible;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public boolean isDisplayCasterName() {
        return this.m_displayCasterName;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_maxLevel = 0;
        this.m_endTrigger = null;
        this.m_duration = null;
        this.m_durationInc = null;
        this.m_endsAtEndOfTurn = false;
        this.m_isDurationInFullTurns = false;
        this.m_inTurnInFight = false;
        this.m_isReplacable = false;
        this.m_isTransmigrable = false;
        this.m_hmiActions = null;
        this.m_applyCriterion = null;
        this.m_isCumulable = false;
        this.m_durationInCasterTurn = false;
        this.m_durationInRealTime = false;
        this.m_effectIds = null;
        this.m_stateImmunities = null;
        this.m_stateShouldBeSaved = false;
        this.m_decursable = false;
        this.m_stateType = 0;
        this.m_statePowerType = 0;
        this.m_isReapplyEvenAtMaxLevel = false;
        this.m_timelineVisible = false;
        this.m_gfxId = 0;
        this.m_displayCasterName = false;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_maxLevel = buffer.getShort();
        this.m_endTrigger = buffer.readIntArray();
        this.m_duration = buffer.readIntArray();
        this.m_durationInc = buffer.readFloatArray();
        this.m_endsAtEndOfTurn = buffer.readBoolean();
        this.m_isDurationInFullTurns = buffer.readBoolean();
        this.m_inTurnInFight = buffer.readBoolean();
        this.m_isReplacable = buffer.readBoolean();
        this.m_isTransmigrable = buffer.readBoolean();
        this.m_hmiActions = buffer.readUTF8().intern();
        this.m_applyCriterion = buffer.readUTF8().intern();
        this.m_isCumulable = buffer.readBoolean();
        this.m_durationInCasterTurn = buffer.readBoolean();
        this.m_durationInRealTime = buffer.readBoolean();
        this.m_effectIds = buffer.readIntArray();
        this.m_stateImmunities = buffer.readIntArray();
        this.m_stateShouldBeSaved = buffer.readBoolean();
        this.m_decursable = buffer.readBoolean();
        this.m_stateType = buffer.get();
        this.m_statePowerType = buffer.get();
        this.m_isReapplyEvenAtMaxLevel = buffer.readBoolean();
        this.m_timelineVisible = buffer.readBoolean();
        this.m_gfxId = buffer.getInt();
        this.m_displayCasterName = buffer.readBoolean();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.STATE.getId();
    }
}
