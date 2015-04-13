package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;

public class RawDimensionalBagForSpawn implements VersionableObject
{
    public long ownerId;
    public String ownerName;
    public long guildId;
    public int customViewModelId;
    public final ArrayList<ShelfItem> shelfItems;
    public final ArrayList<MerchantDisplayInfo> merchantDisplays;
    
    public RawDimensionalBagForSpawn() {
        super();
        this.ownerId = 0L;
        this.ownerName = null;
        this.guildId = 0L;
        this.customViewModelId = 0;
        this.shelfItems = new ArrayList<ShelfItem>(0);
        this.merchantDisplays = new ArrayList<MerchantDisplayInfo>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.ownerId);
        if (this.ownerName != null) {
            final byte[] serialized_ownerName = StringUtils.toUTF8(this.ownerName);
            if (serialized_ownerName.length > 255) {
                return false;
            }
            buffer.put((byte)serialized_ownerName.length);
            buffer.put(serialized_ownerName);
        }
        else {
            buffer.put((byte)0);
        }
        buffer.putLong(this.guildId);
        buffer.putInt(this.customViewModelId);
        if (this.shelfItems.size() > 255) {
            return false;
        }
        buffer.put((byte)this.shelfItems.size());
        for (int i = 0; i < this.shelfItems.size(); ++i) {
            final ShelfItem shelfItems_element = this.shelfItems.get(i);
            final boolean shelfItems_element_ok = shelfItems_element.serialize(buffer);
            if (!shelfItems_element_ok) {
                return false;
            }
        }
        if (this.merchantDisplays.size() > 255) {
            return false;
        }
        buffer.put((byte)this.merchantDisplays.size());
        for (int i = 0; i < this.merchantDisplays.size(); ++i) {
            final MerchantDisplayInfo merchantDisplays_element = this.merchantDisplays.get(i);
            final boolean merchantDisplays_element_ok = merchantDisplays_element.serialize(buffer);
            if (!merchantDisplays_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.ownerId = buffer.getLong();
        final int ownerName_size = buffer.get() & 0xFF;
        final byte[] serialized_ownerName = new byte[ownerName_size];
        buffer.get(serialized_ownerName);
        this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
        this.guildId = buffer.getLong();
        this.customViewModelId = buffer.getInt();
        final int shelfItems_size = buffer.get() & 0xFF;
        this.shelfItems.clear();
        this.shelfItems.ensureCapacity(shelfItems_size);
        for (int i = 0; i < shelfItems_size; ++i) {
            final ShelfItem shelfItems_element = new ShelfItem();
            final boolean shelfItems_element_ok = shelfItems_element.unserialize(buffer);
            if (!shelfItems_element_ok) {
                return false;
            }
            this.shelfItems.add(shelfItems_element);
        }
        final int merchantDisplays_size = buffer.get() & 0xFF;
        this.merchantDisplays.clear();
        this.merchantDisplays.ensureCapacity(merchantDisplays_size);
        for (int j = 0; j < merchantDisplays_size; ++j) {
            final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
            final boolean merchantDisplays_element_ok = merchantDisplays_element.unserialize(buffer);
            if (!merchantDisplays_element_ok) {
                return false;
            }
            this.merchantDisplays.add(merchantDisplays_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.ownerId = 0L;
        this.ownerName = null;
        this.guildId = 0L;
        this.customViewModelId = 0;
        this.shelfItems.clear();
        this.merchantDisplays.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawDimensionalBagForSpawnConverter converter = new RawDimensionalBagForSpawnConverter();
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
        size = ++size + ((this.ownerName != null) ? StringUtils.toUTF8(this.ownerName).length : 0);
        size += 8;
        size += 4;
        ++size;
        for (int i = 0; i < this.shelfItems.size(); ++i) {
            final ShelfItem shelfItems_element = this.shelfItems.get(i);
            size += shelfItems_element.serializedSize();
        }
        ++size;
        for (int i = 0; i < this.merchantDisplays.size(); ++i) {
            final MerchantDisplayInfo merchantDisplays_element = this.merchantDisplays.get(i);
            size += merchantDisplays_element.serializedSize();
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
        repr.append(prefix).append("ownerId=").append(this.ownerId).append('\n');
        repr.append(prefix).append("ownerName=").append(this.ownerName).append('\n');
        repr.append(prefix).append("guildId=").append(this.guildId).append('\n');
        repr.append(prefix).append("customViewModelId=").append(this.customViewModelId).append('\n');
        repr.append(prefix).append("shelfItems=");
        if (this.shelfItems.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.shelfItems.size()).append(" elements)...\n");
            for (int i = 0; i < this.shelfItems.size(); ++i) {
                final ShelfItem shelfItems_element = this.shelfItems.get(i);
                shelfItems_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("merchantDisplays=");
        if (this.merchantDisplays.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.merchantDisplays.size()).append(" elements)...\n");
            for (int i = 0; i < this.merchantDisplays.size(); ++i) {
                final MerchantDisplayInfo merchantDisplays_element = this.merchantDisplays.get(i);
                merchantDisplays_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class ShelfItem implements VersionableObject
    {
        public final RawMerchantItem shelfItem;
        
        public ShelfItem() {
            super();
            this.shelfItem = new RawMerchantItem();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean shelfItem_ok = this.shelfItem.serialize(buffer);
            return shelfItem_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean shelfItem_ok = this.shelfItem.unserialize(buffer);
            return shelfItem_ok;
        }
        
        @Override
        public void clear() {
            this.shelfItem.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10037001) {
                return this.unserialize(buffer);
            }
            final ShelfItemConverter converter = new ShelfItemConverter();
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
            size += this.shelfItem.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("shelfItem=...\n");
            this.shelfItem.internalToString(repr, prefix + "  ");
        }
        
        private final class ShelfItemConverter
        {
            private final RawMerchantItem shelfItem;
            
            private ShelfItemConverter() {
                super();
                this.shelfItem = new RawMerchantItem();
            }
            
            public void pushResult() {
                ShelfItem.this.shelfItem.item.uniqueId = this.shelfItem.item.uniqueId;
                ShelfItem.this.shelfItem.item.refId = this.shelfItem.item.refId;
                ShelfItem.this.shelfItem.item.quantity = this.shelfItem.item.quantity;
                ShelfItem.this.shelfItem.item.timestamp = this.shelfItem.item.timestamp;
                ShelfItem.this.shelfItem.item.pet = this.shelfItem.item.pet;
                ShelfItem.this.shelfItem.item.xp = this.shelfItem.item.xp;
                ShelfItem.this.shelfItem.item.gems = this.shelfItem.item.gems;
                ShelfItem.this.shelfItem.item.rentInfo = this.shelfItem.item.rentInfo;
                ShelfItem.this.shelfItem.item.companionInfo = this.shelfItem.item.companionInfo;
                ShelfItem.this.shelfItem.item.bind = this.shelfItem.item.bind;
                ShelfItem.this.shelfItem.item.elements = this.shelfItem.item.elements;
                ShelfItem.this.shelfItem.item.mergedItems = this.shelfItem.item.mergedItems;
                ShelfItem.this.shelfItem.packSize = this.shelfItem.packSize;
                ShelfItem.this.shelfItem.price = this.shelfItem.price;
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                return true;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 1);
                return shelfItem_ok;
            }
            
            private boolean unserialize_v313(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 313);
                return shelfItem_ok;
            }
            
            private boolean unserialize_v314(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 314);
                return shelfItem_ok;
            }
            
            private boolean unserialize_v315(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 315);
                return shelfItem_ok;
            }
            
            private boolean unserialize_v10003(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 10003);
                return shelfItem_ok;
            }
            
            private boolean unserialize_v10023(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 10023);
                return shelfItem_ok;
            }
            
            private boolean unserialize_v10028000(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 10028000);
                return shelfItem_ok;
            }
            
            private boolean unserialize_v10029000(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 10029000);
                return shelfItem_ok;
            }
            
