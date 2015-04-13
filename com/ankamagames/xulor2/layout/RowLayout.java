package com.ankamagames.xulor2.layout;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class RowLayout extends AbstractLayoutManager implements Releasable
{
    private static Logger m_logger;
    public static final String TAG = "RowLayout";
    public static final String SHORT_TAG = "rl";
    private static final ObjectPool m_pool;
    private boolean m_horizontal;
    private short m_hgap;
    private short m_vgap;
    private Alignment9 m_align;
    public static final int HGAP_HASH;
    public static final int VGAP_HASH;
    public static final int HORIZONTAL_HASH;
    public static final int ALIGN_HASH;
    
    public RowLayout() {
        super();
        this.m_horizontal = true;
        this.m_hgap = 0;
        this.m_vgap = 0;
        this.m_align = Alignment9.CENTER;
    }
    
    public static RowLayout checkOut() {
        RowLayout c;
        try {
            c = (RowLayout)RowLayout.m_pool.borrowObject();
            c.m_currentPool = RowLayout.m_pool;
        }
        catch (Exception e) {
            RowLayout.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            c = new RowLayout();
            c.onCheckOut();
        }
        return c;
    }
    
    @Override
    public String getTag() {
        return "RowLayout";
    }
    
    public Alignment9 getAlign() {
        return this.m_align;
    }
    
    public void setAlign(final Alignment9 align) {
        this.m_align = align;
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
                    final Dimension minSize = w.getMinSize();
                    width += minSize.width;
                    height = Math.max(height, minSize.height);
                }
            }
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
                    final Dimension minSize = w.getMinSize();
                    height += minSize.height;
                    width = Math.max(width, minSize.width);
                }
            }
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
                    final Dimension prefSize = w.getPrefSize();
                    width += prefSize.width;
                    height = Math.max(height, prefSize.height);
                }
            }
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
                    final Dimension prefSize = w.getPrefSize();
                    height += prefSize.height;
                    width = Math.max(width, prefSize.width);
                }
            }
        }
        return new Dimension(width, height);
    }
    
    @Override
    public Dimension getContentGreedySize(final Container container, final Widget greedy, final Dimension contentSize) {
        int width = contentSize.width;
        int height = contentSize.height;
        boolean firstVisible = true;
        if (this.m_horizontal) {
            for (int i = container.getWidgetChildren().size() - 1; i >= 0; --i) {
                final Widget w = container.getWidget(i);
                if (w.getVisible()) {
                    if (firstVisible) {
                        firstVisible = false;
                    }
                    else {
                        width -= this.m_hgap;
                    }
                    if (w != greedy) {
                        width -= w.getPrefSize().width;
                    }
                }
            }
        }
        else {
            for (int i = container.getWidgetChildren().size() - 1; i >= 0; --i) {
                final Widget w = container.getWidget(i);
                if (w.getVisible()) {
                    if (firstVisible) {
                        firstVisible = false;
                    }
                    else {
                        height -= this.m_vgap;
                    }
                    if (w != greedy) {
                        height -= w.getPrefSize().height;
                    }
                }
            }
        }
        return new Dimension(width, height);
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
    
    public void setVgap(final short vgap) {
        this.m_vgap = vgap;
    }
    
    private ArrayList<ObjectPair<Widget, Integer>> computeShrinkableSizes(final ArrayList<Widget> allWidgets, final int totalSize) {
        final ArrayList<ObjectPair<Widget, Integer>> list = new ArrayList<ObjectPair<Widget, Integer>>();
        final ArrayList<Widget> widgets = new ArrayList<Widget>();
        if (allWidgets != null) {
            for (final Widget w : allWidgets) {
                if (w.isShrinkable()) {
                    widgets.add(w);
                }
            }
        }
        if (widgets == null || widgets.size() == 0) {
            return list;
        }
        for (final Widget w : widgets) {
            final Dimension prefSize = w.getPrefSize();
            final int size = this.m_horizontal ? prefSize.width : prefSize.height;
            int index;
            for (index = list.size() - 1; index >= 0; --index) {
                final ObjectPair<Widget, Integer> pair = list.get(index);
                if (pair.getSecond() > size) {
                    break;
                }
            }
            list.add(index + 1, new ObjectPair<Widget, Integer>(w, size));
        }
        int remaining = totalSize;
        while (remaining > 0) {
            final int max = list.get(0).getSecond();
            int num = 0;
            int nextMin = 0;
            for (final ObjectPair<Widget, Integer> pair : list) {
                if (pair.getSecond() != max) {
                    nextMin = pair.getSecond();
                    break;
                }
                ++num;
            }
            if (remaining < num) {
                for (int i = 0; i < num && remaining > 0; --remaining, ++i) {
                    list.get(i).setSecond(list.get(i).getSecond() - 1);
                }
                break;
            }
            int toRemove = max - nextMin;
            if (toRemove * num > remaining || toRemove <= 0) {
                toRemove = (int)Math.floor(remaining / num);
            }
            remaining -= toRemove * num;
            for (int j = 0; j < num; ++j) {
                list.get(j).setSecond(max - toRemove);
            }
        }
        return list;
    }
    
    @Override
    public void layoutContainer(final Container parent) {
        int x = 0;
        int y = 0;
        int visibleWidgets = 0;
        int expandableWidgets = 0;
        int shrinkableWidgets = 0;
        int shrinkableSpace = 0;
        boolean shrinkWhenPossible = false;
        ArrayList<ObjectPair<Widget, Integer>> listOfShrinkablePairs = null;
        final ArrayList<Widget> widgets = parent.getWidgetChildren();
        if (this.m_horizontal) {
            final int availableHeight = parent.getAppearance().getContentHeight();
            int spaceAvailable = parent.getAppearance().getContentWidth();
            for (int i = 0, size = widgets.size(); i < size; ++i) {
                final Widget widget = widgets.get(i);
                if (widget.getVisible()) {
                    spaceAvailable -= widget.getPrefSize().width;
                    ++visibleWidgets;
                    if (widget.isExpandable()) {
                        ++expandableWidgets;
                    }
                    if (widget.isShrinkable()) {
                        ++shrinkableWidgets;
                    }
                }
            }
            spaceAvailable -= (visibleWidgets - 1) * this.m_hgap;
            if (spaceAvailable < 0) {
                shrinkWhenPossible = true;
                shrinkableSpace = -spaceAvailable;
                spaceAvailable = 0;
                listOfShrinkablePairs = this.computeShrinkableSizes(widgets, shrinkableSpace);
            }
            int spaceDivided = 0;
            if (expandableWidgets > 0) {
                spaceDivided = (int)Math.floor(spaceAvailable / expandableWidgets);
            }
            int space = 0;
            if (expandableWidgets > 0) {
                space = spaceAvailable - spaceDivided * expandableWidgets;
            }
            x = ((expandableWidgets == 0) ? this.m_align.getX(spaceAvailable) : 0);
            y = 0;
            for (int j = 0; j < widgets.size(); ++j) {
                final Widget widget2 = widgets.get(j);
                if (widget2.getVisible()) {
                    final Dimension prefSize = widget2.getPrefSize();
                    int width = prefSize.width;
                    int widgetY = y;
                    if (shrinkWhenPossible && widget2.isShrinkable() && listOfShrinkablePairs != null) {
                        for (final ObjectPair<Widget, Integer> pair : listOfShrinkablePairs) {
                            if (pair.getFirst() == widget2) {
                                width = pair.getSecond();
                                break;
                            }
                        }
                    }
                    if (widget2.isExpandable()) {
                        width += spaceDivided;
                        if (space > 0) {
                            ++width;
                            --space;
                        }
                    }
                    int height;
                    if (widget2.getLayoutData() instanceof RowLayoutData) {
                        height = prefSize.height;
                        widgetY += ((RowLayoutData)widget2.getLayoutData()).getAlign().getY(height, availableHeight);
                    }
                    else {
                        height = availableHeight;
                    }
                    widget2.setSize(width, height);
                    widget2.setPosition(x, widgetY);
                    x += width + this.m_hgap;
                }
            }
        }
        else {
            final int availableWidth = parent.getAppearance().getContentWidth();
            int spaceAvailable = parent.getAppearance().getContentHeight();
            for (int i = 0, size = widgets.size(); i < size; ++i) {
                final Widget widget = widgets.get(i);
                if (widget.getVisible()) {
                    spaceAvailable -= widget.getPrefSize().height;
                    ++visibleWidgets;
                    if (widget.isExpandable()) {
                        ++expandableWidgets;
                    }
                    if (widget.isShrinkable()) {
                        ++shrinkableWidgets;
                    }
                }
            }
            spaceAvailable -= (visibleWidgets - 1) * this.m_vgap;
            if (spaceAvailable < 0) {
                shrinkWhenPossible = true;
                shrinkableSpace = -spaceAvailable;
                spaceAvailable = 0;
                listOfShrinkablePairs = this.computeShrinkableSizes(widgets, shrinkableSpace);
            }
            int spaceDivided = 0;
            if (expandableWidgets > 0) {
                spaceDivided = (int)Math.floor(spaceAvailable / expandableWidgets);
            }
            int space = 0;
            if (expandableWidgets > 0) {
                space = spaceAvailable - spaceDivided * expandableWidgets;
            }
            x = 0;
            y = parent.getAppearance().getContentHeight() - ((expandableWidgets == 0) ? (spaceAvailable - this.m_align.getY(spaceAvailable)) : 0);
            for (int j = 0; j < widgets.size(); ++j) {
                final Widget widget2 = widgets.get(j);
                if (widget2.getVisible()) {
                    final Dimension prefSize = widget2.getPrefSize();
                    int height2 = prefSize.height;
                    int widgetX = x;
                    if (shrinkWhenPossible && widget2.isShrinkable() && listOfShrinkablePairs != null) {
                        for (final ObjectPair<Widget, Integer> pair : listOfShrinkablePairs) {
                            if (pair.getFirst() == widget2) {
                                height2 = pair.getSecond();
                                break;
                            }
                        }
                    }
                    if (widget2.isExpandable()) {
                        height2 += spaceDivided;
                        if (space > 0) {
                            ++height2;
                            --space;
                        }
                    }
                    int width2;
                    if (widget2.getLayoutData() instanceof RowLayoutData) {
                        width2 = prefSize.width;
                        widgetX += ((RowLayoutData)widget2.getLayoutData()).getAlign().getX(width2, availableWidth);
                    }
                    else {
                        width2 = availableWidth;
                    }
                    widget2.setSize(width2, height2);
                    y -= height2 + this.m_vgap;
                    widget2.setPosition(widgetX, y);
                }
            }
        }
    }
    
    @Override
    public void copyElement(final BasicElement r) {
        final RowLayout e = (RowLayout)r;
        super.copyElement(e);
        e.m_hgap = this.m_hgap;
        e.m_vgap = this.m_vgap;
        e.m_horizontal = this.m_horizontal;
        e.m_align = this.m_align;
    }
    
    @Override
    public RowLayout clone() {
        final RowLayout l = checkOut();
        this.copyElement(l);
        return l;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_horizontal = true;
        this.m_hgap = 0;
        this.m_vgap = 0;
        this.m_align = Alignment9.CENTER;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == RowLayout.HGAP_HASH) {
            this.setHgap(PrimitiveConverter.getShort(value));
        }
        else if (hash == RowLayout.VGAP_HASH) {
            this.setVgap(PrimitiveConverter.getShort(value));
        }
        else if (hash == RowLayout.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != RowLayout.ALIGN_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setAlign(Alignment9.value(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        RowLayout.m_logger = Logger.getLogger((Class)RowLayout.class);
        m_pool = new MonitoredPool(new ObjectFactory<RowLayout>() {
            @Override
            public RowLayout makeObject() {
                return new RowLayout();
            }
        }, 2000);
        HGAP_HASH = "hgap".hashCode();
        VGAP_HASH = "vgap".hashCode();
        HORIZONTAL_HASH = "horizontal".hashCode();
        ALIGN_HASH = "align".hashCode();
    }
}
