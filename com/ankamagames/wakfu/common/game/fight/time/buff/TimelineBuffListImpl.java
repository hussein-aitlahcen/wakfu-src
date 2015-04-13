package com.ankamagames.wakfu.common.game.fight.time.buff;

import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;

public class TimelineBuffListImpl<E extends WakfuEffect> implements TimelineBuffList<E>
{
    private final List<E> m_effects;
    
    public static TimelineBuffList empty() {
        return new TimelineBuffListImpl();
    }
    
    public TimelineBuffListImpl() {
        super();
        this.m_effects = new ArrayList<E>();
    }
    
    @Override
    public void addEffect(final E effect) {
        if (effect == null) {
            return;
        }
        this.m_effects.add(effect);
    }
    
    @Override
    public List<E> getEffects() {
        return Collections.unmodifiableList((List<? extends E>)this.m_effects);
    }
    
    @Override
    public E getEffectById(final int effectId) {
        for (int i = 0; i < this.m_effects.size(); ++i) {
            final E e = this.m_effects.get(i);
            if (e.getEffectId() == effectId) {
                return e;
            }
        }
        return null;
    }
    
    @Override
    public int indexOfEffectSorted(final E effect, final boolean direct) {
        return this.m_effects.indexOf(effect);
    }
    
    @Override
    public void sort(final Comparator<WakfuEffect> comparator) {
        Collections.sort(this.m_effects, comparator);
    }
}
