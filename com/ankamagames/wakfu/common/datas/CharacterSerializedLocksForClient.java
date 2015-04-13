package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedLocksForClient extends CharacterSerializedPart implements VersionableObject
{
    public final ArrayList<Locks> content;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedLocksForClient() {
        super();
        this.content = new ArrayList<Locks>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedLocksForClient.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedLocksForClient");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedLocksForClient", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedLocksForClient.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedLocksForClient");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedLocksForClient", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedLocksForClient.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.content.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.content.size());
        for (int i = 0; i < this.content.size(); ++i) {
            final Locks content_element = this.content.get(i);
            final boolean content_element_ok = content_element.serialize(buffer);
            if (!content_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int content_size = buffer.getShort() & 0xFFFF;
        this.content.clear();
        this.content.ensureCapacity(content_size);
        for (int i = 0; i < content_size; ++i) {
            final Locks content_element = new Locks();
            final boolean content_element_ok = content_element.unserialize(buffer);
            if (!content_element_ok) {
                return false;
            }
            this.content.add(content_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.content.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10028001) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedLocksForClientConverter converter = new CharacterSerializedLocksForClientConverter();
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
        size += 2;
        for (int i = 0; i < this.content.size(); ++i) {
            final Locks content_element = this.content.get(i);
            size += content_element.serializedSize();
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
        repr.append(prefix).append("content=");
        if (this.content.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.content.size()).append(" elements)...\n");
            for (int i = 0; i < this.content.size(); ++i) {
                final Locks content_element = this.content.get(i);
                content_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Locks implements VersionableObject
    {
        public int lockId;
        public long lockDate;
        public long unlockDate;
        public int currentLockValue;
        public long currentLockValueLastModification;
        public static final int SERIALIZED_SIZE = 32;
        
        public Locks() {
            super();
            this.lockId = 0;
            this.lockDate = 0L;
            this.unlockDate = 0L;
            this.currentLockValue = 0;
            this.currentLockValueLastModification = 0L;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.lockId);
            buffer.putLong(this.lockDate);
            buffer.putLong(this.unlockDate);
            buffer.putInt(this.currentLockValue);
            buffer.putLong(this.currentLockValueLastModification);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.lockId = buffer.getInt();
            this.lockDate = buffer.getLong();
            this.unlockDate = buffer.getLong();
            this.currentLockValue = buffer.getInt();
            this.currentLockValueLastModification = buffer.getLong();
            return true;
        }
        
        @Override
        public void clear() {
            this.lockId = 0;
            this.lockDate = 0L;
            this.unlockDate = 0L;
            this.currentLockValue = 0;
            this.currentLockValueLastModification = 0L;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10028001) {
                return this.unserialize(buffer);
            }
            final LocksConverter converter = new LocksConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            return 32;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("lockId=").append(this.lockId).append('\n');
            repr.append(prefix).append("lockDate=").append(this.lockDate).append('\n');
            repr.append(prefix).append("unlockDate=").append(this.unlockDate).append('\n');
            repr.append(prefix).append("currentLockValue=").append(this.currentLockValue).append('\n');
            repr.append(prefix).append("currentLockValueLastModification=").append(this.currentLockValueLastModification).append('\n');
        }
        
        private final class LocksConverter
        {
            private int lockId;
            private long lockDate;
            private long unlockDate;
            private int currentLockValue;
            private long currentLockValueLastModification;
            
            private LocksConverter() {
                super();
                this.lockId = 0;
                this.lockDate = 0L;
                this.unlockDate = 0L;
                this.currentLockValue = 0;
                this.currentLockValueLastModification = 0L;
            }
            
            public void pushResult() {
                Locks.this.lockId = this.lockId;
                Locks.this.lockDate = this.lockDate;
                Locks.this.unlockDate = this.unlockDate;
                Locks.this.currentLockValue = this.currentLockValue;
                Locks.this.currentLockValueLastModification = this.currentLockValueLastModification;
            }
            
            private boolean unserialize_v10021(final ByteBuffer buffer) {
                this.lockId = buffer.getInt();
                this.lockDate = buffer.getLong();
                this.unlockDate = buffer.getLong();
                return true;
            }
            
            public void convert_v10021_to_v10028001() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 10021) {
                    return false;
                }
                if (version >= 10028001) {
                    return false;
                }
                final boolean ok = this.unserialize_v10021(buffer);
                if (ok) {
                    this.convert_v10021_to_v10028001();
                    return true;
                }
                return false;
            }
        }
    }
    
    private final class CharacterSerializedLocksForClientConverter
    {
        private final ArrayList<Locks> content;
        
        private CharacterSerializedLocksForClientConverter() {
            super();
            this.content = new ArrayList<Locks>(0);
        }
        
        public void pushResult() {
            CharacterSerializedLocksForClient.this.content.clear();
            CharacterSerializedLocksForClient.this.content.ensureCapacity(this.content.size());
            CharacterSerializedLocksForClient.this.content.addAll(this.content);
        }
        
        private boolean unserialize_v10021(final ByteBuffer buffer) {
            final int content_size = buffer.getShort() & 0xFFFF;
            this.content.clear();
            this.content.ensureCapacity(content_size);
            for (int i = 0; i < content_size; ++i) {
                final Locks content_element = new Locks();
                final boolean content_element_ok = content_element.unserializeVersion(buffer, 10021);
                if (!content_element_ok) {
                    return false;
                }
                this.content.add(content_element);
            }
            return true;
        }
        
        public void convert_v10021_to_v10028001() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 10021) {
                return false;
            }
            if (version >= 10028001) {
                return false;
            }
            final boolean ok = this.unserialize_v10021(buffer);
            if (ok) {
                this.convert_v10021_to_v10028001();
                return true;
            }
            return false;
        }
    }
}
