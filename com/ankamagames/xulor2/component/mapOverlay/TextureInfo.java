package com.ankamagames.xulor2.component.mapOverlay;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.net.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.fileFormat.io.*;
import org.apache.commons.lang3.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.texture.*;

public class TextureInfo
{
    private static final Logger m_logger;
    public final double m_isoX;
    public final double m_isoY;
    public final double m_isoWidth;
    public final double m_isoHeight;
    public final int m_textureWidth;
    public final int m_textureHeight;
    
    private TextureInfo(final double isoX, final double isoY, final double isoWidth, final double isoHeight, final int textureWidth, final int textureHeight) {
        super();
        this.m_isoX = isoX;
        this.m_isoY = isoY;
        this.m_isoWidth = isoWidth;
        this.m_isoHeight = isoHeight;
        this.m_textureWidth = textureWidth;
        this.m_textureHeight = textureHeight;
    }
    
    public static TextureInfo create(final DocumentEntry child) {
        if (child.getName().equals("#text") || child.getName().equals("#comment")) {
            return null;
        }
        final double isoX = getDouble(child, "isoX", 0.0);
        final double isoY = getDouble(child, "isoY", 0.0);
        final double isoWidth = getDouble(child, "isoWidth", -1.0);
        final double isoHeight = getDouble(child, "isoHeight", -1.0);
        final int textureWidth = getInt(child, "width", -1);
        final int textureHeight = getInt(child, "height", -1);
        return new TextureInfo(isoX, isoY, isoWidth, isoHeight, textureWidth, textureHeight);
    }
    
    public static Texture createTexture(final DocumentEntry child, final URL url) {
        final DocumentEntry entry = child.getParameterByName("texture");
        if (entry == null) {
            return null;
        }
        final String texturePath = entry.getStringValue();
        try {
            final URL texURL = URLUtils.urlCompound(url, texturePath);
            final String textureName = texURL.toString();
            return createTexturePowerOfTwo(textureName);
        }
        catch (Exception e) {
            TextureInfo.m_logger.error((Object)"Probl\u00e8me lors de la r\u00e9cup\u00e9ration de la texture de la map");
            return null;
        }
    }
    
    public static Texture createMaskTexture(final DocumentEntry child, final URL url, final String path) {
        final DocumentEntry entry = child.getParameterByName("texture");
        if (entry == null) {
            return null;
        }
        try {
            final String fileNameWithoutExt = FileHelper.getNameWithoutExt(path);
            final URL texURL = URLUtils.urlCompound(url, "mask" + StringUtils.capitalize(fileNameWithoutExt) + ".png");
            return URLUtils.urlExists(texURL) ? createTexturePowerOfTwo(texURL.toString(), true) : null;
        }
        catch (Exception e) {
            TextureInfo.m_logger.error((Object)"Probl\u00e8me lors de la r\u00e9cup\u00e9ration de la texture de la map");
            return null;
        }
    }
    
    private static double getDouble(final DocumentEntry node, final String paramName, final double defaultValue) {
        final DocumentEntry entry = node.getParameterByName(paramName);
        return (entry == null) ? defaultValue : entry.getDoubleValue();
    }
    
    private static int getInt(final DocumentEntry node, final String paramName, final int defaultValue) {
        final DocumentEntry entry = node.getParameterByName(paramName);
        return (entry == null) ? defaultValue : entry.getIntValue();
    }
    
    public static Texture createTexturePowerOfTwo(final String path) {
        return createTexturePowerOfTwo(path, false);
    }
    
    public static Texture createTexturePowerOfTwo(final String path, final boolean keepData) {
        if (URLUtils.urlExists(path)) {
            return TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), Engine.getTextureName(path), path, keepData, true);
        }
        TextureInfo.m_logger.info((Object)("Impossible de trouver le fichier " + path));
        return null;
    }
    
    public Pixmap createPixmap(final Texture texture) {
        if (this.m_textureWidth != -1 && this.m_textureHeight != -1) {
            return new Pixmap(texture, 0, 0, this.m_textureWidth, this.m_textureHeight);
        }
        return new Pixmap(texture);
    }
    
    static {
        m_logger = Logger.getLogger((Class)TextureInfo.class);
    }
}
