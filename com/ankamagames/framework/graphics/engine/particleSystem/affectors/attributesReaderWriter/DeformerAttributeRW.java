package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class DeformerAttributeRW extends AffectorAttributesRW<Deformer>
{
    public static final DeformerAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 5;
    }
    
    @Override
    public Deformer createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float growthX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float growthY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float angle = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float growthRandomX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float growthRandomY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float angleRandom = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new Deformer(growthX, growthY, angle, growthRandomX, growthRandomY, angleRandom);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final Deformer min, final Deformer max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_growthX, max.m_growthX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_growthY, max.m_growthY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_angle, max.m_angle);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_growthRandomX, max.m_growthRandomX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_growthRandomY, max.m_growthRandomY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_angleRandom, max.m_angleRandom);
    }
    
    @Override
    protected boolean equals(final Deformer min, final Deformer max) {
        return min.m_growthX == max.m_growthX && min.m_growthY == max.m_growthY && min.m_angle == max.m_angle && min.m_growthRandomX == max.m_growthRandomX && min.m_growthRandomY == max.m_growthRandomY && min.m_angleRandom == max.m_angleRandom;
    }
    
    static {
        m_instance = new DeformerAttributeRW();
    }
}
