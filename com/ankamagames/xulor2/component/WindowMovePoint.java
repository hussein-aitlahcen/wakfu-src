package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.core.windowStick.*;
import com.ankamagames.xulor2.core.event.*;
import java.awt.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class WindowMovePoint extends Container
{
    public static final String TAG = "WindowMovePoint";
    public static final String SHORT_TAG = "WMP";
    private Window m_window;
    private boolean m_beingDragged;
    private boolean m_dragLocked;
    private int m_initialX;
    private int m_initialY;
    private RootContainer m_windowRootContainer;
    private EventListener m_mouseReleasedListener;
    private boolean m_horizontal;
    private boolean m_vertical;
    public static final int HORIZONTAL_HASH;
    public static final int VERTICAL_HASH;
    
    public WindowMovePoint() {
        super();
        this.m_beingDragged = false;
        this.m_dragLocked = false;
        this.m_horizontal = true;
        this.m_vertical = true;
    }
    
    @Override
    public String getTag() {
        return "WindowMovePoint";
    }
    
    public void setHorizontal(final boolean horizontal) {
        this.m_horizontal = horizontal;
    }
    
    public boolean isHorizontal() {
        return this.m_horizontal;
    }
    
    public boolean isVertical() {
        return this.m_vertical;
    }
    
    public void setVertical(final boolean vertical) {
        this.m_vertical = vertical;
    }
    
    public Window getWindow() {
        return this.m_window;
    }
    
    public void setDragMousePosition(final int mouseX, final int mouseY) {
        this.m_beingDragged = true;
        this.m_initialX = mouseX - this.m_window.getDisplayX();
        this.m_initialY = mouseY - this.m_window.getDisplayY();
    }
    
    public void addListeners() {
        this.m_mouseReleasedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (WindowMovePoint.this.m_beingDragged && WindowMovePoint.this.m_window.getStickData() != null) {
                    WindowStickManager.getInstance().endWindowMove(WindowMovePoint.this.m_window, WindowMovePoint.this.m_window.getX(), WindowMovePoint.this.m_window.getY());
                }
                WindowMovePoint.this.m_beingDragged = false;
                WindowMovePoint.this.m_dragLocked = false;
                return false;
            }
        };
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        this.addEventListener(Events.MOUSE_DRAGGED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent m = (MouseEvent)event;
                if (!WindowMovePoint.this.m_window.isMovable() || WindowMovePoint.this.m_dragLocked) {
                    return false;
                }
                if (!WindowMovePoint.this.m_beingDragged) {
                    WindowMovePoint.this.m_initialX = m.getX(WindowMovePoint.this.m_window);
                    WindowMovePoint.this.m_initialY = m.getY(WindowMovePoint.this.m_window);
                    WindowMovePoint.this.m_beingDragged = true;
                }
                int x = WindowMovePoint.this.m_window.getX();
                int y = WindowMovePoint.this.m_window.getY();
                if (WindowMovePoint.this.m_horizontal) {
                    x = m.getX(WindowMovePoint.this.m_window.getContainer()) - WindowMovePoint.this.m_initialX;
                }
                if (WindowMovePoint.this.m_vertical) {
                    y = m.getY(WindowMovePoint.this.m_window.getContainer()) - WindowMovePoint.this.m_initialY;
                }
                if (WindowMovePoint.this.m_window.isStickWithinRootContainer()) {
                    final int deltaX = x - WindowMovePoint.this.m_window.getX();
                    final int deltaY = y - WindowMovePoint.this.m_window.getY();
                    final int wmpX = WindowMovePoint.this.getX(WindowMovePoint.this.m_windowRootContainer);
                    final int wmpY = WindowMovePoint.this.getY(WindowMovePoint.this.m_windowRootContainer);
                    final int wmpXWin = WindowMovePoint.this.getX(WindowMovePoint.this.m_window);
                    final int wmpYWin = WindowMovePoint.this.getY(WindowMovePoint.this.m_window);
                    final int safetyWidth = Math.min(50, WindowMovePoint.this.m_size.width);
                    final int safetyHeight = Math.min(50, WindowMovePoint.this.m_size.height);
                    if (WindowMovePoint.this.m_horizontal) {
                        if (WindowMovePoint.this.m_window.getStickFullyWithinRootContainer()) {
                            if (WindowMovePoint.this.m_window.getX() + deltaX < 0) {
                                x = 0;
                            }
                            else if (WindowMovePoint.this.m_window.getX() + WindowMovePoint.this.m_window.getWidth() + deltaX > WindowMovePoint.this.m_windowRootContainer.getWidth()) {
                                x = WindowMovePoint.this.m_windowRootContainer.getWidth() - WindowMovePoint.this.m_window.getWidth();
                            }
                        }
                        else if (wmpX + deltaX + WindowMovePoint.this.m_size.width - safetyWidth < 0) {
                            x = -wmpXWin - WindowMovePoint.this.m_size.width + safetyWidth;
                        }
                        else if (wmpX + deltaX + safetyWidth > WindowMovePoint.this.m_windowRootContainer.getWidth()) {
                            x = WindowMovePoint.this.m_windowRootContainer.getWidth() - (wmpXWin + safetyWidth);
                        }
                    }
                    if (WindowMovePoint.this.m_vertical) {
                        if (WindowMovePoint.this.m_window.getStickFullyWithinRootContainer()) {
                            if (WindowMovePoint.this.m_window.getY() + deltaY < 0) {
                                y = 0;
                            }
                            else if (WindowMovePoint.this.m_window.getY() + WindowMovePoint.this.m_window.getHeight() + deltaY > WindowMovePoint.this.m_windowRootContainer.getHeight()) {
                                y = WindowMovePoint.this.m_windowRootContainer.getHeight() - WindowMovePoint.this.m_window.getHeight();
                            }
                        }
                        else if (wmpY + deltaY + WindowMovePoint.this.m_size.height - safetyHeight < 0) {
                            y = -wmpYWin - WindowMovePoint.this.m_size.height + safetyHeight;
                        }
                        else if (wmpY + deltaY + safetyHeight > WindowMovePoint.this.m_windowRootContainer.getHeight()) {
                            y = WindowMovePoint.this.m_windowRootContainer.getHeight() - (wmpYWin + safetyHeight);
                        }
                    }
                }
                if (WindowMovePoint.this.m_window.getStickData() != null) {
                    final Point p = new Point(x, y);
                    WindowStickManager.getInstance().processWindowMove(WindowMovePoint.this.m_window, WindowMovePoint.this.m_window.getX(), WindowMovePoint.this.m_window.getY(), x, y, p, MasterRootContainer.getInstance().isShiftPressed());
                    x = p.x;
                    y = p.y;
                    if (m.getX(WindowMovePoint.this.m_window.getContainer()) <= 0) {
                        WindowMovePoint.this.m_window.dispatchEvent(new WindowStickEvent(WindowMovePoint.this.m_window, Alignment9.WEST));
                    }
                    else if (m.getX(WindowMovePoint.this.m_window.getContainer()) >= WindowMovePoint.this.m_window.getContainer().getWidth()) {
                        WindowMovePoint.this.m_window.dispatchEvent(new WindowStickEvent(WindowMovePoint.this.m_window, Alignment9.EAST));
                    }
                    else if (m.getY(WindowMovePoint.this.m_window.getContainer()) <= 0) {
                        WindowMovePoint.this.m_window.dispatchEvent(new WindowStickEvent(WindowMovePoint.this.m_window, Alignment9.SOUTH));
                    }
                    else if (m.getY(WindowMovePoint.this.m_window.getContainer()) >= WindowMovePoint.this.m_window.getContainer().getHeight()) {
                        WindowMovePoint.this.m_window.dispatchEvent(new WindowStickEvent(WindowMovePoint.this.m_window, Alignment9.NORTH));
                    }
                }
                WindowMovePoint.this.m_window.setPosition(x, y);
                if (WindowMovePoint.this.m_window.getContainer() != null) {
                    WindowMovePoint.this.m_window.getContainer().invalidateMinSize();
                }
                return false;
            }
        }, false);
    }
    
    @Override
    public void addedToWidgetTree() {
        super.addedToWidgetTree();
        this.m_window = this.getWidgetParentOfType(Window.class);
        if (this.m_window != null) {
            this.m_windowRootContainer = this.m_window.getWidgetParentOfType(RootContainer.class);
            this.m_window.addWindowMovePoint(this);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        this.m_window = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.addListeners();
        this.setCursorType(CursorFactory.CursorType.MOVE);
        this.m_nonBlocking = false;
        this.m_horizontal = true;
        this.m_vertical = true;
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        final WindowMovePoint wrp = (WindowMovePoint)c;
        super.copyElement(c);
        wrp.setHorizontal(this.m_horizontal);
        wrp.setVertical(this.m_vertical);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == WindowMovePoint.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != WindowMovePoint.VERTICAL_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setVertical(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == WindowMovePoint.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != WindowMovePoint.VERTICAL_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setVertical(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public Widget getWidget(final int x, final int y) {
        return MasterRootContainer.getInstance().isMovePointMode() ? this : super.getWidget(x, y);
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        this.checkEnabled();
    }
    
    @Override
    public void setNetEnabled(final boolean netEnabled) {
        super.setNetEnabled(netEnabled);
        this.checkEnabled();
    }
    
    private void checkEnabled() {
        if (this.isEnabledFull()) {
            this.setCursorType(CursorFactory.CursorType.MOVE);
        }
        else {
            this.setCursorType(CursorFactory.CursorType.DEFAULT);
        }
    }
    
    static {
        HORIZONTAL_HASH = "horizontal".hashCode();
        VERTICAL_HASH = "vertical".hashCode();
    }
}
