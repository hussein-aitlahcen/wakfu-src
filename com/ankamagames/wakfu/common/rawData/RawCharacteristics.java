package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawCharacteristics implements VersionableObject
{
    public final ArrayList<Characteristic> characteristics;
    
    public RawCharacteristics() {
        super();
        this.characteristics = new ArrayList<Characteristic>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.characteristics.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.characteristics.size());
        for (int i = 0; i < this.characteristics.size(); ++i) {
            final Characteristic characteristics_element = this.characteristics.get(i);
            final boolean characteristics_element_ok = characteristics_element.serialize(buffer);
            if (!characteristics_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int characteristics_size = buffer.getShort() & 0xFFFF;
        this.characteristics.clear();
        this.characteristics.ensureCapacity(characteristics_size);
        for (int i = 0; i < characteristics_size; ++i) {
            final Characteristic characteristics_element = new Characteristic();
            final boolean characteristics_element_ok = characteristics_element.unserialize(buffer);
            if (!characteristics_element_ok) {
                return false;
            }
            this.characteristics.add(characteristics_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.characteristics.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10036003) {
            return this.unserialize(buffer);
        }
        final RawCharacteristicsConverter converter = new RawCharacteristicsConverter();
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
        for (int i = 0; i < this.characteristics.size(); ++i) {
            final Characteristic characteristics_element = this.characteristics.get(i);
            size += characteristics_element.serializedSize();
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
        repr.append(prefix).append("characteristics=");
        if (this.characteristics.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.characteristics.size()).append(" elements)...\n");
            for (int i = 0; i < this.characteristics.size(); ++i) {
                final Characteristic characteristics_element = this.characteristics.get(i);
                characteristics_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Characteristic implements VersionableObject
    {
        public byte index;
        public int current;
        public int min;
        public int max;
        public int maxPercentModifier;
        public static final int SERIALIZED_SIZE = 17;
        
        public Characteristic() {
            super();
            this.index = 0;
            this.current = 0;
            this.min = 0;
            this.max = 0;
            this.maxPercentModifier = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.put(this.index);
            buffer.putInt(this.current);
            buffer.putInt(this.min);
            buffer.putInt(this.max);
            buffer.putInt(this.maxPercentModifier);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.index = buffer.get();
            this.current = buffer.getInt();
            this.min = buffer.getInt();
            this.max = buffer.getInt();
            this.maxPercentModifier = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.index = 0;
            this.current = 0;
            this.min = 0;
            this.max = 0;
            this.maxPercentModifier = 0;
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10036003) {
                return this.unserialize(buffer);
            }
            final CharacteristicConverter converter = new CharacteristicConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            return 17;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("index=").append(this.index).append('\n');
            repr.append(prefix).append("current=").append(this.current).append('\n');
            repr.append(prefix).append("min=").append(this.min).append('\n');
            repr.append(prefix).append("max=").append(this.max).append('\n');
            repr.append(prefix).append("maxPercentModifier=").append(this.maxPercentModifier).append('\n');
        }
        
        private final class CharacteristicConverter
        {
            private byte index;
            private int current;
            private int min;
            private int max;
            private int maxPercentModifier;
            
            private CharacteristicConverter() {
                super();
                this.index = 0;
                this.current = 0;
                this.min = 0;
                this.max = 0;
                this.maxPercentModifier = 0;
            }
            
            public void pushResult() {
                Characteristic.this.index = this.index;
                Characteristic.this.current = this.current;
                Characteristic.this.min = this.min;
                Characteristic.this.max = this.max;
                Characteristic.this.maxPercentModifier = this.maxPercentModifier;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                this.index = buffer.get();
                this.current = buffer.getInt();
                this.min = buffer.getInt();
                this.max = buffer.getInt();
                return true;
            }
            
            public void convert_v1_to_v10036003() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
                if (version >= 10036003) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10036003();
                    return true;
                }
                return false;
            }
        }
    }
    
    private final class RawCharacteristicsConverter
    {
        private final ArrayList<Characteristic> characteristics;
        
        private RawCharacteristicsConverter() {
            super();
            this.characteristics = new ArrayList<Characteristic>(0);
        }
        
        public void pushResult() {
            RawCharacteristics.this.characteristics.clear();
            RawCharacteristics.this.characteristics.ensureCapacity(this.characteristics.size());
            RawCharacteristics.this.characteristics.addAll(this.characteristics);
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int characteristics_size = buffer.getShort() & 0xFFFF;
            this.characteristics.clear();
            this.characteristics.ensureCapacity(characteristics_size);
            for (int i = 0; i < characteristics_size; ++i) {
                final Characteristic characteristics_element = new Characteristic();
                final boolean characteristics_element_ok = characteristics_element.unserializeVersion(buffer, 1);
                if (!characteristics_element_ok) {
                    return false;
                }
                this.characteristics.add(characteristics_element);
            }
            return true;
        }
        
        public void convert_v1_to_v10036003() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 1) {
                return false;
            }
            if (version >= 10036003) {
                return false;
            }
            final boolean ok = this.unserialize_v1(buffer);
            if (ok) {
                this.convert_v1_to_v10036003();
                return true;
            }
            return false;
        }
    }
}
