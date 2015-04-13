package com.ankamagames.wakfu.common.datas;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import java.util.*;

public class CharacterSerializedDimensionalBagViewInventory extends CharacterSerializedPart implements VersionableObject
{
    public final ArrayList<View> views;
    private final BinarSerialPart m_binarPart;
    
    public CharacterSerializedDimensionalBagViewInventory() {
        super();
        this.views = new ArrayList<View>(0);
        this.m_binarPart = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                try {
                    final boolean ok = CharacterSerializedDimensionalBagViewInventory.this.serialize(buffer);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la s\u00e9rialisation de CharacterSerializedDimensionalBagViewInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de CharacterSerializedDimensionalBagViewInventory", e);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                try {
                    final boolean ok = CharacterSerializedDimensionalBagViewInventory.this.unserializeVersion(buffer, version);
                    if (ok) {
                        this.markAsSuccess();
                    }
                    else {
                        this.markAsError("Erreur lors de la d\u00e9s\u00e9rialisation de CharacterSerializedDimensionalBagViewInventory");
                    }
                }
                catch (Exception e) {
                    this.markAsError("Exception lev\u00e9e lors de la d\u00e9s\u00e9rialisation de CharacterSerializedDimensionalBagViewInventory", e);
                }
            }
            
            @Override
            public int expectedSize() {
                return CharacterSerializedDimensionalBagViewInventory.this.serializedSize();
            }
        };
    }
    
    @Override
    public BinarSerialPart getBinarPart() {
        return this.m_binarPart;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.views.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.views.size());
        for (int i = 0; i < this.views.size(); ++i) {
            final View views_element = this.views.get(i);
            final boolean views_element_ok = views_element.serialize(buffer);
            if (!views_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int views_size = buffer.getShort() & 0xFFFF;
        this.views.clear();
        this.views.ensureCapacity(views_size);
        for (int i = 0; i < views_size; ++i) {
            final View views_element = new View();
            final boolean views_element_ok = views_element.unserialize(buffer);
            if (!views_element_ok) {
                return false;
            }
            this.views.add(views_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.views.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 1) {
            return this.unserialize(buffer);
        }
        final CharacterSerializedDimensionalBagViewInventoryConverter converter = new CharacterSerializedDimensionalBagViewInventoryConverter();
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
        for (int i = 0; i < this.views.size(); ++i) {
            final View views_element = this.views.get(i);
            size += views_element.serializedSize();
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
        repr.append(prefix).append("views=");
        if (this.views.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.views.size()).append(" elements)...\n");
            for (int i = 0; i < this.views.size(); ++i) {
                final View views_element = this.views.get(i);
                views_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class View implements VersionableObject
    {
        public int viewId;
        public static final int SERIALIZED_SIZE = 4;
        
        public View() {
            super();
            this.viewId = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.viewId);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.viewId = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.viewId = 0;
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
            repr.append(prefix).append("viewId=").append(this.viewId).append('\n');
        }
    }
    
    private final class CharacterSerializedDimensionalBagViewInventoryConverter
    {
        private final ArrayList<View> views;
        
        private CharacterSerializedDimensionalBagViewInventoryConverter() {
            super();
            this.views = new ArrayList<View>(0);
        }
        
        public void pushResult() {
            CharacterSerializedDimensionalBagViewInventory.this.views.clear();
            CharacterSerializedDimensionalBagViewInventory.this.views.ensureCapacity(this.views.size());
            CharacterSerializedDimensionalBagViewInventory.this.views.addAll(this.views);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version >= 1) {
                return false;
            }
            final boolean ok = this.unserialize_v0(buffer);
            if (ok) {
                this.convert_v0_to_v1();
                return true;
            }
            return false;
        }
    }
}
