package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.constant.*;

public class GuildErrorMessage extends InputOnlyProxyMessage
{
    private int m_errorId;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_errorId = bb.getInt();
        return true;
    }
    
    public GuildError getError() {
        return GuildError.values()[this.m_errorId];
    }
    
    @Override
    public int getId() {
        return 20059;
    }
    
    @Override
    public String toString() {
        return "GuildErrorMessage{m_errorId=" + this.m_errorId + '}';
    }
}
