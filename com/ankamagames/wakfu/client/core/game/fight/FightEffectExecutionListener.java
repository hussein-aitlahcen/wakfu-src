package com.ankamagames.wakfu.client.core.game.fight;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.specialEvent.*;

final class FightEffectExecutionListener implements EffectExecutionListener
{
    private static final Logger m_logger;
    private final Fight m_fight;
    
    FightEffectExecutionListener(final Fight fight) {
        super();
        this.m_fight = fight;
    }
    
    @Override
    public void onEffectDirectExecution(final RunningEffect effect) {
        final WakfuRunningEffect wre = (WakfuRunningEffect)effect;
        if (effect.getTarget() != null) {
            if (effect.getTarget().getRunningEffectManager() != null && !effect.mustBeTriggered() && effect.hasDuration() && !effect.mustStoreOnCaster()) {
                effect.getTarget().getRunningEffectManager().storeEffect(effect);
            }
            if (effect.getTarget() instanceof CharacterInfo) {
                ((CharacterInfo)effect.getTarget()).onSpecialFighterEvent(new EffectAppliedEvent(wre));
            }
        }
        EffectUser owner;
        if (effect.mustStoreOnCaster() || (effect.useTargetCell() && !effect.useTarget())) {
            owner = effect.getCaster();
        }
        else {
            owner = effect.getTarget();
        }
        if (owner == null) {
            FightEffectExecutionListener.m_logger.warn((Object)("Pas de cible sur laquelle appliqu\u00e9 les HMI pour l'effet " + effect.getId()));
        }
        if (owner != null && owner instanceof CharacterInfo) {
            ((CharacterInfo)owner).getActor().applyOnExecutionHMIAction((WakfuRunningEffect)effect, effect.useTargetCell());
        }
    }
    
    @Override
    public void onEffectTriggeredExecution(final RunningEffect effect) {
        final WakfuRunningEffect wre = (WakfuRunningEffect)effect;
        final EffectUser target = effect.getTarget();
        if (target != null && target instanceof CharacterInfo) {
            ((CharacterInfo)target).getActor().applyOnExecutionHMIAction(wre, false);
        }
        else if (effect.getCaster() != null && effect.getCaster() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getCaster()).getActor().applyOnExecutionHMIAction(wre, true);
        }
        if (wre.isItemEquip()) {
            return;
        }
        if (target != null && target instanceof CharacterInfo) {
            ((CharacterInfo)target).onSpecialFighterEvent(new EffectAppliedEvent(wre));
        }
    }
    
    @Override
    public void onEffectApplication(final RunningEffect effect) {
        final EffectUser target = effect.getTarget();
        if (target != null && target instanceof CharacterInfo) {
            ((CharacterInfo)target).getActor().applyOnApplicationHMIAction((WakfuRunningEffect)effect, false);
        }
        else if (effect.getCaster() != null && effect.getCaster() instanceof CharacterInfo) {
            ((CharacterInfo)effect.getCaster()).getActor().applyOnApplicationHMIAction((WakfuRunningEffect)effect, true);
        }
        if (((WakfuRunningEffect)effect).isItemEquip()) {
            return;
        }
        if (target == null) {
            return;
        }
        if (target instanceof CharacterInfo && effect.hasDuration()) {
            ((CharacterInfo)target).onSpecialFighterEvent(new EffectAppliedEvent((WakfuRunningEffect)effect));
        }
    }
    
    @Override
    public void onEffectUnApplication(final RunningEffect effect) {
        if (!effect.hasDuration()) {
            return;
        }
        final EffectUser owner = (effect.getManagerWhereIamStored() == null) ? null : effect.getManagerWhereIamStored().getOwner();
        if (owner != null && owner instanceof CharacterInfo) {
            ((CharacterInfo)owner).getActor().applyOnUnapplicationHMIAction((WakfuRunningEffect)effect, false);
        }
        if (effect.getTarget() != null && effect.getTarget() instanceof CharacterInfo && effect.hasDuration()) {
            ((CharacterInfo)effect.getTarget()).onSpecialFighterEvent(new EffectUnappliedEvent((WakfuRunningEffect)effect));
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightEffectExecutionListener.class);
    }
}
