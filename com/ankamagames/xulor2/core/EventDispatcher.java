package com.ankamagames.xulor2.core;

import com.ankamagames.framework.kernel.core.common.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.tween.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.preferences.*;
import com.ankamagames.xulor2.core.taglibrary.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;

public abstract class EventDispatcher extends BasicElement implements TweenClient, Poolable, Comparable<EventDispatcher>, PreferencePropertyChangeListener
{
    protected static Logger m_logger;
    protected ArrayList<EventDispatcher> m_children;
    protected EventDispatcher m_parent;
    private IntObjectLightWeightMap<ArrayList<EventListener>> m_beforeListeners;
    private IntObjectLightWeightMap<ArrayList<EventListener>> m_afterListeners;
    private EnumSet<Events> m_unactivatedEvent;
    protected ArrayList<Widget> m_widgetsToAdd;
    private boolean m_addedToTree;
    private int m_treeDepth;
    private int m_treePosition;
    private short m_modalLevel;
    protected ElementMap m_elementMap;
    protected boolean m_elementMapRoot;
    protected String m_id;
    protected boolean m_valid;
    protected boolean m_childrenAdded;
    protected RenderableContainer m_renderableParent;
    protected UserDefinedManager m_userDefinedManager;
    protected ArrayList<AbstractTween> m_tweens;
    protected ArrayList<AbstractTween> m_tweensToRemove;
    protected boolean m_canBeCloned;
    public static final int ID_HASH;
    
    public EventDispatcher() {
        super();
        this.m_children = new ArrayList<EventDispatcher>();
        this.m_widgetsToAdd = null;
        this.m_addedToTree = false;
        this.m_modalLevel = -1;
        this.m_elementMapRoot = false;
        this.m_id = null;
        this.m_valid = false;
        this.m_childrenAdded = false;
        this.m_userDefinedManager = null;
        this.m_canBeCloned = true;
    }
    
    @Override
    public void add(final EventDispatcher element) {
        this.add(element, true);
    }
    
    public void add(final EventDispatcher element, final boolean addWidget) {
        if (element != null) {
            assert !this.m_unloading : "Object is already checked-in" + this.getClass().getSimpleName();
            super.add(element);
            this.m_children.add(element);
            element.setParent(this);
            element.setTreeDepth(this.m_treeDepth + 1);
            TreeDepthManager.getInstance().setToDirty();
            if (addWidget && element instanceof Widget) {
                this.addWidget((Widget)element);
            }
            if (this.isInTree()) {
                element.addedToTree();
            }
            element.setIsATemplate(this.m_isATemplate);
        }
        else {
            EventDispatcher.m_logger.error((Object)("Tentative d'ajout un element null \u00e0 " + this.getClass().getSimpleName()));
        }
    }
    
    public void remove(final EventDispatcher element) {
        if (this.m_children != null) {
            if (element instanceof Widget) {
                this.removeWidget((Widget)element);
            }
            element.removedFromTree();
            this.m_children.remove(element);
            element.setParent(null);
        }
    }
    
    public void destroy(final EventDispatcher element) {
        this.remove(element);
        release(element);
    }
    
    public void removeSelfFromParent() {
        if (this.m_parent != null) {
            this.m_parent.remove(this);
        }
    }
    
    public void destroySelfFromParent() {
        if (this.m_parent != null) {
            this.m_parent.destroy(this);
        }
        else {
            release(this);
        }
    }
    
    public void removeAllChildren() {
        if (this.m_children != null) {
            for (int i = this.m_children.size() - 1; i >= 0; --i) {
                this.remove(this.m_children.get(i));
            }
            this.m_children.clear();
        }
    }
    
    public void destroyAllChildren() {
        if (this.m_children != null) {
            for (int i = this.m_children.size() - 1; i >= 0; --i) {
                this.destroy(this.m_children.get(i));
            }
            this.m_children.clear();
        }
    }
    
    public void addWidget(final Widget w) {
        if (this.m_parent != null) {
            this.m_parent.addWidget(w);
        }
        else {
            if (this.m_widgetsToAdd == null) {
                this.m_widgetsToAdd = new ArrayList<Widget>();
            }
            this.m_widgetsToAdd.add(w);
            this.setNeedsToPreProcess();
        }
    }
    
