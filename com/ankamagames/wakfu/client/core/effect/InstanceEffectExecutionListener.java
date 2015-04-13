package com.ankamagames.wakfu.client.core.effect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.fighter.specialEvent.*;

final class InstanceEffectExecutionListener implements EffectExecutionListener
{
    @Override
    public void onEffectApplication(final RunningEffect effect) {
        if (effect.getTarget() != null && effect.getTarget() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getTarget()).getActor().applyOnApplicationHMIAction((WakfuRunningEffect)effect, false);
        }
        else if (effect.getCaster() != null && effect.getCaster() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getCaster()).getActor().applyOnApplicationHMIAction((WakfuRunningEffect)effect, true);
        }
        if (((WakfuRunningEffect)effect).isItemEquip()) {
            return;
        }
        if (effect.getTarget() != null && effect.getTarget() instanceof CharacterInfo && effect.hasDuration()) {
            ((CharacterInfo)effect.getTarget()).onSpecialFighterEvent(new EffectAppliedEvent((WakfuRunningEffect)effect));
        }
    }
    
    @Override
    public void onEffectDirectExecution(final RunningEffect effect) {
        if (effect.getTarget() != null && effect.getTarget() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getTarget()).getActor().applyOnExecutionHMIAction((WakfuRunningEffect)effect, false);
        }
        else if (effect.getCaster() != null && effect.getCaster() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getCaster()).getActor().applyOnExecutionHMIAction((WakfuRunningEffect)effect, true);
        }
        if (((WakfuRunningEffect)effect).isItemEquip()) {
            return;
        }
        if (effect.getTarget() == null) {
            return;
        }
        if (effect.getTarget().getRunningEffectManager() != null && !effect.mustBeTriggered() && effect.hasDuration()) {
            effect.getTarget().getRunningEffectManager().storeEffect(effect);
        }
        if (effect.getTarget() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getTarget()).onSpecialFighterEvent(new EffectAppliedEvent((WakfuRunningEffect)effect));
        }
    }
    
    @Override
    public void onEffectTriggeredExecution(final RunningEffect effect) {
    }
    
    @Override
    public void onEffectUnApplication(final RunningEffect effect) {
        if (effect.getTarget() != null && effect.getTarget() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getTarget()).getActor().applyOnUnapplicationHMIAction((WakfuRunningEffect)effect, false);
        }
        else if (effect.getCaster() != null && effect.getCaster() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getCaster()).getActor().applyOnUnapplicationHMIAction((WakfuRunningEffect)effect, true);
        }
        if (!((WakfuRunningEffect)effect).isItemEquip() && effect.getTarget() != null && effect.getTarget() instanceof CharacterInfo && effect.hasDuration()) {
            ((CharacterInfo)effect.getTarget()).onSpecialFighterEvent(new EffectUnappliedEvent((WakfuRunningEffect)effect));
        }
    }
}
