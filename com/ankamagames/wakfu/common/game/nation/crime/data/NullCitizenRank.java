package com.ankamagames.wakfu.common.game.nation.crime.data;

final class NullCitizenRank implements CitizenRank
{
    @Override
    public String getTranslationKey() {
        return "";
    }
    
    @Override
    public int getCap() {
        return 0;
    }
    
    @Override
    public String getColor() {
        return "";
    }
    
    @Override
    public int getPdcLossFactor() {
        return 0;
    }
    
    @Override
    public boolean hasRule(final CitizenRankRule rule) {
        return false;
    }
    
    @Override
    public int getId() {
        return -1;
    }
}
