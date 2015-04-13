package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.graphics.image.*;
import java.util.*;
import java.util.regex.*;
import com.ankamagames.xulor2.util.xmlToJava.*;
import com.ankamagames.xulor2.core.*;

public class ColorConverter implements Converter<Color>
{
    private Class<Color> TEMPLATE;
    private static final Pattern COLOR_PATTERN_1;
    private static final Pattern COLOR_PATTERN_2;
    
    public ColorConverter() {
        super();
        this.TEMPLATE = Color.class;
    }
    
    @Override
    public Color convert(final String value) {
        return this.convert((Class<? extends Color>)this.TEMPLATE, value);
    }
    
    @Override
    public Color convert(final Class<? extends Color> type, final String value) {
        return this.convert((Class<? extends Color>)this.TEMPLATE, value, (ElementMap)null);
    }
    
    @Override
    public Color convert(final Class<? extends Color> type, final String value, final ElementMap elementMap) {
        if (value == null || !type.equals(Color.class)) {
            return null;
        }
        if (value.charAt(0) == '#') {
            final String colorString = value.substring(1);
            final Matcher matcher = ColorConverter.COLOR_PATTERN_2.matcher(colorString);
            float r = 0.0f;
            if (matcher.find()) {
                r = Integer.parseInt(matcher.group(), 16) / 255.0f;
            }
            float g = 0.0f;
            if (matcher.find()) {
                g = Integer.parseInt(matcher.group(), 16) / 255.0f;
            }
            float b = 0.0f;
            if (matcher.find()) {
                b = Integer.parseInt(matcher.group(), 16) / 255.0f;
            }
            float a = 1.0f;
            if (matcher.find()) {
                a = Integer.parseInt(matcher.group(), 16) / 255.0f;
            }
            return new Color(r, g, b, a);
        }
        final StringTokenizer st = new StringTokenizer(value, ",");
        float r2 = 0.0f;
        if (st.hasMoreTokens()) {
            r2 = Float.parseFloat(st.nextToken().trim());
        }
        float g2 = 0.0f;
        if (st.hasMoreTokens()) {
            g2 = Float.parseFloat(st.nextToken().trim());
        }
        float b2 = 0.0f;
        if (st.hasMoreTokens()) {
            b2 = Float.parseFloat(st.nextToken().trim());
        }
        float a2 = 1.0f;
        if (st.hasMoreTokens()) {
            a2 = Float.parseFloat(st.nextToken().trim());
        }
        return new Color(r2, g2, b2, a2);
    }
    
    @Override
    public Class<Color> convertsTo() {
        return this.TEMPLATE;
    }
    
    @Override
    public boolean canConvertFromScratch() {
        return true;
    }
    
    @Override
    public boolean canConvertWithoutVariables() {
        return true;
    }
    
    @Override
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<? extends Color> type, final String value, final Environment env) {
        if (value != null && type.equals(Color.class)) {
            float r = 0.0f;
            float g = 0.0f;
            float b = 0.0f;
            float a = 1.0f;
            if (value.charAt(0) == '#') {
                final String colorString = value.substring(1);
                final Matcher matcher = ColorConverter.COLOR_PATTERN_2.matcher(colorString);
                if (matcher.find()) {
                    r = Integer.parseInt(matcher.group(), 16) / 255.0f;
                }
                if (matcher.find()) {
                    g = Integer.parseInt(matcher.group(), 16) / 255.0f;
                }
                if (matcher.find()) {
                    b = Integer.parseInt(matcher.group(), 16) / 255.0f;
                }
                if (matcher.find()) {
                    a = Integer.parseInt(matcher.group(), 16) / 255.0f;
                }
            }
            else {
                final StringTokenizer st = new StringTokenizer(value, ",");
                if (st.hasMoreTokens()) {
                    r = Float.parseFloat(st.nextToken().trim());
                }
                if (st.hasMoreTokens()) {
                    g = Float.parseFloat(st.nextToken().trim());
                }
                if (st.hasMoreTokens()) {
                    b = Float.parseFloat(st.nextToken().trim());
                }
                if (st.hasMoreTokens()) {
                    a = Float.parseFloat(st.nextToken().trim());
                }
            }
            doc.addImport(this.TEMPLATE);
            return "new " + this.TEMPLATE.getName() + "(" + r + "f, " + g + "f, " + b + "f, " + a + "f)";
        }
        return "null";
    }
    
    static {
        COLOR_PATTERN_1 = Pattern.compile("([0-9\\.]+),?");
        COLOR_PATTERN_2 = Pattern.compile("[0-9A-Fa-f][0-9A-Fa-f]");
    }
}
