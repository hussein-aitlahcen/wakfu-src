package com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.trigger.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.runningEffectIterator.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import java.util.*;
import gnu.trove.*;

public class RunningEffectManager implements Iterable<RunningEffect>, Triggerable<RunningEffect>
{
    protected static final Logger m_logger;
    private static final TObjectProcedure<RunningEffect> RELEASE_PROCEDURE;
    protected final TLongObjectHashMap<RunningEffect> m_effects;
    private boolean m_triggersEnabled;
    protected TriggerHandler m_triggerHandler;
    public static final byte CHECK_BEFORE_COMPUTATION = 1;
    public static final byte CHECK_OWN_BEFORE_COMPUTATION = 10;
    public static final byte CHECK_GLOBAL_BEFORE_COMPUTATION = 11;
    public static final byte CHECK_BEFORE_EXECUTION = 2;
    public static final byte CHECK_OWN_BEFORE_EXECUTION = 20;
    public static final byte CHECK_GLOBAL_BEFORE_EXECUTION = 21;
    public static final byte CHECK_AFTER_EXECUTION = 3;
    public static final byte CHECK_OWN_AFTER_EXECUTION = 30;
    public static final byte CHECK_GLOBAL_AFTER_EXECUTION = 31;
    public static final byte CHECK_UNAPPLICATION_TRIGGERS = 4;
    public static final byte CHECK_OWN_UNAPPLICATION_TRIGGERS = 40;
    public static final byte CHECK_GLOBAL_UNAPPLICATION_TRIGGERS = 41;
    public static final byte CHECK_AFTER_ALL_EXECUTIONS = 5;
    public static final byte CHECK_NOT_RELATED_TO_EXECUTION = 6;
    public static final byte CHECK_OWN_NOT_RELATED_TO_EXECUTION = 60;
    public static final byte CHECK_GLOBAL_NOT_RELATED_TO_EXECUTION = 61;
    private boolean m_doNotNotifyEffectUnapplication;
    
    public RunningEffectManager() {
        super();
        this.m_effects = new TLongObjectHashMap<RunningEffect>();
        this.m_triggerHandler = new TriggerHandler(this);
        this.m_doNotNotifyEffectUnapplication = false;
        this.m_triggersEnabled = true;
        this.createTriggerHandler();
    }
    
    protected void createTriggerHandler() {
        this.m_triggerHandler = new TriggerHandler(this);
    }
    
    public void clear() {
        for (final long key : this.m_effects.keys()) {
            final RunningEffect effect = this.m_effects.get(key);
            if (effect != null) {
                try {
                    effect.unapply(false);
                }
                catch (Exception e) {
                    RunningEffectManager.m_logger.error((Object)"Exception levee lors de la d\u00e9spplication d'un effet", (Throwable)e);
                }
            }
        }
        this.m_effects.clear();
    }
    
    public void destroyAll() {
        this.m_effects.forEachValue(RunningEffectManager.RELEASE_PROCEDURE);
        this.m_effects.clear();
    }
    
    public void disableTriggers() {
        this.m_triggersEnabled = false;
    }
    
    public void enableTriggers() {
        this.m_triggersEnabled = true;
    }
    
    public EffectUser getOwner() {
        return null;
    }
    
    public RunningEffect getRunningEffectFromUID(final long uid) {
        return this.m_effects.get(uid);
    }
    
    public void storeEffect(final RunningEffect effect) {
        if (!this.m_effects.containsKey(effect.getUniqueId())) {
            this.m_effects.put(effect.getUniqueId(), effect);
            effect.setManagerWhereIamStored(this);
        }
        else {
            assert effect == this.m_effects.get(effect.getUniqueId());
        }
    }
    
    public boolean removeEffect(final RunningEffect effect) {
        return this.removeEffect(effect.getUniqueId());
    }
    
