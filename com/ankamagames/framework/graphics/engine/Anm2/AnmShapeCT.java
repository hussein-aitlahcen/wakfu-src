package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AnmShapeCT extends AnmShape
{
    short m_translationX;
    short m_translationY;
    
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        this.m_translationX = bitStream.readShort();
        this.m_translationY = bitStream.readShort();
    }
    
    @Override
    public final float getTranslationX() {
        return this.m_translationX / 256.0f;
    }
    
    @Override
    public final float getTranslationY() {
        return this.m_translationY / 256.0f;
    }
    
    @Override
    public void process(final AnmTransform parentTransform, final AnmTransform result) {
        result.m_rotationIsIdentity = parentTransform.m_rotationIsIdentity;
        result.m_rotationSkewX0 = parentTransform.m_rotationSkewX0;
        result.m_rotationSkewX1 = parentTransform.m_rotationSkewX1;
        result.m_rotationSkewY0 = parentTransform.m_rotationSkewY0;
        result.m_rotationSkewY1 = parentTransform.m_rotationSkewY1;
        result.m_translationIsIdentity = false;
        if (parentTransform.m_rotationIsIdentity) {
            result.m_translationX = this.m_translationX / 256.0f + parentTransform.m_translationX;
            result.m_translationY = this.m_translationY / 256.0f + parentTransform.m_translationY;
        }
        else {
            result.m_translationX = this.m_translationX / 256.0f * parentTransform.m_rotationSkewX0 + this.m_translationY / 256.0f * parentTransform.m_rotationSkewX1 + parentTransform.m_translationX;
            result.m_translationY = this.m_translationX / 256.0f * parentTransform.m_rotationSkewY0 + this.m_translationY / 256.0f * parentTransform.m_rotationSkewY1 + parentTransform.m_translationY;
        }
        result.m_red = parentTransform.m_red;
        result.m_green = parentTransform.m_green;
        result.m_blue = parentTransform.m_blue;
        result.m_alpha = parentTransform.m_alpha;
    }
}
