package com.ankamagames.wakfu.common.game.vault;

import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;

public enum VaultUpgrade
{
    VAULT_BASE(1, 10, 0), 
    VAULT_UPGRADE_1(2, 15, 1), 
    VAULT_UPGRADE_2(3, 20, 2), 
    VAULT_UPGRADE_3(4, 25, 3), 
    VAULT_UPGRADE_4(5, 30, 4);
    
    private static final VaultUpgrade MAX_UPGRADE;
    private final byte m_id;
    private final byte m_size;
    private final byte m_numUpgrade;
    
    private VaultUpgrade(final int id, final int size, final int numUpgrade) {
        this.m_id = MathHelper.ensureByte(id);
        this.m_size = MathHelper.ensureByte(size);
        this.m_numUpgrade = MathHelper.ensureByte(numUpgrade);
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public byte getSize() {
        return this.m_size;
    }
    
    public byte getNumUpgrade() {
        return this.m_numUpgrade;
    }
    
    @Nullable
    public VaultUpgrade getNextUpgrade() {
        switch (this) {
            case VAULT_BASE: {
                return VaultUpgrade.VAULT_UPGRADE_1;
            }
            case VAULT_UPGRADE_1: {
                return VaultUpgrade.VAULT_UPGRADE_2;
            }
            case VAULT_UPGRADE_2: {
                return VaultUpgrade.VAULT_UPGRADE_3;
            }
            case VAULT_UPGRADE_3: {
                return VaultUpgrade.VAULT_UPGRADE_4;
            }
            default: {
                return null;
            }
        }
    }
    
    @Nullable
    public static VaultUpgrade getFromId(final byte id) {
        for (final VaultUpgrade up : values()) {
            if (up.m_id == id) {
                return up;
            }
        }
        return null;
    }
    
    @Nullable
    public static VaultUpgrade getFromSize(final byte size) {
        for (final VaultUpgrade up : values()) {
            if (up.m_size == size) {
                return up;
            }
        }
        return null;
    }
    
    public static VaultUpgrade getFromUpgradeNumber(final byte upgrade) {
        for (final VaultUpgrade up : values()) {
            if (up.m_numUpgrade == upgrade) {
                return up;
            }
        }
        if (upgrade > 0) {
            return VaultUpgrade.MAX_UPGRADE;
        }
        return null;
    }
    
    static {
        VaultUpgrade max = null;
        for (final VaultUpgrade upgrade : values()) {
            if (max == null || max.m_numUpgrade < upgrade.m_numUpgrade) {
                max = upgrade;
            }
        }
        MAX_UPGRADE = max;
    }
}
