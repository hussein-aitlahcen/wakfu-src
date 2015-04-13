package com.ankamagames.framework.text;

import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.translator.*;
import java.io.*;

public class TextWidgetFormater
{
    protected final StringBuilder m_sb;
    protected boolean m_isTagOpened;
    protected DocumentPartType m_openedTagType;
    protected final ArrayList<String> m_openedTags;
    static long max;
    static long total;
    static long count;
    
    public TextWidgetFormater() {
        super();
        this.m_sb = new StringBuilder(100);
        this.m_openedTagType = DocumentPartType.NONE;
        this.m_openedTags = new ArrayList<String>();
    }
    
    public TextWidgetFormater append(final Object value) {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append(value.toString());
        return this;
    }
    
    public TextWidgetFormater append(final CharSequence value) {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append(value);
        return this;
    }
    
    public TextWidgetFormater append(final CharSequence seq, final int startOffset, final int endOffset) {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append(seq, startOffset, endOffset);
        return this;
    }
    
    public TextWidgetFormater append(final byte value) {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append(this.format(value));
        return this;
    }
    
    public TextWidgetFormater append(final short value) {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append(this.format(value));
        return this;
    }
    
    public TextWidgetFormater append(final char value) {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append(value);
        return this;
    }
    
    public TextWidgetFormater append(final int value) {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append(this.format(value));
        return this;
    }
    
    public TextWidgetFormater append(final long value) {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append(this.format(value));
        return this;
    }
    
    public TextWidgetFormater append(final float value) {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append(this.format(value));
        return this;
    }
    
    public TextWidgetFormater append(final double value) {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append(this.format(value));
        return this;
    }
    
    public TextWidgetFormater append(final boolean value) {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append(value);
        return this;
    }
    
    protected void openTextTag(final String tag) {
        this.m_openedTags.add(tag);
        this.m_sb.append('<').append(tag);
        this.m_isTagOpened = true;
        this.m_openedTagType = DocumentPartType.TEXT;
    }
    
    protected void closeTag(final String tag) {
        this.m_openedTags.remove(this.m_openedTags.size() - 1);
        this.m_sb.append("</").append(tag).append('>');
        this.m_openedTagType = DocumentPartType.NONE;
    }
    
    protected void openTag(final String tag, final DocumentPartType type) {
        this.m_openedTags.add(tag);
        this.m_sb.append('<').append(tag);
        this.m_isTagOpened = true;
        this.m_openedTagType = type;
    }
    
