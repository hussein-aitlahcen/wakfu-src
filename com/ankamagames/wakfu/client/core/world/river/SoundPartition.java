package com.ankamagames.wakfu.client.core.world.river;

import gnu.trove.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class SoundPartition
{
    static byte NUM_SUBS;
    static byte BIG_NUM_SUBS;
    static final int SUB_SIZE;
    static final int BIG_SUB_SIZE;
    private final TByteObjectHashMap<Distribution> m_distributions;
    
    public SoundPartition(final short mapX, final short mapY) throws Exception {
        super();
        this.m_distributions = new TByteObjectHashMap<Distribution>();
        final WakfuClientEnvironmentMap map = (WakfuClientEnvironmentMap)EnvironmentMapManager.getInstance().getMap(mapX, mapY);
        final long riverDistribution = (map != null) ? map.getRiverDistribution() : 0L;
        final long lakeDistribution = (map != null) ? map.getLakeDistribution() : 0L;
        final long lavaDistribution = (map != null) ? map.getLavaDistribution() : 0L;
        final long runningLavaDistribution = (map != null) ? map.getRunningLavaDistribution() : 0L;
        this.m_distributions.put(SoundZoneType.RIVER.getId(), new Distribution(riverDistribution));
        this.m_distributions.put(SoundZoneType.LAKE.getId(), new Distribution(lakeDistribution));
        this.m_distributions.put(SoundZoneType.LAVA.getId(), new Distribution(lavaDistribution));
        this.m_distributions.put(SoundZoneType.RUNNING_LAVA.getId(), new Distribution(runningLavaDistribution));
    }
    
    public byte getWeightAtWorldBigCoord(final byte zoneType, final int x, final int y) {
        final Distribution distri = this.m_distributions.get(zoneType);
        if (distri == null) {
            return 0;
        }
        return distri.getWeightAtWorldBigCoord(x, y);
    }
    
    public byte getWeightAt(final byte zoneType, final int x, final int y) {
        final Distribution distri = this.m_distributions.get(zoneType);
        if (distri == null) {
            return 0;
        }
        return distri.getWeightAt(x, y);
    }
    
    public boolean hasAtWorldSubCoord(final byte zoneType, final int x, final int y) {
        final Distribution distri = this.m_distributions.get(zoneType);
        return distri != null && distri.hasAtWorldSubCoord(x, y);
    }
    
    public boolean hasType(final byte type) {
        final Distribution distribution = this.m_distributions.get(type);
        return distribution != null && distribution.hasDistribution();
    }
    
    private static int coordToBigSubdivision(final int coord) {
        return coord / SoundPartition.BIG_SUB_SIZE;
    }
    
    private static int subdivisionToBigSubdivision(final int coord) {
        return coord * SoundPartition.BIG_NUM_SUBS / SoundPartition.NUM_SUBS;
    }
    
    private static int indexFromRowAndCol(final int row, final int col, final int numCols) {
        return col + numCols * row;
    }
    
    public static int worldCoordToWorldSub(final int coord) {
        return (int)Math.floor(coord / SoundPartition.SUB_SIZE);
    }
    
    public static int worldCoordToWorldBigSub(final int coord) {
        return (int)Math.floor(coord / SoundPartition.BIG_SUB_SIZE);
    }
    
    public static int worldSubToWorldCoord(final int coord) {
        return (int)((coord + 0.5) * SoundPartition.SUB_SIZE);
    }
    
    public static int worldBigSubToWorldCoord(final int coord) {
        return (int)((coord + 0.5) * SoundPartition.BIG_SUB_SIZE);
    }
    
    public static short worldSubCoordToMap(final int coord) {
        return (short)Math.floor(coord / SoundPartition.NUM_SUBS);
    }
    
    public static short worldBigSubCoordToMap(final int coord) {
        return (short)Math.floor(coord / SoundPartition.BIG_NUM_SUBS);
    }
    
    static {
        SoundPartition.NUM_SUBS = 6;
        SoundPartition.BIG_NUM_SUBS = 2;
        SUB_SIZE = 18 / SoundPartition.NUM_SUBS;
        BIG_SUB_SIZE = 18 / SoundPartition.BIG_NUM_SUBS;
    }
    
    private static class Distribution
    {
        private final long m_distribution;
        private final byte[] m_distributionWeight;
        
        private Distribution(final long distribution) {
            super();
            this.m_distributionWeight = new byte[SoundPartition.BIG_NUM_SUBS * SoundPartition.BIG_NUM_SUBS];
            this.m_distribution = distribution;
            this.precomputeData();
        }
        
        private void precomputeData() {
            for (int col = 0; col < SoundPartition.NUM_SUBS; ++col) {
                for (int row = 0; row < SoundPartition.NUM_SUBS; ++row) {
                    final int index = indexFromRowAndCol(row, col, SoundPartition.NUM_SUBS);
                    final int bigIndex = indexFromRowAndCol(subdivisionToBigSubdivision(row), subdivisionToBigSubdivision(col), SoundPartition.BIG_NUM_SUBS);
                    if (MathHelper.getBooleanAt(this.m_distribution, index)) {
                        final byte[] distributionWeight = this.m_distributionWeight;
                        final int n = bigIndex;
                        ++distributionWeight[n];
                    }
                }
            }
        }
        
        public byte getWeightAtWorldBigCoord(int x, int y) {
            if (this.m_distribution == 0L) {
                return 0;
            }
            x = (x % SoundPartition.BIG_NUM_SUBS + SoundPartition.BIG_NUM_SUBS) % SoundPartition.BIG_NUM_SUBS;
            y = (y % SoundPartition.BIG_NUM_SUBS + SoundPartition.BIG_NUM_SUBS) % SoundPartition.BIG_NUM_SUBS;
            final int bigIndex = indexFromRowAndCol(y, x, SoundPartition.BIG_NUM_SUBS);
            return this.m_distributionWeight[bigIndex];
        }
        
        public byte getWeightAt(final int x, final int y) {
            if (this.m_distribution == 0L) {
                return 0;
            }
            final int bigIndex = indexFromRowAndCol(coordToBigSubdivision(y), coordToBigSubdivision(x), SoundPartition.BIG_NUM_SUBS);
            return this.m_distributionWeight[bigIndex];
        }
        
        public boolean hasAtWorldSubCoord(int x, int y) {
            if (this.m_distribution == 0L) {
                return false;
            }
            x = (x % SoundPartition.NUM_SUBS + SoundPartition.NUM_SUBS) % SoundPartition.NUM_SUBS;
            y = (y % SoundPartition.NUM_SUBS + SoundPartition.NUM_SUBS) % SoundPartition.NUM_SUBS;
            final int index = indexFromRowAndCol(y, x, SoundPartition.NUM_SUBS);
            return MathHelper.getBooleanAt(this.m_distribution, index);
        }
        
        public boolean hasDistribution() {
            return this.m_distribution != 0L;
        }
        
        public void printGrid() {
            for (int i = 0; i < SoundPartition.NUM_SUBS; ++i) {
                for (int j = 0; j < SoundPartition.NUM_SUBS; ++j) {
                    final int index = indexFromRowAndCol(j, i, SoundPartition.NUM_SUBS);
                    System.out.print(MathHelper.getBooleanAt(this.m_distribution, index) ? "R " : "x ");
                }
                System.out.println("");
            }
        }
    }
}
