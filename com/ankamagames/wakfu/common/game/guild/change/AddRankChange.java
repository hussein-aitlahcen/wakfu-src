package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class AddRankChange implements GuildChange
{
    private static final Logger m_logger;
    private GuildRank m_rank;
    
    AddRankChange() {
        super();
    }
    
    AddRankChange(final GuildRank rank) {
        super();
        this.m_rank = rank;
    }
    
    @Override
    public byte[] serialize() {
        return GuildSerializer.serializeRank(this.m_rank);
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_rank = GuildSerializer.unSerializeRank(bb);
    }
    
    @Override
    public void compute(final GuildController controller) {
        try {
            controller.addRank(this.m_rank);
        }
        catch (GuildException e) {
            AddRankChange.m_logger.error((Object)"Impossible d'ajouter le rang", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.ADD_RANK;
    }
    
    @Override
    public String toString() {
        return "AddRankChange{m_rank=" + this.m_rank + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddRankChange.class);
    }
}
