package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.characteristics.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.external.*;

public abstract class AbstractFakeFighterEffectArea extends AbstractEffectArea
{
    private static final int PERCENT_DAMAGE = 1;
    private static final int FIRE_DAMAGE = 2;
    private static final int WATER_DAMAGE = 4;
    private static final int EARTH_DAMGE = 8;
    private static final int AIR_DAMAGE = 16;
    private static final int STASIS_DAMAGE = 32;
    private static final int SUMMONING_DAMAGE = 64;
    public static final int USER_DEFINED_XELORS_DIAL_CENTER = 1;
    public static final int USER_DEFINED_HYDRAND = 2;
    public static final int USER_DEFINED_SADIDA_TOTEM = 3;
    public static final int USER_DEFINED_STEAMER_MICROBOT = 4;
    public static final int USER_DEFINED_STEAMER_FURNACE = 5;
    public static final int USER_DEFINED_STEAMER_RAIL = 6;
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    protected Direction8 m_direction;
    protected boolean m_isBlockingMovement;
    protected boolean m_isBlockingLOS;
    protected boolean m_triggeredByOwner;
    protected boolean m_triggeredByAnyoneButOwner;
    protected byte m_height;
    protected int m_userDefinedId;
    protected FillableCharacteristicManager m_characteristics;
    protected RunningEffectManager m_runningEffectManager;
    private final long[] m_target;
    
    @Override
    public EffectAreaParameterListSet getParametersListSet() {
        return AbstractFakeFighterEffectArea.PARAMETER_LIST_SET;
    }
    
