package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawDimensionalBagEcosystem implements VersionableObject
{
    public long lastUpdateTime;
    public final ArrayList<Resource> resources;
    
    public RawDimensionalBagEcosystem() {
        super();
        this.lastUpdateTime = 0L;
        this.resources = new ArrayList<Resource>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.lastUpdateTime);
        if (this.resources.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.resources.size());
        for (int i = 0; i < this.resources.size(); ++i) {
            final Resource resources_element = this.resources.get(i);
            final boolean resources_element_ok = resources_element.serialize(buffer);
            if (!resources_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.lastUpdateTime = buffer.getLong();
        final int resources_size = buffer.getShort() & 0xFFFF;
        this.resources.clear();
        this.resources.ensureCapacity(resources_size);
        for (int i = 0; i < resources_size; ++i) {
            final Resource resources_element = new Resource();
            final boolean resources_element_ok = resources_element.unserialize(buffer);
            if (!resources_element_ok) {
                return false;
            }
            this.resources.add(resources_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.lastUpdateTime = 0L;
        this.resources.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 8;
        size += 2;
        for (int i = 0; i < this.resources.size(); ++i) {
            final Resource resources_element = this.resources.get(i);
            size += resources_element.serializedSize();
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
        repr.append(prefix).append("lastUpdateTime=").append(this.lastUpdateTime).append('\n');
        repr.append(prefix).append("resources=");
        if (this.resources.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.resources.size()).append(" elements)...\n");
            for (int i = 0; i < this.resources.size(); ++i) {
                final Resource resources_element = this.resources.get(i);
                resources_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Resource implements VersionableObject
    {
        public final RawDimensionalBagResource resource;
        
        public Resource() {
            super();
            this.resource = new RawDimensionalBagResource();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean resource_ok = this.resource.serialize(buffer);
            return resource_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean resource_ok = this.resource.unserialize(buffer);
            return resource_ok;
        }
        
        @Override
        public void clear() {
            this.resource.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.resource.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("resource=...\n");
            this.resource.internalToString(repr, prefix + "  ");
        }
    }
}
