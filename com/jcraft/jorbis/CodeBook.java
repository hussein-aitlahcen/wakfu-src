package com.jcraft.jorbis;

import com.jcraft.jogg.*;

class CodeBook
{
    final int dim;
    private final float[] valuelist;
    private final DecodeAux decode_tree;
    private int[] t;
    
    CodeBook(final StaticCodeBook c) {
        super();
        this.t = new int[15];
        this.dim = c.dim;
        this.valuelist = c.unquantize();
        this.decode_tree = DecodeAux.makeDecodeTree(c);
        if (this.decode_tree == null) {
            clear();
        }
    }
    
    int valuesize() {
        return this.decode_tree.ptr0.length;
    }
    
    synchronized int decodevs_add(final float[] a, final int offset, final Buffer b, final int n) {
        final int step = n / this.dim;
        if (this.t.length < step) {
            this.t = new int[step];
        }
        for (int i = 0; i < step; ++i) {
            final int entry = this.decode(b);
            if (entry == -1) {
                return -1;
            }
            this.t[i] = entry * this.dim;
        }
        for (int i = 0, o = 0; i < this.dim; ++i, o += step) {
            for (int j = 0; j < step; ++j) {
                final int n2 = offset + o + j;
                a[n2] += this.valuelist[this.t[j] + i];
            }
        }
        return 0;
    }
    
    int decodev_add(final float[] a, final int offset, final Buffer b, final int n) {
        if (this.dim > 8) {
            int i = 0;
            while (i < n) {
                final int entry = this.decode(b);
                if (entry == -1) {
                    return -1;
                }
                int n2;
                for (int t = entry * this.dim, j = 0; j < this.dim; a[n2] += this.valuelist[t + j++]) {
                    n2 = offset + i++;
                }
            }
        }
        else {
            int i = 0;
            while (i < n) {
                final int entry = this.decode(b);
                if (entry == -1) {
                    return -1;
                }
                final int t = entry * this.dim;
                int j = 0;
                switch (this.dim) {
                    case 8: {
                        final int n3 = offset + i++;
                        a[n3] += this.valuelist[t + j++];
                    }
                    case 7: {
                        final int n4 = offset + i++;
                        a[n4] += this.valuelist[t + j++];
                    }
                    case 6: {
                        final int n5 = offset + i++;
                        a[n5] += this.valuelist[t + j++];
                    }
                    case 5: {
                        final int n6 = offset + i++;
                        a[n6] += this.valuelist[t + j++];
                    }
                    case 4: {
                        final int n7 = offset + i++;
                        a[n7] += this.valuelist[t + j++];
                    }
                    case 3: {
                        final int n8 = offset + i++;
                        a[n8] += this.valuelist[t + j++];
                    }
                    case 2: {
                        final int n9 = offset + i++;
                        a[n9] += this.valuelist[t + j++];
                    }
                    case 1: {
                        final int n10 = offset + i++;
                        a[n10] += this.valuelist[t + j++];
                        continue;
                    }
                }
            }
        }
        return 0;
    }
    
    int decodev_set(final float[] a, final int offset, final Buffer b, final int n) {
        int i = 0;
        while (i < n) {
            final int entry = this.decode(b);
            if (entry == -1) {
                return -1;
            }
            for (int t = entry * this.dim, j = 0; j < this.dim; a[offset + i++] = this.valuelist[t + j++]) {}
        }
        return 0;
    }
    
    int decodevv_add(final float[][] a, final int offset, final int ch, final Buffer b, final int n) {
        int chptr = 0;
        int i = offset / ch;
        while (i < (offset + n) / ch) {
            final int entry = this.decode(b);
            if (entry == -1) {
                return -1;
            }
            final int t = entry * this.dim;
            for (int j = 0; j < this.dim; ++j) {
                final float[] array = a[chptr++];
                final int n2 = i;
                array[n2] += this.valuelist[t + j];
                if (chptr == ch) {
                    chptr = 0;
                    ++i;
                }
            }
        }
        return 0;
    }
    
