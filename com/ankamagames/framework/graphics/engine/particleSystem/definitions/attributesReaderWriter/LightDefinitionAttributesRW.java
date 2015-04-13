package com.ankamagames.framework.graphics.engine.particleSystem.definitions.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.definitions.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class LightDefinitionAttributesRW extends AffectorableAttributesRW<LightDefinition>
{
    public static final LightDefinitionAttributesRW m_instance;
    
    @Override
    protected byte getId() {
        return 2;
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final LightDefinition min, final LightDefinition max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_red, max.m_red);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_green, max.m_green);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_blue, max.m_blue);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_intensity, max.m_intensity);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_range, max.m_range);
    }
    
    @Override
    public LightDefinition createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float red = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float green = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float blue = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float intensity = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float range = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new LightDefinition(red, green, blue, intensity, range);
    }
    
    @Override
    protected boolean equals(final LightDefinition min, final LightDefinition max) {
        return min.m_red == max.m_red && min.m_green == max.m_green && min.m_blue == max.m_blue && min.m_intensity == max.m_intensity && min.m_range == max.m_range;
    }
    
    static {
        m_instance = new LightDefinitionAttributesRW();
    }
}
