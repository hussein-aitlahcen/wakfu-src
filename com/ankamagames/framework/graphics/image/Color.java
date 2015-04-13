package com.ankamagames.framework.graphics.image;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;

public final class Color
{
    public static final Color ALPHA;
    public static final Color WHITE_ALPHA;
    public static final Color WHITE_SEMI_ALPHA;
    public static final Color WHITE_QUARTER_ALPHA;
    public static final Color WHITE;
    public static final Color BLACK;
    public static final Color RED;
    public static final Color GREEN;
    public static final Color DARK_GREEN;
    public static final Color BLUE;
    public static final Color CYAN;
    public static final Color GRAY;
    public static final Color DARK_GRAY;
    public static final Color LIGHT_GRAY;
    public static final Color VERY_LIGHT_GRAY;
    public static final Color PURPLE;
    public static final Color GOLD;
    public static final Color ORANGE;
    private static final int BYTE_MASK = 255;
    private static final int MAX_COMPONENT_VALUE = 255;
    private static final int DECAL_ALPHA = 24;
    private static final int DECAL_BLUE = 16;
    private static final int DECAL_GREEN = 8;
    private static final int HEXA_BASE = 16;
    private int m_ABGR;
    
    public Color() {
        super();
        this.m_ABGR = 0;
    }
    
    public Color(final Color color) {
        super();
        this.m_ABGR = color.m_ABGR;
    }
    
    public Color(final float red, final float green, final float blue, final float alpha) {
        super();
        this.setFromFloat(red, green, blue, alpha);
    }
    
    public Color(final int ABGR) {
        super();
        this.m_ABGR = ABGR;
    }
    
    public Color(final byte red, final byte green, final byte blue, final byte alpha) {
        super();
        this.setFromByte(red, green, blue, alpha);
    }
    
    public Color(final int red, final int green, final int blue, final int alpha) {
        super();
        this.setFromInt(red, green, blue, alpha);
    }
    
    public int get() {
        return this.m_ABGR;
    }
    
    public int getABGR() {
        return this.m_ABGR;
    }
    
    public int getRGBA() {
        return _getFromInt(this.getRedInt(), this.getGreenInt(), this.getBlueInt(), this.getAlphaInt());
    }
    
    public int getARGB() {
        return _getFromInt(this.getAlphaInt(), this.getRedInt(), this.getGreenInt(), this.getBlueInt());
    }
    
    public byte getAlphaByte() {
        return (byte)(this.m_ABGR >> 24 & 0xFF);
    }
    
    public byte getRedByte() {
        return (byte)(this.m_ABGR & 0xFF);
    }
    
    public byte getGreenByte() {
        return (byte)(this.m_ABGR >> 8 & 0xFF);
    }
    
    public byte getBlueByte() {
        return (byte)(this.m_ABGR >> 16 & 0xFF);
    }
    
    public int getAlphaInt() {
        return this.m_ABGR >> 24 & 0xFF;
    }
    
    public int getRedInt() {
        return this.m_ABGR & 0xFF;
    }
    
    public int getGreenInt() {
        return this.m_ABGR >> 8 & 0xFF;
    }
    
    public int getBlueInt() {
        return this.m_ABGR >> 16 & 0xFF;
    }
    
    public float getAlpha() {
        int a = this.getAlphaInt();
        if (a < 0) {
            a += 256;
        }
        return a / 255.0f;
    }
    
    public float getRed() {
        return this.getRedInt() / 255.0f;
    }
    
    public float getGreen() {
        return this.getGreenInt() / 255.0f;
    }
    
    public float getBlue() {
        return this.getBlueInt() / 255.0f;
    }
    
    public float getMaxRGBComponent() {
        return Math.max(this.getRed(), Math.max(this.getBlue(), this.getGreen()));
    }
    
    public float getIntensity() {
        return (this.getRed() + this.getGreen() + this.getBlue()) / 3.0f;
    }
    
