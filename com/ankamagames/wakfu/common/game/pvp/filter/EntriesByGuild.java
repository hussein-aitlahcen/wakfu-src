package com.ankamagames.wakfu.common.game.pvp.filter;

import java.nio.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class EntriesByGuild extends NationPvpLadderFilterParam
{
    private long m_guildId;
    
    public EntriesByGuild(final ByteBuffer bb) {
        super();
        this.unserialize(bb);
    }
    
    public EntriesByGuild(final int pageNum, final int pageSize, final long guildId) {
        super(pageNum, pageSize);
        this.m_guildId = guildId;
    }
    
    public long getGuildId() {
        return this.m_guildId;
    }
    
    @Override
    void serialize(final ByteArray bb) {
        super.serialize(bb);
        bb.putLong(this.m_guildId);
    }
    
    @Override
    final void unserialize(final ByteBuffer bb) {
        super.unserialize(bb);
        this.m_guildId = bb.getLong();
    }
    
    @Override
    public FilterParamType getType() {
        return FilterParamType.GUILD;
    }
    
    @Override
    public String toString() {
        return "EntriesByGuild{m_guildId=" + this.m_guildId + '}';
    }
}
