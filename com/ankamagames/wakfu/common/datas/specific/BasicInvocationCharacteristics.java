package com.ankamagames.wakfu.common.datas.specific;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;

public class BasicInvocationCharacteristics implements Levelable, RawConvertible<RawInvocationCharacteristic>
{
    private static final Logger m_logger;
    private static final boolean DEBUG_MODE = false;
    private short m_typeId;
    private AbstractMonsterBreed m_breed;
    private String m_name;
    private int m_currentHp;
    private long m_summonId;
    private long m_currentXP;
    private short m_cappedLevel;
    private short m_forcedLevel;
    private byte m_obstacleId;
    private byte m_teamId;
    private long m_summonerId;
    private Direction8 m_direction;
    private boolean m_comeFromSymbiot;
    
    public BasicInvocationCharacteristics() {
        super();
        this.m_breed = null;
        this.m_name = "";
        this.m_obstacleId = -1;
        this.m_teamId = -1;
        this.m_summonerId = -1L;
        this.m_direction = Direction8.NONE;
    }
    
    public BasicInvocationCharacteristics(final short id, final String name, final int hp, final short level, final short cappedLevel) {
        super();
        this.m_breed = null;
        this.m_name = "";
        this.m_obstacleId = -1;
        this.m_teamId = -1;
        this.m_summonerId = -1L;
        this.m_direction = Direction8.NONE;
        this.m_typeId = id;
        this.m_name = name;
        this.m_currentHp = hp;
        this.m_summonId = -1L;
        this.m_cappedLevel = cappedLevel;
        this.m_currentXP = this.getXpTable().getXpByLevel(level);
    }
    
    public BasicInvocationCharacteristics(final short id, final String name, final int hp, final long xp, final short cappedLevel) {
        super();
        this.m_breed = null;
        this.m_name = "";
        this.m_obstacleId = -1;
        this.m_teamId = -1;
        this.m_summonerId = -1L;
        this.m_direction = Direction8.NONE;
        this.m_typeId = id;
        this.m_name = name;
        this.m_currentHp = hp;
        this.m_summonId = -1L;
        this.m_currentXP = xp;
        this.m_cappedLevel = cappedLevel;
    }
    
    public void setSummonId(final long summoned) {
        this.m_summonId = summoned;
    }
    
    public int getCurrentHp() {
        return this.m_currentHp;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public short getTypeId() {
        return this.m_typeId;
    }
    
    public short getCappedLevel() {
        return this.m_cappedLevel;
    }
    
    @Override
    public short getLevel() {
        if (this.m_forcedLevel > 0) {
            return this.m_forcedLevel;
        }
        short level = this.getXpTable().getLevelByXp(this.m_currentXP);
        if (this.m_cappedLevel < level) {
            level = this.m_cappedLevel;
        }
        return level;
    }
    
    public long getSummonId() {
        return this.m_summonId;
    }
    
    protected void setCurrentXp(final long currentXP) {
        this.m_currentXP = currentXP;
    }
    
    public long getCurrentXP() {
        return this.m_currentXP;
    }
    
    public void setBreed(final AbstractMonsterBreed breed) {
        this.m_breed = breed;
    }
    
    public short getBreedMaxLevel() {
        if (this.m_breed != null) {
            return this.m_breed.getLevelMax();
        }
        return 0;
    }
    
    public void setObstacleId(final byte obstacleId) {
        this.m_obstacleId = obstacleId;
    }
    
    public void setDirection(final Direction8 direction) {
        this.m_direction = direction;
    }
    
    public Direction8 getDirection() {
        return this.m_direction;
    }
    
    public long getSummonerId() {
        return this.m_summonerId;
    }
    
    public void setSummonerId(final long summonerId) {
        this.m_summonerId = summonerId;
    }
    
    public void setForcedLevel(final short forcedLevel) {
        this.m_forcedLevel = forcedLevel;
    }
    
    public byte getSex() {
        return 0;
    }
    
    public byte getTeamId() {
        return this.m_teamId;
    }
    
    public void setTeamId(final byte teamId) {
        this.m_teamId = teamId;
    }
    
    public void initializeSummoning(final BasicCharacterInfo summoning, final BasicCharacterInfo summoner) {
        summoning.setInvocationCharacteristic(this);
        if (this.m_currentHp > 0) {
            summoning.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP).set(this.m_currentHp);
        }
        this.initializeDmgPercent(summoning, summoner);
        summoning.setName(this.m_name);
        summoning.setObstacleId(this.m_obstacleId);
        if (summoner.isActiveProperty(WorldPropertyType.IS_ARCADE_NPC)) {
            summoning.addProperty(WorldPropertyType.IS_ARCADE_NPC);
        }
    }
    
