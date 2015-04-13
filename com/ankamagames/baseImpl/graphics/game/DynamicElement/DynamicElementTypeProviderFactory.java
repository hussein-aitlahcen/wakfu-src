package com.ankamagames.baseImpl.graphics.game.DynamicElement;

public abstract class DynamicElementTypeProviderFactory
{
    private static DynamicElementTypeProviderFactory m_instance;
    
    public static DynamicElementTypeProviderFactory getInstance() {
        if (DynamicElementTypeProviderFactory.m_instance == null) {
            throw new UnsupportedOperationException("Le provider n'est pas initialis\u00e9");
        }
        return DynamicElementTypeProviderFactory.m_instance;
    }
    
    public static void setInstance(final DynamicElementTypeProviderFactory instance) {
        if (DynamicElementTypeProviderFactory.m_instance != null) {
            throw new UnsupportedOperationException("Le provider a d\u00e9j\u00e0 \u00e9t\u00e9 initialis\u00e9");
        }
        DynamicElementTypeProviderFactory.m_instance = instance;
    }
    
    public abstract DynamicElementType getFromId(final int p0);
    
    public abstract String getAnmPath();
}
