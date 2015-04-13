package com.ankamagames.wakfu.common.game.dungeon.ranks;

public class RankScore
{
    private final int m_scoreMinA;
    private final int m_scoreMinB;
    private final int m_scoreMinC;
    private final int m_scoreMinD;
    private final boolean m_skipChallengeRatio;
    
    public RankScore(final int scoreMinA, final int scoreMinB, final int scoreMinC, final int scoreMinD, final boolean skipChallengeRatio) {
        super();
        this.m_scoreMinA = scoreMinA;
        this.m_scoreMinB = scoreMinB;
        this.m_scoreMinC = scoreMinC;
        this.m_scoreMinD = scoreMinD;
        this.m_skipChallengeRatio = skipChallengeRatio;
    }
    
    public Rank getRank(final ScoreProvider scoreProvider) {
        final int score = scoreProvider.getMonsterScore() + scoreProvider.getChallengeScore();
        if (score >= this.m_scoreMinA && (this.m_skipChallengeRatio || scoreProvider.getChallengeScore() >= score * 0.36f)) {
            return Rank.A;
        }
        if (score >= this.m_scoreMinB && (this.m_skipChallengeRatio || scoreProvider.getChallengeScore() >= score * 0.24f)) {
            return Rank.B;
        }
        if (score >= this.m_scoreMinC && (this.m_skipChallengeRatio || scoreProvider.getChallengeScore() >= score * 0.12f)) {
            return Rank.C;
        }
        if (score >= this.m_scoreMinD) {
            return Rank.D;
        }
        return Rank.None;
    }
}
