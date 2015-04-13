package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawBagContainer implements VersionableObject
{
    public final ArrayList<Bag> bags;
    
    public RawBagContainer() {
        super();
        this.bags = new ArrayList<Bag>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.bags.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.bags.size());
        for (int i = 0; i < this.bags.size(); ++i) {
            final Bag bags_element = this.bags.get(i);
            final boolean bags_element_ok = bags_element.serialize(buffer);
            if (!bags_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        final int bags_size = buffer.getShort() & 0xFFFF;
        this.bags.clear();
        this.bags.ensureCapacity(bags_size);
        for (int i = 0; i < bags_size; ++i) {
            final Bag bags_element = new Bag();
            final boolean bags_element_ok = bags_element.unserialize(buffer);
            if (!bags_element_ok) {
                return false;
            }
            this.bags.add(bags_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.bags.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawBagContainerConverter converter = new RawBagContainerConverter();
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
        for (int i = 0; i < this.bags.size(); ++i) {
            final Bag bags_element = this.bags.get(i);
            size += bags_element.serializedSize();
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
        repr.append(prefix).append("bags=");
        if (this.bags.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.bags.size()).append(" elements)...\n");
            for (int i = 0; i < this.bags.size(); ++i) {
                final Bag bags_element = this.bags.get(i);
                bags_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class Bag implements VersionableObject
    {
        public final RawBag bag;
        
        public Bag() {
            super();
            this.bag = new RawBag();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.serialize(buffer);
            return bag_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean bag_ok = this.bag.unserialize(buffer);
            return bag_ok;
        }
        
        @Override
        public void clear() {
            this.bag.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10037001) {
                return this.unserialize(buffer);
            }
            final BagConverter converter = new BagConverter();
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
            size += this.bag.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("bag=...\n");
            this.bag.internalToString(repr, prefix + "  ");
        }
        
        private final class BagConverter
        {
            private final RawBag bag;
            
            private BagConverter() {
                super();
                this.bag = new RawBag();
            }
            
            public void pushResult() {
                Bag.this.bag.uniqueId = this.bag.uniqueId;
                Bag.this.bag.referenceId = this.bag.referenceId;
                Bag.this.bag.position = this.bag.position;
                Bag.this.bag.maximumSize = this.bag.maximumSize;
                Bag.this.bag.inventory.contents.clear();
                Bag.this.bag.inventory.contents.ensureCapacity(this.bag.inventory.contents.size());
                Bag.this.bag.inventory.contents.addAll(this.bag.inventory.contents);
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                return true;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 1);
                return bag_ok;
            }
            
            private boolean unserialize_v313(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 313);
                return bag_ok;
            }
            
            private boolean unserialize_v314(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 314);
                return bag_ok;
            }
            
            private boolean unserialize_v315(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 315);
                return bag_ok;
            }
            
            private boolean unserialize_v10003(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 10003);
                return bag_ok;
            }
            
            private boolean unserialize_v10020(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 10020);
                return bag_ok;
            }
            
            private boolean unserialize_v10023(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 10023);
                return bag_ok;
            }
            
            private boolean unserialize_v10028000(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 10028000);
                return bag_ok;
            }
            
            private boolean unserialize_v10029000(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 10029000);
                return bag_ok;
            }
            
            private boolean unserialize_v10032003(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 10032003);
                return bag_ok;
            }
            
            private boolean unserialize_v10035004(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 10035004);
                return bag_ok;
            }
            
            private boolean unserialize_v10035007(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 10035007);
                return bag_ok;
            }
            
            private boolean unserialize_v10036004(final ByteBuffer buffer) {
                final boolean bag_ok = this.bag.unserializeVersion(buffer, 10036004);
                return bag_ok;
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
            
            public void convert_v10003_to_v10020() {
            }
            
            public void convert_v10020_to_v10023() {
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
                        this.convert_v10003_to_v10020();
                        this.convert_v10020_to_v10023();
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
                        this.convert_v10003_to_v10020();
                        this.convert_v10020_to_v10023();
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
                        this.convert_v10003_to_v10020();
                        this.convert_v10020_to_v10023();
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
                        this.convert_v10003_to_v10020();
                        this.convert_v10020_to_v10023();
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
                        this.convert_v10003_to_v10020();
                        this.convert_v10020_to_v10023();
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
                else if (version < 10020) {
                    final boolean ok = this.unserialize_v10003(buffer);
                    if (ok) {
                        this.convert_v10003_to_v10020();
                        this.convert_v10020_to_v10023();
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
                    final boolean ok = this.unserialize_v10020(buffer);
                    if (ok) {
                        this.convert_v10020_to_v10023();
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
    
    private final class RawBagContainerConverter
    {
        private final ArrayList<Bag> bags;
        
        private RawBagContainerConverter() {
            super();
            this.bags = new ArrayList<Bag>(0);
        }
        
        public void pushResult() {
            RawBagContainer.this.bags.clear();
            RawBagContainer.this.bags.ensureCapacity(this.bags.size());
            RawBagContainer.this.bags.addAll(this.bags);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 1);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 313);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 314);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 315);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 10003);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v10020(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 10020);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 10023);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 10028000);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 10029000);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 10032003);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 10035004);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 10035007);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
            }
            return true;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            final int bags_size = buffer.getShort() & 0xFFFF;
            this.bags.clear();
            this.bags.ensureCapacity(bags_size);
            for (int i = 0; i < bags_size; ++i) {
                final Bag bags_element = new Bag();
                final boolean bags_element_ok = bags_element.unserializeVersion(buffer, 10036004);
                if (!bags_element_ok) {
                    return false;
                }
                this.bags.add(bags_element);
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
        
        public void convert_v10003_to_v10020() {
        }
        
        public void convert_v10020_to_v10023() {
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
                    this.convert_v10003_to_v10020();
                    this.convert_v10020_to_v10023();
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
                    this.convert_v10003_to_v10020();
                    this.convert_v10020_to_v10023();
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
                    this.convert_v10003_to_v10020();
                    this.convert_v10020_to_v10023();
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
                    this.convert_v10003_to_v10020();
                    this.convert_v10020_to_v10023();
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
                    this.convert_v10003_to_v10020();
                    this.convert_v10020_to_v10023();
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
            else if (version < 10020) {
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10020();
                    this.convert_v10020_to_v10023();
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
                final boolean ok = this.unserialize_v10020(buffer);
                if (ok) {
                    this.convert_v10020_to_v10023();
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
