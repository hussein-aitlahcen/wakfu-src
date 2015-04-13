package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SpellGain extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_spellId;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SpellGain.PARAMETERS_LIST_SET;
    }
    
    public SpellGain() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public SpellGain newInstance() {
        SpellGain re;
        try {
            re = (SpellGain)SpellGain.m_staticPool.borrowObject();
            re.m_pool = SpellGain.m_staticPool;
        }
        catch (Exception e) {
            re = new SpellGain();
            re.m_pool = null;
            SpellGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un " + SpellGain.class + " : " + e.getMessage()));
        }
        re.m_spellId = this.m_spellId;
        return re;
    }
    
    public int getSpellId() {
        return this.m_spellId;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final AbstractSpellLevel<?> spell = this.getTargetSpell();
        if (this.m_target != null && spell != null) {
            spell.addLevelGain(this.m_value);
            ((BasicCharacterInfo)this.m_target).updateElementMastery(spell.getElement());
        }
        else {
            this.setNotified(true);
        }
    }
    
    private AbstractSpellLevel<?> getTargetSpell() {
        if (this.m_target == null) {
            return null;
        }
        final Iterable<? extends AbstractSpellLevel> spellInventory = ((BasicCharacterInfo)this.m_target).getPermanentSpellInventory();
        if (spellInventory == null) {
            return null;
        }
        return this.getFirstWithReferenceId(spellInventory);
    }
    
    private AbstractSpellLevel<?> getFirstWithReferenceId(final Iterable<? extends AbstractSpellLevel> spellInventory) {
        for (final AbstractSpellLevel spellLevel : spellInventory) {
            if (spellLevel.getSpellId() == this.m_spellId) {
                return (AbstractSpellLevel<?>)spellLevel;
            }
        }
        return null;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_spellId = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
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
    public void unapplyOverride() {
        if (this.m_executed) {
            final AbstractSpellLevel spell = this.getTargetSpell();
            if (spell != null) {
                spell.addLevelGain(-this.m_value);
                if (this.m_target != null) {
                    ((BasicCharacterInfo)this.m_target).updateElementMastery(spell.getElement());
                }
            }
        }
        super.unapplyOverride();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SpellGain>() {
            @Override
            public SpellGain makeObject() {
                return new SpellGain();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Gain de niveau de sort", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id du sort", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("valeur (en niveaux)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
