package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class HavenWorldBuildingVisualDefinitionBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_buildingId;
    protected VisualElement[] m_elements;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getBuildingId() {
        return this.m_buildingId;
    }
    
    public VisualElement[] getElements() {
        return this.m_elements;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_buildingId = 0;
        this.m_elements = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_buildingId = buffer.getInt();
        final int elementCount = buffer.getInt();
        this.m_elements = new VisualElement[elementCount];
        for (int iElement = 0; iElement < elementCount; ++iElement) {
            (this.m_elements[iElement] = new VisualElement()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.HAVEN_WORLD_BUILDING_VISUAL_DEFINITION.getId();
    }
    
    public static class VisualElement
    {
        protected int m_uid;
        protected int m_gfxId;
        protected boolean m_hasGuildColor;
        protected boolean m_occluder;
        protected byte m_height;
        protected String m_animName;
        protected byte m_direction;
        protected byte m_x;
        protected byte m_y;
        protected byte m_z;
        
        public int getUid() {
            return this.m_uid;
        }
        
        public int getGfxId() {
            return this.m_gfxId;
        }
        
        public boolean hasGuildColor() {
            return this.m_hasGuildColor;
        }
        
        public boolean isOccluder() {
            return this.m_occluder;
        }
        
        public byte getHeight() {
            return this.m_height;
        }
        
        public String getAnimName() {
            return this.m_animName;
        }
        
        public byte getDirection() {
            return this.m_direction;
        }
        
        public byte getX() {
            return this.m_x;
        }
        
        public byte getY() {
            return this.m_y;
        }
        
        public byte getZ() {
            return this.m_z;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_uid = buffer.getInt();
            this.m_gfxId = buffer.getInt();
            this.m_hasGuildColor = buffer.readBoolean();
            this.m_occluder = buffer.readBoolean();
            this.m_height = buffer.get();
            this.m_animName = buffer.readUTF8().intern();
            this.m_direction = buffer.get();
            this.m_x = buffer.get();
            this.m_y = buffer.get();
            this.m_z = buffer.get();
        }
    }
}
