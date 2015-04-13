package com.ankamagames.framework.graphics.engine.text;

import java.io.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.*;

public class BMFTexturedFont implements Font
{
    private byte m_version;
    private short m_numCharacters;
    private short m_numPages;
    private short m_cellHeight;
    private short m_baseLine;
    private short m_maxCharacterWidth;
    private short m_maxCharacterHeight;
    private String m_name;
    private Texture m_texture;
    private TIntObjectHashMap<TexturedCharacter> m_characters;
    
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
    
    private void addHardSpace() {
        this.m_characters.put(160, this.m_characters.get(32));
    }
    
    private void readInfoBlock(final ExtendedDataInputStream bitStream) throws IOException {
        final byte b = bitStream.readByte();
        final int blockSize = bitStream.readInt();
        final int nextBlockOffset = bitStream.getOffset() + blockSize;
        bitStream.setOffset(nextBlockOffset);
    }
    
    private void readCommonBlock(final ExtendedDataInputStream bitStream) throws IOException {
        final byte b = bitStream.readByte();
        final int blockSize = bitStream.readInt();
        final int nextBlockOffset = bitStream.getOffset() + blockSize;
        this.m_cellHeight = bitStream.readShort();
        this.m_baseLine = bitStream.readShort();
        bitStream.setOffset(nextBlockOffset);
    }
    
    private void readPagesBlock(final ExtendedDataInputStream bitStream) throws IOException {
        final byte b = bitStream.readByte();
        final int blockSize = bitStream.readInt();
        final int nextBlockOffset = bitStream.getOffset() + blockSize;
        bitStream.setOffset(nextBlockOffset);
    }
    
    private void readCharactersBlock(final ExtendedDataInputStream bitStream) throws IOException {
        final byte b = bitStream.readByte();
        final int blockSize = bitStream.readInt();
        this.m_numCharacters = (short)(blockSize / 20);
        this.m_characters = new TIntObjectHashMap<TexturedCharacter>(this.m_numCharacters, 1.0f);
        for (int i = 0; i < this.m_numCharacters; ++i) {
            final TexturedCharacter character = new TexturedCharacter();
            final char[] chars = Character.toChars(bitStream.readInt());
            character.m_code = chars[0];
            character.m_x = bitStream.readShort();
            character.m_y = bitStream.readShort();
            character.m_width = bitStream.readShort();
            character.m_height = bitStream.readShort();
            character.m_xOffset = bitStream.readShort();
            character.m_yOffset = bitStream.readShort();
            character.m_xAdvance = bitStream.readShort();
            bitStream.readByte();
            bitStream.readByte();
            this.m_characters.put(character.m_code, character);
            this.m_maxCharacterWidth = (short)Math.max(this.m_maxCharacterWidth, character.m_width);
            this.m_maxCharacterHeight = (short)Math.max(this.m_maxCharacterHeight, character.m_height);
        }
    }
    
    private void readKerningBlock(final ExtendedDataInputStream bitStream) throws IOException {
        if (bitStream.available() <= 0) {
            return;
        }
        final byte b = bitStream.readByte();
        final int blockSize = bitStream.readInt();
        for (int numKernings = blockSize / 10, i = 0; i < numKernings; ++i) {
            final char[] first = Character.toChars(bitStream.readInt());
            final char[] second = Character.toChars(bitStream.readInt());
            final short kerning = bitStream.readShort();
            final TexturedCharacter character = this.m_characters.get(first[0]);
            if (character != null) {
                if (character.m_kerning == null) {
                    character.m_kerning = new IntShortLightWeightMap();
                }
                character.m_kerning.put(second[0], kerning);
            }
        }
        this.compactKernings();
    }
    
    private void compactKernings() {
        this.m_characters.forEachValue(new TObjectProcedure<TexturedCharacter>() {
            @Override
            public boolean execute(final TexturedCharacter characters) {
                if (characters.m_kerning != null) {
                    characters.m_kerning.compact();
                }
                return true;
            }
        });
    }
    
    public void load(final String name, final String path) throws IOException {
        this.m_name = name;
        final ExtendedDataInputStream bitStream = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(path + name + ".fnt"));
        if (bitStream.readByte() != 66 || bitStream.readByte() != 77 || bitStream.readByte() != 70) {
            return;
        }
        this.m_version = bitStream.readByte();
        this.readInfoBlock(bitStream);
        this.readCommonBlock(bitStream);
        this.readPagesBlock(bitStream);
        this.readCharactersBlock(bitStream);
        this.readKerningBlock(bitStream);
        this.addHardSpace();
        bitStream.close();
        final long crc = -6196766170285080576L + Engine.getTextureName(name);
        String fontPath = path + name + "_0.dds";
        if (!URLUtils.urlExists(fontPath)) {
            fontPath = path + name + "_0.DDS";
        }
        (this.m_texture = TextureManager.getInstance().createTexture(RendererType.OpenGL.getRenderer(), crc, fontPath, false)).addReference();
    }
    
    public final String getName() {
        return this.m_name;
    }
    
    public final TexturedCharacter getCharacter(final char code) {
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
        return 0;
    }
    
    public short getBaseLine() {
        return this.m_baseLine;
    }
    
    @Override
    public void setDeltaXY(final int x, final int y) {
    }
    
    public short getBottomLine() {
        return (short)(this.m_cellHeight - this.m_baseLine);
    }
    
    public void setMonospaced() {
        this.m_characters.forEachValue(new TObjectProcedure<TexturedCharacter>() {
            @Override
            public boolean execute(final TexturedCharacter c) {
                c.m_xAdvance = BMFTexturedFont.this.m_maxCharacterWidth;
                c.m_kerning = null;
                return true;
            }
        });
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BMFTexturedFont that = (BMFTexturedFont)o;
        return this.m_name.equals(that.m_name);
    }
    
    @Override
    public int hashCode() {
        return this.m_name.hashCode();
    }
    
    public static final class TexturedCharacter
    {
        public char m_code;
        public short m_x;
        public short m_y;
        public short m_width;
        public short m_height;
        public short m_xOffset;
        public short m_yOffset;
        public short m_xAdvance;
        public IntShortLightWeightMap m_kerning;
    }
}
