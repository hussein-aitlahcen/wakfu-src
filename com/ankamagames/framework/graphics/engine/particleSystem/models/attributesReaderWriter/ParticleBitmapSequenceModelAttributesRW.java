package com.ankamagames.framework.graphics.engine.particleSystem.models.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.models.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import com.ankamagames.framework.graphics.image.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class ParticleBitmapSequenceModelAttributesRW extends ParticleModelAttributesRW<ParticleBitmapSequenceModel>
{
    public static final ParticleBitmapSequenceModelAttributesRW m_instance;
    
    @Override
    protected byte getId() {
        return 2;
    }
    
    @Override
    public ParticleBitmapSequenceModel createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final boolean scaleRandomKeepRatio = istream.readBooleanBit();
        final int bitmapId = istream.readInt();
        final float hotX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float hotY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float scaleX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float scaleY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float scaleRandomX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float scaleRandomY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float rotation = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float rotationRandom = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float redColor = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float greenColor = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float blueColor = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float alphaColor = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float redColorRandom = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float greenColorRandom = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float blueColorRandom = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float alphaColorRandom = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float halfWidth = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float halfHeight = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final AnimData.Use animData = AnimData.Use.fromBuffer(istream, false);
        final float speed = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final int loopCount = AttributesReaderWriter.readInt(istream, leveled, levelPercent);
        final float rotationX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float rotationY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float rotationZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final ParticleBitmapSequenceModel model = new ParticleBitmapSequenceModel(bitmapId, hotX, hotY, scaleX, scaleY, scaleRandomX, scaleRandomY, scaleRandomKeepRatio, rotation, rotationRandom, redColor, greenColor, blueColor, alphaColor, redColorRandom, greenColorRandom, blueColorRandom, alphaColorRandom, animData, speed, loopCount, rotationX, rotationY, rotationZ);
        model.setHalfSize(halfWidth, halfHeight);
        return model;
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final ParticleBitmapSequenceModel min, final ParticleBitmapSequenceModel max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.writeBooleanBit(min.m_scaleRandomKeepRatio);
        ostream.align();
        ostream.writeInt(min.m_bitmapId);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_hotX, max.m_hotX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_hotY, max.m_hotY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_scaleX, max.m_scaleX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_scaleY, max.m_scaleY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_scaleRandomX, max.m_scaleRandomX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_scaleRandomY, max.m_scaleRandomY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_rotation, max.m_rotation);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_rotationRandom, max.m_rotationRandom);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_redColor, max.m_redColor);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_greenColor, max.m_greenColor);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_blueColor, max.m_blueColor);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_alphaColor, max.m_alphaColor);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_redColorRandom, max.m_redColorRandom);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_greenColorRandom, max.m_greenColorRandom);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_blueColorRandom, max.m_blueColorRandom);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_alphaColorRandom, max.m_alphaColorRandom);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_halfWidth * 0.5f, max.m_halfWidth * 0.5f);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_halfHeight * 0.5f, max.m_halfHeight * 0.5f);
        min.m_animData.write(ostream);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_speed, max.m_speed);
        if (min.m_loopCount == -1 || max.m_loopCount == -1) {
            AttributesReaderWriter.writeInt(ostream, leveled, -1, -1);
        }
        else {
            AttributesReaderWriter.writeInt(ostream, leveled, min.m_loopCount, max.m_loopCount);
        }
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_rotationX, max.m_rotationX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_rotationY, max.m_rotationY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_rotationZ, max.m_rotationZ);
    }
    
    @Override
    protected boolean equals(final ParticleBitmapSequenceModel min, final ParticleBitmapSequenceModel max) {
        return super.equals(min, max) && min.m_speed == max.m_speed && min.m_loopCount == max.m_loopCount;
    }
    
    static {
        m_instance = new ParticleBitmapSequenceModelAttributesRW();
    }
}
