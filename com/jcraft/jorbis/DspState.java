package com.jcraft.jorbis;

public class DspState
{
    private static final float M_PI = 3.1415927f;
    Info vi;
    int modebits;
    private float[][] pcm;
    private int pcm_storage;
    private int pcm_current;
    private int pcm_returned;
    private int lW;
    private int W;
    private int centerW;
    private long granulepos;
    private long sequence;
    private Lookup lookup0;
    private Lookup lookup1;
    private Mdct transform0;
    private Mdct transform1;
    CodeBook[] fullbooks;
    Object[] mode;
    private static final Util.LightMap<WindowKey, float[]> WINDOW_MAP;
    
    public void reinit() {
    }
    
    Mdct getTransform(final int window) {
        assert window >= 0 && window < 2;
        return (window == 0) ? this.transform0 : this.transform1;
    }
    
    public float[] getWindow(final int window, final int lW, final int nW, final int windowtype) {
        assert window == 1;
        assert lW == 1;
        assert nW == 1;
        assert windowtype == 0;
        if (window == 0) {
            return this.lookup0.getWindow(lW, nW);
        }
        return this.lookup1.getWindow(lW, nW);
    }
    
    private static float[] window(final int type, final int window, final int left, final int right) {
        if (type != 0) {
            return null;
        }
        final WindowKey key = new WindowKey(window, left, right);
        float[] precomputed;
        synchronized (DspState.WINDOW_MAP) {
            precomputed = DspState.WINDOW_MAP.getValue(key);
            if (precomputed == null) {
                precomputed = precomputed(type, window, left, right);
                DspState.WINDOW_MAP.put(key, precomputed);
            }
        }
        return precomputed;
    }
    
    private static float[] precomputed(final int type, final int window, final int left, final int right) {
        final float[] ret = new float[window];
        final int leftbegin = window / 4 - left / 2;
        final int rightbegin = window - window / 4 - right / 2;
        for (int i = 0; i < left; ++i) {
            float x = (float)((i + 0.5) / left * 3.1415927410125732 / 2.0);
            x = (float)Math.sin(x);
            x *= x;
            x *= 1.5707963705062866;
            x = (float)Math.sin(x);
            ret[i + leftbegin] = x;
        }
        for (int i = leftbegin + left; i < rightbegin; ++i) {
            ret[i] = 1.0f;
        }
        for (int i = 0; i < right; ++i) {
            float x = (float)((right - i - 0.5) / right * 3.1415927410125732 / 2.0);
            x = (float)Math.sin(x);
            x *= x;
            x *= 1.5707963705062866;
            x = (float)Math.sin(x);
            ret[i + rightbegin] = x;
        }
        return ret;
    }
    
    int init(final Info vi, final boolean encp) {
        this.vi = vi;
        this.modebits = Util.ilog2(vi.modes);
        this.transform0 = Mdct.create(vi.blocksizes[0]);
        this.transform1 = Mdct.create(vi.blocksizes[1]);
        this.lookup0 = small(vi.blocksizes);
        this.lookup1 = big(vi.blocksizes);
        this.fullbooks = new CodeBook[vi.books];
        for (int i = 0; i < vi.books; ++i) {
            this.fullbooks[i] = new CodeBook(vi.book_param[i]);
        }
        this.pcm_storage = 0;
        this.pcm = new float[vi.channelsCount][];
        this.lW = 0;
        this.W = 0;
        this.centerW = vi.blocksizes[1] / 2;
        this.pcm_current = this.centerW;
        this.mode = new Mapping0.LookMapping0[vi.modes];
        for (int i = 0; i < vi.modes; ++i) {
            final int mapnum = vi.mode_param[i].mapping;
            assert vi.m_maps[mapnum].m_type == 0;
            final Object[] mode = this.mode;
            final int n = i;
            Mapping0.getInstance();
            mode[n] = Mapping0.look(this, vi.mode_param[i], vi.m_maps[mapnum].m_param);
        }
        return 0;
    }
    
    public int synthesis_init(final Info vi) {
        this.init(vi, false);
        this.pcm_returned = this.centerW;
        this.centerW -= vi.blocksizes[this.W] / 4 + vi.blocksizes[this.lW] / 4;
        this.granulepos = -1L;
        this.sequence = -1L;
        return 0;
    }
    
