package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guildStorage;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class GuildStorageMoneyResultMessage extends InputOnlyProxyMessage
{
    private int m_money;
    private RawGuildStorageHistory m_history;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_money = bb.getInt();
        (this.m_history = new RawGuildStorageHistory()).unserialize(bb);
        return true;
    }
    
    public int getMoney() {
        return this.m_money;
    }
    
    public RawGuildStorageHistory getHistory() {
        return this.m_history;
    }
    
    @Override
    public int getId() {
        return 20080;
    }
}
