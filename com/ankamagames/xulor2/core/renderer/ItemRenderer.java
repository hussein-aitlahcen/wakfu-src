package com.ankamagames.xulor2.core.renderer;

import org.apache.log4j.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.event.*;
import java.util.*;
import com.ankamagames.xulor2.core.converter.*;

public class ItemRenderer extends NonGraphicalElement
{
    public static final String TAG = "ItemRenderer";
    private static final Logger m_logger;
    private ItemRendererManager m_manager;
    private ArrayList<EventDispatcher> m_template;
    private DragNDropContainer m_dragNDropable;
    private ResultProvider m_condition;
    private ArrayList<PropertyElement> m_properties;
    private ArrayList<AbstractEventListener> m_listeners;
    private static final ArrayList<EventDispatcher> itemElements;
    public static final int ON_ACTIVATION_HASH;
    public static final int ON_CLICK_HASH;
    public static final int ON_DOUBLE_CLICK_HASH;
    public static final int ON_FOCUS_CHANGE_HASH;
    public static final int ON_ITEM_CLICK_HASH;
    public static final int ON_ITEM_DOUBLE_CLICK_HASH;
    public static final int ON_ITEM_OUT_HASH;
    public static final int ON_ITEM_OVER_HASH;
    public static final int ON_KEY_PRESS_HASH;
    public static final int ON_KEY_RELEASE_HASH;
    public static final int ON_KEY_TYPE_HASH;
    public static final int ON_LIST_SELECTION_CHANGE_HASH;
    public static final int ON_MOUSE_DRAG_HASH;
    public static final int ON_MOUSE_DRAG_IN_HASH;
    public static final int ON_MOUSE_DRAG_OUT_HASH;
    public static final int ON_MOUSE_ENTER_HASH;
    public static final int ON_MOUSE_EXIT_HASH;
    public static final int ON_MOUSE_MOVE_HASH;
    public static final int ON_MOUSE_PRESS_HASH;
    public static final int ON_MOUSE_RELEASE_HASH;
    public static final int ON_MOUSE_WHEEL_HASH;
    public static final int ON_SELECTION_CHANGE_HASH;
    public static final int ON_SLIDER_MOVE_HASH;
    public static final int ON_DRAG_HASH;
    public static final int ON_DROP_HASH;
    public static final int ON_DRAG_OUT_HASH;
    public static final int ON_DROP_OUT_HASH;
    public static final int ON_DRAG_OVER_HASH;
    public static final int ON_POPUP_DISPLAY_HASH;
    public static final int ON_POPUP_HIDE_HASH;
    
    public ItemRenderer() {
        super();
        this.m_manager = null;
        this.m_template = new ArrayList<EventDispatcher>();
        this.m_dragNDropable = null;
        this.m_properties = new ArrayList<PropertyElement>();
        this.m_listeners = new ArrayList<AbstractEventListener>();
    }
    
    public void addListeners(final RenderableContainer m) {
        for (final AbstractEventListener el : this.m_listeners) {
            m.addEventListener(el.getType(), el, false);
        }
    }
    
    public void removeListeners(final RenderableContainer r) {
        for (final AbstractEventListener el : this.m_listeners) {
            r.removeEventListener(el.getType(), el, false);
        }
    }
    
    @Override
    public String getTag() {
        return "ItemRenderer";
    }
    
    public ItemRendererManager getManager() {
        return this.m_manager;
    }
    
    public void setManager(final ItemRendererManager manager) {
        this.m_manager = manager;
    }
    
    public void setOnActivation(final ActivationChangedListener al) {
        this.m_listeners.add(al);
    }
    
