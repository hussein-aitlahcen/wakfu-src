package com.ankamagames.framework.graphics.engine.particleSystem.conditions.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.conditions.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class PositionConditionAttributesRW extends ConditionAttributesRW<PositionCondition>
{
    public static final PositionConditionAttributesRW m_instance;
    
    @Override
    protected byte getId() {
        return 2;
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final PositionCondition min, final PositionCondition max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.writeBooleanBit(min.m_useSystemAsReference);
        ostream.align();
        ostream.writeByte((byte)min.m_condition.ordinal());
        AttributesReaderWriter.writeUnsignedShort(ostream, leveled, min.m_value, max.m_value);
    }
    
    @Override
    public PositionCondition createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final boolean useSystemAsReference = istream.readBooleanBit();
        final byte modeValue = istream.readByte();
        final PositionCondition.PositionConditionMode mode = PositionCondition.PositionConditionMode.values()[modeValue];
        final int value = AttributesReaderWriter.readUnsignedShort(istream, leveled, levelPercent);
        return new PositionCondition(value, mode, useSystemAsReference);
    }
    
    @Override
    protected boolean equals(final PositionCondition min, final PositionCondition max) {
        return min.m_value == max.m_value;
    }
    
    static {
        m_instance = new PositionConditionAttributesRW();
    }
}
