package com.ankamagames.xulor2.component;

import org.apache.log4j.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.*;
import gnu.trove.*;
import org.apache.commons.lang3.*;
import java.util.*;

public class LayeredContainer extends Container
{
    private static Logger m_logger;
    public static final String TAG = "LayeredContainer";
    public static final int WORLD_LAYER = -40000;
    public static final int MRU_LAYER = -39999;
    public static final int CONTENT_LAYER = -30000;
    public static final int BOTTOM_UI_LAYER = -10000;
    public static final int BUBBLE_LAYER = 25000;
    public static final int UPPER_CONTENT_LAYER = 26000;
    public static final int MESSAGE_BOX_LAYER = 27000;
    public static final int POPUP_LAYER = 30000;
    public static final int CURSOR_LAYER = 40000;
    public static final int LAYER_NOT_FOUND = Integer.MIN_VALUE;
    private TIntArrayList m_layerIndex;
    
    public LayeredContainer() {
        super();
        this.m_layerIndex = new TIntArrayList();
    }
    
    public void addWidgetToLayer(final Widget w, final int layer) {
        this.addWidgetToLayer(w, layer, Integer.MAX_VALUE);
    }
    
    public void addWidgetToLayer(final Widget w, final int layer, final int pos) {
        if (!this.m_unloading) {
            Container c = this.getContainerFromLayer(layer);
            if (c == null) {
                c = this.insertLayer(layer);
            }
            final int index = Math.min(c.getWidgetChildren().size(), pos);
            c.add(w, index);
        }
    }
    
    private Container insertLayer(final int layer) {
        final Container c = Container.checkOut();
        c.setSize(this.getWidth(), this.getHeight());
        final StaticLayoutData sld = new StaticLayoutData();
        sld.onCheckOut();
        sld.setSize(new Dimension(100.0f, 100.0f));
        c.add(sld);
        final StaticLayout sl = new StaticLayout();
        sl.setAdaptToContentSize(true);
        sl.onCheckOut();
        c.add(sl);
        int index = 0;
        for (int size = this.m_widgetChildren.size(); index < size && this.m_layerIndex.get(index) <= layer; ++index) {}
        this.m_layerIndex.insert(index, layer);
        this.add(c, index);
        return c;
    }
    
    @Override
    public void remove(final EventDispatcher w) {
        if (w instanceof Container) {
            final int offset = this.m_widgetChildren.indexOf(w);
            if (offset != -1) {
                this.m_layerIndex.remove(offset);
            }
        }
        super.remove(w);
    }
    
    @Override
    public String getTag() {
        return "LayeredContainer";
    }
    
    public int getLayer(final Widget w) {
        for (int i = 0, size = this.m_widgetChildren.size(); i < size; ++i) {
            final Container c = this.m_widgetChildren.get(i);
            if (c.getWidgetChildren().contains(w)) {
                return this.m_layerIndex.get(i);
            }
        }
        return Integer.MIN_VALUE;
    }
    
    public int getWidgetCountInLayer(final int i) {
        final Container fromLayer = this.getContainerFromLayer(i);
        if (fromLayer != null) {
            return fromLayer.getWidgetChildren().size();
        }
        return 0;
    }
    
    public Container getContainerFromLayer(final int layer) {
        final int index = this.m_layerIndex.indexOf(layer);
        if (index != -1) {
            return this.m_widgetChildren.get(index);
        }
        return null;
    }
    
    public Container getContainerFromWidget(final Widget w) {
        for (int i = 0, size = this.m_widgetChildren.size(); i < size; ++i) {
            final Container c = this.m_widgetChildren.get(i);
            if (c.getWidgetChildren().contains(w)) {
                return c;
            }
        }
        return null;
    }
    
    public void forEachWidgetInLayers(final TObjectProcedure<Widget> procedure, final int... layers) {
        for (final int layer : layers) {
            procedure.execute(this.getContainerFromLayer(layer));
        }
    }
    
    public void forEachWidgetNotInLayers(final TObjectProcedure<Widget> procedure, final int... layers) {
        if (!this.m_layerIndex.isEmpty()) {
            this.m_layerIndex.forEach(new TIntProcedure() {
                @Override
                public boolean execute(final int value) {
                    if (!ArrayUtils.contains(layers, value)) {
                        procedure.execute(LayeredContainer.this.getContainerFromLayer(value));
                    }
                    return true;
                }
            });
        }
    }
    
    public void pushToTop(final Widget w) {
        final int layer = this.getLayer(w);
        if (layer != Integer.MIN_VALUE) {
            final Container c = this.getContainerFromLayer(layer);
            if (c != null) {
                c.getWidgetChildren().remove(w);
                c.getWidgetChildren().add(w);
                c.setNeedsToResetMeshes();
            }
        }
    }
    
    public int getWidgetPositionInLayer(final Widget w) {
        final int layer = this.getLayer(w);
        if (layer != Integer.MIN_VALUE) {
            final Container c = this.getContainerFromLayer(layer);
            if (c != null) {
                return c.getWidgetChildren().indexOf(w);
            }
        }
        return -1;
    }
    
    public void setWidgetPositionInLayer(final Widget w, final int pos) {
        if (pos < 0) {
            LayeredContainer.m_logger.warn((Object)"on essaye de set la position d'un widget dans un layer \u00e0 une position inf\u00e9rieure \u00e0 0");
            return;
        }
        final int layer = this.getLayer(w);
        if (layer != Integer.MIN_VALUE) {
            final Container c = this.getContainerFromLayer(layer);
            if (c != null) {
                final ArrayList<Widget> list = c.getWidgetChildren();
                if (pos >= list.size()) {
                    LayeredContainer.m_logger.warn((Object)"on essaye de set la position d'un widget dans un layer \u00e0 une position trop grande");
                    return;
                }
                list.remove(w);
                list.add(pos, w);
                c.setNeedsToResetMeshes();
            }
        }
    }
    
    public Iterator<Widget> getAllWidgetIterator() {
        return new WidgetIterator();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        final StaticLayout sl = new StaticLayout();
        sl.onCheckOut();
        sl.setAdaptToContentSize(true);
        this.add(sl);
    }
    
    static {
        LayeredContainer.m_logger = Logger.getLogger((Class)LayeredContainer.class);
    }
    
    private class WidgetIterator implements Iterator<Widget>
    {
        private int m_currentLayerIndex;
        private Container m_currentContainer;
        private int m_currentWidgetIndex;
        
        private WidgetIterator() {
            super();
            this.m_currentLayerIndex = -1;
            this.m_currentContainer = null;
            this.m_currentWidgetIndex = -1;
            this.computeState();
        }
        
        private void computeState() {
            while (this.m_currentLayerIndex < LayeredContainer.this.m_layerIndex.size()) {
                if (this.m_currentContainer != null && ++this.m_currentWidgetIndex < this.m_currentContainer.getWidgetChildren().size()) {
                    return;
                }
                ++this.m_currentLayerIndex;
                if (this.m_currentLayerIndex >= LayeredContainer.this.m_layerIndex.size()) {
                    return;
                }
                this.m_currentContainer = LayeredContainer.this.getContainerFromLayer(LayeredContainer.this.m_layerIndex.get(this.m_currentLayerIndex));
                this.m_currentWidgetIndex = -1;
            }
        }
        
        @Override
        public boolean hasNext() {
            return this.m_currentLayerIndex < LayeredContainer.this.m_layerIndex.size();
        }
        
        @Override
        public Widget next() {
            final Widget widget = this.m_currentContainer.getWidgetChildren().get(this.m_currentWidgetIndex);
            this.computeState();
            return widget;
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