    public int synthesis_blockin(final Block vb) {
        if (this.centerW > this.vi.blocksizes[1] / 2 && this.pcm_returned > 8192) {
            int shiftPCM = this.centerW - this.vi.blocksizes[1] / 2;
            shiftPCM = ((this.pcm_returned < shiftPCM) ? this.pcm_returned : shiftPCM);
            this.pcm_current -= shiftPCM;
            this.centerW -= shiftPCM;
            this.pcm_returned -= shiftPCM;
            if (shiftPCM != 0) {
                for (int i = 0; i < this.vi.channelsCount; ++i) {
                    System.arraycopy(this.pcm[i], shiftPCM, this.pcm[i], 0, this.pcm_current);
                }
            }
        }
        this.lW = this.W;
        this.W = vb.W;
        if (this.sequence + 1L != vb.sequence) {
            this.granulepos = -1L;
        }
        this.sequence = vb.sequence;
        final int sizeW = this.vi.blocksizes[this.W];
        int _centerW = this.centerW + this.vi.blocksizes[this.lW] / 4 + sizeW / 4;
        final int beginW = _centerW - sizeW / 2;
        final int endW = beginW + sizeW;
        int beginSl = 0;
        int endSl = 0;
        if (endW > this.pcm_storage) {
            this.pcm_storage = Math.max(endW + this.vi.blocksizes[1], 16384);
            for (int j = 0; j < this.vi.channelsCount; ++j) {
                final float[] foo = new float[this.pcm_storage];
                if (this.pcm[j] != null) {
                    System.arraycopy(this.pcm[j], 0, foo, 0, this.pcm[j].length);
                }
                this.pcm[j] = foo;
            }
        }
        switch (this.W) {
            case 0: {
                beginSl = 0;
                endSl = this.vi.blocksizes[0] / 2;
                break;
            }
            case 1: {
                beginSl = this.vi.blocksizes[1] / 4 - this.vi.blocksizes[this.lW] / 4;
                endSl = beginSl + this.vi.blocksizes[this.lW] / 2;
                break;
            }
        }
        for (int k = 0; k < this.vi.channelsCount; ++k) {
            final int _pcm = beginW;
            for (int l = beginSl; l < endSl; ++l) {
                final float[] array = this.pcm[k];
                final int n = _pcm + l;
                array[n] += vb.pcm[k][l];
            }
            System.arraycopy(vb.pcm[k], endSl, this.pcm[k], _pcm + endSl, sizeW - endSl);
        }
        if (this.granulepos == -1L) {
            this.granulepos = vb.granulepos;
        }
        else {
            this.granulepos += _centerW - this.centerW;
            if (vb.granulepos != -1L && this.granulepos != vb.granulepos) {
                if (this.granulepos > vb.granulepos && vb.eofflag != 0) {
                    _centerW -= (int)(this.granulepos - vb.granulepos);
                }
                this.granulepos = vb.granulepos;
            }
        }
        this.centerW = _centerW;
        this.pcm_current = endW;
        return 0;
    }
    
    public int synthesis_pcmout(final float[][][] _pcm, final int[] index) {
        if (this.pcm_returned >= this.centerW) {
            return 0;
        }
        if (_pcm != null) {
            for (int i = 0; i < this.vi.channelsCount; ++i) {
                index[i] = this.pcm_returned;
            }
            _pcm[0] = this.pcm;
        }
        return this.centerW - this.pcm_returned;
    }
    
    public int synthesis_read(final int bytes) {
        if (bytes != 0 && this.pcm_returned + bytes > this.centerW) {
            return -1;
        }
        this.pcm_returned += bytes;
        return 0;
    }
    
    public void clear() {
    }
    
    static {
        WINDOW_MAP = new Util.LightMap<WindowKey, float[]>();
    }
    
    private static class Lookup
    {
        final float[][] window;
        
        private Lookup() {
            super();
            this.window = new float[4][];
        }
        
        private static Lookup small(final int[] blocksizes) {
            final Lookup lookup = new Lookup();
            lookup.window[0] = window(0, blocksizes[0], blocksizes[0] / 2, blocksizes[0] / 2);
            lookup.window[1] = lookup.window[0];
            lookup.window[2] = lookup.window[0];
            lookup.window[3] = lookup.window[0];
            return lookup;
        }
        
        private static Lookup big(final int[] blocksizes) {
            final Lookup lookup = new Lookup();
            lookup.window[0] = window(0, blocksizes[1], blocksizes[0] / 2, blocksizes[0] / 2);
            lookup.window[1] = window(0, blocksizes[1], blocksizes[0] / 2, blocksizes[1] / 2);
            lookup.window[2] = window(0, blocksizes[1], blocksizes[1] / 2, blocksizes[0] / 2);
            lookup.window[3] = window(0, blocksizes[1], blocksizes[1] / 2, blocksizes[1] / 2);
            return lookup;
        }
        
        float[] getWindow(final int lW, final int nW) {
            return this.window[lW << 1 | nW];
        }
    }
    
    private static class WindowKey
    {
        private final int window;
        private final int left;
        private final int right;
        
        private WindowKey(final int window, final int left, final int right) {
            super();
            this.window = window;
            this.left = left;
            this.right = right;
        }
        
        @Override
        public boolean equals(final Object o) {
            final WindowKey windowKey = (WindowKey)o;
            return this.left == windowKey.left && this.right == windowKey.right && this.window == windowKey.window;
        }
        
        @Override
        public int hashCode() {
            return this.window << 20 | this.left << 10 | this.right;
        }
    }
}
