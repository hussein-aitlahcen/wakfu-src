package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class BlitzkriekEffect extends UsingEffectGroupRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private Elements m_element;
    private short m_stateId;
    private short m_stateLevel;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return BlitzkriekEffect.PARAMETERS_LIST_SET;
    }
    
    public BlitzkriekEffect() {
        super();
        this.m_element = Elements.EARTH;
        this.m_stateId = 0;
        this.m_stateLevel = -1;
        this.setTriggersToExecute();
    }
    
    @Override
    public BlitzkriekEffect newInstance() {
        BlitzkriekEffect re;
        try {
            re = (BlitzkriekEffect)BlitzkriekEffect.m_staticPool.borrowObject();
            re.m_pool = BlitzkriekEffect.m_staticPool;
        }
        catch (Exception e) {
            re = new BlitzkriekEffect();
            re.m_pool = null;
            re.m_isStatic = false;
            BlitzkriekEffect.m_logger.error((Object)("Erreur lors d'un checkOut sur un BlitzkriegEffect : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null || ((WakfuEffect)this.m_genericEffect).getParamsCount() == 0) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 2) {
            this.m_element = Elements.getElementFromId((byte)((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL));
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 3) {
            this.m_stateId = (short)((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            this.m_stateLevel = (short)((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (this.m_caster == null || this.m_targetCell == null || !(this.m_caster instanceof BasicCharacterInfo)) {
            return;
        }
        final Point3 castPosition = ((BasicCharacterInfo)this.m_caster).getPositionConst();
        final Direction8 attackDirection = castPosition.getDirection4To(this.m_targetCell);
        final List<EffectUser> hitTargets = this.getHitTargets(castPosition, attackDirection);
        if (hitTargets.isEmpty()) {
            this.setNotified();
            return;
        }
        final Point3 arrivalCell = this.computeArrivalCell(castPosition, attackDirection, hitTargets);
        if (arrivalCell == null) {
            return;
        }
        for (final EffectUser hitTarget : hitTargets) {
            if (hitTarget.canBeTargeted()) {
                this.executeEffectGroup((WakfuRunningEffect)triggerRE, hitTarget);
            }
        }
        this.executeMovement(triggerRE, hitTargets, arrivalCell);
    }
    
    private void executeMovement(final RunningEffect triggerRE, final List<EffectUser> hitTargets, final Point3 arrivalCell) {
        try {
            final TeleportCaster teleport = TeleportCaster.checkOut((EffectContext<WakfuEffect>)this.m_context, (WakfuEffect)this.m_genericEffect, this.m_caster, (WakfuEffectContainer)this.m_effectContainer, arrivalCell);
            teleport.setId(RunningEffectConstants.TELEPORT_CASTER.getId());
            teleport.askForExecution();
        }
        catch (Exception e) {
            BlitzkriekEffect.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    private Point3 computeArrivalCell(final Point3 castPosition, final Direction8 attackDirection, final List<EffectUser> hitTargets) {
        EffectUser farthestHitTarget = null;
        for (final EffectUser hitTarget : hitTargets) {
            if (farthestHitTarget == null) {
                farthestHitTarget = hitTarget;
            }
            else {
                if (castPosition.getDistance(hitTarget.getWorldCellX(), hitTarget.getWorldCellY()) <= castPosition.getDistance(farthestHitTarget.getWorldCellX(), farthestHitTarget.getWorldCellY())) {
                    continue;
                }
                farthestHitTarget = hitTarget;
            }
        }
        if (farthestHitTarget == null) {
            return null;
        }
        final Point3 arrivalCell = new Point3(farthestHitTarget.getWorldCellX(), farthestHitTarget.getWorldCellY(), farthestHitTarget.getWorldCellAltitude());
        arrivalCell.shift(attackDirection);
        final BasicCharacterInfo caster = (BasicCharacterInfo)this.m_caster;
        final short nearestWalkableZ = TopologyMapManager.getNearestWalkableZ(caster.getInstanceId(), arrivalCell.getX(), arrivalCell.getY(), arrivalCell.getZ(), (short)0);
        arrivalCell.setZ(nearestWalkableZ);
        return arrivalCell;
    }
    
    private List<EffectUser> getHitTargets(final Point3 castPosition, final Direction8 attackDirection) {
        final int distanceMax = this.getDistanceMaxReachable(castPosition, attackDirection);
        final Iterator<EffectUser> allPossibleTargets = this.m_context.getTargetInformationProvider().getAllPossibleTargets();
        final List<EffectUser> hitTargets = new ArrayList<EffectUser>();
        final int directionX = attackDirection.m_x;
        final int directionY = attackDirection.m_y;
        while (allPossibleTargets.hasNext()) {
            final EffectUser next = allPossibleTargets.next();
            if (next instanceof CarryTarget && ((CarryTarget)next).isCarried()) {
                continue;
            }
            if (!this.effectUserCharacToBeTargetted(next)) {
                continue;
            }
            final double diffX = next.getWorldX() - castPosition.getX();
            final double diffY = next.getWorldY() - castPosition.getY();
            if (Math.abs(diffX) + Math.abs(diffY) >= distanceMax) {
                continue;
            }
            if ((diffX != directionX && diffX / Math.max(Math.abs(diffX), 1.0) != directionX) || (diffY != directionY && diffY / Math.max(Math.abs(diffY), 1.0) != directionY)) {
                continue;
            }
            hitTargets.add(next);
        }
        return hitTargets;
    }
    
    private boolean effectUserCharacToBeTargetted(final EffectUser next) {
        return next.hasCharacteristic(FighterCharacteristicType.HP) || next.hasCharacteristic(FighterCharacteristicType.AREA_HP) || next.hasCharacteristic(FighterCharacteristicType.XELORS_DIAL_CHARGE);
    }
    
    private int getDistanceMaxReachable(final Point3 castPosition, final Direction8 attackDirection) {
        final FightMap map = this.m_context.getFightMap();
        if (map == null) {
            BlitzkriekEffect.m_logger.error((Object)("pas de fightmap sur le context " + this.m_context));
            return 0;
        }
        final BasicCharacterInfo caster = (BasicCharacterInfo)this.m_caster;
        final Point3 currentCheckedCell = new Point3(castPosition);
        final Point3 farthestCellReachable = new Point3(castPosition);
        short previousZ = currentCheckedCell.getZ();
        currentCheckedCell.shift(attackDirection);
        short nearestWalkableZ = TopologyMapManager.getNearestWalkableZ(caster.getInstanceId(), currentCheckedCell.getX(), currentCheckedCell.getY(), previousZ, (short)0);
        if (this.altitudeDiffExceedJumpCapacity(previousZ, nearestWalkableZ)) {
            return 0;
        }
        farthestCellReachable.set(currentCheckedCell);
        while (map.isInsideOrBorder(currentCheckedCell.getX(), currentCheckedCell.getY()) && map.isWalkable(currentCheckedCell.getX(), currentCheckedCell.getY(), nearestWalkableZ)) {
            if (!map.isMovementBlocked(currentCheckedCell.getX(), currentCheckedCell.getY(), currentCheckedCell.getZ())) {
                farthestCellReachable.set(currentCheckedCell);
            }
            previousZ = nearestWalkableZ;
            currentCheckedCell.shift(attackDirection);
            nearestWalkableZ = TopologyMapManager.getNearestWalkableZ(caster.getInstanceId(), currentCheckedCell.getX(), currentCheckedCell.getY(), previousZ, (short)0);
            if (this.altitudeDiffExceedJumpCapacity(previousZ, nearestWalkableZ)) {
                break;
            }
        }
        return castPosition.getDistance(farthestCellReachable);
    }
    
    private boolean altitudeDiffExceedJumpCapacity(final short previousZ, final short nearestWalkableZ) {
        return Math.abs(previousZ - nearestWalkableZ) > 4;
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    @Override
    public Elements getElement() {
        return this.m_element;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_stateId = 0;
        this.m_stateLevel = -1;
        this.m_element = Elements.EARTH;
    }
    
    @Override
    protected boolean isProbabilityComputationDisabled() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<BlitzkriekEffect>() {
            @Override
            public BlitzkriekEffect makeObject() {
                return new BlitzkriekEffect();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Valeur de la perte de Pdv", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Valeur de la perte de Pdv", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Perte de Pdv", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Valeur de la perte de Pdv + element", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Perte de Pdv", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Element", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Valeur de la perte de Pdv et de l'\u00e9tat \u00e0 appliquer", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Perte de Pdv", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
