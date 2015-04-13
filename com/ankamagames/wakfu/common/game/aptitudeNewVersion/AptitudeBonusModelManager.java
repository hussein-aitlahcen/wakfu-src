package com.ankamagames.wakfu.common.game.aptitudeNewVersion;

import gnu.trove.*;

public final class AptitudeBonusModelManager
{
    public static final AptitudeBonusModelManager INSTANCE;
    private final TIntObjectHashMap<AptitudeBonusModel> m_bonusById;
    
    private AptitudeBonusModelManager() {
        super();
        this.m_bonusById = new TIntObjectHashMap<AptitudeBonusModel>();
    }
    
    public void add(final AptitudeBonusModel bonusModel) {
        this.put(bonusModel.getId(), bonusModel);
    }
    
    public AptitudeBonusModel put(final int key, final AptitudeBonusModel value) {
        return this.m_bonusById.put(key, value);
    }
    
    public <T extends AptitudeBonusModel> T get(final int key) {
        return (T)this.m_bonusById.get(key);
    }
    
    @Override
    public String toString() {
        return "AptitudeBonusModelManager{m_bonusById=" + this.m_bonusById + '}';
    }
    
    static {
        INSTANCE = new AptitudeBonusModelManager();
    }
}
