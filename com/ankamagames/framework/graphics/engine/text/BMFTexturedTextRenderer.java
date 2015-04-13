package com.ankamagames.framework.graphics.engine.text;

import com.ankamagames.framework.graphics.engine.*;
import javax.media.opengl.*;
import java.nio.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.states.*;

public class BMFTexturedTextRenderer extends TextRenderer
{
    private BMFTexturedFont m_font;
    private static VertexBufferPCT m_vertexBuffer;
    private static float[] m_colors;
    private float m_offsetX;
    private float m_offsetY;
    private float m_zoom;
    
    public BMFTexturedTextRenderer() {
        super();
        this.m_offsetX = 0.0f;
        this.m_offsetY = 0.0f;
        this.m_zoom = 1.0f;
    }
    
    public void setFont(final BMFTexturedFont font) {
        this.m_font = font;
    }
    
    @Override
    public String getFontName() {
        return (this.m_font == null) ? null : this.m_font.getName();
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
        final BMFTexturedFont.TexturedCharacter c = this.m_font.getCharacter(character);
        if (c == null) {
            return 4;
        }
        return c.m_xAdvance;
    }
    
    @Override
    public int getVisualCharacterWidth(final char character) {
        final BMFTexturedFont.TexturedCharacter c = this.m_font.getCharacter(character);
        if (c == null) {
            return 4;
        }
        return c.m_width;
    }
    
