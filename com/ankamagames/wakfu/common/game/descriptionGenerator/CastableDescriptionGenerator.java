package com.ankamagames.wakfu.common.game.descriptionGenerator;

import java.util.regex.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.core.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.delegate.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.framework.text.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effect.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter.*;

public class CastableDescriptionGenerator
{
    public static final String CRAFT_TAG = "craft";
    public static final String ELEMENT_KEY = "(\\[(el)([0-9]?)\\])";
    public static final String STATE_KEY = "(\\[(st)([0-9]+)\\])";
    public static final String AOE_KEY = "(\\[(ar)([0-9]+)\\])";
    public static final Pattern SPELL_PATTERN;
    public static final Pattern EFFECT_PATTERN;
    public static final String FIGHTER_TARGET_TAG = "fighter";
    public static final String SHIELD_TARGET_TAG = "shield";
    public static final String GLYPH_TARGET_TAG = "glyph";
    public static final String DRAKE_TARGET_TAG = "drake";
    public static final String PAW_TARGET_TAG = "paw";
    public static final String TIQUE_TAG = "tique";
    public static final String TAQUE_TAG = "taque";
    public static final String BACKSTAB_TAG = "backstab";
    public static final String SIDESTAB_TAG = "sidestab";
    public static final String INVISIBLE_TAG = "invisible";
    public static final String BARREL_TAG = "barrel";
    public static final String ECA_LUCKY_TAG = "lucky";
    public static final String TOTEM_TAG = "totem";
    public static final String PUPPET_TAG = "puppet";
    public static final String ENEMY_TAG = "enemy";
    public static final String ALLY_TAG = "ally";
    public static final String SEED_TAG = "seed";
    public static final String CHROMATIC_TAG = "chromatic";
    public static final String CASTER_TAG = "caster";
    public static final String SEREIN_TAG = "serein";
    public static final String EXALTE_TAG = "exalte";
    public static final Pattern ICONS_PATTERN;
    public static final Pattern LISTED_CONTAINER_PATTERN;
    public static final Pattern EFFECT_GROUP_PATTERN;
    public static final Pattern EFFECT_GROUP_NUM_REG_PATTERN;
    public static final char PLUS_TAG = '+';
    public static final char MINUS_TAG = '-';
    public static final char MULTIPLE_TAG = '*';
    public static final char DIVIDE_TAG = '/';
    public static final String OPERAND = "([\\+\\-\\*\\/])([0-9]+([,\\.][0-9]+)?)";
    public static final Pattern OPERAND_REG_PATTERN;
    public static final Pattern ARITHMETIC_REG_PATTERN;
    public static final Pattern ELEMENT_PATTERN;
    public static final Pattern STATE_PATTERN;
    public static final Pattern AOE_PATTERN;
    public static final Pattern AOE_SHAPE_PATTERN;
    public static final int APPLY_DEATHTAG_ACTION_ID;
    public static final int APPLY_FECA_ARMOR_ACTION_ID;
    public static final int STATE_ACTION_ID;
    public static final int UNAPPLY_STATE_ACTION_ID;
    public static final int STATE_RESIST_ACTION_ID;
    public static final int STATE_APPLICATION_BONUS_ACTION_ID;
    public static final int APPLY_STATE_PERCENT_FUNCTION_AREA_HP_ID;
    public static final int BLITZKRIEK_ACTION_ID;
    public static final int SET_BOMB_ID;
    public static final int SET_FECA_GLYPH;
    public static final int SET_CADRAN;
    public static final int SET_GLYPH;
    private static final Logger m_logger;
    public static final int ERROR_DESCRIPTOR_RESULT = -1;
    public static final int NO_ERROR_DESCRIPTOR_RESULT = 0;
    public static final String TABULATION_STRING = "    ";
    public static String SUB_EFFECT_CARAC_PREFIX;
    public static String PLOT_URL;
    public static GameContentTranslator m_translator;
    public static TextWidgetFormatterFactory m_twfFactory;
    public static CastableDescriptionGeneratorUtilityDelegate m_utilityDelegate;
    public static String CRITICAL;
    public static String REQUIREMENTS;
    public static String HP_VAR_BEACON;
    public static String HP_VAR_BARREL;
    public static String LEVEL_SHORT;
    public static String LEVEL_SHORT_CUMULABLE;
    public static String WISDOM;
    public static String AGILITY;
    public static String STRENGTH;
    public static String LUCK;
    public static String INTELLIGENCE;
    public static String VITALITY;
    public static String MAX_SHORT;
    public static String LEVEL;
    public static int EFFECT_DESCRIPTION_TRANSLATION_TYPE;
    public static int EFFECT_CUSTOM_DESCRIPTION_TRANSLATION_TYPE;
    public static int AREA_NAME_TRANSLATION_TYPE;
    public static int SPELL_FREE_DESCRIPTION_TRANSLATION_TYPE;
    public static final TIntObjectHashMap<EffectWriter> m_effectWriters;
    
