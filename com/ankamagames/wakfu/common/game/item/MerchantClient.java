package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public interface MerchantClient<ItemType extends Item>
{
    Wallet getWallet();
    
    boolean canStockItem(ItemType p0);
    
    AbstractBag stockItem(ItemType p0);
    
    AbstractPartition getTransactionLocalisation();
}
