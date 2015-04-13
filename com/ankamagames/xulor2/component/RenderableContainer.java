package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.core.dragndrop.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.core.taglibrary.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class RenderableContainer extends Container implements ContentClient
{
    public static final String TAG = "renderableContainer";
    private EventDispatcher[] m_evaluatedElements;
    private ArrayList<ItemElement> m_itemElements;
    private final ArrayList<EventDispatcher> m_renderedChildren;
    private Item m_item;
    private ItemRenderer m_renderer;
    private ItemRendererManager m_rendererManager;
    private EditableRenderableCollection m_collection;
    private ElementMap m_innerElementMap;
    private DragNDropContainer m_dragNDropable;
    private boolean m_enableDND;
    private boolean m_itemNeedToBeApplied;
    private ItemDragNDropHandler m_dragNDropListener;
    private EventListener m_doubleClickListener;
    private String m_contentProperty;
    private ElementMap m_contentPropertyElementMap;
    public static final int CONTENT_HASH;
    public static final int ENABLE_DND_HASH;
    
    public RenderableContainer() {
        this(null);
    }
    
    public RenderableContainer(final EditableRenderableCollection collection) {
        super();
        this.m_renderedChildren = new ArrayList<EventDispatcher>();
        this.m_item = null;
        this.m_renderer = null;
        this.m_rendererManager = new ItemRendererManager();
        this.m_collection = null;
        this.m_innerElementMap = null;
        this.m_enableDND = true;
        this.m_itemNeedToBeApplied = false;
        this.m_dragNDropListener = null;
        this.m_doubleClickListener = null;
        this.m_contentProperty = null;
        this.m_contentPropertyElementMap = null;
        this.m_collection = collection;
        this.setNonBlocking(false);
    }
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
        if (e instanceof ItemRenderer) {
            this.m_rendererManager.addRenderer((ItemRenderer)e);
        }
    }
    
    private void addDragNDropListener() {
        if (this.m_dragNDropListener != null) {
            DragNDropManager.getInstance().removeDragNDropListener(this.m_dragNDropListener, true);
        }
        this.m_dragNDropListener = new ItemDragNDropHandler(this);
        DragNDropManager.getInstance().addDragNDropListener(this.m_dragNDropListener);
    }
    
    public void addRenderedChild(final EventDispatcher e) {
        this.m_renderedChildren.add(e);
        this.add(e);
    }
    
    @Override
    public void addEventListener(final Events type, final EventListener listener, final boolean before) {
        super.addEventListener(type, listener, before);
        if (type == Events.ITEM_DOUBLE_CLICK && this.m_doubleClickListener == null) {
            this.m_doubleClickListener = new EventListener() {
                @Override
                public boolean run(final Event event) {
                    RenderableContainer.this.mouseDoubleClicked((MouseEvent)event);
                    return false;
                }
            };
            super.addEventListener(Events.MOUSE_DOUBLE_CLICKED, this.m_doubleClickListener, false);
        }
    }
    
    @Override
    public void removeEventListener(final Events type, final EventListener listener, final boolean before) {
        super.removeEventListener(type, listener, before);
        if (type == Events.ITEM_DOUBLE_CLICK && this.hasListener(type)) {
            this.removeEventListener(Events.MOUSE_DOUBLE_CLICKED, this.m_doubleClickListener, false);
            this.m_doubleClickListener = null;
        }
    }
    
    @Override
    public String getTag() {
        return "renderableContainer";
    }
    
    public void setRenderableChildren(final EventDispatcher[] elements) {
        this.m_evaluatedElements = elements;
    }
    
    public void setItemElements(final ArrayList<ItemElement> elements) {
        this.m_itemElements = elements;
    }
    
    public void setRenderer(final ItemRenderer renderer) {
        if (renderer != this.m_renderer) {
            if (this.m_renderer != null) {
                this.m_renderer.removeListeners(this);
            }
            this.m_renderer = renderer;
            for (int i = this.m_renderedChildren.size() - 1; i >= 0; --i) {
                this.destroy(this.m_renderedChildren.get(i));
            }
            this.m_renderedChildren.clear();
        }
    }
    
    public boolean getEnableDND() {
        return this.m_enableDND;
    }
    
    public void setEnableDND(final boolean enableDND) {
        this.m_enableDND = enableDND;
    }
    
    public boolean canBeDnD() {
        return this.m_enableDND && this.m_dragNDropable.isEnabledFull();
    }
    
    public ItemRendererManager getRendererManager() {
        return this.m_rendererManager;
    }
    
    public void setRendererManager(final ItemRendererManager manager) {
        if (manager != null && this.m_rendererManager != manager) {
            if (this.m_rendererManager != null) {
                this.m_rendererManager.removeRenderable(this);
            }
            (this.m_rendererManager = manager).addRenderable(this);
        }
    }
    
    @Override
    public void setContentProperty(final String propertyName, final ElementMap map) {
        this.m_contentProperty = propertyName;
        this.m_contentPropertyElementMap = map;
    }
    
    public void mouseEntered(final MouseEvent event) {
        final Object value = (this.m_item == null) ? null : this.m_item.getValue();
        final ItemEvent ioe = ItemEvent.checkOut(event, this, Events.ITEM_OVER, value);
        this.dispatchEvent(ioe);
        event.setSoundConsumed(ioe.isSoundConsumed());
    }
    
    public void mouseExited(final MouseEvent event) {
        final Object value = (this.m_item == null) ? null : this.m_item.getValue();
        final ItemEvent ioe = ItemEvent.checkOut(event, this, Events.ITEM_OUT, value);
        this.dispatchEvent(ioe);
        event.setSoundConsumed(ioe.isSoundConsumed());
    }
    
    public void mouseClicked(final MouseEvent event) {
        final Object value = (this.m_item == null) ? null : this.m_item.getValue();
        final ItemEvent ioe = ItemEvent.checkOut(event, this, Events.ITEM_CLICK, value);
        this.dispatchEvent(ioe);
        event.setSoundConsumed(ioe.isSoundConsumed());
    }
    
    public void mouseDoubleClicked(final MouseEvent event) {
        final Object value = (this.m_item == null) ? null : this.m_item.getValue();
        final ItemEvent ioe = ItemEvent.checkOut(event, this, Events.ITEM_DOUBLE_CLICK, value);
        this.dispatchEvent(ioe);
        event.setSoundConsumed(ioe.isSoundConsumed());
    }
    
    public void applyItem() {
        if (this.m_renderer != null && this.m_evaluatedElements != null && this.m_evaluatedElements.length != 0) {
            this.m_renderer.applyItemValue(this.m_evaluatedElements, this.m_item);
        }
        this.m_itemNeedToBeApplied = false;
    }
    
    public EditableRenderableCollection getCollection() {
        return this.m_collection;
    }
    
    public void setCollection(final EditableRenderableCollection collection) {
        this.m_collection = collection;
    }
    
    public void setInnerElementMap(final ElementMap map) {
        this.m_innerElementMap = map;
    }
    
    public ElementMap getInnerElementMap() {
        return this.m_innerElementMap;
    }
    
    public void setContent(final Object value) {
        if (this.m_isATemplate) {
            return;
        }
        if (this.m_item == null || this.m_item.getValue() != value) {
            this.setItemValue(value);
        }
        this.m_itemNeedToBeApplied = true;
    }
    
    public void setItem(final Item item) {
        this.setItem(item, false);
    }
    
    public void setItem(final Item item, final boolean now) {
        if (this.m_item != item) {
            this.unregisterFromItem(this.m_item);
            Item.checkIn(this.m_item);
            this.m_item = item;
            this.updateRenderer(true, now);
        }
    }
    
    public void invalidateRenderer() {
        this.updateRenderer(true, false);
    }
    
    public void updateRenderer(boolean applyItem, boolean now) {
        now = false;
        if (this.m_rendererManager == null) {
            return;
        }
        if (this.m_rendererManager.assign(this)) {
            this.render();
            applyItem = true;
        }
        if (this.m_itemElements != null && this.m_item != null && this.m_item.getVirtualProperty() != null) {
            final Property p = this.m_item.getVirtualProperty();
            if (p.getValue() instanceof FieldProvider) {
                for (int i = 0, size = this.m_itemElements.size(); i < size; ++i) {
                    final ItemElement ie = this.m_itemElements.get(i);
                    EventDispatcher toAdd;
                    if (ie.getParent() != null) {
                        toAdd = ie.getParent();
                    }
                    else {
                        toAdd = ie;
                    }
                    if (ie.getField() != null && ie.getField().contains("/")) {
                        final ObjectPair<Object, String> pair = Property.getFieldProviderAndField(p.getValue(), ie.getField());
                        final String field = (pair.getSecond() == null) ? ie.getField() : ie.getField().substring(0, ie.getField().length() - pair.getSecond().length() - 1);
                        final String propertyName = p.getName() + "/" + field;
                        Property prop = PropertiesProvider.getInstance().getProperty(propertyName, this.m_contentPropertyElementMap);
                        if (prop == null) {
                            prop = new Property(propertyName, p, field, this.m_contentPropertyElementMap);
                            prop.setValue(pair.getFirst(), false);
                            PropertiesProvider.getInstance().addProperty(prop);
                        }
                        prop.addPropertyClient(new PropertyClientData<BasicElement>(toAdd, XulorTagLibrary.getInstance().getFactory(toAdd.getTag()), ie.getAttribute(), pair.getSecond(), ie.getResultProvider()));
                    }
                    else {
                        p.addPropertyClient(new PropertyClientData<BasicElement>(toAdd, XulorTagLibrary.getInstance().getFactory(toAdd.getTag()), ie.getAttribute(), ie.getField(), ie.getResultProvider()));
                    }
                }
            }
        }
        if (applyItem) {
            if (now) {
                this.applyItem();
            }
            else {
                this.m_itemNeedToBeApplied = true;
            }
        }
    }
    
    public EditableRenderableCollection getRenderableCollection() {
        return this.m_collection;
    }
    
    public void setItemValue(final Object value) {
        final Item item = Item.getItem(value, this.m_contentProperty, this.m_contentPropertyElementMap, this);
        this.setItem(item);
    }
    
    public Object getItemValue() {
        if (this.m_item != null) {
            return this.m_item.getValue();
        }
        return null;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    public ItemRenderer getRenderer() {
        return this.m_renderer;
    }
    
    public void render() {
        if (this.m_widgetChildren != null && this.m_widgetChildren.size() == 0 && this.m_renderer != null) {
            this.m_renderer.render(this);
            TreeDepthManager.getInstance().askForAnotherProcess();
        }
    }
    
    public DragNDropContainer getDragNDropable() {
        return this.m_dragNDropable;
    }
    
    public void setDragNDropable(final DragNDropContainer dnd) {
        if (this.m_dragNDropable == null) {
            this.addDragNDropListener();
        }
        this.m_dragNDropable = dnd;
    }
    
    public void addRenderableContainerListeners() {
        this.addEventListener(Events.MOUSE_ENTERED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                RenderableContainer.this.mouseEntered((MouseEvent)event);
                return false;
            }
        }, false);
        this.addEventListener(Events.MOUSE_EXITED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                RenderableContainer.this.mouseExited((MouseEvent)event);
                return false;
            }
        }, false);
        this.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                RenderableContainer.this.mouseClicked((MouseEvent)event);
                return false;
            }
        }, false);
    }
    
    public void unregisterFromCurrentItem() {
        this.unregisterFromItem(this.m_item);
    }
    
    private void unregisterFromItem(final Item item) {
        if (item != null && item.getVirtualProperty() != null && this.m_itemElements != null) {
            final Property p = item.getVirtualProperty();
            if (p.getValue() instanceof FieldProvider) {
                for (final ItemElement ie : this.m_itemElements) {
                    EventDispatcher toRemove;
                    if (ie.getParent() != null) {
                        toRemove = ie.getParent();
                    }
                    else {
                        toRemove = ie;
                    }
                    if (ie.getField() != null && ie.getField().contains("/")) {
                        final ObjectPair<Object, String> result = Property.getFieldProviderAndField(p.getValue(), ie.getField());
                        final String endField = result.getSecond();
                        final int endFieldLength = (endField != null) ? endField.length() : 0;
                        final String field = ie.getField().substring(0, ie.getField().length() - endFieldLength - 1);
                        final String propertyName = p.getName() + "/" + field;
                        final Property prop = PropertiesProvider.getInstance().getProperty(propertyName, this.m_contentPropertyElementMap);
                        if (prop == null) {
                            continue;
                        }
                        prop.removePropertyClient(toRemove);
                    }
                    else {
                        p.removePropertyClient(toRemove);
                    }
                }
            }
        }
    }
    
    public void invalidateItemValue() {
        this.m_itemNeedToBeApplied = true;
    }
    
    public boolean processSetItem() {
        boolean ret = false;
        if (this.m_renderer == null) {
            this.updateRenderer(true, false);
            ret = true;
        }
        if (this.m_itemNeedToBeApplied) {
            this.applyItem();
            ret = true;
        }
        return ret;
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        final RenderableContainer rc = (RenderableContainer)c;
        super.copyElement(c);
        rc.m_enableDND = this.m_enableDND;
        for (int i = rc.m_widgetChildren.size() - 1; i >= 0; --i) {
            rc.m_widgetChildren.get(i).destroySelfFromParent();
        }
        if (this.m_doubleClickListener != null) {
            rc.m_doubleClickListener = new EventListener() {
                @Override
                public boolean run(final Event event) {
                    rc.mouseDoubleClicked((MouseEvent)event);
                    return false;
                }
            };
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        if (this.m_dragNDropListener != null) {
            try {
                DragNDropManager.getInstance().removeDragNDropListener(this.m_dragNDropListener, true);
            }
            catch (Exception ex) {
                RenderableContainer.m_logger.error((Object)"", (Throwable)ex);
            }
        }
        this.m_dragNDropListener = null;
        RenderableContainerManager.getInstance().removeRenderableContainer(this);
        this.m_renderedChildren.clear();
        this.m_collection = null;
        this.m_dragNDropable = null;
        this.m_evaluatedElements = null;
        if (this.m_item != null) {
            this.unregisterFromItem(this.m_item);
            Item.checkIn(this.m_item);
            this.m_item = null;
        }
        if (this.m_itemElements != null) {
            this.m_itemElements.clear();
            this.m_itemElements = null;
        }
        this.m_contentProperty = null;
        this.m_contentPropertyElementMap = null;
        this.m_innerElementMap = null;
        this.m_renderer = null;
        if (this.m_rendererManager != null) {
            this.m_rendererManager.removeRenderable(this);
            this.m_rendererManager = null;
        }
    }
    
    @Override
    public void onCheckOut() {
        RenderableContainerManager.getInstance().addRenderableContainer(this);
        super.onCheckOut();
        this.addRenderableContainerListeners();
        this.m_enableDND = true;
        this.m_nonBlocking = false;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == RenderableContainer.ENABLE_DND_HASH) {
            this.setEnableDND(PrimitiveConverter.getBoolean(value));
            return true;
        }
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == RenderableContainer.CONTENT_HASH) {
            this.setContent(value);
        }
        else {
            if (hash != RenderableContainer.ENABLE_DND_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setEnableDND(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    static {
        CONTENT_HASH = "content".hashCode();
        ENABLE_DND_HASH = "enableDND".hashCode();
    }
}
