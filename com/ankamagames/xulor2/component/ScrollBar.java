package com.ankamagames.xulor2.component;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;

public class ScrollBar extends Container
{
    private static Logger m_logger;
    public static final String TAG = "ScrollBar";
    public static final String THEME_HORIZONTAL_SLIDER = "horizontalSlider";
    public static final String THEME_VERTICAL_SLIDER = "verticalSlider";
    public static final String THEME_HORIZONTAL_INCREASE_BUTTON = "horizontalIncreaseButton";
    public static final String THEME_VERTICAL_INCREASE_BUTTON = "verticalIncreaseButton";
    public static final String THEME_HORIZONTAL_DECREASE_BUTTON = "horizontalDecreaseButton";
    public static final String THEME_VERTICAL_DECREASE_BUTTON = "verticalDecreaseButton";
    private static final int NO_SCROLL = 0;
    private static final int SCROLL_UP = 1;
    private static final int SCROLL_DOWN = 2;
    private int m_pressedButton;
    private EventListener m_noScrollListener;
    private EventListener m_clickListener;
    private EventListener m_increaseListener;
    private EventListener m_decreaseListener;
    private ScrollBarMessageHandler m_messageHandler;
    private boolean m_horizontal;
    private float m_jump;
    private Button m_increaseButton;
    private Button m_decreaseButton;
    private Slider m_slider;
    public static final int HORIZONTAL_HASH;
    public static final int BUTTON_JUMP_HASH;
    public static final int VALUE_HASH;
    
    public ScrollBar() {
        super();
        this.m_messageHandler = new ScrollBarMessageHandler();
        this.m_jump = 0.05f;
    }
    
    @Override
    public String getTag() {
        return "ScrollBar";
    }
    
    public float getValue() {
        return this.m_slider.getValue();
    }
    
    public void setValue(final float value) {
        this.m_slider.setValue(value);
    }
    
    public float getButtonJump() {
        return this.m_jump;
    }
    
    public void setButtonJump(float jump) {
        if (jump < 0.0f) {
            jump = 0.0f;
        }
        else if (jump > 1.0f) {
            jump = 1.0f;
        }
        this.m_jump = jump;
    }
    
    public boolean isHorizontal() {
        return this.m_horizontal;
    }
    
    public void setHorizontal(final boolean horizontal) {
        this.m_horizontal = horizontal;
        this.m_slider.setHorizontal(horizontal);
    }
    
    public Slider getSlider() {
        return this.m_slider;
    }
    
    public void setSlider(final Slider slider) {
        this.m_slider = slider;
    }
    
    public void setSliderSize(final float size) {
        this.m_slider.setSliderSize(size);
        final boolean enabled = size != 1.0f;
        this.m_slider.getButton().setVisible(enabled);
        this.m_increaseButton.setEnabled(enabled);
        this.m_decreaseButton.setEnabled(enabled);
    }
    
    public Button getDecreaseButton() {
        return this.m_decreaseButton;
    }
    
    public void setDecreaseButton(final Button decreaseButton) {
        this.m_decreaseButton = decreaseButton;
    }
    
    public void setIncreaseButton(final Button increaseButton) {
        this.m_increaseButton = increaseButton;
    }
    
    public Button getIncreaseButton() {
        return this.m_increaseButton;
    }
    
