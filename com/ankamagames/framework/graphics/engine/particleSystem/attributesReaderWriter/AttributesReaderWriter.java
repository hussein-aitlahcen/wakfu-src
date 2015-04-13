package com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter;

import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class AttributesReaderWriter<T>
{
    protected abstract byte getId();
    
    public abstract T createFromStream(final ExtendedDataInputStream p0, final float p1) throws IOException;
    
    protected abstract void writeToStream(final OutputBitStream p0, final T p1, final T p2) throws IOException;
    
    protected abstract boolean equals(final T p0, final T p1);
    
    public final void toStream(final OutputBitStream ostream, final T min, final T max) throws IOException {
        ostream.writeByte(this.getId());
        this.writeToStream(ostream, min, max);
    }
    
    public static void writeUnsignedShort(final OutputBitStream ostream, final boolean leveled, final int a, final int b) throws IOException {
        ostream.writeShort((short)(a & 0xFFFF));
        if (leveled) {
            ostream.writeShort((short)(b & 0xFFFF));
        }
    }
    
    public static int readUnsignedShort(final ExtendedDataInputStream istream, final boolean leveled, final float levelPercent) throws IOException {
        final int a = istream.readShort();
        if (!leveled) {
            return a & 0xFFFF;
        }
        final int b = istream.readShort();
        return Math.round(MathHelper.lerp(a, b, levelPercent)) & 0xFFFF;
    }
    
    public static void writeFloat(final OutputBitStream ostream, final boolean leveled, final float a, final float b) throws IOException {
        ostream.writeFloat(a);
        if (leveled) {
            ostream.writeFloat(b);
        }
    }
    
    public static float readFloat(final ExtendedDataInputStream istream, final boolean leveled, final float levelPercent) throws IOException {
        final float a = istream.readFloat();
        if (!leveled) {
            return a;
        }
        final float b = istream.readFloat();
        return MathHelper.lerp(a, b, levelPercent);
    }
    
    public static void writeInt(final OutputBitStream ostream, final boolean leveled, final int a, final int b) throws IOException {
        ostream.writeInt(a);
        if (leveled) {
            ostream.writeInt(b);
        }
    }
    
    public static int readInt(final ExtendedDataInputStream istream, final boolean leveled, final float levelPercent) throws IOException {
        final int a = istream.readInt();
        if (!leveled) {
            return a;
        }
        final int b = istream.readInt();
        return (int)MathHelper.lerp(a, b, levelPercent);
    }
}
