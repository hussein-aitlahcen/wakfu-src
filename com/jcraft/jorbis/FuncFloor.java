package com.jcraft.jorbis;

import com.jcraft.jogg.*;

abstract class FuncFloor<InfoFloor, LookFloor>
{
    public static final FuncFloor[] floor_P;
    
    static void freeInfo(final Util.TypeParamPair data) {
        FuncFloor.floor_P[data.m_type].free_info(data.m_param);
    }
    
    abstract Object unpack(final Info p0, final Buffer p1);
    
    abstract Object look(final DspState p0, final InfoMode p1, final InfoFloor p2);
    
    abstract void free_info(final Object p0);
    
    abstract Object inverse1(final Block p0, final LookFloor p1, final Object p2);
    
    abstract int inverse2(final Block p0, final LookFloor p1, final Object p2, final float[] p3);
    
    static {
        floor_P = new FuncFloor[] { new Floor0(), new Floor1() };
    }
}
