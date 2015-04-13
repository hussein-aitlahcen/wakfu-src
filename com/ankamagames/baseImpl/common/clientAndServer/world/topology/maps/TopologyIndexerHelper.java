package com.ankamagames.baseImpl.common.clientAndServer.world.topology.maps;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class TopologyIndexerHelper
{
    public static int dataByLong(final int nbBitsByData) {
        return 64 / nbBitsByData;
    }
    
    public static int dataByInt(final int nbBitsByData) {
        return 32 / nbBitsByData;
    }
    
    public static int getMask(final int nbBits) {
        return (1 << nbBits) - 1;
    }
    
    public static int getIndex(final long[] indexes, final int index, final int tableSize) {
        final int nbBits = MathHelper.log2i(tableSize);
        final int dataCount = dataByLong(nbBits);
        final long mask = getMask(nbBits);
        long i = indexes[index / dataCount];
        i >>= nbBits * (index % dataCount);
        return (int)(i & mask);
    }
    
    public static int getIndex(final int[] indexes, final int index, final int tableSize) {
        final int nbBits = MathHelper.log2i(tableSize);
        final int dataCount = dataByInt(nbBits);
        final int mask = getMask(nbBits);
        int i = indexes[index / dataCount];
        i >>= nbBits * (index % dataCount);
        return i & mask;
    }
    
    public static int[] createFor(final int[] indexes, final int count, final ExtendedDataInputStream stream) {
        return stream.readInts(count);
    }
    
    public static long[] createFor(final long[] indexes, final int count, final ExtendedDataInputStream stream) {
        final long[] result = new long[count];
        for (int i = 0; i < count; ++i) {
            result[i] = stream.readLong();
        }
        return result;
    }
    
    public static void write(final int[] cells, final OutputBitStream output) throws IOException {
        for (int i = 0; i < cells.length; ++i) {
            output.writeInt(cells[i]);
        }
    }
    
    public static void write(final long[] cells, final OutputBitStream output) throws IOException {
        for (int i = 0; i < cells.length; ++i) {
            output.writeLong(cells[i]);
        }
    }
    
    public static int[] compressAsInt(final int[] indexes, final int tableSize) {
        final int nbBits = MathHelper.log2i(tableSize);
        final int dataCount = dataByInt(nbBits);
        final int mask = getMask(nbBits);
        final int count = (int)Math.ceil(indexes.length / dataCount);
        final int[] result = new int[count];
        for (int i = 0; i < indexes.length; ++i) {
            final int k = i / dataCount;
            final int[] array = result;
            final int n = k;
            array[n] |= (indexes[i] & mask) << nbBits * (i % dataCount);
        }
        return result;
    }
    
    public static long[] compressAsLong(final int[] indexes, final int tableSize) {
        final int nbBits = MathHelper.log2i(tableSize);
        final int dataCount = dataByLong(nbBits);
        final long mask = getMask(nbBits);
        final int count = (int)Math.ceil(indexes.length / dataCount);
        final long[] result = new long[count];
        for (int i = 0; i < indexes.length; ++i) {
            final int k = i / dataCount;
            final long[] array = result;
            final int n = k;
            array[n] |= (indexes[i] & mask) << nbBits * (i % dataCount);
        }
        return result;
    }
    
    public static void main(final String[] args) {
    }
}
