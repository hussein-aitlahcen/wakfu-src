package com.ankamagames.wakfu.client.core.game.shortcut;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SymbiotShortcutBar extends ShortcutBar
{
    private CharacterInfo m_currentSymbiot;
    private static final Comparator<SpellLevel> DOUBLE_SPELL_COMPARATOR;
    private boolean m_forceClean;
    
    public SymbiotShortcutBar(final byte index) {
        super(ShortCutBarType.SYMBIOT_BAR, index);
        this.m_currentSymbiot = null;
        this.m_forceClean = false;
    }
    
    private void tryToCopyControllerShortcutBar(final ArrayList<SpellLevel> spellLevels, final CharacterInfo controlledCharacter) {
        final CharacterInfo controller = controlledCharacter.getController();
        if (controller == null) {
            return;
        }
        final ShortcutInventory<ShortCutItem> controllerBar = (ShortcutInventory<ShortCutItem>)controller.getShortcutInventory(ShortCutBarType.FIGHT, (byte)0);
        if (controllerBar == null) {
            return;
        }
        final ShortCutItem fromPosition = controllerBar.getFromPosition((short)0);
        final ShortCutItem fromPosition2 = controllerBar.getFromPosition((short)1);
        final Iterator<SpellLevel> it = spellLevels.iterator();
        while (it.hasNext()) {
            final SpellLevel spellLevel = it.next();
            final short position = controllerBar.getPosition(spellLevel.getUniqueId());
            if (position == -1) {
                continue;
            }
            if (spellLevel.getSpell().isPassive()) {
                continue;
            }
            final ShortCutItem shortcut = ShortCutItem.checkOut(ShortCutType.SPELL_LEVEL, spellLevel.getUniqueId(), spellLevel.getReferenceId(), spellLevel.getGfxId());
            if (this.setShortcutItemAt(shortcut, position)) {
                it.remove();
            }
            else {
                shortcut.release();
            }
        }
    }
    
    public void setControlledCharacter(final CharacterInfo controlledCharacter) {
        if (!this.m_forceClean && controlledCharacter == this.m_currentSymbiot) {
            return;
        }
        this.m_forceClean = false;
        this.clean();
        this.m_currentSymbiot = controlledCharacter;
        final ArrayList<SpellLevel> spellLevels = new ArrayList<SpellLevel>();
        final Iterable<SpellLevel> characterSpells = controlledCharacter.getSpellLevels();
        if (characterSpells == null) {
            return;
        }
        for (final SpellLevel sl : characterSpells) {
            spellLevels.add(sl);
        }
        this.tryToCopyControllerShortcutBar(spellLevels, controlledCharacter);
        for (final SpellLevel spell : spellLevels) {
            if (spell.getSpell().isPassive()) {
                continue;
            }
            final ShortCutItem shortcut = ShortCutItem.checkOut(ShortCutType.SPELL_LEVEL, spell.getUniqueId(), spell.getReferenceId(), spell.getGfxId());
            if (this.isPositionFree(spell.getSpell().getUiPosition())) {
                this.setShortcutItemAt(shortcut, spell.getSpell().getUiPosition());
            }
            else {
                final short firstFreeIndex = this.getFirstFreeIndex();
                this.setShortcutItemAt(shortcut, firstFreeIndex);
            }
        }
    }
    
    public void forceClean() {
        this.m_forceClean = true;
    }
    
    static {
        DOUBLE_SPELL_COMPARATOR = new Comparator<SpellLevel>() {
            @Override
            public int compare(final SpellLevel o1, final SpellLevel o2) {
                final Elements element1 = o1.getElement();
                final Elements element2 = o2.getElement();
                final byte elementId1 = element1.getId();
                final byte elementId2 = element2.getId();
                if (elementId1 != elementId2) {
                    return (elementId1 < elementId2) ? -1 : 1;
                }
                return (o1.getLevel() < o2.getLevel()) ? -1 : 1;
            }
        };
    }
}
