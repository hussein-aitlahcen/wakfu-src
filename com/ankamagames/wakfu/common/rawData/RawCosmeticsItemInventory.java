package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawCosmeticsItemInventory implements VersionableObject
{
    public final ArrayList<RawCosmeticsItem> items;
    
    public RawCosmeticsItemInventory() {
        super();
        this.items = new ArrayList<RawCosmeticsItem>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.items.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.items.size());
        for (int i = 0; i < this.items.size(); ++i) {
            final RawCosmeticsItem items_element = this.items.get(i);
            final boolean items_element_ok = items_element.serialize(buffer);
            if (!items_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int items_size = buffer.getShort() & 0xFFFF;
        this.items.clear();
        this.items.ensureCapacity(items_size);
        for (int i = 0; i < items_size; ++i) {
            final RawCosmeticsItem items_element = new RawCosmeticsItem();
            final boolean items_element_ok = items_element.unserialize(buffer);
            if (!items_element_ok) {
                return false;
            }
            this.items.add(items_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.items.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.items.size(); ++i) {
            final RawCosmeticsItem items_element = this.items.get(i);
            size += items_element.serializedSize();
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
        repr.append(prefix).append("items=");
        if (this.items.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.items.size()).append(" elements)...\n");
            for (int i = 0; i < this.items.size(); ++i) {
                final RawCosmeticsItem items_element = this.items.get(i);
                items_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class RawCosmeticsItem implements VersionableObject
    {
        public int refId;
        public static final int SERIALIZED_SIZE = 4;
        
        public RawCosmeticsItem() {
            super();
            this.refId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.refId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.refId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.refId = 0;
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
            repr.append(prefix).append("refId=").append(this.refId).append('\n');
        }
    }
}
