package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class FrictionalForceAttributeRW extends AffectorAttributesRW<FrictionalForce>
{
    public static final FrictionalForceAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 7;
    }
    
    @Override
    public FrictionalForce createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float friction = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new FrictionalForce(friction);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final FrictionalForce min, final FrictionalForce max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_friction, max.m_friction);
    }
    
    @Override
    protected boolean equals(final FrictionalForce min, final FrictionalForce max) {
        return min.m_friction == max.m_friction;
    }
    
    static {
        m_instance = new FrictionalForceAttributeRW();
    }
}
