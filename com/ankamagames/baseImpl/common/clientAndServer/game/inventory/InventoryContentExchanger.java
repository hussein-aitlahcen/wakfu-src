package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;

public interface InventoryContentExchanger<ContentType extends InventoryContent, SourceInventoryType extends Inventory<ContentType>, TargetInventoryType extends Inventory<ContentType>>
{
    int moveItem(SourceInventoryType p0, TargetInventoryType p1, ContentType p2, EffectUser p3, EffectContext p4) throws InventoryCapacityReachedException, ContentAlreadyPresentException;
    
    int moveItem(SourceInventoryType p0, short p1, TargetInventoryType p2, short p3, ContentType p4, EffectUser p5, EffectContext p6) throws InventoryCapacityReachedException, ContentAlreadyPresentException;
}
