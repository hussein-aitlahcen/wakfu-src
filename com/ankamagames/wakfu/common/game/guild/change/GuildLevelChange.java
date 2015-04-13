package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class GuildLevelChange implements GuildChange
{
    private static final Logger m_logger;
    private short m_guildLevel;
    
    GuildLevelChange() {
        super();
    }
    
    GuildLevelChange(final short guildLevel) {
        super();
        this.m_guildLevel = guildLevel;
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(2);
        bb.putShort(this.m_guildLevel);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_guildLevel = bb.getShort();
    }
    
    @Override
    public void compute(final GuildController controller) {
        try {
            controller.setGuildLevel(this.m_guildLevel);
        }
        catch (GuildException e) {
            GuildLevelChange.m_logger.error((Object)"Impossible de retirer le membre", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.GUILD_LEVEL;
    }
    
    @Override
    public String toString() {
        return "GuildPointsChange{m_guildLevel=" + this.m_guildLevel + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildLevelChange.class);
    }
}
