package com.ankamagames.framework.graphics.engine.Anm2.actions;

public class AnmActionFactoryProvider
{
    public static final AnmActionFactoryProvider INSTANCE;
    private AnmActionFactory m_factory;
    
    public AnmActionFactory getFactory() {
        return this.m_factory;
    }
    
    public void setFactory(final AnmActionFactory factory) {
        this.m_factory = factory;
    }
    
    static {
        INSTANCE = new AnmActionFactoryProvider();
    }
}
