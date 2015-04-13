package com.ankamagames.xulor2.core.dragndrop;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;

public class DragNDropManager
{
    private static final Logger m_logger;
    private static final DragNDropManager m_instance;
    private ArrayList<DragNDropListener> m_DNDListeners;
    private ArrayList<DragNDropListener> m_validatedDNDListeners;
    private ArrayList<DragNDropHandler> m_listeners;
    private final ArrayList<DragCancelListener> m_cancelListeners;
    private DragNDropHandler m_currentDND;
    
    private DragNDropManager() {
        super();
        this.m_DNDListeners = new ArrayList<DragNDropListener>();
        this.m_validatedDNDListeners = new ArrayList<DragNDropListener>();
        this.m_listeners = new ArrayList<DragNDropHandler>();
        this.m_cancelListeners = new ArrayList<DragCancelListener>();
    }
    
    public static DragNDropManager getInstance() {
        return DragNDropManager.m_instance;
    }
    
    public void addDragNDropListener(final DragNDropHandler l) {
        this.m_listeners.add(l);
    }
    
    public void removeDragNDropListener(final DragNDropHandler l, final boolean clean) {
        this.m_listeners.remove(l);
        if (clean && l.release()) {
            for (int i = this.m_cancelListeners.size() - 1; i >= 0; --i) {
                this.m_cancelListeners.get(i).cancel();
            }
        }
    }
    
    public void addDragCancelListener(final DragCancelListener l) {
        this.m_cancelListeners.add(l);
    }
    
    public void removeDragCancelListener(final DragCancelListener l) {
        this.m_cancelListeners.remove(l);
    }
    
    public void addDNDListener(final DragNDropListener l) {
        this.m_DNDListeners.add(l);
    }
    
    public void removeDNDListener(final DragNDropListener l) {
        this.m_DNDListeners.remove(l);
    }
    
    public void mousePressed(final Widget w, final int x, final int y) {
        for (final DragNDropHandler l : this.m_listeners) {
            if (l.isDndWidget(w, x, y)) {
                (this.m_currentDND = l).select(x, y);
                this.m_validatedDNDListeners.clear();
                for (final DragNDropListener dragNDropListener : this.m_DNDListeners) {
                    if (dragNDropListener.validateContent(this.m_currentDND.getValue())) {
                        this.m_validatedDNDListeners.add(dragNDropListener);
                    }
                }
            }
        }
    }
    
    public boolean mouseMoved(final Widget draggedOn, final int x, final int y) {
        if (this.m_currentDND != null) {
            this.m_currentDND.drag(x, y, draggedOn);
            for (final DragNDropListener dragNDropListener : this.m_validatedDNDListeners) {
                dragNDropListener.onDrag(this.m_currentDND, x, y, draggedOn);
            }
            return true;
        }
        return false;
    }
    
    public void mouseReleased(final Widget droppedOn, final int x, final int y) {
        if (this.m_currentDND != null) {
            this.m_currentDND.drop(x, y, droppedOn);
            for (final DragNDropListener dragNDropListener : this.m_validatedDNDListeners) {
                dragNDropListener.onDrop(this.m_currentDND, x, y, droppedOn);
            }
            this.m_currentDND = null;
        }
    }
    
    public void cancel() {
        if (this.m_currentDND != null) {
            this.m_currentDND.clean();
            this.m_currentDND = null;
        }
    }
    
    public void cleanUp() {
        this.m_currentDND = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DragNDropManager.class);
        m_instance = new DragNDropManager();
    }
}
