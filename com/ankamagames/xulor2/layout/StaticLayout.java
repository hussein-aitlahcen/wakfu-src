package com.ankamagames.xulor2.layout;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class StaticLayout extends AbstractLayoutManager
{
    private static Logger m_logger;
    public static final String TAG = "StaticLayout";
    public static final String SHORT_TAG = "sl";
    private boolean m_adaptToContentSize;
    private static final HashMap<String, Widget> autoPositionnableReferentWidgets;
    public static final int ADAPT_TO_CONTENT_SIZE_HASH;
    
    public StaticLayout() {
        super();
        this.m_adaptToContentSize = false;
    }
    
    @Override
    public String getTag() {
        return "StaticLayout";
    }
    
    @Override
    public boolean canComputeContentSize() {
        return this.m_adaptToContentSize;
    }
    
    public boolean getAdaptToContentSize() {
        return this.m_adaptToContentSize;
    }
    
    public void setAdaptToContentSize(final boolean adaptToContentSize) {
        this.m_adaptToContentSize = adaptToContentSize;
    }
    
    @Override
    public Dimension getContentMinSize(final Container container) {
        if (this.m_adaptToContentSize) {
            int minWidth = 0;
            int minHeight = 0;
            final ArrayList<Widget> widgets = container.getWidgetChildren();
            for (int i = 0; i < widgets.size(); ++i) {
                final Widget widget = widgets.get(i);
                if (widget.getVisible()) {
                    final StaticLayoutData sld = (StaticLayoutData)widget.getLayoutData();
                    int x = 0;
                    int y = 0;
                    if (sld.isXInit()) {
                        x = sld.getX();
                    }
                    if (sld.isYInit()) {
                        y = sld.getY();
                    }
                    if (sld.getSize() != null) {
                        final Dimension size = sld.getSize();
                        final Dimension minSize = widget.getMinSize();
                        if (size.getWidthPercentage() == -1.0f) {
                            int width = (int)size.getWidth();
                            minWidth = Math.max(size.width + x, minWidth);
                            if (width == -1) {
                                width = widget.m_size.width;
                            }
                            else if (width == -2) {
                                width = (int)minSize.getWidth();
                            }
                            minWidth = Math.max(width + x, minWidth);
                        }
                        else {
                            minWidth = Math.max(Math.round(minSize.width / size.getWidthPercentage() * 100.0f) + x, minWidth);
                        }
                        if (size.getHeightPercentage() == -1.0f) {
                            int height = (int)size.getHeight();
                            minWidth = Math.max(size.height + y, minWidth);
                            if (height == -1) {
                                height = widget.m_size.height;
                            }
                            else if (height == -2) {
                                height = (int)minSize.getHeight();
                            }
                            minWidth = Math.max(height + y, minWidth);
                        }
                        else {
                            minWidth = Math.max(Math.round(minSize.height / size.getHeightPercentage() * 100.0f) + y, minWidth);
                        }
                    }
                    else {
                        final Dimension minSize2 = widget.getMinSize();
                        minWidth = Math.max(minSize2.width + x, minWidth);
                        minHeight = Math.max(minSize2.height + y, minHeight);
                    }
                }
            }
            return new Dimension(minWidth, minHeight);
        }
        return new Dimension(0, 0);
    }
    
    @Override
    public Dimension getContentPreferedSize(final Container container) {
        if (this.m_adaptToContentSize) {
            int prefWidth = 0;
            int prefHeight = 0;
            final ArrayList<Widget> widgets = container.getWidgetChildren();
            for (int i = 0; i < widgets.size(); ++i) {
                final Widget widget = widgets.get(i);
                if (widget.getVisible()) {
                    if (widget.getLayoutData() instanceof StaticLayoutData) {
                        final StaticLayoutData sld = (StaticLayoutData)widget.getLayoutData();
                        if (sld != null) {
                            int x = 0;
                            int y = 0;
                            if (sld.isXInit()) {
                                x = sld.getX();
                            }
                            if (sld.isYInit()) {
                                y = sld.getY();
                            }
                            if (sld.getSize() != null) {
                                final Dimension size = sld.getSize();
                                final Dimension prefSize = widget.getPrefSize();
                                if (size.getWidthPercentage() == -1.0f) {
                                    int width = (int)size.getWidth();
                                    prefWidth = Math.max(size.width + x, prefWidth);
                                    if (width == -1) {
                                        width = widget.m_size.width;
                                    }
                                    else if (width == -2) {
                                        width = (int)prefSize.getWidth();
                                    }
                                    prefWidth = Math.max(width + x, prefWidth);
                                }
                                else {
                                    prefWidth = Math.max(Math.round(prefSize.width / size.getWidthPercentage() * 100.0f) + x, prefWidth);
                                }
                                if (size.getHeightPercentage() == -1.0f) {
                                    int height = (int)size.getHeight();
                                    prefHeight = Math.max(size.height + y, prefHeight);
                                    if (height == -1) {
                                        height = widget.m_size.height;
                                    }
                                    else if (height == -2) {
                                        height = (int)prefSize.getHeight();
                                    }
                                    prefHeight = Math.max(height + y, prefHeight);
                                }
                                else {
                                    prefHeight = Math.max(Math.round(prefSize.height / size.getHeightPercentage() * 100.0f) + y, prefHeight);
                                }
                            }
                            else {
                                final Dimension prefSize2 = widget.getPrefSize();
                                prefWidth = Math.max(prefSize2.width + x, prefWidth);
                                prefHeight = Math.max(prefSize2.height + y, prefHeight);
                            }
                        }
                    }
                }
            }
            return new Dimension(prefWidth, prefHeight);
        }
        return new Dimension(0, 0);
    }
    
    @Override
    public Dimension getContentGreedySize(final Container container, final Widget greedy, final Dimension contentSize) {
        if (!this.m_adaptToContentSize || container == null || greedy == null) {
            return new Dimension(0, 0);
        }
        if (!(greedy.getLayoutData() instanceof StaticLayoutData)) {
            return new Dimension(0, 0);
        }
        final int contentWidth = contentSize.width;
        final int contentHeight = contentSize.height;
        final StaticLayoutData sld = (StaticLayoutData)greedy.getLayoutData();
        int x = 0;
        int y = 0;
        if (sld.isXInit()) {
            x = sld.getX();
        }
        if (sld.isYInit()) {
            y = sld.getY();
        }
        final Dimension size = sld.getSize();
        int prefWidth;
        int prefHeight;
        if (size != null) {
            if (size.getWidthPercentage() == -1.0f) {
                prefWidth = contentWidth - x;
            }
            else {
                prefWidth = (int)(contentWidth * size.getWidthPercentage() / 100.0f) - x;
            }
            if (size.getHeightPercentage() == -1.0f) {
                prefHeight = contentHeight - y;
            }
            else {
                prefHeight = (int)(contentHeight * size.getHeightPercentage() / 100.0f) - y;
            }
        }
        else {
            prefWidth = contentWidth - x;
            prefHeight = contentHeight - y;
        }
        return new Dimension(prefWidth, prefHeight);
    }
    
    public static boolean doLayoutStatic(final Container container, final Widget widget) {
        if (container == null || widget == null) {
            return false;
        }
        if (widget == null || widget.getLayoutData() == null || !widget.getVisible() || !(widget.getLayoutData() instanceof StaticLayoutData)) {
            return false;
        }
        final StaticLayoutData sld = (StaticLayoutData)widget.getLayoutData();
        if (sld.isUsable()) {
            sld.setUsable(false);
            if (!sld.isInitValue() || !widget.isSizeInitByUserDefinition()) {
                if (sld.getSize() != null) {
                    final Dimension size = sld.getSize();
                    final Dimension prefSize = widget.getPrefSize();
                    int width;
                    if (size.getWidthPercentage() != -1.0f) {
                        width = (int)Math.round(container.getAppearance().getContentWidth() * size.getWidthPercentage() / 100.0);
                    }
                    else {
                        width = size.width;
                    }
                    int height;
                    if (size.getHeightPercentage() != -1.0f) {
                        height = (int)Math.round(container.getAppearance().getContentHeight() * size.getHeightPercentage() / 100.0);
                    }
                    else {
                        height = size.height;
                    }
                    if (height == -1) {
                        height = widget.m_size.height;
                    }
                    else if (height == -2) {
                        height = (int)prefSize.getHeight();
                    }
                    if (width == -1) {
                        width = widget.m_size.width;
                    }
                    else if (width == -2) {
                        width = (int)prefSize.getWidth();
                    }
                    widget.setSize(width, height);
                }
                else {
                    widget.setSizeToPrefSize();
                }
            }
            if (!sld.isInitValue() || !widget.isPositionInitByUserDefinition()) {
                if (sld.isXInit()) {
                    widget.setX(sld.getX());
                }
                if (sld.isYInit()) {
                    widget.setY(sld.getY());
                }
                if (sld.getAlignment() != null) {
                    final Alignment17 align = sld.getAlignment();
                    final int xOffset = sld.isXOffsetInit() ? sld.getXOffset() : 0;
                    final int yOffset = sld.isYOffsetInit() ? sld.getYOffset() : 0;
                    widget.setX(align.getX(widget.getSize().width, container.getAppearance().getContentWidth()) + xOffset);
                    widget.setY(align.getY(widget.getSize().height, container.getAppearance().getContentHeight()) + yOffset);
                }
                if (sld.getXPerc() != null) {
                    widget.setX((int)(sld.getXPerc().getValue() / 100.0 * (container.getAppearance().getContentWidth() - widget.getSize().width)));
                }
                if (sld.getYPerc() != null) {
                    widget.setY((int)(sld.getYPerc().getValue() / 100.0 * (container.getAppearance().getContentHeight() - widget.getSize().height)));
                }
                if (sld.isAutoPositionable()) {
                    final Widget referentWidget = sld.getReferentWidget();
                    final RootContainer rootContainer = referentWidget.getWidgetParentOfType(RootContainer.class);
                    rootContainer.getWindowManager().recomputeGroup(sld.getControlGroup(), referentWidget);
                    sld.setControlGroup(null);
                    sld.setReferentWidget(null);
                    sld.setCascadeMethodEnabled(false);
                }
            }
            if (sld.isInitValue() && widget.isPositionInitByUserDefinition()) {
                final RootContainer rootContainer2 = widget.getWidgetParentOfType(RootContainer.class);
                widget.setX(Math.max(0, Math.min(widget.getX(rootContainer2), rootContainer2.getWidth() - widget.getWidth())));
                widget.setY(Math.max(0, Math.min(widget.getY(rootContainer2), rootContainer2.getHeight() - widget.getHeight())));
            }
            return true;
        }
        return false;
    }
    
    public static void doLayoutStatic(final Container container, final ArrayList<Widget> list) {
        if (container == null || list == null) {
            return;
        }
        StaticLayout.autoPositionnableReferentWidgets.clear();
        final int contentWidth = container.getAppearance().getContentWidth();
        final int contentHeight = container.getAppearance().getContentHeight();
        for (int i = 0, lsize = list.size(); i < lsize; ++i) {
            final Widget widget = list.get(i);
            if (widget != null) {
                final AbstractLayoutData layoutData = widget.getLayoutData();
                if (layoutData != null && widget.getVisible()) {
                    if (layoutData instanceof StaticLayoutData) {
                        final StaticLayoutData sld = (StaticLayoutData)layoutData;
                        if (sld.isUsable()) {
                            sld.setUsable(false);
                            if (!sld.isInitValue() || !widget.isSizeInitByUserDefinition()) {
                                final Dimension size = sld.getSize();
                                if (size != null) {
                                    final Dimension prefSize = widget.getPrefSize();
                                    int width;
                                    if (size.getWidthPercentage() != -1.0f) {
                                        width = MathHelper.fastRound(contentWidth * size.getWidthPercentage() / 100.0f);
                                    }
                                    else {
                                        width = size.width;
                                    }
                                    int height;
                                    if (size.getHeightPercentage() != -1.0f) {
                                        height = MathHelper.fastRound(contentHeight * size.getHeightPercentage() / 100.0f);
                                    }
                                    else {
                                        height = size.height;
                                    }
                                    if (height == -1) {
                                        height = widget.m_size.height;
                                    }
                                    else if (height == -2) {
                                        height = (int)prefSize.getHeight();
                                    }
                                    if (width == -1) {
                                        width = widget.m_size.width;
                                    }
                                    else if (width == -2) {
                                        width = (int)prefSize.getWidth();
                                    }
                                    widget.setSize(width, height);
                                }
                                else {
                                    widget.setSizeToPrefSize();
                                }
                            }
                            int x = widget.getX();
                            int y = widget.getY();
                            if (!sld.isInitValue() || !widget.isPositionInitByUserDefinition()) {
                                if (sld.isXInit()) {
                                    x = sld.getX();
                                }
                                if (sld.isYInit()) {
                                    y = sld.getY();
                                }
                                final int width = widget.getSize().width;
                                final int height = widget.getSize().height;
                                if (sld.getAlignment() != null) {
                                    final Alignment17 align = sld.getAlignment();
                                    final int xOffset = sld.isXOffsetInit() ? sld.getXOffset() : 0;
                                    final int yOffset = sld.isYOffsetInit() ? sld.getYOffset() : 0;
                                    x = align.getX(width, contentWidth) + xOffset;
                                    y = align.getY(height, contentHeight) + yOffset;
                                }
                                if (sld.getXPerc() != null) {
                                    final int xOffset2 = sld.isXOffsetInit() ? sld.getXOffset() : 0;
                                    x = (int)MathHelper.fastRound(sld.getXPerc().getValue() / 100.0 * (contentWidth - width)) + xOffset2;
                                }
                                if (sld.getYPerc() != null) {
                                    final int yOffset2 = sld.isYOffsetInit() ? sld.getYOffset() : 0;
                                    y = (int)MathHelper.fastRound(sld.getYPerc().getValue() / 100.0 * (contentHeight - height)) + yOffset2;
                                }
                                if (sld.isAutoPositionable()) {
                                    StaticLayout.autoPositionnableReferentWidgets.put(sld.getControlGroup(), sld.getReferentWidget());
                                    sld.setReferentWidget(null);
                                }
                            }
                            else {
                                final RootContainer rootContainer = widget.getWidgetParentOfType(RootContainer.class);
                                x = Math.max(0, Math.min(widget.getX(rootContainer), rootContainer.getWidth() - widget.getWidth()));
                                y = Math.max(0, Math.min(widget.getY(rootContainer), rootContainer.getHeight() - widget.getHeight()));
                            }
                            widget.setPosition(x, y);
                        }
                    }
                }
            }
        }
        if (StaticLayout.autoPositionnableReferentWidgets.size() != 0) {
            for (final Map.Entry<String, Widget> entry : StaticLayout.autoPositionnableReferentWidgets.entrySet()) {
                final RootContainer rootContainer2 = entry.getValue().getWidgetParentOfType(RootContainer.class);
                rootContainer2.getWindowManager().recomputeGroup(entry.getKey(), entry.getValue());
            }
        }
    }
    
    @Override
    public void layoutContainer(final Container parent) {
        doLayoutStatic(parent, parent.getWidgetChildren());
    }
    
    @Override
    public void layoutWidget(final Container parent, final Widget child) {
        doLayoutStatic(parent, child);
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        ((StaticLayout)source).setAdaptToContentSize(this.m_adaptToContentSize);
    }
    
    @Override
    public StaticLayout clone() {
        final StaticLayout l = new StaticLayout();
        l.onCheckOut();
        this.copyElement(l);
        return l;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_isStandAlone = true;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == StaticLayout.ADAPT_TO_CONTENT_SIZE_HASH) {
            this.setAdaptToContentSize(PrimitiveConverter.getBoolean(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        StaticLayout.m_logger = Logger.getLogger((Class)StaticLayout.class);
        autoPositionnableReferentWidgets = new HashMap<String, Widget>();
        ADAPT_TO_CONTENT_SIZE_HASH = "adaptToContentSize".hashCode();
    }
}
