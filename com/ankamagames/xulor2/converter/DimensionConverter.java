package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.util.*;
import java.util.*;
import com.ankamagames.xulor2.util.xmlToJava.*;
import com.ankamagames.xulor2.core.*;

public final class DimensionConverter implements Converter<Dimension>
{
    public static final Class<Dimension> TEMPLATE;
    
    @Override
    public Dimension convert(final String value) {
        return this.convert((Class<? extends Dimension>)DimensionConverter.TEMPLATE, value);
    }
    
    @Override
    public Dimension convert(final Class<? extends Dimension> type, final String value) {
        return this.convert(type, value, (ElementMap)null);
    }
    
    @Override
    public Dimension convert(final Class<? extends Dimension> type, final String value, final ElementMap elementMap) {
        if (value != null) {
            final StringTokenizer st = new StringTokenizer(value, ",");
            final Dimension dim = new Dimension();
            if (st.hasMoreTokens()) {
                final String width = st.nextToken().trim();
                if (width.endsWith("%")) {
                    dim.setWidthPercentage(Float.parseFloat(width.substring(0, width.length() - 1)));
                }
                else {
                    dim.setWidth(Integer.parseInt(width));
                }
            }
            if (st.hasMoreTokens()) {
                final String height = st.nextToken().trim();
                if (height.endsWith("%")) {
                    dim.setHeightPercentage(Float.parseFloat(height.substring(0, height.length() - 1)));
                }
                else {
                    dim.setHeight(Integer.parseInt(height));
                }
            }
            return dim;
        }
        return null;
    }
    
    @Override
    public Class<Dimension> convertsTo() {
        return DimensionConverter.TEMPLATE;
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
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<? extends Dimension> type, final String value, final Environment env) {
        if (value != null) {
            doc.addImport(DimensionConverter.TEMPLATE);
            final StringTokenizer st = new StringTokenizer(value, ",");
            final StringBuilder sb = new StringBuilder();
            sb.append("new Dimension(");
            if (st.hasMoreTokens()) {
                final String width = st.nextToken().trim();
                if (width.endsWith("%")) {
                    sb.append(width.substring(0, width.length() - 1)).append("f");
                }
                else {
                    sb.append(width);
                }
            }
            else {
                sb.append(0);
            }
            sb.append(", ");
            if (st.hasMoreTokens()) {
                final String height = st.nextToken().trim();
                if (height.endsWith("%")) {
                    sb.append(height.substring(0, height.length() - 1)).append("f");
                }
                else {
                    sb.append(height);
                }
            }
            else {
                sb.append(0);
            }
            sb.append(")");
            return sb.toString();
        }
        return "null";
    }
    
    static {
        TEMPLATE = Dimension.class;
    }
}
