package com.jcraft.jorbis;

import com.jcraft.jogg.*;

abstract class FuncResidue
{
    public static final FuncResidue[] residue_P;
    
    static void freeInfo(final Util.TypeParamPair data) {
        FuncResidue.residue_P[data.m_type].free_info(data.m_param);
    }
    
    abstract Object unpack(final Info p0, final Buffer p1);
    
    abstract Object look(final DspState p0, final InfoMode p1, final Object p2);
    
    abstract void free_info(final Object p0);
    
    abstract int inverse(final Block p0, final Object p1, final float[][] p2, final int[] p3, final int p4);
    
    static {
        residue_P = new FuncResidue[] { new Residue0(), new Residue1(), new Residue2() };
    }
}
