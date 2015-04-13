package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.inventory;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.change.cosmetics.*;
import java.util.*;

public class CosmeticsInventoryChangeMessage extends InputOnlyProxyMessage
{
    private final Collection<CosmeticsInventoryChange> m_changes;
    private InventoryType m_inventoryType;
    
    public CosmeticsInventoryChangeMessage() {
        super();
        this.m_changes = new ArrayList<CosmeticsInventoryChange>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final InventoryType[] values = InventoryType.values();
        final int ordinal = bb.getInt();
        if (ordinal >= 0 && ordinal < values.length) {
            this.m_inventoryType = values[ordinal];
        }
        while (bb.hasRemaining()) {
            final CosmeticsInventoryChangeType type = CosmeticsInventoryChangeType.fromId(bb.get());
            final CosmeticsInventoryChange event = type.createNew();
            event.unSerialize(bb);
            this.m_changes.add(event);
        }
        return true;
    }
    
    public InventoryType getInventoryType() {
        return this.m_inventoryType;
    }
    
    public Iterator<CosmeticsInventoryChange> changesIterator() {
        return this.m_changes.iterator();
    }
    
    @Override
    public int getId() {
        return 15997;
    }
    
    @Override
    public String toString() {
        return "CosmeticsInventoryChangeMessage{m_changes=" + this.m_changes + ", m_inventoryType=" + this.m_inventoryType + '}';
    }
}