    @Override
    public int getVisualCharacterHeight(final char character) {
        final BMFTexturedFont.TexturedCharacter c = this.m_font.getCharacter(character);
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
        float width = this.m_font.getBorderSize();
        final char[] chars = line.toCharArray();
        for (int index = 0; index < chars.length; ++index) {
            if (index == maxCount) {
                return maxCount;
            }
            final BMFTexturedFont.TexturedCharacter character = this.m_font.getCharacter(chars[index]);
            if (character != null) {
                width += character.m_xAdvance;
                if (character.m_kerning != null && index < chars.length - 1) {
                    width += character.m_kerning.get(chars[index + 1]);
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
        float width = 0.0f;
        final char[] chars = line.toCharArray();
        for (int index = 0; index < chars.length; ++index) {
            final BMFTexturedFont.TexturedCharacter character = this.m_font.getCharacter(chars[index]);
            if (character != null) {
                if (character.m_kerning != null && index < chars.length - 1) {
                    width += character.m_kerning.get(chars[index + 1]);
                }
                width += character.m_xAdvance;
            }
        }
        width += this.m_font.getBorderSize();
        return (int)width;
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
        return this.m_font.getBottomLine();
    }
    
    @Override
    public boolean isBlured() {
        return false;
    }
    
    @Override
    public void setColor(final float red, final float green, final float blue, final float alpha) {
        BMFTexturedTextRenderer.m_colors[0] = red * alpha;
        BMFTexturedTextRenderer.m_colors[1] = green * alpha;
        BMFTexturedTextRenderer.m_colors[2] = blue * alpha;
        BMFTexturedTextRenderer.m_colors[3] = alpha;
    }
    
    @Override
    public final void draw(final char[] line, final float x, final float y) {
        this.draw(line, x, line.length, y, 1.0f);
    }
    
    @Override
    public final void draw(final char[] line, final float x, final float y, final float zoom) {
        this.draw(line, x, line.length, y, zoom);
    }
    
    @Override
    public final void draw(final char[] chars, final float x, final int endX, final float y, final float zoom) {
        this.draw(chars, x, endX, y, zoom, 0.0f);
    }
    
    @Override
    public void draw(final char[] chars, final float _x, final int endX, float y, final float zoom, final float espaceIncrement) {
        if (this.m_font == null) {
            return;
        }
        this.m_zoom = zoom;
        final short borderSize = this.m_font.getBorderSize();
        float x = _x + (this.m_offsetX - borderSize);
        y += this.m_offsetY - borderSize + this.m_font.getBaseLine();
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        final Texture texture = this.m_font.getTexture();
        RenderStateManager.getInstance().useTexture(glRenderer, (GLTexture)texture);
        RenderStateManager.getInstance().applyStates(glRenderer);
        final Point2i textureSize = texture.getSize(0);
        final float textureSizeX = textureSize.getX();
        final float textureSizeY = textureSize.getY();
        BMFTexturedTextRenderer.m_vertexBuffer.begin();
        for (int length = Math.min(chars.length, endX), index = 0; index < length; ++index) {
            final char code = chars[index];
            final BMFTexturedFont.TexturedCharacter character = this.m_font.getCharacter(code);
            if (character != null) {
                if (code == ' ') {
                    x += (character.m_xAdvance + espaceIncrement) * this.m_zoom;
                }
                else {
                    final float halfCharacterHeight = (character.m_yOffset + character.m_height) / 2.0f;
                    final float x2 = MathHelper.fastRound(x) + character.m_xOffset * this.m_zoom;
                    final float y2 = y - halfCharacterHeight * this.m_zoom - halfCharacterHeight;
                    final float x3 = x2 + character.m_width * this.m_zoom;
                    final float y3 = y2 + character.m_height * this.m_zoom;
                    final float tx0 = character.m_x / textureSizeX;
                    final float ty0 = character.m_y / textureSizeY;
                    final float tx = (character.m_x + character.m_width) / textureSizeX;
                    final float ty = (character.m_y + character.m_height) / textureSizeY;
                    BMFTexturedTextRenderer.m_vertexBuffer.pushVertex(x2, y2, tx0, ty, BMFTexturedTextRenderer.m_colors);
                    BMFTexturedTextRenderer.m_vertexBuffer.pushVertex(x2, y3, tx0, ty0, BMFTexturedTextRenderer.m_colors);
                    BMFTexturedTextRenderer.m_vertexBuffer.pushVertex(x3, y3, tx, ty0, BMFTexturedTextRenderer.m_colors);
                    BMFTexturedTextRenderer.m_vertexBuffer.pushVertex(x3, y2, tx, ty, BMFTexturedTextRenderer.m_colors);
                    if (character.m_kerning != null && index < length - 1) {
                        x += character.m_kerning.get(chars[index + 1]) * this.m_zoom;
                    }
                    x += character.m_xAdvance * this.m_zoom;
                }
            }
        }
        BMFTexturedTextRenderer.m_vertexBuffer.end();
        final int numVertices = BMFTexturedTextRenderer.m_vertexBuffer.getNumVertices();
        final GL gl = glRenderer.getDevice();
        gl.glVertexPointer(2, 5126, 0, (Buffer)BMFTexturedTextRenderer.m_vertexBuffer.getPositionBuffer());
        gl.glColorPointer(4, 5126, 0, (Buffer)BMFTexturedTextRenderer.m_vertexBuffer.getColorBuffer());
        gl.glTexCoordPointer(2, 5126, 0, (Buffer)BMFTexturedTextRenderer.m_vertexBuffer.getTexCoord0Buffer());
        gl.glDrawArrays(7, 0, numVertices);
    }
    
    @Override
    public void beginRendering(final int viewportWidth, final int viewportHeight) {
        this.m_offsetX = -viewportWidth / 2;
        this.m_offsetY = -viewportHeight / 2;
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        glRenderer.m_stateManager.setVertexArrayComponents(13);
        RenderStateManager.getInstance().setBlendFunc(BlendModes.SrcAlpha, BlendModes.InvSrcAlpha);
    }
    
    @Override
    public void endRendering() {
    }
    
    @Override
    public void begin3DRendering() {
        this.m_offsetX = 0.0f;
        this.m_offsetY = 0.0f;
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        glRenderer.m_stateManager.setVertexArrayComponents(13);
        RenderStateManager.getInstance().setBlendFunc(BlendModes.SrcAlpha, BlendModes.InvSrcAlpha);
    }
    
    @Override
    public void end3DRendering() {
    }
    
    static {
        BMFTexturedTextRenderer.m_colors = new float[4];
        final int maxVertices = 4096;
        BMFTexturedTextRenderer.m_vertexBuffer = VertexBufferPCT.Factory.newInstance(4096);
    }
}
