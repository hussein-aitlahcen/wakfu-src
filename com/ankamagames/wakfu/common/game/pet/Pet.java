package com.ankamagames.wakfu.common.game.pet;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.logs.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public interface Pet extends RawConvertible<RawPet>, LoggableEntity
{
    PetDefinition getDefinition();
    
    @Nullable
    String getName();
    
    int getColorItemRefId();
    
    PetDefinitionColor getColorDefinition();
    
    int getEquippedRefItemId();
    
    int getHealth();
    
    int getXp();
    
    short getLevel();
    
    short getMaxLevel();
    
    GameDateConst getLastMealDate();
    
    GameDateConst getLastHungryDate();
    
    int getSleepRefItemId();
    
    GameDateConst getSleepDate();
    
    boolean addListener(PetModelListener p0);
    
    boolean removeListener(PetModelListener p0);
    
    boolean isSleeping();
    
    boolean isActive();
}
