package com.ankamagames.xulor2.core;

import com.ankamagames.xulor2.component.*;
import java.util.*;

public class RenderableContainerManager
{
    private static RenderableContainerManager m_instance;
    private final ArrayList<RenderableContainerManagerListener> m_listeners;
    private final ArrayList<RenderableContainerManagerListener> m_listenersToAdd;
    private final ArrayList<RenderableContainerManagerListener> m_listenersToRemove;
    private final ArrayList<RenderableContainer> m_renderableContainers;
    private boolean m_needsSorting;
    private boolean m_locked;
    private final ArrayList<RenderableContainer> m_toAdd;
    private final ArrayList<RenderableContainer> m_toRemove;
    private boolean m_changesDuringLock;
    private int m_lastTurnProcesses;
    
    private RenderableContainerManager() {
        super();
        this.m_listeners = new ArrayList<RenderableContainerManagerListener>();
        this.m_listenersToAdd = new ArrayList<RenderableContainerManagerListener>();
        this.m_listenersToRemove = new ArrayList<RenderableContainerManagerListener>();
        this.m_renderableContainers = new ArrayList<RenderableContainer>();
        this.m_needsSorting = false;
        this.m_locked = false;
        this.m_toAdd = new ArrayList<RenderableContainer>();
        this.m_toRemove = new ArrayList<RenderableContainer>();
        this.m_changesDuringLock = false;
        this.m_lastTurnProcesses = 0;
    }
    
    public static RenderableContainerManager getInstance() {
        return RenderableContainerManager.m_instance;
    }
    
    public void addListener(final RenderableContainerManagerListener l) {
        if (!this.m_locked) {
            this.m_listeners.add(l);
        }
        else {
            this.m_listenersToAdd.add(l);
            this.m_changesDuringLock = true;
        }
    }
    
    public void removeListener(final RenderableContainerManagerListener l) {
        if (!this.m_locked) {
            this.m_listeners.remove(l);
        }
        else {
            this.m_listenersToRemove.add(l);
            this.m_changesDuringLock = true;
        }
    }
    
    public void addRenderableContainer(final RenderableContainer renderable) {
        if (this.m_locked) {
            this.m_toAdd.add(renderable);
            this.m_changesDuringLock = true;
        }
        else {
            if (!this.m_renderableContainers.contains(renderable)) {
                this.m_renderableContainers.add(renderable);
            }
            this.m_needsSorting = true;
        }
    }
    
    public void removeRenderableContainer(final RenderableContainer renderable) {
        if (this.m_locked) {
            if (this.m_toAdd.contains(renderable)) {
                this.m_toAdd.remove(renderable);
            }
            else {
                this.m_toRemove.add(renderable);
            }
            this.m_changesDuringLock = true;
        }
        else {
            this.m_renderableContainers.remove(renderable);
        }
    }
    
    public void sort() {
        Collections.sort(this.m_renderableContainers, RenderableContainerComparator.m_instance);
        this.m_needsSorting = false;
    }
    
    public void setToDirty() {
        this.m_needsSorting = true;
    }
    
    public boolean needsSorting() {
        return this.m_needsSorting;
    }
    
    public void processAll() {
        this.lock();
        final int previousProcesses = this.m_lastTurnProcesses;
        this.m_lastTurnProcesses = 0;
        for (int i = 0, size = this.m_renderableContainers.size(); i < size; ++i) {
            if (this.m_renderableContainers.get(i).processSetItem()) {
                ++this.m_lastTurnProcesses;
            }
        }
        if (this.m_lastTurnProcesses == 0 && previousProcesses > 0) {
            for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
                this.m_listeners.get(i).onDoneProcessing();
            }
        }
        this.unlock();
    }
    
    public void lock() {
        this.m_locked = true;
    }
    
    public void unlock() {
        this.m_locked = false;
        if (!this.m_changesDuringLock) {
            return;
        }
        int size = this.m_toAdd.size();
        if (size > 0) {
            for (int i = 0; i < size; ++i) {
                this.addRenderableContainer(this.m_toAdd.get(i));
            }
            this.m_toAdd.clear();
        }
        size = this.m_toRemove.size();
        if (size > 0) {
            for (int i = 0; i < size; ++i) {
                this.removeRenderableContainer(this.m_toRemove.get(i));
            }
            this.m_toRemove.clear();
        }
        size = this.m_listenersToAdd.size();
        if (size > 0) {
            for (int i = 0; i < size; ++i) {
                this.addListener(this.m_listenersToAdd.get(i));
            }
            this.m_listenersToAdd.clear();
        }
        size = this.m_listenersToRemove.size();
        if (size > 0) {
            for (int i = 0; i < size; ++i) {
                this.removeListener(this.m_listenersToRemove.get(i));
            }
            this.m_listenersToRemove.clear();
        }
        this.m_changesDuringLock = false;
    }
    
    static {
        RenderableContainerManager.m_instance = new RenderableContainerManager();
    }
    
    private static class RenderableContainerComparator implements Comparator<RenderableContainer>
    {
        private static RenderableContainerComparator m_instance;
        
        @Override
        public int compare(final RenderableContainer o1, final RenderableContainer o2) {
            return o1.getTreeDepth() - o2.getTreeDepth();
        }
        
        static {
            RenderableContainerComparator.m_instance = new RenderableContainerComparator();
        }
    }
    
    public interface RenderableContainerManagerListener
    {
        void onDoneProcessing();
    }
}
