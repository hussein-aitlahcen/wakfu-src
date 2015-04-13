package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class MonsterBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_familyId;
    protected short m_levelMin;
    protected short m_levelMax;
    protected float m_xpMultiplicator;
    protected float m_arcadePointsMultiplicator;
    protected int m_baseHp;
    protected int m_baseWp;
    protected int m_baseAp;
    protected int m_baseMp;
    protected int m_baseRange;
    protected int m_baseInit;
    protected int m_basePerception;
    protected int m_baseParade;
    protected int m_baseWill;
    protected int m_baseCriticalHit;
    protected int m_baseTimeBeforeDeath;
    protected float m_HpInc;
    protected float m_WpInc;
    protected float m_ApInc;
    protected float m_MpInc;
    protected float m_rangeInc;
    protected float m_initInc;
    protected float m_perceptionInc;
    protected float m_paradeInc;
    protected float m_willInc;
    protected float m_CriticalHitInc;
    protected int m_baseHealingBonus;
    protected int m_baseSummonBonus;
    protected int m_baseMechanicsBonus;
    protected int m_baseTackleBonus;
    protected int m_baseFireDamageBonus;
    protected int m_baseWaterDamageBonus;
    protected int m_baseEarthDamageBonus;
    protected int m_baseWindDamageBonus;
    protected int m_baseFireResistance;
    protected int m_baseWaterResistance;
    protected int m_baseEarthResistance;
    protected int m_baseWindResistance;
    protected int m_baseTackleResistance;
    protected int m_baseAPLossResistance;
    protected int m_basePMLossResistance;
    protected int m_baseWPLossResistance;
    protected float m_healingBonusInc;
    protected float m_tackleBonusInc;
    protected float m_fireDamageBonusInc;
    protected float m_waterDamageBonusInc;
    protected float m_earthDamageBonusInc;
    protected float m_windDamageBonusInc;
    protected float m_fireResistanceInc;
    protected float m_waterResistanceInc;
    protected float m_earthResistanceInc;
    protected float m_windResistanceInc;
    protected float m_tackleResistanceInc;
    protected float m_apLossResistanceInc;
    protected float m_pmLossResistanceInc;
    protected float m_wpLossResistanceInc;
    protected boolean m_hasDeadEvolution;
    protected short m_npcRank;
    protected short m_agroRadius;
    protected short m_sightRadius;
    protected int m_radiusSize;
    protected int[] m_fightProperties;
    protected int[] m_worldProperties;
    protected short[] m_naturalStates;
    protected SpellInfo[] m_spellsIdAndLevel;
    protected byte m_familyRank;
    protected short m_walkingSpeed;
    protected short m_runningSpeed;
    protected short m_runningRadiusInWorld;
    protected short m_runningRadiusInFight;
    protected MonsterAction[] m_monsterActionData;
    protected MonsterCollectActionData[] m_monsterCollectActionData;
    protected MonsterBehaviourData[] m_monsterBehaviourData;
    protected MonsterEvolutionData[] m_monsterEvolutionData;
    protected int m_requiredLeadershipPoints;
    protected short m_alignmentId;
    protected int m_timelineBuffId;
    protected int m_monsterHeight;
    protected int m_defeatScriptId;
    protected int[] m_gfxEquipment;
    protected Equipement[] m_specialGfxEquipement;
    protected Color[] m_specialGfxColor;
    protected Anim[] m_specialGfxAnim;
    protected int m_gfx;
    protected int m_timelineGfx;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getFamilyId() {
        return this.m_familyId;
    }
    
    public short getLevelMin() {
        return this.m_levelMin;
    }
    
    public short getLevelMax() {
        return this.m_levelMax;
    }
    
    public float getXpMultiplicator() {
        return this.m_xpMultiplicator;
    }
    
    public float getArcadePointsMultiplicator() {
        return this.m_arcadePointsMultiplicator;
    }
    
    public int getBaseHp() {
        return this.m_baseHp;
    }
    
    public int getBaseWp() {
        return this.m_baseWp;
    }
    
    public int getBaseAp() {
        return this.m_baseAp;
    }
    
    public int getBaseMp() {
        return this.m_baseMp;
    }
    
    public int getBaseRange() {
        return this.m_baseRange;
    }
    
    public int getBaseInit() {
        return this.m_baseInit;
    }
    
    public int getBasePerception() {
        return this.m_basePerception;
    }
    
    public int getBaseParade() {
        return this.m_baseParade;
    }
    
    public int getBaseWill() {
        return this.m_baseWill;
    }
    
    public int getBaseCriticalHit() {
        return this.m_baseCriticalHit;
    }
    
    public int getBaseCriticalMiss() {
        return 0;
    }
    
    public int getBaseTimeBeforeDeath() {
        return this.m_baseTimeBeforeDeath;
    }
    
    public float getHpInc() {
        return this.m_HpInc;
    }
    
    public float getWpInc() {
        return this.m_WpInc;
    }
    
    public float getApInc() {
        return this.m_ApInc;
    }
    
    public float getMpInc() {
        return this.m_MpInc;
    }
    
    public float getRangeInc() {
        return this.m_rangeInc;
    }
    
    public float getInitInc() {
        return this.m_initInc;
    }
    
    public float getPerceptionInc() {
        return this.m_perceptionInc;
    }
    
    public float getParadeInc() {
        return this.m_paradeInc;
    }
    
    public float getWillInc() {
        return this.m_willInc;
    }
    
    public float getCriticalHitInc() {
        return this.m_CriticalHitInc;
    }
    
    public float getCriticalMissInc() {
        return 0.0f;
    }
    
    public int getBaseHealingBonus() {
        return this.m_baseHealingBonus;
    }
    
    public int getBaseSummonBonus() {
        return this.m_baseSummonBonus;
    }
    
    public int getBaseMechanicsBonus() {
        return this.m_baseMechanicsBonus;
    }
    
    public int getBaseTackleBonus() {
        return this.m_baseTackleBonus;
    }
    
    public int getBaseFireDamageBonus() {
        return this.m_baseFireDamageBonus;
    }
    
    public int getBaseWaterDamageBonus() {
        return this.m_baseWaterDamageBonus;
    }
    
    public int getBaseEarthDamageBonus() {
        return this.m_baseEarthDamageBonus;
    }
    
    public int getBaseWindDamageBonus() {
        return this.m_baseWindDamageBonus;
    }
    
    public int getBaseFireResistance() {
        return this.m_baseFireResistance;
    }
    
    public int getBaseWaterResistance() {
        return this.m_baseWaterResistance;
    }
    
    public int getBaseEarthResistance() {
        return this.m_baseEarthResistance;
    }
    
    public int getBaseWindResistance() {
        return this.m_baseWindResistance;
    }
    
    public int getBaseTackleResistance() {
        return this.m_baseTackleResistance;
    }
    
    public int getBaseAPLossResistance() {
        return this.m_baseAPLossResistance;
    }
    
    public int getBasePMLossResistance() {
        return this.m_basePMLossResistance;
    }
    
    public int getBaseWPLossResistance() {
        return this.m_baseWPLossResistance;
    }
    
    public int getBaseAPDebuffPower() {
        return 0;
    }
    
    public int getBaseMPDebuffPower() {
        return 0;
    }
    
    public float getHealingBonusInc() {
        return this.m_healingBonusInc;
    }
    
    public float getSummonBonusInc() {
        return 0.0f;
    }
    
    public float getTackleBonusInc() {
        return this.m_tackleBonusInc;
    }
    
    public float getFireDamageBonusInc() {
        return this.m_fireDamageBonusInc;
    }
    
    public float getWaterDamageBonusInc() {
        return this.m_waterDamageBonusInc;
    }
    
    public float getEarthDamageBonusInc() {
        return this.m_earthDamageBonusInc;
    }
    
    public float getWindDamageBonusInc() {
        return this.m_windDamageBonusInc;
    }
    
    public float getFireResistanceInc() {
        return this.m_fireResistanceInc;
    }
    
    public float getWaterResistanceInc() {
        return this.m_waterResistanceInc;
    }
    
    public float getEarthResistanceInc() {
        return this.m_earthResistanceInc;
    }
    
    public float getWindResistanceInc() {
        return this.m_windResistanceInc;
    }
    
    public float getTackleResistanceInc() {
        return this.m_tackleResistanceInc;
    }
    
    public float getApLossResistanceInc() {
        return this.m_apLossResistanceInc;
    }
    
    public float getPmLossResistanceInc() {
        return this.m_pmLossResistanceInc;
    }
    
    public float getWpLossResistanceInc() {
        return this.m_wpLossResistanceInc;
    }
    
    public float getApDebuffPowerInc() {
        return 0.0f;
    }
    
    public float getMpDebuffPowerInc() {
        return 0.0f;
    }
    
    public boolean hasDeadEvolution() {
        return this.m_hasDeadEvolution;
    }
    
    public short getNpcRank() {
        return this.m_npcRank;
    }
    
    public short getAgroRadius() {
        return this.m_agroRadius;
    }
    
    public short getSightRadius() {
        return this.m_sightRadius;
    }
    
    public int getRadiusSize() {
        return this.m_radiusSize;
    }
    
    public int[] getFightProperties() {
        return this.m_fightProperties;
    }
    
    public int[] getWorldProperties() {
        return this.m_worldProperties;
    }
    
    public short[] getNaturalStates() {
        return this.m_naturalStates;
    }
    
    public SpellInfo[] getSpellsIdAndLevel() {
        return this.m_spellsIdAndLevel;
    }
    
    public byte getFamilyRank() {
        return this.m_familyRank;
    }
    
    public short getWalkingSpeed() {
        return this.m_walkingSpeed;
    }
    
    public short getRunningSpeed() {
        return this.m_runningSpeed;
    }
    
    public short getRunningRadiusInWorld() {
        return this.m_runningRadiusInWorld;
    }
    
    public short getRunningRadiusInFight() {
        return this.m_runningRadiusInFight;
    }
    
    public MonsterAction[] getMonsterActionData() {
        return this.m_monsterActionData;
    }
    
    public MonsterCollectActionData[] getMonsterCollectActionData() {
        return this.m_monsterCollectActionData;
    }
    
    public MonsterBehaviourData[] getMonsterBehaviourData() {
        return this.m_monsterBehaviourData;
    }
    
    public MonsterEvolutionData[] getMonsterEvolutionData() {
        return this.m_monsterEvolutionData;
    }
    
    public int getRequiredLeadershipPoints() {
        return this.m_requiredLeadershipPoints;
    }
    
    public int getRemainingTimeToAddForTimeScore() {
        return 0;
    }
    
    public short getAlignmentId() {
        return this.m_alignmentId;
    }
    
    public int getTimelineBuffId() {
        return this.m_timelineBuffId;
    }
    
    public int getMonsterHeight() {
        return this.m_monsterHeight;
    }
    
    public int getDefeatScriptId() {
        return this.m_defeatScriptId;
    }
    
    public int[] getGfxEquipment() {
        return this.m_gfxEquipment;
    }
    
    public Equipement[] getSpecialGfxEquipement() {
        return this.m_specialGfxEquipement;
    }
    
    public Color[] getSpecialGfxColor() {
        return this.m_specialGfxColor;
    }
    
    public Anim[] getSpecialGfxAnim() {
        return this.m_specialGfxAnim;
    }
    
    public int getGfx() {
        return this.m_gfx;
    }
    
    public int getTimelineGfx() {
        return this.m_timelineGfx;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_familyId = 0;
        this.m_levelMin = 0;
        this.m_levelMax = 0;
        this.m_xpMultiplicator = 0.0f;
        this.m_arcadePointsMultiplicator = 0.0f;
        this.m_baseHp = 0;
        this.m_baseWp = 0;
        this.m_baseAp = 0;
        this.m_baseMp = 0;
        this.m_baseRange = 0;
        this.m_baseInit = 0;
        this.m_basePerception = 0;
        this.m_baseParade = 0;
        this.m_baseWill = 0;
        this.m_baseCriticalHit = 0;
        this.m_baseTimeBeforeDeath = 0;
        this.m_HpInc = 0.0f;
        this.m_WpInc = 0.0f;
        this.m_ApInc = 0.0f;
        this.m_MpInc = 0.0f;
        this.m_rangeInc = 0.0f;
        this.m_initInc = 0.0f;
        this.m_perceptionInc = 0.0f;
        this.m_paradeInc = 0.0f;
        this.m_willInc = 0.0f;
        this.m_CriticalHitInc = 0.0f;
        this.m_baseHealingBonus = 0;
        this.m_baseSummonBonus = 0;
        this.m_baseMechanicsBonus = 0;
        this.m_baseTackleBonus = 0;
        this.m_baseFireDamageBonus = 0;
        this.m_baseWaterDamageBonus = 0;
        this.m_baseEarthDamageBonus = 0;
        this.m_baseWindDamageBonus = 0;
        this.m_baseFireResistance = 0;
        this.m_baseWaterResistance = 0;
        this.m_baseEarthResistance = 0;
        this.m_baseWindResistance = 0;
        this.m_baseTackleResistance = 0;
        this.m_baseAPLossResistance = 0;
        this.m_basePMLossResistance = 0;
        this.m_baseWPLossResistance = 0;
        this.m_healingBonusInc = 0.0f;
        this.m_tackleBonusInc = 0.0f;
        this.m_fireDamageBonusInc = 0.0f;
        this.m_waterDamageBonusInc = 0.0f;
        this.m_earthDamageBonusInc = 0.0f;
        this.m_windDamageBonusInc = 0.0f;
        this.m_fireResistanceInc = 0.0f;
        this.m_waterResistanceInc = 0.0f;
        this.m_earthResistanceInc = 0.0f;
        this.m_windResistanceInc = 0.0f;
        this.m_tackleResistanceInc = 0.0f;
        this.m_apLossResistanceInc = 0.0f;
        this.m_pmLossResistanceInc = 0.0f;
        this.m_wpLossResistanceInc = 0.0f;
        this.m_hasDeadEvolution = false;
        this.m_npcRank = 0;
        this.m_agroRadius = 0;
        this.m_sightRadius = 0;
        this.m_radiusSize = 0;
        this.m_fightProperties = null;
        this.m_worldProperties = null;
        this.m_naturalStates = null;
        this.m_spellsIdAndLevel = null;
        this.m_familyRank = 0;
        this.m_walkingSpeed = 0;
        this.m_runningSpeed = 0;
        this.m_runningRadiusInWorld = 0;
        this.m_runningRadiusInFight = 0;
        this.m_monsterActionData = null;
        this.m_monsterCollectActionData = null;
        this.m_monsterBehaviourData = null;
        this.m_monsterEvolutionData = null;
        this.m_requiredLeadershipPoints = 0;
        this.m_alignmentId = 0;
        this.m_timelineBuffId = 0;
        this.m_monsterHeight = 0;
        this.m_defeatScriptId = 0;
        this.m_gfxEquipment = null;
        this.m_specialGfxEquipement = null;
        this.m_specialGfxColor = null;
        this.m_specialGfxAnim = null;
        this.m_gfx = 0;
        this.m_timelineGfx = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_familyId = buffer.getInt();
        this.m_levelMin = buffer.getShort();
        this.m_levelMax = buffer.getShort();
        this.m_xpMultiplicator = buffer.getFloat();
        this.m_arcadePointsMultiplicator = buffer.getFloat();
        this.m_baseHp = buffer.getInt();
        this.m_baseWp = buffer.getInt();
        this.m_baseAp = buffer.getInt();
        this.m_baseMp = buffer.getInt();
        this.m_baseRange = buffer.getInt();
        this.m_baseInit = buffer.getInt();
        this.m_basePerception = buffer.getInt();
        this.m_baseParade = buffer.getInt();
        this.m_baseWill = buffer.getInt();
        this.m_baseCriticalHit = buffer.getInt();
        this.m_baseTimeBeforeDeath = buffer.getInt();
        this.m_HpInc = buffer.getFloat();
        this.m_WpInc = buffer.getFloat();
        this.m_ApInc = buffer.getFloat();
        this.m_MpInc = buffer.getFloat();
        this.m_rangeInc = buffer.getFloat();
        this.m_initInc = buffer.getFloat();
        this.m_perceptionInc = buffer.getFloat();
        this.m_paradeInc = buffer.getFloat();
        this.m_willInc = buffer.getFloat();
        this.m_CriticalHitInc = buffer.getFloat();
        this.m_baseHealingBonus = buffer.getInt();
        this.m_baseSummonBonus = buffer.getInt();
        this.m_baseMechanicsBonus = buffer.getInt();
        this.m_baseTackleBonus = buffer.getInt();
        this.m_baseFireDamageBonus = buffer.getInt();
        this.m_baseWaterDamageBonus = buffer.getInt();
        this.m_baseEarthDamageBonus = buffer.getInt();
        this.m_baseWindDamageBonus = buffer.getInt();
        this.m_baseFireResistance = buffer.getInt();
        this.m_baseWaterResistance = buffer.getInt();
        this.m_baseEarthResistance = buffer.getInt();
        this.m_baseWindResistance = buffer.getInt();
        this.m_baseTackleResistance = buffer.getInt();
        this.m_baseAPLossResistance = buffer.getInt();
        this.m_basePMLossResistance = buffer.getInt();
        this.m_baseWPLossResistance = buffer.getInt();
        this.m_healingBonusInc = buffer.getFloat();
        this.m_tackleBonusInc = buffer.getFloat();
        this.m_fireDamageBonusInc = buffer.getFloat();
        this.m_waterDamageBonusInc = buffer.getFloat();
        this.m_earthDamageBonusInc = buffer.getFloat();
        this.m_windDamageBonusInc = buffer.getFloat();
        this.m_fireResistanceInc = buffer.getFloat();
        this.m_waterResistanceInc = buffer.getFloat();
        this.m_earthResistanceInc = buffer.getFloat();
        this.m_windResistanceInc = buffer.getFloat();
        this.m_tackleResistanceInc = buffer.getFloat();
        this.m_apLossResistanceInc = buffer.getFloat();
        this.m_pmLossResistanceInc = buffer.getFloat();
        this.m_wpLossResistanceInc = buffer.getFloat();
        this.m_hasDeadEvolution = buffer.readBoolean();
        this.m_npcRank = buffer.getShort();
        this.m_agroRadius = buffer.getShort();
        this.m_sightRadius = buffer.getShort();
        this.m_radiusSize = buffer.getInt();
        this.m_fightProperties = buffer.readIntArray();
        this.m_worldProperties = buffer.readIntArray();
        this.m_naturalStates = buffer.readShortArray();
        final int spellsIdAndLevelCount = buffer.getInt();
        this.m_spellsIdAndLevel = new SpellInfo[spellsIdAndLevelCount];
        for (int iSpellsIdAndLevel = 0; iSpellsIdAndLevel < spellsIdAndLevelCount; ++iSpellsIdAndLevel) {
            (this.m_spellsIdAndLevel[iSpellsIdAndLevel] = new SpellInfo()).read(buffer);
        }
        this.m_familyRank = buffer.get();
        this.m_walkingSpeed = buffer.getShort();
        this.m_runningSpeed = buffer.getShort();
        this.m_runningRadiusInWorld = buffer.getShort();
        this.m_runningRadiusInFight = buffer.getShort();
        final int monsterActionDataCount = buffer.getInt();
        this.m_monsterActionData = new MonsterAction[monsterActionDataCount];
        for (int iMonsterActionData = 0; iMonsterActionData < monsterActionDataCount; ++iMonsterActionData) {
            (this.m_monsterActionData[iMonsterActionData] = new MonsterAction()).read(buffer);
        }
        final int monsterCollectActionDataCount = buffer.getInt();
        this.m_monsterCollectActionData = new MonsterCollectActionData[monsterCollectActionDataCount];
        for (int iMonsterCollectActionData = 0; iMonsterCollectActionData < monsterCollectActionDataCount; ++iMonsterCollectActionData) {
            (this.m_monsterCollectActionData[iMonsterCollectActionData] = new MonsterCollectActionData()).read(buffer);
        }
        final int monsterBehaviourDataCount = buffer.getInt();
        this.m_monsterBehaviourData = new MonsterBehaviourData[monsterBehaviourDataCount];
        for (int iMonsterBehaviourData = 0; iMonsterBehaviourData < monsterBehaviourDataCount; ++iMonsterBehaviourData) {
            (this.m_monsterBehaviourData[iMonsterBehaviourData] = new MonsterBehaviourData()).read(buffer);
        }
        final int monsterEvolutionDataCount = buffer.getInt();
        this.m_monsterEvolutionData = new MonsterEvolutionData[monsterEvolutionDataCount];
        for (int iMonsterEvolutionData = 0; iMonsterEvolutionData < monsterEvolutionDataCount; ++iMonsterEvolutionData) {
            (this.m_monsterEvolutionData[iMonsterEvolutionData] = new MonsterEvolutionData()).read(buffer);
        }
        this.m_requiredLeadershipPoints = buffer.getInt();
        this.m_alignmentId = buffer.getShort();
        this.m_timelineBuffId = buffer.getInt();
        this.m_monsterHeight = buffer.getInt();
        this.m_defeatScriptId = buffer.getInt();
        this.m_gfxEquipment = buffer.readIntArray();
        final int specialGfxEquipementCount = buffer.getInt();
        this.m_specialGfxEquipement = new Equipement[specialGfxEquipementCount];
        for (int iSpecialGfxEquipement = 0; iSpecialGfxEquipement < specialGfxEquipementCount; ++iSpecialGfxEquipement) {
            (this.m_specialGfxEquipement[iSpecialGfxEquipement] = new Equipement()).read(buffer);
        }
        final int specialGfxColorCount = buffer.getInt();
        this.m_specialGfxColor = new Color[specialGfxColorCount];
        for (int iSpecialGfxColor = 0; iSpecialGfxColor < specialGfxColorCount; ++iSpecialGfxColor) {
            (this.m_specialGfxColor[iSpecialGfxColor] = new Color()).read(buffer);
        }
        final int specialGfxAnimCount = buffer.getInt();
        this.m_specialGfxAnim = new Anim[specialGfxAnimCount];
        for (int iSpecialGfxAnim = 0; iSpecialGfxAnim < specialGfxAnimCount; ++iSpecialGfxAnim) {
            (this.m_specialGfxAnim[iSpecialGfxAnim] = new Anim()).read(buffer);
        }
        this.m_gfx = buffer.getInt();
        this.m_timelineGfx = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.MONSTER.getId();
    }
    
    public static class SpellInfo
    {
        protected int m_id;
        protected int m_level;
        
        public int getId() {
            return this.m_id;
        }
        
        public int getLevel() {
            return this.m_level;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_level = buffer.getInt();
        }
    }
    
    public static class Equipement
    {
        protected String m_fileId;
        protected String[] m_parts;
        
        public String getFileId() {
            return this.m_fileId;
        }
        
        public String[] getParts() {
            return this.m_parts;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_fileId = buffer.readUTF8().intern();
            this.m_parts = buffer.readStringArray();
        }
    }
    
    public static class Color
    {
        protected int m_partIndex;
        protected int m_color;
        
        public int getPartIndex() {
            return this.m_partIndex;
        }
        
        public int getColor() {
            return this.m_color;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_partIndex = buffer.getInt();
            this.m_color = buffer.getInt();
        }
    }
    
    public static class Anim
    {
        protected byte m_key;
        protected String m_animName;
        
        public byte getKey() {
            return this.m_key;
        }
        
        public String getAnimName() {
            return this.m_animName;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_key = buffer.get();
            this.m_animName = buffer.readUTF8().intern();
        }
    }
    
    public static class MonsterCollectActionData
    {
        protected int m_id;
        protected int m_skillId;
        protected int m_skillLevelRequired;
        protected int m_skillVisualFeedbackId;
        protected String m_criteria;
        protected float m_xpFactor;
        protected int m_collectLootListId;
        protected int m_duration;
        protected int m_collectItemId;
        protected int[] m_lootList;
        protected boolean m_displayInCraftDialog;
        
        public int getId() {
            return this.m_id;
        }
        
        public int getSkillId() {
            return this.m_skillId;
        }
        
        public int getSkillLevelRequired() {
            return this.m_skillLevelRequired;
        }
        
        public int getSkillVisualFeedbackId() {
            return this.m_skillVisualFeedbackId;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public float getXpFactor() {
            return this.m_xpFactor;
        }
        
        public int getCollectLootListId() {
            return this.m_collectLootListId;
        }
        
        public int getDuration() {
            return this.m_duration;
        }
        
        public int getCollectItemId() {
            return this.m_collectItemId;
        }
        
        public int[] getLootList() {
            return this.m_lootList;
        }
        
        public boolean isDisplayInCraftDialog() {
            return this.m_displayInCraftDialog;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_skillId = buffer.getInt();
            this.m_skillLevelRequired = buffer.getInt();
            this.m_skillVisualFeedbackId = buffer.getInt();
            this.m_criteria = buffer.readUTF8().intern();
            this.m_xpFactor = buffer.getFloat();
            this.m_collectLootListId = buffer.getInt();
            this.m_duration = buffer.getInt();
            this.m_collectItemId = buffer.getInt();
            this.m_lootList = buffer.readIntArray();
            this.m_displayInCraftDialog = buffer.readBoolean();
        }
    }
    
    public static class MonsterAction
    {
        protected int m_id;
        protected byte m_type;
        protected String m_criteria;
        protected boolean m_criteriaOnNpc;
        protected boolean m_moveToMonsterBeforeInteractWithHim;
        protected int m_duration;
        protected boolean m_showProgressBar;
        protected int m_visualId;
        protected int m_scriptId;
        
        public int getId() {
            return this.m_id;
        }
        
        public byte getType() {
            return this.m_type;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public boolean isCriteriaOnNpc() {
            return this.m_criteriaOnNpc;
        }
        
        public boolean isMoveToMonsterBeforeInteractWithHim() {
            return this.m_moveToMonsterBeforeInteractWithHim;
        }
        
        public int getDuration() {
            return this.m_duration;
        }
        
        public boolean isShowProgressBar() {
            return this.m_showProgressBar;
        }
        
        public int getVisualId() {
            return this.m_visualId;
        }
        
        public int getScriptId() {
            return this.m_scriptId;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_type = buffer.get();
            this.m_criteria = buffer.readUTF8().intern();
            this.m_criteriaOnNpc = buffer.readBoolean();
            this.m_moveToMonsterBeforeInteractWithHim = buffer.readBoolean();
            this.m_duration = buffer.getInt();
            this.m_showProgressBar = buffer.readBoolean();
            this.m_visualId = buffer.getInt();
            this.m_scriptId = buffer.getInt();
        }
    }
    
    public static class MonsterBehaviourData
    {
        protected int m_id;
        protected int m_type;
        protected int m_scriptId;
        protected boolean m_needsToWaitPathEnd;
        
        public int getId() {
            return this.m_id;
        }
        
        public int getType() {
            return this.m_type;
        }
        
        public int getScriptId() {
            return this.m_scriptId;
        }
        
        public boolean isNeedsToWaitPathEnd() {
            return this.m_needsToWaitPathEnd;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_type = buffer.getInt();
            this.m_scriptId = buffer.getInt();
            this.m_needsToWaitPathEnd = buffer.readBoolean();
        }
    }
    
    public static class MonsterEvolutionData
    {
        protected int m_id;
        protected int m_breedId;
        protected int m_scriptId;
        
        public int getId() {
            return this.m_id;
        }
        
        public int getBreedId() {
            return this.m_breedId;
        }
        
        public int getScriptId() {
            return this.m_scriptId;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_breedId = buffer.getInt();
            this.m_scriptId = buffer.getInt();
        }
    }
}
