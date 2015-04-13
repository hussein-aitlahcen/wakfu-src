package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;

public class CharacterSerializedShortcutInventories extends CharacterSerializedPart implements VersionableObject
{
    public final ArrayList<ShortcutInventory> shortcutInventories;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedShortcutInventories() {
        super();
        this.shortcutInventories = new ArrayList<ShortcutInventory>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedShortcutInventories.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedShortcutInventories");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedShortcutInventories", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedShortcutInventories.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedShortcutInventories");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedShortcutInventories", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedShortcutInventories.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.shortcutInventories.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.shortcutInventories.size());
        for (int i = 0; i < this.shortcutInventories.size(); ++i) {
            final ShortcutInventory shortcutInventories_element = this.shortcutInventories.get(i);
            final boolean shortcutInventories_element_ok = shortcutInventories_element.serialize(buffer);
            if (!shortcutInventories_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int shortcutInventories_size = buffer.getShort() & 0xFFFF;
        this.shortcutInventories.clear();
        this.shortcutInventories.ensureCapacity(shortcutInventories_size);
        for (int i = 0; i < shortcutInventories_size; ++i) {
            final ShortcutInventory shortcutInventories_element = new ShortcutInventory();
            final boolean shortcutInventories_element_ok = shortcutInventories_element.unserialize(buffer);
            if (!shortcutInventories_element_ok) {
                return false;
            }
            this.shortcutInventories.add(shortcutInventories_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.shortcutInventories.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        size += 2;
        for (int i = 0; i < this.shortcutInventories.size(); ++i) {
            final ShortcutInventory shortcutInventories_element = this.shortcutInventories.get(i);
            size += shortcutInventories_element.serializedSize();
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
        repr.append(prefix).append("shortcutInventories=");
        if (this.shortcutInventories.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.shortcutInventories.size()).append(" elements)...\n");
            for (int i = 0; i < this.shortcutInventories.size(); ++i) {
                final ShortcutInventory shortcutInventories_element = this.shortcutInventories.get(i);
                shortcutInventories_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class ShortcutInventory implements VersionableObject
    {
        public final RawShortcutInventory inventory;
        
        public ShortcutInventory() {
            super();
            this.inventory = new RawShortcutInventory();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean inventory_ok = this.inventory.serialize(buffer);
            return inventory_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean inventory_ok = this.inventory.unserialize(buffer);
            return inventory_ok;
        }
        
        @Override
        public void clear() {
            this.inventory.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.inventory.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("inventory=...\n");
            this.inventory.internalToString(repr, prefix + "  ");
        }
    }
}
