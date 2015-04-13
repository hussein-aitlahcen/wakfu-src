package com.ankamagames.xulor2.component;

import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.event.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.java.util.*;

public class StackList extends Container implements EditableRenderableCollection, ContentClient
{
    public static final String TAG = "StackList";
    private boolean m_horizontal;
    private ItemRendererManager m_rendererManager;
    private final ArrayList<RenderableContainer> m_renderables;
    private RenderableContainer m_selectedRenderable;
    private Object m_selectedValue;
    private int m_selectedOffset;
    private boolean m_enableDND;
    private boolean m_selectionable;
    private boolean m_selectionTogglable;
    private boolean m_innerExpandable;
    private boolean m_innerNonBlocking;
    private int m_clickSoundId;
    private final ArrayList<CollectionContentLoadedListener> m_collectionContentLoadedListeners;
    private boolean m_listContentLoaded;
    private final ArrayList<Object> m_items;
    private String m_contentProperty;
    private ElementMap m_contentPropertyElementMap;
    public static final int INNER_EXPANDABLE;
    public static final int CLICK_SOUND_ID_HASH;
    public static final int INNER_NON_BLOCKING;
    public static final int CONTENT_HASH;
    public static final int HORIZONTAL_HASH;
    public static final int SELECTED_HASH;
    public static final int SELECTED_VALUE_HASH;
    public static final int SELECTIONABLE_HASH;
    public static final int SELECTION_TOGGLABLE_HASH;
    
    public StackList() {
        super();
        this.m_horizontal = true;
        this.m_renderables = new ArrayList<RenderableContainer>();
        this.m_selectedRenderable = null;
        this.m_selectedValue = null;
        this.m_selectedOffset = -1;
        this.m_enableDND = true;
        this.m_selectionable = true;
        this.m_selectionTogglable = true;
        this.m_innerExpandable = true;
        this.m_innerNonBlocking = false;
        this.m_collectionContentLoadedListeners = new ArrayList<CollectionContentLoadedListener>();
        this.m_listContentLoaded = false;
        this.m_items = new ArrayList<Object>();
        this.m_contentProperty = null;
        this.m_contentPropertyElementMap = null;
    }
    
    public void addListContentListener(final CollectionContentLoadedListener listContentLoadedListener) {
        if (listContentLoadedListener != null && !this.m_collectionContentLoadedListeners.contains(listContentLoadedListener)) {
            this.m_collectionContentLoadedListeners.add(listContentLoadedListener);
        }
    }
    
    public void removeListContentLoadListener(final CollectionContentLoadedListener listContentLoadedListener) {
        this.m_collectionContentLoadedListeners.remove(listContentLoadedListener);
    }
    
    @Override
    public void add(final EventDispatcher e) {
        super.add(e);
        if (e instanceof ItemRenderer) {
            this.m_rendererManager.addRenderer((ItemRenderer)e);
        }
    }
    
    @Override
    public void removeValue(final Object value) {
    }
    
    @Override
    public void addValue(final Object value) {
    }
    
    @Override
    public boolean addValue(final int position, final Object value) {
        return false;
    }
    
    @Override
    public void addValue(final Object oldItem, final Object newItem) {
    }
    
    @Override
    public boolean replaceValue(final Object oldItem, final Object newItem) {
        return false;
    }
    
    @Override
    public Widget getWidget(final String type, final int index) {
        if (index >= 0 && index < this.m_renderables.size()) {
            return this.m_renderables.get(index);
        }
        return null;
    }
    
    @Override
    public ArrayList<RenderableContainer> getRenderables() {
        return this.m_renderables;
    }
    
    public boolean getSelectionable() {
        return this.m_selectionable;
    }
    
    public void setSelectionable(final boolean selectionable) {
        this.m_selectionable = selectionable;
    }
    
    public boolean getSelectionTogglable() {
        return this.m_selectionTogglable;
    }
    
