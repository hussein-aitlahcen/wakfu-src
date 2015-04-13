package com.ankamagames.xulor2.component.text.builder.content;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.text.*;
import java.awt.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.component.text.document.part.*;

public class TextContentBlock extends AbstractContentBlock
{
    private static Logger m_logger;
    private String m_text;
    private char[] m_textAsCharArray;
    private int m_spacesCount;
    private TextRenderer m_textRenderer;
    private BluredTextRenderer m_blurTextRenderer;
    private Color m_color;
    private boolean m_underline;
    private boolean m_crossed;
    private boolean m_useDocumentPartUnderline;
    private boolean m_useDocumentPartCrossed;
    
    public TextContentBlock() {
        super();
        this.m_textRenderer = null;
        this.m_blurTextRenderer = null;
        this.m_color = null;
        this.m_underline = false;
        this.m_crossed = false;
        this.m_useDocumentPartUnderline = true;
        this.m_useDocumentPartCrossed = true;
        this.setType(BlockType.TEXT);
    }
    
    @Override
    public int getLength() {
        return this.m_text.length();
    }
    
    public String getText() {
        return this.m_text;
    }
    
    public char[] getTextAsCharArray() {
        return this.m_textAsCharArray;
    }
    
    public void setText(String text) {
        if (text != null && text.startsWith("\n")) {
            text = text.substring(1);
        }
        if ((this.m_text = text) != null) {
            this.m_textAsCharArray = this.m_text.toCharArray();
        }
        else {
            this.m_textAsCharArray = null;
        }
    }
    
    @Override
    public TextDocumentPart getDocumentPart() {
        return (TextDocumentPart)this.m_documentPart;
    }
    
    public TextRenderer getTextRenderer() {
        if (this.m_textRenderer == null && this.getDocumentPart() != null) {
            return this.getDocumentPart().getTextRenderer();
        }
        return this.m_textRenderer;
    }
    
    public void setTextRenderer(final TextRenderer textRenderer) {
        this.m_textRenderer = textRenderer;
    }
    
    public BluredTextRenderer getBlurTextRenderer() {
        return this.m_blurTextRenderer;
    }
    
    public void setBlurTextRenderer(final BluredTextRenderer textRenderer) {
        this.m_blurTextRenderer = textRenderer;
    }
    
    public int getSpacesCount() {
        return this.m_spacesCount;
    }
    
    public void setSpacesCount(final int spacesCount) {
        this.m_spacesCount = spacesCount;
    }
    
    public Color getColor() {
        if (this.m_color == null && this.getDocumentPart() != null) {
            return this.getDocumentPart().getColor();
        }
        return this.m_color;
    }
    
    public void setColor(final Color color) {
        this.m_color = color;
    }
    
    public boolean isUnderline() {
        if (this.m_useDocumentPartUnderline && this.getDocumentPart() != null) {
            return this.getDocumentPart().isUnderline();
        }
        return this.m_underline;
    }
    
    public void setUnderline(final boolean underline) {
        this.m_underline = underline;
        this.m_useDocumentPartUnderline = false;
    }
    
    public boolean isCrossed() {
        if (this.m_useDocumentPartCrossed && this.getDocumentPart() != null) {
            return this.getDocumentPart().isCrossed();
        }
        return this.m_crossed;
    }
    
    public void setCrossed(final boolean crossed) {
        this.m_crossed = crossed;
        this.m_useDocumentPartCrossed = false;
    }
    
    @Override
    public int getContentBlockPartIndexFromCoordinates(final TextRenderer defaultTextRenderer, final int x) {
        double currentX = 0.0;
        TextRenderer textRenderer = this.getTextRenderer();
        if (textRenderer == null && defaultTextRenderer != null) {
            textRenderer = defaultTextRenderer;
        }
        if (textRenderer != null) {
            final String text = this.m_text;
            for (int characterIndex = 0; characterIndex < text.length(); ++characterIndex) {
                final char character = text.charAt(characterIndex);
                final int characterWidth = textRenderer.getCharacterWidth(character);
                if (currentX + characterWidth >= x) {
                    return characterIndex;
                }
                currentX += characterWidth;
            }
        }
        return -1;
    }
    
    @Override
    public int getContentBlockPartLeftFromIndex(final TextRenderer defaultTextRenderer, final int index) {
        final String subString = this.m_text.substring(0, MathHelper.clamp(index, 0, this.m_text.length()));
        TextRenderer textRenderer = this.getTextRenderer();
        if (textRenderer == null && defaultTextRenderer != null) {
            textRenderer = defaultTextRenderer;
        }
        if (textRenderer != null) {
            return textRenderer.getLineWidth(subString);
        }
        return 0;
    }
    
    @Override
    public int getContentBlockPartRightFromIndex(final TextRenderer defaultTextRenderer, final int index) {
        final String subString = this.m_text.substring(0, index);
        TextRenderer textRenderer = this.getTextRenderer();
        if (textRenderer == null && defaultTextRenderer != null) {
            textRenderer = defaultTextRenderer;
        }
        if (textRenderer != null) {
            return textRenderer.getLineWidth(subString);
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return this.m_text;
    }
    
    static {
        TextContentBlock.m_logger = Logger.getLogger((Class)TextContentBlock.class);
    }
}
