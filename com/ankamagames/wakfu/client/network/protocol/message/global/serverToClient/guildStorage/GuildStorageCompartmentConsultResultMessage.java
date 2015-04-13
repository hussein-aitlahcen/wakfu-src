package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guildStorage;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class GuildStorageCompartmentConsultResultMessage extends InputOnlyProxyMessage
{
    private final ArrayList<RawGuildStorageCompartment> m_compartments;
    private RawGuildStorageHistory m_history;
    
    public GuildStorageCompartmentConsultResultMessage() {
        super();
        this.m_compartments = new ArrayList<RawGuildStorageCompartment>();
    }
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final byte numCompartments = bb.get();
        for (int i = 0; i < numCompartments; ++i) {
            final RawGuildStorageCompartment compartment = new RawGuildStorageCompartment();
            compartment.unserialize(bb);
            this.m_compartments.add(compartment);
        }
        (this.m_history = new RawGuildStorageHistory()).unserialize(bb);
        return true;
    }
    
    public ArrayList<RawGuildStorageCompartment> getCompartments() {
        return this.m_compartments;
    }
    
    public RawGuildStorageHistory getHistory() {
        return this.m_history;
    }
    
    @Override
    public int getId() {
        return 20078;
    }
}
