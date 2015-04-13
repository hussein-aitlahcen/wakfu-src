package com.ankamagames.wakfu.common.game.fight.time.buff;

import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;

public interface TimelineBuffList<E extends WakfuEffect>
{
    void addEffect(E p0);
    
    List<E> getEffects();
    
    E getEffectById(int p0);
    
    int indexOfEffectSorted(E p0, boolean p1);
    
    void sort(Comparator<WakfuEffect> p0);
}
