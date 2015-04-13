package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;

public class CharacterSerializedDimensionalBagForClient extends CharacterSerializedPart implements VersionableObject
{
    public final RawDimensionalBagForClient bag;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedDimensionalBagForClient() {
        super();
        this.bag = new RawDimensionalBagForClient();
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedDimensionalBagForClient.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedDimensionalBagForClient");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedDimensionalBagForClient", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedDimensionalBagForClient.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedDimensionalBagForClient");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedDimensionalBagForClient", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedDimensionalBagForClient.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean bag_ok = this.bag.serialize(buffer);
        return bag_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean bag_ok = this.bag.unserialize(buffer);
        return bag_ok;
    }
    
    @Override
    public void clear() {
        this.bag.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037002) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedDimensionalBagForClientConverter converter = new CharacterSerializedDimensionalBagForClientConverter();
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
        size += this.bag.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("bag=...\n");
        this.bag.internalToString(repr, prefix + "  ");
    }
    
    private final class CharacterSerializedDimensionalBagForClientConverter
    {
        private final RawDimensionalBagForClient bag;
        
        private CharacterSerializedDimensionalBagForClientConverter() {
            super();
            this.bag = new RawDimensionalBagForClient();
        }
        
        public void pushResult() {
            CharacterSerializedDimensionalBagForClient.this.bag.ownerId = this.bag.ownerId;
            CharacterSerializedDimensionalBagForClient.this.bag.ownerName = this.bag.ownerName;
            CharacterSerializedDimensionalBagForClient.this.bag.guildId = this.bag.guildId;
            CharacterSerializedDimensionalBagForClient.this.bag.rooms.clear();
            CharacterSerializedDimensionalBagForClient.this.bag.rooms.ensureCapacity(this.bag.rooms.size());
            CharacterSerializedDimensionalBagForClient.this.bag.rooms.addAll(this.bag.rooms);
            CharacterSerializedDimensionalBagForClient.this.bag.customViewModelId = this.bag.customViewModelId;
            CharacterSerializedDimensionalBagForClient.this.bag.wallet = this.bag.wallet;
            CharacterSerializedDimensionalBagForClient.this.bag.permissions.dimensionalBagLocked = this.bag.permissions.dimensionalBagLocked;
            CharacterSerializedDimensionalBagForClient.this.bag.permissions.groupEntries.clear();
            CharacterSerializedDimensionalBagForClient.this.bag.permissions.groupEntries.ensureCapacity(this.bag.permissions.groupEntries.size());
            CharacterSerializedDimensionalBagForClient.this.bag.permissions.groupEntries.addAll(this.bag.permissions.groupEntries);
            CharacterSerializedDimensionalBagForClient.this.bag.permissions.individualEntries.clear();
            CharacterSerializedDimensionalBagForClient.this.bag.permissions.individualEntries.ensureCapacity(this.bag.permissions.individualEntries.size());
            CharacterSerializedDimensionalBagForClient.this.bag.permissions.individualEntries.addAll(this.bag.permissions.individualEntries);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 1);
            return bag_ok;
        }
        
        private boolean unserialize_v309(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 309);
            return bag_ok;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 313);
            return bag_ok;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 314);
            return bag_ok;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 315);
            return bag_ok;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 10003);
            return bag_ok;
        }
        
        private boolean unserialize_v10005(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 10005);
            return bag_ok;
        }
        
        private boolean unserialize_v10014(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 10014);
            return bag_ok;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 10023);
            return bag_ok;
        }
        
        private boolean unserialize_v1027001(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 1027001);
            return bag_ok;
        }
        
        private boolean unserialize_v1027002(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 1027002);
            return bag_ok;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 10028000);
            return bag_ok;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 10029000);
            return bag_ok;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 10032003);
            return bag_ok;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 10035004);
            return bag_ok;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 10035007);
            return bag_ok;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 10036004);
            return bag_ok;
        }
        
        private boolean unserialize_v10037001(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserializeVersion(buffer, 10037001);
            return bag_ok;
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
        
        public void convert_v10003_to_v10005() {
        }
        
        public void convert_v10005_to_v10014() {
        }
        
        public void convert_v10014_to_v10023() {
        }
        
        public void convert_v10023_to_v1027001() {
        }
        
        public void convert_v1027001_to_v1027002() {
        }
        
        public void convert_v1027002_to_v10028000() {
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
                    this.convert_v1_to_v309();
                    this.convert_v309_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
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
            else if (version < 309) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v309();
                    this.convert_v309_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
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
                final boolean ok = this.unserialize_v309(buffer);
                if (ok) {
                    this.convert_v309_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
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
            else if (version < 10005) {
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
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
            else if (version < 10014) {
                final boolean ok = this.unserialize_v10005(buffer);
                if (ok) {
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
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
                final boolean ok = this.unserialize_v10014(buffer);
                if (ok) {
                    this.convert_v10014_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
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
            else if (version < 1027001) {
                final boolean ok = this.unserialize_v10023(buffer);
                if (ok) {
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
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
            else if (version < 1027002) {
                final boolean ok = this.unserialize_v1027001(buffer);
                if (ok) {
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
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
                final boolean ok = this.unserialize_v1027002(buffer);
                if (ok) {
                    this.convert_v1027002_to_v10028000();
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
