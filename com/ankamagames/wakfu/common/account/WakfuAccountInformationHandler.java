package com.ankamagames.wakfu.common.account;

import com.ankamagames.baseImpl.common.clientAndServer.account.*;
import com.ankamagames.wakfu.common.account.antiAddiction.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.common.datas.*;
import org.apache.commons.lang3.*;
import java.util.*;

public class WakfuAccountInformationHandler implements AccountInformationHandler
{
    private static final EnumSet<SubscriptionLevel> SEA_SUBSCRIBER_LEVELS;
    public static final EnumSet<SubscriptionLevel> ZH_SUBSCRIBER_LEVELS;
    private int[] m_adminRights;
    private byte m_additionalSlots;
    private byte m_vaultUpgrades;
    private boolean m_debuggingHackMode;
    private long m_sessionStartTime;
    private SubscriptionLevel m_subscriptionLevel;
    private SubscriptionLevel m_forcedSubscriptionLevel;
    private AntiAddictionLevel m_antiAddictionLevel;
    private EnumSet<SubscriptionRight> m_additionalRights;
    private WakfuAccountInformationListener m_listener;
    
    public WakfuAccountInformationHandler() {
        super();
        this.m_forcedSubscriptionLevel = SubscriptionLevel.UNKNOWN;
        this.m_additionalRights = EnumSet.noneOf(SubscriptionRight.class);
    }
    
    public void setListener(final WakfuAccountInformationListener listener) {
        this.m_listener = listener;
    }
    
    public void setAdminRights(final int... rights) {
        this.m_adminRights = rights.clone();
    }
    
    public boolean hasAdminRight(final AdminRightsEnum right) {
        return this.hasAdminRight(right.getId());
    }
    
    public boolean hasAdminRight(final short right) {
        return AdminRightHelper.checkRight(this.m_adminRights, right);
    }
    
    public int[] getAdminRights() {
        return this.m_adminRights.clone();
    }
    
    public byte getAdditionalSlots() {
        return this.m_additionalSlots;
    }
    
    public void setAdditionalSlots(final byte numAdditionalSlots) {
        this.m_additionalSlots = numAdditionalSlots;
    }
    
    public byte getVaultUpgrades() {
        return this.m_vaultUpgrades;
    }
    
    public void setVaultUpgrades(final byte vaultUpgrades) {
        this.m_vaultUpgrades = vaultUpgrades;
    }
    
    public void setSubscriptionLevel(final SubscriptionLevel subscriptionLevel) {
        if (this.m_subscriptionLevel != subscriptionLevel) {
            final SubscriptionLevel previousActiveLevel = this.getActiveSubscriptionLevel();
            this.m_subscriptionLevel = subscriptionLevel;
            if (this.m_listener != null) {
                this.m_listener.onSubscriptionRightChanged(previousActiveLevel, this.getActiveSubscriptionLevel());
            }
        }
    }
    
    public void setForcedSubscriptionLevel(final SubscriptionLevel forcedSubscriptionLevel) {
        this.m_forcedSubscriptionLevel = forcedSubscriptionLevel;
    }
    
    public SubscriptionLevel getActiveSubscriptionLevel() {
        if (this.m_forcedSubscriptionLevel != SubscriptionLevel.UNKNOWN) {
            return this.m_forcedSubscriptionLevel;
        }
        return this.m_subscriptionLevel;
    }
    
    public SubscriptionLevel getSubscriptionLevel() {
        return this.m_subscriptionLevel;
    }
    
    public SubscriptionLevel getForcedSubscriptionLevel() {
        return this.m_forcedSubscriptionLevel;
    }
    
    public boolean hasSubscriptionLevel(final SubscriptionLevel level) {
        return (level == SubscriptionLevel.EU_SUBSCRIBER && WakfuAccountInformationHandler.SEA_SUBSCRIBER_LEVELS.contains(this.getActiveSubscriptionLevel())) || (level == SubscriptionLevel.EU_SUBSCRIBER && WakfuAccountInformationHandler.ZH_SUBSCRIBER_LEVELS.contains(this.getActiveSubscriptionLevel())) || this.getActiveSubscriptionLevel() == level;
    }
    
    public boolean hasRight(final SubscriptionRight right) {
        final boolean hasRightInSubscription = this.getActiveSubscriptionLevel() != null && this.getActiveSubscriptionLevel().hasRight(right);
        final boolean hasRightInAdditional = this.m_additionalRights.contains(right);
        return hasRightInSubscription || hasRightInAdditional;
    }
    
    public boolean addSubscriptionRight(final SubscriptionRight right) {
        final boolean ok = this.m_additionalRights.add(right);
        if (ok && this.m_listener != null) {
            this.m_listener.onSubscriptionRightChanged(this.getActiveSubscriptionLevel(), this.getActiveSubscriptionLevel());
        }
        return ok;
    }
    
    public boolean removeSubscriptionRight(final SubscriptionRight right) {
        final boolean ok = this.m_additionalRights.remove(right);
        if (ok && this.m_listener != null) {
            this.m_listener.onSubscriptionRightChanged(this.getActiveSubscriptionLevel(), this.getActiveSubscriptionLevel());
        }
        return ok;
    }
    
    public EnumSet<SubscriptionRight> getAdditionalRights() {
        return this.m_additionalRights;
    }
    
    public float getCraftXpRatio() {
        if (this.getActiveSubscriptionLevel() == null) {
            return 1.0f;
        }
        return this.getActiveSubscriptionLevel().getCraftXpRatio();
    }
    
