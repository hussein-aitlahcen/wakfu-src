package com.ankamagames.xulor2.layout;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;

public class SpringLayoutData extends AbstractLayoutData
{
    public static final String TAG = "SpringLayoutData";
    public static final String SHORT_TAG = "spld";
    private boolean m_resized;
    private Spring m_x;
    private Spring m_y;
    private Spring m_east;
    private Spring m_north;
    private Spring m_width;
    private Spring m_height;
    private Spring m_horizontalConstraint;
    private Spring m_verticalConstraint;
    private SpringLayout m_layout;
    
    public SpringLayoutData() {
        super();
        this.m_resized = false;
    }
    
    public SpringLayoutData(final Spring x, final Spring y, final Spring east, final Spring north) {
        super();
        this.m_resized = false;
        this.m_x = x;
        this.m_y = y;
        this.m_east = east;
        this.m_north = north;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof Spring && this.isInTree()) {
            final Spring spring = (Spring)e;
            this.putConstraint(spring.getEdge(), spring.getValue(), spring.getReferentId(), spring.getReferentEdge());
            this.m_resized = true;
            this.setNeedsToPreProcess();
        }
        super.add(e);
    }
    
    private void checkHorizontalOverConstraints() {
        if (this.m_x != null && this.m_width != null && this.m_horizontalConstraint != null) {
            this.m_width = null;
        }
    }
    
    private void checkVerticalOverConstraints() {
        if (this.m_y != null && this.m_height != null && this.m_verticalConstraint != null) {
            this.m_height = null;
        }
    }
    
    @Override
    public String getTag() {
        return "SpringLayoutData";
    }
    
    public Spring getEast() {
        if (this.m_east != null) {
            return this.m_east;
        }
        if (this.m_horizontalConstraint == null) {
            if (this.m_x == null || this.m_width == null) {
                return Spring.constant(0);
            }
            this.m_horizontalConstraint = Spring.sum(this.m_x, this.m_width);
        }
        return this.m_horizontalConstraint;
    }
    
    public void setEast(final Spring east) {
        this.m_east = east;
    }
    
    public Spring getNorth() {
        if (this.m_north != null) {
            return this.m_north;
        }
        if (this.m_verticalConstraint == null) {
            if (this.m_y == null || this.m_height == null) {
                return Spring.constant(0);
            }
            this.m_verticalConstraint = Spring.sum(this.m_y, this.m_height);
        }
        return this.m_verticalConstraint;
    }
    
    public void setNorth(final Spring north) {
        this.m_north = north;
    }
    
    public Spring getX() {
        if (this.m_x != null) {
            return this.m_x;
        }
        if (this.m_horizontalConstraint == null) {
            if (this.m_east == null || this.m_width == null) {
                return Spring.constant(0);
            }
            this.m_horizontalConstraint = Spring.difference(this.m_east, this.m_width);
        }
        return this.m_horizontalConstraint;
    }
    
    public void setX(final Spring x) {
        this.m_x = x;
    }
    
    public Spring getY() {
        if (this.m_y != null) {
            return this.m_y;
        }
        if (this.m_verticalConstraint == null) {
            if (this.m_north == null || this.m_height == null) {
                return Spring.constant(0);
            }
            this.m_verticalConstraint = Spring.difference(this.m_north, this.m_height);
        }
        return this.m_verticalConstraint;
    }
    
    public void setY(final Spring y) {
        this.m_y = y;
    }
    
    public Spring getWidth() {
        if (this.m_width != null) {
            return this.m_width;
        }
        if (this.m_horizontalConstraint == null) {
            if (this.m_east == null || this.m_x == null) {
                return Spring.constant(0);
            }
            this.m_horizontalConstraint = Spring.difference(this.m_east, this.m_x);
        }
        return this.m_horizontalConstraint;
    }
    
    public void setWidth(final Spring width) {
        this.m_width = width;
        this.checkHorizontalOverConstraints();
    }
    
    public Spring getHeight() {
        if (this.m_height != null) {
            return this.m_height;
        }
        if (this.m_verticalConstraint == null) {
            if (this.m_north == null || this.m_y == null) {
                return Spring.constant(0);
            }
            this.m_verticalConstraint = Spring.difference(this.m_north, this.m_y);
        }
        return this.m_verticalConstraint;
    }
    
    public void setHeight(final Spring height) {
        this.m_height = height;
        this.checkVerticalOverConstraints();
    }
    
    public SpringLayout getLayout() {
        return this.m_layout;
    }
    
    public void setLayout(final SpringLayout layout) {
        this.m_layout = layout;
    }
    
    public Spring getConstraint(final String edge) {
        if (edge.equalsIgnoreCase("North")) {
            return this.getNorth();
        }
        if (edge.equalsIgnoreCase("East")) {
            return this.getEast();
        }
        if (edge.equalsIgnoreCase("West")) {
            return this.getX();
        }
        if (edge.equalsIgnoreCase("South")) {
            return this.getY();
        }
        return null;
    }
    
    public void putConstraint(final String edge, final int value, final String destination, final String destinationEdge) {
        final Spring spring = Spring.sum(Spring.constant(value), new SpringProxy(this.m_layout, destination, destinationEdge));
        this.putConstraint(edge, spring);
    }
    
    public void putConstraint(final String edge, final int value, final Widget destination, final String destinationEdge) {
        final Spring spring = Spring.sum(Spring.constant(value), new SpringProxy(this.m_layout, destination, destinationEdge));
        this.putConstraint(edge, spring);
    }
    
    public void putConstraint(final String edge, final Spring spring) {
        if (edge.equalsIgnoreCase("North")) {
            this.setNorth(spring);
        }
        else if (edge.equalsIgnoreCase("East")) {
            this.setEast(spring);
        }
        else if (edge.equalsIgnoreCase("West")) {
            this.setX(spring);
        }
        else if (edge.equalsIgnoreCase("South")) {
            this.setY(spring);
        }
    }
    
    public static SpringLayoutData defaultConstraints(final SpringLayout layout, final Widget widget) {
        final SpringLayoutData springLayoutData = new SpringLayoutData();
        springLayoutData.setLayout(layout);
        springLayoutData.setX(Spring.x(widget));
        springLayoutData.setY(Spring.y(widget));
        springLayoutData.setWidth(Spring.width(widget));
        springLayoutData.setHeight(Spring.height(widget));
        return springLayoutData;
    }
    
    public void cleanUpWidgetOccurences() {
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final SpringLayoutData spld = (SpringLayoutData)source;
        super.copyElement(spld);
    }
    
    @Override
    public void addedToTree() {
        super.addedToTree();
        LayoutManager layout = null;
        final Widget w = this.getParentOfType(Widget.class);
        if (w != null) {
            final Container c = w.getContainer();
            if (c != null) {
                layout = c.getLayoutManager();
            }
        }
        if (layout instanceof SpringLayout) {
            this.setLayout((SpringLayout)layout);
            if (this.m_parent instanceof Widget) {
                ((SpringLayout)layout).putConstraint((Widget)this.m_parent, this);
            }
        }
        for (int size = this.getChildren().size(), i = 0; i < size; ++i) {
            final EventDispatcher child = this.getChildren().get(i);
            if (child instanceof Spring) {
                final Spring spring = (Spring)child;
                this.putConstraint(spring.getEdge(), spring.getValue(), spring.getReferentId(), spring.getReferentEdge());
                this.m_resized = true;
                this.setNeedsToPreProcess();
            }
        }
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_resized) {
            final Container c = this.getParentOfType(Container.class);
            c.invalidateMinSize();
            this.m_resized = false;
        }
        return ret;
    }
    
    @Override
    public String toString() {
        return "[x=" + this.getX() + ", y=" + this.getY() + ", width=" + this.getWidth() + ", height=" + this.getHeight() + "]";
    }
}
