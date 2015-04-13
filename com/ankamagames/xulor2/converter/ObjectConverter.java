package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.util.xmlToJava.*;
import com.ankamagames.xulor2.core.*;

public class ObjectConverter implements Converter<Object>
{
    protected static final Logger m_logger;
    public static final Class<Object> TEMPLATE;
    
    @Override
    public String convert(final String value) {
        return this.convert((Class<?>)ObjectConverter.TEMPLATE, value);
    }
    
    @Override
    public String convert(final Class<?> type, final String value) {
        return value;
    }
    
    @Override
    public Object convert(final Class<?> type, final String value, final ElementMap elementMap) {
        return value;
    }
    
    @Override
    public Class<Object> convertsTo() {
        return ObjectConverter.TEMPLATE;
    }
    
    @Override
    public boolean canConvertFromScratch() {
        return true;
    }
    
    @Override
    public boolean canConvertWithoutVariables() {
        return false;
    }
    
    @Override
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<?> type, final String value, final Environment env) {
        return "\"" + value + "\"";
    }
    
    static {
        m_logger = Logger.getLogger((Class)ObjectConverter.class);
        TEMPLATE = Object.class;
    }
}
