package gnu.trove;

import java.io.*;

class SerializationProcedure implements TDoubleDoubleProcedure, TDoubleFloatProcedure, TDoubleIntProcedure, TDoubleLongProcedure, TDoubleShortProcedure, TDoubleByteProcedure, TDoubleObjectProcedure, TDoubleProcedure, TFloatDoubleProcedure, TFloatFloatProcedure, TFloatIntProcedure, TFloatLongProcedure, TFloatShortProcedure, TFloatByteProcedure, TFloatObjectProcedure, TFloatProcedure, TIntDoubleProcedure, TIntFloatProcedure, TIntIntProcedure, TIntLongProcedure, TIntShortProcedure, TIntByteProcedure, TIntObjectProcedure, TIntProcedure, TLongDoubleProcedure, TLongFloatProcedure, TLongIntProcedure, TLongLongProcedure, TLongShortProcedure, TLongByteProcedure, TLongObjectProcedure, TLongProcedure, TShortDoubleProcedure, TShortFloatProcedure, TShortIntProcedure, TShortLongProcedure, TShortShortProcedure, TShortByteProcedure, TShortObjectProcedure, TShortProcedure, TByteDoubleProcedure, TByteFloatProcedure, TByteIntProcedure, TByteLongProcedure, TByteShortProcedure, TByteByteProcedure, TByteObjectProcedure, TByteProcedure, TObjectDoubleProcedure, TObjectFloatProcedure, TObjectIntProcedure, TObjectLongProcedure, TObjectShortProcedure, TObjectByteProcedure, TObjectObjectProcedure, TObjectProcedure
{
    private final ObjectOutput stream;
    IOException exception;
    
    SerializationProcedure(final ObjectOutput stream) {
        super();
        this.stream = stream;
    }
    
    public boolean execute(final byte val) {
        try {
            this.stream.writeByte(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final short val) {
        try {
            this.stream.writeShort(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final int val) {
        try {
            this.stream.writeInt(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final double val) {
        try {
            this.stream.writeDouble(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final long val) {
        try {
            this.stream.writeLong(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final float val) {
        try {
            this.stream.writeFloat(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final Object val) {
        try {
            this.stream.writeObject(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final Object key, final Object val) {
        try {
            this.stream.writeObject(key);
            this.stream.writeObject(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final Object key, final byte val) {
        try {
            this.stream.writeObject(key);
            this.stream.writeByte(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final Object key, final short val) {
        try {
            this.stream.writeObject(key);
            this.stream.writeShort(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final Object key, final int val) {
        try {
            this.stream.writeObject(key);
            this.stream.writeInt(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final Object key, final long val) {
        try {
            this.stream.writeObject(key);
            this.stream.writeLong(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final Object key, final double val) {
        try {
            this.stream.writeObject(key);
            this.stream.writeDouble(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final Object key, final float val) {
        try {
            this.stream.writeObject(key);
            this.stream.writeFloat(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final int key, final byte val) {
        try {
            this.stream.writeInt(key);
            this.stream.writeByte(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final int key, final short val) {
        try {
            this.stream.writeInt(key);
            this.stream.writeShort(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final int key, final Object val) {
        try {
            this.stream.writeInt(key);
            this.stream.writeObject(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final int key, final int val) {
        try {
            this.stream.writeInt(key);
            this.stream.writeInt(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final int key, final long val) {
        try {
            this.stream.writeInt(key);
            this.stream.writeLong(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final int key, final double val) {
        try {
            this.stream.writeInt(key);
            this.stream.writeDouble(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final int key, final float val) {
        try {
            this.stream.writeInt(key);
            this.stream.writeFloat(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final long key, final Object val) {
        try {
            this.stream.writeLong(key);
            this.stream.writeObject(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final long key, final byte val) {
        try {
            this.stream.writeLong(key);
            this.stream.writeByte(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final long key, final short val) {
        try {
            this.stream.writeLong(key);
            this.stream.writeShort(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final long key, final int val) {
        try {
            this.stream.writeLong(key);
            this.stream.writeInt(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final long key, final long val) {
        try {
            this.stream.writeLong(key);
            this.stream.writeLong(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final long key, final double val) {
        try {
            this.stream.writeLong(key);
            this.stream.writeDouble(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final long key, final float val) {
        try {
            this.stream.writeLong(key);
            this.stream.writeFloat(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final double key, final Object val) {
        try {
            this.stream.writeDouble(key);
            this.stream.writeObject(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final double key, final byte val) {
        try {
            this.stream.writeDouble(key);
            this.stream.writeByte(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final double key, final short val) {
        try {
            this.stream.writeDouble(key);
            this.stream.writeShort(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final double key, final int val) {
        try {
            this.stream.writeDouble(key);
            this.stream.writeInt(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final double key, final long val) {
        try {
            this.stream.writeDouble(key);
            this.stream.writeLong(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final double key, final double val) {
        try {
            this.stream.writeDouble(key);
            this.stream.writeDouble(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final double key, final float val) {
        try {
            this.stream.writeDouble(key);
            this.stream.writeFloat(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final float key, final Object val) {
        try {
            this.stream.writeFloat(key);
            this.stream.writeObject(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final float key, final byte val) {
        try {
            this.stream.writeFloat(key);
            this.stream.writeByte(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final float key, final short val) {
        try {
            this.stream.writeFloat(key);
            this.stream.writeShort(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final float key, final int val) {
        try {
            this.stream.writeFloat(key);
            this.stream.writeInt(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final float key, final long val) {
        try {
            this.stream.writeFloat(key);
            this.stream.writeLong(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final float key, final double val) {
        try {
            this.stream.writeFloat(key);
            this.stream.writeDouble(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final float key, final float val) {
        try {
            this.stream.writeFloat(key);
            this.stream.writeFloat(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final byte key, final Object val) {
        try {
            this.stream.writeByte(key);
            this.stream.writeObject(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final byte key, final byte val) {
        try {
            this.stream.writeByte(key);
            this.stream.writeByte(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final byte key, final short val) {
        try {
            this.stream.writeByte(key);
            this.stream.writeShort(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final byte key, final int val) {
        try {
            this.stream.writeByte(key);
            this.stream.writeInt(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final byte key, final long val) {
        try {
            this.stream.writeByte(key);
            this.stream.writeLong(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final byte key, final double val) {
        try {
            this.stream.writeByte(key);
            this.stream.writeDouble(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final byte key, final float val) {
        try {
            this.stream.writeByte(key);
            this.stream.writeFloat(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final short key, final Object val) {
        try {
            this.stream.writeShort(key);
            this.stream.writeObject(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final short key, final byte val) {
        try {
            this.stream.writeShort(key);
            this.stream.writeByte(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final short key, final short val) {
        try {
            this.stream.writeShort(key);
            this.stream.writeShort(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final short key, final int val) {
        try {
            this.stream.writeShort(key);
            this.stream.writeInt(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final short key, final long val) {
        try {
            this.stream.writeShort(key);
            this.stream.writeLong(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final short key, final double val) {
        try {
            this.stream.writeShort(key);
            this.stream.writeDouble(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
    
    public boolean execute(final short key, final float val) {
        try {
            this.stream.writeShort(key);
            this.stream.writeFloat(val);
        }
        catch (IOException e) {
            this.exception = e;
            return false;
        }
        return true;
    }
}
