package com.ankamagames.wakfu.common.alea.environment;

import org.apache.log4j.*;
import org.apache.commons.lang3.*;
import java.util.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class SterylCells
{
    private static final Logger m_logger;
    public static final short EMPTY = -1;
    public static final int NO_ALTITUDE_CELLS = 0;
    public static final int CELL_COUNT;
    private final byte[] m_cells;
    private final int[] m_cellAltitude;
    
    public SterylCells() {
        this(new byte[SterylCells.CELL_COUNT], null);
    }
    
    public SterylCells(final byte[] cells, final int[] cellAltitude) {
        super();
        assert cells.length == SterylCells.CELL_COUNT;
        this.m_cells = cells;
        this.m_cellAltitude = cellAltitude;
    }
    
    public SterylCells(final SterylCells steryl) {
        super();
        this.m_cells = steryl.m_cells.clone();
        this.m_cellAltitude = (int[])((steryl.m_cellAltitude == null) ? null : ((int[])steryl.m_cellAltitude.clone()));
    }
    
    public boolean isSteryl(final byte x, final byte y, final short z) {
        assert x >= 0 && x < 18;
        assert y >= 0 && y < 18;
        final int index = x + y * 18;
        final int mask = 128 >> index % 8;
        final boolean steryl = (this.m_cells[index / 8] & mask) != 0x0;
        if (this.m_cellAltitude == null || !steryl) {
            return steryl;
        }
        final int hash = createHashAltitude(x, y, z);
        if (this.m_cellAltitude.length < 16) {
            return ArrayUtils.contains(this.m_cellAltitude, hash);
        }
        return Arrays.binarySearch(this.m_cellAltitude, hash) >= 0;
    }
    
    public static int createHashAltitude(final byte x, final byte y, final short z) {
        return x | y << 8 | z << 16;
    }
    
    public static void save(final SterylCells steryl, final OutputBitStream ostream) throws IOException {
        if (steryl == null) {
            ostream.writeShort((short)(-1));
            return;
        }
        final int count = (steryl.m_cellAltitude == null) ? 0 : steryl.m_cellAltitude.length;
        ostream.writeShort((short)count);
        ostream.writeBytes(steryl.m_cells);
        if (count != 0) {
            for (int i = 0; i < count; ++i) {
                ostream.writeInt(steryl.m_cellAltitude[i]);
            }
        }
    }
    
    public static SterylCells fromStream(final ExtendedDataInputStream istream) throws IOException {
        final short count = istream.readShort();
        if (count == -1) {
            return null;
        }
        final byte[] cells = istream.readBytes(SterylCells.CELL_COUNT);
        int[] cellAltitude;
        if (count == 0) {
            cellAltitude = null;
        }
        else {
            cellAltitude = new int[count];
            for (int i = 0; i < count; ++i) {
                cellAltitude[i] = istream.readInt();
            }
        }
        return new SterylCells(cells, cellAltitude);
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this.equals((SterylCells)obj);
    }
    
    public boolean equals(final SterylCells steryls) {
        return Arrays.equals(this.m_cells, steryls.m_cells) && Arrays.equals(this.m_cellAltitude, steryls.m_cellAltitude);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SterylCells.class);
        CELL_COUNT = (int)Math.ceil(40.5);
    }
}
