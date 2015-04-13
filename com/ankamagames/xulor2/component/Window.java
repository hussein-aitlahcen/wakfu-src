package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.windowStick.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class Window extends RootContainer
{
    public static final String TAG = "Window";
    public static final String MESSAGE_BOX_TAG = "MessageBox";
    public static final String THEME_TITLE_BAR = "titleBar";
    public static final String THEME_LABEL = "label";
    public static final String THEME_CONTENT = "content";
    public static final String THEME_CLOSE_BUTTON = "closeButton";
    public static final String THEME_MIN_BUTTON = "minButton";
    public static final String THEME_MAX_BUTTON = "maxButton";
    private boolean m_modal;
    private boolean m_stickWithinRootContainer;
    private boolean m_stickFullyWithinRootContainer;
    private WindowStickData m_stickData;
    private boolean m_canBePushedToTop;
    private boolean m_movable;
    private ArrayList<WindowMovePoint> m_movePoints;
    private String m_horizontalDialog;
    private String m_verticalDialog;
    private final ArrayList<WindowPostProcessedListener> m_windowPostProcessedListeners;
    public static final int CAN_BE_PUSHED_TO_TOP_HASH;
    public static final int MOVABLE_HASH;
    public static final int STICK_WITHIN_DISPLAY_BOUNDS_HASH;
    public static final int STICK_WITHIN_ROOT_CONTAINER_HASH;
    public static final int STICK_FULLY_WITHIN_ROOT_CONTAINER_HASH;
    public static final int HORIZONTAL_DIALOG_HASH;
    public static final int VERTICAL_DIALOG_HASH;
    
    public Window() {
        super();
        this.m_stickWithinRootContainer = true;
        this.m_stickFullyWithinRootContainer = false;
        this.m_canBePushedToTop = true;
        this.m_movable = true;
        this.m_movePoints = new ArrayList<WindowMovePoint>();
        this.m_windowPostProcessedListeners = new ArrayList<WindowPostProcessedListener>();
    }
    
    @Override
    public void addFromXML(final EventDispatcher e) {
        if (e instanceof AbstractLayoutData) {
            this.add(e);
        }
        else if (e instanceof DecoratorAppearance) {
            this.add(e);
        }
        else if (e instanceof PropertyElement) {
            this.add(e);
        }
        else if (e instanceof ItemElement) {
            this.add(e);
        }
        else {
            super.addFromXML(e);
        }
    }
    
    void addWindowMovePoint(final WindowMovePoint point) {
        this.m_movePoints.add(point);
    }
    
    @Override
    public String getTag() {
        return "Window";
    }
    
    public ArrayList<WindowMovePoint> getMovePoints() {
        return this.m_movePoints;
    }
    
    public boolean isMovable() {
        return this.m_movable;
    }
    
    public void setMovable(final boolean movable) {
        this.m_movable = movable;
    }
    
    @Override
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        return null;
    }
    
    @Deprecated
    public boolean isStickWithinDisplayBounds() {
        return this.m_stickWithinRootContainer;
    }
    
    @Deprecated
    public void setStickWithinDisplayBounds(final boolean stickWithinDisplayBounds) {
        this.m_stickWithinRootContainer = stickWithinDisplayBounds;
    }
    
    public boolean isStickWithinRootContainer() {
        return this.m_stickWithinRootContainer;
    }
    
    public void setStickWithinRootContainer(final boolean stickWithinRootContainer) {
        this.m_stickWithinRootContainer = stickWithinRootContainer;
    }
    
    public boolean getStickFullyWithinRootContainer() {
        return this.m_stickFullyWithinRootContainer;
    }
    
    public void setStickFullyWithinRootContainer(final boolean stickFullyWithinRootContainer) {
        this.m_stickFullyWithinRootContainer = stickFullyWithinRootContainer;
    }
    
    public void setStickData(final WindowStickData data) {
        this.m_stickData = data;
    }
    
    public WindowStickData getStickData() {
        return this.m_stickData;
    }
    
    @Override
    public void setStyle(final String style, final boolean force) {
        super.setStyle(style, force);
        if (this.m_themeElementWidgets != null) {
            for (final Widget w : this.m_themeElementWidgets.values()) {
                w.setStyle(this.getTag() + this.getStyle() + "$" + w.getThemeElementName(), force);
            }
        }
    }
    
    public boolean getCanBePushedToTop() {
        return this.m_canBePushedToTop;
    }
    
    public void setCanBePushedToTop(final boolean canBePushedToTop) {
        this.m_canBePushedToTop = canBePushedToTop;
    }
    
    public void addWindowPostProcessedListener(final WindowPostProcessedListener l) {
        if (l != null && !this.m_windowPostProcessedListeners.contains(l)) {
            this.m_windowPostProcessedListeners.add(l);
        }
    }
    
    public void removeWindowPostProcessedListener(final WindowPostProcessedListener l) {
        this.m_windowPostProcessedListeners.remove(l);
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        for (int i = this.m_windowPostProcessedListeners.size() - 1; i >= 0; --i) {
            this.m_windowPostProcessedListeners.get(i).windowPostProcessed();
        }
        return ret;
    }
    
    public String getHorizontalDialog() {
        return this.m_horizontalDialog;
    }
    
    public void setHorizontalDialog(final String horizontalDialog) {
        this.m_horizontalDialog = horizontalDialog;
    }
    
    public String getVerticalDialog() {
        return this.m_verticalDialog;
    }
    
    public void setVerticalDialog(final String verticalDialog) {
        this.m_verticalDialog = verticalDialog;
    }
    
    public void pushToTop() {
        if (this.m_canBePushedToTop) {
            final LayeredContainer lc = this.getParentOfType(LayeredContainer.class);
            if (lc != null) {
                lc.pushToTop(this);
                Xulor.getInstance().getDialogClosesManager().pushElementMap(this.m_elementMap.getId(), true);
            }
        }
    }
    
    private void addListeners() {
        this.addEventListener(Events.MOUSE_PRESSED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                Window.this.pushToTop();
                return false;
            }
        }, true);
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        super.copyElement(c);
        final Window w = (Window)c;
        w.setMovable(this.m_movable);
        w.m_canBePushedToTop = this.m_canBePushedToTop;
        w.m_modal = this.m_modal;
        w.m_stickWithinRootContainer = this.m_stickWithinRootContainer;
        w.m_stickFullyWithinRootContainer = this.m_stickFullyWithinRootContainer;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_windowPostProcessedListeners.clear();
        if (this.m_stickData != null) {
            WindowStickManager.getInstance().removeWindow(this);
            this.m_stickData = null;
        }
        this.m_movePoints.clear();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final RowLayout rl = RowLayout.checkOut();
        this.add(rl);
        this.m_movable = true;
        this.m_nonBlocking = false;
        this.m_stickWithinContainer = true;
        this.m_stickWithinRootContainer = true;
        this.m_stickFullyWithinRootContainer = false;
        this.addListeners();
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Window.CAN_BE_PUSHED_TO_TOP_HASH) {
            this.setCanBePushedToTop(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Window.MOVABLE_HASH) {
            this.setMovable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Window.STICK_WITHIN_DISPLAY_BOUNDS_HASH || hash == Window.STICK_WITHIN_ROOT_CONTAINER_HASH) {
            this.setStickWithinRootContainer(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Window.STICK_FULLY_WITHIN_ROOT_CONTAINER_HASH) {
            this.setStickFullyWithinRootContainer(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Window.HORIZONTAL_DIALOG_HASH) {
            this.setHorizontalDialog(cl.convertToString(value, this.m_elementMap));
        }
        else {
            if (hash != Window.VERTICAL_DIALOG_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setVerticalDialog(cl.convertToString(value, this.m_elementMap));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Window.CAN_BE_PUSHED_TO_TOP_HASH) {
            this.setCanBePushedToTop(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Window.MOVABLE_HASH) {
            this.setMovable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Window.STICK_WITHIN_DISPLAY_BOUNDS_HASH || hash == Window.STICK_WITHIN_ROOT_CONTAINER_HASH) {
            this.setStickWithinRootContainer(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Window.STICK_FULLY_WITHIN_ROOT_CONTAINER_HASH) {
            this.setStickFullyWithinRootContainer(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == Window.HORIZONTAL_DIALOG_HASH) {
            this.setHorizontalDialog((String)value);
        }
        else {
            if (hash != Window.VERTICAL_DIALOG_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setVerticalDialog((String)value);
        }
        return true;
    }
    
    static {
        CAN_BE_PUSHED_TO_TOP_HASH = "canBePushedToTop".hashCode();
        MOVABLE_HASH = "movable".hashCode();
        STICK_WITHIN_DISPLAY_BOUNDS_HASH = "stickWithinDisplayBounds".hashCode();
        STICK_WITHIN_ROOT_CONTAINER_HASH = "stickWithinRootContainer".hashCode();
        STICK_FULLY_WITHIN_ROOT_CONTAINER_HASH = "stickFullyWithinRootContainer".hashCode();
        HORIZONTAL_DIALOG_HASH = "horizontalDialog".hashCode();
        VERTICAL_DIALOG_HASH = "verticalDialog".hashCode();
    }
}
