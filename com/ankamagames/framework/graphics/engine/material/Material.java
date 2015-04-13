package com.ankamagames.framework.graphics.engine.material;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class Material extends MemoryObject
{
    private static final int DIFFUSE_POS = 0;
    private static final int DIFFUSE_SIZE = 4;
    private static final int SPECULAR_POS = 4;
    private static final int SPECULAR_SIZE = 3;
    private static final int COLOR_SIZE = 7;
    public static final Material WHITE;
    public static final Material WHITE_NO_SPECULAR;
    public static final Material BLACK;
    private static final float[] DEFAULT;
    private static final FreeList m_indexList;
    private static float[] m_colors;
    public static final ObjectFactory Factory;
    private final Object m_freelistMutex;
    private int m_index;
    private final float[] diff;
    private final float[] spec;
    
    private Material() {
        super();
        this.m_freelistMutex = new Object();
        this.diff = new float[4];
        this.spec = new float[3];
    }
    
    @Deprecated
    public float[] getDiffuseColor() {
        this.getDiffuseColor(this.diff);
        return this.diff;
    }
    
    @Deprecated
    public float[] getSpecularColor() {
        this.getSpecularColor(this.spec);
        return this.spec;
    }
    
    public void copy(final Material material) {
        System.arraycopy(Material.m_colors, material.m_index, Material.m_colors, this.m_index, 7);
    }
    
    public void getDiffuseColor(final float[] value) {
        System.arraycopy(Material.m_colors, 0 + this.m_index, value, 0, 4);
    }
    
    public boolean diffuseEquals(final float[] diffuse) {
        final int i = 0 + this.m_index;
        return Material.m_colors[i] == diffuse[0] && Material.m_colors[i + 1] == diffuse[1] && Material.m_colors[i + 2] == diffuse[2] && Material.m_colors[i + 3] == diffuse[3];
    }
    
    public void copyDiffuseTo(final float[] diffuse) {
        System.arraycopy(Material.m_colors, 0 + this.m_index, diffuse, 0, 4);
    }
    
    public void setDiffuseColor(final float[] diffuseColor) {
        System.arraycopy(diffuseColor, 0, Material.m_colors, 0 + this.m_index, 4);
    }
    
    public void setDiffuseColorNoAlpha(final float[] diffuseColor) {
        System.arraycopy(diffuseColor, 0, Material.m_colors, 0 + this.m_index, 3);
    }
    
    public void setDiffuseColor(final float r, final float g, final float b, final float a) {
        final int i = 0 + this.m_index;
        Material.m_colors[i] = r;
        Material.m_colors[i + 1] = g;
        Material.m_colors[i + 2] = b;
        Material.m_colors[i + 3] = a;
    }
    
    public void multDiffuse(final float r, final float g, final float b) {
        final int i = 0 + this.m_index;
        final float[] colors = Material.m_colors;
        final int n = i;
        colors[n] *= r;
        final float[] colors2 = Material.m_colors;
        final int n2 = i + 1;
        colors2[n2] *= g;
        final float[] colors3 = Material.m_colors;
        final int n3 = i + 2;
        colors3[n3] *= b;
    }
    
    public void getSpecularColor(final float[] value) {
        System.arraycopy(Material.m_colors, 4 + this.m_index, value, 0, 3);
    }
    
    public boolean specularEquals(final float[] specular) {
        final int i = 4 + this.m_index;
        return Material.m_colors[i] == specular[0] && Material.m_colors[i + 1] == specular[1] && Material.m_colors[i + 2] == specular[2];
    }
    
    public void copySpecularTo(final float[] specular) {
        System.arraycopy(Material.m_colors, 4 + this.m_index, specular, 0, 3);
    }
    
    public void setSpecularColor(final float[] specularColor) {
        System.arraycopy(specularColor, 0, Material.m_colors, 4 + this.m_index, 3);
    }
    
    public void setSpecularColor(final float r, final float g, final float b) {
        final int i = 4 + this.m_index;
        Material.m_colors[i] = r;
        Material.m_colors[i + 1] = g;
        Material.m_colors[i + 2] = b;
    }
    
    public boolean equal(final Material material) {
        if (material.m_index == this.m_index) {
            return true;
        }
        final int a = this.m_index;
        final int b = material.m_index;
        for (int i = 0; i < 7; ++i) {
            if (Material.m_colors[i + a] != Material.m_colors[i + b]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected void checkout() {
        synchronized (this.m_freelistMutex) {
            int index = Material.m_indexList.checkout();
            if (index == -1) {
                resize();
                index = Material.m_indexList.checkout();
            }
            this.m_index = index * 7;
            System.arraycopy(Material.DEFAULT, 0, Material.m_colors, this.m_index, 7);
        }
    }
    
    @Override
    protected void checkin() {
        synchronized (this.m_freelistMutex) {
            Material.m_indexList.checkin(this.m_index / 7);
        }
    }
    
    private static void resize() {
        Material.m_indexList.resize(Material.m_indexList.getSize() + 1000);
        final float[] colors = Material.m_colors;
        System.arraycopy(colors, 0, Material.m_colors = new float[7 * Material.m_indexList.getSize()], 0, colors.length);
    }
    
    static {
        DEFAULT = new float[] { 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f };
        m_indexList = new FreeList(15000);
        Material.m_colors = new float[7 * Material.m_indexList.getSize()];
        Factory = new ObjectFactory();
        (WHITE = Material.Factory.newInstance()).setDiffuseColor(1.0f, 1.0f, 1.0f, 1.0f);
        Material.WHITE.setSpecularColor(1.0f, 1.0f, 1.0f);
        (WHITE_NO_SPECULAR = Material.Factory.newInstance()).setDiffuseColor(1.0f, 1.0f, 1.0f, 1.0f);
        Material.WHITE_NO_SPECULAR.setSpecularColor(0.0f, 0.0f, 0.0f);
        (BLACK = Material.Factory.newInstance()).setDiffuseColor(0.0f, 0.0f, 0.0f, 1.0f);
        Material.BLACK.setSpecularColor(0.0f, 0.0f, 0.0f);
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<Material>
    {
        public ObjectFactory() {
            super(Material.class);
        }
        
        @Override
        public Material create() {
            return new Material(null);
        }
    }
}
