package com.ankamagames.framework.graphics.engine.text;

import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;
import java.io.*;

public class TexturedFont implements Font
{
    private short m_borderSize;
    private short m_numPages;
    private short m_minCharacterCode;
    private short m_maxCharacterCode;
    private short m_cellHeight;
    private short m_maxCharacterWidth;
    private short m_maxCharacterHeight;
    private String m_name;
    private Texture m_texture;
    private TShortObjectHashMap<TexturedCharacter> m_characters;
    
    public TexturedFont() {
        super();
        this.m_maxCharacterWidth = 0;
        this.m_maxCharacterHeight = 0;
    }
    
    @Override
    public Font createDerivateFont(final int fontStyle, final float size, final boolean enableAWTFont) {
        final String fontName = FontFactory.getType(this.m_name);
        return FontFactory.createFont(fontName, fontStyle, (int)size);
    }
    
    @Override
    public float getSize() {
        return FontFactory.getSize(this.m_name);
    }
    
    @Override
    public int getStyle() {
        return FontFactory.getStyle(this.m_name);
    }
    
    public void load(final String name, final String path) throws IOException {
        this.m_name = name;
        final ExtendedDataInputStream bitStream = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(path + name + ".tab"));
        final boolean bordered = name.contains("bordered");
        bitStream.setOffset(10);
        if (bordered) {
            this.m_borderSize = 2;
        }
        final short numCharacters = bitStream.readShort();
        this.m_numPages = bitStream.readShort();
        this.m_minCharacterCode = bitStream.readShort();
        this.m_maxCharacterCode = bitStream.readShort();
        this.m_cellHeight = (short)(bitStream.readShort() + this.m_borderSize);
        this.m_characters = new TShortObjectHashMap<TexturedCharacter>(numCharacters);
        for (int i = 0; i < numCharacters; ++i) {
            final TexturedCharacter character = new TexturedCharacter();
            character.m_code = bitStream.readShort();
            bitStream.readShort();
            character.m_x = (short)(bitStream.readShort() - this.m_borderSize);
            character.m_y = (short)(bitStream.readShort() - this.m_borderSize);
            character.m_width = (short)(bitStream.readShort() + 2 * this.m_borderSize);
            character.m_height = (short)(bitStream.readShort() + 2 * this.m_borderSize);
            character.m_leadingXOffset = bitStream.readShort();
            character.m_trailingXOffset = bitStream.readShort();
            this.m_characters.put(character.m_code, character);
            if (character.m_width > this.m_maxCharacterWidth) {
                this.m_maxCharacterWidth = character.m_width;
            }
            if (character.m_height > this.m_maxCharacterHeight) {
                this.m_maxCharacterHeight = character.m_height;
            }
        }
        this.addHardSpace();
        bitStream.close();
        final long crc = -6196766170285080576L + Engine.getTextureName(name);
        String fontPath = path + name + "000.DDS";
        if (!URLUtils.urlExists(fontPath)) {
            fontPath = path + name + "000.tga";
        }
        (this.m_texture = TextureManager.getInstance().createTexture(RendererType.OpenGL.getRenderer(), crc, fontPath, false)).addReference();
    }
    
    private void addHardSpace() {
        this.m_characters.put((short)160, this.m_characters.get((short)32));
    }
    
    public final String getName() {
        return this.m_name;
    }
    
    public final TexturedCharacter getCharacter(final short code) {
        if (code < this.m_minCharacterCode || code > (this.m_maxCharacterCode & 0xFFFF)) {
            return null;
        }
        return this.m_characters.get(code);
    }
    
    public final Texture getTexture() {
        return this.m_texture;
    }
    
    public final int getCellHeight() {
        return this.m_cellHeight;
    }
    
    public short getMaxCharacterWidth() {
        return this.m_maxCharacterWidth;
    }
    
    public short getMaxCharacterHeight() {
        return this.m_maxCharacterHeight;
    }
    
    @Override
    public short getBorderSize() {
        return this.m_borderSize;
    }
    
    @Override
    public void setDeltaXY(final int x, final int y) {
    }
    
    public static final class TexturedCharacter
    {
        public short m_code;
        public short m_x;
        public short m_y;
        public short m_width;
        public short m_height;
        public short m_leadingXOffset;
        public short m_trailingXOffset;
    }
}
