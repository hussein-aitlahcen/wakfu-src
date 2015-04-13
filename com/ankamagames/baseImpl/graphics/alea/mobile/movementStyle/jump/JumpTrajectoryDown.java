package com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.jump;

public class JumpTrajectoryDown extends JumpTrajectory
{
    public JumpTrajectoryDown(final float startAsc, final float endAsc, final float startDesc, final float endDesc) {
        super(startAsc, endAsc, startDesc, endDesc);
        assert this.getStartZ(-1, 1, Phase.STABLE) == this.getEndZ(-1, 1, Phase.ASCENDING);
        assert this.getStartZ(-1, 1, Phase.DESCENDING) == this.getEndZ(-1, 1, Phase.STABLE);
    }
    
    @Override
    public float getAltitude(final int cellStartZ, final int cellEndZ, final float cellPositionPercent, final Phase phase) {
        final int dZ = cellStartZ - cellEndZ;
        return cellStartZ - dZ * cellPositionPercent;
    }
    
    @Override
    protected float getEndZ(final int cellStartZ, final int cellEndZ, final Phase phase) {
        assert phase != Phase.BEFORE && phase != Phase.AFTER;
        switch (phase) {
            case ASCENDING: {
                return cellStartZ + 0.4f;
            }
            case STABLE: {
                return cellStartZ + 0.4f;
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
                return cellStartZ + 0.4f;
            }
            case DESCENDING: {
                return cellStartZ + 0.4f;
            }
            default: {
                throw new IllegalArgumentException("phase de suat incorrect " + phase);
            }
        }
    }
}
