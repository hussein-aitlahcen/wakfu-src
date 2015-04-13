package com.ankamagames.wakfu.common.game.characterInfo;

public interface BasicOccupation
{
    short getOccupationTypeId();
    
    boolean isAllowed();
    
    void begin();
    
    boolean cancel();
    
    boolean finish();
}
