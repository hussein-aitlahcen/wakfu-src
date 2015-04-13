package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedAptitudeInventory extends CharacterSerializedPart implements VersionableObject
{
    public final RawAptitudeInventory aptitudeInventory;
    public short version;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedAptitudeInventory() {
        super();
        this.aptitudeInventory = new RawAptitudeInventory();
        this.version = 0;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedAptitudeInventory.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedAptitudeInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedAptitudeInventory", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedAptitudeInventory.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAptitudeInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAptitudeInventory", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedAptitudeInventory.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        final boolean aptitudeInventory_ok = this.aptitudeInventory.serialize(buffer);
        if (!aptitudeInventory_ok) {
            return false;
        }
        buffer.putShort(this.version);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final boolean aptitudeInventory_ok = this.aptitudeInventory.unserialize(buffer);
        if (!aptitudeInventory_ok) {
            return false;
        }
        this.version = buffer.getShort();
        return true;
    }
    
    @Override
    public void clear() {
        this.aptitudeInventory.clear();
        this.version = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10004) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedAptitudeInventoryConverter converter = new CharacterSerializedAptitudeInventoryConverter();
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
        size += this.aptitudeInventory.serializedSize();
        size += 2;
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
        repr.append(prefix).append("version=").append(this.version).append('\n');
    }
    
    private final class CharacterSerializedAptitudeInventoryConverter
    {
        private final RawAptitudeInventory aptitudeInventory;
        private short version;
        
        private CharacterSerializedAptitudeInventoryConverter() {
            super();
            this.aptitudeInventory = new RawAptitudeInventory();
            this.version = 0;
        }
        
        public void pushResult() {
            CharacterSerializedAptitudeInventory.this.aptitudeInventory.contents.clear();
            CharacterSerializedAptitudeInventory.this.aptitudeInventory.contents.ensureCapacity(this.aptitudeInventory.contents.size());
            CharacterSerializedAptitudeInventory.this.aptitudeInventory.contents.addAll(this.aptitudeInventory.contents);
            CharacterSerializedAptitudeInventory.this.aptitudeInventory.availablePointsArray.clear();
            CharacterSerializedAptitudeInventory.this.aptitudeInventory.availablePointsArray.ensureCapacity(this.aptitudeInventory.availablePointsArray.size());
            CharacterSerializedAptitudeInventory.this.aptitudeInventory.availablePointsArray.addAll(this.aptitudeInventory.availablePointsArray);
            CharacterSerializedAptitudeInventory.this.version = this.version;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean aptitudeInventory_ok = this.aptitudeInventory.unserializeVersion(buffer, 1);
            if (!aptitudeInventory_ok) {
                return false;
            }
            this.version = buffer.getShort();
            return true;
        }
        
        public void convert_v1_to_v10004() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10004) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10004();
                return true;
            }
            return false;
        }
    }
}
