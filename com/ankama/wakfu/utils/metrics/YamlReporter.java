package com.ankama.wakfu.utils.metrics;

import java.io.*;
import java.util.concurrent.*;
import org.yaml.snakeyaml.*;
import java.util.*;
import com.codahale.metrics.*;

public class YamlReporter extends ScheduledReporter
{
    private final PrintStream m_output;
    
    public YamlReporter(final MetricRegistry registry, final PrintStream output, final MetricFilter filter, final TimeUnit rateUnit, final TimeUnit timeUnit) {
        super(registry, "yaml-reporter", filter, rateUnit, timeUnit);
        this.m_output = output;
    }
    
    public void report(final SortedMap<String, Gauge> gauges, final SortedMap<String, Counter> counters, final SortedMap<String, Histogram> histograms, final SortedMap<String, Meter> meters, final SortedMap<String, Timer> timers) {
        final Reports reports = new Reports();
        if (!gauges.isEmpty()) {
            for (final Map.Entry<String, Gauge> entry : gauges.entrySet()) {
                final String key = entry.getKey();
                final Gauge value = entry.getValue();
                reports.put(key, new GaugeReport(value.getValue()));
            }
        }
        if (!counters.isEmpty()) {
            for (final Map.Entry<String, Counter> entry2 : counters.entrySet()) {
                final String key = entry2.getKey();
                final Counter value2 = entry2.getValue();
                reports.put(key, new CounterReport(value2.getCount()));
            }
        }
        if (!histograms.isEmpty()) {
            for (final Map.Entry<String, Histogram> entry3 : histograms.entrySet()) {
                final String key = entry3.getKey();
                final Histogram value3 = entry3.getValue();
                reports.put(key, new HistogramReport(value3.getCount(), value3.getSnapshot()));
            }
        }
        if (!meters.isEmpty()) {
            for (final Map.Entry<String, Meter> entry4 : meters.entrySet()) {
                final String key = entry4.getKey();
                final Meter value4 = entry4.getValue();
                reports.put(key, new MeterReport(value4));
            }
        }
        if (!timers.isEmpty()) {
            for (final Map.Entry<String, Timer> entry5 : timers.entrySet()) {
                final String key = entry5.getKey();
                final Timer value5 = entry5.getValue();
                reports.put(key, new TimerReport(this, value5));
            }
        }
        final DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setAllowReadOnlyProperties(true);
        final Yaml yaml = new Yaml(options);
        this.m_output.println(yaml.dumpAsMap(reports));
    }
    
    static /* synthetic */ double access$000(final YamlReporter x0, final double x1) {
        return x0.convertRate(x1);
    }
    
    static /* synthetic */ String access$100(final YamlReporter x0) {
        return x0.getRateUnit();
    }
    
    static /* synthetic */ double access$200(final YamlReporter x0, final double x1) {
        return x0.convertRate(x1);
    }
    
    static /* synthetic */ String access$300(final YamlReporter x0) {
        return x0.getRateUnit();
    }
    
    static /* synthetic */ double access$400(final YamlReporter x0, final double x1) {
        return x0.convertRate(x1);
    }
    
    static /* synthetic */ String access$500(final YamlReporter x0) {
        return x0.getRateUnit();
    }
    
    static /* synthetic */ double access$600(final YamlReporter x0, final double x1) {
        return x0.convertRate(x1);
    }
    
    static /* synthetic */ String access$700(final YamlReporter x0) {
        return x0.getRateUnit();
    }
    
    static /* synthetic */ double access$800(final YamlReporter x0, final double x1) {
        return x0.convertDuration(x1);
    }
    
    static /* synthetic */ String access$900(final YamlReporter x0) {
        return x0.getDurationUnit();
    }
    
    static /* synthetic */ double access$1000(final YamlReporter x0, final double x1) {
        return x0.convertDuration(x1);
    }
    
    static /* synthetic */ String access$1100(final YamlReporter x0) {
        return x0.getDurationUnit();
    }
    
    static /* synthetic */ double access$1200(final YamlReporter x0, final double x1) {
        return x0.convertDuration(x1);
    }
    
    static /* synthetic */ String access$1300(final YamlReporter x0) {
        return x0.getDurationUnit();
    }
    
    static /* synthetic */ double access$1400(final YamlReporter x0, final double x1) {
        return x0.convertDuration(x1);
    }
    
    static /* synthetic */ String access$1500(final YamlReporter x0) {
        return x0.getDurationUnit();
    }
    
    static /* synthetic */ double access$1600(final YamlReporter x0, final double x1) {
        return x0.convertDuration(x1);
    }
    
    static /* synthetic */ String access$1700(final YamlReporter x0) {
        return x0.getDurationUnit();
    }
    
    static /* synthetic */ double access$1800(final YamlReporter x0, final double x1) {
        return x0.convertDuration(x1);
    }
    
    static /* synthetic */ String access$1900(final YamlReporter x0) {
        return x0.getDurationUnit();
    }
    
