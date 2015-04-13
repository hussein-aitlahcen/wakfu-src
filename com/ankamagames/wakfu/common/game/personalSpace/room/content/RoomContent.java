package com.ankamagames.wakfu.common.game.personalSpace.room.content;

import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.common.game.personalSpace.*;

public interface RoomContent
{
    void toRawPersistantData(RawInteractiveElementPersistantData p0);
    
    boolean fromRawPersistantData(RawInteractiveElementPersistantData p0);
    
    int getWorldCellX();
    
    int getWorldCellY();
    
    short getWorldCellAltitude();
    
    boolean spawnInWorld();
    
    void despawnFromWorld();
    
    boolean isBlockingMovements();
    
    GemType[] getAllowedInRooms();
    
    boolean canBeAddedIn(Room p0);
    
    RoomContentType getContentType();
}
