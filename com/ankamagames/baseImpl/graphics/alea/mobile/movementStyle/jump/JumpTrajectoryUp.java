package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.jump;

public class JumpTrajectoryUp extends JumpTrajectory
{
    public JumpTrajectoryUp(final float startAsc, final float endAsc, final float startDesc, final float endDesc) {
        super(startAsc, endAsc, startDesc, endDesc);
        assert this.getStartZ(-1, 1, Phase.STABLE) == this.getEndZ(-1, 1, Phase.ASCENDING);
        assert this.getStartZ(-1, 1, Phase.DESCENDING) == this.getEndZ(-1, 1, Phase.STABLE);
    }
    
    @Override
    public float getAltitude(final int cellStartZ, final int cellEndZ, final float cellPositionPercent, final Phase phase) {
        final int dZ = cellEndZ - cellStartZ;
        return cellStartZ + dZ * cellPositionPercent;
    }
    
    @Override
    protected float getEndZ(final int cellStartZ, final int cellEndZ, final Phase phase) {
        assert phase != Phase.BEFORE && phase != Phase.AFTER;
        switch (phase) {
            case ASCENDING: {
                return cellEndZ + 1.0f;
            }
            case STABLE: {
                return cellEndZ + 1.0f;
            }
            case DESCENDING: {
                return cellEndZ;
            }
            default: {
                throw new IllegalArgumentException("phase de suat incorrect " + phase);
            }
        }
    }
    
    @Override
    protected float getStartZ(final int cellStartZ, final int cellEndZ, final Phase phase) {
        assert phase != Phase.BEFORE && phase != Phase.AFTER;
        switch (phase) {
            case ASCENDING: {
                return cellStartZ;
            }
            case STABLE: {
                return cellEndZ + 1.0f;
            }
            case DESCENDING: {
                return cellEndZ + 1.0f;
            }
            default: {
                throw new IllegalArgumentException("phase de suat incorrect " + phase);
            }
        }
    }
}
