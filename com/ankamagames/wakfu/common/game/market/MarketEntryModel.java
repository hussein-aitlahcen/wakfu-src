package com.ankamagames.wakfu.common.game.market;

import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

class MarketEntryModel implements MarketEntry
{
    private long m_id;
    private long m_sellerId;
    private String m_sellerName;
    private RawInventoryItem m_rawItem;
    private byte m_packTypeId;
    private short m_packNumber;
    private int m_packPrice;
    private byte m_durationId;
    private long m_releaseDate;
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public long getSellerId() {
        return this.m_sellerId;
    }
    
    @Override
    public String getSellerName() {
        return this.m_sellerName;
    }
    
    @Override
    public int getItemRefId() {
        return this.m_rawItem.refId;
    }
    
    @Override
    public PackType getPackType() {
        return PackType.fromId(this.m_packTypeId);
    }
    
    @Override
    public short getPackNumber() {
        return this.m_packNumber;
    }
    
    @Override
    public void decreasePackNumber(final short qty) {
        this.m_packNumber -= qty;
    }
    
    @Override
    public void increasePackNumber(final short qty) {
        this.m_packNumber += qty;
    }
    
    @Override
    public int getPackPrice() {
        return this.m_packPrice;
    }
    
    @Override
    public AuctionDuration getDuration() {
        return AuctionDuration.fromId(this.m_durationId);
    }
    
    @Override
    public long getReleaseDate() {
        return this.m_releaseDate;
    }
    
    @Override
    public RawInventoryItem getRawItem() {
        return this.m_rawItem;
    }
    
    @Override
    public boolean isExpired(final long currentTime) {
        final long expirationDelay = this.getDuration().timeMs;
        final long delta = currentTime - this.m_releaseDate;
        return delta > expirationDelay;
    }
    
    @Override
    public int compareReleaseDate(final MarketEntry entry) {
        return (int)(this.m_releaseDate - entry.getReleaseDate());
    }
    
    @Override
    public int compareRemainingTime(final MarketEntry o2) {
        return (int)(this.getDuration().timeMs + this.m_releaseDate - (o2.getDuration().timeMs + o2.getReleaseDate()));
    }
    
    @Override
    public byte[] toRaw() {
        final byte[] utf = StringUtils.toUTF8(this.m_sellerName);
        final ByteBuffer bb = ByteBuffer.allocate(18 + utf.length + 1 + 2 + 4 + 1 + 8 + this.m_rawItem.serializedSize());
        bb.putLong(this.m_id);
        bb.putLong(this.m_sellerId);
        bb.putShort((short)utf.length);
        bb.put(utf);
        bb.put(this.m_packTypeId);
        bb.putShort(this.m_packNumber);
        bb.putInt(this.m_packPrice);
        bb.put(this.m_durationId);
        bb.putLong(this.m_releaseDate);
        this.m_rawItem.serialize(bb);
        return bb.array();
    }
    
    void setId(final long id) {
        this.m_id = id;
    }
    
    void setSellerId(final long sellerId) {
        this.m_sellerId = sellerId;
    }
    
    void setSellerName(final String sellerName) {
        this.m_sellerName = sellerName;
    }
    
    void setPackTypeId(final byte packTypeId) {
        this.m_packTypeId = packTypeId;
    }
    
    void setPackNumber(final short packNumber) {
        this.m_packNumber = packNumber;
    }
    
    void setPackPrice(final int packPrice) {
        this.m_packPrice = packPrice;
    }
    
    void setDurationId(final byte durationId) {
        this.m_durationId = durationId;
    }
    
    void setReleaseDate(final long releaseDate) {
        this.m_releaseDate = releaseDate;
    }
    
    public void setRawItem(final RawInventoryItem rawItem) {
        this.m_rawItem = rawItem;
    }
    
    @Override
    public String toString() {
        return "MarketEntryModel{m_id=" + this.m_id + ", m_sellerId=" + this.m_sellerId + ", m_sellerName='" + this.m_sellerName + '\'' + ", m_rawItem=" + this.m_rawItem.refId + ", m_packTypeId=" + this.m_packTypeId + ", m_packNumber=" + this.m_packNumber + ", m_packPrice=" + this.m_packPrice + ", m_durationId=" + this.m_durationId + ", m_releaseDate=" + this.m_releaseDate + '}';
    }
}
