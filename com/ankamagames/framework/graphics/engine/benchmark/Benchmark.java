package com.ankamagames.framework.graphics.engine.benchmark;

public interface Benchmark
{
    void initialize();
    
    void run(BenchmarkResult p0);
    
    void cleanUp();
    
    String getName();
}
