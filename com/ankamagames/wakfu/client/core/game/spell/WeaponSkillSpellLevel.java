package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.wakfu.common.rawData.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class WeaponSkillSpellLevel extends SpellLevel
{
    private final TIntHashSet m_skillsId;
    private final BasicCharacterInfo m_skillUser;
    private AbstractSkill m_higherLevelSkill;
    
    public WeaponSkillSpellLevel(final BasicCharacterInfo skillUser) {
        super();
        this.m_skillsId = new TIntHashSet();
        this.m_skillUser = skillUser;
    }
    
    public WeaponSkillSpellLevel(final BasicCharacterInfo skillUser, final Spell spell, final long uid, final List<Skill> skills) {
        super();
        this.m_skillsId = new TIntHashSet();
        this.m_skillUser = skillUser;
        this.m_spell = (Spell)spell;
        final Iterator<Skill> skillIterator = skills.iterator();
        while (skillIterator.hasNext()) {
            this.m_skillsId.add(skillIterator.next().getReferenceSkill().getId());
        }
        this.m_uid = uid;
    }
    
    @Override
    public boolean isInShortcutBar() {
        return true;
    }
    
    @Override
    public byte getType() {
        return 2;
    }
    
    @Override
    public boolean toRaw(final RawSpellLevel rawSpellLevel) {
        if (!super.toRaw(rawSpellLevel)) {
            return false;
        }
        rawSpellLevel.skills.clear();
        this.m_skillsId.forEach(new TIntProcedure() {
            @Override
            public boolean execute(final int skillId) {
                final RawSpellLevel.Skill rawSkillId = new RawSpellLevel.Skill();
                rawSkillId.skillId = skillId;
                rawSpellLevel.skills.add(rawSkillId);
                return true;
            }
        });
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawSpellLevel rawSpellLevel) {
        if (!super.fromRaw(rawSpellLevel)) {
            return false;
        }
        this.m_skillsId.clear();
        for (final RawSpellLevel.Skill skill : rawSpellLevel.skills) {
            this.m_skillsId.add(skill.skillId);
        }
        return true;
    }
    
    @Override
    public WeaponSkillSpellLevel getCopy(final boolean pooled) {
        final WeaponSkillSpellLevel level = new WeaponSkillSpellLevel(this.m_skillUser);
        this.copyInto(level, GUIDGenerator.getGUID());
        return level;
    }
    
    @Override
    public WeaponSkillSpellLevel getClone() {
        final WeaponSkillSpellLevel level = new WeaponSkillSpellLevel(this.m_skillUser);
        this.copyInto(level, this.m_uid);
        return level;
    }
    
    private void copyInto(final WeaponSkillSpellLevel level, final long uid) {
        level.m_uid = uid;
        level.m_spell = this.m_spell;
        this.m_skillsId.forEach(new TIntProcedure() {
            @Override
            public boolean execute(final int skillId) {
                level.m_skillsId.add(skillId);
                return true;
            }
        });
    }
    
    public List<Skill> getSkills() {
        final List<Skill> skills = new ArrayList<Skill>(this.m_skillsId.size());
        this.m_skillsId.forEach(new TIntProcedure() {
            @Override
            public boolean execute(final int skillId) {
                final Skill skill = WeaponSkillSpellLevel.this.m_skillUser.getSkillInventory().getFirstWithReferenceId(skillId);
                if (skill != null) {
                    skills.add(skill);
                }
                return true;
            }
        });
        return skills;
    }
    
    @Override
    public short getLevel() {
        if (this.m_skillUser == null || this.m_skillUser.getSkillInventory() == null) {
            return 0;
        }
        short level = 0;
        for (final Skill skill : this.getSkills()) {
            if (skill.getActualLevel() > level) {
                level = skill.getActualLevel();
                this.m_higherLevelSkill = skill;
            }
        }
        return level;
    }
    
    @Override
    public long getXp() {
        if (this.m_higherLevelSkill == null) {
            return 0L;
        }
        return this.m_higherLevelSkill.getXp();
    }
    
    @Override
    public float getCurrentLevelPercentage() {
        return this.getXpTable().getPercentageInLevel(this.getLevel(), this.getXp());
    }
    
    @Override
    public String getCurrentLevelString() {
        return new StringBuilder().append(this.getXpTable().getXpInLevel(this.getXp())).append('/').append(this.getXpTable().getLevelExtent(this.getLevel())).toString();
    }
}
