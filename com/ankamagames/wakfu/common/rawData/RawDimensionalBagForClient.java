package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;

public class RawDimensionalBagForClient implements VersionableObject
{
    public long ownerId;
    public String ownerName;
    public long guildId;
    public final ArrayList<Room> rooms;
    public int customViewModelId;
    public Wallet wallet;
    public final RawDimensionalBagPermissions permissions;
    
    public RawDimensionalBagForClient() {
        super();
        this.ownerId = 0L;
        this.ownerName = null;
        this.guildId = 0L;
        this.rooms = new ArrayList<Room>(0);
        this.customViewModelId = 0;
        this.wallet = null;
        this.permissions = new RawDimensionalBagPermissions();
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.putLong(this.ownerId);
        if (this.ownerName != null) {
            final byte[] serialized_ownerName = StringUtils.toUTF8(this.ownerName);
            if (serialized_ownerName.length > 65535) {
                return false;
            }
            buffer.putShort((short)serialized_ownerName.length);
            buffer.put(serialized_ownerName);
        }
        else {
            buffer.putShort((short)0);
        }
        buffer.putLong(this.guildId);
        if (this.rooms.size() > 255) {
            return false;
        }
        buffer.put((byte)this.rooms.size());
        for (int i = 0; i < this.rooms.size(); ++i) {
            final Room rooms_element = this.rooms.get(i);
            final boolean rooms_element_ok = rooms_element.serialize(buffer);
            if (!rooms_element_ok) {
                return false;
            }
        }
        buffer.putInt(this.customViewModelId);
        if (this.wallet != null) {
            buffer.put((byte)1);
            final boolean wallet_ok = this.wallet.serialize(buffer);
            if (!wallet_ok) {
                return false;
            }
        }
        else {
            buffer.put((byte)0);
        }
        final boolean permissions_ok = this.permissions.serialize(buffer);
        return permissions_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.ownerId = buffer.getLong();
        final int ownerName_size = buffer.getShort() & 0xFFFF;
        final byte[] serialized_ownerName = new byte[ownerName_size];
        buffer.get(serialized_ownerName);
        this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
        this.guildId = buffer.getLong();
        final int rooms_size = buffer.get() & 0xFF;
        this.rooms.clear();
        this.rooms.ensureCapacity(rooms_size);
        for (int i = 0; i < rooms_size; ++i) {
            final Room rooms_element = new Room();
            final boolean rooms_element_ok = rooms_element.unserialize(buffer);
            if (!rooms_element_ok) {
                return false;
            }
            this.rooms.add(rooms_element);
        }
        this.customViewModelId = buffer.getInt();
        final boolean wallet_present = buffer.get() == 1;
        if (wallet_present) {
            this.wallet = new Wallet();
            final boolean wallet_ok = this.wallet.unserialize(buffer);
            if (!wallet_ok) {
                return false;
            }
        }
        else {
            this.wallet = null;
        }
        final boolean permissions_ok = this.permissions.unserialize(buffer);
        return permissions_ok;
    }
    
