package com.ankamagames.xulor2.graphics;

import org.apache.log4j.*;
import java.net.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class TextureLoader
{
    private Logger m_logger;
    private static TextureLoader m_textureLoader;
    
    private TextureLoader() {
        super();
        this.m_logger = Logger.getLogger((Class)TextureLoader.class);
    }
    
    public Texture loadTexture(final URL url) {
        if (url == null) {
            return null;
        }
        final String fullName = url.toString();
        final long textureName = Engine.getTextureName(fullName);
        Texture texture = TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), textureName, fullName, false, true);
        if (texture == null) {
            this.m_logger.error((Object)("Probl\u00e8me au chargement de la texture " + url));
            final Image image = new Image();
            texture = TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), textureName, image, false);
            image.removeReference();
        }
        return texture;
    }
    
    public Texture loadTexture(final String value) {
        return ConverterLibrary.getInstance().convertToTexture(value);
    }
    
    public Texture loadTextureDirect(final String value) {
        final long textureName = PrimitiveConverter.getLong(value, -1L);
        if (textureName == -1L) {
            return null;
        }
        return TextureManager.getInstance().getTexture(textureName);
    }
    
    public void removeAllTextures() {
    }
    
    public static TextureLoader getInstance() {
        return TextureLoader.m_textureLoader;
    }
    
    static {
        TextureLoader.m_textureLoader = new TextureLoader();
    }
}
