package com.ankamagames.wakfu.common.game.collector.limited;

import com.ankamagames.wakfu.common.game.collector.*;
import com.ankamagames.wakfu.common.game.item.*;

public interface CollectorInventoryObserverLimited extends CollectorInventoryObserver
{
    void onItemAdded(int p0, int p1);
    
    void onWalletUpdated(Wallet p0, int p1);
}
