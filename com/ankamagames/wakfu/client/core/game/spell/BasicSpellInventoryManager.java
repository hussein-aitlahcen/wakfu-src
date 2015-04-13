package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.regex.*;
import com.ankamagames.xulor2.property.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public abstract class BasicSpellInventoryManager implements FieldProvider
{
    private static final Logger m_logger;
    public static final String SPELLS_FIELD = "spells";
    public static final Pattern POSTIONNNED_SPELL_PATTERN;
    private SpellInventory<SpellLevel> m_temporarySpellInventory;
    public static final String[] FIELDS;
    
    public BasicSpellInventoryManager() {
        super();
        this.m_temporarySpellInventory = null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("spells")) {
            return this.getSpellLevels();
        }
        final Matcher matcher = BasicSpellInventoryManager.POSTIONNNED_SPELL_PATTERN.matcher(fieldName);
        if (matcher.matches()) {
            final short position = Short.valueOf(matcher.group(1));
            return this.getSpellLevelByPosition(position);
        }
        return null;
    }
    
    public SpellLevel getFirstSpellLevel() {
        final short position = 32767;
        SpellLevel sl = null;
        for (final SpellLevel spellLevel : this.getSpellLevels()) {
            if (spellLevel.getSpell().getUiPosition() < position) {
                sl = spellLevel;
            }
        }
        return sl;
    }
    
    public SpellLevel getSpellLevelByPosition(final short position) {
        for (final SpellLevel spellLevel : this.getSpellLevels()) {
            if (spellLevel.getSpell().getUiPosition() == position) {
                return spellLevel;
            }
        }
        return null;
    }
    
    public void updateSpellsField() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "spells");
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public abstract Iterable<SpellLevel> getSpellLevels();
    
    public SpellInventory<SpellLevel> getTemporarySpellInventory() {
        return this.m_temporarySpellInventory;
    }
    
    public boolean hasTemporarySpellInventory() {
        return this.m_temporarySpellInventory != null;
    }
    
    public void initTemporarySpellInventory(final List<SpellLevel> spellLevels, final BasicCharacterInfo spellLevelUser) {
        this.createTemporarySpellInventory(spellLevelUser);
        for (final SpellLevel spellLevel : spellLevels) {
            try {
                this.m_temporarySpellInventory.add(spellLevel);
            }
            catch (InventoryCapacityReachedException e) {
                BasicSpellInventoryManager.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
            catch (ContentAlreadyPresentException e2) {
                BasicSpellInventoryManager.m_logger.error((Object)"Exception levee", (Throwable)e2);
            }
        }
    }
    
    public void createTemporarySpellInventory(final BasicCharacterInfo spellLevelUser) {
        this.m_temporarySpellInventory = new SpellInventory<SpellLevel>((short)40, new SpellLevelProvider(spellLevelUser), null, false, false, false);
    }
    
    public void resetTemporarySpellInventory() {
        if (this.m_temporarySpellInventory == null) {
            return;
        }
        this.m_temporarySpellInventory.destroyAll();
        this.m_temporarySpellInventory = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BasicSpellInventoryManager.class);
        POSTIONNNED_SPELL_PATTERN = Pattern.compile("spell([0-9]+)");
        FIELDS = new String[] { "spells" };
    }
}
