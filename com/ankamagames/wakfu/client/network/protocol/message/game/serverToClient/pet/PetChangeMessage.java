package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.pet;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.pet.change.*;
import java.util.*;

public class PetChangeMessage extends InputOnlyProxyMessage
{
    private long m_itemId;
    private final Collection<PetChange> m_changes;
    
    public PetChangeMessage() {
        super();
        this.m_changes = new ArrayList<PetChange>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_itemId = bb.getLong();
        while (bb.hasRemaining()) {
            final PetChangeType type = PetChangeType.fromId(bb.get());
            final PetChange event = type.createNew();
            event.unSerialize(bb);
            this.m_changes.add(event);
        }
        return true;
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
    
    public Iterator<PetChange> changesIterator() {
        return this.m_changes.iterator();
    }
    
    @Override
    public int getId() {
        return 15982;
    }
    
    @Override
    public String toString() {
        return "PetChangeMessage{m_itemId=" + this.m_itemId + ", m_changes=" + this.m_changes.size() + '}';
    }
}
