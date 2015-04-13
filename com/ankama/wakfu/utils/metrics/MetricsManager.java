package com.ankama.wakfu.utils.metrics;

import javax.inject.*;
import org.jetbrains.annotations.*;
import java.util.*;
import java.io.*;
import com.codahale.metrics.*;
import java.util.concurrent.*;

public class MetricsManager
{
    private final MetricRegistry m_registry;
    private final Provider<ScheduledReporter> m_reporterProvider;
    @Nullable
    private ScheduledReporter m_reporter;
    
    private MetricsManager(final MetricRegistry registry, final Provider<ScheduledReporter> reporterProvider) {
        super();
        this.m_registry = registry;
        this.m_reporterProvider = reporterProvider;
    }
    
    public Histogram histogram(final Metrics metric, final Object... args) {
        return this.m_registry.histogram(String.format(metric.m_format, args));
    }
    
    public Timer timer(final Metrics metric, final Object... args) {
        return this.m_registry.timer(String.format(metric.m_format, args));
    }
    
    public SortedSet<String> keys() {
        return (SortedSet<String>)this.m_registry.getNames();
    }
    
    public YamlReporter yamlReporter(final PrintStream output, final MetricFilter filter) {
        return new YamlReporter(this.m_registry, output, filter, TimeUnit.SECONDS, TimeUnit.MILLISECONDS);
    }
    
    public void startGraphiteReporter(final int period, final TimeUnit unit) {
        this.stopGraphiteReporter();
        (this.m_reporter = (ScheduledReporter)this.m_reporterProvider.get()).start((long)period, unit);
    }
    
    public void stopGraphiteReporter() {
        if (this.m_reporter != null) {
            this.m_reporter.stop();
        }
    }
    
    @Override
    public String toString() {
        return "MetricsManager{m_registry=" + this.m_registry.getNames().size() + '}';
    }
}