    public static ArrayList<String> generateDescription(final ContainerWriter writer) {
        return writer.writeContainer();
    }
    
    public static String describeSubContainerDescription(State state, final short stateLevel, final boolean spellPassive, final int freeDescriptionTranslationType, final boolean withTabs, final boolean withPlots) {
        if (state.getLevel() != stateLevel) {
            state = state.instanceAnother(stateLevel);
        }
        final StateWriter stateWriter = new StateWriter(state, DescriptionMode.EFFECTS_ONLY, freeDescriptionTranslationType);
        final ArrayList<String> stateEffectsDescription = generateDescription(stateWriter);
        if (stateEffectsDescription != null && stateEffectsDescription.size() != 0) {
            final TextWidgetFormater twf = CastableDescriptionGenerator.m_twfFactory.createNew();
            if (withTabs && !spellPassive) {
                twf.addColor("8888ff");
            }
            twf.append((withTabs ? "\n" : "") + mergeSubDesc(stateEffectsDescription, withPlots));
            return twf.finishAndToString();
        }
        return null;
    }
    
    public static String mergeSubDesc(final ArrayList<String> splittedSubDesc, final boolean withPlots) {
        String mergedDesc = "";
        boolean first = true;
        for (final String auraEffect : splittedSubDesc) {
            if (!first) {
                mergedDesc += "\n";
            }
            if (withPlots) {
                mergedDesc += (auraEffect.contains(CastableDescriptionGenerator.PLOT_URL) ? "    " : CastableDescriptionGenerator.SUB_EFFECT_CARAC_PREFIX);
            }
            mergedDesc += auraEffect;
            first = false;
        }
        return mergedDesc;
    }
    
