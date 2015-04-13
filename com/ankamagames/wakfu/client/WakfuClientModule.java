package com.ankamagames.wakfu.client;

import java.util.*;
import com.ankamagames.wakfu.client.service.*;
import com.google.common.collect.*;
import com.ankama.wakfu.utils.injection.*;
import com.google.inject.*;

final class WakfuClientModule extends AbstractModule
{
    @RequiredModules
    public List<Module> requiredModules() {
        return (List<Module>)ImmutableList.of((Object)new ServiceModule());
    }
    
    public void configure() {
        this.bind((Class)WakfuClient.class).in(Scopes.SINGLETON);
    }
}
