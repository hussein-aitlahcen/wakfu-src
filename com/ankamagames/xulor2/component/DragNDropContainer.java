package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class DragNDropContainer extends Container
{
    public static final String TAG = "dndc";
    private DropValidateCallBack m_dropValidateCallBack;
    private boolean m_dragEnabled;
    private boolean m_dropEnabled;
    private boolean m_isCopy;
    private boolean m_displayCopy;
    public static final int VALIDATE_DROP_HASH;
    public static final int DRAG_ENABLED_HASH;
    public static final int DROP_ENABLED_HASH;
    public static final int DISPLAY_COPY_HASH;
    
    public DragNDropContainer() {
        super();
        this.m_dropValidateCallBack = null;
        this.m_dragEnabled = true;
        this.m_dropEnabled = true;
        this.m_isCopy = false;
        this.m_displayCopy = true;
    }
    
    public void setDragEnabled(final boolean dragEnabled) {
        this.m_dragEnabled = dragEnabled;
    }
    
    public void setDropEnabled(final boolean dropEnabled) {
        this.m_dropEnabled = dropEnabled;
    }
    
    public boolean isDragEnabled() {
        return this.m_dragEnabled;
    }
    
    public boolean isDropEnabled() {
        return this.m_dropEnabled;
    }
    
    public boolean isDisplayCopy() {
        return this.m_displayCopy;
    }
    
    public void setDisplayCopy(final boolean displayCopy) {
        this.m_displayCopy = displayCopy;
    }
    
    public void setValidateDrop(final DropValidateCallBack c) {
        this.m_dropValidateCallBack = c;
    }
    
    @Override
    public String getTag() {
        return "dndc";
    }
    
    @Override
    public Widget getWidget(final int x, final int y) {
        if (this.isCopy()) {
            return null;
        }
        return super.getWidget(x, y);
    }
    
    public boolean isCopy() {
        return this.m_isCopy;
    }
    
    public void setCopy(final boolean copy) {
        this.m_isCopy = copy;
    }
    
    public void fireDrag(final Object value) {
        if (!this.m_dragEnabled) {
            return;
        }
        final DragEvent event = DragEvent.checkOut(MasterRootContainer.getInstance().getCurrentMouseEvent(), this, value);
        this.dispatchEvent(event);
    }
    
    public void fireDrop(final DragNDropContainer source, final Object value) {
        if (!this.m_dropEnabled) {
            return;
        }
        final DropEvent event = DropEvent.checkOut(MasterRootContainer.getInstance().getCurrentMouseEvent(), this, source, value);
        this.dispatchEvent(event);
    }
    
    public void fireDropOut(final Object value) {
        if (!this.m_dropEnabled) {
            return;
        }
        final DropOutEvent event = DropOutEvent.checkOut(MasterRootContainer.getInstance().getCurrentMouseEvent(), this, value);
        this.dispatchEvent(event);
    }
    
    public void fireDragOut(final DragNDropContainer out, final Object value) {
        if (!this.m_dragEnabled) {
            return;
        }
        final DragOutEvent event = DragOutEvent.checkOut(MasterRootContainer.getInstance().getCurrentMouseEvent(), this, out, value);
        this.dispatchEvent(event);
    }
    
    public void fireDragOver(final DragNDropContainer over, final Object value) {
        if (!this.m_dragEnabled) {
            return;
        }
        XulorSoundManager.getInstance().rollOver();
        final DragOverEvent event = DragOverEvent.checkOut(MasterRootContainer.getInstance().getCurrentMouseEvent(), this, over, value);
        this.dispatchEvent(event);
    }
    
    public boolean isDropValid(final DragNDropContainer source, final Object value) {
        if (!this.m_dropEnabled) {
            return false;
        }
        if (this.m_dropValidateCallBack != null) {
            final Object result = this.m_dropValidateCallBack.invokeCallBack(source, this, value);
            if (result != null) {
                return (boolean)result;
            }
        }
        return true;
    }
    
    @Override
    public void removedFromWidgetTree() {
        super.removedFromWidgetTree();
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        final DragNDropContainer dndc = (DragNDropContainer)c;
        super.copyElement(c);
        dndc.m_dropValidateCallBack = this.m_dropValidateCallBack;
        dndc.m_dragEnabled = this.m_dragEnabled;
        dndc.m_dropEnabled = this.m_dropEnabled;
        dndc.m_displayCopy = this.m_displayCopy;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_dropValidateCallBack = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_isCopy = false;
        this.addEventListener(Events.MOUSE_DRAGGED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                return DragNDropContainer.this.m_dragEnabled;
            }
        }, false);
        this.addEventListener(Events.MOUSE_DRAGGED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                return DragNDropContainer.this.m_dragEnabled;
            }
        }, true);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == DragNDropContainer.VALIDATE_DROP_HASH) {
            this.setValidateDrop(cl.convert(DropValidateCallBack.class, value));
        }
        else if (hash == DragNDropContainer.DRAG_ENABLED_HASH) {
            this.setDragEnabled(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == DragNDropContainer.DROP_ENABLED_HASH) {
            this.setDropEnabled(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != DragNDropContainer.DISPLAY_COPY_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setDisplayCopy(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == DragNDropContainer.DRAG_ENABLED_HASH) {
            this.setDragEnabled(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == DragNDropContainer.DROP_ENABLED_HASH) {
            this.setDropEnabled(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != DragNDropContainer.DISPLAY_COPY_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setDisplayCopy(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        VALIDATE_DROP_HASH = "validateDrop".hashCode();
        DRAG_ENABLED_HASH = "dragEnabled".hashCode();
        DROP_ENABLED_HASH = "dropEnabled".hashCode();
        DISPLAY_COPY_HASH = "displayCopy".hashCode();
    }
}
