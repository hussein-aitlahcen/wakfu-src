package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class ColorFaderAttributeRW extends AffectorAttributesRW<ColorFader>
{
    public static final ColorFaderAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 4;
    }
    
    @Override
    public ColorFader createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float red = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float green = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float blue = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float alpha = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float speed = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new ColorFader(red, green, blue, alpha, speed);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final ColorFader min, final ColorFader max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_red, max.m_red);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_green, max.m_green);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_blue, max.m_blue);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_alpha, max.m_alpha);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_speed, max.m_speed);
    }
    
    @Override
    protected boolean equals(final ColorFader min, final ColorFader max) {
        return min.m_red == max.m_red && min.m_green == max.m_green && min.m_blue == max.m_blue && min.m_alpha == max.m_alpha && min.m_speed == max.m_speed;
    }
    
    static {
        m_instance = new ColorFaderAttributeRW();
    }
}
