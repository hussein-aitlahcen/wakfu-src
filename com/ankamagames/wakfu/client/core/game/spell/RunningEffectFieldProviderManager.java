package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.client.core.game.time.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.client.console.command.debug.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import org.jetbrains.annotations.*;

public class RunningEffectFieldProviderManager implements TimeTickListener
{
    private final THashMap<IntLongPair, RunningEffectFieldProvider> m_runningEffectFieldProviders;
    private long m_protectorContainerId;
    private long m_havenWorldContainerId;
    private static TObjectProcedure<RunningEffectFieldProvider> m_updateTimeProcedure;
    private static TObjectProcedure<RunningEffectFieldProvider> m_releaseProcedure;
    
    public RunningEffectFieldProviderManager() {
        super();
        this.m_runningEffectFieldProviders = new THashMap<IntLongPair, RunningEffectFieldProvider>();
        this.m_protectorContainerId = 0L;
        this.m_havenWorldContainerId = 1L;
    }
    
    @Nullable
    public RunningEffectFieldProvider getRunningEffectProvider(final RunningEffect re, final long remaining) {
        if (re == null) {
            return null;
        }
        EffectContainer<WakfuEffect> container = re.getEffectContainer();
        if (re.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
            container = ((StateRunningEffect)re).getState();
        }
        else if (re.getId() == RunningEffectConstants.SET_EFFECT_AREA.getId()) {
            container = ((SetEffectArea)re).getArea();
        }
        else if (re.getId() == RunningEffectConstants.SET_FECA_GLYPH.getId()) {
            container = ((SetEffectArea)re).getArea();
        }
        else if (re.getId() == RunningEffectConstants.SET_AURA.getId() || re.getId() == RunningEffectConstants.SET_AURA_ON_TARGET.getId()) {
            return null;
        }
        if (container == null) {
            return null;
        }
        if (container instanceof SpellLevel) {
            final Spell spell = ((SpellLevel)container).getSpell();
            if (spell == null || (!spell.isShownInTimeline() && !DisplayStatesCommand.forceDisplayStates())) {
                return null;
            }
        }
        final RunningEffectFieldProvider refp = this.getRunningEffectProvider(re);
        if (refp != null) {
            final boolean display = re.getGenericEffect() == null || re.getGenericEffect().isDisplayInStateBar();
            refp.addRunningEffect(re, display);
            if (remaining != -1L) {
                refp.setRemainingSec(remaining);
            }
        }
        return (refp == null || refp.getEffectsCount() == 0) ? null : refp;
    }
    
    private RunningEffectFieldProvider getRunningEffectProvider(final RunningEffect re) {
        final EffectContainer container = re.getEffectContainer();
        if (container == null) {
            return null;
        }
        long containerId = container.getEffectContainerId();
        if (container.getContainerType() == 19 || container.getContainerType() == 33) {
            if (this.m_protectorContainerId != 0L) {
                containerId = this.m_protectorContainerId;
            }
            else {
                this.m_protectorContainerId = containerId;
            }
        }
        final IntLongPair key = new IntLongPair(container.getContainerType(), containerId);
        RunningEffectFieldProvider refp = this.m_runningEffectFieldProviders.get(key);
        if (refp == null) {
            refp = RunningEffectFieldProvider.checkOut(container);
            this.m_runningEffectFieldProviders.put(key, refp);
        }
        else {
            refp.addEffectContainer(container);
        }
        return refp;
    }
    
    public void removeRunningEffectProvider(final RunningEffect re) {
        if (re == null) {
            return;
        }
        EffectContainer container = re.getEffectContainer();
        if (re.getId() == RunningEffectConstants.RUNNING_STATE.getId()) {
            container = ((StateRunningEffect)re).getState();
        }
        if (container == null) {
            return;
        }
        long containerId = container.getEffectContainerId();
        if ((container.getContainerType() == 19 || container.getContainerType() == 33) && this.m_protectorContainerId != 0L) {
            containerId = this.m_protectorContainerId;
        }
        if (container.getContainerType() == 28) {
            containerId = this.m_havenWorldContainerId;
        }
        final RunningEffectFieldProvider refp = this.m_runningEffectFieldProviders.get(containerId);
        if (refp != null) {
            refp.removeEffectContainer(container);
            if (!refp.hasEffectContainers()) {
                this.m_runningEffectFieldProviders.remove(containerId);
                refp.release();
                if (container.getContainerType() == 19 || container.getContainerType() == 33) {
                    this.m_protectorContainerId = 0L;
                }
            }
        }
    }
    
    public void removeRunningEffectProvider(final EffectContainer effectContainer) {
        final RunningEffectFieldProvider refp = this.m_runningEffectFieldProviders.remove(effectContainer.getEffectContainerId());
        if (refp != null) {
            refp.release();
        }
    }
    
    public void updateDuration() {
        this.m_runningEffectFieldProviders.forEachValue(RunningEffectFieldProviderManager.m_updateTimeProcedure);
    }
    
    public void clear() {
        this.m_protectorContainerId = 0L;
        this.m_runningEffectFieldProviders.forEachValue(RunningEffectFieldProviderManager.m_releaseProcedure);
        this.m_runningEffectFieldProviders.clear();
    }
    
    @Override
    public void tick() {
        this.updateDuration();
    }
    
    static {
        RunningEffectFieldProviderManager.m_updateTimeProcedure = new TObjectProcedure<RunningEffectFieldProvider>() {
            @Override
            public boolean execute(final RunningEffectFieldProvider runningEffectFieldProvider) {
                runningEffectFieldProvider.updateDuration();
                return true;
            }
        };
        RunningEffectFieldProviderManager.m_releaseProcedure = new TObjectProcedure<RunningEffectFieldProvider>() {
            @Override
            public boolean execute(final RunningEffectFieldProvider runningEffectFieldProvider) {
                runningEffectFieldProvider.release();
                return true;
            }
        };
    }
}
