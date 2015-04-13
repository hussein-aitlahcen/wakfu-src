package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.wakfu.common.game.characteristics.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fight.*;
import org.apache.commons.lang3.*;
import java.nio.*;
import java.util.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public abstract class AbstractTrapEffectArea extends AbstractEffectArea
{
    private static final BitSet CASTER_IMMUNIZED_TRIGGERS;
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private static final CharacteristicType[] TRAP_CHARACTERISTICS;
    protected final FillableCharacteristicManager m_characteristics;
    private BitSet m_triggersChecked;
    private boolean m_canBeTriggerByOwner;
    private boolean m_isInvisible;
    private final long[] m_target;
    
    @Override
    public EffectAreaParameterListSet getParametersListSet() {
        return AbstractTrapEffectArea.PARAMETER_LIST_SET;
    }
    
    protected AbstractTrapEffectArea() {
        super();
        this.m_characteristics = new FillableCharacteristicManager(AbstractTrapEffectArea.TRAP_CHARACTERISTICS);
        this.m_canBeTriggerByOwner = false;
        this.m_target = new long[1];
    }
    
    @Override
    public void initialize() {
        this.m_characteristics.makeDefault();
        this.m_canBeTriggerByOwner = false;
        this.m_isInvisible = true;
        if (this.m_params == null) {
            return;
        }
        if (this.m_params.length > 0) {
            this.m_canBeTriggerByOwner = (this.getParams(0) == 1.0f);
        }
        if (this.m_params.length > 1) {
            this.m_isInvisible = (this.getParams(1) == 0.0f);
        }
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
    
    public AbstractTrapEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_characteristics = new FillableCharacteristicManager(AbstractTrapEffectArea.TRAP_CHARACTERISTICS);
        this.m_canBeTriggerByOwner = false;
        this.m_target = new long[1];
    }
    
    @Override
    public int getType() {
        return EffectAreaType.TRAP.getTypeId();
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        this.m_target[0] = PositionValue.toLong(this.m_position);
        return this.m_target;
    }
    
    @Override
    public ArrayList<Target> determineUnapplicationTarget(final Target triggerer) {
        return null;
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        if (this.m_canBeTriggerByOwner) {
            return true;
        }
        if (applicant != this.m_owner) {
            return true;
        }
        if (!(this.m_owner instanceof BasicCharacterInfo)) {
            return true;
        }
        if (this.m_triggersChecked != null && !this.m_triggersChecked.intersects(AbstractTrapEffectArea.CASTER_IMMUNIZED_TRIGGERS)) {
            return true;
        }
        final BasicFighter ownerCharacterInfo = (BasicFighter)this.m_owner;
        final BasicFight<?> currentFight = (BasicFight<?>)ownerCharacterInfo.getCurrentFight();
        return currentFight == null || !currentFight.getTimeline().isCurrentFighter(ownerCharacterInfo.getId());
    }
    
    @Override
    public boolean checkTriggers(final BitSet triggers, final Target applicant) {
        this.m_triggersChecked = triggers;
        final boolean res = this.canBeTriggeredBy(applicant) && super.checkTriggers(triggers, applicant);
        this.m_triggersChecked = null;
        return res;
    }
    
    public boolean isInvisible() {
        return this.m_isInvisible;
    }
    
    @Override
    public byte[] serializeSpecificInfoForReconnection() {
        return ArrayUtils.EMPTY_BYTE_ARRAY;
    }
    
    @Override
    public void unserializeSpecificInfoForReconnection(final ByteBuffer bb) {
    }
    
    static {
        (CASTER_IMMUNIZED_TRIGGERS = new BitSet()).set(10001);
        AbstractTrapEffectArea.CASTER_IMMUNIZED_TRIGGERS.set(10008);
        AbstractTrapEffectArea.CASTER_IMMUNIZED_TRIGGERS.set(10002);
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Pi\u00e8ge standard", new Parameter[0]), new EffectAreaParameterList("Pi\u00e8ge pouvant \u00eatre d\u00e9clench\u00e9 par son proprio", new Parameter[] { new Parameter("peut \u00eatre d\u00e9clench\u00e9 par le proprio (1=oui, 0= non (defaut))") }), new EffectAreaParameterList("Piege visible pour tout le monde", new Parameter[] { new Parameter("peut \u00eatre d\u00e9clench\u00e9 par le proprio (1=oui, 0= non (defaut))"), new Parameter("visible pour tout le monde (1=oui, 0= non (defaut))") }) });
        TRAP_CHARACTERISTICS = new CharacteristicType[] { FighterCharacteristicType.DMG_IN_PERCENT, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.MELEE_DMG, FighterCharacteristicType.RANGED_DMG, FighterCharacteristicType.SINGLE_TARGET_DMG, FighterCharacteristicType.AOE_DMG, FighterCharacteristicType.HEAL_IN_PERCENT };
    }
}
