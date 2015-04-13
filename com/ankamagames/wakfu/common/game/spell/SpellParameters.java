package com.ankamagames.wakfu.common.game.spell;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.constants.*;
import java.util.*;

public class SpellParameters
{
    private int m_id;
    private short m_maxLevel;
    private int m_breedId;
    private byte m_castMaxPerTarget;
    private float m_castMaxPerTurn;
    private float m_castMaxPerTurnIncr;
    private byte m_castMinInterval;
    private boolean m_castOnlyInLine;
    private boolean m_castOnlyInDiag;
    private boolean m_testLineOfSight;
    private boolean m_testFreeCell;
    private boolean m_testDirectPath;
    private boolean m_testNotBorderCell;
    private byte m_elementId;
    private byte m_maxEffectCap;
    private short m_xpGainPercentage;
    private byte m_parentSkillId;
    private SimpleCriterion m_learningCriterions;
    private int m_targetFilter;
    private SimpleCriterion m_castCriterions;
    private Map<FighterCharacteristicType, CastParameter> m_baseCastParameters;
    private float m_rangeMinBase;
    private float m_rangeMinLevelIncrement;
    private float m_rangeMaxBase;
    private float m_rangeMaxLevelIncrement;
    private byte m_passive;
    private boolean m_associatedWithItemUse;
    private boolean m_canCastWhenCarrying;
    private boolean m_spellCastRangeDynamic;
    private boolean m_castSpellWillBreakInvisibility;
    private boolean m_castOnRandomCell;
    private byte m_spellType;
    private int m_scriptId;
    private boolean m_useAutomaticDescription;
    private short m_uiPosition;
    private boolean m_showInTimeline;
    private int m_gfxId;
    private boolean m_tunnelable;
    private Map<SimpleCriterion, SpellCastParameters> m_alternativeCastParameters;
    private boolean m_canCastOnCasterCell;
    private boolean m_sramShadowSpell;
    private ActionOnCriticalMiss m_actionOnCriticalMiss;
    private int[] m_properties;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public int getBreedId() {
        return this.m_breedId;
    }
    
    public byte getCastMaxPerTarget() {
        return this.m_castMaxPerTarget;
    }
    
    public float getCastMaxPerTurn() {
        return this.m_castMaxPerTurn;
    }
    
    public byte getCastMinInterval() {
        return this.m_castMinInterval;
    }
    
    public boolean isCastOnlyInLine() {
        return this.m_castOnlyInLine;
    }
    
    public boolean isCastOnlyInDiag() {
        return this.m_castOnlyInDiag;
    }
    
    public boolean isTestLineOfSight() {
        return this.m_testLineOfSight;
    }
    
    public boolean isTestFreeCell() {
        return this.m_testFreeCell;
    }
    
    public boolean isTestDirectPath() {
        return this.m_testDirectPath;
    }
    
    public boolean isTestNotBorderCell() {
        return this.m_testNotBorderCell;
    }
    
    public byte getElementId() {
        return this.m_elementId;
    }
    
    public byte getMaxEffectCap() {
        return this.m_maxEffectCap;
    }
    
    public short getXpGainPercentage() {
        return this.m_xpGainPercentage;
    }
    
    public byte getParentSkillId() {
        return this.m_parentSkillId;
    }
    
    public SimpleCriterion getLearningCriterions() {
        return this.m_learningCriterions;
    }
    
    public int getTargetFilter() {
        return this.m_targetFilter;
    }
    
    public SimpleCriterion getCastCriterions() {
        return this.m_castCriterions;
    }
    
    public float getRangeMinBase() {
        return this.m_rangeMinBase;
    }
    
    public float getRangeMinLevelIncrement() {
        return this.m_rangeMinLevelIncrement;
    }
    
    public float getRangeMaxBase() {
        return this.m_rangeMaxBase;
    }
    
    public float getRangeMaxLevelIncrement() {
        return this.m_rangeMaxLevelIncrement;
    }
    
    public byte getPassive() {
        return this.m_passive;
    }
    
