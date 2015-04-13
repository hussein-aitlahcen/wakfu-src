package com.ankamagames.xulor2.component;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;
import com.ankamagames.xulor2.core.converter.*;

public class ValueSelector extends Container
{
    public static final String TAG = "valueSelector";
    public static final String INCREASE_BUTTON = "increaseButton";
    public static final String DECREASE_BUTTON = "decreaseButton";
    public static final String VALUE_TEXT_EDITOR = "textEditor";
    private TextEditor m_textEditor;
    private Button m_increaseButton;
    private Button m_decreaseButton;
    private int m_value;
    private int m_minBound;
    private int m_maxBound;
    private String m_displayFormat;
    public static final int MAX_BOUND_HASH;
    public static final int MIN_BOUND_HASH;
    public static final int VALUE_HASH;
    public static final int DISPLAY_FORMAT_HASH;
    
    @Override
    public String getTag() {
        return "valueSelector";
    }
    
    public int getValue() {
        return this.m_value;
    }
    
    public void setValue(int value) {
        value = MathHelper.clamp(value, this.m_minBound, this.m_maxBound);
        final boolean valueChanged = value != this.m_value;
        final int oldValue = this.m_value;
        this.m_value = value;
        this.updateDisplay();
        if (valueChanged) {
            final ValueChangedEvent e = new ValueChangedEvent(this);
            e.onCheckOut();
            e.setValue(this.m_value);
            e.setOldValue(oldValue);
            this.dispatchEvent(e);
        }
    }
    
    public int getMinBound() {
        return this.m_minBound;
    }
    
    public void setMinBound(final int minBound) {
        if (this.m_minBound == minBound) {
            return;
        }
        this.m_minBound = minBound;
        if (this.m_value < this.m_minBound) {
            this.setValue(minBound);
        }
    }
    
    public int getMaxBound() {
        return this.m_maxBound;
    }
    
    public void setMaxBound(final int maxBound) {
        if (this.m_maxBound == maxBound) {
            return;
        }
        this.m_maxBound = maxBound;
        if (this.m_value > this.m_maxBound) {
            this.setValue(maxBound);
        }
    }
    
    public String getDisplayFormat() {
        return this.m_displayFormat;
    }
    
    public void setDisplayFormat(final String displayFormat) {
        this.m_displayFormat = displayFormat;
        this.updateDisplay();
    }
    
    @Override
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        if (themeElementName.equals("increaseButton")) {
            return this.m_increaseButton;
        }
        if (themeElementName.equals("decreaseButton")) {
            return this.m_decreaseButton;
        }
        if (themeElementName.equals("textEditor")) {
            return this.m_textEditor;
        }
        return null;
    }
    
    public void updateDisplay() {
        this.m_textEditor.setText(String.format(this.m_displayFormat, this.m_value));
    }
    
    private void createComponents() {
        (this.m_textEditor = new TextEditor()).onCheckOut();
        this.m_textEditor.setText(String.valueOf(this.m_value));
        this.m_textEditor.addEventListener(Events.KEY_TYPED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                ValueSelector.this.setValue(PrimitiveConverter.getInteger(ValueSelector.this.m_textEditor.getText(), ValueSelector.this.m_minBound));
                return false;
            }
        }, false);
        this.add(this.m_textEditor);
        final Container buttonContainer = Container.checkOut();
        buttonContainer.setExpandable(false);
        final RowLayout rl = RowLayout.checkOut();
        rl.setHorizontal(false);
        rl.setAlign(Alignment9.CENTER);
        buttonContainer.add(rl);
        (this.m_increaseButton = new Button()).onCheckOut();
        this.m_increaseButton.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (ValueSelector.this.m_value == ValueSelector.this.m_maxBound) {
                    ValueSelector.this.setValue(ValueSelector.this.m_minBound);
                }
                else {
                    ValueSelector.this.setValue(ValueSelector.this.m_value + 1);
                }
                return true;
            }
        }, false);
        this.m_increaseButton.setClickSoundId(XulorSoundManager.getInstance().getIncreaseButtonId());
        (this.m_decreaseButton = new Button()).onCheckOut();
        this.m_decreaseButton.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (ValueSelector.this.m_value == ValueSelector.this.m_minBound) {
                    ValueSelector.this.setValue(ValueSelector.this.m_maxBound);
                }
                else {
                    ValueSelector.this.setValue(ValueSelector.this.m_value - 1);
                }
                return true;
            }
        }, false);
        this.m_decreaseButton.setClickSoundId(XulorSoundManager.getInstance().getDecreaseButtonId());
        buttonContainer.add(this.m_increaseButton);
        buttonContainer.add(this.m_decreaseButton);
        this.add(buttonContainer);
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        super.copyElement(c);
        final ValueSelector v = (ValueSelector)c;
        v.m_displayFormat = this.m_displayFormat;
        v.setMinBound(this.m_minBound);
        v.setMaxBound(this.m_maxBound);
        v.setValue(this.m_value);
        ArrayList<EventListener> listenerArrayList = this.m_increaseButton.getListeners(Events.MOUSE_CLICKED, false);
        if (listenerArrayList != null) {
            for (int i = listenerArrayList.size() - 1; i >= 0; --i) {
                v.m_increaseButton.removeEventListener(Events.MOUSE_CLICKED, listenerArrayList.get(i), false);
            }
        }
        listenerArrayList = this.m_decreaseButton.getListeners(Events.MOUSE_CLICKED, false);
        if (listenerArrayList != null) {
            for (int i = listenerArrayList.size() - 1; i >= 0; --i) {
                v.m_decreaseButton.removeEventListener(Events.MOUSE_CLICKED, listenerArrayList.get(i), false);
            }
        }
        listenerArrayList = this.m_textEditor.getListeners(Events.KEY_TYPED, false);
        if (listenerArrayList != null) {
            for (int i = listenerArrayList.size() - 1; i >= 0; --i) {
                v.m_textEditor.removeEventListener(Events.KEY_TYPED, listenerArrayList.get(i), false);
            }
        }
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.createComponents();
        this.m_minBound = 0;
        this.m_maxBound = 100;
        this.m_value = 0;
        this.m_displayFormat = "%d";
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_textEditor = null;
        this.m_increaseButton = null;
        this.m_decreaseButton = null;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ValueSelector.MAX_BOUND_HASH) {
            this.setMaxBound(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ValueSelector.MIN_BOUND_HASH) {
            this.setMinBound(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ValueSelector.VALUE_HASH) {
            this.setValue(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != ValueSelector.DISPLAY_FORMAT_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setDisplayFormat(value);
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ValueSelector.MAX_BOUND_HASH) {
            this.setMaxBound(PrimitiveConverter.getInteger(value));
        }
        else if (hash == ValueSelector.MIN_BOUND_HASH) {
            this.setMinBound(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != ValueSelector.VALUE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setValue(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    static {
        MAX_BOUND_HASH = "maxBound".hashCode();
        MIN_BOUND_HASH = "minBound".hashCode();
        VALUE_HASH = "value".hashCode();
        DISPLAY_FORMAT_HASH = "displayFormat".hashCode();
    }
}
