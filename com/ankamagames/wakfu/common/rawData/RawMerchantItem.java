package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawMerchantItem implements VersionableObject
{
    public final RawInventoryItem item;
    public short packSize;
    public int price;
    
    public RawMerchantItem() {
        super();
        this.item = new RawInventoryItem();
        this.packSize = 1;
        this.price = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean item_ok = this.item.serialize(buffer);
        if (!item_ok) {
            return false;
        }
        buffer.putShort(this.packSize);
        buffer.putInt(this.price);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean item_ok = this.item.unserialize(buffer);
        if (!item_ok) {
            return false;
        }
        this.packSize = buffer.getShort();
        this.price = buffer.getInt();
        return true;
    }
    
    @Override
    public void clear() {
        this.item.clear();
        this.packSize = 1;
        this.price = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawMerchantItemConverter converter = new RawMerchantItemConverter();
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
        size += this.item.serializedSize();
        size += 2;
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
        repr.append(prefix).append("item=...\n");
        this.item.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("packSize=").append(this.packSize).append('\n');
        repr.append(prefix).append("price=").append(this.price).append('\n');
    }
    
    private final class RawMerchantItemConverter
    {
        private final RawInventoryItem item;
        private short packSize;
        private int price;
        
        private RawMerchantItemConverter() {
            super();
            this.item = new RawInventoryItem();
            this.packSize = 1;
            this.price = 0;
        }
        
        public void pushResult() {
            RawMerchantItem.this.item.uniqueId = this.item.uniqueId;
            RawMerchantItem.this.item.refId = this.item.refId;
            RawMerchantItem.this.item.quantity = this.item.quantity;
            RawMerchantItem.this.item.timestamp = this.item.timestamp;
            RawMerchantItem.this.item.pet = this.item.pet;
            RawMerchantItem.this.item.xp = this.item.xp;
            RawMerchantItem.this.item.gems = this.item.gems;
            RawMerchantItem.this.item.rentInfo = this.item.rentInfo;
            RawMerchantItem.this.item.companionInfo = this.item.companionInfo;
            RawMerchantItem.this.item.bind = this.item.bind;
            RawMerchantItem.this.item.elements = this.item.elements;
            RawMerchantItem.this.item.mergedItems = this.item.mergedItems;
            RawMerchantItem.this.packSize = this.packSize;
            RawMerchantItem.this.price = this.price;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 1);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 313);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 314);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 315);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 10003);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 10023);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 10028000);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 10029000);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 10032003);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 10035004);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 10035007);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            final boolean item_ok = this.item.unserializeVersion(buffer, 10036004);
            if (!item_ok) {
                return false;
            }
            this.packSize = buffer.getShort();
            this.price = buffer.getInt();
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
