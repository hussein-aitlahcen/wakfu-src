package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.ai.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.los.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public abstract class AbstractEffectRebound extends WakfuRunningEffect
{
    private static final BitSet EMPTY_BITSET;
    protected byte m_reboundCount;
    protected byte m_dispersionMax;
    protected int m_originalValue;
    protected int m_reboundReduction;
    protected int m_maxCellRange;
    protected boolean m_isDiffused;
    protected HashSet<Long> m_alreadyTargetedTargetIds;
    
    public AbstractEffectRebound() {
        super();
        this.m_reboundCount = 0;
        this.m_dispersionMax = 0;
        this.m_originalValue = 0;
        this.m_reboundReduction = 0;
        this.m_maxCellRange = 20;
        this.m_isDiffused = false;
    }
    
    public abstract AbstractEffectRebound getBorrowedObjectFromPool();
    
    @Override
    public AbstractEffectRebound newInstance() {
        final AbstractEffectRebound re = this.getBorrowedObjectFromPool();
        re.m_alreadyTargetedTargetIds = this.m_alreadyTargetedTargetIds;
        re.m_reboundCount = this.m_reboundCount;
        re.m_dispersionMax = this.m_dispersionMax;
        re.m_originalValue = this.m_originalValue;
        re.m_reboundReduction = this.m_reboundReduction;
        re.m_maxCellRange = this.m_maxCellRange;
        return re;
    }
    
    @Override
    public BitSet getTriggersToExecute() {
        return AbstractEffectRebound.EMPTY_BITSET;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.isValueComputationEnabled()) {
            this.m_alreadyTargetedTargetIds.add(this.m_target.getId());
        }
        this.setNotified(true);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value -= (int)(this.m_reboundCount * this.m_reboundReduction * this.m_value / 100.0f);
        if (this.m_target != null) {
            this.m_alreadyTargetedTargetIds.add(this.m_target.getId());
        }
    }
    
    @Override
    public boolean canBeExecuted() {
        return (!(this.m_target instanceof BasicEffectArea) || ((BasicEffectArea)this.m_target).isBlockingMovement()) && super.canBeExecuted();
    }
    
    @Override
    public void unapplyOverride() {
        ++this.m_reboundCount;
        if (this.m_reboundCount > this.m_dispersionMax) {
            return;
        }
        final int nextValue = this.m_originalValue - (int)(this.m_reboundCount * this.m_reboundReduction * this.m_originalValue / 100.0f);
        if (nextValue <= 0) {
            return;
        }
        if (this.m_context == null || this.m_context.getEffectUserInformationProvider() == null) {
            return;
        }
        if (!this.m_target.hasCharacteristic(FighterCharacteristicType.HP) && !this.m_target.hasCharacteristic(FighterCharacteristicType.AREA_HP)) {
            return;
        }
        if (this.m_target instanceof BasicEffectArea && !((BasicEffectArea)this.m_target).isBlockingMovement()) {
            return;
        }
        final ArrayList<ObjectPair<Integer, EffectUser>> targetsByRange = new ArrayList<ObjectPair<Integer, EffectUser>>();
        if (!(this.m_target instanceof AbstractWallEffectArea)) {
            final Iterator<? extends EffectUser> it = this.getPossibleTargetsAroundOriginalTarget();
            while (it.hasNext()) {
                final EffectUser eu = (EffectUser)it.next();
                if (!eu.hasCharacteristic(FighterCharacteristicType.HP) && !eu.hasCharacteristic(FighterCharacteristicType.AREA_HP)) {
                    continue;
                }
                if (!eu.canBeTargeted()) {
                    continue;
                }
                this.addPossibleSubTargetToNextTargetsIfNecessary(targetsByRange, eu);
            }
        }
        if (!targetsByRange.isEmpty()) {
            this.applyEffectReboundOnNextTargets(targetsByRange);
        }
        super.unapplyOverride();
    }
    
    private Iterator<EffectUser> getPossibleTargetsAroundOriginalTarget() {
        return this.m_context.getTargetInformationProvider().getAllPossibleTargets();
    }
    
    private void addPossibleSubTargetToNextTargetsIfNecessary(final ArrayList<ObjectPair<Integer, EffectUser>> targetsByRange, final EffectUser currentPossibleSubTarget) {
        final int maxDistanceForRebound = this.m_maxCellRange;
        if (this.m_alreadyTargetedTargetIds.contains(currentPossibleSubTarget.getId())) {
            return;
        }
        final SimpleCriterion conditions = ((WakfuEffect)this.m_genericEffect).getConditions();
        if (conditions != null) {
            final boolean valid = conditions.isValid(this.m_caster, currentPossibleSubTarget, ((RunningEffect<FX, Object>)this).getEffectContainer(), this.getContext());
            if (!valid) {
                return;
            }
        }
        final int distanceBetweenSubTargetAndOriginal = DistanceUtils.getIntersectionDistance(currentPossibleSubTarget, this.m_target);
        final boolean isSubTargetTooFar = distanceBetweenSubTargetAndOriginal > maxDistanceForRebound;
        if (isSubTargetTooFar) {
            return;
        }
        if (currentPossibleSubTarget instanceof BasicEffectArea && !((BasicEffectArea)currentPossibleSubTarget).isBlockingMovement()) {
            return;
        }
        if (!(currentPossibleSubTarget instanceof AbstractBeaconEffectArea)) {
            if (!(this.m_caster instanceof FightEffectUser) || !(currentPossibleSubTarget instanceof FightEffectUser)) {
                return;
            }
            if (!this.checkTargetValidity((FightEffectUser)this.m_caster, (FightEffectUser)currentPossibleSubTarget)) {
                return;
            }
        }
        final boolean lineOfSightOk = this.verifyLineOfSight(currentPossibleSubTarget, this.m_target);
        if (!lineOfSightOk) {
            return;
        }
        this.insertTargetInTargetsList(targetsByRange, currentPossibleSubTarget, distanceBetweenSubTargetAndOriginal);
    }
    
    protected abstract boolean checkTargetValidity(final FightEffectUser p0, final FightEffectUser p1);
    
    private boolean verifyLineOfSight(final EffectUser target, final EffectUser caster) {
        final FightMap fightMap = this.m_context.getFightMap();
        if (fightMap == null) {
            AbstractEffectRebound.m_logger.warn((Object)("pas de fightmap sur le context " + this.m_context));
            return false;
        }
        final LineOfSightChecker losChecker = LineOfSightChecker.checkOut();
        losChecker.setStartPoint(caster.getWorldCellX(), caster.getWorldCellY(), caster.getWorldCellAltitude());
        losChecker.setEndPoint(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude());
        losChecker.setTopologyMapInstanceSet(fightMap);
        if (caster instanceof FightObstacle) {
            fightMap.addIgnoredSightObstacle((FightObstacle)caster);
        }
        boolean losCheckOk;
        try {
            if (losChecker.checkLOS()) {
                fightMap.clearIgnoredSightObstacles();
                losChecker.release();
                return true;
            }
            if (target.getHeight() <= 0) {
                fightMap.clearIgnoredSightObstacles();
                losChecker.release();
                return false;
            }
            losChecker.setStartPoint(caster.getWorldCellX(), caster.getWorldCellY(), (short)(target.getWorldCellAltitude() + target.getHeight()));
            losChecker.setEndPoint(target.getWorldCellX(), target.getWorldCellY(), (short)(caster.getWorldCellAltitude() + caster.getHeight()));
            losCheckOk = losChecker.checkLOS();
            losChecker.release();
        }
        catch (Exception e) {
            AbstractEffectRebound.m_logger.error((Object)"Exception levee", (Throwable)e);
            fightMap.clearIgnoredSightObstacles();
            return false;
        }
        return losCheckOk;
    }
    
    void insertTargetInTargetsList(final List<ObjectPair<Integer, EffectUser>> targetsByRange, final EffectUser currentPossibleSubTarget, final int distanceBetweenSubTargetAndOriginal) {
        if (targetsByRange == null) {
            return;
        }
        if (targetsByRange.isEmpty()) {
            targetsByRange.add(new ObjectPair<Integer, EffectUser>(distanceBetweenSubTargetAndOriginal, currentPossibleSubTarget));
        }
        else {
            boolean added = false;
            int index = 0;
            for (final ObjectPair<Integer, EffectUser> pair : targetsByRange) {
                if (pair.getFirst() > distanceBetweenSubTargetAndOriginal) {
                    targetsByRange.add(index, new ObjectPair<Integer, EffectUser>(distanceBetweenSubTargetAndOriginal, currentPossibleSubTarget));
                    added = true;
                    break;
                }
                if (pair.getFirst() == distanceBetweenSubTargetAndOriginal && pair.getSecond().hasCharacteristic(FighterCharacteristicType.HP) && currentPossibleSubTarget.hasCharacteristic(FighterCharacteristicType.HP) && pair.getSecond().getCharacteristicValue(FighterCharacteristicType.HP) > currentPossibleSubTarget.getCharacteristicValue(FighterCharacteristicType.HP)) {
                    targetsByRange.add(index, new ObjectPair<Integer, EffectUser>(distanceBetweenSubTargetAndOriginal, currentPossibleSubTarget));
                    added = true;
                    break;
                }
                ++index;
            }
            if (!added) {
                targetsByRange.add(targetsByRange.size(), new ObjectPair<Integer, EffectUser>(distanceBetweenSubTargetAndOriginal, currentPossibleSubTarget));
            }
        }
    }
    
    private void applyEffectReboundOnNextTargets(final List<ObjectPair<Integer, EffectUser>> targetsByRange) {
        if (!this.isExecuted()) {
            return;
        }
        if (!this.m_isDiffused) {
            this.applyReboundOnNearestTarget(targetsByRange);
        }
        else {
            this.diffuseEffect(targetsByRange);
        }
    }
    
    void applyReboundOnNearestTarget(final List<ObjectPair<Integer, EffectUser>> targetsByRange) {
        if (targetsByRange == null || targetsByRange.isEmpty()) {
            return;
        }
        final EffectUser target = targetsByRange.get(0).getSecond();
        if (this.m_alreadyTargetedTargetIds != null) {
            this.m_alreadyTargetedTargetIds.add(target.getId());
        }
        this.applyRebound(target);
    }
    
    void diffuseEffect(final List<ObjectPair<Integer, EffectUser>> targetsByRange) {
        if (targetsByRange == null) {
            return;
        }
        for (int min = Math.min(this.m_dispersionMax, targetsByRange.size()), dispersionCount = 0; dispersionCount < min; min = Math.min(this.m_dispersionMax, targetsByRange.size()), ++dispersionCount) {
            final EffectUser nextTarget = targetsByRange.get(dispersionCount).getSecond();
            if (this.m_alreadyTargetedTargetIds != null && !this.m_alreadyTargetedTargetIds.contains(nextTarget.getId())) {
                this.m_alreadyTargetedTargetIds.add(nextTarget.getId());
                this.applyRebound(nextTarget);
            }
        }
    }
    
    private void applyRebound(final EffectUser nextTarget) {
        final AbstractEffectRebound re = (AbstractEffectRebound)this.newParameterizedInstance();
        re.applyOnTargets(nextTarget);
        re.release();
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
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_reboundCount = 0;
        this.m_dispersionMax = 0;
        this.m_originalValue = 0;
        this.m_reboundReduction = 0;
        this.m_maxCellRange = 0;
        this.m_alreadyTargetedTargetIds = null;
        this.m_isDiffused = false;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_reboundCount = 0;
        this.m_dispersionMax = 0;
        this.m_originalValue = 0;
        this.m_reboundReduction = 0;
        this.m_maxCellRange = 0;
        this.m_alreadyTargetedTargetIds = null;
        this.m_isDiffused = false;
    }
    
    static {
        EMPTY_BITSET = new BitSet();
    }
}
