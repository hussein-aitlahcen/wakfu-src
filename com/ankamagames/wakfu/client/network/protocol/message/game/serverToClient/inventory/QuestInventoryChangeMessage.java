package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.inventory;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.change.quest.*;
import java.util.*;

public class QuestInventoryChangeMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private final Collection<QuestInventoryChange> m_changes;
    
    public QuestInventoryChangeMessage() {
        super();
        this.m_changes = new ArrayList<QuestInventoryChange>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_characterId = bb.getLong();
        while (bb.hasRemaining()) {
            final QuestInventoryChangeType type = QuestInventoryChangeType.fromId(bb.get());
            final QuestInventoryChange event = type.createNew();
            event.unSerialize(bb);
            this.m_changes.add(event);
        }
        return true;
    }
    
    public Iterator<QuestInventoryChange> changesIterator() {
        return this.m_changes.iterator();
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    @Override
    public int getId() {
        return 15998;
    }
    
    @Override
    public String toString() {
        return "QuestInventoryChangeMessage{m_changes=" + this.m_changes.size() + '}';
    }
}
