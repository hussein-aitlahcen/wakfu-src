package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawVault implements VersionableObject
{
    public final ArrayList<Item> items;
    public int money;
    
    public RawVault() {
        super();
        this.items = new ArrayList<Item>(0);
        this.money = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.items.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.items.size());
        for (int i = 0; i < this.items.size(); ++i) {
            final Item items_element = this.items.get(i);
            final boolean items_element_ok = items_element.serialize(buffer);
            if (!items_element_ok) {
                return false;
            }
        }
        buffer.putInt(this.money);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int items_size = buffer.getShort() & 0xFFFF;
        this.items.clear();
        this.items.ensureCapacity(items_size);
        for (int i = 0; i < items_size; ++i) {
            final Item items_element = new Item();
            final boolean items_element_ok = items_element.unserialize(buffer);
            if (!items_element_ok) {
                return false;
            }
            this.items.add(items_element);
        }
        this.money = buffer.getInt();
        return true;
    }
    
    @Override
    public void clear() {
        this.items.clear();
        this.money = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawVaultConverter converter = new RawVaultConverter();
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
        for (int i = 0; i < this.items.size(); ++i) {
            final Item items_element = this.items.get(i);
            size += items_element.serializedSize();
        }
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
        repr.append(prefix).append("items=");
        if (this.items.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.items.size()).append(" elements)...\n");
            for (int i = 0; i < this.items.size(); ++i) {
                final Item items_element = this.items.get(i);
                items_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("money=").append(this.money).append('\n');
    }
    
    public static class Item implements VersionableObject
    {
        public short position;
        public final RawInventoryItem item;
        
        public Item() {
            super();
            this.position = 0;
            this.item = new RawInventoryItem();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putShort(this.position);
            final boolean item_ok = this.item.serialize(buffer);
            return item_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.position = buffer.getShort();
            final boolean item_ok = this.item.unserialize(buffer);
            return item_ok;
        }
        
        @Override
        public void clear() {
            this.position = 0;
            this.item.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10037001) {
                return this.unserialize(buffer);
            }
            final ItemConverter converter = new ItemConverter();
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
            repr.append(prefix).append("position=").append(this.position).append('\n');
            repr.append(prefix).append("item=...\n");
            this.item.internalToString(repr, prefix + "  ");
        }
        
        private final class ItemConverter
        {
            private short position;
            private final RawInventoryItem item;
            
            private ItemConverter() {
                super();
                this.position = 0;
                this.item = new RawInventoryItem();
            }
            
            public void pushResult() {
                Item.this.position = this.position;
                Item.this.item.uniqueId = this.item.uniqueId;
                Item.this.item.refId = this.item.refId;
                Item.this.item.quantity = this.item.quantity;
                Item.this.item.timestamp = this.item.timestamp;
                Item.this.item.pet = this.item.pet;
                Item.this.item.xp = this.item.xp;
                Item.this.item.gems = this.item.gems;
                Item.this.item.rentInfo = this.item.rentInfo;
                Item.this.item.companionInfo = this.item.companionInfo;
                Item.this.item.bind = this.item.bind;
                Item.this.item.elements = this.item.elements;
                Item.this.item.mergedItems = this.item.mergedItems;
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                return true;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 1);
                return item_ok;
            }
            
            private boolean unserialize_v313(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 313);
                return item_ok;
            }
            
            private boolean unserialize_v314(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 314);
                return item_ok;
            }
            
            private boolean unserialize_v315(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 315);
                return item_ok;
            }
            
            private boolean unserialize_v10003(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 10003);
                return item_ok;
            }
            
            private boolean unserialize_v10023(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 10023);
                return item_ok;
            }
            
            private boolean unserialize_v10028000(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 10028000);
                return item_ok;
            }
            
            private boolean unserialize_v10029000(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 10029000);
                return item_ok;
            }
            
            private boolean unserialize_v10032003(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 10032003);
                return item_ok;
            }
            
            private boolean unserialize_v10035004(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 10035004);
                return item_ok;
            }
            
            private boolean unserialize_v10035007(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 10035007);
                return item_ok;
            }
            
            private boolean unserialize_v10036004(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                final boolean item_ok = this.item.unserializeVersion(buffer, 10036004);
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
            
            public void convert_v10023_to_v10028000() {
            }
            
            public void convert_v10028000_to_v10029000() {
            }
            
            public void convert_v10029000_to_v10032003() {
            }
            
            public void convert_v10032003_to_v10035004() {
            }
            
            public void convert_v10035004_to_v10035007() {
            }
            
            public void convert_v10035007_to_v10036004() {
            }
            
            public void convert_v10036004_to_v10037001() {
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
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
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
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
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
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
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
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10003) {
                    final boolean ok = this.unserialize_v315(buffer);
                    if (ok) {
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10023) {
                    final boolean ok = this.unserialize_v10003(buffer);
                    if (ok) {
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10028000) {
                    final boolean ok = this.unserialize_v10023(buffer);
                    if (ok) {
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10029000) {
                    final boolean ok = this.unserialize_v10028000(buffer);
                    if (ok) {
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10032003) {
                    final boolean ok = this.unserialize_v10029000(buffer);
                    if (ok) {
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
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
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10035007) {
                    final boolean ok = this.unserialize_v10035004(buffer);
                    if (ok) {
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10036004) {
                    final boolean ok = this.unserialize_v10035007(buffer);
                    if (ok) {
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else {
                    if (version >= 10037001) {
                        return false;
                    }
                    final boolean ok = this.unserialize_v10036004(buffer);
                    if (ok) {
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
            }
        }
    }
    
    private final class RawVaultConverter
    {
        private final ArrayList<Item> items;
        private int money;
        
        private RawVaultConverter() {
            super();
            this.items = new ArrayList<Item>(0);
            this.money = 0;
        }
        
        public void pushResult() {
            RawVault.this.items.clear();
            RawVault.this.items.ensureCapacity(this.items.size());
            RawVault.this.items.addAll(this.items);
            RawVault.this.money = this.money;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 0);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 1);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 313);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 314);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 315);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 10003);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 10023);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 10028000);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 10029000);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 10032003);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 10035004);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 10035007);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            final int items_size = buffer.getShort() & 0xFFFF;
            this.items.clear();
            this.items.ensureCapacity(items_size);
            for (int i = 0; i < items_size; ++i) {
                final Item items_element = new Item();
                final boolean items_element_ok = items_element.unserializeVersion(buffer, 10036004);
                if (!items_element_ok) {
                    return false;
                }
                this.items.add(items_element);
            }
            this.money = buffer.getInt();
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
        
        public void convert_v10003_to_v10023() {
        }
        
        public void convert_v10023_to_v10028000() {
        }
        
        public void convert_v10028000_to_v10029000() {
        }
        
        public void convert_v10029000_to_v10032003() {
        }
        
        public void convert_v10032003_to_v10035004() {
        }
        
        public void convert_v10035004_to_v10035007() {
        }
        
        public void convert_v10035007_to_v10036004() {
        }
        
        public void convert_v10036004_to_v10037001() {
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
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
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
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
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
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
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
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10003) {
                final boolean ok = this.unserialize_v315(buffer);
                if (ok) {
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10023) {
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10028000) {
                final boolean ok = this.unserialize_v10023(buffer);
                if (ok) {
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10029000) {
                final boolean ok = this.unserialize_v10028000(buffer);
                if (ok) {
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10032003) {
                final boolean ok = this.unserialize_v10029000(buffer);
                if (ok) {
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
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
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10035007) {
                final boolean ok = this.unserialize_v10035004(buffer);
                if (ok) {
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10036004) {
                final boolean ok = this.unserialize_v10035007(buffer);
                if (ok) {
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10037001) {
                    return false;
                }
                final boolean ok = this.unserialize_v10036004(buffer);
                if (ok) {
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
        }
    }
}
