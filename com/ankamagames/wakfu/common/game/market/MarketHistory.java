package com.ankamagames.wakfu.common.game.market;

import com.ankamagames.wakfu.common.game.market.exception.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;

public class MarketHistory
{
    private final TLongObjectHashMap<TLongObjectHashMap<MarketHistoryEntry>> m_sales;
    private final TLongObjectHashMap<TLongObjectHashMap<MarketEntry>> m_outdated;
    private MarketHistoryValidator m_validator;
    
    public MarketHistory() {
        super();
        this.m_sales = new TLongObjectHashMap<TLongObjectHashMap<MarketHistoryEntry>>();
        this.m_outdated = new TLongObjectHashMap<TLongObjectHashMap<MarketEntry>>();
    }
    
    public void setValidator(final MarketHistoryValidator validator) {
        if (this.m_validator != null) {
            throw new MarketException("Un validator " + this.m_validator + " a d\u00e9j\u00e0 \u00e9t\u00e9 d\u00e9fini");
        }
        this.m_validator = validator;
    }
    
    void entrySold(final MarketEntry entry, final short packQty) {
        if (this.m_validator != null && !this.m_validator.isValidForSales(entry)) {
            return;
        }
        final long sellerId = entry.getSellerId();
        final long entryId = entry.getId();
        TLongObjectHashMap<MarketHistoryEntry> histEntries = this.m_sales.get(sellerId);
        if (histEntries == null) {
            this.m_sales.put(sellerId, histEntries = new TLongObjectHashMap<MarketHistoryEntry>());
        }
        MarketHistoryEntry histEntry = histEntries.get(entryId);
        if (histEntry == null) {
            histEntries.put(entryId, histEntry = new MarketHistoryEntry(entryId, entry.getItemRefId(), entry.getPackType(), entry.getPackPrice(), entry.getRawItem()));
        }
        histEntry.incrementPackSoldNumber(packQty);
    }
    
    int fetchCash(final long sellerId) throws MarketException {
        final TLongObjectHashMap<MarketHistoryEntry> sales = this.m_sales.remove(sellerId);
        if (sales == null) {
            throw new MarketException("Aucune vente stock\u00e9e pour le joueur " + sellerId);
        }
        int cash = 0;
        final TLongObjectIterator<MarketHistoryEntry> it = sales.iterator();
        while (it.hasNext()) {
            it.advance();
            final MarketHistoryEntry entry = it.value();
            cash += entry.getTotalPrice();
        }
        return cash;
    }
    
    MarketEntry fetchOutdatedEntry(final long sellerId, final long entryId) throws MarketException {
        final TLongObjectHashMap<MarketEntry> outs = this.m_outdated.get(sellerId);
        if (outs == null) {
            throw new MarketException("Aucune expiration stock\u00e9e pour le joueur " + sellerId);
        }
        final MarketEntry entry = outs.remove(entryId);
        if (entry == null) {
            throw new MarketException("Aucune expiration d'Id " + entryId + " stock\u00e9e pour le joueur " + sellerId);
        }
        if (outs.isEmpty()) {
            this.m_outdated.remove(sellerId);
        }
        return entry;
    }
    
    void addOutdatedEntry(final MarketEntry entry) {
        final long sellerId = entry.getSellerId();
        TLongObjectHashMap<MarketEntry> outs = this.m_outdated.get(sellerId);
        if (outs == null) {
            this.m_outdated.put(sellerId, outs = new TLongObjectHashMap<MarketEntry>());
        }
        outs.put(entry.getId(), entry);
    }
    
    void addSaleEntry(final long sellerId, final MarketHistoryEntry entry) {
        TLongObjectHashMap<MarketHistoryEntry> histEntries = this.m_sales.get(sellerId);
        if (histEntries == null) {
            this.m_sales.put(sellerId, histEntries = new TLongObjectHashMap<MarketHistoryEntry>());
        }
        histEntries.put(entry.getId(), entry);
    }
    
    void removeEntries(final long sellerId) {
        this.m_sales.remove(sellerId);
        this.m_outdated.remove(sellerId);
    }
    
    TLongObjectHashMap<MarketHistoryEntry> getSaleEntries(final long sellerId) {
        return this.m_sales.get(sellerId);
    }
    
    MarketHistoryEntry getSaleEntry(final long sellerId, final long entryId) {
        final TLongObjectHashMap<MarketHistoryEntry> items = this.m_sales.get(sellerId);
        return (items != null) ? items.get(entryId) : null;
    }
    
    public int getSaleSize(final long sellerId) {
        final TLongObjectHashMap<MarketHistoryEntry> sales = this.m_sales.get(sellerId);
        return (sales == null) ? 0 : sales.size();
    }
    
    TLongObjectHashMap<MarketEntry> getOutdatedEntries(final long sellerId) {
        return this.m_outdated.get(sellerId);
    }
    
    MarketEntry getOutdatedEntry(final long sellerId, final long entryId) {
        final TLongObjectHashMap<MarketEntry> items = this.m_outdated.get(sellerId);
        return (items != null) ? items.get(entryId) : null;
    }
    
    public int getOutdatedSize(final long sellerId) {
        final TLongObjectHashMap<MarketEntry> outdated = this.m_outdated.get(sellerId);
        return (outdated == null) ? 0 : outdated.size();
    }
    
    void clear() {
        this.m_sales.clear();
        this.m_outdated.clear();
    }
    
    byte[] toRaw(final long sellerId) {
        final TLongObjectHashMap<MarketHistoryEntry> sales = this.m_sales.get(sellerId);
        final ByteArray bb = new ByteArray();
        if (sales == null || sales.isEmpty()) {
            bb.putInt(0);
        }
        else {
            bb.putInt(sales.size());
            final TLongObjectIterator<MarketHistoryEntry> it = sales.iterator();
            while (it.hasNext()) {
                it.advance();
                bb.put(it.value().toRaw());
            }
        }
        final TLongObjectHashMap<MarketEntry> outdated = this.m_outdated.get(sellerId);
        if (outdated == null || outdated.isEmpty()) {
            bb.putInt(0);
        }
        else {
            bb.putInt(outdated.size());
            final TLongObjectIterator<MarketEntry> it2 = outdated.iterator();
            while (it2.hasNext()) {
                it2.advance();
                bb.put(it2.value().toRaw());
            }
        }
        return bb.toArray();
    }
    
    @Override
    public String toString() {
        return "MarketHistory{m_sales=" + this.m_sales.size() + ", m_outdated=" + this.m_outdated.size() + ", m_validator=" + this.m_validator + '}';
    }
}
