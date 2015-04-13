package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.jump;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class JumpTrajectory
{
    protected static final float JUMP_DELTA = 1.0f;
    protected static Logger m_logger;
    final float[] m_cellPercent;
    
    public JumpTrajectory(final float startAsc, final float endAsc, final float startDesc, final float endDesc) {
        super();
        this.m_cellPercent = new float[] { startAsc, endAsc, startDesc, endDesc };
    }
    
    public Phase getPhase(final float cellPercent) {
        assert cellPercent >= 0.0f && cellPercent <= 1.0f;
        if (cellPercent < this.m_cellPercent[0]) {
            return Phase.BEFORE;
        }
        if (cellPercent < this.m_cellPercent[1]) {
            return Phase.ASCENDING;
        }
        if (cellPercent < this.m_cellPercent[2]) {
            return Phase.STABLE;
        }
        if (cellPercent < this.m_cellPercent[3]) {
            return Phase.DESCENDING;
        }
        return Phase.AFTER;
    }
    
    public float getAltitude(final int cellStartZ, final int cellEndZ, final float cellPositionPercent, final Phase phase) {
        assert this.getPhase(cellPositionPercent) == phase;
        assert phase != Phase.BEFORE && phase != Phase.AFTER;
        final float startZ = this.getStartZ(cellStartZ, cellEndZ, phase);
        final float endZ = this.getEndZ(cellStartZ, cellEndZ, phase);
        final int index = phase.ordinal();
        final float percent = (cellPositionPercent - this.m_cellPercent[index - 1]) / (this.m_cellPercent[index] - this.m_cellPercent[index - 1]);
        return MathHelper.lerp(startZ, endZ, percent);
    }
    
    protected abstract float getStartZ(final int p0, final int p1, final Phase p2);
    
    protected abstract float getEndZ(final int p0, final int p1, final Phase p2);
    
    static {
        JumpTrajectory.m_logger = Logger.getLogger((Class)JumpTrajectory.class);
    }
}
