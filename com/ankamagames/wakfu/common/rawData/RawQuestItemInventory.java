package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawQuestItemInventory implements VersionableObject
{
    public final ArrayList<RawQuestItem> items;
    
    public RawQuestItemInventory() {
        super();
        this.items = new ArrayList<RawQuestItem>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.items.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.items.size());
        for (int i = 0; i < this.items.size(); ++i) {
            final RawQuestItem items_element = this.items.get(i);
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
            final RawQuestItem items_element = new RawQuestItem();
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
            final RawQuestItem items_element = this.items.get(i);
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
                final RawQuestItem items_element = this.items.get(i);
                items_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class RawQuestItem implements VersionableObject
    {
        public int refId;
        public short quantity;
        public static final int SERIALIZED_SIZE = 6;
        
        public RawQuestItem() {
            super();
            this.refId = 0;
            this.quantity = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.refId);
            buffer.putShort(this.quantity);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.refId = buffer.getInt();
            this.quantity = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.refId = 0;
            this.quantity = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            return 6;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("refId=").append(this.refId).append('\n');
            repr.append(prefix).append("quantity=").append(this.quantity).append('\n');
        }
    }
}
