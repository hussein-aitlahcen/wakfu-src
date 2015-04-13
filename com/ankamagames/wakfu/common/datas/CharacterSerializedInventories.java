package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;

public class CharacterSerializedInventories extends CharacterSerializedPart implements VersionableObject
{
    public final RawInventoryHandler inventoryHandler;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedInventories() {
        super();
        this.inventoryHandler = new RawInventoryHandler();
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedInventories.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedInventories");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedInventories", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedInventories.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedInventories");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedInventories", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedInventories.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean inventoryHandler_ok = this.inventoryHandler.serialize(buffer);
        return inventoryHandler_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean inventoryHandler_ok = this.inventoryHandler.unserialize(buffer);
        return inventoryHandler_ok;
    }
    
    @Override
    public void clear() {
        this.inventoryHandler.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedInventoriesConverter converter = new CharacterSerializedInventoriesConverter();
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
        size += this.inventoryHandler.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("inventoryHandler=...\n");
        this.inventoryHandler.internalToString(repr, prefix + "  ");
    }
    
    private final class CharacterSerializedInventoriesConverter
    {
        private final RawInventoryHandler inventoryHandler;
        
        private CharacterSerializedInventoriesConverter() {
            super();
            this.inventoryHandler = new RawInventoryHandler();
        }
        
        public void pushResult() {
            CharacterSerializedInventories.this.inventoryHandler.questInventory.items.clear();
            CharacterSerializedInventories.this.inventoryHandler.questInventory.items.ensureCapacity(this.inventoryHandler.questInventory.items.size());
            CharacterSerializedInventories.this.inventoryHandler.questInventory.items.addAll(this.inventoryHandler.questInventory.items);
            CharacterSerializedInventories.this.inventoryHandler.temporaryInventory.contents.clear();
            CharacterSerializedInventories.this.inventoryHandler.temporaryInventory.contents.ensureCapacity(this.inventoryHandler.temporaryInventory.contents.size());
            CharacterSerializedInventories.this.inventoryHandler.temporaryInventory.contents.addAll(this.inventoryHandler.temporaryInventory.contents);
            CharacterSerializedInventories.this.inventoryHandler.cosmeticsInventory.items.clear();
            CharacterSerializedInventories.this.inventoryHandler.cosmeticsInventory.items.ensureCapacity(this.inventoryHandler.cosmeticsInventory.items.size());
            CharacterSerializedInventories.this.inventoryHandler.cosmeticsInventory.items.addAll(this.inventoryHandler.cosmeticsInventory.items);
            CharacterSerializedInventories.this.inventoryHandler.petCosmeticsInventory.items.clear();
            CharacterSerializedInventories.this.inventoryHandler.petCosmeticsInventory.items.ensureCapacity(this.inventoryHandler.petCosmeticsInventory.items.size());
            CharacterSerializedInventories.this.inventoryHandler.petCosmeticsInventory.items.addAll(this.inventoryHandler.petCosmeticsInventory.items);
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
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10001);
            return inventoryHandler_ok;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10003);
            return inventoryHandler_ok;
        }
        
        private boolean unserialize_v10007(final ByteBuffer buffer) {
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10007);
            return inventoryHandler_ok;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10023);
            return inventoryHandler_ok;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10028000);
            return inventoryHandler_ok;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10029000);
            return inventoryHandler_ok;
        }
        
        private boolean unserialize_v10032001(final ByteBuffer buffer) {
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10032001);
            return inventoryHandler_ok;
        }
        
        private boolean unserialize_v10032002(final ByteBuffer buffer) {
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10032002);
            return inventoryHandler_ok;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10032003);
            return inventoryHandler_ok;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10035004);
            return inventoryHandler_ok;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10035007);
            return inventoryHandler_ok;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            final boolean inventoryHandler_ok = this.inventoryHandler.unserializeVersion(buffer, 10036004);
            return inventoryHandler_ok;
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
