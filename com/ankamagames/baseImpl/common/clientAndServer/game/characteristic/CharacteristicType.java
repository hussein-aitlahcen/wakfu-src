package com.ankamagames.baseImpl.common.clientAndServer.game.characteristic;

public interface CharacteristicType
{
    public static final int NO_OUT_FIGHT_MAX = -1;
    
    byte getId();
    
    byte getCharacteristicType();
    
    int getLowerBound();
    
    int getUpperBound();
    
    int getDefaultMin();
    
    int getDefaultMax();
    
    int getDefaultValue();
    
    boolean isExpandable();
    
    boolean isNegative();
    
    int getOutFightMax();
}
