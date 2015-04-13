package com.ankamagames.wakfu.client.ui.component.appearance;

import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.decorator.mesh.*;

public class TimePointBarDecorator extends AbstractDecorator
{
    public static final String TAG = "timePointBarDecorator";
    public static final String LINK_PIXMAP_NAME = "link";
    public static final String DOUBLE_BUBBLE_PIXMAP_NAME = "doubleBubble";
    public static final String BORDER_BUBBLE_PIXMAP_NAME = "borderBubble";
    public static final String OPPOSITE_BORDER_BUBBLE_PIXMAP_NAME = "oppositeBorderBubble";
    private TimePointBarDecoratorMesh m_mesh;
    
    @Override
    public void add(final EventDispatcher element) {
        super.add(element);
        if (element instanceof PixmapElement) {
            final PixmapElement pixmap = (PixmapElement)element;
            final String name = pixmap.getName();
            if ("link".equals(name)) {
                this.m_mesh.setLinkPixmap(pixmap.getPixmap());
            }
            else if ("doubleBubble".equals(name)) {
                this.m_mesh.setDoubleBubblePixmap(pixmap.getPixmap());
            }
            else if ("borderBubble".equals(name)) {
                this.m_mesh.setBorderBubblePixmap(pixmap.getPixmap());
            }
            else if ("oppositeBorderBubble".equals(name)) {
                this.m_mesh.setOppositeBorderBubblePixmap(pixmap.getPixmap());
            }
        }
    }
    
    public void setPixelSeparations(final int[] pixelSeparation, final int[] displayedTurns, final float[] alphas) {
        this.m_mesh.setPixelSeparations(pixelSeparation, displayedTurns, alphas);
    }
    
    public int[] getPixelSeparations() {
        return this.m_mesh.getPixelSeparations();
    }
    
    @Override
    public String getTag() {
        return "timePointBarDecorator";
    }
    
    @Override
    public TimePointBarDecoratorMesh getMesh() {
        return this.m_mesh;
    }
    
    @Override
    public Entity getEntity() {
        return this.m_mesh.getEntity();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        (this.m_mesh = new TimePointBarDecoratorMesh()).onCheckOut();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_mesh = null;
    }
}
