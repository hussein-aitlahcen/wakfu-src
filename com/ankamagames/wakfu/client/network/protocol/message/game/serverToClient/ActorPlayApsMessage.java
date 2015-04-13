package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class ActorPlayApsMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private int m_apsId;
    private int m_duree;
    private boolean m_follow;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buff.getLong();
        this.m_apsId = buff.getInt();
        this.m_duree = buff.getInt();
        this.m_follow = (buff.get() == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 9201;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public int getApsId() {
        return this.m_apsId;
    }
    
    public int getDuree() {
        return this.m_duree;
    }
    
    public boolean isFollow() {
        return this.m_follow;
    }
}
