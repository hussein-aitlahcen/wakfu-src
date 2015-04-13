package com.ankamagames.wakfu.common.game.item;

public interface MerchantInventoryEventListener<M extends MerchantClient>
{
    void onMerchantItemSold(M p0, AbstractMerchantInventoryItem p1);
}