    public boolean isAssociatedWithItemUse() {
        return this.m_associatedWithItemUse;
    }
    
    public boolean isCanCastWhenCarrying() {
        return this.m_canCastWhenCarrying;
    }
    
    public boolean isSpellCastRangeDynamic() {
        return this.m_spellCastRangeDynamic;
    }
    
    public boolean isCastSpellWillBreakInvisibility() {
        return this.m_castSpellWillBreakInvisibility;
    }
    
    public boolean isCastOnRandomCell() {
        return this.m_castOnRandomCell;
    }
    
    public void setId(final int id) {
        this.m_id = id;
    }
    
    public void setMaxLevel(final short maxLevel) {
        this.m_maxLevel = maxLevel;
    }
    
    public void setBreedId(final int breedId) {
        this.m_breedId = breedId;
    }
    
    public void setCastMaxPerTarget(final byte castMaxPerTarget) {
        this.m_castMaxPerTarget = castMaxPerTarget;
    }
    
    public void setCastMaxPerTurn(final float castMaxPerTurn) {
        this.m_castMaxPerTurn = castMaxPerTurn;
    }
    
    public void setCastMinInterval(final byte castMinInterval) {
        this.m_castMinInterval = castMinInterval;
    }
    
    public void setCastOnlyInLine(final boolean castOnlyInLine) {
        this.m_castOnlyInLine = castOnlyInLine;
    }
    
    public void setCastOnlyInDiag(final boolean castOnlyInDiag) {
        this.m_castOnlyInDiag = castOnlyInDiag;
    }
    
    public void setTestLineOfSight(final boolean testLineOfSight) {
        this.m_testLineOfSight = testLineOfSight;
    }
    
    public void setTestFreeCell(final boolean testFreeCell) {
        this.m_testFreeCell = testFreeCell;
    }
    
    public void setTestDirectPath(final boolean testDirectPath) {
        this.m_testDirectPath = testDirectPath;
    }
    
    public void setTestNotBorderCell(final boolean testNotBorderCell) {
        this.m_testNotBorderCell = testNotBorderCell;
    }
    
    public void setElementId(final byte elementId) {
        this.m_elementId = elementId;
    }
    
    public void setMaxEffectCap(final byte maxEffectCap) {
        this.m_maxEffectCap = maxEffectCap;
    }
    
    public void setXpGainPercentage(final short xpGainPercentage) {
        this.m_xpGainPercentage = xpGainPercentage;
    }
    
    public void setParentSkillId(final byte parentSkillId) {
        this.m_parentSkillId = parentSkillId;
    }
    
    public void setLearningCriterions(final SimpleCriterion learningCriterions) {
        this.m_learningCriterions = learningCriterions;
    }
    
    public void setTargetFilter(final int targetFilter) {
        this.m_targetFilter = targetFilter;
    }
    
    public void setCastCriterions(final SimpleCriterion castCriterions) {
        this.m_castCriterions = castCriterions;
    }
    
    public void setRangeMinBase(final float rangeMinBase) {
        this.m_rangeMinBase = rangeMinBase;
    }
    
    public void setRangeMinLevelIncrement(final float rangeMinLevelIncrement) {
        this.m_rangeMinLevelIncrement = rangeMinLevelIncrement;
    }
    
    public void setRangeMaxBase(final float rangeMaxBase) {
        this.m_rangeMaxBase = rangeMaxBase;
    }
    
    public void setRangeMaxLevelIncrement(final float rangeMaxLevelIncrement) {
        this.m_rangeMaxLevelIncrement = rangeMaxLevelIncrement;
    }
    
    public void setPassive(final byte passive) {
        this.m_passive = passive;
    }
    
    public void setAssociatedWithItemUse(final boolean associatedWithItemUse) {
        this.m_associatedWithItemUse = associatedWithItemUse;
    }
    
    public void setCanCastWhenCarrying(final boolean canCastWhenCarrying) {
        this.m_canCastWhenCarrying = canCastWhenCarrying;
    }
    
