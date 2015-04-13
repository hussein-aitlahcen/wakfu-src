package com.ankamagames.xulor2.component.mesh;

import com.ankamagames.framework.graphics.engine.geometry.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.engine.*;
import javax.media.opengl.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.text.builder.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.xulor2.component.text.builder.content.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.xulor2.component.text.builder.selection.*;
import com.ankamagames.framework.kernel.core.common.*;

public final class GLTextGeometry extends Geometry
{
    public static final ObjectFactory Factory;
    public static final boolean RENDER_TEXT = true;
    private static final int CURSOR_BLINK_DELAY = 400;
    private TextBuilder m_textBuilder;
    private long m_lastCursorBlinkTime;
    private boolean m_selectionVisible;
    private boolean m_brightenColor;
    private boolean m_darkenColor;
    private Color m_modulationColor;
    private long m_currentTime;
    VertexBufferPCT m_vb;
    
    private GLTextGeometry() {
        super();
        this.m_lastCursorBlinkTime = 0L;
        this.m_selectionVisible = false;
        this.m_brightenColor = false;
        this.m_darkenColor = false;
        this.m_modulationColor = null;
        this.m_currentTime = 0L;
    }
    
    public void setTextBuilder(final TextBuilder textBuilder) {
        this.m_textBuilder = textBuilder;
    }
    
