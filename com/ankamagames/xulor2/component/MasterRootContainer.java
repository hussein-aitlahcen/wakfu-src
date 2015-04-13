package com.ankamagames.xulor2.component;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.*;
import gnu.trove.*;
import com.ankamagames.xulor2.core.event.*;
import java.util.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.core.dragndrop.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.xulor2.core.*;
import java.awt.event.*;
import com.ankamagames.xulor2.core.keyManager.*;
import java.awt.*;

public class MasterRootContainer extends RootContainer
{
    private static final Logger m_logger;
    public static final String TAG = "MasterRootContainer";
    private static final MasterRootContainer m_instance;
    private Widget m_mouseOver;
    private Widget m_previousMousePress;
    private MouseEvent m_lastMouseMovedEvent;
    private com.ankamagames.xulor2.event.MouseEvent m_currentMouseEvent;
    private MouseEvent m_currentAWTMouseEvent;
    private Dimension m_newSize;
    private boolean m_resized;
    private Widget m_dragged;
    private boolean m_startedDragging;
    private int m_dragButton;
    private final TIntHashSet m_pressedButtons;
    private Popup m_popup;
    private boolean m_keyEventConsumed;
    private boolean m_isShiftPressed;
    private final ArrayList<MasterRootContainerListener> m_listeners;
    private boolean m_movePointMode;
    
    private MasterRootContainer() {
        super();
        this.m_lastMouseMovedEvent = null;
        this.m_currentMouseEvent = null;
        this.m_currentAWTMouseEvent = null;
        this.m_newSize = null;
        this.m_resized = false;
        this.m_startedDragging = false;
        this.m_pressedButtons = new TIntHashSet();
        this.m_keyEventConsumed = true;
        this.m_isShiftPressed = false;
        this.m_listeners = new ArrayList<MasterRootContainerListener>();
        this.m_movePointMode = false;
    }
    
    public static MasterRootContainer getInstance() {
        return MasterRootContainer.m_instance;
    }
    
