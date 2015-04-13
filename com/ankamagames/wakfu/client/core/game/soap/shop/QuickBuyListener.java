package com.ankamagames.wakfu.client.core.game.soap.shop;

public interface QuickBuyListener
{
    void onPartnerQuickBuy();
    
    void onQuickBuy(int p0, int p1);
    
    void onError(QuickBuyError p0);
}
