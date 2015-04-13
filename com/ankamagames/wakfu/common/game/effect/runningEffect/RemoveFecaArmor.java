package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class RemoveFecaArmor extends StateDecurse
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final int PEACE_ARMOR_ID = 952;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RemoveFecaArmor.PARAMETERS_LIST_SET;
    }
    
    public RemoveFecaArmor() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RemoveFecaArmor newInstance() {
        RemoveFecaArmor re;
        try {
            re = (RemoveFecaArmor)RemoveFecaArmor.m_staticPool.borrowObject();
            re.m_pool = RemoveFecaArmor.m_staticPool;
        }
        catch (Exception e) {
            re = new RemoveFecaArmor();
            re.m_pool = null;
            re.m_isStatic = false;
            RemoveFecaArmor.m_logger.error((Object)("Erreur lors d'un checkOut sur un RemoveFecaArmor : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected int computeEffectsToRemove(final ArrayList<RunningEffect> effectToRemove, final RunningEffectManager effectManager, final int newStateLevel) {
        for (final RunningEffect effect : effectManager) {
            if (!(effect instanceof StateRunningEffect)) {
                continue;
            }
            final State state = ((StateRunningEffect)effect).getState();
            if (!state.isFecaArmor()) {
                continue;
            }
            effectToRemove.add(effect);
            if (state.getStateBaseId() != 952) {
                continue;
            }
            this.m_triggers.set(2149);
        }
        final EffectUser owner = effectManager.getOwner();
        if (owner != null && owner.hasCharacteristic(FighterCharacteristicType.ARMOR_PLATE)) {
            owner.getCharacteristic(FighterCharacteristicType.ARMOR_PLATE).toMin();
        }
        return -1;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RemoveFecaArmor>() {
            @Override
            public RemoveFecaArmor makeObject() {
                return new RemoveFecaArmor();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
