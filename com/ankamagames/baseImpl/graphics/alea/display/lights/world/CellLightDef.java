package com.ankamagames.baseImpl.graphics.alea.display.lights.world;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class CellLightDef
{
    private static final float COLOR_FACTOR = 2.0f;
    public static final int DEFAULT_COLOR;
    private final float m_ambianceLightR;
    private final float m_ambianceLightG;
    private final float m_ambianceLightB;
    private final float m_shadowsR;
    private final float m_shadowsG;
    private final float m_shadowsB;
    private final float m_lightsR;
    private final float m_lightsG;
    private final float m_lightsB;
    public final boolean m_allowOutdoorLighting;
    public final boolean m_hasShadows;
    private final float[] m_merged;
    private final float[] m_nightLight;
    
    public CellLightDef(final int ambianceLight, final int shadows, final int lights, final boolean allowOutdoorLighting) {
        super();
        this.m_merged = new float[] { 0.0f, 0.0f, 0.0f };
        this.m_ambianceLightR = Color.getRedFromARGB(ambianceLight) * 2.0f;
        this.m_ambianceLightG = Color.getGreenFromARGB(ambianceLight) * 2.0f;
        this.m_ambianceLightB = Color.getBlueFromARGB(ambianceLight) * 2.0f;
        this.m_hasShadows = (shadows != CellLightDef.DEFAULT_COLOR);
        this.m_shadowsR = Color.getRedFromARGB(shadows);
        this.m_shadowsG = Color.getGreenFromARGB(shadows);
        this.m_shadowsB = Color.getBlueFromARGB(shadows);
        float[] nightLight;
        if (lights == CellLightDef.DEFAULT_COLOR) {
            nightLight = null;
        }
        else {
            final float[] array = nightLight = new float[3];
            array[0] = 0.0f;
            array[2] = (array[1] = 0.0f);
        }
        this.m_nightLight = nightLight;
        this.m_lightsR = Color.getRedFromARGB(lights) - 0.5f;
        this.m_lightsG = Color.getGreenFromARGB(lights) - 0.5f;
        this.m_lightsB = Color.getBlueFromARGB(lights) - 0.5f;
        this.m_allowOutdoorLighting = allowOutdoorLighting;
    }
    
    public CellLightDef(final CellLightDef def) {
        super();
        this.m_merged = new float[] { 0.0f, 0.0f, 0.0f };
        this.m_ambianceLightR = def.m_ambianceLightR;
        this.m_ambianceLightG = def.m_ambianceLightG;
        this.m_ambianceLightB = def.m_ambianceLightB;
        this.m_shadowsR = def.m_shadowsR;
        this.m_shadowsG = def.m_shadowsG;
        this.m_shadowsB = def.m_shadowsB;
        this.m_lightsR = def.m_lightsR;
        this.m_lightsG = def.m_lightsG;
        this.m_lightsB = def.m_lightsB;
        this.m_allowOutdoorLighting = def.m_allowOutdoorLighting;
        this.m_hasShadows = def.m_hasShadows;
        float[] nightLight;
        if (def.m_nightLight == null) {
            nightLight = null;
        }
        else {
            final float[] array = nightLight = new float[3];
            array[0] = 0.0f;
            array[2] = (array[1] = 0.0f);
        }
        this.m_nightLight = nightLight;
        System.arraycopy(def.m_merged, 0, this.m_merged, 0, this.m_merged.length);
    }
    
    final void resestCache() {
        this.m_merged[0] = this.m_ambianceLightR;
        this.m_merged[1] = this.m_ambianceLightG;
        this.m_merged[2] = this.m_ambianceLightB;
        if (this.m_nightLight != null) {
            final float[] nightLight = this.m_nightLight;
            final int n = 0;
            final float[] nightLight2 = this.m_nightLight;
            final int n2 = 1;
            final float[] nightLight3 = this.m_nightLight;
            final int n3 = 2;
            final float n4 = 0.0f;
            nightLight3[n3] = n4;
            nightLight[n] = (nightLight2[n2] = n4);
        }
    }
    
    final void updateShadow(final float shadowIntensity) {
        if (shadowIntensity <= 0.0f || !this.m_hasShadows) {
            this.m_merged[0] = this.m_ambianceLightR;
            this.m_merged[1] = this.m_ambianceLightG;
            this.m_merged[2] = this.m_ambianceLightB;
            return;
        }
        float r = this.m_ambianceLightR;
        float g = this.m_ambianceLightG;
        float b = this.m_ambianceLightB;
        final float k = shadowIntensity * 2.0f;
        final float l = -shadowIntensity + 1.0f;
        r *= this.m_shadowsR * k + l;
        g *= this.m_shadowsG * k + l;
        b *= this.m_shadowsB * k + l;
        r = MathHelper.clamp(r, 0.0f, 2.0f);
        g = MathHelper.clamp(g, 0.0f, 2.0f);
        b = MathHelper.clamp(b, 0.0f, 2.0f);
        this.m_merged[0] = r;
        this.m_merged[1] = g;
        this.m_merged[2] = b;
    }
    
    final void updateNightLight(final float nightLightIntensity) {
        if (this.m_nightLight == null) {
            return;
        }
        if (nightLightIntensity > 0.0f) {
            final float k = nightLightIntensity * 2.0f;
            this.m_nightLight[0] = this.m_lightsR * k;
            this.m_nightLight[1] = this.m_lightsG * k;
            this.m_nightLight[2] = this.m_lightsB * k;
        }
        else {
            final float[] nightLight = this.m_nightLight;
            final int n = 0;
            final float[] nightLight2 = this.m_nightLight;
            final int n2 = 1;
            final float[] nightLight3 = this.m_nightLight;
            final int n3 = 2;
            final float n4 = 0.0f;
            nightLight3[n3] = n4;
            nightLight[n] = (nightLight2[n2] = n4);
        }
    }
    
    public final float[] getColor() {
        return this.m_merged;
    }
    
    public final float[] getNightLight() {
        return this.m_nightLight;
    }
    
    public final boolean hasNightLight() {
        return this.m_nightLight != null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final CellLightDef that = (CellLightDef)o;
        return this.m_allowOutdoorLighting == that.m_allowOutdoorLighting && MathHelper.isEqual(that.m_ambianceLightB, this.m_ambianceLightB) && MathHelper.isEqual(that.m_ambianceLightG, this.m_ambianceLightG) && MathHelper.isEqual(that.m_ambianceLightR, this.m_ambianceLightR) && this.m_hasShadows == that.m_hasShadows && MathHelper.isEqual(that.m_lightsB, this.m_lightsB) && MathHelper.isEqual(that.m_lightsG, this.m_lightsG) && MathHelper.isEqual(that.m_lightsR, this.m_lightsR) && MathHelper.isEqual(that.m_shadowsB, this.m_shadowsB) && MathHelper.isEqual(that.m_shadowsG, this.m_shadowsG) && MathHelper.isEqual(that.m_shadowsR, this.m_shadowsR);
    }
    
    static {
        DEFAULT_COLOR = Color.GRAY.get();
    }
}
