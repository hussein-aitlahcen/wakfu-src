package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AnmShapeTAM extends AnmShapeTA
{
    byte m_mulRed;
    byte m_mulGreen;
    byte m_mulBlue;
    byte m_mulAlpha;
    
    public AnmShapeTAM() {
        super();
        this.m_mulRed = 127;
        this.m_mulGreen = 127;
        this.m_mulBlue = 127;
        this.m_mulAlpha = 127;
    }
    
    @Override
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        super.load(bitStream);
        this.m_mulRed = bitStream.readByte();
        this.m_mulGreen = bitStream.readByte();
        this.m_mulBlue = bitStream.readByte();
        this.m_mulAlpha = bitStream.readByte();
    }
    
    @Override
    public final float getMulRed() {
        return this.m_mulRed * 0.007874016f;
    }
    
    @Override
    public final float getMulGreen() {
        return this.m_mulGreen * 0.007874016f;
    }
    
    @Override
    public final float getMulBlue() {
        return this.m_mulBlue * 0.007874016f;
    }
    
    @Override
    public final float getMulAlpha() {
        return this.m_mulAlpha * 0.007874016f;
    }
    
    @Override
    public void process(final AnmTransform parentTransform, final AnmTransform result) {
        result.m_rotationIsIdentity = parentTransform.m_rotationIsIdentity;
        result.m_rotationSkewX0 = parentTransform.m_rotationSkewX0;
        result.m_rotationSkewX1 = parentTransform.m_rotationSkewX1;
        result.m_rotationSkewY0 = parentTransform.m_rotationSkewY0;
        result.m_rotationSkewY1 = parentTransform.m_rotationSkewY1;
        result.m_translationIsIdentity = false;
        final float tx = this.m_translationX / 16.0f;
        final float ty = this.m_translationY / 16.0f;
        if (parentTransform.m_rotationIsIdentity) {
            result.m_translationX = tx + parentTransform.m_translationX;
            result.m_translationY = ty + parentTransform.m_translationY;
        }
        else {
            result.m_translationX = tx * parentTransform.m_rotationSkewX0 + ty * parentTransform.m_rotationSkewX1 + parentTransform.m_translationX;
            result.m_translationY = tx * parentTransform.m_rotationSkewY0 + ty * parentTransform.m_rotationSkewY1 + parentTransform.m_translationY;
        }
        result.m_red = parentTransform.m_red * (this.m_mulRed * 0.007874016f) + this.m_addRed * 0.00390625f;
        result.m_green = parentTransform.m_green * (this.m_mulGreen * 0.007874016f) + this.m_addGreen * 0.00390625f;
        result.m_blue = parentTransform.m_blue * (this.m_mulBlue * 0.007874016f) + this.m_addBlue * 0.00390625f;
        result.m_alpha = parentTransform.m_alpha * (this.m_mulAlpha * 0.007874016f) + this.m_addAlpha * 0.00390625f;
    }
}
