package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.dragndrop.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.appearance.*;

public class ScrollContainer extends Container
{
    public static final String TAG = "ScrollContainer";
    public static final String THEME_HORIZONTAL_SCROLLBAR = "horizontalScrollBar";
    public static final String THEME_VERTICAL_SCROLLBAR = "verticalScrollBar";
    private ScrollBar m_verticalScrollBar;
    private ScrollBar m_horizontalScrollBar;
    private ScrollBar.ScrollBarBehaviour m_verticalScrollBarBehaviour;
    private ScrollBar.ScrollBarBehaviour m_horizontalScrollBarBehaviour;
    private boolean m_displayVerticalScrollBar;
    private boolean m_displayHorizontalScrollBar;
    private Alignment9 m_innerWidgetAlign;
    private boolean m_enableScrollBar;
    private float m_innerScissorPercentage;
    private boolean m_autoScrollVertical;
    private boolean m_autoScrollHorizontal;
    private DNDListenerContentValidator m_dndListenerContentValidator;
    private boolean m_autoScrollDirty;
    private float m_currentVerticalValue;
    private float m_currentHorizontalValue;
    private Alignment4 m_verticalScrollBarAlignment;
    private Alignment4 m_horizontalScrollBarAlignment;
    private boolean m_scrollContainerDirty;
    private Widget m_innerWidget;
    private float m_pageJumpRatio;
    private DragNDropListener m_dragNDropListener;
    public static final int ENABLE_SCROLL_BAR_HASH;
    public static final int HORIZONTAL_SCROLL_BAR_BEHAVIOUR_HASH;
    public static final int VERTICAL_SCROLL_BAR_BEHAVIOUR_HASH;
    public static final int HORIZONTAL_SCROLL_BAR_POSITION_HASH;
    public static final int VERTICAL_SCROLL_BAR_POSITION_HASH;
    public static final int HORIZONTAL_SCROLL_BAR_ALIGNMENT_HASH;
    public static final int VERTICAL_SCROLL_BAR_ALIGNMENT_HASH;
    public static final int AUTOSCROLL_HORIZONTAL_HASH;
    public static final int AUTOSCROLL_VERTICAL_HASH;
    public static final int INNER_WIDGET_ALIGN_HASH;
    public static final int PAGE_JUMP_RATIO_HASH;
    
    public ScrollContainer() {
        super();
        this.m_verticalScrollBarBehaviour = ScrollBar.ScrollBarBehaviour.WHEN_NEEDED;
        this.m_horizontalScrollBarBehaviour = ScrollBar.ScrollBarBehaviour.WHEN_NEEDED;
        this.m_displayVerticalScrollBar = false;
        this.m_displayHorizontalScrollBar = false;
        this.m_innerWidgetAlign = null;
        this.m_enableScrollBar = true;
        this.m_innerScissorPercentage = 1.0f;
        this.m_autoScrollVertical = false;
        this.m_autoScrollHorizontal = false;
        this.m_verticalScrollBarAlignment = Alignment4.EAST;
        this.m_horizontalScrollBarAlignment = Alignment4.SOUTH;
        this.m_scrollContainerDirty = false;
    }
    
    @Override
    public void addWidget(final Widget widget) {
        if (widget != this.m_verticalScrollBar && widget != this.m_horizontalScrollBar) {
            if (this.m_innerWidget != null) {
                this.destroy(this.m_innerWidget);
            }
            (this.m_innerWidget = widget).setSize(this.m_innerWidget.getPrefSize());
        }
        super.addWidget(widget);
    }
    
    @Override
    public String getTag() {
        return "ScrollContainer";
    }
    
    public Alignment4 getVerticalScrollBarAlignment() {
        return this.m_verticalScrollBarAlignment;
    }
    
    public void setVerticalScrollBarAlignment(final Alignment4 verticalScrollBarAlignment) {
        this.m_verticalScrollBarAlignment = verticalScrollBarAlignment;
    }
    
    public Alignment4 getHorizontalScrollBarAlignment() {
        return this.m_horizontalScrollBarAlignment;
    }
    
    public void setHorizontalScrollBarAlignment(final Alignment4 horizontalScrollBarAlignment) {
        this.m_horizontalScrollBarAlignment = horizontalScrollBarAlignment;
    }
    
    public ScrollBar.ScrollBarBehaviour getVerticalScrollBarBehaviour() {
        return this.m_verticalScrollBarBehaviour;
    }
    
    public void setVerticalScrollBarBehaviour(final ScrollBar.ScrollBarBehaviour verticalScrollBarBehaviour) {
        if (this.m_verticalScrollBarBehaviour == null || !this.m_verticalScrollBarBehaviour.equals(verticalScrollBarBehaviour)) {
            this.m_verticalScrollBarBehaviour = verticalScrollBarBehaviour;
            this.m_scrollContainerDirty = true;
            this.setNeedsToPreProcess();
        }
    }
    
