package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class RemoveRankChange implements GuildChange
{
    private static final Logger m_logger;
    private long m_rankId;
    
    RemoveRankChange() {
        super();
    }
    
    RemoveRankChange(final GuildRank rank) {
        super();
        this.m_rankId = rank.getId();
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(this.m_rankId);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_rankId = bb.getLong();
    }
    
    @Override
    public void compute(final GuildController controller) {
        try {
            controller.removeRank(this.m_rankId);
        }
        catch (GuildException e) {
            RemoveRankChange.m_logger.error((Object)"Impossible d'ajouter le rang", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.REMOVE_RANK;
    }
    
    @Override
    public String toString() {
        return "RemoveRankChange{m_rankId=" + this.m_rankId + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)RemoveRankChange.class);
    }
}
