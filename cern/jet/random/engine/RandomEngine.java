package cern.jet.random.engine;

import cern.colt.*;
import cern.colt.function.*;

public abstract class RandomEngine extends PersistentObject implements DoubleFunction, IntFunction
{
    public double apply(final double n) {
        return this.raw();
    }
    
    public int apply(final int n) {
        return this.nextInt();
    }
    
    public static RandomEngine makeDefault() {
        return new MersenneTwister((int)System.currentTimeMillis());
    }
    
    public double nextDouble() {
        double n;
        do {
            n = (this.nextLong() + 9.223372036854776E18) * 5.421010862427522E-20;
        } while (n <= 0.0 || n >= 1.0);
        return n;
    }
    
    public float nextFloat() {
        float n;
        do {
            n = (float)this.raw();
        } while (n >= 1.0f);
        return n;
    }
    
    public abstract int nextInt();
    
    public long nextLong() {
        return (this.nextInt() & 0xFFFFFFFFL) << 32 | (this.nextInt() & 0xFFFFFFFFL);
    }
    
    public double raw() {
        int i;
        do {
            i = this.nextInt();
        } while (i == 0);
        return (i & 0xFFFFFFFFL) * 2.3283064365386963E-10;
    }
}
