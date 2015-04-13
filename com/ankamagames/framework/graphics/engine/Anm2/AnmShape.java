package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AnmShape
{
    public static final byte HAS_ROTATION_SKEW = 1;
    public static final byte HAS_TRANSLATION = 2;
    public static final byte HAS_ADD_COLOR = 4;
    public static final byte HAS_MUL_COLOR = 8;
    public static final byte IS_COMPRESSED = 16;
    public static final byte USE_COMPRESSED_ROTATION_SKEW = 32;
    public static final byte USE_COMPRESSED_TRANSLATION = 64;
    public static final byte USE_COMPRESSED_TRANSLATION_2 = Byte.MIN_VALUE;
    static final float ADD_FACTOR = 0.00390625f;
    static final float MULT_FACTOR = 0.007874016f;
    short m_id;
    
    void load(final ExtendedDataInputStream bitStream) throws IOException {
    }
    
    public final void setId(final short id) {
        this.m_id = id;
    }
    
    public float getRotationSkewX0() {
        return 1.0f;
    }
    
    public float getRotationSkewY0() {
        return 0.0f;
    }
    
    public float getRotationSkewX1() {
        return 0.0f;
    }
    
    public float getRotationSkewY1() {
        return 1.0f;
    }
    
    public float getTranslationX() {
        return 0.0f;
    }
    
    public float getTranslationY() {
        return 0.0f;
    }
    
    public float getAddRed() {
        return 0.0f;
    }
    
    public float getAddGreen() {
        return 0.0f;
    }
    
    public float getAddBlue() {
        return 0.0f;
    }
    
    public float getAddAlpha() {
        return 0.0f;
    }
    
    public float getMulRed() {
        return 1.0f;
    }
    
    public float getMulGreen() {
        return 1.0f;
    }
    
    public float getMulBlue() {
        return 1.0f;
    }
    
    public float getMulAlpha() {
        return 1.0f;
    }
    
    public final boolean hasRotation() {
        return this.getRotationSkewX0() != 1.0f || this.getRotationSkewY0() != 0.0f || this.getRotationSkewX1() != 0.0f || this.getRotationSkewY1() != 1.0f;
    }
    
    public final boolean hasTranslation() {
        return this.getTranslationX() != 0.0f || this.getTranslationY() != 0.0f;
    }
    
    public final boolean hasColorAdd() {
        return this.getAddRed() != 0.0f || this.getAddGreen() != 0.0f || this.getAddBlue() != 0.0f || this.getAddAlpha() != 0.0f;
    }
    
    public final boolean hasColorMul() {
        return this.getMulRed() != 1.0f || this.getMulGreen() != 1.0f || this.getMulBlue() != 1.0f || this.getMulAlpha() != 1.0f;
    }
    
    public void process(final AnmTransform parentTransform, final AnmTransform result) {
        result.m_rotationIsIdentity = parentTransform.m_rotationIsIdentity;
        result.m_rotationSkewX0 = parentTransform.m_rotationSkewX0;
        result.m_rotationSkewX1 = parentTransform.m_rotationSkewX1;
        result.m_rotationSkewY0 = parentTransform.m_rotationSkewY0;
        result.m_rotationSkewY1 = parentTransform.m_rotationSkewY1;
        result.m_translationIsIdentity = parentTransform.m_translationIsIdentity;
        result.m_translationX = parentTransform.m_translationX;
        result.m_translationY = parentTransform.m_translationY;
        result.m_red = parentTransform.m_red;
        result.m_green = parentTransform.m_green;
        result.m_blue = parentTransform.m_blue;
        result.m_alpha = parentTransform.m_alpha;
    }
}
