package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import java.util.*;

public class RawDimensionalBagPermissions implements VersionableObject
{
    public boolean dimensionalBagLocked;
    public final ArrayList<GroupEntry> groupEntries;
    public final ArrayList<IndividualEntry> individualEntries;
    
    public RawDimensionalBagPermissions() {
        super();
        this.dimensionalBagLocked = false;
        this.groupEntries = new ArrayList<GroupEntry>(0);
        this.individualEntries = new ArrayList<IndividualEntry>(0);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        buffer.put((byte)(this.dimensionalBagLocked ? 1 : 0));
        if (this.groupEntries.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.groupEntries.size());
        for (int i = 0; i < this.groupEntries.size(); ++i) {
            final GroupEntry groupEntries_element = this.groupEntries.get(i);
            final boolean groupEntries_element_ok = groupEntries_element.serialize(buffer);
            if (!groupEntries_element_ok) {
                return false;
            }
        }
        if (this.individualEntries.size() > 65535) {
            return false;
        }
        buffer.putShort((short)this.individualEntries.size());
        for (int i = 0; i < this.individualEntries.size(); ++i) {
            final IndividualEntry individualEntries_element = this.individualEntries.get(i);
            final boolean individualEntries_element_ok = individualEntries_element.serialize(buffer);
            if (!individualEntries_element_ok) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        this.dimensionalBagLocked = (buffer.get() == 1);
        final int groupEntries_size = buffer.getShort() & 0xFFFF;
        this.groupEntries.clear();
        this.groupEntries.ensureCapacity(groupEntries_size);
        for (int i = 0; i < groupEntries_size; ++i) {
            final GroupEntry groupEntries_element = new GroupEntry();
            final boolean groupEntries_element_ok = groupEntries_element.unserialize(buffer);
            if (!groupEntries_element_ok) {
                return false;
            }
            this.groupEntries.add(groupEntries_element);
        }
        final int individualEntries_size = buffer.getShort() & 0xFFFF;
        this.individualEntries.clear();
        this.individualEntries.ensureCapacity(individualEntries_size);
        for (int j = 0; j < individualEntries_size; ++j) {
            final IndividualEntry individualEntries_element = new IndividualEntry();
            final boolean individualEntries_element_ok = individualEntries_element.unserialize(buffer);
            if (!individualEntries_element_ok) {
                return false;
            }
            this.individualEntries.add(individualEntries_element);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.dimensionalBagLocked = false;
        this.groupEntries.clear();
        this.individualEntries.clear();
    }
    
    @Override
    public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
        if (version >= 10014) {
            return this.unserialize(buffer);
        }
        final RawDimensionalBagPermissionsConverter converter = new RawDimensionalBagPermissionsConverter();
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
        for (int i = 0; i < this.groupEntries.size(); ++i) {
            final GroupEntry groupEntries_element = this.groupEntries.get(i);
            size += groupEntries_element.serializedSize();
        }
        size += 2;
        for (int i = 0; i < this.individualEntries.size(); ++i) {
            final IndividualEntry individualEntries_element = this.individualEntries.get(i);
            size += individualEntries_element.serializedSize();
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
        repr.append(prefix).append("dimensionalBagLocked=").append(this.dimensionalBagLocked).append('\n');
        repr.append(prefix).append("groupEntries=");
        if (this.groupEntries.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.groupEntries.size()).append(" elements)...\n");
            for (int i = 0; i < this.groupEntries.size(); ++i) {
                final GroupEntry groupEntries_element = this.groupEntries.get(i);
                groupEntries_element.internalToString(repr, prefix + i + "/ ");
            }
        }
        repr.append(prefix).append("individualEntries=");
        if (this.individualEntries.isEmpty()) {
            repr.append("{}").append('\n');
        }
        else {
            repr.append("(").append(this.individualEntries.size()).append(" elements)...\n");
            for (int i = 0; i < this.individualEntries.size(); ++i) {
                final IndividualEntry individualEntries_element = this.individualEntries.get(i);
                individualEntries_element.internalToString(repr, prefix + i + "/ ");
            }
        }
    }
    
    public static class RoomPermissions implements VersionableObject
    {
        public static final int SERIALIZED_SIZE = 0;
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            return true;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            return true;
        }
        
        @Override
        public void clear() {
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version >= 10014) {
                return this.unserialize(buffer);
            }
            final RoomPermissionsConverter converter = new RoomPermissionsConverter();
            final boolean ok = converter.unserializeVersion(buffer, version);
            if (ok) {
                converter.pushResult();
                return true;
            }
            return false;
        }
        
        @Override
        public int serializedSize() {
            return 0;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
        }
        
        private final class RoomPermissionsConverter
        {
            private int roomTypeId;
            private final RawRoomPermissions permissions;
            
            private RoomPermissionsConverter() {
                super();
                this.roomTypeId = 0;
                this.permissions = new RawRoomPermissions();
            }
            
            public void pushResult() {
            }
            
            private boolean unserialize_v0(final ByteBuffer buffer) {
                return true;
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                this.roomTypeId = buffer.getInt();
                final boolean permissions_ok = this.permissions.unserializeVersion(buffer, 1);
                return permissions_ok;
            }
            
