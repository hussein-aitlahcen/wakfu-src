package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.wakfu.common.game.characteristics.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractFecaGlyphEffectArea extends AbstractEffectArea
{
    private static final CharacteristicType[] CHARACTERISTICS;
    protected final FillableCharacteristicManager m_characteristics;
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    private RunningEffectManager m_runningEffectManager;
    private final long[] m_target;
    
    protected AbstractFecaGlyphEffectArea() {
        super();
        this.m_characteristics = new FillableCharacteristicManager(AbstractFecaGlyphEffectArea.CHARACTERISTICS);
        this.m_runningEffectManager = null;
        this.m_target = new long[1];
    }
    
    public AbstractFecaGlyphEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBeTargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBeTargetted, canBeDestroyed, maxLevel);
        this.m_characteristics = new FillableCharacteristicManager(AbstractFecaGlyphEffectArea.CHARACTERISTICS);
        this.m_runningEffectManager = null;
        this.m_target = new long[1];
    }
    
    @Override
    public void initialize() {
        this.m_characteristics.makeDefault();
        this.initializeHp();
        this.m_runningEffectManager = new TimedRunningEffectManager(this);
        if (this.m_owner == null) {
            return;
        }
        this.initializeDmgCharac();
    }
    
    private void initializeHp() {
        final int hpmax = this.getMaxHp();
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.AREA_HP).setMax(hpmax);
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.AREA_HP).toMax();
    }
    
    private int getMaxHp() {
        final float hp = this.getParams(0);
        final short level = this.getLevel();
        final float inc = level * this.getParams(1);
        final float baseHp = hp + inc;
        float ownerBonus;
        if (this.m_owner.hasCharacteristic(FighterCharacteristicType.FECA_GLYPH_CHARGE_BONUS)) {
            ownerBonus = this.m_owner.getCharacteristicValue(FighterCharacteristicType.FECA_GLYPH_CHARGE_BONUS);
        }
        else {
            ownerBonus = 0.0f;
        }
        return (int)(baseHp + ownerBonus);
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
        return this.m_characteristics.contains(charac);
    }
    
    @Override
    public int getType() {
        return EffectAreaType.FECA_GLYPH.getTypeId();
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
        return Collections.singletonList(triggerer);
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return !(applicant instanceof CarryTarget) || !((CarryTarget)applicant).isCarried();
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
    public boolean isBlockingSight() {
        return false;
    }
    
    @Override
    public boolean mustGoOffPlay() {
        return super.mustGoOffPlay() || this.m_characteristics.getCharacteristicValue(FighterCharacteristicType.AREA_HP) <= 0;
    }
    
    @Override
    public boolean isPositionStatic() {
        return true;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return AbstractFecaGlyphEffectArea.PARAMETER_LIST_SET;
    }
    
    @Override
    public RunningEffectManager getRunningEffectManager() {
        return this.m_runningEffectManager;
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
    
    static {
        CHARACTERISTICS = new CharacteristicType[] { FighterCharacteristicType.AREA_HP, FighterCharacteristicType.DMG_IN_PERCENT, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.MELEE_DMG, FighterCharacteristicType.RANGED_DMG, FighterCharacteristicType.SINGLE_TARGET_DMG, FighterCharacteristicType.AOE_DMG };
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Tonneau Panda avec increment des pdv par level", new Parameter[] { new Parameter("PdV"), new Parameter("incr par level"), new Parameter("DEPRECATED : Bonus d\u00e9gats owner recopi\u00e9s (-1:aucun 0:phys 1:Feu 2:Eau 3:Terre 4:Air") }) });
    }
}
