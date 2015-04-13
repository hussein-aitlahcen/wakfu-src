package com.ankamagames.baseImpl.graphics.alea.cellSelector;

import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;

public class ElementSelection
{
    final String m_name;
    
    public ElementSelection(final String name, final float[] color, final Texture texture, final HighLightTextureApplication textureApplication) {
        super();
        this.m_name = name;
        HighLightLayer layer;
        if (texture == null) {
            layer = HighLightManager.getInstance().createLayer(name, textureApplication);
        }
        else {
            layer = HighLightManager.getInstance().createLayer(name, texture, textureApplication);
        }
        layer.setColor(color);
    }
    
    public ElementSelection(final String name, final float[] color) {
        this(name, color, null, HighLightTextureApplication.ISO);
    }
    
    public final void add(final int x, final int y, final short z) {
        this.getHighLightLayer().add(x, y, z);
    }
    
    private HighLightLayer getHighLightLayer() {
        return HighLightManager.getInstance().getLayer(this.m_name);
    }
    
    public final void clear() {
        this.getHighLightLayer().clear();
    }
    
    public final boolean contains(final Point3 target) {
        return this.contains(target.getX(), target.getY(), target.getZ());
    }
    
    public final boolean contains(final int x, final int y, final short z) {
        return this.getHighLightLayer().contains(x, y, z);
    }
    
    public final void setTexture(final String textureFilePath, final HighLightTextureApplication textureApplication) {
        final Texture texture = createTexture(textureFilePath);
        this.getHighLightLayer().setTexture(texture, textureApplication);
    }
    
    public final void setColor(final float[] color) {
        this.getHighLightLayer().setColor(color);
    }
    
    public final void remove(final int x, final int y, final short z) {
        this.getHighLightLayer().remove(x, y, z);
    }
    
    public static Texture createTexture(final String textureFilePath) {
        final String textureName = FileHelper.getNameWithoutExt(textureFilePath);
        return TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), Engine.getTextureName(textureName), textureFilePath, false);
    }
    
    public final void setDisplayPriority(final int priority) {
        this.getHighLightLayer().m_priority = priority;
    }
}
