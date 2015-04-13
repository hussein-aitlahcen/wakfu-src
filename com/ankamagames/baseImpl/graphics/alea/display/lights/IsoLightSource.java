package com.ankamagames.baseImpl.graphics.alea.display.lights;

import com.ankamagames.framework.graphics.engine.light.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.lightMap.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class IsoLightSource extends LightSource
{
    private static final Interpolator NO_INTERPOLATION;
    private static final Interpolator X_LINEAR_INTERPOLATION;
    private static final Interpolator Y_LINEAR_INTERPOLATION;
    private static final Interpolator BILINEAR_INTERPOLATION;
    protected float[] m_intensityMap;
    protected int m_intensityMapSize;
    protected IsoWorldTarget m_target;
    
    protected IsoLightSource() {
        super();
        this.resizeIntesityMap(this.m_range);
    }
    
    protected IsoLightSource(final Vector3 position) {
        super(position);
        this.resizeIntesityMap(this.m_range);
    }
    
    protected IsoLightSource(final Vector3 position, final float range) {
        super(position, range);
        this.resizeIntesityMap(this.m_range);
    }
    
    protected IsoLightSource(final IsoWorldTarget target, final float range) {
        super();
        this.m_target = target;
        this.resizeIntesityMap(this.m_range = range);
    }
    
    protected IsoLightSource(final IsoWorldTarget target) {
        this(target, 3.0f);
        this.m_enabled = true;
        this.resizeIntesityMap(this.m_range);
    }
    
    public IsoWorldTarget getTarget() {
        return this.m_target;
    }
    
    public void setTarget(final IsoWorldTarget target) {
        if (this.m_target instanceof LightTarget) {
            ((LightTarget)this.m_target).onLightDetached(this);
        }
        this.m_target = target;
        if (this.m_target instanceof LightTarget) {
            ((LightTarget)this.m_target).onLightAttached(this);
        }
    }
    
    private void resizeIntesityMap(final float range) {
        final int mapSize = MathHelper.fastFloor(range + 1.0f);
        if (mapSize == this.m_intensityMapSize) {
            return;
        }
        this.m_intensityMapSize = mapSize;
        this.m_intensityMap = new float[this.m_intensityMapSize * this.m_intensityMapSize * 4];
        final float attenuation = 1.0f / (this.m_attenuation[0] + this.m_attenuation[1] * this.m_range + this.m_attenuation[2] * this.m_range * this.m_range);
        final Vector2 ray = new Vector2(0.0f, 0.0f);
        for (int y = 0; y < this.m_intensityMapSize * 2; ++y) {
            for (int x = 0; x < this.m_intensityMapSize * 2; ++x) {
                final int cx = x - this.m_intensityMapSize;
                final int cy = y - this.m_intensityMapSize;
                ray.set(cx, cy);
                final float l = ray.length();
                if (l == 0.0f) {
                    this.m_intensityMap[x + y * this.m_intensityMapSize * 2] = 1.0f;
                }
                else if (l > this.m_range) {
                    this.m_intensityMap[x + y * this.m_intensityMapSize * 2] = 0.0f;
                }
                else {
                    this.m_intensityMap[x + y * this.m_intensityMapSize * 2] = Math.max(0.0f, 1.0f - l * attenuation);
                }
            }
        }
    }
    
    @Override
    public final boolean isEnabled() {
        return (this.m_target == null || this.m_target.isVisible()) && super.isEnabled();
    }
    
    @Override
    public void setRange(final float range) {
        if (range == this.m_range) {
            return;
        }
        super.setRange(range);
        this.resizeIntesityMap(this.m_range);
    }
    
    private static Interpolator getInterpolatorFromDirection(final int dx, final int dy) {
        if (dx == 0) {
            if (dy == 0) {
                return IsoLightSource.NO_INTERPOLATION;
            }
            return IsoLightSource.Y_LINEAR_INTERPOLATION;
        }
        else {
            if (dy == 0) {
                return IsoLightSource.X_LINEAR_INTERPOLATION;
            }
            return IsoLightSource.BILINEAR_INTERPOLATION;
        }
    }
    
    public void apply(final LightMap map, float factor) {
        final Rect bounds = map.getBounds();
        final Vector3 position = this.getPosition();
        final float lx = position.getX();
        final float ly = position.getY();
        final float lz = position.getZ();
        final int fx = MathHelper.fastFloor(lx);
        final int fy = MathHelper.fastFloor(ly);
        final int fz = MathHelper.fastFloor(lz);
        final int r = (int)Math.ceil(this.m_range);
        if (!bounds.containsOrIntersect(fx - r, fx + r, fy - r, fy + r)) {
            return;
        }
        final float ax = fx - lx;
        final float ay = fy - ly;
        final int dx = (int)Math.signum(ax);
        final int dy = (int)Math.signum(ay);
        final Interpolator interpolator = getInterpolatorFromDirection(dx, dy);
        interpolator.set(ax, ay, dx, dy);
        factor *= this.m_intensity;
        for (int intensityMapSize = this.m_intensityMapSize * 2, y = 0; y < intensityMapSize; ++y) {
            final int pfy = fy + y - this.m_intensityMapSize;
            for (int x = 0; x < intensityMapSize; ++x) {
                final int pfx = fx + x - this.m_intensityMapSize;
                final float i0 = this.m_intensityMap[x + y * intensityMapSize];
                float intensity0 = interpolator.compute(x, y, i0, this.m_intensityMap, intensityMapSize);
                intensity0 *= factor;
                if (intensity0 >= 0.004f) {
                    for (int z = 0; z < intensityMapSize; z += intensityMapSize) {
                        final int pfz = fz + z - this.m_intensityMapSize;
                        map.addColors(pfx, pfy, pfz, intensity0 * this.m_baseColor.getRed(), intensity0 * this.m_baseColor.getGreen(), intensity0 * this.m_baseColor.getBlue(), intensity0 * this.m_saturation.getRed(), intensity0 * this.m_saturation.getGreen(), intensity0 * this.m_saturation.getBlue());
                    }
                }
            }
        }
    }
    
    @Override
    public Vector3 getPosition() {
        if (this.m_target != null) {
            return new Vector3(this.m_target.getWorldX(), this.m_target.getWorldY(), this.m_target.getAltitude());
        }
        return super.getPosition();
    }
    
    @Override
    public void setPosition(final Vector3 position) {
        if (this.m_target != null) {
            throw new RuntimeException("La source est attach\u00e9e \u00e0 une cible, on ne changera pas la position de la cible \u00e0 travers la source.");
        }
        super.setPosition(position);
    }
    
    static {
        NO_INTERPOLATION = new Interpolator() {
            @Override
            void set(final float ax, final float ay, final int dx, final int dy) {
            }
            
            @Override
            float compute(final int x, final int y, final float i0, final float[] intensityMap, final int mapSize) {
                return i0;
            }
        };
        X_LINEAR_INTERPOLATION = new Interpolator() {
            private int m_dx;
            private float factor0;
            private float factor1;
            
            @Override
            void set(final float ax, final float ay, final int dx, final int dy) {
                this.m_dx = dx;
                this.factor1 = ax * ax / (dx * dx);
                this.factor0 = 1.0f - this.factor1;
            }
            
            @Override
            float compute(final int x, final int y, final float i0, final float[] intensityMap, final int mapSize) {
                final int cx = x + this.m_dx;
                float i = 0.0f;
                if (cx >= 0 && cx < mapSize) {
                    i = intensityMap[cx + y * mapSize];
                }
                return i0 * this.factor0 + i * this.factor1;
            }
        };
        Y_LINEAR_INTERPOLATION = new Interpolator() {
            private int m_dy;
            private float factor1;
            private float factor0;
            
            @Override
            void set(final float ax, final float ay, final int dx, final int dy) {
                this.m_dy = dy;
                this.factor1 = ay * ay / (dy * dy);
                this.factor0 = 1.0f - this.factor1;
            }
            
            @Override
            float compute(final int x, final int y, final float i0, final float[] intensityMap, final int mapSize) {
                final int cy = y + this.m_dy;
                float i = 0.0f;
                if (cy >= 0 && cy < mapSize) {
                    i = intensityMap[x + cy * mapSize];
                }
                return i0 * this.factor0 + i * this.factor1;
            }
        };
        BILINEAR_INTERPOLATION = new Interpolator() {
            private int m_dx;
            private int m_dy;
            private float factor00;
            private float factor01;
            private float factor10;
            private float factor11;
            
            @Override
            void set(final float ax, final float ay, final int dx, final int dy) {
                this.m_dx = dx;
                this.m_dy = dy;
                final float x = ax * ax / (dx * dx);
                final float y = ay * ay / (dy * dy);
                this.factor00 = (1.0f - x) * (1.0f - y);
                this.factor01 = (1.0f - x) * y;
                this.factor10 = x * (1.0f - y);
                this.factor11 = x * y;
            }
            
            @Override
            float compute(final int x, final int y, final float i0, final float[] intensityMap, final int mapSize) {
                final int cx = x + this.m_dx;
                final int cy = y + this.m_dy;
                float i = 0.0f;
                float i2 = 0.0f;
                float i3 = 0.0f;
                if (cy >= 0 && cy < mapSize) {
                    i = intensityMap[x + cy * mapSize];
                    if (cx >= 0 && cx < mapSize) {
                        i3 = intensityMap[cx + cy * mapSize];
                    }
                }
                if (cx >= 0 && cx < mapSize) {
                    i2 = intensityMap[cx + y * mapSize];
                }
                return i0 * this.factor00 + i * this.factor01 + i2 * this.factor10 + i3 * this.factor11;
            }
        };
    }
    
    abstract static class Interpolator
    {
        abstract void set(final float p0, final float p1, final int p2, final int p3);
        
        abstract float compute(final int p0, final int p1, final float p2, final float[] p3, final int p4);
    }
}