    private void initializeDmgPercent(final BasicCharacterInfo summoning, final BasicCharacterInfo summoner) {
        final boolean useSummonerDmgPercent = ((AbstractMonsterBreed)summoning.getBreed()).containsBaseFightProperties(FightPropertyType.USE_SUMMONER_DMG_PERCENT);
        final boolean useSummoningMastery = !((AbstractMonsterBreed)summoning.getBreed()).containsBaseFightProperties(FightPropertyType.DONT_USE_SUMMONING_MASTERY);
        if (useSummoningMastery && summoner.hasCharacteristic(FighterCharacteristicType.SUMMONING_MASTERY) && !this.comeFromSymbiot()) {
            summoning.getCharacteristic((CharacteristicType)FighterCharacteristicType.DMG_IN_PERCENT).add(summoner.getCharacteristicValue(FighterCharacteristicType.SUMMONING_MASTERY));
        }
        if (useSummonerDmgPercent && summoner.hasCharacteristic(FighterCharacteristicType.DMG_IN_PERCENT)) {
            summoning.getCharacteristic((CharacteristicType)FighterCharacteristicType.DMG_IN_PERCENT).add(summoner.getCharacteristicValue(FighterCharacteristicType.DMG_IN_PERCENT));
        }
    }
    
    @Override
    public boolean toRaw(final RawInvocationCharacteristic rawCharacteristics) {
        rawCharacteristics.clear();
        rawCharacteristics.typeid = this.m_typeId;
        rawCharacteristics.name = this.m_name;
        rawCharacteristics.currentHp = this.m_currentHp;
        rawCharacteristics.summonId = this.m_summonId;
        rawCharacteristics.currentXP = this.m_currentXP;
        rawCharacteristics.cappedLevel = this.m_cappedLevel;
        rawCharacteristics.obstacleId = this.m_obstacleId;
        rawCharacteristics.direction = this.m_direction.m_index;
        rawCharacteristics.summonerId = this.m_summonerId;
        rawCharacteristics.forcedLevel = this.m_forcedLevel;
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawInvocationCharacteristic raw) {
        this.m_typeId = raw.typeid;
        this.m_name = raw.name;
        this.m_currentHp = raw.currentHp;
        this.m_summonId = raw.summonId;
        this.m_currentXP = raw.currentXP;
        this.m_cappedLevel = raw.cappedLevel;
        this.m_obstacleId = raw.obstacleId;
        this.m_direction = Direction8.getDirectionFromIndex(raw.direction);
        this.m_summonerId = raw.summonerId;
        this.m_forcedLevel = raw.forcedLevel;
        return true;
    }
    
    @Override
    public XpTable getXpTable() {
        return InvocationXpTable.getInstance();
    }
    
    @Override
    public XpModification setLevelAndXp(final short level, final long xp) {
        final short levelDiff = (short)(level - this.m_cappedLevel);
        final long xpDiff = xp - this.m_currentXP;
        this.m_cappedLevel = level;
        this.m_currentXP = xp;
        return new XpModification(xpDiff, levelDiff);
    }
    
    @Override
    public XpModification setLevel(final short level, final boolean linkXp) {
        return this.setLevelAndXp(level, this.m_currentXP);
    }
    
    @Override
    public long getXp() {
        return this.m_currentXP;
    }
    
    @Override
    public XpModification addXp(final long xpAdded) {
        if (xpAdded < 0L) {
            return XpModification.NONE;
        }
        if (xpAdded == 0L) {
            return XpModification.NONE;
        }
        this.m_currentXP += xpAdded;
        return new XpModification(xpAdded, (short)0);
    }
    
    @Override
    public float getCurrentLevelPercentage() {
        if (this.m_cappedLevel < this.getXpTable().getLevelByXp(this.getXp())) {
            return 0.0f;
        }
        return this.getXpTable().getPercentageInLevel(this.getLevel(), this.getXp());
    }
    
    @Override
    public float getXpGainMultiplier() {
        return 1.0f;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BasicInvocationCharacteristics");
        sb.append("{m_breed=").append(this.m_breed);
        sb.append(", m_typeId=").append(this.m_typeId);
        sb.append(", m_name='").append(this.m_name).append('\'');
        sb.append(", m_currentHp=").append(this.m_currentHp);
        sb.append(", m_summonId=").append(this.m_summonId);
        sb.append(", m_currentXP=").append(this.m_currentXP);
        sb.append(", m_cappedLevel=").append(this.m_cappedLevel);
        sb.append('}');
        return sb.toString();
    }
    
    public void clean() {
    }
    
    public int getRequiredLeadershipPoint() {
        if (this.m_breed != null) {
            return this.m_breed.getRequiredLeadershipPoints();
        }
        return 0;
    }
    
    public void setComeFromSymbiot(final boolean comeFromSymbiot) {
        this.m_comeFromSymbiot = comeFromSymbiot;
    }
    
    public boolean comeFromSymbiot() {
        return this.m_comeFromSymbiot;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BasicInvocationCharacteristics.class);
    }
}
