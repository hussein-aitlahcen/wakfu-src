package com.ankamagames.wakfu.common.game.pet;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public interface PetModelListener
{
    void nameChanged(String p0);
    
    void colorItemChanged(int p0);
    
    void equippedItemChanged(int p0);
    
    void healthChanged(int p0);
    
    void xpChanged(int p0);
    
    void lastMealDateChanged(GameDateConst p0);
    
    void lastHungryDateChanged(GameDateConst p0);
    
    void sleepDateChanged(GameDateConst p0);
    
    void sleepItemChanged(int p0);
}
