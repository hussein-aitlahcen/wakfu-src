package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;

public class RawInteractiveElementPersistantData implements VersionableObject
{
    public long templateId;
    public int positionX;
    public int positionY;
    public short positionZ;
    public byte direction;
    public final RawInventoryItem itemForm;
    public AbstractRawPersistantData specificData;
    
    public RawInteractiveElementPersistantData() {
        super();
        this.templateId = 0L;
        this.positionX = 0;
        this.positionY = 0;
        this.positionZ = 0;
        this.direction = 0;
        this.itemForm = new RawInventoryItem();
        this.specificData = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.templateId);
        buffer.putInt(this.positionX);
        buffer.putInt(this.positionY);
        buffer.putShort(this.positionZ);
        buffer.put(this.direction);
        final boolean itemForm_ok = this.itemForm.serialize(buffer);
        if (!itemForm_ok) {
            return false;
        }
        if (this.specificData == null) {
            return false;
        }
        buffer.put(this.specificData.getVirtualId());
        final boolean specificData_ok = this.specificData.serialize(buffer);
        return specificData_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.templateId = buffer.getLong();
        this.positionX = buffer.getInt();
        this.positionY = buffer.getInt();
        this.positionZ = buffer.getShort();
        this.direction = buffer.get();
        final boolean itemForm_ok = this.itemForm.unserialize(buffer);
        if (!itemForm_ok) {
            return false;
        }
        this.specificData = AbstractRawPersistantData.unserializeVirtual(buffer);
        return this.specificData != null;
    }
    
    @Override
    public void clear() {
        this.templateId = 0L;
        this.positionX = 0;
        this.positionY = 0;
        this.positionZ = 0;
        this.direction = 0;
        this.itemForm.clear();
        this.specificData = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037002) {
            return this.unserialize(buffer);
        }
        final RawInteractiveElementPersistantDataConverter converter = new RawInteractiveElementPersistantDataConverter();
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
        size += 4;
        size += 2;
        size = ++size + this.itemForm.serializedSize();
        size = ++size + this.specificData.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("templateId=").append(this.templateId).append('\n');
        repr.append(prefix).append("positionX=").append(this.positionX).append('\n');
        repr.append(prefix).append("positionY=").append(this.positionY).append('\n');
        repr.append(prefix).append("positionZ=").append(this.positionZ).append('\n');
        repr.append(prefix).append("direction=").append(this.direction).append('\n');
        repr.append(prefix).append("itemForm=...\n");
        this.itemForm.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("specificData=<");
        if (this.specificData == null) {
            repr.append(">\n");
        }
        else {
            repr.append(this.specificData.getClass().getSimpleName()).append(">...\n");
            this.specificData.internalToString(repr, prefix + "  ");
        }
    }
    
    private final class RawInteractiveElementPersistantDataConverter
    {
        private long templateId;
        private int positionX;
        private int positionY;
        private short positionZ;
        private byte direction;
        private final RawInventoryItem itemForm;
        private AbstractRawPersistantData specificData;
        
        private RawInteractiveElementPersistantDataConverter() {
            super();
            this.templateId = 0L;
            this.positionX = 0;
            this.positionY = 0;
            this.positionZ = 0;
            this.direction = 0;
            this.itemForm = new RawInventoryItem();
            this.specificData = null;
        }
        
        public void pushResult() {
            RawInteractiveElementPersistantData.this.templateId = this.templateId;
            RawInteractiveElementPersistantData.this.positionX = this.positionX;
            RawInteractiveElementPersistantData.this.positionY = this.positionY;
            RawInteractiveElementPersistantData.this.positionZ = this.positionZ;
            RawInteractiveElementPersistantData.this.direction = this.direction;
            RawInteractiveElementPersistantData.this.itemForm.uniqueId = this.itemForm.uniqueId;
            RawInteractiveElementPersistantData.this.itemForm.refId = this.itemForm.refId;
            RawInteractiveElementPersistantData.this.itemForm.quantity = this.itemForm.quantity;
            RawInteractiveElementPersistantData.this.itemForm.timestamp = this.itemForm.timestamp;
            RawInteractiveElementPersistantData.this.itemForm.pet = this.itemForm.pet;
            RawInteractiveElementPersistantData.this.itemForm.xp = this.itemForm.xp;
            RawInteractiveElementPersistantData.this.itemForm.gems = this.itemForm.gems;
            RawInteractiveElementPersistantData.this.itemForm.rentInfo = this.itemForm.rentInfo;
            RawInteractiveElementPersistantData.this.itemForm.companionInfo = this.itemForm.companionInfo;
            RawInteractiveElementPersistantData.this.itemForm.bind = this.itemForm.bind;
            RawInteractiveElementPersistantData.this.itemForm.elements = this.itemForm.elements;
            RawInteractiveElementPersistantData.this.itemForm.mergedItems = this.itemForm.mergedItems;
            RawInteractiveElementPersistantData.this.specificData = this.specificData;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 1);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 1);
            return this.specificData != null;
        }
        
        private boolean unserialize_v309(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 309);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 309);
            return this.specificData != null;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 313);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 313);
            return this.specificData != null;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 314);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 314);
            return this.specificData != null;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 315);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 315);
            return this.specificData != null;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 10003);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 10003);
            return this.specificData != null;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 10023);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 10023);
            return this.specificData != null;
        }
        
        private boolean unserialize_v1027001(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 1027001);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 1027001);
            return this.specificData != null;
        }
        
        private boolean unserialize_v1027002(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 1027002);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 1027002);
            return this.specificData != null;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 10028000);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 10028000);
            return this.specificData != null;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 10029000);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 10029000);
            return this.specificData != null;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 10032003);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 10032003);
            return this.specificData != null;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 10035004);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 10035004);
            return this.specificData != null;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 10035007);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 10035007);
            return this.specificData != null;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 10036004);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 10036004);
            return this.specificData != null;
        }
        
        private boolean unserialize_v10037001(final ByteBuffer buffer) {
            this.templateId = buffer.getLong();
            this.positionX = buffer.getInt();
            this.positionY = buffer.getInt();
            this.positionZ = buffer.getShort();
            this.direction = buffer.get();
            final boolean itemForm_ok = this.itemForm.unserializeVersion(buffer, 10037001);
            if (!itemForm_ok) {
                return false;
            }
            this.specificData = AbstractRawPersistantData.unserializeVirtualVersion(buffer, 10037001);
            return this.specificData != null;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v309() {
        }
        
        public void convert_v309_to_v313() {
        }
        
        public void convert_v313_to_v314() {
        }
        
        public void convert_v314_to_v315() {
        }
        
        public void convert_v315_to_v10003() {
        }
        
        public void convert_v10003_to_v10023() {
        }
        
        public void convert_v10023_to_v1027001() {
        }
        
        public void convert_v1027001_to_v1027002() {
        }
        
        public void convert_v1027002_to_v10028000() {
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
        
        public void convert_v10037001_to_v10037002() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 1) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    this.convert_v1_to_v309();
                    this.convert_v309_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else if (version < 309) {
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v309();
                    this.convert_v309_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else if (version < 313) {
                final boolean ok = this.unserialize_v309(buffer);
                if (ok) {
                    this.convert_v309_to_v313();
                    this.convert_v313_to_v314();
                    this.convert_v314_to_v315();
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else if (version < 10003) {
                final boolean ok = this.unserialize_v315(buffer);
                if (ok) {
                    this.convert_v315_to_v10003();
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else if (version < 10023) {
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10023();
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else if (version < 1027001) {
                final boolean ok = this.unserialize_v10023(buffer);
                if (ok) {
                    this.convert_v10023_to_v1027001();
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else if (version < 1027002) {
                final boolean ok = this.unserialize_v1027001(buffer);
                if (ok) {
                    this.convert_v1027001_to_v1027002();
                    this.convert_v1027002_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else if (version < 10028000) {
                final boolean ok = this.unserialize_v1027002(buffer);
                if (ok) {
                    this.convert_v1027002_to_v10028000();
                    this.convert_v10028000_to_v10029000();
                    this.convert_v10029000_to_v10032003();
                    this.convert_v10032003_to_v10035004();
                    this.convert_v10035004_to_v10035007();
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
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
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else if (version < 10036004) {
                final boolean ok = this.unserialize_v10035007(buffer);
                if (ok) {
                    this.convert_v10035007_to_v10036004();
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else if (version < 10037001) {
                final boolean ok = this.unserialize_v10036004(buffer);
                if (ok) {
                    this.convert_v10036004_to_v10037001();
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10037002) {
                    return false;
                }
                final boolean ok = this.unserialize_v10037001(buffer);
                if (ok) {
                    this.convert_v10037001_to_v10037002();
                    return true;
                }
                return false;
            }
        }
    }
}
