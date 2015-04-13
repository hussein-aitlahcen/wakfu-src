package com.ankamagames.baseImpl.graphics.alea.display.effects.cloud;

import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class CloudModifier extends LitSceneModifierMap
{
    float m_sharpness;
    private static final int CLOUD_MAP_SIZE = 64;
    private final float[] m_c0;
    private IsoCamera m_camera;
    private float m_dx;
    private float m_dy;
    private float m_offsetX;
    private float m_offsetY;
    private int m_cloudX;
    private int m_cloudY;
    private int m_previousCameraPosX;
    private int m_previousCameraPosY;
    private final Vector3 m_cloudsDirection;
    private boolean m_paramChanged;
    float m_noiseScale;
    float m_noiseBias;
    private int m_previousOffX;
    private int m_previousOffY;
    private float factor00;
    private float factor01;
    private float factor10;
    private float factor11;
    private final Perlin2D.Function m_cloudCorrection;
    private final BitmapCloudGenerator m_bitmapGenerator;
    private final CloudGenerator m_cloudGenerator;
    
    public CloudModifier() {
        super();
        this.m_sharpness = 0.1f;
        this.m_c0 = new float[4096];
        this.m_cloudsDirection = new Vector3(-1.0f, 0.5f, 0.0f);
        this.m_paramChanged = false;
        this.m_noiseScale = 0.5f;
        this.m_noiseBias = 0.5f;
        this.m_previousOffX = Integer.MIN_VALUE;
        this.m_previousOffY = Integer.MIN_VALUE;
        this.m_cloudCorrection = new Perlin2D.Function() {
            @Override
            public final float compute(final float x, final float v, final float value) {
                final float d = MathHelper.clamp(value * CloudModifier.this.m_noiseScale + CloudModifier.this.m_noiseBias, 0.0f, 1.0f);
                return d * CloudModifier.this.m_sharpness + (1.0f - CloudModifier.this.m_sharpness);
            }
            
            @Override
            public final Perlin2D.Interpoler interpoler() {
                return Perlin2D.Interpolation.LINEAR;
            }
        };
        this.m_bitmapGenerator = new BitmapCloudGenerator("cloud.tga", this.m_cloudCorrection);
        this.m_cloudGenerator = this.m_bitmapGenerator;
        this.m_offsetX = 0.0f;
        this.m_offsetY = 0.0f;
        this.m_previousCameraPosX = Integer.MAX_VALUE;
        this.m_previousCameraPosY = Integer.MAX_VALUE;
        Arrays.fill(this.m_c0, 1.0f);
    }
    
    public void setTexture(final String texture) {
        if (this.m_cloudGenerator == this.m_bitmapGenerator) {
            this.m_bitmapGenerator.loadCloudTexture(texture);
        }
    }
    
    public void setCamera(final IsoCamera camera) {
        this.m_camera = camera;
    }
    
    public void setDirection(final float x, final float y) {
        this.m_cloudsDirection.set(x, y, 0.0f);
    }
    
    public void setNoiseBias(final float noiseBias) {
        if (!MathHelper.isEqual(noiseBias, this.m_noiseBias)) {
            this.m_noiseBias = noiseBias;
            this.m_paramChanged = true;
        }
    }
    
    public void setNoiseScale(final float noiseScale) {
        if (!MathHelper.isEqual(noiseScale, this.m_noiseScale)) {
            this.m_noiseScale = noiseScale;
            this.m_paramChanged = true;
        }
    }
    
    public void setSharpness(float sharpness) {
        sharpness = MathHelper.clamp(sharpness, 0.0f, 0.7f);
        if (!MathHelper.isEqual(sharpness, this.m_sharpness)) {
            this.m_sharpness = sharpness;
            this.m_paramChanged = true;
        }
    }
    
    @Override
    public void precompute() {
        final IsoCamera camera = this.m_camera;
        if (camera == null) {
            return;
        }
        this.m_previousCameraPosX = MathHelper.fastFloor(camera.getCameraExactIsoWorldX()) - 32;
        this.m_previousCameraPosY = MathHelper.fastFloor(camera.getCameraExactIsoWorldY()) - 32;
        this.updateOffset();
        final int offsetX = this.m_previousCameraPosX + this.m_cloudX;
        final int offsetY = this.m_previousCameraPosY + this.m_cloudY;
        if (this.m_previousOffX != offsetX || this.m_previousOffY != offsetY || this.m_paramChanged) {
            this.m_cloudGenerator.generate(this.m_c0, 64, 64, offsetX, offsetY);
        }
        this.m_paramChanged = false;
        this.m_previousOffX = offsetX;
        this.m_previousOffY = offsetY;
    }
    
    @Override
    public void update(final int deltaTime) {
        final float dd = deltaTime / 1000.0f;
        this.m_dx = this.m_cloudsDirection.getX() * dd;
        this.m_dy = this.m_cloudsDirection.getY() * dd;
    }
    
    private void updateOffset() {
        this.m_offsetX -= this.m_dx;
        this.m_offsetY -= this.m_dy;
        final int x = MathHelper.fastFloor(this.m_offsetX);
        this.m_cloudX += x;
        this.m_offsetX -= x;
        final int y = MathHelper.fastFloor(this.m_offsetY);
        this.m_cloudY += y;
        this.m_offsetY -= y;
        final float offsetX = this.m_offsetX;
        final float offsetY = this.m_offsetY;
        this.factor00 = (1.0f - offsetX) * (1.0f - offsetY);
        this.factor01 = (1.0f - offsetX) * offsetY;
        this.factor10 = offsetX * (1.0f - offsetY);
        this.factor11 = offsetX * offsetY;
    }
    
    @Override
    public int getPriority() {
        return 100;
    }
    
    @Override
    public boolean useless() {
        return this.m_noiseScale == 0.0f && this.m_noiseBias == 0.0f;
    }
    
    @Override
    public void apply(final int x, final int y, final int z, final int layerId, final float[] colors) {
        try {
            final float[] mask = this.m_c0;
            int mx = x - this.m_previousCameraPosX;
            mx %= 64;
            if (mx < 0) {
                mx += 64;
            }
            final int mx2 = (mx + 1) % 64;
            int my = y - this.m_previousCameraPosY;
            my %= 64;
            if (my < 0) {
                my += 64;
            }
            final int my2 = (my + 1) % 64;
            final int i00 = mx + my * 64;
            final int i = mx + my2 * 64;
            final int i2 = mx2 + my * 64;
            final int i3 = mx2 + my2 * 64;
            final float j = this.factor00 * mask[i00] + this.factor01 * mask[i] + this.factor10 * mask[i2] + this.factor11 * mask[i3];
            final int n = 0;
            colors[n] *= j;
            final int n2 = 1;
            colors[n2] *= j;
            final int n3 = 2;
            colors[n3] *= j;
        }
        catch (Exception ex) {}
    }
}
