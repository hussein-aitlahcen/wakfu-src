package com.ankamagames.wakfu.common.game.spell;

import org.apache.log4j.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;

public abstract class AbstractSpellManager<Spell extends AbstractSpell>
{
    protected static AbstractSpellManager m_instance;
    protected static final Logger m_logger;
    protected final TIntObjectHashMap<Spell> m_spells;
    protected final TLongObjectHashMap<AbstractSpellLevel> m_defaultSpellLevels;
    
    protected AbstractSpellManager() {
        super();
        this.m_spells = new TIntObjectHashMap<Spell>();
        this.m_defaultSpellLevels = new TLongObjectHashMap<AbstractSpellLevel>();
    }
    
    public static AbstractSpellManager getInstance() {
        if (AbstractSpellManager.m_instance == null) {
            throw new IllegalStateException("m_instance == null : pas d'instance de SpellManager concret");
        }
        return AbstractSpellManager.m_instance;
    }
    
    public void addSpell(final Spell spell) {
        this.m_spells.put(spell.getId(), spell);
    }
    
    protected TIntObjectHashMap<Spell> getSpells() {
        return this.m_spells;
    }
    
    @Nullable
    public Spell getSpell(final int spellId) {
        return this.m_spells.get(spellId);
    }
    
    public boolean isEmpty() {
        return this.m_spells.isEmpty();
    }
    
    public abstract AbstractSpellLevel getDefaultSpellLevel(final int p0, final short p1);
    
    static {
        AbstractSpellManager.m_instance = null;
        m_logger = Logger.getLogger((Class)AbstractSpellManager.class);
    }
}
