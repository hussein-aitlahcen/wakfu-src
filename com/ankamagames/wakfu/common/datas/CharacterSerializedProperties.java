package com.ankamagames.wakfu.common.datas;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public class CharacterSerializedProperties extends CharacterSerializedPart implements VersionableObject
{
    public Properties properties;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedProperties() {
        super();
        this.properties = null;
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedProperties.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedProperties");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedProperties", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedProperties.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedProperties");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedProperties", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedProperties.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.properties != null) {
            buffer.put((byte)1);
            final boolean properties_ok = this.properties.serialize(buffer);
            if (!properties_ok) {
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
        final boolean properties_present = buffer.get() == 1;
        if (properties_present) {
            this.properties = new Properties();
            final boolean properties_ok = this.properties.unserialize(buffer);
            if (!properties_ok) {
                return false;
            }
        }
        else {
            this.properties = null;
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.properties = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedPropertiesConverter converter = new CharacterSerializedPropertiesConverter();
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
        if (this.properties != null) {
            size += this.properties.serializedSize();
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
        repr.append(prefix).append("properties=");
        if (this.properties == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.properties.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class Properties implements VersionableObject
    {
        public final RawProperties properties;
        
        public Properties() {
            super();
            this.properties = new RawProperties();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean properties_ok = this.properties.serialize(buffer);
            return properties_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean properties_ok = this.properties.unserialize(buffer);
            return properties_ok;
        }
        
        @Override
        public void clear() {
            this.properties.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.properties.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("properties=...\n");
            this.properties.internalToString(repr, prefix + "  ");
        }
    }
    
    private final class CharacterSerializedPropertiesConverter
    {
        private Properties properties;
        
        private CharacterSerializedPropertiesConverter() {
            super();
            this.properties = null;
        }
        
        public void pushResult() {
            CharacterSerializedProperties.this.properties = this.properties;
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
