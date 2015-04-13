package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public final class GroupGlobalDataUpdateMessage extends InputOnlyProxyMessage
{
    private long m_groupId;
    private boolean m_fullData;
    private byte[] m_groupData;
    private LinkedList<ObjectPair<Long, byte[]>> m_serializedCharacterData;
    
    public GroupGlobalDataUpdateMessage() {
        super();
        this.m_serializedCharacterData = new LinkedList<ObjectPair<Long, byte[]>>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        try {
            final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
            this.m_groupId = buffer.getLong();
            this.m_fullData = (buffer.get() == 1);
            short length = buffer.getShort();
            if (length > 0) {
                buffer.get(this.m_groupData = new byte[length]);
            }
            length = buffer.getShort();
            for (int i = 0; i < length; ++i) {
                final long characterId = buffer.getLong();
                final byte[] data = new byte[buffer.getShort()];
                buffer.get(data);
                this.m_serializedCharacterData.add(new ObjectPair<Long, byte[]>(characterId, data));
            }
        }
        catch (RuntimeException e) {
            GroupGlobalDataUpdateMessage.m_logger.error((Object)"Exception lev\u00e9e \u00e0 la d\u00e9s\u00e9rialisation d'un message de type GroupGlobalDataUpdateMessage");
            return false;
        }
        return true;
    }
    
    public boolean isFullData() {
        return this.m_fullData;
    }
    
    public long getGroupId() {
        return this.m_groupId;
    }
    
    @Override
    public int getId() {
        return 500;
    }
    
    public byte[] getGroupData() {
        return this.m_groupData;
    }
    
    public LinkedList<ObjectPair<Long, byte[]>> getSerializedCharacterData() {
        return this.m_serializedCharacterData;
    }
}
