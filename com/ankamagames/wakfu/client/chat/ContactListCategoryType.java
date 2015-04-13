package com.ankamagames.wakfu.client.chat;

import com.ankamagames.wakfu.client.core.*;

public enum ContactListCategoryType
{
    FRIEND((short)1), 
    IGNORE((short)2), 
    PARTY((short)4), 
    GUILD((short)8);
    
    private short m_id;
    
    private ContactListCategoryType(final short id) {
        this.m_id = id;
    }
    
    public String getName() {
        switch (this) {
            case FRIEND: {
                return WakfuTranslator.getInstance().getString("chat.friendList");
            }
            case IGNORE: {
                return WakfuTranslator.getInstance().getString("chat.ignoreList");
            }
            default: {
                return this.name();
            }
        }
    }
    
    public short getId() {
        return this.m_id;
    }
    
    public static ContactListCategoryType getById(final short id) {
        for (final ContactListCategoryType val : values()) {
            if (val.getId() == id) {
                return val;
            }
        }
        return null;
    }
}
