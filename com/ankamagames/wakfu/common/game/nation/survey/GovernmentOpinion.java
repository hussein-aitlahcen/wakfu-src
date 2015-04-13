package com.ankamagames.wakfu.common.game.nation.survey;

public enum GovernmentOpinion
{
    UNLIKE(-1), 
    UNKNOWN(0), 
    LIKE(1);
    
    public final byte idx;
    
    private GovernmentOpinion(final int i) {
        this.idx = (byte)i;
    }
    
    public static GovernmentOpinion fromId(final byte idx) {
        final GovernmentOpinion[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final GovernmentOpinion value = values[i];
            if (value.idx == idx) {
                return value;
            }
        }
        return null;
    }
}
