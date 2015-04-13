package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class CraftXpBoost extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_isCheckedOut;
    private int m_craftId;
    private int m_valueForEcosystem;
    private int m_valueForCraft;
    private static final ObjectPool POOL;
    
    public static CraftXpBoost checkOut() {
        CraftXpBoost obj;
        try {
            obj = (CraftXpBoost)CraftXpBoost.POOL.borrowObject();
            obj.m_isCheckedOut = true;
        }
        catch (Exception e) {
            obj = new CraftXpBoost();
            CraftXpBoost.m_logger.error((Object)("Erreur lors d'un checkOut sur un objet de type EcosystemSkillModification : " + e.getMessage()));
        }
        return obj;
    }
    
    @Override
    public void release() {
        if (this.m_isCheckedOut) {
            try {
                CraftXpBoost.POOL.returnObject(this);
                this.m_isCheckedOut = false;
            }
            catch (Exception e) {
                CraftXpBoost.m_logger.error((Object)"Exception lors du retour au pool", (Throwable)e);
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_valueForCraft = 0;
        this.m_valueForEcosystem = 0;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.m_valueForEcosystem = this.modifyCharacValue(this.m_value, CraftSkillType.CRAFT_ECOSYSTEM_XP_BOOST);
        this.m_valueForCraft = this.modifyCharacValue(this.m_value, CraftSkillType.CRAFT_CRAFT_XP_BOOST);
    }
    
    @Override
    public void unapplyOverride() {
        this.modifyCharacValue(-this.m_valueForEcosystem, CraftSkillType.CRAFT_ECOSYSTEM_XP_BOOST);
        this.modifyCharacValue(-this.m_valueForCraft, CraftSkillType.CRAFT_CRAFT_XP_BOOST);
    }
    
    private int modifyCharacValue(final int value, final CraftSkillType skillType) {
        if (this.m_target == null) {
            return 0;
        }
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            return 0;
        }
        final SkillCharacteristics characteristics = ((BasicCharacterInfo)this.m_target).getSkillCharacteristics();
        final int oldValue = characteristics.getCraftCharacteristicEfficiency(skillType, this.m_craftId);
        characteristics.modifyCraftCharacteristicEfficiency(skillType, this.m_craftId, value);
        final int newValue = characteristics.getCraftCharacteristicEfficiency(skillType, this.m_craftId);
        return newValue - oldValue;
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        final CraftXpBoost newInstance = checkOut();
        newInstance.m_craftId = this.m_craftId;
        newInstance.m_valueForCraft = this.m_valueForCraft;
        newInstance.m_valueForEcosystem = this.m_valueForEcosystem;
        return newInstance;
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
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 2) {
            this.m_craftId = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CraftXpBoost.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Defaut", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur de modification ", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Id du m\u00e9tier)", WakfuRunningEffectParameterType.CONFIG) }) });
        POOL = new MonitoredPool(new ObjectFactory<CraftXpBoost>() {
            @Override
            public CraftXpBoost makeObject() {
                return new CraftXpBoost();
            }
        });
    }
}
