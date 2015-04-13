package com.jcraft.jorbis;

class Mdct
{
    private static final Mdct MDCT_0;
    private static final Mdct MDCT_256;
    private static final Mdct MDCT_2048;
    private final int n;
    private final int log2n;
    private final float[] trig;
    private final int[] bitrev;
    private final float[] _x;
    private final float[] _w;
    
    private Mdct(final int size) {
        super();
        this.bitrev = new int[size / 4];
        this.trig = new float[size + size / 4];
        this.log2n = (int)Math.rint(Math.log(size) / Math.log(2.0));
        this.n = size;
        this._x = new float[this.n / 2];
        this._w = new float[this.n / 2];
        final int AE = 0;
        final int AO = 1;
        final int BE = 0 + size / 2;
        final int BO = BE + 1;
        final int CE = BE + size / 2;
        final int CO = CE + 1;
        for (int i = 0; i < size / 4; ++i) {
            this.trig[0 + i * 2] = (float)Math.cos(3.141592653589793 / size * (4 * i));
            this.trig[1 + i * 2] = (float)(-Math.sin(3.141592653589793 / size * (4 * i)));
            this.trig[BE + i * 2] = (float)Math.cos(3.141592653589793 / (2 * size) * (2 * i + 1));
            this.trig[BO + i * 2] = (float)Math.sin(3.141592653589793 / (2 * size) * (2 * i + 1));
        }
        for (int i = 0; i < size / 8; ++i) {
            this.trig[CE + i * 2] = (float)Math.cos(3.141592653589793 / size * (4 * i + 2));
            this.trig[CO + i * 2] = (float)(-Math.sin(3.141592653589793 / size * (4 * i + 2)));
        }
        final int mask = (1 << this.log2n - 1) - 1;
        final int msb = 1 << this.log2n - 2;
        for (int j = 0; j < size / 8; ++j) {
            int acc = 0;
            for (int k = 0; msb >>> k != 0; ++k) {
                if ((msb >>> k & j) != 0x0) {
                    acc |= 1 << k;
                }
            }
            this.bitrev[j * 2] = (~acc & mask);
            this.bitrev[j * 2 + 1] = acc;
        }
    }
    
    public static Mdct create(final int size) {
        if (size == 256) {
            return Mdct.MDCT_256;
        }
        if (size == 2048) {
            return Mdct.MDCT_2048;
        }
        if (size == 0) {
            return Mdct.MDCT_0;
        }
        return new Mdct(size);
    }
    
    synchronized void backward(final float[] in, final float[] out) {
        final float[] x = this._x;
        final float[] w = this._w;
        int n2 = this.n >>> 1;
        n2 = this.n >>> 2;
        final int n3 = this.n >>> 3;
        int inO = 1;
        int xO = 0;
        int A = n2;
        for (int i = 0; i < n3; ++i) {
            A -= 2;
            x[xO++] = -in[inO + 2] * this.trig[A + 1] - in[inO] * this.trig[A];
            x[xO++] = in[inO] * this.trig[A + 1] - in[inO + 2] * this.trig[A];
            inO += 4;
        }
        inO = n2 - 4;
        for (int i = 0; i < n3; ++i) {
            A -= 2;
            x[xO++] = in[inO] * this.trig[A + 1] + in[inO + 2] * this.trig[A];
            x[xO++] = in[inO] * this.trig[A] - in[inO + 2] * this.trig[A + 1];
            inO -= 4;
        }
        final float[] xxx = this.mdct_kernel(x, w, this.n, n2, n2, n3);
        int xx = 0;
        int B = n2;
        int o1 = n2;
        int o2 = o1 - 1;
        int o3 = n2 + n2;
        int o4 = o3 - 1;
        for (int j = 0; j < n2; ++j) {
            final float temp1 = xxx[xx] * this.trig[B + 1] - xxx[xx + 1] * this.trig[B];
            final float temp2 = -(xxx[xx] * this.trig[B] + xxx[xx + 1] * this.trig[B + 1]);
            out[o1] = -temp1;
            out[o2] = temp1;
            out[o4] = (out[o3] = temp2);
            ++o1;
            --o2;
            ++o3;
            --o4;
            xx += 2;
            B += 2;
        }
    }
    
    private float[] mdct_kernel(float[] x, float[] w, final int n, final int n2, final int n4, final int n8) {
        float x2;
        float x3;
        for (int xA = n4, xB = 0, A = n2, i = 0; i < n4; w[i++] = x2 * this.trig[A] + x3 * this.trig[A + 1], w[i] = x3 * this.trig[A] - x2 * this.trig[A + 1], w[n4 + i] = x[xA++] + x[xB++], ++i) {
            x2 = x[xA] - x[xB];
            w[n4 + i] = x[xA++] + x[xB++];
            x3 = x[xA] - x[xB];
            A -= 4;
        }
        for (int i = 0; i < this.log2n - 3; ++i) {
            int k0 = n >>> i + 2;
            final int k = 1 << i + 3;
            int wbase = n2 - 2;
            for (int A = 0, r = 0; r < k0 >>> 2; --k0, A += k, ++r) {
                int w2 = wbase;
                int w3 = w2 - (k0 >> 1);
                final float AEv = this.trig[A];
                final float AOv = this.trig[A + 1];
                wbase -= 2;
                ++k0;
                for (int s = 0; s < 2 << i; ++s) {
                    final float wB = w[w2] - w[w3];
                    x[w2] = w[w2] + w[w3];
                    final float wA = w[++w2] - w[++w3];
                    x[w2] = w[w2] + w[w3];
                    x[w3] = wA * AEv - wB * AOv;
                    x[w3 - 1] = wB * AEv + wA * AOv;
                    w2 -= k0;
                    w3 -= k0;
                }
            }
            final float[] temp = w;
            w = x;
            x = temp;
        }
        int C = n;
        int bit = 0;
        int x4 = 0;
        int x5 = n2 - 1;
        for (int j = 0; j < n8; ++j) {
            final int t1 = this.bitrev[bit++];
            final int t2 = this.bitrev[bit++];
            final float wA2 = w[t1] - w[t2 + 1];
            final float wB2 = w[t1 - 1] + w[t2];
            final float wC = w[t1] + w[t2 + 1];
            final float wD = w[t1 - 1] - w[t2];
            final float wACE = wA2 * this.trig[C];
            final float wBCE = wB2 * this.trig[C++];
            final float wACO = wA2 * this.trig[C];
            final float wBCO = wB2 * this.trig[C++];
            x[x4++] = (wC + wACO + wBCE) * 0.5f;
            x[x5--] = (-wD + wBCO - wACE) * 0.5f;
            x[x4++] = (wD + wBCO - wACE) * 0.5f;
            x[x5--] = (wC - wACO - wBCE) * 0.5f;
        }
        return x;
    }
    
    static {
        MDCT_0 = new Mdct(0);
        MDCT_256 = new Mdct(256);
        MDCT_2048 = new Mdct(2048);
    }
}
