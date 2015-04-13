package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.fileFormat.io.*;

public class AleaTextureManager
{
    public static final String DEFAULT_FILE_EXTENSION = ".tgam";
    public static final String DEFAULT_GFX_PATH = "";
    private static final AleaTextureManager m_instance;
    private final String m_fileExtension;
    private final boolean m_generatePowerOfTwo;
    private String m_gfxPath;
    
    protected AleaTextureManager(final String fileExtension, final boolean generatePowerOfTwo) {
        super();
        this.m_gfxPath = "";
        this.m_fileExtension = fileExtension;
        this.m_generatePowerOfTwo = generatePowerOfTwo;
    }
    
    public static AleaTextureManager getInstance() {
        return AleaTextureManager.m_instance;
    }
    
    public final void setGfxPath(final String gfxPath) {
        this.m_gfxPath = gfxPath;
        if (!gfxPath.endsWith("/")) {
            this.m_gfxPath += "/";
        }
    }
    
    public Texture getTexture(final int gfxId) {
        final String textureFilename = this.getTextureFilename(gfxId);
        final long name = Engine.getTextureName(textureFilename);
        if (this.m_generatePowerOfTwo) {
            return TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), name, textureFilename, false);
        }
        return TextureManager.getInstance().createTexture(RendererType.OpenGL.getRenderer(), name, textureFilename, false);
    }
    
    protected final String getTextureFilename(final int gfxId) {
        return ContentFileHelper.transformFileName(this.m_gfxPath + gfxId + this.m_fileExtension);
    }
    
    static {
        m_instance = new AleaTextureManager(".tgam", false);
    }
}
