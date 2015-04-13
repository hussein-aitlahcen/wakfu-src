package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawLocks implements VersionableObject
{
    public final ArrayList<Lock> locks;
    
    public RawLocks() {
        super();
        this.locks = new ArrayList<Lock>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.locks.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.locks.size());
        for (int i = 0; i < this.locks.size(); ++i) {
            final Lock locks_element = this.locks.get(i);
            final boolean locks_element_ok = locks_element.serialize(buffer);
            if (!locks_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int locks_size = buffer.getShort() & 0xFFFF;
        this.locks.clear();
        this.locks.ensureCapacity(locks_size);
        for (int i = 0; i < locks_size; ++i) {
            final Lock locks_element = new Lock();
            final boolean locks_element_ok = locks_element.unserialize(buffer);
            if (!locks_element_ok) {
                return false;
            }
            this.locks.add(locks_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.locks.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10028001) {
            return this.unserialize(buffer);
        }
        final RawLocksConverter converter = new RawLocksConverter();
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
        for (int i = 0; i < this.locks.size(); ++i) {
            final Lock locks_element = this.locks.get(i);
            size += locks_element.serializedSize();
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
        repr.append(prefix).append("locks=");
        if (this.locks.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.locks.size()).append(" elements)...\n");
            for (int i = 0; i < this.locks.size(); ++i) {
                final Lock locks_element = this.locks.get(i);
                locks_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Lock implements VersionableObject
    {
        public int lockId;
        public long lockDate;
        public int currentLockValue;
        public long currentLockValueLastChange;
        public static final int SERIALIZED_SIZE = 24;
        
        public Lock() {
            super();
            this.lockId = 0;
            this.lockDate = 0L;
            this.currentLockValue = 0;
            this.currentLockValueLastChange = 0L;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.lockId);
            buffer.putLong(this.lockDate);
            buffer.putInt(this.currentLockValue);
            buffer.putLong(this.currentLockValueLastChange);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.lockId = buffer.getInt();
            this.lockDate = buffer.getLong();
            this.currentLockValue = buffer.getInt();
            this.currentLockValueLastChange = buffer.getLong();
            return true;
        }
        
        @Override
        public void clear() {
            this.lockId = 0;
            this.lockDate = 0L;
            this.currentLockValue = 0;
            this.currentLockValueLastChange = 0L;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10028001) {
                return this.unserialize(buffer);
            }
            final LockConverter converter = new LockConverter();
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
            repr.append(prefix).append("lockId=").append(this.lockId).append('\n');
            repr.append(prefix).append("lockDate=").append(this.lockDate).append('\n');
            repr.append(prefix).append("currentLockValue=").append(this.currentLockValue).append('\n');
            repr.append(prefix).append("currentLockValueLastChange=").append(this.currentLockValueLastChange).append('\n');
        }
        
        private final class LockConverter
        {
            private int lockId;
            private long lockDate;
            private int currentLockValue;
            private long currentLockValueLastChange;
            
            private LockConverter() {
                super();
                this.lockId = 0;
                this.lockDate = 0L;
                this.currentLockValue = 0;
                this.currentLockValueLastChange = 0L;
            }
            
            public void pushResult() {
                Lock.this.lockId = this.lockId;
                Lock.this.lockDate = this.lockDate;
                Lock.this.currentLockValue = this.currentLockValue;
                Lock.this.currentLockValueLastChange = this.currentLockValueLastChange;
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                this.lockId = buffer.getInt();
                this.lockDate = buffer.getLong();
                return true;
            }
            
            public void convert_v0_to_v10028001() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 0) {
                    return false;
                }
                if (version >= 10028001) {
                    return false;
                }
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v10028001();
                    return true;
                }
                return false;
            }
        }
    }
    
    private final class RawLocksConverter
    {
        private final ArrayList<Lock> locks;
        
        private RawLocksConverter() {
            super();
            this.locks = new ArrayList<Lock>(0);
        }
        
        public void pushResult() {
            RawLocks.this.locks.clear();
            RawLocks.this.locks.ensureCapacity(this.locks.size());
            RawLocks.this.locks.addAll(this.locks);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            final int locks_size = buffer.getShort() & 0xFFFF;
            this.locks.clear();
            this.locks.ensureCapacity(locks_size);
            for (int i = 0; i < locks_size; ++i) {
                final Lock locks_element = new Lock();
                final boolean locks_element_ok = locks_element.unserializeVersion(buffer, 0);
                if (!locks_element_ok) {
                    return false;
                }
                this.locks.add(locks_element);
            }
            return true;
        }
        
        public void convert_v0_to_v10028001() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version >= 10028001) {
                return false;
            }
            final boolean ok = this.unserialize_v0(buffer);
            if (ok) {
                this.convert_v0_to_v10028001();
                return true;
            }
            return false;
        }
    }
}
