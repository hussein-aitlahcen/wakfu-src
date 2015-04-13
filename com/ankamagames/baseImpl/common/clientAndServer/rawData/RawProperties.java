package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.util.*;
import java.nio.*;

public class RawProperties implements VersionableObject
{
    public final ArrayList<Property> properties;
    
    public RawProperties() {
        super();
        this.properties = new ArrayList<Property>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.properties.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.properties.size());
        for (int i = 0; i < this.properties.size(); ++i) {
            final Property properties_element = this.properties.get(i);
            final boolean properties_element_ok = properties_element.serialize(buffer);
            if (!properties_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int properties_size = buffer.getShort() & 0xFFFF;
        this.properties.clear();
        this.properties.ensureCapacity(properties_size);
        for (int i = 0; i < properties_size; ++i) {
            final Property properties_element = new Property();
            final boolean properties_element_ok = properties_element.unserialize(buffer);
            if (!properties_element_ok) {
                return false;
            }
            this.properties.add(properties_element);
        }
        return true;
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
        size += 2;
        for (int i = 0; i < this.properties.size(); ++i) {
            final Property properties_element = this.properties.get(i);
            size += properties_element.serializedSize();
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
        if (this.properties.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.properties.size()).append(" elements)...\n");
            for (int i = 0; i < this.properties.size(); ++i) {
                final Property properties_element = this.properties.get(i);
                properties_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Property implements VersionableObject
    {
        public byte id;
        public byte count;
        public static final int SERIALIZED_SIZE = 2;
        
        public Property() {
            super();
            this.id = 0;
            this.count = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.id);
            buffer.put(this.count);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.id = buffer.get();
            this.count = buffer.get();
            return true;
        }
        
        @Override
        public void clear() {
            this.id = 0;
            this.count = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 2;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("id=").append(this.id).append('\n');
            repr.append(prefix).append("count=").append(this.count).append('\n');
        }
    }
}