    public void removeRunningEffects(final RunningEffect... effect) {
        if (effect != null && effect.length > 0) {
            for (final RunningEffect anEffect : effect) {
                this.removeEffect(anEffect);
            }
        }
    }
    
    public void onEffectRemoved(final RunningEffect removedEffect) {
        removedEffect.unapply();
    }
    
    public boolean removeEffect(final long uid) {
        final RunningEffect removedEffect = this.m_effects.remove(uid);
        if (removedEffect != null) {
            this.onEffectRemoved(removedEffect);
            return true;
        }
        return false;
    }
    
    public Iterable<RunningEffect> getEffectsWithContainerType(final int containerType) {
        final ArrayList<RunningEffect> result = new ArrayList<RunningEffect>();
        if (this.m_effects != null && !this.m_effects.isEmpty()) {
            this.m_effects.forEachValue((TObjectProcedure<RunningEffect>)new TObjectProcedure<RunningEffect>() {
                @Override
                public boolean execute(final RunningEffect effect) {
                    if (effect.getEffectContainer() != null && effect.getEffectContainer().getContainerType() == containerType) {
                        result.add(effect);
                    }
                    return true;
                }
            });
        }
        return result;
    }
    
    public List<RunningEffect> getEffectsWithActionId(final int actionId) {
        if (this.m_effects.isEmpty()) {
            return (List<RunningEffect>)Collections.emptyList();
        }
        final List<RunningEffect> result = new ArrayList<RunningEffect>();
        this.m_effects.forEachValue((TObjectProcedure<RunningEffect>)new TObjectProcedure<RunningEffect>() {
            @Override
            public boolean execute(final RunningEffect effect) {
                final Effect genericEffect = effect.getGenericEffect();
                if (genericEffect != null && genericEffect.getActionId() == actionId) {
                    result.add(effect);
                }
                return true;
            }
        });
        return result;
    }
    
    public Iterable<RunningEffect> getEffectsWithEffectId(final int effectId) {
        final ArrayList<RunningEffect> result = new ArrayList<RunningEffect>();
        if (this.m_effects != null && !this.m_effects.isEmpty()) {
            this.m_effects.forEachValue((TObjectProcedure<RunningEffect>)new TObjectProcedure<RunningEffect>() {
                @Override
                public boolean execute(final RunningEffect effect) {
                    final Effect genericEffect = effect.getGenericEffect();
                    if (genericEffect != null && genericEffect.getEffectId() == effectId) {
                        result.add(effect);
                    }
                    return true;
                }
            });
        }
        return result;
    }
    
    protected LinkedToEffectUserIterator getLinkedToEffectUserRunningEffects(final EffectUser user) {
        return LinkedToEffectUserIterator.checkOut(this, (TLongObjectIterator<RunningEffect>)this.m_effects.iterator(), user, false);
    }
    
    public LinkedToEffectUserIterator getTargetingEffectUserRunningEffects(final EffectUser user) {
        return LinkedToEffectUserIterator.checkOut(this, (TLongObjectIterator<RunningEffect>)this.m_effects.iterator(), user, true);
    }
    
    protected LinkedToEffectContainerIterator getLinkedToContainerRunningEffects(final EffectContainer container) {
        return new LinkedToEffectContainerIterator(this, (TLongObjectIterator<RunningEffect>)this.m_effects.iterator(), container);
    }
    
    public void removeChildEffect(final RunningEffect parentEffect) {
        try {
            final TLongObjectIterator<RunningEffect> it = (TLongObjectIterator<RunningEffect>)this.m_effects.iterator();
            while (it.hasNext()) {
                it.advance();
                final RunningEffect re = it.value();
                if (re.getParent() == parentEffect) {
                    it.remove();
                    re.unapply();
                }
            }
        }
        catch (ConcurrentModificationException e) {
            this.removeChildEffect(parentEffect);
        }
    }
    
