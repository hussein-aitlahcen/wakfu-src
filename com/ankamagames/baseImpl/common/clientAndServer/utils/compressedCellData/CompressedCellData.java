package com.ankamagames.baseImpl.common.clientAndServer.utils.compressedCellData;

import gnu.trove.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.util.*;

public class CompressedCellData
{
    public static final int MAX_VALUE = 16;
    private final short m_defaultValue;
    private final short[] m_dataTable;
    private final byte[] m_cells;
    
    public CompressedCellData(final CompressedCellData data) {
        super();
        this.m_defaultValue = data.m_defaultValue;
        if (data.m_dataTable != null) {
            this.m_dataTable = new short[data.m_dataTable.length];
            System.arraycopy(data.m_dataTable, 0, this.m_dataTable, 0, data.m_dataTable.length);
        }
        else {
            this.m_dataTable = null;
        }
        if (data.m_cells != null) {
            this.m_cells = new byte[data.m_cells.length];
            System.arraycopy(data.m_cells, 0, this.m_cells, 0, data.m_cells.length);
        }
        else {
            this.m_cells = null;
        }
    }
    
    public CompressedCellData(final short defaultValue, final short[] dataTable, final byte[] cells) {
        super();
        this.m_defaultValue = defaultValue;
        this.m_dataTable = dataTable;
        this.m_cells = cells;
    }
    
    final int getValueCount() {
        return this.m_dataTable.length;
    }
    
    short getValue(final int index) {
        return this.m_dataTable[index];
    }
    
    public short getValueForCell(final int cellIndex) {
        final int count = this.m_dataTable.length;
        if (count == 1) {
            return this.m_dataTable[0];
        }
        if (count <= 16) {
            int valueByByte = 2;
            int mask = 15;
            if (count <= 4) {
                valueByByte = 4;
                mask = 3;
            }
            if (count <= 2) {
                valueByByte = 8;
                mask = 1;
            }
            final int shift = cellIndex % valueByByte * (8 / valueByByte);
            final int index = (this.m_cells[cellIndex / valueByByte] & 0xFF) >>> shift & mask;
            return this.m_dataTable[index];
        }
        assert false : "ne devrait pas arriver, export foireux(?)";
        return this.m_defaultValue;
    }
    
    public static CompressedCellData create(final TShortArrayList differentValue, final short[] cellsValue, final short defaultValue) {
        final int numDiff = differentValue.size();
        if (numDiff == 1) {
            return new CompressedCellData(defaultValue, differentValue.toNativeArray(), null);
        }
        int valueByByte = 2;
        if (numDiff <= 4) {
            valueByByte *= 2;
        }
        if (numDiff == 2) {
            valueByByte *= 2;
        }
        final int cellCount = (cellsValue.length + (valueByByte - 1)) / valueByByte;
        final byte[] optimized = new byte[cellCount];
        final int valueHeaderSize = 8 / valueByByte;
        for (int i = 0; i < cellsValue.length; ++i) {
            final byte index = (byte)differentValue.indexOf(cellsValue[i]);
            final int shifting = i % valueByByte * valueHeaderSize;
            final int k = i / valueByByte;
            final byte[] array = optimized;
            final int n = k;
            array[n] |= (byte)((index & 0xF) << shifting);
        }
        return new CompressedCellData(defaultValue, differentValue.toNativeArray(), optimized);
    }
    
    public void toStream(final OutputBitStream ostream) throws IOException {
        final int count = this.m_dataTable.length;
        assert count < 255;
        ostream.writeByte((byte)(count & 0xFF));
        for (int i = 0; i < count; ++i) {
            ostream.writeShort(this.m_dataTable[i]);
        }
        if (count != 1) {
            ostream.writeByte((byte)(this.m_cells.length & 0xFF));
            ostream.writeBytes(this.m_cells);
        }
    }
    
    public static CompressedCellData fromStream(final ExtendedDataInputStream istream, final short defaultValue) {
        final int count = istream.readByte() & 0xFF;
        if (count == 0) {
            return null;
        }
        final short[] grounds = istream.readShorts(count);
        byte[] cells;
        if (count != 1) {
            final int size = istream.readByte() & 0xFF;
            cells = istream.readBytes(size);
        }
        else {
            cells = null;
        }
        return new CompressedCellData(defaultValue, grounds, cells);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof CompressedCellData)) {
            return false;
        }
        final CompressedCellData other = (CompressedCellData)obj;
        if (this.m_dataTable == null) {
            if (other.m_dataTable != null) {
                return false;
            }
        }
        else if (!Arrays.equals(this.m_dataTable, other.m_dataTable)) {
            return false;
        }
        if ((this.m_cells != null) ? Arrays.equals(this.m_cells, other.m_cells) : (other.m_cells == null)) {
            return true;
        }
        return false;
    }
}
