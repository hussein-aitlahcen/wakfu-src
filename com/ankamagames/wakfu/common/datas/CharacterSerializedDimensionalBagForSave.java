package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.wakfu.common.rawData.*;

public class CharacterSerializedDimensionalBagForSave extends CharacterSerializedPart implements VersionableObject
{
    public DimensionalBag dimensionalBag;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedDimensionalBagForSave() {
        super();
        this.dimensionalBag = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedDimensionalBagForSave.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedDimensionalBagForSave");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedDimensionalBagForSave", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedDimensionalBagForSave.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedDimensionalBagForSave");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedDimensionalBagForSave", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedDimensionalBagForSave.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.dimensionalBag != null) {
            buffer.put((byte)1);
            final boolean dimensionalBag_ok = this.dimensionalBag.serialize(buffer);
            if (!dimensionalBag_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean dimensionalBag_present = buffer.get() == 1;
        if (dimensionalBag_present) {
            this.dimensionalBag = new DimensionalBag();
            final boolean dimensionalBag_ok = this.dimensionalBag.unserialize(buffer);
            if (!dimensionalBag_ok) {
                return false;
            }
        }
        else {
            this.dimensionalBag = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.dimensionalBag = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037002) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedDimensionalBagForSaveConverter converter = new CharacterSerializedDimensionalBagForSaveConverter();
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
        ++size;
        if (this.dimensionalBag != null) {
            size += this.dimensionalBag.serializedSize();
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
        repr.append(prefix).append("dimensionalBag=");
        if (this.dimensionalBag == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.dimensionalBag.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class DimensionalBag implements VersionableObject
    {
        public final RawDimensionalBagForSave bag;
        
        public DimensionalBag() {
            super();
            this.bag = new RawDimensionalBagForSave();
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
            final DimensionalBagConverter converter = new DimensionalBagConverter();
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
        
        private final class DimensionalBagConverter
        {
            private final RawDimensionalBagForSave bag;
            
            private DimensionalBagConverter() {
                super();
                this.bag = new RawDimensionalBagForSave();
            }
            
            public void pushResult() {
                DimensionalBag.this.bag.rooms.clear();
                DimensionalBag.this.bag.rooms.ensureCapacity(this.bag.rooms.size());
                DimensionalBag.this.bag.rooms.addAll(this.bag.rooms);
                DimensionalBag.this.bag.cash = this.bag.cash;
                DimensionalBag.this.bag.customViewModelId = this.bag.customViewModelId;
                DimensionalBag.this.bag.transactionLog.transactions.clear();
                DimensionalBag.this.bag.transactionLog.transactions.ensureCapacity(this.bag.transactionLog.transactions.size());
                DimensionalBag.this.bag.transactionLog.transactions.addAll(this.bag.transactionLog.transactions);
                DimensionalBag.this.bag.transactionLog.newTransactionsCount = this.bag.transactionLog.newTransactionsCount;
                DimensionalBag.this.bag.transactionLog.newTransactionsKamas = this.bag.transactionLog.newTransactionsKamas;
                DimensionalBag.this.bag.transactionLog.lastReadTransactionDate = this.bag.transactionLog.lastReadTransactionDate;
                DimensionalBag.this.bag.permissions.dimensionalBagLocked = this.bag.permissions.dimensionalBagLocked;
                DimensionalBag.this.bag.permissions.groupEntries.clear();
                DimensionalBag.this.bag.permissions.groupEntries.ensureCapacity(this.bag.permissions.groupEntries.size());
                DimensionalBag.this.bag.permissions.groupEntries.addAll(this.bag.permissions.groupEntries);
                DimensionalBag.this.bag.permissions.individualEntries.clear();
                DimensionalBag.this.bag.permissions.individualEntries.ensureCapacity(this.bag.permissions.individualEntries.size());
                DimensionalBag.this.bag.permissions.individualEntries.addAll(this.bag.permissions.individualEntries);
                DimensionalBag.this.bag.ecosystem.lastUpdateTime = this.bag.ecosystem.lastUpdateTime;
                DimensionalBag.this.bag.ecosystem.resources.clear();
                DimensionalBag.this.bag.ecosystem.resources.ensureCapacity(this.bag.ecosystem.resources.size());
                DimensionalBag.this.bag.ecosystem.resources.addAll(this.bag.ecosystem.resources);
                DimensionalBag.this.bag.storageBox.rawStorageBox = this.bag.storageBox.rawStorageBox;
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
            
            public void convert_v10003_to_v10014() {
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
                        this.convert_v10003_to_v10014();
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
                        this.convert_v10003_to_v10014();
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
                        this.convert_v10003_to_v10014();
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
                        this.convert_v10003_to_v10014();
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
                        this.convert_v10003_to_v10014();
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
                        this.convert_v10003_to_v10014();
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
                    final boolean ok = this.unserialize_v10003(buffer);
                    if (ok) {
                        this.convert_v10003_to_v10014();
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
    
    private final class CharacterSerializedDimensionalBagForSaveConverter
    {
        private DimensionalBag dimensionalBag;
        
        private CharacterSerializedDimensionalBagForSaveConverter() {
            super();
            this.dimensionalBag = null;
        }
        
        public void pushResult() {
            CharacterSerializedDimensionalBagForSave.this.dimensionalBag = this.dimensionalBag;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 1);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v309(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 309);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 313);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 314);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 315);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 10003);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v10014(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 10014);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 10023);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v1027001(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 1027001);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v1027002(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 1027002);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 10028000);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 10029000);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 10032003);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 10035004);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 10035007);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 10036004);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
        }
        
        private boolean unserialize_v10037001(final ByteBuffer buffer) {
            final boolean dimensionalBag_present = buffer.get() == 1;
            if (dimensionalBag_present) {
                this.dimensionalBag = new DimensionalBag();
                final boolean dimensionalBag_ok = this.dimensionalBag.unserializeVersion(buffer, 10037001);
                if (!dimensionalBag_ok) {
                    return false;
                }
            }
            else {
                this.dimensionalBag = null;
            }
            return true;
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
        
        public void convert_v10003_to_v10014() {
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
                    this.convert_v10003_to_v10014();
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
                    this.convert_v10003_to_v10014();
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
                    this.convert_v10003_to_v10014();
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
                    this.convert_v10003_to_v10014();
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
                    this.convert_v10003_to_v10014();
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
                    this.convert_v10003_to_v10014();
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
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10014();
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
