package com.ankamagames.wakfu.client.core.game.skill;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.skill.*;

public class Skill extends AbstractSkill<ReferenceSkill> implements FieldProvider, Comparable
{
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String LEVEL_FIELD = "level";
    public static final String LEVEL_TEXT_SHORT_FIELD = "levelTextShort";
    public static final String XP_FIELD = "xp";
    public static final String CURRENT_LEVEL_PERCENTAGE_FIELD = "currentLevelPercentage";
    public static final String CURRENT_LEVEL_PERCENTAGE_STRING_FIELD = "currentLevelPercentageString";
    public static final String CURRENT_LEVEL_STRING_FIELD = "currentLevelString";
    public static final String SKILL_TYPE_FIELD = "skillType";
    public static final String SMALL_ICON_URL_FIELD = "smallIconUrl";
    public static final String BIG_ICON_URL_FIELD = "bigIconUrl";
    public static final String SKILL_INFO_FIELD = "skillInfo";
    public static final String SKILL_RANK_FIELD = "skillRank";
    public static final String SKILL_XP_RATIO_FIELD = "skillXpRatio";
    public static final String SKILL_EFFECTS = "skillEffects";
    public static final String[] FIELDS;
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public String[] getFields() {
        return Skill.FIELDS;
    }
    
    @Override
    protected AbstractSkill<ReferenceSkill> newInstance() {
        return new Skill();
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equalsIgnoreCase("id")) {
            return this.getReferenceSkill().getId();
        }
        if (fieldName.equalsIgnoreCase("level")) {
            return this.getActualLevel();
        }
        if (fieldName.equals("levelTextShort")) {
            return WakfuTranslator.getInstance().getString("levelShort.custom", this.getLevel());
        }
        if (fieldName.equalsIgnoreCase("xp")) {
            return this.getXp();
        }
        if (fieldName.equalsIgnoreCase("currentLevelPercentage")) {
            return this.getCurrentLevelPercentage();
        }
        if (fieldName.equalsIgnoreCase("currentLevelPercentageString")) {
            return String.format("%.2f%%", this.getCurrentLevelPercentage() * 100.0f);
        }
        if (fieldName.equals("currentLevelString")) {
            return "xp : " + this.getCurrentLevelString();
        }
        if (fieldName.equalsIgnoreCase("skillType")) {
            return this.getReferenceSkill().getType().getTypeId();
        }
        if (fieldName.equalsIgnoreCase("smallIconUrl")) {
            return WakfuConfiguration.getInstance().getCollectSkillSmallIcon(this.getReferenceId());
        }
        if (fieldName.equalsIgnoreCase("bigIconUrl")) {
            return WakfuConfiguration.getInstance().getCollectSkillBigIcon(this.getReferenceSkill().getGfxId());
        }
        if (fieldName.equalsIgnoreCase("name")) {
            if (this.getReferenceSkill() != null) {
                return this.getReferenceSkill().getName();
            }
        }
        else if (fieldName.equalsIgnoreCase("skillInfo")) {
            if (this.getReferenceSkill() != null) {
                return WakfuTranslator.getInstance().getString("craft.frame.skillInfo", this.getReferenceSkill().getName(), this.getActualLevel());
            }
        }
        else {
            if (fieldName.equalsIgnoreCase("skillRank")) {
                final short level = this.getRankLevel();
                final StringBuilder sb = new StringBuilder();
                sb.append("craft.frame.skillRank.").append(level);
                return WakfuTranslator.getInstance().getString(sb.toString());
            }
            if (fieldName.equalsIgnoreCase("skillXpRatio")) {
                return this.getCurrentLevelString();
            }
            if (fieldName.equalsIgnoreCase("skillEffects") && this.getReferenceSkill() != null && this.getReferenceSkill().getType() == SkillType.WEAPON_SKILL) {
                final StringBuilder effectsString = new StringBuilder();
                if (!this.getReferenceSkill().getAssociatedItemTypes().contains(219)) {
                    effectsString.append(WakfuTranslator.getInstance().getString("weaponSkill.firstEffect", this.getActualLevel())).append("\n\n");
                }
                effectsString.append(WakfuTranslator.getInstance().getString("weaponSkill.secondEffect", this.getActualLevel()));
                return effectsString.toString();
            }
        }
        return null;
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
        final Skill skill = (Skill)o;
        return this.getReferenceSkill().getName().compareTo(skill.getReferenceSkill().getName());
    }
    
    @Override
    public AbstractReferenceSkillManager<ReferenceSkill> getReferenceSkillManager() {
        return ReferenceSkillManager.getInstance();
    }
    
    static {
        FIELDS = new String[] { "id", "name", "level", "levelTextShort", "xp", "currentLevelPercentage", "currentLevelPercentageString", "currentLevelString", "skillType", "smallIconUrl", "bigIconUrl", "skillInfo", "skillRank", "skillXpRatio", "skillEffects" };
    }
}
