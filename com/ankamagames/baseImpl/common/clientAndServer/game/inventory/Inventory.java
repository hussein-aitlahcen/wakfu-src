package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import org.jetbrains.annotations.*;
import java.util.*;

public abstract class Inventory<C extends InventoryContent> implements Iterable<C>
{
    private static final Logger m_logger;
    public static final short SIZE_INFINITE = -1;
    protected short m_maximumSize;
    protected final boolean m_stacking;
    private boolean m_locked;
    protected final List<InventoryObserver> m_eventsObservers;
    
    protected Inventory(final boolean canStack, final short maximumSize) {
        super();
        this.m_eventsObservers = new ArrayList<InventoryObserver>(1);
        this.m_stacking = canStack;
        this.m_maximumSize = MathHelper.maxShort((short)(-1), maximumSize);
    }
    
    public boolean isStacking() {
        return this.m_stacking;
    }
    
    public void addObserver(final InventoryObserver observer) {
        if (observer == null) {
            return;
        }
        if (!this.m_eventsObservers.contains(observer)) {
            this.m_eventsObservers.add(observer);
        }
    }
    
    public void removeObserver(final InventoryObserver observer) {
        if (observer == null) {
            return;
        }
        this.m_eventsObservers.remove(observer);
    }
    
    public void removeAllObservers() {
        this.m_eventsObservers.clear();
    }
    
    protected void notifyObservers(final InventoryEvent event) {
        final InventoryObserver[] arr$;
        final InventoryObserver[] observers = arr$ = this.m_eventsObservers.toArray(new InventoryObserver[this.m_eventsObservers.size()]);
        for (final InventoryObserver observer : arr$) {
            try {
                observer.onInventoryEvent(event);
            }
            catch (Exception e) {
                Inventory.m_logger.error((Object)("Exception lors de la notification de l'event " + event + " \u00e0 l'observer " + observer), (Throwable)e);
            }
        }
        try {
            event.release();
        }
        catch (Exception e2) {
            Inventory.m_logger.error((Object)"Exception lors du release d'un InventoryEvent", (Throwable)e2);
        }
    }
    
    public void cleanup() {
        this.destroyAll();
        this.removeAllObservers();
    }
    
    public boolean setMaximumSize(final short maxSize) {
        if (this.m_maximumSize > 0 && maxSize < this.size()) {
            Inventory.m_logger.error((Object)("Can't change the size of the inventory to " + maxSize + " : current size is " + this.size()));
            return false;
        }
        this.m_maximumSize = MathHelper.maxShort((short)(-1), maxSize);
        return true;
    }
    
    public short getMaximumSize() {
        return this.m_maximumSize;
    }
    
    public boolean isFull() {
        return this.m_maximumSize != -1 && this.size() >= this.m_maximumSize;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    public boolean isLocked() {
        return this.m_locked;
    }
    
    public void setLocked(final boolean locked) {
        this.m_locked = locked;
        this.notifyObservers(InventoryLockedEvent.checkOut(this));
    }
    
    public abstract boolean add(final C p0) throws InventoryCapacityReachedException, ContentAlreadyPresentException;
    
    public abstract boolean updateQuantity(final long p0, final short p1);
    
    public abstract short getQuantity(final long p0);
    
    public abstract boolean remove(final C p0);
    
    public abstract boolean destroy(final C p0);
    
    @Nullable
    public abstract C removeWithUniqueId(final long p0);
    
    public abstract int destroyWithReferenceId(final int p0);
    
    public abstract int destroyWithReferenceId(final int p0, final int p1);
    
    public abstract boolean contains(final C p0);
    
    public abstract boolean containsUniqueId(final long p0);
    
    public abstract boolean containsReferenceId(final int p0);
    
    @Nullable
    public abstract C getWithUniqueId(final long p0);
    
    @Nullable
    public abstract C getFirstWithReferenceId(final int p0);
    
    public abstract C getFirstWithReferenceId(final int p0, final InventoryContentValidator<C> p1);
    
    public abstract ArrayList<C> getAllWithReferenceId(final int p0);
    
    public abstract ArrayList<C> getAllWithReferenceId(final int p0, final InventoryContentValidator<C> p1);
    
    public abstract ArrayList<C> getAllWithValidator(final InventoryContentValidator<C> p0);
    
    public abstract int size();
    
    public abstract int removeAll();
    
    public abstract int destroyAll();
    
    @Override
    public abstract Iterator<C> iterator();
    
    public abstract InventoryContentChecker<C> getContentChecker();
    
    public abstract void setContentChecker(final InventoryContentChecker<C> p0);
    
    static {
        m_logger = Logger.getLogger((Class)Inventory.class);
    }
}
