package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import java.awt.*;
import java.util.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.util.xmlToJava.*;
import com.ankamagames.xulor2.core.*;

public class InsetsConverter implements Converter<Insets>
{
    private Class<Insets> TEMPLATE;
    
    public InsetsConverter() {
        super();
        this.TEMPLATE = Insets.class;
    }
    
    @Override
    public Insets convert(final String value) {
        return this.convert((Class<? extends Insets>)this.TEMPLATE, value);
    }
    
    @Override
    public Insets convert(final Class<? extends Insets> type, final String value) {
        return this.convert(type, value, (ElementMap)null);
    }
    
    @Override
    public Insets convert(final Class<? extends Insets> type, final String value, final ElementMap map) {
        if (value != null && type.equals(Insets.class)) {
            final StringTokenizer st = new StringTokenizer(value, ",");
            final Insets i = new Insets(0, 0, 0, 0);
            if (st.hasMoreTokens()) {
                i.top = PrimitiveConverter.getInteger(st.nextToken().trim());
            }
            if (st.hasMoreTokens()) {
                i.bottom = PrimitiveConverter.getInteger(st.nextToken().trim());
            }
            if (st.hasMoreTokens()) {
                i.left = PrimitiveConverter.getInteger(st.nextToken().trim());
            }
            if (st.hasMoreTokens()) {
                i.right = PrimitiveConverter.getInteger(st.nextToken().trim());
            }
            return i;
        }
        return null;
    }
    
    @Override
    public Class<Insets> convertsTo() {
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
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<? extends Insets> type, final String attr, final Environment env) {
        final Insets i = this.convert(type, attr);
        doc.addImport(type);
        return "new " + type.getSimpleName() + "(" + i.top + ", " + i.left + ", " + i.bottom + ", " + i.right + ")";
    }
}
