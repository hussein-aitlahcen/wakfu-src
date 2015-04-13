package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class AnmShapeAM extends AnmShapeA
{
    byte m_mulRed;
    byte m_mulGreen;
    byte m_mulBlue;
    byte m_mulAlpha;
    
    public AnmShapeAM() {
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
        return this.m_mulRed / 127.0f;
    }
    
    @Override
    public final float getMulGreen() {
        return this.m_mulGreen / 127.0f;
    }
    
    @Override
    public final float getMulBlue() {
        return this.m_mulBlue / 127.0f;
    }
    
    @Override
    public final float getMulAlpha() {
        return this.m_mulAlpha / 127.0f;
    }
    
    @Override
    public void process(final AnmTransform parentTransform, final AnmTransform result) {
        result.m_rotationIsIdentity = parentTransform.m_rotationIsIdentity;
        result.m_rotationSkewX0 = parentTransform.m_rotationSkewX0;
        result.m_rotationSkewX1 = parentTransform.m_rotationSkewX1;
        result.m_rotationSkewY0 = parentTransform.m_rotationSkewY0;
        result.m_rotationSkewY1 = parentTransform.m_rotationSkewY1;
        result.m_translationIsIdentity = parentTransform.m_translationIsIdentity;
        result.m_translationX = parentTransform.m_translationX;
        result.m_translationY = parentTransform.m_translationY;
        result.m_red = parentTransform.m_red * (this.m_mulRed / 127.0f) + this.m_addRed / 256.0f;
        result.m_green = parentTransform.m_green * (this.m_mulGreen / 127.0f) + this.m_addGreen / 256.0f;
        result.m_blue = parentTransform.m_blue * (this.m_mulBlue / 127.0f) + this.m_addBlue / 256.0f;
        result.m_alpha = parentTransform.m_alpha * (this.m_mulAlpha / 127.0f) + this.m_addAlpha / 256.0f;
    }
}
