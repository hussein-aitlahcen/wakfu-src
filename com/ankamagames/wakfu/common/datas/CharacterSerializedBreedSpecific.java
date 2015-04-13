package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.util.*;

public class CharacterSerializedBreedSpecific extends CharacterSerializedPart implements VersionableObject
{
    public OsaSpecific osaSpecific;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedBreedSpecific() {
        super();
        this.osaSpecific = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedBreedSpecific.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedBreedSpecific");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedBreedSpecific", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedBreedSpecific.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedBreedSpecific");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedBreedSpecific", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedBreedSpecific.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.osaSpecific != null) {
            buffer.put((byte)1);
            final boolean osaSpecific_ok = this.osaSpecific.serialize(buffer);
            if (!osaSpecific_ok) {
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
        final boolean osaSpecific_present = buffer.get() == 1;
        if (osaSpecific_present) {
            this.osaSpecific = new OsaSpecific();
            final boolean osaSpecific_ok = this.osaSpecific.unserialize(buffer);
            if (!osaSpecific_ok) {
                return false;
            }
        }
        else {
            this.osaSpecific = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.osaSpecific = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10013) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedBreedSpecificConverter converter = new CharacterSerializedBreedSpecificConverter();
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
        if (this.osaSpecific != null) {
            size += this.osaSpecific.serializedSize();
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
        repr.append(prefix).append("osaSpecific=");
        if (this.osaSpecific == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.osaSpecific.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class OsaSpecific implements VersionableObject
    {
        public final RawSymbiot symbiot;
        
        public OsaSpecific() {
            super();
            this.symbiot = new RawSymbiot();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean symbiot_ok = this.symbiot.serialize(buffer);
            return symbiot_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean symbiot_ok = this.symbiot.unserialize(buffer);
            return symbiot_ok;
        }
        
        @Override
        public void clear() {
            this.symbiot.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10013) {
                return this.unserialize(buffer);
            }
            final OsaSpecificConverter converter = new OsaSpecificConverter();
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
            size += this.symbiot.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("symbiot=...\n");
            this.symbiot.internalToString(repr, prefix + "  ");
        }
        
        private final class OsaSpecificConverter
        {
            private final RawSymbiot symbiot;
            
            private OsaSpecificConverter() {
                super();
                this.symbiot = new RawSymbiot();
            }
            
            public void pushResult() {
                OsaSpecific.this.symbiot.capturedCreatures.clear();
                OsaSpecific.this.symbiot.capturedCreatures.ensureCapacity(this.symbiot.capturedCreatures.size());
                OsaSpecific.this.symbiot.capturedCreatures.addAll(this.symbiot.capturedCreatures);
                OsaSpecific.this.symbiot.currentCreatureIndex = this.symbiot.currentCreatureIndex;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean symbiot_ok = this.symbiot.unserializeVersion(buffer, 1);
                return symbiot_ok;
            }
            
            public void convert_v1_to_v10013() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10013) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10013();
                    return true;
                }
                return false;
            }
        }
    }
    
    private final class CharacterSerializedBreedSpecificConverter
    {
        private OsaSpecific osaSpecific;
        
        private CharacterSerializedBreedSpecificConverter() {
            super();
            this.osaSpecific = null;
        }
        
        public void pushResult() {
            CharacterSerializedBreedSpecific.this.osaSpecific = this.osaSpecific;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean osaSpecific_present = buffer.get() == 1;
            if (osaSpecific_present) {
                this.osaSpecific = new OsaSpecific();
                final boolean osaSpecific_ok = this.osaSpecific.unserializeVersion(buffer, 1);
                if (!osaSpecific_ok) {
                    return false;
                }
            }
            else {
                this.osaSpecific = null;
            }
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v10013() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 1) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    this.convert_v1_to_v10013();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10013) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10013();
                    return true;
                }
                return false;
            }
        }
    }
}