    static /* synthetic */ double access$2000(final YamlReporter x0, final double x1) {
        return x0.convertDuration(x1);
    }
    
    static /* synthetic */ String access$2100(final YamlReporter x0) {
        return x0.getDurationUnit();
    }
    
    static /* synthetic */ double access$2200(final YamlReporter x0, final double x1) {
        return x0.convertDuration(x1);
    }
    
    static /* synthetic */ String access$2300(final YamlReporter x0) {
        return x0.getDurationUnit();
    }
    
    static /* synthetic */ double access$2400(final YamlReporter x0, final double x1) {
        return x0.convertDuration(x1);
    }
    
    static /* synthetic */ String access$2500(final YamlReporter x0) {
        return x0.getDurationUnit();
    }
    
    static /* synthetic */ double access$2600(final YamlReporter x0, final double x1) {
        return x0.convertDuration(x1);
    }
    
    static /* synthetic */ String access$2700(final YamlReporter x0) {
        return x0.getDurationUnit();
    }
    
    private static class Reports
    {
        private final Map<String, GaugeReport> m_gauges;
        private final Map<String, CounterReport> m_counters;
        private final Map<String, HistogramReport> m_histograms;
        private final Map<String, MeterReport> m_meters;
        private final Map<String, TimerReport> m_timers;
        
        Reports() {
            super();
            this.m_gauges = new HashMap<String, GaugeReport>();
            this.m_counters = new HashMap<String, CounterReport>();
            this.m_histograms = new HashMap<String, HistogramReport>();
            this.m_meters = new HashMap<String, MeterReport>();
            this.m_timers = new HashMap<String, TimerReport>();
        }
        
        void put(final String key, final GaugeReport report) {
            this.m_gauges.put(key, report);
        }
        
        public Map<String, GaugeReport> getGauges() {
            return Collections.unmodifiableMap((Map<? extends String, ? extends GaugeReport>)this.m_gauges);
        }
        
        void put(final String key, final CounterReport report) {
            this.m_counters.put(key, report);
        }
        
        public Map<String, CounterReport> getCounters() {
            return Collections.unmodifiableMap((Map<? extends String, ? extends CounterReport>)this.m_counters);
        }
        
        void put(final String key, final HistogramReport report) {
            this.m_histograms.put(key, report);
        }
        
        public Map<String, HistogramReport> getHistograms() {
            return Collections.unmodifiableMap((Map<? extends String, ? extends HistogramReport>)this.m_histograms);
        }
        
        void put(final String key, final MeterReport report) {
            this.m_meters.put(key, report);
        }
        
        public Map<String, MeterReport> getMeters() {
            return Collections.unmodifiableMap((Map<? extends String, ? extends MeterReport>)this.m_meters);
        }
        
        void put(final String key, final TimerReport report) {
            this.m_timers.put(key, report);
        }
        
        public Map<String, TimerReport> getTimers() {
            return Collections.unmodifiableMap((Map<? extends String, ? extends TimerReport>)this.m_timers);
        }
        
        @Override
        public String toString() {
            return "Reports{m_gauges=" + this.m_gauges.size() + ", m_counters=" + this.m_counters.size() + ", m_histograms=" + this.m_histograms.size() + ", m_meters=" + this.m_meters.size() + ", m_timers=" + this.m_timers.size() + '}';
        }
    }
    
    private static class GaugeReport
    {
        private final Object m_value;
        
        GaugeReport(final Object value) {
            super();
            this.m_value = value;
        }
        
        public Object getValue() {
            return this.m_value;
        }
        
        @Override
        public String toString() {
            return "GaugeReport{m_value=" + this.m_value + '}';
        }
    }
    
    private static class CounterReport
    {
        private final long m_count;
        
        CounterReport(final long count) {
            super();
            this.m_count = count;
        }
        
        public long getCount() {
            return this.m_count;
        }
        
        @Override
        public String toString() {
            return "CounterReport{m_count=" + this.m_count + '}';
        }
    }
    
    private static class HistogramReport
    {
        private final long m_count;
        private final Snapshot m_snapshot;
        
        HistogramReport(final long count, final Snapshot snapshot) {
            super();
            this.m_count = count;
            this.m_snapshot = snapshot;
        }
        
        public long getCount() {
            return this.m_count;
        }
        
        public long getMin() {
            return this.m_snapshot.getMin();
        }
        
        public long getMax() {
            return this.m_snapshot.getMax();
        }
        
        public double getMean() {
            return this.m_snapshot.getMean();
        }
        
        public double getMedian() {
            return this.m_snapshot.getMedian();
        }
        
        public double getStdDev() {
            return this.m_snapshot.getStdDev();
        }
        
        public double get75thPercentile() {
            return this.m_snapshot.get75thPercentile();
        }
        
        public double get95thPercentile() {
            return this.m_snapshot.get95thPercentile();
        }
        
        public double get98thPercentile() {
            return this.m_snapshot.get98thPercentile();
        }
        
