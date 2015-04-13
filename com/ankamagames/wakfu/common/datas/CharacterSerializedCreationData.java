package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedCreationData extends CharacterSerializedPart implements VersionableObject
{
    public CreationData creationData;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedCreationData() {
        super();
        this.creationData = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedCreationData.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedCreationData");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedCreationData", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedCreationData.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedCreationData");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedCreationData", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedCreationData.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.creationData != null) {
            buffer.put((byte)1);
            final boolean creationData_ok = this.creationData.serialize(buffer);
            if (!creationData_ok) {
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
        final boolean creationData_present = buffer.get() == 1;
        if (creationData_present) {
            this.creationData = new CreationData();
            final boolean creationData_ok = this.creationData.unserialize(buffer);
            if (!creationData_ok) {
                return false;
            }
        }
        else {
            this.creationData = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.creationData = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10031000) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedCreationDataConverter converter = new CharacterSerializedCreationDataConverter();
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
        if (this.creationData != null) {
            size += this.creationData.serializedSize();
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
        repr.append(prefix).append("creationData=");
        if (this.creationData == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.creationData.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class CreationData implements VersionableObject
    {
        public boolean newCharacter;
        public boolean needsRecustom;
        public short recustomValue;
        public static final int SERIALIZED_SIZE = 4;
        
        public CreationData() {
            super();
            this.newCharacter = false;
            this.needsRecustom = false;
            this.recustomValue = 127;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put((byte)(this.newCharacter ? 1 : 0));
            buffer.put((byte)(this.needsRecustom ? 1 : 0));
            buffer.putShort(this.recustomValue);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.newCharacter = (buffer.get() == 1);
            this.needsRecustom = (buffer.get() == 1);
            this.recustomValue = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.newCharacter = false;
            this.needsRecustom = false;
            this.recustomValue = 127;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10031000) {
                return this.unserialize(buffer);
            }
            final CreationDataConverter converter = new CreationDataConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            return 4;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("newCharacter=").append(this.newCharacter).append('\n');
            repr.append(prefix).append("needsRecustom=").append(this.needsRecustom).append('\n');
            repr.append(prefix).append("recustomValue=").append(this.recustomValue).append('\n');
        }
        
        private final class CreationDataConverter
        {
            private boolean newCharacter;
            private boolean needsRecustom;
            private short recustomValue;
            
            private CreationDataConverter() {
                super();
                this.newCharacter = false;
                this.needsRecustom = false;
                this.recustomValue = 127;
            }
            
            public void pushResult() {
                CreationData.this.newCharacter = this.newCharacter;
                CreationData.this.needsRecustom = this.needsRecustom;
                CreationData.this.recustomValue = this.recustomValue;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                this.newCharacter = (buffer.get() == 1);
                return true;
            }
            
            private boolean unserialize_v10027003(final ByteBuffer buffer) {
                this.newCharacter = (buffer.get() == 1);
                this.needsRecustom = (buffer.get() == 1);
                return true;
            }
            
            public void convert_v1_to_v10027003() {
            }
            
            public void convert_v10027003_to_v10031000() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version < 10027003) {
                    final boolean ok = this.unserialize_v1(buffer);
                    if (ok) {
                        this.convert_v1_to_v10027003();
                        this.convert_v10027003_to_v10031000();
                        return true;
                    }
                    return false;
                }
                else {
                    if (version >= 10031000) {
                        return false;
                    }
                    final boolean ok = this.unserialize_v10027003(buffer);
                    if (ok) {
                        this.convert_v10027003_to_v10031000();
                        return true;
                    }
                    return false;
                }
            }
        }
    }
    
    private final class CharacterSerializedCreationDataConverter
    {
        private CreationData creationData;
        
        private CharacterSerializedCreationDataConverter() {
            super();
            this.creationData = null;
        }
        
        public void pushResult() {
            CharacterSerializedCreationData.this.creationData = this.creationData;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean creationData_present = buffer.get() == 1;
            if (creationData_present) {
                this.creationData = new CreationData();
                final boolean creationData_ok = this.creationData.unserializeVersion(buffer, 1);
                if (!creationData_ok) {
                    return false;
                }
            }
            else {
                this.creationData = null;
            }
            return true;
        }
        
        private boolean unserialize_v10027003(final ByteBuffer buffer) {
            final boolean creationData_present = buffer.get() == 1;
            if (creationData_present) {
                this.creationData = new CreationData();
                final boolean creationData_ok = this.creationData.unserializeVersion(buffer, 10027003);
                if (!creationData_ok) {
                    return false;
                }
            }
            else {
                this.creationData = null;
            }
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v10027003() {
        }
        
        public void convert_v10027003_to_v10031000() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 1) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    this.convert_v1_to_v10027003();
                    this.convert_v10027003_to_v10031000();
                    return true;
                }
                return false;
            }
            else if (version < 10027003) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10027003();
                    this.convert_v10027003_to_v10031000();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10031000) {
                    return false;
                }
                final boolean ok = this.unserialize_v10027003(buffer);
                if (ok) {
                    this.convert_v10027003_to_v10031000();
                    return true;
                }
                return false;
            }
        }
    }
}
