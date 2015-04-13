package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawSymbiot implements VersionableObject
{
    public final ArrayList<CapturedCreature> capturedCreatures;
    public byte currentCreatureIndex;
    
    public RawSymbiot() {
        super();
        this.capturedCreatures = new ArrayList<CapturedCreature>(0);
        this.currentCreatureIndex = 0;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.capturedCreatures.size() > 255) {
            return false;
        }
        buffer.put((byte)this.capturedCreatures.size());
        for (int i = 0; i < this.capturedCreatures.size(); ++i) {
            final CapturedCreature capturedCreatures_element = this.capturedCreatures.get(i);
            final boolean capturedCreatures_element_ok = capturedCreatures_element.serialize(buffer);
            if (!capturedCreatures_element_ok) {
                return false;
            }
        }
        buffer.put(this.currentCreatureIndex);
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int capturedCreatures_size = buffer.get() & 0xFF;
        this.capturedCreatures.clear();
        this.capturedCreatures.ensureCapacity(capturedCreatures_size);
        for (int i = 0; i < capturedCreatures_size; ++i) {
            final CapturedCreature capturedCreatures_element = new CapturedCreature();
            final boolean capturedCreatures_element_ok = capturedCreatures_element.unserialize(buffer);
            if (!capturedCreatures_element_ok) {
                return false;
            }
            this.capturedCreatures.add(capturedCreatures_element);
        }
        this.currentCreatureIndex = buffer.get();
        return true;
    }
    
    @Override
    public void clear() {
        this.capturedCreatures.clear();
        this.currentCreatureIndex = 0;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10013) {
            return this.unserialize(buffer);
        }
        final RawSymbiotConverter converter = new RawSymbiotConverter();
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
        ++size;
        for (int i = 0; i < this.capturedCreatures.size(); ++i) {
            final CapturedCreature capturedCreatures_element = this.capturedCreatures.get(i);
            size += capturedCreatures_element.serializedSize();
        }
        return ++size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("capturedCreatures=");
        if (this.capturedCreatures.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.capturedCreatures.size()).append(" elements)...\n");
            for (int i = 0; i < this.capturedCreatures.size(); ++i) {
                final CapturedCreature capturedCreatures_element = this.capturedCreatures.get(i);
                capturedCreatures_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("currentCreatureIndex=").append(this.currentCreatureIndex).append('\n');
    }
    
    public static class CapturedCreature implements VersionableObject
    {
        public final RawCapturedCreature capturedCreature;
        
        public CapturedCreature() {
            super();
            this.capturedCreature = new RawCapturedCreature();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean capturedCreature_ok = this.capturedCreature.serialize(buffer);
            return capturedCreature_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean capturedCreature_ok = this.capturedCreature.unserialize(buffer);
            return capturedCreature_ok;
        }
        
        @Override
        public void clear() {
            this.capturedCreature.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10013) {
                return this.unserialize(buffer);
            }
            final CapturedCreatureConverter converter = new CapturedCreatureConverter();
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
            size += this.capturedCreature.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("capturedCreature=...\n");
            this.capturedCreature.internalToString(repr, prefix + "  ");
        }
        
        private final class CapturedCreatureConverter
        {
            private final RawCapturedCreature capturedCreature;
            
            private CapturedCreatureConverter() {
                super();
                this.capturedCreature = new RawCapturedCreature();
            }
            
            public void pushResult() {
                CapturedCreature.this.capturedCreature.index = this.capturedCreature.index;
                CapturedCreature.this.capturedCreature.typeId = this.capturedCreature.typeId;
                CapturedCreature.this.capturedCreature.quantity = this.capturedCreature.quantity;
                CapturedCreature.this.capturedCreature.name = this.capturedCreature.name;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean capturedCreature_ok = this.capturedCreature.unserializeVersion(buffer, 1);
                return capturedCreature_ok;
            }
            
            public void convert_v1_to_v10013() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10013) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10013();
                    return true;
                }
                return false;
            }
        }
    }
    
    private final class RawSymbiotConverter
    {
        private final ArrayList<CapturedCreature> capturedCreatures;
        private byte currentCreatureIndex;
        
        private RawSymbiotConverter() {
            super();
            this.capturedCreatures = new ArrayList<CapturedCreature>(0);
            this.currentCreatureIndex = 0;
        }
        
        public void pushResult() {
            RawSymbiot.this.capturedCreatures.clear();
            RawSymbiot.this.capturedCreatures.ensureCapacity(this.capturedCreatures.size());
            RawSymbiot.this.capturedCreatures.addAll(this.capturedCreatures);
            RawSymbiot.this.currentCreatureIndex = this.currentCreatureIndex;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int capturedCreatures_size = buffer.get() & 0xFF;
            this.capturedCreatures.clear();
            this.capturedCreatures.ensureCapacity(capturedCreatures_size);
            for (int i = 0; i < capturedCreatures_size; ++i) {
                final CapturedCreature capturedCreatures_element = new CapturedCreature();
                final boolean capturedCreatures_element_ok = capturedCreatures_element.unserializeVersion(buffer, 1);
                if (!capturedCreatures_element_ok) {
                    return false;
                }
                this.capturedCreatures.add(capturedCreatures_element);
            }
            this.currentCreatureIndex = buffer.get();
            return true;
        }
        
        public void convert_v1_to_v10013() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10013) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10013();
                return true;
            }
            return false;
        }
    }
}
