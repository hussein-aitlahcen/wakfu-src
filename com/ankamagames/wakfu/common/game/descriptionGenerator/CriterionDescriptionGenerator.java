package com.ankamagames.wakfu.common.game.descriptionGenerator;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.kernel.core.translator.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.system.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.java.util.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.ai.antlrcriteria.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.framework.ai.targetfinder.*;

public class CriterionDescriptionGenerator
{
    public static final String CRITERION_OR = "or";
    public static final String CRITERION_TRUE = "critere.true";
    public static final String CRITERION_FALSE = "critere.false";
    public static final String CRITERION_HP = "critere.hp";
    public static final String CRITERION_AP = "critere.ap";
    public static final String CRITERION_MP = "critere.mp";
    public static final String CRITERION_WP = "critere.wp";
    public static final String CRITERION_CHRAGE = "critere.chrage";
    public static final String CRITERION_TRAP = "critere.control";
    public static final String CRITERION_LEADERSHIP = "LEADERSHIPShort";
    public static final String CRITERION_CASTERBREED = "critere.casterbreed";
    public static final String CRITERION_TARGETBREED = "critere.targetbreed";
    public static final String CRITERION_CASTERBREEDID = "critere.casterbreedId";
    public static final String CRITERION_TARGETBREEDID = "critere.targetbreedId";
    public static final String CRITERION_NOTTARGETBREEDID = "critere.nottargetbreedid";
    public static final String CRITERION_HASSUMMON = "critere.hassummon";
    public static final String CRITERION_NOTHASSUMMON = "critere.nothassummon";
    public static final String CRITERION_HASEQUIP = "critere.hasequip";
    public static final String CRITERION_NOTHASEQUIP = "critere.nothasequip";
    public static final String CRITERION_HASEQUIPWITHPOS = "critere.hasequipwithpos";
    public static final String CRITERION_NOTHASEQUIPWITHPOS = "critere.nothasequipwithpos";
    public static final String CRITERION_TARGETHOUR = "critere.targethour";
    public static final String CRITERION_NOTTARGETHOUR = "critere.nottargethour";
    public static final String CRITERION_ENNEMY = "critere.isennemy";
    public static final String CRITERION_NOT_ENNEMY = "critere.isnotennemy";
    public static final String CRITERION_PET_IN_RANGE = "critere.petinrange";
    public static final String CRITERION_PET_SUMMNONED = "critere.summoned";
    public static final String CRITERION_PET_NOT_SUMMONED = "critere.not.summoned";
    public static final String CRITERION_BEACON_AMOUNT = "critere.beaconamount";
    public static final String CRITERION_BARREL_AMOUNT = "critere.barrelamount";
    public static final String CRITERION_TRAP_AMOUNT = "critere.trapamount";
    public static final String CRITERION_NBSUMMONS = "critere.nbsummons";
    public static final String CRITERION_NB_ROUBLABOT = "critere.nbroublabot";
    public static final String CRITERION_INF = "critere.inf";
    public static final String CRITERION_SUP = "critere.sup";
    public static final String CRITERION_INFEQ = "critere.infeq";
    public static final String CRITERION_SUPEQ = "critere.supeq";
    public static final String CRITERION_ISSEX = "critere.isSex.";
    public static final String CRITERION_GETSPELLTREELEVEL = "critere.getSpellTreeLevel";
    public static final String CRITERION_KAMASCOUNT = "critere.kamaCount";
    public static final String CRITERION_HASSTATE = "critere.hasState";
    public static final String CRITERION_NOT_HASSTATE = "critere.notHasState";
    public static final String CRITERION_HASSTATE_FROM_LEVEL = "critere.hasStateFromLevel";
    public static final String CRITERION_NOT_HASSTATE_FROM_LEVEL = "critere.notHasStateFromLevel";
    public static final String CRITERION_HASCRAFT = "critere.hasCraft";
    public static final String CRITERION_NOT_HASCRAFT = "critere.notHasCraft";
    public static final String CRITERION_SYMBIOTSPACE = "critere.symbiotSpace";
    public static final String CRITERION_CRAFTLEVEL = "critere.craftLevel";
    public static final String CRITERION_GETWAKFUGAUGE = "critere.getWakfuGaugeValue";
    public static final String CRITERION_GETSTASISGAUGE = "critere.getStasisGauge";
    public static final String CRITERION_GETCRIMESCORE = "critere.getCrimeScore";
    public static final String CRITERION_ISDEAD = "critere.isDead";
    public static final String CRITERION_ISNOTDEAD = "critere.isNotDead";
    public static final String CRITERION_ISTARGETONSAMETEAM = "critere.istargetonsameteam";
    public static final String CRITERION_NOTISTARGETONSAMETEAM = "critere.notistargetonsameteam";
    public static final String CRITERION_GETZONEWAKFU = "critere.getZoneWakfu";
    public static final String CRITERION_GETZONESTASIS = "critere.getZoneStasis";
    public static final String CRITERION_HASSUMMONWITHBREED = "critere.hasSummonWithBreed";
    public static final String CRITERION_NOTHASSUMMONWITHBREED = "critere.nothasSummonWithBreed";
    public static final String CRITERION_GETINSTANCEID = "critere.getInstanceId";
    public static final String CRITERION_NOTGETINSTANCEID = "critere.notGetInstanceId";
    public static final String CRITERION_GETNATIONID = "critere.getNationId";
    public static final String CRITERION_NOTGETNATIONID = "critere.notGetNationId";
    public static final String CRITERION_GETNATIONALIGNMENT = "critere.getNationAlignment";
    public static final String CRITERION_GETNONATIONALIGNMENT = "critere.getNoNationAlignment";
    public static final String CRITERION_TARGET = "critere.target";
    public static final String CRITERION_ISCARRYINGOWNBARREL = "critere.isCarryingOwnBarrel";
    public static final String CRITERION_HASSURRONDINGCELLWITHOWNBARREL = "critere.hasSurrondingCellWithOwnBarrel";
    public static final String CRITERION_GETDISTANCE_BETWEEN_TARGET_AND_NEAREST_ALLY_BEACON = "critere.distanceBetweenTargetAndNearestAllyBeacon";
    public static final String CRITERION_IS_TARGET_CELL_VALID_FOR_NEW_OBSTACLE = "critere.isTargetCellValidForNewObstacle";
    public static final String CRITERION_NBBOMBS = "critere.nbBombs";
    public static final String CRITERION_GET_TOTAL_LEADERSHIP = "critere.getTotalLeadership";
    public static final String CRITERION_HAS_AVAILABLE_CREATURE = "critere.hasAvailableCreature";
    public static final String CRITERION_IS_ON_OWN_DIAL = "critere.isOnOwnDial";
    public static final String CRITERION_NOT_IS_CARRIED = "critere.not.isCarried";
    public static final String CRITERION_IS_CARRIED = "critere.isCarried";
    public static final String CRITERION_ADD = "critere.add";
    public static final String CRITERION_GUILD_LEVEL = "critere.guildLevel";
    public static final String CRITERION_GUILD_BONUS = "critere.guildBonus";
    public static final String CRITERION_HAS_ANOTHER_SAME_EQUIPMENT = "critere.hasAnotherSameEquipment";
    public static final String CRITERION_IS_ON_HOUR = "critere.isOnHour";
    public static final String CRITERION_NOT_IS_ON_HOUR = "critere.notIsOnHour";
    public static final String CRITERION_HAS_EQUIPMENT_ID = "critere.hasEquipmentId";
    public static final String CRITERION_HAS_UNLOCKED_COMPANION = "critere.hasUnlockedCompanion";
    public static final String CRITERION_IS_COMPANION = "critere.isCompanion";
    public static final String CRITERION_IS_COMPANION_NEGATED = "critere.isCompanionNegated";
    public static final String CRITERION_HAS_UNLOCKED_COMPANION_NEGATED = "critere.notHasUnlockedCompanion";
    public static final String CRITERION_GET_STATE_COUNT_IN_RANGE = "critere.getStateCountInRange";
    public static final String CRITERION_GET_AWN_TEAM_STATE_COUNT_IN_RANGE = "critere.getOwnTeamStateCountInRange";
    public static final String CRITERION_HAS_SUBSCRIPTION_LEVEL = "critere.hasSubscriptionLevel";
    public static final String CRITERION_HAS_PVP_RANK = "critere.hasPvpRank";
    public static final String CRITERION_IS_ACHIEVEMENT_COMPLETE = "critere.isAchievementComplete";
    
