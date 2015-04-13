package com.ankamagames.wakfu.client.core.game.soap.shop;

public interface PartnerFinalizeTxnListener
{
    void onPartnerFinalizeTxn(int p0, int p1);
    
    void onError(PartnerFinalizeTxnError p0);
}
