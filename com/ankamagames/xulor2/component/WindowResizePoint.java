package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.util.alignment.*;
import java.awt.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;

public class WindowResizePoint extends Container
{
    public static final String TAG = "WindowResizePoint";
    public static final String SHORT_TAG = "WRP";
    private Window m_window;
    private Alignment9 m_pointAlign;
    private Point m_oldPosition;
    private boolean m_beingResized;
    private Container m_windowRootContainer;
    private EventListener m_mouseReleasedListener;
    public static final int POINT_ALIGN_HASH;
    
    public WindowResizePoint() {
        super();
        this.m_window = null;
        this.m_beingResized = false;
        this.m_windowRootContainer = null;
    }
    
    @Override
    public String getTag() {
        return "WindowResizePoint";
    }
    
    public void setPointAlign(final Alignment9 align) {
        this.m_pointAlign = align;
        switch (this.m_pointAlign) {
            case EAST:
            case WEST: {
                this.setCursorType(CursorFactory.CursorType.HORIZONTAL_RESIZE);
                break;
            }
            case NORTH:
            case SOUTH: {
                this.setCursorType(CursorFactory.CursorType.VERTICAL_RESIZE);
                break;
            }
            case NORTH_WEST:
            case SOUTH_EAST: {
                this.setCursorType(CursorFactory.CursorType.NW_RESIZE);
                break;
            }
            case NORTH_EAST:
            case SOUTH_WEST: {
                this.setCursorType(CursorFactory.CursorType.SW_RESIZE);
                break;
            }
            case CENTER: {
                this.setCursorType(CursorFactory.CursorType.MOVE);
                break;
            }
        }
    }
    
    public Alignment9 getPointAlign() {
        return this.m_pointAlign;
    }
    
    public void setWindow(final Window w) {
        this.m_window = w;
    }
    
    public Window getWindow() {
        return this.m_window;
    }
    
    protected int setCheckedWidth(int width) {
        final int deltaWidth = 0;
        final Dimension pref = this.m_window.getPrefSize();
        if (width >= pref.width) {
            this.m_window.setSize(width, this.m_window.m_size.height);
        }
        else {
            width = pref.width;
            this.m_window.setSize(width, this.m_window.m_size.height);
        }
        return width;
    }
    
    protected int setCheckedHeight(int height) {
        final int deltaHeight = 0;
        final Dimension pref = this.m_window.getPrefSize();
        if (height >= pref.height) {
            this.m_window.setSize(this.m_window.m_size.width, height);
        }
        else {
            height = pref.height;
            this.m_window.setSize(this.m_window.m_size.width, height);
        }
        return height;
    }
    
