package com.ankamagames.wakfu.client.core;

import com.google.inject.*;

public final class WakfuConfigurationModule extends AbstractModule
{
    protected void configure() {
        this.bind((Class)WakfuConfiguration.class).toInstance((Object)WakfuConfiguration.getInstance());
    }
}
