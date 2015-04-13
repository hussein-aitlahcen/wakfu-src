package com.ankamagames.xulor2.core.dragndrop;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;

public class MapDragNDropHandler implements DragNDropHandler
{
    private static final Logger m_logger;
    private MapOverlay m_mapWidget;
    private Widget m_over;
    private Image m_copyDND;
    private boolean m_dragged;
    private DisplayableMapPoint m_point;
    private EntitySprite m_mesh;
    private int m_startX;
    private int m_startY;
    
    public MapDragNDropHandler(final MapOverlay source) {
        super();
        this.m_mapWidget = source;
    }
    
    @Override
    public void drag(final int displayX, final int displayY, final Widget draggedOver) {
        if (!this.m_dragged && (displayX < this.m_startX - 20 || displayX > this.m_startX + 20 || displayY < this.m_startY - 20 || displayY > this.m_startY + 20)) {
            final PixmapElement pixmapElement = new PixmapElement();
            pixmapElement.onCheckOut();
            pixmapElement.setPixmap(new Pixmap(this.m_mesh.getTexture()));
            (this.m_copyDND = new Image()).onCheckOut();
            this.m_copyDND.add(pixmapElement);
            this.m_copyDND.setSize(this.m_mesh.getWidth(), this.m_mesh.getHeight());
            this.m_copyDND.setNonBlocking(true);
            this.m_copyDND.setLayoutData(null);
            this.m_mapWidget.fireDrag(this.m_point);
            MasterRootContainer.getInstance().getLayeredContainer().addWidgetToLayer(this.m_copyDND, 30000);
            this.m_dragged = true;
        }
        if (this.m_dragged && this.m_copyDND != null) {
            this.m_copyDND.setPosition(displayX - this.m_copyDND.getWidth() / 2, displayY - this.m_copyDND.getHeight() / 2);
            if (draggedOver != this.m_over) {
                if (this.m_over != null) {
                    this.m_mapWidget.fireDragOut(null, this.m_point);
                    this.m_over = null;
                }
                if (draggedOver != null) {
                    this.m_over = draggedOver;
                    this.m_mapWidget.fireDragOver(null, this.m_point);
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
            if (dnd != null && dnd.isDropValid(this.m_mapWidget, this.m_point)) {
                dnd.fireDrop(dnd, this.m_point);
            }
            else {
                final DropOutEvent event = DropOutEvent.checkOut(MasterRootContainer.getInstance().getCurrentAWTMouseEvent(), this.m_mapWidget, this.m_point);
                this.m_mapWidget.dispatchEvent(event);
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
        this.m_point = null;
        this.m_mesh = null;
        this.m_over = null;
    }
    
    @Override
    public boolean release() {
        final boolean dragging = this.m_dragged;
        this.clean();
        this.m_mapWidget = null;
        return dragging;
    }
    
    @Override
    public Object getValue() {
        return null;
    }
    
    @Override
    public boolean isDndWidget(final Widget w, final int displayX, final int displayY) {
        if (w == null) {
            return false;
        }
        if (w == this.m_mapWidget || w.hasInParentHierarchy(this.m_mapWidget)) {
            final DisplayableMapPoint point = this.m_mapWidget.getOverItem();
            return this.m_mapWidget.isDragEnabled() && point != null && point.isDndropable() && (!point.isEditable() || !point.isBeingEdited());
        }
        return false;
    }
    
    @Override
    public void select(final int displayX, final int displayY) {
        this.m_startX = displayX;
        this.m_startY = displayY;
        this.m_point = this.m_mapWidget.getOverItem();
        this.m_mesh = this.m_mapWidget.getOverMesh();
        this.m_dragged = false;
        this.m_over = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapDragNDropHandler.class);
    }
}
