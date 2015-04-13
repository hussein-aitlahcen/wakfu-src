package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guildStorage;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.guild.storage.*;
import java.nio.*;

public class GuildStorageConsultResultMessage extends InputOnlyProxyMessage
{
    private TIntObjectHashMap<Boolean> m_compartments;
    private int m_money;
    private GuildStorageHistory m_history;
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final int numCompartments = bb.getInt();
        this.m_compartments = new TIntObjectHashMap<Boolean>(numCompartments);
        for (int i = 0; i < numCompartments; ++i) {
            this.m_compartments.put(bb.getInt(), bb.get() != 0);
        }
        this.m_money = bb.getInt();
        (this.m_history = new GuildStorageHistory()).fromBuild(bb);
        return true;
    }
    
    public TIntObjectHashMap<Boolean> getCompartments() {
        return this.m_compartments;
    }
    
    public int getMoney() {
        return this.m_money;
    }
    
    public GuildStorageHistory getHistory() {
        return this.m_history;
    }
    
    @Override
    public int getId() {
        return 20076;
    }
}
