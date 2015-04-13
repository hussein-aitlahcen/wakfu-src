package com.ankamagames.framework.graphics.engine.text;

import com.ankamagames.framework.graphics.engine.entity.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import java.io.*;

public class EntityText extends Entity
{
    public static final ObjectFactory Factory;
    private static final Matrix44 m_oldCameraMatrix;
    private static final Matrix44 m_worldMatrix;
    protected String m_text;
    protected int m_maxWidth;
    protected int m_minWidth;
    protected int m_minHeight;
    protected int m_textWidth;
    protected int m_textHeight;
    protected final ArrayList<char[]> m_lines;
    protected int m_lineSpace;
    protected int m_offsetX;
    protected int m_offsetY;
    private int m_extraHeightSpace;
    private GeometryText m_textGeometry;
    private GeometryBackground m_backgroundGeometry;
    private boolean m_reformatNeeded;
    private float m_zoom;
    private TextAlignment m_align;
    
    private EntityText() {
        super();
        this.m_zoom = 1.0f;
        this.m_align = TextAlignment.SOUTH;
        this.m_lines = new ArrayList<char[]>(32);
    }
    
    public final void setTextGeometry(final GeometryText geometryText) {
        geometryText.addReference();
        if (this.m_textGeometry != null) {
            this.m_textGeometry.removeReference();
        }
        this.m_textGeometry = geometryText;
    }
    
    public final void setBackgroundGeometry(final GeometryBackground backgroundGeometry) {
        if (backgroundGeometry != null) {
            backgroundGeometry.addReference();
        }
        if (this.m_backgroundGeometry != null) {
            this.m_backgroundGeometry.removeReference();
        }
        this.m_backgroundGeometry = backgroundGeometry;
    }
    
    public final void setBackgoundColor(final float r, final float g, final float b, final float a) {
        if (this.m_backgroundGeometry == null) {
            return;
        }
        this.m_backgroundGeometry.setColor(r, g, b, a);
    }
    
    public final GeometryText getTextGeometry() {
        return this.m_textGeometry;
    }
    
    public final GeometryBackground getBackgroundGeometry() {
        return this.m_backgroundGeometry;
    }
    
    @Override
    public final void update(final float timeIncrement) {
    }
    
    @Override
    public final void renderWithoutEffect(final Renderer renderer) {
        if (!this.isVisible()) {
            return;
        }
        if (this.m_reformatNeeded) {
            this.format(this.m_text);
            this.m_reformatNeeded = false;
        }
        renderer.setWorldMatrix(this.getTransformer().getMatrix());
        this.m_preRenderStates.apply(renderer);
        int offsetX = 0;
        int offsetY = 0;
        RenderStateManager.getInstance().enableTextures(false);
        if (this.m_backgroundGeometry != null) {
            final int width = Math.max(this.m_textWidth, this.m_minWidth);
            final int height = Math.max(this.m_textHeight, this.m_minHeight);
            this.m_backgroundGeometry.setWidth(width);
            this.m_backgroundGeometry.setHeight(height);
            this.m_backgroundGeometry.setXOffset(this.m_offsetX + this.m_align.getX(width));
            this.m_backgroundGeometry.setYOffset(this.m_offsetY + this.m_align.getY(height));
            this.m_backgroundGeometry.render(renderer);
            offsetX += (int)this.m_backgroundGeometry.getLeftMargin();
            offsetY += (int)this.m_backgroundGeometry.getBottomMargin();
        }
        this.m_textGeometry.setOffset(this.m_offsetX, this.m_offsetY);
        this.m_textGeometry.setAlignOffsetX(this.m_align.getX(this.m_textWidth) + offsetX);
        this.m_textGeometry.setAlignOffsetY(this.m_align.getY(this.m_textHeight) + offsetY);
        this.m_textGeometry.setLines(this.m_lines);
        this.m_textGeometry.setLineHeight(this.m_lineSpace);
        this.m_textGeometry.setScale(this.m_zoom);
        this.m_textGeometry.render(renderer);
        this.m_postRenderStates.apply(renderer);
    }
    
    public final void setPosition(final Vector4 position) {
        final TransformerSRT transformer = (TransformerSRT)this.getTransformer().getTransformer(0);
        transformer.setTranslation(position);
        this.getTransformer().setToUpdate();
        this.m_textGeometry.setPosition(position);
    }
    
    public final void setTextOffset(final int offsetX, final int offsetY) {
        this.m_offsetX = offsetX;
        this.m_offsetY = offsetY;
    }
    
    public void setFont(final Font font) {
        this.m_textGeometry.setFont(font);
        this.m_reformatNeeded = true;
    }
    
    public final void setText(final String text) {
        this.m_text = text;
        this.m_reformatNeeded = true;
    }
    
    public final String getText() {
        return this.m_text;
    }
    
    @Override
    public final void setColor(final float r, final float g, final float b, final float a) {
        this.m_textGeometry.setColor(r, g, b, a);
    }
    
    public final Color getColor() {
        return this.m_textGeometry.getColor();
    }
    
