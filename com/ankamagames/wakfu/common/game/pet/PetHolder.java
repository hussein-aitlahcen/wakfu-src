package com.ankamagames.wakfu.common.game.pet;

import com.ankamagames.wakfu.common.game.pet.exception.*;

public interface PetHolder
{
    boolean hasPet();
    
    Pet getPet() throws PetException;
    
    long getUniqueId();
}
