package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;

public class CharacterSerializedAptitudeBonusInventory extends CharacterSerializedPart implements VersionableObject
{
    public Optional optional;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedAptitudeBonusInventory() {
        super();
        this.optional = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedAptitudeBonusInventory.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedAptitudeBonusInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedAptitudeBonusInventory", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedAptitudeBonusInventory.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAptitudeBonusInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAptitudeBonusInventory", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedAptitudeBonusInventory.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.optional != null) {
            buffer.put((byte)1);
            final boolean optional_ok = this.optional.serialize(buffer);
            if (!optional_ok) {
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
        final boolean optional_present = buffer.get() == 1;
        if (optional_present) {
            this.optional = new Optional();
            final boolean optional_ok = this.optional.unserialize(buffer);
            if (!optional_ok) {
                return false;
            }
        }
        else {
            this.optional = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.optional = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10036010) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedAptitudeBonusInventoryConverter converter = new CharacterSerializedAptitudeBonusInventoryConverter();
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
        if (this.optional != null) {
            size += this.optional.serializedSize();
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
        repr.append(prefix).append("optional=");
        if (this.optional == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.optional.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class Optional implements VersionableObject
    {
        public final RawAptitudeBonusInventory aptitudeInventory;
        
        public Optional() {
            super();
            this.aptitudeInventory = new RawAptitudeBonusInventory();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean aptitudeInventory_ok = this.aptitudeInventory.serialize(buffer);
            return aptitudeInventory_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean aptitudeInventory_ok = this.aptitudeInventory.unserialize(buffer);
            return aptitudeInventory_ok;
        }
        
        @Override
        public void clear() {
            this.aptitudeInventory.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.aptitudeInventory.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("aptitudeInventory=...\n");
            this.aptitudeInventory.internalToString(repr, prefix + "  ");
        }
    }
    
    private final class CharacterSerializedAptitudeBonusInventoryConverter
    {
        private Optional optional;
        
        private CharacterSerializedAptitudeBonusInventoryConverter() {
            super();
            this.optional = null;
        }
        
        public void pushResult() {
            CharacterSerializedAptitudeBonusInventory.this.optional = this.optional;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        public void convert_v0_to_v10036010() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version >= 10036010) {
                return false;
            }
            final boolean ok = this.unserialize_v0(buffer);
            if (ok) {
                this.convert_v0_to_v10036010();
                return true;
            }
            return false;
        }
    }
}
