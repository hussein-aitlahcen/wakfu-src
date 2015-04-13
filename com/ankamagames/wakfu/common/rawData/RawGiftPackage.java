package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class RawGiftPackage implements VersionableObject
{
    public String title;
    public String message;
    public final ArrayList<Content> contents;
    
    public RawGiftPackage() {
        super();
        this.title = null;
        this.message = null;
        this.contents = new ArrayList<Content>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.title != null) {
            final byte[] serialized_title = StringUtils.toUTF8(this.title);
            if (serialized_title.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_title.length);
            buffer.put(serialized_title);
        }
        else {
            buffer.putShort((short)0);
        }
        if (this.message != null) {
            final byte[] serialized_message = StringUtils.toUTF8(this.message);
            if (serialized_message.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_message.length);
            buffer.put(serialized_message);
        }
        else {
            buffer.putShort((short)0);
        }
        if (this.contents.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.contents.size());
        for (int i = 0; i < this.contents.size(); ++i) {
            final Content contents_element = this.contents.get(i);
            final boolean contents_element_ok = contents_element.serialize(buffer);
            if (!contents_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int title_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_title = new byte[title_size];
        buffer.get(serialized_title);
        this.title = StringUtils.fromUTF8(serialized_title);
        final int message_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_message = new byte[message_size];
        buffer.get(serialized_message);
        this.message = StringUtils.fromUTF8(serialized_message);
        final int contents_size = buffer.getShort() & 0xFFFF;
        this.contents.clear();
        this.contents.ensureCapacity(contents_size);
        for (int i = 0; i < contents_size; ++i) {
            final Content contents_element = new Content();
            final boolean contents_element_ok = contents_element.unserialize(buffer);
            if (!contents_element_ok) {
                return false;
            }
            this.contents.add(contents_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.title = null;
        this.message = null;
        this.contents.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        size += ((this.title != null) ? StringUtils.toUTF8(this.title).length : 0);
        size += 2;
        size += ((this.message != null) ? StringUtils.toUTF8(this.message).length : 0);
        size += 2;
        for (int i = 0; i < this.contents.size(); ++i) {
            final Content contents_element = this.contents.get(i);
            size += contents_element.serializedSize();
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
        repr.append(prefix).append("title=").append(this.title).append('\n');
        repr.append(prefix).append("message=").append(this.message).append('\n');
        repr.append(prefix).append("contents=");
        if (this.contents.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.contents.size()).append(" elements)...\n");
            for (int i = 0; i < this.contents.size(); ++i) {
                final Content contents_element = this.contents.get(i);
                contents_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Content implements VersionableObject
    {
        public final RawGiftItem giftItem;
        
        public Content() {
            super();
            this.giftItem = new RawGiftItem();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean giftItem_ok = this.giftItem.serialize(buffer);
            return giftItem_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean giftItem_ok = this.giftItem.unserialize(buffer);
            return giftItem_ok;
        }
        
        @Override
        public void clear() {
            this.giftItem.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.giftItem.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("giftItem=...\n");
            this.giftItem.internalToString(repr, prefix + "  ");
        }
    }
}
