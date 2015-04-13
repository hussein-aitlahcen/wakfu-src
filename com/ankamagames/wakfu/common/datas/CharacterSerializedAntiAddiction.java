package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedAntiAddiction extends CharacterSerializedPart implements VersionableObject
{
    public AddictionData addictionData;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedAntiAddiction() {
        super();
        this.addictionData = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedAntiAddiction.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedAntiAddiction");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedAntiAddiction", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedAntiAddiction.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAntiAddiction");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedAntiAddiction", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedAntiAddiction.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.addictionData != null) {
            buffer.put((byte)1);
            final boolean addictionData_ok = this.addictionData.serialize(buffer);
            if (!addictionData_ok) {
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
        final boolean addictionData_present = buffer.get() == 1;
        if (addictionData_present) {
            this.addictionData = new AddictionData();
            final boolean addictionData_ok = this.addictionData.unserialize(buffer);
            if (!addictionData_ok) {
                return false;
            }
        }
        else {
            this.addictionData = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.addictionData = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10026000) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedAntiAddictionConverter converter = new CharacterSerializedAntiAddictionConverter();
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
        if (this.addictionData != null) {
            size += this.addictionData.serializedSize();
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
        repr.append(prefix).append("addictionData=");
        if (this.addictionData == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.addictionData.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class AddictionData implements VersionableObject
    {
        public long lastConnectionDate;
        public long currentUsedQuota;
        public static final int SERIALIZED_SIZE = 16;
        
        public AddictionData() {
            super();
            this.lastConnectionDate = 0L;
            this.currentUsedQuota = 0L;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putLong(this.lastConnectionDate);
            buffer.putLong(this.currentUsedQuota);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.lastConnectionDate = buffer.getLong();
            this.currentUsedQuota = buffer.getLong();
            return true;
        }
        
        @Override
        public void clear() {
            this.lastConnectionDate = 0L;
            this.currentUsedQuota = 0L;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 16;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("lastConnectionDate=").append(this.lastConnectionDate).append('\n');
            repr.append(prefix).append("currentUsedQuota=").append(this.currentUsedQuota).append('\n');
        }
    }
    
    private final class CharacterSerializedAntiAddictionConverter
    {
        private AddictionData addictionData;
        
        private CharacterSerializedAntiAddictionConverter() {
            super();
            this.addictionData = null;
        }
        
        public void pushResult() {
            CharacterSerializedAntiAddiction.this.addictionData = this.addictionData;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        public void convert_v0_to_v10026000() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version >= 10026000) {
                return false;
            }
            final boolean ok = this.unserialize_v0(buffer);
            if (ok) {
                this.convert_v0_to_v10026000();
                return true;
            }
            return false;
        }
    }
}
