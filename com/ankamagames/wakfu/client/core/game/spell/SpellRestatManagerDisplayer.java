package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.restat.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;
import com.ankamagames.framework.reflect.*;
import gnu.trove.*;

public class SpellRestatManagerDisplayer extends ImmutableFieldProvider
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
    public static final String GLOBAL_RESTAT_FIELD = "globalRestat";
    public static final String GLOBAL_XP_VALUE_FIELD = "globalXpValue";
    public static final String GLOBAL_XP_TEXT_FIELD = "globalXpText";
    public static final String CAN_VALID_FIELD = "canValid";
    public static final String IS_DIRTY_FIELD = "isDirty";
    public static final String[] FIELDS;
    public static final String[] ALL_FIELDS;
    protected ElementReference[] m_elementReferences;
    protected CharacterInfo m_characterInfo;
    protected SpellRestatComputer m_spellRestatComputer;
    private TByteObjectHashMap<ArrayList> m_spells;
    private static final Comparator staticSpellOrLevelComparator;
    
    public SpellRestatManagerDisplayer(final CharacterInfo characterInfo, final SpellRestatComputer spellRestatComputer) {
        super();
        this.m_spellRestatComputer = spellRestatComputer;
        this.m_characterInfo = characterInfo;
    }
    
    public void initialize() {
        final AvatarBreedInfo info = this.m_characterInfo.getBreedInfo();
        this.m_spells = new TByteObjectHashMap<ArrayList>();
        this.refreshSpells();
        final ElementReference[] elements = ElementReference.getElementReferencesFromElements(info.getBreed());
        this.m_elementReferences = new ElementReference[elements.length];
        for (int i = 0; i < this.m_elementReferences.length; ++i) {
            final ElementReference element = elements[i];
            if (this.m_spellRestatComputer.isElementConcerned(element.m_element)) {
                this.m_elementReferences[i] = new RestatElementReference(element.m_element);
            }
            else {
                this.m_elementReferences[i] = element;
            }
        }
        PropertiesProvider.getInstance().setPropertyValue("spellsRestatManager", this);
    }
    
    private void refreshSpells() {
        final SpellLevel describedSpellLevel = (SpellLevel)PropertiesProvider.getInstance().getObjectProperty("editableDescribedSpell", "spellsRestatDialog");
        this.m_spells.clear();
        final AvatarBreedInfo info = this.m_characterInfo.getBreedInfo();
        final TByteObjectIterator<TLongObjectHashMap<SpellElement>> it = info.getSpellIterator();
        while (it.hasNext()) {
            it.advance();
            final byte elementId = it.key();
            final ArrayList spells = new ArrayList();
            final TLongObjectIterator<SpellElement> it2 = it.value().iterator();
            while (it2.hasNext()) {
                it2.advance();
                final SpellElement spellElement = it2.value();
                final SpellLevel spellLevel = WakfuGameEntity.getInstance().getLocalPlayer().getSpellInventory().getFirstWithReferenceId(spellElement.getSpell().getId());
                final Elements elements = Elements.getElementFromId(elementId);
                if (this.m_spellRestatComputer.isElementConcerned(elements)) {
                    final RestatSpellLevel restatSpellLevel = new RestatSpellLevel(spellElement.getSpell());
                    spells.add(restatSpellLevel);
                    this.m_spells.put(elementId, spells);
                    if (describedSpellLevel == null || describedSpellLevel.getSpell().getId() != restatSpellLevel.getSpell().getId()) {
                        continue;
                    }
                    PropertiesProvider.getInstance().setLocalPropertyValue("editableDescribedSpell", restatSpellLevel.getRealSpellLevel().getCopy(false, true), "spellsRestatDialog");
                }
                else if (spellLevel != null) {
                    spells.add(spellLevel);
                }
                else {
                    spells.add(spellElement.getSpell());
                }
            }
            Collections.sort((List<Object>)spells, SpellRestatManagerDisplayer.staticSpellOrLevelComparator);
            this.m_spells.put(elementId, spells);
        }
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
            return this.m_spells.get(this.m_elementReferences[0].getId());
        }
        if (fieldName.equals("element1Spells")) {
            return this.m_spells.get(this.m_elementReferences[1].getId());
        }
        if (fieldName.equals("element2Spells")) {
            return this.m_spells.get(this.m_elementReferences[2].getId());
        }
        if (fieldName.equals("elementSupportSpells")) {
            return this.m_spells.get(this.m_elementReferences[3].getId());
        }
        if (fieldName.equals("globalRestat")) {
            return this.m_spellRestatComputer.getRestatType() == SpellRestatComputer.RestatType.GLOBAL;
        }
        if (fieldName.equals("globalXpValue")) {
            return this.m_spellRestatComputer.getRemainingXpToDistribute() / this.m_spellRestatComputer.getTotalXpToDistribute();
        }
        if (fieldName.equals("globalXpText")) {
            return WakfuTranslator.getInstance().getString("xp.remaining", this.m_spellRestatComputer.getRemainingXpToDistribute());
        }
        if (fieldName.equals("canValid")) {
            return this.m_spellRestatComputer.canValidateRestat();
        }
        if (fieldName.equals("isDirty")) {
            return this.m_spellRestatComputer.getTotalAlreadyDistributeXp() > 0L;
        }
        return null;
    }
    
    public Iterable<SpellLevel> getSpellLevelsForElement(final Elements elements) {
        final ArrayList<SpellLevel> spellLevels = new ArrayList<SpellLevel>();
        final ArrayList spells = this.m_spells.get(elements.getId());
        for (final Object spell : spells) {
            if (spell instanceof RestatSpellLevel) {
                spellLevels.add(((RestatSpellLevel)spell).getRealSpellLevel());
            }
            else {
                if (!(spell instanceof SpellLevel)) {
                    continue;
                }
                spellLevels.add((SpellLevel)spell);
            }
        }
        return spellLevels;
    }
    
    @Override
    public String[] getFields() {
        return SpellRestatManagerDisplayer.FIELDS;
    }
    
    public SpellRestatComputer getSpellRestatComputer() {
        return this.m_spellRestatComputer;
    }
    
    public void refresh() {
        this.refreshSpells();
        for (final ElementReference elementReference : this.m_elementReferences) {
            PropertiesProvider.getInstance().firePropertyValueChanged(elementReference, elementReference.getFields());
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, SpellRestatManagerDisplayer.FIELDS);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpellRestatManagerDisplayer.class);
        FIELDS = new String[] { "element0", "element1", "element2", "elementSupport", "element0Spells", "element1Spells", "element2Spells", "elementSupportSpells", "globalLevel", "globalRestat", "globalXpValue", "globalXpValue", "canValid", "isDirty" };
        ALL_FIELDS = new String[BasicSpellInventoryManager.FIELDS.length + SpellRestatManagerDisplayer.FIELDS.length];
        System.arraycopy(SpellRestatManagerDisplayer.FIELDS, 0, SpellRestatManagerDisplayer.ALL_FIELDS, 0, SpellRestatManagerDisplayer.FIELDS.length);
        System.arraycopy(BasicSpellInventoryManager.FIELDS, 0, SpellRestatManagerDisplayer.ALL_FIELDS, SpellRestatManagerDisplayer.FIELDS.length, BasicSpellInventoryManager.FIELDS.length);
        staticSpellOrLevelComparator = new Comparator() {
            @Override
            public int compare(final Object o1, final Object o2) {
                Spell spell1;
                if (o1 instanceof SpellLevel) {
                    spell1 = ((SpellLevel)o1).getSpell();
                }
                else {
                    spell1 = (Spell)o1;
                }
                Spell spell2;
                if (o2 instanceof SpellLevel) {
                    spell2 = ((SpellLevel)o2).getSpell();
                }
                else {
                    spell2 = (Spell)o2;
                }
                return spell1.getUiPosition() - spell2.getUiPosition();
            }
        };
    }
}
