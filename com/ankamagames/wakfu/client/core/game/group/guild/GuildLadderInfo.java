package com.ankamagames.wakfu.client.core.game.group.guild;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class GuildLadderInfo
{
    private long m_guildId;
    private long m_blazon;
    private String m_name;
    private String m_description;
    private int m_guildPoints;
    private int m_conquestPoints;
    private short m_level;
    
    public long getGuildId() {
        return this.m_guildId;
    }
    
    public long getBlazon() {
        return this.m_blazon;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public int getGuildPoints() {
        return this.m_guildPoints;
    }
    
    public int getConquestPoints() {
        return this.m_conquestPoints;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public void unserialize(final ByteBuffer bb) {
        this.m_guildId = bb.getLong();
        this.m_blazon = bb.getLong();
        final byte[] name = new byte[bb.getInt()];
        bb.get(name);
        this.m_name = StringUtils.fromUTF8(name);
        final byte[] desc = new byte[bb.getInt()];
        bb.get(desc);
        this.m_description = StringUtils.fromUTF8(desc);
        this.m_guildPoints = bb.getInt();
        this.m_conquestPoints = bb.getInt();
        this.m_level = bb.getShort();
    }
}
