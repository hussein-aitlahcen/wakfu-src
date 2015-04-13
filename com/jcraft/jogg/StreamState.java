package com.jcraft.jogg;

public class StreamState
{
    private byte[] body_data;
    private int body_fill;
    private int body_returned;
    private int[] lacing_vals;
    private long[] granule_vals;
    private int lacing_storage;
    private int lacing_fill;
    private int lacing_packet;
    private int lacing_returned;
    private int serialno;
    private int pageno;
    private long packetno;
    
    public StreamState() {
        super();
        this.init();
    }
    
    private void init() {
        this.body_data = new byte[16384];
        this.lacing_storage = 1024;
        this.lacing_vals = new int[this.lacing_storage];
        this.granule_vals = new long[this.lacing_storage];
    }
    
    public void reinit() {
        this.reset();
    }
    
    public void init(final int serialno) {
        if (this.body_data == null) {
            this.init();
        }
        else {
            for (int i = 0; i < this.body_data.length; ++i) {
                this.body_data[i] = 0;
            }
            for (int i = 0; i < this.lacing_vals.length; ++i) {
                this.lacing_vals[i] = 0;
            }
            for (int i = 0; i < this.granule_vals.length; ++i) {
                this.granule_vals[i] = 0L;
            }
        }
        this.serialno = serialno;
    }
    
    public void clear() {
        this.body_data = null;
        this.lacing_vals = null;
        this.granule_vals = null;
    }
    
    private void body_expand(final int needed) {
        int body_storage = this.body_data.length;
        if (body_storage <= this.body_fill + needed) {
            body_storage += needed + 1024;
            final byte[] foo = new byte[body_storage];
            System.arraycopy(this.body_data, 0, foo, 0, this.body_data.length);
            this.body_data = foo;
        }
    }
    
    private void lacing_expand(final int needed) {
        if (this.lacing_storage <= this.lacing_fill + needed) {
            this.lacing_storage += needed + 32;
            final int[] foo = new int[this.lacing_storage];
            System.arraycopy(this.lacing_vals, 0, foo, 0, this.lacing_vals.length);
            this.lacing_vals = foo;
            final long[] bar = new long[this.lacing_storage];
            System.arraycopy(this.granule_vals, 0, bar, 0, this.granule_vals.length);
            this.granule_vals = bar;
        }
    }
    
    public int packetout(final Packet op) {
        int ptr = this.lacing_returned;
        if (this.lacing_packet <= ptr) {
            return 0;
        }
        if ((this.lacing_vals[ptr] & 0x400) != 0x0) {
            ++this.lacing_returned;
            ++this.packetno;
            return -1;
        }
        int size = this.lacing_vals[ptr] & 0xFF;
        op.packet_base = this.body_data;
        op.packet = this.body_returned;
        op.e_o_s = (this.lacing_vals[ptr] & 0x200);
        op.b_o_s = (this.lacing_vals[ptr] & 0x100);
        int bytes = 0;
        bytes += size;
        while (size == 255) {
            final int val = this.lacing_vals[++ptr];
            size = (val & 0xFF);
            if ((val & 0x200) != 0x0) {
                op.e_o_s = 512;
            }
            bytes += size;
        }
        op.packetno = this.packetno;
        op.granulepos = this.granule_vals[ptr];
        op.bytes = bytes;
        this.body_returned += bytes;
        this.lacing_returned = ptr + 1;
        ++this.packetno;
        return 1;
    }
    
    public int pagein(final Page og) {
        final byte[] header_base = og.header_base;
        final int header = og.header;
        final byte[] body_base = og.body_base;
        int body = og.body;
        int bodysize = og.body_len;
        final int version = og.version();
        final int continued = og.continued();
        int bos = og.bos();
        final int eos = og.eos();
        final long granulepos = og.granulepos();
        final int _serialno = og.serialno();
        final int _pageno = og.pageno();
        final int segments = header_base[header + 26] & 0xFF;
        final int lr = this.lacing_returned;
        final int br = this.body_returned;
        if (br != 0) {
            this.body_fill -= br;
            if (this.body_fill != 0) {
                System.arraycopy(this.body_data, br, this.body_data, 0, this.body_fill);
            }
            this.body_returned = 0;
        }
        if (lr != 0) {
            if (this.lacing_fill - lr != 0) {
                System.arraycopy(this.lacing_vals, lr, this.lacing_vals, 0, this.lacing_fill - lr);
                System.arraycopy(this.granule_vals, lr, this.granule_vals, 0, this.lacing_fill - lr);
            }
            this.lacing_fill -= lr;
            this.lacing_packet -= lr;
            this.lacing_returned = 0;
        }
        if (_serialno != this.serialno) {
            return -1;
        }
        if (version > 0) {
            return -1;
        }
        this.lacing_expand(segments + 1);
        int segptr = 0;
        if (_pageno != this.pageno) {
            for (int i = this.lacing_packet; i < this.lacing_fill; ++i) {
                this.body_fill -= (this.lacing_vals[i] & 0xFF);
            }
            this.lacing_fill = this.lacing_packet;
            if (this.pageno != -1) {
                this.lacing_vals[this.lacing_fill++] = 1024;
                ++this.lacing_packet;
            }
            if (continued != 0) {
                bos = 0;
                while (segptr < segments) {
                    final int val = header_base[header + 27 + segptr] & 0xFF;
                    body += val;
                    bodysize -= val;
                    if (val < 255) {
                        ++segptr;
                        break;
                    }
                    ++segptr;
                }
            }
        }
        if (bodysize != 0) {
            this.body_expand(bodysize);
            System.arraycopy(body_base, body, this.body_data, this.body_fill, bodysize);
            this.body_fill += bodysize;
        }
        int saved = -1;
        while (segptr < segments) {
            final int val2 = header_base[header + 27 + segptr] & 0xFF;
            this.lacing_vals[this.lacing_fill] = val2;
            this.granule_vals[this.lacing_fill] = -1L;
            if (bos != 0) {
                final int[] lacing_vals = this.lacing_vals;
                final int lacing_fill = this.lacing_fill;
                lacing_vals[lacing_fill] |= 0x100;
                bos = 0;
            }
            if (val2 < 255) {
                saved = this.lacing_fill;
            }
            ++this.lacing_fill;
            ++segptr;
            if (val2 < 255) {
                this.lacing_packet = this.lacing_fill;
            }
        }
        if (saved != -1) {
            this.granule_vals[saved] = granulepos;
        }
        if (eos != 0 && this.lacing_fill > 0) {
            final int[] lacing_vals2 = this.lacing_vals;
            final int n = this.lacing_fill - 1;
            lacing_vals2[n] |= 0x200;
        }
        this.pageno = _pageno + 1;
        return 0;
    }
    
    public int reset() {
        this.body_fill = 0;
        this.body_returned = 0;
        this.lacing_fill = 0;
        this.lacing_packet = 0;
        this.lacing_returned = 0;
        this.pageno = -1;
        this.packetno = 0L;
        return 0;
    }
}
