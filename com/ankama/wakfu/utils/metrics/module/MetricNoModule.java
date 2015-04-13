package com.ankama.wakfu.utils.metrics.module;

import com.google.common.collect.*;
import com.ankama.wakfu.utils.injection.*;
import com.google.inject.*;
import java.util.concurrent.*;
import java.util.*;
import com.codahale.metrics.*;

public class MetricNoModule extends AbstractModule
{
    protected void configure() {
    }
    
    @RequiredModules
    public static List<Module> requiredModules() {
        return (List<Module>)ImmutableList.of((Object)new MetricReportModule());
    }
    
    @Provides
    protected static ScheduledReporter createReporter(final MetricRegistry registry) {
        return new NoReporter(registry);
    }
    
    private static class NoReporter extends ScheduledReporter
    {
        NoReporter(final MetricRegistry registry) {
            super(registry, "no-reporter", MetricFilter.ALL, TimeUnit.SECONDS, TimeUnit.SECONDS);
        }
        
        public void report(final SortedMap<String, Gauge> gauges, final SortedMap<String, Counter> counters, final SortedMap<String, Histogram> histograms, final SortedMap<String, Meter> meters, final SortedMap<String, Timer> timers) {
        }
        
        public void report() {
        }
        
        public void start(final long period, final TimeUnit unit) {
        }
        
        public void stop() {
        }
        
        public void close() {
        }
    }
}
