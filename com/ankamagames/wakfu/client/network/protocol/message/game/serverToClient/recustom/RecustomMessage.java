package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.recustom;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class RecustomMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private String m_characterName;
    private short m_recustomType;
    private byte m_source;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        final byte[] nameData = new byte[buffer.get()];
        buffer.get(nameData);
        this.m_characterName = StringUtils.fromUTF8(nameData);
        this.m_recustomType = buffer.getShort();
        this.m_source = buffer.get();
        return true;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public String getCharacterName() {
        return this.m_characterName;
    }
    
    public short getRecustomType() {
        return this.m_recustomType;
    }
    
    public byte getSource() {
        return this.m_source;
    }
    
    @Override
    public int getId() {
        return 2065;
    }
}
