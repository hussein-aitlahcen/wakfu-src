package com.ankamagames.framework.graphics.engine.benchmark;

import org.apache.log4j.*;
import java.util.*;

public class BenchmarkRunner implements Benchmark
{
    protected static Logger m_logger;
    public static final BenchmarkRunner INSTANCE;
    private final ArrayList<Benchmark> m_benchmarks;
    
    private BenchmarkRunner() {
        super();
        this.m_benchmarks = new ArrayList<Benchmark>();
    }
    
    public void addBenchmark(final Benchmark benchmark) {
        this.m_benchmarks.add(benchmark);
    }
    
    @Override
    public void initialize() {
        BenchmarkRunner.m_logger.info((Object)"Initializing benchmarks...");
        for (int i = 0, size = this.m_benchmarks.size(); i < size; ++i) {
            final Benchmark benchmark = this.m_benchmarks.get(i);
            try {
                benchmark.initialize();
            }
            catch (Exception e) {
                BenchmarkRunner.m_logger.error((Object)("Erreur \u00e0 l'initialisation du benchmark " + benchmark.getName()), (Throwable)e);
            }
        }
    }
    
    @Override
    public void run(final BenchmarkResult result) {
        BenchmarkRunner.m_logger.info((Object)"Running benchmarks...");
        for (int i = 0, size = this.m_benchmarks.size(); i < size; ++i) {
            final Benchmark benchmark = this.m_benchmarks.get(i);
            BenchmarkRunner.m_logger.info((Object)("\t* benchmarking " + benchmark.getName() + "..."));
            try {
                benchmark.run(result);
            }
            catch (Exception e) {
                BenchmarkRunner.m_logger.error((Object)("Erreur \u00e0 l'execution du benchmark " + benchmark.getName()), (Throwable)e);
            }
        }
    }
    
    @Override
    public void cleanUp() {
        BenchmarkRunner.m_logger.info((Object)"Cleaning up benchmarks...");
        for (int i = 0, size = this.m_benchmarks.size(); i < size; ++i) {
            final Benchmark benchmark = this.m_benchmarks.get(i);
            try {
                benchmark.cleanUp();
            }
            catch (Exception e) {
                BenchmarkRunner.m_logger.error((Object)("Erreur au cleanup du benchmark " + benchmark.getName()), (Throwable)e);
            }
        }
    }
    
    @Override
    public String getName() {
        return "Benchmark runner";
    }
    
    static {
        BenchmarkRunner.m_logger = Logger.getLogger((Class)BenchmarkRunner.class);
        INSTANCE = new BenchmarkRunner();
    }
}
