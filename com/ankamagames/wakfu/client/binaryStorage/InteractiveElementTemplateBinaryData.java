package com.ankamagames.wakfu.client.binaryStorage;

import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class InteractiveElementTemplateBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_modelType;
    protected short m_worldId;
    protected int m_x;
    protected int m_y;
    protected short m_z;
    protected short m_initialState;
    protected boolean m_initiallyVisible;
    protected boolean m_initiallyUsable;
    protected boolean m_blockingMovement;
    protected boolean m_blockingLos;
    protected byte m_direction;
    protected short m_activationPattern;
    protected String m_parameter;
    protected int m_templateId;
    protected int[] m_properties;
    protected Point3[] m_positionsTrigger;
    protected TShortIntHashMap m_actions;
    protected int[] m_views;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getModelType() {
        return this.m_modelType;
    }
    
    public short getWorldId() {
        return this.m_worldId;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public short getZ() {
        return this.m_z;
    }
    
    public short getInitialState() {
        return this.m_initialState;
    }
    
    public boolean isInitiallyVisible() {
        return this.m_initiallyVisible;
    }
    
    public boolean isInitiallyUsable() {
        return this.m_initiallyUsable;
    }
    
    public boolean isBlockingMovement() {
        return this.m_blockingMovement;
    }
    
    public boolean isBlockingLos() {
        return this.m_blockingLos;
    }
    
    public byte getDirection() {
        return this.m_direction;
    }
    
    public short getActivationPattern() {
        return this.m_activationPattern;
    }
    
    public String getParameter() {
        return this.m_parameter;
    }
    
    public int getTemplateId() {
        return this.m_templateId;
    }
    
    public int[] getProperties() {
        return this.m_properties;
    }
    
    public Point3[] getPositionsTrigger() {
        return this.m_positionsTrigger;
    }
    
    public TShortIntHashMap getActions() {
        return this.m_actions;
    }
    
    public int[] getViews() {
        return this.m_views;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_modelType = 0;
        this.m_worldId = 0;
        this.m_x = 0;
        this.m_y = 0;
        this.m_z = 0;
        this.m_initialState = 0;
        this.m_initiallyVisible = false;
        this.m_initiallyUsable = false;
        this.m_blockingMovement = false;
        this.m_blockingLos = false;
        this.m_direction = 0;
        this.m_activationPattern = 0;
        this.m_parameter = null;
        this.m_templateId = 0;
        this.m_properties = null;
        this.m_positionsTrigger = null;
        this.m_actions = null;
        this.m_views = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_modelType = buffer.getShort();
        this.m_worldId = buffer.getShort();
        this.m_x = buffer.getInt();
        this.m_y = buffer.getInt();
        this.m_z = buffer.getShort();
        this.m_initialState = buffer.getShort();
        this.m_initiallyVisible = buffer.readBoolean();
        this.m_initiallyUsable = buffer.readBoolean();
        this.m_blockingMovement = buffer.readBoolean();
        this.m_blockingLos = buffer.readBoolean();
        this.m_direction = buffer.get();
        this.m_activationPattern = buffer.getShort();
        this.m_parameter = buffer.readUTF8().intern();
        this.m_templateId = buffer.getInt();
        this.m_properties = buffer.readIntArray();
        final int positionsTriggerCount = buffer.getInt();
        this.m_positionsTrigger = new Point3[positionsTriggerCount];
        for (int iPositionsTrigger = 0; iPositionsTrigger < positionsTriggerCount; ++iPositionsTrigger) {
            (this.m_positionsTrigger[iPositionsTrigger] = new Point3()).read(buffer);
        }
        final int actionCount = buffer.getInt();
        this.m_actions = new TShortIntHashMap(actionCount);
        for (int iAction = 0; iAction < actionCount; ++iAction) {
            final short actionKey = buffer.getShort();
            final int actionValue = buffer.getInt();
            this.m_actions.put(actionKey, actionValue);
        }
        this.m_views = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.INTERACTIVE_ELEMENT_TEMPLATE.getId();
    }
    
    public static class Point3
    {
        protected int m_x;
        protected int m_y;
        protected short m_z;
        
        public int getX() {
            return this.m_x;
        }
        
        public int getY() {
            return this.m_y;
        }
        
        public short getZ() {
            return this.m_z;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_x = buffer.getInt();
            this.m_y = buffer.getInt();
            this.m_z = buffer.getShort();
        }
    }
}
