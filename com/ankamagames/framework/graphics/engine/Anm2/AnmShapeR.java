package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AnmShapeR extends AnmShape
{
    short m_rotationSkewX0;
    short m_rotationSkewY0;
    short m_rotationSkewX1;
    short m_rotationSkewY1;
    
    public AnmShapeR() {
        super();
        this.m_rotationSkewX0 = 256;
        this.m_rotationSkewY0 = 0;
        this.m_rotationSkewX1 = 0;
        this.m_rotationSkewY1 = 256;
    }
    
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        this.m_rotationSkewX0 = bitStream.readShort();
        this.m_rotationSkewY0 = bitStream.readShort();
        this.m_rotationSkewX1 = bitStream.readShort();
        this.m_rotationSkewY1 = bitStream.readShort();
    }
    
    @Override
    public final float getRotationSkewX0() {
        return this.m_rotationSkewX0 / 256.0f;
    }
    
    @Override
    public final float getRotationSkewY0() {
        return this.m_rotationSkewY0 / 256.0f;
    }
    
    @Override
    public final float getRotationSkewX1() {
        return this.m_rotationSkewX1 / 256.0f;
    }
    
    @Override
    public final float getRotationSkewY1() {
        return this.m_rotationSkewY1 / 256.0f;
    }
    
    @Override
    public void process(final AnmTransform parentTransform, final AnmTransform result) {
        result.m_rotationIsIdentity = false;
        if (parentTransform.m_rotationIsIdentity) {
            result.m_rotationSkewX0 = this.m_rotationSkewX0 / 256.0f;
            result.m_rotationSkewY0 = this.m_rotationSkewY0 / 256.0f;
            result.m_rotationSkewX1 = this.m_rotationSkewX1 / 256.0f;
            result.m_rotationSkewY1 = this.m_rotationSkewY1 / 256.0f;
        }
        else {
            result.m_rotationSkewX0 = this.m_rotationSkewX0 / 256.0f * parentTransform.m_rotationSkewX0 + this.m_rotationSkewY0 / 256.0f * parentTransform.m_rotationSkewX1;
            result.m_rotationSkewY0 = this.m_rotationSkewX0 / 256.0f * parentTransform.m_rotationSkewY0 + this.m_rotationSkewY0 / 256.0f * parentTransform.m_rotationSkewY1;
            result.m_rotationSkewX1 = this.m_rotationSkewX1 / 256.0f * parentTransform.m_rotationSkewX0 + this.m_rotationSkewY1 / 256.0f * parentTransform.m_rotationSkewX1;
            result.m_rotationSkewY1 = this.m_rotationSkewX1 / 256.0f * parentTransform.m_rotationSkewY0 + this.m_rotationSkewY1 / 256.0f * parentTransform.m_rotationSkewY1;
        }
        result.m_translationIsIdentity = parentTransform.m_translationIsIdentity;
        result.m_translationX = parentTransform.m_translationX;
        result.m_translationY = parentTransform.m_translationY;
        result.m_red = parentTransform.m_red;
        result.m_green = parentTransform.m_green;
        result.m_blue = parentTransform.m_blue;
        result.m_alpha = parentTransform.m_alpha;
    }
}
