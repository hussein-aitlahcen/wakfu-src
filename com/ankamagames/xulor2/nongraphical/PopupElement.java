package com.ankamagames.xulor2.nongraphical;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class PopupElement extends NonGraphicalElement
{
    public static final String TAG = "popup";
    private Alignment9 m_hotSpotPosition;
    private Alignment9 m_align;
    private boolean m_hideOnClick;
    private Widget m_content;
    private int m_XOffset;
    private int m_YOffset;
    public static final int ALIGN_HASH;
    public static final int HOT_SPOT_POSITION_HASH;
    public static final int HIDE_ON_CLICK_HASH;
    public static final int X_OFFSET_HASH;
    public static final int Y_OFFSET_HASH;
    
    public PopupElement() {
        super();
        this.m_hotSpotPosition = Alignment9.NORTH_WEST;
        this.m_align = Alignment9.SOUTH_EAST;
        this.m_hideOnClick = true;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof Widget) {
            (this.m_content = (Widget)e).setIsATemplate(true);
            super.add(e, false);
        }
        else {
            super.add(e);
        }
    }
    
    @Override
    public String getTag() {
        return "popup";
    }
    
    public Alignment9 getHotSpotPosition() {
        return this.m_hotSpotPosition;
    }
    
    public void setHotSpotPosition(final Alignment9 hotSpotPosition) {
        this.m_hotSpotPosition = hotSpotPosition;
    }
    
    public Alignment9 getAlign() {
        return this.m_align;
    }
    
    public void setAlign(final Alignment9 align) {
        this.m_align = align;
    }
    
    public boolean getHideOnClick() {
        return this.m_hideOnClick;
    }
    
    public void setHideOnClick(final boolean hideOnClick) {
        this.m_hideOnClick = hideOnClick;
    }
    
    public void setXOffset(final int XOffset) {
        this.m_XOffset = XOffset;
    }
    
    public void setYOffset(final int YOffset) {
        this.m_YOffset = YOffset;
    }
    
    public Widget getContent() {
        return this.m_content;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final PopupElement t = (PopupElement)source;
        super.copyElement(source);
        t.setAlign(this.m_align);
        t.setHotSpotPosition(this.m_hotSpotPosition);
        t.setHideOnClick(this.m_hideOnClick);
    }
    
    public void toggle(final PopupClient w) {
        this.toggle(w, MasterRootContainer.getInstance().getPopupContainer());
    }
    
    public void toggle(final PopupClient w, final Popup p) {
        if (p != null) {
            if (!p.getVisible()) {
                this.show(w, p);
            }
            else {
                this.hide(p);
            }
        }
    }
    
    public void show(final PopupClient client) {
        final Popup p = MasterRootContainer.getInstance().getPopupContainer();
        this.show(client, p);
    }
    
    public void show(final PopupClient client, final Popup p) {
        if (p != null && client.getElementMap() != null) {
            p.setAlign(this.m_align);
            p.setHotSpotPosition(this.m_hotSpotPosition);
            p.setContent(this.m_content);
            p.setClient(client);
            p.setHideOnClick(this.m_hideOnClick);
            p.setXOffset(this.m_XOffset);
            p.setYOffset(this.m_YOffset);
            p.show();
            XulorSoundManager.getInstance().popup();
        }
    }
    
    public void hide() {
        this.hide(MasterRootContainer.getInstance().getPopupContainer());
    }
    
    public void hide(final Popup p) {
        if (p != null) {
            p.hide();
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        if (this.m_content != null) {
            this.m_content = null;
        }
        this.m_align = null;
        this.m_hotSpotPosition = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_hideOnClick = true;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == PopupElement.ALIGN_HASH) {
            this.setAlign(Alignment9.value(value));
        }
        else if (hash == PopupElement.HOT_SPOT_POSITION_HASH) {
            this.setHotSpotPosition(Alignment9.value(value));
        }
        else if (hash == PopupElement.HIDE_ON_CLICK_HASH) {
            this.setHideOnClick(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == PopupElement.X_OFFSET_HASH) {
            this.setXOffset(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != PopupElement.Y_OFFSET_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setYOffset(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == PopupElement.HIDE_ON_CLICK_HASH) {
            this.setHideOnClick(PrimitiveConverter.getBoolean(value));
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        ALIGN_HASH = "align".hashCode();
        HOT_SPOT_POSITION_HASH = "hotSpotPosition".hashCode();
        HIDE_ON_CLICK_HASH = "hideOnClick".hashCode();
        X_OFFSET_HASH = "xOffset".hashCode();
        Y_OFFSET_HASH = "yOffset".hashCode();
    }
}