    public void setVerticalScrollBarPosition(final float position) {
        this.m_verticalScrollBar.setValue(position);
    }
    
    public ScrollBar.ScrollBarBehaviour getHorizontalScrollBarBehaviour() {
        return this.m_horizontalScrollBarBehaviour;
    }
    
    public void setHorizontalScrollBarBehaviour(final ScrollBar.ScrollBarBehaviour horizontalScrollBarBehaviour) {
        if (this.m_horizontalScrollBarBehaviour == null || !this.m_horizontalScrollBarBehaviour.equals(horizontalScrollBarBehaviour)) {
            this.m_horizontalScrollBarBehaviour = horizontalScrollBarBehaviour;
            this.m_scrollContainerDirty = true;
            this.setNeedsToPreProcess();
        }
    }
    
    public void setHorizontalScrollBarPosition(final float position) {
        this.m_horizontalScrollBar.setValue(position);
    }
    
    public boolean isEnableScrollBar() {
        return this.m_enableScrollBar;
    }
    
    public void setEnableScrollBar(final boolean enableScrollBar) {
        this.m_enableScrollBar = enableScrollBar;
        this.m_scrollContainerDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public Alignment9 getInnerWidgetAlign() {
        return this.m_innerWidgetAlign;
    }
    
    public void setInnerWidgetAlign(final Alignment9 innerWidgetAlign) {
        this.m_innerWidgetAlign = innerWidgetAlign;
    }
    
    public ScrollBar getVerticalScrollBar() {
        return this.m_verticalScrollBar;
    }
    
    public ScrollBar getHorizontalScrollBar() {
        return this.m_horizontalScrollBar;
    }
    
    public float getPageJumpRatio() {
        return this.m_pageJumpRatio;
    }
    
    public void setAutoScrollVertical(final boolean autoScrollVertical) {
        this.m_autoScrollVertical = autoScrollVertical;
    }
    
    public void setAutoScrollHorizontal(final boolean autoScrollHorizontal) {
        this.m_autoScrollHorizontal = autoScrollHorizontal;
    }
    
    public void setDndListenerContentValidator(final DNDListenerContentValidator dndListenerContentValidator) {
        this.m_dndListenerContentValidator = dndListenerContentValidator;
        if (this.m_dragNDropListener == null) {
            this.m_dragNDropListener = new DragNDropListener() {
                private static final int MAXIMUM_BOUND = 50;
                private static final long DELAY = 500L;
                private long m_startTime;
                
                @Override
                public void onDrag(final DragNDropHandler dragNDropHandler, final int displayX, final int displayY, final Widget dragOver) {
                    if (ScrollContainer.this.m_containerParent == null) {
                        return;
                    }
                    ScrollContainer.this.m_autoScrollDirty = false;
                    if (ScrollContainer.this.m_autoScrollHorizontal) {
                        if (displayX < ScrollContainer.this.getDisplayX() && displayX > ScrollContainer.this.getDisplayX() - 50) {
                            final int deltaX = Math.abs(ScrollContainer.this.getDisplayX() - displayX);
                            final float valueX = deltaX / ScrollContainer.this.m_innerWidget.getWidth();
                            ScrollContainer.this.m_currentHorizontalValue = -valueX;
                            this.checkDelay();
                        }
                        else if (displayX > ScrollContainer.this.getDisplayX() + ScrollContainer.this.getWidth() && displayX < ScrollContainer.this.getDisplayX() + ScrollContainer.this.getWidth() + 50) {
                            final int deltaX = Math.abs(ScrollContainer.this.getDisplayX() + ScrollContainer.this.getWidth() - displayX);
                            final float valueX = deltaX / ScrollContainer.this.m_innerWidget.getWidth();
                            ScrollContainer.this.m_currentHorizontalValue = valueX;
                            this.checkDelay();
                        }
                    }
                    if (ScrollContainer.this.m_autoScrollVertical) {
                        if (displayY < ScrollContainer.this.getDisplayY() && displayY > ScrollContainer.this.getDisplayY() - 50) {
                            final int deltaY = Math.abs(ScrollContainer.this.getDisplayY() - displayY);
                            final float valueY = deltaY / ScrollContainer.this.m_innerWidget.getHeight();
                            ScrollContainer.this.m_currentVerticalValue = -valueY;
                            this.checkDelay();
                        }
                        else if (displayY > ScrollContainer.this.getDisplayY() + ScrollContainer.this.getHeight() && displayY < ScrollContainer.this.getDisplayY() + ScrollContainer.this.getHeight() + 50) {
                            final int deltaY = Math.abs(ScrollContainer.this.getDisplayY() + ScrollContainer.this.getHeight() - displayY);
                            final float valueY = deltaY / ScrollContainer.this.m_innerWidget.getHeight();
                            ScrollContainer.this.m_currentVerticalValue = valueY;
                            this.checkDelay();
                        }
                    }
                    if (ScrollContainer.this.m_autoScrollDirty) {
                        ScrollContainer.this.setNeedsToPreProcess();
                    }
                }
                
                private void checkDelay() {
                    if (this.m_startTime == 0L) {
                        this.m_startTime = System.currentTimeMillis();
                    }
                    if (this.m_startTime + 500L < System.currentTimeMillis()) {
                        ScrollContainer.this.m_autoScrollDirty = true;
                    }
                }
                
                @Override
                public void onDrop(final DragNDropHandler dragNDropHandler, final int displayX, final int displayY, final Widget droppedOn) {
                    ScrollContainer.this.m_currentVerticalValue = 0.0f;
                    ScrollContainer.this.m_currentHorizontalValue = 0.0f;
                    ScrollContainer.this.m_autoScrollDirty = false;
                    ScrollContainer.this.setNeedsToPreProcess();
                    this.m_startTime = 0L;
                }
                
                @Override
                public boolean validateContent(final Object content) {
                    return ScrollContainer.this.m_dndListenerContentValidator == null || ScrollContainer.this.m_dndListenerContentValidator.validate(content);
                }
            };
            DragNDropManager.getInstance().addDNDListener(this.m_dragNDropListener);
        }
    }
    
    public void setPageJumpRatio(final float pageJumpRatio) {
        this.m_pageJumpRatio = pageJumpRatio;
    }
    
    @Override
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        if ("horizontalScrollBar".equalsIgnoreCase(themeElementName)) {
            return this.m_horizontalScrollBar;
        }
        if ("verticalScrollBar".equalsIgnoreCase(themeElementName)) {
            return this.m_verticalScrollBar;
        }
        return null;
    }
    
