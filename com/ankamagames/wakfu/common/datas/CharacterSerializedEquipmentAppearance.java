package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;

public class CharacterSerializedEquipmentAppearance extends CharacterSerializedPart implements VersionableObject
{
    public final ArrayList<EquipmentAppearance> content;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedEquipmentAppearance() {
        super();
        this.content = new ArrayList<EquipmentAppearance>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedEquipmentAppearance.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedEquipmentAppearance");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedEquipmentAppearance", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedEquipmentAppearance.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedEquipmentAppearance");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedEquipmentAppearance", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedEquipmentAppearance.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.content.size() > 255) {
            return false;
        }
        buffer.put((byte)this.content.size());
        for (int i = 0; i < this.content.size(); ++i) {
            final EquipmentAppearance content_element = this.content.get(i);
            final boolean content_element_ok = content_element.serialize(buffer);
            if (!content_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int content_size = buffer.get() & 0xFF;
        this.content.clear();
        this.content.ensureCapacity(content_size);
        for (int i = 0; i < content_size; ++i) {
            final EquipmentAppearance content_element = new EquipmentAppearance();
            final boolean content_element_ok = content_element.unserialize(buffer);
            if (!content_element_ok) {
                return false;
            }
            this.content.add(content_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.content.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        return this.unserialize(buffer);
    }
    
    @Override
    public int serializedSize() {
        int size = 0;
        ++size;
        for (int i = 0; i < this.content.size(); ++i) {
            final EquipmentAppearance content_element = this.content.get(i);
            size += content_element.serializedSize();
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
        repr.append(prefix).append("content=");
        if (this.content.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.content.size()).append(" elements)...\n");
            for (int i = 0; i < this.content.size(); ++i) {
                final EquipmentAppearance content_element = this.content.get(i);
                content_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class EquipmentAppearance implements VersionableObject
    {
        public byte position;
        public int referenceId;
        public static final int SERIALIZED_SIZE = 5;
        
        public EquipmentAppearance() {
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
