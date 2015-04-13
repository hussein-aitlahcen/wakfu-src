package com.ankamagames.wakfu.common.game.guild.storage;

import java.util.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class GuildStorageHistory
{
    private static final int HISTORY_SIZE = 20;
    private final LinkedList<GuildStorageHistoryItemEntry> m_itemHistory;
    private final LinkedList<GuildStorageHistoryMoneyEntry> m_moneyHistory;
    
    public GuildStorageHistory() {
        super();
        this.m_itemHistory = new LinkedList<GuildStorageHistoryItemEntry>();
        this.m_moneyHistory = new LinkedList<GuildStorageHistoryMoneyEntry>();
    }
    
    public void addEntry(final GuildStorageHistoryItemEntry entry) {
        this.m_itemHistory.addFirst(entry);
        while (this.m_itemHistory.size() > 20) {
            this.m_itemHistory.removeLast();
        }
    }
    
    public void addEntry(final GuildStorageHistoryMoneyEntry entry) {
        this.m_moneyHistory.addFirst(entry);
        while (this.m_moneyHistory.size() > 20) {
            this.m_moneyHistory.removeLast();
        }
    }
    
    public void addAll(final GuildStorageHistory history) {
        this.addAllItem(history);
        this.addAllMoney(history);
    }
    
    private void addAllItem(final GuildStorageHistory history) {
        for (int i = 0, size = history.m_itemHistory.size(); i < size; ++i) {
            this.addEntry(history.m_itemHistory.get(i));
        }
    }
    
    private void addAllMoney(final GuildStorageHistory history) {
        for (int i = 0, size = history.m_moneyHistory.size(); i < size; ++i) {
            this.addEntry(history.m_moneyHistory.get(i));
        }
    }
    
    public boolean forEachItem(final TObjectProcedure<GuildStorageHistoryItemEntry> proc) {
        for (int i = 0, size = this.m_itemHistory.size(); i < size; ++i) {
            if (!proc.execute(this.m_itemHistory.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachMoney(final TObjectProcedure<GuildStorageHistoryMoneyEntry> proc) {
        for (int i = 0, size = this.m_moneyHistory.size(); i < size; ++i) {
            if (!proc.execute(this.m_moneyHistory.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public void toRaw(final RawGuildStorageHistory rawHistory) {
        this.toRawItem(rawHistory);
        this.toRawMoney(rawHistory);
    }
    
    private void toRawItem(final RawGuildStorageHistory rawHistory) {
        for (int i = 0, size = this.m_itemHistory.size(); i < size; ++i) {
            final GuildStorageHistoryItemEntry entry = this.m_itemHistory.get(i);
            final RawGuildStorageHistory.HistoryItemEntry rawEntry = new RawGuildStorageHistory.HistoryItemEntry();
            rawEntry.memberName = entry.getMemberName();
            rawEntry.date = entry.getDate();
            rawEntry.qty = entry.getQty();
            final ByteBuffer bb = ByteBuffer.allocate(entry.getItem().serializedSize());
            entry.getItem().serialize(bb);
            bb.rewind();
            rawEntry.item.unserialize(bb);
            rawHistory.itemHistory.add(rawEntry);
        }
    }
    
    private void toRawMoney(final RawGuildStorageHistory rawHistory) {
        for (int i = 0, size = this.m_moneyHistory.size(); i < size; ++i) {
            final GuildStorageHistoryMoneyEntry entry = this.m_moneyHistory.get(i);
            final RawGuildStorageHistory.HistoryMoneyEntry rawEntry = new RawGuildStorageHistory.HistoryMoneyEntry();
            rawEntry.memberName = entry.getMemberName();
            rawEntry.date = entry.getDate();
            rawEntry.amount = entry.getAmount();
            rawHistory.moneyHistory.add(rawEntry);
        }
    }
    
    public void fromRaw(final RawGuildStorageHistory rawHistory) {
        this.fromRawItem(rawHistory);
        this.fromRawMoney(rawHistory);
    }
    
    private void fromRawItem(final RawGuildStorageHistory rawHistory) {
        for (int i = 0, size = rawHistory.itemHistory.size(); i < size; ++i) {
            final RawGuildStorageHistory.HistoryItemEntry rawEntry = rawHistory.itemHistory.get(i);
            this.m_itemHistory.addLast(new GuildStorageHistoryItemEntry(rawEntry.memberName, rawEntry.date, rawEntry.item, rawEntry.qty));
        }
    }
    
    private void fromRawMoney(final RawGuildStorageHistory rawHistory) {
        for (int i = 0, size = rawHistory.moneyHistory.size(); i < size; ++i) {
            final RawGuildStorageHistory.HistoryMoneyEntry rawEntry = rawHistory.moneyHistory.get(i);
            this.m_moneyHistory.addLast(new GuildStorageHistoryMoneyEntry(rawEntry.memberName, rawEntry.date, rawEntry.amount));
        }
    }
    
    public void fromBuild(final ByteBuffer bb) {
        final RawGuildStorageHistory raw = new RawGuildStorageHistory();
        raw.unserialize(bb);
        this.fromRaw(raw);
    }
    
    public byte[] serialize() {
        final RawGuildStorageHistory raw = new RawGuildStorageHistory();
        this.toRaw(raw);
        final ByteBuffer bb = ByteBuffer.allocate(raw.serializedSize());
        raw.serialize(bb);
        return bb.array();
    }
    
    @Override
    public String toString() {
        return "GuildStorageHistory{m_itemHistory=" + this.m_itemHistory.size() + ", m_moneyHistory=" + this.m_moneyHistory.size() + '}';
    }
}
