package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import java.nio.*;

public class RawSpellLevelInventory implements VersionableObject
{
    public final ArrayList<Content> contents;
    
    public RawSpellLevelInventory() {
        super();
        this.contents = new ArrayList<Content>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
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
        public final RawSpellLevel spellLevel;
        
        public Content() {
            super();
            this.spellLevel = new RawSpellLevel();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean spellLevel_ok = this.spellLevel.serialize(buffer);
            return spellLevel_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean spellLevel_ok = this.spellLevel.unserialize(buffer);
            return spellLevel_ok;
        }
        
        @Override
        public void clear() {
            this.spellLevel.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.spellLevel.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("spellLevel=...\n");
            this.spellLevel.internalToString(repr, prefix + "  ");
        }
    }
}
