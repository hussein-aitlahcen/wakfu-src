package com.ankamagames.wakfu.client.core.game.breed;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class AvatarBreedInfo implements FieldProvider, Comparable
{
    protected static final Logger m_logger;
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String LONG_NAME_FIELD = "longName";
    public static final String GOD_NAME_FIELD = "godName";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String BACKGROUND_DESCRIPTION_FIELD = "backgroundDescription";
    public static final String BUTTON_STYLE_FIELD = "buttonStyle";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String SMALL_ICON_URL_FIELD = "smallIconUrl";
    public static final String SMALL_BACKGROUND_URL_FIELD = "smallBackgroundUrl";
    public static final String BIG_BACKGROUND_URL_FIELD = "bigBackgroundUrl";
    public static final String MAIN_SPELLS_FIELD = "mainSpells";
    public static final String SUPPORT_SPELLS_TEXT_FIELD = "supportSpellsText";
    public static final String FEMALE_ILLUSTRATION_FIELD = "femaleIllustration";
    public static final String MALE_ILLUSTRATION_FIELD = "maleIllustration";
    public static final String MALE_PORTRAIT_ILLUSTRATION_FIELD = "malePortraitIllustration";
    public static final String FEMALE_PORTRAIT_ILLUSTRATION_FIELD = "femalePortraitIllustration";
    public static final String MALE_FEMALE_PORTRAIT_ILLUSTRATION_FIELD = "maleFemalePortraitIllustration";
    public static final String[] FIELDS;
    private AvatarBreed m_breed;
    private TByteObjectHashMap<TLongObjectHashMap<SpellElement>> m_spells;
    private byte m_uiElementTree;
    
    public AvatarBreedInfo(final AvatarBreed breed) {
        super();
        this.m_breed = AvatarBreed.NONE;
        this.m_breed = breed;
        this.m_spells = new TByteObjectHashMap<TLongObjectHashMap<SpellElement>>();
        this.m_uiElementTree = Elements.PHYSICAL.getId();
    }
    
    public short getId() {
        return this.m_breed.getBreedId();
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString("breed." + this.getId());
    }
    
    public String getDescription() {
        return WakfuTranslator.getInstance().getString("breedLongDesc." + this.getId());
    }
    
    public String getLongName() {
        final String item = WakfuTranslator.getInstance().getStringWithoutFormat("breedLongName." + this.getId());
        return StringFormatter.format(item, new TextWidgetFormater().append(this.getName()).toString());
    }
    
    public AvatarBreed getBreed() {
        return this.m_breed;
    }
    
    public void addSpell(final Spell spell, final SpellLevel spellLevel) {
        final byte id = spell.getElementId();
        if (!this.m_spells.containsKey(id)) {
            this.m_spells.put(id, new TLongObjectHashMap<SpellElement>());
        }
        this.m_spells.get(id).put(spell.getId(), new SpellElement(spell, spellLevel));
    }
    
    public List getSpellsByElementId(final Elements element) {
        final SpellElement[] spellsElement = this.getSpellsElement(element);
        if (spellsElement == null) {
            return null;
        }
        final List<Object> ret = new ArrayList<Object>(spellsElement.length);
        for (final SpellElement spellElement : spellsElement) {
            ret.add((spellElement.getSpellLevel() != null) ? spellElement.getSpellLevel() : spellElement.getSpell());
        }
        return ret;
    }
    
    public SpellElement[] getSpellsElement(final Elements element) {
        final TLongObjectHashMap<SpellElement> elementList = this.m_spells.get(element.getId());
        if (elementList == null) {
            return null;
        }
        final SpellElement[] elements = new SpellElement[elementList.size()];
        elementList.getValues(elements);
        Arrays.sort(elements);
        return elements;
    }
    
    private Spell[] getSampleSpells() {
        final Elements[] elements = Elements.values();
        final Spell[] sample = new Spell[elements.length];
        byte idx = 0;
        for (final Elements element : Elements.values()) {
            if (element != Elements.PHYSICAL) {
                final SpellElement[] spells = this.getSpellsElement(element);
                if (spells != null) {
                    if (spells.length != 0) {
                        final Spell spell = spells[0].getSpell();
                        if (spell != null) {
                            final Spell[] array = sample;
                            final byte b = idx;
                            ++idx;
                            array[b] = spell;
                        }
                    }
                }
            }
        }
        return sample;
    }
    
    public TByteObjectIterator<TLongObjectHashMap<SpellElement>> getSpellIterator() {
        return this.m_spells.iterator();
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("id")) {
            return this.getId();
        }
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("longName")) {
            return this.getLongName();
        }
        if (fieldName.equals("description")) {
            return this.getDescription();
        }
        if (fieldName.equals("iconUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedIconPath"), this.getId());
            }
            catch (PropertyException e) {
                AvatarBreedInfo.m_logger.error((Object)e.getMessage(), (Throwable)e);
                return null;
            }
        }
        if (fieldName.equals("smallIconUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedSmallIconPath"), this.getId());
            }
            catch (PropertyException e) {
                AvatarBreedInfo.m_logger.error((Object)e.getMessage(), (Throwable)e);
                return null;
            }
        }
        if (fieldName.equals("smallBackgroundUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedSmallBackgroundsPath"), this.getId());
            }
            catch (PropertyException e) {
                AvatarBreedInfo.m_logger.error((Object)e.getMessage(), (Throwable)e);
                return null;
            }
        }
        if (fieldName.equals("bigBackgroundUrl")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedBigBackgroundsPath"), this.getId());
            }
            catch (PropertyException e) {
                AvatarBreedInfo.m_logger.error((Object)e.getMessage(), (Throwable)e);
                return null;
            }
        }
        if (fieldName.equals("buttonStyle")) {
            return "characterCreationBreed" + this.getId();
        }
        if (fieldName.equals("mainSpells")) {
            return this.getSampleSpells();
        }
        if (fieldName.equals("supportSpellsText")) {
            return WakfuTranslator.getInstance().getString("breedSupportSpells", this.getName());
        }
        if (fieldName.equals("maleIllustration")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedIllustrationPath"), this.getBreed().getBreedId(), (byte)0);
            }
            catch (PropertyException e) {
                AvatarBreedInfo.m_logger.error((Object)"Exception", (Throwable)e);
                return null;
            }
        }
        if (fieldName.equals("femaleIllustration")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedIllustrationPath"), this.getBreed().getBreedId(), (byte)1);
            }
            catch (PropertyException e) {
                AvatarBreedInfo.m_logger.error((Object)"Exception", (Throwable)e);
                return null;
            }
        }
        if (fieldName.equals("malePortraitIllustration")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedPortraitIllustrationPath"), String.valueOf(this.getBreed().getBreedId()) + String.valueOf(0));
            }
            catch (PropertyException e) {
                AvatarBreedInfo.m_logger.error((Object)"Exception", (Throwable)e);
                return null;
            }
        }
        if (fieldName.equals("femalePortraitIllustration")) {
            try {
                return String.format(WakfuConfiguration.getInstance().getString("breedPortraitIllustrationPath"), String.valueOf(this.getBreed().getBreedId()) + String.valueOf(1));
            }
            catch (PropertyException e) {
                AvatarBreedInfo.m_logger.error((Object)"Exception", (Throwable)e);
                return null;
            }
        }
        if (fieldName.equals("maleFemalePortraitIllustration")) {
            try {
                final String[] textureUrls = { String.format(WakfuConfiguration.getInstance().getString("breedPortraitIllustrationPath"), String.valueOf(this.getBreed().getBreedId()) + String.valueOf(0)), String.format(WakfuConfiguration.getInstance().getString("breedPortraitIllustrationPath"), String.valueOf(this.getBreed().getBreedId()) + String.valueOf(1)) };
                return textureUrls;
            }
            catch (PropertyException e) {
                AvatarBreedInfo.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
        return null;
    }
    
    @Override
    public String[] getFields() {
        return AvatarBreedInfo.FIELDS;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldNaLme) {
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
        final AvatarBreedInfo breedInfo = (AvatarBreedInfo)o;
        return this.getId() - breedInfo.getId();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AvatarBreedInfo.class);
        FIELDS = new String[] { "id", "name", "longName", "godName", "description", "backgroundDescription", "buttonStyle", "iconUrl", "smallIconUrl", "smallBackgroundUrl", "bigBackgroundUrl", "mainSpells", "supportSpellsText", "femaleIllustration", "maleIllustration", "malePortraitIllustration", "femalePortraitIllustration", "maleFemalePortraitIllustration" };
    }
}
