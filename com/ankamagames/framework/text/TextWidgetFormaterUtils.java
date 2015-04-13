package com.ankamagames.framework.text;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.html.parser.datasource.impl.*;
import com.ankamagames.framework.fileFormat.html.parser.lexer.impl.*;
import com.ankamagames.framework.fileFormat.html.parser.datasource.*;
import org.apache.commons.lang3.*;
import com.ankamagames.framework.fileFormat.html.parser.lexer.*;
import com.ankamagames.framework.fileFormat.html.parser.*;
import java.util.*;

public class TextWidgetFormaterUtils
{
    public static final Logger m_logger;
    
    public static TextWidgetFormater fromHtml(@NotNull final String html) {
        final TextWidgetFormater formater = new TextWidgetFormater();
        final Lexer htmlLexer = new LexerImpl(new PSstringBuffer(new StringBuilder(html)));
        while (true) {
            final Token nextToken = htmlLexer.getNextToken();
            if (nextToken == null) {
                break;
            }
            if (nextToken instanceof TokenText) {
                final String valueText = ((TokenText)nextToken).getValueText();
                String unicodeText = StringEscapeUtils.unescapeHtml4(valueText);
                unicodeText = StringUtils.replaceChars(unicodeText, '\u2019', '\'');
                formater.append(unicodeText);
            }
            else {
                if (!(nextToken instanceof TokenTag)) {
                    continue;
                }
                final TokenTag tag = (TokenTag)nextToken;
                final String tagName = tag.getTagName();
                if ("b".equalsIgnoreCase(tagName) || "strong".equalsIgnoreCase(tagName)) {
                    if (tag.isClosedTag()) {
                        formater._b();
                    }
                    else {
                        formater.b();
                    }
                }
                else if ("i".equalsIgnoreCase(tagName)) {
                    if (tag.isClosedTag()) {
                        formater._i();
                    }
                    else {
                        formater.i();
                    }
                }
                else if ("u".equalsIgnoreCase(tagName)) {
                    if (tag.isClosedTag()) {
                        formater._u();
                    }
                    else {
                        formater.u();
                    }
                }
                else if ("s".equalsIgnoreCase(tagName)) {
                    if (tag.isClosedTag()) {
                        formater._c();
                    }
                    else {
                        formater.c();
                    }
                }
                else if ("font".equalsIgnoreCase(tagName)) {
                    if (tag.isClosedTag()) {
                        formater.closeText();
                    }
                    else {
                        boolean atLeastOne = false;
                        for (final TagAttribute attribute : tag.getAttrs()) {
                            final String attributeName = attribute.getAttrName();
                            if ("size".equalsIgnoreCase(attributeName)) {
                                formater.addSize(Integer.parseInt(attribute.getAttrValue()));
                                atLeastOne = true;
                            }
                            else if ("color".equalsIgnoreCase(attributeName)) {
                                final String color = formatColor(attribute.getAttrValue());
                                if (color == null) {
                                    continue;
                                }
                                formater.addColor(color);
                                atLeastOne = true;
                            }
                            else {
                                if (!"face".equalsIgnoreCase(attributeName)) {
                                    continue;
                                }
                                formater.addFontName(attribute.getAttrValue());
                                atLeastOne = true;
                            }
                        }
                        if (atLeastOne) {
                            continue;
                        }
                        formater.openText();
                    }
                }
                else if ("br".equalsIgnoreCase(tagName)) {
                    formater.append('\n');
                }
                else if ("p".equalsIgnoreCase(tagName)) {
                    formater.append('\n');
                }
                else {
                    if (!"span".equalsIgnoreCase(tagName)) {
                        continue;
                    }
                    if (tag.isClosedTag()) {
                        formater.closeText();
                    }
                    else {
                        formater.openText();
                        for (final TagAttribute attribute2 : tag.getAttrs()) {
                            final String attributeName2 = attribute2.getAttrName();
                            if ("style".equalsIgnoreCase(attributeName2)) {
                                final HashMap<String, String> properties = parseCssProperties(attribute2.getAttrValue());
                                for (final String propertyName : properties.keySet()) {
                                    if ("color".equalsIgnoreCase(propertyName)) {
                                        final String color2 = formatColor(properties.get(propertyName));
                                        if (color2 == null) {
                                            continue;
                                        }
                                        formater.addColor(color2);
                                    }
                                    else if ("font-family".equalsIgnoreCase(propertyName)) {
                                        final String fonts = properties.get(propertyName);
                                        String font;
                                        if (fonts.contains(",")) {
                                            font = StringUtils.split(fonts, ',')[0].trim();
                                        }
                                        else {
                                            font = fonts;
                                        }
                                        formater.addFontName(font);
                                    }
                                    else if ("font-size".equalsIgnoreCase(propertyName)) {
                                        final String sizeString = properties.get(propertyName);
                                        if (!sizeString.endsWith("px")) {
                                            continue;
                                        }
                                        try {
                                            final int size = Integer.parseInt(sizeString.substring(0, sizeString.length() - 2));
                                            formater.addSize(size);
                                        }
                                        catch (NumberFormatException e) {
                                            TextWidgetFormaterUtils.m_logger.error((Object)("Error while reading font size : invalid size " + sizeString));
                                        }
                                    }
                                    else {
                                        if (!"text-align".equalsIgnoreCase(propertyName)) {
                                            continue;
                                        }
                                        final String orientation = properties.get(propertyName);
                                        if ("center".equalsIgnoreCase(orientation)) {
                                            formater.addCenterAlignment();
                                        }
                                        else if ("right".equalsIgnoreCase(orientation)) {
                                            formater.addEastAlignment();
                                        }
                                        else {
                                            if (!"left".equalsIgnoreCase(orientation)) {
                                                continue;
                                            }
                                            formater.addWestAlignment();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return formater;
    }
    
    private static String formatColor(String color) {
        if (color == null) {
            return null;
        }
        if (color.startsWith("#")) {
            color = color.substring(1);
        }
        if (color.length() < 6) {
            return null;
        }
        return color.substring(0, 6);
    }
    
    private static HashMap<String, String> parseCssProperties(final String css) {
        final HashMap<String, String> values = new HashMap<String, String>();
        final String[] arr$;
        final String[] strings = arr$ = StringUtils.split(css, ';');
        for (final String property : arr$) {
            final String[] propertyParts = StringUtils.split(property, ':');
            if (propertyParts.length < 2) {
                TextWidgetFormaterUtils.m_logger.error((Object)("Invalid css property : '" + property + "' in css string : '" + css + "'"));
            }
            else {
                final String propertyName = propertyParts[0].trim();
                final String propertyValue = propertyParts[1].trim();
                values.put(propertyName, propertyValue);
            }
        }
        return values;
    }
    
    public static void main(final String[] args) {
        final char c = '\u2019';
        final char d = '\u2019';
        final char e = '\u0146';
        System.out.println(true);
        System.out.println(false);
    }
    
    static {
        m_logger = Logger.getLogger((Class)TextWidgetFormaterUtils.class);
    }
}
