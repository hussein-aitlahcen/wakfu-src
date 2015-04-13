package com.jcraft.jorbis;

import com.jcraft.jogg.*;

public class Block
{
    float[][] pcm;
    final Buffer opb;
    int lW;
    int W;
    int nW;
    int pcmend;
    int mode;
    int eofflag;
    long granulepos;
    long sequence;
    DspState vd;
    
    public Block(final DspState vd) {
        super();
        this.opb = new Buffer();
        this.vd = vd;
    }
    
    public void init(final DspState vd) {
        this.vd = vd;
    }
    
    public int clear() {
        return 0;
    }
    
    public int synthesis(final Packet op) {
        final Info vi = this.vd.vi;
        this.opb.wrap(op.packet_base, op.packet, op.bytes);
        if (this.opb.read(1) != 0) {
            return -1;
        }
        final int _mode = this.opb.read(this.vd.modebits);
        if (_mode == -1) {
            return -1;
        }
        this.mode = _mode;
        this.W = vi.mode_param[this.mode].blockflag;
        if (this.W != 0) {
            this.lW = this.opb.read(1);
            this.nW = this.opb.read(1);
            if (this.nW == -1) {
                return -1;
            }
        }
        else {
            this.lW = 0;
            this.nW = 0;
        }
        this.granulepos = op.granulepos;
        this.sequence = op.packetno - 3L;
        this.eofflag = op.e_o_s;
        this.pcmend = vi.blocksizes[this.W];
        if (this.pcm == null || this.pcm.length < vi.channelsCount) {
            this.pcm = new float[vi.channelsCount][];
        }
        for (int i = 0; i < vi.channelsCount; ++i) {
            if (this.pcm[i] == null || this.pcm[i].length < this.pcmend) {
                this.pcm[i] = new float[this.pcmend];
            }
            else {
                for (int j = 0; j < this.pcmend; ++j) {
                    this.pcm[i][j] = 0.0f;
                }
            }
        }
        assert vi.m_maps[vi.mode_param[this.mode].mapping].m_type == 0;
        return Mapping0.getInstance().inverse(this, this.vd.mode[this.mode]);
    }
}