    public void removeLinkedToCaster(final EffectUser caster) {
        try {
            final TLongObjectIterator<RunningEffect> it = (TLongObjectIterator<RunningEffect>)this.m_effects.iterator();
            while (it.hasNext()) {
                it.advance();
                final RunningEffect re = it.value();
                if (re.getCaster() == caster || re.getTarget() == caster) {
                    it.remove();
                    re.unapply();
                }
            }
        }
        catch (ConcurrentModificationException e) {
            this.removeLinkedToCaster(caster);
        }
    }
    
    public void removeLinkedToContainer(final EffectContainer container, final boolean withUnapplication) {
        try {
            final TLongObjectIterator<RunningEffect> it = (TLongObjectIterator<RunningEffect>)this.m_effects.iterator();
            while (it.hasNext()) {
                it.advance();
                final RunningEffect re = it.value();
                if (re.getEffectContainer() == container) {
                    it.remove();
                    if (!withUnapplication) {
                        continue;
                    }
                    re.unapply();
                }
            }
        }
        catch (ConcurrentModificationException e) {
            this.removeLinkedToContainer(container, withUnapplication);
        }
    }
    
    public void removeLinkedToContainer(final EffectContainer container, final boolean withUnapplication, final boolean notifyUnapplication) {
        throw new UnsupportedOperationException("Ne devrait pas \u00eatre appel\u00e9 si n'est pas impl\u00e9ment\u00e9");
    }
    
    public void removeLinkedToContainerType(final int containerType, final boolean withUnapplication) {
        try {
            final TLongObjectIterator<RunningEffect> it = (TLongObjectIterator<RunningEffect>)this.m_effects.iterator();
            while (it.hasNext()) {
                it.advance();
                final RunningEffect re = it.value();
                if (re.getEffectContainer() != null && re.getEffectContainer().getContainerType() == containerType) {
                    it.remove();
                    if (!withUnapplication) {
                        continue;
                    }
                    re.unapply();
                }
            }
        }
        catch (ConcurrentModificationException e) {
            this.removeLinkedToContainerType(containerType, withUnapplication);
        }
    }
    
    public void removeLinkedToEffect(final Effect effect) {
        try {
            final TLongObjectIterator<RunningEffect> it = (TLongObjectIterator<RunningEffect>)this.m_effects.iterator();
            while (it.hasNext()) {
                it.advance();
                final RunningEffect re = it.value();
                if (re.getGenericEffect() == effect) {
                    it.remove();
                    re.unapply();
                }
            }
        }
        catch (ConcurrentModificationException e) {
            this.removeLinkedToEffect(effect);
        }
    }
    
    @Override
    public Iterator<RunningEffect> iterator() {
        return new TroveLongHashMapValueIterator<RunningEffect>(this.m_effects);
    }
    
    @Override
    public boolean trigger(final BitSet triggers, final RunningEffect linkedRE, final byte check) {
        return this.m_triggersEnabled && this.m_triggerHandler.trigger(triggers, linkedRE, check);
    }
    
    public boolean isEmpty() {
        return this.m_effects.isEmpty();
    }
    
    public int size() {
        return this.m_effects.size();
    }
    
    public boolean containsWithSpecificId(final int effectSpecificId) {
        for (final RunningEffect runningEffect : this) {
            if (runningEffect.getGenericEffect() != null && runningEffect.getGenericEffect().getEffectId() == effectSpecificId) {
                return true;
            }
        }
        return false;
    }
    
    public void setDoNotNotifyEffectUnapplication(final boolean doNotNotifyEffectUnapplication) {
        this.m_doNotNotifyEffectUnapplication = doNotNotifyEffectUnapplication;
    }
    
    public boolean doNotNotifyEffectUnapplication() {
        return this.m_doNotNotifyEffectUnapplication;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RunningEffectManager.class);
        RELEASE_PROCEDURE = new TObjectProcedure<RunningEffect>() {
            @Override
            public boolean execute(final RunningEffect effect) {
                effect.release();
                return true;
            }
        };
    }
}
