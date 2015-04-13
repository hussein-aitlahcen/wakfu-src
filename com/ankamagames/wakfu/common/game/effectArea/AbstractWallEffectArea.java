package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.common.game.characteristics.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;

public abstract class AbstractWallEffectArea extends AbstractEffectArea
{
    private static final ArrayList<Target> EMPTYTARGET;
    private boolean m_blockMovement;
    private boolean m_blockLOS;
    private static final CharacteristicType[] DEFAULT_CHARACTERISTICS;
    protected FillableCharacteristicManager m_characteristics;
    protected FighterCharacteristicType m_hpCharacteristic;
    private RunningEffectManager m_wallRunningEffectManager;
    public static final EffectAreaParameterListSet PARAMETER_LIST_SET;
    
    @Override
    public EffectAreaParameterListSet getParametersListSet() {
        return AbstractWallEffectArea.PARAMETER_LIST_SET;
    }
    
    public AbstractWallEffectArea(final int baseId, final AreaOfEffect area, final BitSet applicationtriggers, final BitSet unapplicationtriggers, final int maxExecutionCount, final int targetsToShow, final float[] deactivationDelay, final float[] params, final boolean canBetargetted, final boolean canBeDestroyed, final int maxLevel) {
        super(baseId, area, applicationtriggers, unapplicationtriggers, maxExecutionCount, targetsToShow, deactivationDelay, params, canBetargetted, canBeDestroyed, maxLevel);
        this.m_wallRunningEffectManager = null;
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
    
    protected AbstractWallEffectArea() {
        super();
        this.m_wallRunningEffectManager = null;
    }
    
    @Override
    public int getType() {
        return EffectAreaType.WALL.getTypeId();
    }
    
    @Override
    public void initialize() {
        final List<CharacteristicType> characs = new ArrayList<CharacteristicType>(Arrays.asList(AbstractWallEffectArea.DEFAULT_CHARACTERISTICS));
        final boolean useAreaHp = this.m_params != null && this.m_params.length >= 5 && this.getParams(4) == 1.0f;
        if (useAreaHp) {
            this.m_hpCharacteristic = FighterCharacteristicType.AREA_HP;
        }
        else {
            this.m_hpCharacteristic = FighterCharacteristicType.HP;
        }
        characs.add(this.m_hpCharacteristic);
        (this.m_characteristics = new FillableCharacteristicManager(characs)).makeDefault();
        if (this.m_params != null && this.m_params.length >= 4) {
            final int hpmax = (int)(this.getParams(0) + this.getLevel() * this.getParams(1));
            this.m_characteristics.getCharacteristic(this.m_hpCharacteristic).setMax(hpmax);
            this.m_characteristics.getCharacteristic(this.m_hpCharacteristic).toMax();
            this.m_blockMovement = (this.getParams(2) == 1.0f);
            this.m_blockLOS = (this.getParams(3) == 1.0f);
            this.m_wallRunningEffectManager = new TimedRunningEffectManager(this);
        }
        else {
            AbstractWallEffectArea.m_logger.error((Object)"nombre de param\u00e8tre incorrect pour la cr\u00e9ation d'un mur");
        }
    }
    
    @Override
    public boolean canBeTriggeredBy(final Target applicant) {
        return false;
    }
    
    @Override
    public AbstractCharacteristic getDisplayedCharacteristic() {
        return this.getCharacteristic(this.m_hpCharacteristic);
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
        return super.mustGoOffPlay() || this.m_characteristics.getCharacteristicValue(this.m_hpCharacteristic) <= 0;
    }
    
    @Override
    public long[] determineApplicationTargetCells(final Target triggerer) {
        return null;
    }
    
    @Override
    public ArrayList<Target> determineUnapplicationTarget(final Target triggerer) {
        return AbstractWallEffectArea.EMPTYTARGET;
    }
    
    @Override
    public byte getHeight() {
        return 6;
    }
    
    @Override
    public boolean canBlockMovementOrSight() {
        return this.m_blockMovement || this.m_blockLOS;
    }
    
    @Override
    public boolean isBlockingMovement() {
        return this.m_blockMovement;
    }
    
    @Override
    public boolean isBlockingSight() {
        return this.m_blockLOS;
    }
    
    @Override
    public boolean canBlockLOS() {
        return this.m_blockLOS;
    }
    
    @Override
    public RunningEffectManager getRunningEffectManager() {
        return this.m_wallRunningEffectManager;
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
        EMPTYTARGET = new ArrayList<Target>();
        DEFAULT_CHARACTERISTICS = new CharacteristicType[] { FighterCharacteristicType.RES_IN_PERCENT, FighterCharacteristicType.RES_AIR_PERCENT, FighterCharacteristicType.RES_FIRE_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT };
        PARAMETER_LIST_SET = new EffectAreaParameterListSet(new ParameterList[] { new EffectAreaParameterList("Mur destructible, avec HP", new Parameter[] { new Parameter("HP"), new Parameter("incr par level"), new Parameter("bloque le mouvement (l:oui, 0:non)"), new Parameter("bloque la ligne de vue (l:oui, 0:non)") }), new EffectAreaParameterList("Mur destructible, avec HP ou AREA_HP", new Parameter[] { new Parameter("HP ou AREA_HP"), new Parameter("incr par level"), new Parameter("bloque le mouvement (1:oui, 0:non)"), new Parameter("bloque la ligne de vue (1:oui, 0:non)"), new Parameter("Utilise des AREA_HP plut\u00f4t que des HP (1:oui, 0:non(defaut))") }) });
    }
}
