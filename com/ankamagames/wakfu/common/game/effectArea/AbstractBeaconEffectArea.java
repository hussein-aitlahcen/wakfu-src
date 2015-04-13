package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.wakfu.common.game.part.*;
import com.ankamagames.wakfu.common.game.characteristics.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractBeaconEffectArea extends AbstractEffectArea
{
    private final BeaconPartLocalisator m_localisator;
    private Direction8 m_direction;
    private static final CharacteristicType[] BEACON_CHARACTERISTICS;
    protected final FillableCharacteristicManager m_characteristics;
    private int m_linkedSpellId;
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private RunningEffectManager m_runningEffectManager;
    
    @Override
    public EffectAreaParameterListSet getParametersListSet() {
        return AbstractBeaconEffectArea.PARAMETER_LIST_SET;
    }
    
    public AbstractBeaconEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBetargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBetargetted, canBeDestroyed, maxLevel);
        this.m_localisator = new BeaconPartLocalisator();
        this.m_characteristics = new FillableCharacteristicManager(AbstractBeaconEffectArea.BEACON_CHARACTERISTICS);
        this.m_runningEffectManager = null;
    }
    
    @Override
    public AbstractEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final AbstractEffectArea area = super.instanceAnother(parameters);
        if (parameters != null && parameters.getDirection() != null) {
            area.setDirection(parameters.getDirection());
        }
        else {
            area.setDirection(Direction8.SOUTH_WEST);
        }
        return area;
    }
    
    protected AbstractBeaconEffectArea() {
        super();
        this.m_localisator = new BeaconPartLocalisator();
        this.m_characteristics = new FillableCharacteristicManager(AbstractBeaconEffectArea.BEACON_CHARACTERISTICS);
        this.m_runningEffectManager = null;
    }
    
    @Override
    public int getType() {
        return EffectAreaType.BEACON.getTypeId();
    }
    
    @Override
    public void initialize() {
        this.m_characteristics.makeDefault();
        final int hpmax = (int)(this.getParams(0) + this.getLevel() * this.getParams(1));
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.AREA_HP).setMax(hpmax);
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.AREA_HP).toMax();
        this.m_runningEffectManager = new TimedRunningEffectManager(this);
        this.addProperty(FightPropertyType.CANT_BE_STATE_TARGET);
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return applicant == this.m_owner;
    }
    
    @Override
    public AbstractCharacteristic getDisplayedCharacteristic() {
        return this.getCharacteristic(FighterCharacteristicType.AREA_HP);
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
        return this.m_characteristics != null && this.m_characteristics.contains(charac);
    }
    
    @Override
    public boolean mustGoOffPlay() {
        return super.mustGoOffPlay() || this.m_characteristics.getCharacteristicValue(FighterCharacteristicType.AREA_HP) <= 0;
    }
    
    @Override
    public Direction8 getDirection() {
        return this.m_direction;
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        this.m_direction = direction;
    }
    
    @Override
    public void setSpecialMovementDirection(final Direction8 direction) {
    }
    
    @Override
    public Direction8 getMovementDirection() {
        return this.getDirection();
    }
    
    @Override
    public PartLocalisator getPartLocalisator() {
        this.m_localisator.update(this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude(), this.getDirection());
        return this.m_localisator;
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        return this.determineTargetCells(triggerer).getSecond();
    }
    
    public ObjectPair<ArrayList<Point3>, long[]> determineTargetCells(final Target triggerer) {
        final long[] targetCells = { PositionValue.toLong(this.getWorldCellX(), this.getWorldCellY(), this.getWorldCellAltitude()) };
        final ArrayList<Point3> pathCells = new ArrayList<Point3>();
        return new ObjectPair<ArrayList<Point3>, long[]>(pathCells, targetCells);
    }
    
    @Override
    public ArrayList<Target> determineUnapplicationTarget(final Target triggerer) {
        return new ArrayList<Target>();
    }
    
    @Override
    public void onGoesOffPlay() {
        super.onGoesOffPlay();
        final Collection<BasicEffectArea> areasToRemove = new ArrayList<BasicEffectArea>();
        if (this.m_context != null) {
            for (final BasicEffectArea area : this.m_context.getEffectAreaManager().getActiveEffectAreas()) {
                if (area != this && area.getType() == EffectAreaType.BEACON.getTypeId() && area.getWorldCellX() == this.getWorldCellX() && area.getWorldCellY() == this.getWorldCellY() && area.getWorldCellAltitude() == this.getWorldCellAltitude() && area.getOwner() == this.getOwner()) {
                    areasToRemove.add(area);
                }
            }
            for (final BasicEffectArea area : areasToRemove) {
                this.m_context.getEffectAreaManager().removeEffectArea(area);
            }
        }
    }
    
    @Override
    public RunningEffectManager getRunningEffectManager() {
        return this.m_runningEffectManager;
    }
    
    @Override
    public byte getHeight() {
        return 6;
    }
    
    @Override
    public boolean canBlockMovementOrSight() {
        return true;
    }
    
    @Override
    public boolean isBlockingMovement() {
        return true;
    }
    
    @Override
    public boolean isBlockingSight() {
        return true;
    }
    
    public void setLinkedSpellId(final int linkedSpellId) {
        this.m_linkedSpellId = linkedSpellId;
    }
    
    public int getLinkedSpellId() {
        return this.m_linkedSpellId;
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        final ByteArray ba = new ByteArray();
        ba.putInt(this.getCharacteristicMax(FighterCharacteristicType.AREA_HP));
        ba.putInt(this.getCharacteristicValue(FighterCharacteristicType.AREA_HP));
        return ba.toArray();
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
        final int max = bb.getInt();
        final int areaHp = bb.getInt();
        this.getCharacteristic(FighterCharacteristicType.AREA_HP).setMax(max);
        this.getCharacteristic(FighterCharacteristicType.AREA_HP).set(areaHp);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_linkedSpellId = 0;
    }
    
    static {
        BEACON_CHARACTERISTICS = new CharacteristicType[] { FighterCharacteristicType.AREA_HP, FighterCharacteristicType.DMG_IN_PERCENT, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.MELEE_DMG, FighterCharacteristicType.RANGED_DMG, FighterCharacteristicType.SINGLE_TARGET_DMG, FighterCharacteristicType.AOE_DMG };
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Balise destructible", new Parameter[] { new Parameter("HP"), new Parameter("incr par level") }) });
    }
}
