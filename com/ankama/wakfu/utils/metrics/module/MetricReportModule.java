package com.ankama.wakfu.utils.metrics.module;

import com.codahale.metrics.*;
import com.google.inject.*;
import com.ankama.wakfu.utils.metrics.*;

public class MetricReportModule extends AbstractModule
{
    protected void configure() {
        this.bind((Class)MetricRegistry.class).in(Scopes.SINGLETON);
        this.bind((Class)MetricsManager.class).in(Scopes.SINGLETON);
    }
}
