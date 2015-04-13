package com.ankamagames.wakfu.common.game.fighter;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class EffectProbabilityModificators
{
    public static final Logger m_logger;
    private static final TIntObjectHashMap<Modificator> m_modificators;
    
    public static boolean isEffectConcerned(final RunningEffect effect) {
        return effect != null && effect.getCaster() != effect.getTarget() && !((WakfuRunningEffect)effect).hasProperty(RunningEffectPropertyType.DONT_APPLY_PROBABILITY_MODIFCATOR) && EffectProbabilityModificators.m_modificators.contains(effect.getId());
    }
    
    public static int getProbabilityModif(final RunningEffect effect) {
        if (effect == null) {
            return 0;
        }
        final Modificator modificator = getActionModificator(effect);
        if (modificator == null) {
            return 0;
        }
        final int casterBoost = modificator.getBoostFrom(effect.getCaster());
        final int targetDeboost = modificator.getDeboostFrom(effect.getTarget());
        return casterBoost - targetDeboost;
    }
    
    public static void applyProbabilityManagementState(final RunningEffect effect) {
        if (effect == null) {
            return;
        }
        final Modificator modificator = getActionModificator(effect);
        if (modificator == null) {
            return;
        }
        if (modificator.m_stateId <= 0) {
            return;
        }
        if (modificator.m_levelIncrementPerApplication <= 0) {
            return;
        }
        final ApplyState applyState = ApplyState.checkout(effect.getContext(), effect.getTarget(), modificator.m_stateId, (short)0, false);
        applyState.setTarget(effect.getTarget());
        applyState.setParent(effect);
        applyState.setCaster(effect.getTarget());
        final float[] params = { modificator.m_stateId, 0.0f, modificator.m_levelIncrementPerApplication, 0.0f, 100.0f, 0.0f };
        final State state = StateManager.getInstance().getState(modificator.m_stateId);
        int stateMaxLevel;
        if (state != null) {
            stateMaxLevel = state.getMaxlevel();
        }
        else {
            EffectProbabilityModificators.m_logger.error((Object)("UNable to find state " + modificator.m_stateId + " for an EffectProbabilityMOdificator"));
            stateMaxLevel = 200;
        }
        final DefaultFightOneFullTurnEffect fakeGenericEffect = DefaultFightOneFullTurnEffect.makeWithParamsAndMaxLevel(params, stateMaxLevel);
        ((RunningEffect<DefaultFightOneFullTurnEffect, EC>)applyState).setGenericEffect(fakeGenericEffect);
        applyState.setContext(effect.getContext());
        applyState.bypassResistancesCheck();
        applyState.applyOnTargets(effect.getTarget());
        applyState.release();
    }
    
    private static void register(final Modificator modif) {
        EffectProbabilityModificators.m_modificators.put(modif.m_actionId, modif);
    }
    
    private static Modificator getActionModificator(final RunningEffect effect) {
        if (effect == null) {
            return null;
        }
        return EffectProbabilityModificators.m_modificators.get(effect.getId());
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectProbabilityModificators.class);
        m_modificators = new TIntObjectHashMap<Modificator>();
        register(new Modificator(RunningEffectConstants.AP_DEBOOST, FighterCharacteristicType.AP_DEBUFF_POWER, FighterCharacteristicType.AP_DEBUFF_RES, 993, 20));
        register(new Modificator(RunningEffectConstants.AP_LOSS, FighterCharacteristicType.AP_DEBUFF_POWER, FighterCharacteristicType.AP_DEBUFF_RES, 993, 20));
        register(new Modificator(RunningEffectConstants.AP_LEECH, FighterCharacteristicType.AP_DEBUFF_POWER, FighterCharacteristicType.AP_DEBUFF_RES, 993, 20));
        register(new Modificator(RunningEffectConstants.AP_LEECH_IN_PERCENT, FighterCharacteristicType.AP_DEBUFF_POWER, FighterCharacteristicType.AP_DEBUFF_RES, 993, 20));
        register(new Modificator(RunningEffectConstants.MP_DEBOOST, FighterCharacteristicType.MP_DEBUFF_POWER, FighterCharacteristicType.MP_DEBUFF_RES, 1038, 20));
        register(new Modificator(RunningEffectConstants.MP_LOSS, FighterCharacteristicType.MP_DEBUFF_POWER, FighterCharacteristicType.MP_DEBUFF_RES, 1038, 20));
        register(new Modificator(RunningEffectConstants.MP_LEECH, FighterCharacteristicType.MP_DEBUFF_POWER, FighterCharacteristicType.MP_DEBUFF_RES, 1038, 20));
    }
    
    private static class Modificator
    {
        final int m_actionId;
        final FighterCharacteristicType m_probabilityBoostCharac;
        final FighterCharacteristicType m_probabilityDeboostCharac;
        final short m_stateId;
        final short m_levelIncrementPerApplication;
        
        private Modificator(final RunningEffectDefinition def, final FighterCharacteristicType probabilityBoostCharac, final FighterCharacteristicType probabilityDeboostCharac, final int stateId, final int levelIncrementPerApplication) {
            super();
            this.m_actionId = def.getId();
            this.m_probabilityBoostCharac = probabilityBoostCharac;
            this.m_probabilityDeboostCharac = probabilityDeboostCharac;
            this.m_stateId = (short)stateId;
            this.m_levelIncrementPerApplication = (short)levelIncrementPerApplication;
        }
        
        public int getBoostFrom(final EffectUser effectUser) {
            if (this.m_probabilityBoostCharac == null) {
                return 0;
            }
            if (effectUser == null) {
                return 0;
            }
            final AbstractCharacteristic characteristic = effectUser.getCharacteristic(this.m_probabilityBoostCharac);
            if (characteristic == null) {
                return 0;
            }
            return characteristic.value();
        }
        
        public int getDeboostFrom(final EffectUser effectUser) {
            if (this.m_probabilityDeboostCharac == null) {
                return 0;
            }
            if (effectUser == null) {
                return 0;
            }
            final AbstractCharacteristic characteristic = effectUser.getCharacteristic(this.m_probabilityDeboostCharac);
            if (characteristic == null) {
                return 0;
            }
            return characteristic.value();
        }
    }
}
