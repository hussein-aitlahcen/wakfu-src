package com.ankamagames.baseImpl.common.clientAndServer.account;

public class AccountDataFlag
{
    private final AccountDataFlagType m_type;
    private final long m_value;
    
    public AccountDataFlag(final AccountDataFlagType type, final long value) {
        super();
        this.m_type = type;
        this.m_value = value;
    }
    
    public AccountDataFlagType getType() {
        return this.m_type;
    }
    
    public long getValue() {
        return this.m_value;
    }
    
    public static int serializedSize() {
        return 9;
    }
}
