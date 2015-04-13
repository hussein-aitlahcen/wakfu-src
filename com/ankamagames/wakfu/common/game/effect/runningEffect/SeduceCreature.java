package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.specific.symbiot.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SeduceCreature extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final boolean DEBUG_EFFECT = false;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SeduceCreature.PARAMETERS_LIST_SET;
    }
    
    @Override
    public SeduceCreature newInstance() {
        SeduceCreature re;
        try {
            re = (SeduceCreature)SeduceCreature.m_staticPool.borrowObject();
            re.m_pool = SeduceCreature.m_staticPool;
        }
        catch (Exception e) {
            re = new SeduceCreature();
            re.m_pool = null;
            re.m_isStatic = false;
            SeduceCreature.m_logger.error((Object)("Erreur lors d'un checkOut sur un ArenaRunningEffect : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.getExecutionStatus() != 14) {
            return;
        }
        if (!(this.m_caster instanceof BasicCharacterInfo) || !(this.m_target instanceof BasicCharacterInfo)) {
            SeduceCreature.m_logger.error((Object)"La cible ou le caster n'est pas du bon type");
            this.setNotified(true);
            return;
        }
        final BasicCharacterInfo caster = (BasicCharacterInfo)this.m_caster;
        final AbstractSymbiot symbiot = caster.getSymbiot();
        if (symbiot == null) {
            SeduceCreature.m_logger.error((Object)"On n'a pas de symbiote");
            this.setNotified(true);
            return;
        }
        final BasicCharacterInfo targetInfo = (BasicCharacterInfo)this.m_target;
        if (targetInfo.getType() != 1 || targetInfo.isSummoned()) {
            SeduceCreature.m_logger.error((Object)"La cible que l'on tente de seduire n'est pas seductible");
            this.setNotified(true);
            return;
        }
        final BasicInvocationCharacteristics toSeduce = symbiot.getNewSpecificInvocationCharacteristics(targetInfo.getBreedId());
        if (toSeduce == null) {
            return;
        }
        SeduceCreature.m_logger.info((Object)("S\u00e9duction d'une cr\u00e9ature avec " + toSeduce.getCurrentHp() + " PV"));
        if (this.isValueComputationEnabled()) {
            this.m_value = symbiot.addCreaturesParameters(toSeduce);
        }
        else {
            symbiot.addCreaturesParametersToIndex(toSeduce, (byte)this.m_value);
        }
        if (this.m_value == -1) {
            SeduceCreature.m_logger.info((Object)"Erreur lors de l'ajout de la creature au symbiot, annulation de la seduction");
            this.setNotified(true);
            return;
        }
        this.m_target.addProperty(FightPropertyType.NO_KO);
        this.m_target.addProperty(FightPropertyType.NO_DEATH);
        this.m_target.addProperty(WorldPropertyType.SEDUCE);
        this.m_target.addProperty(FightPropertyType.CANNOT_BE_RAISED);
        this.m_target.addProperty(FightPropertyType.DONT_TRIGGER_KO);
        this.notifyExecution(linkedRE, trigger);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (!(this.m_target instanceof BasicCharacterInfo) || this.m_target.isActiveProperty(FightPropertyType.CANNOT_BE_SEDUCED) || !this.m_target.isActiveProperty(FightPropertyType.CAN_BE_SEDUCED)) {
            this.setExecutionStatus((byte)13);
            return;
        }
        final short casterLevel = ((BasicCharacterInfo)this.m_caster).getLevel();
        final int targetLevel = ((BasicCharacterInfo)this.m_target).getLevel();
        if (targetLevel > casterLevel) {
            this.setExecutionStatus((byte)12);
            return;
        }
        this.setExecutionStatus((byte)14);
    }
    
    @Override
    public void unapplyOverride() {
        super.unapplyOverride();
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
        m_staticPool = new MonitoredPool(new ObjectFactory<SeduceCreature>() {
            @Override
            public SeduceCreature makeObject() {
                return new SeduceCreature();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Param standard", new WakfuRunningEffectParameter[0]) });
    }
}
