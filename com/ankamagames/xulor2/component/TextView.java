package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.text.builder.*;
import com.ankamagames.xulor2.component.text.document.*;
import com.ankamagames.xulor2.core.converter.*;

public class TextView extends AbstractSelectableTextWidget
{
    public static final String TAG = "TextView";
    protected String m_rawAppendText;
    
    public void appendText(String text) {
        if (text == null) {
            text = "";
        }
        if (this.m_rawText != null) {
            this.m_rawText += text;
        }
        else {
            if (this.m_rawAppendText == null) {
                this.m_rawAppendText = "";
            }
            this.m_rawAppendText += text;
        }
        this.setNeedsToPreProcess();
    }
    
    @Override
    public String getTag() {
        return "TextView";
    }
    
    @Override
    public void setText(final Object text) {
        super.setText(text);
        this.m_rawAppendText = null;
    }
    
    protected void applyAppendText() {
        if (this.m_rawAppendText != null) {
            this.getTextBuilder().appendRawText(this.m_rawAppendText);
            this.resetGeometryProgressTextTimer();
            this.m_rawAppendText = null;
        }
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        this.applyAppendText();
        return super.preProcess(deltaTime);
    }
    
    @Override
    public void copyElement(final BasicElement a) {
        final TextView t = (TextView)a;
        if (this.m_rawAppendText != null) {
            t.m_rawAppendText = this.m_rawAppendText;
        }
        super.copyElement(a);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final TextWidgetAppearance app = TextWidgetAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        this.setTextBuilder(new TextBuilder(new MultiplePartTextDocument()));
        this.getTextBuilder().setTextWidget(this);
        this.setMultiline(true);
    }
    
    @Override
    public boolean appendXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == TextView.TEXT_HASH) {
            this.appendText(cl.convertToString(value, this.m_elementMap));
            return true;
        }
        return super.appendXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean appendPropertyAttribute(final int hash, final Object value) {
        if (hash == TextView.TEXT_HASH) {
            this.appendText(String.valueOf(value));
            return true;
        }
        return super.appendPropertyAttribute(hash, value);
    }
}
