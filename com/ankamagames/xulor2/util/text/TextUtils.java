package com.ankamagames.xulor2.util.text;

public class TextUtils
{
    public static String idAttr(final String id) {
        return "id=\"" + id + "\"";
    }
    
    public static String colorAttr(final String color) {
        return "color=\"" + color + "\"";
    }
    
    public static String sizeAttr(final int color) {
        return "size=\"" + color + "\"";
    }
    
    public static String alignAttr(final String align) {
        return "align=\"" + align + "\"";
    }
    
    public static String toBold(final String value, final String... attrs) {
        return toTag(value, "b", attrs);
    }
    
    public static String toItalic(final String value, final String... attrs) {
        return toTag(value, "i", attrs);
    }
    
    public static String toUnderline(final String value, final String... attrs) {
        return toTag(value, "u", attrs);
    }
    
    public static String getImageTag(final String path, final int width, final int height, final String align) {
        return getImageTag(path, width, height, align, null);
    }
    
    public static String getImageTag(final String path, final int width, final int height, final String align, final String translatorKey) {
        final StringBuilder sb = new StringBuilder("<image pixmap=\"");
        sb.append(path).append("\"");
        if (width > 0) {
            sb.append(" width=\"").append(width).append("\"");
        }
        if (height > 0) {
            sb.append(" height=\"").append(height).append("\"");
        }
        if (align != null) {
            sb.append(" align=\"").append(align).append("\"");
        }
        if (translatorKey != null) {
            sb.append(" popupTranslatorKey=\"").append(translatorKey).append("\"");
        }
        sb.append("></image>");
        return sb.toString();
    }
    
    public static String toTextTag(final String value, final String... attrs) {
        return toTag(value, "text", attrs);
    }
    
    private static String toTag(final String value, final String tag, final String... attrs) {
        final StringBuilder sb = new StringBuilder("<").append(tag);
        if (attrs != null) {
            for (final String attr : attrs) {
                sb.append(" ").append(attr);
            }
        }
        return sb.append(">").append(value).append("</").append(tag).append(">").toString();
    }
}
