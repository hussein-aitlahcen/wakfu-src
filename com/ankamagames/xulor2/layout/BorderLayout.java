package com.ankamagames.xulor2.layout;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class BorderLayout extends AbstractLayoutManager
{
    private static Logger m_logger;
    public static final String TAG = "BorderLayout";
    public static final String SHORT_TAG = "bl";
    private int m_hgap;
    private int m_vgap;
    public static final int HGAP_HASH;
    public static final int VGAP_HASH;
    
    public BorderLayout() {
        super();
        this.m_hgap = 0;
        this.m_vgap = 0;
    }
    
    @Override
    public String getTag() {
        return "BorderLayout";
    }
    
    public void setHGap(final int hgap) {
        this.m_hgap = hgap;
    }
    
    public void setVGap(final int vgap) {
        this.m_vgap = vgap;
    }
    
    private Widget getWidgetByConstraint(final Container parent, final BorderLayoutData.Values data) {
        for (final Widget w : parent.getWidgetChildren()) {
            if (w.getVisible() && w.getLayoutData() instanceof BorderLayoutData) {
                final BorderLayoutData bld = (BorderLayoutData)w.getLayoutData();
                if (bld.getData().equals(data)) {
                    return w;
                }
                continue;
            }
        }
        return null;
    }
    
    @Override
    public Dimension getContentMinSize(final Container container) {
        final Dimension m_minSize = new Dimension(0, 0);
        Widget w = this.getWidgetByConstraint(container, BorderLayoutData.Values.EAST);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getMinSize();
            final Dimension dimension = m_minSize;
            dimension.width += d.width + this.m_hgap;
            m_minSize.height = Math.max(d.height, m_minSize.height);
        }
        w = this.getWidgetByConstraint(container, BorderLayoutData.Values.WEST);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getMinSize();
            final Dimension dimension2 = m_minSize;
            dimension2.width += d.width + this.m_hgap;
            m_minSize.height = Math.max(d.height, m_minSize.height);
        }
        w = this.getWidgetByConstraint(container, BorderLayoutData.Values.CENTER);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getMinSize();
            final Dimension dimension3 = m_minSize;
            dimension3.width += d.width;
            m_minSize.height = Math.max(d.height, m_minSize.height);
        }
        w = this.getWidgetByConstraint(container, BorderLayoutData.Values.NORTH);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getMinSize();
            m_minSize.width = Math.max(d.width, m_minSize.width);
            final Dimension dimension4 = m_minSize;
            dimension4.height += d.height + this.m_vgap;
        }
        w = this.getWidgetByConstraint(container, BorderLayoutData.Values.SOUTH);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getMinSize();
            m_minSize.width = Math.max(d.width, m_minSize.width);
            final Dimension dimension5 = m_minSize;
            dimension5.height += d.height + this.m_vgap;
        }
        return m_minSize;
    }
    
    @Override
    public Dimension getContentPreferedSize(final Container container) {
        final Dimension m_prefSize = new Dimension(0, 0);
        Widget w = this.getWidgetByConstraint(container, BorderLayoutData.Values.EAST);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getPrefSize();
            final Dimension dimension = m_prefSize;
            dimension.width += d.width + this.m_hgap;
            m_prefSize.height = Math.max(d.height, m_prefSize.height);
        }
        w = this.getWidgetByConstraint(container, BorderLayoutData.Values.WEST);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getPrefSize();
            final Dimension dimension2 = m_prefSize;
            dimension2.width += d.width + this.m_hgap;
            m_prefSize.height = Math.max(d.height, m_prefSize.height);
        }
        w = this.getWidgetByConstraint(container, BorderLayoutData.Values.CENTER);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getPrefSize();
            final Dimension dimension3 = m_prefSize;
            dimension3.width += d.width;
            m_prefSize.height = Math.max(d.height, m_prefSize.height);
        }
        w = this.getWidgetByConstraint(container, BorderLayoutData.Values.NORTH);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getPrefSize();
            m_prefSize.width = Math.max(d.width, m_prefSize.width);
            final Dimension dimension4 = m_prefSize;
            dimension4.height += d.height + this.m_vgap;
        }
        w = this.getWidgetByConstraint(container, BorderLayoutData.Values.SOUTH);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getPrefSize();
            m_prefSize.width = Math.max(d.width, m_prefSize.width);
            final Dimension dimension5 = m_prefSize;
            dimension5.height += d.height + this.m_vgap;
        }
        return m_prefSize;
    }
    
    @Override
    public void layoutContainer(final Container parent) {
        int top = parent.getAppearance().getContentHeight();
        int bottom = 0;
        int left = 0;
        int right = parent.getAppearance().getContentWidth();
        Widget w = this.getWidgetByConstraint(parent, BorderLayoutData.Values.SOUTH);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getPrefSize();
            int x = left;
            int width;
            if (w.isExpandable()) {
                width = right - left;
            }
            else {
                width = (int)d.getWidth();
                x += (right - left - width) / 2;
            }
            w.setSize(width, d.height);
            w.setPosition(x, bottom);
            bottom += d.height + this.m_vgap;
        }
        w = this.getWidgetByConstraint(parent, BorderLayoutData.Values.NORTH);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getPrefSize();
            int x = left;
            int width;
            if (w.isExpandable()) {
                width = right - left;
            }
            else {
                width = (int)d.getWidth();
                x += (right - left - width) / 2;
            }
            w.setSize(width, d.height);
            w.setPosition(x, top - d.height);
            top -= d.height + this.m_vgap;
        }
        w = this.getWidgetByConstraint(parent, BorderLayoutData.Values.EAST);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getPrefSize();
            int y = bottom;
            int height;
            if (w.isExpandable()) {
                height = top - bottom;
            }
            else {
                height = (int)d.getHeight();
                y += (top - bottom - height) / 2;
            }
            w.setSize(d.width, height);
            w.setPosition(right - d.width, y);
            right -= d.width + this.m_hgap;
        }
        w = this.getWidgetByConstraint(parent, BorderLayoutData.Values.WEST);
        if (w != null && w.getVisible()) {
            final Dimension d = w.getPrefSize();
            int y = bottom;
            int height;
            if (w.isExpandable()) {
                height = top - bottom;
            }
            else {
                height = d.height;
                y += (top - bottom - height) / 2;
            }
            w.setSize(d.width, height);
            w.setPosition(left, y);
            left += d.width + this.m_hgap;
        }
        w = this.getWidgetByConstraint(parent, BorderLayoutData.Values.CENTER);
        if (w != null && w.getVisible()) {
            if (w.isExpandable()) {
                w.setSize(right - left, top - bottom);
                w.setPosition(left, bottom);
            }
            else {
                w.setSizeToPrefSize();
                w.setPosition(left + (right - left - w.getWidth()) / 2, bottom + (top - bottom - w.getHeight()) / 2);
            }
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    @Override
    public void copyElement(final BasicElement b) {
        final BorderLayout e = (BorderLayout)b;
        super.copyElement(e);
        e.m_hgap = this.m_hgap;
        e.m_vgap = this.m_vgap;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == BorderLayout.HGAP_HASH) {
            this.setHGap(PrimitiveConverter.getInteger(value));
        }
        else {
            if (hash != BorderLayout.VGAP_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setVGap(PrimitiveConverter.getInteger(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    @Override
    public BorderLayout clone() {
        final BorderLayout bl = new BorderLayout();
        bl.onCheckOut();
        this.copyElement(bl);
        return bl;
    }
    
    static {
        BorderLayout.m_logger = Logger.getLogger((Class)BorderLayout.class);
        HGAP_HASH = "hgap".hashCode();
        VGAP_HASH = "vgap".hashCode();
    }
}
