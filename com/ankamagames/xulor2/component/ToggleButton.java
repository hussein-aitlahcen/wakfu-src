package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.appearance.*;

public class ToggleButton extends Button
{
    public static final String TAG = "CheckBox";
    public static final String TAG2 = "ToggleButton";
    protected EventListener m_mouseClickedListener;
    protected boolean m_overrideClickSound;
    public static final int SELECTED_HASH;
    public static final int OVERRIDE_CLICK_SOUND_HASH;
    
    public ToggleButton() {
        super();
        this.m_overrideClickSound = true;
    }
    
    @Override
    public String getTag() {
        return "CheckBox";
    }
    
    public void setSelected(final boolean selected) {
        final ToggleButtonAppearance buttonAppearance = this.getAppearance();
        if (buttonAppearance != null && selected != buttonAppearance.isChecked()) {
            buttonAppearance.toggleButton();
        }
    }
    
    public boolean getSelected() {
        return this.getAppearance().isChecked();
    }
    
    public void setOverrideClickSound(final boolean overrideClickSound) {
        this.m_overrideClickSound = overrideClickSound;
    }
    
    @Override
    public void setClickSoundId(final int clickSoundId) {
        super.setClickSoundId(clickSoundId);
        this.m_overrideClickSound = false;
    }
    
    @Override
    public ToggleButtonAppearance getAppearance() {
        return (ToggleButtonAppearance)this.m_appearance;
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return appearance instanceof ToggleButtonAppearance;
    }
    
    @Override
    public void addButtonAppearanceListeners() {
        super.addButtonAppearanceListeners();
        this.m_mouseClickedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final boolean eventConsumed = ToggleButton.this.getAppearance().toggleButton();
                if (event.isSoundConsumed()) {
                    return eventConsumed;
                }
                if (ToggleButton.this.getAppearance().isChecked()) {
                    XulorSoundManager.getInstance().toggleButtonSelect();
                }
                else {
                    XulorSoundManager.getInstance().toggleButtonUnselect();
                }
                event.setSoundConsumed(true);
                return eventConsumed;
            }
        };
        this.addEventListener(Events.MOUSE_CLICKED, this.m_mouseClickedListener, true);
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        ((ToggleButton)source).m_overrideClickSound = this.m_overrideClickSound;
        ((ToggleButton)source).removeEventListener(Events.MOUSE_CLICKED, this.m_mouseClickedListener, true);
    }
    
    @Override
    protected void processEventForSound(final Event e, final boolean up) {
        if (this.m_overrideClickSound) {
            switch (e.getType()) {
                case MOUSE_CLICKED:
                case MOUSE_DOUBLE_CLICKED:
                case ITEM_CLICK:
                case ITEM_DOUBLE_CLICK: {
                    return;
                }
            }
        }
        super.processEventForSound(e, up);
    }
    
    @Override
    public void prepareRender() {
        super.prepareRender();
        this.getAppearance().prepareRender();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mouseClickedListener = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_overrideClickSound = true;
        final ToggleButtonAppearance app = new ToggleButtonAppearance();
        app.onCheckOut();
        app.setWidget(this);
        this.add(app);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ToggleButton.SELECTED_HASH) {
            this.setSelected(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != ToggleButton.OVERRIDE_CLICK_SOUND_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setOverrideClickSound(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ToggleButton.SELECTED_HASH) {
            this.setSelected(PrimitiveConverter.getBoolean(value));
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        SELECTED_HASH = "selected".hashCode();
        OVERRIDE_CLICK_SOUND_HASH = "overrideClickSound".hashCode();
    }
}