    public void addListeners() {
        this.m_mouseReleasedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                WindowResizePoint.this.m_beingResized = false;
                return false;
            }
        };
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        this.addEventListener(Events.MOUSE_DRAGGED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent m = (MouseEvent)event;
                if (!WindowResizePoint.this.m_beingResized) {
                    WindowResizePoint.this.m_oldPosition = new Point(m.getX(WindowResizePoint.this.m_window.getContainer()), m.getY(WindowResizePoint.this.m_window.getContainer()));
                }
                final int deltaX = m.getX(WindowResizePoint.this.m_window.getContainer()) - WindowResizePoint.this.m_oldPosition.x;
                final int deltaY = m.getY(WindowResizePoint.this.m_window.getContainer()) - WindowResizePoint.this.m_oldPosition.y;
                final int refX = WindowResizePoint.this.m_window.getX() + WindowResizePoint.this.m_window.getWidth();
                final int refY = WindowResizePoint.this.m_window.getY() + WindowResizePoint.this.m_window.getHeight();
                int newWidth = WindowResizePoint.this.m_window.getWidth();
                int newHeight = WindowResizePoint.this.m_window.getHeight();
                int newX = WindowResizePoint.this.m_window.getX();
                int newY = WindowResizePoint.this.m_window.getY();
                switch (WindowResizePoint.this.m_pointAlign) {
                    case EAST: {
                        newWidth = Math.min(WindowResizePoint.this.setCheckedWidth(WindowResizePoint.this.m_window.getWidth() + deltaX), WindowResizePoint.this.m_windowRootContainer.getWidth() - WindowResizePoint.this.m_window.getX());
                        break;
                    }
                    case NORTH: {
                        newHeight = Math.min(WindowResizePoint.this.setCheckedHeight(WindowResizePoint.this.m_window.getHeight() + deltaY), WindowResizePoint.this.m_windowRootContainer.getHeight() - WindowResizePoint.this.m_window.getY());
                        break;
                    }
                    case SOUTH: {
                        newHeight = Math.min(WindowResizePoint.this.setCheckedHeight(WindowResizePoint.this.m_window.getHeight() - deltaY), refY);
                        newY = refY - newHeight;
                        break;
                    }
                    case NORTH_EAST: {
                        newWidth = Math.min(WindowResizePoint.this.setCheckedWidth(WindowResizePoint.this.m_window.getWidth() + deltaX), WindowResizePoint.this.m_windowRootContainer.getWidth() - WindowResizePoint.this.m_window.getX());
                        newHeight = Math.min(WindowResizePoint.this.setCheckedHeight(WindowResizePoint.this.m_window.getHeight() + deltaY), WindowResizePoint.this.m_windowRootContainer.getHeight() - WindowResizePoint.this.m_window.getY());
                        break;
                    }
                    case NORTH_WEST: {
                        newHeight = Math.min(WindowResizePoint.this.setCheckedHeight(WindowResizePoint.this.m_window.getHeight() + deltaY), WindowResizePoint.this.m_windowRootContainer.getHeight() - WindowResizePoint.this.m_window.getY());
                        newWidth = Math.min(WindowResizePoint.this.setCheckedWidth(WindowResizePoint.this.m_window.getWidth() - deltaX), refX);
                        newX = refX - newWidth;
                        break;
                    }
                    case SOUTH_WEST: {
                        newWidth = Math.min(WindowResizePoint.this.setCheckedWidth(WindowResizePoint.this.m_window.getWidth() - deltaX), refX);
                        newX = refX - newWidth;
                        newHeight = Math.min(WindowResizePoint.this.setCheckedHeight(WindowResizePoint.this.m_window.getHeight() - deltaY), refY);
                        newY = refY - newHeight;
                        break;
                    }
                    case SOUTH_EAST: {
                        newHeight = Math.min(WindowResizePoint.this.setCheckedHeight(WindowResizePoint.this.m_window.getHeight() - deltaY), refY);
                        newY = refY - newHeight;
                        newWidth = Math.min(WindowResizePoint.this.setCheckedWidth(WindowResizePoint.this.m_window.getWidth() + deltaX), WindowResizePoint.this.m_windowRootContainer.getWidth() - WindowResizePoint.this.m_window.getX());
                        break;
                    }
                    case WEST: {
                        newWidth = Math.min(WindowResizePoint.this.setCheckedWidth(WindowResizePoint.this.m_window.getWidth() - deltaX), refX);
                        newX = refX - newWidth;
                        break;
                    }
                }
                WindowResizePoint.this.m_window.setSize(newWidth, newHeight);
                WindowResizePoint.this.m_window.setPosition(newX, newY);
                WindowResizePoint.this.m_oldPosition = new Point(m.getX(WindowResizePoint.this.m_window.getContainer()), m.getY(WindowResizePoint.this.m_window.getContainer()));
                if (WindowResizePoint.this.m_window.getContainer() != null) {
                    WindowResizePoint.this.m_window.getContainer().invalidateMinSize();
                }
                WindowResizePoint.this.m_beingResized = true;
                return false;
            }
        }, false);
    }
    
    @Override
    public void addedToWidgetTree() {
        super.addedToWidgetTree();
        this.m_window = this.getParentOfType(Window.class);
        if (this.m_window != null) {
            this.m_windowRootContainer = this.m_window.getWidgetParentOfType(RootContainer.class);
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        this.m_window = null;
        this.m_pointAlign = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.addListeners();
        this.m_nonBlocking = false;
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        final WindowResizePoint wrp = (WindowResizePoint)c;
        super.copyElement(c);
        wrp.setPointAlign(this.m_pointAlign);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == WindowResizePoint.POINT_ALIGN_HASH) {
            this.setPointAlign(Alignment9.value(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
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
            this.setPointAlign(this.m_pointAlign);
        }
        else {
            this.setCursorType(CursorFactory.CursorType.DEFAULT);
        }
    }
    
    static {
        POINT_ALIGN_HASH = "pointAlign".hashCode();
    }
}
