package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedEmoteInventory extends CharacterSerializedPart implements VersionableObject
{
    public final ArrayList<Emote> emotes;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedEmoteInventory() {
        super();
        this.emotes = new ArrayList<Emote>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedEmoteInventory.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedEmoteInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedEmoteInventory", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedEmoteInventory.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedEmoteInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedEmoteInventory", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedEmoteInventory.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.emotes.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.emotes.size());
        for (int i = 0; i < this.emotes.size(); ++i) {
            final Emote emotes_element = this.emotes.get(i);
            final boolean emotes_element_ok = emotes_element.serialize(buffer);
            if (!emotes_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int emotes_size = buffer.getShort() & 0xFFFF;
        this.emotes.clear();
        this.emotes.ensureCapacity(emotes_size);
        for (int i = 0; i < emotes_size; ++i) {
            final Emote emotes_element = new Emote();
            final boolean emotes_element_ok = emotes_element.unserialize(buffer);
            if (!emotes_element_ok) {
                return false;
            }
            this.emotes.add(emotes_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.emotes.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 220) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedEmoteInventoryConverter converter = new CharacterSerializedEmoteInventoryConverter();
        final boolean ok = converter.unserializeVersion(buffer, version);
        if (ok) {
            converter.pushResult();
            return true;
        }
        return false;
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.emotes.size(); ++i) {
            final Emote emotes_element = this.emotes.get(i);
            size += emotes_element.serializedSize();
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
        repr.append(prefix).append("emotes=");
        if (this.emotes.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.emotes.size()).append(" elements)...\n");
            for (int i = 0; i < this.emotes.size(); ++i) {
                final Emote emotes_element = this.emotes.get(i);
                emotes_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Emote implements VersionableObject
    {
        public int emoteId;
        public static final int SERIALIZED_SIZE = 4;
        
        public Emote() {
            super();
            this.emoteId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.emoteId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.emoteId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.emoteId = 0;
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
            repr.append(prefix).append("emoteId=").append(this.emoteId).append('\n');
        }
    }
    
    private final class CharacterSerializedEmoteInventoryConverter
    {
        private final ArrayList<Emote> emotes;
        
        private CharacterSerializedEmoteInventoryConverter() {
            super();
            this.emotes = new ArrayList<Emote>(0);
        }
        
        public void pushResult() {
            CharacterSerializedEmoteInventory.this.emotes.clear();
            CharacterSerializedEmoteInventory.this.emotes.ensureCapacity(this.emotes.size());
            CharacterSerializedEmoteInventory.this.emotes.addAll(this.emotes);
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int emotes_size = buffer.getShort() & 0xFFFF;
            this.emotes.clear();
            this.emotes.ensureCapacity(emotes_size);
            for (int i = 0; i < emotes_size; ++i) {
                final Emote emotes_element = new Emote();
                final boolean emotes_element_ok = emotes_element.unserializeVersion(buffer, 1);
                if (!emotes_element_ok) {
                    return false;
                }
                this.emotes.add(emotes_element);
            }
            return true;
        }
        
        public void convert_v1_to_v220() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 220) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v220();
                return true;
            }
            return false;
        }
    }
}
