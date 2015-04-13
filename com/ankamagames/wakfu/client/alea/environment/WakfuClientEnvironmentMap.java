package com.ankamagames.wakfu.client.alea.environment;

import com.ankamagames.baseImpl.graphics.alea.environment.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.compressedCellData.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.alea.environment.*;
import com.ankamagames.wakfu.common.constants.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class WakfuClientEnvironmentMap extends ClientEnvironmentMap
{
    private static final Logger m_logger;
    public static final int FILE_VERSION = 0;
    protected CompressedCellData m_groundType;
    private float m_seaPercent;
    private long m_riverDistribution;
    private long m_lakeDistribution;
    private long m_lavaDistribution;
    private long m_runningLavaDistribution;
    private BagPlacementDelegate m_bagPlacement;
    
    @Override
    public byte getVersion() {
        return 0;
    }
    
    public final boolean isMarket() {
        return BagPlacementDelegate.isMarket(this.m_bagPlacement);
    }
    
    public boolean isHavroSteryl(final int destX, final int destY, final short z) {
        return this.getBagDirectionOn(destX, destY, z) == null;
    }
    
    public Direction8 getBagDirectionOn(final int destX, final int destY, final short z) {
        if (this.m_bagPlacement == null) {
            return Direction8.NONE;
        }
        if (this.m_bagPlacement.isMarket()) {
            final BagPlacementDelegate.InMarket market = (BagPlacementDelegate.InMarket)this.m_bagPlacement;
            final Direction8 dir = market.getDirectionAt(destX, destY, z);
            return dir;
        }
        final BagPlacementDelegate.InWorld world = (BagPlacementDelegate.InWorld)this.m_bagPlacement;
        return this.isSteryl(world.getHavroSteryl(), destX, destY, z) ? null : Direction8.NONE;
    }
    
    private boolean isSteryl(final SterylCells cells, final int destX, final int destY, final short z) {
        if (cells == null) {
            return false;
        }
        final byte x = (byte)(destX - this.m_x * 18);
        final byte y = (byte)(destY - this.m_y * 18);
        return cells.isSteryl(x, y, z);
    }
    
    public final boolean isPictoSterylFromWorld(final int worldX, final int worldY, final int worldZ) {
        final int x = worldX - this.m_x * 18;
        final int y = worldY - this.m_y * 18;
        return this.isPictoSteryl(x, y, worldZ);
    }
    
    public final boolean isPictoSteryl(final int cellX, final int cellY, final int cellZ) {
        return this.getGroundType(cellX, cellY, cellZ) == 0;
    }
    
    public final short getGroundTypeFromWorld(final int worldX, final int worldY, final int worldZ) {
        final int x = worldX - this.m_x * 18;
        final int y = worldY - this.m_y * 18;
        return this.getGroundType(x, y, worldZ);
    }
    
    public short getGroundType(final int cellX, final int cellY, final int cellZ) {
        return EnvironmentConstants.forPartition().getValue(cellX, cellY, cellZ, this.m_groundType);
    }
    
    public float getSeaPercent() {
        return this.m_seaPercent;
    }
    
    public long getRiverDistribution() {
        return this.m_riverDistribution;
    }
    
    public long getLakeDistribution() {
        return this.m_lakeDistribution;
    }
    
    public long getLavaDistribution() {
        return this.m_lavaDistribution;
    }
    
    public long getRunningLavaDistribution() {
        return this.m_runningLavaDistribution;
    }
    
    @Override
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        super.load(istream);
        this.loadSteryls(istream);
        this.m_seaPercent = istream.readByte() / 100.0f;
        this.m_riverDistribution = istream.readLong();
        this.m_lakeDistribution = istream.readLong();
        this.m_lavaDistribution = istream.readLong();
        this.m_runningLavaDistribution = istream.readLong();
        this.loadHavroSteryl(istream);
    }
    
    private void loadSteryls(final ExtendedDataInputStream istream) throws IOException {
        this.m_groundType = CompressedCellData.fromStream(istream, (short)0);
    }
    
    private void loadHavroSteryl(final ExtendedDataInputStream istream) throws IOException {
        this.m_bagPlacement = BagPlacementDelegate.from(istream);
    }
    
    @Override
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        super.save(ostream);
        this.saveSteryls(ostream);
        ostream.writeByte((byte)(this.m_seaPercent * 100.0f));
        ostream.writeLong(this.m_riverDistribution);
        ostream.writeLong(this.m_lakeDistribution);
        ostream.writeLong(this.m_lavaDistribution);
        ostream.writeLong(this.m_runningLavaDistribution);
        this.saveHavroSteryl(ostream);
    }
    
    public final void setGroundType(final CompressedCellData groundTypeData) {
        this.m_groundType = groundTypeData;
    }
    
    public void setSeaPercent(final float seaPercent) {
        assert seaPercent >= 0.0f && seaPercent <= 1.0f;
        this.m_seaPercent = seaPercent;
    }
    
    public void setRiverDistribution(final long riverDistribution) {
        this.m_riverDistribution = riverDistribution;
    }
    
    public void setLakeDistribution(final long lakeDistribution) {
        this.m_lakeDistribution = lakeDistribution;
    }
    
    public void setLavaDistribution(final long lavaDistribution) {
        this.m_lavaDistribution = lavaDistribution;
    }
    
    public void setRunningLavaDistribution(final long lavaDistribution) {
        this.m_runningLavaDistribution = lavaDistribution;
    }
    
    private void saveSteryls(final OutputBitStream ostream) throws IOException {
        if (this.m_groundType == null) {
            ostream.writeByte((byte)0);
            return;
        }
        this.m_groundType.toStream(ostream);
    }
    
    public void setBagPlacement(final BagPlacementDelegate bagPlacementDelegate) {
        this.m_bagPlacement = bagPlacementDelegate;
    }
    
    private void saveHavroSteryl(final OutputBitStream ostream) throws IOException {
        BagPlacementDelegate.save(ostream, this.m_bagPlacement);
    }
    
    @Override
    public void clear() {
        super.clear();
        this.m_groundType = null;
        this.m_seaPercent = 0.0f;
        this.m_riverDistribution = 0L;
        this.m_lakeDistribution = 0L;
        this.m_lavaDistribution = 0L;
        this.m_runningLavaDistribution = 0L;
        this.m_bagPlacement = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuClientEnvironmentMap.class);
    }
}