    public TextWidgetFormater b() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.openTextTag("b");
        return this;
    }
    
    public TextWidgetFormater _b() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.closeTag("b");
        return this;
    }
    
    public TextWidgetFormater c() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.openTextTag("c");
        return this;
    }
    
    public TextWidgetFormater _c() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.closeTag("c");
        return this;
    }
    
    public TextWidgetFormater i() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.openTextTag("i");
        return this;
    }
    
    public TextWidgetFormater _i() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.closeTag("i");
        return this;
    }
    
    public TextWidgetFormater u() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.openTextTag("u");
        return this;
    }
    
    public TextWidgetFormater _u() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.closeTag("u");
        return this;
    }
    
    public TextWidgetFormater newLine() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.m_sb.append('\n');
        return this;
    }
    
    public TextWidgetFormater openText() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.openTextTag("text");
        return this;
    }
    
    public TextWidgetFormater closeText() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.closeTag("text");
        return this;
    }
    
    public TextWidgetFormater openImage() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.openTag("image", DocumentPartType.IMAGE);
        return this;
    }
    
    public TextWidgetFormater closeImage() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        this.closeTag("image");
        return this;
    }
    
    public TextWidgetFormater addImage(final String path, final int width, final int height, @Nullable final String align) {
        this.addImage(path, width, height, align, null);
        return this;
    }
    
    public TextWidgetFormater addImage(final String path, final int width, final int height, @Nullable final String align, @Nullable final String translatorKey) {
        this.openImage();
        this.addImageAttribute("pixmap", path);
        if (width > 0) {
            this.addImageAttribute("width", String.valueOf(width));
        }
        if (height > 0) {
            this.addImageAttribute("height", String.valueOf(height));
        }
        if (align != null) {
            this.addImageAttribute("align", align);
        }
        if (translatorKey != null) {
            this.addImageAttribute("popupTranslatorKey", translatorKey);
        }
        this.closeImage();
        return this;
    }
    
    public TextWidgetFormater addCenterAlignment() {
        this.addTextAttribute("align", "center");
        return this;
    }
    
    public TextWidgetFormater addEastAlignment() {
        this.addTextAttribute("align", "east");
        return this;
    }
    
    public TextWidgetFormater addWestAlignment() {
        this.addTextAttribute("align", "west");
        return this;
    }
    
    public TextWidgetFormater addColor(final Color color) {
        return this.addColor(color.getRGBtoHex());
    }
    
    public TextWidgetFormater addColor(final String color) {
        this.addTextAttribute("color", color);
        return this;
    }
    
    public TextWidgetFormater addFontName(final String name) {
        this.addTextAttribute("name", name);
        return this;
    }
    
    public TextWidgetFormater addSize(final int size) {
        this.addTextAttribute("size", String.valueOf(size));
        return this;
    }
    
    public TextWidgetFormater addId(final String id) {
        this.addTextAttribute("id", id);
        return this;
    }
    
    public TextWidgetFormater addImageWidth(final int width) {
        this.addTextAttribute("width", String.valueOf(width));
        return this;
    }
    
    public TextWidgetFormater addImageHeight(final int height) {
        this.addTextAttribute("height", String.valueOf(height));
        return this;
    }
    
    public TextWidgetFormater addImagePath(final String path) {
        this.addTextAttribute("pixmap", path);
        return this;
    }
    
    @Override
    public String toString() {
        return this.m_sb.toString();
    }
    
    public int length() {
        return this.m_sb.length();
    }
    
    public String finishAndToString() {
        if (!this.m_openedTags.isEmpty()) {
            this.closeAllOpenedTags();
        }
        if (this.m_sb.length() > TextWidgetFormater.max) {
            TextWidgetFormater.max = this.m_sb.length();
        }
        ++TextWidgetFormater.count;
        TextWidgetFormater.total += this.m_sb.length();
        return this.m_sb.toString();
    }
    
    public static boolean containsTextTag(final String content) {
        return content.contains("<text");
    }
    
    public static boolean containsColorTag(final String content) {
        return content.contains("color=");
    }
    
    protected void addTextAttribute(final String attr, final String value) {
        if (this.m_openedTagType != DocumentPartType.TEXT) {
            this.openText();
        }
        this.m_sb.append(' ').append(attr).append("=\"").append(value).append('\"');
    }
    
    protected void addImageAttribute(final String attr, final String value) {
        if (this.m_openedTagType != DocumentPartType.IMAGE) {
            this.openImage();
        }
        this.m_sb.append(' ').append(attr).append("=\"").append(value).append('\"');
    }
    
    protected void closeAllOpenedTags() {
        if (this.m_isTagOpened) {
            this.closeCurrentTag();
        }
        while (!this.m_openedTags.isEmpty()) {
            this.m_sb.append("</").append(this.m_openedTags.remove(this.m_openedTags.size() - 1)).append('>');
        }
    }
    
    protected void closeCurrentTag() {
        assert this.m_isTagOpened;
        this.m_sb.append('>');
        this.m_isTagOpened = false;
    }
    
    public void clear() {
        this.m_sb.delete(0, this.m_sb.length());
    }
    
    public boolean hasOpennedTag() {
        return this.m_isTagOpened;
    }
    
    private String format(final long number) {
        if (Translator.getInstance() == null) {
            return String.valueOf(number);
        }
        return Translator.getInstance().formatNumber(number);
    }
    
    private String format(final double number) {
        if (Translator.getInstance() == null) {
            return String.valueOf(number);
        }
        return Translator.getInstance().formatNumber(number);
    }
}
