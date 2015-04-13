package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AnmShapeRT extends AnmShapeR
{
    short m_translationX;
    short m_translationY;
    
    public AnmShapeRT() {
        super();
        this.m_translationX = 0;
        this.m_translationY = 0;
    }
    
    @Override
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        super.load(bitStream);
        this.m_translationX = (short)(bitStream.readFloat() * 16.0f);
        this.m_translationY = (short)(bitStream.readFloat() * 16.0f);
    }
    
    @Override
    public final float getTranslationX() {
        return this.m_translationX / 16.0f;
    }
    
    @Override
    public final float getTranslationY() {
        return this.m_translationY / 16.0f;
    }
    
    @Override
    public void process(final AnmTransform parentTransform, final AnmTransform result) {
        result.m_rotationIsIdentity = false;
        result.m_translationIsIdentity = false;
        final float tx = this.m_translationX / 16.0f;
        final float ty = this.m_translationY / 16.0f;
        final float rx0 = this.m_rotationSkewX0 / 256.0f;
        final float ry0 = this.m_rotationSkewY0 / 256.0f;
        final float rx = this.m_rotationSkewX1 / 256.0f;
        final float ry = this.m_rotationSkewY1 / 256.0f;
        if (parentTransform.m_rotationIsIdentity) {
            result.m_rotationSkewX0 = rx0;
            result.m_rotationSkewY0 = ry0;
            result.m_rotationSkewX1 = rx;
            result.m_rotationSkewY1 = ry;
            result.m_translationX = tx + parentTransform.m_translationX;
            result.m_translationY = ty + parentTransform.m_translationY;
        }
        else {
            result.m_rotationSkewX0 = rx0 * parentTransform.m_rotationSkewX0 + ry0 * parentTransform.m_rotationSkewX1;
            result.m_rotationSkewY0 = rx0 * parentTransform.m_rotationSkewY0 + ry0 * parentTransform.m_rotationSkewY1;
            result.m_rotationSkewX1 = rx * parentTransform.m_rotationSkewX0 + ry * parentTransform.m_rotationSkewX1;
            result.m_rotationSkewY1 = rx * parentTransform.m_rotationSkewY0 + ry * parentTransform.m_rotationSkewY1;
            result.m_translationX = tx * parentTransform.m_rotationSkewX0 + ty * parentTransform.m_rotationSkewX1 + parentTransform.m_translationX;
            result.m_translationY = tx * parentTransform.m_rotationSkewY0 + ty * parentTransform.m_rotationSkewY1 + parentTransform.m_translationY;
        }
        result.m_red = parentTransform.m_red;
        result.m_green = parentTransform.m_green;
        result.m_blue = parentTransform.m_blue;
        result.m_alpha = parentTransform.m_alpha;
    }
}
