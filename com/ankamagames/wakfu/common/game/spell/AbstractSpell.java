package com.ankamagames.wakfu.common.game.spell;

import org.apache.log4j.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;

public abstract class AbstractSpell
{
    protected static final Logger m_logger;
    private final int m_id;
    private final short m_maxLevel;
    private final int m_breedId;
    private final byte m_castMaxPerTarget;
    private final float m_castMaxPerTurn;
    private final float m_castMaxPerTurnIncr;
    private final byte m_castMinInterval;
    private final boolean m_castOnlyInDiag;
    private final boolean m_testFreeCell;
    private final boolean m_testDirectPath;
    private final boolean m_testNotBorderCell;
    private final byte m_elementId;
    private final byte m_maxEffectCap;
    private final short m_xpGainPercentage;
    private final byte m_parentSkillId;
    private final SimpleCriterion m_learningCriterions;
    private final int m_targetFilter;
    private final SimpleCriterion m_castCriterions;
    private final SpellCastParameters m_baseCastParameters;
    private final Map<SimpleCriterion, SpellCastParameters> m_alternativeCastParameters;
    private GrowingArray<WakfuEffect> m_effects;
    private EnumSet<SpellPropertyType> m_properties;
    private boolean m_canBeCritical;
    private byte m_passive;
    private boolean m_associatedWithItemUse;
    private boolean m_canCastWhenCarrying;
    private final boolean m_castSpellWillBreakInvisibility;
    private boolean m_castOnRandomCell;
    private boolean m_tunnelable;
    private boolean m_canCastOnCasterCell;
    private ActionOnCriticalMiss m_actionOnCriticalMiss;
    protected final short m_uiPosition;
    
