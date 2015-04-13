package com.ankamagames.wakfu.common.game.havenWorld;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class GuildInfo
{
    private static final Logger m_logger;
    private final long m_guildId;
    private final String m_guildName;
    private final long m_blazon;
    
    private GuildInfo(final long guildId, final String guildName, final long blazon) {
        super();
        this.m_guildId = guildId;
        this.m_guildName = guildName;
        this.m_blazon = blazon;
    }
    
    public long getGuildId() {
        return this.m_guildId;
    }
    
    public String getGuildName() {
        return this.m_guildName;
    }
    
    public long getBlazon() {
        return this.m_blazon;
    }
    
    @Override
    public String toString() {
        return "GuildInfo{m_guildId=" + this.m_guildId + ", m_guildName='" + this.m_guildName + '\'' + ", m_blazon=" + this.m_blazon + '}';
    }
    
    public static long getGuildId(final HavenWorld world) {
        if (world.getGuildInfo() == null) {
            GuildInfo.m_logger.error((Object)"Demande d'info de guild sur un monde qui n'appartient pas \u00e0 une guilde");
            return 0L;
        }
        return world.getGuildInfo().getGuildId();
    }
    
    public static GuildInfo decode(final ByteBuffer bb) {
        final long guildId = bb.getLong();
        if (guildId == 0L) {
            return null;
        }
        final long blazon = bb.getLong();
        final byte[] utfName = new byte[bb.getInt()];
        bb.get(utfName);
        final String guildName = StringUtils.fromUTF8(utfName);
        return new GuildInfo(guildId, guildName, blazon);
    }
    
    public static void encode(final GuildInfo guildInfo, final ByteArray bb) {
        if (guildInfo == null) {
            GuildInfo.m_logger.error((Object)"serialisation d'un HM qui n'appartient \u00e0 personne");
            bb.putLong(0L);
            return;
        }
        bb.putLong(guildInfo.getGuildId());
        bb.putLong(guildInfo.getBlazon());
        final byte[] guildName = StringUtils.toUTF8(guildInfo.getGuildName());
        bb.putInt(guildName.length);
        bb.put(guildName);
    }
    
    public static GuildInfo create(final long guildId, final String name, final long blazon) {
        if (guildId == 0L) {
            return null;
        }
        return new GuildInfo(guildId, name, blazon);
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildInfo.class);
    }
}
