package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.FighterCharacteristicProcedures.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CharacModifiedByAnother extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private CharacBoostAnotherCharacProcedure m_procedure;
    private FighterCharacteristicType m_referentialCharac;
    private FighterCharacteristicType m_modifiedCharac;
    private boolean m_usePercent;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacModifiedByAnother.PARAMETERS_LIST_SET;
    }
    
    private CharacModifiedByAnother() {
        super();
    }
    
    public CharacModifiedByAnother(final FighterCharacteristicType referentialCharac, final FighterCharacteristicType modifiedCharac) {
        super();
        this.setTriggersToExecute();
        this.m_referentialCharac = referentialCharac;
        this.m_modifiedCharac = modifiedCharac;
    }
    
    @Override
    public CharacModifiedByAnother newInstance() {
        CharacModifiedByAnother re;
        try {
            re = (CharacModifiedByAnother)CharacModifiedByAnother.m_staticPool.borrowObject();
            re.m_pool = CharacModifiedByAnother.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacModifiedByAnother();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacModifiedByAnother.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacModifiedByArmorPlate : " + e.getMessage()));
        }
        re.m_modifiedCharac = this.m_modifiedCharac;
        re.m_referentialCharac = this.m_referentialCharac;
        re.m_usePercent = this.m_usePercent;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.isInvalidTarget()) {
            this.setNotified();
            return;
        }
        this.m_usePercent = true;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2 && ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1) {
            this.m_usePercent = false;
        }
        int cap = -1;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            cap = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        float ratioBetweenCharacs;
        if (this.m_usePercent) {
            ratioBetweenCharacs = this.m_value / 100.0f;
        }
        else {
            ratioBetweenCharacs = this.m_value;
        }
        final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
        final FighterCharacteristic referentialCharac = target.getCharacteristic((CharacteristicType)this.m_referentialCharac);
        if (cap <= 0) {
            this.m_procedure = new CharacBoostAnotherCharacProcedure(target.getCharacteristics(), this.m_modifiedCharac, ratioBetweenCharacs, 0);
        }
        else {
            this.m_procedure = new CharacBoostAnotherCharacProcedureCapped(target.getCharacteristics(), this.m_modifiedCharac, ratioBetweenCharacs, 0, cap);
        }
        referentialCharac.addProcedure(this.m_procedure);
        this.m_procedure.execute(FighterCharacteristicEvent.VALUE_ADDED, referentialCharac.value());
    }
    
    private boolean isInvalidTarget() {
        return this.m_target == null || !(this.m_target instanceof BasicCharacterInfo) || !this.m_target.hasCharacteristic(this.m_referentialCharac) || !this.m_target.hasCharacteristic(this.m_modifiedCharac);
    }
    
    @Override
    public void unapplyOverride() {
        this.removeProcedure();
    }
    
    private void removeProcedure() {
        if (this.isInvalidTarget()) {
            return;
        }
        final FighterCharacteristic referentialCharac = (FighterCharacteristic)this.m_target.getCharacteristic(this.m_referentialCharac);
        if (referentialCharac.containsProcedure(this.m_procedure)) {
            this.m_procedure.execute(FighterCharacteristicEvent.VALUE_SUBSTRACTED, referentialCharac.value());
        }
        referentialCharac.removeProcedure(this.m_procedure);
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
        this.removeProcedure();
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacModifiedByAnother>() {
            @Override
            public CharacModifiedByAnother makeObject() {
                return new CharacModifiedByAnother(null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Modificateur / charac de reference (en % de la valeur de r\u00e9f\u00e9rence)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Modificateur", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Modificateur / charac de reference (en % ou en qte par valeur de r\u00e9f\u00e9rence)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Modificateur (% ou quantit\u00e9)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("en % de la valeur de ref (0) en en multiplication (1)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Avec Cap max", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Modificateur (% ou quantit\u00e9)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("en % de la valeur de ref (0) en en multiplication (1)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Cap max", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
