package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.wakfu.common.game.characteristics.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fight.bombCombination.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractBombEffectArea extends AbstractCarriableEffectArea
{
    private static final CharacteristicType[] BOMB_CHARACTERISTICS;
    protected final FillableCharacteristicManager m_characteristics;
    private Direction8 m_onSpecialMovement;
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private RunningEffectManager m_runningEffectManager;
    protected BombPositionChangedListener m_positionChangedListener;
    private final long[] m_target;
    
    protected AbstractBombEffectArea() {
        super();
        this.m_characteristics = new FillableCharacteristicManager(AbstractBombEffectArea.BOMB_CHARACTERISTICS);
        this.m_onSpecialMovement = null;
        this.m_runningEffectManager = null;
        this.m_target = new long[1];
    }
    
    public AbstractBombEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_characteristics = new FillableCharacteristicManager(AbstractBombEffectArea.BOMB_CHARACTERISTICS);
        this.m_onSpecialMovement = null;
        this.m_runningEffectManager = null;
        this.m_target = new long[1];
    }
    
    @Override
    public void initialize() {
        this.m_characteristics.makeDefault();
        final float inc = (this.m_params.length > 1) ? (this.getLevel() * this.getParams(1)) : 0.0f;
        final int cooldown = (int)(this.getParams(0) + inc);
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.BOMB_COOLDOWN).setMax(cooldown);
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.BOMB_COOLDOWN).toMax();
        this.m_runningEffectManager = new TimedRunningEffectManager(this);
    }
    
    @Override
    public AbstractCharacteristic getDisplayedCharacteristic() {
        return this.getCharacteristic(FighterCharacteristicType.BOMB_COOLDOWN);
    }
    
    @Override
    public AbstractCharacteristic getCharacteristic(final CharacteristicType charac) {
        return this.m_characteristics.getCharacteristic(charac);
    }
    
    @Override
    public FillableCharacteristicManager getCharacteristics() {
        return this.m_characteristics;
    }
    
    @Override
    public boolean hasCharacteristic(final CharacteristicType charac) {
        return this.m_characteristics.contains(charac);
    }
    
    @Override
    public int getType() {
        return EffectAreaType.BOMB.getTypeId();
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        this.m_target[0] = PositionValue.toLong(this.m_position);
        return this.m_target;
    }
    
    @Override
    public List<Target> determineUnapplicationTarget(final Target triggerer) {
        return null;
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        if (applicant == null) {
            return true;
        }
        if (this.m_owner == applicant) {
            return true;
        }
        if (applicant instanceof AbstractBombEffectArea) {
            return ((AreaOwnerProvider)applicant).getOwner() == this.m_owner;
        }
        return applicant instanceof BasicCharacterInfo && ((Carrier)applicant).getController() == this.m_owner;
    }
    
    @Override
    public boolean canBeCarriedBy(final Carrier carrier) {
        return this.getCharacteristicValue(FighterCharacteristicType.BOMB_COOLDOWN) > 0 && super.canBeCarriedBy(carrier);
    }
    
    @Override
    public boolean isBlockingMovement() {
        return false;
    }
    
    @Override
    public boolean canBlockMovementOrSight() {
        return false;
    }
    
    @Override
    public boolean isPositionStatic() {
        return false;
    }
    
    @Override
    public int getWorldCellX() {
        final Carrier carrier = this.getCarrier();
        if (carrier != null) {
            carrier.getWorldCellX();
        }
        return super.getWorldCellX();
    }
    
    @Override
    public int getWorldCellY() {
        final Carrier carrier = this.getCarrier();
        if (carrier != null) {
            carrier.getWorldCellY();
        }
        return super.getWorldCellY();
    }
    
    @Override
    public short getWorldCellAltitude() {
        final Carrier carrier = this.getCarrier();
        if (carrier != null) {
            carrier.getWorldCellAltitude();
        }
        return super.getWorldCellAltitude();
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AbstractBombEffectArea.PARAMETER_LIST_SET;
    }
    
    @Override
    public void setSpecialMovementDirection(final Direction8 direction) {
        this.m_onSpecialMovement = direction;
    }
    
    @Override
    public Direction8 getMovementDirection() {
        if (this.m_onSpecialMovement == null) {
            return this.getDirection();
        }
        return this.m_onSpecialMovement;
    }
    
    @Override
    public RunningEffectManager getRunningEffectManager() {
        return this.m_runningEffectManager;
    }
    
    @Override
    protected byte concernedCheck() {
        return 3;
    }
    
    public void onCooldownInitialize() {
    }
    
    public void setPositionChangedListener(final BombPositionChangedListener listener) {
        this.m_positionChangedListener = listener;
    }
    
    @Override
    public void onPositionChanged() {
        super.onPositionChanged();
        if (this.m_positionChangedListener != null) {
            try {
                this.m_positionChangedListener.onBombPositionChanged(this);
            }
            catch (Exception e) {
                AbstractBombEffectArea.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
        }
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        final ByteArray ba = new ByteArray();
        ba.putInt(this.getCharacteristicMax(FighterCharacteristicType.BOMB_COOLDOWN));
        ba.putInt(this.getCharacteristicValue(FighterCharacteristicType.BOMB_COOLDOWN));
        return ba.toArray();
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
        final int max = bb.getInt();
        final int areaHp = bb.getInt();
        this.getCharacteristic(FighterCharacteristicType.BOMB_COOLDOWN).setMax(max);
        this.getCharacteristic(FighterCharacteristicType.BOMB_COOLDOWN).set(areaHp);
    }
    
    static {
        BOMB_CHARACTERISTICS = new CharacteristicType[] { FighterCharacteristicType.BOMB_COOLDOWN, FighterCharacteristicType.DMG_IN_PERCENT, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.MELEE_DMG, FighterCharacteristicType.RANGED_DMG, FighterCharacteristicType.SINGLE_TARGET_DMG, FighterCharacteristicType.AOE_DMG, FighterCharacteristicType.FINAL_DMG_IN_PERCENT, FighterCharacteristicType.CRITICAL_BONUS };
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Bombe roublard", new Parameter[] { new Parameter("BOMB_COOLDOWN") }), new EffectAreaParameterList("Bombe roublard avec increment des pdv par level", new Parameter[] { new Parameter("BOMB_COOLDOWN"), new Parameter("incr par level") }) });
    }
}
