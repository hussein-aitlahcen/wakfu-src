package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.game.fighter.specialEvent.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class LocalTimedRunningEffectManager extends TimedRunningEffectManager
{
    private long m_clockId;
    private int m_currentTime;
    private final TLongLongHashMap m_timed;
    private RunningEffectFieldProviderManager m_fieldProviderRef;
    
    public LocalTimedRunningEffectManager(final EffectUser owner) {
        super(owner);
        this.m_timed = new TLongLongHashMap();
    }
    
    @Override
    public void clear() {
        this.m_timed.clear();
        if (this.m_fieldProviderRef != null) {
            this.m_fieldProviderRef.clear();
        }
        super.clear();
    }
    
    @Override
    public void destroyAll() {
        this.m_timed.clear();
        if (this.m_fieldProviderRef != null) {
            this.m_fieldProviderRef.clear();
        }
        super.destroyAll();
    }
    
    @Override
    public void removeManager() {
        MessageScheduler.getInstance().removeClock(this.m_clockId);
        super.removeManager();
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (!(message instanceof ClockMessage)) {
            return false;
        }
        if (((ClockMessage)message).getClockId() != this.m_clockId) {
            return super.onMessage(message);
        }
        if (!this.m_timed.isEmpty()) {
            ++this.m_currentTime;
            final HashMap<Long, RunningEffect> pairs = new HashMap<Long, RunningEffect>();
            for (final long id : this.m_timed.keys()) {
                final RunningEffect effect = this.m_effects.get(id);
                if (effect != null) {
                    pairs.put(this.getRemainingSec(id), effect);
                }
            }
            if (WakfuClientInstance.getGameEntity().getLocalPlayer() != null) {
                WakfuClientInstance.getGameEntity().getLocalPlayer().onSpecialFighterEvent(new EffectModifiedEvent(pairs));
            }
            return true;
        }
        return true;
    }
    
    @Override
    public boolean removeEffect(final long uid, final boolean withoutException) {
        this.m_timed.remove(uid);
        return super.removeEffect(uid, withoutException);
    }
    
    @Override
    public void storeEffect(final RunningEffect reffect) {
        long delay = 0L;
        final WakfuRunningEffect effect = (WakfuRunningEffect)reffect;
        if (effect.getContext() != null) {
            if (!effect.isInfinite() && effect.hasDurationInMs()) {
                delay = effect.getRemainingTimeInMs();
                this.m_timed.put(effect.getUniqueId(), this.m_currentTime + delay / 1000L);
            }
            super.storeEffect(reffect);
        }
    }
    
    @Override
    public void initialize(final EffectUser target, final EffectContext context) {
        this.m_clockId = MessageScheduler.getInstance().addClock(this, 1000L, 0);
        this.m_currentTime = 0;
        this.m_fieldProviderRef = null;
        super.initialize(target, context);
    }
    
    public void setFieldProviderRef(final RunningEffectFieldProviderManager fieldProviderRef) {
        this.m_fieldProviderRef = fieldProviderRef;
    }
    
    public long getRemainingSec(final long effectId) {
        if (this.m_timed.get(effectId) > this.m_currentTime) {
            return this.m_timed.get(effectId) - this.m_currentTime;
        }
        return -1L;
    }
}
