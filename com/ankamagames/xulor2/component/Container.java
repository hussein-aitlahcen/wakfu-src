package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.util.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.graphics.image.*;
import java.awt.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Container extends Widget
{
    public static final String TAG = "Container";
    private static final ObjectPool m_pool;
    protected final ArrayList<Widget> m_widgetChildren;
    protected boolean m_rootFocusContainer;
    protected AbstractLayoutManager m_layout;
    protected boolean m_widgetListChanged;
    protected boolean m_pack;
    protected boolean m_invalidateOnMinSizeChange;
    private Dimension m_contentMinSize;
    private Dimension m_contentPrefSize;
    private boolean m_justInvalidatedSize;
    public static final int PACK_HASH;
    
    public Container() {
        super();
        this.m_widgetChildren = new ArrayList<Widget>();
        this.m_rootFocusContainer = false;
        this.m_widgetListChanged = false;
        this.m_pack = false;
        this.m_invalidateOnMinSizeChange = false;
        this.m_contentMinSize = null;
        this.m_contentPrefSize = null;
        this.m_justInvalidatedSize = false;
    }
    
    @Override
    public void addWidget(final Widget widget) {
        this.addWidget(widget, this.m_widgetChildren.size());
    }
    
    public boolean addWidget(final Widget widget, final int index) {
        if (index < 0 || index > this.m_widgetChildren.size()) {
            Container.m_logger.error((Object)("Tentative d'ajout d'un widget a un parent avec un index invalide (index=" + index + ", taille=" + this.m_widgetChildren.size()));
        }
        else {
            if (!this.m_widgetChildren.contains(widget)) {
                this.m_widgetChildren.add(index, widget);
                widget.setContainerParent(this);
                if (this.isInWidgetTree()) {
                    widget.addedToWidgetTree();
                }
                this.m_widgetListChanged = true;
                this.setNeedsToPreProcess();
                return true;
            }
            Container.m_logger.error((Object)"Tentative d'ajout d'un widget d\u00e9j\u00e0 contenu");
        }
        return false;
    }
    
    @Override
    public void removeWidget(final Widget widget) {
        if (widget != null && this.m_widgetChildren != null && this.m_widgetChildren.contains(widget)) {
            if (this.isInWidgetTree()) {
                widget.removedFromWidgetTree();
            }
            this.m_widgetChildren.remove(widget);
            widget.setContainerParent(null);
            this.m_widgetListChanged = true;
            this.setNeedsToPreProcess();
            widget.removeSelfFromParent();
        }
    }
    
    @Override
    public void setWidgetOnTop(final Widget widget) {
        if (widget != null && this.m_widgetChildren != null && this.m_widgetChildren.contains(widget)) {
            this.m_widgetChildren.remove(widget);
            this.addWidget(widget);
        }
    }
    
    @Override
    public void add(final DataElement e) {
        super.add(e);
        if (e instanceof AbstractLayoutManager) {
            this.setLayoutManager((AbstractLayoutManager)e);
        }
    }
    
    public void add(final Widget widget, final int index) {
        if (this.addWidget(widget, index)) {
            super.add(widget, false);
        }
        else if (widget != null) {
            widget.destroySelfFromParent();
            Container.m_logger.warn((Object)"On lib\u00e8re le Widget qui n'a pas p\u00fb \u00eatre ajout\u00e9");
        }
    }
    
    public void removeAllWidgets() {
        for (final Widget w : this.m_widgetChildren) {
            if (this.isInWidgetTree()) {
                w.removedFromWidgetTree();
            }
            w.setContainerParent(null);
        }
        this.m_widgetChildren.clear();
        this.m_widgetListChanged = true;
        this.setNeedsToPreProcess();
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
        for (int numChildren = this.m_widgetChildren.size(), i = 0; i < numChildren; ++i) {
            final Widget w = this.m_widgetChildren.get(i);
            final Entity entity = w.getEntity();
            if (w.getVisible() && entity != null) {
                this.m_entity.addChild(entity);
            }
        }
    }
    
    @Override
    public String getTag() {
        return "Container";
    }
    
    public ArrayList<Widget> getWidgetChildren() {
        return this.m_widgetChildren;
    }
    
    @Override
    public DecoratorAppearance getAppearance() {
        return this.m_appearance;
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return true;
    }
    
    public void setLayoutManager(final AbstractLayoutManager manager) {
        if (this.m_layout != null && !this.m_layout.isUnloading()) {
            this.m_layout.release();
        }
        this.m_layout = manager;
    }
    
    public LayoutManager getLayoutManager() {
        return this.m_layout;
    }
    
    @Override
    public void setVisible(final boolean visible) {
        if (visible != this.m_visible) {
            this.setNeedsToPreProcess();
            this.setNeedsToMiddleProcess();
        }
        super.setVisible(visible);
    }
    
    @Override
    protected void setParentVisible(final boolean parentVisible) {
        if (parentVisible != this.m_parentVisible) {
            super.setParentVisible(parentVisible);
            for (int i = this.m_widgetChildren.size() - 1; i >= 0; --i) {
                this.m_widgetChildren.get(i).setParentVisible(parentVisible);
            }
        }
    }
    
    @Override
    public Dimension getMaxSize() {
        if (this.m_layout != null) {
            return this.m_layout.getContentPreferedSize(this);
        }
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    @Override
    public Dimension getContentMinSize() {
        if (this.m_layout != null) {
            if (this.m_contentMinSize == null) {
                this.m_contentMinSize = this.m_layout.getContentMinSize(this);
            }
            final int width = (this.m_minSize == null) ? this.m_contentMinSize.width : Math.max(this.m_contentMinSize.width, this.m_minSize.width);
            final int height = (this.m_minSize == null) ? this.m_contentMinSize.height : Math.max(this.m_contentMinSize.height, this.m_minSize.height);
            return new Dimension(width, height);
        }
        return super.getContentMinSize();
    }
    
    @Override
    public Dimension getContentPrefSize() {
        if (this.m_layout != null) {
            if (this.m_contentPrefSize == null) {
                this.m_contentPrefSize = this.m_layout.getContentPreferedSize(this);
            }
            final int width = (this.m_prefSize == null) ? this.m_contentPrefSize.width : Math.max(this.m_contentPrefSize.width, this.m_prefSize.width);
            final int height = (this.m_prefSize == null) ? this.m_contentPrefSize.height : Math.max(this.m_contentPrefSize.height, this.m_prefSize.height);
            return new Dimension(width, height);
        }
        return super.getContentPrefSize();
    }
    
    @Override
    public Dimension getContentGreedySize() {
        if (this.m_containerParent == null) {
            return new Dimension(this.m_appearance.getContentWidth(), this.m_appearance.getContentHeight());
        }
        final Dimension contentGreedySize;
        final Dimension parentGreedySize = contentGreedySize = this.m_containerParent.getContentGreedySize();
        contentGreedySize.width -= this.m_containerParent.getAppearance().getLeftInset() + this.m_containerParent.getAppearance().getRightInset();
        final Dimension dimension = parentGreedySize;
        dimension.height -= this.m_containerParent.getAppearance().getTopInset() + this.m_containerParent.getAppearance().getBottomInset();
        return this.m_containerParent.getLayoutManager().getContentGreedySize(this.m_containerParent, this, parentGreedySize);
    }
    
    public boolean isRootFocusContainer() {
        return this.m_rootFocusContainer;
    }
    
    public void setRootFocusContainer(final boolean rootFocusContainer) {
        this.m_rootFocusContainer = rootFocusContainer;
    }
    
    @Override
    public Container getRootFocusParent() {
        if (this.m_rootFocusContainer) {
            return this;
        }
        return super.getRootFocusParent();
    }
    
    @Override
    public void setNonBlocking(final boolean nonBlocking) {
        this.setNonBlocking(nonBlocking, false);
    }
    
    public void setNonBlocking(final boolean nonBlocking, final boolean propagateToChildren) {
        super.setNonBlocking(nonBlocking);
        if (propagateToChildren) {
            for (final Widget w : this.getWidgetChildren()) {
                if (w instanceof Container) {
                    ((Container)w).setNonBlocking(nonBlocking, propagateToChildren);
                }
                else {
                    w.setNonBlocking(nonBlocking);
                }
            }
        }
    }
    
    public boolean getInvalidateOnMinSizeChange() {
        return this.m_invalidateOnMinSizeChange;
    }
    
    public void setInvalidateOnMinSizeChange(final boolean invalidateOnMinSizeChange) {
        this.m_invalidateOnMinSizeChange = invalidateOnMinSizeChange;
    }
    
    public void setPack(final boolean pack) {
        this.m_pack = pack;
    }
    
    public boolean getPack() {
        return this.m_pack;
    }
    
    @Override
    public Widget getWidget(int x, int y) {
        if (this.m_unloading || !this.m_visible || !this.getAppearance().insideInsets(x, y)) {
            return null;
        }
        Widget found = this.m_nonBlocking ? null : this;
        x -= this.getAppearance().getLeftInset();
        y -= this.getAppearance().getBottomInset();
        for (int i = this.m_widgetChildren.size() - 1; i >= 0; --i) {
            Widget w = this.m_widgetChildren.get(i);
            if (!w.isUnloading()) {
                w = w.getWidget(x - w.m_position.x, y - w.m_position.y);
                if (w != null) {
                    found = w;
                    break;
                }
            }
        }
        return found;
    }
    
    public Widget getWidget(final int index) {
        try {
            return this.m_widgetChildren.get(index);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public Widget getNextFocusableWidget() {
        return null;
    }
    
    public PooledRectangle getScissor(final Widget widget) {
        final Point screen = this.getScreenPosition();
        final int x1 = screen.x + this.m_appearance.getLeftInset();
        final int y1 = screen.y + this.m_appearance.getBottomInset();
        final int w1 = this.m_appearance.getContentWidth();
        final int h1 = this.m_appearance.getContentHeight();
        if (widget == null) {
            return PooledRectangle.checkout(x1, y1, w1, h1);
        }
        final Point widgetScreen = widget.getScreenPosition();
        final int x2 = widgetScreen.x;
        final int y2 = widgetScreen.y;
        final int w2 = widget.getWidth();
        final int h2 = widget.getHeight();
        final PooledRectangle result = PooledRectangle.checkout();
        if (PooledRectangle.intersects(x2, y2, w2, h2, x1, y1, w1, h1)) {
            result.setBoundsFromIntersection(x2, y2, w2, h2, x1, y1, w1, h1);
        }
        return result;
    }
    
    @Override
    public void addedToWidgetTree() {
        super.addedToWidgetTree();
        for (final Widget w : this.m_widgetChildren) {
            w.addedToWidgetTree();
        }
    }
    
    @Override
    public void removedFromWidgetTree() {
        super.removedFromWidgetTree();
        for (final Widget w : this.m_widgetChildren) {
            w.removedFromWidgetTree();
        }
    }
    
    protected void resetContentSizeCache() {
        final Dimension dimension = null;
        this.m_contentPrefSize = dimension;
        this.m_contentMinSize = dimension;
        this.m_justInvalidatedSize = true;
        this.setNeedsToMiddleProcess();
    }
    
    public void invalidateMinSize() {
        this.resetContentSizeCache();
        if (this.m_containerParent != null) {
            this.m_containerParent.invalidateMinSize();
        }
        if (this.m_pack || this.m_invalidateOnMinSizeChange) {
            this.invalidate();
        }
    }
    
    @Override
    public void validate() {
        super.validate();
        this.m_justInvalidatedSize = false;
        if (this.m_pack) {
            this.setSizeToPrefSize();
        }
        if (this.m_layout != null && this.m_appearance != null) {
            this.m_layout.layoutContainer(this);
        }
        this.setNeedsToResetMeshes();
        this.m_widgetListChanged = false;
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        final boolean ret = super.preProcess(deltaTime);
        if (this.m_widgetListChanged) {
            this.invalidateMinSize();
        }
        return ret;
    }
    
    @Override
    public boolean middleProcess(final int deltaTime) {
        if (this.m_visible && this.m_justInvalidatedSize) {
            this.invalidate();
        }
        return super.middleProcess(deltaTime);
    }
    
    public static Container checkOut() {
        Container c;
        try {
            c = (Container)Container.m_pool.borrowObject();
            c.m_currentPool = Container.m_pool;
        }
        catch (Exception e) {
            Container.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            c = new Container();
            c.onCheckOut();
        }
        return c;
    }
    
    @Override
    public void onCheckIn() {
        this.removeAllWidgets();
        super.onCheckIn();
        this.m_contentMinSize = null;
        this.m_contentPrefSize = null;
        if (this.m_layout != null) {
            this.m_layout.release();
            this.m_layout = null;
        }
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_rootFocusContainer = false;
        this.m_widgetListChanged = false;
        this.m_pack = false;
        this.m_invalidateOnMinSizeChange = false;
        final DecoratorAppearance app = DecoratorAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        final RowLayout rowLayout = RowLayout.checkOut();
        this.add(rowLayout);
        this.m_nonBlocking = true;
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        final Container e = (Container)c;
        super.copyElement(e);
        e.m_pack = this.m_pack;
        e.m_rootFocusContainer = this.m_rootFocusContainer;
        AbstractLayoutManager l = null;
        if (this.m_layout != null) {
            l = this.m_layout.clone();
        }
        if (l != null) {
            e.setLayoutManager(l);
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == Container.PACK_HASH) {
            this.setPack(PrimitiveConverter.getBoolean(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == Container.PACK_HASH) {
            this.setPack(PrimitiveConverter.getBoolean(value));
            return true;
        }
        return super.setPropertyAttribute(hash, value);
    }
    
    static {
        m_pool = new MonitoredPool(new ObjectFactory<Container>() {
            @Override
            public Container makeObject() {
                return new Container();
            }
        });
        PACK_HASH = "pack".hashCode();
    }
}
