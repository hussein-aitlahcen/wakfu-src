package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class CirclePathAttributeRW extends AffectorAttributesRW<CirclePath>
{
    public static final CirclePathAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 3;
    }
    
    @Override
    public CirclePath createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float radialSpeed = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new CirclePath(radialSpeed);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final CirclePath min, final CirclePath max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_radialSpeed, max.m_radialSpeed);
    }
    
    @Override
    protected boolean equals(final CirclePath min, final CirclePath max) {
        return min.m_radialSpeed == max.m_radialSpeed;
    }
    
    static {
        m_instance = new CirclePathAttributeRW();
    }
}
