package com.ankamagames.framework.kernel.core.maths;

import org.apache.log4j.*;

public final class Perlin2D
{
    private static final Logger m_logger;
    
    private static float noise(final int x, final int y) {
        int n = x + y * 57;
        n ^= n << 13;
        return 1.0f - (n * (n * n * 15731 + 789221) + 1376312589 & Integer.MAX_VALUE) / 1.07374182E9f;
    }
    
    private static float smoothNoise(final int x, final int y) {
        final float c = (noise(x - 1, y - 1) + noise(x + 1, y - 1) + noise(x - 1, y + 1) + noise(x + 1, y + 1)) / 16.0f;
        final float s = (noise(x - 1, y) + noise(x + 1, y) + noise(x, y + 1) + noise(x, y - 1)) / 8.0f;
        final float noise = noise(x, y) / 4.0f + c + s;
        return noise;
    }
    
    private static float interpolatedNoise(final float x, final float y, final Interpoler interpolation) {
        final int ix = (x < 0.0f) ? ((int)(x - 1.0f)) : ((int)x);
        final int iy = (y < 0.0f) ? ((int)(y - 1.0f)) : ((int)y);
        final float fx = x - ix;
        final float fy = y - iy;
        final float a = smoothNoise(ix, iy);
        final float b = smoothNoise(ix + 1, iy);
        final float c = smoothNoise(ix, iy + 1);
        final float d = smoothNoise(ix + 1, iy + 1);
        final float i0 = interpolation.compute(a, b, fx);
        final float i = interpolation.compute(c, d, fx);
        return interpolation.compute(i0, i, fy);
    }
    
    public static float perlinNoise(final float x, final float y, final float persistance, final int numOctaves, final Interpoler interpolation) {
        float t = 0.0f;
        float f = 1.0f;
        float a = 1.0f;
        for (int i = 0; i < numOctaves - 1; ++i) {
            t += interpolatedNoise(x * f, y * f, interpolation) * a;
            f *= 2.0f;
            a *= persistance;
        }
        return t;
    }
    
    public static void generate(final float[] dest, final int width, final int height, final float scaleX, final float scaleY, final int offX, final int offY, final float persistance, final int numOctaves, final Function effectFunction) {
        final Function function = (effectFunction != null) ? effectFunction : Effect.NONE;
        for (int y = 0; y < height; ++y) {
            final float v = (y + offY) * scaleY / height;
            for (int x = 0; x < width; ++x) {
                final float u = (x + offX) * scaleX / width;
                final float p = perlinNoise(u, v, persistance, numOctaves, function.interpoler());
                dest[x + y * width] = function.compute(u, v, p);
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)Perlin2D.class);
    }
    
    public enum Interpolation implements Interpoler
    {
        LINEAR {
            @Override
            public float compute(final float u, final float v, final float t) {
                return u * (1.0f - t) + v * t;
            }
        }, 
        COSINE {
            @Override
            public float compute(final float u, final float v, final float t) {
                final float ft = t * 3.1415927f;
                final float f = (1.0f - (float)Math.cos(ft)) * 0.5f;
                return u * (1.0f - f) + v * f;
            }
        };
    }
    
    public enum Effect implements Function
    {
        NONE {
            @Override
            public float compute(final float x, final float v, final float perlinXY) {
                return perlinXY;
            }
            
            @Override
            public Interpoler interpoler() {
                return Interpolation.LINEAR;
            }
        }, 
        WOOD {
            @Override
            public float compute(final float x, final float v, final float perlinXY) {
                final float k = perlinXY * 20.0f;
                return k - (int)k;
            }
            
            @Override
            public Interpoler interpoler() {
                return Interpolation.COSINE;
            }
        }, 
        MARBLE {
            @Override
            public float compute(final float x, final float v, final float perlinXY) {
                return (float)Math.cos(x + perlinXY);
            }
            
            @Override
            public Interpoler interpoler() {
                return Interpolation.COSINE;
            }
        };
    }
    
    public interface Function
    {
        float compute(float p0, float p1, float p2);
        
        Interpoler interpoler();
    }
    
    public interface Interpoler
    {
        float compute(float p0, float p1, float p2);
    }
}