    public AbstractFakeFighterEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBetargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBetargetted, canBeDestroyed, maxLevel);
        this.m_target = new long[1];
    }
    
    @Override
    public AbstractFakeFighterEffectArea instanceAnother(final EffectAreaParameters parameters) {
        final AbstractFakeFighterEffectArea area = (AbstractFakeFighterEffectArea)super.instanceAnother(parameters);
        if (parameters != null && parameters.getDirection() != null) {
            area.setDirection(parameters.getDirection());
        }
        return area;
    }
    
    protected AbstractFakeFighterEffectArea() {
        super();
        this.m_target = new long[1];
    }
    
    @Override
    public int getType() {
        return EffectAreaType.FAKE_FIGHTER.getTypeId();
    }
    
    @Override
    public void initialize() {
        this.m_userDefinedId = Math.round(this.getParams(0));
        final boolean hasLifepoints = this.getParams(1) != -1.0f;
        final boolean hasDamagesBonuses = this.getParams(9) >= 0.0f && this.getParams(9) <= 127.0f;
        final boolean hasDynamicCharacs = this.m_params.length >= 14;
        if (hasLifepoints || hasDamagesBonuses || hasDynamicCharacs) {
            final ArrayList<CharacteristicType> characteristicTypes = new ArrayList<CharacteristicType>();
            if (hasLifepoints) {
                characteristicTypes.add(FighterCharacteristicType.HP);
            }
            if (hasDamagesBonuses) {
                characteristicTypes.add(FighterCharacteristicType.DMG_IN_PERCENT);
            }
            for (int dynamicCharacIdx = 10; this.m_params.length >= dynamicCharacIdx + 4; dynamicCharacIdx += 4) {
                final byte characId = (byte)this.getParams(dynamicCharacIdx);
                final FighterCharacteristicType type = FighterCharacteristicType.getCharacteristicTypeFromId(characId);
                if (type == null) {
                    AbstractFakeFighterEffectArea.m_logger.error((Object)("Unable to add dynamic charac " + this.getParams(dynamicCharacIdx) + " to effectarea : unknown charac"));
                }
                else {
                    characteristicTypes.add(type);
                }
            }
            if (characteristicTypes.size() > 0) {
                (this.m_characteristics = new FillableCharacteristicManager(characteristicTypes)).makeDefault();
            }
            for (int dynamicCharacIdx = 10; this.m_params.length >= dynamicCharacIdx + 4; dynamicCharacIdx += 4) {
                final byte characId = (byte)this.getParams(dynamicCharacIdx);
                final FighterCharacteristicType type = FighterCharacteristicType.getCharacteristicTypeFromId(characId);
                if (type != null) {
                    final FighterCharacteristic characteristic = (FighterCharacteristic)this.getCharacteristic(type);
                    if (characteristic == null) {
                        AbstractFakeFighterEffectArea.m_logger.error((Object)"UNable to find a newly created dynamic charac for this effect area");
                    }
                    else {
                        final int initialValue = (int)this.getParams(dynamicCharacIdx + 1);
                        final int minValue = (int)this.getParams(dynamicCharacIdx + 2);
                        final int maxValue = (int)this.getParams(dynamicCharacIdx + 3);
                        if (minValue == -2) {
                            characteristic.setMin(characteristic.getLowerBound());
                        }
                        else if (minValue != -1) {
                            characteristic.setMin(minValue);
                        }
                        if (maxValue == -2) {
                            characteristic.setMax(characteristic.getUpperBound());
                        }
                        else if (maxValue != -1) {
                            characteristic.setMax(maxValue);
                        }
                        characteristic.set(initialValue);
                    }
                }
            }
        }
        final EffectUser owner = this.getOwner();
        if (hasLifepoints) {
            final float lifePointsBase = this.getParams(1);
            final float lifePointsIncrement = this.getParams(2);
            final int hpmax = (int)(lifePointsBase + this.getLevel() * lifePointsIncrement);
            this.m_characteristics.getCharacteristic(FighterCharacteristicType.HP).setMax(hpmax);
            this.m_characteristics.getCharacteristic(FighterCharacteristicType.HP).toMax();
        }
        if (hasDamagesBonuses && owner != null) {
            final AbstractCharacteristic characteristic2 = this.getCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT);
            final byte dmgToUse = (byte)this.getParams(9);
            if (owner.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT) && (dmgToUse & 0x1) != 0x0) {
                characteristic2.add(owner.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT));
            }
            if (owner.hasCharacteristic(FighterCharacteristicType.DMG_FIRE_PERCENT) && (dmgToUse & 0x2) != 0x0) {
                characteristic2.add(owner.getCharacteristicValue(FighterCharacteristicType.DMG_FIRE_PERCENT));
            }
            if (owner.hasCharacteristic(FighterCharacteristicType.DMG_WATER_PERCENT) && (dmgToUse & 0x4) != 0x0) {
                characteristic2.add(owner.getCharacteristicValue(FighterCharacteristicType.DMG_WATER_PERCENT));
            }
            if (owner.hasCharacteristic(FighterCharacteristicType.DMG_EARTH_PERCENT) && (dmgToUse & 0x8) != 0x0) {
                characteristic2.add(owner.getCharacteristicValue(FighterCharacteristicType.DMG_EARTH_PERCENT));
            }
            if (owner.hasCharacteristic(FighterCharacteristicType.DMG_AIR_PERCENT) && (dmgToUse & 0x10) != 0x0) {
                characteristic2.add(owner.getCharacteristicValue(FighterCharacteristicType.DMG_AIR_PERCENT));
            }
            if (owner.hasCharacteristic(FighterCharacteristicType.DMG_STASIS_PERCENT) && (dmgToUse & 0x20) != 0x0) {
                characteristic2.add(owner.getCharacteristicValue(FighterCharacteristicType.DMG_STASIS_PERCENT));
            }
            if (owner.hasCharacteristic(FighterCharacteristicType.SUMMONING_MASTERY) && (dmgToUse & 0x40) != 0x0) {
                characteristic2.add(owner.getCharacteristicValue(FighterCharacteristicType.SUMMONING_MASTERY));
            }
        }
        this.m_isBlockingMovement = (this.getParams(3) == 1.0f);
        this.m_isBlockingLOS = (this.getParams(4) == 1.0f);
        if (this.getParams(5) == 1.0f) {
            this.m_runningEffectManager = new TimedRunningEffectManager(this);
        }
        else {
            this.m_runningEffectManager = null;
        }
        this.m_triggeredByOwner = (this.getParams(6) == 1.0f);
        this.m_triggeredByAnyoneButOwner = (this.getParams(7) == 1.0f);
        final int height = (int)this.getParams(8);
        if (height <= 0) {
            this.m_height = 6;
        }
        else {
            this.m_height = (byte)height;
        }
    }
    
    @Override
    public boolean canBlockMovementOrSight() {
        return this.m_isBlockingMovement || this.m_isBlockingLOS;
    }
    
    @Override
    public boolean isBlockingMovement() {
        return this.m_isBlockingMovement;
    }
    
    @Override
    public boolean isBlockingSight() {
        return this.m_isBlockingLOS;
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        if (applicant == this.m_owner) {
            return this.m_triggeredByOwner;
        }
        return this.m_triggeredByAnyoneButOwner;
    }
    
    @Override
    public RunningEffectManager getRunningEffectManager() {
        return this.m_runningEffectManager;
    }
    
    @Override
    public AbstractCharacteristic getDisplayedCharacteristic() {
        switch (this.m_userDefinedId) {
            case 1: {
                return this.getOwner().getCharacteristic(FighterCharacteristicType.XELORS_DIAL_CHARGE);
            }
            default: {
                if (this.hasCharacteristic(FighterCharacteristicType.AREA_HP)) {
                    return this.getCharacteristic(FighterCharacteristicType.AREA_HP);
                }
                return this.getCharacteristic(FighterCharacteristicType.HP);
            }
        }
    }
    
    @Override
    public AbstractCharacteristic getCharacteristic(final CharacteristicType charac) {
        if (this.m_characteristics == null) {
            return null;
        }
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
    public long[] determineApplicationTargetCells(final Target triggerer) {
        if (triggerer != null) {
            this.m_target[0] = PositionValue.toLong(triggerer.getWorldCellX(), triggerer.getWorldCellY(), triggerer.getWorldCellAltitude());
            return this.m_target;
        }
        return null;
    }
    
    @Override
    public List<Target> determineUnapplicationTarget(final Target triggerer) {
        return (List<Target>)Collections.singletonList(this.m_owner);
    }
    
    @Override
    public byte getHeight() {
        return this.m_height;
    }
    
    public int getUserDefinedId() {
        return this.m_userDefinedId;
    }
    
    @Override
    public boolean mustGoOffPlay() {
        return super.mustGoOffPlay() || (this.m_characteristics != null && this.hasCharacteristic(FighterCharacteristicType.HP) && this.m_characteristics.getCharacteristicValue(FighterCharacteristicType.HP) <= 0) || (this.m_characteristics != null && this.hasCharacteristic(FighterCharacteristicType.AREA_HP) && this.m_characteristics.getCharacteristicValue(FighterCharacteristicType.AREA_HP) <= 0);
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        this.m_direction = direction;
    }
    
    @Override
    public Direction8 getDirection() {
        if (this.m_direction == null) {
            return super.getDirection();
        }
        return this.m_direction;
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        final ByteArray ba = new ByteArray();
        if (this.hasCharacteristic(FighterCharacteristicType.AREA_HP)) {
            ba.putInt(this.getCharacteristicMax(FighterCharacteristicType.AREA_HP));
            ba.putInt(this.getCharacteristicValue(FighterCharacteristicType.AREA_HP));
        }
        if (this.hasCharacteristic(FighterCharacteristicType.HP)) {
            ba.putInt(this.getCharacteristicMax(FighterCharacteristicType.HP));
            ba.putInt(this.getCharacteristicValue(FighterCharacteristicType.HP));
        }
        return ba.toArray();
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
        if (this.hasCharacteristic(FighterCharacteristicType.AREA_HP)) {
            final int max = bb.getInt();
            final int areaHp = bb.getInt();
            this.getCharacteristic(FighterCharacteristicType.AREA_HP).setMax(max);
            this.getCharacteristic(FighterCharacteristicType.AREA_HP).set(areaHp);
        }
        if (this.hasCharacteristic(FighterCharacteristicType.HP)) {
            final int max = bb.getInt();
            final int areaHp = bb.getInt();
            this.getCharacteristic(FighterCharacteristicType.HP).setMax(max);
            this.getCharacteristic(FighterCharacteristicType.HP).set(areaHp);
        }
    }
    
    static {
        final ArrayList<EffectAreaParameterList> parametersLists = new ArrayList<EffectAreaParameterList>();
        final ArrayList<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter("UserDefined ID"));
        parameters.add(new Parameter("HP (-1 = pas de pdv)"));
        parameters.add(new Parameter("HP inc par level (useless si HP == -1)"));
        parameters.add(new Parameter("Bloque mouvement (oui si 1, non sinon)"));
        parameters.add(new Parameter("Bloque ligne de vue (oui si 1, non sinon)"));
        parameters.add(new Parameter("Avec RunningEffectManager (oui si 1, non sinon)"));
        parameters.add(new Parameter("P-e triggered par owner (oui si 1, non sinon)"));
        parameters.add(new Parameter("P-e triggered par qqun d'autre (oui si 1, non sinon)"));
        parameters.add(new Parameter("Hauteur (NORMAL_HEIGHT si <= 0)"));
        parameters.add(new Parameter("Bonus d\u00e9gats owner recopi\u00e9s (-1:aucun 1:phys 2:Feu 4:Eau 8:Terre 16:Air 32:Stasis 64:Meca"));
        parametersLists.add(new EffectAreaParameterList("Fake fighter sous forme d'effect area", (Parameter[])parameters.toArray(new Parameter[parameters.size()])));
        for (int dynamicCharacIdx = 1; dynamicCharacIdx < 6; ++dynamicCharacIdx) {
            parameters.add(new Parameter("Charac#" + dynamicCharacIdx + " : id"));
            parameters.add(new Parameter("Charac#" + dynamicCharacIdx + " : valeur initiale"));
            parameters.add(new Parameter("Charac#" + dynamicCharacIdx + " : valeur min (-1 = default min, -2 = lower bound)"));
            parameters.add(new Parameter("Charac#" + dynamicCharacIdx + " : valeur max (-1 = default max, -2 = upper bound)"));
            parametersLists.add(new EffectAreaParameterList("Fake fighter avec " + dynamicCharacIdx + " charac dynamique", (Parameter[])parameters.toArray(new Parameter[parameters.size()])));
        }
        PARAMETER_LIST_SET = new EffectAreaParameterListSet((ParameterList[])parametersLists.toArray(new EffectAreaParameterList[parametersLists.size()]));
    }
}
