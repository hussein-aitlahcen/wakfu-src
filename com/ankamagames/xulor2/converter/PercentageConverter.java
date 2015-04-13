package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.util.xmlToJava.*;
import com.ankamagames.xulor2.core.*;

public final class PercentageConverter implements Converter<Percentage>
{
    public static final Class<Percentage> TEMPLATE;
    
    @Override
    public Percentage convert(final String value) {
        return this.convert((Class<? extends Percentage>)PercentageConverter.TEMPLATE, value);
    }
    
    @Override
    public Percentage convert(final Class<? extends Percentage> type, final String value) {
        return this.convert(type, value, (ElementMap)null);
    }
    
    @Override
    public Percentage convert(final Class<? extends Percentage> type, final String value, final ElementMap map) {
        if (value != null) {
            return Percentage.valueOf(value);
        }
        return null;
    }
    
    @Override
    public Class<Percentage> convertsTo() {
        return PercentageConverter.TEMPLATE;
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
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<? extends Percentage> type, final String value, final Environment env) {
        if (value != null) {
            doc.addImport(PercentageConverter.TEMPLATE);
            final StringBuilder sb = new StringBuilder();
            final Percentage p = Percentage.valueOf(value);
            final double pValue = p.getValue();
            sb.append("new Percentage(").append(pValue).append(")");
            return sb.toString();
        }
        return "null";
    }
    
    static {
        TEMPLATE = Percentage.class;
    }
}
