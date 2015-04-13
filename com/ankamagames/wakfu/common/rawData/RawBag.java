package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawBag implements VersionableObject
{
    public long uniqueId;
    public int referenceId;
    public byte position;
    public short maximumSize;
    public final RawInventoryItemInventory inventory;
    
    public RawBag() {
        super();
        this.uniqueId = 0L;
        this.referenceId = 0;
        this.position = 0;
        this.maximumSize = 0;
        this.inventory = new RawInventoryItemInventory();
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.uniqueId);
        buffer.putInt(this.referenceId);
        buffer.put(this.position);
        buffer.putShort(this.maximumSize);
        final boolean inventory_ok = this.inventory.serialize(buffer);
        return inventory_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.uniqueId = buffer.getLong();
        this.referenceId = buffer.getInt();
        this.position = buffer.get();
        this.maximumSize = buffer.getShort();
        final boolean inventory_ok = this.inventory.unserialize(buffer);
        return inventory_ok;
    }
    
    @Override
    public void clear() {
        this.uniqueId = 0L;
        this.referenceId = 0;
        this.position = 0;
        this.maximumSize = 0;
        this.inventory.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037001) {
            return this.unserialize(buffer);
        }
        final RawBagConverter converter = new RawBagConverter();
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
        size += 8;
        size += 4;
        ++size;
        size += 2;
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
        repr.append(prefix).append("uniqueId=").append(this.uniqueId).append('\n');
        repr.append(prefix).append("referenceId=").append(this.referenceId).append('\n');
        repr.append(prefix).append("position=").append(this.position).append('\n');
        repr.append(prefix).append("maximumSize=").append(this.maximumSize).append('\n');
        repr.append(prefix).append("inventory=...\n");
        this.inventory.internalToString(repr, prefix + "  ");
    }
    
    private final class RawBagConverter
    {
        private long uniqueId;
        private int referenceId;
        private byte position;
        private byte type;
        private short maximumSize;
        private final RawInventoryItemInventory inventory;
        
        private RawBagConverter() {
            super();
            this.uniqueId = 0L;
            this.referenceId = 0;
            this.position = 0;
            this.type = 0;
            this.maximumSize = 0;
            this.inventory = new RawInventoryItemInventory();
        }
        
        public void pushResult() {
            RawBag.this.uniqueId = this.uniqueId;
            RawBag.this.referenceId = this.referenceId;
            RawBag.this.position = this.position;
            RawBag.this.maximumSize = this.maximumSize;
            RawBag.this.inventory.contents.clear();
            RawBag.this.inventory.contents.ensureCapacity(this.inventory.contents.size());
            RawBag.this.inventory.contents.addAll(this.inventory.contents);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.type = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 1);
            return inventory_ok;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.type = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 313);
            return inventory_ok;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.type = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 314);
            return inventory_ok;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.type = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 315);
            return inventory_ok;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.type = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 10003);
            return inventory_ok;
        }
        
        private boolean unserialize_v10020(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 10020);
            return inventory_ok;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 10023);
            return inventory_ok;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 10028000);
            return inventory_ok;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 10029000);
            return inventory_ok;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 10032003);
            return inventory_ok;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 10035004);
            return inventory_ok;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 10035007);
            return inventory_ok;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            this.uniqueId = buffer.getLong();
            this.referenceId = buffer.getInt();
            this.position = buffer.get();
            this.maximumSize = buffer.getShort();
            final boolean inventory_ok = this.inventory.unserializeVersion(buffer, 10036004);
            return inventory_ok;
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
