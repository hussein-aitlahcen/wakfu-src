package com.ankamagames.framework.graphics.engine.text;

import com.ankamagames.framework.graphics.engine.*;
import javax.media.opengl.*;
import java.nio.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class TexturedTextRenderer extends TextRenderer
{
    private TexturedFont m_font;
    private static VertexBufferPCT m_vertexBuffer;
    private static float[] m_positions;
    private static float[] m_colors;
    private static float[] m_borderColors;
    private static float[] m_textureCoords;
    private float m_offsetX;
    private float m_offsetY;
    private int m_borderSize;
    private float m_zoom;
    
    public TexturedTextRenderer() {
        super();
        this.m_offsetX = 0.0f;
        this.m_offsetY = 0.0f;
        this.m_borderSize = 1;
        this.m_zoom = 1.0f;
        TexturedTextRenderer.m_colors[3] = 1.0f;
        TexturedTextRenderer.m_colors[7] = 1.0f;
        TexturedTextRenderer.m_colors[11] = 1.0f;
        TexturedTextRenderer.m_colors[15] = 1.0f;
        TexturedTextRenderer.m_borderColors[3] = 1.0f;
        TexturedTextRenderer.m_borderColors[7] = 1.0f;
        TexturedTextRenderer.m_borderColors[11] = 1.0f;
        TexturedTextRenderer.m_borderColors[15] = 1.0f;
    }
    
    public void setFont(final TexturedFont font) {
        this.m_font = font;
    }
    
    @Override
    public String getFontName() {
        if (this.m_font == null) {
            return null;
        }
        return this.m_font.getName();
    }
    
    @Override
    public int getFontStyle() {
        if (this.m_font == null) {
            return 0;
        }
        return this.m_font.getStyle();
    }
    
    @Override
    public Font getFont() {
        return this.m_font;
    }
    
    @Override
    public Font createDerivedFont(final int fontStyle, final float size) {
        return null;
    }
    
    @Override
    public int getCharacterWidth(final char character) {
        final TexturedFont.TexturedCharacter c = this.m_font.getCharacter((short)character);
        if (c == null) {
            return 4;
        }
        if (character == ' ') {
            return c.m_trailingXOffset;
        }
        return c.m_trailingXOffset + c.m_leadingXOffset;
    }
    
    @Override
    public int getVisualCharacterWidth(final char character) {
        final TexturedFont.TexturedCharacter c = this.m_font.getCharacter((short)character);
        if (c == null) {
            return 4;
        }
        return c.m_width;
    }
    
    @Override
    public int getVisualCharacterHeight(final char character) {
        final TexturedFont.TexturedCharacter c = this.m_font.getCharacter((short)character);
        if (c == null) {
            return 4;
        }
        return c.m_height;
    }
    
    @Override
    public int getMaxCharacterWidth() {
        if (this.m_font == null) {
            return 8;
        }
        return this.m_font.getMaxCharacterWidth() + this.m_font.getBorderSize() * 2;
    }
    
    @Override
    public int getMaxCharacterHeight() {
        if (this.m_font == null) {
            return 12;
        }
        return this.m_font.getMaxCharacterHeight() + this.m_font.getBorderSize() * 2;
    }
    
    @Override
    public int getMaxVisibleTextLength(final String line, final int maxCount, final int maxWidth) {
        if (line == null || line.length() == 0) {
            return 0;
        }
        if (this.m_font == null) {
            return line.length();
        }
        int width = this.m_font.getBorderSize();
        final char[] chars = line.toCharArray();
        boolean first = true;
        for (int index = 0; index < chars.length; ++index) {
            if (index == maxCount) {
                return maxCount;
            }
            final char code = chars[index];
            final TexturedFont.TexturedCharacter character = this.m_font.getCharacter((short)code);
            if (character != null) {
                if (first) {
                    width += character.m_leadingXOffset;
                    first = false;
                }
                if (code == ' ') {
                    width += character.m_trailingXOffset;
                }
                else {
                    width += character.m_trailingXOffset;
                }
                if (width > maxWidth) {
                    return index;
                }
            }
        }
        return line.length();
    }
    
    @Override
    public int getLineWidth(final String line) {
        if (this.m_font == null) {
            return 4 * line.length();
        }
        int width = 0;
        final char[] chars = line.toCharArray();
        boolean first = true;
        for (int index = 0; index < chars.length; ++index) {
            final char code = chars[index];
            final TexturedFont.TexturedCharacter character = this.m_font.getCharacter((short)code);
            if (character != null) {
                if (first) {
                    width += character.m_leadingXOffset;
                    first = false;
                }
                if (code == ' ') {
                    width += character.m_trailingXOffset;
                }
                else {
                    width += character.m_trailingXOffset;
                }
            }
        }
        width += this.m_font.getBorderSize();
        return width;
    }
    
    @Override
    public int getLineHeight(final String line) {
        if (this.m_font == null) {
            return 12;
        }
        return this.m_font.getCellHeight();
    }
    
    @Override
    public int getDescent(final String line) {
        return 0;
    }
    
    @Override
    public boolean isBlured() {
        return false;
    }
    
    @Override
    public void setColor(final float red, final float green, final float blue, final float alpha) {
        TexturedTextRenderer.m_colors[0] = red * alpha;
        TexturedTextRenderer.m_colors[1] = green * alpha;
        TexturedTextRenderer.m_colors[2] = blue * alpha;
        TexturedTextRenderer.m_colors[3] = alpha;
        TexturedTextRenderer.m_colors[4] = red * alpha;
        TexturedTextRenderer.m_colors[5] = green * alpha;
        TexturedTextRenderer.m_colors[6] = blue * alpha;
        TexturedTextRenderer.m_colors[7] = alpha;
        TexturedTextRenderer.m_colors[8] = red * alpha;
        TexturedTextRenderer.m_colors[9] = green * alpha;
        TexturedTextRenderer.m_colors[10] = blue * alpha;
        TexturedTextRenderer.m_colors[11] = alpha;
        TexturedTextRenderer.m_colors[12] = red * alpha;
        TexturedTextRenderer.m_colors[13] = green * alpha;
        TexturedTextRenderer.m_colors[14] = blue * alpha;
        TexturedTextRenderer.m_colors[15] = alpha;
        TexturedTextRenderer.m_borderColors[3] = alpha;
        TexturedTextRenderer.m_borderColors[7] = alpha;
        TexturedTextRenderer.m_borderColors[11] = alpha;
        TexturedTextRenderer.m_borderColors[15] = alpha;
    }
    
    public void draw(final char[] line, final float x, final int endX, final float y) {
        this.draw(line, x, endX, y, 1.0f);
    }
    
    @Override
    public void draw(final char[] line, final float x, final float y) {
        this.draw(line, x, line.length, y, 1.0f);
    }
    
    @Override
    public void draw(final char[] line, final float x, final float y, final float zoom) {
        this.draw(line, x, line.length, y, zoom);
    }
    
    @Override
    public void draw(final char[] line, final float x, final int endX, final float y, final float zoom, final float justificationSpacing) {
        this.draw(line, x, line.length, y, zoom);
    }
    
    @Override
    public void draw(final char[] chars, float x, final int endX, float y, final float zoom) {
        if (this.m_font == null) {
            return;
        }
        this.m_zoom = zoom;
        final Texture texture = this.m_font.getTexture();
        final short borderSize = this.m_font.getBorderSize();
        x += this.m_offsetX - borderSize;
        y += this.m_offsetY - borderSize;
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        TexturedTextRenderer.m_vertexBuffer.rewind();
        texture.activate(glRenderer);
        final Point2i textureSize = texture.getSize(0);
        final float textureSizeX = textureSize.getX();
        final float textureSizeY = textureSize.getY();
        boolean first = true;
        for (int index = 0; index < Math.min(chars.length, endX); ++index) {
            final char code = chars[index];
            final TexturedFont.TexturedCharacter character = this.m_font.getCharacter((short)code);
            if (character != null) {
                if (first) {
                    x += character.m_leadingXOffset * this.m_zoom;
                    first = false;
                }
                x -= character.m_leadingXOffset * this.m_zoom;
                if (code == ' ') {
                    x += character.m_trailingXOffset * this.m_zoom;
                }
                else {
                    this.drawCharacter(x, y, textureSizeX, textureSizeY, character, TexturedTextRenderer.m_colors);
                    x += (character.m_trailingXOffset + character.m_leadingXOffset) * this.m_zoom;
                }
            }
        }
        final int numVertices = TexturedTextRenderer.m_vertexBuffer.getNumVertices();
        final GL gl = glRenderer.getDevice();
        gl.glVertexPointer(2, 5126, 0, (Buffer)TexturedTextRenderer.m_vertexBuffer.getPositionBuffer());
        gl.glColorPointer(4, 5126, 0, (Buffer)TexturedTextRenderer.m_vertexBuffer.getColorBuffer());
        gl.glTexCoordPointer(2, 5126, 0, (Buffer)TexturedTextRenderer.m_vertexBuffer.getTexCoord0Buffer());
        gl.glDrawArrays(7, 0, numVertices);
    }
    
    private void addBorder(final String line, int x, final int y) {
        final Texture texture = this.m_font.getTexture();
        final Point2i textureSize = texture.getSize(0);
        final float textureSizeX = textureSize.getX();
        final float textureSizeY = textureSize.getY();
        final char[] chars = line.toCharArray();
        for (int index = 0; index < chars.length; ++index) {
            final char code = chars[index];
            final TexturedFont.TexturedCharacter character = this.m_font.getCharacter((short)code);
            if (character != null) {
                if (code == ' ') {
                    x += character.m_trailingXOffset;
                }
                else {
                    for (int borderSize = this.m_borderSize, i = -borderSize; i <= borderSize; ++i) {
                        for (int j = -borderSize; j <= borderSize; ++j) {
                            if (i != 0 || j != 0) {
                                this.drawCharacter(x + i, y + j, textureSizeX, textureSizeY, character, TexturedTextRenderer.m_borderColors);
                            }
                        }
                    }
                    x += character.m_trailingXOffset;
                }
            }
        }
    }
    
    private void drawCharacter(final float x, final float y, final float textureSizeX, final float textureSizeY, final TexturedFont.TexturedCharacter character, final float[] color) {
        TexturedTextRenderer.m_positions[0] = x;
        TexturedTextRenderer.m_positions[1] = y;
        TexturedTextRenderer.m_textureCoords[0] = character.m_x / textureSizeX;
        TexturedTextRenderer.m_textureCoords[1] = (character.m_y + character.m_height) / textureSizeY;
        TexturedTextRenderer.m_positions[2] = x;
        TexturedTextRenderer.m_positions[3] = y + character.m_height * this.m_zoom;
        TexturedTextRenderer.m_textureCoords[2] = character.m_x / textureSizeX;
        TexturedTextRenderer.m_textureCoords[3] = character.m_y / textureSizeY;
        TexturedTextRenderer.m_positions[4] = x + character.m_width * this.m_zoom;
        TexturedTextRenderer.m_positions[5] = y + character.m_height * this.m_zoom;
        TexturedTextRenderer.m_textureCoords[4] = (character.m_x + character.m_width) / textureSizeX;
        TexturedTextRenderer.m_textureCoords[5] = character.m_y / textureSizeY;
        TexturedTextRenderer.m_positions[6] = x + character.m_width * this.m_zoom;
        TexturedTextRenderer.m_positions[7] = y;
        TexturedTextRenderer.m_textureCoords[6] = (character.m_x + character.m_width) / textureSizeX;
        TexturedTextRenderer.m_textureCoords[7] = (character.m_y + character.m_height) / textureSizeY;
        TexturedTextRenderer.m_vertexBuffer.putPositionBuffer(TexturedTextRenderer.m_positions);
        TexturedTextRenderer.m_vertexBuffer.putTexCoord(TexturedTextRenderer.m_textureCoords);
        TexturedTextRenderer.m_vertexBuffer.putColorBuffer(color);
        TexturedTextRenderer.m_vertexBuffer.setNumVertices(TexturedTextRenderer.m_vertexBuffer.getNumVertices() + 4);
    }
    
    @Override
    public void beginRendering(final int viewportWidth, final int viewportHeight) {
        this.m_offsetX = -viewportWidth / 2;
        this.m_offsetY = -viewportHeight / 2;
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        glRenderer.m_stateManager.setVertexArrayComponents(13);
        RenderStateManager.getInstance().setBlendFunc(BlendModes.One, BlendModes.InvSrcAlpha);
    }
    
    @Override
    public void endRendering() {
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        RenderStateManager.getInstance().setBlendFunc(BlendModes.SrcAlpha, BlendModes.InvSrcAlpha);
    }
    
    @Override
    public void begin3DRendering() {
        this.m_offsetX = 0.0f;
        this.m_offsetY = 0.0f;
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        glRenderer.m_stateManager.setVertexArrayComponents(13);
        RenderStateManager.getInstance().setBlendFunc(BlendModes.One, BlendModes.InvSrcAlpha);
    }
    
    @Override
    public void end3DRendering() {
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        RenderStateManager.getInstance().setBlendFunc(BlendModes.SrcAlpha, BlendModes.InvSrcAlpha);
    }
    
    static {
        TexturedTextRenderer.m_positions = new float[8];
        TexturedTextRenderer.m_colors = new float[16];
        TexturedTextRenderer.m_borderColors = new float[16];
        TexturedTextRenderer.m_textureCoords = new float[8];
        final int maxVertices = 4096;
        TexturedTextRenderer.m_vertexBuffer = VertexBufferPCT.Factory.newInstance(4096);
    }
}