    @Override
    public PooledRectangle getScissor(final Widget widget) {
        if (widget == this.m_verticalScrollBar || widget == this.m_horizontalScrollBar) {
            return PooledRectangle.checkout(this.getScreenX() + this.m_appearance.getLeftInset(), this.getScreenY() + this.m_appearance.getBottomInset(), this.m_size.width - this.m_appearance.getLeftInset() - this.m_appearance.getRightInset(), this.m_size.height - this.m_appearance.getBottomInset() - this.m_appearance.getTopInset());
        }
        return PooledRectangle.checkout(this.getScreenX() + this.m_appearance.getLeftInset() + ((this.m_displayVerticalScrollBar && this.m_verticalScrollBarAlignment.equals(Alignment4.WEST)) ? this.m_verticalScrollBar.getWidth() : 0), this.getScreenY() + this.m_appearance.getBottomInset() + ((this.m_displayHorizontalScrollBar && this.m_horizontalScrollBarAlignment.equals(Alignment4.SOUTH)) ? this.m_horizontalScrollBar.getHeight() : 0), this.m_size.width - this.m_appearance.getLeftInset() - this.m_appearance.getRightInset() - (this.m_displayVerticalScrollBar ? this.m_verticalScrollBar.getWidth() : 0), (int)(this.m_innerScissorPercentage * (this.m_size.height - this.m_appearance.getBottomInset() - this.m_appearance.getTopInset() - (this.m_displayHorizontalScrollBar ? this.m_horizontalScrollBar.getHeight() : 0))));
    }
    
    @Override
    public Widget getWidget(int x, int y) {
        if (this.m_unloading || !this.m_visible || !this.getAppearance().insideInsets(x, y) || MasterRootContainer.getInstance().isMovePointMode()) {
            return null;
        }
        x -= this.getAppearance().getLeftInset();
        y -= this.getAppearance().getBottomInset();
        Widget w = null;
        if (this.m_displayHorizontalScrollBar && !this.m_horizontalScrollBar.isUnloading()) {
            w = this.m_horizontalScrollBar.getWidget(x - this.m_horizontalScrollBar.m_position.x, y - this.m_horizontalScrollBar.m_position.y);
            if (w != null) {
                return w;
            }
        }
        if (this.m_displayVerticalScrollBar && !this.m_verticalScrollBar.isUnloading()) {
            w = this.m_verticalScrollBar.getWidget(x - this.m_verticalScrollBar.m_position.x, y - this.m_verticalScrollBar.m_position.y);
            if (w != null) {
                return w;
            }
        }
        if (this.m_displayVerticalScrollBar && this.m_displayHorizontalScrollBar && y < this.m_verticalScrollBar.getY()) {
            return this.m_nonBlocking ? null : this;
        }
        if (this.m_innerWidget != null && !this.m_innerWidget.isUnloading()) {
            w = this.m_innerWidget.getWidget(x - this.m_innerWidget.m_position.x, y - this.m_innerWidget.m_position.y);
        }
        return (w != null) ? w : (this.m_nonBlocking ? null : this);
    }
    
