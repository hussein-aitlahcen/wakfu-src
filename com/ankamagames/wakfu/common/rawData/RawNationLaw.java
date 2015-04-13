package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawNationLaw implements VersionableObject
{
    public boolean firstChange;
    public final ArrayList<Law> laws;
    
    public RawNationLaw() {
        super();
        this.firstChange = false;
        this.laws = new ArrayList<Law>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put((byte)(this.firstChange ? 1 : 0));
        if (this.laws.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.laws.size());
        for (int i = 0; i < this.laws.size(); ++i) {
            final Law laws_element = this.laws.get(i);
            final boolean laws_element_ok = laws_element.serialize(buffer);
            if (!laws_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.firstChange = (buffer.get() == 1);
        final int laws_size = buffer.getShort() & 0xFFFF;
        this.laws.clear();
        this.laws.ensureCapacity(laws_size);
        for (int i = 0; i < laws_size; ++i) {
            final Law laws_element = new Law();
            final boolean laws_element_ok = laws_element.unserialize(buffer);
            if (!laws_element_ok) {
                return false;
            }
            this.laws.add(laws_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.firstChange = false;
        this.laws.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        ++size;
        size += 2;
        for (int i = 0; i < this.laws.size(); ++i) {
            final Law laws_element = this.laws.get(i);
            size += laws_element.serializedSize();
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
        repr.append(prefix).append("firstChange=").append(this.firstChange).append('\n');
        repr.append(prefix).append("laws=");
        if (this.laws.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.laws.size()).append(" elements)...\n");
            for (int i = 0; i < this.laws.size(); ++i) {
                final Law laws_element = this.laws.get(i);
                laws_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Law implements VersionableObject
    {
        public long lawId;
        public static final int SERIALIZED_SIZE = 8;
        
        public Law() {
            super();
            this.lawId = 0L;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putLong(this.lawId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.lawId = buffer.getLong();
            return true;
        }
        
        @Override
        public void clear() {
            this.lawId = 0L;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 8;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("lawId=").append(this.lawId).append('\n');
        }
    }
}
