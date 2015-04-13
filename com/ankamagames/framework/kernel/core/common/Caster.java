package com.ankamagames.framework.kernel.core.common;

public final class Caster
{
    public static short toUnsignedByte(final byte b) {
        if (b < 0) {
            return (short)(256 + b);
        }
        return b;
    }
    
    public static byte toByte(final byte[] buffer, final int offset) {
        return buffer[offset];
    }
    
    public static short toShort(final byte[] buffer, final int offset) {
        return (short)(toUnsignedByte(buffer[offset]) | toUnsignedByte(buffer[offset + 1]) << 8);
    }
    
    public static int toInt(final byte[] buffer, final int offset) {
        return toUnsignedByte(buffer[offset]) | toUnsignedByte(buffer[offset + 1]) << 8 | toUnsignedByte(buffer[offset + 2]) << 16 | toUnsignedByte(buffer[offset + 3]) << 24;
    }
    
    public static byte toByte(final byte[] buffer, final TypeRef<Integer> offset) {
        final int iOffset = offset.get();
        offset.set(iOffset + 1);
        return toByte(buffer, iOffset);
    }
    
    public static short toShort(final byte[] buffer, final TypeRef<Integer> offset) {
        final int iOffset = offset.get();
        offset.set(iOffset + 2);
        return toShort(buffer, iOffset);
    }
    
    public static int toInt(final byte[] buffer, final TypeRef<Integer> offset) {
        final int iOffset = offset.get();
        offset.set(iOffset + 4);
        return toInt(buffer, iOffset);
    }
}
