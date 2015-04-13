package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class LightRadiusDeformerAttributeRW extends AffectorAttributesRW<LightRadiusDeformer>
{
    public static final LightRadiusDeformerAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 11;
    }
    
    @Override
    public LightRadiusDeformer createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float radius = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new LightRadiusDeformer(radius);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final LightRadiusDeformer min, final LightRadiusDeformer max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_growthX, max.m_growthX);
    }
    
    @Override
    protected boolean equals(final LightRadiusDeformer min, final LightRadiusDeformer max) {
        return min.m_growthX == max.m_growthX;
    }
    
    static {
        m_instance = new LightRadiusDeformerAttributeRW();
    }
}
