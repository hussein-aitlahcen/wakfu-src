package com.ankamagames.wakfu.client.core.game.havenWorld;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.buff.*;

public class HavenWorldBuffFactory
{
    private final TIntObjectHashMap<HavenWorldBuff> m_buffs;
    public static final HavenWorldBuffFactory INSTANCE;
    
    public HavenWorldBuffFactory() {
        super();
        this.m_buffs = new TIntObjectHashMap<HavenWorldBuff>();
    }
    
    public HavenWorldBuff get(final int id) {
        HavenWorldBuff buff = this.m_buffs.get(id);
        if (buff == null) {
            buff = this.createBuff(id);
            if (buff != null) {
                this.m_buffs.put(id, buff);
            }
        }
        return buff;
    }
    
    private HavenWorldBuff createBuff(final int id) {
        final HavenWorldBuffDefinition definition = HavenWorldBuffConstants.INSTANCE.getObjectFromId(id);
        if (definition instanceof ModifyBuildDurationFactorDefinition) {
            return new ModifyBuildDurationFactors((ModifyBuildDurationFactorDefinition)definition);
        }
        if (definition instanceof PerceptionBonusDefinition) {
            return new PerceptionBonus((PerceptionBonusDefinition)definition);
        }
        if (definition instanceof ModifyBuildResourceCostFactorDefinition) {
            return new ModifyBuildResourceCostFactor((ModifyBuildResourceCostFactorDefinition)definition);
        }
        if (definition instanceof ModifyBuildKamaCostFactorDefinition) {
            return new ModifyBuildKamaCostFactor((ModifyBuildKamaCostFactorDefinition)definition);
        }
        return null;
    }
    
    static {
        INSTANCE = new HavenWorldBuffFactory();
    }
}