    public void save(final OutputBitStream bitStream) throws IOException {
        assert false : "Currently not implemented";
    }
    
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        assert false : "Currently not implemented";
    }
    
    @Override
    public void update(final float timeIncrement) {
    }
    
    @Override
    public void setColor(final float r, final float g, final float b, final float a) {
        assert false : "Currently not implemented";
    }
    
    public boolean isBrightenColor() {
        return this.m_brightenColor;
    }
    
    public void setBrightenColor(final boolean brightenColor) {
        this.m_brightenColor = brightenColor;
    }
    
    public boolean isDarkenColor() {
        return this.m_darkenColor;
    }
    
    public void setDarkenColor(final boolean darkenColor) {
        this.m_darkenColor = darkenColor;
    }
    
    public void setModulationColor(final Color color) {
        if (this.m_modulationColor == color) {
            return;
        }
        this.m_modulationColor = color;
    }
    
    public Color getModulationColor() {
        return this.m_modulationColor;
    }
    
    public void setCurrentTime(final long currentTime) {
        this.m_currentTime = currentTime;
    }
    
    @Override
    public void render(final Renderer renderer) {
        assert renderer.getType() == RendererType.OpenGL;
        final GL gl = renderer.getDevice();
        final RenderStateManager stateManager = RenderStateManager.getInstance();
        final PooledRectangle scissor = Graphics.getInstance().peekScissor();
        final TextWidget widget = this.m_textBuilder.getWidget();
        final ArrayList<LineBlock> lineBlocks = this.m_textBuilder.getLineBlocks();
        final int lineCount = lineBlocks.size();
        final int fullHeight = this.m_textBuilder.getOrientedHeight();
        final int yOffset = this.m_textBuilder.getTextYOffset();
        final XulorScene scene = Xulor.getInstance().getScene();
        final float scale = scene.getScale();
        final int widgetStartY = (int)((widget.getScreenY() + widget.getAppearance().getBottomInset()) * scale);
        final long charsDelay = this.m_textBuilder.getCharsDelay();
        final int endX = (charsDelay > 0L && this.m_currentTime != -1L) ? this.calculateCurrentDisplayedCharsNumber(charsDelay) : -1;
        if (endX != -1 && endX >= this.m_textBuilder.getTextWidget().getTextBuilder().getFormattedLength() && this.m_currentTime != -1L) {
            this.m_textBuilder.getTextWidget().onTimedCharDisplayedEnd();
            this.m_currentTime = -1L;
        }
        int offset = 0;
    Label_1523:
        for (int i = this.m_textBuilder.getScroll(); i < lineCount; ++i) {
            final LineBlock lineBlock = lineBlocks.get(i);
            final int y = lineBlock.getY() - yOffset + fullHeight;
            if (y + lineBlock.getHeight() <= 0) {
                break;
            }
            if (scissor != null) {
                if (y + widgetStartY >= scissor.getY() + scissor.getHeight()) {
                    continue;
                }
                if (y + lineBlock.getHeight() + widgetStartY <= scissor.getY()) {
                    break;
                }
            }
            float totalPixelsForJustification = 0.0f;
            float pixelsPerSpaceForJustification;
            if (lineBlock.getSpacesCount() > 0) {
                pixelsPerSpaceForJustification = (this.m_textBuilder.getSize().width - lineBlock.getWidth()) / lineBlock.getSpacesCount();
            }
            else {
                pixelsPerSpaceForJustification = 0.0f;
            }
            final ArrayList<AbstractContentBlock> contentBlocks = lineBlock.getContentBlocks();
            for (int j = 0; j < contentBlocks.size(); ++j) {
                final AbstractContentBlock block = contentBlocks.get(j);
                switch (block.getType()) {
                    case IMAGE: {
                        final ImageContentBlock imageBlock = (ImageContentBlock)block;
                        final Pixmap pixmap = imageBlock.getPixmap();
                        if (pixmap == null || pixmap.getTexture() == null) {
                            break;
                        }
                        final int imageX = MathHelper.fastRound(lineBlock.getX() + imageBlock.getX() + totalPixelsForJustification);
                        final int imageWidth = imageBlock.getWidth();
                        final int imageHeight = imageBlock.getImageHeight();
                        final int alignDeltaY = (lineBlock.getHeight() - imageHeight) / 2;
                        if (this.m_modulationColor != null) {
                            gl.glColor4f(this.m_modulationColor.getRed(), this.m_modulationColor.getGreen(), this.m_modulationColor.getBlue(), this.m_modulationColor.getAlpha());
                        }
                        else {
                            gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                        }
                        stateManager.useTexture(renderer, (GLTexture)pixmap.getTexture());
                        stateManager.applyStates(renderer);
                        gl.glBegin(7);
                        gl.glTexCoord2f(pixmap.m_left, pixmap.m_bottom);
                        gl.glVertex2i(imageX, y + alignDeltaY);
                        gl.glTexCoord2f(pixmap.m_right, pixmap.m_bottom);
                        gl.glVertex2i(imageX + imageWidth, y + alignDeltaY);
                        gl.glTexCoord2f(pixmap.m_right, pixmap.m_top);
                        gl.glVertex2i(imageX + imageWidth, y + imageHeight + alignDeltaY);
                        gl.glTexCoord2f(pixmap.m_left, pixmap.m_top);
                        gl.glVertex2i(imageX, y + imageHeight + alignDeltaY);
                        gl.glEnd();
                        if (offset + 1 > endX && endX != -1) {
                            break Label_1523;
                        }
                        ++offset;
                        break;
                    }
                    case TEXT: {
                        final TextContentBlock textBlock = (TextContentBlock)block;
                        boolean partialDisplay = false;
                        if (textBlock.getText().length() + offset > endX && endX != -1) {
                            partialDisplay = true;
                        }
                        TextRenderer textRenderer = textBlock.getTextRenderer();
                        java.awt.Color color = textBlock.getColor();
                        final boolean underline = textBlock.isUnderline();
                        final boolean crossed = textBlock.isCrossed();
                        if (color == null) {
                            color = this.m_textBuilder.getDefaultColor();
                        }
                        if (textRenderer == null) {
                            textRenderer = this.m_textBuilder.getDefaultTextRenderer();
                        }
                        if (textRenderer != null && color != null && textBlock.getWidth() != 0 && textBlock.getHeight() != 0) {
                            final int textStartX = MathHelper.fastRound(lineBlock.getX() + textBlock.getX() + totalPixelsForJustification);
                            totalPixelsForJustification += textBlock.getSpacesCount() * pixelsPerSpaceForJustification;
                            if (this.m_brightenColor) {
                                color = color.brighter().brighter();
                            }
                            if (this.m_darkenColor) {
                                color = color.darker();
                            }
                            float a = color.getAlpha() / 255.0f;
                            float r = color.getRed() / 255.0f;
                            float g = color.getGreen() / 255.0f;
                            float b = color.getBlue() / 255.0f;
                            if (this.m_modulationColor != null) {
                                r *= this.m_modulationColor.getRed();
                                g *= this.m_modulationColor.getGreen();
                                b *= this.m_modulationColor.getBlue();
                                a *= this.m_modulationColor.getAlpha();
                            }
                            textRenderer.begin3DRendering();
                            textRenderer.setColor(r, g, b, a);
                            textRenderer.draw(textBlock.getTextAsCharArray(), textStartX, partialDisplay ? (endX - offset) : textBlock.getText().length(), y + lineBlock.getBaseLine(), 1.0f, pixelsPerSpaceForJustification);
                            textRenderer.end3DRendering();
                            if (underline) {
                                stateManager.enableTextures(false);
                                stateManager.setLineWidth(1.0f);
                                stateManager.applyStates(renderer);
                                gl.glColor4f(r, g, b, a);
                                gl.glBegin(1);
                                gl.glVertex2i(textStartX, y);
                                final int width = MathHelper.fastRound(textBlock.getWidth() + textBlock.getSpacesCount() * pixelsPerSpaceForJustification);
                                gl.glVertex2i(textStartX + width, y);
                                gl.glEnd();
                            }
                            if (crossed) {
                                stateManager.enableTextures(false);
                                stateManager.setLineWidth(1.0f);
                                stateManager.applyStates(renderer);
                                gl.glColor4f(r, g, b, a);
                                gl.glBegin(1);
                                gl.glVertex2i(textStartX, y + lineBlock.getHeight() / 2);
                                final int width = MathHelper.fastRound(textBlock.getWidth() + textBlock.getSpacesCount() * pixelsPerSpaceForJustification);
                                gl.glVertex2i(textStartX + width, y + lineBlock.getHeight() / 2);
                                gl.glEnd();
                            }
                        }
                        offset += textBlock.getText().length();
                        if (partialDisplay) {
                            break Label_1523;
                        }
                        break;
                    }
                }
            }
            final SelectionBlock selectionBlock = lineBlock.getSelectionBlock();
            if (selectionBlock != null) {
                final int selectionX = lineBlock.getX() + selectionBlock.getX();
                final int selectionWidth = selectionBlock.getWidth();
                if (selectionBlock.isCursor()) {
                    if (System.currentTimeMillis() - this.m_lastCursorBlinkTime >= 400L) {
                        this.m_selectionVisible = !this.m_selectionVisible;
                        this.m_lastCursorBlinkTime = System.currentTimeMillis();
                    }
                }
                else {
                    this.m_selectionVisible = true;
                }
                if (this.m_selectionVisible) {
                    stateManager.enableTextures(false);
                    stateManager.setBlendFunc(BlendModes.InvDestColor, BlendModes.Zero);
                    stateManager.applyStates(renderer);
                    gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    gl.glBegin(7);
                    gl.glVertex2i(selectionX, y);
                    gl.glVertex2i(selectionX + selectionWidth, y);
                    gl.glVertex2i(selectionX + selectionWidth, y + lineBlock.getHeight());
                    gl.glVertex2i(selectionX, y + lineBlock.getHeight());
                    gl.glEnd();
                }
            }
        }
        stateManager.reset();
        stateManager.applyStates(renderer);
    }
    
    public void render2(final Renderer renderer) {
        assert renderer.getType() == RendererType.OpenGL;
        final GL gl = renderer.getDevice();
        final RenderStateManager stateManager = RenderStateManager.getInstance();
        final PooledRectangle scissor = Graphics.getInstance().peekScissor();
        final TextWidget widget = this.m_textBuilder.getWidget();
        final ArrayList<LineBlock> lineBlocks = this.m_textBuilder.getLineBlocks();
        final int lineCount = lineBlocks.size();
        final int fullHeight = this.m_textBuilder.getOrientedHeight();
        final int yOffset = this.m_textBuilder.getTextYOffset();
        final XulorScene scene = Xulor.getInstance().getScene();
        final float scale = scene.getScale();
        final int widgetStartY = (int)(widget.getScreenY() * scale);
        final long charsDelay = this.m_textBuilder.getCharsDelay();
        final int endX = (charsDelay > 0L && this.m_currentTime != -1L) ? this.calculateCurrentDisplayedCharsNumber(charsDelay) : -1;
        if (endX != -1 && endX >= this.m_textBuilder.getTextWidget().getTextBuilder().getFormattedLength() && this.m_currentTime != -1L) {
            this.m_textBuilder.getTextWidget().onTimedCharDisplayedEnd();
            this.m_currentTime = -1L;
        }
        int offset = 0;
    Label_1499:
        for (int i = this.m_textBuilder.getScroll(); i < lineCount; ++i) {
            final LineBlock lineBlock = lineBlocks.get(i);
            float totalPixelsForJustification = 0.0f;
            float pixelsPerSpaceForJustification;
            if (lineBlock.getSpacesCount() > 0) {
                pixelsPerSpaceForJustification = (this.m_textBuilder.getSize().width - lineBlock.getWidth()) / lineBlock.getSpacesCount();
            }
            else {
                pixelsPerSpaceForJustification = 0.0f;
            }
            final int y = lineBlock.getY() - yOffset + fullHeight;
            if (y + lineBlock.getHeight() <= 0) {
                break;
            }
            if (scissor != null) {
                if (y + widgetStartY >= scissor.getY() + scissor.getHeight()) {
                    continue;
                }
                if (y + lineBlock.getHeight() + widgetStartY <= scissor.getY()) {
                    break;
                }
            }
            final ArrayList<AbstractContentBlock> contentBlocks = lineBlock.getContentBlocks();
            for (int j = 0; j < contentBlocks.size(); ++j) {
                final AbstractContentBlock block = contentBlocks.get(j);
                switch (block.getType()) {
                    case IMAGE: {
                        final ImageContentBlock imageBlock = (ImageContentBlock)block;
                        final Pixmap pixmap = imageBlock.getPixmap();
                        if (pixmap == null || pixmap.getTexture() == null) {
                            break;
                        }
                        final int imageX = MathHelper.fastRound(lineBlock.getX() + imageBlock.getX() + totalPixelsForJustification);
                        final int imageWidth = imageBlock.getWidth();
                        final int imageHeight = imageBlock.getImageHeight();
                        if (this.m_modulationColor != null) {
                            gl.glColor4f(this.m_modulationColor.getRed(), this.m_modulationColor.getGreen(), this.m_modulationColor.getBlue(), this.m_modulationColor.getAlpha());
                        }
                        else {
                            gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                        }
                        stateManager.useTexture(renderer, (GLTexture)pixmap.getTexture());
                        stateManager.applyStates(renderer);
                        gl.glBegin(7);
                        gl.glTexCoord2f(pixmap.m_left, pixmap.m_bottom);
                        gl.glVertex2i(imageX, y);
                        gl.glTexCoord2f(pixmap.m_right, pixmap.m_bottom);
                        gl.glVertex2i(imageX + imageWidth, y);
                        gl.glTexCoord2f(pixmap.m_right, pixmap.m_top);
                        gl.glVertex2i(imageX + imageWidth, y + imageHeight);
                        gl.glTexCoord2f(pixmap.m_left, pixmap.m_top);
                        gl.glVertex2i(imageX, y + imageHeight);
                        gl.glEnd();
                        if (offset + 1 > endX && endX != -1) {
                            break Label_1499;
                        }
                        ++offset;
                        break;
                    }
                    case TEXT: {
                        final TextContentBlock textBlock = (TextContentBlock)block;
                        boolean partialDisplay = false;
                        if (textBlock.getText().length() + offset > endX && endX != -1) {
                            partialDisplay = true;
                        }
                        TextRenderer textRenderer = textBlock.getTextRenderer();
                        java.awt.Color color = textBlock.getColor();
                        final boolean underline = textBlock.isUnderline();
                        final boolean crossed = textBlock.isCrossed();
                        if (color == null) {
                            color = this.m_textBuilder.getDefaultColor();
                        }
                        if (textRenderer == null) {
                            textRenderer = this.m_textBuilder.getDefaultTextRenderer();
                        }
                        if (textRenderer != null && color != null && textBlock.getWidth() != 0 && textBlock.getHeight() != 0) {
                            final int textStartX = MathHelper.fastRound(lineBlock.getX() + textBlock.getX() + totalPixelsForJustification);
                            totalPixelsForJustification += textBlock.getSpacesCount() * pixelsPerSpaceForJustification;
                            if (textRenderer.isBlured() || this.m_brightenColor) {
                                color = color.brighter().brighter();
                            }
                            if (this.m_darkenColor) {
                                color = color.darker();
                            }
                            float a = color.getAlpha() / 255.0f;
                            float r = color.getRed() / 255.0f;
                            float g = color.getGreen() / 255.0f;
                            float b = color.getBlue() / 255.0f;
                            if (this.m_modulationColor != null) {
                                r *= this.m_modulationColor.getRed();
                                g *= this.m_modulationColor.getGreen();
                                b *= this.m_modulationColor.getBlue();
                                a *= this.m_modulationColor.getAlpha();
                            }
                            textRenderer.begin3DRendering();
                            textRenderer.setColor(r, g, b, a);
                            textRenderer.draw(textBlock.getTextAsCharArray(), textStartX, partialDisplay ? (endX - offset) : textBlock.getText().length(), y + lineBlock.getBaseLine(), 1.0f, pixelsPerSpaceForJustification);
                            textRenderer.end3DRendering();
                            if (underline) {
                                stateManager.enableTextures(false);
                                stateManager.applyStates(renderer);
                                gl.glLineWidth(1.0f);
                                gl.glColor4f(r, g, b, a);
                                gl.glBegin(1);
                                gl.glVertex2i(textStartX, y);
                                final int width = MathHelper.fastRound(textBlock.getWidth() + textBlock.getSpacesCount() * pixelsPerSpaceForJustification);
                                gl.glVertex2i(textStartX + width, y);
                                gl.glEnd();
                            }
                            if (crossed) {
                                stateManager.enableTextures(false);
                                stateManager.applyStates(renderer);
                                gl.glLineWidth(1.0f);
                                gl.glColor4f(r, g, b, a);
                                gl.glBegin(1);
                                gl.glVertex2i(textStartX, y + lineBlock.getHeight() / 2);
                                final int width = MathHelper.fastRound(textBlock.getWidth() + textBlock.getSpacesCount() * pixelsPerSpaceForJustification);
                                gl.glVertex2i(textStartX + width, y + lineBlock.getHeight() / 2);
                                gl.glEnd();
                            }
                        }
                        offset += textBlock.getText().length();
                        if (partialDisplay) {
                            break Label_1499;
                        }
                        break;
                    }
                }
            }
            final SelectionBlock selectionBlock = lineBlock.getSelectionBlock();
            if (selectionBlock != null) {
                final int selectionX = lineBlock.getX() + selectionBlock.getX();
                final int selectionWidth = selectionBlock.getWidth();
                if (selectionBlock.isCursor()) {
                    if (System.currentTimeMillis() - this.m_lastCursorBlinkTime >= 400L) {
                        this.m_selectionVisible = !this.m_selectionVisible;
                        this.m_lastCursorBlinkTime = System.currentTimeMillis();
                    }
                }
                else {
                    this.m_selectionVisible = true;
                }
                if (this.m_selectionVisible) {
                    stateManager.enableTextures(false);
                    stateManager.setBlendFunc(BlendModes.InvDestColor, BlendModes.Zero);
                    stateManager.applyStates(renderer);
                    gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    gl.glBegin(7);
                    gl.glVertex2i(selectionX, y);
                    gl.glVertex2i(selectionX + selectionWidth, y);
                    gl.glVertex2i(selectionX + selectionWidth, y + lineBlock.getHeight());
                    gl.glVertex2i(selectionX, y + lineBlock.getHeight());
                    gl.glEnd();
                }
            }
        }
        stateManager.reset();
        stateManager.applyStates(renderer);
    }
    
    private int calculateCurrentDisplayedCharsNumber(final long charsDelay) {
        final long cur = System.currentTimeMillis();
        if (this.m_currentTime == 0L) {
            this.m_currentTime = cur;
        }
        final long past = cur - this.m_currentTime;
        return (int)(past / charsDelay);
    }
    
    public void resetTextProgressionTimer() {
        this.m_currentTime = 0L;
    }
    
    @Override
    protected void checkout() {
        this.m_currentTime = 0L;
        this.m_lastCursorBlinkTime = 0L;
        this.m_selectionVisible = false;
        this.m_brightenColor = false;
        this.m_darkenColor = false;
    }
    
    @Override
    protected void checkin() {
        this.m_textBuilder = null;
        this.m_modulationColor = null;
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<GLTextGeometry>
    {
        public ObjectFactory() {
            super(GLTextGeometry.class);
        }
        
        @Override
        public GLTextGeometry create() {
            return new GLTextGeometry(null);
        }
    }
}
