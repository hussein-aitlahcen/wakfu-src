package com.ankamagames.xulor2.appearance;

import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;

public class ToggleButtonAppearance extends ButtonAppearance
{
    public static final String TAG = "ToggleButtonAppearance";
    public static final String SELECTED = "selected";
    public static final String DISABLED_SELECTED = "disabledSelected";
    public static final String MOUSE_OVER_SELECTED = "mouseHoverSelected";
    public static final String PRESSED_SELECTED = "pressedSelected";
    protected boolean m_checked;
    
    @Override
    public String getTag() {
        return "ToggleButtonAppearance";
    }
    
    public boolean isChecked() {
        return this.m_checked;
    }
    
    public boolean toggleButton() {
        this.m_checked = !this.m_checked;
        final SelectionChangedEvent event = new SelectionChangedEvent(this.m_widget, this.m_checked);
        final boolean returnValue = this.m_widget.dispatchEvent(event);
        this.updateAppearance();
        return returnValue;
    }
    
    @Override
    protected void enableSelectedDecorators() {
        if (this.m_checked) {
            this.setEnabled(this.getEnableLabel(), true);
        }
        else {
            super.enableSelectedDecorators();
        }
    }
    
    private String getEnableLabel() {
        if (!this.m_enabled) {
            return "disabledSelected";
        }
        if (!this.m_over) {
            return "selected";
        }
        if (!this.m_armed) {
            return "mouseHoverSelected";
        }
        return "pressedSelected";
    }
    
    @Override
    public void prepareRender() {
        this.m_checked = false;
        this.updateAppearance();
    }
    
    @Override
    public void copyElement(final BasicElement t) {
        final ToggleButtonAppearance e = (ToggleButtonAppearance)t;
        super.copyElement(e);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_checked = false;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_checked = false;
    }
}
