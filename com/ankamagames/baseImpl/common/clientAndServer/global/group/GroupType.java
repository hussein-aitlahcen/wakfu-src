package com.ankamagames.baseImpl.common.clientAndServer.global.group;

public enum GroupType
{
    NO_GROUP, 
    PARTY;
    
    public final byte getId() {
        return (byte)this.ordinal();
    }
    
    public static GroupType getFromTypeId(final byte id) {
        if (id < 0) {
            return GroupType.NO_GROUP;
        }
        final GroupType[] values = values();
        if (id >= values.length) {
            return GroupType.NO_GROUP;
        }
        return values[id];
    }
    
    public static GroupType getFromGroupId(final long id) {
        final byte groupTypeId = (byte)(id >>> 56);
        return getFromTypeId(groupTypeId);
    }
    
    public static GroupType getFromString(final String groupName) throws IllegalArgumentException {
        if (groupName == null) {
            return GroupType.NO_GROUP;
        }
        return valueOf(groupName.toUpperCase());
    }
}
