package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.nongraphical.*;
import java.awt.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.taglibrary.*;
import com.ankamagames.xulor2.core.factory.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;

public abstract class Widget extends EventDispatcher implements SwitchClient, PopupClient
{
    protected DecoratorAppearance m_appearance;
    protected DecoratorAppearance m_xmlAppearance;
    protected Container m_containerParent;
    protected EntityGroup m_entity;
    protected Point m_position;
    private float m_xPercInParent;
    private float m_yPercInParent;
    protected Point m_screenPosition;
    public Dimension m_size;
    protected Dimension m_minSize;
    protected boolean m_minSizeSet;
    protected Dimension m_prefSize;
    protected boolean m_prefSizeSet;
    protected Dimension m_maxSize;
    protected boolean m_maxSizeSet;
    protected boolean m_greedy;
    protected boolean m_expandable;
    protected boolean m_shrinkable;
    protected boolean m_nonBlocking;
    protected boolean m_visible;
    protected boolean m_parentVisible;
    protected boolean m_enabled;
    protected boolean m_netEnabled;
    protected String m_netEnabledId;
    protected boolean m_focusable;
    protected boolean m_focused;
    protected String[] m_style;
    protected boolean m_styleIsDirty;
    protected HashMap<String, Widget> m_themeElementWidgets;
    protected String m_themeElementName;
    protected String m_themeElementParentType;
    protected AbstractLayoutData m_layoutData;
    protected DragNDropContainer m_dndParent;
    protected PopupElement m_popup;
    protected CursorFactory.CursorType m_cursorType;
    protected boolean m_useResizeTween;
    protected boolean m_usePositionTween;
    protected boolean m_needToResetMeshes;
    protected Rectangle m_scissor;
    protected boolean m_needsScissor;
    private boolean m_positionChanged;
    protected boolean m_enableResizeEvents;
    protected boolean m_enablePositionEvents;
    protected boolean m_stickWithinContainer;
    public static final int SIZE_HASH;
    public static final int PREF_SIZE_HASH;
    public static final int MAX_SIZE_HASH;
    public static final int EXPANDABLE_HASH;
    public static final int SHRINKABLE_HASH;
    public static final int GREEDY_HASH;
    public static final int FOCUSABLE_HASH;
    public static final int FOCUSED_HASH;
    public static final int ENABLED_HASH;
    public static final int NET_ENABLED_ID_HASH;
    public static final int VISIBLE_HASH;
    public static final int USED_IN_LAYOUT_HASH;
    public static final int USE_POSITION_TWEEN_HASH;
    public static final int USE_RESIZE_TWEEN_HASH;
    public static final int X_HASH;
    public static final int Y_HASH;
    public static final int STYLE_HASH;
    public static final int THEME_ELEMENT_NAME_HASH;
    public static final int THEME_ELEMENT_PARENT_TYPE_HASH;
    public static final int NON_BLOCKING_HASH;
    public static final int CURSOR_TYPE_HASH;
    public static final int NEEDS_SCISSOR_HASH;
    public static final int USER_DEFINED_SIZE_HASH;
    public static final int USER_DEFINED_POSITION_HASH;
    public static final int STICK_WITHIN_CONTAINER_HASH;
    public static final int ON_CLICK_HASH;
    public static final int ON_DOUBLE_CLICK_HASH;
    public static final int ON_FOCUS_CHANGE_HASH;
    public static final int ON_ITEM_CLICK_HASH;
    public static final int ON_ITEM_DOUBLE_CLICK_HASH;
    public static final int ON_ITEM_OUT_HASH;
    public static final int ON_ITEM_OVER_HASH;
    public static final int ON_KEY_PRESS_HASH;
    public static final int ON_KEY_RELEASE_HASH;
    public static final int ON_KEY_TYPE_HASH;
    public static final int ON_LIST_SELECTION_CHANGE_HASH;
    public static final int ON_MOUSE_DRAG_HASH;
    public static final int ON_MOUSE_DRAG_IN_HASH;
    public static final int ON_MOUSE_DRAG_OUT_HASH;
    public static final int ON_MOUSE_MOVE_HASH;
    public static final int ON_MOUSE_ENTER_HASH;
    public static final int ON_MOUSE_EXIT_HASH;
    public static final int ON_MOUSE_PRESS_HASH;
    public static final int ON_MOUSE_RELEASE_HASH;
    public static final int ON_MOUSE_WHEEL_HASH;
    public static final int ON_SELECTION_CHANGE_HASH;
    public static final int ON_SLIDER_MOVE_HASH;
    public static final int ON_VALUE_CHANGE_HASH;
    public static final int ON_DRAG_HASH;
    public static final int ON_DROP_HASH;
    public static final int ON_DRAG_OUT_HASH;
    public static final int ON_DROP_OUT_HASH;
    public static final int ON_DRAG_OVER_HASH;
    public static final int ON_STICK_HASH;
    public static final int ON_POPUP_DISPLAY_HASH;
    public static final int ON_POPUP_HIDE_HASH;
    
    public Widget() {
        super();
        this.m_screenPosition = new Point(-1, -1);
        this.m_minSizeSet = false;
        this.m_prefSizeSet = false;
        this.m_maxSizeSet = false;
        this.m_greedy = false;
        this.m_expandable = true;
        this.m_shrinkable = false;
        this.m_nonBlocking = false;
        this.m_visible = false;
        this.m_parentVisible = false;
        this.m_enabled = true;
        this.m_netEnabled = true;
        this.m_netEnabledId = null;
        this.m_focusable = false;
        this.m_focused = false;
        this.m_style = new String[5];
        this.m_styleIsDirty = false;
        this.m_themeElementWidgets = null;
        this.m_cursorType = CursorFactory.CursorType.DEFAULT;
        this.m_useResizeTween = false;
        this.m_usePositionTween = false;
        this.m_needToResetMeshes = true;
        this.m_scissor = null;
        this.m_needsScissor = false;
        this.m_positionChanged = true;
        this.m_enableResizeEvents = false;
        this.m_enablePositionEvents = false;
        this.m_stickWithinContainer = false;
    }
    
    @Override
    public void addFromXML(final EventDispatcher element) {
        if (element instanceof Popup) {
            ((Popup)element).setClient(this);
            MasterRootContainer.getInstance().getLayeredContainer().addWidgetToLayer((Widget)element, 30000);
        }
        else {
            super.addFromXML(element);
        }
    }
    
