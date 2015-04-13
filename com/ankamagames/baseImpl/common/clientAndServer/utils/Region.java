package com.ankamagames.baseImpl.common.clientAndServer.utils;

import java.util.*;
import com.google.common.collect.*;

public enum Region
{
    WESTERN("config.properties", false, new String[0]), 
    APAC("config-asia.properties", false, new String[] { "AF", "AU", "BH", "BD", "BT", "BN", "KH", "CK", "EG", "FJ", "IN", "ID", "IR", "IQ", "JP", "KR", "KW", "KG", "LA", "MY", "MN", "MM", "NP", "NZ", "OM", "PK", "PS", "PG", "PH", "QA", "WS", "SA", "SG", "SB", "LK", "SY", "TJ", "TH", "TK", "TO", "TM", "TV", "AE", "UZ", "VU", "VN", "YE" }), 
    CHINA("config.properties", true, new String[] { "CH" }), 
    TAIWAN("config-taiwan.properties", true, new String[] { "TW" });
    
    private final String m_configFile;
    private final boolean m_hidden;
    private final Set<String> m_countries;
    
    private Region(final String configFile, final boolean hidden, final String[] countries) {
        this.m_configFile = configFile;
        this.m_hidden = hidden;
        this.m_countries = (Set<String>)ImmutableSet.copyOf((Object[])countries);
    }
    
    public static Region getRegionFromCountryCode(final String counrtyCode) {
        for (final Region region : values()) {
            if (region.m_countries.contains(counrtyCode)) {
                return region;
            }
        }
        return Region.WESTERN;
    }
    
    public boolean isHidden() {
        return this.m_hidden;
    }
    
    public String getConfigFile() {
        return this.m_configFile;
    }
}
