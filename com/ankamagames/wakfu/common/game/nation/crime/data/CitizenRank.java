package com.ankamagames.wakfu.common.game.nation.crime.data;

public interface CitizenRank
{
    String getTranslationKey();
    
    int getCap();
    
    String getColor();
    
    int getPdcLossFactor();
    
    boolean hasRule(CitizenRankRule p0);
    
    int getId();
}
