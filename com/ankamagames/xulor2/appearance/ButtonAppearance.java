package com.ankamagames.xulor2.appearance;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class ButtonAppearance extends TextWidgetAppearance
{
    private static Logger m_logger;
    public static final String TAG = "ButtonAppearance";
    public static final String DEFAULT = "default";
    public static final String DISABLED = "disabled";
    public static final String MOUSE_OVER = "mouseHover";
    public static final String PRESSED = "pressed";
    private static final ObjectPool m_pool;
    protected int m_gap;
    protected int m_clickSoundId;
    protected boolean m_armed;
    protected boolean m_over;
    protected boolean m_enabled;
    public static final int GAP_HASH;
    
    public ButtonAppearance() {
        super();
        this.m_gap = 5;
        this.m_clickSoundId = -3;
        this.m_armed = false;
        this.m_over = false;
        this.m_enabled = true;
    }
    
    public static ButtonAppearance checkOut() {
        ButtonAppearance c;
        try {
            c = (ButtonAppearance)ButtonAppearance.m_pool.borrowObject();
            c.m_currentPool = ButtonAppearance.m_pool;
        }
        catch (Exception e) {
            ButtonAppearance.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            c = new ButtonAppearance();
            c.onCheckOut();
        }
        return c;
    }
    
    @Override
    public String getTag() {
        return "ButtonAppearance";
    }
    
    public int getGap() {
        return this.m_gap;
    }
    
    public void setGap(final int gap) {
        this.m_gap = gap;
    }
    
    public int getClickSoundId() {
        return this.m_clickSoundId;
    }
    
    public void setClickSoundId(final int clickSoundId) {
        this.m_clickSoundId = clickSoundId;
        final Button b = (Button)this.m_widget;
        if (b != null && this.m_clickSoundId != -3) {
            b.setClickSoundId(this.m_clickSoundId);
        }
    }
    
    @Override
    public void setWidget(final Widget w) {
        super.setWidget(w);
        final Button b = (Button)this.m_widget;
        if (this.m_clickSoundId != -3) {
            b.setClickSoundId(this.m_clickSoundId);
        }
    }
    
    public boolean isOver() {
        return this.m_over;
    }
    
    public boolean isArmed() {
        return this.m_armed;
    }
    
    public void enter() {
        this.m_over = true;
        this.updateAppearance();
    }
    
    public void exit() {
        this.m_over = false;
        this.updateAppearance();
    }
    
    public void pressed() {
        this.m_armed = true;
        this.updateAppearance();
    }
    
    public void released() {
        if (this.m_armed) {
            this.m_armed = false;
            this.updateAppearance();
        }
    }
    
    public void disabled() {
        if (this.m_enabled) {
            this.m_enabled = false;
            this.updateAppearance();
        }
    }
    
    public void enabled() {
        if (!this.m_enabled) {
            this.m_enabled = true;
            this.updateAppearance();
        }
    }
    
    protected void updateAppearance() {
        this.disableAllDecorators();
        this.enableSelectedDecorators();
    }
    
    protected void enableSelectedDecorators() {
        if (this.m_enabled) {
            if (this.m_over) {
                if (this.m_armed) {
                    this.setEnabled("pressed", true);
                }
                else {
                    this.setEnabled("mouseHover", true);
                }
            }
            else {
                this.setEnabled("default", true);
            }
        }
        else {
            this.setEnabled("disabled", true);
        }
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final ButtonAppearance a = (ButtonAppearance)source;
        super.copyElement(source);
        a.m_gap = this.m_gap;
        a.m_clickSoundId = this.m_clickSoundId;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_gap = 5;
        this.m_clickSoundId = -3;
        this.m_armed = false;
        this.m_over = false;
        this.m_enabled = true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_gap = 5;
        this.m_clickSoundId = -3;
        this.m_armed = false;
        this.m_over = false;
        this.m_enabled = true;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ButtonAppearance.GAP_HASH) {
            this.setGap(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != Button.CLICK_SOUND_ID_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setClickSoundId(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ButtonAppearance.GAP_HASH) {
            this.setGap(PrimitiveConverter.getInteger(value));
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        ButtonAppearance.m_logger = Logger.getLogger((Class)ButtonAppearance.class);
        m_pool = new MonitoredPool(new ObjectFactory<ButtonAppearance>() {
            @Override
            public ButtonAppearance makeObject() {
                return new ButtonAppearance();
            }
        });
        GAP_HASH = "gap".hashCode();
    }
}
