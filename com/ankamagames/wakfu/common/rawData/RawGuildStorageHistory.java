package com.ankamagames.wakfu.common.rawData;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;

public class RawGuildStorageHistory implements VersionableObject
{
    public final ArrayList<HistoryItemEntry> itemHistory;
    public final ArrayList<HistoryMoneyEntry> moneyHistory;
    
    public RawGuildStorageHistory() {
        super();
        this.itemHistory = new ArrayList<HistoryItemEntry>(0);
        this.moneyHistory = new ArrayList<HistoryMoneyEntry>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.itemHistory.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.itemHistory.size());
        for (int i = 0; i < this.itemHistory.size(); ++i) {
            final HistoryItemEntry itemHistory_element = this.itemHistory.get(i);
            final boolean itemHistory_element_ok = itemHistory_element.serialize(buffer);
            if (!itemHistory_element_ok) {
                return false;
            }
        }
        if (this.moneyHistory.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.moneyHistory.size());
        for (int i = 0; i < this.moneyHistory.size(); ++i) {
            final HistoryMoneyEntry moneyHistory_element = this.moneyHistory.get(i);
            final boolean moneyHistory_element_ok = moneyHistory_element.serialize(buffer);
            if (!moneyHistory_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int itemHistory_size = buffer.getShort() & 0xFFFF;
        this.itemHistory.clear();
        this.itemHistory.ensureCapacity(itemHistory_size);
        for (int i = 0; i < itemHistory_size; ++i) {
            final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
            final boolean itemHistory_element_ok = itemHistory_element.unserialize(buffer);
            if (!itemHistory_element_ok) {
                return false;
            }
            this.itemHistory.add(itemHistory_element);
        }
        final int moneyHistory_size = buffer.getShort() & 0xFFFF;
        this.moneyHistory.clear();
        this.moneyHistory.ensureCapacity(moneyHistory_size);
        for (int j = 0; j < moneyHistory_size; ++j) {
            final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
            final boolean moneyHistory_element_ok = moneyHistory_element.unserialize(buffer);
            if (!moneyHistory_element_ok) {
                return false;
            }
            this.moneyHistory.add(moneyHistory_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.itemHistory.clear();
        this.moneyHistory.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawGuildStorageHistoryConverter converter = new RawGuildStorageHistoryConverter();
        final boolean ok = converter.unserializeVersion(buffer, version);
        if (ok) {
            converter.pushResult();
            return true;
        }
        return false;
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.itemHistory.size(); ++i) {
            final HistoryItemEntry itemHistory_element = this.itemHistory.get(i);
            size += itemHistory_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.moneyHistory.size(); ++i) {
            final HistoryMoneyEntry moneyHistory_element = this.moneyHistory.get(i);
            size += moneyHistory_element.serializedSize();
        }
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("itemHistory=");
        if (this.itemHistory.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.itemHistory.size()).append(" elements)...\n");
            for (int i = 0; i < this.itemHistory.size(); ++i) {
                final HistoryItemEntry itemHistory_element = this.itemHistory.get(i);
                itemHistory_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("moneyHistory=");
        if (this.moneyHistory.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.moneyHistory.size()).append(" elements)...\n");
            for (int i = 0; i < this.moneyHistory.size(); ++i) {
                final HistoryMoneyEntry moneyHistory_element = this.moneyHistory.get(i);
                moneyHistory_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class HistoryItemEntry implements VersionableObject
    {
        public String memberName;
        public long date;
        public short qty;
        public final RawInventoryItem item;
        
        public HistoryItemEntry() {
            super();
            this.memberName = null;
            this.date = 0L;
            this.qty = 0;
            this.item = new RawInventoryItem();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.memberName != null) {
                final byte[] serialized_memberName = StringUtils.toUTF8(this.memberName);
                if (serialized_memberName.length > 65535) {
                    return false;
                }
                buffer.putShort((short)serialized_memberName.length);
                buffer.put(serialized_memberName);
            }
            else {
                buffer.putShort((short)0);
            }
            buffer.putLong(this.date);
            buffer.putShort(this.qty);
            final boolean item_ok = this.item.serialize(buffer);
            return item_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int memberName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_memberName = new byte[memberName_size];
            buffer.get(serialized_memberName);
            this.memberName = StringUtils.fromUTF8(serialized_memberName);
            this.date = buffer.getLong();
            this.qty = buffer.getShort();
            final boolean item_ok = this.item.unserialize(buffer);
            return item_ok;
        }
        
        @Override
        public void clear() {
            this.memberName = null;
            this.date = 0L;
            this.qty = 0;
            this.item.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10037001) {
                return this.unserialize(buffer);
            }
            final HistoryItemEntryConverter converter = new HistoryItemEntryConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 2;
            size += ((this.memberName != null) ? StringUtils.toUTF8(this.memberName).length : 0);
            size += 8;
            size += 2;
            size += this.item.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("memberName=").append(this.memberName).append('\n');
            repr.append(prefix).append("date=").append(this.date).append('\n');
            repr.append(prefix).append("qty=").append(this.qty).append('\n');
            repr.append(prefix).append("item=...\n");
            this.item.internalToString(repr, prefix + "  ");
        }
        
        private final class HistoryItemEntryConverter
        {
            private String memberName;
            private long date;
            private int refId;
            private short qty;
            private final RawItemOptionalInfo optionalInfo;
            private final RawInventoryItem item;
            
            private HistoryItemEntryConverter() {
                super();
                this.memberName = null;
                this.date = 0L;
                this.refId = 0;
                this.qty = 0;
                this.optionalInfo = new RawItemOptionalInfo();
                this.item = new RawInventoryItem();
            }
            
            public void pushResult() {
                HistoryItemEntry.this.memberName = this.memberName;
                HistoryItemEntry.this.date = this.date;
                HistoryItemEntry.this.qty = this.qty;
                HistoryItemEntry.this.item.uniqueId = this.item.uniqueId;
                HistoryItemEntry.this.item.refId = this.item.refId;
                HistoryItemEntry.this.item.quantity = this.item.quantity;
                HistoryItemEntry.this.item.timestamp = this.item.timestamp;
                HistoryItemEntry.this.item.pet = this.item.pet;
                HistoryItemEntry.this.item.xp = this.item.xp;
                HistoryItemEntry.this.item.gems = this.item.gems;
                HistoryItemEntry.this.item.rentInfo = this.item.rentInfo;
                HistoryItemEntry.this.item.companionInfo = this.item.companionInfo;
                HistoryItemEntry.this.item.bind = this.item.bind;
                HistoryItemEntry.this.item.elements = this.item.elements;
                HistoryItemEntry.this.item.mergedItems = this.item.mergedItems;
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                return true;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                return true;
            }
            
            private boolean unserialize_v313(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                return true;
            }
            
            private boolean unserialize_v314(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                return true;
            }
            
            private boolean unserialize_v315(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                return true;
            }
            
            private boolean unserialize_v10003(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                return true;
            }
            
            private boolean unserialize_v10023(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                return true;
            }
            
            private boolean unserialize_v10024001(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                final boolean optionalInfo_ok = this.optionalInfo.unserializeVersion(buffer, 10024001);
                return optionalInfo_ok;
            }
            
            private boolean unserialize_v10028000(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                final boolean optionalInfo_ok = this.optionalInfo.unserializeVersion(buffer, 10028000);
                return optionalInfo_ok;
            }
            
            private boolean unserialize_v10029000(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                final boolean optionalInfo_ok = this.optionalInfo.unserializeVersion(buffer, 10029000);
                return optionalInfo_ok;
            }
            
            private boolean unserialize_v10030002(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                final boolean optionalInfo_ok = this.optionalInfo.unserializeVersion(buffer, 10030002);
                return optionalInfo_ok;
            }
            
            private boolean unserialize_v10032003(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                final boolean optionalInfo_ok = this.optionalInfo.unserializeVersion(buffer, 10032003);
                return optionalInfo_ok;
            }
            
            private boolean unserialize_v10035004(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                final boolean optionalInfo_ok = this.optionalInfo.unserializeVersion(buffer, 10035004);
                return optionalInfo_ok;
            }
            
            private boolean unserialize_v10035007(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                final boolean optionalInfo_ok = this.optionalInfo.unserializeVersion(buffer, 10035007);
                return optionalInfo_ok;
            }
            
            private boolean unserialize_v10036004(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.refId = buffer.getInt();
                this.qty = buffer.getShort();
                final boolean optionalInfo_ok = this.optionalInfo.unserializeVersion(buffer, 10036004);
                return optionalInfo_ok;
            }
            
            private boolean unserialize_v10036008(final ByteBuffer buffer) {
                final int memberName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_memberName = new byte[memberName_size];
                buffer.get(serialized_memberName);
                this.memberName = StringUtils.fromUTF8(serialized_memberName);
                this.date = buffer.getLong();
                this.qty = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 10036008);
                return item_ok;
            }
            
            public void convert_v0_to_v1() {
            }
            
            public void convert_v1_to_v313() {
            }
            
            public void convert_v313_to_v314() {
            }
            
            public void convert_v314_to_v315() {
            }
            
            public void convert_v315_to_v10003() {
            }
            
            public void convert_v10003_to_v10023() {
            }
            
            public void convert_v10023_to_v10024001() {
            }
            
            public void convert_v10024001_to_v10028000() {
            }
            
            public void convert_v10028000_to_v10029000() {
            }
            
            public void convert_v10029000_to_v10030002() {
            }
            
            public void convert_v10030002_to_v10032003() {
            }
            
            public void convert_v10032003_to_v10035004() {
            }
            
            public void convert_v10035004_to_v10035007() {
            }
            
            public void convert_v10035007_to_v10036004() {
            }
            
            public void convert_v10036004_to_v10036008() {
            }
            
            public void convert_v10036008_to_v10037001() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 0) {
                    return false;
                }
                if (version < 1) {
                    final boolean ok = this.unserialize_v0(buffer);
                    if (ok) {
                        this.convert_v0_to_v1();
                        this.convert_v1_to_v313();
                        this.convert_v313_to_v314();
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10024001();
                        this.convert_v10024001_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10030002();
                        this.convert_v10030002_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 313) {
                    final boolean ok = this.unserialize_v1(buffer);
                    if (ok) {
                        this.convert_v1_to_v313();
                        this.convert_v313_to_v314();
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10024001();
                        this.convert_v10024001_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10030002();
                        this.convert_v10030002_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 314) {
                    final boolean ok = this.unserialize_v313(buffer);
                    if (ok) {
                        this.convert_v313_to_v314();
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10024001();
                        this.convert_v10024001_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10030002();
                        this.convert_v10030002_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 315) {
                    final boolean ok = this.unserialize_v314(buffer);
                    if (ok) {
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10024001();
                        this.convert_v10024001_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10030002();
                        this.convert_v10030002_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10003) {
                    final boolean ok = this.unserialize_v315(buffer);
                    if (ok) {
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10024001();
                        this.convert_v10024001_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10030002();
                        this.convert_v10030002_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10023) {
                    final boolean ok = this.unserialize_v10003(buffer);
                    if (ok) {
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10024001();
                        this.convert_v10024001_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10030002();
                        this.convert_v10030002_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10024001) {
                    final boolean ok = this.unserialize_v10023(buffer);
                    if (ok) {
                        this.convert_v10023_to_v10024001();
                        this.convert_v10024001_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10030002();
                        this.convert_v10030002_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10028000) {
                    final boolean ok = this.unserialize_v10024001(buffer);
                    if (ok) {
                        this.convert_v10024001_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10030002();
                        this.convert_v10030002_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10029000) {
                    final boolean ok = this.unserialize_v10028000(buffer);
                    if (ok) {
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10030002();
                        this.convert_v10030002_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10030002) {
                    final boolean ok = this.unserialize_v10029000(buffer);
                    if (ok) {
                        this.convert_v10029000_to_v10030002();
                        this.convert_v10030002_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10032003) {
                    final boolean ok = this.unserialize_v10030002(buffer);
                    if (ok) {
                        this.convert_v10030002_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10035004) {
                    final boolean ok = this.unserialize_v10032003(buffer);
                    if (ok) {
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10035007) {
                    final boolean ok = this.unserialize_v10035004(buffer);
                    if (ok) {
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10036004) {
                    final boolean ok = this.unserialize_v10035007(buffer);
                    if (ok) {
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10036008) {
                    final boolean ok = this.unserialize_v10036004(buffer);
                    if (ok) {
                        this.convert_v10036004_to_v10036008();
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else {
                    if (version >= 10037001) {
                        return false;
                    }
                    final boolean ok = this.unserialize_v10036008(buffer);
                    if (ok) {
                        this.convert_v10036008_to_v10037001();
                        return true;
                    }
                    return false;
                }
            }
        }
    }
    
    public static class HistoryMoneyEntry implements VersionableObject
    {
        public String memberName;
        public long date;
        public int amount;
        
        public HistoryMoneyEntry() {
            super();
            this.memberName = null;
            this.date = 0L;
            this.amount = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            if (this.memberName != null) {
                final byte[] serialized_memberName = StringUtils.toUTF8(this.memberName);
                if (serialized_memberName.length > 65535) {
                    return false;
                }
                buffer.putShort((short)serialized_memberName.length);
                buffer.put(serialized_memberName);
            }
            else {
                buffer.putShort((short)0);
            }
            buffer.putLong(this.date);
            buffer.putInt(this.amount);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final int memberName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_memberName = new byte[memberName_size];
            buffer.get(serialized_memberName);
            this.memberName = StringUtils.fromUTF8(serialized_memberName);
            this.date = buffer.getLong();
            this.amount = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.memberName = null;
            this.date = 0L;
            this.amount = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 2;
            size += ((this.memberName != null) ? StringUtils.toUTF8(this.memberName).length : 0);
            size += 8;
            size += 4;
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("memberName=").append(this.memberName).append('\n');
            repr.append(prefix).append("date=").append(this.date).append('\n');
            repr.append(prefix).append("amount=").append(this.amount).append('\n');
        }
    }
    
    private final class RawGuildStorageHistoryConverter
    {
        private final ArrayList<HistoryItemEntry> itemHistory;
        private final ArrayList<HistoryMoneyEntry> moneyHistory;
        
        private RawGuildStorageHistoryConverter() {
            super();
            this.itemHistory = new ArrayList<HistoryItemEntry>(0);
            this.moneyHistory = new ArrayList<HistoryMoneyEntry>(0);
        }
        
        public void pushResult() {
            RawGuildStorageHistory.this.itemHistory.clear();
            RawGuildStorageHistory.this.itemHistory.ensureCapacity(this.itemHistory.size());
            RawGuildStorageHistory.this.itemHistory.addAll(this.itemHistory);
            RawGuildStorageHistory.this.moneyHistory.clear();
            RawGuildStorageHistory.this.moneyHistory.ensureCapacity(this.moneyHistory.size());
            RawGuildStorageHistory.this.moneyHistory.addAll(this.moneyHistory);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 0);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 1);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 313);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 314);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 315);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10003);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10012(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10012);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            final int moneyHistory_size = buffer.getShort() & 0xFFFF;
            this.moneyHistory.clear();
            this.moneyHistory.ensureCapacity(moneyHistory_size);
            for (int j = 0; j < moneyHistory_size; ++j) {
                final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
                final boolean moneyHistory_element_ok = moneyHistory_element.unserializeVersion(buffer, 10012);
                if (!moneyHistory_element_ok) {
                    return false;
                }
                this.moneyHistory.add(moneyHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10023);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            final int moneyHistory_size = buffer.getShort() & 0xFFFF;
            this.moneyHistory.clear();
            this.moneyHistory.ensureCapacity(moneyHistory_size);
            for (int j = 0; j < moneyHistory_size; ++j) {
                final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
                final boolean moneyHistory_element_ok = moneyHistory_element.unserializeVersion(buffer, 10023);
                if (!moneyHistory_element_ok) {
                    return false;
                }
                this.moneyHistory.add(moneyHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10024001(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10024001);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            final int moneyHistory_size = buffer.getShort() & 0xFFFF;
            this.moneyHistory.clear();
            this.moneyHistory.ensureCapacity(moneyHistory_size);
            for (int j = 0; j < moneyHistory_size; ++j) {
                final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
                final boolean moneyHistory_element_ok = moneyHistory_element.unserializeVersion(buffer, 10024001);
                if (!moneyHistory_element_ok) {
                    return false;
                }
                this.moneyHistory.add(moneyHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10028000);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            final int moneyHistory_size = buffer.getShort() & 0xFFFF;
            this.moneyHistory.clear();
            this.moneyHistory.ensureCapacity(moneyHistory_size);
            for (int j = 0; j < moneyHistory_size; ++j) {
                final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
                final boolean moneyHistory_element_ok = moneyHistory_element.unserializeVersion(buffer, 10028000);
                if (!moneyHistory_element_ok) {
                    return false;
                }
                this.moneyHistory.add(moneyHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10029000);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            final int moneyHistory_size = buffer.getShort() & 0xFFFF;
            this.moneyHistory.clear();
            this.moneyHistory.ensureCapacity(moneyHistory_size);
            for (int j = 0; j < moneyHistory_size; ++j) {
                final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
                final boolean moneyHistory_element_ok = moneyHistory_element.unserializeVersion(buffer, 10029000);
                if (!moneyHistory_element_ok) {
                    return false;
                }
                this.moneyHistory.add(moneyHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10030002(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10030002);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            final int moneyHistory_size = buffer.getShort() & 0xFFFF;
            this.moneyHistory.clear();
            this.moneyHistory.ensureCapacity(moneyHistory_size);
            for (int j = 0; j < moneyHistory_size; ++j) {
                final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
                final boolean moneyHistory_element_ok = moneyHistory_element.unserializeVersion(buffer, 10030002);
                if (!moneyHistory_element_ok) {
                    return false;
                }
                this.moneyHistory.add(moneyHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10032003);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            final int moneyHistory_size = buffer.getShort() & 0xFFFF;
            this.moneyHistory.clear();
            this.moneyHistory.ensureCapacity(moneyHistory_size);
            for (int j = 0; j < moneyHistory_size; ++j) {
                final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
                final boolean moneyHistory_element_ok = moneyHistory_element.unserializeVersion(buffer, 10032003);
                if (!moneyHistory_element_ok) {
                    return false;
                }
                this.moneyHistory.add(moneyHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10035004);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            final int moneyHistory_size = buffer.getShort() & 0xFFFF;
            this.moneyHistory.clear();
            this.moneyHistory.ensureCapacity(moneyHistory_size);
            for (int j = 0; j < moneyHistory_size; ++j) {
                final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
                final boolean moneyHistory_element_ok = moneyHistory_element.unserializeVersion(buffer, 10035004);
                if (!moneyHistory_element_ok) {
                    return false;
                }
                this.moneyHistory.add(moneyHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10035007);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            final int moneyHistory_size = buffer.getShort() & 0xFFFF;
            this.moneyHistory.clear();
            this.moneyHistory.ensureCapacity(moneyHistory_size);
            for (int j = 0; j < moneyHistory_size; ++j) {
                final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
                final boolean moneyHistory_element_ok = moneyHistory_element.unserializeVersion(buffer, 10035007);
                if (!moneyHistory_element_ok) {
                    return false;
                }
                this.moneyHistory.add(moneyHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10036004);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            final int moneyHistory_size = buffer.getShort() & 0xFFFF;
            this.moneyHistory.clear();
            this.moneyHistory.ensureCapacity(moneyHistory_size);
            for (int j = 0; j < moneyHistory_size; ++j) {
                final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
                final boolean moneyHistory_element_ok = moneyHistory_element.unserializeVersion(buffer, 10036004);
                if (!moneyHistory_element_ok) {
                    return false;
                }
                this.moneyHistory.add(moneyHistory_element);
            }
            return true;
        }
        
        private boolean unserialize_v10036008(final ByteBuffer buffer) {
            final int itemHistory_size = buffer.getShort() & 0xFFFF;
            this.itemHistory.clear();
            this.itemHistory.ensureCapacity(itemHistory_size);
            for (int i = 0; i < itemHistory_size; ++i) {
                final HistoryItemEntry itemHistory_element = new HistoryItemEntry();
                final boolean itemHistory_element_ok = itemHistory_element.unserializeVersion(buffer, 10036008);
                if (!itemHistory_element_ok) {
                    return false;
                }
                this.itemHistory.add(itemHistory_element);
            }
            final int moneyHistory_size = buffer.getShort() & 0xFFFF;
            this.moneyHistory.clear();
            this.moneyHistory.ensureCapacity(moneyHistory_size);
            for (int j = 0; j < moneyHistory_size; ++j) {
                final HistoryMoneyEntry moneyHistory_element = new HistoryMoneyEntry();
                final boolean moneyHistory_element_ok = moneyHistory_element.unserializeVersion(buffer, 10036008);
                if (!moneyHistory_element_ok) {
                    return false;
                }
                this.moneyHistory.add(moneyHistory_element);
            }
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v313() {
        }
        
        public void convert_v313_to_v314() {
        }
        
        public void convert_v314_to_v315() {
        }
        
        public void convert_v315_to_v10003() {
        }
        
        public void convert_v10003_to_v10012() {
        }
        
        public void convert_v10012_to_v10023() {
        }
        
        public void convert_v10023_to_v10024001() {
        }
        
        public void convert_v10024001_to_v10028000() {
        }
        
        public void convert_v10028000_to_v10029000() {
        }
        
        public void convert_v10029000_to_v10030002() {
        }
        
        public void convert_v10030002_to_v10032003() {
        }
        
        public void convert_v10032003_to_v10035004() {
        }
        
        public void convert_v10035004_to_v10035007() {
        }
        
        public void convert_v10035007_to_v10036004() {
        }
        
        public void convert_v10036004_to_v10036008() {
        }
        
        public void convert_v10036008_to_v10037001() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 1) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    this.convert_v1_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10012();
                    this.convert_v10012_to_v10023();
                    this.convert_v10023_to_v10024001();
                    this.convert_v10024001_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10030002();
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 313) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10012();
                    this.convert_v10012_to_v10023();
                    this.convert_v10023_to_v10024001();
                    this.convert_v10024001_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10030002();
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 314) {
                final boolean ok = this.unserialize_v313(buffer);
                if (ok) {
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10012();
                    this.convert_v10012_to_v10023();
                    this.convert_v10023_to_v10024001();
                    this.convert_v10024001_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10030002();
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 315) {
                final boolean ok = this.unserialize_v314(buffer);
                if (ok) {
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10012();
                    this.convert_v10012_to_v10023();
                    this.convert_v10023_to_v10024001();
                    this.convert_v10024001_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10030002();
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10003) {
                final boolean ok = this.unserialize_v315(buffer);
                if (ok) {
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10012();
                    this.convert_v10012_to_v10023();
                    this.convert_v10023_to_v10024001();
                    this.convert_v10024001_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10030002();
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10012) {
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10012();
                    this.convert_v10012_to_v10023();
                    this.convert_v10023_to_v10024001();
                    this.convert_v10024001_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10030002();
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10023) {
                final boolean ok = this.unserialize_v10012(buffer);
                if (ok) {
                    this.convert_v10012_to_v10023();
                    this.convert_v10023_to_v10024001();
                    this.convert_v10024001_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10030002();
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10024001) {
                final boolean ok = this.unserialize_v10023(buffer);
                if (ok) {
                    this.convert_v10023_to_v10024001();
                    this.convert_v10024001_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10030002();
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10028000) {
                final boolean ok = this.unserialize_v10024001(buffer);
                if (ok) {
                    this.convert_v10024001_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10030002();
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10029000) {
                final boolean ok = this.unserialize_v10028000(buffer);
                if (ok) {
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10030002();
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10030002) {
                final boolean ok = this.unserialize_v10029000(buffer);
                if (ok) {
                    this.convert_v10029000_to_v10030002();
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10032003) {
                final boolean ok = this.unserialize_v10030002(buffer);
                if (ok) {
                    this.convert_v10030002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10035004) {
                final boolean ok = this.unserialize_v10032003(buffer);
                if (ok) {
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10035007) {
                final boolean ok = this.unserialize_v10035004(buffer);
                if (ok) {
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10036004) {
                final boolean ok = this.unserialize_v10035007(buffer);
                if (ok) {
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10036008) {
                final boolean ok = this.unserialize_v10036004(buffer);
                if (ok) {
                    this.convert_v10036004_to_v10036008();
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10037001) {
                    return false;
                }
                final boolean ok = this.unserialize_v10036008(buffer);
                if (ok) {
                    this.convert_v10036008_to_v10037001();
                    return true;
                }
                return false;
            }
        }
    }
}
