package com.ankamagames.wakfu.common.game.effect.runningEffect.manager;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;

final class WakfuTriggerHandler extends TriggerHandler
{
    WakfuTriggerHandler(final RunningEffectManager runningEffectManager) {
        super(runningEffectManager);
    }
    
    @Override
    protected void executeTriggeredEffects(final RunningEffect linkedRE, final byte check) {
        final TLongArrayList effectToDeactivate = new TLongArrayList(this.m_effectToDeactivate.toNativeArray());
        final TLongArrayList effectToActivate = new TLongArrayList(this.m_effectToActivate.toNativeArray());
        final TLongObjectHashMap<RunningEffect> effectToUse = (TLongObjectHashMap<RunningEffect>)this.m_effectToUse.clone();
        this.m_effectToDeactivate.clear();
        this.m_effectToActivate.clear();
        this.m_effectToUse.clear();
        this.unapplyEffects(effectToDeactivate, effectToUse);
        this.executeEffects(linkedRE, effectToActivate, effectToUse);
    }
    
    private void executeEffects(final RunningEffect linkedRE, final TLongArrayList effectToActivate, final TLongObjectHashMap<RunningEffect> effectToUse) {
        final boolean shouldResetApplyCount = linkedRE != null && EffectTriggersResetingLimitingCounter.containsResetingTrigger(linkedRE.getTriggersToExecute());
        for (int i = 0, n = effectToActivate.size(); i < n; ++i) {
            final long effectUid = effectToActivate.get(i);
            final RunningEffect re = effectToUse.get(effectUid);
            if (re != null) {
                if (re.getUniqueId() == effectUid) {
                    if (!re.hasBeenUnnaplied()) {
                        if (!re.isReleased()) {
                            if (shouldResetApplyCount) {
                                RunningEffect.resetLimitedApplyCount();
                            }
                            re.askForTriggeredExecution(linkedRE);
                        }
                    }
                }
            }
        }
    }
    
    private void unapplyEffects(final TLongArrayList effectToDeactivate, final TLongObjectHashMap<RunningEffect> effectToUse) {
        for (int i = 0, n = effectToDeactivate.size(); i < n; ++i) {
            final long effectUid = effectToDeactivate.get(i);
            final RunningEffect runningEffect = effectToUse.get(effectUid);
            if (runningEffect != null && runningEffect.getUniqueId() == effectUid) {
                runningEffect.askForTriggeredUnapplication();
            }
        }
    }
    
    @Override
    protected boolean checkGlobalTriggers(final byte check, final RunningEffect re, final BitSet triggers) {
        final WakfuRunningEffect wre = (WakfuRunningEffect)re;
        if (!wre.isGlobalTriggerListener()) {
            return false;
        }
        boolean somethingWasTriggered = false;
        switch (check) {
            case 31: {
                if (wre.getListeningTriggersAfterExecution() != null && wre.getListeningTriggersAfterExecution().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 11: {
                if (re.getListeningTriggersBeforeComputation() != null && re.getListeningTriggersBeforeComputation().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 21: {
                if (re.getListeningTriggersBeforeExecution() != null && re.getListeningTriggersBeforeExecution().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 61: {
                if (re.getListeningTriggesrNotRelatedToExecutions() != null && re.getListeningTriggesrNotRelatedToExecutions().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 41: {
                if (re.getListeningTriggerForUnapplication() != null && re.getListeningTriggerForUnapplication().intersects(triggers)) {
                    this.addEffectToDeactivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
        }
        return somethingWasTriggered;
    }
    
    @Override
    protected boolean isCheckGlobalTriggers(final RunningEffect re) {
        final WakfuRunningEffect wre = (WakfuRunningEffect)re;
        return wre.isGlobalTriggerListener();
    }
    
    @Override
    protected boolean checkEffectsToTrigger(final BitSet triggers, final RunningEffect linkedRE, final byte check) {
        if (linkedRE != null && ((WakfuRunningEffect)linkedRE).hasProperty(RunningEffectPropertyType.DONT_TRIGGER_ANYTHING_LEVEL_2)) {
            return false;
        }
        boolean somethingWasTriggered = false;
        for (final WakfuRunningEffect re : this.m_runningEffectManager) {
            if (linkedRE != re) {
                if (linkedRE != null && linkedRE.getParent() == re) {
                    continue;
                }
                if (linkedRE != null && linkedRE.isDontTriggerAnythingForced()) {
                    continue;
                }
                if (linkedRE != null && linkedRE.dontTriggerAnything() && !re.hasProperty(RunningEffectPropertyType.ALWAYS_TRIGGER_LEVEL_1)) {
                    continue;
                }
                if (this.isCheckGlobalTriggers(re)) {
                    somethingWasTriggered |= this.checkGlobalTriggers(check, re, triggers);
                }
                else if (re.isSelfTrigger()) {
                    somethingWasTriggered |= this.checkSelfTriggers(triggers, check, re);
                }
                else {
                    somethingWasTriggered |= this.checkNotSelfTriggers(triggers, check, re);
                }
            }
        }
        return somethingWasTriggered;
    }
}
