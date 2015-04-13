package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class AttractionForceAttributeRW extends AffectorAttributesRW<AttractionForce>
{
    public static final AttractionForceAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 1;
    }
    
    @Override
    public AttractionForce createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float intensity = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float offsetX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float offsetY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float offsetZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final byte axisValue = istream.readByte();
        final AttractionForce.AttractorAxis axis = AttractionForce.AttractorAxis.values()[axisValue];
        return new AttractionForce(intensity, axis, offsetX, offsetY, offsetZ);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final AttractionForce min, final AttractionForce max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_intensity, max.m_intensity);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_offsetX, max.m_offsetX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_offsetY, max.m_offsetY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_offsetZ, max.m_offsetZ);
        final byte axisMode = (byte)min.m_axis.ordinal();
        ostream.writeByte(axisMode);
    }
    
    @Override
    protected boolean equals(final AttractionForce min, final AttractionForce max) {
        return min.m_intensity == max.m_intensity && min.m_offsetX == max.m_offsetX && min.m_offsetY == max.m_offsetY && min.m_offsetZ == max.m_offsetZ;
    }
    
    static {
        m_instance = new AttractionForceAttributeRW();
    }
}
