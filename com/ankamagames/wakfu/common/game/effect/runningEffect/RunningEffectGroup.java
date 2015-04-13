package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.ai.targetfinder.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class RunningEffectGroup extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    protected static final ParameterListSet PARAMETERS_LIST_SET;
    public static final byte NO_CHANGE = 0;
    public static final byte CASTER_AS_TARGET = 1;
    public static final byte INVERT_CASTER_AND_TARGET = 2;
    private boolean m_forceTargetCellRelativeToCaster;
    private byte m_decalageX;
    private byte m_decalageY;
    private boolean m_withSuccess;
    private boolean m_withRelativeProbability;
    private int m_maxEffectToExecute;
    protected boolean m_transmitOriginalTarget;
    private byte m_casterTargetChange;
    private final BitSet m_endTriggers;
    protected AbstractEffectGroup m_effectGroup;
    private final ArrayList<WakfuEffect> m_effectToExecute;
    
    public RunningEffectGroup() {
        super();
        this.m_forceTargetCellRelativeToCaster = false;
        this.m_decalageX = 0;
        this.m_decalageY = 0;
        this.m_withSuccess = false;
        this.m_withRelativeProbability = false;
        this.m_maxEffectToExecute = -1;
        this.m_transmitOriginalTarget = true;
        this.m_casterTargetChange = 0;
        this.m_endTriggers = new BitSet();
        this.m_effectToExecute = new ArrayList<WakfuEffect>();
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroup.PARAMETERS_LIST_SET;
    }
    
    @Override
    public RunningEffectGroup newInstance() {
        RunningEffectGroup re;
        try {
            re = (RunningEffectGroup)RunningEffectGroup.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroup.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroup();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroup.m_logger.error((Object)("Erreur lors d'un checkOut sur un ArenaRunningEffect : " + e.getMessage()));
        }
        re.m_endTriggers.clear();
        re.m_effectGroup = null;
        re.m_forceTargetCellRelativeToCaster = this.m_forceTargetCellRelativeToCaster;
        re.m_decalageX = this.m_decalageX;
        re.m_decalageY = this.m_decalageY;
        re.m_withSuccess = this.m_withSuccess;
        re.m_withRelativeProbability = this.m_withRelativeProbability;
        re.m_maxEffectToExecute = this.m_maxEffectToExecute;
        re.m_transmitOriginalTarget = this.m_transmitOriginalTarget;
        re.m_transmitOriginalTarget = this.m_transmitOriginalTarget;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_effectGroup != null && this.isValueComputationEnabled()) {
            this.modifyTargetAndCasterIfNecessary();
            if (this.m_withRelativeProbability) {
                final short level = this.getContainerLevel();
                short probaMax = 0;
                this.m_effectToExecute.clear();
                for (final WakfuEffect e : this.m_effectGroup) {
                    final Iterable<EffectUser> effectUserIterable = TargetFinder.getInstance().getTargets(this.getCaster(), this.m_context.getTargetInformationProvider(), e.getEmptyCellNeededAreaOfEffect(), this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), e.getTargetValidator());
                    if (effectUserIterable.iterator().hasNext()) {
                        continue;
                    }
                    final int minLevel = e.getContainerMinLevel();
                    final int maxLevel = e.getContainerMaxLevel();
                    if (level < minLevel) {
                        continue;
                    }
                    if (level > maxLevel) {
                        continue;
                    }
                    if (e.mustBeTriggered()) {
                        probaMax += (short)e.getExecutionProbability(level);
                        this.m_effectToExecute.add(e);
                    }
                    else if (e.getConditions() != null) {
                        if (!e.getConditions().isValid((linkedRE != null) ? linkedRE.getCaster() : this.m_caster, this.m_target, this, this.m_context)) {
                            continue;
                        }
                        probaMax += (short)e.getExecutionProbability(level);
                        this.m_effectToExecute.add(e);
                    }
                    else {
                        probaMax += (short)e.getExecutionProbability(level);
                        this.m_effectToExecute.add(e);
                    }
                }
                final Iterator<WakfuEffect> iterator = this.m_effectToExecute.iterator();
                while (iterator.hasNext()) {
                    final WakfuEffect wakfuEffect = iterator.next();
                    if (DiceRoll.roll(probaMax) > ValueRounder.randomRound(wakfuEffect.getExecutionProbability(level))) {
                        iterator.remove();
                    }
                }
                this.executeEffectGroupWithNewParams(this.m_effectToExecute.iterator(), linkedRE, true);
            }
            else {
                this.executeEffectGroupWithNewParams(this.m_effectGroup.iterator(), linkedRE, false);
            }
        }
        this.setNotified(true);
    }
    
    private void modifyTargetAndCasterIfNecessary() {
        if (this.m_casterTargetChange == 0) {
            return;
        }
        if (this.m_casterTargetChange == 1) {
            if (this.m_target != null) {
                this.m_caster = this.m_target;
            }
            else {
                RunningEffectGroup.m_logger.error((Object)("On veut changer le caster d'un groupe d'effet pas la cible mais la cible est nulle, on ne fait rien, effectId = " + this.getEffectId()));
            }
        }
        else if (this.m_casterTargetChange == 2) {
            if (this.m_target != null && this.m_caster != null) {
                final EffectUser target = this.m_target;
                this.m_target = this.m_caster;
                this.m_caster = target;
            }
            else {
                RunningEffectGroup.m_logger.error((Object)("On veut inverser le caster et la target d'un groupe mais l'un des deux est null, on ne fait rien, effectId = " + this.getEffectId()));
            }
        }
    }
    
    private void executeEffectGroupWithNewParams(final Iterator<WakfuEffect> effects, final RunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = this.getExecutionParameters((WakfuRunningEffect)linkedRE, disableProbabilityComputation);
        try {
            if (this.hasProperty(RunningEffectPropertyType.TRANSMIT_LEVEL_TO_CHILDREN) && params.getForcedLevel() == -1) {
                params.setForcedLevel(this.getContainerLevel());
            }
            this.executeEffectGroup(effects, params);
        }
        catch (Exception e) {
            RunningEffectGroup.m_logger.error((Object)("Exception levee lors de l'execution d'un groupe d'effets id " + ((this.m_genericEffect == null) ? -1 : ((WakfuEffect)this.m_genericEffect).getEffectId())), (Throwable)e);
        }
        finally {
            params.release();
        }
    }
    
    protected WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(disableProbabilityComputation, false, linkedRE);
        params.setResetLimitedApplyCount(false);
        return params;
    }
    
    private void executeEffectGroup(final Iterator<WakfuEffect> effects, final WakfuEffectExecutionParameters params) {
        int executionCount = 0;
        int executionCountWithSuccess = 0;
        while (effects.hasNext()) {
            final WakfuEffect e = effects.next();
            if (!this.canBeExecutedForEffectGroup(e, executionCountWithSuccess, executionCount)) {
                break;
            }
            if (this.m_maxEffectToExecute > 0) {
                if (this.m_withSuccess) {
                    if (executionCountWithSuccess >= this.m_maxEffectToExecute) {
                        break;
                    }
                }
                else if (executionCount >= this.m_maxEffectToExecute) {
                    break;
                }
            }
            if (this.useTarget()) {
                if (this.m_target == null) {
                    RunningEffectGroup.m_logger.error((Object)("Impossible d'executer l'effet " + e.getEffectId() + " on a plus de cible"));
                    return;
                }
                EffectExecutionResult result;
                try {
                    result = e.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), this.m_transmitOriginalTarget ? this.m_target : null, params, true);
                }
                catch (Exception e2) {
                    RunningEffectGroup.m_logger.error((Object)("Pb a l''execution de l'effet " + e.getEffectId()), (Throwable)e2);
                    return;
                }
                if (result != null && result.getExecutionCount() > 0) {
                    ++executionCountWithSuccess;
                }
                ++executionCount;
                if (result == null) {
                    continue;
                }
                result.clear();
            }
            else {
                final Point3 targetCell = new Point3(this.getTargetCell());
                if (this.m_genericEffect == null || ((WakfuEffect)this.m_genericEffect).getAreaOfEffect() == null) {
                    continue;
                }
                final AreaOfEffect area = ((WakfuEffect)this.m_genericEffect).getAreaOfEffect();
                final Iterable<int[]> iterable = area.getCells(this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), this.m_caster.getWorldCellX(), this.m_caster.getWorldCellY(), this.m_caster.getWorldCellAltitude(), this.m_caster.getDirection());
                for (final int[] ints : iterable) {
                    targetCell.setX(ints[0]);
                    targetCell.setY(ints[1]);
                    if (this.isValidTargetCellAndSetZIfNecessary(targetCell) && this.m_context.getFightMap().isInMap(targetCell.getX(), targetCell.getY())) {
                        final EffectExecutionResult result = e.execute(this.getEffectContainer(), this.getCaster(), this.getContext(), RunningEffectConstants.getInstance(), targetCell.getX(), targetCell.getY(), targetCell.getZ(), null, params, true);
                        if (result != null && result.getExecutionCount() > 0) {
                            ++executionCountWithSuccess;
                        }
                        ++executionCount;
                        if (result != null) {
                            result.clear();
                        }
                    }
                    if (this.m_maxEffectToExecute > 0) {
                        if (this.m_withSuccess) {
                            if (executionCountWithSuccess >= this.m_maxEffectToExecute) {
                                break;
                            }
                            continue;
                        }
                        else {
                            if (executionCount >= this.m_maxEffectToExecute) {
                                break;
                            }
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    protected boolean canBeExecutedForEffectGroup(final WakfuEffect e, final int executionCountWithSuccess, final int executionCount) {
        return true;
    }
    
    private boolean isValidTargetCellAndSetZIfNecessary(final Point3 targetCell) {
        final FightMap fightMap = this.m_context.getFightMap();
        if (fightMap == null) {
            RunningEffectGroup.m_logger.error((Object)("On tente d'execute un groupe d'effets sur cellule sans FightMap " + ((WakfuEffect)this.m_genericEffect).getEffectId()));
            return false;
        }
        if (fightMap.isInsideOrBorder(targetCell.getX(), targetCell.getY())) {
            final short cellHeight = fightMap.getCellHeight(targetCell.getX(), targetCell.getY());
            if (cellHeight != -32768) {
                targetCell.setZ(cellHeight);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void unapplyOverride() {
        super.unapplyOverride();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        this.m_effectGroup = null;
        this.m_withSuccess = false;
        this.m_withRelativeProbability = false;
        this.m_casterTargetChange = 0;
        this.m_maxEffectToExecute = 0;
        if (this.m_genericEffect == null) {
            return;
        }
        if (AbstractEffectGroupManager.getInstance() == null) {
            return;
        }
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(((WakfuEffect)this.m_genericEffect).getEffectId());
        if (effectGroup != null) {
            this.m_effectGroup = effectGroup.instanceAnother(level);
        }
        this.m_maxEffectToExecute = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
            this.m_withSuccess = (((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 4) {
            this.m_withRelativeProbability = (((WakfuEffect)this.m_genericEffect).getParam(3, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 5) {
            this.m_transmitOriginalTarget = (((WakfuEffect)this.m_genericEffect).getParam(4, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 6) {
            this.m_casterTargetChange = (byte)((WakfuEffect)this.m_genericEffect).getParam(5, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    protected boolean canBeExecutedOnKO() {
        return true;
    }
    
    public boolean isSetCasterFromTarget() {
        return this.m_casterTargetChange == 1;
    }
    
    public boolean isTransmitOriginalTarget() {
        return this.m_transmitOriginalTarget;
    }
    
    @Override
    public boolean useTarget() {
        return this.m_genericEffect == null || ((WakfuEffect)this.m_genericEffect).getParamsCount() < 3 || ((WakfuEffect)this.m_genericEffect).getParam(2) == 1.0f;
    }
    
    @Override
    public boolean useTargetCell() {
        return this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() >= 3 && ((WakfuEffect)this.m_genericEffect).getParam(2) == 0.0f;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_forceTargetCellRelativeToCaster = false;
        this.m_decalageX = 0;
        this.m_decalageY = 0;
        this.m_withSuccess = false;
        this.m_withRelativeProbability = false;
        this.m_maxEffectToExecute = -1;
        this.m_transmitOriginalTarget = true;
        this.m_casterTargetChange = 0;
        this.m_effectGroup = null;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroup>() {
            @Override
            public RunningEffectGroup makeObject() {
                return new RunningEffectGroup();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("groupe d'effet standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("groupe de r\u00e9ussite", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 r\u00e9ussir", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("groupe d'effet cibl\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("groupe d'effet cibl\u00e9, probabilit\u00e9 relative", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Gestion de la tansmission de la cible originale", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles  (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Change le caster du groupe d'effet par sa cible ou les inverse", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Modif Caster-Target :  La target devient le caster : 1, Target et Caster invers\u00e9s : 2, rien : 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