    public void setIntensity(final float intensity) {
        assert intensity >= 0.0f && intensity <= 1.0f : "Invalid intensity value " + intensity;
        final float maxFactor = Math.max(this.getRed(), Math.max(this.getBlue(), this.getGreen()));
        if (maxFactor == 0.0f) {
            this.setFromFloat(intensity, intensity, intensity, this.getAlpha());
            return;
        }
        final float factor = intensity / maxFactor;
        final float red = Math.min(1.0f, this.getRed() * factor);
        final float blue = Math.min(1.0f, this.getBlue() * factor);
        final float green = Math.min(1.0f, this.getGreen() * factor);
        this.setFromFloat(red, green, blue, this.getAlpha());
    }
    
    public float[] getFloatRGBA() {
        return new float[] { this.getRed(), this.getGreen(), this.getBlue(), this.getAlpha() };
    }
    
    public void setFromFloat(final float red, final float green, final float blue, final float alpha) {
        this.m_ABGR = getFromFloat(red, green, blue, alpha);
    }
    
    public void setAlpha(final float alpha) {
        final float a = MathHelper.clamp(alpha, 0.0f, 1.0f);
        this.m_ABGR = ((this.m_ABGR & 0xFFFFFF) | (int)(a * 255.0f) << 24);
    }
    
    public void set(final Color color) {
        this.m_ABGR = color.m_ABGR;
    }
    
    public void set(final int ABGR) {
        this.m_ABGR = ABGR;
    }
    
    public void setBRGA(final int bgra) {
        this.m_ABGR = getFromBGRAInt(bgra);
    }
    
    public void setARGB(final int argb) {
        this.m_ABGR = getFromARGBInt(argb);
    }
    
    public void setFromByte(final byte red, final byte green, final byte blue, final byte alpha) {
        this.m_ABGR = getFromByte(red, green, blue, alpha);
    }
    
    public void setFromInt(final int red, final int green, final int blue, final int alpha) {
        this.m_ABGR = getFromInt(red, green, blue, alpha);
    }
    
    public void mult(final Color c) {
        this.setFromFloat(this.getRed() * c.getRed(), this.getGreen() * c.getGreen(), this.getBlue() * c.getBlue(), this.getAlpha() * c.getAlpha());
    }
    
    public void random() {
        this.setFromInt(MathHelper.random(0, 255), MathHelper.random(0, 255), MathHelper.random(0, 255), MathHelper.random(0, 255));
    }
    
    public static Color mult(final Color c1, final Color c2) {
        final Color c3 = new Color(c1);
        c3.mult(c2);
        return c3;
    }
    
    public static float getAlphaFromARGB(final int ARGB) {
        return (ARGB >> 24 & 0xFF) / 255.0f;
    }
    
    public static float getBlueFromARGB(final int ARGB) {
        return (ARGB >> 16 & 0xFF) / 255.0f;
    }
    
    public static float getGreenFromARGB(final int ARGB) {
        return (ARGB >> 8 & 0xFF) / 255.0f;
    }
    
    public static float getRedFromARGB(final int ARGB) {
        return (ARGB & 0xFF) / 255.0f;
    }
    
    public static int getFromFloat(final float red, final float green, final float blue, final float alpha) {
        return (int)(MathHelper.clamp(alpha, 0.0f, 1.0f) * 255.0f) << 24 | (int)(MathHelper.clamp(red, 0.0f, 1.0f) * 255.0f) | (int)(MathHelper.clamp(green, 0.0f, 1.0f) * 255.0f) << 8 | (int)(MathHelper.clamp(blue, 0.0f, 1.0f) * 255.0f) << 16;
    }
    
    public static int getFromByte(final byte red, final byte green, final byte blue, final byte alpha) {
        return Caster.toUnsignedByte(alpha) << 24 | Caster.toUnsignedByte(red) | Caster.toUnsignedByte(green) << 8 | Caster.toUnsignedByte(blue) << 16;
    }
    
    public static int getFromInt(final int red, final int green, final int blue, final int alpha) {
        return MathHelper.clamp(alpha, 0, 255) << 24 | MathHelper.clamp(red, 0, 255) | MathHelper.clamp(green, 0, 255) << 8 | MathHelper.clamp(blue, 0, 255) << 16;
    }
    
