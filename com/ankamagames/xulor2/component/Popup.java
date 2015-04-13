package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class Popup extends Container
{
    private EventListener m_currentHideListener;
    private Alignment9 m_hotSpotPosition;
    private Alignment9 m_align;
    private PopupClient m_client;
    private Widget m_contentReference;
    private String m_clientElementMap;
    private boolean m_hideOnClick;
    private boolean m_enableSwitchXAlign;
    private boolean m_enableSwitchYAlign;
    private int m_XOffset;
    private int m_YOffset;
    public static final int ALIGN_HASH;
    public static final int HOT_SPOT_POSITION_HASH;
    
    public Popup() {
        super();
        this.m_currentHideListener = null;
        this.m_hotSpotPosition = Alignment9.NORTH_WEST;
        this.m_align = Alignment9.SOUTH_EAST;
        this.m_clientElementMap = null;
        this.m_hideOnClick = true;
        this.m_enableSwitchXAlign = true;
        this.m_enableSwitchYAlign = true;
    }
    
    @Override
    public void addedToWidgetTree() {
        this.m_currentHideListener = new EventListener() {
            @Override
            public boolean run(final Event e) {
                if (Popup.this.m_hideOnClick && Popup.this.getVisible()) {
                    Popup.this.hide();
                }
                return false;
            }
        };
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_PRESSED, this.m_currentHideListener, false);
        this.addEventListener(Events.MOUSE_PRESSED, new EventListener() {
            @Override
            public boolean run(final Event e) {
                return true;
            }
        }, false);
        this.addEventListener(Events.MOUSE_RELEASED, new EventListener() {
            @Override
            public boolean run(final Event e) {
                if (Popup.this.m_hideOnClick && Popup.this.getVisible()) {
                    Popup.this.hide();
                }
                return true;
            }
        }, false);
    }
    
    @Override
    public void removedFromWidgetTree() {
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_PRESSED, this.m_currentHideListener, false);
    }
    
    public void show() {
        if (!this.getVisible()) {
            this.setVisible(true);
            this.setSizeToPrefSize();
            this.setPositionToOptimal();
        }
    }
    
    @Override
    public void validate() {
        super.validate();
        this.setPositionToOptimal();
    }
    
    public void setPositionToOptimal() {
        if (this.m_client != null && this.m_align != null && this.m_hotSpotPosition != null) {
            final int clientX = this.m_client.getDisplayX();
            final int clientY = this.m_client.getDisplayY();
            Alignment9 align = this.m_align;
            Alignment9 hotSpot = this.m_hotSpotPosition;
            int x = clientX + align.getX(this.m_client.getWidth()) - hotSpot.getX(this.m_size.width);
            int y = clientY + align.getY(this.m_client.getHeight()) - hotSpot.getY(this.m_size.height);
            if (this.m_enableSwitchXAlign && (x < 0 || x > this.m_containerParent.getAppearance().getContentWidth() - this.m_size.width)) {
                align = align.getXOpposite();
                hotSpot = hotSpot.getXOpposite();
            }
            if (this.m_enableSwitchYAlign && (y < 0 || y > this.m_containerParent.getAppearance().getContentHeight() - this.m_size.height)) {
                align = align.getYOpposite();
                hotSpot = hotSpot.getYOpposite();
            }
            x = clientX + align.getX(this.m_client.getWidth()) - hotSpot.getX(this.m_size.width);
            y = clientY + align.getY(this.m_client.getHeight()) - hotSpot.getY(this.m_size.height);
            x = Math.max(0, Math.min(x, this.m_containerParent.getAppearance().getContentWidth() - this.m_size.width));
            y = Math.max(0, Math.min(y, this.m_containerParent.getAppearance().getContentHeight() - this.m_size.height));
            if (clientX >= x && clientY >= y && clientX < x + this.m_size.width && clientY < y + this.m_size.height) {
                x = clientX - this.m_size.width;
                x = Math.max(0, Math.min(x, this.m_containerParent.getAppearance().getContentWidth() - this.m_size.width));
            }
            this.setPosition(x + this.m_XOffset, y + this.m_YOffset);
        }
    }
    
    public void hide() {
        this.setVisible(false);
        this.m_hideOnClick = true;
    }
    
    public PopupClient getClient() {
        return this.m_client;
    }
    
    public void setClient(final PopupClient client) {
        this.m_clientElementMap = null;
        this.m_client = client;
        if (this.m_client != null) {
            ElementMap map;
            for (map = this.m_client.getElementMap(); map.getParentElementMap() != null; map = map.getParentElementMap()) {}
            this.m_clientElementMap = map.getId();
        }
    }
    
    public Alignment9 getHotSpotPosition() {
        return this.m_hotSpotPosition;
    }
    
    public void setHotSpotPosition(final Alignment9 align) {
        if (align != null) {
            this.m_hotSpotPosition = align;
        }
    }
    
    public boolean getHideOnClick() {
        return this.m_hideOnClick;
    }
    
    public void setHideOnClick(final boolean hideOnClick) {
        this.m_hideOnClick = hideOnClick;
    }
    
    public Alignment9 getAlign() {
        return this.m_align;
    }
    
    public void setAlign(final Alignment9 align) {
        this.m_align = align;
    }
    
    public void setXOffset(final int XOffset) {
        this.m_XOffset = XOffset;
    }
    
    public void setYOffset(final int YOffset) {
        this.m_YOffset = YOffset;
    }
    
    public void setContent(Widget w) {
        if (this.m_contentReference != w) {
            for (int i = this.m_widgetChildren.size() - 1; i >= 0; --i) {
                this.m_widgetChildren.get(i).destroySelfFromParent();
            }
            if (w != null) {
                w = (Widget)w.cloneElementStructure();
                this.add(w);
            }
            this.m_contentReference = w;
        }
    }
    
    public Widget getContent() {
        return this.m_contentReference;
    }
    
    public boolean getEnableSwitchXAlign() {
        return this.m_enableSwitchXAlign;
    }
    
    public void setEnableSwitchXAlign(final boolean enableSwitchXAlign) {
        this.m_enableSwitchXAlign = enableSwitchXAlign;
    }
    
    public boolean getEnableSwitchYAlign() {
        return this.m_enableSwitchYAlign;
    }
    
    public void setEnableSwitchYAlign(final boolean enableSwitchYAlign) {
        this.m_enableSwitchYAlign = enableSwitchYAlign;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final StaticLayoutData sld = new StaticLayoutData();
        sld.onCheckOut();
        this.add(sld);
        this.setVisible(false);
        Xulor.getInstance().addDialogUnloadListener(new DialogUnloadListener() {
            @Override
            public void dialogUnloaded(final String id) {
                if (Popup.this.m_client == null) {
                    return;
                }
                if (id.equals(Popup.this.m_clientElementMap)) {
                    Popup.this.setClient(null);
                    Popup.this.setContent(null);
                }
            }
        });
        this.m_hideOnClick = true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_align = null;
        this.m_hotSpotPosition = null;
        this.m_clientElementMap = null;
        this.m_currentHideListener = null;
        this.m_contentReference = null;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Popup.ALIGN_HASH) {
            this.setAlign(Alignment9.value(value));
        }
        else if (hash == Popup.HOT_SPOT_POSITION_HASH) {
            this.setHotSpotPosition(Alignment9.value(value));
        }
        else {
            if (hash != Popup.VISIBLE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setVisible(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Popup.ALIGN_HASH) {
            this.setAlign((Alignment9)value);
        }
        else if (hash == Popup.HOT_SPOT_POSITION_HASH) {
            this.setHotSpotPosition((Alignment9)value);
        }
        else {
            if (hash != Popup.VISIBLE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setVisible(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        ALIGN_HASH = "align".hashCode();
        HOT_SPOT_POSITION_HASH = "hotSpotPosition".hashCode();
    }
}
