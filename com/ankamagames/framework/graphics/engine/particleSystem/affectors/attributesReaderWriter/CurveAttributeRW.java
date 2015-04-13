package com.ankamagames.framework.graphics.engine.particleSystem.affectors.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.affectors.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.maths.motion.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import com.ankamagames.framework.fileFormat.io.*;

public class CurveAttributeRW extends AffectorAttributesRW<Curve>
{
    private static final Logger m_logger;
    public static final CurveAttributeRW m_instance;
    
    @Override
    protected byte getId() {
        return 12;
    }
    
    @Override
    public Curve createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final Vector3 startPos = readVector3(istream, levelPercent, leveled);
        final Vector3 startVelocity = readVector3(istream, levelPercent, leveled);
        final Vector3 endPos = readVector3(istream, levelPercent, leveled);
        final Vector3 endVelocity = readVector3(istream, levelPercent, leveled);
        final Trajectory t = new CubicSplineTrajectory();
        t.setInitialTime(0L);
        t.setFinalTime(1000000L);
        t.setInitialPosition(startPos);
        t.setInitialVelocity(startVelocity);
        t.setFinalPosition(endPos);
        t.setFinalVelocity(endVelocity);
        return new Curve(t);
    }
    
    private static Vector3 readVector3(final ExtendedDataInputStream istream, final float levelPercent, final boolean leveled) throws IOException {
        final float x = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float y = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float z = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new Vector3(x, y, z);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final Curve min, final Curve max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.align();
        this.writeVector3(ostream, leveled, min.getStartPosition(), max.getStartPosition());
        this.writeVector3(ostream, leveled, min.getStartVelocity(), max.getStartVelocity());
        this.writeVector3(ostream, leveled, min.getEndPosition(), max.getEndPosition());
        this.writeVector3(ostream, leveled, min.getEndVelocity(), max.getEndVelocity());
    }
    
    private void writeVector3(final OutputBitStream ostream, final boolean leveled, final Vector3 min, final Vector3 max) throws IOException {
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_x, max.m_x);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_y, max.m_y);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_z, max.m_z);
    }
    
    @Override
    protected boolean equals(final Curve min, final Curve max) {
        return min.getStartPosition().equals(max.getStartPosition()) && min.getStartVelocity().equals(max.getStartVelocity()) && min.getEndPosition().equals(max.getEndPosition()) && min.getEndPosition().equals(max.getEndVelocity());
    }
    
    static {
        m_logger = Logger.getLogger((Class)CurveAttributeRW.class);
        m_instance = new CurveAttributeRW();
    }
}
