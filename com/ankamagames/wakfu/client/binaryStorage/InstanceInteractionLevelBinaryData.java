package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class InstanceInteractionLevelBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_worldId;
    protected int m_subscriptionLevel;
    protected int m_interactionLevel;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getWorldId() {
        return this.m_worldId;
    }
    
    public int getSubscriptionLevel() {
        return this.m_subscriptionLevel;
    }
    
    public int getInteractionLevel() {
        return this.m_interactionLevel;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_worldId = 0;
        this.m_subscriptionLevel = 0;
        this.m_interactionLevel = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_worldId = buffer.getInt();
        this.m_subscriptionLevel = buffer.getInt();
        this.m_interactionLevel = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.INSTANCE_INTERACTION_LEVEL.getId();
    }
}
