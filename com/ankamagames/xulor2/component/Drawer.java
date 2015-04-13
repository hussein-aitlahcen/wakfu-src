package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.xulor2.layout.*;

public class Drawer extends Container
{
    public static final String TAG = "drawer";
    public static final String THEME_STATIC_CONTAINER = "staticContainer";
    public static final String THEME_POPUP_CONTAINER = "popupContainer";
    private boolean m_popupIsBeingDisplayed;
    private boolean m_popupJustGotDisplayed;
    private EventListener m_mousePressedListener;
    private EventListener m_mouseReleasedListener;
    private Container m_popupContainer;
    private Container m_staticContainer;
    private Alignment9 m_align;
    private Alignment9 m_hotSpotPosition;
    public static final int ALIGN_HASH;
    public static final int HOT_SPOT_POSITION_HASH;
    
    public Drawer() {
        super();
        this.m_popupIsBeingDisplayed = false;
        this.m_popupJustGotDisplayed = true;
        this.m_popupContainer = null;
        this.m_staticContainer = null;
        this.m_align = Alignment9.SOUTH;
        this.m_hotSpotPosition = Alignment9.NORTH;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        boolean addWidget = true;
        if (e instanceof Container) {
            final Container container = (Container)e;
            final String teName = container.getThemeElementName();
            if (teName != null) {
                if (teName.equals("popupContainer")) {
                    if (this.m_popupContainer != null) {
                        this.m_popupContainer.release();
                    }
                    addWidget = false;
                    (this.m_popupContainer = container).setModalLevel(ModalManager.POP_UP_MODAL_LEVEL);
                    this.addPopupListener(this.m_popupContainer);
                }
                else if (teName.equals("staticContainer")) {
                    if (this.m_staticContainer != null) {
                        this.m_staticContainer.destroySelfFromParent();
                    }
                    this.m_staticContainer = container;
                }
            }
        }
        if (addWidget) {
            super.add(e);
        }
    }
    
    @Override
    public Widget getWidgetByThemeElementName(final String themeElementName, final boolean compilationMode) {
        if ("staticContainer".equalsIgnoreCase(themeElementName)) {
            if (this.m_staticContainer != null) {
                return this.m_staticContainer;
            }
        }
        else if ("popupContainer".equalsIgnoreCase(themeElementName) && this.m_popupContainer != null) {
            return this.m_popupContainer;
        }
        return null;
    }
    
    @Override
    public String getTag() {
        return "drawer";
    }
    
    @Override
    public void setElementMap(final ElementMap map) {
        super.setElementMap(map);
        if (this.m_popupContainer != null) {
            this.m_popupContainer.setElementMap(map);
        }
    }
    
    public void setHotSpotPosition(final Alignment9 hotSpotPosition) {
        if (hotSpotPosition != null) {
            this.m_hotSpotPosition = hotSpotPosition;
        }
    }
    
    public void setAlign(final Alignment9 align) {
        if (align != null) {
            this.m_align = align;
        }
    }
    
    @Override
    public void addedToWidgetTree() {
        super.addedToWidgetTree();
        this.setFocusable(true);
    }
    
