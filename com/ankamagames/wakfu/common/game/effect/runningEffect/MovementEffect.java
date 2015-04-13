package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.movementEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effect.*;

public abstract class MovementEffect extends WakfuRunningEffect
{
    private Point3 m_arrivalCell;
    private Point3 m_startCell;
    private int m_fallHeight;
    private boolean m_movementStopped;
    protected float m_lifePointsToLose;
    private int m_distance;
    private float m_collisionDamage;
    private int m_nbCellsCovered;
    private FightObstacle m_obstacle;
    private List<Point3> m_path;
    protected Elements m_collisionDamageElement;
    protected boolean m_computeCollisionDamage;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    public MovementEffect() {
        super();
        this.m_distance = 0;
        this.m_collisionDamage = 0.0f;
        this.m_collisionDamageElement = Elements.EARTH;
        this.m_computeCollisionDamage = false;
        this.ADDITIONNAL_DATAS = new BinarSerialPart(10) {
            @Override
            public int expectedSize() {
                final int pathDataLength = 10 * this.getPathSize();
                return 2 + pathDataLength;
            }
            
            private int getPathSize() {
                return (MovementEffect.this.m_path == null) ? 0 : MovementEffect.this.m_path.size();
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                final int size = this.getPathSize();
                buffer.putShort((short)size);
                for (int i = 0; i < size; ++i) {
                    final Point3 point3 = MovementEffect.this.m_path.get(i);
                    buffer.putInt(point3.getX());
                    buffer.putInt(point3.getY());
                    buffer.putShort(point3.getZ());
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final short size = buffer.getShort();
                if (size == 0) {
                    return;
                }
                MovementEffect.this.m_path = (List<Point3>)new ArrayList();
                for (int i = 0; i < size; ++i) {
                    MovementEffect.this.m_path.add(new Point3(buffer.getInt(), buffer.getInt(), buffer.getShort()));
                }
                MovementEffect.this.m_arrivalCell = MovementEffect.this.m_path.get(MovementEffect.this.m_path.size() - 1);
            }
        };
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(185);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final MovementEffectUser mover = this.getMover();
        if (mover == null) {
            this.setNotified(true);
            return;
        }
        if (this.isValueComputationEnabled()) {
            this.computeMovementAndDamage();
        }
        if (this.m_arrivalCell == null) {
            if (this.isValueComputationEnabled()) {
                this.executeCollisionDamage();
            }
            this.setNotified(true);
            return;
        }
        if (this.m_context instanceof WakfuFightEffectContext && this.isValueComputationEnabled()) {
            this.areaInteractions();
        }
        final Point3 startCell = new Point3(mover.getWorldCellX(), mover.getWorldCellY(), mover.getWorldCellAltitude());
        if (!startCell.equals(this.m_arrivalCell)) {
            this.uncarryIfNecessary();
            this.teleportCasterToNewPos();
        }
        this.m_value = this.m_path.size();
        this.notifyExecution(linkedRE, trigger);
        if (!this.isValueComputationEnabled()) {
            return;
        }
        this.executeCollisionDamage();
        this.triggerMovement(startCell);
        if (mover instanceof AbstractBombEffectArea && this.isValueComputationEnabled()) {
            this.destroyBombOnSameCellIfNecessary((AbstractBombEffectArea)mover);
        }
    }
    
    protected void uncarryIfNecessary() {
        final MovementEffectUser mover = this.getMover();
        if (!(mover instanceof Carrier)) {
            return;
        }
        final Carrier target = (Carrier)mover;
        if (!target.isCarrying()) {
            return;
        }
        final int effectId = Carry.CARRIER_EFFECT_ID.get(mover.getId());
        final AbstractEffectGroup group = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(effectId);
        if (group == null || group.getEffectsCount() == 0) {
            return;
        }
        final WakfuEffect effect = group.getEffect(0);
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(true, false, null);
        try {
            effect.execute(this.getEffectContainer(), mover, this.getContext(), RunningEffectConstants.getInstance(), mover.getWorldCellX(), mover.getWorldCellY(), mover.getWorldCellAltitude(), null, params, false);
        }
        catch (Exception e) {
            MovementEffect.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        params.release();
    }
    
    private void destroyBombOnSameCellIfNecessary(final AbstractBombEffectArea bomb) {
        final EffectContext context = bomb.getContext();
        if (context == null) {
            return;
        }
        final BasicEffectAreaManager effectAreaManager = context.getEffectAreaManager();
        if (effectAreaManager == null) {
            return;
        }
        final Point3 position = bomb.getPosition();
        final Collection<BasicEffectArea> activeAreas = effectAreaManager.getActiveEffectAreas();
        final ArrayList<BasicEffectArea> areas = new ArrayList<BasicEffectArea>(activeAreas);
        for (final BasicEffectArea area : areas) {
            if (area.getPosition().equals(position) && area != bomb && area.getType() == EffectAreaType.BOMB.getTypeId()) {
                area.triggers(this, null);
            }
        }
    }
    
    private void areaInteractions() {
        final AbstractFight<BasicCharacterInfo> fight = ((WakfuFightEffectContext)this.m_context).getFight();
        if (this.m_path.isEmpty()) {
            return;
        }
        if (!(this.getMover() instanceof BasicCharacterInfo)) {
            return;
        }
        final AreaOccupationComputer areaOccupationComputer = new AreaOccupationComputer(fight, this.getMover(), this.m_path.get(0));
        areaOccupationComputer.setInitialState();
        for (int i = 0, size = this.m_path.size(); i < size; ++i) {
            final Point3 nextPathCell = this.m_path.get(i);
            areaOccupationComputer.setMoverDestinationCell(nextPathCell);
            areaOccupationComputer.computeAreaModificationsOnMove();
            if (areaOccupationComputer.willTriggerSomething() && areaOccupationComputer.hasToBeStopByAreaInteraction()) {
                this.m_path = this.m_path.subList(0, i + 1);
                this.m_arrivalCell = this.m_path.get(this.m_path.size() - 1);
                break;
            }
            areaOccupationComputer.setInitialState(nextPathCell);
        }
    }
    
    private void triggerMovement(final Point3 startCell) {
        if (this.m_path == null) {
            return;
        }
        final AbstractFight<BasicCharacterInfo> fight = ((WakfuFightEffectContext)this.m_context).getFight();
        final AreaOccupationComputer areaOccupationComputer = new AreaOccupationComputer(fight, this.getMover(), this.m_path.get(0));
        areaOccupationComputer.setInitialState(startCell);
        final Direction8 dir = new Vector3i(startCell.getX(), startCell.getY(), startCell.getZ(), this.getMover().getWorldCellX(), this.getMover().getWorldCellY(), this.getMover().getWorldCellAltitude()).toDirection4();
        for (int i = 0, size = this.m_path.size(); i < size; ++i) {
            final Point3 nextPathCell = this.m_path.get(i);
            areaOccupationComputer.setMoverDestinationCell(nextPathCell);
            areaOccupationComputer.computeAreaModificationsOnMove();
            if (areaOccupationComputer.willTriggerSomething()) {
                this.getMover().setSpecialMovementDirection(dir);
                try {
                    areaOccupationComputer.triggerAreaEffects();
                }
                catch (Exception e) {
                    MovementEffect.m_logger.error((Object)"Exception levee", (Throwable)e);
                }
                finally {
                    this.getMover().setSpecialMovementDirection(null);
                }
            }
            areaOccupationComputer.setInitialState(nextPathCell);
        }
    }
    
    boolean moverIsCarried() {
        return this.getMover().isCarried();
    }
    
    boolean moverIsRooted() {
        return this.getMover().isActiveProperty(FightPropertyType.ROOTED);
    }
    
    boolean moverIsStabilized() {
        return this.getMover().isActiveProperty(FightPropertyType.STABILIZED);
    }
    
    boolean moverCantBePushOrPull() {
        return this.getMover().isActiveProperty(FightPropertyType.CANT_BE_PUSH_OR_PULLED);
    }
    
    boolean moverCantMoveAwayOrRepell() {
        return this.getMover().isActiveProperty(FightPropertyType.CANT_MOVE_AWAY_OR_REPELL);
    }
    
    boolean moverCantGetCloser() {
        return this.getMover().isActiveProperty(FightPropertyType.CANT_GET_CLOSER);
    }
    
    private void teleportCasterToNewPos() {
        this.getMover().teleport(this.m_arrivalCell.getX(), this.m_arrivalCell.getY(), this.m_arrivalCell.getZ());
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.getMover() == null) {
            return;
        }
        if (!this.validatePrecondition()) {
            this.m_arrivalCell = null;
            return;
        }
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
                this.m_distance = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            }
            else {
                this.m_distance = this.m_targetCell.getDistance(this.getMover().getWorldCellX(), this.getMover().getWorldCellY());
            }
            if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
                this.m_collisionDamage = ((WakfuEffect)this.m_genericEffect).getParam(1, level);
            }
            else {
                this.m_collisionDamage = 0.0f;
            }
        }
        else {
            this.m_distance = this.m_targetCell.getDistance(this.getMover().getWorldCellX(), this.getMover().getWorldCellY());
            this.m_collisionDamage = 0.0f;
        }
    }
    
    private void computeMovementAndDamage() {
        this.resetResults();
        this.computeMovement();
        this.computeDamage();
        this.m_value = this.m_nbCellsCovered;
    }
    
    private void computeDamage() {
        this.m_lifePointsToLose = 0.0f;
        if (!this.doesCollide()) {
            return;
        }
        if (this.moverWillStopOnReferentialCell()) {
            return;
        }
        final int cellLeft = this.m_distance - this.m_nbCellsCovered;
        if (cellLeft > 0) {
            this.m_lifePointsToLose = cellLeft * this.m_collisionDamage;
        }
    }
    
    private boolean moverWillStopOnReferentialCell() {
        if (this.m_arrivalCell == null) {
            return false;
        }
        final Point3 referentialCell = this.getReferentialCell();
        return referentialCell.getX() == this.m_arrivalCell.getX() && referentialCell.getY() == this.m_arrivalCell.getY() && referentialCell.getZ() == this.m_arrivalCell.getZ();
    }
    
    public void computeMovement() {
        if (this.m_context == null || this.m_context.getFightMap() == null) {
            MovementEffect.m_logger.error((Object)("Pas de fightMap pour le context " + this.m_context));
            return;
        }
        final PathComputer pathComputer = new PathComputer(this.getMover(), this.getReferentialCell(), this.m_distance, (EffectContext<WakfuEffect>)this.m_context);
        if (!this.getCloser()) {
            pathComputer.setOppositeMovement(true);
        }
        this.m_lifePointsToLose = 0.0f;
        final PathComputationResult res = pathComputer.computeMovement();
        if (res == null) {
            return;
        }
        this.m_obstacle = res.getObstacle();
        this.m_movementStopped = res.isBlocked();
        if (res.getArrivalCell() == null) {
            return;
        }
        this.m_path = res.getPath();
        this.m_nbCellsCovered = res.getNbCellsCovered();
        this.m_fallHeight = res.getFallHeight();
        this.m_startCell = new Point3(this.getMover().getWorldCellX(), this.getMover().getWorldCellY(), this.getMover().getWorldCellAltitude());
        this.m_arrivalCell = new Point3(res.getArrivalCell());
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.resetResults();
        this.m_collisionDamage = 0.0f;
        this.m_distance = 0;
        this.m_collisionDamageElement = Elements.EARTH;
        this.m_computeCollisionDamage = false;
    }
    
    private void resetResults() {
        this.m_arrivalCell = null;
        this.m_startCell = null;
        this.m_fallHeight = 0;
        this.m_movementStopped = false;
        this.m_obstacle = null;
        this.m_lifePointsToLose = 0.0f;
        this.m_nbCellsCovered = 0;
    }
    
    @Override
    protected boolean checkIsNotValidTargetProperty() {
        return this.isNotValidTargetAndContainerNotSpecialState();
    }
    
    public Point3 getArrivalCell() {
        return this.m_arrivalCell;
    }
    
    public int getNbCellsCovered() {
        return this.m_nbCellsCovered;
    }
    
    public Point3 getStartCell() {
        return this.m_startCell;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    private void executeCollisionDamage() {
        if (this.m_lifePointsToLose > 0.0f) {
            final HPLoss hpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_collisionDamageElement, HPLoss.ComputeMode.CLASSIC, ValueRounder.randomRound(this.m_lifePointsToLose), this.getMover());
            hpLoss.setCaster(this.m_caster);
            if (this.m_computeCollisionDamage) {
                hpLoss.computeModificatorWithDefaults();
                this.m_lifePointsToLose = hpLoss.getValue();
            }
            hpLoss.disableValueComputation();
            ((RunningEffect<DefaultFightInstantEffectWithChatNotif, EC>)hpLoss).setGenericEffect(DefaultFightInstantEffectWithChatNotif.getInstance());
            hpLoss.getTriggersToExecute().set(204);
            hpLoss.applyOnTargets(this.getMover());
            hpLoss.release();
            if (this.m_obstacle != null && this.m_obstacle instanceof EffectUser && ((EffectUser)this.m_obstacle).hasCharacteristic(FighterCharacteristicType.HP)) {
                final int halfDamages = ValueRounder.randomRound(this.m_lifePointsToLose / 2.0f);
                final HPLoss obstacleHpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_collisionDamageElement, HPLoss.ComputeMode.CLASSIC, halfDamages, (EffectUser)this.m_obstacle);
                ((RunningEffect<DefaultFightInstantEffectWithChatNotif, EC>)obstacleHpLoss).setGenericEffect(DefaultFightInstantEffectWithChatNotif.getInstance());
                obstacleHpLoss.setCaster(this.m_caster);
                obstacleHpLoss.getTriggersToExecute().set(204);
                obstacleHpLoss.applyOnTargets((EffectUser)this.m_obstacle);
                obstacleHpLoss.release();
            }
        }
    }
    
    abstract boolean getCloser();
    
    public abstract boolean validatePrecondition();
    
    abstract boolean doesCollide();
    
    public abstract MovementEffectUser getMover();
    
    abstract Point3 getReferentialCell();
    
    boolean targetAndCasterOnSameCell() {
        return this.m_target.getWorldCellX() == this.m_caster.getWorldCellX() && this.m_target.getWorldCellY() == this.m_caster.getWorldCellY();
    }
    
    protected boolean ignoreStabilisation() {
        return this.hasProperty(RunningEffectPropertyType.IGNORE_STABILISATION);
    }
}
