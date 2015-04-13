package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class MoveRankChange implements GuildChange
{
    private static final Logger m_logger;
    private GuildRank m_rank;
    
    MoveRankChange() {
        super();
    }
    
    MoveRankChange(final GuildRank rank) {
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
            controller.moveRank(this.m_rank.getId(), this.m_rank.getPosition());
        }
        catch (GuildException e) {
            MoveRankChange.m_logger.error((Object)"Impossible de modifier le rang", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.MOVE_RANK;
    }
    
    @Override
    public String toString() {
        return "ModifyRankChange{m_rank=" + this.m_rank + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)MoveRankChange.class);
    }
}