            private boolean unserialize_v10032003(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 10032003);
                return shelfItem_ok;
            }
            
            private boolean unserialize_v10035004(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 10035004);
                return shelfItem_ok;
            }
            
            private boolean unserialize_v10035007(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 10035007);
                return shelfItem_ok;
            }
            
            private boolean unserialize_v10036004(final ByteBuffer buffer) {
                final boolean shelfItem_ok = this.shelfItem.unserializeVersion(buffer, 10036004);
                return shelfItem_ok;
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
    
    public static class MerchantDisplayInfo implements VersionableObject
    {
        public byte itemType;
        public byte contentQuantity;
        public byte contentQuality;
        public static final int SERIALIZED_SIZE = 3;
        
        public MerchantDisplayInfo() {
            super();
            this.itemType = 0;
            this.contentQuantity = 0;
            this.contentQuality = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.itemType);
            buffer.put(this.contentQuantity);
            buffer.put(this.contentQuality);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.itemType = buffer.get();
            this.contentQuantity = buffer.get();
            this.contentQuality = buffer.get();
            return true;
        }
        
        @Override
        public void clear() {
            this.itemType = 0;
            this.contentQuantity = 0;
            this.contentQuality = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 3;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("itemType=").append(this.itemType).append('\n');
            repr.append(prefix).append("contentQuantity=").append(this.contentQuantity).append('\n');
            repr.append(prefix).append("contentQuality=").append(this.contentQuality).append('\n');
        }
    }
    
    private final class RawDimensionalBagForSpawnConverter
    {
        private long ownerId;
        private String ownerName;
        private long guildId;
        private int customViewModelId;
        private final ArrayList<ShelfItem> shelfItems;
        private final ArrayList<MerchantDisplayInfo> merchantDisplays;
        
        private RawDimensionalBagForSpawnConverter() {
            super();
            this.ownerId = 0L;
            this.ownerName = null;
            this.guildId = 0L;
            this.customViewModelId = 0;
            this.shelfItems = new ArrayList<ShelfItem>(0);
            this.merchantDisplays = new ArrayList<MerchantDisplayInfo>(0);
        }
        
        public void pushResult() {
            RawDimensionalBagForSpawn.this.ownerId = this.ownerId;
            RawDimensionalBagForSpawn.this.ownerName = this.ownerName;
            RawDimensionalBagForSpawn.this.guildId = this.guildId;
            RawDimensionalBagForSpawn.this.customViewModelId = this.customViewModelId;
            RawDimensionalBagForSpawn.this.shelfItems.clear();
            RawDimensionalBagForSpawn.this.shelfItems.ensureCapacity(this.shelfItems.size());
            RawDimensionalBagForSpawn.this.shelfItems.addAll(this.shelfItems);
            RawDimensionalBagForSpawn.this.merchantDisplays.clear();
            RawDimensionalBagForSpawn.this.merchantDisplays.ensureCapacity(this.merchantDisplays.size());
            RawDimensionalBagForSpawn.this.merchantDisplays.addAll(this.merchantDisplays);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 1);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 1);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 313);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 313);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 314);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 314);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 315);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 315);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 10003);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 10003);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v10005(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 10005);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 10005);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 10023);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 10023);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 10028000);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 10028000);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 10029000);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 10029000);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 10032003);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 10032003);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 10035004);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 10035004);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 10035007);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 10035007);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
            }
            return true;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.get() & 0xFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            this.customViewModelId = buffer.getInt();
            final int shelfItems_size = buffer.get() & 0xFF;
            this.shelfItems.clear();
            this.shelfItems.ensureCapacity(shelfItems_size);
            for (int i = 0; i < shelfItems_size; ++i) {
                final ShelfItem shelfItems_element = new ShelfItem();
                final boolean shelfItems_element_ok = shelfItems_element.unserializeVersion(buffer, 10036004);
                if (!shelfItems_element_ok) {
                    return false;
                }
                this.shelfItems.add(shelfItems_element);
            }
            final int merchantDisplays_size = buffer.get() & 0xFF;
            this.merchantDisplays.clear();
            this.merchantDisplays.ensureCapacity(merchantDisplays_size);
            for (int j = 0; j < merchantDisplays_size; ++j) {
                final MerchantDisplayInfo merchantDisplays_element = new MerchantDisplayInfo();
                final boolean merchantDisplays_element_ok = merchantDisplays_element.unserializeVersion(buffer, 10036004);
                if (!merchantDisplays_element_ok) {
                    return false;
                }
                this.merchantDisplays.add(merchantDisplays_element);
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
        
        public void convert_v10003_to_v10005() {
        }
        
        public void convert_v10005_to_v10023() {
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10023();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10023();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10023();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10023();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10023();
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
            else if (version < 10005) {
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10023();
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
                final boolean ok = this.unserialize_v10005(buffer);
                if (ok) {
                    this.convert_v10005_to_v10023();
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
