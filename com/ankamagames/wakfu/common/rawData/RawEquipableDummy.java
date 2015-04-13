package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawEquipableDummy implements VersionableObject
{
    public int setPackId;
    public final RawInventoryItem item;
    
    public RawEquipableDummy() {
        super();
        this.setPackId = 0;
        this.item = new RawInventoryItem();
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.setPackId);
        final boolean item_ok = this.item.serialize(buffer);
        return item_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.setPackId = buffer.getInt();
        final boolean item_ok = this.item.unserialize(buffer);
        return item_ok;
    }
    
    @Override
    public void clear() {
        this.setPackId = 0;
        this.item.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037002) {
            return this.unserialize(buffer);
        }
        final RawEquipableDummyConverter converter = new RawEquipableDummyConverter();
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
        size += 4;
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
        repr.append(prefix).append("setPackId=").append(this.setPackId).append('\n');
        repr.append(prefix).append("item=...\n");
        this.item.internalToString(repr, prefix + "  ");
    }
    
    private final class RawEquipableDummyConverter
    {
        private int setPackId;
        private final RawInventoryItem item;
        
        private RawEquipableDummyConverter() {
            super();
            this.setPackId = 0;
            this.item = new RawInventoryItem();
        }
        
        public void pushResult() {
            RawEquipableDummy.this.setPackId = this.setPackId;
            RawEquipableDummy.this.item.uniqueId = this.item.uniqueId;
            RawEquipableDummy.this.item.refId = this.item.refId;
            RawEquipableDummy.this.item.quantity = this.item.quantity;
            RawEquipableDummy.this.item.timestamp = this.item.timestamp;
            RawEquipableDummy.this.item.pet = this.item.pet;
            RawEquipableDummy.this.item.xp = this.item.xp;
            RawEquipableDummy.this.item.gems = this.item.gems;
            RawEquipableDummy.this.item.rentInfo = this.item.rentInfo;
            RawEquipableDummy.this.item.companionInfo = this.item.companionInfo;
            RawEquipableDummy.this.item.bind = this.item.bind;
            RawEquipableDummy.this.item.elements = this.item.elements;
            RawEquipableDummy.this.item.mergedItems = this.item.mergedItems;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
            return true;
        }
        
        private boolean unserialize_v10037001(final ByteBuffer buffer) {
            this.setPackId = buffer.getInt();
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
        
        public void convert_v10037001_to_v10037002() {
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else if (version < 10036004) {
                final boolean ok = this.unserialize_v10035007(buffer);
                if (ok) {
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else if (version < 10037001) {
                final boolean ok = this.unserialize_v10036004(buffer);
                if (ok) {
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10037002) {
                    return false;
                }
                final boolean ok = this.unserialize_v10037001(buffer);
                if (ok) {
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
        }
    }
}