    @Override
    public void add(final EventDispatcher e) {
        boolean add = true;
        if (e instanceof Margin || e instanceof Padding) {
            this.m_appearance.add(e);
            return;
        }
        if (e instanceof AbstractLayoutData) {
            add &= this.setLayoutData((AbstractLayoutData)e);
        }
        if (e instanceof DecoratorAppearance) {
            add &= this.setAppearance((DecoratorAppearance)e);
        }
        if (e instanceof PopupElement) {
            this.setPopup((PopupElement)e);
        }
        if (add) {
            super.add(e);
        }
    }
    
    public void addThemeElementName(final String themeElementName, final Widget widget) {
        if (themeElementName == null || widget == null) {
            return;
        }
        if (this.m_themeElementWidgets == null) {
            this.m_themeElementWidgets = new HashMap<String, Widget>();
        }
        this.m_themeElementWidgets.put(themeElementName.toUpperCase(), widget);
        this.m_styleIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    protected void addMeshes() {
        assert this.m_appearance != null;
        this.m_appearance.addMeshes();
        this.addInnerMeshes();
        this.m_needToResetMeshes = false;
    }
    
    protected void addInnerMeshes() {
    }
    
    public void setPopup(final PopupElement popup) {
        this.m_popup = popup;
    }
    
    public PopupElement getPopup() {
        return this.m_popup;
    }
    
    public EntityGroup getEntity() {
        return this.m_entity;
    }
    
    public void setContainerParent(final Container c) {
        this.m_containerParent = c;
    }
    
    public Container getContainer() {
        return this.m_containerParent;
    }
    
    public Dimension getMaxSize() {
        return this.m_maxSize;
    }
    
    public void setMaxSize(final Dimension maxSize) {
        this.m_maxSize = maxSize;
        this.m_maxSizeSet = (maxSize != null);
    }
    
    public Dimension getContentMinSize() {
        if (this.m_minSize != null) {
            return this.m_minSize;
        }
        return new Dimension(0, 0);
    }
    
    public Dimension getMinSize() {
        final Dimension dim = this.getContentMinSize();
        return new Dimension(dim.width + this.m_appearance.getLeftInset() + this.m_appearance.getRightInset(), dim.height + this.m_appearance.getTopInset() + this.m_appearance.getBottomInset());
    }
    
    public void onAppearanceInsetsChanged() {
    }
    
    public void setMinSize(final Dimension minSize) {
        this.m_minSize = minSize;
        this.m_minSizeSet = (minSize != null);
    }
    
    public Dimension getGreedySize() {
        final Dimension dim = this.getContentGreedySize();
        return new Dimension(dim.width + this.m_appearance.getLeftInset() + this.m_appearance.getRightInset(), dim.height + this.m_appearance.getTopInset() + this.m_appearance.getBottomInset());
    }
    
    public Dimension getContentGreedySize() {
        if (this.m_containerParent == null) {
            return new Dimension(this.m_appearance.getContentWidth(), this.m_appearance.getContentHeight());
        }
        return new Dimension(this.m_appearance.getContentWidth(), this.m_appearance.getContentHeight());
    }
    
    public Dimension getPrefSize() {
        final Dimension dim = this.getContentPrefSize();
        if (this.m_shrinkable) {
            if (this.m_maxSize != null) {
                dim.height = Math.min(dim.height, this.m_maxSize.height);
                dim.width = Math.min(dim.width, this.m_maxSize.width);
            }
            else {
                dim.height = 0;
                dim.width = 0;
            }
        }
        return new Dimension(dim.width + this.m_appearance.getLeftInset() + this.m_appearance.getRightInset(), dim.height + this.m_appearance.getTopInset() + this.m_appearance.getBottomInset());
    }
    
    public Dimension getContentPrefSize() {
        if (this.m_prefSize == null) {
            return this.getContentMinSize();
        }
        final Dimension minSize = this.getContentMinSize();
        if (minSize == null) {
            return this.m_prefSize;
        }
        final int width = Math.max(this.m_prefSize.width, minSize.width);
        final int height = Math.max(this.m_prefSize.height, minSize.height);
        return new Dimension(width, height);
    }
    
    public Dimension getSetPrefSize() {
        if (this.m_prefSizeSet) {
            return this.m_prefSize;
        }
        return null;
    }
    
    public void setPrefSize(final Dimension prefSize) {
        this.m_prefSize = prefSize;
        this.m_prefSizeSet = (prefSize != null);
    }
    
    public Dimension getSize() {
        return this.m_size;
    }
    
    public void setSize(final Dimension size) {
        this.setSize(size.width, size.height);
    }
    
    public void setSize(final int width, final int height) {
        this.setSize(width, height, false);
    }
    
    public void setSize(final int width, final int height, final boolean applyAtOnce) {
        if (!this.m_useResizeTween || applyAtOnce) {
            final boolean sizeChanged = this.m_size.width != width | this.m_size.height != height;
            if (sizeChanged) {
                this.m_size.width = width;
                this.m_size.height = height;
                this.invalidate();
                if (this.m_enableResizeEvents) {
                    final Event event = Event.checkOut();
                    event.setType(Events.RESIZED);
                    event.setTarget(this);
                    this.dispatchEvent(event);
                }
                if (this.m_stickWithinContainer) {
                    this.setPositionWithinParentBounds();
                }
                this.recomputeRelativePosition();
                if (this.m_elementMapRoot && this.m_userDefinedManager != null) {
                    ((WidgetUserDefinedManager)this.m_userDefinedManager).sizeChanged();
                }
            }
        }
        else {
            this.removeTweensOfType(ResizeTween.class);
            final ResizeTween rt = new ResizeTween(this.getSize(), new Dimension(width, height), this, 0, 500, TweenFunction.PROGRESSIVE);
            this.addTween(rt);
        }
    }
    
    public void setSizeToMinSize() {
        this.setSize(this.getMinSize());
    }
    
    public void setSizeToPrefSize() {
        this.setSize(this.getPrefSize());
    }
    
    public void setPositionWithinParentBounds() {
        if (this.m_containerParent == null) {
            return;
        }
        final int x = MathHelper.clamp(this.getX(), 0, this.m_containerParent.getAppearance().getContentWidth() - this.getWidth());
        final int y = MathHelper.clamp(this.getY(), 0, this.m_containerParent.getAppearance().getContentHeight() - this.getHeight());
        this.setPosition(x, y);
    }
    
    @Override
    public int getWidth() {
        return this.m_size.width;
    }
    
    public void setWidth(final int width) {
        this.setSize(width, this.m_size.height);
    }
    
    public void setHeight(final int height) {
        this.setSize(this.m_size.width, height);
    }
    
    @Override
    public int getHeight() {
        return this.m_size.height;
    }
    
    public void setEnableResizeEvents(final boolean enableResizeEvents) {
        this.m_enableResizeEvents = enableResizeEvents;
    }
    
    public void setEnablePositionEvents(final boolean enablePositionEvents) {
        this.m_enablePositionEvents = enablePositionEvents;
    }
    
    public boolean isNonBlocking() {
        return this.m_nonBlocking;
    }
    
    public void setNonBlocking(final boolean nonBlocking) {
        this.m_nonBlocking = nonBlocking;
    }
    
    public int getX(final Container c) {
        if (this.m_position == null) {
            return 0;
        }
        if (this.m_containerParent == null || this.m_containerParent == c) {
            return this.m_position.x + this.m_appearance.getLeftInset();
        }
        return this.m_position.x + this.m_containerParent.getX(c) + this.m_appearance.getLeftInset();
    }
    
    public int getY(final Container c) {
        if (this.m_position == null) {
            return 0;
        }
        if (this.m_containerParent == null || this.m_containerParent == c) {
            return this.m_position.y + this.m_appearance.getBottomInset();
        }
        return this.m_position.y + this.m_containerParent.getY(c) + this.m_appearance.getBottomInset();
    }
    
    public int getScreenX() {
        if (this.m_screenPosition.x != -1 && this.m_screenPosition.y != -1) {
            return this.m_screenPosition.x;
        }
        if (this.m_position == null) {
            return 0;
        }
        if (this.m_containerParent != null && this.m_containerParent.getAppearance() != null) {
            return this.m_position.x + this.m_containerParent.getScreenX() + this.m_containerParent.getAppearance().getLeftInset();
        }
        return this.m_position.x;
    }
    
    public int getScreenY() {
        if (this.m_screenPosition.x != -1 && this.m_screenPosition.y != -1) {
            return this.m_screenPosition.y;
        }
        if (this.m_position == null) {
            return 0;
        }
        if (this.m_containerParent != null && this.m_containerParent.getAppearance() != null) {
            return this.m_position.y + this.m_containerParent.getScreenY() + this.m_containerParent.getAppearance().getBottomInset();
        }
        return this.m_position.y;
    }
    
    protected final Point getScreenPosition() {
        return this.m_screenPosition;
    }
    
    public void setScreenPosition(final int x, final int y) {
        this.m_screenPosition.setLocation(x, y);
    }
    
    public Point getPosition() {
        return this.m_position;
    }
    
    public void setPosition(final Point position) {
        this.setPosition(position.x, position.y, false);
    }
    
    public void setPosition(final int x, final int y) {
        this.setPosition(x, y, false);
    }
    
    public void setPosition(final int x, final int y, final boolean applyAtOnce) {
        this.setPosition(x, y, applyAtOnce ? 0 : 300);
    }
    
    public void setPosition(final int x, final int y, final int tweenDuration) {
        this.setPosition(x, y, tweenDuration, true, true);
    }
    
    public float getXPercInParent() {
        return this.m_xPercInParent;
    }
    
    public float getYPercInParent() {
        return this.m_yPercInParent;
    }
    
    public void setPosition(final int x, final int y, final int tweenDuration, final boolean recomputeRelativePosition, final boolean checkPositionInParent) {
        if (tweenDuration == 0 || !this.m_usePositionTween) {
            boolean positionChanged = false;
            if (this.m_position == null) {
                this.m_position = new Point(x, y);
                positionChanged = true;
            }
            else if (this.m_position.x != x || this.m_position.y != y) {
                this.m_position.x = x;
                this.m_position.y = y;
                positionChanged = true;
            }
            if (checkPositionInParent && this.m_stickWithinContainer && this.m_containerParent != null) {
                this.m_position.x = MathHelper.clamp(this.m_position.x, 0, this.m_containerParent.getAppearance().getContentWidth() - this.getWidth());
                this.m_position.y = MathHelper.clamp(this.m_position.y, 0, this.m_containerParent.getAppearance().getContentHeight() - this.getHeight());
            }
            if (recomputeRelativePosition) {
                this.recomputeRelativePosition();
            }
            if (this.m_enablePositionEvents) {
                final Event e = Event.checkOut();
                e.setType(Events.REPOSITIONED);
                e.setTarget(this);
                this.dispatchEvent(e);
            }
            if (positionChanged && this.m_elementMapRoot && this.m_userDefinedManager != null) {
                ((WidgetUserDefinedManager)this.m_userDefinedManager).positionChanged();
            }
            this.m_positionChanged = true;
            this.setNeedsToPostProcess();
        }
        else {
            final PositionTween pt = new PositionTween(this.m_position.x, this.m_position.y, x, y, this, 0, tweenDuration, TweenFunction.PROGRESSIVE);
            this.addTween(pt);
        }
    }
    
    private void recomputeRelativePosition() {
        if (this.m_containerParent != null) {
            this.m_xPercInParent = this.m_position.x / (this.m_containerParent.getWidth() - this.m_size.width);
            this.m_yPercInParent = this.m_position.y / (this.m_containerParent.getHeight() - this.m_size.height);
        }
    }
    
    public void setX(final int x) {
        this.setPosition(x, this.m_position.y, false);
    }
    
    public int getX() {
        return this.m_position.x;
    }
    
    @Override
    public int getDisplayX() {
        return this.getScreenX();
    }
    
    public void setY(final int y) {
        this.setPosition(this.m_position.x, y, false);
    }
    
    public int getY() {
        return this.m_position.y;
    }
    
    @Override
    public int getDisplayY() {
        return this.getScreenY();
    }
    
    public MasterRootContainer getMasterRootContainer() {
        if (this.m_containerParent != null) {
            return this.m_containerParent.getMasterRootContainer();
        }
        return null;
    }
    
    public PooledRectangle getComputedScissor() {
        if (this.m_scissor == null) {
            return null;
        }
        return PooledRectangle.checkout(this.getScreenX() + this.m_scissor.x, this.getScreenY() + this.m_scissor.y, this.m_scissor.width, this.m_scissor.height);
    }
    
    public Rectangle getScissor() {
        return this.m_scissor;
    }
    
    public void setScissor(final Rectangle scissor) {
        this.m_scissor = scissor;
    }
    
    public boolean isExpandable() {
        return this.m_expandable;
    }
    
    public void setExpandable(final boolean expandable) {
        this.m_expandable = expandable;
    }
    
    public boolean isShrinkable() {
        return this.m_shrinkable;
    }
    
    public void setShrinkable(final boolean shrinkable) {
        this.m_shrinkable = shrinkable;
    }
    
    public boolean getGreedy() {
        return this.m_greedy;
    }
    
    public void setGreedy(final boolean greedy) {
        this.m_greedy = greedy;
    }
    
    public void setCursorType(final CursorFactory.CursorType type) {
        this.m_cursorType = type;
    }
    
    public CursorFactory.CursorType getCursorType() {
        return this.m_cursorType;
    }
    
    public boolean getVisible() {
        return this.m_visible;
    }
    
    public boolean isParentVisible() {
        return this.m_parentVisible;
    }
    
    protected void setParentVisible(final boolean parentVisible) {
        this.m_parentVisible = parentVisible;
    }
    
    @Deprecated
    public boolean getUsedInLayout() {
        return this.getVisible();
    }
    
    public void setVisible(final boolean visible) {
        if (visible != this.m_visible) {
            this.m_visible = visible;
            if (this.m_containerParent != null) {
                this.m_containerParent.invalidateMinSize();
                this.m_containerParent.setNeedsToResetMeshes();
            }
            this.setParentVisible(visible && (this.m_containerParent == null || this.m_containerParent.isParentVisible()));
            this.setNeedsToPostProcess();
        }
    }
    
    public boolean isVisible() {
        return this.m_visible;
    }
    
    @Deprecated
    public void setUsedInLayout(final boolean usedInLayout) {
        this.setVisible(usedInLayout);
    }
    
    public boolean getUseResizeTween() {
        return this.m_useResizeTween;
    }
    
    public void setUseResizeTween(final boolean useResizeTween) {
        this.m_useResizeTween = useResizeTween;
    }
    
    public boolean getUsePositionTween() {
        return this.m_usePositionTween;
    }
    
    public void setUsePositionTween(final boolean usePositionTween) {
        this.m_usePositionTween = usePositionTween;
    }
    
    public boolean getEnabled() {
        return this.m_enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
        this.checkEnabled();
    }
    
    public boolean getNetEnabled() {
        return this.m_netEnabled;
    }
    
    public void setNetEnabled(final boolean netEnabled) {
        this.m_netEnabled = netEnabled;
        this.checkEnabled();
    }
    
    public String getNetEnabledId() {
        return this.m_netEnabledId;
    }
    
    public void setNetEnabledId(final String netEnabledId) {
        if (this.m_netEnabledId != null) {
            NetEnabledWidgetManager.INSTANCE.removeFromGroup(this.m_netEnabledId, this);
        }
        this.m_netEnabledId = netEnabledId;
        if (this.m_netEnabledId != null) {
            NetEnabledWidgetManager.INSTANCE.addToGroup(netEnabledId, this);
        }
    }
    
    public boolean isEnabledFull() {
        return this.m_netEnabled && this.m_enabled;
    }
    
    private void checkEnabled() {
        final ActivationChangedEvent ae = new ActivationChangedEvent(this, this.m_enabled && this.m_netEnabled);
        this.dispatchEvent(ae);
    }
    
    public void setStickWithinContainer(final boolean stickWithinContainer) {
        this.m_stickWithinContainer = stickWithinContainer;
    }
    
    public boolean isStickWithinContainer() {
        return this.m_stickWithinContainer;
    }
    
    public boolean getFocusable() {
        return this.m_focusable;
    }
    
    public void setFocusable(final boolean focusable) {
        if (focusable && !this.m_focusable) {
            FocusManager.getInstance().addWidgetToFocusList(this);
        }
        else if (!focusable && this.m_focusable) {
            FocusManager.getInstance().removeWidgetFromFocusList(this);
        }
        this.m_focusable = focusable;
    }
    
    public void setFocused(final boolean focused) {
        if (this.isInTree()) {
            if (FocusManager.getInstance().getFocused() != this && focused) {
                FocusManager.getInstance().setFocused(this);
            }
            else if (FocusManager.getInstance().getFocused() == this) {
                FocusManager.getInstance().focusNext();
            }
        }
        else {
            this.m_focused = focused;
        }
    }
    
    public Container getRootFocusParent() {
        if (this.m_containerParent != null) {
            return this.m_containerParent.getRootFocusParent();
        }
        return null;
    }
    
    @Nullable
    public Widget getWidget(final int x, final int y) {
        if (this.m_unloading) {
            return null;
        }
        if (this.m_visible && !this.m_nonBlocking && this.getAppearance().insideInsets(x, y) && !MasterRootContainer.getInstance().isMovePointMode()) {
            return this;
        }
        return null;
    }
    
    public void setNeedsToResetMeshes() {
        this.m_needToResetMeshes = true;
        this.setNeedsToPostProcess();
    }
    
    public boolean getNeedsToResetMeshes() {
        return this.m_needToResetMeshes;
    }
    
    public DecoratorAppearance getAppearance() {
        return this.m_appearance;
    }
    
    @Override
    public boolean setAppearance(final DecoratorAppearance appearance) {
        boolean changed = false;
        if (this.isAppearanceCompatible(appearance)) {
            if (this.m_appearance != null && this.m_appearance != appearance) {
                appearance.setWidget(this);
                this.destroy(this.m_appearance);
                this.m_appearance = appearance;
                changed = true;
            }
            else if (this.m_appearance == null) {
                this.m_appearance = appearance;
                changed = true;
            }
        }
        else if (appearance != null) {
            appearance.destroySelfFromParent();
        }
        return changed;
    }
    
    public abstract boolean isAppearanceCompatible(final DecoratorAppearance p0);
    
    public void setDecoratorState(final String newState) {
        if (this.m_appearance != null && !this.m_appearance.getCurrentState().equalsIgnoreCase(newState)) {
            this.m_appearance.disableAllDecorators();
            this.m_appearance.setEnabled(newState, true);
        }
    }
    
    public void addStyle(final String style) {
        if (style == null) {
            return;
        }
        boolean added = false;
        int i;
        for (i = 0; i < this.m_style.length; ++i) {
            if (this.m_style[i] == null) {
                this.m_style[i] = style;
                added = true;
                break;
            }
        }
        if (!added) {
            final String[] newStyles = new String[this.m_style.length + 5];
            System.arraycopy(this.m_style = newStyles, 0, newStyles, 0, this.m_style.length);
            this.m_style[i] = style;
        }
        this.m_styleIsDirty = true;
        this.setNeedsToPreProcess();
    }
    
    public void setStyle(final String style, final boolean force) {
        if (this.m_style == null || style == null) {
            return;
        }
        if (force || !style.equals(this.m_style[0])) {
            this.m_style[0] = style;
            if (this.m_childrenAdded) {
                if (this.m_appearance != null) {
                    this.m_appearance.destroyAllRemovableDecorators();
                }
                for (int i = 0; i < this.m_style.length; ++i) {
                    if (this.m_style[i] != null) {
                        Xulor.getInstance().getDocumentParser().changeStyle(this, this.m_style[i]);
                    }
                }
                if (this.m_xmlAppearance != null) {
                    this.m_xmlAppearance.copyElement(this.m_appearance);
                }
                this.m_styleIsDirty = false;
            }
        }
    }
    
    public void setStyle(final String style) {
        try {
            this.setStyle(style, false);
        }
        catch (Exception e) {
            Widget.m_logger.error((Object)"Exception ", (Throwable)e);
        }
    }
    
    public String getThemeElementName() {
        return this.m_themeElementName;
    }
    
    public void setThemeElementName(final String themeElementName) {
        this.m_themeElementName = themeElementName;
    }
    
    public String getThemeElementParentType() {
        return this.m_themeElementParentType;
    }
    
    public void setThemeElementParentType(final String themeElementParentType) {
        this.m_themeElementParentType = themeElementParentType;
    }
    
    public String getStyle() {
        return (this.m_style[0] == null) ? "" : this.m_style[0];
    }
    
    public String[] getStyles() {
        return this.m_style;
    }
    
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        if (this.m_themeElementWidgets != null) {
            return this.m_themeElementWidgets.get(themeElementName.toUpperCase());
        }
        return null;
    }
    
