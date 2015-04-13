package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class ModifyMemberChange implements GuildChange
{
    private static final Logger m_logger;
    private GuildMember m_member;
    
    ModifyMemberChange() {
        super();
    }
    
    ModifyMemberChange(final GuildMember member) {
        super();
        this.m_member = member;
    }
    
    @Override
    public byte[] serialize() {
        return GuildSerializer.serializeMember(this.m_member);
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
        this.m_member = GuildSerializer.unSerializeMember(bb);
    }
    
    @Override
    public void compute(final GuildController controller) {
        try {
            controller.changeMemberGuildPoints(this.m_member.getId(), this.m_member.getGuildPoints());
            controller.changeMemberXp(this.m_member.getId(), this.m_member.getXp());
            controller.changeMemberRank(this.m_member.getId(), this.m_member.getRank());
            controller.changeMemberConnected(this.m_member.getId(), this.m_member.isConnected());
            controller.changeMemberSmiley(this.m_member.getId(), this.m_member.getSmiley());
            controller.changeMemberNation(this.m_member.getId(), this.m_member.getNationId());
        }
        catch (GuildException e) {
            ModifyMemberChange.m_logger.error((Object)"Impossible de modifier le membre", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.MODIFY_MEMBER;
    }
    
    @Override
    public String toString() {
        return "ModifyMemberChange{m_member=" + this.m_member + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)ModifyMemberChange.class);
    }
}
