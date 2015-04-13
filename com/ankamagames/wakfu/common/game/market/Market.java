package com.ankamagames.wakfu.common.game.market;

import com.ankamagames.wakfu.common.game.market.exception.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import gnu.trove.*;

public class Market
{
    private final int m_id;
    private MarketObserver m_observer;
    private final TLongObjectHashMap<MarketEntry> m_items;
    private final TLongObjectHashMap<TLongHashSet> m_itemsByCharacterId;
    private final TByteObjectHashMap<SortedList<MarketEntry>> m_itemsByExpiration;
    private final TLongObjectHashMap<SortedList<MarketEntry>> m_itemsByRefAndPackCheapest;
    private final TLongObjectHashMap<MarketEntry> m_ordered;
    private final TLongShortHashMap m_orderedQty;
    private final MarketHistory m_history;
    
    public Market(final int id) {
        super();
        this.m_items = new TLongObjectHashMap<MarketEntry>();
        this.m_itemsByCharacterId = new TLongObjectHashMap<TLongHashSet>();
        this.m_itemsByExpiration = new TByteObjectHashMap<SortedList<MarketEntry>>();
        this.m_itemsByRefAndPackCheapest = new TLongObjectHashMap<SortedList<MarketEntry>>();
        this.m_ordered = new TLongObjectHashMap<MarketEntry>();
        this.m_orderedQty = new TLongShortHashMap();
        this.m_history = new MarketHistory();
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void setObserver(final MarketObserver observer) {
        if (this.m_observer != null) {
            throw new MarketException("Un observer " + this.m_observer + " a d\u00e9j\u00e0 \u00e9t\u00e9 d\u00e9fini sur ce market");
        }
        this.m_observer = observer;
    }
    
    public void setHistoryValidator(final MarketHistoryValidator validator) {
        this.m_history.setValidator(validator);
    }
    
    public void addEntry(final MarketEntry entry) throws MarketException {
        final long entryId = entry.getId();
        if (this.m_items.containsKey(entryId)) {
            throw new MarketException("Une entr\u00e9e d'Id " + entryId + " existe d\u00e9j\u00e0");
        }
        this.m_items.put(entryId, entry);
        final long sellerId = entry.getSellerId();
        TLongHashSet characterItems = this.m_itemsByCharacterId.get(sellerId);
        if (characterItems == null) {
            this.m_itemsByCharacterId.put(sellerId, characterItems = new TLongHashSet());
        }
        characterItems.add(entryId);
        final AuctionDuration duration = entry.getDuration();
        SortedList<MarketEntry> expItems = this.m_itemsByExpiration.get(duration.id);
        if (expItems == null) {
            this.m_itemsByExpiration.put(duration.id, expItems = new SortedList<MarketEntry>(MarketEntryComparator.RELEASE_DATE_CRESCENT.comparator));
        }
        expItems.add(entry);
        final int refId = entry.getItemRefId();
        final byte packId = entry.getPackType().id;
        final long key = MathHelper.getLongFromTwoInt(refId, packId);
        SortedList<MarketEntry> cheapestItems = this.m_itemsByRefAndPackCheapest.get(key);
        if (cheapestItems == null) {
            this.m_itemsByRefAndPackCheapest.put(key, cheapestItems = new SortedList<MarketEntry>(MarketEntryComparator.PRICE_CRESCENT.comparator));
        }
        cheapestItems.add(entry);
        if (this.m_observer != null) {
            this.m_observer.entryAdded(this, entry);
        }
    }
    
    public MarketEntry removeEntry(final long entryId) throws MarketException {
        final MarketEntry entry = this.m_items.remove(entryId);
        if (entry == null) {
            throw new MarketException("Entr\u00e9e " + entryId + " introuvable");
        }
        final long sellerId = entry.getSellerId();
        final TLongHashSet characterItems = this.m_itemsByCharacterId.get(sellerId);
        characterItems.remove(entryId);
        if (characterItems.isEmpty()) {
            this.m_itemsByCharacterId.remove(sellerId);
        }
        final AuctionDuration duration = entry.getDuration();
        final SortedList<MarketEntry> expItems = this.m_itemsByExpiration.get(duration.id);
        expItems.remove(entry);
        if (expItems.isEmpty()) {
            this.m_itemsByExpiration.remove(duration.id);
        }
        final int refId = entry.getItemRefId();
        final byte packId = entry.getPackType().id;
        final long key = MathHelper.getLongFromTwoInt(refId, packId);
        final SortedList<MarketEntry> cheapestItems = this.m_itemsByRefAndPackCheapest.get(key);
        cheapestItems.remove(entry);
        if (cheapestItems.isEmpty()) {
            this.m_itemsByRefAndPackCheapest.remove(key);
        }
        if (this.m_observer != null) {
            this.m_observer.entryRemoved(this, entry);
        }
        return entry;
    }
    
    @Nullable
    public MarketEntry orderPack(final long buyerId, final long entryUid, final short packQty) throws MarketException {
        final MarketEntry order = this.m_ordered.get(buyerId);
        if (order != null) {
            throw new MarketException("Le joueur " + buyerId + " a d\u00e9j\u00e0 un ordre d'achat en attente pour l'entr\u00e9e " + order);
        }
        final MarketEntry entry = this.m_items.get(entryUid);
        if (entry == null) {
            throw new MarketException("Impossible de trouver l'entr\u00e9e " + entryUid + " pour l' ordre d'achat");
        }
        if (entry.getPackNumber() == packQty) {
            this.removeEntry(entry.getId());
        }
        else {
            entry.decreasePackNumber(packQty);
        }
        this.m_ordered.put(buyerId, entry);
        this.m_orderedQty.put(buyerId, packQty);
        return entry;
    }
    
    public void restoreOrder(final long buyerId) throws MarketException {
        final MarketEntry entry = this.m_ordered.remove(buyerId);
        final short packQty = this.m_orderedQty.remove(buyerId);
        if (entry == null) {
            throw new MarketException("Aucun ordre d'achat pour le joueur " + buyerId);
        }
        if (this.m_items.containsKey(entry.getId())) {
            entry.increasePackNumber(packQty);
        }
        else {
            this.addEntry(entry);
        }
    }
    
    public void purchaseOrder(final long buyerId) throws MarketException {
        final MarketEntry entry = this.m_ordered.remove(buyerId);
        final short packQty = this.m_orderedQty.remove(buyerId);
        if (entry == null) {
            throw new MarketException("Aucun ordre d'achat pour le joueur " + buyerId);
        }
        this.m_history.entrySold(entry, packQty);
        if (this.m_observer != null) {
            this.m_observer.entryPackPurchased(this, entry, packQty);
        }
    }
    
    public int fetchSaleCash(final long sellerId) throws MarketException {
        final int cash = this.m_history.fetchCash(sellerId);
        if (this.m_observer != null) {
            this.m_observer.entriesSalesFetched(this, sellerId);
        }
        return cash;
    }
    
    public MarketEntry[] fetchOutdatedEntries(final long sellerId) throws MarketException {
        final TLongObjectHashMap<MarketEntry> entries = this.m_history.getOutdatedEntries(sellerId);
        if (entries == null) {
            throw new MarketException("Aucune entr\u00e9e expir\u00e9e a r\u00e9cup\u00e9rer pour le joueur " + sellerId);
        }
        final long[] entryIds = entries.keys();
        final MarketEntry[] fetched = new MarketEntry[entryIds.length];
        for (int i = 0, size = entryIds.length; i < size; ++i) {
            fetched[i] = this.fetchOutdatedEntry(sellerId, entryIds[i]);
        }
        return fetched;
    }
    
    public MarketEntry fetchOutdatedEntry(final long sellerId, final long entryId) throws MarketException {
        final MarketEntry entry = this.m_history.fetchOutdatedEntry(sellerId, entryId);
        if (this.m_observer != null) {
            this.m_observer.entryOutdatedFetched(this, sellerId, entry);
        }
        return entry;
    }
    
    public void updateOutdatedEntries(final GameDateProvider calendar) {
        final AuctionDuration[] values = AuctionDuration.values();
        for (int i = 0, size = values.length; i < size; ++i) {
            final AuctionDuration duration = values[i];
            this.updateOutdatedEntries(calendar, duration);
        }
    }
    
    private void updateOutdatedEntries(final GameDateProvider calendar, final AuctionDuration duration) {
        final SortedList<MarketEntry> entries = this.m_itemsByExpiration.get(duration.id);
        final long currentDate = calendar.getDate().toLong();
        while (entries != null && !entries.isEmpty() && entries.getFirst().isExpired(currentDate)) {
            final MarketEntry entry = entries.remove(0);
            this.removeEntry(entry.getId());
            this.m_history.addOutdatedEntry(entry);
            if (this.m_observer != null) {
                this.m_observer.entryOutdated(this, entry);
            }
        }
    }
    
    public MarketEntry getEntry(final long entryId) {
        return this.m_items.get(entryId);
    }
    
    public TLongHashSet getEntries(final long characterId) {
        return this.m_itemsByCharacterId.get(characterId);
    }
    
    public int getEntrySize(final long sellerId) {
        final TLongHashSet entries = this.getEntries(sellerId);
        return (entries != null) ? entries.size() : 0;
    }
    
    public MarketEntry getOrder(final long buyerId) {
        return this.m_ordered.get(buyerId);
    }
    
    public void addSaleEntry(final long sellerId, final MarketHistoryEntry historyEntry) {
        this.m_history.addSaleEntry(sellerId, historyEntry);
    }
    
    public void removeHistoryEntries(final long sellerId) {
        this.m_history.removeEntries(sellerId);
    }
    
    public void removeEntries(final long sellerId) {
        final TLongHashSet entries = this.m_itemsByCharacterId.get(sellerId);
        if (entries == null) {
            return;
        }
        final long[] intEntries = entries.toArray();
        for (int i = 0, size = intEntries.length; i < size; ++i) {
            final long entryId = intEntries[i];
            this.removeEntry(entryId);
        }
    }
    
    public MarketHistoryEntry getSaleEntry(final long sellerId, final long entryId) {
        return this.m_history.getSaleEntry(sellerId, entryId);
    }
    
    public int getSaleSize(final long sellerId) {
        return this.m_history.getSaleSize(sellerId);
    }
    
    public void addOutdatedEntry(final MarketEntry entry) {
        this.m_history.addOutdatedEntry(entry);
    }
    
    public MarketEntry getOutdatedEntry(final long sellerId, final long entryId) {
        return this.m_history.getOutdatedEntry(sellerId, entryId);
    }
    
    public int getOutdatedSize(final long sellerId) {
        return this.m_history.getOutdatedSize(sellerId);
    }
    
    public int size() {
        return this.m_items.size();
    }
    
    public int sellerSize() {
        return this.m_itemsByCharacterId.size();
    }
    
    public int orderSize() {
        return this.m_ordered.size();
    }
    
    @Nullable
    public MarketEntry getCheapest(final int refId, final PackType packType) {
        final long key = MathHelper.getLongFromTwoInt(refId, packType.id);
        final SortedList<MarketEntry> entries = this.m_itemsByRefAndPackCheapest.get(key);
        return (entries == null) ? null : entries.getFirst();
    }
    
    public void clear() {
        this.m_items.clear();
        this.m_itemsByCharacterId.clear();
        this.m_ordered.clear();
        this.m_orderedQty.clear();
        this.m_history.clear();
    }
    
    public byte[] toRawClient(final MarketEntryValidator validator, final MarketEntryComparator comp, final short startIndex, final short endIndex) {
        final List<MarketEntry> entries = new ArrayList<MarketEntry>();
        final TLongObjectIterator<MarketEntry> it = this.m_items.iterator();
        while (it.hasNext()) {
            it.advance();
            final MarketEntry entry = it.value();
            if (validator.isValid(entry)) {
                entries.add(entry);
            }
        }
        if (comp != null) {
            Collections.sort(entries, comp.comparator);
        }
        final int startId = Math.max(startIndex, 0);
        final int endId = Math.min(endIndex, entries.size());
        final ByteArray bb = new ByteArray();
        bb.putInt(endId - startId);
        for (int i = startId; i < endId; ++i) {
            bb.put(entries.get(i).toRaw());
        }
        return bb.toArray();
    }
    
    public byte[] toRawByCharacter(final long characterId) {
        final TLongHashSet entries = this.m_itemsByCharacterId.get(characterId);
        final ByteArray bb = new ByteArray();
        final int count = (entries == null) ? 0 : entries.size();
        bb.putInt(count);
        if (entries != null) {
            final TLongIterator it = entries.iterator();
            while (it.hasNext()) {
                bb.put(this.m_items.get(it.next()).toRaw());
            }
        }
        return bb.toArray();
    }
    
    public byte[] toRawHistory(final long characterId) {
        return this.m_history.toRaw(characterId);
    }
    
    @Override
    public String toString() {
        return "Market{m_id=" + this.m_id + ", m_items=" + this.m_items.size() + ", m_itemsByCharacterId=" + this.m_itemsByCharacterId.size() + ", m_itemsByExpiration=" + this.m_itemsByExpiration.size() + ", m_itemsByRefAndPackCheapest=" + this.m_itemsByRefAndPackCheapest.size() + ", m_ordered=" + this.m_ordered.size() + ", m_orderedQty=" + this.m_orderedQty.size() + '}';
    }
}
