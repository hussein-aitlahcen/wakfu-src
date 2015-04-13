package com.ankamagames.framework.kernel.core.maths;

import java.util.*;

public class Distribution
{
    protected final double m_mean;
    protected final double m_deviation;
    protected final double m_variance;
    protected final Random m_random;
    
    public Distribution(final double mean, final double deviation) {
        this(mean, deviation, System.currentTimeMillis());
    }
    
    public Distribution(final double mean, final double deviation, final long seed) {
        super();
        this.m_mean = mean;
        this.m_deviation = deviation;
        this.m_variance = this.m_deviation * this.m_deviation;
        this.m_random = new MersenneTwister(seed);
    }
    
    public double pdf(final double x) {
        final double delta = x - this.m_mean;
        final double sqrtInv = 1.0 / Math.sqrt(6.283185307179586 * this.m_variance);
        return sqrtInv * Math.exp(-(delta * delta) / (2.0 * this.m_variance));
    }
    
    public double nextDouble() {
        final double random = this.m_random.nextGaussian();
        return this.m_mean + this.m_deviation * random;
    }
    
    @Override
    public String toString() {
        return "CraftDistribution{m_mean=" + this.m_mean + ", m_deviation=" + this.m_deviation + ", m_variance=" + this.m_variance + '}';
    }
}