    public void setSpellCastRangeDynamic(final boolean spellCastRangeDynamic) {
        this.m_spellCastRangeDynamic = spellCastRangeDynamic;
    }
    
    public void setCastSpellWillBreakInvisibility(final boolean castSpellWillBreakInvisibility) {
        this.m_castSpellWillBreakInvisibility = castSpellWillBreakInvisibility;
    }
    
    public void setCastOnRandomCell(final boolean castOnRandomCell) {
        this.m_castOnRandomCell = castOnRandomCell;
    }
    
    public byte getSpellType() {
        return this.m_spellType;
    }
    
    public int getScriptId() {
        return this.m_scriptId;
    }
    
    public boolean getUseAutomaticDescription() {
        return this.m_useAutomaticDescription;
    }
    
    public short getUiPosition() {
        return this.m_uiPosition;
    }
    
    public boolean isShowInTimeline() {
        return this.m_showInTimeline;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public void setSpellType(final byte spellType) {
        this.m_spellType = spellType;
    }
    
    public void setScriptId(final int scriptId) {
        this.m_scriptId = scriptId;
    }
    
    public void setUseAutomaticDescription(final boolean useAutomaticDescription) {
        this.m_useAutomaticDescription = useAutomaticDescription;
    }
    
    public void setUiPosition(final short uiPosition) {
        this.m_uiPosition = uiPosition;
    }
    
    public void setShowInTimeline(final boolean showInTimeline) {
        this.m_showInTimeline = showInTimeline;
    }
    
    public void setGfxId(final int gfxId) {
        this.m_gfxId = gfxId;
    }
    
    public boolean isTunnelable() {
        return this.m_tunnelable;
    }
    
    public void setTunnelable(final boolean tunnelable) {
        this.m_tunnelable = tunnelable;
    }
    
    public Map<SimpleCriterion, SpellCastParameters> getAlternativeCastParameters() {
        return this.m_alternativeCastParameters;
    }
    
    public void addAlternativeCastParameters(final SimpleCriterion crit, final SpellCastParameters params) {
        if (this.m_alternativeCastParameters == null) {
            this.m_alternativeCastParameters = new HashMap<SimpleCriterion, SpellCastParameters>();
        }
        this.m_alternativeCastParameters.put(crit, params);
    }
    
    public void addBaseCharacCost(final FighterCharacteristicType charac, final float baseValue, final float increment) {
        if (this.m_baseCastParameters == null) {
            this.m_baseCastParameters = new HashMap<FighterCharacteristicType, CastParameter>(1);
        }
        this.m_baseCastParameters.put(charac, new CastParameter(baseValue, increment));
    }
    
    public Map<FighterCharacteristicType, CastParameter> getBaseCastParameters() {
        return this.m_baseCastParameters;
    }
    
    public boolean isCanCastOnCasterCell() {
        return this.m_canCastOnCasterCell;
    }
    
    public void setCanCastOnCasterCell(final boolean canCastOnCasterCell) {
        this.m_canCastOnCasterCell = canCastOnCasterCell;
    }
    
    public boolean isSramShadowSpell() {
        return this.m_sramShadowSpell;
    }
    
    public void setSramShadowSpell(final boolean sramShadowSpell) {
        this.m_sramShadowSpell = sramShadowSpell;
    }
    
    public ActionOnCriticalMiss getActionOnCriticalMiss() {
        return this.m_actionOnCriticalMiss;
    }
    
    public void setActionOnCriticalMiss(final ActionOnCriticalMiss action) {
        this.m_actionOnCriticalMiss = action;
    }
    
    public float getCastMaxPerTurnIncr() {
        return this.m_castMaxPerTurnIncr;
    }
    
    public void setCastMaxPerTurnIncr(final float castMaxPerTurnIncr) {
        this.m_castMaxPerTurnIncr = castMaxPerTurnIncr;
    }
    
    public void setProperties(final int[] properties) {
        this.m_properties = properties;
    }
    
    public int[] getProperties() {
        return this.m_properties;
    }
}
