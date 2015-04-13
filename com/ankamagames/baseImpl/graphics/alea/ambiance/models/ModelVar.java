package com.ankamagames.baseImpl.graphics.alea.ambiance.models;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import java.nio.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.maths.equations.*;

public abstract class ModelVar<T extends Var>
{
    private final String m_name;
    
    public ModelVar(final String name) {
        super();
        this.m_name = name;
    }
    
    public final String getName() {
        return this.m_name;
    }
    
    public abstract T createVariable();
    
    public abstract void read(final T p0, final ByteBuffer p1) throws Exception;
    
    public abstract void write(final OutputBitStream p0, final String p1) throws Exception;
    
    public static final class MByte extends ModelVar<Var.VByte>
    {
        public MByte(final String name) {
            super(name);
        }
        
        @Override
        public final Var.VByte createVariable() {
            return new Var.VByte();
        }
        
        @Override
        public final void read(final Var.VByte param, final ByteBuffer buffer) {
            param.m_value = buffer.get();
        }
        
        @Override
        public void write(final OutputBitStream ostream, final String value) throws Exception {
            ostream.writeByte(Byte.parseByte(value));
        }
    }
    
    public static final class MShort extends ModelVar<Var.VShort>
    {
        public MShort(final String name) {
            super(name);
        }
        
        @Override
        public final Var.VShort createVariable() {
            return new Var.VShort();
        }
        
        @Override
        public final void read(final Var.VShort param, final ByteBuffer buffer) {
            param.m_value = buffer.getShort();
        }
        
        @Override
        public void write(final OutputBitStream ostream, final String value) throws Exception {
            ostream.writeShort(Short.parseShort(value));
        }
    }
    
    public static final class MFloat extends ModelVar<Var.VFloat>
    {
        public MFloat(final String name) {
            super(name);
        }
        
        @Override
        public final Var.VFloat createVariable() {
            return new Var.VFloat();
        }
        
        @Override
        public void read(final Var.VFloat param, final ByteBuffer buffer) throws Exception {
            param.m_value = buffer.getFloat();
        }
        
        @Override
        public void write(final OutputBitStream ostream, final String value) throws Exception {
            ostream.writeFloat(Float.parseFloat(value));
        }
    }
    
    public static final class MString extends ModelVar<Var.VString>
    {
        public MString(final String name) {
            super(name);
        }
        
        @Override
        public Var.VString createVariable() {
            return new Var.VString();
        }
        
        @Override
        public void read(final Var.VString param, final ByteBuffer buffer) throws Exception {
            final int length = buffer.get() & 0xFF;
            final byte[] nameBytes = new byte[length];
            buffer.get(nameBytes);
            param.m_value = StringUtils.fromUTF8(nameBytes);
        }
        
        @Override
        public void write(final OutputBitStream ostream, final String value) throws Exception {
            final byte[] nameBytes = StringUtils.toUTF8(value);
            ostream.writeByte((byte)nameBytes.length);
            ostream.writeBytes(nameBytes);
        }
    }
    
    public static final class MSpline extends ModelVar<Var.VSpline>
    {
        public MSpline(final String name) {
            super(name);
        }
        
        @Override
        public Var.VSpline createVariable() {
            return new Var.VSpline();
        }
        
        @Override
        public void read(final Var.VSpline param, final ByteBuffer buffer) throws Exception {
            param.m_value.setValueBounds(buffer.getFloat(), buffer.getFloat(), buffer.getFloat(), buffer.getFloat());
            final int count = buffer.get() & 0xFF;
            param.m_value.clear();
            for (int i = 0; i < count; ++i) {
                final double x = buffer.getFloat();
                final double y = buffer.getFloat();
                final ControlPoint pt = new ControlPoint(x, y);
                param.m_value.addPoint(pt);
                pt.setInTangent(buffer.getFloat(), buffer.getFloat());
                pt.setOutTangent(buffer.getFloat(), buffer.getFloat());
            }
        }
        
        @Override
        public void write(final OutputBitStream ostream, final String value) throws Exception {
            final Spline spline = SplineParser.fromText(value);
            final int count = spline.size();
            ostream.writeFloat((float)spline.getBoundMinX());
            ostream.writeFloat((float)spline.getBoundMaxX());
            ostream.writeFloat((float)spline.getBoundMinY());
            ostream.writeFloat((float)spline.getBoundMaxY());
            ostream.writeByte((byte)count);
            for (int i = 0; i < count; ++i) {
                final ControlPoint pt = spline.get(i);
                ostream.writeFloat((float)pt.getX());
                ostream.writeFloat((float)pt.getY());
                ostream.writeFloat((float)pt.getInX());
                ostream.writeFloat((float)pt.getInY());
                ostream.writeFloat((float)pt.getOutX());
                ostream.writeFloat((float)pt.getOutY());
            }
        }
    }
}
