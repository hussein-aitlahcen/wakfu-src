package com.jcraft.jorbis;

import com.jcraft.jogg.*;

class Mapping0
{
    private float[][] pcmbundle;
    private int[] zerobundle;
    private int[] nonzero;
    private Object[] floormemo;
    private static final Mapping0 m_instance;
    
    public static Mapping0 getInstance() {
        return Mapping0.m_instance;
    }
    
    static void free_info(final Object imap) {
    }
    
    static LookMapping0 look(final DspState vd, final InfoMode vm, final InfoMapping0 m) {
        final Info vi = vd.vi;
        final LookMapping0 look = new LookMapping0();
        look.map = m;
        look.mode = vm;
        look.floor_look = new Object[m.submaps];
        look.residue_look = new Object[m.submaps];
        look.floor_func = new FuncFloor[m.submaps];
        look.residue_func = new FuncResidue[m.submaps];
        for (int i = 0; i < m.submaps; ++i) {
            final int floornum = m.floorsubmap[i];
            final int resnum = m.residuesubmap[i];
            look.floor_func[i] = FuncFloor.floor_P[vi.m_floors[floornum].m_type];
            look.floor_look[i] = look.floor_func[i].look(vd, vm, vi.m_floors[floornum].m_param);
            look.residue_func[i] = FuncResidue.residue_P[vi.m_residues[resnum].m_type];
            look.residue_look[i] = look.residue_func[i].look(vd, vm, vi.m_residues[resnum].m_param);
        }
        return look;
    }
    
    static InfoMapping0 unpack(final Info vi, final Buffer opb) {
        int submaps;
        if (opb.read(1) != 0) {
            submaps = opb.read(4) + 1;
        }
        else {
            submaps = 1;
        }
        final InfoMapping0 info = new InfoMapping0(submaps);
        if (opb.read(1) != 0) {
            info.setCouplingSteps(opb.read(8) + 1);
            for (int i = 0; i < info.coupling_steps; ++i) {
                final int[] coupling_mag = info.coupling_mag;
                final int n = i;
                final int read = opb.read(Util.ilog2(vi.channelsCount));
                coupling_mag[n] = read;
                final int testM = read;
                final int[] coupling_ang = info.coupling_ang;
                final int n2 = i;
                final int read2 = opb.read(Util.ilog2(vi.channelsCount));
                coupling_ang[n2] = read2;
                final int testA = read2;
                if (testM < 0 || testA < 0 || testM == testA || testM >= vi.channelsCount || testA >= vi.channelsCount) {
                    info.free();
                    return null;
                }
            }
        }
        if (opb.read(2) > 0) {
            info.free();
            return null;
        }
        if (info.submaps > 1) {
            for (int i = 0; i < vi.channelsCount; ++i) {
                info.chmuxlist[i] = opb.read(4);
                if (info.chmuxlist[i] >= info.submaps) {
                    info.free();
                    return null;
                }
            }
        }
        for (int i = 0; i < info.submaps; ++i) {
            final int timesubmap = opb.read(8);
            if (timesubmap >= vi.times) {
                info.free();
                return null;
            }
            info.floorsubmap[i] = opb.read(8);
            if (info.floorsubmap[i] >= vi.flooCounts) {
                info.free();
                return null;
            }
            info.residuesubmap[i] = opb.read(8);
            if (info.residuesubmap[i] >= vi.residueCount) {
                info.free();
                return null;
            }
        }
        return info;
    }
    
