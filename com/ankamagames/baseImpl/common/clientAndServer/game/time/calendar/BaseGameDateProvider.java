package com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar;

public class BaseGameDateProvider implements GameDateProvider
{
    public static final BaseGameDateProvider INSTANCE;
    private GameDateProvider m_provider;
    
    public void setProvider(final GameDateProvider provider) {
        this.m_provider = provider;
    }
    
    @Override
    public GameDateConst getDate() {
        return this.m_provider.getDate();
    }
    
    static {
        INSTANCE = new BaseGameDateProvider();
    }
}