    protected AbstractSpell(final SpellParameters params) {
        super();
        this.m_effects = new GrowingArray<WakfuEffect>();
        this.m_id = params.getId();
        this.m_maxLevel = params.getMaxLevel();
        this.m_breedId = params.getBreedId();
        this.m_castMaxPerTarget = params.getCastMaxPerTarget();
        this.m_castMaxPerTurn = params.getCastMaxPerTurn();
        this.m_castMaxPerTurnIncr = params.getCastMaxPerTurnIncr();
        this.m_castMinInterval = params.getCastMinInterval();
        this.m_castOnlyInDiag = params.isCastOnlyInDiag();
        this.m_testDirectPath = params.isTestDirectPath();
        this.m_testFreeCell = params.isTestFreeCell();
        this.m_testNotBorderCell = params.isTestNotBorderCell();
        this.m_targetFilter = params.getTargetFilter();
        this.m_castCriterions = params.getCastCriterions();
        this.m_baseCastParameters = new SpellCastParameters();
        final Map<FighterCharacteristicType, CastParameter> baseCastParameters = params.getBaseCastParameters();
        if (baseCastParameters != null) {
            for (final Map.Entry<FighterCharacteristicType, CastParameter> castParameter : baseCastParameters.entrySet()) {
                this.m_baseCastParameters.addCost(castParameter.getKey(), castParameter.getValue().getBaseValue(), castParameter.getValue().getIncrement());
            }
        }
        this.m_alternativeCastParameters = params.getAlternativeCastParameters();
        this.m_baseCastParameters.setRangeMin(params.getRangeMinBase(), params.getRangeMinLevelIncrement());
        this.m_baseCastParameters.setRangeMax(params.getRangeMaxBase(), params.getRangeMaxLevelIncrement());
        this.m_baseCastParameters.setLosAware(params.isTestLineOfSight());
        this.m_baseCastParameters.setOnlyInLine(params.isCastOnlyInLine());
        this.m_baseCastParameters.setRangeDynamic(params.isSpellCastRangeDynamic());
        this.m_maxEffectCap = params.getMaxEffectCap();
        this.m_elementId = params.getElementId();
        this.m_xpGainPercentage = params.getXpGainPercentage();
        this.m_learningCriterions = params.getLearningCriterions();
        this.m_parentSkillId = params.getParentSkillId();
        this.m_passive = params.getPassive();
        this.m_associatedWithItemUse = params.isAssociatedWithItemUse();
        this.m_canCastWhenCarrying = params.isCanCastWhenCarrying();
        this.m_castSpellWillBreakInvisibility = params.isCastSpellWillBreakInvisibility();
        this.m_canBeCritical = false;
        this.m_castOnRandomCell = params.isCastOnRandomCell();
        this.m_tunnelable = params.isTunnelable();
        this.m_canCastOnCasterCell = params.isCanCastOnCasterCell();
        this.m_actionOnCriticalMiss = params.getActionOnCriticalMiss();
        this.m_uiPosition = params.getUiPosition();
        this.addProperties(params.getProperties());
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void addEffect(final WakfuEffect effect) {
        this.m_effects.add(effect);
        if (effect.checkFlags(1L)) {
            this.m_canBeCritical = true;
        }
    }
    
    public Iterator<WakfuEffect> getEffectsForLevel(final short level) {
        return this.getEffectsForLevelAsList(level).iterator();
    }
    
    public ArrayList<WakfuEffect> getEffectsForLevelAsList(final short level) {
        final int size = this.m_effects.size();
        final ArrayList<WakfuEffect> result = new ArrayList<WakfuEffect>(size);
        for (int i = 0; i < size; ++i) {
            final WakfuEffect effect = this.m_effects.get(i);
            if (level >= effect.getContainerMinLevel() && level <= effect.getContainerMaxLevel()) {
                result.add(effect);
            }
        }
        return result;
    }
    
    public boolean canBeCritical() {
        return this.m_canBeCritical;
    }
    
    public int getMaxLevel() {
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
    
    public float getCastMaxPerTurnIncr() {
        return this.m_castMaxPerTurnIncr;
    }
    
    public byte getCastMinInterval() {
        return this.m_castMinInterval;
    }
    
    public boolean isTestLineOfSight(final AbstractSpellLevel spellLevel, final Object user, final Object target, final Object context) {
        final SpellCastParameters castParameters = this.getSpellCastParameters(user, target, spellLevel, context);
        return castParameters.isLosAware();
    }
    
    public boolean isCastOnlyInLine(final AbstractSpellLevel spellLevel, final Object user, final Object target, final Object context) {
        final SpellCastParameters castParameters = this.getSpellCastParameters(user, target, spellLevel, context);
        return castParameters.isOnlyInLine();
    }
    
    public boolean isSpellCastRangeDynamic(final AbstractSpellLevel spellLevel, final Object user, final Object target, final Object context) {
        final SpellCastParameters castParameters = this.getSpellCastParameters(user, target, spellLevel, context);
        return castParameters.isRangeDynamic();
    }
    
    public boolean hasToTestDirectPath() {
        return this.m_testDirectPath;
    }
    
    public boolean hasToTestFreeCell() {
        return this.m_testFreeCell;
    }
    
    public boolean hasToTestNotBorderCell() {
        return this.m_testNotBorderCell;
    }
    
    public int getTargetFilter() {
        return this.m_targetFilter;
    }
    
    public SimpleCriterion getCastCriterions() {
        return this.m_castCriterions;
    }
    
    public byte getElementId() {
        return this.m_elementId;
    }
    
    public byte getMaxEffectCap() {
        return this.m_maxEffectCap;
    }
    
    public SpellCost getSpellCost(final AbstractSpellLevel spellLevel, final BasicCharacterInfo user, final Object target, final Object context) {
        final SpellCastParameters castParameters = this.getSpellCastParameters(user, target, spellLevel, context);
        final SpellCostModification spellCostModification = user.getSpellCostModification();
        final SpellCost res = new SpellCost();
        final Map<FighterCharacteristicType, CastParameter> parameters = castParameters.getCost();
        final Set<FighterCharacteristicType> characs = parameters.keySet();
        final short level = spellLevel.getLevel();
        for (final FighterCharacteristicType charac : characs) {
            final CastParameter castParameter = parameters.get(charac);
            final byte cost = castParameter.getCost(level);
            int modificator = 0;
            if (this.m_properties != null) {
                for (final SpellPropertyType property : this.m_properties) {
                    modificator += spellCostModification.getModification(charac, property);
                }
            }
            res.addCost(charac.getId(), (byte)Math.max(0, cost + modificator));
        }
        return res;
    }
    
    public byte getActionPoints(final int spellLevel) {
        if (spellLevel < 0 || spellLevel > this.m_maxLevel) {
            throw new IllegalArgumentException("Level invalide : " + spellLevel + " demand\u00e9, max=" + this.m_maxLevel);
        }
        final SpellCastParameters castParameters = this.m_baseCastParameters;
        return castParameters.getCost(FighterCharacteristicType.AP, spellLevel);
    }
    
    public byte getActionPoints(final AbstractSpellLevel spellLevel, final BasicCharacterInfo user, final Object target, final Object context) {
        if (spellLevel == null) {
            throw new IllegalArgumentException("SpellLevel null");
        }
        final short level = spellLevel.getLevel();
        if (level < 0 || level > this.m_maxLevel) {
            throw new IllegalArgumentException("Level invalide : " + spellLevel + " demand\u00e9, max=" + this.m_maxLevel);
        }
        final SpellCost spellCost = this.getSpellCost(spellLevel, user, target, context);
        return spellCost.getCharacCost(FighterCharacteristicType.AP);
    }
    
    public byte getWakfuPoints(final int spellLevel) {
        if (spellLevel < 0 || spellLevel > this.m_maxLevel) {
            throw new IllegalArgumentException("Level invalide : " + spellLevel + " demand\u00e9, max=" + this.m_maxLevel);
        }
        return this.m_baseCastParameters.getCost(FighterCharacteristicType.WP, spellLevel);
    }
    
    public byte getWakfuPoints(final AbstractSpellLevel spellLevel, final Object user, final Object target, final Object context) {
        if (spellLevel == null) {
            throw new IllegalArgumentException("SpellLevel null");
        }
        final short level = spellLevel.getLevel();
        if (level < 0 || level > this.m_maxLevel) {
            throw new IllegalArgumentException("Level invalide : " + spellLevel + " demand\u00e9, max=" + this.m_maxLevel);
        }
        final SpellCastParameters castParameters = this.getCastParameters(user, target, spellLevel, context);
        return castParameters.getCost(FighterCharacteristicType.WP, level);
    }
    
    public byte getMovementPoints(final int spellLevel) {
        if (spellLevel < 0 || spellLevel > this.m_maxLevel) {
            throw new IllegalArgumentException("Level invalide : " + spellLevel + " demand\u00e9, max=" + this.m_maxLevel);
        }
        return this.m_baseCastParameters.getCost(FighterCharacteristicType.MP, spellLevel);
    }
    
    public byte getMovementPoints(final AbstractSpellLevel spellLevel, final Object user, final Object target, final Object context) {
        final SpellCastParameters castParameters = this.getSpellCastParameters(user, target, spellLevel, context);
        final short level = spellLevel.getLevel();
        return castParameters.getCost(FighterCharacteristicType.MP, level);
    }
    
    public int getRangeMax(final int spellLevel) {
        if (spellLevel < 0 || spellLevel > this.m_maxLevel) {
            throw new IllegalArgumentException("Level invalide : " + spellLevel + " demand\u00e9, max=" + this.m_maxLevel);
        }
        return this.m_baseCastParameters.getRangeMax().getCost(spellLevel);
    }
    
    public int getRangeMax(final AbstractSpellLevel spellLevel, final BasicCharacterInfo user, final Object target, final Object context) {
        final SpellCastParameters castParameters = this.getSpellCastParameters(user, target, spellLevel, context);
        final short level = spellLevel.getLevel();
        final byte baseCost = castParameters.getRangeMax().getCost(level);
        final SpellCostModification spellCostModification = user.getSpellCostModification();
        int modificator = 0;
        if (this.m_properties != null) {
            for (final SpellPropertyType property : this.m_properties) {
                modificator += spellCostModification.getRangeMaxModification(property);
            }
        }
        return baseCost + modificator;
    }
    
    public int getRangeMin(final AbstractSpellLevel spellLevel, final BasicCharacterInfo user, final Object target, final Object context) {
        final SpellCastParameters castParameters = this.getSpellCastParameters(user, target, spellLevel, context);
        final short level = spellLevel.getLevel();
        return castParameters.getRangeMin().getCost(level);
    }
    
    public short getXpGainPercentage() {
        return this.m_xpGainPercentage;
    }
    
    public SimpleCriterion getLearningCriterions() {
        return this.m_learningCriterions;
    }
    
    public byte getParentSkillId() {
        return this.m_parentSkillId;
    }
    
    public boolean isPassive() {
        return this.m_passive != PassivityType.ACTIVE.getId();
    }
    
    public boolean isPassiveEnabledFromStart() {
        return this.m_passive == PassivityType.PASSIVE_FROM_0.getId();
    }
    
    public boolean isAssociatedWithItemUse() {
        return this.m_associatedWithItemUse;
    }
    
    public byte getAssociatedItemPosition() {
        return EquipmentPosition.FIRST_WEAPON.getId();
    }
    
    public boolean canBeCastedWhenCarrying() {
        return this.m_canCastWhenCarrying;
    }
    
    public boolean isCastSpellWillBreakInvisibility() {
        return this.m_castSpellWillBreakInvisibility;
    }
    
    public boolean isCastOnlyInDiag() {
        return this.m_castOnlyInDiag;
    }
    
    public boolean isCastOnRandomCell() {
        return this.m_castOnRandomCell;
    }
    
    public boolean isTunnelable() {
        return this.m_tunnelable;
    }
    
    public boolean isCanCastOnCasterCell() {
        return this.m_canCastOnCasterCell;
    }
    
    public ActionOnCriticalMiss getActionOnCriticalMiss() {
        return this.m_actionOnCriticalMiss;
    }
    
    public short getUiPosition() {
        return this.m_uiPosition;
    }
    
    public final void addProperties(final int... effectProperties) {
        if (effectProperties == null) {
            return;
        }
        for (int i = 0; i < effectProperties.length; ++i) {
            final int id = effectProperties[i];
            final SpellPropertyType property = SpellPropertyType.getPropertyFromId(id);
            this.addProperty(property);
        }
    }
    
    public final void addProperty(final SpellPropertyType property) {
        if (property == null) {
            return;
        }
        if (this.m_properties == null) {
            this.m_properties = EnumSet.noneOf(SpellPropertyType.class);
        }
        this.m_properties.add(property);
    }
    
    public final boolean hasProperty(final SpellPropertyType property) {
        return this.m_properties != null && this.m_properties.contains(property);
    }
    
    private SpellCastParameters getSpellCastParameters(final Object user, final Object target, final AbstractSpellLevel spellLevel, final Object context) {
        if (spellLevel == null) {
            throw new IllegalArgumentException("SpellLevel null");
        }
        final short level = spellLevel.getLevel();
        if (level < 0 || level > this.m_maxLevel) {
            throw new IllegalArgumentException("Level invalide : " + level + " demand\u00e9, max=" + this.m_maxLevel);
        }
        return this.getCastParameters(user, target, spellLevel, context);
    }
    
    private SpellCastParameters getCastParameters(final Object user, final Object target, final Object content, final Object context) {
        SpellCastParameters castParameters = this.m_baseCastParameters;
        if (this.m_alternativeCastParameters != null && !this.m_alternativeCastParameters.isEmpty()) {
            final Set<SimpleCriterion> criterions = this.m_alternativeCastParameters.keySet();
            for (final SimpleCriterion criterion : criterions) {
                if (criterion.isValid(user, target, content, context)) {
                    castParameters = this.m_alternativeCastParameters.get(criterion);
                }
            }
        }
        return castParameters;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractSpell.class);
    }
}