    public void removeWidget(final Widget widget) {
        if (this.m_parent != null) {
            this.m_parent.removeWidget(widget);
        }
    }
    
    public void setWidgetOnTop(final Widget widget) {
        if (this.m_parent != null) {
            this.m_parent.setWidgetOnTop(widget);
        }
    }
    
    @Override
    public void addTween(final AbstractTween tween) {
        if (this.m_tweens == null) {
            this.m_tweens = new ArrayList<AbstractTween>(5);
        }
        this.m_tweens.add(tween);
        tween.setTweenClient(this);
        this.setNeedsToPreProcess();
    }
    
    @Override
    public void removeTween(final AbstractTween tween) {
        if (tween == null) {
            return;
        }
        if (this.m_tweensToRemove == null) {
            this.m_tweensToRemove = new ArrayList<AbstractTween>(5);
        }
        if (!this.m_tweensToRemove.contains(tween)) {
            this.m_tweensToRemove.add(tween);
            this.setNeedsToPreProcess();
        }
    }
    
    public boolean removeTweensOfType(final Class<? extends AbstractTween> type) {
        if (this.m_tweens == null) {
            return false;
        }
        boolean ret = false;
        for (int i = this.m_tweens.size() - 1; i >= 0; --i) {
            final AbstractTween tw = this.m_tweens.get(i);
            if (tw.getClass().equals(type)) {
                this.removeTween(tw);
                ret = true;
            }
        }
        return ret;
    }
    
    public boolean hasTweensOfType(final Class<? extends AbstractTween> type) {
        if (this.m_tweens == null) {
            return false;
        }
        for (int i = this.m_tweens.size() - 1; i >= 0; --i) {
            if (this.m_tweens.get(i).getClass().equals(type)) {
                return true;
            }
        }
        return false;
    }
    
