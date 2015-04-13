package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guild;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.guild.change.*;
import java.util.*;

public class GuildChangeMessage extends InputOnlyProxyMessage
{
    private final Collection<GuildChange> m_changes;
    
    public GuildChangeMessage() {
        super();
        this.m_changes = new ArrayList<GuildChange>();
    }
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        while (bb.hasRemaining()) {
            final GuildChangeType type = GuildChangeType.fromId(bb.get());
            final GuildChange event = type.createNew();
            event.unSerialize(bb);
            this.m_changes.add(event);
        }
        return true;
    }
    
    public Iterator<GuildChange> changesIterator() {
        return this.m_changes.iterator();
    }
    
    @Override
    public int getId() {
        return 20050;
    }
    
    @Override
    public String toString() {
        return "GuildChangeMessage{m_changes=" + this.m_changes.size() + '}';
    }
}