        public double get99thPercentile() {
            return this.m_snapshot.get99thPercentile();
        }
        
        public double get999thPercentile() {
            return this.m_snapshot.get999thPercentile();
        }
        
        @Override
        public String toString() {
            return "HistogramReport{m_count=" + this.m_count + ", m_snapshot=" + this.m_snapshot.size() + '}';
        }
    }
    
    private static class MeterReport
    {
        private final Meter m_meter;
        
        MeterReport(final Meter meter) {
            super();
            this.m_meter = meter;
        }
        
        public long getCount() {
            return this.m_meter.getCount();
        }
        
        public double getMeanRate() {
            return this.m_meter.getMeanRate();
        }
        
        public double getOneMinuteRate() {
            return this.m_meter.getOneMinuteRate();
        }
        
        public double getFiveMinuteRate() {
            return this.m_meter.getFiveMinuteRate();
        }
        
        public double getFifteenMinuteRate() {
            return this.m_meter.getFifteenMinuteRate();
        }
        
        @Override
        public String toString() {
            return "MeterReport{m_meter=" + this.m_meter.getCount() + '}';
        }
    }
    
    private static class TimerReport
    {
        private final YamlReporter m_reporter;
        private final Timer m_timer;
        
        TimerReport(final YamlReporter yamlReporter, final Timer timer) {
            super();
            this.m_reporter = yamlReporter;
            this.m_timer = timer;
        }
        
        public long getCount() {
            return this.m_timer.getCount();
        }
        
        public String getMeanRate() {
            return String.format("%2.2f calls/%s", YamlReporter.access$000(this.m_reporter, this.m_timer.getMeanRate()), YamlReporter.access$100(this.m_reporter));
        }
        
        public String getOneMinuteRate() {
            return String.format("%2.2f calls/%s", YamlReporter.access$200(this.m_reporter, this.m_timer.getOneMinuteRate()), YamlReporter.access$300(this.m_reporter));
        }
        
        public String getFiveMinuteRate() {
            return String.format("%2.2f calls/%s", YamlReporter.access$400(this.m_reporter, this.m_timer.getFiveMinuteRate()), YamlReporter.access$500(this.m_reporter));
        }
        
        public String getFifteenMinuteRate() {
            return String.format("%2.2f calls/%s", YamlReporter.access$600(this.m_reporter, this.m_timer.getFifteenMinuteRate()), YamlReporter.access$700(this.m_reporter));
        }
        
        public String getMin() {
            return String.format("%2.2f %s", YamlReporter.access$800(this.m_reporter, this.m_timer.getSnapshot().getMin()), YamlReporter.access$900(this.m_reporter));
        }
        
        public String getMax() {
            return String.format("%2.2f %s", YamlReporter.access$1000(this.m_reporter, this.m_timer.getSnapshot().getMax()), YamlReporter.access$1100(this.m_reporter));
        }
        
        public String getMean() {
            return String.format("%2.2f %s", YamlReporter.access$1200(this.m_reporter, this.m_timer.getSnapshot().getMean()), YamlReporter.access$1300(this.m_reporter));
        }
        
        public String getStdDev() {
            return String.format("%2.2f %s", YamlReporter.access$1400(this.m_reporter, this.m_timer.getSnapshot().getStdDev()), YamlReporter.access$1500(this.m_reporter));
        }
        
        public String getMedian() {
            return String.format("%2.2f %s", YamlReporter.access$1600(this.m_reporter, this.m_timer.getSnapshot().getMedian()), YamlReporter.access$1700(this.m_reporter));
        }
        
        public String get75thPercentile() {
            return String.format("<= %2.2f %s", YamlReporter.access$1800(this.m_reporter, this.m_timer.getSnapshot().get75thPercentile()), YamlReporter.access$1900(this.m_reporter));
        }
        
        public String get95thPercentile() {
            return String.format("<= %2.2f %s", YamlReporter.access$2000(this.m_reporter, this.m_timer.getSnapshot().get95thPercentile()), YamlReporter.access$2100(this.m_reporter));
        }
        
        public String get98thPercentile() {
            return String.format("<= %2.2f %s", YamlReporter.access$2200(this.m_reporter, this.m_timer.getSnapshot().get98thPercentile()), YamlReporter.access$2300(this.m_reporter));
        }
        
        public String get99thPercentile() {
            return String.format("<= %2.2f %s", YamlReporter.access$2400(this.m_reporter, this.m_timer.getSnapshot().get99thPercentile()), YamlReporter.access$2500(this.m_reporter));
        }
        
        public String get999thPercentile() {
            return String.format("<= %2.2f %s", YamlReporter.access$2600(this.m_reporter, this.m_timer.getSnapshot().get999thPercentile()), YamlReporter.access$2700(this.m_reporter));
        }
        
        @Override
        public String toString() {
            return "TimerReport{m_timer=" + this.m_timer.getCount() + '}';
        }
    }
}
