package com.jcraft.jorbis;

import com.jcraft.jogg.*;

public class Info
{
    private static final int VI_TIMEB = 1;
    private static final int VI_FLOORB = 2;
    private static final int VI_RESB = 3;
    private static final int VI_MAPB = 1;
    private static final int VI_WINDOWB = 1;
    public static final int BLOCK_SIZE_COUNT = 2;
    private int version;
    public int channelsCount;
    public int rate;
    private int bitrate_upper;
    private int bitrate_nominal;
    private int bitrate_lower;
    final int[] blocksizes;
    int modes;
    private int mapCount;
    int times;
    int flooCounts;
    int residueCount;
    int books;
    InfoMode[] mode_param;
    Util.TypeParamPair<Mapping0.InfoMapping0>[] m_maps;
    Util.TypeParamPair<FuncFloor>[] m_floors;
    Util.TypeParamPair<FuncResidue>[] m_residues;
    StaticCodeBook[] book_param;
    
    public Info() {
        super();
        this.blocksizes = new int[2];
        this.mode_param = null;
        this.book_param = null;
    }
    
    public void init() {
        this.rate = 0;
    }
    
    public void clear() {
        this.mode_param = null;
        for (int i = 0; i < this.mapCount; ++i) {
            assert this.m_maps[i].m_type == 0;
            Mapping0.getInstance();
            Mapping0.free_info(this.m_maps[i].m_param);
        }
        this.m_maps = null;
        for (int i = 0; i < this.flooCounts; ++i) {
            FuncFloor.freeInfo(this.m_floors[i]);
        }
        this.m_floors = null;
        for (int i = 0; i < this.residueCount; ++i) {
            FuncResidue.freeInfo(this.m_residues[i]);
        }
        this.m_residues = null;
        for (int i = 0; i < this.books; ++i) {
            if (this.book_param[i] != null) {
                this.book_param[i].clear();
            }
        }
        this.book_param = null;
    }
    
    int unpack_info(final Buffer opb) {
        this.version = opb.read(32);
        if (this.version != 0) {
            return -1;
        }
        this.channelsCount = opb.read(8);
        this.rate = opb.read(32);
        this.bitrate_upper = opb.read(32);
        this.bitrate_nominal = opb.read(32);
        this.bitrate_lower = opb.read(32);
        this.blocksizes[0] = 1 << opb.read(4);
        this.blocksizes[1] = 1 << opb.read(4);
        if (this.rate < 1 || this.channelsCount < 1 || this.blocksizes[0] < 8 || this.blocksizes[1] < this.blocksizes[0] || opb.read(1) != 1) {
            this.clear();
            return -1;
        }
        return 0;
    }
    
    int unpack_books(final Buffer opb) {
        this.books = opb.read(8) + 1;
        if (this.book_param == null || this.book_param.length != this.books) {
            this.book_param = new StaticCodeBook[this.books];
        }
        for (int i = 0; i < this.books; ++i) {
            this.book_param[i] = new StaticCodeBook();
            if (this.book_param[i].unpack(opb) != 0) {
                this.clear();
                return -1;
            }
        }
        this.times = opb.read(6) + 1;
        for (int i = 0; i < this.times; ++i) {
            final int time_type = opb.read(16);
            assert time_type == 0;
            if (time_type < 0 || time_type >= 1) {
                this.clear();
                return -1;
            }
        }
        this.flooCounts = opb.read(6) + 1;
        if (this.m_floors == null || this.m_floors.length != this.flooCounts) {
            this.m_floors = (Util.TypeParamPair<FuncFloor>[])new Util.TypeParamPair[this.flooCounts];
        }
        for (int i = 0; i < this.flooCounts; ++i) {
            final int type = opb.read(16);
            if (type < 0 || type >= 2) {
                this.clear();
                return -1;
            }
            final Object param = FuncFloor.floor_P[type].unpack(this, opb);
            if (param == null) {
                this.clear();
                return -1;
            }
            this.m_floors[i] = new Util.TypeParamPair<FuncFloor>(type, (FuncFloor)param);
        }
        this.residueCount = opb.read(6) + 1;
        if (this.m_residues == null || this.m_residues.length != this.residueCount) {
            this.m_residues = (Util.TypeParamPair<FuncResidue>[])new Util.TypeParamPair[this.residueCount];
        }
        for (int i = 0; i < this.residueCount; ++i) {
            final int type = opb.read(16);
            if (type < 0 || type >= 3) {
                this.clear();
                return -1;
            }
            final Object param = FuncResidue.residue_P[type].unpack(this, opb);
            if (param == null) {
                this.clear();
                return -1;
            }
            this.m_residues[i] = new Util.TypeParamPair<FuncResidue>(type, (FuncResidue)param);
        }
        this.mapCount = opb.read(6) + 1;
        if (this.m_maps == null || this.m_maps.length != this.mapCount) {
            this.m_maps = (Util.TypeParamPair<Mapping0.InfoMapping0>[])new Util.TypeParamPair[this.mapCount];
        }
        for (int i = 0; i < this.mapCount; ++i) {
            final int type = opb.read(16);
            if (type < 0 || type >= 1) {
                this.clear();
                return -1;
            }
            Mapping0.getInstance();
            final Mapping0.InfoMapping0 param2 = Mapping0.unpack(this, opb);
            if (param2 == null) {
                this.clear();
                return -1;
            }
            this.m_maps[i] = new Util.TypeParamPair<Mapping0.InfoMapping0>(type, param2);
        }
        this.modes = opb.read(6) + 1;
        if (this.mode_param == null || this.mode_param.length != this.modes) {
            this.mode_param = new InfoMode[this.modes];
        }
        for (int i = 0; i < this.modes; ++i) {
            this.mode_param[i] = new InfoMode();
            this.mode_param[i].blockflag = opb.read(1);
            this.mode_param[i].windowtype = opb.read(16);
            this.mode_param[i].transformtype = opb.read(16);
            this.mode_param[i].mapping = opb.read(8);
            if (this.mode_param[i].windowtype >= 1 || this.mode_param[i].transformtype >= 1 || this.mode_param[i].mapping >= this.mapCount) {
                this.clear();
                return -1;
            }
        }
        if (opb.read(1) != 1) {
            this.clear();
            return -1;
        }
        return 0;
    }
    
    public int synthesis_headerin(final Comment vc, final Packet op) {
        if (op == null) {
            return -1;
        }
        final Buffer opb = new Buffer();
        opb.wrap(op.packet_base, op.packet, op.bytes);
        final byte[] buffer = new byte[6];
        final int packtype = opb.read(8);
        opb.read(buffer, 6);
        if (buffer[0] != 118 || buffer[1] != 111 || buffer[2] != 114 || buffer[3] != 98 || buffer[4] != 105 || buffer[5] != 115) {
            return -1;
        }
        switch (packtype) {
            case 1: {
                if (op.b_o_s == 0) {
                    return -1;
                }
                if (this.rate != 0) {
                    return -1;
                }
                return this.unpack_info(opb);
            }
            case 3: {
                if (this.rate == 0) {
                    return -1;
                }
                return vc.unpack(opb);
            }
            case 5: {
                if (this.rate == 0 || !vc.hasVendor()) {
                    return -1;
                }
                return this.unpack_books(opb);
            }
            default: {
                return -1;
            }
        }
    }
    
    @Override
    public String toString() {
        return "version:" + this.version + ", channels:" + this.channelsCount + ", rate:" + this.rate + ", bitrate:" + this.bitrate_upper + ',' + this.bitrate_nominal + ',' + this.bitrate_lower;
    }
}
