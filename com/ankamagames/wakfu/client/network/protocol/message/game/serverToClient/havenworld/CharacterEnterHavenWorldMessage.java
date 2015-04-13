package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import java.nio.*;

public class CharacterEnterHavenWorldMessage extends InputOnlyProxyMessage
{
    private byte[] m_rawTopology;
    private byte[] m_rawBuildings;
    private GuildInfo m_guildInfo;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final short rawTopologySize = buffer.getShort();
        buffer.get(this.m_rawTopology = new byte[rawTopologySize]);
        final short rawBuildingsSize = buffer.getShort();
        buffer.get(this.m_rawBuildings = new byte[rawBuildingsSize]);
        this.m_guildInfo = GuildInfo.decode(buffer);
        return true;
    }
    
    public byte[] getRawTopology() {
        return this.m_rawTopology;
    }
    
    public byte[] getRawBuildings() {
        return this.m_rawBuildings;
    }
    
    public GuildInfo getGuildInfo() {
        return this.m_guildInfo;
    }
    
    @Override
    public int getId() {
        return 15650;
    }
}
