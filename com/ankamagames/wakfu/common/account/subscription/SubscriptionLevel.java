package com.ankamagames.wakfu.common.account.subscription;

import org.jetbrains.annotations.*;
import java.util.*;

public enum SubscriptionLevel
{
    UNKNOWN(0, SubscriptionRightsSet.NO_RIGHTS, 0, 0, 1.0f, 1.0f, 1.0f, 1.0f, 0, (short)(-1)), 
    EU_FREE(1, SubscriptionRightsSet.EU_FREE, 2, 1, 1.0f, 1.0f, 1.0f, 1.0f, 0, (short)(-1)), 
    EU_SUBSCRIBER(2, SubscriptionRightsSet.EU_SUBSCRIBER_RIGHTS, 2, 1, 1.0f, 1.0f, 1.0f, 1.0f, 0, (short)(-1)), 
    F2P_FREE(101, SubscriptionRightsSet.F2P_FREE, 104, 101, 0.7692f, 1.0f, 1.0f, 0.91f, 0, (short)(-1)), 
    F2P_PREMIUM1(102, SubscriptionRightsSet.F2P_SUBSCRIBER_RIGHTS, 104, 101, 0.8076f, 1.0f, 1.05f, 0.9282f, 0, (short)2749), 
    F2P_PREMIUM2(103, SubscriptionRightsSet.F2P_SUBSCRIBER_RIGHTS, 104, 101, 0.8846f, 1.0f, 1.15f, 0.9555f, 0, (short)2750), 
    F2P_PREMIUM3(104, SubscriptionRightsSet.F2P_SUBSCRIBER_RIGHTS, 104, 101, 1.0f, 1.0f, 1.3f, 1.0f, 0, (short)2751), 
    ZH_FREE(201, SubscriptionRightsSet.ZH_FREE, 205, 201, 0.6f, 1.0f, 1.0f, 1.0f, 0, (short)(-1)), 
    ZH_VIP1(202, SubscriptionRightsSet.ZH_VIP1, 205, 201, 0.7f, 1.0f, 1.0f, 1.0f, 0, (short)2742), 
    ZH_VIP2(203, SubscriptionRightsSet.ZH_VIP2, 205, 201, 0.8f, 1.05f, 1.0f, 1.0f, 0, (short)2743), 
    ZH_VIP3(204, SubscriptionRightsSet.ZH_VIP3, 205, 201, 0.9f, 1.1f, 1.0f, 1.0f, 0, (short)2744), 
    ZH_VIP4(205, SubscriptionRightsSet.ZH_VIP4, 205, 201, 1.0f, 1.15f, 1.0f, 1.0f, 0, (short)2745);
    
    public final int m_id;
    private final SubscriptionRightsSet m_rightsSet;
    private final int m_premiumLevel;
    private final int m_freeLevel;
    private final float m_xpRatio;
    private final float m_craftXpRatio;
    private final float m_kamasRatio;
    private final float m_dropRatio;
    private final int m_lootExtraRoll;
    private final short m_stateId;
    
    private SubscriptionLevel(final int id, final SubscriptionRightsSet rightsSet, final int premiumLevel, final int freeLevel, final float xpRatio, final float craftXpRatio, final float kamasRatio, final float dropRatio, final int lootExtraRoll, final short stateId) {
        this.m_id = id;
        this.m_rightsSet = rightsSet;
        this.m_premiumLevel = premiumLevel;
        this.m_freeLevel = freeLevel;
        this.m_xpRatio = xpRatio;
        this.m_craftXpRatio = craftXpRatio;
        this.m_kamasRatio = kamasRatio;
        this.m_dropRatio = dropRatio;
        this.m_lootExtraRoll = lootExtraRoll;
        this.m_stateId = stateId;
    }
    
    @NotNull
    public static SubscriptionLevel fromId(final int id) {
        for (final SubscriptionLevel level : values()) {
            if (level.m_id == id) {
                return level;
            }
        }
        return SubscriptionLevel.UNKNOWN;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean hasRight(final SubscriptionRight right) {
        return this.m_rightsSet.getRights().contains(right);
    }
    
    public EnumSet<SubscriptionRight> getRightsSet() {
        return this.m_rightsSet.getRights();
    }
    
    public int getPremiumLevel() {
        return this.m_premiumLevel;
    }
    
    public int getFreeLevel() {
        return this.m_freeLevel;
    }
    
    public float getCraftXpRatio() {
        return this.m_craftXpRatio;
    }
    
    public int getLootExtraRoll() {
        return this.m_lootExtraRoll;
    }
    
    public short getStateId() {
        return this.m_stateId;
    }
    
    public float getXpRatio() {
        return this.m_xpRatio;
    }
    
    public float getDropRatio() {
        return this.m_dropRatio;
    }
    
    public float getKamasRatio() {
        return this.m_kamasRatio;
    }
    
    @Override
    public String toString() {
        return "SubscriptionLevel{name=" + this.name() + ", m_id=" + this.m_id + ", m_rightsSet=" + this.m_rightsSet + ", m_premiumLevel=" + this.m_premiumLevel + ", m_freeLevel=" + this.m_freeLevel + ", m_craftXpRatio=" + this.m_craftXpRatio + ", m_lootExtraRoll=" + this.m_lootExtraRoll + ", m_stateId=" + this.m_stateId + ", m_xpRatio=" + this.m_xpRatio + ", m_dropRatio=" + this.m_dropRatio + ", m_kamasRatio=" + this.m_kamasRatio + '}';
    }
}
