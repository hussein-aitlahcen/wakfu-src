package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class DecurseStandardEffect extends WakfuRunningEffect
{
    private static final int REMOVE_ALL_EFFECTS_DEFAULT_VALUE = Integer.MIN_VALUE;
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return DecurseStandardEffect.PARAMETERS_LIST_SET;
    }
    
    @Override
    public DecurseStandardEffect newInstance() {
        DecurseStandardEffect re;
        try {
            re = (DecurseStandardEffect)DecurseStandardEffect.m_staticPool.borrowObject();
            re.m_pool = DecurseStandardEffect.m_staticPool;
        }
        catch (Exception e) {
            re = new DecurseStandardEffect();
            re.m_pool = null;
            DecurseStandardEffect.m_logger.error((Object)("Erreur lors d'un checkOut sur un ArenaRunningEffect : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        this.setNotified(true);
        if (this.m_genericEffect == null || this.m_target == null || this.m_target.getRunningEffectManager() == null) {
            return;
        }
        final List<WakfuRunningEffect> reToUnapply = new ArrayList<WakfuRunningEffect>();
        final boolean removeByActionID = ((WakfuEffect)this.m_genericEffect).getParamsCount() == 2;
        int effectToRemoveId;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 0) {
            effectToRemoveId = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.RANDOM);
        }
        else {
            effectToRemoveId = Integer.MIN_VALUE;
        }
        for (final RunningEffect re : this.m_target.getRunningEffectManager()) {
            if (re.getGenericEffect() == null) {
                continue;
            }
            int effectId;
            if (removeByActionID) {
                effectId = re.getGenericEffect().getActionId();
            }
            else {
                effectId = ((RunningEffect<WakfuEffect, EC>)re).getGenericEffect().getEffectId();
            }
            if (((RunningEffect<WakfuEffect, EC>)re).getGenericEffect() != null && effectToRemoveId == effectId) {
                reToUnapply.add((WakfuRunningEffect)re);
            }
            else {
                if (!this.shouldDecurse(re)) {
                    continue;
                }
                if (re.getId() == RunningEffectConstants.DECURSE.getId()) {
                    continue;
                }
                if (effectToRemoveId != Integer.MIN_VALUE) {
                    continue;
                }
                reToUnapply.add((WakfuRunningEffect)re);
            }
        }
        for (final WakfuRunningEffect re2 : reToUnapply) {
            re2.setNotifyUnapplicationForced(true);
            this.m_target.getRunningEffectManager().removeEffect(re2);
        }
    }
    
    private boolean shouldDecurse(final RunningEffect re) {
        final boolean isFightEffect = re.getGenericEffect() != null && re.getGenericEffect().getEffectType() == 2;
        if (isFightEffect) {
            final boolean isDecursable = re.getGenericEffect().isDecursable();
            if (!isDecursable) {
                return false;
            }
        }
        if (re instanceof StateRunningEffect) {
            final State state = ((StateRunningEffect)re).getState();
            if (state != null && (state.isInamovable() || !state.isDecursable())) {
                return false;
            }
        }
        final EffectContainer effectContainer = re.getEffectContainer();
        if (effectContainer == null) {
            return false;
        }
        final int effectContainerType = effectContainer.getContainerType();
        if (effectContainerType == 3) {
            return true;
        }
        if (effectContainerType == 1) {
            return true;
        }
        if (effectContainerType != 11) {
            return false;
        }
        if (!(effectContainer instanceof AbstractSpellLevel)) {
            return true;
        }
        final AbstractSpellLevel spellLevel = (AbstractSpellLevel)effectContainer;
        return !spellLevel.getSpell().isPassive();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<DecurseStandardEffect>() {
            @Override
            public DecurseStandardEffect makeObject() {
                return new DecurseStandardEffect();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("D\u00e9faut (on retire tous les effets)", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("D\u00e9senvoutement avec l'id AGT", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de l'effet a retirer (ID AGT)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("D\u00e9senvoutement avec l'id de l'action", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Id de l'effet a retirer (ID de l'action)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Pas d'utilit\u00e9", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
