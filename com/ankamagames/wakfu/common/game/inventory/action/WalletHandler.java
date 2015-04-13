package com.ankamagames.wakfu.common.game.inventory.action;

public interface WalletHandler
{
    boolean canAddCash(int p0);
    
    boolean addAmount(int p0);
    
    boolean canSubtractCash(int p0);
    
    boolean subtractAmount(int p0);
}
