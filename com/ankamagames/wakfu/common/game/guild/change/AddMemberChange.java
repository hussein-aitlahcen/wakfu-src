package com.ankamagames.wakfu.common.game.guild.change;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.exception.*;

class AddMemberChange implements GuildChange
{
    private static final Logger m_logger;
    private GuildMember m_member;
    
    AddMemberChange() {
        super();
    }
    
    AddMemberChange(final GuildMember member) {
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
            controller.addMember(this.m_member);
        }
        catch (GuildException e) {
            AddMemberChange.m_logger.error((Object)"Impossible d'ajouter le membre", (Throwable)e);
        }
    }
    
    @Override
    public GuildChangeType getType() {
        return GuildChangeType.ADD_MEMBER;
    }
    
    @Override
    public String toString() {
        return "AddMemberChange{m_member=" + this.m_member + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddMemberChange.class);
    }
}
