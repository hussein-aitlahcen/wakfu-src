package com.ankamagames.wakfu.client.core.game.dimensionalBag.room;

import com.ankamagames.wakfu.common.game.personalSpace.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;

public class DimensionalBagRoomFactory implements RoomFactory
{
    public static final DimensionalBagRoomFactory INSTANCE;
    
    @Override
    public Room newRoom() {
        return new GemControlledRoom();
    }
    
    static {
        INSTANCE = new DimensionalBagRoomFactory();
    }
}
