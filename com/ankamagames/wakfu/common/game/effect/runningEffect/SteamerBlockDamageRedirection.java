package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class SteamerBlockDamageRedirection extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_blockBaseId;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SteamerBlockDamageRedirection.PARAMETERS_LIST_SET;
    }
    
    public SteamerBlockDamageRedirection() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public SteamerBlockDamageRedirection newInstance() {
        SteamerBlockDamageRedirection re;
        try {
            re = (SteamerBlockDamageRedirection)SteamerBlockDamageRedirection.m_staticPool.borrowObject();
            re.m_pool = SteamerBlockDamageRedirection.m_staticPool;
        }
        catch (Exception e) {
            re = new SteamerBlockDamageRedirection();
            re.m_pool = null;
            re.m_isStatic = false;
            SteamerBlockDamageRedirection.m_logger.error((Object)("Erreur lors d'un checkOut sur un SteamerBlockDamageRedirection : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_blockBaseId = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        this.setNotified();
        RunningEffect triggeringEffect = triggerRE;
        if (triggeringEffect == null) {
            final WakfuEffectExecutionParameters params = (WakfuEffectExecutionParameters)this.getParams();
            triggeringEffect = ((params == null) ? null : params.getExternalTriggeringEffect());
        }
        if (triggeringEffect == null) {
            SteamerBlockDamageRedirection.m_logger.error((Object)"Cet effet doit \u00eatre d\u00e9clench\u00e9");
            return;
        }
        if (!triggeringEffect.getTriggersToExecute().get(2)) {
            SteamerBlockDamageRedirection.m_logger.error((Object)"Cet effet doit \u00eatre d\u00e9clench\u00e9 par une perte de pdv");
            return;
        }
        final List<BasicEffectArea> casterBlocks = this.getCasterBlocks();
        if (casterBlocks.isEmpty()) {
            return;
        }
        final int valueToAbsorb = triggeringEffect.getValue() * this.m_value / 100;
        if (valueToAbsorb == 0) {
            return;
        }
        this.sortBlockByDistance(casterBlocks);
        final int absorbedValue = this.executeHpLossOnBlocks(casterBlocks, valueToAbsorb, triggeringEffect);
        triggeringEffect.update(1, -absorbedValue, false);
    }
    
    private int executeHpLossOnBlocks(final List<BasicEffectArea> absorbingBlocks, final int valueToAbsorb, final RunningEffect triggerRE) {
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(((WakfuEffect)this.m_genericEffect).getEffectId());
        if (effectGroup == null) {
            return 0;
        }
        if (effectGroup.getEffectsCount() != 1) {
            SteamerBlockDamageRedirection.m_logger.error((Object)("On ne peut pas qu'un seul effet dans un groupe d'effet de ce type " + ((WakfuEffect)this.m_genericEffect).getEffectId()));
            return 0;
        }
        final int[] remainingToAbsorb = { valueToAbsorb };
        for (final BasicEffectArea absorbingBlock : absorbingBlocks) {
            final WakfuEffectExecutionParameters params = this.getExecutionParameters((WakfuRunningEffect)triggerRE, true);
            params.addListener(new WakfuRunningEffectListener() {
                @Override
                public void onAfterExecution(final WakfuRunningEffect effect) {
                    final int value = effect.getValue();
                    final int[] val$remainingToAbsorb = remainingToAbsorb;
                    final int n = 0;
                    val$remainingToAbsorb[n] -= value;
                }
                
                @Override
                public void valueComputed(final WakfuRunningEffect effect) {
                }
            });
            final int hp = absorbingBlock.getCharacteristicValue(FighterCharacteristicType.HP);
            params.setForcedValue(Math.min(hp, remainingToAbsorb[0]));
            final WakfuEffect firstEffect = effectGroup.getEffect(0);
            firstEffect.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), absorbingBlock.getWorldCellX(), absorbingBlock.getWorldCellY(), absorbingBlock.getWorldCellAltitude(), absorbingBlock, params, false);
            if (remainingToAbsorb[0] <= 0) {
                break;
            }
        }
        return valueToAbsorb - remainingToAbsorb[0];
    }
    
    private WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(disableProbabilityComputation, false, linkedRE);
        params.setResetLimitedApplyCount(false);
        return params;
    }
    
    private void sortBlockByDistance(final List<BasicEffectArea> casterBlocks) {
        Collections.sort(casterBlocks, new Comparator<BasicEffectArea>() {
            @Override
            public int compare(final BasicEffectArea o1, final BasicEffectArea o2) {
                final Point3 casterPos = SteamerBlockDamageRedirection.this.m_caster.getPosition();
                final Integer distance1 = o1.getPosition().getDistance(casterPos);
                final int distance2 = o2.getPosition().getDistance(casterPos);
                return distance1.compareTo(Integer.valueOf(distance2));
            }
        });
    }
    
    public List<BasicEffectArea> getCasterBlocks() {
        final BasicEffectAreaManager effectAreaManager = this.getContext().getEffectAreaManager();
        if (effectAreaManager == null) {
            return (List<BasicEffectArea>)Collections.emptyList();
        }
        final List<BasicEffectArea> res = new ArrayList<BasicEffectArea>();
        final Collection<BasicEffectArea> areas = effectAreaManager.getActiveEffectAreas();
        for (final BasicEffectArea area : areas) {
            if (area.getBaseId() == this.m_blockBaseId && area.getOwner() == this.m_caster) {
                res.add(area);
            }
        }
        return res;
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
        this.m_blockBaseId = 0;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SteamerBlockDamageRedirection>() {
            @Override
            public SteamerBlockDamageRedirection makeObject() {
                return new SteamerBlockDamageRedirection();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("valeur de l'absorption", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Absorption des d\u00e9gats en % ", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Base Id des blocs ", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
