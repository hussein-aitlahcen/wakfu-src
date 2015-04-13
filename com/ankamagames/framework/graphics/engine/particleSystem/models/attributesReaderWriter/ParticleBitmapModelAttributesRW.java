package com.ankamagames.framework.graphics.engine.particleSystem.models.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.models.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class ParticleBitmapModelAttributesRW extends ParticleModelAttributesRW<ParticleBitmapModel>
{
    public static final ParticleBitmapModelAttributesRW m_instance;
    
    @Override
    protected byte getId() {
        return 1;
    }
    
    @Override
    public ParticleBitmapModel createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
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
        final float textureTop = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float textureLeft = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float textureBottom = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float textureRight = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float halfWidth = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float halfHeight = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float rotationX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float rotationY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float rotationZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final ParticleBitmapModel model = new ParticleBitmapModel(bitmapId, hotX, hotY, scaleX, scaleY, scaleRandomX, scaleRandomY, scaleRandomKeepRatio, rotation, rotationRandom, redColor, greenColor, blueColor, alphaColor, redColorRandom, greenColorRandom, blueColorRandom, alphaColorRandom, rotationX, rotationY, rotationZ);
        model.setTextureCoords(textureTop, textureLeft, textureBottom, textureRight);
        model.setHalfSize(halfWidth, halfHeight);
        return model;
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final ParticleBitmapModel min, final ParticleBitmapModel max) throws IOException {
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
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_textureTop, max.m_textureTop);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_textureLeft, max.m_textureLeft);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_textureBottom, max.m_textureBottom);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_textureRight, max.m_textureRight);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_halfWidth, max.m_halfWidth);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_halfHeight, max.m_halfHeight);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_rotationX, max.m_rotationX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_rotationY, max.m_rotationY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_rotationZ, max.m_rotationZ);
    }
    
    static {
        m_instance = new ParticleBitmapModelAttributesRW();
    }
}
