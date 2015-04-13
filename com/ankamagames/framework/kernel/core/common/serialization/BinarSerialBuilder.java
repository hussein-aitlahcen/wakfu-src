package com.ankamagames.framework.kernel.core.common.serialization;

import org.apache.log4j.*;
import gnu.trove.*;
import java.nio.*;

public class BinarSerialBuilder
{
    protected static final Logger m_logger;
    private static final BinarSerialBuilder m_instance;
    private final TIntObjectHashMap<ByteBufferPool> m_pools;
    private final TIntArrayList m_poolSizes;
    
    public static BinarSerialBuilder getInstance() {
        return BinarSerialBuilder.m_instance;
    }
    
    private BinarSerialBuilder() {
        super();
        this.m_pools = new TIntObjectHashMap<ByteBufferPool>();
        this.m_poolSizes = new TIntArrayList();
        for (int p = 1; p < 17; ++p) {
            final int size = 1 << p;
            final ByteBufferPool pool = this.getPool(size);
            if (pool == null) {
                throw new RuntimeException("Impossible de cr\u00e9er un des pools par d\u00e9faut : size = " + size + " bytes");
            }
        }
    }
    
    public ByteBufferPool getPool(final int size) {
        for (int i = 0, length = this.m_poolSizes.size(); i < length; ++i) {
            final int storedSizes = this.m_poolSizes.getQuick(i);
            if (storedSizes >= size) {
                return this.m_pools.get(storedSizes);
            }
        }
        final ByteBufferPool pool = new ByteBufferPool(size);
        this.m_pools.put(size, pool);
        this.m_poolSizes.add(size);
        return pool;
    }
    
    byte[] buildSerial(final BinarSerial serialBuild, final BinarSerialPart... parts) {
        if (parts == null || parts.length == 0) {
            return new byte[0];
        }
        int totalExpectedSize = 0;
        int partNumber = 0;
        final int[] serialPartExpectedSizes = new int[parts.length];
        for (int i = 0, partsLength = parts.length; i < partsLength; ++i) {
            final BinarSerialPart part = parts[i];
            if (part == null) {
                throw new RuntimeException("Impossible de s\u00e9rialiser le BInarSerial : une part est null");
            }
            if (part != BinarSerialPart.EMPTY) {
                try {
                    part.grabDataFromSource();
                    serialPartExpectedSizes[i] = part.expectedSize();
                }
                catch (Exception e) {
                    throw new RuntimeException("Error while calling expectedSize()", e);
                }
                totalExpectedSize += 1 + serialPartExpectedSizes[i];
                ++partNumber;
            }
        }
        final ByteBufferPool pool = this.getPool(totalExpectedSize + parts.length * 5 + 1);
        final ByteBuffer finalSerial = pool.borrowBuffer();
        final byte[] tocIndex = new byte[partNumber];
        final int[] tocOffset = new int[partNumber];
        finalSerial.put((byte)partNumber);
        finalSerial.position(1 + partNumber * 5);
        partNumber = 0;
        for (int j = 0, partsLength2 = parts.length; j < partsLength2; ++j) {
            final BinarSerialPart part2 = parts[j];
            if (part2 != BinarSerialPart.EMPTY) {
                ByteBufferPool partPool = null;
                ByteBuffer partBuffer = null;
                try {
                    final int expectedSize = 1 + serialPartExpectedSizes[j];
                    partPool = this.getPool(expectedSize);
                    partBuffer = partPool.borrowBuffer();
                    partBuffer.limit(expectedSize);
                    final byte partIndex = (byte)serialBuild.getPartIndex(part2);
                    if (partIndex >= 0) {
                        tocIndex[partNumber] = partIndex;
                        tocOffset[partNumber] = finalSerial.position();
                        partBuffer.put(partIndex);
                        part2.clearError();
                        if (serialPartExpectedSizes[j] > 0) {
                            part2.serialize(partBuffer);
                        }
                        ++partNumber;
                    }
                    else {
                        BinarSerialBuilder.m_logger.error((Object)("Impossible d'ajouter une part non r\u00e9f\u00e9renc\u00e9e : " + part2.getClass().getName()));
                    }
                    partBuffer.flip();
                    finalSerial.put(partBuffer);
                }
                catch (Exception e2) {
                    part2.markAsError("Exception lev\u00e9e lors de la s\u00e9rialisation de la part " + part2.getClass().getName(), e2);
                }
                finally {
                    if (partPool != null && partBuffer != null) {
                        partPool.returnBuffer(partBuffer);
                    }
                }
            }
        }
        for (int j = 0; j < partNumber; ++j) {
            finalSerial.put(1 + j * 5, tocIndex[j]);
            finalSerial.putInt(1 + j * 5 + 1, tocOffset[j]);
        }
        finalSerial.flip();
        final byte[] serial = new byte[finalSerial.limit() - finalSerial.position()];
        finalSerial.get(serial);
        pool.returnBuffer(finalSerial);
        return serial;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BinarSerialBuilder.class);
        m_instance = new BinarSerialBuilder();
    }
}
