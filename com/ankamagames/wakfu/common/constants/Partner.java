package com.ankamagames.wakfu.common.constants;

public enum Partner
{
    NONE("default", true, false, false), 
    STEAM("steam", true, true, false), 
    ASIA("asia", true, false, false), 
    SPEEDYBIRD("speedybird", false, false, false), 
    BIGPOINT("bigpoint", false, false, true), 
    LIKEVN("likevn", false, false, true);
    
    private final String m_name;
    private final boolean m_enableRegionSelection;
    private final boolean m_enableSteam;
    private final boolean m_useOAuth;
    private static Partner s_currentPartner;
    
    public static Partner getCurrentPartner() {
        return Partner.s_currentPartner;
    }
    
    private Partner(final String name, final boolean enableRegionSelection, final boolean enableSteam, final boolean useOAuth) {
        this.m_name = name;
        this.m_enableRegionSelection = enableRegionSelection;
        this.m_enableSteam = enableSteam;
        this.m_useOAuth = useOAuth;
    }
    
    public static void initializeCurrentPartner(final String name) {
        for (final Partner p : values()) {
            if (p.m_name.equals(name)) {
                Partner.s_currentPartner = p;
                return;
            }
        }
        Partner.s_currentPartner = Partner.NONE;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public boolean isEnableRegionSelection() {
        return this.m_enableRegionSelection;
    }
    
    public boolean isEnableSteam() {
        return this.m_enableSteam;
    }
    
    public boolean isUseOAuth() {
        return this.m_useOAuth;
    }
    
    static {
        Partner.s_currentPartner = Partner.NONE;
    }
}
