package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedCharacteristics extends CharacterSerializedPart implements VersionableObject
{
    public final RawCharacteristics characteristics;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedCharacteristics() {
        super();
        this.characteristics = new RawCharacteristics();
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedCharacteristics.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedCharacteristics");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedCharacteristics", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedCharacteristics.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedCharacteristics");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedCharacteristics", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedCharacteristics.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean characteristics_ok = this.characteristics.serialize(buffer);
        return characteristics_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean characteristics_ok = this.characteristics.unserialize(buffer);
        return characteristics_ok;
    }
    
    @Override
    public void clear() {
        this.characteristics.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10036003) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedCharacteristicsConverter converter = new CharacterSerializedCharacteristicsConverter();
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
        size += this.characteristics.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("characteristics=...\n");
        this.characteristics.internalToString(repr, prefix + "  ");
    }
    
    private final class CharacterSerializedCharacteristicsConverter
    {
        private final RawCharacteristics characteristics;
        
        private CharacterSerializedCharacteristicsConverter() {
            super();
            this.characteristics = new RawCharacteristics();
        }
        
        public void pushResult() {
            CharacterSerializedCharacteristics.this.characteristics.characteristics.clear();
            CharacterSerializedCharacteristics.this.characteristics.characteristics.ensureCapacity(this.characteristics.characteristics.size());
            CharacterSerializedCharacteristics.this.characteristics.characteristics.addAll(this.characteristics.characteristics);
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean characteristics_ok = this.characteristics.unserializeVersion(buffer, 1);
            return characteristics_ok;
        }
        
        public void convert_v1_to_v10036003() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10036003) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10036003();
                return true;
            }
            return false;
        }
    }
}
