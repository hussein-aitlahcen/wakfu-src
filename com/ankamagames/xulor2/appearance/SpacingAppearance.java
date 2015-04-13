package com.ankamagames.xulor2.appearance;

import java.awt.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;

public class SpacingAppearance extends NonGraphicalElement
{
    protected Insets m_margin;
    protected Insets m_border;
    protected Insets m_padding;
    protected WidgetShape m_shape;
    protected Dimension m_contentSize;
    protected boolean m_dirty;
    protected boolean m_widgetChanged;
    protected Widget m_widget;
    public static final int MARGIN_HASH;
    public static final int BORDER_HASH;
    public static final int PADDING_HASH;
    public static final int SHAPE_HASH;
    
    public SpacingAppearance() {
        super();
        this.m_contentSize = null;
        this.m_dirty = false;
        this.m_widgetChanged = false;
    }
    
    public SpacingAppearance(final Widget widget) {
        super();
        this.m_contentSize = null;
        this.m_dirty = false;
        this.m_widgetChanged = false;
        this.m_widget = widget;
    }
    
    @Override
    public void add(final EventDispatcher e) {
        if (e instanceof InsetsElement) {
            e.addEventListener(Events.SPACING_CHANGED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    SpacingAppearance.this.setSpacing(((SpacingChangedEvent)event).getSpacing());
                    return false;
                }
            }, false);
            this.setSpacing((InsetsElement)e);
        }
        super.add(e);
    }
    
    public void setSpacing(final InsetsElement spacing) {
        if (spacing instanceof Margin) {
            this.setMargin(spacing.getInsets());
        }
        else if (spacing instanceof Padding) {
            this.setPadding(spacing.getInsets());
        }
        else if (spacing instanceof Border) {
            this.setBorder(spacing.getInsets());
        }
    }
    
    public Insets getBorder() {
        return this.m_border;
    }
    
    public void setBorder(final Insets border) {
        this.m_border.bottom = border.bottom;
        this.m_border.top = border.top;
        this.m_border.left = border.left;
        this.m_border.right = border.right;
        this.m_dirty = true;
        this.setNeedsToPreProcess();
    }
    
    public Insets getMargin() {
        return this.m_margin;
    }
    
    public void setMargin(final Insets margin) {
        this.m_margin.bottom = margin.bottom;
        this.m_margin.top = margin.top;
        this.m_margin.left = margin.left;
        this.m_margin.right = margin.right;
        this.m_dirty = true;
        this.setNeedsToPreProcess();
    }
    
    public Insets getPadding() {
        return this.m_padding;
    }
    
    public void setPadding(final Insets padding) {
        this.m_padding.bottom = padding.bottom;
        this.m_padding.top = padding.top;
        this.m_padding.left = padding.left;
        this.m_padding.right = padding.right;
        this.m_dirty = true;
        this.setNeedsToPreProcess();
    }
    
    public Widget getWidget() {
        return this.m_widget;
    }
    
    public void setWidget(final Widget widget) {
        this.m_widget = widget;
        this.m_widgetChanged = true;
        this.setNeedsToPreProcess();
        this.setNeedsToPostProcess();
        this.m_dirty = true;
    }
    
    public void setShape(final WidgetShape shape) {
        this.m_shape = shape;
    }
    
    public WidgetShape getShape() {
        return this.m_shape;
    }
    
    public Insets getTotalInsets() {
        return new Insets(this.m_padding.top + this.m_border.top + this.m_margin.top, this.m_padding.left + this.m_border.left + this.m_margin.left, this.m_padding.bottom + this.m_border.bottom + this.m_margin.bottom, this.m_padding.right + this.m_border.right + this.m_margin.right);
    }
    
    public int getTopInset() {
        return this.m_padding.top + this.m_border.top + this.m_margin.top;
    }
    
    public int getBottomInset() {
        return this.m_padding.bottom + this.m_border.bottom + this.m_margin.bottom;
    }
    
    public int getLeftInset() {
        return this.m_padding.left + this.m_border.left + this.m_margin.left;
    }
    
    public int getRightInset() {
        return this.m_padding.right + this.m_border.right + this.m_margin.right;
    }
    
    private void computeContentSize() {
        this.m_contentSize = new Dimension(this.m_widget.m_size.width - this.m_margin.left - this.m_margin.right - this.m_padding.left - this.m_padding.right - this.m_border.left - this.m_border.right, this.m_widget.m_size.height - this.m_margin.top - this.m_margin.bottom - this.m_padding.top - this.m_padding.bottom - this.m_border.top - this.m_border.bottom);
    }
    
    public Dimension getContentSize() {
        if (this.m_contentSize == null) {
            this.computeContentSize();
        }
        return this.m_contentSize;
    }
    
    public int getContentWidth() {
        if (this.m_contentSize == null) {
            this.computeContentSize();
        }
        return this.m_contentSize.width;
    }
    
    public int getContentHeight() {
        if (this.m_contentSize == null) {
            this.computeContentSize();
        }
        return this.m_contentSize.height;
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        this.m_contentSize = null;
    }
    
    public boolean insideInsets(final int x, final int y) {
        return this.m_shape.insideInsets(x - this.m_margin.left, y - this.m_margin.bottom, this.m_widget.m_size.width - this.m_margin.left - this.m_margin.right, this.m_widget.m_size.height - this.m_margin.bottom - this.m_margin.top);
    }
    
    public int getOnScreenX(final int x, final int y) {
        return this.m_shape.getOnScreenX(x, y, this.m_widget.m_size.width - this.m_margin.left - this.m_margin.right, this.m_widget.m_size.height - this.m_margin.bottom - this.m_margin.top);
    }
    
    public int getOnScreenY(final int x, final int y) {
        return this.m_shape.getOnScreenY(x, y, this.m_widget.m_size.width - this.m_margin.left - this.m_margin.right, this.m_widget.m_size.height - this.m_margin.bottom - this.m_margin.top);
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_dirty) {
            this.m_dirty = false;
            this.invalidate();
            if (this.m_widget != null && this.m_widget instanceof Container) {
                final ArrayList<Widget> list = ((Container)this.m_widget).getWidgetChildren();
                for (int i = list.size() - 1; i >= 0; --i) {
                    list.get(i).setNeedsToPostProcess();
                }
            }
        }
        return ret;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        this.m_widgetChanged = false;
        return super.postProcess(deltaTime);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_margin = null;
        this.m_border = null;
        this.m_padding = null;
        this.m_shape = null;
        this.m_widget = null;
        this.m_contentSize = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_margin = new Insets(0, 0, 0, 0);
        this.m_border = new Insets(0, 0, 0, 0);
        this.m_padding = new Insets(0, 0, 0, 0);
        this.m_shape = WidgetShape.RECTANGLE;
        this.m_dirty = false;
        this.m_widgetChanged = false;
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        ((SpacingAppearance)source).setShape(this.m_shape);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == SpacingAppearance.SHAPE_HASH) {
            this.setShape(WidgetShape.value(value));
        }
        else if (hash == SpacingAppearance.BORDER_HASH) {
            this.setBorder(cl.convertToInsets(value));
        }
        else if (hash == SpacingAppearance.MARGIN_HASH) {
            this.setMargin(cl.convertToInsets(value));
        }
        else {
            if (hash != SpacingAppearance.PADDING_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setPadding(cl.convertToInsets(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == SpacingAppearance.SHAPE_HASH) {
            this.setShape((WidgetShape)value);
        }
        else if (hash == SpacingAppearance.BORDER_HASH) {
            this.setBorder((Insets)value);
        }
        else if (hash == SpacingAppearance.MARGIN_HASH) {
            this.setMargin((Insets)value);
        }
        else {
            if (hash != SpacingAppearance.PADDING_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setPadding((Insets)value);
        }
        return true;
    }
    
    static {
        MARGIN_HASH = "margin".hashCode();
        BORDER_HASH = "border".hashCode();
        PADDING_HASH = "padding".hashCode();
        SHAPE_HASH = "shape".hashCode();
    }
}
