package com.ankamagames.wakfu.client.core.game.soap.account;

public interface MoneyListener
{
    void onMoney(int p0, int p1);
    
    void onError();
}
