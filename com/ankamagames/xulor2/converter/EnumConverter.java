package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.util.xmlToJava.*;
import com.ankamagames.xulor2.core.*;

public class EnumConverter implements Converter<Enum>
{
    private Class<Enum> TEMPLATE;
    
    public EnumConverter() {
        super();
        this.TEMPLATE = Enum.class;
    }
    
    @Override
    public Enum convert(final String value) {
        return null;
    }
    
    @Override
    public Enum convert(final Class<? extends Enum> type, final String value) {
        return this.convert(type, value, (ElementMap)null);
    }
    
    @Override
    public Enum convert(final Class<? extends Enum> type, final String value, final ElementMap map) {
        Object o = null;
        try {
            o = Enum.valueOf((Class<Object>)type, value.toUpperCase());
        }
        catch (IllegalArgumentException ex) {}
        if (o != null) {
            return (Enum)o;
        }
        if (((Enum[])type.getEnumConstants()).length > 0) {
            return ((Enum[])type.getEnumConstants())[0];
        }
        return null;
    }
    
    @Override
    public Class<Enum> convertsTo() {
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
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<? extends Enum> type, final String value, final Environment env) {
        doc.addImport(type);
        Object o = null;
        try {
            o = Enum.valueOf((Class<Object>)type, value.toUpperCase());
        }
        catch (IllegalArgumentException ex) {}
        if (o == null && ((Enum[])type.getEnumConstants()).length > 0) {
            o = ((Enum[])type.getEnumConstants())[0];
        }
        final Enum o2 = (Enum)o;
        return type.getSimpleName() + "." + o2.toString();
    }
}
