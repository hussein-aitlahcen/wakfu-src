package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawInventoryHandler implements VersionableObject
{
    public final RawQuestItemInventory questInventory;
    public final RawInventoryItemInventory temporaryInventory;
    public final RawCosmeticsItemInventory cosmeticsInventory;
    public final RawCosmeticsItemInventory petCosmeticsInventory;
    
    public RawInventoryHandler() {
        super();
        this.questInventory = new RawQuestItemInventory();
        this.temporaryInventory = new RawInventoryItemInventory();
        this.cosmeticsInventory = new RawCosmeticsItemInventory();
        this.petCosmeticsInventory = new RawCosmeticsItemInventory();
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean questInventory_ok = this.questInventory.serialize(buffer);
        if (!questInventory_ok) {
            return false;
        }
        final boolean temporaryInventory_ok = this.temporaryInventory.serialize(buffer);
        if (!temporaryInventory_ok) {
            return false;
        }
        final boolean cosmeticsInventory_ok = this.cosmeticsInventory.serialize(buffer);
        if (!cosmeticsInventory_ok) {
            return false;
        }
        final boolean petCosmeticsInventory_ok = this.petCosmeticsInventory.serialize(buffer);
        return petCosmeticsInventory_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean questInventory_ok = this.questInventory.unserialize(buffer);
        if (!questInventory_ok) {
            return false;
        }
        final boolean temporaryInventory_ok = this.temporaryInventory.unserialize(buffer);
        if (!temporaryInventory_ok) {
            return false;
        }
        final boolean cosmeticsInventory_ok = this.cosmeticsInventory.unserialize(buffer);
        if (!cosmeticsInventory_ok) {
            return false;
        }
        final boolean petCosmeticsInventory_ok = this.petCosmeticsInventory.unserialize(buffer);
        return petCosmeticsInventory_ok;
    }
    
    @Override
    public void clear() {
        this.questInventory.clear();
        this.temporaryInventory.clear();
        this.cosmeticsInventory.clear();
        this.petCosmeticsInventory.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawInventoryHandlerConverter converter = new RawInventoryHandlerConverter();
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
        size += this.questInventory.serializedSize();
        size += this.temporaryInventory.serializedSize();
        size += this.cosmeticsInventory.serializedSize();
        size += this.petCosmeticsInventory.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("questInventory=...\n");
        this.questInventory.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("temporaryInventory=...\n");
        this.temporaryInventory.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("cosmeticsInventory=...\n");
        this.cosmeticsInventory.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("petCosmeticsInventory=...\n");
        this.petCosmeticsInventory.internalToString(repr, prefix + "  ");
    }
    
    private final class RawInventoryHandlerConverter
    {
        private final RawQuestItemInventory questInventory;
        private final RawInventoryItemInventory temporaryInventory;
        private final RawCosmeticsItemInventory cosmeticsInventory;
        private final RawCosmeticsItemInventory petCosmeticsInventory;
        
        private RawInventoryHandlerConverter() {
            super();
            this.questInventory = new RawQuestItemInventory();
            this.temporaryInventory = new RawInventoryItemInventory();
            this.cosmeticsInventory = new RawCosmeticsItemInventory();
            this.petCosmeticsInventory = new RawCosmeticsItemInventory();
        }
        
        public void pushResult() {
            RawInventoryHandler.this.questInventory.items.clear();
            RawInventoryHandler.this.questInventory.items.ensureCapacity(this.questInventory.items.size());
            RawInventoryHandler.this.questInventory.items.addAll(this.questInventory.items);
            RawInventoryHandler.this.temporaryInventory.contents.clear();
            RawInventoryHandler.this.temporaryInventory.contents.ensureCapacity(this.temporaryInventory.contents.size());
            RawInventoryHandler.this.temporaryInventory.contents.addAll(this.temporaryInventory.contents);
            RawInventoryHandler.this.cosmeticsInventory.items.clear();
            RawInventoryHandler.this.cosmeticsInventory.items.ensureCapacity(this.cosmeticsInventory.items.size());
            RawInventoryHandler.this.cosmeticsInventory.items.addAll(this.cosmeticsInventory.items);
            RawInventoryHandler.this.petCosmeticsInventory.items.clear();
            RawInventoryHandler.this.petCosmeticsInventory.items.ensureCapacity(this.petCosmeticsInventory.items.size());
            RawInventoryHandler.this.petCosmeticsInventory.items.addAll(this.petCosmeticsInventory.items);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v10001(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10001);
            return questInventory_ok;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10003);
            return questInventory_ok;
        }
        
        private boolean unserialize_v10007(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10007);
            if (!questInventory_ok) {
                return false;
            }
            final boolean temporaryInventory_ok = this.temporaryInventory.unserializeVersion(buffer, 10007);
            return temporaryInventory_ok;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10023);
            if (!questInventory_ok) {
                return false;
            }
            final boolean temporaryInventory_ok = this.temporaryInventory.unserializeVersion(buffer, 10023);
            return temporaryInventory_ok;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10028000);
            if (!questInventory_ok) {
                return false;
            }
            final boolean temporaryInventory_ok = this.temporaryInventory.unserializeVersion(buffer, 10028000);
            return temporaryInventory_ok;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10029000);
            if (!questInventory_ok) {
                return false;
            }
            final boolean temporaryInventory_ok = this.temporaryInventory.unserializeVersion(buffer, 10029000);
            return temporaryInventory_ok;
        }
        
        private boolean unserialize_v10032001(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10032001);
            if (!questInventory_ok) {
                return false;
            }
            final boolean temporaryInventory_ok = this.temporaryInventory.unserializeVersion(buffer, 10032001);
            if (!temporaryInventory_ok) {
                return false;
            }
            final boolean cosmeticsInventory_ok = this.cosmeticsInventory.unserializeVersion(buffer, 10032001);
            return cosmeticsInventory_ok;
        }
        
        private boolean unserialize_v10032002(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10032002);
            if (!questInventory_ok) {
                return false;
            }
            final boolean temporaryInventory_ok = this.temporaryInventory.unserializeVersion(buffer, 10032002);
            if (!temporaryInventory_ok) {
                return false;
            }
            final boolean cosmeticsInventory_ok = this.cosmeticsInventory.unserializeVersion(buffer, 10032002);
            if (!cosmeticsInventory_ok) {
                return false;
            }
            final boolean petCosmeticsInventory_ok = this.petCosmeticsInventory.unserializeVersion(buffer, 10032002);
            return petCosmeticsInventory_ok;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10032003);
            if (!questInventory_ok) {
                return false;
            }
            final boolean temporaryInventory_ok = this.temporaryInventory.unserializeVersion(buffer, 10032003);
            if (!temporaryInventory_ok) {
                return false;
            }
            final boolean cosmeticsInventory_ok = this.cosmeticsInventory.unserializeVersion(buffer, 10032003);
            if (!cosmeticsInventory_ok) {
                return false;
            }
            final boolean petCosmeticsInventory_ok = this.petCosmeticsInventory.unserializeVersion(buffer, 10032003);
            return petCosmeticsInventory_ok;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10035004);
            if (!questInventory_ok) {
                return false;
            }
            final boolean temporaryInventory_ok = this.temporaryInventory.unserializeVersion(buffer, 10035004);
            if (!temporaryInventory_ok) {
                return false;
            }
            final boolean cosmeticsInventory_ok = this.cosmeticsInventory.unserializeVersion(buffer, 10035004);
            if (!cosmeticsInventory_ok) {
                return false;
            }
            final boolean petCosmeticsInventory_ok = this.petCosmeticsInventory.unserializeVersion(buffer, 10035004);
            return petCosmeticsInventory_ok;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10035007);
            if (!questInventory_ok) {
                return false;
            }
            final boolean temporaryInventory_ok = this.temporaryInventory.unserializeVersion(buffer, 10035007);
            if (!temporaryInventory_ok) {
                return false;
            }
            final boolean cosmeticsInventory_ok = this.cosmeticsInventory.unserializeVersion(buffer, 10035007);
            if (!cosmeticsInventory_ok) {
                return false;
            }
            final boolean petCosmeticsInventory_ok = this.petCosmeticsInventory.unserializeVersion(buffer, 10035007);
            return petCosmeticsInventory_ok;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            final boolean questInventory_ok = this.questInventory.unserializeVersion(buffer, 10036004);
            if (!questInventory_ok) {
                return false;
            }
            final boolean temporaryInventory_ok = this.temporaryInventory.unserializeVersion(buffer, 10036004);
            if (!temporaryInventory_ok) {
                return false;
            }
            final boolean cosmeticsInventory_ok = this.cosmeticsInventory.unserializeVersion(buffer, 10036004);
            if (!cosmeticsInventory_ok) {
                return false;
            }
            final boolean petCosmeticsInventory_ok = this.petCosmeticsInventory.unserializeVersion(buffer, 10036004);
            return petCosmeticsInventory_ok;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v313() {
        }
        
        public void convert_v313_to_v314() {
        }
        
        public void convert_v314_to_v315() {
        }
        
        public void convert_v315_to_v10001() {
        }
        
        public void convert_v10001_to_v10003() {
        }
        
        public void convert_v10003_to_v10007() {
        }
        
        public void convert_v10007_to_v10023() {
        }
        
        public void convert_v10023_to_v10028000() {
        }
        
        public void convert_v10028000_to_v10029000() {
        }
        
        public void convert_v10029000_to_v10032001() {
        }
        
        public void convert_v10032001_to_v10032002() {
        }
        
        public void convert_v10032002_to_v10032003() {
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
                    this.convert_v315_to_v10001();
                    this.convert_v10001_to_v10003();
                    this.convert_v10003_to_v10007();
                    this.convert_v10007_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032001();
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
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
                    this.convert_v315_to_v10001();
                    this.convert_v10001_to_v10003();
                    this.convert_v10003_to_v10007();
                    this.convert_v10007_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032001();
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
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
                    this.convert_v315_to_v10001();
                    this.convert_v10001_to_v10003();
                    this.convert_v10003_to_v10007();
                    this.convert_v10007_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032001();
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
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
                    this.convert_v315_to_v10001();
                    this.convert_v10001_to_v10003();
                    this.convert_v10003_to_v10007();
                    this.convert_v10007_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032001();
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10001) {
                final boolean ok = this.unserialize_v315(buffer);
                if (ok) {
                    this.convert_v315_to_v10001();
                    this.convert_v10001_to_v10003();
                    this.convert_v10003_to_v10007();
                    this.convert_v10007_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032001();
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10003) {
                final boolean ok = this.unserialize_v10001(buffer);
                if (ok) {
                    this.convert_v10001_to_v10003();
                    this.convert_v10003_to_v10007();
                    this.convert_v10007_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032001();
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10007) {
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10007();
                    this.convert_v10007_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032001();
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10023) {
                final boolean ok = this.unserialize_v10007(buffer);
                if (ok) {
                    this.convert_v10007_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032001();
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
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
                    this.convert_v10029000_to_v10032001();
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
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
                    this.convert_v10029000_to_v10032001();
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10032001) {
                final boolean ok = this.unserialize_v10029000(buffer);
                if (ok) {
                    this.convert_v10029000_to_v10032001();
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10032002) {
                final boolean ok = this.unserialize_v10032001(buffer);
                if (ok) {
                    this.convert_v10032001_to_v10032002();
                    this.convert_v10032002_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10032003) {
                final boolean ok = this.unserialize_v10032002(buffer);
                if (ok) {
                    this.convert_v10032002_to_v10032003();
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
