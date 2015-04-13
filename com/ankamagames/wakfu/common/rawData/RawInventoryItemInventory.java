package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawInventoryItemInventory implements VersionableObject
{
    public final ArrayList<Content> contents;
    
    public RawInventoryItemInventory() {
        super();
        this.contents = new ArrayList<Content>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.contents.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.contents.size());
        for (int i = 0; i < this.contents.size(); ++i) {
            final Content contents_element = this.contents.get(i);
            final boolean contents_element_ok = contents_element.serialize(buffer);
            if (!contents_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int contents_size = buffer.getShort() & 0xFFFF;
        this.contents.clear();
        this.contents.ensureCapacity(contents_size);
        for (int i = 0; i < contents_size; ++i) {
            final Content contents_element = new Content();
            final boolean contents_element_ok = contents_element.unserialize(buffer);
            if (!contents_element_ok) {
                return false;
            }
            this.contents.add(contents_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.contents.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawInventoryItemInventoryConverter converter = new RawInventoryItemInventoryConverter();
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
        for (int i = 0; i < this.contents.size(); ++i) {
            final Content contents_element = this.contents.get(i);
            size += contents_element.serializedSize();
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
        repr.append(prefix).append("contents=");
        if (this.contents.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.contents.size()).append(" elements)...\n");
            for (int i = 0; i < this.contents.size(); ++i) {
                final Content contents_element = this.contents.get(i);
                contents_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Content implements VersionableObject
    {
        public short position;
        public final RawInventoryItem item;
        
        public Content() {
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
            final ContentConverter converter = new ContentConverter();
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
        
        private final class ContentConverter
        {
            private short position;
            private final RawInventoryItem item;
            
            private ContentConverter() {
                super();
                this.position = 0;
                this.item = new RawInventoryItem();
            }
            
            public void pushResult() {
                Content.this.position = this.position;
                Content.this.item.uniqueId = this.item.uniqueId;
                Content.this.item.refId = this.item.refId;
                Content.this.item.quantity = this.item.quantity;
                Content.this.item.timestamp = this.item.timestamp;
                Content.this.item.pet = this.item.pet;
                Content.this.item.xp = this.item.xp;
                Content.this.item.gems = this.item.gems;
                Content.this.item.rentInfo = this.item.rentInfo;
                Content.this.item.companionInfo = this.item.companionInfo;
                Content.this.item.bind = this.item.bind;
                Content.this.item.elements = this.item.elements;
                Content.this.item.mergedItems = this.item.mergedItems;
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
    
    private final class RawInventoryItemInventoryConverter
    {
        private final ArrayList<Content> contents;
        
        private RawInventoryItemInventoryConverter() {
            super();
            this.contents = new ArrayList<Content>(0);
        }
        
        public void pushResult() {
            RawInventoryItemInventory.this.contents.clear();
            RawInventoryItemInventory.this.contents.ensureCapacity(this.contents.size());
            RawInventoryItemInventory.this.contents.addAll(this.contents);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 1);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 313);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 314);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 315);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            return true;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 10003);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            return true;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 10023);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            return true;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 10028000);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            return true;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 10029000);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            return true;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 10032003);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 10035004);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            return true;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 10035007);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
            }
            return true;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            final int contents_size = buffer.getShort() & 0xFFFF;
            this.contents.clear();
            this.contents.ensureCapacity(contents_size);
            for (int i = 0; i < contents_size; ++i) {
                final Content contents_element = new Content();
                final boolean contents_element_ok = contents_element.unserializeVersion(buffer, 10036004);
                if (!contents_element_ok) {
                    return false;
                }
                this.contents.add(contents_element);
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
