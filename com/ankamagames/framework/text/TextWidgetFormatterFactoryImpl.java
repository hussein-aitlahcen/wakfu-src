package com.ankamagames.framework.text;

public class TextWidgetFormatterFactoryImpl implements TextWidgetFormatterFactory
{
    public static final TextWidgetFormatterFactoryImpl INSTANCE;
    
    @Override
    public TextWidgetFormater createNew() {
        return new TextWidgetFormater();
    }
    
    static {
        INSTANCE = new TextWidgetFormatterFactoryImpl();
    }
}
