package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.datas.specific.symbiot.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class BringBackToSymbiot extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return BringBackToSymbiot.PARAMETERS_LIST_SET;
    }
    
    public BringBackToSymbiot() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public BringBackToSymbiot newInstance() {
        BringBackToSymbiot re;
        try {
            re = (BringBackToSymbiot)BringBackToSymbiot.m_staticPool.borrowObject();
            re.m_pool = BringBackToSymbiot.m_staticPool;
        }
        catch (Exception e) {
            re = new BringBackToSymbiot();
            re.m_pool = null;
            re.m_isStatic = false;
            BringBackToSymbiot.m_logger.error((Object)("Erreur lors d'un checkOut sur un BringBackToSymbiot : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final EffectUser symbiotOwner = this.getSymbiotOwner();
        if (!(symbiotOwner instanceof BasicCharacterInfo) || !(symbiotOwner instanceof SymbioticCharacter)) {
            this.setNotified();
            return;
        }
        final SymbioticCharacter summoner = (SymbioticCharacter)symbiotOwner;
        final AbstractSymbiot symbiot = summoner.getSymbiot();
        if (symbiot == null) {
            this.setNotified();
            return;
        }
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            this.setNotified();
            return;
        }
        final BasicCharacterInfo basicCharacterInfo = (BasicCharacterInfo)this.m_target;
        if (basicCharacterInfo.getOriginalController() != summoner) {
            this.setNotified();
            return;
        }
        final byte index = symbiot.getIndexBySummonId(this.m_target.getId());
        final BasicInvocationCharacteristics creature = symbiot.getCreatureParametersFromIndex(index);
        if (creature != null) {
            creature.setSummonId(-1L);
        }
        basicCharacterInfo.setAlreadyReturnedToSymbiot(true);
        symbiot.setCreatureAvailability(index, true);
        this.m_target.addProperty(FightPropertyType.DONT_TRIGGER_KO);
        this.m_target.addProperty(FightPropertyType.CANNOT_BE_RAISED);
        this.m_target.addProperty(FightPropertyType.NO_KO);
        this.m_target.addProperty(FightPropertyType.NO_DEATH);
        this.notifyExecution(linkedRE, trigger);
        this.m_target.getCharacteristic(FighterCharacteristicType.HP).toMin();
        this.m_target.getCharacteristic(FighterCharacteristicType.KO_TIME_BEFORE_DEATH).setMax(0);
    }
    
    protected EffectUser getSymbiotOwner() {
        return this.m_caster;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
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
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<BringBackToSymbiot>() {
            @Override
            public BringBackToSymbiot makeObject() {
                return new BringBackToSymbiot();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Rappelle l'invocation dans le symbiote", new WakfuRunningEffectParameter[0]) });
    }
}
