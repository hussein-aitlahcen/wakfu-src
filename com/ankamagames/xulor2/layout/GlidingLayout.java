package com.ankamagames.xulor2.layout;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class GlidingLayout extends AbstractLayoutManager
{
    private static Logger m_logger;
    public static final String TAG = "GlidingLayout";
    public static final String SHORT_TAG = "gl";
    private boolean m_horizontal;
    private short m_hgap;
    private short m_vgap;
    public static final int HGAP_HASH;
    public static final int VGAP_HASH;
    public static final int HORIZONTAL_HASH;
    
    public GlidingLayout() {
        super();
        this.m_horizontal = true;
        this.m_hgap = 0;
        this.m_vgap = 0;
    }
    
    @Override
    public String getTag() {
        return "GlidingLayout";
    }
    
    @Override
    public Dimension getContentMinSize(final Container container) {
        int width = 0;
        int height = 0;
        boolean firstVisible = true;
        if (this.m_horizontal) {
            for (final Widget w : container.getWidgetChildren()) {
                if (w.getVisible()) {
                    if (firstVisible) {
                        firstVisible = false;
                    }
                    else {
                        width += this.m_hgap;
                    }
                    width += w.getMinSize().width;
                    height = Math.max(height, w.getMinSize().height);
                }
            }
            width += 2 * this.m_hgap;
            height += 2 * this.m_vgap;
        }
        else {
            for (final Widget w : container.getWidgetChildren()) {
                if (w.getVisible()) {
                    if (firstVisible) {
                        firstVisible = false;
                    }
                    else {
                        height += this.m_vgap;
                    }
                    height += w.getMinSize().height;
                    width = Math.max(width, w.getMinSize().width);
                }
            }
            width += 2 * this.m_hgap;
            height += 2 * this.m_vgap;
        }
        return new Dimension(width, height);
    }
    
    @Override
    public Dimension getContentPreferedSize(final Container container) {
        int width = 0;
        int height = 0;
        boolean firstVisible = true;
        if (this.m_horizontal) {
            for (int i = container.getWidgetChildren().size() - 1; i >= 0; --i) {
                final Widget w = container.getWidget(i);
                if (w.getVisible()) {
                    if (firstVisible) {
                        firstVisible = false;
                    }
                    else {
                        width += this.m_hgap;
                    }
                    width += w.getPrefSize().width;
                    height = Math.max(height, w.getPrefSize().height);
                }
            }
            width += 2 * this.m_hgap;
            height += 2 * this.m_vgap;
        }
        else {
            for (int i = container.getWidgetChildren().size() - 1; i >= 0; --i) {
                final Widget w = container.getWidget(i);
                if (w.getVisible()) {
                    if (firstVisible) {
                        firstVisible = false;
                    }
                    else {
                        height += this.m_vgap;
                    }
                    height += w.getPrefSize().height;
                    width = Math.max(width, w.getPrefSize().width);
                }
            }
            width += 2 * this.m_hgap;
            height += 2 * this.m_vgap;
        }
        return new Dimension(width, height);
    }
    
    public short getHgap() {
        return this.m_hgap;
    }
    
    public void setHgap(final short hgap) {
        this.m_hgap = hgap;
    }
    
    public boolean isHorizontal() {
        return this.m_horizontal;
    }
    
    public void setHorizontal(final boolean horizontal) {
        this.m_horizontal = horizontal;
    }
    
    public short getVgap() {
        return this.m_vgap;
    }
    
    public void setVgap(final short vgap) {
        this.m_vgap = vgap;
    }
    
    @Override
    public void layoutContainer(final Container parent) {
        final int x = 0;
        final int y = 0;
        final ArrayList<Widget> widgets = parent.getWidgetChildren();
        if (this.m_horizontal) {
            final int availableHeight = parent.getAppearance().getContentHeight();
            final int availableWidth = parent.getAppearance().getContentWidth();
            for (int i = 0; i < widgets.size(); ++i) {
                final Widget widget = widgets.get(i);
                GlidingLayoutData glidingLayoutData = null;
                if (widget.getLayoutData() instanceof GlidingLayoutData) {
                    glidingLayoutData = (GlidingLayoutData)widget.getLayoutData();
                }
                if (widget.getVisible()) {
                    final int width = widget.getPrefSize().width;
                    int widgetY = y;
                    int widgetX = widget.getX();
                    int height;
                    if (glidingLayoutData != null) {
                        height = widget.getPrefSize().height;
                    }
                    else {
                        height = availableHeight;
                    }
                    if (glidingLayoutData != null) {
                        final Alignment9 initAlignment9 = glidingLayoutData.getInitAlign();
                        if ((!glidingLayoutData.isInitValue() || !widget.isPositionInitByUserDefinition() || MasterRootContainer.getInstance().isResized()) && glidingLayoutData.isUsable() && initAlignment9 != null) {
                            widgetX = initAlignment9.getX(width, availableWidth);
                            glidingLayoutData.setUsable(false);
                        }
                        widgetY += glidingLayoutData.getAlign().getY(height, availableHeight);
                    }
                    widget.setSize(width, height);
                    widget.setPosition(widgetX, widgetY);
                }
            }
        }
        else {
            final int availableWidth2 = parent.getAppearance().getContentWidth();
            final int availableHeight2 = parent.getAppearance().getContentHeight();
            for (int i = 0; i < widgets.size(); ++i) {
                final Widget widget = widgets.get(i);
                GlidingLayoutData glidingLayoutData = null;
                if (widget.getLayoutData() instanceof GlidingLayoutData) {
                    glidingLayoutData = (GlidingLayoutData)widget.getLayoutData();
                }
                if (widget.getVisible()) {
                    final int height2 = widget.getPrefSize().height;
                    int widgetX2 = x;
                    int widgetY2 = widget.getY();
                    int width2;
                    if (glidingLayoutData != null) {
                        width2 = widget.getPrefSize().width;
                    }
                    else {
                        width2 = availableWidth2;
                    }
                    if (glidingLayoutData != null) {
                        final Alignment9 initAlignment9 = glidingLayoutData.getInitAlign();
                        if ((!glidingLayoutData.isInitValue() || !widget.isPositionInitByUserDefinition() || MasterRootContainer.getInstance().isResized()) && glidingLayoutData.isUsable() && initAlignment9 != null) {
                            widgetY2 = initAlignment9.getY(height2, availableHeight2);
                            glidingLayoutData.setUsable(false);
                        }
                        widgetX2 += glidingLayoutData.getAlign().getX(width2, availableWidth2);
                    }
                    widget.setSize(width2, height2);
                    widget.setPosition(widgetX2, widget.getY());
                }
            }
        }
    }
    
    @Override
    public void copyElement(final BasicElement r) {
        final GlidingLayout e = (GlidingLayout)r;
        super.copyElement(e);
        e.m_hgap = this.m_hgap;
        e.m_vgap = this.m_vgap;
        e.m_horizontal = this.m_horizontal;
    }
    
    @Override
    public GlidingLayout clone() {
        final GlidingLayout l = new GlidingLayout();
        l.onCheckOut();
        this.copyElement(l);
        return l;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == GlidingLayout.HGAP_HASH) {
            this.setHgap(PrimitiveConverter.getShort(value));
        }
        else if (hash == GlidingLayout.VGAP_HASH) {
            this.setVgap(PrimitiveConverter.getShort(value));
        }
        else {
            if (hash != GlidingLayout.HORIZONTAL_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        GlidingLayout.m_logger = Logger.getLogger((Class)GlidingLayout.class);
        HGAP_HASH = "hgap".hashCode();
        VGAP_HASH = "vgap".hashCode();
        HORIZONTAL_HASH = "horizontal".hashCode();
    }
}