    @Override
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        if (this.m_horizontal || compilationMode) {
            if ("horizontalSlider".equalsIgnoreCase(themeElementName)) {
                return this.m_slider;
            }
            if ("horizontalDecreaseButton".equalsIgnoreCase(themeElementName)) {
                return this.m_decreaseButton;
            }
            if ("horizontalIncreaseButton".equalsIgnoreCase(themeElementName)) {
                return this.m_increaseButton;
            }
        }
        if (!this.m_horizontal || compilationMode) {
            if ("verticalSlider".equalsIgnoreCase(themeElementName)) {
                return this.m_slider;
            }
            if ("verticalDecreaseButton".equalsIgnoreCase(themeElementName)) {
                return this.m_decreaseButton;
            }
            if ("verticalIncreaseButton".equalsIgnoreCase(themeElementName)) {
                return this.m_increaseButton;
            }
        }
        return null;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        this.m_increaseButton.setEnabled(enabled);
        this.m_decreaseButton.setEnabled(enabled);
        this.m_slider.setEnabled(enabled);
        this.m_slider.setVisible(this.m_slider.isEnabledFull());
    }
    
    @Override
    public void setNetEnabled(final boolean netEnabled) {
        super.setNetEnabled(netEnabled);
        this.m_increaseButton.setNetEnabled(netEnabled);
        this.m_decreaseButton.setNetEnabled(netEnabled);
        this.m_slider.setNetEnabled(netEnabled);
        this.m_slider.setVisible(this.isEnabledFull());
    }
    
    public void initPosition() {
        int x = 0;
        int y = 0;
        if (this.m_horizontal) {
            this.m_decreaseButton.setPosition(x, y);
            this.m_slider.setPosition(x + this.m_decreaseButton.getWidth(), y);
            x = this.getAppearance().getContentWidth() - this.m_decreaseButton.getWidth();
            this.m_increaseButton.setPosition(x, y);
        }
        else {
            y = this.getAppearance().getContentHeight() - this.m_increaseButton.getHeight();
            this.m_increaseButton.setPosition(x, y);
            y = 0;
            this.m_decreaseButton.setPosition(x, y);
            this.m_slider.setPosition(x, y + this.m_decreaseButton.getHeight());
        }
    }
    
    private void renewClockIfNeeded() {
        if (this.m_pressedButton != 0) {
            MessageScheduler.getInstance().removeAllClocks(this.m_messageHandler);
            MessageScheduler.getInstance().addClock(this.m_messageHandler, 150L, this.m_pressedButton, 1);
        }
    }
    
    public void addScrollBarListeners() {
        this.m_noScrollListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                ScrollBar.this.m_pressedButton = 0;
                MessageScheduler.getInstance().removeAllClocks(ScrollBar.this.m_messageHandler);
                return false;
            }
        };
        this.m_clickListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (event.getTarget() == ScrollBar.this.m_increaseButton) {
                    ScrollBar.this.m_slider.setValue(ScrollBar.this.m_slider.getValue() + ScrollBar.this.m_jump);
                }
                if (event.getTarget() == ScrollBar.this.m_decreaseButton) {
                    ScrollBar.this.m_slider.setValue(ScrollBar.this.m_slider.getValue() - ScrollBar.this.m_jump);
                }
                return false;
            }
        };
        this.m_increaseListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (ScrollBar.this.m_increaseButton.getAppearance().isArmed()) {
                    ScrollBar.this.m_pressedButton = 1;
                    MessageScheduler.getInstance().removeAllClocks(ScrollBar.this.m_messageHandler);
                    MessageScheduler.getInstance().addClock(ScrollBar.this.m_messageHandler, 500L, ScrollBar.this.m_pressedButton, 1);
                }
                return false;
            }
        };
        this.m_decreaseListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (ScrollBar.this.m_decreaseButton.getAppearance().isArmed()) {
                    ScrollBar.this.m_pressedButton = 2;
                    MessageScheduler.getInstance().removeAllClocks(ScrollBar.this.m_messageHandler);
                    MessageScheduler.getInstance().addClock(ScrollBar.this.m_messageHandler, 500L, ScrollBar.this.m_pressedButton, 1);
                }
                return false;
            }
        };
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, this.m_noScrollListener, false);
        this.addEventListener(Events.MOUSE_CLICKED, this.m_clickListener, false);
        this.m_increaseButton.addEventListener(Events.MOUSE_EXITED, this.m_noScrollListener, false);
        this.m_increaseButton.addEventListener(Events.MOUSE_PRESSED, this.m_increaseListener, false);
        this.m_increaseButton.addEventListener(Events.MOUSE_ENTERED, this.m_increaseListener, false);
        this.m_decreaseButton.addEventListener(Events.MOUSE_EXITED, this.m_noScrollListener, false);
        this.m_decreaseButton.addEventListener(Events.MOUSE_PRESSED, this.m_decreaseListener, false);
        this.m_decreaseButton.addEventListener(Events.MOUSE_ENTERED, this.m_decreaseListener, false);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_increaseButton = null;
        this.m_decreaseButton = null;
        this.m_slider = null;
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this.m_noScrollListener, false);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final ScrollBarLayout sbl = new ScrollBarLayout();
        sbl.onCheckOut();
        this.add(sbl);
        (this.m_increaseButton = new Button()).onCheckOut();
        this.m_increaseButton.setCanBeCloned(false);
        this.m_increaseButton.setClickSoundId(XulorSoundManager.getInstance().getIncreaseButtonId());
        (this.m_decreaseButton = new Button()).onCheckOut();
        this.m_decreaseButton.setCanBeCloned(false);
        this.m_decreaseButton.setClickSoundId(XulorSoundManager.getInstance().getDecreaseButtonId());
        (this.m_slider = new Slider()).onCheckOut();
        this.m_slider.setCanBeCloned(false);
        this.m_nonBlocking = false;
        this.add(this.m_decreaseButton);
        this.add(this.m_increaseButton);
        this.add(this.m_slider);
        this.addScrollBarListeners();
    }
    
    @Override
    public void copyElement(final BasicElement s) {
        final ScrollBar e = (ScrollBar)s;
        super.copyElement(e);
        e.m_horizontal = this.m_horizontal;
        e.m_jump = this.m_jump;
        e.removeEventListener(Events.MOUSE_CLICKED, this.m_clickListener, false);
        e.getIncreaseButton().removeEventListener(Events.MOUSE_ENTERED, this.m_increaseListener, false);
        e.getIncreaseButton().removeEventListener(Events.MOUSE_PRESSED, this.m_increaseListener, false);
        e.getIncreaseButton().removeEventListener(Events.MOUSE_EXITED, this.m_noScrollListener, false);
        e.getDecreaseButton().removeEventListener(Events.MOUSE_ENTERED, this.m_decreaseListener, false);
        e.getDecreaseButton().removeEventListener(Events.MOUSE_PRESSED, this.m_decreaseListener, false);
        e.getDecreaseButton().removeEventListener(Events.MOUSE_EXITED, this.m_noScrollListener, false);
        e.m_styleIsDirty = true;
        e.setNeedsToPreProcess();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ScrollBar.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ScrollBar.BUTTON_JUMP_HASH) {
            this.setButtonJump(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != ScrollBar.VALUE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setValue(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ScrollBar.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ScrollBar.BUTTON_JUMP_HASH) {
            this.setButtonJump(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != ScrollBar.VALUE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setValue(PrimitiveConverter.getFloat(value));
        }
        return true;
    }
    
    static {
        ScrollBar.m_logger = Logger.getLogger((Class)ScrollBar.class);
        HORIZONTAL_HASH = "horizontal".hashCode();
        BUTTON_JUMP_HASH = "buttonJump".hashCode();
        VALUE_HASH = "value".hashCode();
    }
    
    private class ScrollBarMessageHandler implements MessageHandler
    {
        @Override
        public boolean onMessage(final Message message) {
            final ClockMessage msg = (ClockMessage)message;
            if (msg.getSubId() == 1) {
                ScrollBar.this.setValue(ScrollBar.this.getValue() + ScrollBar.this.getButtonJump());
            }
            else if (msg.getSubId() == 2) {
                ScrollBar.this.setValue(ScrollBar.this.getValue() - ScrollBar.this.getButtonJump());
            }
            ScrollBar.this.renewClockIfNeeded();
            return false;
        }
        
        @Override
        public long getId() {
            return 1L;
        }
        
        @Override
        public void setId(final long id) {
        }
    }
    
    public enum ScrollBarBehaviour
    {
        WHEN_NEEDED, 
        FORCE_DISPLAY, 
        FORCE_HIDE;
        
        public static ScrollBarBehaviour value(final String value) {
            final ScrollBarBehaviour[] arr$;
            final ScrollBarBehaviour[] values = arr$ = values();
            for (final ScrollBarBehaviour a : arr$) {
                if (a.name().equals(value.toUpperCase())) {
                    return a;
                }
            }
            return values[0];
        }
    }
    
    private class ScrollBarLayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            final Dimension upperButtonMin = ScrollBar.this.m_increaseButton.getMinSize();
            final Dimension lowerButtonMin = ScrollBar.this.m_decreaseButton.getMinSize();
            final Dimension sliderMin = ScrollBar.this.m_slider.getMinSize();
            if (ScrollBar.this.m_horizontal) {
                return new Dimension(upperButtonMin.width + lowerButtonMin.width + sliderMin.width, Math.max(upperButtonMin.height, Math.max(lowerButtonMin.height, sliderMin.height)));
            }
            return new Dimension(Math.max(upperButtonMin.width, Math.max(lowerButtonMin.width, sliderMin.width)), upperButtonMin.height + lowerButtonMin.height + sliderMin.height);
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            final Dimension upperButtonPref = ScrollBar.this.m_increaseButton.getPrefSize();
            final Dimension lowerButtonPref = ScrollBar.this.m_decreaseButton.getPrefSize();
            final Dimension sliderPref = ScrollBar.this.m_slider.getPrefSize();
            if (ScrollBar.this.m_horizontal) {
                return new Dimension(upperButtonPref.width + lowerButtonPref.width + sliderPref.width, Math.max(upperButtonPref.height, Math.max(lowerButtonPref.height, sliderPref.height)));
            }
            return new Dimension(Math.max(upperButtonPref.width, Math.max(lowerButtonPref.width, sliderPref.width)), upperButtonPref.height + lowerButtonPref.height + sliderPref.height);
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            if (ScrollBar.this.m_horizontal) {
                final int buttonHeight = ScrollBar.this.getAppearance().getContentHeight();
                ScrollBar.this.m_increaseButton.setSize((int)ScrollBar.this.m_increaseButton.getPrefSize().getWidth(), buttonHeight);
                ScrollBar.this.m_decreaseButton.setSize((int)ScrollBar.this.m_decreaseButton.getPrefSize().getWidth(), buttonHeight);
                ScrollBar.this.m_slider.setSize(ScrollBar.this.getAppearance().getContentWidth() - ScrollBar.this.m_increaseButton.getWidth() - ScrollBar.this.m_decreaseButton.getWidth(), buttonHeight);
            }
            else {
                final int buttonWidth = ScrollBar.this.getAppearance().getContentWidth();
                ScrollBar.this.m_increaseButton.setSize(buttonWidth, (int)ScrollBar.this.m_increaseButton.getPrefSize().getHeight());
                ScrollBar.this.m_decreaseButton.setSize(buttonWidth, (int)ScrollBar.this.m_decreaseButton.getPrefSize().getHeight());
                ScrollBar.this.m_slider.setSize(buttonWidth, ScrollBar.this.getAppearance().getContentHeight() - ScrollBar.this.m_increaseButton.getHeight() - ScrollBar.this.m_decreaseButton.getHeight());
            }
            ScrollBar.this.initPosition();
        }
    }
}
