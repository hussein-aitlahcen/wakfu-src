package com.ankamagames.xulor2.core.dragndrop;

import org.apache.log4j.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;

public class ItemDragNDropHandler implements DragNDropHandler
{
    private static Logger m_logger;
    private DragNDropContainer m_sourceDND;
    private Widget m_over;
    private RenderableContainer m_sourceRenderable;
    private DragNDropContainer m_copyDND;
    private boolean m_dragged;
    private Object m_value;
    
    public ItemDragNDropHandler(final RenderableContainer source) {
        super();
        this.m_sourceDND = null;
        this.m_over = null;
        this.m_value = null;
        this.m_sourceRenderable = source;
    }
    
    @Override
    public void drag(final int displayX, final int displayY, Widget draggedOver) {
        if (!this.m_dragged && this.m_sourceDND.isDragEnabled()) {
            final Widget widget = this.m_sourceRenderable;
            if (displayX < widget.getDisplayX() || displayX > widget.getDisplayX() + widget.getWidth() || displayY < widget.getDisplayY() || displayY > widget.getDisplayY() + widget.getHeight()) {
                if (this.m_sourceDND == null || this.m_sourceDND.isUnloading()) {
                    return;
                }
                this.m_copyDND = (DragNDropContainer)this.m_sourceDND.cloneElementStructure();
                if (this.m_copyDND == null) {
                    ItemDragNDropHandler.m_logger.warn((Object)"probl\u00e8me au clone du dnd container, le clone est null");
                    return;
                }
                this.m_copyDND.setCopy(true);
                this.m_copyDND.setSize(this.m_sourceDND.getSize());
                this.m_copyDND.setNonBlocking(true);
                this.m_copyDND.setLayoutData(null);
                this.m_sourceDND.fireDrag(this.m_value);
                if (this.m_copyDND.isDisplayCopy()) {
                    MasterRootContainer.getInstance().getLayeredContainer().addWidgetToLayer(this.m_copyDND, 30000);
                }
                this.m_dragged = true;
            }
        }
        if (this.m_dragged && this.m_copyDND != null) {
            this.m_copyDND.setPosition(displayX - this.m_copyDND.getWidth() / 2, displayY - this.m_copyDND.getHeight() / 2);
            if (draggedOver != null && !(draggedOver instanceof RenderableContainer)) {
                draggedOver = draggedOver.getParentOfType(RenderableContainer.class);
            }
            if (draggedOver != this.m_over) {
                if (this.m_over != null) {
                    this.m_sourceDND.fireDragOut(((RenderableContainer)this.m_over).getDragNDropable(), this.m_value);
                    this.m_over = null;
                }
                if (draggedOver != null) {
                    this.m_over = draggedOver;
                    this.m_sourceDND.fireDragOver(((RenderableContainer)this.m_over).getDragNDropable(), this.m_value);
                }
            }
        }
    }
    
    @Override
    public void drop(final int displayX, final int displayY, final Widget droppedOn) {
        if (this.m_dragged) {
            DragNDropContainer dnd = null;
            if (droppedOn != null) {
                if (droppedOn instanceof DragNDropContainer) {
                    dnd = (DragNDropContainer)droppedOn;
                }
                else {
                    dnd = droppedOn.getParentOfType((Class<MapOverlay<?>>)MapOverlay.class);
                }
                if (dnd == null) {
                    if (!(droppedOn instanceof RenderableContainer)) {
                        final Object e = droppedOn.getParentOfType(RenderableContainer.class);
                        if (e instanceof RenderableContainer) {
                            dnd = ((RenderableContainer)e).getDragNDropable();
                        }
                    }
                    else {
                        dnd = ((RenderableContainer)droppedOn).getDragNDropable();
                    }
                }
            }
            if (dnd != null && dnd.isDropValid(this.m_sourceDND, this.m_value)) {
                dnd.fireDrop(this.m_sourceDND, this.m_value);
            }
            else if (dnd != this.m_sourceDND) {
                final DropOutEvent event = DropOutEvent.checkOut(MasterRootContainer.getInstance().getCurrentAWTMouseEvent(), this.m_sourceDND, this.m_value);
                this.m_sourceRenderable.dispatchEvent(event);
            }
        }
        this.clean();
    }
    
    @Override
    public void clean() {
        if (this.m_copyDND != null) {
            this.m_copyDND.destroySelfFromParent();
            this.m_copyDND = null;
        }
        this.m_sourceDND = null;
        this.m_over = null;
        this.m_value = null;
    }
    
    @Override
    public boolean release() {
        final boolean dragging = this.m_dragged;
        this.clean();
        this.m_sourceRenderable = null;
        return dragging;
    }
    
    @Override
    public boolean isDndWidget(final Widget w, final int displayX, final int displayY) {
        return w != null && (this.m_sourceRenderable.canBeDnD() && (w == this.m_sourceRenderable || w.hasInParentHierarchy(this.m_sourceRenderable)) && this.m_sourceRenderable.getItem() != null);
    }
    
    @Override
    public void select(final int displayX, final int displayY) {
        this.m_value = this.m_sourceRenderable.getItemValue();
        this.m_sourceDND = this.m_sourceRenderable.getDragNDropable();
        this.m_dragged = false;
        this.m_over = null;
    }
    
    @Override
    public Object getValue() {
        return this.m_value;
    }
    
    static {
        ItemDragNDropHandler.m_logger = Logger.getLogger((Class)ItemDragNDropHandler.class);
    }
}
