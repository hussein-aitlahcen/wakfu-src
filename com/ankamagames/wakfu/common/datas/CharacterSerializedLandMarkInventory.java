package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedLandMarkInventory extends CharacterSerializedPart implements VersionableObject
{
    public final ArrayList<LandMark> landMarks;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedLandMarkInventory() {
        super();
        this.landMarks = new ArrayList<LandMark>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedLandMarkInventory.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedLandMarkInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedLandMarkInventory", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedLandMarkInventory.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedLandMarkInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedLandMarkInventory", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedLandMarkInventory.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.landMarks.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.landMarks.size());
        for (int i = 0; i < this.landMarks.size(); ++i) {
            final LandMark landMarks_element = this.landMarks.get(i);
            final boolean landMarks_element_ok = landMarks_element.serialize(buffer);
            if (!landMarks_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int landMarks_size = buffer.getShort() & 0xFFFF;
        this.landMarks.clear();
        this.landMarks.ensureCapacity(landMarks_size);
        for (int i = 0; i < landMarks_size; ++i) {
            final LandMark landMarks_element = new LandMark();
            final boolean landMarks_element_ok = landMarks_element.unserialize(buffer);
            if (!landMarks_element_ok) {
                return false;
            }
            this.landMarks.add(landMarks_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.landMarks.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.landMarks.size(); ++i) {
            final LandMark landMarks_element = this.landMarks.get(i);
            size += landMarks_element.serializedSize();
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
        repr.append(prefix).append("landMarks=");
        if (this.landMarks.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.landMarks.size()).append(" elements)...\n");
            for (int i = 0; i < this.landMarks.size(); ++i) {
                final LandMark landMarks_element = this.landMarks.get(i);
                landMarks_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class LandMark implements VersionableObject
    {
        public byte landMarkId;
        public static final int SERIALIZED_SIZE = 1;
        
        public LandMark() {
            super();
            this.landMarkId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.landMarkId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.landMarkId = buffer.get();
            return true;
        }
        
        @Override
        public void clear() {
            this.landMarkId = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 1;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("landMarkId=").append(this.landMarkId).append('\n');
        }
    }
}
