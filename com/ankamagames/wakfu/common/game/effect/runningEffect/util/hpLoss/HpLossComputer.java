package com.ankamagames.wakfu.common.game.effect.runningEffect.util.hpLoss;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.effect.*;

public interface HpLossComputer
{
    public static final int DMG_BOOST = 1;
    public static final int RESISTANCE = 2;
    public static final int DMG_REBOUND = 4;
    public static final int DMG_ABSORB = 8;
    public static final int FINAL_MOD = 16;
    
    int getValue();
    
    void setConditions(int p0);
    
    void setAffectedByLocalisation(boolean p0);
    
    void setValue(int p0);
    
    void setWeaponUsed(Item p0);
    
    void setIsCritical(boolean p0);
    
    void computeWithModificator();
    
    void setCustomResistPercentBonus(int p0);
    
    void setCustomDmgPercentBonus(int p0);
    
    Elements getElementForResistance();
}
