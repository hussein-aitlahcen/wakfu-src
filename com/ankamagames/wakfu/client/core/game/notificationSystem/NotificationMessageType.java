package com.ankamagames.wakfu.client.core.game.notificationSystem;

public enum NotificationMessageType
{
    CHARACTER((byte)0, "singleCharacterIcon"), 
    CRAFT((byte)1, "optionsIcon"), 
    CITIZEN((byte)2, "LaurelIcon"), 
    OUTLAW((byte)3, "LaurelIcon"), 
    NATION((byte)4, "balanceIcon", 5000, 0), 
    SOCIAL((byte)5, "contactsIcon"), 
    DUNGEON_LADDER((byte)6, "DongeonIcon"), 
    PROTECTOR_CHALLENGE((byte)7, "ChallengeIcon"), 
    PROTECTOR_WEATHER((byte)8, "WeatherIcon"), 
    TUTORIAL((byte)9, "questionMark", 15000, 0), 
    ACHIEVEMENTS((byte)10, "ChallengeIcon"), 
    QUEST_SUCCESS((byte)11, "ChallengeIcon", 5000, 1), 
    QUEST_FAILURE((byte)12, "ChallengeIcon", 5000, 2), 
    PVP((byte)13, "passportPvpCategoryIcon");
    
    private byte m_id;
    private String m_iconStyle;
    private int m_duration;
    private int m_type;
    
    private NotificationMessageType(final byte id, final String iconStyle) {
        this(id, iconStyle, 5000, 0);
    }
    
    private NotificationMessageType(final byte id, final String iconStyle, final int duration, final int type) {
        this.m_id = id;
        this.m_iconStyle = iconStyle;
        this.m_duration = duration;
        this.m_type = type;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public String getIconStyle() {
        return this.m_iconStyle;
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public int getType() {
        return this.m_type;
    }
}
