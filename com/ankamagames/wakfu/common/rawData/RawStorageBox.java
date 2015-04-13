package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawStorageBox implements VersionableObject
{
    public final ArrayList<Compartment> compartments;
    
    public RawStorageBox() {
        super();
        this.compartments = new ArrayList<Compartment>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.compartments.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.compartments.size());
        for (int i = 0; i < this.compartments.size(); ++i) {
            final Compartment compartments_element = this.compartments.get(i);
            final boolean compartments_element_ok = compartments_element.serialize(buffer);
            if (!compartments_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int compartments_size = buffer.getShort() & 0xFFFF;
        this.compartments.clear();
        this.compartments.ensureCapacity(compartments_size);
        for (int i = 0; i < compartments_size; ++i) {
            final Compartment compartments_element = new Compartment();
            final boolean compartments_element_ok = compartments_element.unserialize(buffer);
            if (!compartments_element_ok) {
                return false;
            }
            this.compartments.add(compartments_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.compartments.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawStorageBoxConverter converter = new RawStorageBoxConverter();
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
        for (int i = 0; i < this.compartments.size(); ++i) {
            final Compartment compartments_element = this.compartments.get(i);
            size += compartments_element.serializedSize();
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
        repr.append(prefix).append("compartments=");
        if (this.compartments.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.compartments.size()).append(" elements)...\n");
            for (int i = 0; i < this.compartments.size(); ++i) {
                final Compartment compartments_element = this.compartments.get(i);
                compartments_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Compartment implements VersionableObject
    {
        public final RawStorageBoxCompartment compartment;
        
        public Compartment() {
            super();
            this.compartment = new RawStorageBoxCompartment();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean compartment_ok = this.compartment.serialize(buffer);
            return compartment_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean compartment_ok = this.compartment.unserialize(buffer);
            return compartment_ok;
        }
        
        @Override
        public void clear() {
            this.compartment.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10037001) {
                return this.unserialize(buffer);
            }
            final CompartmentConverter converter = new CompartmentConverter();
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
            size += this.compartment.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("compartment=...\n");
            this.compartment.internalToString(repr, prefix + "  ");
        }
        
        private final class CompartmentConverter
        {
            private final RawStorageBoxCompartment compartment;
            
            private CompartmentConverter() {
                super();
                this.compartment = new RawStorageBoxCompartment();
            }
            
            public void pushResult() {
                Compartment.this.compartment.id = this.compartment.id;
                Compartment.this.compartment.items.clear();
                Compartment.this.compartment.items.ensureCapacity(this.compartment.items.size());
                Compartment.this.compartment.items.addAll(this.compartment.items);
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 0);
                return compartment_ok;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 1);
                return compartment_ok;
            }
            
            private boolean unserialize_v313(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 313);
                return compartment_ok;
            }
            
            private boolean unserialize_v314(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 314);
                return compartment_ok;
            }
            
            private boolean unserialize_v315(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 315);
                return compartment_ok;
            }
            
            private boolean unserialize_v10003(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 10003);
                return compartment_ok;
            }
            
            private boolean unserialize_v10023(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 10023);
                return compartment_ok;
            }
            
            private boolean unserialize_v10028000(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 10028000);
                return compartment_ok;
            }
            
            private boolean unserialize_v10029000(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 10029000);
                return compartment_ok;
            }
            
            private boolean unserialize_v10032003(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 10032003);
                return compartment_ok;
            }
            
            private boolean unserialize_v10035004(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 10035004);
                return compartment_ok;
            }
            
            private boolean unserialize_v10035007(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 10035007);
                return compartment_ok;
            }
            
            private boolean unserialize_v10036004(final ByteBuffer buffer) {
                final boolean compartment_ok = this.compartment.unserializeVersion(buffer, 10036004);
                return compartment_ok;
            }
            
            public void convert_v0_to_v1() {
            }
            
            public void convert_v1_to_v313() {
            }
            
            public void convert_v313_to_v314() {
            }
            
            public void convert_v314_to_v315() {
            }
            
            public void convert_v315_to_v10003() {
            }
            
            public void convert_v10003_to_v10023() {
            }
            
            public void convert_v10023_to_v10028000() {
            }
            
            public void convert_v10028000_to_v10029000() {
            }
            
            public void convert_v10029000_to_v10032003() {
            }
            
            public void convert_v10032003_to_v10035004() {
            }
            
            public void convert_v10035004_to_v10035007() {
            }
            
            public void convert_v10035007_to_v10036004() {
            }
            
            public void convert_v10036004_to_v10037001() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 0) {
                    return false;
                }
                if (version < 1) {
                    final boolean ok = this.unserialize_v0(buffer);
                    if (ok) {
                        this.convert_v0_to_v1();
                        this.convert_v1_to_v313();
                        this.convert_v313_to_v314();
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 313) {
                    final boolean ok = this.unserialize_v1(buffer);
                    if (ok) {
                        this.convert_v1_to_v313();
                        this.convert_v313_to_v314();
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 314) {
                    final boolean ok = this.unserialize_v313(buffer);
                    if (ok) {
                        this.convert_v313_to_v314();
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 315) {
                    final boolean ok = this.unserialize_v314(buffer);
                    if (ok) {
                        this.convert_v314_to_v315();
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10003) {
                    final boolean ok = this.unserialize_v315(buffer);
                    if (ok) {
                        this.convert_v315_to_v10003();
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10023) {
                    final boolean ok = this.unserialize_v10003(buffer);
                    if (ok) {
                        this.convert_v10003_to_v10023();
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10028000) {
                    final boolean ok = this.unserialize_v10023(buffer);
                    if (ok) {
                        this.convert_v10023_to_v10028000();
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10029000) {
                    final boolean ok = this.unserialize_v10028000(buffer);
                    if (ok) {
                        this.convert_v10028000_to_v10029000();
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10032003) {
                    final boolean ok = this.unserialize_v10029000(buffer);
                    if (ok) {
                        this.convert_v10029000_to_v10032003();
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10035004) {
                    final boolean ok = this.unserialize_v10032003(buffer);
                    if (ok) {
                        this.convert_v10032003_to_v10035004();
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10035007) {
                    final boolean ok = this.unserialize_v10035004(buffer);
                    if (ok) {
                        this.convert_v10035004_to_v10035007();
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else if (version < 10036004) {
                    final boolean ok = this.unserialize_v10035007(buffer);
                    if (ok) {
                        this.convert_v10035007_to_v10036004();
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
                else {
                    if (version >= 10037001) {
                        return false;
                    }
                    final boolean ok = this.unserialize_v10036004(buffer);
                    if (ok) {
                        this.convert_v10036004_to_v10037001();
                        return true;
                    }
                    return false;
                }
            }
        }
    }
    
    private final class RawStorageBoxConverter
    {
        private final ArrayList<Compartment> compartments;
        
        private RawStorageBoxConverter() {
            super();
            this.compartments = new ArrayList<Compartment>(0);
        }
        
        public void pushResult() {
            RawStorageBox.this.compartments.clear();
            RawStorageBox.this.compartments.ensureCapacity(this.compartments.size());
            RawStorageBox.this.compartments.addAll(this.compartments);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 0);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 1);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 313);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 314);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 315);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 10003);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 10023);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 10028000);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 10029000);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 10032003);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 10035004);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 10035007);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            final int compartments_size = buffer.getShort() & 0xFFFF;
            this.compartments.clear();
            this.compartments.ensureCapacity(compartments_size);
            for (int i = 0; i < compartments_size; ++i) {
                final Compartment compartments_element = new Compartment();
                final boolean compartments_element_ok = compartments_element.unserializeVersion(buffer, 10036004);
                if (!compartments_element_ok) {
                    return false;
                }
                this.compartments.add(compartments_element);
            }
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v313() {
        }
        
        public void convert_v313_to_v314() {
        }
        
        public void convert_v314_to_v315() {
        }
        
        public void convert_v315_to_v10003() {
        }
        
        public void convert_v10003_to_v10023() {
        }
        
        public void convert_v10023_to_v10028000() {
        }
        
        public void convert_v10028000_to_v10029000() {
        }
        
        public void convert_v10029000_to_v10032003() {
        }
        
        public void convert_v10032003_to_v10035004() {
        }
        
        public void convert_v10035004_to_v10035007() {
        }
        
        public void convert_v10035007_to_v10036004() {
        }
        
        public void convert_v10036004_to_v10037001() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 1) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    this.convert_v1_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 313) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 314) {
                final boolean ok = this.unserialize_v313(buffer);
                if (ok) {
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 315) {
                final boolean ok = this.unserialize_v314(buffer);
                if (ok) {
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10003) {
                final boolean ok = this.unserialize_v315(buffer);
                if (ok) {
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10023) {
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10028000) {
                final boolean ok = this.unserialize_v10023(buffer);
                if (ok) {
                    this.convert_v10023_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10029000) {
                final boolean ok = this.unserialize_v10028000(buffer);
                if (ok) {
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10032003) {
                final boolean ok = this.unserialize_v10029000(buffer);
                if (ok) {
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10035004) {
                final boolean ok = this.unserialize_v10032003(buffer);
                if (ok) {
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10035007) {
                final boolean ok = this.unserialize_v10035004(buffer);
                if (ok) {
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else if (version < 10036004) {
                final boolean ok = this.unserialize_v10035007(buffer);
                if (ok) {
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10037001) {
                    return false;
                }
                final boolean ok = this.unserialize_v10036004(buffer);
                if (ok) {
                    this.convert_v10036004_to_v10037001();
                    return true;
                }
                return false;
            }
        }
    }
}
