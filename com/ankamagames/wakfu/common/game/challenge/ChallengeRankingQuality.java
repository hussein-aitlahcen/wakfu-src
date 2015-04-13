package com.ankamagames.wakfu.common.game.challenge;

public enum ChallengeRankingQuality
{
    INVALID((byte)(-1)), 
    TOP((byte)1), 
    VERY_GOOD((byte)2), 
    MEDIUM((byte)3), 
    POOR((byte)4);
    
    private byte m_id;
    
    private ChallengeRankingQuality(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static ChallengeRankingQuality getById(final byte id) {
        for (final ChallengeRankingQuality value : values()) {
            if (value.m_id == id) {
                return value;
            }
        }
        return ChallengeRankingQuality.INVALID;
    }
}
