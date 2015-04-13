package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import org.apache.commons.lang3.*;
import java.util.*;

public class CharacterInformationMessage extends InputOnlyProxyMessage
{
    private byte[] m_serializedCharacterInfo;
    private long[] m_reservedIds;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buf = ByteBuffer.wrap(rawDatas);
        final Short ids = buf.getShort();
        this.m_reservedIds = new long[(short)ids];
        for (int i = 0; i < ids; ++i) {
            this.m_reservedIds[i] = buf.getLong();
        }
        buf.get(this.m_serializedCharacterInfo = new byte[buf.getInt()]);
        return true;
    }
    
    @Override
    public int getId() {
        return 4098;
    }
    
    public byte[] getSerializedCharacterInfo() {
        return ArrayUtils.clone(this.m_serializedCharacterInfo);
    }
    
    public long[] getReservedIds() {
        return ArrayUtils.clone(this.m_reservedIds);
    }
    
    @Override
    public String toString() {
        return "CharacterInformationMessage{m_serializedCharacterInfo=" + Arrays.toString(this.m_serializedCharacterInfo) + ", m_reservedIds=" + Arrays.toString(this.m_reservedIds) + '}';
    }
}