    public void scrollVerticalScrollBar(final float percent) {
        this.m_verticalScrollBar.setValue(percent);
    }
    
    public void scrollHorizontalScrollBar(final float percent) {
        this.m_horizontalScrollBar.setValue(percent);
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_verticalScrollBar = null;
        this.m_horizontalScrollBar = null;
        if (this.m_dragNDropListener != null) {
            DragNDropManager.getInstance().removeDNDListener(this.m_dragNDropListener);
            this.m_dragNDropListener = null;
        }
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final ScrollContainerLayout scl = new ScrollContainerLayout();
        scl.onCheckOut();
        this.add(scl);
        this.m_nonBlocking = false;
        this.enableEvent(Events.MOUSE_PRESSED, false);
        this.enableEvent(Events.MOUSE_RELEASED, false);
        this.enableEvent(Events.MOUSE_CLICKED, false);
        this.enableEvent(Events.MOUSE_DOUBLE_CLICKED, false);
        this.enableEvent(Events.MOUSE_DRAGGED, false);
        this.enableEvent(Events.MOUSE_DRAGGED_IN, false);
        this.enableEvent(Events.MOUSE_DRAGGED_OUT, false);
        this.m_innerScissorPercentage = 1.0f;
        this.m_pageJumpRatio = 0.5f;
        (this.m_verticalScrollBar = new ScrollBar()).onCheckOut();
        this.m_verticalScrollBar.setCanBeCloned(false);
        this.m_verticalScrollBar.setHorizontal(false);
        this.add(this.m_verticalScrollBar);
        this.m_verticalScrollBar.addEventListener(Events.SLIDER_MOVED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final SliderMovedEvent sliderMovedEvent = (SliderMovedEvent)event;
                final double position = sliderMovedEvent.getValue();
                ScrollContainer.this.computeInnerWidgetVerticalPosition(position);
                return false;
            }
        }, false);
        (this.m_horizontalScrollBar = new ScrollBar()).onCheckOut();
        this.m_horizontalScrollBar.setCanBeCloned(false);
        this.m_horizontalScrollBar.setHorizontal(true);
        this.add(this.m_horizontalScrollBar);
        this.m_horizontalScrollBar.addEventListener(Events.SLIDER_MOVED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final SliderMovedEvent sliderMovedEvent = (SliderMovedEvent)event;
                final double position = sliderMovedEvent.getValue();
                ScrollContainer.this.computeInnerWidgetHorizontalPosition(position);
                return false;
            }
        }, false);
        this.addEventListener(Events.MOUSE_WHEELED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent e = (MouseEvent)event;
                if (ScrollContainer.this.m_displayVerticalScrollBar) {
                    ScrollContainer.this.m_verticalScrollBar.setValue(ScrollContainer.this.m_verticalScrollBar.getValue() - ScrollContainer.this.m_verticalScrollBar.getButtonJump() * e.getRotations());
                }
                else if (ScrollContainer.this.m_displayHorizontalScrollBar) {
                    ScrollContainer.this.m_horizontalScrollBar.setValue(ScrollContainer.this.m_horizontalScrollBar.getValue() + ScrollContainer.this.m_horizontalScrollBar.getButtonJump() * e.getRotations());
                }
                return true;
            }
        }, false);
        this.m_enableScrollBar = true;
        this.m_needsScissor = true;
    }
    
    public void resetScrollContainerScissorRatio() {
        this.removeTweensOfType(ScrollContainerScissorShrinkTween.class);
        this.m_innerScissorPercentage = 1.0f;
    }
    
    public void triggerScrollContainerScissorShrink() {
        this.addTween(new ScrollContainerScissorShrinkTween(this.m_innerScissorPercentage, 0.0f, this, 10000, 5000, TweenFunction.PROGRESSIVE));
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_scrollContainerDirty) {
            this.invalidateMinSize();
            this.m_scrollContainerDirty = false;
        }
        if (this.m_autoScrollDirty) {
            this.m_verticalScrollBar.setValue(MathHelper.clamp(this.m_verticalScrollBar.getValue() + this.m_currentVerticalValue, 0.0f, 1.0f));
            this.m_horizontalScrollBar.setValue(MathHelper.clamp(this.m_horizontalScrollBar.getValue() + this.m_currentHorizontalValue, 0.0f, 1.0f));
            return true;
        }
        return ret;
    }
    
    @Override
    public void copyElement(final BasicElement s) {
        final ScrollContainer e = (ScrollContainer)s;
        super.copyElement(e);
        e.m_horizontalScrollBarBehaviour = this.m_horizontalScrollBarBehaviour;
        e.m_verticalScrollBarBehaviour = this.m_verticalScrollBarBehaviour;
        e.m_innerWidgetAlign = this.m_innerWidgetAlign;
        e.m_enableScrollBar = this.m_enableScrollBar;
        e.m_pageJumpRatio = this.m_pageJumpRatio;
        e.m_dndListenerContentValidator = this.m_dndListenerContentValidator;
        e.m_autoScrollVertical = this.m_autoScrollVertical;
        e.m_autoScrollHorizontal = this.m_autoScrollHorizontal;
        e.m_styleIsDirty = true;
        e.setNeedsToPreProcess();
    }
    
    private void computeInnerWidgetPosition() {
        this.computeInnerWidgetHorizontalPosition(this.m_horizontalScrollBar.getSlider().getValue());
        this.computeInnerWidgetVerticalPosition(this.m_verticalScrollBar.getSlider().getValue());
    }
    
    private void computeInnerWidgetHorizontalPosition(final double position) {
        final int innerWidgetWidth = this.m_innerWidget.getSize().width;
        int displayAreaWidth = this.m_appearance.getContentWidth();
        if (this.m_displayVerticalScrollBar) {
            displayAreaWidth -= (int)this.m_verticalScrollBar.getSize().getWidth();
        }
        int x;
        if (this.m_innerWidgetAlign == null || innerWidgetWidth - displayAreaWidth > 0) {
            x = -(int)((innerWidgetWidth - displayAreaWidth) * position);
        }
        else {
            x = this.m_innerWidgetAlign.getX(innerWidgetWidth, displayAreaWidth);
        }
        if (this.m_displayVerticalScrollBar && this.m_verticalScrollBarAlignment.equals(Alignment4.WEST)) {
            x += (int)this.m_verticalScrollBar.getSize().getWidth();
        }
        this.m_innerWidget.setX(x);
    }
    
    private void computeInnerWidgetVerticalPosition(final double position) {
        final int innerWidgetHeight = this.m_innerWidget.getSize().height;
        int displayAreaHeight = this.m_appearance.getContentHeight();
        if (this.m_displayHorizontalScrollBar) {
            displayAreaHeight -= (int)this.m_horizontalScrollBar.getSize().getHeight();
        }
        int y;
        if (this.m_innerWidgetAlign == null || innerWidgetHeight - displayAreaHeight > 0) {
            y = -(int)((innerWidgetHeight - displayAreaHeight) * position);
        }
        else {
            y = this.m_innerWidgetAlign.getY(innerWidgetHeight, displayAreaHeight);
        }
        if (this.m_displayHorizontalScrollBar && this.m_horizontalScrollBarAlignment.equals(Alignment4.SOUTH)) {
            y += (int)this.m_horizontalScrollBar.getSize().getHeight();
        }
        this.m_innerWidget.setY(y);
    }
    
    public boolean needVerticalScroll() {
        return this.getAppearance().getContentHeight() < this.m_innerWidget.getPrefSize().getHeight();
    }
    
    public boolean needHorizontalScroll() {
        return this.getAppearance().getContentWidth() < this.m_innerWidget.getPrefSize().getWidth();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ScrollContainer.ENABLE_SCROLL_BAR_HASH) {
            this.setEnableScrollBar(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ScrollContainer.HORIZONTAL_SCROLL_BAR_BEHAVIOUR_HASH) {
            this.setHorizontalScrollBarBehaviour(ScrollBar.ScrollBarBehaviour.value(value));
        }
        else if (hash == ScrollContainer.VERTICAL_SCROLL_BAR_BEHAVIOUR_HASH) {
            this.setVerticalScrollBarBehaviour(ScrollBar.ScrollBarBehaviour.value(value));
        }
        else if (hash == ScrollContainer.INNER_WIDGET_ALIGN_HASH) {
            this.setInnerWidgetAlign(Alignment9.value(value));
        }
        else if (hash == ScrollContainer.HORIZONTAL_SCROLL_BAR_POSITION_HASH) {
            this.setHorizontalScrollBarPosition(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ScrollContainer.VERTICAL_SCROLL_BAR_POSITION_HASH) {
            this.setVerticalScrollBarPosition(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ScrollContainer.HORIZONTAL_SCROLL_BAR_ALIGNMENT_HASH) {
            this.setHorizontalScrollBarAlignment(Alignment4.value(value));
        }
        else if (hash == ScrollContainer.VERTICAL_SCROLL_BAR_ALIGNMENT_HASH) {
            this.setVerticalScrollBarAlignment(Alignment4.value(value));
        }
        else if (hash == ScrollContainer.PAGE_JUMP_RATIO_HASH) {
            this.setPageJumpRatio(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ScrollContainer.AUTOSCROLL_HORIZONTAL_HASH) {
            this.setAutoScrollHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != ScrollContainer.AUTOSCROLL_VERTICAL_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setAutoScrollVertical(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == ScrollContainer.ENABLE_SCROLL_BAR_HASH) {
            this.setEnableScrollBar(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == ScrollContainer.HORIZONTAL_SCROLL_BAR_BEHAVIOUR_HASH) {
            this.setHorizontalScrollBarBehaviour((ScrollBar.ScrollBarBehaviour)value);
        }
        else if (hash == ScrollContainer.VERTICAL_SCROLL_BAR_BEHAVIOUR_HASH) {
            this.setVerticalScrollBarBehaviour((ScrollBar.ScrollBarBehaviour)value);
        }
        else if (hash == ScrollContainer.HORIZONTAL_SCROLL_BAR_POSITION_HASH) {
            this.setHorizontalScrollBarPosition(PrimitiveConverter.getFloat(value));
        }
        else if (hash == ScrollContainer.VERTICAL_SCROLL_BAR_POSITION_HASH) {
            this.setVerticalScrollBarPosition(PrimitiveConverter.getFloat(value));
        }
        else {
            if (hash != ScrollContainer.INNER_WIDGET_ALIGN_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setInnerWidgetAlign((Alignment9)value);
        }
        return true;
    }
    
    static {
        ENABLE_SCROLL_BAR_HASH = "enableScrollBar".hashCode();
        HORIZONTAL_SCROLL_BAR_BEHAVIOUR_HASH = "horizontalScrollBarBehaviour".hashCode();
        VERTICAL_SCROLL_BAR_BEHAVIOUR_HASH = "verticalScrollBarBehaviour".hashCode();
        HORIZONTAL_SCROLL_BAR_POSITION_HASH = "horizontalScrollBarPosition".hashCode();
        VERTICAL_SCROLL_BAR_POSITION_HASH = "verticalScrollBarPosition".hashCode();
        HORIZONTAL_SCROLL_BAR_ALIGNMENT_HASH = "horizontalScrollBarAlignment".hashCode();
        VERTICAL_SCROLL_BAR_ALIGNMENT_HASH = "verticalScrollBarAlignment".hashCode();
        AUTOSCROLL_HORIZONTAL_HASH = "autoScrollHorizontal".hashCode();
        AUTOSCROLL_VERTICAL_HASH = "autoScrollVertical".hashCode();
        INNER_WIDGET_ALIGN_HASH = "innerWidgetAlign".hashCode();
        PAGE_JUMP_RATIO_HASH = "pageJumpRatio".hashCode();
    }
    
    public class ScrollContainerScissorShrinkTween extends AbstractWidgetTween<Float>
    {
        public ScrollContainerScissorShrinkTween(final float a, final float b, final ScrollContainer w, final int delay, final int duration, final TweenFunction function) {
            super(a, b, w, delay, duration, function);
        }
        
        @Override
        public boolean process(final int deltaTime) {
            if (!super.process(deltaTime)) {
                return false;
            }
            if (this.m_function != null) {
                final float newValue = this.m_function.compute((float)this.m_a, (float)this.m_b, this.m_elapsedTime, this.m_duration);
                if (newValue <= ScrollContainer.this.m_innerScissorPercentage) {
                    ScrollContainer.this.m_innerScissorPercentage = newValue;
                }
            }
            return true;
        }
    }
    
    private class ScrollContainerLayout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container scrollContainer) {
            if (!ScrollContainer.this.m_enableScrollBar) {
                return new Dimension(0, 0);
            }
            int minHeight = 0;
            int minWidth = 0;
            final Dimension verticalMinSize = ScrollContainer.this.m_verticalScrollBar.getMinSize();
            final Dimension horizontalMinSize = ScrollContainer.this.m_horizontalScrollBar.getMinSize();
            final Dimension minSize = ScrollContainer.this.m_innerWidget.getMinSize();
            if (!ScrollContainer.this.m_horizontalScrollBarBehaviour.equals(ScrollBar.ScrollBarBehaviour.FORCE_HIDE)) {
                minHeight += horizontalMinSize.height;
                minWidth += horizontalMinSize.width;
            }
            else {
                minWidth += minSize.width;
            }
            if (!ScrollContainer.this.m_verticalScrollBarBehaviour.equals(ScrollBar.ScrollBarBehaviour.FORCE_HIDE)) {
                minWidth += verticalMinSize.width;
                minHeight += verticalMinSize.height;
            }
            else {
                minHeight += minSize.height;
            }
            return new Dimension(minWidth, minHeight);
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            if (!ScrollContainer.this.m_enableScrollBar) {
                return new Dimension(0, 0);
            }
            int minHeight = 0;
            int minWidth = 0;
            final Dimension verticalMinSize = ScrollContainer.this.m_verticalScrollBar.getPrefSize();
            final Dimension horizontalMinSize = ScrollContainer.this.m_horizontalScrollBar.getPrefSize();
            final Dimension minSize = ScrollContainer.this.m_innerWidget.getPrefSize();
            if (!ScrollContainer.this.m_horizontalScrollBarBehaviour.equals(ScrollBar.ScrollBarBehaviour.FORCE_HIDE)) {
                if (ScrollContainer.this.m_maxSize != null) {
                    if (minSize.width <= ScrollContainer.this.m_maxSize.width) {
                        minWidth += minSize.width;
                        if (ScrollContainer.this.m_horizontalScrollBarBehaviour == ScrollBar.ScrollBarBehaviour.FORCE_DISPLAY) {
                            minHeight += horizontalMinSize.height;
                        }
                    }
                    else {
                        minWidth += Math.max(ScrollContainer.this.m_maxSize.width, horizontalMinSize.width);
                        minHeight += horizontalMinSize.height;
                    }
                }
                else {
                    minHeight += horizontalMinSize.height;
                    minWidth += horizontalMinSize.width;
                }
            }
            else {
                minWidth += minSize.width;
            }
            if (!ScrollContainer.this.m_verticalScrollBarBehaviour.equals(ScrollBar.ScrollBarBehaviour.FORCE_HIDE)) {
                if (ScrollContainer.this.m_maxSize != null) {
                    if (minSize.height <= ScrollContainer.this.m_maxSize.height) {
                        minHeight += minSize.height;
                        if (ScrollContainer.this.m_verticalScrollBarBehaviour == ScrollBar.ScrollBarBehaviour.FORCE_DISPLAY) {
                            minWidth += verticalMinSize.width;
                        }
                    }
                    else {
                        minHeight += Math.max(ScrollContainer.this.m_maxSize.height, verticalMinSize.height);
                        minWidth += verticalMinSize.width;
                    }
                }
                else {
                    minWidth += verticalMinSize.width;
                    minHeight += verticalMinSize.height;
                }
            }
            else {
                minHeight += minSize.height;
            }
            return new Dimension(minWidth, minHeight);
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            if (ScrollContainer.this.m_innerWidget != null) {
                final DecoratorAppearance scrollContainerAppearance = ScrollContainer.this.getAppearance();
                int displayedAreaHeight = scrollContainerAppearance.getContentHeight();
                int displayedAreaWidth = scrollContainerAppearance.getContentWidth();
                if (ScrollContainer.this.m_innerWidget instanceof TextWidget) {
                    ((TextWidget)ScrollContainer.this.m_innerWidget).setTextWidgetSize(displayedAreaWidth, displayedAreaHeight, true);
                    ((TextWidget)ScrollContainer.this.m_innerWidget).getTextBuilder().computeMinSize();
                }
                if (!ScrollContainer.this.m_enableScrollBar || (displayedAreaWidth >= ScrollContainer.this.m_innerWidget.getPrefSize().getWidth() && !ScrollContainer.this.m_horizontalScrollBarBehaviour.equals(ScrollBar.ScrollBarBehaviour.FORCE_DISPLAY)) || ScrollContainer.this.m_horizontalScrollBarBehaviour.equals(ScrollBar.ScrollBarBehaviour.FORCE_HIDE)) {
                    ScrollContainer.this.m_displayHorizontalScrollBar = false;
                }
                else {
                    ScrollContainer.this.m_displayHorizontalScrollBar = true;
                    displayedAreaHeight -= (int)ScrollContainer.this.m_horizontalScrollBar.getPrefSize().getHeight();
                }
                if (!ScrollContainer.this.m_enableScrollBar || (displayedAreaHeight >= ScrollContainer.this.m_innerWidget.getPrefSize().getHeight() && !ScrollContainer.this.m_verticalScrollBarBehaviour.equals(ScrollBar.ScrollBarBehaviour.FORCE_DISPLAY)) || ScrollContainer.this.m_verticalScrollBarBehaviour.equals(ScrollBar.ScrollBarBehaviour.FORCE_HIDE)) {
                    ScrollContainer.this.m_displayVerticalScrollBar = false;
                }
                else {
                    displayedAreaWidth -= (int)ScrollContainer.this.m_verticalScrollBar.getPrefSize().getWidth();
                    if (ScrollContainer.this.m_innerWidget instanceof TextWidget) {
                        ((TextWidget)ScrollContainer.this.m_innerWidget).setTextWidgetSize(displayedAreaWidth, ScrollContainer.this.m_innerWidget.getHeight(), true);
                        ((TextWidget)ScrollContainer.this.m_innerWidget).getTextBuilder().computeMinSize();
                    }
                    else if (!ScrollContainer.this.m_displayVerticalScrollBar) {
                        ScrollContainer.this.m_verticalScrollBar.setValue(1.0f);
                    }
                    ScrollContainer.this.m_displayVerticalScrollBar = true;
                }
                if (ScrollContainer.this.m_displayVerticalScrollBar && !ScrollContainer.this.m_displayHorizontalScrollBar) {
                    if ((displayedAreaWidth >= ScrollContainer.this.m_innerWidget.getPrefSize().getWidth() && !ScrollContainer.this.m_horizontalScrollBarBehaviour.equals(ScrollBar.ScrollBarBehaviour.FORCE_DISPLAY)) || ScrollContainer.this.m_horizontalScrollBarBehaviour.equals(ScrollBar.ScrollBarBehaviour.FORCE_HIDE)) {
                        ScrollContainer.this.m_displayHorizontalScrollBar = false;
                    }
                    else {
                        ScrollContainer.this.m_displayHorizontalScrollBar = true;
                        displayedAreaHeight -= (int)ScrollContainer.this.m_horizontalScrollBar.getPrefSize().getHeight();
                    }
                }
                int innerWidgetWidth;
                if (ScrollContainer.this.m_innerWidgetAlign != null && ScrollContainer.this.m_horizontalScrollBarBehaviour != ScrollBar.ScrollBarBehaviour.FORCE_HIDE) {
                    innerWidgetWidth = ScrollContainer.this.m_innerWidget.getPrefSize().width;
                }
                else {
                    innerWidgetWidth = (int)Math.max(displayedAreaWidth, ScrollContainer.this.m_innerWidget.getPrefSize().getWidth());
                }
                int innerWidgetHeight;
                if (ScrollContainer.this.m_innerWidgetAlign != null && ScrollContainer.this.m_verticalScrollBarBehaviour != ScrollBar.ScrollBarBehaviour.FORCE_HIDE) {
                    innerWidgetHeight = ScrollContainer.this.m_innerWidget.getPrefSize().height;
                }
                else {
                    innerWidgetHeight = (int)Math.max(displayedAreaHeight, ScrollContainer.this.m_innerWidget.getPrefSize().getHeight());
                }
                ScrollContainer.this.m_innerWidget.setSize(innerWidgetWidth, innerWidgetHeight);
                ScrollContainer.this.computeInnerWidgetPosition();
                if (ScrollContainer.this.m_displayHorizontalScrollBar) {
                    ScrollContainer.this.m_horizontalScrollBar.setSize(displayedAreaWidth, (int)ScrollContainer.this.m_horizontalScrollBar.getPrefSize().getHeight());
                }
                if (ScrollContainer.this.m_displayVerticalScrollBar) {
                    ScrollContainer.this.m_verticalScrollBar.setSize((int)ScrollContainer.this.m_verticalScrollBar.getPrefSize().getWidth(), displayedAreaHeight);
                }
                if (ScrollContainer.this.m_displayHorizontalScrollBar) {
                    int x = 0;
                    int y = 0;
                    if (ScrollContainer.this.m_displayVerticalScrollBar && ScrollContainer.this.m_verticalScrollBarAlignment.equals(Alignment4.WEST)) {
                        x += ScrollContainer.this.m_verticalScrollBar.getWidth();
                    }
                    if (ScrollContainer.this.m_horizontalScrollBarAlignment.equals(Alignment4.NORTH)) {
                        y += displayedAreaHeight;
                    }
                    ScrollContainer.this.m_horizontalScrollBar.setPosition(x, y);
                    ScrollContainer.this.m_horizontalScrollBar.setSliderSize(displayedAreaWidth / innerWidgetWidth);
                    final float jumpRatio = (innerWidgetWidth / displayedAreaWidth - 1.0f) * ScrollContainer.this.m_pageJumpRatio;
                    ScrollContainer.this.m_horizontalScrollBar.setButtonJump(1.0f / Math.max(1.0f, jumpRatio));
                }
                if (ScrollContainer.this.m_displayVerticalScrollBar) {
                    int x = 0;
                    int y = 0;
                    if (ScrollContainer.this.m_displayHorizontalScrollBar && ScrollContainer.this.m_horizontalScrollBarAlignment.equals(Alignment4.SOUTH)) {
                        y += ScrollContainer.this.m_horizontalScrollBar.getHeight();
                    }
                    if (ScrollContainer.this.m_verticalScrollBarAlignment.equals(Alignment4.EAST)) {
                        x += displayedAreaWidth;
                    }
                    ScrollContainer.this.m_verticalScrollBar.setPosition(x, y);
                    ScrollContainer.this.m_verticalScrollBar.setSliderSize(displayedAreaHeight / innerWidgetHeight);
                    final float jumpRatio = 1.0f / Math.max(1.0f, innerWidgetHeight / displayedAreaHeight - 1.0f) * ScrollContainer.this.m_pageJumpRatio;
                    ScrollContainer.this.m_verticalScrollBar.setButtonJump(jumpRatio);
                }
                ScrollContainer.this.m_horizontalScrollBar.setVisible(ScrollContainer.this.m_displayHorizontalScrollBar);
                ScrollContainer.this.m_verticalScrollBar.setVisible(ScrollContainer.this.m_displayVerticalScrollBar);
            }
        }
    }
}
