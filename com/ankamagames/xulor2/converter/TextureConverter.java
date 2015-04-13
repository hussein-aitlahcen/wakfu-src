package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.java.util.*;
import java.io.*;
import java.net.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public class TextureConverter implements Converter<Texture>
{
    private static Logger m_logger;
    private Class<Texture> TEMPLATE;
    private boolean m_canConvertFromScratch;
    
    public TextureConverter() {
        super();
        this.TEMPLATE = Texture.class;
        this.m_canConvertFromScratch = false;
    }
    
    @Override
    public Texture convert(final String value) {
        return this.convert((Class<? extends Texture>)this.TEMPLATE, value);
    }
    
    @Override
    public Texture convert(final Class<? extends Texture> type, final String value) {
        return this.convert(type, value, (ElementMap)null);
    }
    
    @Override
    public Texture convert(final Class<? extends Texture> type, final String value, final ElementMap map) {
        if (value == null || !type.equals(this.TEMPLATE)) {
            return null;
        }
        Texture texture = Xulor.getInstance().getDocumentParser().getTexture(value);
        if (texture != null) {
            return texture;
        }
        texture = TextureLoader.getInstance().loadTextureDirect(value);
        if (texture != null) {
            return texture;
        }
        try {
            final URL url = ContentFileHelper.getURL(value);
            if (!URLUtils.urlExists(url)) {
                TextureConverter.m_logger.warn((Object)("Impossible de lire l'image " + value));
                return null;
            }
            return TextureLoader.getInstance().loadTexture(url);
        }
        catch (MalformedURLException e2) {
            final URL resourceUrl = this.getClass().getClassLoader().getResource(value);
            if (resourceUrl != null) {
                return TextureLoader.getInstance().loadTexture(resourceUrl);
            }
            final File f = new File(value);
            if (f.exists()) {
                try {
                    return TextureLoader.getInstance().loadTexture(f.toURI().toURL());
                }
                catch (MalformedURLException e) {
                    TextureConverter.m_logger.error((Object)"Exception", (Throwable)e);
                }
            }
            TextureConverter.m_logger.error((Object)("pas de texture " + value));
            return null;
        }
    }
    
    @Override
    public Class<Texture> convertsTo() {
        return this.TEMPLATE;
    }
    
    @Override
    public boolean canConvertFromScratch() {
        return this.m_canConvertFromScratch;
    }
    
    public void setCanConvertFromScratch(final boolean canConvert) {
        this.m_canConvertFromScratch = canConvert;
    }
    
    @Override
    public boolean canConvertWithoutVariables() {
        return false;
    }
    
    @Override
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<? extends Texture> type, final String value, final Environment env) {
        if (value == null || !type.equals(this.TEMPLATE)) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        doc.addImport(this.TEMPLATE);
        Texture texture = Xulor.getInstance().getDocumentParser().getTexture(value);
        if (texture != null) {
            return sb.append("doc.getTexture(\"").append(value).append("\")").toString();
        }
        texture = TextureLoader.getInstance().loadTextureDirect(value);
        if (texture != null) {
            doc.addImport(TextureLoader.class);
            return sb.append("TextureLoader.getInstance().loadTextureDirect(\"").append(value).append("\")").toString();
        }
        if (URLUtils.urlExists(value)) {
            final String textureVarName = doc.getUnusedVarName();
            doc.addImport(MalformedURLException.class);
            doc.addImport(TextureLoader.class);
            doc.addGeneratedCommandLine(new ClassVariable(this.TEMPLATE, textureVarName, "null"));
            doc.addGeneratedCommandLine(new RawCommand("try {"));
            doc.addGeneratedCommandLine(new RawCommand("\tURL url = ContentFileHelper.getURL(\"" + value + "\""));
            doc.addGeneratedCommandLine(new ClassVariable(null, textureVarName, "TextureLoader.getInstance().loadTexture(url)", true));
            doc.addGeneratedCommandLine(new RawCommand("} catch (MalformedURLException e) {}"));
            return textureVarName;
        }
        final URL resourceUrl = this.getClass().getClassLoader().getResource(value);
        if (resourceUrl != null) {
            doc.addImport(TextureLoader.class);
            final String textureVarName2 = doc.getUnusedVarName();
            doc.addGeneratedCommandLine(new ClassVariable(this.TEMPLATE, textureVarName2, "null"));
            doc.addGeneratedCommandLine(new RawCommand("{"));
            doc.addGeneratedCommandLine(new RawCommand("\tURL url = getClass().getClassLoader().getResource(\"" + value + "\""));
            doc.addGeneratedCommandLine(new ClassVariable(null, textureVarName2, "TextureLoader.getInstance().loadTexture(url)", true));
            doc.addGeneratedCommandLine(new RawCommand("}"));
            return textureVarName2;
        }
        final File f = new File(value);
        if (f.exists()) {
            try {
                doc.addImport(TextureLoader.class);
                doc.addImport(File.class);
                doc.addImport(MalformedURLException.class);
                TextureLoader.getInstance().loadTexture(f.toURI().toURL());
                final String textureVarName3 = doc.getUnusedVarName();
                doc.addGeneratedCommandLine(new ClassVariable(this.TEMPLATE, textureVarName3, "null"));
                doc.addGeneratedCommandLine(new RawCommand("try {"));
                doc.addGeneratedCommandLine(new RawCommand("\tFile f = new File(\"" + value + "\""));
                doc.addGeneratedCommandLine(new ClassVariable(null, textureVarName3, "TextureLoader.getInstance().loadTexture(f.toURI().toURL())", true));
                doc.addGeneratedCommandLine(new RawCommand("} catch (MalformedURLException e) {}"));
                return textureVarName3;
            }
            catch (MalformedURLException e) {
                TextureConverter.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        TextureConverter.m_logger.error((Object)("pas de texture " + value));
        return "null";
    }
    
    static {
        TextureConverter.m_logger = Logger.getLogger((Class)TextureConverter.class);
    }
}
