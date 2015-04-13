package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.java.util.*;
import java.util.regex.*;
import gnu.trove.*;

public class CharacteristicViewProvider extends ImmutableFieldProvider implements CharacteristicUpdateListener
{
    private static final Logger m_logger;
    public static final String MAIN = "main";
    public static final String MAIN_CUSTOM = "mainCustom";
    public static final String HAS_CUSTOM = "hasCustom";
    public static final String IS_CUSTOM_FULL = "isCustomFull";
    public static final String FIGHT = "fight";
    public static final String SECONDARY = "secondary";
    public static final byte MAX_CUSTOM_SIZE = 8;
    private static final HashMap<String, FighterCharacteristicType> NAME_TO_CHARAC;
    private final TByteObjectHashMap<BaseCharacteristicView> m_views;
    private final CharacterInfo m_info;
    private final ArrayList<BaseCharacteristicView> m_main;
    private final ArrayList<BaseCharacteristicView> m_fight;
    private final ArrayList<BaseCharacteristicView> m_secondary;
    private final ArrayList<BaseCharacteristicView> m_mainCustom;
    private static final String HIGHLIGHTED_CHARACTERISTICS = "highlightedCharacteristics";
    private static final Pattern PATTERN;
    
    private static void addToNameMap(final FighterCharacteristicType... types) {
        for (final FighterCharacteristicType type : types) {
            CharacteristicViewProvider.NAME_TO_CHARAC.put(type.name(), type);
        }
    }
    
    public CharacteristicViewProvider(final CharacterInfo info) {
        super();
        this.m_views = new TByteObjectHashMap<BaseCharacteristicView>();
        this.m_main = new ArrayList<BaseCharacteristicView>();
        this.m_fight = new ArrayList<BaseCharacteristicView>();
        this.m_secondary = new ArrayList<BaseCharacteristicView>();
        this.m_mainCustom = new ArrayList<BaseCharacteristicView>();
        this.createViews(this.m_info = info);
        this.initializeLists();
    }
    
