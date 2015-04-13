package com.ankamagames.wakfu.common.constants;

public enum ModerationSanction
{
    BAD_USAGE_SELL_CHAN(0, "invalid use of trade chan", ModerationSanctionType.WARNING), 
    BAD_USAGE_RECRUITMENT_CHAN(1, "invalid use of recruitment chan", ModerationSanctionType.WARNING), 
    RECRUITMENT(2, "recruitment flood", ModerationSanctionType.WARNING), 
    VULGARITY(3, "vulgarity", ModerationSanctionType.WARNING), 
    FLOOD(4, "flood", ModerationSanctionType.WARNING), 
    SCAM(5, "", ModerationSanctionType.WARNING), 
    BAD_LANGUAGE(6, "", ModerationSanctionType.WARNING), 
    USEBUG(7, "usebug", ModerationSanctionType.WARNING), 
    PRIVATE_SERVER(8, "", ModerationSanctionType.WARNING), 
    WEBSITE(9, "", ModerationSanctionType.WARNING), 
    BAD_SELLING(10, "membership or account trade", ModerationSanctionType.WARNING), 
    BAD_COMPORTMENT(11, "inappropriate behaviour", ModerationSanctionType.WARNING), 
    RECIDIVISM_ADVERTISE(12, "advertising (repeat offense)", ModerationSanctionType.MUTE, 60), 
    HARASSMENT(13, "harassment or threats", ModerationSanctionType.MUTE, 1440, 99), 
    RECIDIVISM_VULGARITY(14, "vulgarity (repeat offense)", ModerationSanctionType.MUTE, 60), 
    RECIDIVISM_SELLING(15, "trade flood (repeat offense)", ModerationSanctionType.MUTE, 15), 
    RECIDIVISM_RECRUITMENT(16, "recruitment flood (repeat offense)", ModerationSanctionType.MUTE, 15), 
    RECIDIVISM_BAD_USAGE_SELL_CHAN(17, "invalid use of trade chan (repeat offense)", ModerationSanctionType.MUTE, 15), 
    RECIDIVISM_BAD_USAGE_RECRUITMENT_CHAN(18, "invalid use of recruitment chan (repeat offense)", ModerationSanctionType.MUTE, 15), 
    CAPS_LOCK(19, "misuse of capital letters", ModerationSanctionType.MUTE, 15), 
    RECIDIVISM_FLOOD(20, "flood (repeat offense)", ModerationSanctionType.MUTE, 30), 
    SUR_RECIDIVISM_FLOOD(21, "flood (additional repeat offense)", ModerationSanctionType.MUTE, 60), 
    SUR_RECIDIVISM_BAD_USAGE_CHAN(22, "invalid use of chan (additional repeat offense)", ModerationSanctionType.MUTE, 60), 
    FLOOD_HARD(23, "flood hard", ModerationSanctionType.MUTE, 1440, 33), 
    OFF_CHARTER_PSEUDO(24, "inappropriate nickname", ModerationSanctionType.BAN, 0, 23), 
    VERY_BAD_SPEAKING(25, "racism or xenophobia or homophobia or paedophilia", ModerationSanctionType.BAN, 2880, 21), 
    BAD_SPEAKING(26, "insults", ModerationSanctionType.BAN, 1440, 19), 
    SEXUAL_SPEAKING(27, "content of a sexual nature", ModerationSanctionType.BAN, 1440, 120), 
    SEX_ADVERTISING(28, "sex website advert", ModerationSanctionType.BAN, 2880, 100), 
    PHISHING_ADVERTISING(29, "phishing website advert", ModerationSanctionType.BAN, 2880, 72), 
    DO_AS_PLAYER(30, "player identity theft", ModerationSanctionType.BAN, 2880, 13), 
    DO_AS_MODERATOR(31, "mod identity theft", ModerationSanctionType.BAN, 2880, 12), 
    ANONYMITY(32, "disrespect of anonymity", ModerationSanctionType.BAN, 1440, 86), 
    RECIDIVISM_SCAM(33, "scam", ModerationSanctionType.BAN, 1440, 26), 
    RECIDIVISM_USEBUG(34, "usebug (repeat offense)", ModerationSanctionType.BAN, 1440, 68), 
    RECIDIVISM_PRIVATE_SERVER(35, "private server", ModerationSanctionType.BAN, 1440, 139), 
    STOLEN_ACCOUNT(36, "stolen account protection", ModerationSanctionType.BAN, 2880, 71), 
    STEALING_ACCOUNT(37, "illegal access to a player's account", ModerationSanctionType.BAN, 2880, 48), 
    KAMAS_WEBSITE(38, "kama site", ModerationSanctionType.BAN, 2880, 105), 
    BOT(39, "farming bot", ModerationSanctionType.BAN, 2880, 123), 
    WHISP_BOT(40, "bot", ModerationSanctionType.BAN, 2880, 124), 
    SELLING(41, "trade flood", ModerationSanctionType.WARNING), 
    ILLEGAL_SELLING(42, "trade or kamas or guild or account subscription selling", ModerationSanctionType.BAN, 1440, 78), 
    VERY_BAD_COMPORTMENT(43, "inappropriate behaviour", ModerationSanctionType.BAN, 1440, 3), 
    VERY_BAD_USEBUG(44, "bug abuse", ModerationSanctionType.BAN, 2880, 119), 
    KAMAS_SELLING(45, "kama trading", ModerationSanctionType.WARNING), 
    WARNING_HARASSMENT(46, "harassment", ModerationSanctionType.WARNING), 
    WARNING_CAPS_LOCK(47, "misuse of capital letters", ModerationSanctionType.WARNING), 
    PVP_SELLING(48, "selling of pvp points", ModerationSanctionType.WARNING), 
    PVP_SELLING_RECIDIVISM(49, "selling of pvp points", ModerationSanctionType.BAN, 1440, 158);
    
    private final int m_id;
    private final String m_label;
    private final ModerationSanctionType m_sanctionType;
    private final int m_durationInMinutes;
    private final int m_banRequestId;
    
    private ModerationSanction(final int id, final String label, final ModerationSanctionType type) {
        this.m_id = id;
        this.m_label = label;
        this.m_sanctionType = type;
        this.m_durationInMinutes = 0;
        this.m_banRequestId = -1;
    }
    
    private ModerationSanction(final int id, final String label, final ModerationSanctionType type, final int duration) {
        this.m_id = id;
        this.m_label = label;
        this.m_sanctionType = type;
        this.m_durationInMinutes = duration;
        this.m_banRequestId = -1;
    }
    
    private ModerationSanction(final int id, final String label, final ModerationSanctionType sanctionType, final int durationInMinutes, final int banRequestId) {
        this.m_id = id;
        this.m_label = label;
        this.m_sanctionType = sanctionType;
        this.m_durationInMinutes = durationInMinutes;
        this.m_banRequestId = banRequestId;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getLabel() {
        return this.m_label;
    }
    
    public ModerationSanctionType getSanctionType() {
        return this.m_sanctionType;
    }
    
    public int getDurationInMinutes() {
        return this.m_durationInMinutes;
    }
    
    public int getBanRequestId() {
        return this.m_banRequestId;
    }
    
    public static ModerationSanction getFromId(final int id) {
        for (final ModerationSanction v : values()) {
            if (id == v.m_id) {
                return v;
            }
        }
        return null;
    }
    
    static final class Constants
    {
        public static final int ONE_HOUR = 60;
        private static final int ONE_DAY = 1440;
    }
}
