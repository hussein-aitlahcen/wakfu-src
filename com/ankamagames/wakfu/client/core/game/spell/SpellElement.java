package com.ankamagames.wakfu.client.core.game.spell;

public class SpellElement implements Comparable
{
    private final Spell m_spell;
    private SpellLevel m_spellLevel;
    
    public SpellElement(final Spell spell, final SpellLevel spellLevel) {
        super();
        this.m_spell = spell;
        this.m_spellLevel = spellLevel;
    }
    
    public Spell getSpell() {
        return this.m_spell;
    }
    
    public SpellLevel getSpellLevel() {
        return this.m_spellLevel;
    }
    
    public void setSpellLevel(final SpellLevel spellLevel) {
        this.m_spellLevel = spellLevel;
    }
    
    @Override
    public int compareTo(final Object o) {
        if (o instanceof SpellElement) {
            return this.getSpell().getUiPosition() - ((SpellElement)o).getSpell().getUiPosition();
        }
        return 0;
    }
}
