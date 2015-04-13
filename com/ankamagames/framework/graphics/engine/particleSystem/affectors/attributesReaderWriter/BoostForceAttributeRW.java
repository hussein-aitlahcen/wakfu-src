package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class BoostForceAttributeRW extends AffectorAttributesRW<BoostForce>
{
    public static final BoostForceAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 2;
    }
    
    @Override
    public BoostForce createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float x = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float y = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float z = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new BoostForce(x, y, z);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final BoostForce min, final BoostForce max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_x, max.m_x);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_y, max.m_y);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_z, max.m_z);
    }
    
    @Override
    protected boolean equals(final BoostForce min, final BoostForce max) {
        return min.m_x == max.m_x && min.m_y == max.m_y && min.m_z == max.m_z;
    }
    
    static {
        m_instance = new BoostForceAttributeRW();
    }
}
