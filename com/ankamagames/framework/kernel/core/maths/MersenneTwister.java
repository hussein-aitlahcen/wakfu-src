package com.ankamagames.framework.kernel.core.maths;

import java.util.*;
import java.io.*;

public class MersenneTwister extends Random implements Serializable
{
    private static final long serialVersionUID = 2932129847991607657L;
    private static final MersenneTwister _instance;
    private static final int N = 624;
    private static final int M = 397;
    private static final int MATRIX_A = -1727483681;
    private static final int UPPER_MASK = Integer.MIN_VALUE;
    private static final int LOWER_MASK = Integer.MAX_VALUE;
    private static final int TEMPERING_MASK_B = -1658038656;
    private static final int TEMPERING_MASK_C = -272236544;
    private int[] mt;
    private int mti;
    private int[] mag01;
    private double __nextNextGaussian;
    private boolean __haveNextNextGaussian;
    
    public MersenneTwister() {
        this(System.currentTimeMillis());
    }
    
    public MersenneTwister(final long seed) {
        super(seed);
        this.setSeed(seed);
    }
    
    public MersenneTwister(final int[] array) {
        super(System.currentTimeMillis());
        this.setSeed(array);
    }
    
    @Override
    public synchronized void setSeed(final long seed) {
        super.setSeed(seed);
        this.__haveNextNextGaussian = false;
        this.mt = new int[624];
        (this.mag01 = new int[2])[0] = 0;
        this.mag01[1] = -1727483681;
        this.mt[0] = (int)(seed & 0xFFFFFFFL);
        this.mti = 1;
        while (this.mti < 624) {
            this.mt[this.mti] = 1812433253 * (this.mt[this.mti - 1] ^ this.mt[this.mti - 1] >>> 30) + this.mti;
            final int[] mt = this.mt;
            final int mti = this.mti;
            mt[mti] &= -1;
            ++this.mti;
        }
    }
    
    public synchronized void setSeed(final int[] array) {
        this.setSeed(19650218L);
        int i = 1;
        int j = 0;
        for (int k = (624 > array.length) ? 624 : array.length; k != 0; --k) {
            this.mt[i] = (this.mt[i] ^ (this.mt[i - 1] ^ this.mt[i - 1] >>> 30) * 1664525) + array[j] + j;
            final int[] mt = this.mt;
            final int n = i;
            mt[n] &= -1;
            ++i;
            ++j;
            if (i >= 624) {
                this.mt[0] = this.mt[623];
                i = 1;
            }
            if (j >= array.length) {
                j = 0;
            }
        }
        for (int k = 623; k != 0; --k) {
            this.mt[i] = (this.mt[i] ^ (this.mt[i - 1] ^ this.mt[i - 1] >>> 30) * 1566083941) - i;
            final int[] mt2 = this.mt;
            final int n2 = i;
            mt2[n2] &= -1;
            if (++i >= 624) {
                this.mt[0] = this.mt[623];
                i = 1;
            }
        }
        this.mt[0] = Integer.MIN_VALUE;
    }
    
    @Override
    protected synchronized int next(final int bits) {
        if (this.mti >= 624) {
            int kk;
            for (kk = 0; kk < 227; ++kk) {
                final int y = (this.mt[kk] & Integer.MIN_VALUE) | (this.mt[kk + 1] & Integer.MAX_VALUE);
                this.mt[kk] = (this.mt[kk + 397] ^ y >>> 1 ^ this.mag01[y & 0x1]);
            }
            while (kk < 623) {
                final int y = (this.mt[kk] & Integer.MIN_VALUE) | (this.mt[kk + 1] & Integer.MAX_VALUE);
                this.mt[kk] = (this.mt[kk - 227] ^ y >>> 1 ^ this.mag01[y & 0x1]);
                ++kk;
            }
            final int y = (this.mt[623] & Integer.MIN_VALUE) | (this.mt[0] & Integer.MAX_VALUE);
            this.mt[623] = (this.mt[396] ^ y >>> 1 ^ this.mag01[y & 0x1]);
            this.mti = 0;
        }
        int y = this.mt[this.mti++];
        y ^= y >>> 11;
        y ^= (y << 7 & 0x9D2C5680);
        y ^= (y << 15 & 0xEFC60000);
        y ^= y >>> 18;
        return y >>> 32 - bits;
    }
    
    private synchronized void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
    
    private synchronized void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
    
    @Override
    public boolean nextBoolean() {
        return this.next(1) != 0;
    }
    
    public boolean nextBoolean(final float probability) {
        if (probability < 0.0f || probability > 1.0f) {
            throw new IllegalArgumentException("probability must be between 0.0 and 1.0 inclusive.");
        }
        return probability != 0.0f && (probability == 1.0f || this.nextFloat() < probability);
    }
    
    public boolean nextBoolean(final double probability) {
        if (probability < 0.0 || probability > 1.0) {
            throw new IllegalArgumentException("probability must be between 0.0 and 1.0 inclusive.");
        }
        return probability != 0.0 && (probability == 1.0 || this.nextDouble() < probability);
    }
    
    @Override
    public int nextInt(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        if (n == 0) {
            return 0;
        }
        if ((n & -n) == n) {
            return n * this.next(31) >> 31;
        }
        int bits;
        int val;
        do {
            bits = this.next(31);
            val = bits % n;
        } while (bits - val + (n - 1) < 0);
        return val;
    }
    
    public long nextLong(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException("n must be > 0");
        }
        if (n == 0L) {
            return 0L;
        }
        long bits;
        long val;
        do {
            bits = this.nextLong() >>> 1;
            val = bits % n;
        } while (bits - val + (n - 1L) < 0L);
        return val;
    }
    
    @Override
    public double nextDouble() {
        return ((this.next(26) << 27) + this.next(27)) / 9.007199254740992E15;
    }
    
    @Override
    public float nextFloat() {
        return this.next(24) / 1.6777216E7f;
    }
    
    @Override
    public void nextBytes(final byte[] bytes) {
        for (int x = 0; x < bytes.length; ++x) {
            bytes[x] = (byte)this.next(8);
        }
    }
    
    public char nextChar() {
        return (char)this.next(16);
    }
    
    public short nextShort() {
        return (short)this.next(16);
    }
    
    public byte nextByte() {
        return (byte)this.next(8);
    }
    
    @Override
    public synchronized double nextGaussian() {
        if (this.__haveNextNextGaussian) {
            this.__haveNextNextGaussian = false;
            return this.__nextNextGaussian;
        }
        double s;
        double v1;
        double v2;
        do {
            v1 = 2.0 * this.nextDouble() - 1.0;
            v2 = 2.0 * this.nextDouble() - 1.0;
            s = v1 * v1 + v2 * v2;
        } while (s >= 1.0 || s == 0.0);
        final double multiplier = Math.sqrt(-2.0 * Math.log(s) / s);
        this.__nextNextGaussian = v2 * multiplier;
        this.__haveNextNextGaussian = true;
        return v1 * multiplier;
    }
    
    public static final synchronized MersenneTwister getInstance() {
        return MersenneTwister._instance;
    }
    
    static {
        _instance = new MersenneTwister();
    }
}