    public static int getFromBGRAInt(final int bgra) {
        return getFromInt(bgra >> 8 & 0xFF, bgra >> 16 & 0xFF, bgra >> 24 & 0xFF, bgra & 0xFF);
    }
    
    public static int getFromARGBInt(final int argb) {
        return getFromInt(argb >> 16 & 0xFF, argb >> 8 & 0xFF, argb & 0xFF, argb >> 24 & 0xFF);
    }
    
    private static int _getFromInt(final int i1, final int i2, final int i3, final int i4) {
        return MathHelper.clamp(i1, 0, 255) << 24 | MathHelper.clamp(i2, 0, 255) << 16 | MathHelper.clamp(i3, 0, 255) << 8 | MathHelper.clamp(i4, 0, 255);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getRedFromARGB(this.m_ABGR)).append(", ").append(getGreenFromARGB(this.m_ABGR)).append(", ").append(getBlueFromARGB(this.m_ABGR)).append(", ").append(getAlphaFromARGB(this.m_ABGR));
        return sb.toString();
    }
    
    public String getRGBtoHex() {
        final StringBuilder sb = new StringBuilder();
        final String red = ((this.getRedInt() < 16) ? "0" : "") + Integer.toHexString(this.getRedInt());
        final String green = ((this.getGreenInt() < 16) ? "0" : "") + Integer.toHexString(this.getGreenInt());
        final String blue = ((this.getBlueInt() < 16) ? "0" : "") + Integer.toHexString(this.getBlueInt());
        sb.append(red).append(green).append(blue);
        return sb.toString();
    }
    
    public String getRGBAtoHex() {
        final StringBuilder sb = new StringBuilder();
        final String red = ((this.getRedInt() < 16) ? "0" : "") + Integer.toHexString(this.getRedInt());
        final String green = ((this.getGreenInt() < 16) ? "0" : "") + Integer.toHexString(this.getGreenInt());
        final String blue = ((this.getBlueInt() < 16) ? "0" : "") + Integer.toHexString(this.getBlueInt());
        final String alpha = ((this.getAlphaInt() < 16) ? "0" : "") + Integer.toHexString(this.getAlphaInt());
        sb.append(red).append(green).append(blue).append(alpha);
        return sb.toString();
    }
    
    public static Color getRGBAFromHex(String hex) {
        if (hex.charAt(0) == '#') {
            hex = hex.substring(1);
        }
        final int r = Integer.parseInt(hex.substring(0, 2), 16);
        final int g = Integer.parseInt(hex.substring(2, 4), 16);
        final int b = Integer.parseInt(hex.substring(4, 6), 16);
        int a;
        if (hex.length() == 8) {
            a = Integer.parseInt(hex.substring(6, 8), 16);
        }
        else {
            a = 255;
        }
        return new Color(r, g, b, a);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Color color = (Color)o;
        return this.m_ABGR == color.m_ABGR;
    }
    
    @Override
    public int hashCode() {
        return this.m_ABGR;
    }
    
    static {
        ALPHA = new Color(0.0f, 0.0f, 0.0f, 0.0f);
        WHITE_ALPHA = new Color(1.0f, 1.0f, 1.0f, 0.0f);
        WHITE_SEMI_ALPHA = new Color(1.0f, 1.0f, 1.0f, 0.5f);
        WHITE_QUARTER_ALPHA = new Color(1.0f, 1.0f, 1.0f, 0.25f);
        WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        BLACK = new Color(0.0f, 0.0f, 0.0f, 1.0f);
        RED = new Color(1.0f, 0.0f, 0.0f, 1.0f);
        GREEN = new Color(0.0f, 1.0f, 0.0f, 1.0f);
        DARK_GREEN = new Color(0.0f, 0.4f, 0.0f, 1.0f);
        BLUE = new Color(0.0f, 0.0f, 1.0f, 1.0f);
        CYAN = new Color(0.0f, 1.0f, 1.0f, 1.0f);
        GRAY = new Color(128, 128, 128, 255);
        DARK_GRAY = new Color(64, 64, 64, 255);
        LIGHT_GRAY = new Color(192, 192, 192, 255);
        VERY_LIGHT_GRAY = new Color(224, 224, 224, 255);
        PURPLE = new Color(0.57f, 0.2f, 0.75f, 0.66f);
        GOLD = new Color(0.95f, 0.64f, 0.25f, 1.0f);
        ORANGE = new Color(237, 172, 97, 255);
    }
    
    public static class GrayColorKeying extends AbstractColorComparator
    {
        private static final Color TEMP_COLOR;
        
        public GrayColorKeying(final boolean is32) {
            super((byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, is32);
        }
        
        @Override
        public boolean matches(final byte r, final byte g, final byte b, final byte a, final byte pixelFormat) {
            return r == g && g == b;
        }
        
        private static void setTempColor(final byte r, final byte g, final byte b, final byte a, final int pixelFormat) {
            if (pixelFormat == 17) {
                GrayColorKeying.TEMP_COLOR.setFromByte(r, g, b, a);
            }
            else {
                GrayColorKeying.TEMP_COLOR.setFromByte(b, g, r, a);
            }
        }
        
        @Override
        public void replace(final byte[] data, final int offset, final byte pixelFormat) {
            setTempColor(data[offset], data[offset], data[offset], (byte)(-1), pixelFormat);
            float intensity = GrayColorKeying.TEMP_COLOR.getIntensity();
            setTempColor(this.m_r2, this.m_g2, this.m_b2, this.m_a2, pixelFormat);
            intensity *= GrayColorKeying.TEMP_COLOR.getMaxRGBComponent();
            GrayColorKeying.TEMP_COLOR.setIntensity(MathHelper.clamp(intensity, 0.0f, 1.0f));
            data[offset] = GrayColorKeying.TEMP_COLOR.getRedByte();
            data[offset + 1] = GrayColorKeying.TEMP_COLOR.getGreenByte();
            data[offset + 2] = GrayColorKeying.TEMP_COLOR.getBlueByte();
            if (this.m_is32) {
                final int n = offset + 3;
                data[n] *= (byte)GrayColorKeying.TEMP_COLOR.getAlpha();
            }
        }
        
        static {
            TEMP_COLOR = new Color();
        }
    }
    
    public abstract static class AbstractColorComparator implements ColorComparator
    {
        protected byte m_r1;
        protected byte m_g1;
        protected byte m_b1;
        protected byte m_a1;
        protected byte m_r2;
        protected byte m_g2;
        protected byte m_b2;
        protected byte m_a2;
        protected boolean m_is32;
        
        protected AbstractColorComparator(final byte r1, final byte g1, final byte b1, final byte a1, final byte r2, final byte g2, final byte b2, final byte a2, final boolean is32) {
            super();
            this.m_is32 = false;
            this.m_r1 = r1;
            this.m_g1 = g1;
            this.m_b1 = b1;
            this.m_a1 = a1;
            this.m_r2 = r2;
            this.m_g2 = g2;
            this.m_b2 = b2;
            this.m_a2 = a2;
            this.m_is32 = is32;
        }
        
        @Override
        public void setSrcColor(final byte r, final byte g, final byte b, final byte a) {
            this.m_r1 = r;
            this.m_g1 = g;
            this.m_b1 = b;
            this.m_a1 = a;
        }
        
        @Override
        public void setDestColor(final byte r, final byte g, final byte b, final byte a) {
            this.m_r2 = r;
            this.m_g2 = g;
            this.m_b2 = b;
            this.m_a2 = a;
        }
        
        @Override
        public void setBitDepth(final int bitDepth) {
            if (bitDepth == 32) {
                this.m_is32 = true;
            }
        }
    }
    
    public interface ColorComparator
    {
        void setSrcColor(byte p0, byte p1, byte p2, byte p3);
        
        void setDestColor(byte p0, byte p1, byte p2, byte p3);
        
        void setBitDepth(int p0);
        
        boolean matches(byte p0, byte p1, byte p2, byte p3, byte p4);
        
        void replace(byte[] p0, int p1, byte p2);
    }
}
