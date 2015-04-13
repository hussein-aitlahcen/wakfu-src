package com.ankamagames.xulor2.util.xmlToJava.init;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.util.xmlToJava.*;

public class TooltipInitLoader implements InitLoader
{
    private float m_borderWidth;
    private Color m_bgColor;
    private Color m_textColor;
    private Color m_borderColor;
    private String m_font;
    private boolean m_init;
    
    public TooltipInitLoader(final float borderWidth, final Color bgColor, final Color textColor, final Color borderColor, final String font) {
        super();
        this.m_init = false;
        this.m_borderWidth = borderWidth;
        this.m_bgColor = bgColor;
        this.m_textColor = textColor;
        this.m_borderColor = borderColor;
        this.m_font = font;
        this.m_init = true;
    }
    
    public TooltipInitLoader(final DocumentEntry entry, final DocumentParser doc) {
        super();
        this.m_init = false;
        if (!entry.getName().equalsIgnoreCase("tooltip")) {
            return;
        }
        this.m_borderWidth = ToolTipElement.DEFAULT_BORDER_WIDTH;
        DocumentEntry param = entry.getParameterByName("borderWidth");
        if (param != null) {
            this.m_borderWidth = param.getFloatValue();
        }
        this.m_bgColor = ToolTipElement.DEFAULT_BACKGROUND_COLOR;
        param = entry.getParameterByName("backgroundColor");
        if (param != null) {
            final Color old = this.m_bgColor;
            this.m_bgColor = doc.getColor(param.getStringValue());
            if (this.m_bgColor == null) {
                this.m_bgColor = ConverterLibrary.getInstance().convert(Color.class, param.getStringValue());
            }
            if (this.m_bgColor == null) {
                this.m_bgColor = old;
            }
        }
        this.m_textColor = ToolTipElement.DEFAULT_TEXT_COLOR;
        param = entry.getParameterByName("textColor");
        if (param != null) {
            final Color old = this.m_textColor;
            this.m_textColor = doc.getColor(param.getStringValue());
            if (this.m_textColor == null) {
                this.m_textColor = ConverterLibrary.getInstance().convert(Color.class, param.getStringValue());
            }
            if (this.m_textColor == null) {
                this.m_textColor = old;
            }
        }
        this.m_borderColor = ToolTipElement.DEFAULT_BORDER_COLOR;
        param = entry.getParameterByName("borderColor");
        if (param != null) {
            final Color old = this.m_borderColor;
            this.m_borderColor = doc.getColor(param.getStringValue());
            if (this.m_borderColor == null) {
                this.m_borderColor = ConverterLibrary.getInstance().convert(Color.class, param.getStringValue());
            }
            if (this.m_borderColor == null) {
                this.m_borderColor = old;
            }
        }
        this.m_font = null;
        param = entry.getParameterByName("font");
        if (param != null) {
            this.m_font = param.getStringValue();
        }
        this.m_init = true;
    }
    
    @Override
    public void init(final DocumentParser doc) {
        if (this.m_init) {
            doc.loadTooltip(this.m_borderWidth, this.m_bgColor, this.m_textColor, this.m_borderColor, this.m_font);
        }
    }
    
    public String getCommand(final DocumentParser doc, final DocumentVariableAccessor accessor) {
        if (!this.m_init) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("InitLoaderManager.getInstance().addLoader(new TooltipInitLoader(").append(this.m_borderWidth).append("f, ");
        sb.append("new ").append(Color.class.getSimpleName()).append("(").append(this.m_bgColor.getRed()).append("f, ").append(this.m_bgColor.getGreen()).append("f, ").append(this.m_bgColor.getBlue()).append("f, ").append(this.m_bgColor.getAlpha()).append("f), ");
        sb.append("new ").append(Color.class.getSimpleName()).append("(").append(this.m_textColor.getRed()).append("f, ").append(this.m_textColor.getGreen()).append("f, ").append(this.m_textColor.getBlue()).append("f, ").append(this.m_textColor.getAlpha()).append("f), ");
        sb.append("new ").append(Color.class.getSimpleName()).append("(").append(this.m_borderColor.getRed()).append("f, ").append(this.m_borderColor.getGreen()).append("f, ").append(this.m_borderColor.getBlue()).append("f, ").append(this.m_borderColor.getAlpha()).append("f), ");
        sb.append("\"").append(this.m_font).append("\"));");
        return sb.toString();
    }
    
    @Override
    public void addToDocument(final ThemeInitClassDocument doc) {
        if (!this.m_init) {
            return;
        }
        final String docVarName = doc.getDocumentParserVarName();
        doc.addGeneratedCommandLine(new ClassMethodCall(null, "loadTooltip", docVarName, new String[] { this.m_borderWidth + "f", "new " + Color.class.getSimpleName() + "(" + this.m_bgColor.getRed() + "f, " + this.m_bgColor.getGreen() + "f, " + this.m_bgColor.getBlue() + "f, " + this.m_bgColor.getAlpha() + "f)", "new " + Color.class.getSimpleName() + "(" + this.m_textColor.getRed() + "f, " + this.m_textColor.getGreen() + "f, " + this.m_textColor.getBlue() + "f, " + this.m_textColor.getAlpha() + "f)", "new " + Color.class.getSimpleName() + "(" + this.m_borderColor.getRed() + "f, " + this.m_borderColor.getGreen() + "f, " + this.m_borderColor.getBlue() + "f, " + this.m_borderColor.getAlpha() + "f)", "\"" + this.m_font + "\"" }));
    }
    
    @Override
    public boolean isInitialized() {
        return this.m_init;
    }
}
