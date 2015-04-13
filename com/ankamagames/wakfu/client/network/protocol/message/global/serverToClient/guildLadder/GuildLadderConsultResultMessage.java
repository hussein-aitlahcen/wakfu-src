package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guildLadder;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import java.nio.*;

public class GuildLadderConsultResultMessage extends InputOnlyProxyMessage
{
    private ArrayList<GuildLadderInfo> m_ladderInfo;
    private int m_totalGuildSize;
    
    public GuildLadderConsultResultMessage() {
        super();
        this.m_ladderInfo = new ArrayList<GuildLadderInfo>();
    }
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_totalGuildSize = bb.getInt();
        while (bb.remaining() > 0) {
            final GuildLadderInfo guildLadderInfo = new GuildLadderInfo();
            guildLadderInfo.unserialize(bb);
            this.m_ladderInfo.add(guildLadderInfo);
        }
        return true;
    }
    
    public int getTotalGuildSize() {
        return this.m_totalGuildSize;
    }
    
    public ArrayList<GuildLadderInfo> getLadderInfo() {
        return this.m_ladderInfo;
    }
    
    @Override
    public int getId() {
        return 20086;
    }
}
