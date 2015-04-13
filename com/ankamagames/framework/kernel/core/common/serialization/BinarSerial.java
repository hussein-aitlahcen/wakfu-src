package com.ankamagames.framework.kernel.core.common.serialization;

import org.apache.log4j.*;
import java.nio.*;
import java.util.*;

public abstract class BinarSerial
{
    private static final int MAX_BINAR_SERIAL_SIZE = 10485760;
    private static final Logger m_logger;
    
    public abstract BinarSerialPart[] partsEnumeration();
    
    int getPartIndex(final BinarSerialPart part) {
        final BinarSerialPart[] parts = this.partsEnumeration();
        if (parts != null) {
            for (int i = 0; i < parts.length; ++i) {
                if (parts[i] == part) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public final byte[] build(final BinarSerialPart... parts) {
        if (parts != null && parts.length > 0) {
            return BinarSerialBuilder.getInstance().buildSerial(this, parts);
        }
        throw new RuntimeException("Unable to serialize content without parts");
    }
    
    public final byte[] build(final int... partsIndex) {
        if (partsIndex != null && partsIndex.length > 0) {
            final BinarSerialPart[] parts = new BinarSerialPart[partsIndex.length];
            final BinarSerialPart[] partsEnum = this.partsEnumeration();
            int k = 0;
            for (final int i : partsIndex) {
                parts[k++] = partsEnum[i];
            }
            return BinarSerialBuilder.getInstance().buildSerial(this, parts);
        }
        throw new RuntimeException("Unable to serialize content without parts");
    }
    
    public final void fromBuild(final byte[] binarBuild) {
        this.fromBuild(binarBuild, 0);
    }
    
    public final void fromBuild(final byte[] binarBuild, final int version) {
        final BinarSerialPart[] parts = this.partsEnumeration();
        if (parts == null || parts.length <= 0) {
            return;
        }
        final ArrayList<BinarSerialPart> successParts = new ArrayList<BinarSerialPart>(parts.length);
        final ByteBuffer buffer = ByteBuffer.wrap(binarBuild);
        final int tocLength = buffer.get();
        final byte[] tocIndex = new byte[tocLength];
        final int[] tocOffset = new int[tocLength];
        for (int i = 0; i < tocLength; ++i) {
            tocIndex[i] = buffer.get();
            tocOffset[i] = buffer.getInt();
        }
        for (int i = 0; i < tocLength; ++i) {
            final byte index = tocIndex[i];
            final int offset = tocOffset[i];
            int size;
            if (i < tocLength - 1) {
                size = tocOffset[i + 1] - offset - 1;
            }
            else {
                size = buffer.limit() - offset - 1;
            }
            if (size <= 0) {
                BinarSerial.m_logger.warn((Object)("Part " + index + "(offset=" + offset + ") is empty for " + this + " ! (voir log serveur)"), (Throwable)new Exception());
            }
            else if (size > 10485760) {
                BinarSerial.m_logger.error((Object)("Part " + index + "(offset=" + offset + ") exceeds max limit (" + size + " > " + 10485760 + " bytes)"), (Throwable)new Exception());
            }
            else {
                final ByteBufferPool pool = BinarSerialBuilder.getInstance().getPool(size);
                final ByteBuffer partBuffer = pool.borrowBuffer();
                buffer.position(offset + 1);
                partBuffer.limit(size);
                buffer.get(partBuffer.array(), 0, size);
                if (index >= 0 && index < parts.length) {
                    final BinarSerialPart part = parts[index];
                    if (part == BinarSerialPart.EMPTY) {
                        BinarSerial.m_logger.warn((Object)("Don't know how to unserialise part #" + index + " (EMPTY)."));
                        continue;
                    }
                    if (part == null) {
                        BinarSerial.m_logger.error((Object)("Part " + index + " of " + this + " is null"), (Throwable)new Exception());
                        continue;
                    }
                    try {
                        part.clearError();
                        part.unserialize(partBuffer, version);
                        if (!part.hasError()) {
                            successParts.add(part);
                        }
                    }
                    catch (Exception e) {
                        part.markAsError("Exception lev\u00e9e lors de la d\u00e9serialisation de " + this + ", part :" + index, e);
                    }
                    if (partBuffer.remaining() > 0) {
                        BinarSerial.m_logger.warn((Object)("Part " + index + " of " + this + " still have " + partBuffer.remaining() + " byte(s) left !"), (Throwable)new Exception());
                    }
                }
                pool.returnBuffer(partBuffer);
            }
        }
        for (final BinarSerialPart successPart : successParts) {
            successPart.notifyListener();
        }
    }
    
    public final void extractPartFromBuild(final BinarSerialPart part, final byte[] binarBuild, final int version) {
        final BinarSerialPart[] parts = this.partsEnumeration();
        if (part == null || parts == null || parts.length <= 0) {
            return;
        }
        final int index = this.getPartIndex(part);
        if (index < 0) {
            throw new RuntimeException("Unable to find part in BinarSerial class : " + this.getClass().getSimpleName());
        }
        final ByteBuffer buffer = ByteBuffer.wrap(binarBuild);
        for (int tocLength = buffer.get(), i = 0; i < tocLength; ++i) {
            final int tocIndex = buffer.get();
            final int tocOffset = buffer.getInt();
            if (tocIndex == index) {
                buffer.position(tocOffset + 1);
                try {
                    part.clearError();
                    part.unserialize(buffer, version);
                    if (!part.hasError()) {
                        part.notifyListener();
                    }
                }
                catch (Exception e) {
                    BinarSerial.m_logger.error((Object)("Exception lev\u00e9e lors de la d\u00e9serialisation de la part :" + index), (Throwable)e);
                }
                return;
            }
        }
        throw new RuntimeException("Part (" + index + ")doesnt exist in BinarSerial class : " + this.getClass().getSimpleName());
    }
    
    static {
        m_logger = Logger.getLogger((Class)BinarSerial.class);
    }
}
