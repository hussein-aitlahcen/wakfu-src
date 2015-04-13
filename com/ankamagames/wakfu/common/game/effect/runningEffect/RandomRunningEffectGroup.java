package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class RandomRunningEffectGroup extends RunningEffectGroup
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    protected int m_maxEffectToExecute;
    private static final ObjectPool m_staticPool;
    
    public RandomRunningEffectGroup() {
        super();
        this.m_maxEffectToExecute = -1;
    }
    
    @Override
    public RandomRunningEffectGroup newInstance() {
        RandomRunningEffectGroup re;
        try {
            re = (RandomRunningEffectGroup)RandomRunningEffectGroup.m_staticPool.borrowObject();
            re.m_pool = RandomRunningEffectGroup.m_staticPool;
        }
        catch (Exception e) {
            re = new RandomRunningEffectGroup();
            re.m_pool = null;
            RandomRunningEffectGroup.m_logger.error((Object)("Erreur lors d'un newInstance sur un RandomRunningEffectGroup : " + e.getMessage()));
        }
        this.copyParams(re);
        return re;
    }
    
    protected void copyParams(final RandomRunningEffectGroup re) {
        re.m_maxEffectToExecute = this.m_maxEffectToExecute;
        re.m_transmitOriginalTarget = this.m_transmitOriginalTarget;
        re.m_effectGroup = null;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.m_effectGroup == null) {
            RandomRunningEffectGroup.m_logger.error((Object)("Groupe d'effets inexistant " + this.getEffectId()));
            return;
        }
        final List<WakfuEffect> selectedEffects = this.selectEffectToExecute();
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(true, false, (WakfuRunningEffect)triggerRE);
        if (this.hasProperty(RunningEffectPropertyType.TRANSMIT_LEVEL_TO_CHILDREN) && params.getForcedLevel() == -1) {
            params.setForcedLevel(this.getContainerLevel());
        }
        for (final WakfuEffect e : selectedEffects) {
            if (this.useTarget()) {
                e.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), this.m_transmitOriginalTarget ? this.m_target : null, params, false);
            }
            else {
                if (this.m_genericEffect == null || ((WakfuEffect)this.m_genericEffect).getAreaOfEffect() == null) {
                    return;
                }
                this.executeEffectsOnArea(params, e);
            }
        }
    }
    
    private void executeEffectsOnArea(final WakfuEffectExecutionParameters params, final WakfuEffect e) {
        final FightMap fightMap = this.m_context.getFightMap();
        if (fightMap == null) {
            RandomRunningEffectGroup.m_logger.warn((Object)("pas de fightmap sur le context " + this.m_context));
            return;
        }
        final Point3 targetCell = new Point3(this.getTargetCell());
        final AreaOfEffect area = ((WakfuEffect)this.m_genericEffect).getAreaOfEffect();
        final Iterable<int[]> iterable = area.getCells(this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), this.m_caster.getWorldCellX(), this.m_caster.getWorldCellY(), this.m_caster.getWorldCellAltitude(), this.m_caster.getDirection());
        for (final int[] ints : iterable) {
            targetCell.setX(ints[0]);
            targetCell.setY(ints[1]);
            if (!fightMap.isInsideOrBorder(targetCell.getX(), targetCell.getY())) {
                continue;
            }
            final short cellHeight = fightMap.getCellHeight(targetCell.getX(), targetCell.getY());
            if (cellHeight == -32768) {
                continue;
            }
            targetCell.setZ(cellHeight);
            if (!fightMap.isInMap(targetCell.getX(), targetCell.getY())) {
                continue;
            }
            e.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), targetCell.getX(), targetCell.getY(), targetCell.getZ(), null, params, false);
        }
    }
    
    private boolean isSelectableEffect(final WakfuEffect effect) {
        if (effect.getContainerMinLevel() > this.getContainerLevel()) {
            return false;
        }
        if (effect.getContainerMaxLevel() < this.getContainerLevel()) {
            return false;
        }
        final SimpleCriterion conditions = effect.getConditions();
        return conditions == null || conditions.isValid(this.m_caster, this.useTarget() ? this.m_target : this.m_targetCell, null, this.m_context);
    }
    
    private List<WakfuEffect> selectEffectToExecute() {
        final List<WakfuEffect> selectableEffects = new ArrayList<WakfuEffect>();
        for (final WakfuEffect effect : this.m_effectGroup) {
            if (this.isSelectableEffect(effect)) {
                selectableEffects.add(effect);
            }
        }
        final List<WakfuEffect> selectedEffects = new ArrayList<WakfuEffect>();
        Iterator<WakfuEffect> it = selectableEffects.iterator();
        int probaMax = 0;
        while (it.hasNext()) {
            final WakfuEffect effect2 = it.next();
            probaMax += (int)effect2.getExecutionProbability(this.getContainerLevel());
        }
        for (int i = 0; i < this.m_maxEffectToExecute; ++i) {
            int currentProba = 0;
            final int random = MathHelper.random(probaMax);
            it = selectableEffects.iterator();
            while (it.hasNext()) {
                final WakfuEffect effect3 = it.next();
                if (selectedEffects.contains(effect3)) {
                    continue;
                }
                currentProba += (int)effect3.getExecutionProbability(this.getContainerLevel());
                if (currentProba >= random) {
                    selectedEffects.add(effect3);
                    probaMax -= (int)effect3.getExecutionProbability(this.getContainerLevel());
                    break;
                }
            }
        }
        return selectedEffects;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(((WakfuEffect)this.m_genericEffect).getEffectId());
        if (effectGroup != null) {
            this.m_effectGroup = effectGroup.instanceAnother(level);
        }
        this.m_maxEffectToExecute = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3) {
            this.m_transmitOriginalTarget = (((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.RANDOM) == 1);
        }
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RandomRunningEffectGroup.PARAMETERS_LIST_SET;
    }
    
    @Override
    public boolean useTarget() {
        return this.m_genericEffect == null || ((WakfuEffect)this.m_genericEffect).getParamsCount() < 2 || ((WakfuEffect)this.m_genericEffect).getParam(1) == 1.0f;
    }
    
    @Override
    public boolean useTargetCell() {
        return this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2 && ((WakfuEffect)this.m_genericEffect).getParam(1) == 0.0f;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    @Override
    public void onCheckIn() {
        this.m_effectGroup = null;
        this.m_maxEffectToExecute = -1;
        this.m_transmitOriginalTarget = false;
        super.onCheckIn();
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("groupe d'effets standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb d'effets \u00e0 executer", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("groupe d'effets cibl\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb d'effets \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Gestion de la tansmission de la cible originale", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb d'effets \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG) }) });
        m_staticPool = new MonitoredPool(new ObjectFactory<RandomRunningEffectGroup>() {
            @Override
            public RandomRunningEffectGroup makeObject() {
                return new RandomRunningEffectGroup();
            }
        });
    }
}
