package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class TeleporterBinaryData implements BinaryData
{
    protected int m_teleporterId;
    protected int m_lockId;
    protected Destination[] m_destinations;
    
    public int getTeleporterId() {
        return this.m_teleporterId;
    }
    
    public int getLockId() {
        return this.m_lockId;
    }
    
    public Destination[] getDestinations() {
        return this.m_destinations;
    }
    
    @Override
    public void reset() {
        this.m_teleporterId = 0;
        this.m_lockId = 0;
        this.m_destinations = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_teleporterId = buffer.getInt();
        this.m_lockId = buffer.getInt();
        final int destinationCount = buffer.getInt();
        this.m_destinations = new Destination[destinationCount];
        for (int iDestination = 0; iDestination < destinationCount; ++iDestination) {
            (this.m_destinations[iDestination] = new Destination()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.TELEPORTER.getId();
    }
    
    public static class Destination
    {
        protected int m_destinationId;
        protected int m_x;
        protected int m_y;
        protected int m_z;
        protected int m_worldId;
        protected byte m_direction;
        protected String m_criteria;
        protected int m_visualId;
        protected int m_apsId;
        protected short m_delay;
        protected int m_itemConsumed;
        protected short m_itemQuantity;
        protected short m_kamaCost;
        protected boolean m_doConsumeItem;
        protected boolean m_isInvisible;
        protected String m_loadingAnimationName;
        protected int m_loadingMinDuration;
        protected int m_loadingFadeInDuration;
        protected int m_loadingFadeOutDuration;
        
        public int getDestinationId() {
            return this.m_destinationId;
        }
        
        public int getX() {
            return this.m_x;
        }
        
        public int getY() {
            return this.m_y;
        }
        
        public int getZ() {
            return this.m_z;
        }
        
        public int getWorldId() {
            return this.m_worldId;
        }
        
        public byte getDirection() {
            return this.m_direction;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public int getVisualId() {
            return this.m_visualId;
        }
        
        public int getApsId() {
            return this.m_apsId;
        }
        
        public short getDelay() {
            return this.m_delay;
        }
        
        public int getItemConsumed() {
            return this.m_itemConsumed;
        }
        
        public short getItemQuantity() {
            return this.m_itemQuantity;
        }
        
        public short getKamaCost() {
            return this.m_kamaCost;
        }
        
        public boolean isDoConsumeItem() {
            return this.m_doConsumeItem;
        }
        
        public boolean isInvisible() {
            return this.m_isInvisible;
        }
        
        public String getLoadingAnimationName() {
            return this.m_loadingAnimationName;
        }
        
        public int getLoadingMinDuration() {
            return this.m_loadingMinDuration;
        }
        
        public int getLoadingFadeInDuration() {
            return this.m_loadingFadeInDuration;
        }
        
        public int getLoadingFadeOutDuration() {
            return this.m_loadingFadeOutDuration;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_destinationId = buffer.getInt();
            this.m_x = buffer.getInt();
            this.m_y = buffer.getInt();
            this.m_z = buffer.getInt();
            this.m_worldId = buffer.getInt();
            this.m_direction = buffer.get();
            this.m_criteria = buffer.readUTF8().intern();
            this.m_visualId = buffer.getInt();
            this.m_apsId = buffer.getInt();
            this.m_delay = buffer.getShort();
            this.m_itemConsumed = buffer.getInt();
            this.m_itemQuantity = buffer.getShort();
            this.m_kamaCost = buffer.getShort();
            this.m_doConsumeItem = buffer.readBoolean();
            this.m_isInvisible = buffer.readBoolean();
            this.m_loadingAnimationName = buffer.readUTF8().intern();
            this.m_loadingMinDuration = buffer.getInt();
            this.m_loadingFadeInDuration = buffer.getInt();
            this.m_loadingFadeOutDuration = buffer.getInt();
        }
    }
}
