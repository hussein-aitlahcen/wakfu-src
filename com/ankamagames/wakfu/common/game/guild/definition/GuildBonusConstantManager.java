package com.ankamagames.wakfu.common.game.guild.definition;

public class GuildBonusConstantManager
{
    private double m_learningDurationFactor;
    private double m_durationFactor;
    private int m_maxSimultaneous;
    private double m_bonusPointEarnedFactor;
    private boolean m_canChangeNation;
    
    public GuildBonusConstantManager() {
        super();
        this.m_learningDurationFactor = 1.0;
        this.m_durationFactor = 1.0;
        this.m_maxSimultaneous = 1;
        this.m_bonusPointEarnedFactor = 1.0;
    }
    
    public double getLearningDurationFactor() {
        return this.m_learningDurationFactor;
    }
    
    public void setLearningDurationFactor(final double learningDurationFactor) {
        this.m_learningDurationFactor = learningDurationFactor;
    }
    
    public int getMaxSimultaneous() {
        return this.m_maxSimultaneous;
    }
    
    public void setMaxSimultaneous(final int maxSimultaneous) {
        this.m_maxSimultaneous = maxSimultaneous;
    }
    
    public double getDurationFactor() {
        return this.m_durationFactor;
    }
    
    public double getBonusPointEarnedFactor() {
        return this.m_bonusPointEarnedFactor;
    }
    
    public void setBonusPointEarnedFactor(final double bonusPointEarnedFactor) {
        this.m_bonusPointEarnedFactor = bonusPointEarnedFactor;
    }
    
    public boolean canChangeNation() {
        return this.m_canChangeNation;
    }
    
    public void setCanChangeNation(final boolean canChangeNation) {
        this.m_canChangeNation = canChangeNation;
    }
}
