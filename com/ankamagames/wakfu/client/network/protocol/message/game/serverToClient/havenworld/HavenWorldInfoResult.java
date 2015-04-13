package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import java.util.*;

public class HavenWorldInfoResult extends InputOnlyProxyMessage
{
    private HavenWorldError m_error;
    private String m_guildName;
    private final ArrayList<Building> m_buildings;
    private int m_guildRank;
    private int m_conquestRank;
    
    public HavenWorldInfoResult() {
        super();
        this.m_buildings = new ArrayList<Building>();
    }
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_error = HavenWorldError.valueOf(bb.getInt());
        if (bb.get() == 1) {
            this.m_guildRank = bb.getInt();
            this.m_conquestRank = bb.getInt();
            final byte[] utfName = new byte[bb.getInt()];
            bb.get(utfName);
            this.m_guildName = StringUtils.fromUTF8(utfName);
            for (int i = 0, size = bb.getInt(); i < size; ++i) {
                this.m_buildings.add(HavenWorldSerializer.unSerializeBuildingNoElements(bb));
            }
        }
        return true;
    }
    
    public HavenWorldError getError() {
        return this.m_error;
    }
    
    public String getGuildName() {
        return this.m_guildName;
    }
    
    public ArrayList<Building> getBuildings() {
        return new ArrayList<Building>(this.m_buildings);
    }
    
    public int getGuildRank() {
        return this.m_guildRank;
    }
    
    public int getConquestRank() {
        return this.m_conquestRank;
    }
    
    @Override
    public int getId() {
        return 20090;
    }
    
    @Override
    public String toString() {
        return "HavenWorldInfoResult{m_error=" + this.m_error + ", m_guildName='" + this.m_guildName + '\'' + ", m_buildings=" + this.m_buildings.size() + '}';
    }
}
