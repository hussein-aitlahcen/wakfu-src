package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class GuildPointsChange implements GuildChange
{
    private static final Logger m_logger;
    private int m_guildPoints;
    
    GuildPointsChange() {
        super();
    }
    
    GuildPointsChange(final int guildPoints) {
        super();
        this.m_guildPoints = guildPoints;
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(this.m_guildPoints);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_guildPoints = bb.getInt();
    }
    
    @Override
    public void compute(final GuildController controller) {
        try {
            if (this.m_guildPoints > 0) {
                controller.addGuildPoints(this.m_guildPoints);
            }
            else {
                controller.removeGuildPoints(Math.abs(this.m_guildPoints));
            }
        }
        catch (GuildException e) {
            GuildPointsChange.m_logger.error((Object)"Impossible de retirer le membre", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.GUILD_POINTS;
    }
    
    @Override
    public String toString() {
        return "GuildPointsChange{m_guildPoints=" + this.m_guildPoints + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildPointsChange.class);
    }
}
