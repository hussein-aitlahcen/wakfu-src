package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AreaEffectBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_scriptId;
    protected int m_areaAreaId;
    protected int m_maxExecutionCount;
    protected int m_targetsToShow;
    protected boolean m_canBeTargeted;
    protected boolean m_obstacleForAI;
    protected boolean m_shouldStopMovement;
    protected boolean m_canBeTargetedByAI;
    protected boolean m_canBeDestroyed;
    protected String m_type;
    protected int[] m_areaAreaParams;
    protected int[] m_applicationTriggers;
    protected int[] m_unapplicationTriggers;
    protected int[] m_destructionTriggers;
    protected float[] m_deactivationDelay;
    protected float[] m_params;
    protected int[] m_properties;
    protected int[] m_effectIds;
    protected String m_areaGfx;
    protected String m_areaCellGfx;
    protected String m_aps;
    protected String m_cellAps;
    protected int m_maxLevel;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getScriptId() {
        return this.m_scriptId;
    }
    
    public int getAreaAreaId() {
        return this.m_areaAreaId;
    }
    
    public int getMaxExecutionCount() {
        return this.m_maxExecutionCount;
    }
    
    public int getTargetsToShow() {
        return this.m_targetsToShow;
    }
    
    public boolean isCanBeTargeted() {
        return this.m_canBeTargeted;
    }
    
    public boolean isObstacleForAI() {
        return this.m_obstacleForAI;
    }
    
    public boolean isShouldStopMovement() {
        return this.m_shouldStopMovement;
    }
    
    public boolean isCanBeTargetedByAI() {
        return this.m_canBeTargetedByAI;
    }
    
    public boolean isCanBeDestroyed() {
        return this.m_canBeDestroyed;
    }
    
    public String getType() {
        return this.m_type;
    }
    
    public int[] getAreaAreaParams() {
        return this.m_areaAreaParams;
    }
    
    public int[] getApplicationTriggers() {
        return this.m_applicationTriggers;
    }
    
    public int[] getUnapplicationTriggers() {
        return this.m_unapplicationTriggers;
    }
    
    public int[] getDestructionTriggers() {
        return this.m_destructionTriggers;
    }
    
    public float[] getDeactivationDelay() {
        return this.m_deactivationDelay;
    }
    
    public float[] getParams() {
        return this.m_params;
    }
    
    public int[] getProperties() {
        return this.m_properties;
    }
    
    public int[] getEffectIds() {
        return this.m_effectIds;
    }
    
    public String getAreaGfx() {
        return this.m_areaGfx;
    }
    
    public String getAreaCellGfx() {
        return this.m_areaCellGfx;
    }
    
    public String getAps() {
        return this.m_aps;
    }
    
    public String getCellAps() {
        return this.m_cellAps;
    }
    
    public int getMaxLevel() {
        return this.m_maxLevel;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_scriptId = 0;
        this.m_areaAreaId = 0;
        this.m_maxExecutionCount = 0;
        this.m_targetsToShow = 0;
        this.m_canBeTargeted = false;
        this.m_obstacleForAI = false;
        this.m_shouldStopMovement = false;
        this.m_canBeTargetedByAI = false;
        this.m_canBeDestroyed = false;
        this.m_type = null;
        this.m_areaAreaParams = null;
        this.m_applicationTriggers = null;
        this.m_unapplicationTriggers = null;
        this.m_destructionTriggers = null;
        this.m_deactivationDelay = null;
        this.m_params = null;
        this.m_properties = null;
        this.m_effectIds = null;
        this.m_areaGfx = null;
        this.m_areaCellGfx = null;
        this.m_aps = null;
        this.m_cellAps = null;
        this.m_maxLevel = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_scriptId = buffer.getInt();
        this.m_areaAreaId = buffer.getInt();
        this.m_maxExecutionCount = buffer.getInt();
        this.m_targetsToShow = buffer.getInt();
        this.m_canBeTargeted = buffer.readBoolean();
        this.m_obstacleForAI = buffer.readBoolean();
        this.m_shouldStopMovement = buffer.readBoolean();
        this.m_canBeTargetedByAI = buffer.readBoolean();
        this.m_canBeDestroyed = buffer.readBoolean();
        this.m_type = buffer.readUTF8().intern();
        this.m_areaAreaParams = buffer.readIntArray();
        this.m_applicationTriggers = buffer.readIntArray();
        this.m_unapplicationTriggers = buffer.readIntArray();
        this.m_destructionTriggers = buffer.readIntArray();
        this.m_deactivationDelay = buffer.readFloatArray();
        this.m_params = buffer.readFloatArray();
        this.m_properties = buffer.readIntArray();
        this.m_effectIds = buffer.readIntArray();
        this.m_areaGfx = buffer.readUTF8().intern();
        this.m_areaCellGfx = buffer.readUTF8().intern();
        this.m_aps = buffer.readUTF8().intern();
        this.m_cellAps = buffer.readUTF8().intern();
        this.m_maxLevel = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.AREA_EFFECT.getId();
    }
}
