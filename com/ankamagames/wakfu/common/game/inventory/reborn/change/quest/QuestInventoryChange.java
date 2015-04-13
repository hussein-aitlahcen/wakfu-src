package com.ankamagames.wakfu.common.game.inventory.reborn.change.quest;

import java.nio.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;

public interface QuestInventoryChange
{
    byte[] serialize();
    
    void unSerialize(ByteBuffer p0);
    
    void compute(QuestInventoryController p0);
    
    QuestInventoryChangeType getType();
}
