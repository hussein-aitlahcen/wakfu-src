package com.ankamagames.wakfu.client.core.effect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.fighter.specialEvent.*;

public final class UniqueEffectUserExecutionListener implements EffectExecutionListener
{
    private final CharacterInfo m_user;
    
    public UniqueEffectUserExecutionListener(final CharacterInfo user) {
        super();
        this.m_user = user;
    }
    
    @Override
    public void onEffectDirectExecution(final RunningEffect effect) {
        if (effect.getTarget() != null && effect.getTarget() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getTarget()).getActor().applyOnExecutionHMIAction((WakfuRunningEffect)effect, false);
        }
        else if (effect.getCaster() != null && effect.getCaster() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getCaster()).getActor().applyOnExecutionHMIAction((WakfuRunningEffect)effect, true);
        }
        if (!((WakfuRunningEffect)effect).isItemEquip() && effect.getTarget() != null) {
            if (effect.getTarget().getRunningEffectManager() != null && !effect.mustBeTriggered() && effect.hasDuration()) {
                effect.getTarget().getRunningEffectManager().storeEffect(effect);
            }
            if (effect.getTarget() instanceof CharacterInfo) {
                ((CharacterInfo)effect.getTarget()).onSpecialFighterEvent(new EffectAppliedEvent((WakfuRunningEffect)effect));
            }
        }
    }
    
    @Override
    public void onEffectUnApplication(final RunningEffect effect) {
        this.m_user.getActor().applyOnUnapplicationHMIAction((WakfuRunningEffect)effect, false);
        if (!((WakfuRunningEffect)effect).isItemEquip() && effect.hasDuration()) {
            final CharacterInfo target = (CharacterInfo)effect.getTarget();
            if (target != null) {
                target.onSpecialFighterEvent(new EffectUnappliedEvent((WakfuRunningEffect)effect));
            }
        }
    }
    
    @Override
    public void onEffectTriggeredExecution(final RunningEffect effect) {
    }
    
    @Override
    public void onEffectApplication(final RunningEffect effect) {
        this.m_user.getActor().applyOnApplicationHMIAction((WakfuRunningEffect)effect, false);
        if (!((WakfuRunningEffect)effect).isItemEquip() && effect.hasDuration()) {
            ((CharacterInfo)effect.getTarget()).onSpecialFighterEvent(new EffectAppliedEvent((WakfuRunningEffect)effect));
        }
    }
}
