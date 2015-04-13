package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class RotationAttributeRW extends AffectorAttributesRW<Rotation>
{
    public static final RotationAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 13;
    }
    
    @Override
    public Rotation createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float angleX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float angleY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float angleZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new Rotation(angleX, angleY, angleZ);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final Rotation min, final Rotation max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_angleX, max.m_angleX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_angleY, max.m_angleY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_angleZ, max.m_angleZ);
    }
    
    @Override
    protected boolean equals(final Rotation min, final Rotation max) {
        return min.m_angleX == max.m_angleX && min.m_angleY == max.m_angleY && min.m_angleZ == max.m_angleZ;
    }
    
    static {
        m_instance = new RotationAttributeRW();
    }
}
