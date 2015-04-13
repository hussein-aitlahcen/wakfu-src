package com.ankamagames.baseImpl.common.clientAndServer.game.effectArea;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.ai.targetfinder.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public abstract class BasicEffectArea<FX extends Effect, P extends BasicEffectAreaParameters> implements EffectContainer<FX>, Poolable, EffectUser, FightEffectUser, FightObstacle, AreaOwnerProvider, EffectAreaProvider
{
    protected static final Logger m_logger;
    public static final int SERIALIZATION_SIZE = 45;
    protected BitSet m_applicationTriggers;
    protected BitSet m_unapplicationTriggers;
    protected GrowingArray<FX> m_effects;
    protected long m_baseId;
    protected long m_id;
    private byte m_obstacleId;
    protected final Point3 m_position;
    protected AreaOfEffect m_area;
    protected EffectContext m_context;
    protected ObjectPool m_pool;
    protected EffectUser m_owner;
    protected int m_maxExecutionCount;
    protected final ArrayList<Target> m_unactiveForTargets;
    protected float[] m_deactivationDelay;
    private EffectAreaActionListener m_listener;
    protected int m_targetsToShow;
    protected float[] m_params;
    protected boolean m_canBeTargeted;
    protected boolean m_canBeTargetedByAI;
    protected boolean m_canBeDestroyed;
    protected boolean m_isOffPlay;
    private boolean m_canBeComputedAndExecuted;
    private boolean m_disappearWithOwner;
    protected Iterable<int[]> m_zone;
    private int m_maxLevel;
    private boolean m_isInExecution;
    private boolean m_stateChange;
    
    protected BasicEffectArea() {
        super();
        this.m_position = new Point3();
        this.m_unactiveForTargets = new ArrayList<Target>();
        this.m_canBeTargeted = false;
        this.m_canBeTargetedByAI = false;
        this.m_canBeDestroyed = true;
        this.m_isOffPlay = false;
        this.m_canBeComputedAndExecuted = true;
        this.m_disappearWithOwner = true;
        this.m_isInExecution = false;
        this.m_stateChange = false;
    }
    
    public int expectedSerializedEffectUserDataSize() {
        return 8;
    }
    
    @Override
    public void unserializeEffectUser(final byte[] serializedEffectUserDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(serializedEffectUserDatas);
        this.unserializeEffectUser(buffer);
    }
    
    public void unserializeEffectUser(final ByteBuffer buffer) {
        final long id = buffer.getLong();
        if (this.m_context != null && id != 0L) {
            this.setOwner(this.m_context.getEffectUserInformationProvider().getEffectUserFromId(id));
        }
        else {
            BasicEffectArea.m_logger.error((Object)"contexte non initialis\u00e9");
        }
    }
    
    @Override
    public byte[] serializeEffectUser() {
        final ByteBuffer buffer = ByteBuffer.allocate(this.expectedSerializedEffectUserDataSize());
        buffer.putLong((this.m_owner != null) ? this.m_owner.getId() : 0L);
        return buffer.array();
    }
    
    public BasicEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargeted, final boolean canBeDestroyed, final int maxLevel) {
        super();
        this.m_position = new Point3();
        this.m_unactiveForTargets = new ArrayList<Target>();
        this.m_canBeTargeted = false;
        this.m_canBeTargetedByAI = false;
        this.m_canBeDestroyed = true;
        this.m_isOffPlay = false;
        this.m_canBeComputedAndExecuted = true;
        this.m_disappearWithOwner = true;
        this.m_isInExecution = false;
        this.m_stateChange = false;
        this.onCheckOut();
        this.m_baseId = baseId;
        this.m_area = area;
        this.m_applicationTriggers = applicationtriggers;
        this.m_unapplicationTriggers = unapplicationtriggers;
        this.m_maxExecutionCount = maxExecutionCount;
        this.m_targetsToShow = targetsToShow;
        this.m_deactivationDelay = deactivationDelay;
        this.m_params = params;
        this.m_canBeTargeted = canBeTargeted;
        this.m_canBeDestroyed = canBeDestroyed;
        this.m_maxLevel = maxLevel;
    }
    
    public BasicEffectArea instanceAnother(final P parameters) {
        final BasicEffectArea area = this.instanceNew();
        area.setArea(this.m_area);
        area.m_applicationTriggers = this.m_applicationTriggers;
        area.m_unapplicationTriggers = this.m_unapplicationTriggers;
        area.m_effects = this.m_effects;
        area.m_baseId = this.m_baseId;
        area.m_maxExecutionCount = this.m_maxExecutionCount;
        area.m_deactivationDelay = this.m_deactivationDelay;
        area.m_targetsToShow = this.m_targetsToShow;
        area.m_params = this.m_params;
        area.m_canBeTargeted = this.m_canBeTargeted;
        area.m_canBeTargetedByAI = this.m_canBeTargetedByAI;
        area.m_canBeComputedAndExecuted = this.m_canBeComputedAndExecuted;
        area.m_canBeDestroyed = this.m_canBeDestroyed;
        area.m_maxLevel = this.m_maxLevel;
        if (parameters != null) {
            area.m_id = parameters.getId();
            area.setPosition(parameters.getX(), parameters.getY(), parameters.getZ());
            area.m_context = parameters.getContext();
            area.setOwner(parameters.getOwner());
        }
        area.m_unactiveForTargets.clear();
        return area;
    }
    
    protected abstract BasicEffectArea instanceNew();
    
    @Override
    public abstract int getType();
    
    @Override
    public void onCheckOut() {
        this.m_effects = new GrowingArray<FX>();
        this.m_id = 0L;
        this.m_position.setX(0);
        this.m_position.setY(0);
        this.m_position.setZ((short)0);
        this.m_area = null;
        this.m_context = null;
        this.m_owner = null;
        this.m_maxExecutionCount = 0;
        this.m_listener = null;
        this.m_isOffPlay = false;
        this.m_unactiveForTargets.clear();
        this.m_isInExecution = false;
        this.m_maxLevel = -1;
    }
    
    @Override
    public void onCheckIn() {
        this.m_effects = null;
        this.m_id = 0L;
        this.m_position.setX(0);
        this.m_position.setY(0);
        this.m_position.setZ((short)0);
        this.m_area = null;
        this.m_context = null;
        this.m_owner = null;
        this.m_maxExecutionCount = 0;
        this.m_listener = null;
        this.m_isOffPlay = false;
        this.m_unactiveForTargets.clear();
        this.m_zone = null;
        this.m_maxLevel = -1;
    }
    
    public void release() {
        if (this.m_pool != null) {
            try {
                this.m_pool.returnObject(this);
            }
            catch (Exception e) {
                BasicEffectArea.m_logger.error((Object)"impossible");
            }
            this.m_pool = null;
        }
        else {
            this.onCheckIn();
        }
    }
    
    public long getBaseId() {
        return this.m_baseId;
    }
    
    public void setListener(final EffectAreaActionListener listener) {
        this.m_listener = listener;
    }
    
    public GrowingArray<FX> getEffects() {
        return this.m_effects;
    }
    
    public abstract boolean hasNoExecutionCount();
    
    public abstract boolean hasActivationDelay();
    
    protected byte concernedCheck() {
        return 1;
    }
    
    @Override
    public boolean trigger(final BitSet triggers, final RunningEffect triggerer, final byte options) {
        if (this.getRunningEffectManager() != null) {
            this.getRunningEffectManager().trigger(triggers, triggerer, options);
        }
        if (triggerer == null) {
            return false;
        }
        final byte concernedCheck = this.concernedCheck();
        if (options == concernedCheck) {
            boolean check = true;
            if (triggerer.getGenericEffect() == null) {
                final Point3 targetCell = triggerer.getTargetCell();
                if (!this.m_position.equals(targetCell)) {
                    check = false;
                }
            }
            if (check && this.checkTriggers(triggers, triggerer.getCaster())) {
                this.triggers(triggers, triggerer, triggerer.getCaster());
                return true;
            }
        }
        return false;
    }
    
    @Override
    public EffectUser getOwner() {
        return this.m_owner;
    }
    
    @Override
    public AbstractCharacteristic getCharacteristic(final CharacteristicType charac) {
        return null;
    }
    
    @Override
    public boolean hasCharacteristic(final CharacteristicType charac) {
        return false;
    }
    
    @Override
    public int getCharacteristicValue(final CharacteristicType charac) throws UnsupportedOperationException {
        final AbstractCharacteristic cha = this.getCharacteristic(charac);
        if (cha != null) {
            return cha.value();
        }
        throw new UnsupportedOperationException("caract\u00e9ristique inexistante " + charac);
    }
    
    @Override
    public Direction8 getDirection() {
        return Direction8.NORTH_EAST;
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
    }
    
    @Override
    public Direction8 getMovementDirection() {
        return null;
    }
    
    @Override
    public void setSpecialMovementDirection(final Direction8 direction) {
    }
    
    @Override
    public PartLocalisator getPartLocalisator() {
        return null;
    }
    
    @Override
    public boolean mustGoOffPlay() {
        return this.m_maxExecutionCount == 0 && !this.m_isInExecution;
    }
    
    @Override
    public boolean mustGoBackInPlay() {
        return false;
    }
    
    @Override
    public RunningEffectManager getRunningEffectManager() {
        return null;
    }
    
    @Override
    public boolean isValidForEffectExecution() {
        return true;
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public long getEffectContainerId() {
        return this.m_id;
    }
    
    @Override
    public int getWorldCellX() {
        return this.m_position.getX();
    }
    
    @Override
    public int getWorldCellY() {
        return this.m_position.getY();
    }
    
    @Override
    public short getWorldCellAltitude() {
        return this.m_position.getZ();
    }
    
    @Override
    public float getWorldX() {
        return this.m_position.getX();
    }
    
    @Override
    public float getWorldY() {
        return this.m_position.getY();
    }
    
    @Override
    public float getAltitude() {
        return this.m_position.getZ();
    }
    
    public int getMaxLevel() {
        return this.m_maxLevel;
    }
    
    @Override
    public void setPosition(final int x, final int y, final short alt) {
        this.m_position.setX(x);
        this.m_position.setY(y);
        this.m_position.setZ(alt);
        if (this.m_listener != null) {
            this.m_listener.onEffectAreaPositionChanged(this);
        }
    }
    
    @Override
    public final void setPosition(final Point3 pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public boolean isPositionStatic() {
        return true;
    }
    
    public EffectContext getContext() {
        return this.m_context;
    }
    
    @Override
    public void teleport(final int x, final int y, final short z) {
        throw new UnsupportedOperationException("Teleport de BasicEffectArea non impl\u00e9ment\u00e9");
    }
    
    public void teleport(final Point3 pos) {
        throw new UnsupportedOperationException("Teleport de BasicEffectArea non impl\u00e9ment\u00e9");
    }
    
    @Override
    public int getContainerType() {
        return 3;
    }
    
    public void addEffect(final FX effect) {
        this.m_effects.add(effect);
    }
    
    public void addEffects(final FX[] effects) {
        this.m_effects.add(effects);
    }
    
    @Override
    public Iterator<FX> iterator() {
        return this.m_effects.iterator();
    }
    
    public void setArea(final AreaOfEffect area) {
        this.m_area = area;
    }
    
    public AreaOfEffect getArea() {
        return this.m_area;
    }
    
    public void setPool(final ObjectPool pool) {
        this.m_pool = pool;
    }
    
    public boolean contains(final Point3 point) {
        return point != null && this.contains(point.getX(), point.getY(), point.getZ());
    }
    
    public boolean contains(final int x, final int y, final short z) {
        if (this.m_area == null) {
            BasicEffectArea.m_logger.error((Object)"m_area est null");
            return false;
        }
        if (this.m_zone != null) {
            for (final int[] ints : this.m_zone) {
                if (ints[0] == x && ints[1] == y) {
                    return true;
                }
            }
            return false;
        }
        return this.m_area.isPointInside(this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), x, y, z);
    }
    
    public boolean contains(final EffectUser user, final int x, final int y, final short z) {
        if (this.contains(x, y, z)) {
            return true;
        }
        final byte radius = user.getPhysicalRadius();
        if (radius == 0) {
            return false;
        }
        for (int radiusX = -radius; radiusX <= radius; ++radiusX) {
            for (int radiusY = -radius; radiusY <= radius; ++radiusY) {
                if (this.contains(x + radiusX, y + radiusY, z)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void goOffPlay(final EffectUser killer) {
        this.setUnactive(null);
        this.onGoesOffPlay();
        this.m_isOffPlay = true;
    }
    
    @Override
    public void goBackInPlay(final EffectUser killer) {
    }
    
    public boolean checkTriggers(final int specificTrigger, final Target applicant) {
        final BitSet trigger = new BitSet();
        trigger.set(specificTrigger);
        return this.checkTriggers(trigger, applicant);
    }
    
    public boolean checkApplicationTriggers(final int triggers) {
        return this.m_applicationTriggers.get(triggers);
    }
    
    public boolean checkTriggers(final BitSet triggers, final Target applicant) {
        return this.m_applicationTriggers.intersects(triggers) || this.m_unapplicationTriggers.intersects(triggers);
    }
    
    public void triggers(final int specificTrigger, final RunningEffect triggeringRE, final Target applicant) {
        final BitSet trigger = new BitSet();
        trigger.set(specificTrigger);
        this.triggers(trigger, triggeringRE, applicant);
    }
    
    @Deprecated
    public void triggers(final BitSet triggers, final Target applicant) {
        this.triggers(triggers, null, applicant);
    }
    
    public void triggers(final BitSet triggers, @Nullable final RunningEffect triggeringRE, final Target applicant) {
        if (this.m_applicationTriggers.intersects(triggers)) {
            this.triggers(triggeringRE, applicant);
        }
        if (this.m_unapplicationTriggers.intersects(triggers)) {
            this.untriggers(applicant);
        }
    }
    
    public boolean mayTrigger(final int specificTrigger) {
        final BitSet trigger = new BitSet();
        trigger.set(specificTrigger);
        return this.m_applicationTriggers.intersects(trigger) || this.m_unapplicationTriggers.intersects(trigger);
    }
    
    public void setActive(final Target triggerer) {
        if (triggerer != null) {
            this.m_unactiveForTargets.remove(triggerer);
        }
        this.onActivation(triggerer);
    }
    
    public void setUnactive(final Target triggerer) {
        if (triggerer != null && !this.m_unactiveForTargets.contains(triggerer)) {
            this.m_unactiveForTargets.add(triggerer);
        }
        this.onDeactivation(triggerer);
    }
    
    public void setCanBeComputedAndExecuted(final boolean canBeComputedAndExecuted) {
        this.m_canBeComputedAndExecuted = canBeComputedAndExecuted;
    }
    
    protected boolean canBeComputedAndExecuted() {
        return this.m_canBeComputedAndExecuted;
    }
    
    public boolean canBeDestroyed() {
        return this.m_canBeDestroyed;
    }
    
    public boolean triggers(@Nullable final RunningEffect triggeringRE, final Target triggerer) {
        if (this.canBeTriggeredBy(triggerer) && this.m_maxExecutionCount != 0 && !this.m_isInExecution) {
            if (this.hasActivationDelay()) {
                this.setUnactive(triggerer);
                this.pushActivationEventForTargetInTimeline(triggerer);
            }
            this.onApplication(triggerer);
            if (this.canBeComputedAndExecuted()) {
                if (!this.hasNoExecutionCount() && this.m_maxExecutionCount > 0) {
                    --this.m_maxExecutionCount;
                }
                if (this.m_listener != null) {
                    try {
                        this.m_listener.onEffectAreaPreExecution(this, triggerer);
                    }
                    catch (Exception e) {
                        BasicEffectArea.m_logger.error((Object)"Exception levee", (Throwable)e);
                    }
                }
                final long[] targets = this.determineApplicationTargetCells(triggerer);
                this.m_isInExecution = true;
                if (targets != null) {
                    for (int index = targets.length - 1; index >= 0; --index) {
                        final long target = targets[index];
                        try {
                            this.execute(PositionValue.getXFromLong(target), PositionValue.getYFromLong(target), PositionValue.getZFromLong(target), triggeringRE);
                        }
                        catch (Exception e2) {
                            BasicEffectArea.m_logger.error((Object)"Exception levee lors de l'execution des effets d'une zone", (Throwable)e2);
                        }
                    }
                }
                this.m_isInExecution = false;
            }
            if (this.m_listener != null) {
                try {
                    this.m_listener.onEffectAreaExecuted(this);
                }
                catch (Exception e) {
                    BasicEffectArea.m_logger.error((Object)"Exception levee", (Throwable)e);
                }
            }
            return true;
        }
        return false;
    }
    
    public abstract long[] determineApplicationTargetCells(final Target p0);
    
    public abstract List<Target> determineUnapplicationTarget(final Target p0);
    
    public abstract void execute(final int p0, final int p1, final short p2, final RunningEffect p3);
    
    public abstract boolean canBeTriggeredBy(final Target p0);
    
    public abstract void pushActivationEventForTargetInTimeline(final Target p0);
    
    public float getParams(final int paramNum) {
        if (this.m_params == null || paramNum >= this.m_params.length) {
            BasicEffectArea.m_logger.error((Object)("appel d'un param\u00e8tre inexistant : " + paramNum));
            return -1.0f;
        }
        return this.m_params[paramNum];
    }
    
    public int getParamCount() {
        if (this.m_params == null) {
            return 0;
        }
        return this.m_params.length;
    }
    
    public void untriggers(final Target triggerer) {
        final List<Target> targets = this.determineUnapplicationTarget(triggerer);
        if (targets == null) {
            return;
        }
        for (final Target target : targets) {
            if (target != null && target instanceof EffectUser && ((EffectUser)target).getRunningEffectManager() != null) {
                ((EffectUser)target).getRunningEffectManager().removeLinkedToContainer(this, true);
            }
            this.onUnapplication(target);
        }
    }
    
    @Override
    public boolean isActiveProperty(final PropertyType property) {
        return false;
    }
    
    @Override
    public void addProperty(final PropertyType property) {
    }
    
    @Override
    public byte getPropertyValue(final PropertyType property) {
        return 0;
    }
    
    @Override
    public void setPropertyValue(final PropertyType property, final byte value) {
    }
    
    @Override
    public void substractProperty(final PropertyType property) {
    }
    
    @Override
    public void removeProperty(final PropertyType property) {
    }
    
    @Override
    public boolean isInPlay() {
        return !this.m_isOffPlay;
    }
    
    @Override
    public boolean isOffPlay() {
        return this.m_isOffPlay;
    }
    
    @Override
    public void onGoesOffPlay() {
    }
    
    @Override
    public boolean mustGoOutOfPlay() {
        return this.isOffPlay();
    }
    
    @Override
    public boolean isOutOfPlay() {
        return this.isOffPlay();
    }
    
    @Override
    public void onGoesOutOfPlay() {
    }
    
    @Override
    public void onBackInPlay() {
    }
    
    @Override
    public void goOutOfPlay(final EffectUser killer) {
    }
    
    public void onApplication(final Target applicant) {
        this.m_listener.onEffectAreaApplication(this, applicant);
    }
    
    public void onUnapplication(final Target unapplicant) {
        this.m_listener.onEffectAreaUnapplication(this, unapplicant);
    }
    
    public void onActivation(final Target triggerer) {
    }
    
    public void onDeactivation(final Target triggerer) {
    }
    
    public void onEffectAreaAddedToManager() {
    }
    
    public void onEffectAreaRemovedFromManager() {
    }
    
    public boolean canBlockLOS() {
        return false;
    }
    
    @Override
    public byte getHeight() {
        return 0;
    }
    
    @Override
    public byte getPhysicalRadius() {
        return 0;
    }
    
    @Override
    public boolean canChangePlayStatus() {
        return !this.m_stateChange;
    }
    
    @Override
    public void setUnderChange(final boolean bool) {
        this.m_stateChange = bool;
    }
    
    @Override
    public byte getTeamId() {
        if (this.m_owner != null) {
            return ((FightEffectUser)this.m_owner).getTeamId();
        }
        return 0;
    }
    
    @Override
    public byte getObstacleId() {
        return this.m_obstacleId;
    }
    
    @Override
    public void setObstacleId(final byte obstacleId) {
        this.m_obstacleId = obstacleId;
    }
    
    protected void setOwner(final EffectUser owner) {
        this.m_owner = owner;
    }
    
    @Override
    public boolean canBlockMovementOrSight() {
        return false;
    }
    
    @Override
    public boolean isBlockingMovement() {
        return false;
    }
    
    @Override
    public boolean isBlockingSight() {
        return false;
    }
    
    public boolean isCanBeTargeted() {
        return this.m_canBeTargeted;
    }
    
    public boolean isCanBeTargetedByAI() {
        return this.m_canBeTargetedByAI;
    }
    
    public void setCanBeTargetedByAI(final boolean canBeTargetedByAI) {
        this.m_canBeTargetedByAI = canBeTargetedByAI;
    }
    
    public void computeZone() {
        final int cellX = (this.m_owner == null) ? this.m_position.getX() : this.m_owner.getWorldCellX();
        final int cellY = (this.m_owner == null) ? this.m_position.getY() : this.m_owner.getWorldCellY();
        final short cellZ = (this.m_owner == null) ? this.m_position.getZ() : this.m_owner.getWorldCellAltitude();
        final Direction8 direction = (this.m_owner == null) ? Direction8.NORTH_EAST : this.m_owner.getDirection();
        this.m_zone = this.m_area.getCells(this.m_position.getX(), this.m_position.getY(), this.m_position.getZ(), cellX, cellY, cellZ, direction);
    }
    
    public Iterable<int[]> getZone() {
        return this.m_zone;
    }
    
    public boolean disappearWithOwner() {
        return this.m_disappearWithOwner;
    }
    
    public void setDisappearWithOwner(final boolean disappearWithOwner) {
        this.m_disappearWithOwner = disappearWithOwner;
    }
    
    public boolean shouldStopMover() {
        return true;
    }
    
    @Override
    public Point3 getPosition() {
        return this.m_position;
    }
    
    @Override
    public BasicEffectArea getEffectArea() {
        return this;
    }
    
    @Override
    public EffectUser getResistanceTarget() {
        return this;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BasicEffectArea.class);
    }
}
