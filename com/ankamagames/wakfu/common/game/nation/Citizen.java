package com.ankamagames.wakfu.common.game.nation;

import org.jetbrains.annotations.*;

public interface Citizen
{
    @NotNull
    CitizenComportment getCitizenComportment();
    
    long getId();
    
    long getOwnerId();
    
    String getName();
    
    short getLevel();
    
    short getBreedId();
    
    byte getSex();
}
