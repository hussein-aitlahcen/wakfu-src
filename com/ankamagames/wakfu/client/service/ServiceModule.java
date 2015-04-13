package com.ankamagames.wakfu.client.service;

import com.ankama.wakfu.utils.injection.*;
import com.ankamagames.wakfu.client.service.updater.*;
import java.util.*;
import com.google.common.collect.*;
import com.google.inject.*;

public final class ServiceModule extends AbstractModule
{
    @RequiredModules
    public Module requiredModule() {
        return (Module)new UpdaterCommunicationServiceModule();
    }
    
    protected void configure() {
        this.bind((Class)IServiceManager.class).to((Class)ServiceManager.class).in(Scopes.SINGLETON);
    }
    
    @Provides
    @Singleton
    List<IService> registeredServices(final UpdaterCommunicationService updaterCommunicationService) {
        return (List<IService>)ImmutableList.of((Object)updaterCommunicationService);
    }
}