    private void createViews(final CharacterInfo info) {
        this.m_views.put(FighterCharacteristicType.TOTAL_HP.getId(), new TotalHPCharacteristicView(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.VIRTUAL_HP), this, info, new FighterCharacteristic[] { info.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP), info.getCharacteristic((CharacteristicType)FighterCharacteristicType.VIRTUAL_HP) }));
        this.m_views.put(FighterCharacteristicType.HP.getId(), new HPCharacteristicView(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.HP), this, info));
        this.m_views.put(FighterCharacteristicType.VIRTUAL_HP.getId(), new HPCharacteristicView(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.VIRTUAL_HP), this, info, (byte)39));
        this.m_views.put(FighterCharacteristicType.CHRAGE.getId(), new ChrageCharacteristicView(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.CHRAGE), this));
        this.m_views.put(FighterCharacteristicType.AP.getId(), new ApCharacteristicView(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP), this, info));
        this.m_views.put(FighterCharacteristicType.ARMOR.getId(), new ArmorCharacteristicView(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.ARMOR), this, info));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP), (byte)2);
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.WP), (byte)5);
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.RANGE));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.INIT));
        this.addDamageResistCharacteristic(info, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.RES_FIRE_PERCENT, true);
        this.addDamageResistCharacteristic(info, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT, true);
        this.addDamageResistCharacteristic(info, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT, true);
        this.addDamageResistCharacteristic(info, FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.RES_AIR_PERCENT, true);
        this.addDamageResistCharacteristic(info, FighterCharacteristicType.DMG_STASIS_PERCENT, FighterCharacteristicType.RES_STASIS_PERCENT, true);
        this.addDamageResistCharacteristic(info, FighterCharacteristicType.RES_FIRE_PERCENT, FighterCharacteristicType.DMG_FIRE_PERCENT, false);
        this.addDamageResistCharacteristic(info, FighterCharacteristicType.RES_EARTH_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, false);
        this.addDamageResistCharacteristic(info, FighterCharacteristicType.RES_WATER_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, false);
        this.addDamageResistCharacteristic(info, FighterCharacteristicType.RES_AIR_PERCENT, FighterCharacteristicType.DMG_AIR_PERCENT, false);
        this.addDamageResistCharacteristic(info, FighterCharacteristicType.RES_STASIS_PERCENT, FighterCharacteristicType.DMG_STASIS_PERCENT, false);
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.FIRE_MASTERY));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.EARTH_MASTERY));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.WATER_MASTERY));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.AIR_MASTERY));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.RES_BACKSTAB));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.ARMOR_PLATE));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP_DEBUFF_POWER));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.AP_DEBUFF_RES));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP_DEBUFF_POWER));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.MP_DEBUFF_RES));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.ARMOR_PLATE));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.BLOCK));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.WILLPOWER));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.PERCEPTION));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.EQUIPMENT_KNOWLEDGE));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.SUMMONING_MASTERY));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.LEADERSHIP));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.BACKSTAB_BONUS));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.HEAL_IN_PERCENT));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.RANGE));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.FUMBLE_RATE));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.FEROCITY));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.CRITICAL_BONUS));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.WISDOM));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.PROSPECTION));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.DODGE));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.TACKLE));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.DMG_IN_PERCENT));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.RES_IN_PERCENT));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.MELEE_DMG));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.RANGED_DMG));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.SINGLE_TARGET_DMG));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.AOE_DMG));
        this.addCharacteristic(info.getCharacteristic((CharacteristicType)FighterCharacteristicType.BERSERK_DMG));
    }
    
    private void initializeLists() {
        this.initMainList();
        this.initFightList();
        this.initSecondaryList();
    }
    
    private void initSecondaryList() {
        this.m_secondary.add(this.m_views.get(FighterCharacteristicType.HEAL_IN_PERCENT.getId()));
        this.m_secondary.add(this.m_views.get(FighterCharacteristicType.SUMMONING_MASTERY.getId()));
        this.m_secondary.add(this.m_views.get(FighterCharacteristicType.WISDOM.getId()));
        this.m_secondary.add(this.m_views.get(FighterCharacteristicType.PROSPECTION.getId()));
        this.m_secondary.add(this.m_views.get(FighterCharacteristicType.LEADERSHIP.getId()));
        this.m_secondary.add(this.m_views.get(FighterCharacteristicType.EQUIPMENT_KNOWLEDGE.getId()));
    }
    
    private void initFightList() {
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.INIT.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.RANGE.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.DODGE.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.TACKLE.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.FEROCITY.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.BLOCK.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.CRITICAL_BONUS.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.BERSERK_DMG.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.MELEE_DMG.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.RANGED_DMG.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.SINGLE_TARGET_DMG.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.AOE_DMG.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.BACKSTAB_BONUS.getId()));
        this.m_fight.add(this.m_views.get(FighterCharacteristicType.RES_BACKSTAB.getId()));
    }
    
    private void initMainList() {
        this.m_main.add(this.m_views.get(FighterCharacteristicType.HP.getId()));
        this.m_main.add(this.m_views.get(FighterCharacteristicType.AP.getId()));
        this.m_main.add(this.m_views.get(FighterCharacteristicType.MP.getId()));
        this.m_main.add(this.m_views.get(FighterCharacteristicType.WP.getId()));
    }
    
    private void addCharacteristic(final FighterCharacteristic charac) {
        this.addCharacteristic(charac, (byte)(-1));
    }
    
    private void addCharacteristic(final FighterCharacteristic charac, final byte iconId) {
        final FighterCharacteristicType type = charac.getType();
        this.m_views.put(type.getId(), new CharacteristicView(charac, this, iconId));
    }
    
    private void addDamageResistCharacteristic(final CharacterInfo info, final FighterCharacteristicType characType, final FighterCharacteristicType reverseType, final boolean isDamage) {
        final FighterCharacteristic charac = info.getCharacteristic((CharacteristicType)characType);
        final FighterCharacteristic reverse = info.getCharacteristic((CharacteristicType)reverseType);
        this.m_views.put(charac.getType().getId(), new DamageCharacteristicView(charac, this, info, reverse, isDamage));
    }
    
    public void updateViews(final FighterCharacteristicType... types) {
        for (final FighterCharacteristicType type : types) {
            final BaseCharacteristicView view = this.m_views.get(type.getId());
            if (view != null) {
                view.updateView();
            }
        }
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        final FighterCharacteristicType type = (FighterCharacteristicType)charac.getType();
        switch (type) {
            case HP:
            case VIRTUAL_HP:
            case TOTAL_HP:
            case MP:
            case AP:
            case WP: {
                PropertiesProvider.getInstance().firePropertyValueChanged(this.m_info, "timelineDescription");
                break;
            }
            case AP_DEBUFF_POWER:
            case AP_DEBUFF_RES: {
                PropertiesProvider.getInstance().firePropertyValueChanged(this.m_info, "debuffApVisible");
                break;
            }
            case MP_DEBUFF_POWER:
            case MP_DEBUFF_RES: {
                PropertiesProvider.getInstance().firePropertyValueChanged(this.m_info, "debuffMpVisible");
                break;
            }
            case RES_AIR_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.DMG_AIR_PERCENT);
                    break;
                }
                break;
            }
            case RES_EARTH_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.DMG_EARTH_PERCENT);
                    break;
                }
                break;
            }
            case RES_FIRE_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.DMG_FIRE_PERCENT);
                    break;
                }
                break;
            }
            case RES_WATER_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.DMG_WATER_PERCENT);
                    break;
                }
                break;
            }
            case RES_STASIS_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.DMG_STASIS_PERCENT);
                    break;
                }
                break;
            }
            case DMG_AIR_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.RES_AIR_PERCENT);
                    break;
                }
                break;
            }
            case DMG_EARTH_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.RES_EARTH_PERCENT);
                    break;
                }
                break;
            }
            case DMG_FIRE_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.RES_FIRE_PERCENT);
                    break;
                }
                break;
            }
            case DMG_WATER_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.RES_WATER_PERCENT);
                    break;
                }
                break;
            }
            case DMG_STASIS_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.RES_STASIS_PERCENT);
                    break;
                }
                break;
            }
            case RES_IN_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.DMG_STASIS_PERCENT);
                    break;
                }
                this.updateViews(FighterCharacteristicType.RES_AIR_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT, FighterCharacteristicType.RES_FIRE_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT, FighterCharacteristicType.RES_STASIS_PERCENT);
                break;
            }
            case DMG_IN_PERCENT: {
                if (this.m_info.isActiveProperty(FightPropertyType.INVERT_DMG_AND_RES)) {
                    this.updateViews(FighterCharacteristicType.RES_AIR_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT, FighterCharacteristicType.RES_FIRE_PERCENT, FighterCharacteristicType.RES_STASIS_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT);
                    break;
                }
                this.updateViews(FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.DMG_STASIS_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT);
                break;
            }
        }
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("main")) {
            return this.m_main;
        }
        if (fieldName.equals("mainCustom")) {
            return this.m_mainCustom;
        }
        if (fieldName.equals("hasCustom")) {
            return this.m_mainCustom.size() != 0;
        }
        if (fieldName.equals("isCustomFull")) {
            return this.m_mainCustom.size() >= 8;
        }
        if (fieldName.equals("fight")) {
            return this.m_fight;
        }
        if (fieldName.equals("secondary")) {
            return this.m_secondary;
        }
        final FighterCharacteristicType type = CharacteristicViewProvider.NAME_TO_CHARAC.get(fieldName);
        if (type != null) {
            return this.m_views.get(type.getId());
        }
        return null;
    }
    
    public BaseCharacteristicView getCharacteristicView(final FighterCharacteristicType type) {
        return this.m_views.get(type.getId());
    }
    
    public void setHighlighted(final FighterCharacteristicType type, final boolean highlighted) {
        this.setHighlighted(type, highlighted, true);
    }
    
    public void setHighlighted(final FighterCharacteristicType type, final boolean highlighted, final boolean update) {
        final BaseCharacteristicView view = this.m_views.get(type.getId());
        if (view == null) {
            CharacteristicViewProvider.m_logger.warn((Object)("On essaye de highlighter une caract\u00e9ristique qui n'est pas dans le provider : " + type));
            return;
        }
        view.setHighlighted(highlighted);
        if (highlighted && !this.m_mainCustom.contains(view)) {
            this.m_mainCustom.add(view);
            if (update) {
                PropertiesProvider.getInstance().firePropertyValueChanged(this, "isCustomFull", "mainCustom", "hasCustom");
            }
        }
        else if (!highlighted && this.m_mainCustom.contains(view)) {
            this.m_mainCustom.remove(view);
            if (update) {
                PropertiesProvider.getInstance().firePropertyValueChanged(this, "isCustomFull", "mainCustom", "hasCustom");
            }
        }
    }
    
    private void clearHighlighted() {
        this.m_mainCustom.clear();
        final TByteObjectIterator<BaseCharacteristicView> it = this.m_views.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().setHighlighted(false);
        }
    }
    
    public void loadFromConfiguration() {
        this.clearHighlighted();
        final String highlighted = WakfuClientConfigurationManager.getInstance().getStringValue((byte)3, this.getPropertyKey());
        if (highlighted == null) {
            return;
        }
        final Matcher matcher = CharacteristicViewProvider.PATTERN.matcher(highlighted);
        while (matcher.find()) {
            final byte characId = PrimitiveConverter.getByte(matcher.group(1));
            final FighterCharacteristicType charac = FighterCharacteristicType.getCharacteristicTypeFromId(characId);
            if (charac == null) {
                continue;
            }
            if (!CharacteristicViewProvider.NAME_TO_CHARAC.containsKey(charac.name())) {
                this.setHighlighted(charac, false, false);
            }
            else {
                this.setHighlighted(charac, true, false);
            }
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isCustomFull", "mainCustom", "hasCustom");
    }
    
    public void saveToConfiguration() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0, size = this.m_mainCustom.size(); i < size; ++i) {
            sb.append(this.m_mainCustom.get(i).getType().getId()).append(";");
        }
        WakfuClientConfigurationManager.getInstance().setValue((byte)3, this.getPropertyKey(), sb.toString());
    }
    
    private String getPropertyKey() {
        return "highlightedCharacteristics" + this.m_info.getBreedId();
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacteristicViewProvider.class);
        NAME_TO_CHARAC = new HashMap<String, FighterCharacteristicType>();
        addToNameMap(FighterCharacteristicType.HP, FighterCharacteristicType.VIRTUAL_HP, FighterCharacteristicType.TOTAL_HP, FighterCharacteristicType.AP, FighterCharacteristicType.MP, FighterCharacteristicType.WP, FighterCharacteristicType.RANGE, FighterCharacteristicType.INIT, FighterCharacteristicType.CHRAGE, FighterCharacteristicType.DMG_FIRE_PERCENT, FighterCharacteristicType.DMG_EARTH_PERCENT, FighterCharacteristicType.DMG_WATER_PERCENT, FighterCharacteristicType.DMG_AIR_PERCENT, FighterCharacteristicType.DMG_STASIS_PERCENT, FighterCharacteristicType.RES_FIRE_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT, FighterCharacteristicType.RES_AIR_PERCENT, FighterCharacteristicType.RES_STASIS_PERCENT, FighterCharacteristicType.FIRE_MASTERY, FighterCharacteristicType.EARTH_MASTERY, FighterCharacteristicType.WATER_MASTERY, FighterCharacteristicType.AIR_MASTERY, FighterCharacteristicType.RES_BACKSTAB, FighterCharacteristicType.ARMOR_PLATE, FighterCharacteristicType.AP_DEBUFF_POWER, FighterCharacteristicType.AP_DEBUFF_RES, FighterCharacteristicType.MP_DEBUFF_POWER, FighterCharacteristicType.MP_DEBUFF_RES, FighterCharacteristicType.BLOCK, FighterCharacteristicType.WILLPOWER, FighterCharacteristicType.PERCEPTION, FighterCharacteristicType.EQUIPMENT_KNOWLEDGE, FighterCharacteristicType.SUMMONING_MASTERY, FighterCharacteristicType.LEADERSHIP, FighterCharacteristicType.MECHANISM_MASTERY, FighterCharacteristicType.MECHANICS, FighterCharacteristicType.BACKSTAB_BONUS, FighterCharacteristicType.HEAL_IN_PERCENT, FighterCharacteristicType.RANGE, FighterCharacteristicType.FUMBLE_RATE, FighterCharacteristicType.FEROCITY, FighterCharacteristicType.CRITICAL_BONUS, FighterCharacteristicType.WISDOM, FighterCharacteristicType.PROSPECTION, FighterCharacteristicType.DODGE, FighterCharacteristicType.TACKLE, FighterCharacteristicType.ARMOR, FighterCharacteristicType.MELEE_DMG, FighterCharacteristicType.RANGED_DMG, FighterCharacteristicType.SINGLE_TARGET_DMG, FighterCharacteristicType.AOE_DMG, FighterCharacteristicType.BERSERK_DMG);
        PATTERN = Pattern.compile("([0-9]+);?");
    }
}
