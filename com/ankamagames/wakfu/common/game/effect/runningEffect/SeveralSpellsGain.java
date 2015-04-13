package com.ankamagames.wakfu.common.game.effect.runningEffect;

import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;

abstract class SeveralSpellsGain extends WakfuRunningEffect
{
    private final TLongHashSet m_modifiedSpells;
    
    SeveralSpellsGain() {
        super();
        this.m_modifiedSpells = new TLongHashSet();
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        final List<AbstractSpellLevel<?>> spells = this.getTargetSpells();
        if (this.m_target == null || spells == null || spells.isEmpty()) {
            this.setNotified();
            return;
        }
        for (final AbstractSpellLevel<?> spell : spells) {
            spell.addLevelGain(this.m_value);
            this.m_modifiedSpells.add(spell.getUniqueId());
        }
        this.afterModification();
    }
    
    protected abstract void afterModification();
    
    protected abstract List<AbstractSpellLevel<?>> getTargetSpells();
    
    @Override
    public void unapplyOverride() {
        if (!this.m_executed) {
            super.unapplyOverride();
            return;
        }
        if (!this.m_modifiedSpells.isEmpty()) {
            final List<AbstractSpellLevel<?>> spells = this.getTargetSpells();
            for (final AbstractSpellLevel<?> spell : spells) {
                if (this.m_modifiedSpells.contains(spell.getUniqueId())) {
                    spell.addLevelGain(-this.m_value);
                }
            }
            if (this.m_target != null) {
                this.afterModification();
            }
        }
        super.unapplyOverride();
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_modifiedSpells.clear();
    }
}
