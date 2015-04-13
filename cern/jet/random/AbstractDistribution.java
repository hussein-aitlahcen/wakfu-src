package cern.jet.random;

import cern.colt.*;
import cern.colt.function.*;
import cern.jet.random.engine.*;

public abstract class AbstractDistribution extends PersistentObject implements DoubleFunction, IntFunction
{
    protected RandomEngine randomGenerator;
    
    public double apply(final double n) {
        return this.nextDouble();
    }
    
    public int apply(final int n) {
        return this.nextInt();
    }
    
    public Object clone() {
        final AbstractDistribution abstractDistribution = (AbstractDistribution)super.clone();
        if (this.randomGenerator != null) {
            abstractDistribution.randomGenerator = (RandomEngine)this.randomGenerator.clone();
        }
        return abstractDistribution;
    }
    
    protected RandomEngine getRandomGenerator() {
        return this.randomGenerator;
    }
    
    public static RandomEngine makeDefaultGenerator() {
        return RandomEngine.makeDefault();
    }
    
    public abstract double nextDouble();
    
    public int nextInt() {
        return (int)Math.round(this.nextDouble());
    }
    
    protected void setRandomGenerator(final RandomEngine randomGenerator) {
        this.randomGenerator = randomGenerator;
    }
}
