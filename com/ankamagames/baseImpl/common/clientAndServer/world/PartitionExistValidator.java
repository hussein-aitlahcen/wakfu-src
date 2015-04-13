package com.ankamagames.baseImpl.common.clientAndServer.world;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class PartitionExistValidator
{
    private static final Logger m_logger;
    private final TIntHashSet m_mapsCoordinates;
    
    public PartitionExistValidator() {
        super();
        this.m_mapsCoordinates = new TIntHashSet();
    }
    
    public void loadFromFile(final String filename, final int worldId) {
        try {
            assert this.m_mapsCoordinates.isEmpty();
            final byte[] bytes = WorldMapFileHelper.readFile(String.format(filename, worldId));
            final ByteBuffer buffer = ByteBuffer.wrap(bytes);
            for (int numMapsCoord = bytes.length / 4, i = 0; i < numMapsCoord; ++i) {
                this.m_mapsCoordinates.add(buffer.getInt());
            }
        }
        catch (Throwable e) {
            PartitionExistValidator.m_logger.error((Object)"probl\u00e8me de lecture des coordonn\u00e9es de maps valides", e);
        }
    }
    
    public boolean partitionExists(final int x, final int y) {
        return this.m_mapsCoordinates.contains(MathHelper.getIntFromTwoInt(x, y));
    }
    
    public void clear() {
        this.m_mapsCoordinates.clear();
    }
    
    public void add(final short x, final short y) {
        this.m_mapsCoordinates.add(MathHelper.getIntFromTwoInt(x, y));
    }
    
    static {
        m_logger = Logger.getLogger((Class)PartitionExistValidator.class);
    }
}
