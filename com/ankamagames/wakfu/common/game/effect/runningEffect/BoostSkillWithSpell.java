package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class BoostSkillWithSpell extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return BoostSkillWithSpell.PARAMETERS_LIST_SET;
    }
    
    @Override
    public BoostSkillWithSpell newInstance() {
        BoostSkillWithSpell re;
        try {
            re = (BoostSkillWithSpell)BoostSkillWithSpell.m_staticPool.borrowObject();
            re.m_pool = BoostSkillWithSpell.m_staticPool;
        }
        catch (Exception e) {
            re = new BoostSkillWithSpell();
            re.m_pool = null;
            BoostSkillWithSpell.m_logger.error((Object)("Erreur lors d'un checkOut sur un BoostSkillWithSpell : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target != null && this.m_target.getEffectUserType() == 20 && ((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer() != null && (((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer().getContainerType() == 11 || ((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer().getContainerType() == 25)) {
            final AbstractSkill skill = ((BasicCharacterInfo)this.m_target).getSkillInventory().getFirstWithReferenceId(this.m_value);
            if (skill != null) {
                skill.setLinkedLevelable(((RunningEffect<FX, AbstractSpellLevel<?>>)this).getEffectContainer());
            }
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
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
        if (this.m_target != null && this.m_target.getEffectUserType() == 20 && ((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer() != null && (((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer().getContainerType() == 11 || ((RunningEffect<FX, WakfuEffectContainer>)this).getEffectContainer().getContainerType() == 25)) {
            final SkillInventory<? extends AbstractSkill> inventory = ((BasicCharacterInfo)this.m_target).getSkillInventory();
            if (inventory != null) {
                final AbstractSkill skill = inventory.getFirstWithReferenceId(this.m_value);
                if (skill != null) {
                    skill.setLinkedLevelable(null);
                }
            }
        }
        super.unapplyOverride();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<BoostSkillWithSpell>() {
            @Override
            public BoostSkillWithSpell makeObject() {
                return new BoostSkillWithSpell();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("boost le skill donn\u00e9 avec le sort li\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id de la skill", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
