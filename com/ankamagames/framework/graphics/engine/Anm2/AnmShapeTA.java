package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AnmShapeTA extends AnmShapeT
{
    short m_addRed;
    short m_addGreen;
    short m_addBlue;
    short m_addAlpha;
    
    public AnmShapeTA() {
        super();
        this.m_addRed = 0;
        this.m_addGreen = 0;
        this.m_addBlue = 0;
        this.m_addAlpha = 0;
    }
    
    @Override
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        super.load(bitStream);
        this.m_addRed = bitStream.readShort();
        this.m_addGreen = bitStream.readShort();
        this.m_addBlue = bitStream.readShort();
        this.m_addAlpha = bitStream.readShort();
    }
    
    @Override
    public final float getAddRed() {
        return this.m_addRed * 0.00390625f;
    }
    
    @Override
    public final float getAddGreen() {
        return this.m_addGreen * 0.00390625f;
    }
    
    @Override
    public final float getAddBlue() {
        return this.m_addBlue * 0.00390625f;
    }
    
    @Override
    public final float getAddAlpha() {
        return this.m_addAlpha * 0.00390625f;
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
        result.m_red = parentTransform.m_red + this.m_addRed * 0.00390625f;
        result.m_green = parentTransform.m_green + this.m_addGreen * 0.00390625f;
        result.m_blue = parentTransform.m_blue + this.m_addBlue * 0.00390625f;
        result.m_alpha = parentTransform.m_alpha + this.m_addAlpha * 0.00390625f;
    }
}
