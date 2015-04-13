package com.ankamagames.xulor2.tween;

public abstract class TweenFunction
{
    public static final TweenFunction LINEAR;
    public static final TweenFunction PROGRESSIVE;
    public static final TweenFunction FULL_TO_NULL;
    public static final TweenFunction NULL_TO_FULL;
    
    public abstract float compute(final float p0, final float p1, final int p2, final int p3);
    
    static {
        LINEAR = new LinearFunction();
        PROGRESSIVE = new ProgressiveFunction();
        FULL_TO_NULL = new FullToNullFunction();
        NULL_TO_FULL = new NullToFullFunction();
    }
    
    public static class LinearFunction extends TweenFunction
    {
        @Override
        public float compute(final float a, final float b, final int current, final int duration) {
            final float percent = current / duration;
            return a + (b - a) * percent;
        }
    }
    
    public static class ProgressiveFunction extends TweenFunction
    {
        @Override
        public float compute(final float a, final float b, final int current, final int duration) {
            float percent = current / duration;
            final float delta = (0.5f - percent) * (1.0f - 2.0f * Math.abs(0.5f - percent));
            percent -= delta;
            return a + (b - a) * percent;
        }
    }
    
    public static class FullToNullFunction extends TweenFunction
    {
        @Override
        public float compute(final float a, final float b, final int current, final int duration) {
            float percent = current / duration / 2.0f + 0.5f;
            final float delta = (0.5f - percent) * (1.0f - 2.0f * Math.abs(0.5f - percent));
            percent = (percent - delta - 0.5f) * 2.0f;
            return a + (b - a) * percent;
        }
    }
    
    public static class NullToFullFunction extends TweenFunction
    {
        @Override
        public float compute(final float a, final float b, final int current, final int duration) {
            float percent = current / duration / 2.0f;
            final float delta = (0.5f - percent) * (1.0f - 2.0f * Math.abs(0.5f - percent));
            percent = (percent - delta) * 2.0f;
            return a + (b - a) * percent;
        }
    }
}