    public static ArrayList<String> displayMaxCrits(final ArrayList<SimpleCriterion> allCrits) {
        TShortArrayList possibleBreeds = null;
        TByteShortHashMap minCharValues = null;
        TByteShortHashMap maxCharValues = null;
        TIntShortHashMap minSpellLevel = null;
        TIntShortHashMap minSkillLevel = null;
        TIntShortHashMap maxSpellLevel = null;
        TIntShortHashMap maxSkillLevel = null;
        final ArrayList<SimpleCriterion> notSimplifiable = null;
        for (int i = 0; i < allCrits.size(); ++i) {
            final SimpleCriterion crit = allCrits.get(i);
            if (crit != null) {
                final LinkedList<SimpleCriterion> crits = splitAnd(crit);
                for (final SimpleCriterion smallCrit : crits) {
                    boolean isInf = false;
                    boolean isSup = false;
                    boolean isInfEq = false;
                    boolean isSupEq = false;
                    NumericalValue constant = null;
                    ParserObject variable = null;
                    final Enum criterionEnum = smallCrit.getEnum();
                    if (criterionEnum instanceof CriterionIds) {
                        switch ((CriterionIds)criterionEnum) {
                            case INF: {
                                final InfCriterion infCrit = (InfCriterion)smallCrit;
                                if (infCrit.getLeft().isConstant()) {
                                    isInf = true;
                                    constant = infCrit.getLeft();
                                    variable = infCrit.getRight();
                                }
                                else {
                                    isSup = true;
                                    variable = infCrit.getLeft();
                                    constant = infCrit.getRight();
                                }
                                break;
                            }
                            case INFEQ: {
                                final InfEqCriterion infCrit2 = (InfEqCriterion)smallCrit;
                                if (infCrit2.getLeft().isConstant()) {
                                    isInfEq = true;
                                    constant = infCrit2.getLeft();
                                    variable = infCrit2.getRight();
                                }
                                else {
                                    isSupEq = true;
                                    variable = infCrit2.getLeft();
                                    constant = infCrit2.getRight();
                                }
                                break;
                            }
                            case EQUAL: {
                                final EqualCriterion eqCrit = (EqualCriterion)smallCrit;
                                isInfEq = true;
                                isSupEq = true;
                                if (eqCrit.getLeft().isConstant()) {
                                    constant = eqCrit.getLeft();
                                    variable = eqCrit.getRight();
                                    break;
                                }
                                variable = eqCrit.getLeft();
                                constant = eqCrit.getRight();
                                break;
                            }
                        }
                    }
                    else if (criterionEnum instanceof WakfuCriterionIds) {
                        switch ((WakfuCriterionIds)criterionEnum) {
                            case ISBREED: {
                                final IsBreed breedCrit = (IsBreed)smallCrit;
                                if (possibleBreeds == null) {
                                    possibleBreeds = breedCrit.getBreedId();
                                    break;
                                }
                                for (int j = 0; j < possibleBreeds.size(); ++j) {
                                    if (!breedCrit.getBreedId().contains(possibleBreeds.get(j))) {
                                        possibleBreeds.remove(j);
                                    }
                                }
                                break;
                            }
                        }
                    }
                    if (constant != null && criterionEnum instanceof WakfuCriterionIds) {
                        switch ((WakfuCriterionIds)criterionEnum) {
                            case GETSKILLLEVEL: {
                                final GetSkillLevel skillCrit = (GetSkillLevel)variable;
                                final int skillId = skillCrit.getSkillId();
                                if (skillId != -1) {
                                    if (isInfEq || isInf) {
                                        if (minSkillLevel == null) {
                                            minSkillLevel = new TIntShortHashMap();
                                        }
                                        if (isInf) {
                                            minSkillLevel.put(skillId, (short)Math.max(minSkillLevel.get(skillId), constant.getDoubleValue(null, null, null, null) + 1.0));
                                        }
                                        else {
                                            minSkillLevel.put(skillId, (short)Math.max(minSkillLevel.get(skillId), constant.getDoubleValue(null, null, null, null)));
                                        }
                                    }
                                    else {
                                        if (maxSkillLevel == null) {
                                            maxSkillLevel = new TIntShortHashMap();
                                        }
                                        if (isSup) {
                                            maxSkillLevel.put(skillId, (short)Math.min(maxSkillLevel.contains(skillId) ? maxSkillLevel.get(skillId) : 32767, constant.getDoubleValue(null, null, null, null) - 1.0));
                                        }
                                        else {
                                            maxSkillLevel.put(skillId, (short)Math.min(maxSkillLevel.contains(skillId) ? maxSkillLevel.get(skillId) : 32767, constant.getDoubleValue(null, null, null, null)));
                                        }
                                    }
                                }
                                else {
                                    if (notSimplifiable == null) {
                                        continue;
                                    }
                                    notSimplifiable.add(smallCrit);
                                }
                                continue;
                            }
                            case GETSPELLLEVEL: {
                                final GetSpellLevel spellLevel = (GetSpellLevel)variable;
                                final int spellId = spellLevel.getSpellId();
                                if (spellId != -1) {
                                    if (isInfEq || isInf) {
                                        if (minSpellLevel == null) {
                                            minSpellLevel = new TIntShortHashMap();
                                        }
                                        if (isInf) {
                                            minSpellLevel.put(spellId, (short)Math.max(minSpellLevel.get(spellId), constant.getDoubleValue(null, null, null, null) + 1.0));
                                        }
                                        else {
                                            minSpellLevel.put(spellId, (short)Math.max(minSkillLevel.get(spellId), constant.getDoubleValue(null, null, null, null)));
                                        }
                                    }
                                    else {
                                        if (maxSpellLevel == null) {
                                            maxSpellLevel = new TIntShortHashMap();
                                        }
                                        if (isSup) {
                                            maxSpellLevel.put(spellId, (short)Math.min(maxSpellLevel.contains(spellId) ? maxSpellLevel.get(spellId) : 32767, constant.getDoubleValue(null, null, null, null) - 1.0));
                                        }
                                        else {
                                            maxSkillLevel.put(spellId, (short)Math.min(maxSpellLevel.contains(spellId) ? maxSpellLevel.get(spellId) : 32767, constant.getDoubleValue(null, null, null, null)));
                                        }
                                    }
                                }
                                else {
                                    if (notSimplifiable == null) {
                                        continue;
                                    }
                                    notSimplifiable.add(smallCrit);
                                }
                                continue;
                            }
                            case GETCHARACTERISTIC: {
                                final GetCharacteristic charac = (GetCharacteristic)variable;
                                final byte characId = charac.getCharacType().getId();
                                if (isInfEq || isInf) {
                                    if (minCharValues == null) {
                                        minCharValues = new TByteShortHashMap();
                                    }
                                    if (isInf) {
                                        minCharValues.put(characId, (short)Math.max(minCharValues.get(characId), constant.getDoubleValue(null, null, null, null) + 1.0));
                                    }
                                    else {
                                        minCharValues.put(characId, (short)Math.max(minCharValues.get(characId), constant.getDoubleValue(null, null, null, null)));
                                    }
                                }
                                else {
                                    if (maxCharValues == null) {
                                        maxCharValues = new TByteShortHashMap();
                                    }
                                    if (isSup) {
                                        maxCharValues.put(characId, (short)Math.min(maxCharValues.contains(characId) ? maxCharValues.get(characId) : 32767, constant.getDoubleValue(null, null, null, null) - 1.0));
                                    }
                                    else {
                                        maxCharValues.put(characId, (short)Math.min(maxCharValues.contains(characId) ? maxCharValues.get(characId) : 32767, constant.getDoubleValue(null, null, null, null)));
                                    }
                                }
                                continue;
                            }
                            default: {
                                if (notSimplifiable != null) {
                                    notSimplifiable.add(smallCrit);
                                    continue;
                                }
                                continue;
                            }
                        }
                    }
                    else {
                        if (constant == null || !(criterionEnum instanceof CriterionIds)) {
                            continue;
                        }
                        final int n = CriterionDescriptionGenerator$1.$SwitchMap$com$ankamagames$framework$ai$criteria$antlrcriteria$CriterionIds[((CriterionIds)criterionEnum).ordinal()];
                        if (notSimplifiable == null) {
                            continue;
                        }
                        notSimplifiable.add(smallCrit);
                    }
                }
            }
        }
        final ArrayList<String> criteres = new ArrayList<String>();
        if (possibleBreeds != null) {
            String temp = CastableDescriptionGenerator.m_translator.getString("critere.casterbreed");
            for (int k = 0; k < possibleBreeds.size(); ++k) {
                temp = temp + " " + CastableDescriptionGenerator.m_utilityDelegate.getAvatarBreedName(possibleBreeds.get(k));
            }
            criteres.add(temp);
        }
        for (final FighterCharacteristicType charac2 : FighterCharacteristicType.values()) {
            short min = 0;
            short max = 0;
            if (minCharValues != null && minCharValues.get(charac2.getId()) > 0) {
                min = minCharValues.get(charac2.getId());
            }
            if (maxCharValues != null && maxCharValues.get(charac2.getId()) > 0) {
                max = maxCharValues.get(charac2.getId());
            }
            if (max > 0 && min > 0) {
                if (min < max) {
                    criteres.add(min + " < " + displayCharac(charac2) + " < " + max);
                }
                else {
                    criteres.add(displayCharac(charac2) + " = " + min);
                }
            }
            else if (max > 0) {
                criteres.add(displayCharac(charac2) + " < " + max);
            }
            else if (min > 0) {
                criteres.add(displayCharac(charac2) + " > " + min);
            }
        }
        if (minSpellLevel != null) {
            final TIntShortIterator it = minSpellLevel.iterator();
            while (it.hasNext()) {
                it.advance();
                final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
                final AbstractSpellLevel spell = CastableDescriptionGenerator.m_utilityDelegate.getSpell((short)it.key());
                CastableDescriptionGenerator.m_utilityDelegate.formatSpellName(sb, spell.getSpell());
                sb.append(" > ").append(it.value());
                criteres.add(sb.finishAndToString());
            }
        }
        if (maxSpellLevel != null) {
            final TIntShortIterator it = maxSpellLevel.iterator();
            while (it.hasNext()) {
                it.advance();
                final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
                final AbstractSpellLevel spell = CastableDescriptionGenerator.m_utilityDelegate.getSpell((short)it.key());
                CastableDescriptionGenerator.m_utilityDelegate.formatSpellName(sb, spell.getSpell());
                sb.append(" < ").append(it.value());
                criteres.add(sb.finishAndToString());
            }
        }
        if (minSkillLevel != null) {
            final TIntShortIterator it = minSkillLevel.iterator();
            while (it.hasNext()) {
                it.advance();
                final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
                CastableDescriptionGenerator.m_utilityDelegate.getSkillName((short)it.key());
                sb.append(" > ").append(it.value());
                criteres.add(sb.finishAndToString());
            }
        }
        if (maxSkillLevel != null) {
            final TIntShortIterator it = maxSkillLevel.iterator();
            while (it.hasNext()) {
                it.advance();
                final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
                CastableDescriptionGenerator.m_utilityDelegate.getSkillName((short)it.key());
                sb.append(" < ").append(it.value());
                criteres.add(sb.finishAndToString());
            }
        }
        return criteres;
    }
    
