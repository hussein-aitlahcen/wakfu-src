package com.ankamagames.baseImpl.common.clientAndServer.account;

import gnu.trove.*;
import java.util.*;

public enum AccountDataFlagType
{
    STEAMER_BETA((byte)1, "WKSTEAMER"), 
    ANTI_ADDICTION((byte)2, "ANTIADDICT"), 
    CHARACTER_SLOTS((byte)3, "WKCHARACTERS"), 
    VAULT_UPGRADE((byte)4, "WKVAULTUP");
    
    private static final TByteObjectHashMap<AccountDataFlagType> m_flagsById;
    private static final HashMap<String, AccountDataFlagType> m_flagsByWebRepresentation;
    private final byte m_id;
    private final String m_webRepresentation;
    
    private AccountDataFlagType(final byte id, final String webRepresentation) {
        this.m_id = id;
        this.m_webRepresentation = webRepresentation;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public String getWebRepresentation() {
        return this.m_webRepresentation;
    }
    
    public static AccountDataFlagType fromId(final byte id) {
        return AccountDataFlagType.m_flagsById.get(id);
    }
    
    public static AccountDataFlagType fromWebRepresentation(final String webRepresentation) {
        return AccountDataFlagType.m_flagsByWebRepresentation.get(webRepresentation);
    }
    
    static {
        m_flagsById = new TByteObjectHashMap<AccountDataFlagType>();
        m_flagsByWebRepresentation = new HashMap<String, AccountDataFlagType>();
        for (final AccountDataFlagType flag : values()) {
            AccountDataFlagType.m_flagsById.put(flag.getId(), flag);
            AccountDataFlagType.m_flagsByWebRepresentation.put(flag.getWebRepresentation(), flag);
        }
    }
}
