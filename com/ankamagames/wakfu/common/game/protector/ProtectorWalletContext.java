package com.ankamagames.wakfu.common.game.protector;

public enum ProtectorWalletContext
{
    CHALLENGE(0), 
    CLIMATE(1), 
    ECOSYSTEM(2), 
    BUFF(3);
    
    public final byte idx;
    
    private ProtectorWalletContext(final int idx) {
        this.idx = (byte)idx;
    }
    
    public static ProtectorWalletContext fromId(final byte idx) {
        final ProtectorWalletContext[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final ProtectorWalletContext value = values[i];
            if (value.idx == idx) {
                return value;
            }
        }
        return null;
    }
}
