package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AnmShapeCRT extends AnmShapeCR
{
    byte m_translationX;
    byte m_translationY;
    
    @Override
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        super.load(bitStream);
        this.m_translationX = bitStream.readByte();
        this.m_translationY = bitStream.readByte();
    }
    
    @Override
    public final float getTranslationX() {
        return this.m_translationX * 16.0f / 127.0f;
    }
    
    @Override
    public final float getTranslationY() {
        return this.m_translationY * 16.0f / 127.0f;
    }
    
    @Override
    public void process(final AnmTransform parentTransform, final AnmTransform result) {
        result.m_rotationIsIdentity = false;
        if (parentTransform.m_rotationIsIdentity) {
            result.m_rotationSkewX0 = this.m_rotationSkewX0 / 127.0f;
            result.m_rotationSkewY0 = this.m_rotationSkewY0 / 127.0f;
            result.m_rotationSkewX1 = this.m_rotationSkewX1 / 127.0f;
            result.m_rotationSkewY1 = this.m_rotationSkewY1 / 127.0f;
        }
        else {
            result.m_rotationSkewX0 = this.m_rotationSkewX0 / 127.0f * parentTransform.m_rotationSkewX0 + this.m_rotationSkewY0 / 127.0f * parentTransform.m_rotationSkewX1;
            result.m_rotationSkewY0 = this.m_rotationSkewX0 / 127.0f * parentTransform.m_rotationSkewY0 + this.m_rotationSkewY0 / 127.0f * parentTransform.m_rotationSkewY1;
            result.m_rotationSkewX1 = this.m_rotationSkewX1 / 127.0f * parentTransform.m_rotationSkewX0 + this.m_rotationSkewY1 / 127.0f * parentTransform.m_rotationSkewX1;
            result.m_rotationSkewY1 = this.m_rotationSkewX1 / 127.0f * parentTransform.m_rotationSkewY0 + this.m_rotationSkewY1 / 127.0f * parentTransform.m_rotationSkewY1;
        }
        result.m_translationIsIdentity = false;
        final float tx = this.m_translationX * 16.0f / 127.0f;
        final float ty = this.m_translationY * 16.0f / 127.0f;
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