    public final void setMaxWidth(int maxWidth) {
        if (maxWidth < 0) {
            maxWidth = Integer.MAX_VALUE;
        }
        this.m_maxWidth = maxWidth;
        this.m_reformatNeeded = true;
    }
    
    public final int getTextWidth() {
        return this.m_textWidth;
    }
    
    public final int getTextHeight() {
        return this.m_textHeight;
    }
    
    public final int getMinWidth() {
        return this.m_minWidth;
    }
    
    public int getMaxWidth() {
        return this.m_maxWidth;
    }
    
    public final void setMinWidth(final int minWidth) {
        this.m_minWidth = minWidth;
    }
    
    public final int getMinHeight() {
        return this.m_minHeight;
    }
    
    public final void setMinHeight(final int minHeight) {
        this.m_minHeight = minHeight;
    }
    
    public float getZoom() {
        return this.m_zoom;
    }
    
    public void setZoom(final float zoom) {
        this.m_zoom = zoom;
    }
    
    @Override
    protected void checkout() {
        super.checkout();
        this.m_maxWidth = Integer.MAX_VALUE;
        this.m_minWidth = 0;
        this.m_minHeight = 0;
        this.m_textWidth = 0;
        this.m_offsetX = 0;
        this.m_offsetY = 0;
        this.m_extraHeightSpace = 2;
        this.m_reformatNeeded = false;
        this.m_zoom = 1.0f;
        this.m_align = TextAlignment.SOUTH;
        final TransformerSRT transformer = new TransformerSRT();
        transformer.setIdentity();
        this.getTransformer().addTransformer(transformer);
    }
    
    @Override
    protected void checkin() {
        super.checkin();
        this.m_lines.clear();
        if (this.m_textGeometry != null) {
            this.m_textGeometry.removeReference();
            this.m_textGeometry = null;
        }
        if (this.m_backgroundGeometry != null) {
            this.m_backgroundGeometry.removeReference();
            this.m_backgroundGeometry = null;
        }
    }
    
    protected void format(final String text) {
        this.m_lines.clear();
        this.m_textWidth = 0;
        this.m_textHeight = 0;
        if (text == null || text.length() == 0) {
            this.m_lineSpace = 0;
            return;
        }
        if (this.m_maxWidth == 0) {
            return;
        }
        StringBuilder lineBuilder = new StringBuilder(128);
        StringBuilder wordBuilder = new StringBuilder(128);
        final char[] charArray = text.toCharArray();
        int offset = 0;
        while (offset < charArray.length) {
            final StringBuilder stringBuilder = new StringBuilder(128);
            Point2i dimension = new Point2i(0, 0);
            boolean lineBreak = false;
            boolean cut = false;
            while (dimension.getX() < this.m_maxWidth && offset < charArray.length && !lineBreak) {
                final char c = charArray[offset++];
                boolean cutAtSpace = true;
                if (offset < charArray.length) {
                    final char c2 = charArray[offset];
                    if (c2 == '.' || c2 == '?' || c2 == '!' || c2 == ':' || c2 == ';' || c2 == ',') {
                        cutAtSpace = false;
                    }
                }
                if (c == ' ' && cutAtSpace) {
                    stringBuilder.append(wordBuilder.toString()).append(' ');
                    wordBuilder = new StringBuilder(128);
                    cut = true;
                }
                else if (c == '\n') {
                    lineBreak = true;
                    stringBuilder.append(wordBuilder.toString());
                    wordBuilder = new StringBuilder(128);
                    cut = true;
                }
                else {
                    wordBuilder.append(c);
                }
                if (!lineBreak) {
                    lineBuilder.append(c);
                    dimension = this.m_textGeometry.getLineDimensions(lineBuilder.toString());
                }
            }
            if (!cut) {
                stringBuilder.append(wordBuilder.toString());
                wordBuilder = new StringBuilder(128);
            }
            if (offset >= charArray.length) {
                stringBuilder.append(wordBuilder.toString());
            }
            lineBuilder = new StringBuilder(128);
            if (wordBuilder.length() != 0) {
                lineBuilder.append((CharSequence)wordBuilder);
            }
            final String lineString = stringBuilder.toString();
            if (lineString.length() < 0) {
                continue;
            }
            dimension = this.m_textGeometry.getLineDimensions(lineString);
            this.m_lines.add(stringBuilder.toString().toCharArray());
            this.m_textWidth = Math.max(this.m_textWidth, dimension.getX());
            this.m_textHeight += dimension.getY();
        }
        this.m_lineSpace = this.m_textHeight / this.m_lines.size() + this.m_extraHeightSpace;
        this.m_textHeight = this.m_lineSpace * this.m_lines.size();
    }
    
    public void setAlign(final TextAlignment align) {
        this.m_align = align;
    }
    
    static {
        Factory = new ObjectFactory();
        m_oldCameraMatrix = Matrix44.Factory.newInstance();
        m_worldMatrix = Matrix44.Factory.newInstance();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<EntityText>
    {
        public ObjectFactory() {
            super(EntityText.class);
        }
        
        @Override
        public EntityText create() {
            return new EntityText(null);
        }
    }
}