    public void addRootContainerlistener(final MasterRootContainer masterRootContainer) {
        this.m_mousePressedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                final MouseEvent e = (MouseEvent)event;
                if (Drawer.this.m_popupIsBeingDisplayed) {
                    if (Drawer.this.m_appearance == null) {
                        return true;
                    }
                    if (Drawer.this.m_appearance.insideInsets(e.getX(Drawer.this), e.getY(Drawer.this))) {
                        return true;
                    }
                    final DecoratorAppearance app = Drawer.this.m_popupContainer.getAppearance();
                    if (app == null) {
                        return true;
                    }
                    if (!app.insideInsets(e.getX(Drawer.this.m_popupContainer), e.getY(Drawer.this.m_popupContainer))) {
                        Drawer.this.closePopup();
                        return true;
                    }
                }
                return false;
            }
        };
        masterRootContainer.addEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        this.m_mouseReleasedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (!Drawer.this.m_popupJustGotDisplayed && Drawer.this.m_popupIsBeingDisplayed) {
                    Drawer.this.closePopup();
                    return true;
                }
                if (Drawer.this.m_popupJustGotDisplayed) {
                    Drawer.this.m_popupJustGotDisplayed = false;
                }
                return false;
            }
        };
        masterRootContainer.addEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
    }
    
    public void addPopupListener(final Container popup) {
        popup.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                Drawer.this.closePopup();
                return false;
            }
        }, false);
    }
    
    public boolean isPopupIsBeingDisplayed() {
        return this.m_popupIsBeingDisplayed;
    }
    
    public void switchPopup() {
        if (this.m_popupIsBeingDisplayed) {
            this.closePopup();
        }
        else {
            this.openPopup();
        }
    }
    
    private void closePopup() {
        if (this.m_popupIsBeingDisplayed) {
            this.m_popupContainer.removeSelfFromParent();
            this.m_popupIsBeingDisplayed = false;
            XulorSoundManager.getInstance().comboBoxClose();
        }
    }
    
    private void openPopup() {
        if (!this.m_popupIsBeingDisplayed) {
            final Dimension idealSize = this.m_popupContainer.getPrefSize();
            int width = idealSize.width;
            int height = idealSize.height;
            final int displayX = this.getDisplayX();
            final int displayY = this.getDisplayY();
            final MasterRootContainer rootContainer = MasterRootContainer.getInstance();
            Alignment9 align = this.m_align;
            Alignment9 hotSpot = this.m_hotSpotPosition;
            int y = this.getDisplayY() + align.getY(this.getHeight()) - hotSpot.getY(height);
            if (y < 0 || y > rootContainer.getAppearance().getContentHeight() - height) {
                align = align.getYOpposite();
                hotSpot = hotSpot.getYOpposite();
            }
            y = this.getDisplayY() + align.getY(this.getHeight()) - hotSpot.getY(height);
            y = Math.max(0, Math.min(y, rootContainer.getAppearance().getContentHeight() - height));
            if (displayY - height < 0) {
                if (displayY + this.getHeight() + height > rootContainer.getHeight()) {
                    height = displayY;
                    y = 0;
                }
            }
            int x = displayX + align.getX(this.getWidth()) - hotSpot.getX(width);
            if (x < 0 || x > rootContainer.getAppearance().getContentWidth() - width) {
                align = align.getXOpposite();
                hotSpot = hotSpot.getXOpposite();
            }
            x = this.getDisplayX() + align.getX(this.getWidth()) - hotSpot.getX(width);
            x = Math.max(0, Math.min(x, rootContainer.getAppearance().getContentWidth() - width));
            if (displayX - width < 0) {
                if (displayX + this.getWidth() + width > rootContainer.getWidth()) {
                    width = displayX;
                    x = 0;
                }
            }
            this.m_popupContainer.setSizeToPrefSize();
            width = Math.max(this.m_popupContainer.getWidth(), width);
            height = Math.max(this.m_popupContainer.getHeight(), height);
            this.m_popupContainer.setSize(width, height);
            this.m_popupContainer.setPosition(x, y);
            this.m_popupContainer.setNonBlocking(false);
            rootContainer.getLayeredContainer().addWidgetToLayer(this.m_popupContainer, 30000);
            this.m_popupIsBeingDisplayed = true;
            this.m_popupJustGotDisplayed = true;
            XulorSoundManager.getInstance().comboBoxClick();
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
        this.m_mouseReleasedListener = null;
        this.m_mousePressedListener = null;
        this.m_align = null;
        this.m_hotSpotPosition = null;
        this.m_popupContainer.destroySelfFromParent();
        this.m_popupContainer = null;
        this.m_staticContainer = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final Layout layout = new Layout();
        layout.onCheckOut();
        this.add(layout);
        this.m_popupContainer = Container.checkOut();
        this.m_staticContainer = Container.checkOut();
        this.m_nonBlocking = false;
        this.addRootContainerlistener(MasterRootContainer.getInstance());
    }
    
    @Override
    public void copyElement(final BasicElement s) {
        final Drawer e = (Drawer)s;
        super.copyElement(e);
        final Widget popup = (Widget)this.m_popupContainer.cloneElementStructure();
        popup.m_styleIsDirty = false;
        popup.removeAllEventListeners();
        e.add(popup);
        e.removeEventListener(Events.MOUSE_PRESSED, this.m_mousePressedListener, false);
        e.removeEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, false);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Drawer.ALIGN_HASH) {
            this.setAlign(Alignment9.value(value));
        }
        else {
            if (hash != Drawer.HOT_SPOT_POSITION_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setHotSpotPosition(Alignment9.value(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Drawer.ALIGN_HASH) {
            this.setAlign((Alignment9)value);
        }
        else {
            if (hash != Drawer.HOT_SPOT_POSITION_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setHotSpotPosition((Alignment9)value);
        }
        return true;
    }
    
    static {
        ALIGN_HASH = "align".hashCode();
        HOT_SPOT_POSITION_HASH = "hotSpotPosition".hashCode();
    }
    
    private class Layout extends AbstractLayoutManager
    {
        public boolean canBeCloned() {
            return false;
        }
        
        @Override
        public Dimension getContentMinSize(final Container container) {
            return Drawer.this.getContentPrefSize();
        }
        
        @Override
        public Dimension getContentPreferedSize(final Container container) {
            return (Drawer.this.m_staticContainer != null) ? Drawer.this.m_staticContainer.getPrefSize() : new Dimension();
        }
        
        @Override
        public void layoutContainer(final Container parent) {
            if (Drawer.this.m_staticContainer != null && Drawer.this.m_staticContainer.getVisible()) {
                Drawer.this.m_staticContainer.setPosition(0, 0);
                Drawer.this.m_staticContainer.setSize(new Dimension(Drawer.this.m_appearance.getContentWidth(), Drawer.this.m_appearance.getContentHeight()));
            }
        }
    }
}
