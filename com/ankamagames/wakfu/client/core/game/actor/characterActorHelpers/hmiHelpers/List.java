package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.hmiHelpers;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.effect.*;

public abstract class List<T extends AdditionnalData> implements Iterable<T>
{
    private static final Logger m_logger;
    protected final ArrayList<T> m_stack;
    
    public List() {
        super();
        this.m_stack = new ArrayList<T>();
    }
    
    public final boolean isEmpty() {
        return this.m_stack.isEmpty();
    }
    
    public void addAndApply(final CharacterActor actor, final T data) {
        this.onAdding(actor, data);
        this.m_stack.add(data);
        data.apply(actor);
    }
    
    public final T getLast() {
        if (this.isEmpty()) {
            return null;
        }
        return this.m_stack.get(this.m_stack.size() - 1);
    }
    
    public final T remove(final T dataRemoved) {
        return this.remove(dataRemoved, true);
    }
    
    public boolean contains(final T data) {
        return this.m_stack.contains(data);
    }
    
    @Override
    public final Iterator<T> iterator() {
        return this.m_stack.iterator();
    }
    
    public final T remove(final T dataRemoved, final boolean removeAll) {
        if (this.isEmpty()) {
            List.m_logger.error((Object)"changement d'apparance mais liste vide");
            return null;
        }
        final T current = this.getLast();
        assert current != null;
        T removed = null;
        int count = 0;
        final Iterator<? extends T> iter = (Iterator<? extends T>)this.m_stack.iterator();
        while (iter.hasNext()) {
            final T data = (T)iter.next();
            if (data.equals(dataRemoved)) {
                ++count;
                iter.remove();
                removed = data;
                if (!removeAll) {
                    break;
                }
                continue;
            }
        }
        if (count == 0) {
            List.m_logger.error((Object)"Appearance change remove requested, but no entry found for the linked Object");
        }
        if (count > 1) {
            List.m_logger.warn((Object)"Appearance change remove requested, but several entries found for the linked Object");
        }
        return removed;
    }
    
    public void remove(final CharacterActor actor, final T data) {
        this.remove(actor, data, true);
    }
    
    public void remove(final CharacterActor actor, final T data, final boolean removeAll) {
        final T current = this.getLast();
        final T removed = this.remove(data, removeAll);
        this.onRemoved(current, removed, actor);
    }
    
    public void clear(final CharacterActor actor) {
        this.m_stack.clear();
    }
    
    public int size() {
        return this.m_stack.size();
    }
    
    public T removeFirst() {
        return this.remove(this.m_stack.get(0));
    }
    
    protected abstract void onAdding(final CharacterActor p0, final T p1);
    
    public abstract void onRemoved(final T p0, final T p1, final CharacterActor p2);
    
    public void copyToOtherActor(final CharacterActor actor, final List<T> actorList) {
        this.copyToOtherActor(actor, actorList, null);
    }
    
    public void copyToOtherActor(final CharacterActor actor, final List<T> actorList, @Nullable final DataCopyFilter<T> copyFilter) {
        try {
            if (actor == null) {
                return;
            }
            for (final T data : this) {
                if (copyFilter != null && !copyFilter.hasToBeCopied(data)) {
                    continue;
                }
                actorList.addAndApply(actor, data.duplicateForNewList());
            }
        }
        catch (Exception e) {
            List.m_logger.error((Object)("Error while recopying HMIActions of type " + this + " : "), (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)List.class);
    }
    
    public abstract static class AdditionnalData
    {
        protected final WakfuEffect m_effect;
        
        protected AdditionnalData(final WakfuEffect effect) {
            super();
            this.m_effect = effect;
        }
        
        public abstract void apply(final CharacterActor p0);
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final AdditionnalData that = (AdditionnalData)o;
            return this.m_effect.getEffectId() == that.m_effect.getEffectId();
        }
        
        @Override
        public int hashCode() {
            return this.m_effect.hashCode();
        }
        
        public abstract <U extends AdditionnalData> U duplicateForNewList();
    }
}