    int decode(final Buffer b) {
        int ptr = 0;
        final DecodeAux t = this.decode_tree;
        final int lok = b.look(t.tabn);
        if (lok >= 0) {
            ptr = t.tab[lok];
            b.adv(t.tabl[lok]);
            if (ptr <= 0) {
                return -ptr;
            }
        }
        do {
            switch (b.read1()) {
                case 0: {
                    ptr = t.ptr0[ptr];
                    continue;
                }
                case 1: {
                    ptr = t.ptr1[ptr];
                    continue;
                }
                default: {
                    return -1;
                }
            }
        } while (ptr > 0);
        return -ptr;
    }
    
    static void clear() {
    }
    
    private static int[] make_words(final int[] l, final int n) {
        final int[] marker = new int[33];
        final int[] r = new int[n];
        for (int i = 0; i < n; ++i) {
            final int length = l[i];
            if (length > 0) {
                int entry = marker[length];
                if (length < 32 && entry >>> length != 0) {
                    return null;
                }
                r[i] = entry;
                int j = length;
                while (j > 0) {
                    if ((marker[j] & 0x1) != 0x0) {
                        if (j == 1) {
                            final int[] array = marker;
                            final int n2 = 1;
                            ++array[n2];
                            break;
                        }
                        marker[j] = marker[j - 1] << 1;
                        break;
                    }
                    else {
                        final int[] array2 = marker;
                        final int n3 = j;
                        ++array2[n3];
                        --j;
                    }
                }
                for (j = length + 1; j < 33 && marker[j] >>> 1 == entry; entry = marker[j], marker[j] = marker[j - 1] << 1, ++j) {}
            }
        }
        for (int i = 0; i < n; ++i) {
            int temp = 0;
            for (int k = 0; k < l[i]; ++k) {
                temp <<= 1;
                temp |= (r[i] >>> k & 0x1);
            }
            r[i] = temp;
        }
        return r;
    }
    
    private static class DecodeAux
    {
        final int[] tab;
        final int[] tabl;
        final int tabn;
        final int[] ptr0;
        final int[] ptr1;
        
        DecodeAux(final int entriesCount, final int[] codelist, final int[] bookLengthList) {
            super();
            this.ptr0 = new int[entriesCount * 2];
            this.ptr1 = new int[entriesCount * 2];
            int top = 0;
            for (int i = 0; i < entriesCount; ++i) {
                final int lengthList = bookLengthList[i];
                if (lengthList > 0) {
                    int ptr = 0;
                    int j;
                    for (j = 0; j < lengthList - 1; ++j) {
                        final int bit = codelist[i] >>> j & 0x1;
                        if (bit == 0) {
                            if (this.ptr0[ptr] == 0) {
                                this.ptr0[ptr] = ++top;
                            }
                            ptr = this.ptr0[ptr];
                        }
                        else {
                            if (this.ptr1[ptr] == 0) {
                                this.ptr1[ptr] = ++top;
                            }
                            ptr = this.ptr1[ptr];
                        }
                    }
                    if ((codelist[i] >>> j & 0x1) == 0x0) {
                        this.ptr0[ptr] = -i;
                    }
                    else {
                        this.ptr1[ptr] = -i;
                    }
                }
            }
            this.tabn = computeTabn(entriesCount);
            final int n = 1 << this.tabn;
            this.tab = new int[n];
            this.tabl = new int[n];
            for (int k = 0; k < n; ++k) {
                int j;
                int p;
                for (p = 0, j = 0; j < this.tabn && (p > 0 || j == 0); p = (((k & 1 << j) != 0x0) ? this.ptr1[p] : this.ptr0[p]), ++j) {}
                this.tab[k] = p;
                this.tabl[k] = j;
            }
        }
        
        private static int computeTabn(final int entriesCount) {
            final int tabn = Util.ilog(entriesCount) - 4;
            return (tabn < 5) ? 5 : tabn;
        }
        
        static DecodeAux makeDecodeTree(final StaticCodeBook c) {
            final int entriesCount = c.entries;
            final int[] codelist = make_words(c.lengthlist, entriesCount);
            if (codelist == null) {
                return null;
            }
            return new DecodeAux(entriesCount, codelist, c.lengthlist);
        }
    }
}
