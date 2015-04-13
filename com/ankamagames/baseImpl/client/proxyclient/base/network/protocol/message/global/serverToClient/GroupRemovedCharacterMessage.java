package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class GroupRemovedCharacterMessage extends InputOnlyProxyMessage
{
    private long m_groupId;
    private TLongArrayList m_characterRemovedIds;
    
    public GroupRemovedCharacterMessage() {
        super();
        this.m_characterRemovedIds = new TLongArrayList();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_groupId = buff.getLong();
        while (buff.hasRemaining()) {
            this.m_characterRemovedIds.add(buff.getLong());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 506;
    }
    
    public long getGroupId() {
        return this.m_groupId;
    }
    
    public TLongArrayList getCharacterRemovedIds() {
        return this.m_characterRemovedIds;
    }
}
