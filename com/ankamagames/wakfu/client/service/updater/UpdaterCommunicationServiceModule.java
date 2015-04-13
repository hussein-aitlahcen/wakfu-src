package com.ankamagames.wakfu.client.service.updater;

import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.updater.*;
import com.google.common.collect.*;
import com.ankama.wakfu.utils.injection.*;
import io.netty.channel.*;
import com.google.common.base.*;
import io.netty.handler.codec.string.*;
import com.google.inject.*;

public final class UpdaterCommunicationServiceModule extends PrivateModule
{
    @RequiredModules
    public List<Module> requiredModule() {
        return (List<Module>)ImmutableList.of((Object)new WakfuConfigurationModule(), (Object)new UpdaterModule());
    }
    
    protected void configure() {
        this.bind((Class)UpdaterCommunicationService.class).in(Scopes.SINGLETON);
        this.expose((Class)UpdaterCommunicationService.class);
    }
    
    @Provides
    private List<ChannelHandler> pipelineHandlers(final MessageBufferHandler messageBufferHandler, final MessageCompleteHandler messageCompleteHandler, final InitCommunicationHandler initCommunicationHandler) {
        return (List<ChannelHandler>)ImmutableList.of((Object)new StringDecoder(Charsets.UTF_8), (Object)new StringEncoder(Charsets.UTF_8), (Object)messageBufferHandler, (Object)messageCompleteHandler, (Object)initCommunicationHandler);
    }
}
