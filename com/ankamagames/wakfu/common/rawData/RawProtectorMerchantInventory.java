package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawProtectorMerchantInventory implements VersionableObject
{
    public long uid;
    public final ArrayList<Content> contents;
    
    public RawProtectorMerchantInventory() {
        super();
        this.uid = 0L;
        this.contents = new ArrayList<Content>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.uid);
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
        this.uid = buffer.getLong();
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
        this.uid = 0L;
        this.contents.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawProtectorMerchantInventoryConverter converter = new RawProtectorMerchantInventoryConverter();
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
        size += 8;
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
        repr.append(prefix).append("uid=").append(this.uid).append('\n');
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
        public byte type;
        public int featureReferenceId;
        public long duration;
        public long startDate;
        public final RawMerchantItem merchantItem;
        
        public Content() {
            super();
            this.position = 0;
            this.type = 0;
            this.featureReferenceId = 0;
            this.duration = 0L;
            this.startDate = -1L;
            this.merchantItem = new RawMerchantItem();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putShort(this.position);
            buffer.put(this.type);
            buffer.putInt(this.featureReferenceId);
            buffer.putLong(this.duration);
            buffer.putLong(this.startDate);
            final boolean merchantItem_ok = this.merchantItem.serialize(buffer);
            return merchantItem_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.position = buffer.getShort();
            this.type = buffer.get();
            this.featureReferenceId = buffer.getInt();
            this.duration = buffer.getLong();
            this.startDate = buffer.getLong();
            final boolean merchantItem_ok = this.merchantItem.unserialize(buffer);
            return merchantItem_ok;
        }
        
        @Override
        public void clear() {
            this.position = 0;
            this.type = 0;
            this.featureReferenceId = 0;
            this.duration = 0L;
            this.startDate = -1L;
            this.merchantItem.clear();
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
            ++size;
            size += 4;
            size += 8;
            size += 8;
            size += this.merchantItem.serializedSize();
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
            repr.append(prefix).append("type=").append(this.type).append('\n');
            repr.append(prefix).append("featureReferenceId=").append(this.featureReferenceId).append('\n');
            repr.append(prefix).append("duration=").append(this.duration).append('\n');
            repr.append(prefix).append("startDate=").append(this.startDate).append('\n');
            repr.append(prefix).append("merchantItem=...\n");
            this.merchantItem.internalToString(repr, prefix + "  ");
        }
        
        private final class ContentConverter
        {
            private short position;
            private byte type;
            private int featureReferenceId;
            private long duration;
            private long startDate;
            private final RawMerchantItem merchantItem;
            
            private ContentConverter() {
                super();
                this.position = 0;
                this.type = 0;
                this.featureReferenceId = 0;
                this.duration = 0L;
                this.startDate = -1L;
                this.merchantItem = new RawMerchantItem();
            }
            
            public void pushResult() {
                Content.this.position = this.position;
                Content.this.type = this.type;
                Content.this.featureReferenceId = this.featureReferenceId;
                Content.this.duration = this.duration;
                Content.this.startDate = this.startDate;
                Content.this.merchantItem.item.uniqueId = this.merchantItem.item.uniqueId;
                Content.this.merchantItem.item.refId = this.merchantItem.item.refId;
                Content.this.merchantItem.item.quantity = this.merchantItem.item.quantity;
                Content.this.merchantItem.item.timestamp = this.merchantItem.item.timestamp;
                Content.this.merchantItem.item.pet = this.merchantItem.item.pet;
                Content.this.merchantItem.item.xp = this.merchantItem.item.xp;
                Content.this.merchantItem.item.gems = this.merchantItem.item.gems;
                Content.this.merchantItem.item.rentInfo = this.merchantItem.item.rentInfo;
                Content.this.merchantItem.item.companionInfo = this.merchantItem.item.companionInfo;
                Content.this.merchantItem.item.bind = this.merchantItem.item.bind;
                Content.this.merchantItem.item.elements = this.merchantItem.item.elements;
                Content.this.merchantItem.item.mergedItems = this.merchantItem.item.mergedItems;
                Content.this.merchantItem.packSize = this.merchantItem.packSize;
                Content.this.merchantItem.price = this.merchantItem.price;
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                return true;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 1);
                return merchantItem_ok;
            }
            
            private boolean unserialize_v313(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 313);
                return merchantItem_ok;
            }
            
            private boolean unserialize_v314(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 314);
                return merchantItem_ok;
            }
            
            private boolean unserialize_v315(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 315);
                return merchantItem_ok;
            }
            
            private boolean unserialize_v10003(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 10003);
                return merchantItem_ok;
            }
            
            private boolean unserialize_v10023(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 10023);
                return merchantItem_ok;
            }
            
            private boolean unserialize_v10028000(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 10028000);
                return merchantItem_ok;
            }
            
            private boolean unserialize_v10029000(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 10029000);
                return merchantItem_ok;
            }
            
            private boolean unserialize_v10032003(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 10032003);
                return merchantItem_ok;
            }
            
            private boolean unserialize_v10035004(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 10035004);
                return merchantItem_ok;
            }
            
            private boolean unserialize_v10035007(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 10035007);
                return merchantItem_ok;
            }
            
            private boolean unserialize_v10036004(final ByteBuffer buffer) {
                this.position = buffer.getShort();
                this.type = buffer.get();
                this.featureReferenceId = buffer.getInt();
                this.duration = buffer.getLong();
                this.startDate = buffer.getLong();
                final boolean merchantItem_ok = this.merchantItem.unserializeVersion(buffer, 10036004);
                return merchantItem_ok;
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
    
    private final class RawProtectorMerchantInventoryConverter
    {
        private long uid;
        private final ArrayList<Content> contents;
        
        private RawProtectorMerchantInventoryConverter() {
            super();
            this.uid = 0L;
            this.contents = new ArrayList<Content>(0);
        }
        
        public void pushResult() {
            RawProtectorMerchantInventory.this.uid = this.uid;
            RawProtectorMerchantInventory.this.contents.clear();
            RawProtectorMerchantInventory.this.contents.ensureCapacity(this.contents.size());
            RawProtectorMerchantInventory.this.contents.addAll(this.contents);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.uid = buffer.getLong();
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
            this.uid = buffer.getLong();
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
            this.uid = buffer.getLong();
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
            this.uid = buffer.getLong();
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
            this.uid = buffer.getLong();
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
            this.uid = buffer.getLong();
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
            this.uid = buffer.getLong();
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
            this.uid = buffer.getLong();
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
            this.uid = buffer.getLong();
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
            this.uid = buffer.getLong();
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
            this.uid = buffer.getLong();
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
            this.uid = buffer.getLong();
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
