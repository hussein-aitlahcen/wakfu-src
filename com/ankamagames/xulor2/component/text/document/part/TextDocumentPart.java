package com.ankamagames.xulor2.component.text.document.part;

import org.apache.log4j.*;
import java.awt.*;
import com.ankamagames.xulor2.component.text.document.*;
import com.ankamagames.framework.text.*;
import java.util.regex.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.text.*;

public class TextDocumentPart extends AbstractDocumentPart
{
    private static Logger m_logger;
    private static final String TYPE_NAME = "text";
    private static final String FONT_ELEMENT_NAME = "font";
    private static final String UNDERLINE_ELEMENT_NAME = "u";
    private static final String BOLD_ELEMENT_NAME = "b";
    private static final String ITALIC_ELEMENT_NAME = "i";
    private static final String CROSSED_ELEMENT_NAME = "c";
    private static final String FONT_ATTRIBUTE_NAME_1 = "name";
    private static final String FONT_ATTRIBUTE_NAME = "(name|face)";
    private static final String COLOR_ATTRIBUTE_NAME = "color";
    private static final String ID_ATTRIBUTE_NAME = "id";
    private static final Pattern FONT_PATTERN;
    private static final Pattern SIZE_PATTERN;
    private static final Pattern COLOR_PATTERN;
    private static final Pattern ID_PATTERN;
    private String m_id;
    private TextRenderer m_textRenderer;
    private boolean m_bold;
    private boolean m_italic;
    private Color m_color;
    private boolean m_underline;
    private boolean m_crossed;
    
    public TextDocumentPart(final TextDocument document, final AbstractDocumentPart parent, final boolean selectable) {
        super(document, parent, selectable);
        this.m_id = null;
        this.m_bold = false;
        this.m_italic = false;
        this.m_underline = false;
        this.m_crossed = false;
        if (!selectable) {
            this.setType(DocumentPartType.TEXT);
        }
    }
    
    @Override
    public void setData(final String data) {
        super.setData(this.getRestrictedText(data));
    }
    
    public int setTextAt(final String text, final int index) {
        final String restrictedText = this.getRestrictedText(text);
        final String data = this.getText();
        final String textBefore = (data == null) ? "" : data.substring(0, index);
        final String textAfter = (data == null) ? "" : data.substring(index);
        super.setData(textBefore + restrictedText + textAfter);
        return restrictedText.length();
    }
    
    public void setText(final String text) {
        this.setData(text);
    }
    
    public String getText() {
        return this.getData();
    }
    
    public String getDisplayedText() {
        if (this.getDocument().isPassword()) {
            return this.getText().replaceAll(".", "*");
        }
        return this.getText();
    }
    
    public TextRenderer getTextRenderer() {
        return this.m_textRenderer;
    }
    
    public void setTextRenderer(final TextRenderer textRenderer) {
        this.m_textRenderer = textRenderer;
    }
    
    public Color getColor() {
        return this.m_color;
    }
    
    public void setColor(final Color color) {
        if (color != null) {
            this.m_color = color;
        }
    }
    
    public boolean isUnderline() {
        return this.m_underline;
    }
    
    public void setUnderline(final boolean underline) {
        this.m_underline = underline;
    }
    
    public boolean isCrossed() {
        return this.m_crossed;
    }
    
    public void setCrossed(final boolean crossed) {
        this.m_crossed = crossed;
    }
    
    public String getId() {
        return this.m_id;
    }
    
    public void setId(final String id) {
        this.m_id = id;
    }
    
    @Override
    protected String getTypeName() {
        return "text";
    }
    
    @Override
    public int getLength() {
        return this.getText().length();
    }
    
    @Override
    public boolean unserialize(final Matcher textDataMatcher, final ArrayList<AbstractDocumentPart> toAddAfter) {
        final int index = toAddAfter.size();
        final boolean success = super.unserialize(textDataMatcher, toAddAfter);
        if (success && (this.getData() == null || this.getData().length() == 0)) {
            return false;
        }
        toAddAfter.add(index, this);
        return success;
    }
    