    public Widget getWidgetByThemeElementName(final String themeElementName) {
        return this.getWidgetByThemeElementName(themeElementName, false);
    }
    
    public boolean setLayoutData(final AbstractLayoutData data) {
        boolean changed = false;
        if (this.m_layoutData != null && this.m_layoutData != data) {
            this.destroy(this.m_layoutData);
            this.m_layoutData = data;
            changed = true;
        }
        else if (this.m_layoutData == null) {
            this.m_layoutData = data;
            changed = true;
        }
        return changed;
    }
    
    public AbstractLayoutData getLayoutData() {
        return this.m_layoutData;
    }
    
    public DragNDropContainer getDragAndDropParent() {
        return this.m_dndParent;
    }
    
    public void setDragAndDropParent(final DragNDropContainer component) {
        this.m_dndParent = component;
    }
    
    public void setOnFocusChange(final FocusChangedListener fl) {
        this.addEventListener(fl.getType(), fl, true);
    }
    
    public void setOnClick(final MouseClickedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnDoubleClick(final MouseDoubleClickedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnMouseMove(final MouseMovedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnMouseEnter(final MouseEnteredListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnMouseExit(final MouseExitedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnMousePress(final MousePressedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnMouseRelease(final MouseReleasedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnMouseWheel(final MouseWheeledListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnMouseDrag(final MouseDraggedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnMouseDragOut(final MouseDraggedOutListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnMouseDragIn(final MouseDraggedInListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnKeyPress(final KeyPressedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnKeyRelease(final KeyReleasedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnKeyType(final KeyTypedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnPopupDisplay(final PopupDisplayListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnPopupHide(final PopupHideListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnSelectionChange(final SelectionChangedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnSliderMove(final SliderMovedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnStick(final WindowStickListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnValueChange(final ValueChangedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnListSelectionChange(final ListSelectionChangedListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnItemOver(final ItemOverListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnItemOut(final ItemOutListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnItemClick(final ItemClickListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnItemDoubleClick(final ItemDoubleClickListener l) {
        this.addEventListener(l.getType(), l, true);
    }
    
    public void setOnDrag(final DragListener l) {
        this.addEventListener(Events.DRAG, l, false);
    }
    
    public void setOnDrop(final DropListener l) {
        this.addEventListener(Events.DROP, l, false);
    }
    
    public void setOnDropOut(final DropOutListener l) {
        this.addEventListener(Events.DROP_OUT, l, false);
    }
    
    public void setOnDragOut(final DragOutListener l) {
        this.addEventListener(Events.DRAG_OUT, l, false);
    }
    
    public void setOnDragOver(final DragOverListener l) {
        this.addEventListener(Events.DRAG_OVER, l, false);
    }
    
    public <T extends Container> T getWidgetParentOfType(final Class<T> type) {
        for (Container parent = this.m_containerParent; parent != null; parent = parent.m_containerParent) {
            if (type.isAssignableFrom(parent.getClass())) {
                return (T)parent;
            }
        }
        return null;
    }
    
    public void setUserDefinedSize(final boolean userDefinedSize) {
        if (this.m_userDefinedManager == null) {
            this.m_userDefinedManager = new WidgetUserDefinedManager(this);
        }
        final WidgetUserDefinedManager udm = (WidgetUserDefinedManager)this.m_userDefinedManager;
        if (udm.isUseSize() == userDefinedSize) {
            return;
        }
        udm.setUseSize(userDefinedSize);
        this.loadPreferences();
    }
    
    public boolean isSizeInitByUserDefinition() {
        return this.m_userDefinedManager != null && ((WidgetUserDefinedManager)this.m_userDefinedManager).isUseSize() && (this.m_size.getWidth() != 0.0 || this.m_size.getHeight() != 0.0);
    }
    
    public void setUserDefinedPosition(final boolean userDefinedPosition) {
        if (this.m_userDefinedManager == null) {
            this.m_userDefinedManager = new WidgetUserDefinedManager(this);
        }
        final WidgetUserDefinedManager udm = (WidgetUserDefinedManager)this.m_userDefinedManager;
        if (udm.isUsePosition() == userDefinedPosition) {
            return;
        }
        udm.setUsePosition(userDefinedPosition);
        this.loadPreferences();
    }
    
    public boolean isPositionInitByUserDefinition() {
        return this.m_userDefinedManager != null && ((WidgetUserDefinedManager)this.m_userDefinedManager).isUsePosition() && this.m_userDefinedManager.hasRecord();
    }
    
    @Override
    public boolean dispatchEvent(final Event event) {
        if (this.isEnabledFull() || !(event instanceof InputEvent)) {
            return super.dispatchEvent(event);
        }
        event.release();
        return false;
    }
    
    @Override
    public void invalidate() {
        if (this.m_appearance != null) {
            this.m_appearance.invalidate();
        }
        super.invalidate();
    }
    
    public boolean isInWidgetTree() {
        return this.m_containerParent != null && this.m_containerParent.isInWidgetTree();
    }
    
    public void addedToWidgetTree() {
        if (this.m_themeElementName != null && this.m_themeElementParentType != null) {
            final Factory<?> f = XulorTagLibrary.getInstance().getFactory(this.m_themeElementParentType);
            if (f != null) {
                final Class<?> c = f.getTemplate();
                final Container container = this.getWidgetParentOfType(c);
                if (container != null) {
                    container.addThemeElementName(this.m_themeElementName, this);
                }
            }
        }
        if (this.m_focusable) {
            FocusManager.getInstance().addWidgetToFocusList(this);
        }
    }
    
    public void removedFromWidgetTree() {
    }
    
    @Override
    public void addedToTree() {
        super.addedToTree();
        if (this.m_focused) {
            this.setFocused(this.m_focused);
        }
    }
    
    @Override
    public void onChildrenAdded() {
        super.onChildrenAdded();
        if (this.m_xmlAppearance != null) {
            this.m_xmlAppearance.release();
        }
        this.m_xmlAppearance = (DecoratorAppearance)this.m_appearance.cloneElementStructure();
        if (this.m_style[0] == null) {
            this.setStyle("", true);
        }
        else {
            this.setStyle(this.m_style[0], true);
        }
    }
    
    @Override
    public void onCheckIn() {
        FocusManager.getInstance().removeWidgetFromFocusList(this);
        super.onCheckIn();
        if (MasterRootContainer.getInstance() != null) {
            MasterRootContainer.getInstance().cleanUpReferences(this);
        }
        this.m_entity.setPreRenderStates(null);
        this.m_entity.setPostRenderStates(null);
        this.m_entity.removeAllChildren();
        this.m_entity.removeReference();
        this.m_entity = null;
        this.m_size = null;
        this.m_minSize = null;
        this.m_maxSize = null;
        this.m_prefSize = null;
        this.m_position = null;
        this.m_scissor = null;
        this.setNetEnabledId(null);
        this.m_containerParent = null;
        this.m_dndParent = null;
        if (this.m_xmlAppearance != null) {
            this.m_xmlAppearance.release();
            this.m_xmlAppearance = null;
        }
        this.m_appearance = null;
        Arrays.fill(this.m_style, null);
        this.m_themeElementName = null;
        this.m_themeElementParentType = null;
        if (this.m_themeElementWidgets != null) {
            this.m_themeElementWidgets.clear();
            this.m_themeElementWidgets = null;
        }
        this.m_layoutData = null;
        this.m_popup = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_enablePositionEvents = false;
        this.m_enableResizeEvents = false;
        this.m_expandable = true;
        this.m_shrinkable = false;
        this.m_greedy = false;
        this.m_parentVisible = false;
        this.m_enabled = true;
        this.m_netEnabled = true;
        this.m_focusable = false;
        this.m_focused = false;
        this.m_styleIsDirty = false;
        this.m_stickWithinContainer = false;
        this.m_needsScissor = false;
        this.m_positionChanged = true;
        this.m_cursorType = CursorFactory.CursorType.DEFAULT;
        this.m_useResizeTween = false;
        this.m_usePositionTween = false;
        this.m_needToResetMeshes = true;
        final TransformerSRT transformer = new TransformerSRT();
        transformer.setIdentity();
        assert this.m_entity == null;
        this.m_entity = EntityGroup.Factory.newPooledInstance();
        this.m_entity.m_owner = this;
        this.m_entity.getTransformer().addTransformer(transformer);
        final ContainerRenderStates crs = new ContainerRenderStates(this);
        this.m_entity.setPreRenderStates(crs);
        this.m_entity.setPostRenderStates(crs);
        this.m_entity.getMatrix().setTranslation(new Vector4(10000.0f, 0.0f, 0.0f));
        this.m_minSizeSet = false;
        this.m_maxSizeSet = false;
        this.m_prefSizeSet = false;
        this.m_position = new Point(0, 0);
        this.m_size = new Dimension(0, 0);
        this.m_visible = true;
        this.m_nonBlocking = false;
        this.setNeedsToPostProcess();
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_styleIsDirty) {
            this.setStyle(this.m_style[0], true);
        }
        return ret;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        if (this.m_needToResetMeshes && this.m_entity != null) {
            this.m_entity.removeAllChildren();
            this.addMeshes();
        }
        if (this.m_positionChanged && this.m_visible && this.m_entity != null) {
            int x = this.m_position.x;
            int y = this.m_position.y;
            if (this.m_containerParent != null) {
                x += this.m_containerParent.getAppearance().getLeftInset();
                y += this.m_containerParent.getAppearance().getBottomInset();
            }
            this.m_entity.getTransformer().setTranslation(0, x, y);
            this.m_positionChanged = false;
        }
        return ret;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[").append(this.getClass().getSimpleName()).append("] ");
        if (this.m_id != null) {
            sb.append("id = ").append(this.m_id).append(" ");
        }
        if (this.m_position != null) {
            sb.append("Position <").append(this.m_position.x).append(", ").append(this.m_position.y).append("> ");
        }
        if (this.m_size != null) {
            sb.append("Taille (").append(this.m_size.width).append(", ").append(this.m_size.height).append(")");
        }
        if (this.m_id != null) {
            sb.append("Id=").append(this.getId());
        }
        if (this.isUnloading()) {
            sb.append("released");
        }
        return sb.toString();
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final Widget w = (Widget)source;
        super.copyElement(w);
        w.m_enablePositionEvents = this.m_enablePositionEvents;
        w.m_enableResizeEvents = this.m_enableResizeEvents;
        w.m_enabled = this.m_enabled;
        w.setNetEnabledId(this.m_netEnabledId);
        w.m_expandable = this.m_expandable;
        w.m_shrinkable = this.m_shrinkable;
        w.m_greedy = this.m_greedy;
        w.setFocusable(this.m_focusable);
        if (this.m_maxSize != null) {
            w.m_maxSize = this.m_maxSize.cloneDimension();
        }
        w.m_maxSizeSet = this.m_maxSizeSet;
        if (this.m_prefSize != null) {
            w.m_prefSize = this.m_prefSize.cloneDimension();
        }
        w.m_prefSizeSet = this.m_prefSizeSet;
        if (this.m_minSize != null) {
            w.m_minSize = this.m_minSize.cloneDimension();
        }
        w.m_minSizeSet = this.m_minSizeSet;
        w.m_nonBlocking = this.m_nonBlocking;
        w.m_position = (Point)this.m_position.clone();
        w.m_size = (Dimension)this.m_size.clone();
        w.m_cursorType = this.m_cursorType;
        System.arraycopy(this.m_style, 0, w.m_style, 0, this.m_style.length);
        w.m_themeElementName = this.m_themeElementName;
        w.m_themeElementParentType = this.m_themeElementParentType;
        w.m_visible = this.m_visible;
        if (w.m_xmlAppearance != null) {
            w.m_xmlAppearance.release();
        }
        w.m_xmlAppearance = ((this.m_xmlAppearance != null) ? ((DecoratorAppearance)this.m_xmlAppearance.cloneElementStructure()) : null);
        w.m_needsScissor = this.m_needsScissor;
        if (this.m_userDefinedManager != null) {
            w.setUserDefinedSize(this.isSizeInitByUserDefinition());
            w.setUserDefinedPosition(this.isPositionInitByUserDefinition());
        }
        w.m_stickWithinContainer = this.m_stickWithinContainer;
    }
    
    public final boolean needsScissor() {
        return this.m_needsScissor;
    }
    
    public void setNeedsScissor(final boolean needsScissor) {
        this.m_needsScissor = needsScissor;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Widget.SIZE_HASH) {
            this.setSize(cl.convertToDimension(value));
        }
        else if (hash == Widget.MAX_SIZE_HASH) {
            this.setMaxSize(cl.convertToDimension(value));
        }
        else if (hash == Widget.PREF_SIZE_HASH) {
            this.setPrefSize(cl.convertToDimension(value));
        }
        else if (hash == Widget.EXPANDABLE_HASH) {
            this.setExpandable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.SHRINKABLE_HASH) {
            this.setShrinkable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.GREEDY_HASH) {
            this.setGreedy(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.NET_ENABLED_ID_HASH) {
            this.setNetEnabledId(value);
        }
        else if (hash == Widget.ENABLED_HASH) {
            this.setEnabled(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.FOCUSABLE_HASH) {
            this.setFocusable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.FOCUSED_HASH) {
            this.setFocused(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.VISIBLE_HASH || hash == Widget.USED_IN_LAYOUT_HASH) {
            this.setVisible(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.USE_POSITION_TWEEN_HASH) {
            this.setUsePositionTween(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.USE_RESIZE_TWEEN_HASH) {
            this.setUseResizeTween(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.STICK_WITHIN_CONTAINER_HASH) {
            this.setStickWithinContainer(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.X_HASH) {
            this.setX(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Widget.Y_HASH) {
            this.setY(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Widget.STYLE_HASH) {
            this.setStyle(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == Widget.THEME_ELEMENT_NAME_HASH) {
            this.setThemeElementName(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == Widget.THEME_ELEMENT_PARENT_TYPE_HASH) {
            this.setThemeElementParentType(cl.convertToString(value, this.m_elementMap));
        }
        else if (hash == Widget.NON_BLOCKING_HASH) {
            this.setNonBlocking(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.CURSOR_TYPE_HASH) {
            this.setCursorType(CursorFactory.CursorType.value(value));
        }
        else if (hash == Widget.NEEDS_SCISSOR_HASH) {
            this.setNeedsScissor(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.USER_DEFINED_SIZE_HASH) {
            this.setUserDefinedSize(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.USER_DEFINED_POSITION_HASH) {
            this.setUserDefinedPosition(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.ON_DRAG_HASH) {
            this.setOnDrag(cl.convert(DragListener.class, value));
        }
        else if (hash == Widget.ON_DROP_HASH) {
            this.setOnDrop(cl.convert(DropListener.class, value));
        }
        else if (hash == Widget.ON_DRAG_OUT_HASH) {
            this.setOnDragOut(cl.convert(DragOutListener.class, value));
        }
        else if (hash == Widget.ON_DROP_OUT_HASH) {
            this.setOnDropOut(cl.convert(DropOutListener.class, value));
        }
        else if (hash == Widget.ON_DRAG_OVER_HASH) {
            this.setOnDragOver(cl.convert(DragOverListener.class, value));
        }
        else if (hash == Widget.ON_CLICK_HASH) {
            this.setOnClick(cl.convert(MouseClickedListener.class, value));
        }
        else if (hash == Widget.ON_DOUBLE_CLICK_HASH) {
            this.setOnDoubleClick(cl.convert(MouseDoubleClickedListener.class, value));
        }
        else if (hash == Widget.ON_FOCUS_CHANGE_HASH) {
            this.setOnFocusChange(cl.convert(FocusChangedListener.class, value));
        }
        else if (hash == Widget.ON_ITEM_CLICK_HASH) {
            this.setOnItemClick(cl.convert(ItemClickListener.class, value));
        }
        else if (hash == Widget.ON_ITEM_DOUBLE_CLICK_HASH) {
            this.setOnItemDoubleClick(cl.convert(ItemDoubleClickListener.class, value));
        }
        else if (hash == Widget.ON_ITEM_OUT_HASH) {
            this.setOnItemOut(cl.convert(ItemOutListener.class, value));
        }
        else if (hash == Widget.ON_ITEM_OVER_HASH) {
            this.setOnItemOver(cl.convert(ItemOverListener.class, value));
        }
        else if (hash == Widget.ON_KEY_PRESS_HASH) {
            this.setOnKeyPress(cl.convert(KeyPressedListener.class, value));
        }
        else if (hash == Widget.ON_KEY_RELEASE_HASH) {
            this.setOnKeyRelease(cl.convert(KeyReleasedListener.class, value));
        }
        else if (hash == Widget.ON_KEY_TYPE_HASH) {
            this.setOnKeyType(cl.convert(KeyTypedListener.class, value));
        }
        else if (hash == Widget.ON_LIST_SELECTION_CHANGE_HASH) {
            this.setOnListSelectionChange(cl.convert(ListSelectionChangedListener.class, value));
        }
        else if (hash == Widget.ON_MOUSE_DRAG_HASH) {
            this.setOnMouseDrag(cl.convert(MouseDraggedListener.class, value));
        }
        else if (hash == Widget.ON_MOUSE_DRAG_IN_HASH) {
            this.setOnMouseDragIn(cl.convert(MouseDraggedInListener.class, value));
        }
        else if (hash == Widget.ON_MOUSE_DRAG_OUT_HASH) {
            this.setOnMouseDragOut(cl.convert(MouseDraggedOutListener.class, value));
        }
        else if (hash == Widget.ON_MOUSE_MOVE_HASH) {
            this.setOnMouseMove(cl.convert(MouseMovedListener.class, value));
        }
        else if (hash == Widget.ON_MOUSE_ENTER_HASH) {
            this.setOnMouseEnter(cl.convert(MouseEnteredListener.class, value));
        }
        else if (hash == Widget.ON_MOUSE_EXIT_HASH) {
            this.setOnMouseExit(cl.convert(MouseExitedListener.class, value));
        }
        else if (hash == Widget.ON_MOUSE_PRESS_HASH) {
            this.setOnMousePress(cl.convert(MousePressedListener.class, value));
        }
        else if (hash == Widget.ON_MOUSE_RELEASE_HASH) {
            this.setOnMouseRelease(cl.convert(MouseReleasedListener.class, value));
        }
        else if (hash == Widget.ON_MOUSE_WHEEL_HASH) {
            this.setOnMouseWheel(cl.convert(MouseWheeledListener.class, value));
        }
        else if (hash == Widget.ON_SELECTION_CHANGE_HASH) {
            this.setOnSelectionChange(cl.convert(SelectionChangedListener.class, value));
        }
        else if (hash == Widget.ON_SLIDER_MOVE_HASH) {
            this.setOnSliderMove(cl.convert(SliderMovedListener.class, value));
        }
        else if (hash == Widget.ON_STICK_HASH) {
            this.setOnStick(cl.convert(WindowStickListener.class, value));
        }
        else if (hash == Widget.ON_POPUP_DISPLAY_HASH) {
            this.setOnPopupDisplay(cl.convert(PopupDisplayListener.class, value));
        }
        else {
            if (hash != Widget.ON_POPUP_HIDE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setOnPopupHide(cl.convert(PopupHideListener.class, value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Widget.SIZE_HASH) {
            this.setSize((Dimension)value);
        }
        else if (hash == Widget.MAX_SIZE_HASH) {
            this.setMaxSize((Dimension)value);
        }
        else if (hash == Widget.PREF_SIZE_HASH) {
            this.setPrefSize((Dimension)value);
        }
        else if (hash == Widget.EXPANDABLE_HASH) {
            this.setExpandable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.SHRINKABLE_HASH) {
            this.setShrinkable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.GREEDY_HASH) {
            this.setGreedy(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.ENABLED_HASH) {
            this.setEnabled(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.FOCUSABLE_HASH) {
            this.setFocusable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.FOCUSED_HASH) {
            this.setFocused(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.VISIBLE_HASH || hash == Widget.USED_IN_LAYOUT_HASH) {
            this.setVisible(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.USE_POSITION_TWEEN_HASH) {
            this.setUsePositionTween(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.USE_RESIZE_TWEEN_HASH) {
            this.setUseResizeTween(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Widget.X_HASH) {
            this.setX(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Widget.Y_HASH) {
            this.setY(PrimitiveConverter.getInteger(value));
        }
        else if (hash == Widget.STYLE_HASH) {
            this.setStyle((String)value);
        }
        else if (hash == Widget.THEME_ELEMENT_NAME_HASH) {
            this.setThemeElementName((String)value);
        }
        else if (hash == Widget.THEME_ELEMENT_PARENT_TYPE_HASH) {
            this.setThemeElementParentType((String)value);
        }
        else if (hash == Widget.NON_BLOCKING_HASH) {
            this.setNonBlocking(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != Widget.CURSOR_TYPE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setCursorType((CursorFactory.CursorType)value);
        }
        return true;
    }
    
    @Override
    public BasicElement getNewElement(final String name, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
        BasicElement element = super.getNewElement(name, parent, elementMaps, env);
        if (element instanceof DecoratorAppearance && this.m_appearance != null) {
            element.release();
            element = this.getAppearance();
        }
        return element;
    }
    
    static {
        SIZE_HASH = "size".hashCode();
        PREF_SIZE_HASH = "prefSize".hashCode();
        MAX_SIZE_HASH = "maxSize".hashCode();
        EXPANDABLE_HASH = "expandable".hashCode();
        SHRINKABLE_HASH = "shrinkable".hashCode();
        GREEDY_HASH = "greedy".hashCode();
        FOCUSABLE_HASH = "focusable".hashCode();
        FOCUSED_HASH = "focused".hashCode();
        ENABLED_HASH = "enabled".hashCode();
        NET_ENABLED_ID_HASH = "netEnabledId".hashCode();
        VISIBLE_HASH = "visible".hashCode();
        USED_IN_LAYOUT_HASH = "usedInLayout".hashCode();
        USE_POSITION_TWEEN_HASH = "usePositionTween".hashCode();
        USE_RESIZE_TWEEN_HASH = "useResizeTween".hashCode();
        X_HASH = "x".hashCode();
        Y_HASH = "y".hashCode();
        STYLE_HASH = "style".hashCode();
        THEME_ELEMENT_NAME_HASH = "themeElementName".hashCode();
        THEME_ELEMENT_PARENT_TYPE_HASH = "themeElementParentType".hashCode();
        NON_BLOCKING_HASH = "nonBlocking".hashCode();
        CURSOR_TYPE_HASH = "cursorType".hashCode();
        NEEDS_SCISSOR_HASH = "needsScissor".hashCode();
        USER_DEFINED_SIZE_HASH = "userDefinedSize".hashCode();
        USER_DEFINED_POSITION_HASH = "userDefinedPosition".hashCode();
        STICK_WITHIN_CONTAINER_HASH = "stickWithinContainer".hashCode();
        ON_CLICK_HASH = "onClick".hashCode();
        ON_DOUBLE_CLICK_HASH = "onDoubleClick".hashCode();
        ON_FOCUS_CHANGE_HASH = "onFocusChange".hashCode();
        ON_ITEM_CLICK_HASH = "onItemClick".hashCode();
        ON_ITEM_DOUBLE_CLICK_HASH = "onItemDoubleClick".hashCode();
        ON_ITEM_OUT_HASH = "onItemOut".hashCode();
        ON_ITEM_OVER_HASH = "onItemOver".hashCode();
        ON_KEY_PRESS_HASH = "onKeyPress".hashCode();
        ON_KEY_RELEASE_HASH = "onKeyRelease".hashCode();
        ON_KEY_TYPE_HASH = "onKeyType".hashCode();
        ON_LIST_SELECTION_CHANGE_HASH = "onListSelectionChange".hashCode();
        ON_MOUSE_DRAG_HASH = "onMouseDrag".hashCode();
        ON_MOUSE_DRAG_IN_HASH = "onMouseDragIn".hashCode();
        ON_MOUSE_DRAG_OUT_HASH = "onMouseDragOut".hashCode();
        ON_MOUSE_MOVE_HASH = "onMouseMove".hashCode();
        ON_MOUSE_ENTER_HASH = "onMouseEnter".hashCode();
        ON_MOUSE_EXIT_HASH = "onMouseExit".hashCode();
        ON_MOUSE_PRESS_HASH = "onMousePress".hashCode();
        ON_MOUSE_RELEASE_HASH = "onMouseRelease".hashCode();
        ON_MOUSE_WHEEL_HASH = "onMouseWheel".hashCode();
        ON_SELECTION_CHANGE_HASH = "onSelectionChange".hashCode();
        ON_SLIDER_MOVE_HASH = "onSliderMove".hashCode();
        ON_VALUE_CHANGE_HASH = "onValueChange".hashCode();
        ON_DRAG_HASH = "onDrag".hashCode();
        ON_DROP_HASH = "onDrop".hashCode();
        ON_DRAG_OUT_HASH = "onDragOut".hashCode();
        ON_DROP_OUT_HASH = "onDropOut".hashCode();
        ON_DRAG_OVER_HASH = "onDragOver".hashCode();
        ON_STICK_HASH = "onStick".hashCode();
        ON_POPUP_DISPLAY_HASH = "onPopupDisplay".hashCode();
        ON_POPUP_HIDE_HASH = "onPopupHide".hashCode();
    }
}
