package com.ankamagames.wakfu.common.game.havenWorld;

import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import gnu.trove.*;

public interface Building
{
    AbstractBuildingDefinition getDefinition();
    
    long getUid();
    
    long getCreationDate();
    
    short getX();
    
    short getY();
    
    void setX(short p0);
    
    void setY(short p0);
    
    boolean hasElement();
    
    BuildingElement getElement(long p0);
    
    boolean forEachElement(TObjectProcedure<BuildingElement> p0);
    
    int getEquippedItemId();
}
