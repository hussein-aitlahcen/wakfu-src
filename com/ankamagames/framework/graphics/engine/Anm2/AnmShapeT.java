package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AnmShapeT extends AnmShape
{
    short m_translationX;
    short m_translationY;
    
    public AnmShapeT() {
        super();
        this.m_translationX = 0;
        this.m_translationY = 0;
    }
    
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
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
        result.m_red = parentTransform.m_red;
        result.m_green = parentTransform.m_green;
        result.m_blue = parentTransform.m_blue;
        result.m_alpha = parentTransform.m_alpha;
    }
}
