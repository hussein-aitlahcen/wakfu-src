package com.jcraft.jorbis;

import com.jcraft.jogg.*;

class StaticCodeBook
{
    int dim;
    int entries;
    int[] lengthlist;
    private int maptype;
    private int q_min;
    private int q_delta;
    private int q_quant;
    private int q_sequencep;
    private int[] quantlist;
    private static final int VQ_FMAN = 21;
    private static final int VQ_FEXP_BIAS = 768;
    
    int unpack(final Buffer opb) {
        if (opb.read(24) != 5653314) {
            this.clear();
            return -1;
        }
        this.dim = opb.read(16);
        this.entries = opb.read(24);
        if (this.entries == -1) {
            this.clear();
            return -1;
        }
        switch (opb.read(1)) {
            case 0: {
                this.lengthlist = new int[this.entries];
                if (opb.read(1) != 0) {
                    for (int i = 0; i < this.entries; ++i) {
                        if (opb.read(1) != 0) {
                            final int num = opb.read(5);
                            if (num == -1) {
                                this.clear();
                                return -1;
                            }
                            this.lengthlist[i] = num + 1;
                        }
                        else {
                            this.lengthlist[i] = 0;
                        }
                    }
                    break;
                }
                for (int i = 0; i < this.entries; ++i) {
                    final int num = opb.read(5);
                    if (num == -1) {
                        this.clear();
                        return -1;
                    }
                    this.lengthlist[i] = num + 1;
                }
                break;
            }
            case 1: {
                int length = opb.read(5) + 1;
                this.lengthlist = new int[this.entries];
                int i = 0;
                while (i < this.entries) {
                    final int num2 = opb.read(Util.ilog(this.entries - i));
                    if (num2 == -1) {
                        this.clear();
                        return -1;
                    }
                    for (int j = 0; j < num2; ++j, ++i) {
                        this.lengthlist[i] = length;
                    }
                    ++length;
                }
                break;
            }
            default: {
                return -1;
            }
        }
        switch (this.maptype = opb.read(4)) {
            case 0: {
                break;
            }
            case 1:
            case 2: {
                this.q_min = opb.read(32);
                this.q_delta = opb.read(32);
                this.q_quant = opb.read(4) + 1;
                this.q_sequencep = opb.read(1);
                int quantvals = 0;
                switch (this.maptype) {
                    case 1: {
                        quantvals = this.maptype1_quantvals();
                        break;
                    }
                    case 2: {
                        quantvals = this.entries * this.dim;
                        break;
                    }
                }
                this.quantlist = new int[quantvals];
                for (int i = 0; i < quantvals; ++i) {
                    this.quantlist[i] = opb.read(this.q_quant);
                }
                if (this.quantlist[quantvals - 1] == -1) {
                    this.clear();
                    return -1;
                }
                break;
            }
            default: {
                this.clear();
                return -1;
            }
        }
        return 0;
    }
    
    private int maptype1_quantvals() {
        int vals = (int)Math.floor(Math.pow(this.entries, 1.0 / this.dim));
        while (true) {
            int acc = 1;
            int acc2 = 1;
            for (int i = 0; i < this.dim; ++i) {
                acc *= vals;
                acc2 *= vals + 1;
            }
            if (acc <= this.entries && acc2 > this.entries) {
                break;
            }
            if (acc > this.entries) {
                --vals;
            }
            else {
                ++vals;
            }
        }
        return vals;
    }
    
    void clear() {
    }
    
    float[] unquantize() {
        if (this.maptype == 1 || this.maptype == 2) {
            final float mindel = float32_unpack(this.q_min);
            final float delta = float32_unpack(this.q_delta);
            final float[] r = new float[this.entries * this.dim];
            switch (this.maptype) {
                case 1: {
                    final int quantvals = this.maptype1_quantvals();
                    for (int j = 0; j < this.entries; ++j) {
                        float last = 0.0f;
                        int indexdiv = 1;
                        final int jdim = j * this.dim;
                        for (int k = 0; k < this.dim; ++k) {
                            final int index = j / indexdiv % quantvals;
                            final float val = Math.abs(this.quantlist[index]) * delta + mindel + last;
                            if (this.q_sequencep != 0) {
                                last = val;
                            }
                            r[jdim + k] = val;
                            indexdiv *= quantvals;
                        }
                    }
                    break;
                }
                case 2: {
                    for (int j = 0; j < this.entries; ++j) {
                        float last = 0.0f;
                        final int jdim2 = j * this.dim;
                        for (int i = 0; i < this.dim; ++i) {
                            final int jdimk = jdim2 + i;
                            final float val2 = Math.abs(this.quantlist[jdimk]) * delta + mindel + last;
                            if (this.q_sequencep != 0) {
                                last = val2;
                            }
                            r[jdimk] = val2;
                        }
                    }
                    break;
                }
            }
            return r;
        }
        return null;
    }
    
    private static float float32_unpack(final int val) {
        float mant = val & 0x1FFFFF;
        final float exp = (val & 0x7FE00000) >>> 21;
        if ((val & Integer.MIN_VALUE) != 0x0) {
            mant = -mant;
        }
        return ldexp(mant, (int)exp - 20 - 768);
    }
    
    private static float ldexp(final float foo, final int e) {
        return (float)(foo * Math.pow(2.0, e));
    }
}