    public void addEventListener(final Events type, final EventListener listener, final boolean before) {
        final int typeOrdinal = type.ordinal();
        if (before) {
            if (this.m_beforeListeners == null) {
                this.m_beforeListeners = new IntObjectLightWeightMap<ArrayList<EventListener>>(5);
            }
            ArrayList<EventListener> listeners = this.m_beforeListeners.get(typeOrdinal);
            if (listeners == null) {
                listeners = new ArrayList<EventListener>();
                this.m_beforeListeners.put(typeOrdinal, listeners);
            }
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
        else {
            if (this.m_afterListeners == null) {
                this.m_afterListeners = new IntObjectLightWeightMap<ArrayList<EventListener>>(5);
            }
            ArrayList<EventListener> listeners = this.m_afterListeners.get(typeOrdinal);
            if (listeners == null) {
                listeners = new ArrayList<EventListener>();
                this.m_afterListeners.put(typeOrdinal, listeners);
            }
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }
    
    public void removeEventListener(final Events type, final EventListener listener, final boolean before) {
        final int typeOrdinal = type.ordinal();
        if (before) {
            if (this.m_beforeListeners == null) {
                return;
            }
            final ArrayList<EventListener> listeners = this.m_beforeListeners.get(typeOrdinal);
            if (listeners != null) {
                listeners.remove(listener);
                if (listeners.isEmpty()) {
                    this.m_beforeListeners.remove(typeOrdinal);
                }
            }
        }
        else {
            if (this.m_afterListeners == null) {
                return;
            }
            final ArrayList<EventListener> listeners = this.m_afterListeners.get(typeOrdinal);
            if (listeners != null) {
                listeners.remove(listener);
                if (listeners.isEmpty()) {
                    this.m_afterListeners.remove(typeOrdinal);
                }
            }
        }
    }
    
    public void removeAllEventListeners() {
        if (this.m_beforeListeners != null) {
            this.m_beforeListeners.clear();
            this.m_beforeListeners = null;
        }
        if (this.m_afterListeners != null) {
            this.m_afterListeners.clear();
            this.m_afterListeners = null;
        }
    }
    
    @Nullable
    public ArrayList<EventListener> getListeners(final Events type, final boolean before) {
        if (before && this.m_beforeListeners != null) {
            return this.m_beforeListeners.get(type.ordinal());
        }
        if (!before && this.m_afterListeners != null) {
            return this.m_afterListeners.get(type.ordinal());
        }
        return null;
    }
    
    @Override
    public ElementType getElementType() {
        return ElementType.EVENT_DISPATCHER;
    }
    
    public EventDispatcher getParent() {
        return this.m_parent;
    }
    
    public void setParent(final EventDispatcher parent) {
        assert parent != this : "On ne peut pas se d\u00e9finir soi-m\u00eame en parent";
        this.m_parent = parent;
        if (this.m_parent != null && this.m_elementMap == null) {
            this.m_elementMap = this.m_parent.getElementMap();
        }
    }
    
    public ArrayList<EventDispatcher> getChildren() {
        return this.m_children;
    }
    
    public String getId() {
        return this.m_id;
    }
    
    public void setId(final String id) {
        if (this.m_id != null && !this.m_id.equalsIgnoreCase(id) && this.m_elementMap != null) {
            this.m_elementMap.changeElementId(this.m_id, id);
            this.m_id = id;
        }
        else if (this.m_id == null && id != null && this.m_elementMap != null) {
            this.m_id = id;
            this.m_elementMap.add(id, this);
        }
        else {
            this.m_id = id;
        }
    }
    
    public short getModalLevel() {
        return this.m_modalLevel;
    }
    
    public void setModalLevel(final short modalLevel) {
        this.m_modalLevel = modalLevel;
    }
    
    public boolean isValidAdd(final BasicElement toAdd) {
        return toAdd != this;
    }
    
    @Override
    public int getTreePosition() {
        return this.m_treePosition;
    }
    
    public void setTreePosition(final int treePosition) {
        this.m_treePosition = treePosition;
    }
    
    public void setTreeDepth(final int depth) {
        this.m_treeDepth = depth;
        if (this.m_children != null) {
            for (int i = 0, size = this.m_children.size(); i < size; ++i) {
                this.m_children.get(i).setTreeDepth(depth + 1);
            }
        }
    }
    
    @Override
    public int getTreeDepth() {
        return this.m_treeDepth;
    }
    
    @Override
    public void setIsATemplate(final boolean template) {
        this.m_isATemplate |= template;
        for (int i = this.m_children.size() - 1; i >= 0; --i) {
            this.m_children.get(i).setIsATemplate(template);
        }
    }
    
    @Override
    public boolean isATemplate() {
        return this.m_isATemplate;
    }
    
    public boolean hasListener(final Events type) {
        return (this.m_beforeListeners != null && this.m_beforeListeners.contains(type.ordinal())) || (this.m_afterListeners != null && this.m_afterListeners.contains(type.ordinal()));
    }
    
    public boolean isValid() {
        return this.m_valid;
    }
    
    public boolean setAppearance(final DecoratorAppearance decoratorAppearance) {
        EventDispatcher.m_logger.warn((Object)("Tentative d'ajout d'une apparence " + decoratorAppearance.getClass().getSimpleName() + " \u00e0 un " + this.getClass().getSimpleName()));
        decoratorAppearance.destroySelfFromParent();
        return false;
    }
    
    public Widget getParentWidget() {
        if (this.m_parent == null) {
            return null;
        }
        if (this.m_parent instanceof Widget) {
            return (Widget)this.m_parent;
        }
        return this.m_parent.getParentWidget();
    }
    
    public boolean hasInParentHierarchy(final EventDispatcher c) {
        return this.m_parent != null && (this.m_parent == c || this.m_parent.hasInParentHierarchy(c));
    }
    
    public Object getElementValue() {
        return this;
    }
    
    public void setElementMap(final ElementMap map) {
        this.m_elementMap = map;
    }
    
    public ElementMap getElementMap() {
        if (this.m_elementMap == null && this.m_parent != null) {
            return this.m_parent.getElementMap();
        }
        return this.m_elementMap;
    }
    
    public boolean isElementMapRoot() {
        return this.m_elementMapRoot;
    }
    
    public void setElementMapRoot(final boolean elementMapRoot) {
        this.m_elementMapRoot = elementMapRoot;
    }
    
    public RenderableContainer getRenderableParent() {
        return this.m_renderableParent;
    }
    
    public void setRenderableParent(final RenderableContainer renderable) {
        if (this.m_renderableParent != renderable) {
            this.m_renderableParent = renderable;
        }
    }
    
    public void setChildrenAdded(final boolean added) {
        this.m_childrenAdded = added;
    }
    
    public boolean areChildrenAdded() {
        return this.m_childrenAdded;
    }
    
    public UserDefinedManager getUserDefinedManager() {
        return this.m_userDefinedManager;
    }
    
    public void setUserDefinedManager(final UserDefinedManager userDefined) {
        this.m_userDefinedManager = userDefined;
    }
    
    public void enableEvent(final Events type, final boolean enabled) {
        if (enabled && this.m_unactivatedEvent != null) {
            this.m_unactivatedEvent.remove(type);
        }
        else if (!enabled) {
            if (this.m_unactivatedEvent == null) {
                this.m_unactivatedEvent = EnumSet.noneOf(Events.class);
            }
            this.m_unactivatedEvent.add(type);
        }
    }
    
    protected void processEventForSound(final Event e, final boolean up) {
    }
    
    public void setCanBeCloned(final boolean canBeCloned) {
        this.m_canBeCloned = canBeCloned;
    }
    
    public boolean canBeCloned() {
        return this.m_canBeCloned;
    }
    
    @Override
    public void propertyChange(final PreferencePropertyChangeEvent event) {
    }
    
    public void loadPreferences() {
        if (this.m_userDefinedManager != null) {
            this.m_userDefinedManager.loadPreferences();
        }
    }
    
    public void storePreferences() {
        if (this.m_userDefinedManager != null) {
            this.m_userDefinedManager.storePreferences();
        }
    }
    
    public boolean isInTree() {
        return this.m_parent != null && this.m_parent.isInTree();
    }
    
    public void addedToTree() {
        for (int i = 0, size = this.m_children.size(); i < size; ++i) {
            this.m_children.get(i).addedToTree();
        }
    }
    
    public void removedFromTree() {
        if (this.m_userDefinedManager != null) {
            this.m_userDefinedManager.storePreferences();
            this.m_userDefinedManager.removeFromGlobalManager();
            final PreferenceStore preferenceStore = Xulor.getInstance().getUserDefinedAdapter().getPreferenceStoreForDialog(this.m_elementMap.getId());
            if (preferenceStore != null) {
                preferenceStore.removePreferencePropertyChangedListener(this);
            }
        }
        for (int i = 0, size = this.m_children.size(); i < size; ++i) {
            this.m_children.get(i).removedFromTree();
        }
    }
    
    @Override
    public void copyElement(final BasicElement source) {
        final EventDispatcher e = (EventDispatcher)source;
        super.copyElement(source);
        e.m_id = this.m_id;
        e.m_childrenAdded = this.m_childrenAdded;
        e.m_modalLevel = this.m_modalLevel;
        e.m_elementMap = this.m_elementMap;
        if (this.m_beforeListeners != null) {
            e.m_beforeListeners = this.copyListeners(e.m_beforeListeners, this.m_beforeListeners);
        }
        if (this.m_afterListeners != null) {
            e.m_afterListeners = this.copyListeners(e.m_afterListeners, this.m_afterListeners);
        }
        if (this.m_children != null) {
            for (int i = 0, size = this.m_children.size(); i < size; ++i) {
                final EventDispatcher child = this.m_children.get(i);
                if (child.canBeCloned()) {
                    e.add(child.cloneElementStructure());
                }
            }
        }
    }
    
    private IntObjectLightWeightMap<ArrayList<EventListener>> copyListeners(IntObjectLightWeightMap<ArrayList<EventListener>> dest, final IntObjectLightWeightMap<ArrayList<EventListener>> src) {
        final int size = src.size();
        if (dest != null) {
            dest.ensureCapacity(dest.size() + size);
        }
        else {
            dest = new IntObjectLightWeightMap<ArrayList<EventListener>>(size);
        }
        for (int i = 0; i < size; ++i) {
            final int key = src.getQuickKey(i);
            final ArrayList<EventListener> listeners = src.getQuickValue(i);
            ArrayList<EventListener> list = dest.get(key);
            final int listenersCount = listeners.size();
            if (list == null) {
                list = new ArrayList<EventListener>(listenersCount);
                dest.put(key, list);
            }
            else {
                list.ensureCapacity(listenersCount + list.size());
            }
            for (int a = 0; a < listenersCount; ++a) {
                list.add(listeners.get(a));
            }
        }
        return dest;
    }
    
    public EventDispatcher cloneElementStructure() {
        try {
            EventDispatcher e;
            try {
                e = (EventDispatcher)XulorTagLibrary.getInstance().getFactory(this.getClass()).newInstance();
            }
            catch (NullPointerException npe) {
                EventDispatcher.m_logger.error((Object)("pas de factory trouv\u00e9e pour " + this.getClass().getSimpleName()), (Throwable)npe);
                return null;
            }
            this.copyElement(e);
            return e;
        }
        catch (Throwable e2) {
            EventDispatcher.m_logger.error((Object)"Exception", e2);
            return null;
        }
    }
    
    @Override
    public int compareTo(final EventDispatcher ed) {
        return ed.getTreeDepth() - this.m_treeDepth;
    }
    
    public boolean runEvent(final Event e, boolean up) {
        this.processEventForSound(e, up);
        e.setCurrentTarget(this);
        boolean stop = false;
        if (this.m_unactivatedEvent == null || !this.m_unactivatedEvent.contains(e.getType())) {
            if (up) {
                stop = this.runBeforeListeners(e);
            }
            else {
                stop = this.runAfterListeners(e);
            }
        }
        if (stop) {
            return true;
        }
        if (e.getTarget() == this && !e.isRunAfterOnTarget()) {
            e.setRunAfterOnTarget(true);
            up = false;
        }
        else if (e.getTarget() == this && e.isRunAfterOnTarget()) {
            e.setRunAfterOnTarget(false);
        }
        if (up) {
            final EventDispatcher ed = e.popNextEventDispatcher();
            if (ed != null) {
                return ed.runEvent(e, true);
            }
        }
        else {
            if (e.isRunAfterOnTarget()) {
                return e.getTarget().runEvent(e, false);
            }
            if (this.m_parent != null) {
                return this.m_parent.runEvent(e, false);
            }
        }
        return false;
    }
    
    public void dispatchEventDebug(final Event event) {
        if (this.m_afterListeners != null) {
            boolean stop = false;
            final ArrayList<EventListener> l = this.m_afterListeners.get(event.getType().ordinal());
            if (l != null) {
                for (int i = 0; i < l.size(); ++i) {
                    stop |= l.get(i).run(event);
                    if (stop) {
                        break;
                    }
                }
            }
        }
    }
    
    public boolean dispatchEvent(final Event event) {
        if (event == null) {
            EventDispatcher.m_logger.error((Object)"L'event est null, impossible de le traiter");
            return false;
        }
        try {
            if (event.getTarget() == null) {
                EventDispatcher.m_logger.error((Object)("[" + event.getType() + "] L'event n'a pas de target, impossible de le traiter"));
                return false;
            }
            event.setTarget(this);
            final boolean up = true;
            EventDispatcher parent;
            if (this instanceof MasterRootContainer) {
                parent = this;
            }
            else {
                parent = this.m_parent;
            }
            event.pushEventDispatcher(this);
            while (parent != null && !(parent instanceof MasterRootContainer)) {
                event.pushEventDispatcher(parent);
                parent = parent.m_parent;
            }
            return parent != null && parent.runEvent(event, true);
        }
        finally {
            event.release();
        }
    }
    
    public static void release(final EventDispatcher dispatcher) {
        if (!dispatcher.isUnloading()) {
            dispatcher.release();
        }
    }
    
    public void validate() {
        this.m_valid = true;
    }
    
    public void invalidate() {
        this.m_valid = false;
        this.setNeedsToPostProcess();
    }
    
    @Override
    public void onChildrenAdded() {
        super.onChildrenAdded();
        this.m_childrenAdded = true;
    }
    
    public void onAdd() {
        this.m_addedToTree = true;
        this.setNeedsToMiddleProcess();
    }
    
    @Override
    public boolean preProcess(final int deltaTime) {
        boolean ret = super.preProcess(deltaTime);
        if (this.m_widgetsToAdd != null && !this.m_widgetsToAdd.isEmpty()) {
            for (int size = this.m_widgetsToAdd.size(), i = 0; i < size; ++i) {
                this.addWidget(this.m_widgetsToAdd.get(i));
            }
            this.m_widgetsToAdd.clear();
        }
        if (this.m_tweensToRemove != null) {
            final int tweenSize = this.m_tweensToRemove.size();
            if (tweenSize > 0) {
                for (int i = 0; i < tweenSize; ++i) {
                    this.m_tweensToRemove.get(i).onEnd();
                }
                if (this.m_tweens != null) {
                    this.m_tweens.removeAll(this.m_tweensToRemove);
                }
                this.m_tweensToRemove.clear();
            }
        }
        if (this.m_tweens != null) {
            final int tweenSize = this.m_tweens.size();
            if (tweenSize > 0) {
                for (int i = 0; i < tweenSize; ++i) {
                    this.m_tweens.get(i).process(deltaTime);
                }
            }
        }
        if (this.m_tweens != null && this.m_tweens.size() > 0) {
            ret = true;
        }
        return ret;
    }
    
    @Override
    public boolean middleProcess(final int deltaTime) {
        final boolean ret = super.middleProcess(deltaTime);
        if (!this.m_addedToTree) {
            this.onAdd();
        }
        return ret;
    }
    
    @Override
    public boolean postProcess(final int deltaTime) {
        final boolean ret = super.postProcess(deltaTime);
        if (!this.m_valid) {
            this.validate();
        }
        return ret;
    }
    
    public boolean runBeforeListeners(final Event event) {
        if (this.m_beforeListeners == null) {
            return false;
        }
        final ArrayList<EventListener> listeners = this.getListeners(event.getType(), true);
        if (listeners != null) {
            synchronized (listeners) {
                for (int i = 0; i < listeners.size(); ++i) {
                    if (listeners.get(i).run(event)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean runAfterListeners(final Event event) {
        if (this.m_afterListeners == null) {
            return false;
        }
        final ArrayList<EventListener> listeners = this.getListeners(event.getType(), false);
        if (listeners != null) {
            synchronized (listeners) {
                for (int i = 0; i < listeners.size(); ++i) {
                    if (listeners.get(i).run(event)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        if (this.m_children != null) {
            for (int i = this.m_children.size() - 1; i >= 0; --i) {
                this.destroy(this.m_children.get(i));
            }
            this.m_children.clear();
        }
        if (this.m_widgetsToAdd != null) {
            this.m_widgetsToAdd.clear();
            this.m_widgetsToAdd = null;
        }
        this.m_currentPool = null;
        this.m_parent = null;
        this.removeAllEventListeners();
        this.m_beforeListeners = null;
        this.m_afterListeners = null;
        this.m_unactivatedEvent = null;
        if (this.m_elementMap != null) {
            if (this.m_id != null) {
                this.m_elementMap.removeElement(this.m_id);
            }
            if (this.m_elementMapRoot) {
                Xulor.getInstance().cleanId(this.m_elementMap.getId());
                this.m_elementMap.getEnvironment().removeElementMap(this.m_elementMap.getId());
            }
            this.m_elementMap = null;
        }
        this.m_id = null;
        this.m_renderableParent = null;
        if (this.m_tweens != null) {
            this.m_tweens.clear();
            this.m_tweens = null;
        }
        if (this.m_tweensToRemove != null) {
            this.m_tweensToRemove.clear();
            this.m_tweensToRemove = null;
        }
        this.m_userDefinedManager = null;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_addedToTree = false;
        this.m_modalLevel = -1;
        this.m_treeDepth = 0;
        this.m_elementMapRoot = false;
        this.m_valid = false;
        this.m_childrenAdded = false;
        this.m_isATemplate = false;
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        if (EventDispatcher.ID_HASH == hash) {
            this.setId(cl.convertToString(value, this.m_elementMap));
            return true;
        }
        return false;
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        if (EventDispatcher.ID_HASH == hash) {
            this.setId((String)value);
            return true;
        }
        return false;
    }
    
    @Override
    public void preApplyAttributes(final DocumentEntry entry, final EventDispatcher parent, final Stack<ElementMap> elementMaps, final Environment env) {
        ElementMap elementMap = (elementMaps != null) ? elementMaps.peek() : null;
        if (elementMap == null && parent != null) {
            elementMap = parent.getElementMap();
        }
        this.setElementMap(elementMap);
        super.preApplyAttributes(entry, parent, elementMaps, env);
    }
    
    public void prepareRender() {
    }
    
    static {
        EventDispatcher.m_logger = Logger.getLogger((Class)EventDispatcher.class);
        ID_HASH = "id".hashCode();
    }
}
