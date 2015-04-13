package com.ankamagames.wakfu.common.game.spell;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import gnu.trove.*;
import java.nio.*;

public class SpellLevelCastHistory
{
    private TIntIntHashMap m_lastTurnSpellCast;
    private TIntIntHashMap m_spellCastedThisTurnCount;
    private TLongIntHashMap m_spellCastedThisTurnOnTargetCount;
    private boolean m_storeAllSpellCasts;
    
    public SpellLevelCastHistory() {
        super();
        this.m_storeAllSpellCasts = false;
    }
    
    public void onNewTurn() {
        if (this.m_spellCastedThisTurnCount != null) {
            this.m_spellCastedThisTurnCount.clear();
        }
        if (this.m_spellCastedThisTurnOnTargetCount != null) {
            this.m_spellCastedThisTurnOnTargetCount.clear();
        }
    }
    
    public int getLastTurnSpellCast(final int spellId) {
        if (this.m_lastTurnSpellCast == null) {
            return -1;
        }
        if (this.m_lastTurnSpellCast.containsKey(spellId)) {
            return this.m_lastTurnSpellCast.get(spellId);
        }
        return -1;
    }
    
    private void storeSpellCast(final AbstractSpellLevel spellLevel, final int currentTableTurn) {
        if (!this.m_storeAllSpellCasts && getSpellCastMinInterval(spellLevel) == 0) {
            return;
        }
        this.ensureSpellLevelCasted();
        this.m_lastTurnSpellCast.put(spellLevel.getReferenceId(), currentTableTurn);
    }
    
    private void storeSpellCastThisTurn(final AbstractSpellLevel spellLevel) {
        if (!this.m_storeAllSpellCasts && spellLevel.getCastMaxPerTurn() <= 0) {
            return;
        }
        this.ensureSpellLevelCastedThisTurn();
        this.m_spellCastedThisTurnCount.adjustOrPutValue(spellLevel.getReferenceId(), 1, 1);
    }
    
    private void storeSpellCastOnTargetThisTurn(final AbstractSpellLevel spellLevel, final Target target) {
        if (target == null || (!this.m_storeAllSpellCasts && spellLevel.getSpell().getCastMaxPerTarget() <= 0)) {
            return;
        }
        this.ensureSpellsCastedThisTurnOnTarget();
        final long hash = getSpellOnTargetHashCode(spellLevel, target);
        this.m_spellCastedThisTurnOnTargetCount.adjustOrPutValue(hash, 1, 1);
    }
    
    public void storeSpellCastForTargets(final AbstractSpellLevel spellLevel, final int currentTableTurn, final List<? extends EffectUser> targets) {
        this.storeSpellCast(spellLevel, currentTableTurn);
        this.storeSpellCastThisTurn(spellLevel);
        for (final EffectUser target : targets) {
            this.addVoodoolTarget(spellLevel, targets, target);
            this.storeSpellCastOnTargetThisTurn(spellLevel, target);
        }
    }
    
    private void addVoodoolTarget(final AbstractSpellLevel spellLevel, final List<? extends EffectUser> targets, final EffectUser target) {
        if (target.getRunningEffectManager() == null) {
            return;
        }
        final Iterable<RunningEffect> voodoolSplit = getVoodoolSplitEffects(target);
        for (final RunningEffect effect : voodoolSplit) {
            final EffectUser voodoolTarget = effect.getTarget();
            if (!targets.contains(voodoolTarget)) {
                this.storeSpellCastOnTargetThisTurn(spellLevel, voodoolTarget);
            }
        }
    }
    
    private static Iterable<RunningEffect> getVoodoolSplitEffects(final EffectUser target) {
        return target.getRunningEffectManager().getEffectsWithActionId(RunningEffectConstants.VOODOOL_SPLIT_EFFECT.getId());
    }
    
    public int getTurnsBeforeBeingAbleToCastSpell(final AbstractSpellLevel spellLevel, final int currentTableTurn) {
        if (getSpellCastMinInterval(spellLevel) == 0) {
            return 0;
        }
        if (this.m_lastTurnSpellCast == null) {
            return 0;
        }
        if (!this.m_lastTurnSpellCast.contains(spellLevel.getReferenceId())) {
            return 0;
        }
        return getSpellCastMinInterval(spellLevel) - (currentTableTurn - this.m_lastTurnSpellCast.get(spellLevel.getReferenceId()));
    }
    
