package com.ankamagames.wakfu.common.datas.Breed;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.movement.*;

public interface Breed
{
    short getBreedId();
    
    int getFamilyId();
    
    int getBaseCharacteristicValue(FighterCharacteristicType p0);
    
    float getRatio(BreedRatios p0);
    
    int getBaseTimerCountBeforeDeath();
    
    AbstractBattlegroundBorderEffectArea getBattlegroundBorderEffectArea();
    
    int[] getBaseFightProperties();
    
    int[] getBaseWorldProperties();
    
    byte getHeight();
    
    byte getPhysicalRadius();
    
    int getMaxWalkDistance();
    
    int getMaxFightWalkDistance();
    
    MovementSpeed getWalkTimeBetweenCells();
    
    MovementSpeed getRunTimeBetweenCells();
    
    int getDefeatScriptId();
    
    int getLeveledCharacteristic(int p0, FighterCharacteristicType p1);
}