    synchronized int inverse(final Block vb, final Object l) {
        final DspState vd = vb.vd;
        final Info vi = vd.vi;
        final LookMapping0 look = (LookMapping0)l;
        final InfoMapping0 info = look.map;
        final InfoMode mode = look.mode;
        final int pcmend = vi.blocksizes[vb.W];
        vb.pcmend = pcmend;
        final int n = pcmend;
        final float[] window = vd.getWindow(vb.W, vb.lW, vb.nW, mode.windowtype);
        if (this.pcmbundle == null || this.pcmbundle.length < vi.channelsCount) {
            this.pcmbundle = new float[vi.channelsCount][];
            this.nonzero = new int[vi.channelsCount];
            this.zerobundle = new int[vi.channelsCount];
            this.floormemo = new Object[vi.channelsCount];
        }
        for (int i = 0; i < vi.channelsCount; ++i) {
            final float[] pcm = vb.pcm[i];
            final int submap = info.chmuxlist[i];
            this.floormemo[i] = look.floor_func[submap].inverse1(vb, look.floor_look[submap], this.floormemo[i]);
            if (this.floormemo[i] != null) {
                this.nonzero[i] = 1;
            }
            else {
                this.nonzero[i] = 0;
            }
            for (int j = 0; j < n / 2; ++j) {
                pcm[j] = 0.0f;
            }
        }
        for (int i = 0; i < info.coupling_steps; ++i) {
            if (this.nonzero[info.coupling_mag[i]] != 0 || this.nonzero[info.coupling_ang[i]] != 0) {
                this.nonzero[info.coupling_mag[i]] = 1;
                this.nonzero[info.coupling_ang[i]] = 1;
            }
        }
        for (int i = 0; i < info.submaps; ++i) {
            int ch_in_bundle = 0;
            for (int k = 0; k < vi.channelsCount; ++k) {
                if (info.chmuxlist[k] == i) {
                    if (this.nonzero[k] != 0) {
                        this.zerobundle[ch_in_bundle] = 1;
                    }
                    else {
                        this.zerobundle[ch_in_bundle] = 0;
                    }
                    this.pcmbundle[ch_in_bundle++] = vb.pcm[k];
                }
            }
            look.residue_func[i].inverse(vb, look.residue_look[i], this.pcmbundle, this.zerobundle, ch_in_bundle);
        }
        for (int i = info.coupling_steps - 1; i >= 0; --i) {
            final float[] pcmM = vb.pcm[info.coupling_mag[i]];
            final float[] pcmA = vb.pcm[info.coupling_ang[i]];
            for (int j = 0; j < n / 2; ++j) {
                final float mag = pcmM[j];
                final float ang = pcmA[j];
                if (mag > 0.0f) {
                    if (ang > 0.0f) {
                        pcmA[j] = (pcmM[j] = mag) - ang;
                    }
                    else {
                        pcmM[j] = (pcmA[j] = mag) + ang;
                    }
                }
                else if (ang > 0.0f) {
                    pcmA[j] = (pcmM[j] = mag) + ang;
                }
                else {
                    pcmM[j] = (pcmA[j] = mag) - ang;
                }
            }
        }
        for (int i = 0; i < vi.channelsCount; ++i) {
            final int submap2 = info.chmuxlist[i];
            look.floor_func[submap2].inverse2(vb, look.floor_look[submap2], this.floormemo[i], vb.pcm[i]);
        }
        for (int i = 0; i < vi.channelsCount; ++i) {
            final float[] pcm = vb.pcm[i];
            vd.getTransform(vb.W).backward(pcm, pcm);
        }
        for (int i = 0; i < vi.channelsCount; ++i) {
            final float[] pcm = vb.pcm[i];
            if (this.nonzero[i] != 0) {
                for (int k = 0; k < n; ++k) {
                    final float[] array = pcm;
                    final int n2 = k;
                    array[n2] *= window[k];
                }
            }
            else {
                for (int k = 0; k < n; ++k) {
                    pcm[k] = 0.0f;
                }
            }
        }
        return 0;
    }
    
    static {
        m_instance = new Mapping0();
    }
    
    static class InfoMapping0
    {
        final int submaps;
        int[] chmuxlist;
        int[] floorsubmap;
        int[] residuesubmap;
        int coupling_steps;
        int[] coupling_mag;
        int[] coupling_ang;
        
        InfoMapping0(final int submapCount) {
            super();
            this.chmuxlist = new int[256];
            this.submaps = submapCount;
            this.floorsubmap = new int[submapCount];
            this.residuesubmap = new int[submapCount];
        }
        
        void free() {
            this.chmuxlist = null;
            this.floorsubmap = null;
            this.residuesubmap = null;
            this.coupling_mag = null;
            this.coupling_ang = null;
        }
        
        public void setCouplingSteps(final int steps) {
            this.coupling_steps = steps;
            this.coupling_mag = new int[steps];
            this.coupling_ang = new int[steps];
        }
    }
    
    static class LookMapping0
    {
        InfoMode mode;
        InfoMapping0 map;
        Object[] floor_look;
        Object[] residue_look;
        FuncFloor[] floor_func;
        FuncResidue[] residue_func;
    }
}