    public CastValidity canCastSpell(final AbstractSpellLevel spellLevel, final int currentTableTurn) {
        return this.canCastSpell(spellLevel, currentTableTurn, null);
    }
    
    public CastValidity canCastSpell(final AbstractSpellLevel spellLevel, final int currentTableTurn, final Target target) {
        if (getSpellCastMinInterval(spellLevel) != 0 && this.m_lastTurnSpellCast != null && this.m_lastTurnSpellCast.contains(spellLevel.getReferenceId())) {
            if (getSpellCastMinInterval(spellLevel) < 0 || currentTableTurn - this.m_lastTurnSpellCast.get(spellLevel.getReferenceId()) < getSpellCastMinInterval(spellLevel)) {
                return CastValidity.LAST_CAST_TOO_RECENT;
            }
            this.m_lastTurnSpellCast.remove(spellLevel.getReferenceId());
        }
        if (spellLevel.getCastMaxPerTurn() > 0 && this.m_spellCastedThisTurnCount != null && this.m_spellCastedThisTurnCount.contains(spellLevel.getReferenceId())) {
            final int castsCount = this.m_spellCastedThisTurnCount.get(spellLevel.getReferenceId());
            if (castsCount >= spellLevel.getCastMaxPerTurn()) {
                return CastValidity.TOO_MUCH_CASTS_THIS_TURN;
            }
        }
        if (target != null && spellLevel.getSpell().getCastMaxPerTarget() > 0 && this.m_spellCastedThisTurnOnTargetCount != null) {
            final long hash = getSpellOnTargetHashCode(spellLevel, target);
            final Integer castsCount2 = this.m_spellCastedThisTurnOnTargetCount.get(hash);
            if (castsCount2 >= spellLevel.getSpell().getCastMaxPerTarget()) {
                return CastValidity.TOO_MUCH_CASTS_ON_THIS_TARGET;
            }
        }
        return CastValidity.OK;
    }
    
    protected static byte getSpellCastMinInterval(final AbstractSpellLevel spellLevel) {
        return spellLevel.getSpell().getCastMinInterval();
    }
    
    private static long getSpellOnTargetHashCode(final AbstractSpellLevel spellLevel, final Target target) {
        return spellLevel.getSpell().getId() << 32 | HashFunctions.hash(target);
    }
    
    TIntIntHashMap getLastTurnSpellCast() {
        return this.m_lastTurnSpellCast;
    }
    
    TIntIntHashMap getSpellCastedThisTurnCount() {
        return this.m_spellCastedThisTurnCount;
    }
    
    TLongIntHashMap getSpellCastedThisTurnOnTargetCount() {
        return this.m_spellCastedThisTurnOnTargetCount;
    }
    
    public byte[] serialize() {
        return SpellCastHistorySerializer.serialize(this);
    }
    
    public void unserialize(final ByteBuffer buf) {
        SpellCastHistorySerializer.unserialize(buf, this);
    }
    
    public void setStoreAllSpellCasts(final boolean storeAllSpellCasts) {
        this.m_storeAllSpellCasts = storeAllSpellCasts;
    }
    
    void ensureSpellLevelCasted() {
        if (this.m_lastTurnSpellCast == null) {
            this.m_lastTurnSpellCast = new TIntIntHashMap(3);
        }
    }
    
    void ensureSpellLevelCastedThisTurn() {
        if (this.m_spellCastedThisTurnCount == null) {
            this.m_spellCastedThisTurnCount = new TIntIntHashMap(3);
        }
    }
    
    void ensureSpellsCastedThisTurnOnTarget() {
        if (this.m_spellCastedThisTurnOnTargetCount == null) {
            this.m_spellCastedThisTurnOnTargetCount = new TLongIntHashMap(3);
        }
    }
    
    public void reset() {
        if (this.m_lastTurnSpellCast != null) {
            this.m_lastTurnSpellCast.clear();
        }
        if (this.m_spellCastedThisTurnCount != null) {
            this.m_spellCastedThisTurnCount.clear();
        }
        if (this.m_spellCastedThisTurnOnTargetCount != null) {
            this.m_spellCastedThisTurnOnTargetCount.clear();
        }
    }
}
