package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedPet extends CharacterSerializedPart implements VersionableObject
{
    public Pet pet;
    public Mount mount;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedPet() {
        super();
        this.pet = null;
        this.mount = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedPet.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedPet");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedPet", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedPet.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedPet");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedPet", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedPet.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.pet != null) {
            buffer.put((byte)1);
            final boolean pet_ok = this.pet.serialize(buffer);
            if (!pet_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        if (this.mount != null) {
            buffer.put((byte)1);
            final boolean mount_ok = this.mount.serialize(buffer);
            if (!mount_ok) {
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
        final boolean pet_present = buffer.get() == 1;
        if (pet_present) {
            this.pet = new Pet();
            final boolean pet_ok = this.pet.unserialize(buffer);
            if (!pet_ok) {
                return false;
            }
        }
        else {
            this.pet = null;
        }
        final boolean mount_present = buffer.get() == 1;
        if (mount_present) {
            this.mount = new Mount();
            final boolean mount_ok = this.mount.unserialize(buffer);
            if (!mount_ok) {
                return false;
            }
        }
        else {
            this.mount = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.pet = null;
        this.mount = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10035005) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedPetConverter converter = new CharacterSerializedPetConverter();
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
        if (this.pet != null) {
            size += this.pet.serializedSize();
        }
        ++size;
        if (this.mount != null) {
            size += this.mount.serializedSize();
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
        repr.append(prefix).append("pet=");
        if (this.pet == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.pet.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("mount=");
        if (this.mount == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.mount.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class Pet implements VersionableObject
    {
        public int definitionId;
        public int colorRefItemId;
        public int equippedRefItemId;
        public int sleepRefItemId;
        public int health;
        public int reskinRefItemId;
        public static final int SERIALIZED_SIZE = 24;
        
        public Pet() {
            super();
            this.definitionId = 0;
            this.colorRefItemId = 0;
            this.equippedRefItemId = 0;
            this.sleepRefItemId = 0;
            this.health = 0;
            this.reskinRefItemId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.definitionId);
            buffer.putInt(this.colorRefItemId);
            buffer.putInt(this.equippedRefItemId);
            buffer.putInt(this.sleepRefItemId);
            buffer.putInt(this.health);
            buffer.putInt(this.reskinRefItemId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.definitionId = buffer.getInt();
            this.colorRefItemId = buffer.getInt();
            this.equippedRefItemId = buffer.getInt();
            this.sleepRefItemId = buffer.getInt();
            this.health = buffer.getInt();
            this.reskinRefItemId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.definitionId = 0;
            this.colorRefItemId = 0;
            this.equippedRefItemId = 0;
            this.sleepRefItemId = 0;
            this.health = 0;
            this.reskinRefItemId = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10035004) {
                return this.unserialize(buffer);
            }
            final PetConverter converter = new PetConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            return 24;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("definitionId=").append(this.definitionId).append('\n');
            repr.append(prefix).append("colorRefItemId=").append(this.colorRefItemId).append('\n');
            repr.append(prefix).append("equippedRefItemId=").append(this.equippedRefItemId).append('\n');
            repr.append(prefix).append("sleepRefItemId=").append(this.sleepRefItemId).append('\n');
            repr.append(prefix).append("health=").append(this.health).append('\n');
            repr.append(prefix).append("reskinRefItemId=").append(this.reskinRefItemId).append('\n');
        }
        
        private final class PetConverter
        {
            private int definitionId;
            private int colorRefItemId;
            private int equippedRefItemId;
            private int sleepRefItemId;
            private int health;
            private int reskinRefItemId;
            
            private PetConverter() {
                super();
                this.definitionId = 0;
                this.colorRefItemId = 0;
                this.equippedRefItemId = 0;
                this.sleepRefItemId = 0;
                this.health = 0;
                this.reskinRefItemId = 0;
            }
            
            public void pushResult() {
                Pet.this.definitionId = this.definitionId;
                Pet.this.colorRefItemId = this.colorRefItemId;
                Pet.this.equippedRefItemId = this.equippedRefItemId;
                Pet.this.sleepRefItemId = this.sleepRefItemId;
                Pet.this.health = this.health;
                Pet.this.reskinRefItemId = this.reskinRefItemId;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                this.definitionId = buffer.getInt();
                this.colorRefItemId = buffer.getInt();
                this.equippedRefItemId = buffer.getInt();
                this.sleepRefItemId = buffer.getInt();
                this.health = buffer.getInt();
                return true;
            }
            
            public void convert_v1_to_v10035004() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10035004) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10035004();
                    return true;
                }
                return false;
            }
        }
    }
    
    public static class Mount implements VersionableObject
    {
        public int definitionId;
        public int colorRefItemId;
        public int equippedRefItemId;
        public int sleepRefItemId;
        public int health;
        public int reskinRefItemId;
        public static final int SERIALIZED_SIZE = 24;
        
        public Mount() {
            super();
            this.definitionId = 0;
            this.colorRefItemId = 0;
            this.equippedRefItemId = 0;
            this.sleepRefItemId = 0;
            this.health = 0;
            this.reskinRefItemId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.definitionId);
            buffer.putInt(this.colorRefItemId);
            buffer.putInt(this.equippedRefItemId);
            buffer.putInt(this.sleepRefItemId);
            buffer.putInt(this.health);
            buffer.putInt(this.reskinRefItemId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.definitionId = buffer.getInt();
            this.colorRefItemId = buffer.getInt();
            this.equippedRefItemId = buffer.getInt();
            this.sleepRefItemId = buffer.getInt();
            this.health = buffer.getInt();
            this.reskinRefItemId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.definitionId = 0;
            this.colorRefItemId = 0;
            this.equippedRefItemId = 0;
            this.sleepRefItemId = 0;
            this.health = 0;
            this.reskinRefItemId = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 24;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("definitionId=").append(this.definitionId).append('\n');
            repr.append(prefix).append("colorRefItemId=").append(this.colorRefItemId).append('\n');
            repr.append(prefix).append("equippedRefItemId=").append(this.equippedRefItemId).append('\n');
            repr.append(prefix).append("sleepRefItemId=").append(this.sleepRefItemId).append('\n');
            repr.append(prefix).append("health=").append(this.health).append('\n');
            repr.append(prefix).append("reskinRefItemId=").append(this.reskinRefItemId).append('\n');
        }
    }
    
    private final class CharacterSerializedPetConverter
    {
        private Pet pet;
        private Mount mount;
        
        private CharacterSerializedPetConverter() {
            super();
            this.pet = null;
            this.mount = null;
        }
        
        public void pushResult() {
            CharacterSerializedPet.this.pet = this.pet;
            CharacterSerializedPet.this.mount = this.mount;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 1);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final boolean pet_present = buffer.get() == 1;
            if (pet_present) {
                this.pet = new Pet();
                final boolean pet_ok = this.pet.unserializeVersion(buffer, 10035004);
                if (!pet_ok) {
                    return false;
                }
            }
            else {
                this.pet = null;
            }
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v10035004() {
        }
        
        public void convert_v10035004_to_v10035005() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 1) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    this.convert_v1_to_v10035004();
                    this.convert_v10035004_to_v10035005();
                    return true;
                }
                return false;
            }
            else if (version < 10035004) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10035004();
                    this.convert_v10035004_to_v10035005();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10035005) {
                    return false;
                }
                final boolean ok = this.unserialize_v10035004(buffer);
                if (ok) {
                    this.convert_v10035004_to_v10035005();
                    return true;
                }
                return false;
            }
        }
    }
}
