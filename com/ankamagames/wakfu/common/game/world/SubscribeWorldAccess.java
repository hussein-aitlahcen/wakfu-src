package com.ankamagames.wakfu.common.game.world;

public enum SubscribeWorldAccess
{
    NON_ABO_FREE_ACCESS((byte)1), 
    NON_ABO_LIMITED_ACCESS((byte)2), 
    NON_ABO_LOCKED_ACCESS((byte)4);
    
    public static final SubscribeWorldAccess DEFAULT;
    private final byte m_id;
    
    private SubscribeWorldAccess(final byte id) {
        this.m_id = id;
    }
    
    public static SubscribeWorldAccess from(final String stringValue) {
        if (stringValue.equals("Zone libre")) {
            return SubscribeWorldAccess.NON_ABO_FREE_ACCESS;
        }
        if (stringValue.equals("Zone limit\u00e9e")) {
            return SubscribeWorldAccess.NON_ABO_LIMITED_ACCESS;
        }
        if (stringValue.equals("Zone bloqu\u00e9e")) {
            return SubscribeWorldAccess.NON_ABO_LOCKED_ACCESS;
        }
        return SubscribeWorldAccess.DEFAULT;
    }
    
    public static SubscribeWorldAccess from(final byte id) {
        for (final SubscribeWorldAccess s : values()) {
            if (s.m_id == id) {
                return s;
            }
        }
        return SubscribeWorldAccess.DEFAULT;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    static {
        DEFAULT = SubscribeWorldAccess.NON_ABO_FREE_ACCESS;
    }
}