    public String getRestrictedText(final String text) {
        final Pattern restrictPattern = this.getDocument().getRestrictPattern();
        if (restrictPattern == null) {
            return text;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            final String c = Character.toString(text.charAt(i));
            if (restrictPattern.matcher(c).matches()) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    @Override
    protected void serializeAttributes(final StringBuilder sb) {
        super.serializeAttributes(sb);
        if (this.m_textRenderer != null) {
            final String fontName = this.m_textRenderer.getFontName();
            if (fontName != null) {
                AbstractDocumentPart.appendAttribute(sb, "name", fontName);
            }
        }
        if (this.m_color != null) {
            AbstractDocumentPart.appendAttribute(sb, "color", this.m_color);
        }
        if (this.m_id != null) {
            AbstractDocumentPart.appendAttribute(sb, "id", this.m_id);
        }
        if (this.m_underline) {
            AbstractDocumentPart.appendAttribute(sb, "u", this.m_underline);
        }
        if (this.m_crossed) {
            AbstractDocumentPart.appendAttribute(sb, "c", this.m_crossed);
        }
    }
    
    @Override
    protected void extractAttributes(final String element, final String attributes) {
        super.extractAttributes(element, attributes);
        int newStyle = 0;
        final TextDocumentPart parent = (TextDocumentPart)this.getParentOfType(DocumentPartType.TEXT);
        if (parent != null) {
            this.m_bold = parent.m_bold;
            this.m_italic = parent.m_italic;
            this.m_underline = parent.m_underline;
            this.m_crossed = parent.m_crossed;
            this.m_color = parent.m_color;
            this.m_textRenderer = parent.m_textRenderer;
            this.m_id = parent.m_id;
            if (this.m_bold) {
                newStyle |= 0x1;
            }
            if (this.m_italic) {
                newStyle |= 0x2;
            }
        }
        Font font = null;
        if (this.getDocument().getDefaultTextRenderer() != null) {
            font = this.getDocument().getDefaultTextRenderer().getFont();
        }
        if (this.m_textRenderer != null) {
            font = this.m_textRenderer.getFont();
        }
        boolean updateFont = false;
        if (attributes != null) {
            final Matcher fontMatcher = TextDocumentPart.FONT_PATTERN.matcher(attributes);
            if (fontMatcher.find()) {
                final String fontName = fontMatcher.group(2);
                font = FontFactory.createFont(fontName, this.getDocument().isEnableAWTFont());
                updateFont = true;
            }
        }
        float newSize = 0.0f;
        boolean updateSize = false;
        if (attributes != null) {
            final Matcher sizeMatcher = TextDocumentPart.SIZE_PATTERN.matcher(attributes);
            if (font != null && sizeMatcher.find()) {
                newSize = Float.parseFloat(sizeMatcher.group(1));
                updateSize = true;
            }
        }
        if (font != null) {
            if (attributes != null) {
                final Matcher colorMatcher = TextDocumentPart.COLOR_PATTERN.matcher(attributes);
                if (colorMatcher.find()) {
                    try {
                        final int colorValue = Integer.valueOf(colorMatcher.group(1), 16);
                        this.setColor(new Color(colorValue));
                    }
                    catch (NumberFormatException e) {
                        TextDocumentPart.m_logger.warn((Object)("la couleur " + colorMatcher.group(1) + " est invalide !"));
                    }
                }
            }
            newStyle = font.getStyle();
            boolean updateStyle = false;
            if ("b".equalsIgnoreCase(element)) {
                this.m_bold = true;
                updateStyle = true;
            }
            if ("i".equalsIgnoreCase(element)) {
                this.m_italic = true;
                updateStyle = true;
            }
            if (updateStyle) {
                if (this.m_bold) {
                    newStyle |= 0x1;
                }
                if (this.m_italic) {
                    newStyle |= 0x2;
                }
            }
            if (!updateSize) {
                newSize = font.getSize();
            }
            if (updateSize || updateStyle) {
                font = font.createDerivateFont(newStyle, newSize, this.getDocument().isEnableAWTFont());
                updateFont = true;
            }
            if (updateFont) {
                this.setTextRenderer(TexturedFontRendererFactory.createTextRenderer(font));
            }
        }
        if ("u".equals(element)) {
            this.setUnderline(true);
        }
        if ("c".equals(element)) {
            this.setCrossed(true);
        }
        if (attributes != null) {
            final Matcher idMatcher = TextDocumentPart.ID_PATTERN.matcher(attributes);
            if (idMatcher.find()) {
                this.setId(String.valueOf(idMatcher.group(1)));
            }
        }
    }
    
    @Override
    public boolean removeSubPartFromIndex(final int index) {
        final String text = this.getText();
        if (text.length() <= index) {
            return false;
        }
        this.setText(text.substring(0, index));
        return this.getText().length() == 0;
    }
    
    @Override
    public boolean removeSubPartFromToIndices(final int indexFrom, final int indexTo) {
        final String text = this.getText();
        if (text == null) {
            return false;
        }
        if (indexFrom < 0 || indexFrom > text.length() || indexTo < 0 || indexTo > text.length()) {
            return false;
        }
        this.setText(text.substring(0, indexFrom) + text.substring(indexTo));
        return this.getText().length() == 0;
    }
    
    @Override
    public boolean removeSubPartToIndex(final int index) {
        this.setText(this.getText().substring(index));
        return this.getText().length() == 0;
    }
    
    @Override
    public void clean() {
    }
    
    static {
        TextDocumentPart.m_logger = Logger.getLogger((Class)TextDocumentPart.class);
        FONT_PATTERN = Pattern.compile("(name|face)=\"([a-zA-Z 0-9-]+)\"");
        SIZE_PATTERN = Pattern.compile("size=\"([0-9]+)\"");
        COLOR_PATTERN = Pattern.compile("color=\"([0-9A-Fa-f]{6})\"");
        ID_PATTERN = Pattern.compile("id=\"(([^,]+)|([^,]+\\-[^,]+))\"");
    }
}
