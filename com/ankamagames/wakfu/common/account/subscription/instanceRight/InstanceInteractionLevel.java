package com.ankamagames.wakfu.common.account.subscription.instanceRight;

public enum InstanceInteractionLevel
{
    NONE(0), 
    FULL_ACCESS(1), 
    RESTRICTED_ACCESS(2), 
    FORBIDDEN_ACCESS(3);
    
    private final int m_id;
    
    private InstanceInteractionLevel(final int id) {
        this.m_id = id;
    }
    
    public static InstanceInteractionLevel getFromId(final int id) {
        final InstanceInteractionLevel[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final InstanceInteractionLevel value = values[i];
            if (value.m_id == id) {
                return value;
            }
        }
        return InstanceInteractionLevel.NONE;
    }
}
