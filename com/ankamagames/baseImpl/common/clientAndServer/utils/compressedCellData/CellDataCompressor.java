package com.ankamagames.baseImpl.common.clientAndServer.utils.compressedCellData;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;

public class CellDataCompressor
{
    private static final Logger m_logger;
    private final int m_partitionWidth;
    private final int m_partitionHeight;
    private final short m_defaultValue;
    
    public CellDataCompressor(final int partitionWidth, final int partitionHeight, final short defaultValue) {
        super();
        this.m_partitionWidth = partitionWidth;
        this.m_partitionHeight = partitionHeight;
        this.m_defaultValue = defaultValue;
    }
    
    public short getValue(final int cellX, final int cellY, final int cellZ, final CompressedCellData values) {
        assert cellX >= 0 && cellX < this.m_partitionWidth;
        assert cellY >= 0 && cellY < this.m_partitionHeight;
        if (values == null) {
            return this.m_defaultValue;
        }
        final int count = values.getValueCount();
        if (count == 1) {
            return values.getValue(0);
        }
        final int cellIndex = cellX + cellY * this.m_partitionWidth;
        return values.getValueForCell(cellIndex);
    }
    
    public CompressedCellData optimize(@NotNull final short[] cellsValue) throws CompressedDataException {
        assert cellsValue.length == this.m_partitionWidth * this.m_partitionHeight;
        final TShortArrayList differentValue = new TShortArrayList();
        for (int i = 0; i < cellsValue.length; ++i) {
            if (!differentValue.contains(cellsValue[i])) {
                differentValue.add(cellsValue[i]);
            }
        }
        final int numDiff = differentValue.size();
        if (numDiff == 0) {
            throw new CompressedDataException("pas de cellsValue");
        }
        if (numDiff > 16) {
            throw new CompressedDataException("Trop de valeur diff\u00e9rentes");
        }
        return CompressedCellData.create(differentValue, cellsValue, this.m_defaultValue);
    }
    
    public short getDefaultValue() {
        return this.m_defaultValue;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CellDataCompressor.class);
    }
}
