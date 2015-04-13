package com.ankamagames.wakfu.client.core.game.exchange;

public class ClientTradeHelper
{
    public static final ClientTradeHelper INSTANCE;
    private ItemTrade m_currentTrade;
    
    public boolean isTradeRunning() {
        return this.m_currentTrade != null;
    }
    
    public ItemTrade getCurrentTrade() {
        return this.m_currentTrade;
    }
    
    public void setCurrentTrade(final ItemTrade currentTrade) {
        this.m_currentTrade = currentTrade;
    }
    
    static {
        INSTANCE = new ClientTradeHelper();
    }
}
