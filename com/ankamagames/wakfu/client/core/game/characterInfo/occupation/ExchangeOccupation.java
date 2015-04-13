package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;

public class ExchangeOccupation extends AbstractOccupation
{
    protected static final Logger m_logger;
    private final ItemTrade m_trade;
    
    public ExchangeOccupation(final ItemTrade trade) {
        super();
        this.m_trade = trade;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 24;
    }
    
    @Override
    public boolean isAllowed() {
        return true;
    }
    
    public ItemTrade getTrade() {
        return this.m_trade;
    }
    
    @Override
    public void begin() {
    }
    
    @Override
    public boolean cancel(final boolean fromServer, final boolean sendMessage) {
        return true;
    }
    
    @Override
    public boolean finish() {
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ExchangeOccupation.class);
    }
}
