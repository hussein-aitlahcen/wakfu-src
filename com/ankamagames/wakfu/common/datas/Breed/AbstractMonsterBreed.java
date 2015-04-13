package com.ankamagames.wakfu.common.datas.Breed;

import com.ankamagames.wakfu.common.game.craft.collect.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.movement.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.wakfu.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public abstract class AbstractMonsterBreed<TCollect extends AbstractCollectAction> implements Breed, NaturalStatesOwner
{
    private static final Logger m_logger;
    public static final byte DEFAULT_BREED_HEIGHT = 6;
    private static final TIntObjectHashMap EMPTY_COLLECTS;
    protected final MonsterBreedCharacteristicManager m_characteristicManager;
    private final short m_breedId;
    private final int m_familyId;
    private final short m_levelMin;
    private final short m_levelMax;
    private TIntObjectHashMap<TCollect> m_collectActions;
    private final int[] m_baseWorldProperties;
    private final int[] m_baseFightProperties;
    private final short m_aggroRadius;
    private final short m_sightRadius;
    private final byte m_physicalRadius;
    private final short[] m_naturalStates;
    private final int m_defeatScriptId;
    private final MovementSpeed m_walkSpeed;
    private final MovementSpeed m_runSpeed;
    private final int m_maxWalkDistance;
    private final int m_maxFightWalkDistance;
    private int m_requiredLeadershipPoints;
    private int[] m_fightMapIds;
    private int m_timelineBuffId;
    
    protected AbstractMonsterBreed(final short breedId, final int familyId, final short levelMin, final short levelMax, final EnumMap<FighterCharacteristicType, ObjectPair<Integer, Float>> fightCharacteristics, final int[] fightProperties, final int[] worldProperties, final short[] naturalStates, final short aggroRadius, final short sightRadius, final byte physicalRadius, final int defeatScriptId, final MovementSpeed walkSpeed, final MovementSpeed runSpeed, final int maxWalkDistance, final int maxFightWalkDistance, final WakfuMonsterAlignment wakfuAlignment) {
        super();
        this.m_requiredLeadershipPoints = 1;
        this.m_breedId = breedId;
        this.m_familyId = familyId;
        this.m_levelMin = levelMin;
        this.m_levelMax = levelMax;
        this.m_aggroRadius = aggroRadius;
        this.m_sightRadius = sightRadius;
        this.m_physicalRadius = physicalRadius;
        this.m_characteristicManager = new MonsterBreedCharacteristicManager(this.m_levelMin);
        for (final Map.Entry<FighterCharacteristicType, ObjectPair<Integer, Float>> entry : fightCharacteristics.entrySet()) {
            final ObjectPair<Integer, Float> characteristicValue = entry.getValue();
            this.m_characteristicManager.setCharacteristic(entry.getKey(), characteristicValue.getFirst(), characteristicValue.getSecond());
        }
        this.m_baseFightProperties = fightProperties;
        this.m_baseWorldProperties = worldProperties;
        if (naturalStates.length % 3 == 0) {
            this.m_naturalStates = naturalStates;
        }
        else {
            AbstractMonsterBreed.m_logger.error((Object)("INITIALISATION ERROR : the natural states array is not well built (%3 != 0 : all entries are not a stateId/level pair) sur la breed " + breedId));
            this.m_naturalStates = null;
        }
        this.m_defeatScriptId = defeatScriptId;
        this.m_walkSpeed = walkSpeed;
        this.m_runSpeed = runSpeed;
        this.m_maxWalkDistance = maxWalkDistance;
        this.m_maxFightWalkDistance = maxFightWalkDistance;
    }
    
    @Override
    public int getBaseCharacteristicValue(final FighterCharacteristicType type) {
        return this.m_characteristicManager.getBaseCharacteristic(type);
    }
    
    @Override
    public int getBaseTimerCountBeforeDeath() {
        return this.m_characteristicManager.getBaseCharacteristic(FighterCharacteristicType.KO_TIME_BEFORE_DEATH);
    }
    
    @Override
    public AbstractBattlegroundBorderEffectArea getBattlegroundBorderEffectArea() {
        return null;
    }
    
    @Override
    public float getRatio(final BreedRatios type) {
        return 1.0f;
    }
    
    @Override
    public byte getHeight() {
        return 6;
    }
    
    @Override
    public int getLeveledCharacteristic(final int level, final FighterCharacteristicType charac) {
        return this.m_characteristicManager.getCharacteristicValue(charac, level);
    }
    
    public void addCollectAction(final TCollect collect) {
        if (this.m_collectActions == null) {
            this.m_collectActions = new TIntObjectHashMap<TCollect>();
        }
        this.m_collectActions.put(collect.getId(), collect);
    }
    
    @Nullable
    public TCollect getCollectAction(final int collectId) {
        return (TCollect)((this.m_collectActions == null) ? null : ((TCollect)this.m_collectActions.get(collectId)));
    }
    
    public TIntObjectIterator<TCollect> collectActionIterator() {
        return (this.m_collectActions == null) ? AbstractMonsterBreed.EMPTY_COLLECTS.iterator() : this.m_collectActions.iterator();
    }
    
    public int[] getCollectActionIds() {
        return (this.m_collectActions == null) ? ArrayUtils.EMPTY_INT_ARRAY : this.m_collectActions.keys();
    }
    
    @Override
    public int[] getBaseFightProperties() {
        return this.m_baseFightProperties;
    }
    
    @Override
    public int[] getBaseWorldProperties() {
        return this.m_baseWorldProperties;
    }
    
    public boolean containsBaseFightProperties(final FightPropertyType fightPropertyType) {
        for (final int baseFightProperty : this.m_baseFightProperties) {
            if (baseFightProperty == fightPropertyType.getId()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public short getBreedId() {
        return this.m_breedId;
    }
    
    @Override
    public int getFamilyId() {
        return this.m_familyId;
    }
    
    public short getLevelMax() {
        return this.m_levelMax;
    }
    
    public short getLevelMin() {
        return this.m_levelMin;
    }
    
    @Override
    public short[] getNaturalStates() {
        return this.m_naturalStates;
    }
    
    public short getSightRadius() {
        return this.m_sightRadius;
    }
    
    public short getAggroRadius() {
        return this.m_aggroRadius;
    }
    
    @Override
    public byte getPhysicalRadius() {
        return this.m_physicalRadius;
    }
    
    public MonsterBreedCharacteristicManager getCharacteristicManager() {
        return this.m_characteristicManager;
    }
    
    @Override
    public int getDefeatScriptId() {
        return this.m_defeatScriptId;
    }
    
    @Override
    public int getMaxWalkDistance() {
        return this.m_maxWalkDistance;
    }
    
    @Override
    public int getMaxFightWalkDistance() {
        return this.m_maxFightWalkDistance;
    }
    
    @Override
    public MovementSpeed getWalkTimeBetweenCells() {
        return this.m_walkSpeed;
    }
    
    @Override
    public MovementSpeed getRunTimeBetweenCells() {
        return this.m_runSpeed;
    }
    
    public int getRequiredLeadershipPoints() {
        return this.m_requiredLeadershipPoints;
    }
    
    protected void setRequiredLeadershipPoints(final int requiredLeadershipPoints) {
        this.m_requiredLeadershipPoints = requiredLeadershipPoints;
    }
    
    public int[] getFightMapIds() {
        return this.m_fightMapIds;
    }
    
    public void setFightMapIds(final int[] fightMapIds) {
        this.m_fightMapIds = fightMapIds;
    }
    
    public int getTimelineBuffId() {
        return this.m_timelineBuffId;
    }
    
    public void setTimelineBuffId(final int timelineBuffId) {
        this.m_timelineBuffId = timelineBuffId;
    }
    
    @Override
    public String toString() {
        return "AbstractMonsterBreed{m_breedId=" + this.m_breedId + ", m_familyId=" + this.m_familyId + "} ";
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractMonsterBreed.class);
        EMPTY_COLLECTS = new TIntObjectHashMap();
    }
}
