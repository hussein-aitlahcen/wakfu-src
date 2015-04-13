package com.ankamagames.framework.graphics.engine.benchmark;

import org.apache.log4j.*;

public class BenchmarkResult
{
    protected static Logger m_logger;
    private int m_bestGLTextureFormat;
    private double m_rgbaBandwidthIndex;
    private double m_bgraBandwidthIndex;
    private double m_pixelFillRateIndex;
    private double m_textureFillRateIndex;
    
    public int getBestGLTextureFormat() {
        return this.m_bestGLTextureFormat;
    }
    
    public void setBestGLTextureFormat(final int bestGLTextureFormat) {
        this.m_bestGLTextureFormat = bestGLTextureFormat;
    }
    
    public double getRgbaBandwidthIndex() {
        return this.m_rgbaBandwidthIndex;
    }
    
    public void setRgbaBandwidthIndex(final double rgbaBandwidthIndex) {
        this.m_rgbaBandwidthIndex = rgbaBandwidthIndex;
    }
    
    public double getBgraBandwidthIndex() {
        return this.m_bgraBandwidthIndex;
    }
    
    public void setBgraBandwidthIndex(final double bgraBandwidthIndex) {
        this.m_bgraBandwidthIndex = bgraBandwidthIndex;
    }
    
    public double getPixelFillRateIndex() {
        return this.m_pixelFillRateIndex;
    }
    
    public void setPixelFillRateIndex(final double uniformQuadDrawIndex) {
        this.m_pixelFillRateIndex = uniformQuadDrawIndex;
    }
    
    public double getTextureFillRateIndex() {
        return this.m_textureFillRateIndex;
    }
    
    public void setTextureFillRateIndex(final double textureFillRateIndex) {
        this.m_textureFillRateIndex = textureFillRateIndex;
    }
    
    public void log() {
        BenchmarkResult.m_logger.info((Object)"Benchmark result :");
        BenchmarkResult.m_logger.info((Object)("\t* best texture format : " + ((this.m_bestGLTextureFormat == 6408) ? "RGBA" : "BGRA")));
        BenchmarkResult.m_logger.info((Object)("\t* rgba bandwidth index : " + this.m_rgbaBandwidthIndex));
        BenchmarkResult.m_logger.info((Object)("\t* bgra bandwidth index : " + this.m_bgraBandwidthIndex));
        BenchmarkResult.m_logger.info((Object)("\t* pixel fillrate index : " + this.m_pixelFillRateIndex));
        BenchmarkResult.m_logger.info((Object)("\t* texture fillrate index : " + this.m_textureFillRateIndex));
    }
    
    static {
        BenchmarkResult.m_logger = Logger.getLogger((Class)BenchmarkResult.class);
    }
}
