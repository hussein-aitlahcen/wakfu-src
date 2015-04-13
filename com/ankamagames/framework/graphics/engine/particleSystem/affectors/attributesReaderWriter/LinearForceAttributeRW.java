package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class LinearForceAttributeRW extends AffectorAttributesRW<LinearForce>
{
    public static final LinearForceAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 8;
    }
    
    @Override
    public LinearForce createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final boolean applyOnVelocity = istream.readBooleanBit();
        final float x = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float y = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float z = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return LinearForce.create(x, y, z, applyOnVelocity);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final LinearForce min, final LinearForce max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.writeBooleanBit(min instanceof LinearForce.Velocity);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_x * 33.0f, max.m_x * 33.0f);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_y * 33.0f, max.m_y * 33.0f);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_z * 33.0f, max.m_z * 33.0f);
    }
    
    @Override
    protected boolean equals(final LinearForce min, final LinearForce max) {
        return min.m_x == max.m_x && min.m_y == max.m_y && min.m_z == max.m_z;
    }
    
    static {
        m_instance = new LinearForceAttributeRW();
    }
}
