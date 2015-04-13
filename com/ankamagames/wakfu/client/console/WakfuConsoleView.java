package com.ankamagames.wakfu.client.console;

import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;

public class WakfuConsoleView implements ConsoleView, FieldProvider
{
    public static final String FIELDEDPROPERTY_NAME = "debug.console";
    public static final String PROMPT_FIELD = "prompt";
    public static final String INPUT_FIELD = "input";
    public static final String LOGS_FIELD = "logs";
    public static final String[] FIELDS;
    private static final WakfuConsoleView m_instance;
    private String m_prompt;
    private String m_imput;
    private String m_logs;
    
    public WakfuConsoleView() {
        super();
        this.m_prompt = "";
        this.m_imput = "";
        this.m_logs = "";
        final PropertiesProvider propertiesProvider = Xulor.getInstance().getEnvironment().getPropertiesProvider();
        propertiesProvider.setPropertyValue("debug.console", this);
    }
    
    public static WakfuConsoleView getInstance() {
        return WakfuConsoleView.m_instance;
    }
    
    @Override
    public void setPrompt(final String prompt) {
        this.m_prompt = prompt;
        Xulor.getInstance().getEnvironment().getPropertiesProvider().setPropertyValue("debug.console", "prompt", prompt);
    }
    
    @Override
    public void err(final String text) {
        final String formatedText = new TextWidgetFormater().addColor("FF0000").append(text).append("\n").finishAndToString();
        Xulor.getInstance().getEnvironment().getPropertiesProvider().appendPropertyValue("debug.console", "logs", formatedText);
    }
    
    @Override
    public void log(final String text) {
        final String formatedText = new TextWidgetFormater().addColor("00FF00").append(text).append("\n").finishAndToString();
        Xulor.getInstance().getEnvironment().getPropertiesProvider().appendPropertyValue("debug.console", "logs", formatedText);
    }
    
    @Override
    public void customStyle(final String text) {
        Xulor.getInstance().getEnvironment().getPropertiesProvider().appendPropertyValue("debug.console", "logs", text);
    }
    
    @Override
    public void customTrace(final String text, final int colorRGB) {
        final int BGR = (colorRGB & 0xFF) << 16 | (colorRGB & 0xFF00) | (colorRGB & 0xFF0000) >> 16;
        final Color coloOor = new Color(BGR);
        final String formatedText = new TextWidgetFormater().addColor(coloOor.getRGBtoHex()).append(text).append("\n").finishAndToString();
        Xulor.getInstance().getEnvironment().getPropertiesProvider().appendPropertyValue("debug.console", "logs", formatedText);
    }
    
    @Override
    public void trace(final String text) {
        Xulor.getInstance().getEnvironment().getPropertiesProvider().appendPropertyValue("debug.console", "logs", text + "\n");
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("prompt")) {
            return this.m_prompt;
        }
        if (fieldName.equals("input")) {
            return this.m_imput;
        }
        if (fieldName.equals("logs")) {
            return this.m_logs;
        }
        return null;
    }
    
    @Override
    public String[] getFields() {
        return WakfuConsoleView.FIELDS;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
        if (fieldName.equals("prompt")) {
            this.m_prompt = (String)value;
        }
        else if (fieldName.equals("input")) {
            this.m_imput = (String)value;
        }
        else if (fieldName.equals("logs")) {
            this.m_logs = (String)value;
        }
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
        if (fieldName.equals("logs")) {
            this.m_logs += (String)value;
        }
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return !fieldName.equals("logs");
    }
    
    static {
        FIELDS = new String[] { "prompt", "input", "logs" };
        m_instance = new WakfuConsoleView();
    }
}