            public void convert_v0_to_v1() {
            }
            
            public void convert_v1_to_v10014() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 0) {
                    return false;
                }
                if (version < 1) {
                    final boolean ok = this.unserialize_v0(buffer);
                    if (ok) {
                        this.convert_v0_to_v1();
                        this.convert_v1_to_v10014();
                        return true;
                    }
                    return false;
                }
                else {
                    if (version >= 10014) {
                        return false;
                    }
                    final boolean ok = this.unserialize_v1(buffer);
                    if (ok) {
                        this.convert_v1_to_v10014();
                        return true;
                    }
                    return false;
                }
            }
        }
    }
    
    public static class GroupEntry implements VersionableObject
    {
        public final RawDimensionalBagPermissionGroupEntry entry;
        
        public GroupEntry() {
            super();
            this.entry = new RawDimensionalBagPermissionGroupEntry();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean entry_ok = this.entry.serialize(buffer);
            return entry_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean entry_ok = this.entry.unserialize(buffer);
            return entry_ok;
        }
        
        @Override
        public void clear() {
            this.entry.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.entry.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("entry=...\n");
            this.entry.internalToString(repr, prefix + "  ");
        }
    }
    
    public static class IndividualEntry implements VersionableObject
    {
        public final RawDimensionalBagPermissionIndividualEntry entry;
        
        public IndividualEntry() {
            super();
            this.entry = new RawDimensionalBagPermissionIndividualEntry();
        }
        
        @Override
        public boolean serialize(final ByteBuffer buffer) {
            final boolean entry_ok = this.entry.serialize(buffer);
            return entry_ok;
        }
        
        @Override
        public boolean unserialize(final ByteBuffer buffer) {
            final boolean entry_ok = this.entry.unserialize(buffer);
            return entry_ok;
        }
        
        @Override
        public void clear() {
            this.entry.clear();
        }
        
        @Override
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            return this.unserialize(buffer);
        }
        
        @Override
        public int serializedSize() {
            int size = 0;
            size += this.entry.serializedSize();
            return size;
        }
        
        @Override
        public final String toString() {
            final StringBuilder repr = new StringBuilder();
            this.internalToString(repr, "");
            return repr.toString();
        }
        
        public final void internalToString(final StringBuilder repr, final String prefix) {
            repr.append(prefix).append("entry=...\n");
            this.entry.internalToString(repr, prefix + "  ");
        }
    }
    
    private final class RawDimensionalBagPermissionsConverter
    {
        private final ArrayList<RoomPermissions> roomPermissions;
        private boolean dimensionalBagLocked;
        private final ArrayList<GroupEntry> groupEntries;
        private final ArrayList<IndividualEntry> individualEntries;
        
        private RawDimensionalBagPermissionsConverter() {
            super();
            this.roomPermissions = new ArrayList<RoomPermissions>(0);
            this.dimensionalBagLocked = false;
            this.groupEntries = new ArrayList<GroupEntry>(0);
            this.individualEntries = new ArrayList<IndividualEntry>(0);
        }
        
        public void pushResult() {
            RawDimensionalBagPermissions.this.dimensionalBagLocked = this.dimensionalBagLocked;
            RawDimensionalBagPermissions.this.groupEntries.clear();
            RawDimensionalBagPermissions.this.groupEntries.ensureCapacity(this.groupEntries.size());
            RawDimensionalBagPermissions.this.groupEntries.addAll(this.groupEntries);
            RawDimensionalBagPermissions.this.individualEntries.clear();
            RawDimensionalBagPermissions.this.individualEntries.ensureCapacity(this.individualEntries.size());
            RawDimensionalBagPermissions.this.individualEntries.addAll(this.individualEntries);
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int roomPermissions_size = buffer.getShort() & 0xFFFF;
            this.roomPermissions.clear();
            this.roomPermissions.ensureCapacity(roomPermissions_size);
            for (int i = 0; i < roomPermissions_size; ++i) {
                final RoomPermissions roomPermissions_element = new RoomPermissions();
                final boolean roomPermissions_element_ok = roomPermissions_element.unserializeVersion(buffer, 1);
                if (!roomPermissions_element_ok) {
                    return false;
                }
                this.roomPermissions.add(roomPermissions_element);
            }
            this.dimensionalBagLocked = (buffer.get() == 1);
            return true;
        }
        
        public void convert_v0_to_v1() {
        }
        
        public void convert_v1_to_v10014() {
        }
        
        public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
            if (version < 0) {
                return false;
            }
            if (version < 1) {
                final boolean ok = this.unserialize_v0(buffer);
                if (ok) {
                    this.convert_v0_to_v1();
                    this.convert_v1_to_v10014();
                    return true;
                }
                return false;
            }
            else {
                if (version >= 10014) {
                    return false;
                }
                final boolean ok = this.unserialize_v1(buffer);
                if (ok) {
                    this.convert_v1_to_v10014();
                    return true;
                }
                return false;
            }
        }
    }
}
