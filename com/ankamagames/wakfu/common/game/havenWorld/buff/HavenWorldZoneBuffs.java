package com.ankamagames.wakfu.common.game.havenWorld.buff;

import com.ankamagames.framework.kernel.core.maths.*;

public final class HavenWorldZoneBuffs
{
    private int m_buildKamaCostFactor;
    private int m_buildResourceCostFactor;
    private int m_buildDurationPercentModificator;
    private int m_adminBuildDurationPercentModificator;
    private int m_perceptionRate;
    
    public void modifyBuildDurationFactor(final int updateValue) {
        this.m_buildDurationPercentModificator += updateValue;
    }
    
    public void setAdminBuildDurationPercentModificator(final int adminBuildDurationPercentModificator) {
        this.m_adminBuildDurationPercentModificator = adminBuildDurationPercentModificator;
    }
    
    public void modifyKamaCostFactor(final int updateValue) {
        this.m_buildKamaCostFactor += updateValue;
    }
    
    public void modifyResourceCostFactor(final int updateValue) {
        this.m_buildResourceCostFactor += updateValue;
    }
    
    public int getBuildDurationPercentModificator() {
        return MathHelper.max(-100, this.m_buildDurationPercentModificator + this.m_adminBuildDurationPercentModificator, new int[0]);
    }
    
    public int getAdminBuildDurationPercentModificator() {
        return this.m_adminBuildDurationPercentModificator;
    }
    
    public int getBuildKamaCostFactor() {
        return this.m_buildKamaCostFactor;
    }
    
    public int getBuildResourceCostFactor() {
        return this.m_buildResourceCostFactor;
    }
    
    public void setPerceptionRate(final int updateValue) {
        this.m_perceptionRate = updateValue;
    }
    
    public int getPerceptionRate() {
        return this.m_perceptionRate;
    }
}