    public static LinkedList<SimpleCriterion> splitAnd(final SimpleCriterion crit) {
        if (crit == null) {
            return null;
        }
        if (crit.getEnum() == CriterionIds.AND) {
            final AndCriterion andCrit = (AndCriterion)crit;
            final LinkedList<SimpleCriterion> list = new LinkedList<SimpleCriterion>();
            list.addAll(splitAnd(andCrit.getLeft()));
            list.addAll(splitAnd(andCrit.getRight()));
            return list;
        }
        final LinkedList<SimpleCriterion> list2 = new LinkedList<SimpleCriterion>();
        list2.add(crit);
        return list2;
    }
    
    public static String displayCharac(final FighterCharacteristicType charac) {
        if (charac == FighterCharacteristicType.HP) {
            return CastableDescriptionGenerator.m_translator.getString("critere.hp");
        }
        if (charac == FighterCharacteristicType.AP) {
            return CastableDescriptionGenerator.m_translator.getString("critere.ap");
        }
        if (charac == FighterCharacteristicType.WP) {
            return CastableDescriptionGenerator.m_translator.getString("critere.wp");
        }
        if (charac == FighterCharacteristicType.MP) {
            return CastableDescriptionGenerator.m_translator.getString("critere.mp");
        }
        if (charac == FighterCharacteristicType.MECHANICS) {
            return CastableDescriptionGenerator.m_translator.getString("LEADERSHIPShort");
        }
        if (charac == FighterCharacteristicType.LEADERSHIP) {
            return CastableDescriptionGenerator.m_translator.getString("LEADERSHIPShort");
        }
        if (charac == FighterCharacteristicType.WISDOM) {
            return CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.WISDOM);
        }
        if (charac == FighterCharacteristicType.AIR_MASTERY) {
            return CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.AGILITY);
        }
        if (charac == FighterCharacteristicType.EARTH_MASTERY) {
            return CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.STRENGTH);
        }
        if (charac == FighterCharacteristicType.WATER_MASTERY) {
            return CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.LUCK);
        }
        if (charac == FighterCharacteristicType.FIRE_MASTERY) {
            return CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.INTELLIGENCE);
        }
        if (charac == FighterCharacteristicType.VITALITY) {
            return CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.VITALITY);
        }
        if (charac == FighterCharacteristicType.CHRAGE) {
            return CastableDescriptionGenerator.m_utilityDelegate.getChrageIcon();
        }
        return "";
    }
    
    public static String getDescription(final ParserObject object) {
        if (!object.isDisplayable()) {
            return "";
        }
        if (object.getInvalidCriterionTradKey() != null) {
            return Translator.getInstance().getString(object.getInvalidCriterionTradKey());
        }
        final Enum criterionEnum = object.getEnum();
        String description = "";
        if (criterionEnum instanceof CriterionIds) {
            switch ((CriterionIds)criterionEnum) {
                case INF: {
                    final InfCriterion crit = (InfCriterion)object;
                    final String leftDesc = getDescription(crit.getLeft());
                    final String rightDesc = getDescription(crit.getRight());
                    if (leftDesc.length() == 0 || rightDesc.length() == 0) {
                        return "";
                    }
                    if (crit.getRight().isConstant()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.inf", leftDesc, rightDesc);
                        break;
                    }
                    if (crit.getLeft().isConstant()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.sup", rightDesc, leftDesc);
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.inf", leftDesc, rightDesc);
                    break;
                }
                case INFEQ: {
                    final InfEqCriterion crit2 = (InfEqCriterion)object;
                    final String leftDesc = getDescription(crit2.getLeft());
                    final String rightDesc = getDescription(crit2.getRight());
                    if (leftDesc.length() == 0 || rightDesc.length() == 0) {
                        return "";
                    }
                    if (crit2.getRight().isConstant()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.infeq", leftDesc, getDescription(crit2.getRight()));
                        break;
                    }
                    if (crit2.getLeft().isConstant()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.supeq", getDescription(crit2.getRight()), leftDesc);
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.infeq", leftDesc, getDescription(crit2.getRight()));
                    break;
                }
                case AND: {
                    final AndCriterion crit3 = (AndCriterion)object;
                    final String left = getDescription(crit3.getLeft());
                    final String right = getDescription(crit3.getRight());
                    if (left.length() == 0 && right.length() == 0) {
                        return "";
                    }
                    if (left.length() == 0) {
                        description = right;
                        break;
                    }
                    if (right.length() == 0) {
                        description = left;
                        break;
                    }
                    description = left + ((left.length() > 0) ? "\n" : "") + right;
                    break;
                }
                case EQUAL: {
                    NumericalValue left2;
                    NumericalValue right2;
                    boolean equals;
                    if (object instanceof EqualCriterion) {
                        final EqualCriterion crit4 = (EqualCriterion)object;
                        left2 = crit4.getLeft();
                        right2 = crit4.getRight();
                        equals = true;
                    }
                    else {
                        final NotEqualCriterion crit5 = (NotEqualCriterion)object;
                        left2 = crit5.getLeft();
                        right2 = crit5.getRight();
                        equals = false;
                    }
                    if (left2.isConstant()) {
                        description = equalCriterion(right2, left2, equals);
                        break;
                    }
                    description = equalCriterion(left2, right2, equals);
                    break;
                }
                case OR: {
                    final OrCriterion crit6 = (OrCriterion)object;
                    final String rightDesc2 = getDescription(crit6.getRight());
                    final String leftDesc2 = getDescription(crit6.getLeft());
                    if (leftDesc2.length() == 0 && rightDesc2.length() == 0) {
                        return "";
                    }
                    if (leftDesc2.length() == 0) {
                        description = rightDesc2;
                        break;
                    }
                    if (rightDesc2.length() == 0) {
                        description = leftDesc2;
                        break;
                    }
                    description = rightDesc2 + '\n' + CastableDescriptionGenerator.m_translator.getString("or") + " " + leftDesc2;
                    break;
                }
                case STRING: {
                    final StringObject crit7 = (StringObject)object;
                    description = crit7.getValue();
                    break;
                }
                case INT: {
                    final ConstantIntegerValue crit8 = (ConstantIntegerValue)object;
                    description = String.valueOf(crit8.getLongValue(null, null, null, null));
                    break;
                }
                case ADD: {
                    final AddValue crit9 = (AddValue)object;
                    final String leftDesc = getDescription(crit9.getLeft());
                    final String rightDesc = getDescription(crit9.getRight());
                    if (leftDesc.length() == 0 && rightDesc.length() == 0) {
                        return "";
                    }
                    if (leftDesc.length() == 0) {
                        return rightDesc;
                    }
                    if (rightDesc.length() == 0) {
                        return leftDesc;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.add", leftDesc, rightDesc);
                    break;
                }
                default: {
                    description = "";
                    break;
                }
            }
        }
        else if (criterionEnum instanceof WakfuCriterionIds) {
            switch ((WakfuCriterionIds)criterionEnum) {
                case GETCHARACTERISTIC: {
                    final GetCharacteristic crit10 = (GetCharacteristic)object;
                    description = displayCharac(crit10.getCharacType());
                    break;
                }
                case GETCHARACTERISTICMAX: {
                    final GetCharacteristicMax crit11 = (GetCharacteristicMax)object;
                    description = displayCharac(crit11.getCharacType()) + " " + CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.MAX_SHORT);
                    break;
                }
                case GET_CHARACTERISTIC_PCT: {
                    final GetCharacteristicInPct crit12 = (GetCharacteristicInPct)object;
                    description = displayCharac(crit12.getCharacType()) + " %";
                    break;
                }
                case GET_LEVEL: {
                    description = CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.LEVEL);
                    break;
                }
                case GETSKILLLEVEL: {
                    final GetSkillLevel crit13 = (GetSkillLevel)object;
                    final int skillId = crit13.getSkillId();
                    if (skillId != -1) {
                        description = CastableDescriptionGenerator.m_utilityDelegate.getSkillName((short)skillId);
                        break;
                    }
                    description = "";
                    break;
                }
                case GETSPELLLEVEL: {
                    final GetSpellLevel crit14 = (GetSpellLevel)object;
                    final int spellId = crit14.getSpellId();
                    if (spellId != -1) {
                        final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
                        final AbstractSpellLevel spellLevel = CastableDescriptionGenerator.m_utilityDelegate.getSpell((short)spellId);
                        final AbstractSpell spell = spellLevel.getSpell();
                        CastableDescriptionGenerator.m_utilityDelegate.formatSpellName(sb, spell);
                        description = sb.finishAndToString();
                        break;
                    }
                    description = "";
                    break;
                }
                case ISBREED: {
                    final IsBreed crit15 = (IsBreed)object;
                    String temp;
                    if (crit15.isTarget()) {
                        temp = CastableDescriptionGenerator.m_translator.getString("critere.targetbreed");
                    }
                    else {
                        temp = CastableDescriptionGenerator.m_translator.getString("critere.casterbreed");
                    }
                    for (int i = 0; i < crit15.getBreedId().size(); ++i) {
                        temp += " ";
                        temp += CastableDescriptionGenerator.m_utilityDelegate.getAvatarBreedName(crit15.getBreedId().get(i));
                    }
                    description = temp;
                    break;
                }
                case IS_BREED_ID: {
                    final IsBreedId crit16 = (IsBreedId)object;
                    final boolean negated = ((SimpleCriterion)object).isNegated();
                    String temp;
                    if (crit16.isTarget()) {
                        temp = CastableDescriptionGenerator.m_translator.getString(negated ? "critere.nottargetbreedid" : "critere.targetbreedId");
                    }
                    else {
                        temp = CastableDescriptionGenerator.m_translator.getString("critere.casterbreedId");
                    }
                    for (int j = 0; j < crit16.getBreedIds().size(); ++j) {
                        temp += " ";
                        temp += CastableDescriptionGenerator.m_utilityDelegate.getMonsterName(crit16.getBreedIds().get(j));
                    }
                    description = temp;
                    break;
                }
                case ISBREEDFAMILY: {
                    final IsBreedFamily crit17 = (IsBreedFamily)object;
                    final boolean negated = ((SimpleCriterion)object).isNegated();
                    String temp;
                    if (crit17.isTarget()) {
                        temp = CastableDescriptionGenerator.m_translator.getString(negated ? "critere.nottargetbreedid" : "critere.targetbreedId");
                    }
                    else {
                        temp = CastableDescriptionGenerator.m_translator.getString("critere.casterbreedId");
                    }
                    for (int j = 0; j < crit17.getBreedId().size(); ++j) {
                        temp += " ";
                        temp += CastableDescriptionGenerator.m_utilityDelegate.getMonsterTypeName((short)crit17.getBreedId().get(0));
                    }
                    description = temp;
                    break;
                }
                case HASSUMMONS: {
                    if (((SimpleCriterion)object).isNegated()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.nothassummon");
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.hassummon");
                    break;
                }
                case HAS_ANOTHER_SAME_EQUIPMENT: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.hasAnotherSameEquipment");
                    break;
                }
                case HASEQUIPMENTTYPE: {
                    final HasEquipmentType crit18 = (HasEquipmentType)object;
                    String positions = "";
                    final boolean negated = ((SimpleCriterion)object).isNegated();
                    if (crit18.getPos().size() > 0) {
                        positions = CastableDescriptionGenerator.m_translator.getString(crit18.getPos().get(0).descKey);
                    }
                    for (int j = 1; j < crit18.getPos().size(); ++j) {
                        positions = positions + " " + CastableDescriptionGenerator.m_translator.getString("or") + " " + CastableDescriptionGenerator.m_translator.getString(crit18.getPos().get(j).descKey);
                    }
                    String types = "";
                    if (crit18.getTypes().size() > 0) {
                        types = CastableDescriptionGenerator.m_utilityDelegate.getItemTypeName(crit18.getTypes().get(0));
                    }
                    for (int k = 1; k < crit18.getTypes().size(); ++k) {
                        final int id = crit18.getTypes().get(k);
                        types = types + " " + CastableDescriptionGenerator.m_translator.getString("or") + " " + CastableDescriptionGenerator.m_utilityDelegate.getItemTypeName(id);
                    }
                    if (crit18.getPos().size() > 0) {
                        if (negated) {
                            description = CastableDescriptionGenerator.m_translator.getString("critere.nothasequipwithpos", types, positions);
                            break;
                        }
                        description = CastableDescriptionGenerator.m_translator.getString("critere.hasequipwithpos", types, positions);
                        break;
                    }
                    else {
                        if (negated) {
                            description = CastableDescriptionGenerator.m_translator.getString("critere.nothasequip", types);
                            break;
                        }
                        description = CastableDescriptionGenerator.m_translator.getString("critere.hasequip", types);
                        break;
                    }
                    break;
                }
                case IS_HOUR: {
                    if (((SimpleCriterion)object).isNegated()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.nottargethour");
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.targethour");
                    break;
                }
                case IS_ON_SPECIFIC_EFFECT_AREA: {
                    final IsOnSpecificEffectArea criterion = (IsOnSpecificEffectArea)object;
                    final long areaId = criterion.getAreaId().getLongValue(null, null, null, null);
                    final AbstractEffectArea areaFromId = StaticEffectAreaManager.getInstance().getAreaFromId(areaId);
                    if (areaFromId == null || areaFromId.getType() != EffectAreaType.HOUR.getTypeId()) {
                        break;
                    }
                    if (((SimpleCriterion)object).isNegated()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.notIsOnHour");
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.isOnHour");
                    break;
                }
                case GETKAMASCOUNT: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.kamaCount");
                    break;
                }
                case ISENNEMY: {
                    if (((SimpleCriterion)object).isNegated()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.isnotennemy");
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.isennemy");
                    break;
                }
                case PETINRANGE: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.petinrange");
                    break;
                }
                case IS_SELECTED_CREATURE_SUMMONED: {
                    if (((SimpleCriterion)object).isNegated()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.not.summoned");
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.summoned");
                    break;
                }
                case GETBEACONAMOUNT: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.beaconamount");
                    break;
                }
                case GETBARRELAMOUNT: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.barrelamount");
                    break;
                }
                case GETTRAPAMOUNT: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.trapamount");
                    break;
                }
                case NBSUMMONS: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.nbsummons");
                    break;
                }
                case NB_ROUBLABOT: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.nbroublabot");
                    break;
                }
                case GETSPACEINSYMBIOT: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.symbiotSpace");
                    break;
                }
                case GET_CRAFT_LEVEL: {
                    final GetCraftLevel crit19 = (GetCraftLevel)object;
                    final String craftName = CastableDescriptionGenerator.m_utilityDelegate.getCraftName((short)crit19.getCraftId());
                    description = CastableDescriptionGenerator.m_translator.getString("critere.craftLevel", craftName);
                    break;
                }
                case ISSEX: {
                    final IsSex crit20 = (IsSex)object;
                    description = CastableDescriptionGenerator.m_translator.getString(String.format("%s%s", "critere.isSex.", crit20.getSex()));
                    break;
                }
                case GETSPELLTREELEVEL: {
                    final GetSpellTreeLevel crit21 = (GetSpellTreeLevel)object;
                    final byte elementId = (byte)crit21.getElementId();
                    description = CastableDescriptionGenerator.m_translator.getString("critere.getSpellTreeLevel", getElementText(elementId));
                    break;
                }
                case HAS_STATE: {
                    final HasState crit22 = (HasState)object;
                    final State state = StateManager.getInstance().getState(crit22.getStateId());
                    String key;
                    if (((SimpleCriterion)object).isNegated()) {
                        key = "critere.notHasState";
                    }
                    else {
                        key = "critere.hasState";
                    }
                    description = CastableDescriptionGenerator.m_translator.getString(key, state.getName(), crit22.getStateLevel());
                    break;
                }
                case HAS_STATE_FROM_LEVEL: {
                    final HasStateFromLevel crit23 = (HasStateFromLevel)object;
                    final State state = StateManager.getInstance().getState(crit23.getStateId());
                    String key;
                    if (((SimpleCriterion)object).isNegated()) {
                        key = "critere.notHasStateFromLevel";
                    }
                    else {
                        key = "critere.hasStateFromLevel";
                    }
                    description = CastableDescriptionGenerator.m_translator.getString(key, state.getName(), crit23.getStateLevel());
                    break;
                }
                case HAS_CRAFT: {
                    final HasCraft crit24 = (HasCraft)object;
                    final String craftName = CastableDescriptionGenerator.m_utilityDelegate.getCraftName((short)crit24.getCraftId());
                    if (((SimpleCriterion)object).isNegated()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.notHasCraft", craftName);
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.hasCraft", craftName);
                    break;
                }
                case GETWAKFUGAUGE: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.getWakfuGaugeValue");
                    break;
                }
                case GETSTASISGAUGE: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.getStasisGauge");
                    break;
                }
                case GET_CRIME_SCORE: {
                    final GetCrimeScore crit25 = (GetCrimeScore)object;
                    final int nationId = crit25.getNationId();
                    if (nationId > 0) {
                        final String nationString = CastableDescriptionGenerator.m_utilityDelegate.getNationName(nationId);
                        description = CastableDescriptionGenerator.m_translator.getString("critere.getCrimeScore", nationString);
                        break;
                    }
                    description = CastableDescriptionGenerator.m_utilityDelegate.getCitizenPointsIcon();
                    break;
                }
                case IS_DEAD: {
                    final IsDead c = (IsDead)object;
                    if (c.isNegated()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.isNotDead");
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.isDead");
                    break;
                }
                case TOTAL_LEADERSHIP_NEEDED_FOR_CURRENT_CREATURE: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.getTotalLeadership");
                    break;
                }
                case HAS_AVAILABLE_CREATURE_IN_SYMBIOT: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.hasAvailableCreature");
                    break;
                }
                case IS_ON_OWN_DIAL: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.isOnOwnDial");
                    break;
                }
                case GET_ZONE_WAKFU: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.getZoneWakfu");
                    break;
                }
                case GET_ZONE_STASIS: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.getZoneStasis");
                    break;
                }
                case HAS_SUMMON_WITH_BREED: {
                    final String monsterName = CastableDescriptionGenerator.m_utilityDelegate.getMonsterName(((HasSummonWithBreed)object).getBreedId());
                    if (((SimpleCriterion)object).isNegated()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.nothasSummonWithBreed", monsterName);
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.hasSummonWithBreed", monsterName);
                    break;
                }
                case IS_CARRYING_OWN_BARREL: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.isCarryingOwnBarrel");
                    break;
                }
                case IS_CARRIED: {
                    final boolean negated2 = ((SimpleCriterion)object).isNegated();
                    if (negated2) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.not.isCarried");
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.isCarried");
                    break;
                }
                case HAS_SURROUNDING_CELL_WITH_OWN_BARREL: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.hasSurrondingCellWithOwnBarrel");
                    break;
                }
                case IS_TARGET_CELL_VALID_FOR_NEW_OBSTACLE: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.isTargetCellValidForNewObstacle");
                    break;
                }
                case NB_BOMBS: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.nbBombs");
                    break;
                }
                case GETDISTANCEBETWEENTARGETANDNEARESTALLYBEACON: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.distanceBetweenTargetAndNearestAllyBeacon");
                    break;
                }
                case GET_GUILD_LEVEL: {
                    description = CastableDescriptionGenerator.m_translator.getString("critere.guildLevel");
                    break;
                }
                case HAS_GUILD_BONUS: {
                    final HasGuildBonus hasGuildBonus = (HasGuildBonus)object;
                    final String bonusName = CastableDescriptionGenerator.m_utilityDelegate.getBonusName(hasGuildBonus.getBonusId());
                    description = CastableDescriptionGenerator.m_translator.getString("critere.guildBonus", bonusName);
                    break;
                }
                case HASEQUIPMENTID: {
                    final HasEquipmentId hasEquipmentId = (HasEquipmentId)object;
                    final TIntArrayList ids = hasEquipmentId.getIds();
                    final StringBuilder sb2 = new StringBuilder();
                    for (int j = 0, size = ids.size(); j < size; ++j) {
                        if (j != 0) {
                            sb2.append(", ");
                        }
                        sb2.append(CastableDescriptionGenerator.m_utilityDelegate.getItemName(ids.get(j)));
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.hasEquipmentId", sb2.toString());
                    break;
                }
                case GETNATIONID: {
                    description = String.valueOf(CastableDescriptionGenerator.m_utilityDelegate.getNationId());
                    break;
                }
                case HAS_UNLOCKED_COMPANION: {
                    final String monsterName = CastableDescriptionGenerator.m_utilityDelegate.getMonsterName(((HasUnlockedCompanion)object).getBreedId());
                    if (((SimpleCriterion)object).isNegated()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.notHasUnlockedCompanion", monsterName);
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.hasUnlockedCompanion", monsterName);
                    break;
                }
                case IS_COMPANION: {
                    if (((SimpleCriterion)object).isNegated()) {
                        description = CastableDescriptionGenerator.m_translator.getString("critere.isCompanionNegated");
                        break;
                    }
                    description = CastableDescriptionGenerator.m_translator.getString("critere.isCompanion");
                    break;
                }
                case GET_STATE_COUNT_IN_RANGE: {
                    final GetStateCountInRange crit26 = (GetStateCountInRange)object;
                    final State state = StateManager.getInstance().getState((int)crit26.getStateId());
                    description = CastableDescriptionGenerator.m_translator.getString("critere.getStateCountInRange", state.getName(), crit26.getStateLevel(), crit26.getMaxRange());
                    break;
                }
                case GET_OWN_TEAM_STATE_COUNT_IN_RANGE: {
                    final GetStateCountInRange crit26 = (GetStateCountInRange)object;
                    final State state = StateManager.getInstance().getState((int)crit26.getStateId());
                    description = CastableDescriptionGenerator.m_translator.getString("critere.getOwnTeamStateCountInRange", state.getName(), crit26.getStateLevel(), crit26.getMaxRange());
                    break;
                }
                case HAS_SUBSCRIPTION_LEVEL: {
                    final HasSubscriptionLevel crit27 = (HasSubscriptionLevel)object;
                    String levelList = "";
                    for (final int level : crit27.getSubscriptionLevels().toArray()) {
                        levelList = levelList + CastableDescriptionGenerator.m_utilityDelegate.getSubscriptionLevelName(level) + ",";
                    }
                    levelList = levelList.substring(0, levelList.length() - 1) + ".";
                    description = CastableDescriptionGenerator.m_translator.getString("critere.hasSubscriptionLevel", levelList);
                    break;
                }
                case HASPVPRANK: {
                    final HasPvpRank crit28 = (HasPvpRank)object;
                    final byte pvpRankId = crit28.getPvpRankId();
                    final NationPvpRanks rank = NationPvpRanks.getById(pvpRankId);
                    final String pvpRankName = CastableDescriptionGenerator.m_utilityDelegate.getPvpRankName(rank);
                    description = CastableDescriptionGenerator.m_translator.getString("critere.hasPvpRank", pvpRankName);
                    break;
                }
                case GETITEMQUANTITY: {
                    final GetItemQuantity crit29 = (GetItemQuantity)object;
                    final TLongArrayList itemIds = crit29.getItemIds(null, null, null, null);
                    String itemsList = "";
                    for (final long itemId : itemIds.toNativeArray()) {
                        itemsList = itemsList + CastableDescriptionGenerator.m_utilityDelegate.getItemName((int)itemId) + ",";
                    }
                    itemsList = (description = itemsList.substring(0, itemsList.length() - 1) + ".");
                    break;
                }
                case IS_ACHIEVEMENT_COMPLETE: {
                    final IsAchievementComplete crit30 = (IsAchievementComplete)object;
                    final String achievementName = CastableDescriptionGenerator.m_utilityDelegate.getAchievementName(crit30.getAchievementId());
                    description = CastableDescriptionGenerator.m_translator.getString("critere.isAchievementComplete", achievementName);
                    break;
                }
                default: {
                    description = "";
                    break;
                }
            }
        }
        if (object instanceof Targetable && ((Targetable)object).isTarget()) {
            description += CastableDescriptionGenerator.m_translator.getString("critere.target");
        }
        return description;
    }
    
    private static String getElementText(final byte elementId) {
        return CastableDescriptionGenerator.m_translator.getString(Elements.getElementFromId(elementId).getEnumLabel());
    }
    
    public static ArrayList<ObjectPair<String, SimpleCriterion>> splitCriterion(final SimpleCriterion crit) {
        final ArrayList<ObjectPair<String, SimpleCriterion>> temp = new ArrayList<ObjectPair<String, SimpleCriterion>>();
        final LinkedList<SimpleCriterion> list = splitAnd(crit);
        if (list != null) {
            for (final SimpleCriterion scrit : list) {
                final String s = getDescription(scrit);
                if (s.length() > 0) {
                    temp.add(new ObjectPair<String, SimpleCriterion>(s, scrit));
                }
            }
        }
        return temp;
    }
    
    private static String equalCriterion(final ParserObject left, final ParserObject right, final boolean equals) {
        final String rightValue = getDescription(right);
        if (rightValue.length() == 0) {
            return "";
        }
        if (left.getEnum() instanceof WakfuCriterionIds) {
            final WakfuCriterionIds type = (WakfuCriterionIds)left.getEnum();
            switch (type) {
                case GETNATIONALIGNMENT: {
                    final GetNationAlignment getNationAlignment = (GetNationAlignment)left;
                    final String nation1 = CastableDescriptionGenerator.m_utilityDelegate.getNationName(PrimitiveConverter.getInteger(getDescription(getNationAlignment.getNation1())));
                    final String nation2 = CastableDescriptionGenerator.m_utilityDelegate.getNationName(PrimitiveConverter.getInteger(getDescription(getNationAlignment.getNation2())));
                    final String alignment = CastableDescriptionGenerator.m_utilityDelegate.getAlignmentName(PrimitiveConverter.getByte(rightValue));
                    if (equals) {
                        return CastableDescriptionGenerator.m_translator.getString("critere.getNationAlignment", nation1, alignment, nation2);
                    }
                    return CastableDescriptionGenerator.m_translator.getString("critere.getNoNationAlignment", nation1, alignment, nation2);
                }
                case GETINSTANCEID: {
                    final String instanceName = CastableDescriptionGenerator.m_utilityDelegate.getInstanceName(PrimitiveConverter.getInteger(rightValue));
                    if (equals) {
                        return CastableDescriptionGenerator.m_translator.getString("critere.getInstanceId", instanceName);
                    }
                    return CastableDescriptionGenerator.m_translator.getString("critere.notGetInstanceId", instanceName);
                }
                case GETNATIONID: {
                    final String nationName = CastableDescriptionGenerator.m_utilityDelegate.getNationName(PrimitiveConverter.getInteger(rightValue));
                    if (equals) {
                        return CastableDescriptionGenerator.m_translator.getString("critere.getNationId", nationName);
                    }
                    return CastableDescriptionGenerator.m_translator.getString("critere.notGetNationId", nationName);
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        final String leftValue = getDescription(left);
        if (leftValue.length() == 0) {
            return "";
        }
        sb.append(leftValue);
        sb.append(equals ? " = " : " != ");
        sb.append(rightValue);
        return sb.toString();
    }
}
