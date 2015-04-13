package cern.jet.random;

import cern.jet.random.engine.*;
import cern.jet.stat.*;

public class Normal extends AbstractContinousDistribution
{
    protected double mean;
    protected double variance;
    protected double standardDeviation;
    protected double cache;
    protected boolean cacheFilled;
    protected double SQRT_INV;
    protected static Normal shared;
    
    public Normal(final double n, final double n2, final RandomEngine randomGenerator) {
        super();
        this.setRandomGenerator(randomGenerator);
        this.setState(n, n2);
    }
    
    public double cdf(final double n) {
        return Probability.normal(this.mean, this.variance, n);
    }
    
    public double nextDouble() {
        return this.nextDouble(this.mean, this.standardDeviation);
    }
    
    public double nextDouble(final double n, final double n2) {
        if (this.cacheFilled && this.mean == n && this.standardDeviation == n2) {
            this.cacheFilled = false;
            return this.cache;
        }
        double a;
        double n3;
        double n4;
        do {
            n3 = 2.0 * this.randomGenerator.raw() - 1.0;
            n4 = 2.0 * this.randomGenerator.raw() - 1.0;
            a = n3 * n3 + n4 * n4;
        } while (a >= 1.0);
        final double sqrt = Math.sqrt(-2.0 * Math.log(a) / a);
        this.cache = n + n2 * n3 * sqrt;
        this.cacheFilled = true;
        return n + n2 * n4 * sqrt;
    }
    
    public double pdf(final double n) {
        final double n2 = n - this.mean;
        return this.SQRT_INV * Math.exp(-(n2 * n2) / (2.0 * this.variance));
    }
    
    protected void setRandomGenerator(final RandomEngine randomGenerator) {
        super.setRandomGenerator(randomGenerator);
        this.cacheFilled = false;
    }
    
    public void setState(final double mean, final double standardDeviation) {
        if (mean != this.mean || standardDeviation != this.standardDeviation) {
            this.mean = mean;
            this.standardDeviation = standardDeviation;
            this.variance = standardDeviation * standardDeviation;
            this.cacheFilled = false;
            this.SQRT_INV = 1.0 / Math.sqrt(6.283185307179586 * this.variance);
        }
    }
    
    public static double staticNextDouble(final double n, final double n2) {
        synchronized (Normal.shared) {
            return Normal.shared.nextDouble(n, n2);
        }
    }
    
    public String toString() {
        return this.getClass().getName() + "(" + this.mean + "," + this.standardDeviation + ")";
    }
    
    private static void xstaticSetRandomGenerator(final RandomEngine randomGenerator) {
        synchronized (Normal.shared) {
            Normal.shared.setRandomGenerator(randomGenerator);
        }
    }
    
    static {
        Normal.shared = new Normal(0.0, 1.0, AbstractDistribution.makeDefaultGenerator());
    }
}
