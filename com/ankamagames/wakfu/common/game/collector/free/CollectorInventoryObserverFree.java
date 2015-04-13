package com.ankamagames.wakfu.common.game.collector.free;

import com.ankamagames.wakfu.common.game.collector.*;
import com.ankamagames.wakfu.common.game.item.*;

public interface CollectorInventoryObserverFree extends CollectorInventoryObserver
{
    void onItemAdded(Item p0);
    
    void onWalletUpdated(Wallet p0, int p1);
}
