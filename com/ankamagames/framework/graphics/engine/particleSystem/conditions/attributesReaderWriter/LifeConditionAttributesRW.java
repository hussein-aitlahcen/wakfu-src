package com.ankamagames.framework.graphics.engine.particleSystem.conditions.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.conditions.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class LifeConditionAttributesRW extends ConditionAttributesRW<LifeCondition>
{
    public static final LifeConditionAttributesRW m_instance;
    
    @Override
    protected byte getId() {
        return 1;
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final LifeCondition minAffector, final LifeCondition maxAffector) throws IOException {
        final boolean leveled = !minAffector.equals(maxAffector);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, minAffector.m_start, maxAffector.m_start);
        AttributesReaderWriter.writeFloat(ostream, leveled, minAffector.m_end, maxAffector.m_end);
    }
    
    @Override
    public LifeCondition createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float start = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float end = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new LifeCondition(start, end);
    }
    
    @Override
    protected boolean equals(final LifeCondition minAffector, final LifeCondition maxAffector) {
        return minAffector.m_end == maxAffector.m_end && minAffector.m_start == maxAffector.m_start;
    }
    
    static {
        m_instance = new LifeConditionAttributesRW();
    }
}
