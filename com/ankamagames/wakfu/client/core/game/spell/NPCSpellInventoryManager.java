package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public class NPCSpellInventoryManager extends BasicSpellInventoryManager
{
    private HashMap<Long, SpellLevel> m_spells;
    
    public NPCSpellInventoryManager() {
        super();
        this.m_spells = new HashMap<Long, SpellLevel>();
    }
    
    @Override
    public String[] getFields() {
        return BasicSpellInventoryManager.FIELDS;
    }
    
    @Override
    public Iterable<SpellLevel> getSpellLevels() {
        if (!this.hasTemporarySpellInventory()) {
            return this.m_spells.values();
        }
        return (Iterable<SpellLevel>)this.getTemporarySpellInventory();
    }
    
    public SpellLevel getSpellLevelById(final long uid) {
        if (!this.hasTemporarySpellInventory()) {
            return this.m_spells.get(uid);
        }
        return this.getTemporarySpellInventory().getWithUniqueId(uid);
    }
    
    public void initialize(final ArrayList<IntObjectPair<Spell>> spells, final short npcLevel) {
        for (final IntObjectPair<Spell> spellAndLevel : spells) {
            final Spell spell = spellAndLevel.getSecond();
            final int level = (spellAndLevel.getFirst() == -1) ? npcLevel : spellAndLevel.getFirst();
            final SpellLevel spellLevel = new SpellLevel(spell, (short)level, spell.getId());
            this.m_spells.put(spellLevel.getUniqueId(), spellLevel);
        }
    }
    
    public void addSpellLevel(final SpellLevel spellLevel) {
        this.m_spells.put(spellLevel.getUniqueId(), spellLevel);
    }
    
    public void clear() {
        this.m_spells.clear();
    }
}
