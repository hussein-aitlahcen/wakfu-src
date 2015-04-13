package com.ankamagames.xulor2.converter;

import com.ankamagames.xulor2.core.converter.*;
import org.apache.log4j.*;
import java.util.regex.*;
import com.ankamagames.xulor2.*;
import org.apache.commons.lang3.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public class StringConverter implements Converter<String>
{
    protected static final Logger m_logger;
    public static final Class<String> TEMPLATE;
    private static final Pattern TRANSLATION_PATTERN;
    private static final Pattern ENVIRONMENT_PROPERTY_PATTERN;
    
    public static void main(final String[] args) {
        final String s = "$pouet:meuh$";
        final Matcher matcher = StringConverter.ENVIRONMENT_PROPERTY_PATTERN.matcher(s);
        if (matcher.matches()) {
            System.out.println(matcher.group(0));
        }
    }
    
    @Override
    public String convert(final String value) {
        return this.convert((Class<? extends String>)StringConverter.TEMPLATE, value);
    }
    
    @Override
    public String convert(final Class<? extends String> type, final String value) {
        return this.convert(type, value, (ElementMap)null);
    }
    
    @Override
    public String convert(final Class<? extends String> type, final String value, final ElementMap map) {
        if (value == null) {
            return null;
        }
        final Matcher matcher = StringConverter.TRANSLATION_PATTERN.matcher(value);
        String translatedValue = value;
        while (matcher.find()) {
            try {
                final String replacement = Xulor.getInstance().getTranslatedString(matcher.group(2));
                translatedValue = StringUtils.replace(translatedValue, matcher.group(1), replacement);
            }
            catch (Exception e) {
                StringConverter.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        if (map != null) {
            final Matcher matcher2 = StringConverter.ENVIRONMENT_PROPERTY_PATTERN.matcher(translatedValue);
            if (matcher2.matches()) {
                final String name = matcher2.group(2);
                final String defaultValue = matcher2.group(4);
                translatedValue = map.getEnvironmentProperty(name, defaultValue);
            }
        }
        return translatedValue;
    }
    
    @Override
    public Class<String> convertsTo() {
        return StringConverter.TEMPLATE;
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
    public String toJavaCommandLine(final AbstractClassDocument doc, final DocumentParser parser, final Class<? extends String> type, final String value, final Environment env) {
        doc.addImport(StringConverter.TEMPLATE);
        final Matcher matcher = StringConverter.TRANSLATION_PATTERN.matcher(value);
        if (matcher.find()) {
            matcher.reset();
            final String varName = doc.getUnusedVarName();
            doc.addGeneratedCommandLine(new ClassVariable(type, varName, '\"' + value + '\"'));
            while (matcher.find()) {
                doc.addGeneratedCommandLine(new ClassVariable(type, varName, varName + ".replace(\"" + matcher.group(1) + "\", Xulor.getInstance().getTranslatedString(\"" + matcher.group(2) + "\"))"));
            }
            return varName;
        }
        final Matcher matcher2 = StringConverter.ENVIRONMENT_PROPERTY_PATTERN.matcher(value);
        if (matcher2.matches()) {
            final String name = '\"' + matcher2.group(2) + '\"';
            final String defaultValue = (matcher2.group(4) == null) ? "null" : ('\"' + matcher2.group(4) + '\"');
            final String varName2 = doc.getUnusedVarName();
            doc.addGeneratedCommandLine(new ClassVariable(type, varName2, "elementMap.getEnvironmentProperty(" + name + ", " + defaultValue + ')'));
            return varName2;
        }
        return '\"' + StringUtils.replace(value, "\\", "\\\\") + '\"';
    }
    
    static {
        m_logger = Logger.getLogger((Class)StringConverter.class);
        TEMPLATE = String.class;
        TRANSLATION_PATTERN = Pattern.compile("(%([^%]*)%)");
        ENVIRONMENT_PROPERTY_PATTERN = Pattern.compile("(\\$([A-Za-z0-9_\\-]+)(:([^\\$]+))*\\$)");
    }
}
