package com.ankamagames.wakfu.common.game.nation.survey;

import gnu.trove.*;

public class RatingProcedure implements TByteIntProcedure
{
    private int m_cumulRating;
    private int m_totalVote;
    
    public RatingProcedure() {
        super();
        this.m_cumulRating = 0;
        this.m_totalVote = 0;
    }
    
    @Override
    public boolean execute(final byte a, final int b) {
        this.m_cumulRating += a * b;
        this.m_totalVote += b;
        return true;
    }
    
    public void clear() {
        this.m_cumulRating = 0;
        this.m_totalVote = 0;
    }
    
    public float getPopularityRate() {
        return (this.m_totalVote == 0) ? 0.0f : (this.m_cumulRating / this.m_totalVote);
    }
    
    public int getTotalVote() {
        return this.m_totalVote;
    }
}