    public void addListeners() {
        this.addEventListener(Events.KEY_TYPED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                MasterRootContainer.this.m_keyEventConsumed = true;
                return false;
            }
        }, false);
    }
    
    @Override
    public void setSize(final int width, final int height, final boolean applyAtOnce) {
        this.m_newSize = new Dimension(width, height);
        this.setNeedsToPreProcess();
    }
    
    @Override
    public MasterRootContainer getMasterRootContainer() {
        return this;
    }
    
    @Override
    public String getTag() {
        return "MasterRootContainer";
    }
    
    public Widget getMouseOver() {
        return this.m_mouseOver;
    }
    
    public com.ankamagames.xulor2.event.MouseEvent getCurrentMouseEvent() {
        return this.m_currentMouseEvent;
    }
    
    public MouseEvent getCurrentAWTMouseEvent() {
        return this.m_currentAWTMouseEvent;
    }
    
    public Popup getPopupContainer() {
        return this.m_popup;
    }
    
    public boolean isResized() {
        return this.m_resized;
    }
    
    public boolean isKeyEventConsumed() {
        return this.m_keyEventConsumed;
    }
    
    public void setKeyEventConsumed(final boolean keyEventConsumed) {
        this.m_keyEventConsumed = keyEventConsumed;
    }
    
    @Override
    public void setElementMap(final ElementMap map) {
        super.setElementMap(map);
        this.m_layeredContainer.setElementMap(map);
    }
    
    @Override
    public void setVisible(final boolean visible) {
        final boolean wasVisibleBefore = this.m_visible;
        super.setVisible(visible);
        CursorFactory.getInstance().show(CursorFactory.CursorType.DEFAULT);
        if (wasVisibleBefore != visible) {
            for (final MasterRootContainerListener listener : this.m_listeners) {
                if (visible) {
                    listener.onMasterRootContainerShown(this);
                }
                else {
                    listener.onMasterRootContainerHidden(this);
                }
            }
        }
    }
    
    public void addListener(final MasterRootContainerListener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removeListener(final MasterRootContainerListener listener) {
        this.m_listeners.remove(listener);
    }
    
    public void reInitializeContent() {
        this.onCheckIn();
        this.onCheckOut();
        this.setSize(Graphics.getInstance().getDrawableSize());
        final XulorScene scene = Xulor.getInstance().getScene();
        scene.applyScale(this.m_entity);
    }
    
    @Override
    public boolean isInWidgetTree() {
        return true;
    }
    
    @Override
    public boolean isInTree() {
        return true;
    }
    
    public boolean isDragging() {
        return this.m_dragged != null;
    }
    
    public Widget getDragged() {
        return this.m_dragged;
    }
    
    public int getDragButton() {
        return this.m_dragButton;
    }
    
    public void setDragged(final Widget dragged, final int dragButton) {
        this.m_dragged = dragged;
        this.m_dragButton = dragButton;
    }
    
    public boolean isShiftPressed() {
        return this.m_isShiftPressed;
    }
    
    public void cancelDragNDrop() {
        if (this.m_dragged != null) {
            this.m_dragged = null;
            this.m_dragButton = 0;
            DragNDropManager.getInstance().cancel();
        }
    }
    
    public void cleanUpReferences(final Widget w) {
        if (this.m_dragged == w) {
            this.m_dragged = null;
            this.m_dragButton = 0;
            DragNDropManager.getInstance().cleanUp();
        }
        if (this.m_mouseOver == w) {
            this.m_mouseOver = null;
        }
        if (this.m_previousMousePress == w) {
            this.m_previousMousePress = null;
        }
    }
    
    public void reSendMouseMoved() {
        if (this.m_lastMouseMovedEvent != null) {
            this.mouseMoved(this.m_lastMouseMovedEvent);
        }
    }
    
    public boolean isMovePointMode() {
        return this.m_movePointMode;
    }
    
    public void setMovePointMode(final boolean movePointMode) {
        this.m_movePointMode = movePointMode;
        CursorFactory.getInstance().show(movePointMode ? CursorFactory.CursorType.MOVE : CursorFactory.CursorType.DEFAULT);
    }
    
    public boolean mouseMoved(final MouseEvent mouseEvent) {
        this.m_lastMouseMovedEvent = mouseEvent;
        this.m_currentAWTMouseEvent = mouseEvent;
        final int mouseY = this.m_size.height - mouseEvent.getY();
        final int mouseX = mouseEvent.getX();
        boolean modal = false;
        if (!ModalManager.getInstance().isCoordinateActive(mouseX, mouseY)) {
            modal = true;
        }
        if (this.m_dragged == null && modal) {
            this.m_currentAWTMouseEvent = null;
            return true;
        }
        MouseManager.getInstance().setXY(mouseX, mouseY);
        final Widget w = modal ? null : this.getWidget(mouseX, mouseY);
        if (w != null) {
            if (w != this || w != this.m_mouseOver) {
                CursorFactory.getInstance().show(w.getCursorType());
            }
            final com.ankamagames.xulor2.event.MouseEvent me = com.ankamagames.xulor2.event.MouseEvent.checkOut(mouseEvent);
            me.setTarget(w);
            me.setScreenX(mouseX);
            me.setScreenY(mouseY);
            me.setType(Events.MOUSE_MOVED);
            this.m_currentMouseEvent = me;
            this.m_currentAWTMouseEvent = mouseEvent;
            w.dispatchEvent(me);
        }
        if (this.m_movePointMode) {
            CursorFactory.getInstance().show(CursorFactory.CursorType.MOVE);
        }
        if (w != this.m_mouseOver) {
            if (this.m_mouseOver != null) {
                final com.ankamagames.xulor2.event.MouseEvent me = com.ankamagames.xulor2.event.MouseEvent.checkOut(mouseEvent);
                me.setTarget(this.m_mouseOver);
                me.setScreenX(mouseX);
                me.setScreenY(mouseY);
                me.setType(Events.MOUSE_EXITED);
                this.m_currentMouseEvent = me;
                this.m_mouseOver.dispatchEvent(me);
                this.m_currentMouseEvent = null;
                final PopupEvent pe = PopupEvent.checkOut();
                pe.setTarget(this.m_mouseOver);
                pe.setType(Events.POPUP_HIDE);
                this.m_mouseOver.dispatchEvent(pe);
            }
            if (w != null) {
                final com.ankamagames.xulor2.event.MouseEvent me = com.ankamagames.xulor2.event.MouseEvent.checkOut(mouseEvent);
                me.setTarget(w);
                me.setScreenX(mouseX);
                me.setScreenY(mouseY);
                me.setType(Events.MOUSE_ENTERED);
                w.dispatchEvent(this.m_currentMouseEvent = me);
                this.m_currentMouseEvent = null;
                final PopupEvent pe = PopupEvent.checkOut();
                pe.setTarget(w);
                pe.setType(Events.POPUP_DISPLAY);
                w.dispatchEvent(pe);
            }
            this.m_mouseOver = w;
        }
        if (this.m_dragged != null) {
            (this.m_currentMouseEvent = com.ankamagames.xulor2.event.MouseEvent.checkOut(mouseEvent)).setTarget(this.m_dragged);
            this.m_currentMouseEvent.setScreenX(mouseX);
            this.m_currentMouseEvent.setScreenY(mouseY);
            this.m_currentMouseEvent.setType(Events.MOUSE_DRAGGED);
            if (!DragNDropManager.getInstance().mouseMoved(this.m_mouseOver, mouseX, mouseY)) {
                if (this.m_startedDragging) {
                    final com.ankamagames.xulor2.event.MouseEvent dragOutEvent = com.ankamagames.xulor2.event.MouseEvent.checkOut(this.m_currentMouseEvent);
                    dragOutEvent.setType(Events.MOUSE_DRAGGED_IN);
                    this.m_dragged.dispatchEvent(dragOutEvent);
                    this.m_startedDragging = false;
                }
                this.m_dragged.dispatchEvent(this.m_currentMouseEvent);
            }
            else {
                this.m_currentMouseEvent.release();
            }
            this.m_currentMouseEvent = null;
        }
        GraphicalMouseManager.getInstance().setXY(mouseEvent.getX(), mouseY);
        this.m_currentAWTMouseEvent = null;
        return this.m_dragged != null;
    }
    
    public boolean mousePressed(final MouseEvent mouseEvent) {
        final int button = mouseEvent.getButton();
        if (button == 0) {
            return false;
        }
        this.m_currentAWTMouseEvent = mouseEvent;
        final int mouseX = mouseEvent.getX();
        final int mouseY = this.m_size.height - mouseEvent.getY();
        if (!ModalManager.getInstance().isCoordinateActive(mouseX, mouseY)) {
            this.m_currentAWTMouseEvent = null;
            this.m_pressedButtons.add(button);
            return true;
        }
        MouseManager.getInstance().setXY(mouseX, mouseY);
        Widget w = this.getWidget(mouseX, mouseY);
        if (w == null) {
            w = this;
        }
        FocusManager.getInstance().setFocused(w);
        final com.ankamagames.xulor2.event.MouseEvent me = com.ankamagames.xulor2.event.MouseEvent.checkOut(mouseEvent);
        me.setTarget(w);
        me.setScreenX(mouseX);
        me.setScreenY(mouseY);
        me.setType(Events.MOUSE_PRESSED);
        this.m_currentMouseEvent = me;
        if (this.m_dragged == null && w != this) {
            this.m_startedDragging = true;
            this.m_dragged = w;
            this.m_dragButton = button;
            DragNDropManager.getInstance().mousePressed(w, mouseX, mouseY);
        }
        MouseManager.getInstance().notifyPressed(w, me);
        w.dispatchEvent(me);
        this.m_currentMouseEvent = null;
        this.m_currentAWTMouseEvent = null;
        if (w != this) {
            this.m_pressedButtons.add(button);
        }
        return w != this;
    }
    
    public boolean mouseReleased(final MouseEvent mouseEvent) {
        final int button = mouseEvent.getButton();
        if (button == 0) {
            return false;
        }
        this.m_currentAWTMouseEvent = mouseEvent;
        final int mouseX = mouseEvent.getX();
        final int mouseY = this.m_size.height - mouseEvent.getY();
        final boolean dragging = this.m_dragButton == button;
        final boolean modal = !ModalManager.getInstance().isCoordinateActive(mouseX, mouseY);
        if (modal && !dragging) {
            this.m_currentAWTMouseEvent = null;
            return this.m_pressedButtons.remove(button);
        }
        MouseManager.getInstance().setXY(mouseX, mouseY);
        Widget w = this.getWidget(mouseX, mouseY);
        if (w == null) {
            w = this;
        }
        com.ankamagames.xulor2.event.MouseEvent me = null;
        if (!modal) {
            me = com.ankamagames.xulor2.event.MouseEvent.checkOut(mouseEvent);
            me.setTarget(w);
            me.setScreenX(mouseX);
            me.setScreenY(mouseY);
            me.setType(Events.MOUSE_RELEASED);
            this.m_currentMouseEvent = me;
        }
        if (dragging) {
            final com.ankamagames.xulor2.event.MouseEvent dragOutEvent = com.ankamagames.xulor2.event.MouseEvent.checkOut(mouseEvent);
            dragOutEvent.setTarget(w);
            dragOutEvent.setScreenX(mouseX);
            dragOutEvent.setScreenY(mouseY);
            dragOutEvent.setType(Events.MOUSE_DRAGGED_OUT);
            this.m_dragged.dispatchEvent(dragOutEvent);
            this.m_dragged = null;
            this.m_dragButton = 0;
            DragNDropManager.getInstance().mouseReleased(w, mouseX, mouseY);
        }
        if (!modal) {
            MouseManager.getInstance().notifyReleased(w, me);
            w.dispatchEvent(me);
            this.m_currentMouseEvent = null;
        }
        this.m_currentAWTMouseEvent = null;
        return this.m_pressedButtons.remove(button);
    }
    
    public boolean mouseWheelMoved(final MouseWheelEvent mouseEvent) {
        this.m_currentAWTMouseEvent = mouseEvent;
        final int mouseX = mouseEvent.getX();
        final int mouseY = this.m_size.height - mouseEvent.getY();
        if (!ModalManager.getInstance().isCoordinateActive(mouseX, mouseY)) {
            return true;
        }
        Widget w = this.getWidget(mouseX, mouseY);
        if (w == null) {
            w = this;
        }
        final com.ankamagames.xulor2.event.MouseEvent me = com.ankamagames.xulor2.event.MouseEvent.checkOut(mouseEvent);
        me.setTarget(w);
        me.setScreenX(mouseX);
        me.setScreenY(mouseY);
        me.setRotations(mouseEvent.getWheelRotation());
        me.setType(Events.MOUSE_WHEELED);
        w.dispatchEvent(this.m_currentMouseEvent = me);
        this.m_currentMouseEvent = null;
        this.m_currentAWTMouseEvent = null;
        return w != this;
    }
    
    public boolean keyPressed(final KeyEvent keyEvent) {
        boolean ret = false;
        if (!ModalManager.getInstance().isEmpty() && !Xulor.getInstance().getShortcutManager().isShortcutAlwaysActive(keyEvent)) {
            ret = true;
        }
        if (KeyManager.getInstance().keyPressed(keyEvent)) {
            return true;
        }
        if (keyEvent.getKeyCode() == 16) {
            this.m_isShiftPressed = true;
        }
        if (keyEvent.getKeyCode() == 17) {
            this.setMovePointMode(true);
        }
        if (keyEvent.getKeyCode() == 9) {
            if ((keyEvent.getModifiersEx() & 0x40) == 0x40) {
                FocusManager.getInstance().focusPrevious();
            }
            else {
                FocusManager.getInstance().focusNext();
            }
            return ret;
        }
        ret |= this.dispatchKeyEvent(keyEvent, Events.KEY_PRESSED);
        return ret;
    }
    
    public boolean keyReleased(final KeyEvent keyEvent) {
        boolean ret = false;
        if (!ModalManager.getInstance().isEmpty() && !Xulor.getInstance().getShortcutManager().isShortcutAlwaysActive(keyEvent)) {
            ret = true;
        }
        if (KeyManager.getInstance().keyReleased(keyEvent)) {
            return true;
        }
        if (keyEvent.getKeyCode() == 16) {
            this.m_isShiftPressed = false;
        }
        if (keyEvent.getKeyCode() == 17) {
            this.setMovePointMode(false);
        }
        final boolean result = this.dispatchKeyEvent(keyEvent, Events.KEY_RELEASED);
        if (result) {
            Xulor.getInstance().getShortcutManager().releaseCurrentKeyCode();
            this.setMovePointMode(false);
        }
        return result | ret;
    }
    
    public boolean keyTyped(final KeyEvent keyEvent) {
        return this.dispatchKeyEvent(keyEvent, Events.KEY_TYPED);
    }
    
    private boolean dispatchKeyEvent(final KeyEvent keyEvent, final Events type) {
        final int keyCode = keyEvent.getKeyCode();
        final Widget w = FocusManager.getInstance().getFocused();
        if (w != null) {
            final com.ankamagames.xulor2.event.KeyEvent ke = com.ankamagames.xulor2.event.KeyEvent.checkOut();
            ke.setTarget(w);
            ke.setKeyChar(keyEvent.getKeyChar());
            ke.setKeyCode(keyCode);
            ke.setModifiers(keyEvent.getModifiersEx());
            ke.setType(type);
            this.m_keyEventConsumed = false;
            final boolean retValue = w.dispatchEvent(ke);
            return this.m_keyEventConsumed || retValue;
        }
        return false;
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        if (this.m_resized) {
            this.m_resized = false;
        }
        boolean ret = false;
        if (this.m_newSize != null) {
            super.setSize(this.m_newSize.width, this.m_newSize.height, false);
            this.m_newSize = null;
            this.m_resized = true;
            ret = true;
        }
        ret |= super.preProcess(deltaTime);
        return ret;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        final Point centerOffset = Graphics.getInstance().getCenterOffset();
        this.m_entity.getTransformer().setTranslation(0, centerOffset.x, centerOffset.y + this.m_size.height);
        return ret;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setTreeDepth(0);
        this.m_nonBlocking = false;
        this.m_resized = false;
        this.m_enableResizeEvents = true;
        (this.m_popup = new Popup()).onCheckOut();
        this.m_layeredContainer.addWidgetToLayer(this.m_popup, 30000);
        CompassWidget.INSTANCE.onCheckOut();
        this.m_layeredContainer.addWidgetToLayer(CompassWidget.INSTANCE, -40000);
        CompassWidget.INSTANCE.setElementMap(new ElementMap("", Xulor.getInstance().getEnvironment()));
        CompassWidget.INSTANCE.onChildrenAdded();
        this.setScreenPosition(0, 0);
        this.addListeners();
        this.m_needsScissor = true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_dragged = null;
        this.m_lastMouseMovedEvent = null;
        this.m_mouseOver = null;
        this.m_currentMouseEvent = null;
        this.m_currentAWTMouseEvent = null;
        this.m_newSize = null;
        this.m_popup = null;
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
    }
    
    static {
        m_logger = Logger.getLogger((Class)MasterRootContainer.class);
        m_instance = new MasterRootContainer();
    }
}
