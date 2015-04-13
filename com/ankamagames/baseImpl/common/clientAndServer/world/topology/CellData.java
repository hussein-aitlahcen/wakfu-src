package com.ankamagames.baseImpl.common.clientAndServer.world.topology;

import com.ankamagames.framework.kernel.core.maths.*;

public abstract class CellData
{
    public int m_x;
    public int m_y;
    public short m_z;
    public byte m_height;
    public boolean m_hollow;
    
    public int getBottom() {
        return this.m_z - this.m_height;
    }
    
    public boolean inside(final CellData c) {
        return c.getBottom() <= this.m_z && c.m_z >= this.m_z;
    }
    
    public static boolean collide(final CellData c1, final CellData c2) {
        return c1.inside(c2) || c2.inside(c1);
    }
    
    public abstract CellData createMerged(final CellData p0);
    
    protected static void merge(final CellData c1, final CellData c2, final CellData result) {
        assert c1.m_x == c2.m_x && c1.m_y == c2.m_y;
        result.m_x = c1.m_x;
        result.m_y = c1.m_y;
        result.m_z = (short)Math.max(c1.m_z, c2.m_z);
        final int bottom = Math.min(c1.getBottom(), c2.getBottom());
        result.m_height = MathHelper.ensureByte(result.m_z - bottom);
        result.m_hollow = (c1.m_hollow && c2.m_hollow);
    }
}
