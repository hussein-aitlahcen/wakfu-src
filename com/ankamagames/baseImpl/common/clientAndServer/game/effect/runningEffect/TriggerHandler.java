package com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect;

import gnu.trove.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents.*;

public class TriggerHandler
{
    protected RunningEffectManager m_runningEffectManager;
    protected TLongArrayList m_effectToActivate;
    protected TLongArrayList m_effectToDeactivate;
    protected TLongObjectHashMap<RunningEffect> m_effectToUse;
    
    protected TriggerHandler(final RunningEffectManager runningEffectManager) {
        super();
        this.m_effectToActivate = new TLongArrayList();
        this.m_effectToDeactivate = new TLongArrayList();
        this.m_effectToUse = new TLongObjectHashMap<RunningEffect>();
        this.m_runningEffectManager = runningEffectManager;
    }
    
    public boolean trigger(final BitSet triggers, final RunningEffect linkedRE, final byte check) {
        this.clearEffectLists();
        final boolean triggeredSomething = this.checkEffectsToTrigger(triggers, linkedRE, check);
        if (triggeredSomething) {
            this.executeTriggeredEffects(linkedRE, check);
        }
        this.clearEffectLists();
        return triggeredSomething;
    }
    
    private void clearEffectLists() {
        this.m_effectToActivate.clear();
        this.m_effectToDeactivate.clear();
        this.m_effectToUse.clear();
    }
    
    protected boolean checkEffectsToTrigger(final BitSet triggers, final RunningEffect linkedRE, final byte check) {
        if (linkedRE != null && linkedRE.dontTriggerAnything()) {
            return false;
        }
        boolean somethingWasTriggered = false;
        for (final RunningEffect re : this.m_runningEffectManager) {
            if (linkedRE != re) {
                if (linkedRE != null && linkedRE.getParent() == re) {
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
    
    protected void executeTriggeredEffects(final RunningEffect linkedRE, final byte check) {
        final TLongArrayList effectToDeactivate = new TLongArrayList(this.m_effectToDeactivate.toNativeArray());
        final TLongArrayList effectToActivate = new TLongArrayList(this.m_effectToActivate.toNativeArray());
        final TLongObjectHashMap<RunningEffect> effectToUse = (TLongObjectHashMap<RunningEffect>)this.m_effectToUse.clone();
        this.m_effectToDeactivate.clear();
        this.m_effectToActivate.clear();
        this.m_effectToUse.clear();
        for (int i = 0, n = effectToDeactivate.size(); i < n; ++i) {
            final long effectUid = effectToDeactivate.get(i);
            final RunningEffect runningEffect = effectToUse.get(effectUid);
            if (runningEffect != null && runningEffect.getUniqueId() == effectUid) {
                runningEffect.askForTriggeredUnapplication();
            }
        }
        if (check == 10 || check == 1) {
            AbsoluteFightTime runningEffectCastingTime = null;
            RunningEffect runningEffectToActivate = null;
            for (int j = 0, n2 = effectToActivate.size(); j < n2; ++j) {
                final long effectUid2 = effectToActivate.get(j);
                final RunningEffect re = effectToUse.get(effectUid2);
                if (re != null) {
                    if (re.getUniqueId() == effectUid2) {
                        if (runningEffectCastingTime == null || runningEffectCastingTime.compareTo(re.getStartTime()) < 0) {
                            runningEffectCastingTime = re.getStartTime();
                            runningEffectToActivate = re;
                        }
                    }
                }
            }
            if (runningEffectToActivate != null) {
                runningEffectToActivate.askForTriggeredExecution(linkedRE);
            }
        }
        else {
            for (int i = 0, n = effectToActivate.size(); i < n; ++i) {
                final long effectUid = effectToActivate.get(i);
                final RunningEffect re2 = effectToUse.get(effectUid);
                if (re2 != null) {
                    if (re2.getUniqueId() == effectUid) {
                        re2.askForTriggeredExecution(linkedRE);
                    }
                }
            }
        }
    }
    
    protected boolean checkNotSelfTriggers(final BitSet triggers, final byte check, final RunningEffect re) {
        boolean somethingWasTriggered = false;
        switch (check) {
            case 1: {
                if (re.getListeningTriggersBeforeComputation() != null && re.getListeningTriggersBeforeComputation().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 2: {
                if (re.getListeningTriggersBeforeExecution() != null && re.getListeningTriggersBeforeExecution().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 3: {
                if (re.getListeningTriggersAfterExecution() != null && re.getListeningTriggersAfterExecution().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 5: {
                if (re.getListeningTriggesrAfterAllExecutions() != null && re.getListeningTriggesrAfterAllExecutions().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 6: {
                if (re.getListeningTriggesrNotRelatedToExecutions() != null && re.getListeningTriggesrNotRelatedToExecutions().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 4: {
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
    
    protected boolean checkSelfTriggers(final BitSet triggers, final byte check, final RunningEffect re) {
        boolean somethingWasTriggered = false;
        switch (check) {
            case 10: {
                if (re.getListeningTriggersBeforeComputation() != null && re.getListeningTriggersBeforeComputation().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 20: {
                if (re.getListeningTriggersBeforeExecution() != null && re.getListeningTriggersBeforeExecution().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 30: {
                if (re.getListeningTriggersAfterExecution() != null && re.getListeningTriggersAfterExecution().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 60: {
                if (re.getListeningTriggesrNotRelatedToExecutions() != null && re.getListeningTriggesrNotRelatedToExecutions().intersects(triggers)) {
                    this.addEffectToActivate(re);
                    somethingWasTriggered = true;
                    break;
                }
                break;
            }
            case 40: {
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
    
    protected void addEffectToActivate(final RunningEffect re) {
        this.m_effectToActivate.add(re.getUniqueId());
        this.m_effectToUse.put(re.getUniqueId(), re);
    }
    
    protected void addEffectToDeactivate(final RunningEffect re) {
        this.m_effectToDeactivate.add(re.getUniqueId());
        this.m_effectToUse.put(re.getUniqueId(), re);
    }
    
    protected boolean isCheckGlobalTriggers(final RunningEffect re) {
        return false;
    }
    
    protected boolean checkGlobalTriggers(final byte check, final RunningEffect re, final BitSet triggers) {
        return false;
    }
}
