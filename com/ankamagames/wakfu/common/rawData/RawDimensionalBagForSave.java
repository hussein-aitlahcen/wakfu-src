package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawDimensionalBagForSave implements VersionableObject
{
    public final ArrayList<Room> rooms;
    public int cash;
    public int customViewModelId;
    public final RawTransactionLog transactionLog;
    public final RawDimensionalBagPermissions permissions;
    public final RawDimensionalBagEcosystem ecosystem;
    public final RawDimensionalBagStorageBox storageBox;
    
    public RawDimensionalBagForSave() {
        super();
        this.rooms = new ArrayList<Room>(0);
        this.cash = 0;
        this.customViewModelId = 0;
        this.transactionLog = new RawTransactionLog();
        this.permissions = new RawDimensionalBagPermissions();
        this.ecosystem = new RawDimensionalBagEcosystem();
        this.storageBox = new RawDimensionalBagStorageBox();
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
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
        buffer.putInt(this.cash);
        buffer.putInt(this.customViewModelId);
        final boolean transactionLog_ok = this.transactionLog.serialize(buffer);
        if (!transactionLog_ok) {
            return false;
        }
        final boolean permissions_ok = this.permissions.serialize(buffer);
        if (!permissions_ok) {
            return false;
        }
        final boolean ecosystem_ok = this.ecosystem.serialize(buffer);
        if (!ecosystem_ok) {
            return false;
        }
        final boolean storageBox_ok = this.storageBox.serialize(buffer);
        return storageBox_ok;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
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
        this.cash = buffer.getInt();
        this.customViewModelId = buffer.getInt();
        final boolean transactionLog_ok = this.transactionLog.unserialize(buffer);
        if (!transactionLog_ok) {
            return false;
        }
        final boolean permissions_ok = this.permissions.unserialize(buffer);
        if (!permissions_ok) {
            return false;
        }
        final boolean ecosystem_ok = this.ecosystem.unserialize(buffer);
        if (!ecosystem_ok) {
            return false;
        }
        final boolean storageBox_ok = this.storageBox.unserialize(buffer);
        return storageBox_ok;
    }
    
    @Override
    public void clear() {
        this.rooms.clear();
        this.cash = 0;
        this.customViewModelId = 0;
        this.transactionLog.clear();
        this.permissions.clear();
        this.ecosystem.clear();
        this.storageBox.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10037002) {
            return this.unserialize(buffer);
        }
        final RawDimensionalBagForSaveConverter converter = new RawDimensionalBagForSaveConverter();
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
        for (int i = 0; i < this.rooms.size(); ++i) {
            final Room rooms_element = this.rooms.get(i);
            size += rooms_element.serializedSize();
        }
        size += 4;
        size += 4;
        size += this.transactionLog.serializedSize();
        size += this.permissions.serializedSize();
        size += this.ecosystem.serializedSize();
        size += this.storageBox.serializedSize();
        return size;
    }
    
    @Override
    public final String toString() {
        final StringBuilder repr = new StringBuilder();
        this.internalToString(repr, "");
        return repr.toString();
    }
    
    public final void internalToString(final StringBuilder repr, final String prefix) {
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
        repr.append(prefix).append("cash=").append(this.cash).append('\n');
        repr.append(prefix).append("customViewModelId=").append(this.customViewModelId).append('\n');
        repr.append(prefix).append("transactionLog=...\n");
        this.transactionLog.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("permissions=...\n");
        this.permissions.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("ecosystem=...\n");
        this.ecosystem.internalToString(repr, prefix + "  ");
        repr.append(prefix).append("storageBox=...\n");
        this.storageBox.internalToString(repr, prefix + "  ");
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
    
    private final class RawDimensionalBagForSaveConverter
    {
        private final ArrayList<Room> rooms;
        private int cash;
        private int customViewModelId;
        private final RawTransactionLog transactionLog;
        private final RawDimensionalBagPermissions permissions;
        private final RawDimensionalBagEcosystem ecosystem;
        private final RawDimensionalBagStorageBox storageBox;
        
        private RawDimensionalBagForSaveConverter() {
            super();
            this.rooms = new ArrayList<Room>(0);
            this.cash = 0;
            this.customViewModelId = 0;
            this.transactionLog = new RawTransactionLog();
            this.permissions = new RawDimensionalBagPermissions();
            this.ecosystem = new RawDimensionalBagEcosystem();
            this.storageBox = new RawDimensionalBagStorageBox();
        }
        
        public void pushResult() {
            RawDimensionalBagForSave.this.rooms.clear();
            RawDimensionalBagForSave.this.rooms.ensureCapacity(this.rooms.size());
            RawDimensionalBagForSave.this.rooms.addAll(this.rooms);
            RawDimensionalBagForSave.this.cash = this.cash;
            RawDimensionalBagForSave.this.customViewModelId = this.customViewModelId;
            RawDimensionalBagForSave.this.transactionLog.transactions.clear();
            RawDimensionalBagForSave.this.transactionLog.transactions.ensureCapacity(this.transactionLog.transactions.size());
            RawDimensionalBagForSave.this.transactionLog.transactions.addAll(this.transactionLog.transactions);
            RawDimensionalBagForSave.this.transactionLog.newTransactionsCount = this.transactionLog.newTransactionsCount;
            RawDimensionalBagForSave.this.transactionLog.newTransactionsKamas = this.transactionLog.newTransactionsKamas;
            RawDimensionalBagForSave.this.transactionLog.lastReadTransactionDate = this.transactionLog.lastReadTransactionDate;
            RawDimensionalBagForSave.this.permissions.dimensionalBagLocked = this.permissions.dimensionalBagLocked;
            RawDimensionalBagForSave.this.permissions.groupEntries.clear();
            RawDimensionalBagForSave.this.permissions.groupEntries.ensureCapacity(this.permissions.groupEntries.size());
            RawDimensionalBagForSave.this.permissions.groupEntries.addAll(this.permissions.groupEntries);
            RawDimensionalBagForSave.this.permissions.individualEntries.clear();
            RawDimensionalBagForSave.this.permissions.individualEntries.ensureCapacity(this.permissions.individualEntries.size());
            RawDimensionalBagForSave.this.permissions.individualEntries.addAll(this.permissions.individualEntries);
            RawDimensionalBagForSave.this.ecosystem.lastUpdateTime = this.ecosystem.lastUpdateTime;
            RawDimensionalBagForSave.this.ecosystem.resources.clear();
            RawDimensionalBagForSave.this.ecosystem.resources.ensureCapacity(this.ecosystem.resources.size());
            RawDimensionalBagForSave.this.ecosystem.resources.addAll(this.ecosystem.resources);
            RawDimensionalBagForSave.this.storageBox.rawStorageBox = this.storageBox.rawStorageBox;
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 1);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 1);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 1);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 1);
            return storageBox_ok;
        }
        
        private boolean unserialize_v309(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 309);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 309);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 309);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 309);
            return storageBox_ok;
        }
        
        private boolean unserialize_v313(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 313);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 313);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 313);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 313);
            return storageBox_ok;
        }
        
        private boolean unserialize_v314(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 314);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 314);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 314);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 314);
            return storageBox_ok;
        }
        
        private boolean unserialize_v315(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 315);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 315);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 315);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 315);
            return storageBox_ok;
        }
        
        private boolean unserialize_v10003(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 10003);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10003);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10003);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 10003);
            return storageBox_ok;
        }
        
        private boolean unserialize_v10014(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 10014);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10014);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10014);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 10014);
            return storageBox_ok;
        }
        
        private boolean unserialize_v10023(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 10023);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10023);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10023);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 10023);
            return storageBox_ok;
        }
        
        private boolean unserialize_v1027001(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 1027001);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 1027001);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 1027001);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 1027001);
            return storageBox_ok;
        }
        
        private boolean unserialize_v1027002(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 1027002);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 1027002);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 1027002);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 1027002);
            return storageBox_ok;
        }
        
        private boolean unserialize_v10028000(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 10028000);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10028000);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10028000);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 10028000);
            return storageBox_ok;
        }
        
        private boolean unserialize_v10029000(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 10029000);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10029000);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10029000);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 10029000);
            return storageBox_ok;
        }
        
        private boolean unserialize_v10032003(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 10032003);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10032003);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10032003);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 10032003);
            return storageBox_ok;
        }
        
        private boolean unserialize_v10035004(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 10035004);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10035004);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10035004);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 10035004);
            return storageBox_ok;
        }
        
        private boolean unserialize_v10035007(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 10035007);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10035007);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10035007);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 10035007);
            return storageBox_ok;
        }
        
        private boolean unserialize_v10036004(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 10036004);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10036004);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10036004);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 10036004);
            return storageBox_ok;
        }
        
        private boolean unserialize_v10037001(final ByteBuffer buffer) {
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
            this.cash = buffer.getInt();
            this.customViewModelId = buffer.getInt();
            final boolean transactionLog_ok = this.transactionLog.unserializeVersion(buffer, 10037001);
            if (!transactionLog_ok) {
                return false;
            }
            final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 10037001);
            if (!permissions_ok) {
                return false;
            }
            final boolean ecosystem_ok = this.ecosystem.unserializeVersion(buffer, 10037001);
            if (!ecosystem_ok) {
                return false;
            }
            final boolean storageBox_ok = this.storageBox.unserializeVersion(buffer, 10037001);
            return storageBox_ok;
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
        
        public void convert_v10003_to_v10014() {
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
                    this.convert_v10003_to_v10014();
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
                    this.convert_v10003_to_v10014();
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
                    this.convert_v10003_to_v10014();
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
                    this.convert_v10003_to_v10014();
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
                    this.convert_v10003_to_v10014();
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
                    this.convert_v10003_to_v10014();
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
                final boolean ok = this.unserialize_v10003(buffer);
                if (ok) {
                    this.convert_v10003_to_v10014();
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
