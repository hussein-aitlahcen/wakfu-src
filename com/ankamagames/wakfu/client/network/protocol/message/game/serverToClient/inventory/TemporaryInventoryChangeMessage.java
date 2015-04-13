package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.inventory;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.change.temporaryInventory.*;
import java.util.*;

public class TemporaryInventoryChangeMessage extends InputOnlyProxyMessage
{
    private final Collection<TemporaryInventoryChange> m_changes;
    private long m_characterId;
    
    public TemporaryInventoryChangeMessage() {
        super();
        this.m_changes = new ArrayList<TemporaryInventoryChange>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_characterId = bb.getLong();
        while (bb.hasRemaining()) {
            final TemporaryInventoryChangeType type = TemporaryInventoryChangeType.fromId(bb.get());
            final TemporaryInventoryChange event = type.createNew();
            event.unSerialize(bb);
            this.m_changes.add(event);
        }
        return true;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public Iterator<TemporaryInventoryChange> changesIterator() {
        return this.m_changes.iterator();
    }
    
    @Override
    public int getId() {
        return 15999;
    }
    
    @Override
    public String toString() {
        return "TemporaryInventoryChangeMessage{m_changes=" + this.m_changes.size() + '}';
    }
}
