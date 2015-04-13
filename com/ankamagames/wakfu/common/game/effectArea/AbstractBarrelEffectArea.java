package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.wakfu.common.game.characteristics.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractBarrelEffectArea extends AbstractCarriableEffectArea
{
    private static final CharacteristicType[] CHARACTERISTICS;
    protected final FillableCharacteristicManager m_characteristics;
    private Direction8 m_onSpecialMovement;
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private RunningEffectManager m_runningEffectManager;
    private final long[] m_target;
    
    public static FighterCharacteristicType getHpCharac() {
        if (SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.PANDA_NEW_BARREL)) {
            return FighterCharacteristicType.HP;
        }
        return FighterCharacteristicType.AREA_HP;
    }
    
    protected AbstractBarrelEffectArea() {
        super();
        this.m_characteristics = new FillableCharacteristicManager(AbstractBarrelEffectArea.CHARACTERISTICS);
        this.m_onSpecialMovement = null;
        this.m_runningEffectManager = null;
        this.m_target = new long[1];
    }
    
    public AbstractBarrelEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_characteristics = new FillableCharacteristicManager(AbstractBarrelEffectArea.CHARACTERISTICS);
        this.m_onSpecialMovement = null;
        this.m_runningEffectManager = null;
        this.m_target = new long[1];
    }
    
    @Override
    public void initialize() {
        this.m_characteristics.makeDefault();
        final int hpmax = this.getMaxHp();
        this.m_characteristics.getCharacteristic(getHpCharac()).setMax(hpmax);
        this.m_characteristics.getCharacteristic(getHpCharac()).toMax();
        this.m_runningEffectManager = new TimedRunningEffectManager(this);
    }
    
    private int getMaxHp() {
        final float hp = this.getParams(0);
        final short level = this.getLevel();
        final float inc = level * this.getParams(1);
        final float ownerPercent = this.getParams(2) + this.getParams(3) * level;
        final float baseHp = hp + inc;
        final float ownerBonus = ownerPercent * ((this.m_owner != null) ? (this.m_owner.getCharacteristic(getHpCharac()).max() / 100) : 0);
        return (int)(baseHp + ownerBonus);
    }
    
    @Override
    public AbstractCharacteristic getDisplayedCharacteristic() {
        return this.getCharacteristic(getHpCharac());
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
        return EffectAreaType.BARREL.getTypeId();
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
        return false;
    }
    
    @Override
    public boolean isBlockingMovement() {
        return true;
    }
    
    @Override
    public boolean canBlockMovementOrSight() {
        return true;
    }
    
    @Override
    public boolean isBlockingSight() {
        return true;
    }
    
    @Override
    public boolean mustGoOffPlay() {
        return super.mustGoOffPlay() || this.m_characteristics.getCharacteristicValue(getHpCharac()) <= 0;
    }
    
    @Override
    public boolean isPositionStatic() {
        return false;
    }
    
    @Override
    public int getWorldCellX() {
        if (this.getCarrier() != null) {
            this.getCarrier().getWorldCellX();
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
        return AbstractBarrelEffectArea.PARAMETER_LIST_SET;
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
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        final ByteArray ba = new ByteArray();
        ba.putInt(this.getCharacteristicMax(getHpCharac()));
        ba.putInt(this.getCharacteristicValue(getHpCharac()));
        return ba.toArray();
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
        final int max = bb.getInt();
        final int areaHp = bb.getInt();
        this.getCharacteristic(getHpCharac()).setMax(max);
        this.getCharacteristic(getHpCharac()).set(areaHp);
    }
    
    static {
        CHARACTERISTICS = new CharacteristicType[] { getHpCharac(), FighterCharacteristicType.DMG_IN_PERCENT, FighterCharacteristicType.DMG_REBOUND, FighterCharacteristicType.RES_AIR_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT, FighterCharacteristicType.RES_FIRE_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT, FighterCharacteristicType.RES_IN_PERCENT };
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Tonneau Panda avec increment des pdv par level", new Parameter[] { new Parameter("PdV"), new Parameter("incr par level"), new Parameter("Bonus de PdV en % de PdV du caster"), new Parameter("incr du Bonus de PdV en % de PdV du caster") }) });
    }
}
