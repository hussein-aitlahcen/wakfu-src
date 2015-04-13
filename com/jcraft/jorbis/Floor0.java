package com.jcraft.jorbis;

import com.jcraft.jogg.*;

class Floor0 extends FuncFloor<InfoFloor0, LookFloor0>
{
    @Override
    InfoFloor0 unpack(final Info vi, final Buffer opb) {
        final InfoFloor0 info = new InfoFloor0(opb);
        if (info.order < 1 || info.rate < 1 || info.barkmap < 1 || info.numbooks < 1) {
            return null;
        }
        for (int j = 0; j < info.numbooks; ++j) {
            info.books[j] = opb.read(8);
            if (info.books[j] < 0 || info.books[j] >= vi.books) {
                return null;
            }
        }
        return info;
    }
    
    @Override
    LookFloor0 look(final DspState vd, final InfoMode mi, final InfoFloor0 info) {
        final LookFloor0 look = new LookFloor0(info, vd.vi.blocksizes[mi.blockflag] / 2);
        final float scale = look.ln / toBARK((float)(info.rate / 2.0));
        for (int j = 0; j < look.n; ++j) {
            int val = (int)Math.floor(toBARK((float)(info.rate / 2.0 / look.n * j)) * scale);
            if (val >= look.ln) {
                val = look.ln;
            }
            look.linearmap[j] = val;
        }
        return look;
    }
    
    private static float toBARK(final float v) {
        return (float)(13.1 * Math.atan(7.4E-4 * v) + 2.24 * Math.atan(v * v * 1.85E-8) + 1.0E-4 * v);
    }
    
    @Override
    void free_info(final Object i) {
    }
    
    @Override
    float[] inverse1(final Block vb, final LookFloor0 look, final Object memo) {
        final InfoFloor0 info = look.vi;
        float[] lsp = null;
        if (memo instanceof float[]) {
            lsp = (float[])memo;
        }
        final int ampraw = vb.opb.read(info.ampbits);
        if (ampraw > 0) {
            final int maxval = (1 << info.ampbits) - 1;
            final float amp = ampraw / maxval * info.ampdB;
            final int booknum = vb.opb.read(Util.ilog(info.numbooks));
            if (booknum != -1 && booknum < info.numbooks) {
                final CodeBook b = vb.vd.fullbooks[info.books[booknum]];
                if (lsp == null || lsp.length < look.m + 1) {
                    lsp = new float[look.m + 1];
                }
                else {
                    for (int j = 0; j < lsp.length; ++j) {
                        lsp[j] = 0.0f;
                    }
                }
                for (int j = 0; j < look.m; j += b.dim) {
                    if (b.decodev_set(lsp, j, vb.opb, b.dim) == -1) {
                        return null;
                    }
                }
                float last = 0.0f;
                int i = 0;
                while (i < look.m) {
                    for (int k = 0; k < b.dim; ++k, ++i) {
                        final float[] array = lsp;
                        final int n = i;
                        array[n] += last;
                    }
                    last = lsp[i - 1];
                }
                lsp[look.m] = amp;
                return lsp;
            }
        }
        return null;
    }
    
    @Override
    int inverse2(final Block vb, final LookFloor0 look, final Object memo, final float[] out) {
        final InfoFloor0 info = look.vi;
        if (memo != null) {
            final float[] lsp = (float[])memo;
            final float amp = lsp[look.m];
            Lsp.lsp_to_curve(out, look.linearmap, look.n, look.ln, lsp, look.m, amp, info.ampdB);
            return 1;
        }
        for (int j = 0; j < look.n; ++j) {
            out[j] = 0.0f;
        }
        return 0;
    }
    
    static class InfoFloor0
    {
        final int order;
        final int rate;
        final int barkmap;
        final int ampbits;
        final int ampdB;
        final int numbooks;
        final int[] books;
        
        InfoFloor0(final Buffer opb) {
            super();
            this.books = new int[16];
            this.order = opb.read(8);
            this.rate = opb.read(16);
            this.barkmap = opb.read(16);
            this.ampbits = opb.read(6);
            this.ampdB = opb.read(8);
            this.numbooks = opb.read(4) + 1;
        }
    }
    
    static class LookFloor0
    {
        final int n;
        final int ln;
        final int m;
        final int[] linearmap;
        final InfoFloor0 vi;
        
        LookFloor0(final InfoFloor0 info, final int i) {
            super();
            this.m = info.order;
            this.n = i;
            this.ln = info.barkmap;
            this.vi = info;
            this.linearmap = new int[i];
        }
    }
}
