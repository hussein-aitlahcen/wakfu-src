package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class SpellLevel extends AbstractSpellLevel<Spell> implements FieldProvider, Comparable
{
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String LEVEL_FIELD = "level";
    public static final String MAX_LEVEL_FIELD = "maxLevel";
    public static final String LEVEL_TEXT_SHORT_FIELD = "levelTextShort";
    public static final String XP_FIELD = "xp";
    public static final String CURRENT_LEVEL_STRING_FIELD = "currentLevelString";
    public static final String CURRENT_LEVEL_PERCENTAGE_FIELD = "currentLevelPercentage";
    public static final String CURRENT_LEVEL_PERCENTAGE_STRING_FIELD = "currentLevelPercentageString";
    public static final String SPELL_FIELD = "spell";
    public static final String AP_FIELD = "ap";
    public static final String MP_FIELD = "mp";
    public static final String WP_FIELD = "wp";
    public static final String CHRAGE_FIELD = "chrage";
    public static final String RANGE_FIELD = "range";
    public static final String AREA_OF_EFFECT_ICON_URL_FIELD = "areaOfEffectIconURL";
    public static final String AREA_OF_EFFECT_BIG_ICON_URL_FIELD = "areaOfEffectBigIconURL";
    public static final String ELEMENT_USED_ICON_URL_FIELD = "elementsUsedIconURL";
    public static final String SMALL_ICON_URL_FIELD = "smallIconUrl";
    public static final String BIG_ICON_URL_FIELD = "bigIconUrl";
    public static final String LONG_DESCRIPTION_FIELD = "longDescription";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String SHORT_DESCRIPTION_FIELD = "shortDescription";
    public static final String CRITICAL_DESCRIPTION_FIELD = "criticalDescription";
    public static final String SHORT_DESCRIPTION_ELEMENT_FIELD = "shortDescriptionElement";
    public static final String USABLE_FIELD = "usable";
    public static final String HAS_CAST_INTERVAL_RESTRICTIONS_FIELD = "hasCastIntervalRestrictions";
    public static final String CAST_INTERVAL_RESTRICTIONS_DESCRIPTION_FIELD = "castIntervalRestrictionsDescription";
    public static final String CAST_MAX_PER_TARGET_FIELD = "castMaxPerTarget";
    public static final String CAST_MAX_PER_TURN_FIELD = "castMaxPerTurn";
    public static final String CAST_INTERVAL_FIELD = "castInterval";
    public static final String TEST_LINE_OF_SIGHT_FIELD = "testLineOfSight";
    public static final String CAST_ONLY_IN_LINE_FIELD = "castOnlyInLine";
    public static final String TEST_FREE_CELL_FIELD = "testFreeCell";
    public static final String TEST_NOT_BORDER_CELL_FIELD = "testNotBorderCell";
    public static final String CONDITIONS_DESCRIPTION_FIELD = "conditionsDescription";
    public static final String WEAPONS_SKILLS_LIST_FIELD = "weaponSkillsList";
    public static final String IS_MONSTER_SPELL_FIELD = "isMonsterSpell";
    public static final String HAS_CRITICAL_EFFECT_FIELD = "hasCriticalEffect";
    public static final String IS_SUPPORT_FIELD = "isSupport";
    public static final String IS_PASSIVE_FIELD = "isPassive";
    public static final String MODIFIABLE_RANGE_FIELD = "modifiableRange";
    public static final String IS_LOCKED_FIELD = "isLocked";
    public static final String[] FIELDS;
    private boolean m_criterionOk;
    
    public void setCriterionOk(final boolean criterionOk) {
        this.m_criterionOk = criterionOk;
    }
    
    public SpellLevel() {
        super();
        this.m_criterionOk = true;
    }
    
    public SpellLevel(final Spell spell, final short level, final long uid) {
        super();
        this.m_criterionOk = true;
        this.m_spell = (Spell)spell;
        this.setLevelAndXp(level, 0L);
        this.m_uid = uid;
    }
    
    @Override
    public byte getType() {
        return 1;
    }
    
    @Override
    public AbstractSpellManager<Spell> getSpellManager() {
        return SpellManager.getInstance();
    }
    
    public boolean isInShortcutBar() {
        return WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager().isInShortcutBarWithReferenceId(this.getReferenceId());
    }
    
    public SpellLevel getCopy(final boolean pooled, final boolean convertLevelGainAsPlainLevel) {
        final SpellLevel level = new SpellLevel();
        level.m_spell = this.m_spell;
        level.m_levelGain = (convertLevelGainAsPlainLevel ? 0 : this.m_levelGain);
        level.setLevelAndXp((short)(this.getLevel() - level.m_levelGain), this.getXp());
        level.m_uid = GUIDGenerator.getGUID();
        return level;
    }
    
    @Override
    public SpellLevel getCopy(final boolean pooled) {
        return this.getCopy(pooled, false);
    }
    
    @Override
    public SpellLevel getClone() {
        final SpellLevel level = new SpellLevel();
        level.m_spell = this.m_spell;
        level.setLevelAndXp(this.getLevel(), this.getXp());
        level.m_uid = this.m_uid;
        return level;
    }
    
    public int getGfxId() {
        return ((Spell)this.m_spell).getGfxId();
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public String[] getFields() {
        return SpellLevel.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        final LocalPlayerCharacter lp = WakfuGameEntity.getInstance().getLocalPlayer();
        if (fieldName.equals("id")) {
            return ((Spell)this.m_spell).getId();
        }
        if (fieldName.equals("levelTextShort")) {
            return WakfuTranslator.getInstance().getString("levelShort.custom", this.getLevel());
        }
        if (fieldName.equals("maxLevel")) {
            return this.getMaxLevel();
        }
        if (fieldName.equals("level")) {
            return this.getLevel();
        }
        if (fieldName.equals("xp")) {
            return this.getXp();
        }
        if (fieldName.equals("currentLevelString")) {
            return WakfuTranslator.getInstance().getString("xpRatio", this.getXpTable().getXpInLevel(this.getXp()), this.getXpTable().getLevelExtent(this.getLevelWithoutGain()));
        }
        if (fieldName.equals("currentLevelPercentage")) {
            return this.getCurrentLevelPercentage();
        }
        if (fieldName.equals("currentLevelPercentageString")) {
            return String.format("%.2f%%", this.getCurrentLevelPercentage() * 100.0f);
        }
        if (fieldName.equals("spell")) {
            return this.m_spell;
        }
        if (fieldName.equals("smallIconUrl")) {
            return WakfuConfiguration.getInstance().getSpellSmallIcon(this.getGfxId());
        }
        if (fieldName.equals("bigIconUrl")) {
            return WakfuConfiguration.getInstance().getSpellBigIcon(this.getGfxId());
        }
        if (fieldName.equals("name")) {
            if (this.m_spell != null) {
                return ((Spell)this.m_spell).getName();
            }
        }
        else if (fieldName.equals("ap")) {
            if (this.m_spell != null) {
                int apCost;
                if (((Spell)this.m_spell).isAssociatedWithItemUse()) {
                    final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                    if (player.getEquipmentInventory() != null && player.getEquipmentInventory() != null && ((ArrayInventoryWithoutCheck<Item, R>)player.getEquipmentInventory()).getFromPosition(EquipmentPosition.FIRST_WEAPON.m_id) != null) {
                        final Item weapon = ((ArrayInventoryWithoutCheck<Item, R>)player.getEquipmentInventory()).getFromPosition(EquipmentPosition.FIRST_WEAPON.m_id);
                        apCost = ((Spell)this.m_spell).getActionPoints(this, lp, null, lp.getEffectContext()) + weapon.getReferenceItem().getActionPoints();
                    }
                    else {
                        apCost = ((Spell)this.m_spell).getActionPoints(this, lp, null, lp.getEffectContext());
                    }
                }
                else {
                    apCost = ((Spell)this.m_spell).getActionPoints(this, lp, null, lp.getEffectContext());
                }
                final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                if (player != null && apCost > 0) {
                    if (player.isActiveProperty(FightPropertyType.GROGGY_2)) {
                        apCost += 2;
                    }
                    else if (player.isActiveProperty(FightPropertyType.GROGGY_1)) {
                        ++apCost;
                    }
                }
                return apCost;
            }
        }
        else if (fieldName.equals("mp")) {
            if (this.m_spell != null) {
                byte mpCost = ((Spell)this.m_spell).getMovementPoints(this, lp, null, lp.getEffectContext());
                final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                if (player != null && mpCost > 0) {
                    if (player.isActiveProperty(FightPropertyType.CRIPPLED_2)) {
                        mpCost += 2;
                    }
                    else if (player.isActiveProperty(FightPropertyType.CRIPPLED_1)) {
                        ++mpCost;
                    }
                }
                return mpCost;
            }
        }
        else if (fieldName.equals("wp")) {
            if (this.m_spell != null) {
                final LocalPlayerCharacter player2 = WakfuGameEntity.getInstance().getLocalPlayer();
                int wpCost = ((Spell)this.m_spell).getWakfuPoints(this, lp, null, lp.getEffectContext());
                if (player2 != null && wpCost > 0) {
                    if (player2.isActiveProperty(FightPropertyType.STASIS_2)) {
                        wpCost += 2;
                    }
                    else if (player2.isActiveProperty(FightPropertyType.STASIS_1)) {
                        ++wpCost;
                    }
                }
                return wpCost;
            }
        }
        else if (fieldName.equals("chrage")) {
            if (this.m_spell != null) {
                final SpellCost chrageCost = ((Spell)this.m_spell).getSpellCost(this, lp, null, lp.getEffectContext());
                return chrageCost.getCharacCost(FighterCharacteristicType.CHRAGE);
            }
        }
        else if (fieldName.equals("range")) {
            if (this.m_spell == null || this.getType() == 2) {
                return null;
            }
            final int min = this.getMinRange();
            final int max = this.getMaxRange();
            if ((min > 0 || max > 0) && min != max) {
                final StringBuilder sb = new StringBuilder();
                sb.append(min).append("-").append(max);
                return sb.toString();
            }
            return max;
        }
        else if (fieldName.equals("areaOfEffectIconURL")) {
            if (this.m_spell != null) {
                for (final WakfuEffect wakfuEffect : this) {
                    final AreaOfEffectShape effectShape = wakfuEffect.getAreaOfEffect().getShape();
                    if (effectShape == AreaOfEffectShape.POINT || effectShape == AreaOfEffectShape.SPECIAL) {
                        return null;
                    }
                    final String string = effectShape.toString();
                    try {
                        return String.format(WakfuConfiguration.getInstance().getString("areasIconsPath"), string);
                    }
                    catch (Exception e2) {
                        SpellLevel.m_logger.error((Object)("Impossible de trouver l'url de la zone " + string));
                        continue;
                    }
                    break;
                }
            }
        }
        else if (fieldName.equals("areaOfEffectBigIconURL")) {
            if (this.m_spell != null) {
                for (final WakfuEffect wakfuEffect : this) {
                    final String string2 = wakfuEffect.getAreaOfEffect().getShape().toString();
                    try {
                        return String.format(WakfuConfiguration.getInstance().getString("areasBigIconsPath"), string2);
                    }
                    catch (Exception e3) {
                        SpellLevel.m_logger.error((Object)("Impossible de trouver l'url de la zone " + string2));
                        continue;
                    }
                    break;
                }
            }
        }
        else if (fieldName.equals("elementsUsedIconURL")) {
            if (this.m_spell != null) {
                final Elements element = Elements.getElementFromId(((Spell)this.m_spell).getElementId());
                String result = "";
                if (element != null) {
                    try {
                        result = String.format(WakfuConfiguration.getInstance().getString("elementsIconsPath"), element.name());
                    }
                    catch (PropertyException e) {
                        SpellLevel.m_logger.error((Object)"Exception", (Throwable)e);
                    }
                    return result;
                }
                return null;
            }
        }
        else if (fieldName.equals("longDescription")) {
            if (this.m_spell != null) {
                return "\"" + ((Spell)this.m_spell).getBackgroundDescription() + "\"";
            }
        }
        else {
            if (fieldName.equals("description")) {
                return this.getSpellLevelDescription(CastableDescriptionGenerator.DescriptionMode.ALL);
            }
            if (fieldName.equals("shortDescription")) {
                return this.getSpellLevelDescription(CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY);
            }
            if (fieldName.equals("criticalDescription")) {
                return this.getSpellLevelDescription(CastableDescriptionGenerator.DescriptionMode.CRITICALS_ONLY);
            }
            if (fieldName.equals("shortDescriptionElement")) {
                if (this.m_spell != null) {
                    if (this.getType() == 2) {
                        return WakfuTranslator.getInstance().getString("passiveSpell.weapons");
                    }
                    final Elements element = Elements.getElementFromId(((Spell)this.m_spell).getElementId());
                    final StringBuilder shortDesc = new StringBuilder();
                    shortDesc.append(WakfuTranslator.getInstance().getString("spellDescription.levelShort")).append(" ").append(this.getLevel());
                    if (element.equals(Elements.SUPPORT)) {
                        shortDesc.append(" ").append(WakfuTranslator.getInstance().getString(element.name()));
                    }
                    else if (!element.equals(Elements.PHYSICAL)) {
                        shortDesc.append(" ").append(WakfuTranslator.getInstance().getString("spellDescription.element")).append(" ").append(WakfuTranslator.getInstance().getString(element.name()));
                    }
                    return shortDesc;
                }
            }
            else if (fieldName.equals("usable")) {
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final Fight fight = localPlayer.getCurrentFight();
                if (fight == null) {
                    return true;
                }
                final CharacterInfo fighter = fight.getTimeline().getCurrentFighter();
                SpellLevel spell = null;
                if (fighter != null && fighter.getController() == localPlayer) {
                    spell = fighter.getSpellLevelById(this.getUniqueId());
                }
                if (fighter == null || spell == null) {
                    return false;
                }
                final CastValidity validity = fight.getSpellCastValidity(localPlayer, spell, null, true);
                if (validity == CastValidity.OK) {
                    spell.setCriterionOk(true);
                    return true;
                }
                if (validity == CastValidity.CAST_CRITERIONS_NOT_VALID) {
                    spell.setCriterionOk(false);
                    return false;
                }
                spell.setCriterionOk(true);
                return false;
            }
            else {
                if (fieldName.equals("castIntervalRestrictionsDescription")) {
                    final StringBuilder sb2 = new StringBuilder();
                    boolean first = true;
                    final byte castPerTarget = ((Spell)this.m_spell).getCastMaxPerTarget();
                    final byte castPerTurn = this.getCastMaxPerTurn();
                    final byte minInterval = this.getCastMinInterval();
                    if (castPerTarget > 0) {
                        first = false;
                        sb2.append(WakfuTranslator.getInstance().getString("spell.cast.maxPerTarget", castPerTarget));
                    }
                    if (castPerTurn > 0) {
                        if (first) {
                            first = false;
                        }
                        else {
                            sb2.append("\n");
                        }
                        sb2.append(WakfuTranslator.getInstance().getString("spell.cast.maxPerTurn", castPerTurn));
                    }
                    if (minInterval == -1 || minInterval > 0) {
                        if (first) {
                            first = false;
                        }
                        else {
                            sb2.append("\n");
                        }
                        if (minInterval == -1) {
                            sb2.append(WakfuTranslator.getInstance().getString("spell.cast.oncePerFight"));
                        }
                        else {
                            sb2.append(WakfuTranslator.getInstance().getString("spell.cast.minInterval", minInterval));
                        }
                    }
                    return sb2.toString();
                }
                if (fieldName.equals("hasCastIntervalRestrictions")) {
                    return ((Spell)this.m_spell).getCastMaxPerTarget() > 0 || this.getCastMaxPerTurn() > 0 || this.getCastMinInterval() > 0;
                }
                if (fieldName.equals("castMaxPerTurn")) {
                    return (this.getCastMaxPerTurn() > 0) ? this.getCastMaxPerTurn() : "-";
                }
                if (fieldName.equals("castMaxPerTarget")) {
                    return (((Spell)this.m_spell).getCastMaxPerTarget() > 0) ? ((Spell)this.m_spell).getCastMaxPerTarget() : "-";
                }
                if (fieldName.equals("castInterval")) {
                    return this.getCastMinInterval();
                }
                if (fieldName.equals("testLineOfSight")) {
                    return ((Spell)this.m_spell).isTestLineOfSight(this, lp, null, lp.getEffectContext());
                }
                if (fieldName.equals("testFreeCell")) {
                    return ((Spell)this.m_spell).hasToTestFreeCell();
                }
                if (fieldName.equals("testNotBorderCell")) {
                    return ((Spell)this.m_spell).hasToTestNotBorderCell();
                }
                if (fieldName.equals("castOnlyInLine")) {
                    return ((Spell)this.m_spell).getRangeMax(this.getLevel()) > 1 && ((Spell)this.m_spell).isCastOnlyInLine(this, lp, null, lp.getEffectContext());
                }
                if (fieldName.equals("conditionsDescription")) {
                    final SimpleCriterion criterion = ((Spell)this.m_spell).getCastCriterions();
                    final ArrayList<String> strs = new ArrayList<String>();
                    if (criterion != null) {
                        final String s = CriterionDescriptionGenerator.getDescription(criterion);
                        if (s.length() > 0) {
                            final String[] arr$;
                            final String[] strings = arr$ = s.split("\n");
                            for (final String s2 : arr$) {
                                strs.add(s2);
                            }
                        }
                    }
                    final String cast = (String)this.getFieldValue("castIntervalRestrictionsDescription");
                    if (cast.length() > 0) {
                        for (final String s3 : cast.split("\n")) {
                            strs.add(s3);
                        }
                    }
                    return (strs.size() > 0) ? strs : null;
                }
                if (fieldName.equals("weaponSkillsList")) {
                    if (this instanceof WeaponSkillSpellLevel) {
                        final List<Skill> skills = ((WeaponSkillSpellLevel)this).getSkills();
                        if (skills.isEmpty()) {
                            return null;
                        }
                        return skills.toArray();
                    }
                }
                else {
                    if (fieldName.equals("isMonsterSpell")) {
                        return AvatarBreedInfoManager.getInstance().getBreedInfo(((Spell)this.m_spell).getBreedId()) == null;
                    }
                    if (fieldName.equals("hasCriticalEffect")) {
                        for (final WakfuEffect wakfuEffect : this) {
                            if (wakfuEffect.checkFlags(1L)) {
                                return true;
                            }
                        }
                        return false;
                    }
                    if (fieldName.equals("isSupport")) {
                        return this.getSpell().getElementId() == 9;
                    }
                    if (fieldName.equals("isPassive")) {
                        return this.getSpell().isPassive();
                    }
                    if (fieldName.equals("modifiableRange")) {
                        return this.getSpell().isSpellCastRangeDynamic(this, lp, null, lp.getEffectContext());
                    }
                }
            }
        }
        if (fieldName.equals("isLocked")) {
            return this.isLocked();
        }
        return null;
    }
    
    protected byte getCastMinInterval() {
        return ((Spell)this.m_spell).getCastMinInterval();
    }
    
    public boolean isLocked() {
        return WakfuGameEntity.getInstance().getLocalPlayer().getLockedSpellId() == this.getSpell().getId();
    }
    
    public ArrayList<String> getSpellLevelDescription(final CastableDescriptionGenerator.DescriptionMode descriptionMode) {
        if (this.m_spell != null && this.getType() != 2) {
            final SimpleCriterion crit = ((Spell)this.m_spell).getCastCriterions();
            ArrayList<String> validCrits = null;
            ArrayList<String> invalidCrits = null;
            if (crit != null) {
                final ArrayList<ObjectPair<String, SimpleCriterion>> crits = CriterionDescriptionGenerator.splitCriterion(crit);
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final Fight fight = localPlayer.getCurrentFight();
                for (final ObjectPair<String, SimpleCriterion> pair : crits) {
                    try {
                        if (fight == null || pair.getSecond().isValid(localPlayer, localPlayer, this, fight)) {
                            if (validCrits == null) {
                                validCrits = new ArrayList<String>();
                            }
                            validCrits.add(pair.getFirst());
                        }
                        else {
                            if (invalidCrits == null) {
                                invalidCrits = new ArrayList<String>();
                            }
                            invalidCrits.add(pair.getFirst());
                        }
                    }
                    catch (CriteriaExecutionException e) {
                        if (validCrits == null) {
                            validCrits = new ArrayList<String>();
                        }
                        validCrits.add(pair.getFirst());
                    }
                }
            }
            return CastableDescriptionGenerator.generateDescription(new SpellWriter(this, validCrits, invalidCrits, descriptionMode));
        }
        return null;
    }
    
    public int getMaxRange() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterInfo owner = this.getSpellOwner();
        if (owner == null) {
            return ((Spell)this.m_spell).getRangeMax(this, localPlayer, null, localPlayer.getAppropriateContext());
        }
        final int poBoost = owner.getCharacteristicValue(FighterCharacteristicType.RANGE);
        final int baseMaxRange = ((Spell)this.m_spell).getRangeMax(this, owner, null, owner.getAppropriateContext());
        final int maxRangeBoosted = ((Spell)this.m_spell).isSpellCastRangeDynamic(this, localPlayer, null, localPlayer.getAppropriateContext()) ? (baseMaxRange + poBoost) : baseMaxRange;
        return Math.max(this.getMinRange(), maxRangeBoosted);
    }
    
    private CharacterInfo getSpellOwner() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.getSpellInventory().getWithUniqueId(this.getUniqueId()) != null) {
            return localPlayer;
        }
        return null;
    }
    
    public int getMinRange() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterInfo owner = this.getSpellOwner();
        if (owner == null) {
            return ((Spell)this.m_spell).getRangeMin(this, localPlayer, null, localPlayer.getAppropriateContext());
        }
        return ((Spell)this.m_spell).getRangeMin(this, owner, null, owner.getAppropriateContext());
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public int compareTo(final Object o) {
        final SpellLevel spell = (SpellLevel)o;
        return ((Spell)this.m_spell).getName().compareTo(spell.getSpell().getName());
    }
    
    private void updateSpellXP() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentLevelPercentage", "currentLevelPercentageString", "level", "xp", "levelTextShort");
    }
    
    @Override
    public XpModification setLevelAndXp(short level, final long xp) {
        if (level > this.getXpTable().getMaxLevel()) {
            level = this.getXpTable().getMaxLevel();
        }
        final XpModification xpModification = super.setLevelAndXp(level, xp);
        this.updateSpellXP();
        return xpModification;
    }
    
    @Override
    public XpModification setLevel(final short level, final boolean linkXp) {
        final XpModification xpModification = super.setLevel(level, linkXp);
        this.updateSpellXP();
        return xpModification;
    }
    
    @Override
    public XpModification addXp(final long xpAdded) {
        final XpModification xpModification = super.addXp(xpAdded);
        this.updateSpellXP();
        return xpModification;
    }
    
    @Override
    public void addLevelGain(final int levelGainToAdd) {
        super.addLevelGain(levelGainToAdd);
        this.updateSpellXP();
    }
    
    @Override
    public int getLevelGain() {
        return this.m_levelGain;
    }
    
    static {
        FIELDS = new String[] { "id", "name", "levelTextShort", "level", "xp", "currentLevelString", "currentLevelPercentage", "currentLevelPercentageString", "ap", "mp", "wp", "chrage", "range", "elementsUsedIconURL", "spell", "smallIconUrl", "bigIconUrl", "longDescription", "description", "usable", "castMaxPerTarget", "castMaxPerTurn", "castInterval", "testLineOfSight", "castOnlyInLine", "testFreeCell", "testNotBorderCell", "conditionsDescription", "areaOfEffectIconURL", "areaOfEffectBigIconURL", "isMonsterSpell", "shortDescription", "criticalDescription", "hasCriticalEffect", "isSupport", "isPassive", "modifiableRange", "isLocked" };
    }
}
