package cern.jet.random.engine;

import java.util.*;

public class MersenneTwister extends RandomEngine
{
    private int mti;
    private int[] mt;
    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = -1727483681;
    private static final int UPPER_MASK = Integer.MIN_VALUE;
    private static final int LOWER_MASK = Integer.MAX_VALUE;
    private static final int TEMPERING_MASK_B = -1658038656;
    private static final int TEMPERING_MASK_C = -272236544;
    private static final int mag0 = 0;
    private static final int mag1 = -1727483681;
    public static final int DEFAULT_SEED = 4357;
    
    public MersenneTwister() {
        this(4357);
    }
    
    public MersenneTwister(final int seed) {
        super();
        this.mt = new int[624];
        this.setSeed(seed);
    }
    
    public MersenneTwister(final Date date) {
        this((int)date.getTime());
    }
    
    public Object clone() {
        final MersenneTwister mersenneTwister = (MersenneTwister)super.clone();
        mersenneTwister.mt = this.mt.clone();
        return mersenneTwister;
    }
    
    protected void nextBlock() {
        int i;
        for (i = 0; i < 227; ++i) {
            final int n = (this.mt[i] & Integer.MIN_VALUE) | (this.mt[i + 1] & Integer.MAX_VALUE);
            this.mt[i] = (this.mt[i + 397] ^ n >>> 1 ^ (((n & 0x1) == 0x0) ? 0 : -1727483681));
        }
        while (i < 623) {
            final int n2 = (this.mt[i] & Integer.MIN_VALUE) | (this.mt[i + 1] & Integer.MAX_VALUE);
            this.mt[i] = (this.mt[i - 227] ^ n2 >>> 1 ^ (((n2 & 0x1) == 0x0) ? 0 : -1727483681));
            ++i;
        }
        final int n3 = (this.mt[623] & Integer.MIN_VALUE) | (this.mt[0] & Integer.MAX_VALUE);
        this.mt[623] = (this.mt[396] ^ n3 >>> 1 ^ (((n3 & 0x1) == 0x0) ? 0 : -1727483681));
        this.mti = 0;
    }
    
    public int nextInt() {
        if (this.mti == 624) {
            this.nextBlock();
        }
        final int n = this.mt[this.mti++];
        final int n2 = n ^ n >>> 11;
        final int n3 = n2 ^ (n2 << 7 & 0x9D2C5680);
        final int n4 = n3 ^ (n3 << 15 & 0xEFC60000);
        return n4 ^ n4 >>> 18;
    }
    
    protected void setSeed(final int n) {
        this.mt[0] = (n & -1);
        for (int i = 1; i < 624; ++i) {
            this.mt[i] = 1812433253 * (this.mt[i - 1] ^ this.mt[i - 1] >> 30) + i;
            final int[] mt = this.mt;
            final int n2 = i;
            mt[n2] &= -1;
        }
        this.mti = 624;
    }
}