    public void setOnClick(final MouseClickedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnDoubleClick(final MouseDoubleClickedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnFocusChange(final FocusChangedListener fl) {
        this.m_listeners.add(fl);
    }
    
    public void setOnKeyPress(final KeyPressedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnKeyRelease(final KeyReleasedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnKeyType(final KeyTypedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnListSelectionChange(final ListSelectionChangedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnMouseDrag(final MouseDraggedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnMouseDragIn(final MouseDraggedInListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnMouseDragOut(final MouseDraggedOutListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnMouseEnter(final MouseEnteredListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnMouseExit(final MouseExitedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnMouseMove(final MouseMovedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnMousePress(final MousePressedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnMouseRelease(final MouseReleasedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnMouseWheel(final MouseWheeledListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnItemOut(final ItemOutListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnItemOver(final ItemOverListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnItemClick(final ItemClickListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnItemDoubleClick(final ItemDoubleClickListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnDrag(final DragListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnDrop(final DropListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnDropOut(final DropOutListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnDragOut(final DragOutListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnDragOver(final DragOverListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnSliderMove(final SliderMovedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnSelectionChange(final SelectionChangedListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnPopupDisplay(final PopupDisplayListener l) {
        this.m_listeners.add(l);
    }
    
    public void setOnPopupHide(final PopupHideListener l) {
        this.m_listeners.add(l);
    }
    
    @Override
    public void add(final EventDispatcher element) {
        boolean add = true;
        if (element instanceof ResultProvider) {
            this.m_condition = (ResultProvider)element;
        }
        else if (element instanceof PropertyElement) {
            this.m_properties.add((PropertyElement)element);
        }
        else {
            if (element instanceof Widget) {
                ((Widget)element).setVisible(false);
            }
            this.m_template.add(element);
            element.setIsATemplate(true);
            add = false;
        }
        if (add) {
            super.add(element, false);
        }
    }
    
    public boolean isRenderableCompatible(final RenderableContainer renderable) {
        if (this.m_condition != null) {
            final Object value = this.m_condition.getResult(renderable);
            if (value instanceof Boolean) {
                return (boolean)value;
            }
        }
        return true;
    }
    
    public void render(final RenderableContainer renderable) {
        this.m_dragNDropable = null;
        final ArrayList<ItemElement> items = new ArrayList<ItemElement>();
        final ElementMap map = new ElementMap(null, this.m_elementMap.getEnvironment());
        map.setParentElementMap(this.m_elementMap);
        for (final PropertyElement propertyElement : this.m_properties) {
            propertyElement.createProperty();
            final Property property = propertyElement.getProperty();
            if (property != null) {
                property.removePropertyClient(renderable);
            }
            propertyElement.addPropertyClient(renderable);
        }
        this.addListeners(renderable);
        for (int i = 0, size = this.m_template.size(); i < size; ++i) {
            final EventDispatcher element = this.m_template.get(i).cloneElementStructure();
            if (element instanceof Widget) {
                ((Widget)element).setVisible(true);
            }
            this.addElementAndItem(element, ItemRenderer.itemElements, items, renderable, null, map);
            renderable.addRenderedChild(element);
        }
        if (this.m_dragNDropable != null) {
            renderable.setDragNDropable(this.m_dragNDropable);
        }
        renderable.setInnerElementMap(map);
        renderable.setRenderableChildren(ItemRenderer.itemElements.toArray(new EventDispatcher[ItemRenderer.itemElements.size()]));
        renderable.setItemElements(items);
        ItemRenderer.itemElements.clear();
    }
    
    private void addElementAndItem(final EventDispatcher element, final ArrayList<EventDispatcher> itemElements, final ArrayList<ItemElement> items, final RenderableContainer renderable, DragNDropContainer dnd, final ElementMap map) {
        element.setElementMap(map);
        if (element.getId() != null) {
            map.add(element.getId(), element);
        }
        if (element instanceof ItemElement && element.getParentOfType(ItemRenderer.class) == null) {
            items.add((ItemElement)element);
            EventDispatcher toAdd;
            if (element.getParent() != null) {
                toAdd = element.getParent();
            }
            else {
                toAdd = renderable;
            }
            if (!itemElements.contains(toAdd)) {
                itemElements.add(toAdd);
            }
        }
        if (element instanceof Widget) {
            element.setRenderableParent(renderable);
            if (element instanceof DragNDropContainer) {
                dnd = (this.m_dragNDropable = (DragNDropContainer)element);
                ((Widget)element).setDragAndDropParent(dnd);
            }
        }
        for (final EventDispatcher child : element.getChildren()) {
            this.addElementAndItem(child, itemElements, items, renderable, dnd, map);
        }
    }
    
    private void applyValue(final EventDispatcher element, final String attribute, final int attributeHash, final Item item, final String field, final ResultProvider resultProvider) {
        if (element == null || attribute == null) {
            return;
        }
        try {
            MethodUtil.castInvokeWithItem(attribute, element, item, attributeHash, field, resultProvider);
        }
        catch (Exception e) {
            ItemRenderer.m_logger.error((Object)("Erreur \u00e0 l'invoke method=" + attribute), (Throwable)e);
        }
    }
    
    public void applyItemValue(final EventDispatcher[] elements, final Item item) {
        if (elements == null) {
            return;
        }
        for (final EventDispatcher element : elements) {
            final ArrayList<EventDispatcher> children = element.getChildren();
            final EventDispatcher[] list = children.toArray(new EventDispatcher[children.size()]);
            element.prepareRender();
            for (final EventDispatcher child : list) {
                if (child instanceof ItemElement) {
                    final ItemElement itemElement = (ItemElement)child;
                    final String field = (item != null) ? itemElement.getField() : null;
                    this.applyValue(element, itemElement.getAttribute(), itemElement.getAttributeHash(), item, field, itemElement.getResultProvider());
                }
            }
        }
    }
    
    @Override
    public boolean runBeforeListeners(final Event event) {
        return false;
    }
    
    @Override
    public boolean runAfterListeners(final Event event) {
        return false;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_condition = null;
        this.m_dragNDropable = null;
        this.m_listeners.clear();
        this.m_listeners = null;
        this.m_properties.clear();
        this.m_properties = null;
        if (this.m_template != null) {
            for (int i = this.m_template.size() - 1; i >= 0; --i) {
                this.m_template.get(i).release();
            }
            this.m_template.clear();
            this.m_template = null;
        }
        this.m_manager = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        super.copyElement(source);
        final ItemRenderer r = (ItemRenderer)source;
        r.m_listeners.addAll(this.m_listeners);
        for (int i = 0; i < this.m_template.size(); ++i) {
            r.m_template.add(this.m_template.get(i).cloneElementStructure());
        }
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (hash == ItemRenderer.ON_ACTIVATION_HASH) {
            this.setOnActivation(cl.convert(ActivationChangedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_CLICK_HASH) {
            this.setOnClick(cl.convert(MouseClickedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_DOUBLE_CLICK_HASH) {
            this.setOnDoubleClick(cl.convert(MouseDoubleClickedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_FOCUS_CHANGE_HASH) {
            this.setOnFocusChange(cl.convert(FocusChangedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_ITEM_CLICK_HASH) {
            this.setOnItemClick(cl.convert(ItemClickListener.class, value));
        }
        else if (hash == ItemRenderer.ON_ITEM_DOUBLE_CLICK_HASH) {
            this.setOnItemDoubleClick(cl.convert(ItemDoubleClickListener.class, value));
        }
        else if (hash == ItemRenderer.ON_ITEM_OUT_HASH) {
            this.setOnItemOut(cl.convert(ItemOutListener.class, value));
        }
        else if (hash == ItemRenderer.ON_ITEM_OVER_HASH) {
            this.setOnItemOver(cl.convert(ItemOverListener.class, value));
        }
        else if (hash == ItemRenderer.ON_DRAG_HASH) {
            this.setOnDrag(cl.convert(DragListener.class, value));
        }
        else if (hash == ItemRenderer.ON_DROP_HASH) {
            this.setOnDrop(cl.convert(DropListener.class, value));
        }
        else if (hash == ItemRenderer.ON_DRAG_OUT_HASH) {
            this.setOnDragOut(cl.convert(DragOutListener.class, value));
        }
        else if (hash == ItemRenderer.ON_DROP_OUT_HASH) {
            this.setOnDropOut(cl.convert(DropOutListener.class, value));
        }
        else if (hash == ItemRenderer.ON_DRAG_OVER_HASH) {
            this.setOnDragOver(cl.convert(DragOverListener.class, value));
        }
        else if (hash == ItemRenderer.ON_KEY_PRESS_HASH) {
            this.setOnKeyPress(cl.convert(KeyPressedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_KEY_RELEASE_HASH) {
            this.setOnKeyRelease(cl.convert(KeyReleasedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_KEY_TYPE_HASH) {
            this.setOnKeyType(cl.convert(KeyTypedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_LIST_SELECTION_CHANGE_HASH) {
            this.setOnListSelectionChange(cl.convert(ListSelectionChangedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_MOUSE_DRAG_HASH) {
            this.setOnMouseDrag(cl.convert(MouseDraggedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_MOUSE_DRAG_IN_HASH) {
            this.setOnMouseDragIn(cl.convert(MouseDraggedInListener.class, value));
        }
        else if (hash == ItemRenderer.ON_MOUSE_DRAG_OUT_HASH) {
            this.setOnMouseDragOut(cl.convert(MouseDraggedOutListener.class, value));
        }
        else if (hash == ItemRenderer.ON_MOUSE_ENTER_HASH) {
            this.setOnMouseEnter(cl.convert(MouseEnteredListener.class, value));
        }
        else if (hash == ItemRenderer.ON_MOUSE_EXIT_HASH) {
            this.setOnMouseExit(cl.convert(MouseExitedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_MOUSE_MOVE_HASH) {
            this.setOnMouseMove(cl.convert(MouseMovedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_MOUSE_PRESS_HASH) {
            this.setOnMousePress(cl.convert(MousePressedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_MOUSE_RELEASE_HASH) {
            this.setOnMouseRelease(cl.convert(MouseReleasedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_MOUSE_WHEEL_HASH) {
            this.setOnMouseWheel(cl.convert(MouseWheeledListener.class, value));
        }
        else if (hash == ItemRenderer.ON_SELECTION_CHANGE_HASH) {
            this.setOnSelectionChange(cl.convert(SelectionChangedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_SLIDER_MOVE_HASH) {
            this.setOnSliderMove(cl.convert(SliderMovedListener.class, value));
        }
        else if (hash == ItemRenderer.ON_POPUP_DISPLAY_HASH) {
            this.setOnPopupDisplay(cl.convert(PopupDisplayListener.class, value));
        }
        else {
            if (hash != ItemRenderer.ON_POPUP_HIDE_HASH) {
                return super.setXMLAttribute(hash, value, cl);
            }
            this.setOnPopupHide(cl.convert(PopupHideListener.class, value));
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemRenderer.class);
        itemElements = new ArrayList<EventDispatcher>();
        ON_ACTIVATION_HASH = "onActivation".hashCode();
        ON_CLICK_HASH = "onClick".hashCode();
        ON_DOUBLE_CLICK_HASH = "onDoubleClick".hashCode();
        ON_FOCUS_CHANGE_HASH = "onFocusChange".hashCode();
        ON_ITEM_CLICK_HASH = "onItemClick".hashCode();
        ON_ITEM_DOUBLE_CLICK_HASH = "onItemDoubleClick".hashCode();
        ON_ITEM_OUT_HASH = "onItemOut".hashCode();
        ON_ITEM_OVER_HASH = "onItemOver".hashCode();
        ON_KEY_PRESS_HASH = "onKeyPress".hashCode();
        ON_KEY_RELEASE_HASH = "onKeyRelease".hashCode();
        ON_KEY_TYPE_HASH = "onKeyType".hashCode();
        ON_LIST_SELECTION_CHANGE_HASH = "onListSelectionChange".hashCode();
        ON_MOUSE_DRAG_HASH = "onMouseDrag".hashCode();
        ON_MOUSE_DRAG_IN_HASH = "onMouseDragIn".hashCode();
        ON_MOUSE_DRAG_OUT_HASH = "onMouseDragOut".hashCode();
        ON_MOUSE_ENTER_HASH = "onMouseEnter".hashCode();
        ON_MOUSE_EXIT_HASH = "onMouseExit".hashCode();
        ON_MOUSE_MOVE_HASH = "onMouseMove".hashCode();
        ON_MOUSE_PRESS_HASH = "onMousePress".hashCode();
        ON_MOUSE_RELEASE_HASH = "onMouseRelease".hashCode();
        ON_MOUSE_WHEEL_HASH = "onMouseWheel".hashCode();
        ON_SELECTION_CHANGE_HASH = "onSelectionChange".hashCode();
        ON_SLIDER_MOVE_HASH = "onSliderMove".hashCode();
        ON_DRAG_HASH = "onDrag".hashCode();
        ON_DROP_HASH = "onDrop".hashCode();
        ON_DRAG_OUT_HASH = "onDragOut".hashCode();
        ON_DROP_OUT_HASH = "onDropOut".hashCode();
        ON_DRAG_OVER_HASH = "onDragOver".hashCode();
        ON_POPUP_DISPLAY_HASH = "onPopupDisplay".hashCode();
        ON_POPUP_HIDE_HASH = "onPopupHide".hashCode();
    }
}
