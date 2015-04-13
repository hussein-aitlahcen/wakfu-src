package com.ankamagames.wakfu.common.game.market;

import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import java.nio.*;

public class MarketHistoryEntry
{
    private final long m_id;
    private final int m_refId;
    private final byte m_packTypeId;
    private final int m_packPrice;
    private final RawInventoryItem m_rawInventoryItem;
    private short m_packSoldNumber;
    
    public MarketHistoryEntry(final long id, final int refId, final PackType packType, final int packPrice, final RawInventoryItem rawInventoryItem) {
        super();
        this.m_id = id;
        this.m_refId = refId;
        this.m_packTypeId = packType.id;
        this.m_packPrice = packPrice;
        this.m_rawInventoryItem = rawInventoryItem;
        this.m_packSoldNumber = 0;
    }
    
    public MarketHistoryEntry(final long id, final int refId, final PackType packType, final int packPrice, final short packSoldNumber, final RawInventoryItem rawInventoryItem) {
        super();
        this.m_id = id;
        this.m_refId = refId;
        this.m_packTypeId = packType.id;
        this.m_packPrice = packPrice;
        this.m_packSoldNumber = packSoldNumber;
        this.m_rawInventoryItem = rawInventoryItem;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public int getItemRefId() {
        return this.m_refId;
    }
    
    public PackType getPackType() {
        return PackType.fromId(this.m_packTypeId);
    }
    
    public int getPackPrice() {
        return this.m_packPrice;
    }
    
    public short getPackSoldNumber() {
        return this.m_packSoldNumber;
    }
    
    public int getTotalPrice() {
        return this.m_packSoldNumber * this.m_packPrice;
    }
    
    public void incrementPackSoldNumber(final short packQty) {
        this.m_packSoldNumber += packQty;
    }
    
    public RawInventoryItem getRawInventoryItem() {
        return this.m_rawInventoryItem;
    }
    
    public byte[] toRaw() {
        final int rawItemSize = (this.m_rawInventoryItem == null) ? 0 : this.m_rawInventoryItem.serializedSize();
        final ByteBuffer bb = ByteBuffer.allocate(20 + rawItemSize);
        bb.putLong(this.m_id);
        bb.putInt(this.m_refId);
        bb.put(this.m_packTypeId);
        bb.putInt(this.m_packPrice);
        bb.putShort(this.m_packSoldNumber);
        bb.put((byte)((this.m_rawInventoryItem != null) ? 1 : 0));
        if (this.m_rawInventoryItem != null) {
            this.m_rawInventoryItem.serialize(bb);
        }
        return bb.array();
    }
    
    public static MarketHistoryEntry fromRaw(final ByteBuffer bb) {
        final long id = bb.getLong();
        final int refId = bb.getInt();
        final PackType packType = PackType.fromId(bb.get());
        final int packPrice = bb.getInt();
        final short packSoldNumber = bb.getShort();
        RawInventoryItem rawInventoryItem = null;
        if (bb.get() == 1) {
            rawInventoryItem = new RawInventoryItem();
            rawInventoryItem.unserialize(bb);
        }
        return new MarketHistoryEntry(id, refId, packType, packPrice, packSoldNumber, rawInventoryItem);
    }
    
    @Override
    public String toString() {
        return "MarketHistoryEntry{m_id=" + this.m_id + ", m_packTypeId=" + this.m_packTypeId + ", m_packPrice=" + this.m_packPrice + ", m_rawInventoryItem=" + this.m_rawInventoryItem + ", m_packSoldNumber=" + this.m_packSoldNumber + '}';
    }
}
