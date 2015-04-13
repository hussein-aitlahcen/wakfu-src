package com.ankamagames.wakfu.common.rawData;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;

public class RawRoomPermissions implements VersionableObject
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
        final RawRoomPermissionsConverter converter = new RawRoomPermissionsConverter();
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
    
    public static class GroupPermissions implements VersionableObject
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
            final GroupPermissionsConverter converter = new GroupPermissionsConverter();
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
        
        private final class GroupPermissionsConverter
        {
            private int groupId;
            private boolean granted;
            
            private GroupPermissionsConverter() {
                super();
                this.groupId = 0;
                this.granted = false;
            }
            
            public void pushResult() {
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                this.groupId = buffer.getInt();
                this.granted = (buffer.get() == 1);
                return true;
            }
            
            public void convert_v1_to_v10014() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
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
    
    public static class IndividualPermissions implements VersionableObject
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
            final IndividualPermissionsConverter converter = new IndividualPermissionsConverter();
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
        
        private final class IndividualPermissionsConverter
        {
            private long userId;
            private String userName;
            private boolean granted;
            
            private IndividualPermissionsConverter() {
                super();
                this.userId = 0L;
                this.userName = null;
                this.granted = false;
            }
            
            public void pushResult() {
            }
            
            private boolean unserialize_v1(final ByteBuffer buffer) {
                this.userId = buffer.getLong();
                final int userName_size = buffer.getShort() & 0xFFFF;
                final byte[] serialized_userName = new byte[userName_size];
                buffer.get(serialized_userName);
                this.userName = StringUtils.fromUTF8(serialized_userName);
                this.granted = (buffer.get() == 1);
                return true;
            }
            
            public void convert_v1_to_v10014() {
            }
            
            public boolean unserializeVersion(final ByteBuffer buffer, final int version) {
                if (version < 1) {
                    return false;
                }
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
    
    private final class RawRoomPermissionsConverter
    {
        private final ArrayList<GroupPermissions> groupPermissions;
        private final ArrayList<IndividualPermissions> individualPermissions;
        
        private RawRoomPermissionsConverter() {
            super();
            this.groupPermissions = new ArrayList<GroupPermissions>(0);
            this.individualPermissions = new ArrayList<IndividualPermissions>(0);
        }
        
        public void pushResult() {
        }
        
        private boolean unserialize_v0(final ByteBuffer buffer) {
            final int groupPermissions_size = buffer.getShort() & 0xFFFF;
            this.groupPermissions.clear();
            this.groupPermissions.ensureCapacity(groupPermissions_size);
            for (int i = 0; i < groupPermissions_size; ++i) {
                final GroupPermissions groupPermissions_element = new GroupPermissions();
                final boolean groupPermissions_element_ok = groupPermissions_element.unserializeVersion(buffer, 0);
                if (!groupPermissions_element_ok) {
                    return false;
                }
                this.groupPermissions.add(groupPermissions_element);
            }
            final int individualPermissions_size = buffer.getShort() & 0xFFFF;
            this.individualPermissions.clear();
            this.individualPermissions.ensureCapacity(individualPermissions_size);
            for (int j = 0; j < individualPermissions_size; ++j) {
                final IndividualPermissions individualPermissions_element = new IndividualPermissions();
                final boolean individualPermissions_element_ok = individualPermissions_element.unserializeVersion(buffer, 0);
                if (!individualPermissions_element_ok) {
                    return false;
                }
                this.individualPermissions.add(individualPermissions_element);
            }
            return true;
        }
        
        private boolean unserialize_v1(final ByteBuffer buffer) {
            final int groupPermissions_size = buffer.getShort() & 0xFFFF;
            this.groupPermissions.clear();
            this.groupPermissions.ensureCapacity(groupPermissions_size);
            for (int i = 0; i < groupPermissions_size; ++i) {
                final GroupPermissions groupPermissions_element = new GroupPermissions();
                final boolean groupPermissions_element_ok = groupPermissions_element.unserializeVersion(buffer, 1);
                if (!groupPermissions_element_ok) {
                    return false;
                }
                this.groupPermissions.add(groupPermissions_element);
            }
            final int individualPermissions_size = buffer.getShort() & 0xFFFF;
            this.individualPermissions.clear();
            this.individualPermissions.ensureCapacity(individualPermissions_size);
            for (int j = 0; j < individualPermissions_size; ++j) {
                final IndividualPermissions individualPermissions_element = new IndividualPermissions();
                final boolean individualPermissions_element_ok = individualPermissions_element.unserializeVersion(buffer, 1);
                if (!individualPermissions_element_ok) {
                    return false;
                }
                this.individualPermissions.add(individualPermissions_element);
            }
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
