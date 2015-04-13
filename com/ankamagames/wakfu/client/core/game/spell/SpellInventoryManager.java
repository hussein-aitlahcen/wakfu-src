package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import gnu.trove.*;

public class SpellInventoryManager extends BasicSpellInventoryManager implements FieldProvider
{
    private static final Logger m_logger;
    public static final String ELEMENT_0_FIELD = "element0";
    public static final String ELEMENT_1_FIELD = "element1";
    public static final String ELEMENT_2_FIELD = "element2";
    public static final String ELEMENT_SUPPORT_FIELD = "elementSupport";
    public static final String ELEMENT_0_SPELLS_FIELD = "element0Spells";
    public static final String ELEMENT_1_SPELLS_FIELD = "element1Spells";
    public static final String ELEMENT_2_SPELLS_FIELD = "element2Spells";
    public static final String ELEMENT_SUPPORT_SPELLS_FIELD = "elementSupportSpells";
    public static final String GLOBAL_LEVEL = "globalLevel";
    public static final String[] FIELDS;
    public static final String[] ALL_FIELDS;
    protected SpellInventory<SpellLevel> m_spellInventory;
    protected ElementReference[] m_elementReferences;
    protected CharacterInfo m_characterInfo;
    
    public SpellInventoryManager(final CharacterInfo info) {
        super();
        this.m_spellInventory = new SpellInventory<SpellLevel>((short)40, new SpellLevelProvider(info), null, false, false, false);
        this.m_characterInfo = info;
    }
    
    public void initialize() {
        final AvatarBreedInfo info = this.m_characterInfo.getBreedInfo();
        final TByteObjectIterator<TLongObjectHashMap<SpellElement>> it = info.getSpellIterator();
        while (it.hasNext()) {
            it.advance();
            final TLongObjectIterator<SpellElement> it2 = it.value().iterator();
            while (it2.hasNext()) {
                it2.advance();
                final SpellElement spellElement = it2.value();
                final SpellLevel spellLevel = this.m_spellInventory.getFirstWithReferenceId(spellElement.getSpell().getId());
                if (spellLevel != null) {
                    spellElement.setSpellLevel(spellLevel);
                }
                else {
                    spellElement.setSpellLevel(null);
                }
            }
        }
        this.m_elementReferences = ElementReference.getElementReferencesFromElements(info.getBreed());
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName == null) {
            return null;
        }
        if (fieldName.equals("element0")) {
            return this.m_elementReferences[0];
        }
        if (fieldName.equals("element1")) {
            return this.m_elementReferences[1];
        }
        if (fieldName.equals("element2")) {
            return this.m_elementReferences[2];
        }
        if (fieldName.equals("elementSupport")) {
            return this.m_elementReferences[3];
        }
        if (fieldName.equals("element0Spells")) {
            return this.getSpellsByElementId((byte)0);
        }
        if (fieldName.equals("element1Spells")) {
            return this.getSpellsByElementId((byte)1);
        }
        if (fieldName.equals("element2Spells")) {
            return this.getSpellsByElementId((byte)2);
        }
        if (fieldName.equals("elementSupportSpells")) {
            return this.getSpellsByElementId((byte)3);
        }
        if (fieldName.equals("globalLevel")) {
            final int globalLevel = this.getGlobalLevel();
            return WakfuTranslator.getInstance().getString("desc.globalSpellLevel", globalLevel);
        }
        return super.getFieldValue(fieldName);
    }
    
    public List getSpellsByElementId(final byte elementIndex) {
        return this.m_characterInfo.getBreedInfo().getSpellsByElementId(this.m_elementReferences[elementIndex].getElement());
    }
    
    @Override
    public String[] getFields() {
        return SpellInventoryManager.FIELDS;
    }
    
    public boolean add(final SpellLevel spellLevel) {
        try {
            this.m_spellInventory.add(spellLevel);
            return true;
        }
        catch (InventoryCapacityReachedException e) {
            SpellInventoryManager.m_logger.error((Object)"[SPELL] Plus de place dans l'inventaire des sort pour en apprendre un nouveau");
        }
        catch (ContentAlreadyPresentException e2) {
            SpellInventoryManager.m_logger.error((Object)"[SPELL] Sort d\u00e9j\u00e0 appri et pr\u00e9sent dans l'inventaire");
        }
        return false;
    }
    
    public SpellLevel getSeductionSpell() {
        return this.m_spellInventory.getFirstWithReferenceId(730);
    }
    
    public String getSpellTreeLevelString(final byte elementId) {
        return WakfuTranslator.getInstance().getString("levelShort.custom", this.getSpellTreeLevel(elementId));
    }
    
    public int getSpellTreeLevel(final byte elementId) {
        int treeLevel = 0;
        for (final AbstractSpellLevel spellLevel : this.m_spellInventory) {
            if (spellLevel.getSpell().getElementId() == elementId) {
                treeLevel += spellLevel.getLevel();
            }
        }
        return treeLevel;
    }
    
    public SpellLevel getSpellLevelById(final long id) {
        return this.getSpellInventory().getWithUniqueId(id);
    }
    
    public SpellInventory<SpellLevel> getSpellInventory() {
        if (this.hasTemporarySpellInventory()) {
            return this.getTemporarySpellInventory();
        }
        return this.m_spellInventory;
    }
    
    public SpellInventory<SpellLevel> getPermanentSpellInventory() {
        return this.m_spellInventory;
    }
    
    @Override
    public Iterable<SpellLevel> getSpellLevels() {
        return (Iterable<SpellLevel>)this.getSpellInventory();
    }
    
    public SpellLevel getFirstWeaponSkillSpellLevel() {
        for (final SpellLevel spellLevel : this.m_spellInventory) {
            if (spellLevel instanceof WeaponSkillSpellLevel) {
                return spellLevel;
            }
        }
        return null;
    }
    
    public int getGlobalLevel() {
        int globalLevel = 0;
        for (final AbstractSpellLevel spellLevel : this.m_spellInventory) {
            globalLevel += spellLevel.getLevel();
        }
        return globalLevel;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpellInventoryManager.class);
        FIELDS = new String[] { "element0", "element1", "element2", "elementSupport", "element0Spells", "element1Spells", "element2Spells", "elementSupportSpells", "globalLevel" };
        ALL_FIELDS = new String[BasicSpellInventoryManager.FIELDS.length + SpellInventoryManager.FIELDS.length];
        System.arraycopy(SpellInventoryManager.FIELDS, 0, SpellInventoryManager.ALL_FIELDS, 0, SpellInventoryManager.FIELDS.length);
        System.arraycopy(BasicSpellInventoryManager.FIELDS, 0, SpellInventoryManager.ALL_FIELDS, SpellInventoryManager.FIELDS.length, BasicSpellInventoryManager.FIELDS.length);
    }
}
