package com.ankamagames.xulor2.component.text.document.part;

import com.ankamagames.xulor2.component.text.document.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.graphics.*;
import java.util.regex.*;
import java.util.*;

public class ImageDocumentPart extends AbstractDocumentPart
{
    private static final String TYPE_NAME = "image";
    private static final String WIDTH_ATTRIBUTE_NAME = "width";
    private static final String HEIGHT_ATTRIBUTE_NAME = "height";
    private static final String PIXMAP_ATTRIBUTE_NAME = "pixmap";
    private static final String POPUP_TRANSLATOR_KEY_ATTRIBUTE_NAME = "popupTranslatorKey";
    private static final Pattern WIDTH_PATTERN;
    private static final Pattern HEIGHT_PATTERN;
    private static final Pattern PIXMAP_PATTERN;
    private static final Pattern POPUP_TRANSLATOR_KEY_PATTERN;
    private String m_pixmapFile;
    private Pixmap m_pixmap;
    private int m_width;
    private int m_height;
    private boolean m_useCustomSize;
    private String m_popupTranslatorKey;
    
    public ImageDocumentPart(final TextDocument document, final AbstractDocumentPart parent) {
        super(document, parent, true);
        this.m_width = 0;
        this.m_height = 0;
        this.m_useCustomSize = false;
        this.setType(DocumentPartType.IMAGE);
    }
    
    public Pixmap getPixmap() {
        return this.m_pixmap;
    }
    
    public void setPixmap(final Pixmap pixmap) {
        if (this.m_pixmap != null) {
            this.m_pixmap.setTexture(null);
        }
        this.m_pixmap = pixmap;
    }
    
    public int getWidth() {
        if (this.m_useCustomSize) {
            return this.m_width;
        }
        if (this.m_pixmap != null) {
            return this.m_pixmap.getWidth();
        }
        return 0;
    }
    
    public void setWidth(final int width) {
        this.m_width = width;
        this.m_useCustomSize = true;
    }
    
    public int getHeight() {
        if (this.m_useCustomSize) {
            return this.m_height;
        }
        if (this.m_pixmap != null) {
            return this.m_pixmap.getHeight();
        }
        return 0;
    }
    
    public void setHeight(final int height) {
        this.m_height = height;
        this.m_useCustomSize = true;
    }
    
    @Override
    public int getLength() {
        return 1;
    }
    
    @Override
    protected void serializeAttributes(final StringBuilder sb) {
        super.serializeAttributes(sb);
        if (this.m_width != 0) {
            AbstractDocumentPart.appendAttribute(sb, "width", this.m_width);
        }
        if (this.m_height != 0) {
            AbstractDocumentPart.appendAttribute(sb, "height", this.m_height);
        }
        if (this.m_pixmapFile != null) {
            AbstractDocumentPart.appendAttribute(sb, "pixmap", this.m_pixmapFile);
        }
    }
    
    @Override
    protected void extractAttributes(final String element, final String attributes) {
        super.extractAttributes(element, attributes);
        if (attributes != null) {
            final Matcher widthMatcher = ImageDocumentPart.WIDTH_PATTERN.matcher(attributes);
            if (widthMatcher.find()) {
                this.setWidth(Integer.valueOf(widthMatcher.group(1)));
            }
            final Matcher heightMatcher = ImageDocumentPart.HEIGHT_PATTERN.matcher(attributes);
            if (heightMatcher.find()) {
                this.setHeight(Integer.valueOf(heightMatcher.group(1)));
            }
            final Matcher pixmapMatcher = ImageDocumentPart.PIXMAP_PATTERN.matcher(attributes);
            if (pixmapMatcher.find()) {
                final String textureFile = pixmapMatcher.group(1);
                final Texture texture = TextureLoader.getInstance().loadTexture(textureFile);
                if (texture != null) {
                    this.setPixmap(new Pixmap(texture));
                    this.m_pixmapFile = textureFile;
                }
            }
            final Matcher popupTranslatorKeyMatcher = ImageDocumentPart.POPUP_TRANSLATOR_KEY_PATTERN.matcher(attributes);
            if (popupTranslatorKeyMatcher.find()) {
                this.m_popupTranslatorKey = popupTranslatorKeyMatcher.group(1);
            }
        }
    }
    
    public String getPopupTranslatorKey() {
        return this.m_popupTranslatorKey;
    }
    
    @Override
    public boolean removeSubPartFromIndex(final int index) {
        return true;
    }
    
    @Override
    public boolean removeSubPartFromToIndices(final int indexFrom, final int indexTo) {
        return true;
    }
    
    @Override
    public boolean removeSubPartToIndex(final int index) {
        return true;
    }
    
    @Override
    public boolean unserialize(final Matcher textDataMatcher, final ArrayList<AbstractDocumentPart> toAddAfter) {
        final int index = toAddAfter.size();
        final boolean success = super.unserialize(textDataMatcher, toAddAfter);
        if (this.m_pixmapFile == null) {
            return false;
        }
        toAddAfter.add(index, this);
        return success;
    }
    
    @Override
    protected String getTypeName() {
        return "image";
    }
    
    @Override
    public String getData() {
        return "";
    }
    
    @Override
    public void clean() {
        if (this.m_pixmap != null) {
            this.m_pixmap.setTexture(null);
        }
    }
    
    static {
        WIDTH_PATTERN = Pattern.compile("width=\"([0-9]+)\"");
        HEIGHT_PATTERN = Pattern.compile("height=\"([0-9]+)\"");
        PIXMAP_PATTERN = Pattern.compile("pixmap=\"(@?[a-zA-Z0-9\\-_/!:.]+)\"");
        POPUP_TRANSLATOR_KEY_PATTERN = Pattern.compile("popupTranslatorKey=\"([a-zA-Z0-9\\-_/!:.]+)\"");
    }
}
