package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawRoom implements VersionableObject
{
    public byte layoutPosition;
    public final ArrayList<ContainedInteractiveElement> interactiveElements;
    public RawSpecificRooms roomSpecificData;
    
    public RawRoom() {
        super();
        this.layoutPosition = 0;
        this.interactiveElements = new ArrayList<ContainedInteractiveElement>(0);
        this.roomSpecificData = null;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put(this.layoutPosition);
        if (this.interactiveElements.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.interactiveElements.size());
        for (int i = 0; i < this.interactiveElements.size(); ++i) {
            final ContainedInteractiveElement interactiveElements_element = this.interactiveElements.get(i);
            final boolean interactiveElements_element_ok = interactiveElements_element.serialize(buffer);
            if (!interactiveElements_element_ok) {
                return false;
            }
        }
        if (this.roomSpecificData == null) {
            return false;
        }
        buffer.put(this.roomSpecificData.getVirtualId());
        final boolean roomSpecificData_ok = this.roomSpecificData.serialize(buffer);
        return roomSpecificData_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.layoutPosition = buffer.get();
        final int interactiveElements_size = buffer.getShort() & 0xFFFF;
        this.interactiveElements.clear();
        this.interactiveElements.ensureCapacity(interactiveElements_size);
        for (int i = 0; i < interactiveElements_size; ++i) {
            final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
            final boolean interactiveElements_element_ok = interactiveElements_element.unserialize(buffer);
            if (!interactiveElements_element_ok) {
                return false;
            }
            this.interactiveElements.add(interactiveElements_element);
        }
        this.roomSpecificData = RawSpecificRooms.unserializeVirtual(buffer);
        return this.roomSpecificData != null;
    }
    
    @Override
    public void clear() {
        this.layoutPosition = 0;
        this.interactiveElements.clear();
        this.roomSpecificData = null;
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037002) {
            return this.unserialize(buffer);
        }
        final RawRoomConverter converter = new RawRoomConverter();
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
        size += 2;
        for (int i = 0; i < this.interactiveElements.size(); ++i) {
            final ContainedInteractiveElement interactiveElements_element = this.interactiveElements.get(i);
            size += interactiveElements_element.serializedSize();
        }
        size = ++size + this.roomSpecificData.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("layoutPosition=").append(this.layoutPosition).append('\n');
        repr.append(prefix).append("interactiveElements=");
        if (this.interactiveElements.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.interactiveElements.size()).append(" elements)...\n");
            for (int i = 0; i < this.interactiveElements.size(); ++i) {
                final ContainedInteractiveElement interactiveElements_element = this.interactiveElements.get(i);
                interactiveElements_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("roomSpecificData=<");
        if (this.roomSpecificData == null) {
            repr.append(">\n");
        }
        else {
            repr.append(this.roomSpecificData.getClass().getSimpleName()).append(">...\n");
            this.roomSpecificData.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class ContainedInteractiveElement implements VersionableObject
    {
        public final RawInteractiveElementPersistantData persistantData;
        
        public ContainedInteractiveElement() {
            super();
            this.persistantData = new RawInteractiveElementPersistantData();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean persistantData_ok = this.persistantData.serialize(buffer);
            return persistantData_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean persistantData_ok = this.persistantData.unserialize(buffer);
            return persistantData_ok;
        }
        
        @Override
        public void clear() {
            this.persistantData.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10037002) {
                return this.unserialize(buffer);
            }
            final ContainedInteractiveElementConverter converter = new ContainedInteractiveElementConverter();
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
            size += this.persistantData.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("persistantData=...\n");
            this.persistantData.internalToString(repr, prefix + "  ");
        }
        
        private final class ContainedInteractiveElementConverter
        {
            private final RawInteractiveElementPersistantData persistantData;
            
            private ContainedInteractiveElementConverter() {
                super();
                this.persistantData = new RawInteractiveElementPersistantData();
            }
            
            public void pushResult() {
                ContainedInteractiveElement.this.persistantData.templateId = this.persistantData.templateId;
                ContainedInteractiveElement.this.persistantData.positionX = this.persistantData.positionX;
                ContainedInteractiveElement.this.persistantData.positionY = this.persistantData.positionY;
                ContainedInteractiveElement.this.persistantData.positionZ = this.persistantData.positionZ;
                ContainedInteractiveElement.this.persistantData.direction = this.persistantData.direction;
                ContainedInteractiveElement.this.persistantData.itemForm.uniqueId = this.persistantData.itemForm.uniqueId;
                ContainedInteractiveElement.this.persistantData.itemForm.refId = this.persistantData.itemForm.refId;
                ContainedInteractiveElement.this.persistantData.itemForm.quantity = this.persistantData.itemForm.quantity;
                ContainedInteractiveElement.this.persistantData.itemForm.timestamp = this.persistantData.itemForm.timestamp;
                ContainedInteractiveElement.this.persistantData.itemForm.pet = this.persistantData.itemForm.pet;
                ContainedInteractiveElement.this.persistantData.itemForm.xp = this.persistantData.itemForm.xp;
                ContainedInteractiveElement.this.persistantData.itemForm.gems = this.persistantData.itemForm.gems;
                ContainedInteractiveElement.this.persistantData.itemForm.rentInfo = this.persistantData.itemForm.rentInfo;
                ContainedInteractiveElement.this.persistantData.itemForm.companionInfo = this.persistantData.itemForm.companionInfo;
                ContainedInteractiveElement.this.persistantData.itemForm.bind = this.persistantData.itemForm.bind;
                ContainedInteractiveElement.this.persistantData.itemForm.elements = this.persistantData.itemForm.elements;
                ContainedInteractiveElement.this.persistantData.itemForm.mergedItems = this.persistantData.itemForm.mergedItems;
                ContainedInteractiveElement.this.persistantData.specificData = this.persistantData.specificData;
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                return true;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 1);
                return persistantData_ok;
            }
            
            private boolean unserialize_v309(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 309);
                return persistantData_ok;
            }
            
            private boolean unserialize_v313(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 313);
                return persistantData_ok;
            }
            
            private boolean unserialize_v314(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 314);
                return persistantData_ok;
            }
            
            private boolean unserialize_v315(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 315);
                return persistantData_ok;
            }
            
            private boolean unserialize_v10003(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 10003);
                return persistantData_ok;
            }
            
            private boolean unserialize_v10023(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 10023);
                return persistantData_ok;
            }
            
            private boolean unserialize_v1027001(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 1027001);
                return persistantData_ok;
            }
            
            private boolean unserialize_v1027002(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 1027002);
                return persistantData_ok;
            }
            
            private boolean unserialize_v10028000(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 10028000);
                return persistantData_ok;
            }
            
            private boolean unserialize_v10029000(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 10029000);
                return persistantData_ok;
            }
            
            private boolean unserialize_v10032003(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 10032003);
                return persistantData_ok;
            }
            
            private boolean unserialize_v10035004(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 10035004);
                return persistantData_ok;
            }
            
            private boolean unserialize_v10035007(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 10035007);
                return persistantData_ok;
            }
            
            private boolean unserialize_v10036004(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 10036004);
                return persistantData_ok;
            }
            
            private boolean unserialize_v10037001(final ByteBuffer buffer) {
                final boolean persistantData_ok = this.persistantData.unserializeVersion(buffer, 10037001);
                return persistantData_ok;
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
    
    private final class RawRoomConverter
    {
        private byte layoutPosition;
        private final ArrayList<ContainedInteractiveElement> interactiveElements;
        private RawSpecificRooms roomSpecificData;
        
        private RawRoomConverter() {
            super();
            this.layoutPosition = 0;
            this.interactiveElements = new ArrayList<ContainedInteractiveElement>(0);
            this.roomSpecificData = null;
        }
        
        public void pushResult() {
            RawRoom.this.layoutPosition = this.layoutPosition;
            RawRoom.this.interactiveElements.clear();
            RawRoom.this.interactiveElements.ensureCapacity(this.interactiveElements.size());
            RawRoom.this.interactiveElements.addAll(this.interactiveElements);
            RawRoom.this.roomSpecificData = this.roomSpecificData;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 1);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 1);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v309(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 309);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 309);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 313);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 313);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 314);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 314);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 315);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 315);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 10003);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 10003);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 10023);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 10023);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v1027001(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 1027001);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 1027001);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v1027002(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 1027002);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 1027002);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 10028000);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 10028000);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 10029000);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 10029000);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 10032003);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 10032003);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 10035004);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 10035004);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 10035007);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 10035007);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 10036004);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 10036004);
            return this.roomSpecificData != null;
        }
        
        private boolean unserialize_v10037001(final ByteBuffer buffer) {
            this.layoutPosition = buffer.get();
            final int interactiveElements_size = buffer.getShort() & 0xFFFF;
            this.interactiveElements.clear();
            this.interactiveElements.ensureCapacity(interactiveElements_size);
            for (int i = 0; i < interactiveElements_size; ++i) {
                final ContainedInteractiveElement interactiveElements_element = new ContainedInteractiveElement();
                final boolean interactiveElements_element_ok = interactiveElements_element.unserializeVersion(buffer, 10037001);
                if (!interactiveElements_element_ok) {
                    return false;
                }
                this.interactiveElements.add(interactiveElements_element);
            }
            this.roomSpecificData = RawSpecificRooms.unserializeVirtualVersion(buffer, 10037001);
            return this.roomSpecificData != null;
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
