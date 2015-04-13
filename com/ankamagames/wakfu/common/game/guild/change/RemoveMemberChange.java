package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class RemoveMemberChange implements GuildChange
{
    private static final Logger m_logger;
    private long m_memberId;
    
    RemoveMemberChange() {
        super();
    }
    
    RemoveMemberChange(final GuildMember member) {
        super();
        this.m_memberId = member.getId();
    }
    
    @Override
    public byte[] serialize() {
        final ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(this.m_memberId);
        return bb.array();
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_memberId = bb.getLong();
    }
    
    @Override
    public void compute(final GuildController controller) {
        try {
            controller.removeMember(this.m_memberId);
        }
        catch (GuildException e) {
            RemoveMemberChange.m_logger.error((Object)"Impossible de retirer le membre", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.REMOVE_MEMBER;
    }
    
    @Override
    public String toString() {
        return "RemoveMemberChange{m_memberId=" + this.m_memberId + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)RemoveMemberChange.class);
    }
}
