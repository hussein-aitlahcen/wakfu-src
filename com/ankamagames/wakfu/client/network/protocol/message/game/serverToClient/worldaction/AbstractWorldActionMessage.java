package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.worldaction;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.world.action.*;
import java.nio.*;

public abstract class AbstractWorldActionMessage extends InputOnlyProxyMessage
{
    protected static final int WORLD_ACTION_HEADER_SIZE = 8;
    private int m_uniqueId;
    private int m_triggeringActionUniqueId;
    
    public AbstractWorldActionMessage() {
        super();
        this.m_triggeringActionUniqueId = -1;
    }
    
    public abstract int getActionId();
    
    public abstract WorldActionType getWorldActionType();
    
    protected void decodeWorldActionHeader(final ByteBuffer buff) {
        this.m_uniqueId = buff.getInt();
        this.m_triggeringActionUniqueId = buff.getInt();
    }
    
    public int getUniqueId() {
        return this.m_uniqueId;
    }
    
    public int getTriggeringActionUniqueId() {
        return this.m_triggeringActionUniqueId;
    }
}
