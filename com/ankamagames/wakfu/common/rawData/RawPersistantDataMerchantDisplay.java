package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawPersistantDataMerchantDisplay extends AbstractRawPersistantData implements VersionableObject
{
    public final RawMerchantItemInventory content;
    public static final byte VIRTUAL_ID = 1;
    
    public RawPersistantDataMerchantDisplay() {
        super();
        this.content = new RawMerchantItemInventory();
    }
    
    @Override
    public byte getVirtualId() {
        return 1;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean content_ok = this.content.serialize(buffer);
        return content_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean content_ok = this.content.unserialize(buffer);
        return content_ok;
    }
    
    @Override
    public void clear() {
        this.content.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawPersistantDataMerchantDisplayConverter converter = new RawPersistantDataMerchantDisplayConverter();
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
        size += this.content.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    @Override
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("content=...\n");
        this.content.internalToString(repr, prefix + "  ");
    }
    
    private final class RawPersistantDataMerchantDisplayConverter
    {
        private final RawMerchantItemInventory content;
        
        private RawPersistantDataMerchantDisplayConverter() {
            super();
            this.content = new RawMerchantItemInventory();
        }
        
        public void pushResult() {
            RawPersistantDataMerchantDisplay.this.content.uid = this.content.uid;
            RawPersistantDataMerchantDisplay.this.content.requiredItemType = this.content.requiredItemType;
            RawPersistantDataMerchantDisplay.this.content.nSlots = this.content.nSlots;
            RawPersistantDataMerchantDisplay.this.content.maxPackSize = this.content.maxPackSize;
            RawPersistantDataMerchantDisplay.this.content.contents.clear();
            RawPersistantDataMerchantDisplay.this.content.contents.ensureCapacity(this.content.contents.size());
            RawPersistantDataMerchantDisplay.this.content.contents.addAll(this.content.contents);
            RawPersistantDataMerchantDisplay.this.content.shortAd = this.content.shortAd;
            RawPersistantDataMerchantDisplay.this.content.locked = this.content.locked;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 1);
            return content_ok;
        }
        
        private boolean unserialize_v309(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 309);
            return content_ok;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 313);
            return content_ok;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 314);
            return content_ok;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 315);
            return content_ok;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 10003);
            return content_ok;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 10023);
            return content_ok;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 10028000);
            return content_ok;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 10029000);
            return content_ok;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 10032003);
            return content_ok;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 10035004);
            return content_ok;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 10035007);
            return content_ok;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            final boolean content_ok = this.content.unserializeVersion(buffer, 10036004);
            return content_ok;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v309() {
        }
        
        public void convert_v309_to_v313() {
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
                    this.convert_v1_to_v309();
                    this.convert_v309_to_v313();
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
            else if (version < 309) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v309();
                    this.convert_v309_to_v313();
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
                final boolean ok = this.unserialize_v309(buffer);
                if (ok) {
                    this.convert_v309_to_v313();
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
