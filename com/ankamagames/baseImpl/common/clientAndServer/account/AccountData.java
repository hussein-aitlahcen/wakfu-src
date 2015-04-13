package com.ankamagames.baseImpl.common.clientAndServer.account;

import java.nio.*;
import java.util.*;

public class AccountData
{
    private final EnumMap<AccountDataFlagType, AccountDataFlag> m_flags;
    
    public AccountData() {
        super();
        this.m_flags = new EnumMap<AccountDataFlagType, AccountDataFlag>(AccountDataFlagType.class);
    }
    
    public void addFlag(final AccountDataFlag flag) {
        this.m_flags.put(flag.getType(), flag);
    }
    
    public boolean hasFlag(final AccountDataFlagType flagType) {
        return this.m_flags.containsKey(flagType);
    }
    
    public AccountDataFlag getFlag(final AccountDataFlagType flagType) {
        return this.m_flags.get(flagType);
    }
    
    public int serializedSize() {
        return 2 + this.m_flags.size() * AccountDataFlag.serializedSize();
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.putShort((short)this.m_flags.size());
        for (final Map.Entry<AccountDataFlagType, AccountDataFlag> flag : this.m_flags.entrySet()) {
            buffer.put(flag.getKey().getId());
            buffer.putLong(flag.getValue().getValue());
        }
    }
    
    public void unserialize(final ByteBuffer buffer) {
        final short flagCount = buffer.getShort();
        for (int i = 0; i < flagCount; ++i) {
            final byte flagId = buffer.get();
            final long flagValue = buffer.getLong();
            final AccountDataFlagType flagType = AccountDataFlagType.fromId(flagId);
            if (flagType != null) {
                this.m_flags.put(flagType, new AccountDataFlag(flagType, flagValue));
            }
        }
    }
}
