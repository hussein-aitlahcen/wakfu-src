package com.ankamagames.wakfu.client.core.game.fight.history;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.core.*;

public class SpellXpGainFieldProvider extends ImmutableFieldProvider implements Comparable<SpellXpGainFieldProvider>
{
    public static final String SPELL_FIELD = "spell";
    public static final String SPELL_XP_VALUE_FIELD = "spellXpValue";
    public static final String SPELL_XP_GAIN_FIELD = "spellXpGain";
    public static final String SPELL_LEVEL_GAIN_FIELD = "spellLevelGain";
    public static final String[] FIELDS;
    private final XpModification m_xpModification;
    private XpValueFieldProvider m_xpValue;
    private FieldProvider m_skillOrSpell;
    
    public SpellXpGainFieldProvider(final SkillOrSpellXpModification skillOrSpellXpModification, final LocalPlayerCharacter player) {
        super();
        this.m_xpModification = skillOrSpellXpModification.getXpModification();
        this.readSkillOrSpellFrom(skillOrSpellXpModification.getSubject(), player);
    }
    
    private void readSkillOrSpellFrom(final SkillOrSpell skillOrSpell, final LocalPlayerCharacter player) {
        final long xpDiff = (this.m_xpModification != null) ? this.m_xpModification.getXpDifference() : -1L;
        if (skillOrSpell.isSkill()) {
            final Skill skill = player.getSkillInventory().getFirstWithReferenceId(skillOrSpell.getRefId());
            this.m_xpValue = new XpValueFieldProvider(skill.getXpTable(), skill.getActualLevel(), skill.getXp(), xpDiff, this.m_xpModification.getLevelDifference());
            this.m_skillOrSpell = skill;
        }
        else {
            final SpellLevel spellLevel = player.getSpellInventory().getFirstWithReferenceId(skillOrSpell.getRefId());
            this.m_xpValue = new XpValueFieldProvider(spellLevel.getXpTable(), spellLevel.getLevelWithoutGain(), spellLevel.getLevel(), spellLevel.getXp(), Long.valueOf(xpDiff), this.m_xpModification.getLevelDifference());
            this.m_skillOrSpell = spellLevel;
        }
    }
    
    @Override
    public String[] getFields() {
        return SpellXpGainFieldProvider.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if ("spell".equals(fieldName)) {
            return this.m_skillOrSpell;
        }
        if ("spellXpValue".equals(fieldName)) {
            return this.m_xpValue;
        }
        if ("spellXpGain".equals(fieldName)) {
            return this.xpGainField();
        }
        if ("spellLevelGain".equals(fieldName)) {
            return this.levelGainField();
        }
        return this.m_skillOrSpell.getFieldValue(fieldName);
    }
    
    private Object levelGainField() {
        final short difference = this.m_xpModification.getLevelDifference();
        return (difference > 0) ? WakfuTranslator.getInstance().getString("levelGain", difference) : null;
    }
    
    private Object xpGainField() {
        if (!this.m_xpModification.affectsTarget()) {
            return null;
        }
        final long xpDifference = this.m_xpModification.getXpDifference();
        if (xpDifference >= 0L) {
            return '+' + WakfuTranslator.getInstance().getString("xpGain", xpDifference);
        }
        return WakfuTranslator.getInstance().getString("xpGain", xpDifference);
    }
    
    long getXpDifference() {
        return this.m_xpModification.getXpDifference();
    }
    
    public XpModification getXpModification() {
        return this.m_xpModification;
    }
    
    @Override
    public int compareTo(final SpellXpGainFieldProvider other) {
        if (other == null) {
            return -1;
        }
        final int comparisonByLevelGain = Integer.signum(other.m_xpModification.getLevelDifference() - this.m_xpModification.getLevelDifference());
        if (comparisonByLevelGain != 0) {
            return comparisonByLevelGain;
        }
        return Long.signum(other.m_xpModification.getXpDifference() - this.m_xpModification.getXpDifference());
    }
    
    static {
        FIELDS = new String[] { "spell", "spellXpValue", "spellXpGain", "spellLevelGain" };
    }
}
