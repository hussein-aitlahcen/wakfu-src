package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class ReboundAttributeRW extends AffectorAttributesRW<Rebound>
{
    public static final ReboundAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 9;
    }
    
    @Override
    public Rebound createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final float minX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float minY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float minZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float maxX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float maxY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float maxZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float restitutionX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float restitutionY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float restitutionZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float restitutionRandomX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float restitutionRandomY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float restitutionRandomZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new Rebound(minX, minY, minZ, maxX, maxY, maxZ, restitutionX, restitutionY, restitutionZ, restitutionRandomX, restitutionRandomY, restitutionRandomZ);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final Rebound min, final Rebound max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_minX, max.m_minX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_minY, max.m_minY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_minZ, max.m_minZ);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_maxX, max.m_maxX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_maxY, max.m_maxY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_maxZ, max.m_maxZ);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_restitutionX, max.m_restitutionX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_restitutionY, max.m_restitutionY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_restitutionZ, max.m_restitutionZ);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_restitutionRandomX, max.m_restitutionRandomX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_restitutionRandomY, max.m_restitutionRandomY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_restitutionRandomZ, max.m_restitutionRandomZ);
    }
    
    @Override
    protected boolean equals(final Rebound min, final Rebound max) {
        return min.m_minX == max.m_minX && min.m_minY == max.m_minY && min.m_minZ == max.m_minZ && min.m_maxX == max.m_maxX && min.m_maxY == max.m_maxY && min.m_maxZ == max.m_maxZ && min.m_restitutionX == max.m_restitutionX && min.m_restitutionY == max.m_restitutionY && min.m_restitutionZ == max.m_restitutionZ && min.m_restitutionRandomX == max.m_restitutionRandomX && min.m_restitutionRandomY == max.m_restitutionRandomY && min.m_restitutionRandomZ == max.m_restitutionRandomZ;
    }
    
    static {
        m_instance = new ReboundAttributeRW();
    }
}
