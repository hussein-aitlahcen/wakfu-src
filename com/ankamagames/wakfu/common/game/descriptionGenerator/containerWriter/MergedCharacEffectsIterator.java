package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import java.util.*;

public class MergedCharacEffectsIterator implements Iterator<WakfuEffect>
{
    private final ArrayList<WakfuEffect> m_effects;
    private int m_currentIndex;
    
    public MergedCharacEffectsIterator(final Iterator<WakfuEffect> it) {
        super();
        this.m_effects = new ArrayList<WakfuEffect>();
        this.computeEffects(it);
    }
    
    private void computeEffects(final Iterator<WakfuEffect> it) {
        while (it.hasNext()) {
            final WakfuEffect effect = it.next();
            if (this.isMergeable(effect)) {
                final WakfuEffect wakfuEffect = this.findEffectWithActionId(effect.getActionId());
                if (wakfuEffect != null) {
                    final float[] toAddParams = effect.getRawParams();
                    final float[] sourceParams = wakfuEffect.getRawParams();
                    for (int i = 0, size = sourceParams.length; i < size; i += 2) {
                        final float[] array = sourceParams;
                        final int n = i;
                        array[n] += toAddParams[i];
                    }
                }
                else {
                    this.m_effects.add(effect.clone());
                }
            }
            else {
                this.m_effects.add(effect);
            }
        }
    }
    
    @Nullable
    private WakfuEffect findEffectWithActionId(final int actionId) {
        for (int i = 0, size = this.m_effects.size(); i < size; ++i) {
            final WakfuEffect effect = this.m_effects.get(i);
            if (effect.getActionId() == actionId) {
                return effect;
            }
        }
        return null;
    }
    
    private boolean isMergeable(final Effect effect) {
        final WakfuRunningEffect re = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId());
        return re instanceof CharacBuff || re instanceof CharacDebuff;
    }
    
    @Override
    public boolean hasNext() {
        return this.m_currentIndex < this.m_effects.size();
    }
    
    @Override
    public WakfuEffect next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException("Il n'y a plus d'\u00e9l\u00e9ments");
        }
        return this.m_effects.get(this.m_currentIndex++);
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove non support\u00e9");
    }
}