    public static String addElement(final WakfuEffect effect) {
        assert effect != null : "Effect null !";
        final WakfuRunningEffect runningEffect = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId());
        if (runningEffect == null) {
            CastableDescriptionGenerator.m_logger.error((Object)("impossible de trouver de RunningEffect associ\u00e9 \u00e0 l'actionId " + effect.getActionId()));
            return "";
        }
        final Elements element = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId()).getElement();
        return addElement(element);
    }
    
    public static String addElement(final Elements element) {
        if (element != null && element != Elements.PHYSICAL && element != Elements.SUPPORT) {
            return CastableDescriptionGenerator.m_utilityDelegate.getElementIcon(CastableDescriptionGenerator.m_twfFactory.createNew(), element).finishAndToString();
        }
        return "";
    }
    
    public static String getDuration(final WakfuEffect effect, final short level, final State state, final int stateLevel) {
        if (effect.getEffectType() == 2) {
            final WakfuFightEffect feffect = (WakfuFightEffect)effect;
            int turn = feffect.getDuration(level).getTableTurnsFromNow();
            if (state != null) {
                turn = state.getFightDuration((short)stateLevel).getTableTurnsFromNow();
            }
            if (turn == -1) {
                return CastableDescriptionGenerator.m_utilityDelegate.getInfiniteDurationString(CastableDescriptionGenerator.m_translator);
            }
            if (turn > 0) {
                return CastableDescriptionGenerator.m_utilityDelegate.getRemainingDurationString(CastableDescriptionGenerator.m_translator, turn);
            }
        }
        return "";
    }
    
    public static String getStateNameLink(final State currentState, final short stateLevel, final short stateMaxLevel, final boolean displayLevel) {
        final TextWidgetFormater stateSb = CastableDescriptionGenerator.m_twfFactory.createNew();
        CastableDescriptionGenerator.m_utilityDelegate.formatStateName(stateSb, currentState, stateLevel);
        if (stateMaxLevel > 0 && displayLevel) {
            final State state = StateManager.getInstance().getState(currentState.getStateBaseId());
            stateSb.append(" (" + CastableDescriptionGenerator.m_translator.getString(state.isCumulable() ? CastableDescriptionGenerator.LEVEL_SHORT_CUMULABLE : CastableDescriptionGenerator.LEVEL_SHORT, Math.min(state.getMaxlevel(), stateLevel)) + ')');
        }
        return stateSb.finishAndToString();
    }
    
    public static String getEffectAreaNameLink(final AbstractEffectArea abstractEffectArea, final short level, final boolean displayLevel) {
        final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
        CastableDescriptionGenerator.m_utilityDelegate.formatGlyphName(sb, abstractEffectArea, level);
        if (displayLevel) {
            sb.append(" (" + CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.LEVEL_SHORT, Math.min(abstractEffectArea.getMaxLevel(), level)) + ')');
        }
        return sb.finishAndToString();
    }
    
    public static EffectWriter getEffectWriter(final int actionId) {
        final EffectWriter effectWriter = CastableDescriptionGenerator.m_effectWriters.get(actionId);
        return (effectWriter != null) ? effectWriter : CastableDescriptionGenerator.m_effectWriters.get(-1);
    }
    
    static {
        SPELL_PATTERN = Pattern.compile("\\[@(el|mr|Mr|ap|mp|wp|ch|lv)\\]");
        EFFECT_PATTERN = Pattern.compile("\\[(pr|se|pl|ae)\\]");
        ICONS_PATTERN = Pattern.compile("\\[(fighter|shield|glyph|paw|taque|tique|backstab|sidestab|invisible|barrel|lucky|totem|puppet|enemy|ally|caster|serein|exalte|seed|chromatic|drake)\\]");
        LISTED_CONTAINER_PATTERN = Pattern.compile("\\[(li|lp)\\]");
        EFFECT_GROUP_PATTERN = Pattern.compile("(\\[(\\$[0-9]+)+(ef|ae|el|pr|st|li|lp|(#[0-9]+))\\])");
        EFFECT_GROUP_NUM_REG_PATTERN = Pattern.compile("(\\$[0-9]+|ef|ae|el|pr|st|li|lp|#[0-9]+)");
        OPERAND_REG_PATTERN = Pattern.compile("([\\+\\-\\*\\/])([0-9]+([,\\.][0-9]+)?)");
        ARITHMETIC_REG_PATTERN = Pattern.compile("\\|([0-9]d)?([0-9]+([,\\.][0-9]+)?)([^|]*)\\|");
        ELEMENT_PATTERN = Pattern.compile("(\\[(el)([0-9]?)\\])");
        STATE_PATTERN = Pattern.compile("(\\[(st)([0-9]+)\\])");
        AOE_PATTERN = Pattern.compile("(\\[(ar)([0-9]+)\\])");
        APPLY_DEATHTAG_ACTION_ID = RunningEffectConstants.APPLY_DEATHTAG.getId();
        APPLY_FECA_ARMOR_ACTION_ID = RunningEffectConstants.APPLY_STATE_FOR_FECA_ARMOR.getId();
        STATE_ACTION_ID = RunningEffectConstants.STATE_APPLY.getId();
        UNAPPLY_STATE_ACTION_ID = RunningEffectConstants.STATE_FORCE_UNAPPLY.getId();
        STATE_RESIST_ACTION_ID = RunningEffectConstants.STATE_RESISTANCE.getId();
        STATE_APPLICATION_BONUS_ACTION_ID = RunningEffectConstants.STATE_APPLICATION_BONUS.getId();
        APPLY_STATE_PERCENT_FUNCTION_AREA_HP_ID = RunningEffectConstants.APPLY_STATE_PERCENT_FUNCTION_AREA_HP.getId();
        BLITZKRIEK_ACTION_ID = RunningEffectConstants.BLITZKRIEK_EFFECT.getId();
        SET_BOMB_ID = RunningEffectConstants.SET_BOMB.getId();
        SET_FECA_GLYPH = RunningEffectConstants.SET_FECA_GLYPH.getId();
        SET_CADRAN = RunningEffectConstants.SET_CADRAN.getId();
        SET_GLYPH = RunningEffectConstants.SET_GLYPH.getId();
        m_logger = Logger.getLogger((Class)CastableDescriptionGenerator.class);
        CastableDescriptionGenerator.SUB_EFFECT_CARAC_PREFIX = "";
        CastableDescriptionGenerator.PLOT_URL = "";
        m_effectWriters = new TIntObjectHashMap<EffectWriter>();
        String regex = "\\[(";
        for (final AreaOfEffectShape areaOfEffectShape : AreaOfEffectShape.values()) {
            if (!areaOfEffectShape.equals(AreaOfEffectShape.values()[0])) {
                regex += "|";
            }
            regex += areaOfEffectShape.name();
        }
        regex += ")\\]";
        AOE_SHAPE_PATTERN = Pattern.compile(regex);
        CastableDescriptionGenerator.m_effectWriters.put(-1, new DefaultEffectWriter());
        final EffectAreaEffectWriter effectAreaEffectWriter = new EffectAreaEffectWriter();
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_BARREL.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_BEACON.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_TRAP.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_GLYPH.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_AURA.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_AURA_ON_TARGET.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_LOOT_EFFECT_AREA.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_HOUR.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_CADRAN.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_EFFECT_AREA.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_FECA_GLYPH.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_BOMB.getId(), effectAreaEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SET_AURA_ON_TARGET.getId(), effectAreaEffectWriter);
        final EffectGroupEffectWriter effectGroupEffectWriter = new EffectGroupEffectWriter();
        RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.forEach(new TIntProcedure() {
            @Override
            public boolean execute(final int value) {
                if (RunningEffectGroupType.WITHOUT_EFFECT_GROUP_DESCRIPTION.contains(value)) {
                    return true;
                }
                CastableDescriptionGenerator.m_effectWriters.put(value, effectGroupEffectWriter);
                return true;
            }
        });
        final SpellBoostEffectWriter spellBoostEffectWriter = new SpellBoostEffectWriter();
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SPELL_BOOST_LEVEL.getId(), spellBoostEffectWriter);
        final EffectWriter spellWithPropertyModificationEffectWriter = new SpellWithPropertyModificationEffectWriter();
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SPELL_WITH_PROPERTY_AP_COST_REDUCTION.getId(), spellWithPropertyModificationEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SPELL_WITH_PROPERTY_RANGE_GAIN.getId(), spellWithPropertyModificationEffectWriter);
        final ElementalSpellGainEffectWriter elementalSpellGainEffectWriter = new ElementalSpellGainEffectWriter();
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.ELEMENT_SPELL_GAIN.getId(), elementalSpellGainEffectWriter);
        final StateEffectWriter stateEffectWriter = new StateEffectWriter();
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.STATE_APPLY.getId(), stateEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.STATE_FORCE_UNAPPLY.getId(), stateEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.STATE_RESISTANCE.getId(), stateEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.STATE_APPLICATION_BONUS.getId(), stateEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.APPLY_DEATHTAG.getId(), stateEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.APPLY_STATE_FOR_FECA_ARMOR.getId(), stateEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.APPLY_STATE_PERCENT_FUNCTION_AREA_HP.getId(), stateEffectWriter);
        final SummonEffectWriter summonEffectWriter = new SummonEffectWriter();
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.SUMMON.getId(), summonEffectWriter);
        final DefaultEffectWriter exponentialParametersEffectWriter = new DefaultEffectWriter(ExponentialEffectParametersComputer.INSTANCE);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.HP_LOSS_STASIS_EXPONENTIAL_GROWTH.getId(), exponentialParametersEffectWriter);
        final VariableElementsEffectWriter variableElementsEffectWriter = new VariableElementsEffectWriter();
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.VARIABLE_ELEMENTS_DAMAGE_GAIN.getId(), variableElementsEffectWriter);
        CastableDescriptionGenerator.m_effectWriters.put(RunningEffectConstants.VARIABLE_ELEMENTS_RES_GAIN.getId(), variableElementsEffectWriter);
    }
    
    public enum DescriptionMode
    {
        ALL, 
        EFFECTS_ONLY, 
        CRITICALS_ONLY;
    }
}
