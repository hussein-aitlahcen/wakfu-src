package com.ankamagames.wakfu.common.game.pvp.fight;

import com.ankamagames.wakfu.common.game.pvp.*;
import org.jetbrains.annotations.*;

public final class PlayerReportInfo
{
    private int m_previousStrength;
    private int m_currentStrength;
    private int m_previousRanking;
    private int m_currentRanking;
    private NationPvpRanks m_previousRank;
    private NationPvpRanks m_currentRank;
    private int m_streak;
    
    public void setPreviousStrength(final int previousStrength) {
        this.m_previousStrength = previousStrength;
    }
    
    public void setCurrentStrength(final int currentStrength) {
        this.m_currentStrength = currentStrength;
    }
    
    public void setCurrentRanking(final int currentRanking) {
        this.m_currentRanking = currentRanking;
    }
    
    public void setCurrentRank(final NationPvpRanks currentRank) {
        this.m_currentRank = currentRank;
    }
    
    public void setPreviousRanking(final int previousRanking) {
        this.m_previousRanking = previousRanking;
    }
    
    public void setPreviousRank(final NationPvpRanks previousRank) {
        this.m_previousRank = previousRank;
    }
    
    public void setStreak(final int streak) {
        this.m_streak = streak;
    }
    
    public int getPreviousStrength() {
        return this.m_previousStrength;
    }
    
    public int getCurrentStrength() {
        return this.m_currentStrength;
    }
    
    public int getPreviousRanking() {
        return this.m_previousRanking;
    }
    
    public int getCurrentRanking() {
        return this.m_currentRanking;
    }
    
    public NationPvpRanks getPreviousRank() {
        return this.m_previousRank;
    }
    
    @Nullable
    public NationPvpRanks getCurrentRank() {
        return this.m_currentRank;
    }
    
    public int getStreak() {
        return this.m_streak;
    }
    
    @Override
    public String toString() {
        return "PlayerReportInfo{m_previousStrength=" + this.m_previousStrength + ", m_currentStrength=" + this.m_currentStrength + ", m_previousRanking=" + this.m_previousRanking + ", m_currentRanking=" + this.m_currentRanking + ", m_previousRank=" + this.m_previousRank + ", m_currentRank=" + this.m_currentRank + '}';
    }
}
