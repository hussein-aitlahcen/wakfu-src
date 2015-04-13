package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedPosition extends CharacterSerializedPart implements VersionableObject
{
    public int x;
    public int y;
    public short z;
    public short instanceId;
    public byte direction;
    public DimBagPosition dimBagPosition;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedPosition() {
        super();
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.instanceId = 0;
        this.direction = 0;
        this.dimBagPosition = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedPosition.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedPosition");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedPosition", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedPosition.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedPosition");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedPosition", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedPosition.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putInt(this.x);
        buffer.putInt(this.y);
        buffer.putShort(this.z);
        buffer.putShort(this.instanceId);
        buffer.put(this.direction);
        if (this.dimBagPosition != null) {
            buffer.put((byte)1);
            final boolean dimBagPosition_ok = this.dimBagPosition.serialize(buffer);
            if (!dimBagPosition_ok) {
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
        this.x = buffer.getInt();
        this.y = buffer.getInt();
        this.z = buffer.getShort();
        this.instanceId = buffer.getShort();
        this.direction = buffer.get();
        final boolean dimBagPosition_present = buffer.get() == 1;
        if (dimBagPosition_present) {
            this.dimBagPosition = new DimBagPosition();
            final boolean dimBagPosition_ok = this.dimBagPosition.unserialize(buffer);
            if (!dimBagPosition_ok) {
                return false;
            }
        }
        else {
            this.dimBagPosition = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.instanceId = 0;
        this.direction = 0;
        this.dimBagPosition = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedPositionConverter converter = new CharacterSerializedPositionConverter();
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
        size += 4;
        size += 4;
        size += 2;
        size += 2;
        ++size;
        ++size;
        if (this.dimBagPosition != null) {
            size += this.dimBagPosition.serializedSize();
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
        repr.append(prefix).append("x=").append(this.x).append('\n');
        repr.append(prefix).append("y=").append(this.y).append('\n');
        repr.append(prefix).append("z=").append(this.z).append('\n');
        repr.append(prefix).append("instanceId=").append(this.instanceId).append('\n');
        repr.append(prefix).append("direction=").append(this.direction).append('\n');
        repr.append(prefix).append("dimBagPosition=");
        if (this.dimBagPosition == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.dimBagPosition.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class DimBagPosition implements VersionableObject
    {
        public int x;
        public int y;
        public short z;
        public short instanceId;
        public static final int SERIALIZED_SIZE = 12;
        
        public DimBagPosition() {
            super();
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.instanceId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.x);
            buffer.putInt(this.y);
            buffer.putShort(this.z);
            buffer.putShort(this.instanceId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.x = buffer.getInt();
            this.y = buffer.getInt();
            this.z = buffer.getShort();
            this.instanceId = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.instanceId = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 12;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("x=").append(this.x).append('\n');
            repr.append(prefix).append("y=").append(this.y).append('\n');
            repr.append(prefix).append("z=").append(this.z).append('\n');
            repr.append(prefix).append("instanceId=").append(this.instanceId).append('\n');
        }
    }
    
    private final class CharacterSerializedPositionConverter
    {
        private int x;
        private int y;
        private short z;
        private short instanceId;
        private byte direction;
        private DimBagPosition dimBagPosition;
        
        private CharacterSerializedPositionConverter() {
            super();
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.instanceId = 0;
            this.direction = 0;
            this.dimBagPosition = null;
        }
        
        public void pushResult() {
            CharacterSerializedPosition.this.x = this.x;
            CharacterSerializedPosition.this.y = this.y;
            CharacterSerializedPosition.this.z = this.z;
            CharacterSerializedPosition.this.instanceId = this.instanceId;
            CharacterSerializedPosition.this.direction = this.direction;
            CharacterSerializedPosition.this.dimBagPosition = this.dimBagPosition;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            final boolean dimBagPosition_present = buffer.get() == 1;
            if (dimBagPosition_present) {
                this.dimBagPosition = new DimBagPosition();
                final boolean dimBagPosition_ok = this.dimBagPosition.unserializeVersion(buffer, 0);
                if (!dimBagPosition_ok) {
                    return false;
                }
            }
            else {
                this.dimBagPosition = null;
            }
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