    public int getLootExtraRoll() {
        if (this.getActiveSubscriptionLevel() == null) {
            return 0;
        }
        return this.getActiveSubscriptionLevel().getLootExtraRoll();
    }
    
    public long getSessionStartTime() {
        return this.m_sessionStartTime;
    }
    
    public void setSessionStartTime(final long sessionStartTime) {
        this.m_sessionStartTime = sessionStartTime;
    }
    
    public AntiAddictionLevel getAntiAddictionLevel() {
        return this.m_antiAddictionLevel;
    }
    
    public void setAntiAddictionLevel(final AntiAddictionLevel antiAddictionLevel) {
        this.m_antiAddictionLevel = antiAddictionLevel;
    }
    
    public boolean isAntiAddictionEnabled() {
        return this.m_antiAddictionLevel == AntiAddictionLevel.ACTIVATED || this.m_antiAddictionLevel == AntiAddictionLevel.UNKNOWN;
    }
    
    public void setDebuggingHackMode(final boolean debuggingHackMode) {
        this.m_debuggingHackMode = debuggingHackMode;
    }
    
    public boolean isDebuggingHackMode() {
        return this.m_debuggingHackMode;
    }
    
    public void build(final CharacterSerializedAccountInformation part) {
        part.adminRights = this.m_adminRights;
        part.subscriptionLevel = this.m_subscriptionLevel.m_id;
        part.forcedSubscriptionLevel = this.m_forcedSubscriptionLevel.m_id;
        part.antiAddictionLevel = this.m_antiAddictionLevel.m_id;
        part.additionalSlots = this.m_additionalSlots;
        part.vaultUpgrades = this.m_vaultUpgrades;
        part.additionalRights = new int[this.m_additionalRights.size()];
        part.sessionStartTime = this.m_sessionStartTime;
        int i = 0;
        for (final SubscriptionRight additionalRight : this.m_additionalRights) {
            part.additionalRights[i] = additionalRight.id;
            ++i;
        }
    }
    
    public void fromBuild(final CharacterSerializedAccountInformation part) {
        final SubscriptionLevel previousActiveLevel = this.getActiveSubscriptionLevel();
        this.m_adminRights = part.adminRights;
        this.m_subscriptionLevel = SubscriptionLevel.fromId(part.subscriptionLevel);
        this.m_forcedSubscriptionLevel = SubscriptionLevel.fromId(part.forcedSubscriptionLevel);
        this.m_antiAddictionLevel = AntiAddictionLevel.fromId(part.antiAddictionLevel);
        this.m_additionalSlots = part.additionalSlots;
        this.m_vaultUpgrades = part.vaultUpgrades;
        this.m_additionalRights.clear();
        this.m_sessionStartTime = part.sessionStartTime;
        if (part.additionalRights != null) {
            for (int i = 0; i < part.additionalRights.length; ++i) {
                final int additionalRight = part.additionalRights[i];
                this.m_additionalRights.add(SubscriptionRight.fromId(additionalRight));
            }
        }
        if (this.m_listener != null) {
            this.m_listener.onSubscriptionRightChanged(previousActiveLevel, this.getActiveSubscriptionLevel());
        }
    }
    
    public void clear() {
        this.m_adminRights = ArrayUtils.EMPTY_INT_ARRAY;
        this.m_subscriptionLevel = SubscriptionLevel.UNKNOWN;
        this.m_forcedSubscriptionLevel = SubscriptionLevel.UNKNOWN;
        this.m_antiAddictionLevel = AntiAddictionLevel.UNKNOWN;
        this.m_additionalSlots = 0;
        this.m_vaultUpgrades = 0;
        this.m_debuggingHackMode = false;
        this.m_additionalRights.clear();
    }
    
    public void copyFrom(final WakfuAccountInformationHandler handler) {
        this.m_adminRights = handler.m_adminRights;
        this.m_subscriptionLevel = handler.m_subscriptionLevel;
        this.m_forcedSubscriptionLevel = handler.m_forcedSubscriptionLevel;
        this.m_antiAddictionLevel = handler.m_antiAddictionLevel;
        this.m_additionalSlots = handler.m_additionalSlots;
        this.m_vaultUpgrades = handler.m_vaultUpgrades;
        this.m_debuggingHackMode = handler.m_debuggingHackMode;
        this.m_additionalRights = handler.m_additionalRights;
    }
    
    @Override
    public String toString() {
        return "WakfuAccountInformationHandler{m_adminRights=" + Arrays.toString(this.m_adminRights) + ", m_debuggingHackMode=" + this.m_debuggingHackMode + ", m_subscriptionLevel=" + this.m_subscriptionLevel + ", m_antiAddictionLevel=" + this.m_antiAddictionLevel + ", m_additionalSlots" + this.m_additionalSlots + ", m_additionalRights=" + this.m_additionalRights + '}';
    }
    
    static {
        SEA_SUBSCRIBER_LEVELS = EnumSet.of(SubscriptionLevel.F2P_FREE, SubscriptionLevel.F2P_PREMIUM1, SubscriptionLevel.F2P_PREMIUM2, SubscriptionLevel.F2P_PREMIUM3);
        ZH_SUBSCRIBER_LEVELS = EnumSet.of(SubscriptionLevel.ZH_VIP1, SubscriptionLevel.ZH_VIP2, SubscriptionLevel.ZH_VIP3, SubscriptionLevel.ZH_VIP4);
    }
}