    public void setSelectionTogglable(final boolean selectionTogglable) {
        this.m_selectionTogglable = selectionTogglable;
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0, size = this.m_renderables.size(); i < size; ++i) {
            this.m_renderables.get(i).setEnabled(enabled);
        }
    }
    
    @Override
    public void setNetEnabled(final boolean enabled) {
        super.setNetEnabled(enabled);
        for (int i = 0, size = this.m_renderables.size(); i < size; ++i) {
            this.m_renderables.get(i).setNetEnabled(enabled);
        }
    }
    
    public void setEnableDND(final boolean enableDND) {
        this.m_enableDND = enableDND;
    }
    
    public boolean getEnableDND() {
        return this.m_enableDND;
    }
    
    public boolean isHorizontal() {
        return this.m_horizontal;
    }
    
    public void setHorizontal(final boolean horizontal) {
        ((RowLayout)this.m_layout).setHorizontal(horizontal);
        this.m_horizontal = horizontal;
        this.invalidateMinSize();
    }
    
    public boolean isInnerExpandable() {
        return this.m_innerExpandable;
    }
    
    public void setInnerExpandable(final boolean innerExpandable) {
        if (this.m_innerExpandable != innerExpandable) {
            this.m_innerExpandable = innerExpandable;
            for (int i = 0; i < this.m_renderables.size(); ++i) {
                this.m_renderables.get(i).setExpandable(this.m_innerExpandable);
            }
        }
    }
    
    public boolean getInnerNonBlocking() {
        return this.m_innerNonBlocking;
    }
    
    public void setInnerNonBlocking(final boolean innerNonBlocking) {
        if (this.m_innerNonBlocking != innerNonBlocking) {
            this.m_innerNonBlocking = innerNonBlocking;
            for (int i = 0; i < this.m_renderables.size(); ++i) {
                this.m_renderables.get(i).setNonBlocking(this.m_innerNonBlocking);
            }
        }
    }
    
    public int getSelectedOffsetByValue(final Object value) {
        int offset;
        int size;
        for (offset = 0, size = this.m_items.size(); offset < size && this.m_items.get(offset) != value; ++offset) {}
        if (offset == size) {
            return -1;
        }
        return offset;
    }
    
    public Object getSelectedValue() {
        return this.m_selectedValue;
    }
    
    public void setContent(final Object[] content) {
        if (this.m_isATemplate) {
            return;
        }
        final int oldSelectedOffset = this.m_selectedOffset;
        final Object oldSelectedValue = this.getSelectedValue();
        this.m_items.clear();
        if (content != null) {
            this.m_items.ensureCapacity(content.length);
            for (int i = 0; i < content.length; ++i) {
                this.m_items.add(content[i]);
            }
        }
        this.m_selectedOffset = this.getSelectedOffsetByValue(oldSelectedValue);
        if (this.m_selectedOffset == -1 && oldSelectedOffset != -1) {
            this.dispatchEvent(new ListSelectionChangedEvent(this, null, oldSelectedValue, false));
            this.dispatchEvent(new ListSelectionChangedEvent(this, null, null, true));
        }
        this.ensureRenderableContainersListSize(this.m_items.size());
        this.updateValues();
        this.m_listContentLoaded = true;
    }
    
    public void setContent(final Iterable content) {
        if (this.m_isATemplate) {
            return;
        }
        final int oldSelectedOffset = this.m_selectedOffset;
        final Object oldSelectedValue = this.getSelectedValue();
        this.m_items.clear();
        if (content != null) {
            final Iterator it = content.iterator();
            while (it != null && it.hasNext()) {
                final Object o = it.next();
                this.m_items.add(o);
            }
        }
        this.m_selectedOffset = this.getSelectedOffsetByValue(oldSelectedValue);
        if (this.m_selectedOffset == -1 && oldSelectedOffset != -1) {
            this.dispatchEvent(new ListSelectionChangedEvent(this, null, oldSelectedValue, false));
            this.dispatchEvent(new ListSelectionChangedEvent(this, null, null, true));
        }
        this.ensureRenderableContainersListSize(this.m_items.size());
        this.updateValues();
    }
    
    public ArrayList<Object> getItems() {
        return this.m_items;
    }
    
    @Override
    public Object getValue(final int i) {
        if (i >= 0 && i < this.m_items.size()) {
            return this.m_items.get(i);
        }
        return null;
    }
    
    @Override
    public RenderableContainer getSelected() {
        return this.m_selectedRenderable;
    }
    
    @Override
    public int getTableIndex(final RenderableContainer container) {
        return this.m_renderables.indexOf(container);
    }
    
    @Override
    public int getItemIndex(final Object value) {
        return this.m_items.indexOf(value);
    }
    
    @Override
    public void setContentProperty(final String propertyName, final ElementMap elementMap) {
        this.m_contentProperty = propertyName;
        this.m_contentPropertyElementMap = elementMap;
    }
    
    public int getOffsetByRenderable(final RenderableContainer selected) {
        return this.m_renderables.indexOf(selected);
    }
    
    public void setSelected(final Item item) {
        this.m_selectedOffset = this.m_items.indexOf(item);
        this.updateSelectedAppearance();
    }
    
    public int getClickSoundId() {
        return this.m_clickSoundId;
    }
    
    public void setClickSoundId(final int clickSoundId) {
        this.m_clickSoundId = clickSoundId;
    }
    
    public int getSelectedOffset() {
        return this.m_selectedOffset;
    }
    
    public void setSelectedValue(final Object value) {
        if (this.m_items == null) {
            return;
        }
        final int oldSelection = this.m_selectedOffset;
        this.m_selectedOffset = -1;
        for (int i = 0; i < this.m_items.size(); ++i) {
            if (this.m_items.get(i) == value) {
                this.m_selectedOffset = i;
                break;
            }
        }
        if (oldSelection != this.m_selectedOffset) {
            if (oldSelection != -1) {
                this.dispatchEvent(new ListSelectionChangedEvent(this, this.m_renderables.get(this.m_selectedOffset), this.m_items.get(oldSelection), false));
            }
            if (this.m_selectedOffset != -1) {
                this.dispatchEvent(new ListSelectionChangedEvent(this, this.m_renderables.get(this.m_selectedOffset), this.m_items.get(this.m_selectedOffset), true));
            }
            this.updateSelectedAppearance();
        }
    }
    
    @Override
    protected void processEventForSound(final Event e, final boolean up) {
        if (!e.isSoundConsumed() && (e.getType() == Events.ITEM_CLICK || e.getType() == Events.ITEM_DOUBLE_CLICK)) {
            e.setSoundConsumed(true);
            switch (this.m_clickSoundId) {
                case -1: {
                    XulorSoundManager.getInstance().click();
                    break;
                }
                case -2: {
                    XulorSoundManager.getInstance().tabClick();
                    break;
                }
                default: {
                    XulorSoundManager.getInstance().playSound(this.m_clickSoundId);
                    break;
                }
            }
        }
    }
    
    private void updateValues() {
        if (this.m_renderables == null) {
            return;
        }
        this.m_selectedRenderable = null;
        boolean isSelectedDisplayed = false;
        for (int size = this.m_renderables.size(), i = 0; i < size; ++i) {
            final RenderableContainer renderable = this.m_renderables.get(i);
            renderable.setContentProperty(this.m_contentProperty + "#" + i, this.m_contentPropertyElementMap);
            if (this.m_items != null && i < this.m_items.size()) {
                if (i == this.m_selectedOffset && !isSelectedDisplayed) {
                    isSelectedDisplayed = true;
                    this.m_selectedRenderable = renderable;
                }
                renderable.setContent(this.m_items.get(i));
            }
            else {
                renderable.setContent(null);
            }
        }
        if (!isSelectedDisplayed) {
            this.m_selectedRenderable = null;
        }
    }
    
    private void updateSelectedAppearance() {
        this.m_selectedRenderable = this.m_renderables.get(this.m_selectedOffset);
    }
    
    private void fireSelectionChanged(final RenderableContainer selected) {
        final RenderableContainer old = this.m_selectedRenderable;
        if (selected == this.m_selectedRenderable) {
            return;
        }
        final Object oldValue = this.getSelectedValue();
        this.m_selectedRenderable = selected;
        if (this.m_selectedRenderable != null) {
            this.m_selectedOffset = this.getOffsetByRenderable(this.m_selectedRenderable);
        }
        else {
            this.m_selectedOffset = -1;
        }
        Object selectedValue;
        if (this.m_selectedOffset == -1) {
            selectedValue = null;
        }
        else {
            selectedValue = this.m_items.get(this.m_selectedOffset);
        }
        if (old != null) {
            final ListSelectionChangedEvent e = new ListSelectionChangedEvent(this);
            e.setRenderableContainer(old);
            e.setSelected(false);
            e.setValue(oldValue);
            this.dispatchEvent(e);
        }
        if (this.m_selectedRenderable != null) {
            final ListSelectionChangedEvent e = new ListSelectionChangedEvent(this);
            e.setRenderableContainer(this.m_selectedRenderable);
            e.setSelected(true);
            e.setValue(selectedValue);
            this.dispatchEvent(e);
        }
    }
    
    private void ensureRenderableContainersListSize(final int size) {
        for (int i = this.m_renderables.size(); i < size; ++i) {
            final RenderableContainer container = new RenderableContainer();
            container.onCheckOut();
            container.setCollection(this);
            container.setNonBlocking(this.m_nonBlocking);
            container.setRendererManager(this.m_rendererManager);
            container.setEnableDND(this.m_enableDND);
            container.setEnabled(this.m_enabled);
            container.setNetEnabled(this.m_netEnabled);
            container.setExpandable(this.m_innerExpandable);
            container.setNonBlocking(this.m_innerNonBlocking);
            container.addEventListener(Events.MOUSE_CLICKED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    if (StackList.this.m_selectionable) {
                        RenderableContainer selected;
                        if (StackList.this.m_selectionTogglable && StackList.this.m_selectedRenderable == event.getCurrentTarget()) {
                            selected = null;
                        }
                        else {
                            selected = event.getCurrentTarget();
                        }
                        StackList.this.fireSelectionChanged(selected);
                    }
                    return false;
                }
            }, false);
            this.m_renderables.add(container);
            this.add(container);
        }
        final int renderablesSize = this.m_renderables.size();
        for (int j = renderablesSize - 1; j >= size; --j) {
            this.m_renderables.remove(j).destroySelfFromParent();
        }
    }
    
    @Override
    public int size() {
        return (this.m_items != null) ? this.m_items.size() : 0;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_contentProperty = null;
        this.m_contentPropertyElementMap = null;
        this.m_selectedRenderable = null;
        this.m_renderables.clear();
        this.m_items.clear();
        this.m_selectedValue = null;
        this.m_contentProperty = null;
        this.m_contentPropertyElementMap = null;
        this.m_rendererManager = null;
        this.m_collectionContentLoadedListeners.clear();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_nonBlocking = false;
        this.m_listContentLoaded = false;
        final ListAppearance app = new ListAppearance();
        app.onCheckOut();
        app.setWidget(this);
        this.add(app);
        this.m_rendererManager = new ItemRendererManager();
        this.m_selectedOffset = -1;
        this.m_enableDND = true;
        this.m_needsScissor = true;
        this.m_clickSoundId = -1;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        if (this.m_listContentLoaded) {
            for (int i = this.m_collectionContentLoadedListeners.size() - 1; i >= 0; --i) {
                this.m_collectionContentLoadedListeners.get(i).onContentLoaded();
            }
            this.m_listContentLoaded = false;
        }
        return ret;
    }
    
    @Override
    public void copyElement(final BasicElement c) {
        final StackList l = (StackList)c;
        super.copyElement(c);
        l.setHorizontal(this.m_horizontal);
        l.setEnableDND(this.m_enableDND);
        l.setInnerExpandable(this.m_innerExpandable);
        l.setInnerNonBlocking(this.m_innerNonBlocking);
        l.setClickSoundId(this.m_clickSoundId);
        for (int i = l.m_widgetChildren.size() - 1; i >= 0; --i) {
            final Widget w = l.m_widgetChildren.get(i);
            w.destroySelfFromParent();
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == StackList.INNER_EXPANDABLE) {
            this.setInnerExpandable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == StackList.CLICK_SOUND_ID_HASH) {
            this.setClickSoundId(PrimitiveConverter.getInteger(value));
        }
        else if (hash == StackList.INNER_NON_BLOCKING) {
            this.setInnerNonBlocking(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == StackList.HORIZONTAL_HASH) {
            this.setHorizontal(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == StackList.SELECTIONABLE_HASH) {
            this.setSelectionable(PrimitiveConverter.getBoolean(value));
        }
        else {
            if (hash != StackList.SELECTION_TOGGLABLE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setSelectionTogglable(PrimitiveConverter.getBoolean(value));
        }
        return true;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (hash == StackList.INNER_EXPANDABLE) {
            this.setInnerExpandable(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == StackList.CLICK_SOUND_ID_HASH) {
            this.setClickSoundId(PrimitiveConverter.getInteger(value));
        }
        else if (hash == StackList.INNER_NON_BLOCKING) {
            this.setInnerNonBlocking(PrimitiveConverter.getBoolean(value));
        }
        else if (hash == StackList.CONTENT_HASH) {
            if (value == null || value.getClass().isArray()) {
                this.setContent((Object[])value);
            }
            else {
                if (!(value instanceof Iterable)) {
                    return false;
                }
                this.setContent((Iterable)value);
            }
        }
        else if (hash == StackList.SELECTED_HASH) {
            this.setSelected((Item)value);
        }
        else {
            if (hash != StackList.SELECTED_VALUE_HASH) {
                return super.setPropertyAttribute(hash, value);
            }
            this.setSelectedValue(value);
        }
        return true;
    }
    
    static {
        INNER_EXPANDABLE = "innerExpandable".hashCode();
        CLICK_SOUND_ID_HASH = "clickSoundId".hashCode();
        INNER_NON_BLOCKING = "innerNonBlocking".hashCode();
        CONTENT_HASH = "content".hashCode();
        HORIZONTAL_HASH = "horizontal".hashCode();
        SELECTED_HASH = "selected".hashCode();
        SELECTED_VALUE_HASH = "selectedValue".hashCode();
        SELECTIONABLE_HASH = "selectionable".hashCode();
        SELECTION_TOGGLABLE_HASH = "selectionTogglable".hashCode();
    }
}
