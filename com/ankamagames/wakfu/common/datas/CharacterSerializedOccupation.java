package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedOccupation extends CharacterSerializedPart implements VersionableObject
{
    public Occupation occupation;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedOccupation() {
        super();
        this.occupation = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedOccupation.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedOccupation");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedOccupation", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedOccupation.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedOccupation");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedOccupation", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedOccupation.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.occupation != null) {
            buffer.put((byte)1);
            final boolean occupation_ok = this.occupation.serialize(buffer);
            if (!occupation_ok) {
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
        final boolean occupation_present = buffer.get() == 1;
        if (occupation_present) {
            this.occupation = new Occupation();
            final boolean occupation_ok = this.occupation.unserialize(buffer);
            if (!occupation_ok) {
                return false;
            }
        }
        else {
            this.occupation = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.occupation = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedOccupationConverter converter = new CharacterSerializedOccupationConverter();
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
        if (this.occupation != null) {
            size += this.occupation.serializedSize();
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
        repr.append(prefix).append("occupation=");
        if (this.occupation == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.occupation.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class Occupation implements VersionableObject
    {
        public short occupationId;
        public byte[] occupationData;
        
        public Occupation() {
            super();
            this.occupationId = 0;
            this.occupationData = null;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putShort(this.occupationId);
            if (this.occupationData != null) {
                if (this.occupationData.length > 65535) {
                    return false;
                }
                buffer.putShort((short)this.occupationData.length);
                buffer.put(this.occupationData);
            }
            else {
                buffer.putShort((short)0);
            }
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.occupationId = buffer.getShort();
            final int occupationData_size = buffer.getShort() & 0xFFFF;
            if (occupationData_size > 0) {
                buffer.get(this.occupationData = new byte[occupationData_size]);
            }
            else {
                this.occupationData = null;
            }
            return true;
        }
        
        @Override
        public void clear() {
            this.occupationId = 0;
            this.occupationData = null;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += 2;
            size += 2;
            size += ((this.occupationData != null) ? this.occupationData.length : 0);
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("occupationId=").append(this.occupationId).append('\n');
            repr.append(prefix).append("occupationData=(").append((this.occupationData != null) ? this.occupationData.length : 0).append(" bytes)\n");
        }
    }
    
    private final class CharacterSerializedOccupationConverter
    {
        private Occupation occupation;
        
        private CharacterSerializedOccupationConverter() {
            super();
            this.occupation = null;
        }
        
        public void pushResult() {
            CharacterSerializedOccupation.this.occupation = this.occupation;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version >= 1) {
                return false;
            }
            final boolean ok = this.unserialize_v0(buffer);
            if (ok) {
                this.convert_v0_to_v1();
                return true;
            }
            return false;
        }
    }
}
