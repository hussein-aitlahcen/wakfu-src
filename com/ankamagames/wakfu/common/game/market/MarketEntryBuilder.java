package com.ankamagames.wakfu.common.game.market;

import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.kernel.utils.*;

public class MarketEntryBuilder
{
    private MarketEntryModel m_entry;
    
    public MarketEntryBuilder createMarketEntry() {
        this.m_entry = new MarketEntryModel();
        return this;
    }
    
    public MarketEntry getMarketEntry() {
        if (this.m_entry == null) {
            throw new UnsupportedOperationException("L'objet n'a pas \u00e9t\u00e9 cr\u00e9\u00e9");
        }
        return this.m_entry;
    }
    
    public MarketEntryBuilder setId(final long id) {
        this.m_entry.setId(id);
        return this;
    }
    
    public MarketEntryBuilder setSellerId(final long sellerId) {
        this.m_entry.setSellerId(sellerId);
        return this;
    }
    
    public MarketEntryBuilder setSellerName(final String sellerName) {
        this.m_entry.setSellerName(sellerName);
        return this;
    }
    
    public MarketEntryBuilder setPackType(final PackType packType) {
        this.m_entry.setPackTypeId(packType.id);
        return this;
    }
    
    public MarketEntryBuilder setPackNumber(final short packNumber) {
        this.m_entry.setPackNumber(packNumber);
        return this;
    }
    
    public MarketEntryBuilder setPackPrice(final int packPrice) {
        this.m_entry.setPackPrice(packPrice);
        return this;
    }
    
    public MarketEntryBuilder setDuration(final AuctionDuration duration) {
        this.m_entry.setDurationId(duration.id);
        return this;
    }
    
    public MarketEntryBuilder setReleaseDate(final long releaseDate) {
        this.m_entry.setReleaseDate(releaseDate);
        return this;
    }
    
    public MarketEntryBuilder setRawItem(final RawInventoryItem rawInventoryItem) {
        this.m_entry.setRawItem(rawInventoryItem);
        return this;
    }
    
    public void clear() {
        this.m_entry = null;
    }
    
    public static ArrayList<MarketEntry> unSerializeList(final ByteBuffer bb) {
        final ArrayList<MarketEntry> entries = new ArrayList<MarketEntry>();
        for (int i = 0, size = bb.getInt(); i < size; ++i) {
            entries.add(unSerialize(bb));
        }
        return entries;
    }
    
    public static MarketEntry unSerialize(final ByteBuffer bb) {
        final MarketEntryModel entry = new MarketEntryModel();
        entry.setId(bb.getLong());
        entry.setSellerId(bb.getLong());
        final byte[] utf = new byte[bb.getShort()];
        bb.get(utf);
        final String sellerName = StringUtils.fromUTF8(utf);
        entry.setSellerName(sellerName);
        entry.setPackTypeId(bb.get());
        entry.setPackNumber(bb.getShort());
        entry.setPackPrice(bb.getInt());
        entry.setDurationId(bb.get());
        entry.setReleaseDate(bb.getLong());
        final RawInventoryItem rawInventoryItem = new RawInventoryItem();
        rawInventoryItem.unserialize(bb);
        entry.setRawItem(rawInventoryItem);
        return entry;
    }
    
    @Override
    public String toString() {
        return "MarketEntryBuilder{m_entry=" + this.m_entry + '}';
    }
}
