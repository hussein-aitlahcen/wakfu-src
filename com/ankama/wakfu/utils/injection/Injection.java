package com.ankama.wakfu.utils.injection;

import com.google.inject.*;
import com.google.common.base.*;
import javax.inject.*;

public class Injection
{
    private static final Injection INSTANCE;
    private Injector m_injector;
    
    public static Injection getInstance() {
        return Injection.INSTANCE;
    }
    
    public void setInjector(final Injector injector) {
        this.m_injector = injector;
    }
    
    public <T> T getInstance(final Class<T> type) {
        Preconditions.checkNotNull((Object)this.m_injector, (Object)"Injector is not already configured");
        return (T)this.m_injector.getInstance((Class)type);
    }
    
    public <T> Provider<T> getProvider(final Class<T> type) {
        Preconditions.checkNotNull((Object)this.m_injector, (Object)"Injector is not already configured");
        return (Provider<T>)this.m_injector.getProvider((Class)type);
    }
    
    static {
        INSTANCE = new Injection();
    }
}
