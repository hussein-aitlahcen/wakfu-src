package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class RotorForceAttributeRW extends AffectorAttributesRW<RotorForce>
{
    public static final RotorForceAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 10;
    }
    
    @Override
    public RotorForce createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float intensity = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new RotorForce(intensity);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final RotorForce min, final RotorForce max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_intensity, max.m_intensity);
    }
    
    @Override
    protected boolean equals(final RotorForce min, final RotorForce max) {
        return min.m_intensity == max.m_intensity;
    }
    
    static {
        m_instance = new RotorForceAttributeRW();
    }
}
