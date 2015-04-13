package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawBookcase implements VersionableObject
{
    public final ArrayList<Content> bookRefIds;
    
    public RawBookcase() {
        super();
        this.bookRefIds = new ArrayList<Content>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.bookRefIds.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.bookRefIds.size());
        for (int i = 0; i < this.bookRefIds.size(); ++i) {
            final Content bookRefIds_element = this.bookRefIds.get(i);
            final boolean bookRefIds_element_ok = bookRefIds_element.serialize(buffer);
            if (!bookRefIds_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int bookRefIds_size = buffer.getShort() & 0xFFFF;
        this.bookRefIds.clear();
        this.bookRefIds.ensureCapacity(bookRefIds_size);
        for (int i = 0; i < bookRefIds_size; ++i) {
            final Content bookRefIds_element = new Content();
            final boolean bookRefIds_element_ok = bookRefIds_element.unserialize(buffer);
            if (!bookRefIds_element_ok) {
                return false;
            }
            this.bookRefIds.add(bookRefIds_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.bookRefIds.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.bookRefIds.size(); ++i) {
            final Content bookRefIds_element = this.bookRefIds.get(i);
            size += bookRefIds_element.serializedSize();
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
        repr.append(prefix).append("bookRefIds=");
        if (this.bookRefIds.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.bookRefIds.size()).append(" elements)...\n");
            for (int i = 0; i < this.bookRefIds.size(); ++i) {
                final Content bookRefIds_element = this.bookRefIds.get(i);
                bookRefIds_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Content implements VersionableObject
    {
        public int bookRefId;
        public static final int SERIALIZED_SIZE = 4;
        
        public Content() {
            super();
            this.bookRefId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.bookRefId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.bookRefId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.bookRefId = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
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
            repr.append(prefix).append("bookRefId=").append(this.bookRefId).append('\n');
        }
    }
}