    @Override
    public void clear() {
        this.ownerId = 0L;
        this.ownerName = null;
        this.guildId = 0L;
        this.rooms.clear();
        this.customViewModelId = 0;
        this.wallet = null;
        this.permissions.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037002) {
            return this.unserialize(buffer);
        }
        final RawDimensionalBagForClientConverter converter = new RawDimensionalBagForClientConverter();
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
        size += 2;
        size += ((this.ownerName != null) ? StringUtils.toUTF8(this.ownerName).length : 0);
        size += 8;
        ++size;
        for (int i = 0; i < this.rooms.size(); ++i) {
            final Room rooms_element = this.rooms.get(i);
            size += rooms_element.serializedSize();
        }
        size += 4;
        ++size;
        if (this.wallet != null) {
            size += this.wallet.serializedSize();
        }
        size += this.permissions.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
        repr.append(prefix).append("ownerId=").append(this.ownerId).append('\n');
        repr.append(prefix).append("ownerName=").append(this.ownerName).append('\n');
        repr.append(prefix).append("guildId=").append(this.guildId).append('\n');
        repr.append(prefix).append("rooms=");
        if (this.rooms.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.rooms.size()).append(" elements)...\n");
            for (int i = 0; i < this.rooms.size(); ++i) {
                final Room rooms_element = this.rooms.get(i);
                rooms_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("customViewModelId=").append(this.customViewModelId).append('\n');
        repr.append(prefix).append("wallet=");
        if (this.wallet == null) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("...\n");
            this.wallet.internalToString(repr, prefix + "  ");
        }
        repr.append(prefix).append("permissions=...\n");
        this.permissions.internalToString(repr, prefix + "  ");
    }
    
    public static class Room implements VersionableObject
    {
        public final RawRoom room;
        
        public Room() {
            super();
            this.room = new RawRoom();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean room_ok = this.room.serialize(buffer);
            return room_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean room_ok = this.room.unserialize(buffer);
            return room_ok;
        }
        
        @Override
        public void clear() {
            this.room.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10037002) {
                return this.unserialize(buffer);
            }
            final RoomConverter converter = new RoomConverter();
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
            size += this.room.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("room=...\n");
            this.room.internalToString(repr, prefix + "  ");
        }
        
        private final class RoomConverter
        {
            private final RawRoom room;
            
            private RoomConverter() {
                super();
                this.room = new RawRoom();
            }
            
            public void pushResult() {
                Room.this.room.layoutPosition = this.room.layoutPosition;
                Room.this.room.interactiveElements.clear();
                Room.this.room.interactiveElements.ensureCapacity(this.room.interactiveElements.size());
                Room.this.room.interactiveElements.addAll(this.room.interactiveElements);
                Room.this.room.roomSpecificData = this.room.roomSpecificData;
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                return true;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 1);
                return room_ok;
            }
            
            private boolean unserialize_v309(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 309);
                return room_ok;
            }
            
            private boolean unserialize_v313(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 313);
                return room_ok;
            }
            
            private boolean unserialize_v314(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 314);
                return room_ok;
            }
            
            private boolean unserialize_v315(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 315);
                return room_ok;
            }
            
            private boolean unserialize_v10003(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 10003);
                return room_ok;
            }
            
            private boolean unserialize_v10023(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 10023);
                return room_ok;
            }
            
            private boolean unserialize_v1027001(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 1027001);
                return room_ok;
            }
            
            private boolean unserialize_v1027002(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 1027002);
                return room_ok;
            }
            
            private boolean unserialize_v10028000(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 10028000);
                return room_ok;
            }
            
            private boolean unserialize_v10029000(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 10029000);
                return room_ok;
            }
            
            private boolean unserialize_v10032003(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 10032003);
                return room_ok;
            }
            
            private boolean unserialize_v10035004(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 10035004);
                return room_ok;
            }
            
            private boolean unserialize_v10035007(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 10035007);
                return room_ok;
            }
            
            private boolean unserialize_v10036004(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 10036004);
                return room_ok;
            }
            
            private boolean unserialize_v10037001(final ByteBuffer buffer) {
                final boolean room_ok = this.room.unserializeVersion(buffer, 10037001);
                return room_ok;
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
    
    public static class Wallet implements VersionableObject
    {
        public int cash;
        public static final int SERIALIZED_SIZE = 4;
        
        public Wallet() {
            super();
            this.cash = 0;
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            buffer.putInt(this.cash);
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            this.cash = buffer.getInt();
            return true;
        }
        
        @Override
        public void clear() {
            this.cash = 0;
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
            repr.append(prefix).append("cash=").append(this.cash).append('\n');
        }
    }
    
    private final class RawDimensionalBagForClientConverter
    {
        private long ownerId;
        private String ownerName;
        private long guildId;
        private final ArrayList<Room> rooms;
        private int customViewModelId;
        private Wallet wallet;
        private final RawDimensionalBagPermissions permissions;
        
        private RawDimensionalBagForClientConverter() {
            super();
            this.ownerId = 0L;
            this.ownerName = null;
            this.guildId = 0L;
            this.rooms = new ArrayList<Room>(0);
            this.customViewModelId = 0;
            this.wallet = null;
            this.permissions = new RawDimensionalBagPermissions();
        }
        
        public void pushResult() {
            RawDimensionalBagForClient.this.ownerId = this.ownerId;
            RawDimensionalBagForClient.this.ownerName = this.ownerName;
            RawDimensionalBagForClient.this.guildId = this.guildId;
            RawDimensionalBagForClient.this.rooms.clear();
            RawDimensionalBagForClient.this.rooms.ensureCapacity(this.rooms.size());
            RawDimensionalBagForClient.this.rooms.addAll(this.rooms);
            RawDimensionalBagForClient.this.customViewModelId = this.customViewModelId;
            RawDimensionalBagForClient.this.wallet = this.wallet;
            RawDimensionalBagForClient.this.permissions.dimensionalBagLocked = this.permissions.dimensionalBagLocked;
            RawDimensionalBagForClient.this.permissions.groupEntries.clear();
            RawDimensionalBagForClient.this.permissions.groupEntries.ensureCapacity(this.permissions.groupEntries.size());
            RawDimensionalBagForClient.this.permissions.groupEntries.addAll(this.permissions.groupEntries);
            RawDimensionalBagForClient.this.permissions.individualEntries.clear();
            RawDimensionalBagForClient.this.permissions.individualEntries.ensureCapacity(this.permissions.individualEntries.size());
            RawDimensionalBagForClient.this.permissions.individualEntries.addAll(this.permissions.individualEntries);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 1);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 1);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 1);
            return permissions_ok;
        }
        
        private boolean unserialize_v309(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 309);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 309);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 309);
            return permissions_ok;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 313);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 313);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 313);
            return permissions_ok;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 314);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 314);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 314);
            return permissions_ok;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 315);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 315);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 315);
            return permissions_ok;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 10003);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10003);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10003);
            return permissions_ok;
        }
        
        private boolean unserialize_v10005(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 10005);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10005);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10005);
            return permissions_ok;
        }
        
        private boolean unserialize_v10014(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 10014);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10014);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10014);
            return permissions_ok;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 10023);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10023);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10023);
            return permissions_ok;
        }
        
        private boolean unserialize_v1027001(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 1027001);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 1027001);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 1027001);
            return permissions_ok;
        }
        
        private boolean unserialize_v1027002(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 1027002);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 1027002);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 1027002);
            return permissions_ok;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 10028000);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10028000);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10028000);
            return permissions_ok;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 10029000);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10029000);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10029000);
            return permissions_ok;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 10032003);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10032003);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10032003);
            return permissions_ok;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 10035004);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10035004);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10035004);
            return permissions_ok;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 10035007);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10035007);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10035007);
            return permissions_ok;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 10036004);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10036004);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10036004);
            return permissions_ok;
        }
        
        private boolean unserialize_v10037001(final ByteBuffer buffer) {
            this.ownerId = buffer.getLong();
            final int ownerName_size = buffer.getShort() & 0xFFFF;
            final byte[] serialized_ownerName = new byte[ownerName_size];
            buffer.get(serialized_ownerName);
            this.ownerName = StringUtils.fromUTF8(serialized_ownerName);
            this.guildId = buffer.getLong();
            final int rooms_size = buffer.get() & 0xFF;
            this.rooms.clear();
            this.rooms.ensureCapacity(rooms_size);
            for (int i = 0; i < rooms_size; ++i) {
                final Room rooms_element = new Room();
                final boolean rooms_element_ok = rooms_element.unserializeVersion(buffer, 10037001);
                if (!rooms_element_ok) {
                    return false;
                }
                this.rooms.add(rooms_element);
            }
            this.customViewModelId = buffer.getInt();
            final boolean wallet_present = buffer.get() == 1;
            if (wallet_present) {
                this.wallet = new Wallet();
                final boolean wallet_ok = this.wallet.unserializeVersion(buffer, 10037001);
                if (!wallet_ok) {
                    return false;
                }
            }
            else {
                this.wallet = null;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10037001);
            return permissions_ok;
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
        
        public void convert_v10003_to_v10005() {
        }
        
        public void convert_v10005_to_v10014() {
        }
        
        public void convert_v10014_to_v10023() {
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
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
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
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
            else if (version < 10005) {
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10005();
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
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
            else if (version < 10014) {
                final boolean ok = this.unserialize_v10005(buffer);
                if (ok) {
                    this.convert_v10005_to_v10014();
                    this.convert_v10014_to_v10023();
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
                final boolean ok = this.unserialize_v10014(buffer);
                if (ok) {
                    this.convert_v10014_to_v10023();
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
