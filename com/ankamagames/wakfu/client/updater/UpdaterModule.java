package com.ankamagames.wakfu.client.updater;

import com.ankamagames.wakfu.client.core.*;
import com.ankama.wakfu.utils.injection.*;
import com.google.inject.*;

public final class UpdaterModule extends AbstractModule
{
    @RequiredModules
    public Module requiredModule() {
        return (Module)new WakfuConfigurationModule();
    }
    
    protected void configure() {
        this.bind((Class)IComponentManager.class).to((Class)ComponentManager.class).in(Scopes.SINGLETON);
        this.bind((Class)ITextureManager.class).to((Class)TextureManagerProxy.class).in(Scopes.SINGLETON);
    }
}
