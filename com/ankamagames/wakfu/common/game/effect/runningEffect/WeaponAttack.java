package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class WeaponAttack extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private byte m_equipementPos;
    
    public WeaponAttack() {
        super();
        this.m_equipementPos = -1;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return WeaponAttack.PARAMETERS_LIST_SET;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(186);
    }
    
    @Override
    public WeaponAttack newInstance() {
        WeaponAttack wre;
        try {
            wre = (WeaponAttack)WeaponAttack.m_staticPool.borrowObject();
            wre.m_pool = WeaponAttack.m_staticPool;
        }
        catch (Exception e) {
            wre = new WeaponAttack();
            wre.m_pool = null;
            wre.m_isStatic = false;
            WeaponAttack.m_logger.error((Object)("Erreur lors d'un checkOut sur un WeaponAttack : " + e.getMessage()));
        }
        return wre;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target == null) {
            return;
        }
        if (this.m_target instanceof BasicCharacterInfo && trigger && linkedRE != null && linkedRE.getCaster() != null && linkedRE.getCaster() != this.m_target) {
            final EffectUser user = linkedRE.getCaster();
            ((BasicCharacterInfo)this.m_target).useItem(this.m_equipementPos, new Point3(user.getWorldCellX(), user.getWorldCellY(), user.getWorldCellAltitude()), false, null);
        }
        else {
            AbstractSpellLevel associatedSpellLevel = null;
            if (this.getEffectContainer() instanceof AbstractSpellLevel) {
                associatedSpellLevel = ((RunningEffect<FX, AbstractSpellLevel>)this).getEffectContainer();
            }
            if (this.m_target != null) {
                ((BasicCharacterInfo)this.m_caster).useItem(this.m_equipementPos, new Point3(this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude()), false, associatedSpellLevel);
            }
            else {
                ((BasicCharacterInfo)this.m_caster).useItem(this.m_equipementPos, this.getTargetCell(), false, associatedSpellLevel);
            }
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() > 0) {
            this.m_equipementPos = (byte)((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        }
        else {
            this.m_equipementPos = EquipmentPosition.FIRST_WEAPON.getId();
        }
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<WeaponAttack>() {
            @Override
            public WeaponAttack makeObject() {
                return new WeaponAttack();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Attaque si Cac, avec l'arme en cours(main droite)", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Attaque si Cac, avec l'arme sp\u00e9cifi\u00e9e", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id de position de l'\u00e9quipement", WakfuRunningEffectParameterType.ID) }) });
    }
}
