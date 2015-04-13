package com.ankamagames.baseImpl.common.clientAndServer.rawData;

import java.util.*;
import java.nio.*;

public class RawGems implements VersionableObject
{
    public final ArrayList<Content> gems;
    
    public RawGems() {
        super();
        this.gems = new ArrayList<Content>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.gems.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.gems.size());
        for (int i = 0; i < this.gems.size(); ++i) {
            final Content gems_element = this.gems.get(i);
            final boolean gems_element_ok = gems_element.serialize(buffer);
            if (!gems_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int gems_size = buffer.getShort() & 0xFFFF;
        this.gems.clear();
        this.gems.ensureCapacity(gems_size);
        for (int i = 0; i < gems_size; ++i) {
            final Content gems_element = new Content();
            final boolean gems_element_ok = gems_element.unserialize(buffer);
            if (!gems_element_ok) {
                return false;
            }
            this.gems.add(gems_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.gems.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.gems.size(); ++i) {
            final Content gems_element = this.gems.get(i);
            size += gems_element.serializedSize();
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
        repr.append(prefix).append("gems=");
        if (this.gems.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.gems.size()).append(" elements)...\n");
            for (int i = 0; i < this.gems.size(); ++i) {
                final Content gems_element = this.gems.get(i);
                gems_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Content implements VersionableObject
    {
        public byte position;
        public int referenceId;
        public static final int SERIALIZED_SIZE = 5;
        
        public Content() {
            super();
            this.position = 0;
            this.referenceId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.position);
            buffer.putInt(this.referenceId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.position = buffer.get();
            this.referenceId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.position = 0;
            this.referenceId = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 5;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("position=").append(this.position).append('\n');
            repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
        }
    }
}
