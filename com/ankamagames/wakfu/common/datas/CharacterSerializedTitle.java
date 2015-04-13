package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedTitle extends CharacterSerializedPart implements VersionableObject
{
    public final ArrayList<TitleAvalability> availableTitles;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedTitle() {
        super();
        this.availableTitles = new ArrayList<TitleAvalability>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedTitle.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedTitle");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedTitle", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedTitle.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedTitle");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedTitle", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedTitle.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.availableTitles.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.availableTitles.size());
        for (int i = 0; i < this.availableTitles.size(); ++i) {
            final TitleAvalability availableTitles_element = this.availableTitles.get(i);
            final boolean availableTitles_element_ok = availableTitles_element.serialize(buffer);
            if (!availableTitles_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int availableTitles_size = buffer.getShort() & 0xFFFF;
        this.availableTitles.clear();
        this.availableTitles.ensureCapacity(availableTitles_size);
        for (int i = 0; i < availableTitles_size; ++i) {
            final TitleAvalability availableTitles_element = new TitleAvalability();
            final boolean availableTitles_element_ok = availableTitles_element.unserialize(buffer);
            if (!availableTitles_element_ok) {
                return false;
            }
            this.availableTitles.add(availableTitles_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.availableTitles.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.availableTitles.size(); ++i) {
            final TitleAvalability availableTitles_element = this.availableTitles.get(i);
            size += availableTitles_element.serializedSize();
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
        repr.append(prefix).append("availableTitles=");
        if (this.availableTitles.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.availableTitles.size()).append(" elements)...\n");
            for (int i = 0; i < this.availableTitles.size(); ++i) {
                final TitleAvalability availableTitles_element = this.availableTitles.get(i);
                availableTitles_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class TitleAvalability implements VersionableObject
    {
        public short availableTitle;
        public static final int SERIALIZED_SIZE = 2;
        
        public TitleAvalability() {
            super();
            this.availableTitle = -1;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putShort(this.availableTitle);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.availableTitle = buffer.getShort();
            return true;
        }
        
        @Override
        public void clear() {
            this.availableTitle = -1;
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
            repr.append(prefix).append("availableTitle=").append(this.availableTitle).append('\n');
        }
    }
}
