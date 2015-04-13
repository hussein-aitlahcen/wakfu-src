package com.ankamagames.wakfu.client.core.game.aptitude;

import gnu.trove.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.aptitude.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.datas.*;

public class AptitudeDisplayerImpl implements AptitudeDisplayer
{
    private static AptitudeDisplayerImpl m_instance;
    public static final String NAME_AND_LEVEL_FIELD = "nameAndLevel";
    public static final String NAME_FIELD = "name";
    public static final String ID_FIELD = "id";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String LEVEL_VALUE_FIELD = "levelValue";
    public static final String LEVEL_FIELD = "level";
    public static final String LEVEL_WITH_COLOR_FIELD = "levelWithColor";
    public static final String LEVEL_COLOR_FIELD = "levelColor";
    public static final String CAN_BE_INCREASED_FIELD = "canBeIncreased";
    public static final String CAN_BE_DECREASED_FIELD = "canBeDecreased";
    public static final String CHARACTERISTIC_VALUE_FIELD = "characteristicValue";
    public static final String CHARACTERISTIC_NAME_FIELD = "characteristicName";
    public static final String CHARACTERISTIC_DESCRIPTION_FIELD = "characteristicDescription";
    public static final String IS_MAXED_OUT_FIELD = "isMaxedOut";
    private TByteObjectHashMap<TShortShortHashMap> m_aptitudeLevelIncreaseMapsMap;
    private TIntIntHashMap m_totalAddedPoints;
    
    private AptitudeDisplayerImpl() {
        super();
        this.m_aptitudeLevelIncreaseMapsMap = new TByteObjectHashMap<TShortShortHashMap>();
        this.m_totalAddedPoints = new TIntIntHashMap();
        for (final AptitudeType type : AptitudeType.values()) {
            this.m_aptitudeLevelIncreaseMapsMap.put(type.getId(), new TShortShortHashMap());
        }
    }
    
    public static AptitudeDisplayerImpl getInstance() {
        return AptitudeDisplayerImpl.m_instance;
    }
    
    @Override
    public Object getFieldValue(final Aptitude aptitude, final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString(44, aptitude.getReferenceId(), new Object[0]);
        }
        if (fieldName.equals("nameAndLevel")) {
            final String levelDesc = this.getIncreasedLevel(aptitude) + ((aptitude.getMaxLevel() != 32767) ? ("/" + aptitude.getMaxLevel()) : "");
            final String name = WakfuTranslator.getInstance().getString(44, aptitude.getReferenceId(), new Object[0]);
            return name + " - " + WakfuTranslator.getInstance().getString("levelShort.custom", levelDesc);
        }
        if (fieldName.equals("id")) {
            return aptitude.getReferenceAptitude().getReferenceId();
        }
        if (fieldName.equals("iconUrl")) {
            final ReferenceAptitude referenceAptitude = aptitude.getReferenceAptitude();
            if (referenceAptitude.getSpellGfxId() != 0) {
                return WakfuConfiguration.getInstance().getSpellBigIcon(referenceAptitude.getSpellGfxId());
            }
            return WakfuConfiguration.getInstance().getAptitudeIcon(referenceAptitude.getAptitudeGfxId());
        }
        else {
            if (fieldName.equals("description")) {
                final TextWidgetFormater format = new TextWidgetFormater();
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final int levelUnlock = this.levelToUnlockNextLevel(aptitude);
                if (levelUnlock > 0) {
                    format.b();
                    if (localPlayer.getLevel() < levelUnlock) {
                        format.addColor(Color.RED.getRGBtoHex());
                    }
                    format.append(WakfuTranslator.getInstance().getString("required.level.custom", levelUnlock));
                    format._b().newLine();
                }
                final int cost = this.costForNextLevel(aptitude);
                format.b();
                if (cost == -1) {
                    format.append(WakfuTranslator.getInstance().getString("aptitudes.maxLevelReached"));
                }
                else {
                    if (!this.hasLevelToIncrease(aptitude)) {
                        format.addColor("FF0000");
                    }
                    format.append(WakfuTranslator.getInstance().getString("aptitudes.pointsToNextLevel", cost));
                }
                format._b();
                if (!this.hasSpellToIncrease(aptitude)) {
                    format.append("\n").b().addColor("FF0000");
                    format.append(WakfuTranslator.getInstance().getString("aptitudes.spellUnkown"));
                    format._b();
                }
                final String description = WakfuTranslator.getInstance().getString(45, aptitude.getReferenceId(), new Object[0]);
                if (description != null && description.length() > 0) {
                    format.addColor("fac400");
                    format.append("\n\n").append(description).closeText();
                }
                final short aptitudeLevel = this.getIncreasedLevel(aptitude);
                format.append("\n\n").u().append(WakfuTranslator.getInstance().getString("aptitudes.currentLevelEffects"))._u().append("\n");
                final int spellId = aptitude.getReferenceAptitude().getLinkedSpellId();
                ArrayList<String> desc;
                if (spellId == 0) {
                    desc = CastableDescriptionGenerator.generateDescription(new DummyEffectContainerWriter(aptitude.getEffectsForLevel(aptitudeLevel), 0, aptitudeLevel));
                }
                else {
                    final SpellLevel spell = (SpellLevel)SpellManager.getInstance().getDefaultSpellLevel(spellId, aptitudeLevel);
                    if (spell.getSpell() == null) {
                        return null;
                    }
                    desc = CastableDescriptionGenerator.generateDescription(new SpellWriter(spell));
                }
                boolean first = true;
                for (final String subDesc : desc) {
                    if (!first) {
                        format.append("\n");
                    }
                    format.append(subDesc);
                    first = false;
                }
                if (!this.isMaxedOut(aptitude)) {
                    format.append("\n\n").u().append(WakfuTranslator.getInstance().getString("aptitudes.nextLevelEffects"))._u().append("\n");
                    final short nextLevel = (short)(aptitudeLevel + 1);
                    final int spellId2 = aptitude.getReferenceAptitude().getLinkedSpellId();
                    ArrayList<String> desc2;
                    if (spellId2 == 0) {
                        desc2 = CastableDescriptionGenerator.generateDescription(new DummyEffectContainerWriter(aptitude.getEffectsForLevel(nextLevel), 0, nextLevel));
                    }
                    else {
                        final SpellLevel spell2 = (SpellLevel)SpellManager.getInstance().getDefaultSpellLevel(spellId2, nextLevel);
                        if (spell2.getSpell() == null) {
                            return null;
                        }
                        desc2 = CastableDescriptionGenerator.generateDescription(new SpellWriter(spell2));
                    }
                    boolean first2 = true;
                    for (final String subDesc2 : desc2) {
                        if (!first2) {
                            format.append("\n");
                        }
                        format.append(subDesc2);
                        first2 = false;
                    }
                }
                return format.finishAndToString();
            }
            if (fieldName.equals("levelValue")) {
                return this.getIncreasedLevel(aptitude);
            }
            if (fieldName.equals("level")) {
                return new StringBuilder(this.getIncreasedLevel(aptitude)).append("/").append(aptitude.getMaxLevel());
            }
            if (fieldName.equals("levelWithColor")) {
                boolean hasAptitudePointsLeft = true;
                final LocalPlayerCharacter localPlayer2 = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer2 != null) {
                    final AptitudeInventory aptitudeInventory = localPlayer2.getAptitudeInventory();
                    if (aptitudeInventory != null && aptitudeInventory.getAvailablePoints(aptitude.getReferenceAptitude().getType()) == 0) {
                        hasAptitudePointsLeft = false;
                    }
                }
                final boolean canUpdate = this.canBeIncreased(aptitude);
                String color;
                if (this.isMaxedOut(aptitude)) {
                    color = "FFD020";
                }
                else if (hasAptitudePointsLeft && canUpdate) {
                    color = "00FF00";
                }
                else {
                    color = "FFFFFF";
                }
                final TextWidgetFormater sb = new TextWidgetFormater();
                sb.openText().addColor(color);
                sb.append(this.getIncreasedLevel(aptitude));
                sb.closeText();
                return sb.finishAndToString();
            }
            if (fieldName.equals("levelColor")) {
                boolean hasAptitudePointsLeft2 = true;
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer != null) {
                    final AptitudeInventory aptitudeInventory2 = localPlayer.getAptitudeInventory();
                    if (aptitudeInventory2 != null && aptitudeInventory2.getAvailablePoints(aptitude.getReferenceAptitude().getType()) == 0) {
                        hasAptitudePointsLeft2 = false;
                    }
                }
                final boolean canUpdate2 = this.canBeIncreased(aptitude);
                if (this.isMaxedOut(aptitude)) {
                    return "1,.9,.1";
                }
                if (hasAptitudePointsLeft2 && canUpdate2) {
                    return "0,1,0";
                }
                return "1,1,1";
            }
            else {
                if (fieldName.equals("canBeIncreased")) {
                    return this.canBeIncreased(aptitude);
                }
                if (fieldName.equals("canBeDecreased")) {
                    return this.canBeDecreased(aptitude);
                }
                if (fieldName.equals("isMaxedOut")) {
                    return this.isMaxedOut(aptitude);
                }
                if (fieldName.equals("characteristicValue")) {
                    final List<WakfuEffect> effects = aptitude.getReferenceAptitude().getEffects();
                    final WakfuEffect effect = effects.get(0);
                    return (int)effect.getParam(0, this.getIncreasedLevel(aptitude));
                }
                if (fieldName.equals("characteristicName")) {
                    final FighterCharacteristicType charac = aptitude.getReferenceAptitude().getCharacteristic();
                    if (charac != null) {
                        return WakfuTranslator.getInstance().getString(charac.name() + "Short");
                    }
                    return null;
                }
                else {
                    if (!fieldName.equals("characteristicDescription")) {
                        return null;
                    }
                    final FighterCharacteristicType charac = aptitude.getReferenceAptitude().getCharacteristic();
                    if (charac != null) {
                        return WakfuTranslator.getInstance().getString(charac.name() + "Description");
                    }
                    return null;
                }
            }
        }
    }
    
    public void fireAvailablePointsChanged(final Aptitude aptitude) {
        PropertiesProvider.getInstance().firePropertyValueChanged(aptitude, "levelWithColor", "canBeIncreased", "canBeDecreased");
    }
    
    @Override
    public void fireLevelChanged(final Aptitude aptitude) {
        PropertiesProvider.getInstance().firePropertyValueChanged(aptitude, "nameAndLevel", "description", "characteristicValue", "level", "levelColor", "levelWithColor", "isMaxedOut", "canBeIncreased", "canBeDecreased");
    }
    
    private short getIncreasedLevel(final Aptitude aptitude) {
        final TShortShortHashMap increasesMap = this.m_aptitudeLevelIncreaseMapsMap.get(aptitude.getReferenceAptitude().getType().getId());
        return (short)(aptitude.getLevel() + increasesMap.get(aptitude.getReferenceAptitude().getReferenceId()));
    }
    
    private int levelToUnlockNextLevel(final Aptitude aptitude) {
        final TShortShortHashMap increasesMap = this.m_aptitudeLevelIncreaseMapsMap.get(aptitude.getReferenceAptitude().getType().getId());
        final short levelModification = increasesMap.get(aptitude.getReferenceAptitude().getReferenceId());
        return aptitude.getReferenceAptitude().getLevelUnlock((short)(aptitude.getLevel() + levelModification + 1));
    }
    
    private int costForNextLevel(final Aptitude aptitude) {
        final TShortShortHashMap increasesMap = this.m_aptitudeLevelIncreaseMapsMap.get(aptitude.getReferenceAptitude().getType().getId());
        final short levelModification = increasesMap.get(aptitude.getReferenceAptitude().getReferenceId());
        if (levelModification == 0) {
            return aptitude.costForNextLevel(WakfuGameEntity.getInstance().getLocalPlayer());
        }
        if (!this.isMaxedOut(aptitude)) {
            return aptitude.getPointsForLevel((short)(aptitude.getLevel() + levelModification + 1), WakfuGameEntity.getInstance().getLocalPlayer());
        }
        return -1;
    }
    
    private int creditForPreviousLevel(final Aptitude aptitude) {
        final TShortShortHashMap increasesMap = this.m_aptitudeLevelIncreaseMapsMap.get(aptitude.getReferenceAptitude().getType().getId());
        final short levelModification = increasesMap.get(aptitude.getReferenceAptitude().getReferenceId());
        if (levelModification == 0) {
            return 0;
        }
        return aptitude.getPointsForLevel((short)(aptitude.getLevel() + levelModification), WakfuGameEntity.getInstance().getLocalPlayer());
    }
    
    private boolean isMaxedOut(final Aptitude aptitude) {
        final TShortShortHashMap increasesMap = this.m_aptitudeLevelIncreaseMapsMap.get(aptitude.getReferenceAptitude().getType().getId());
        final short levelModification = increasesMap.get(aptitude.getReferenceAptitude().getReferenceId());
        if (levelModification == 0) {
            return aptitude.isMaxedOut();
        }
        return aptitude.getReferenceAptitude().getMaxLevel() <= aptitude.getLevel() + levelModification;
    }
    
    public boolean canBeDecreased(final Aptitude aptitude) {
        return this.creditForPreviousLevel(aptitude) != 0;
    }
    
    private boolean hasUnlockedLevel(final Aptitude aptitude) {
        final short playerLevel = WakfuGameEntity.getInstance().getLocalPlayer().getLevel();
        final int unlockLevel = this.levelToUnlockNextLevel(aptitude);
        return playerLevel >= unlockLevel;
    }
    
    private boolean hasLevelToIncrease(final Aptitude aptitude) {
        final int cost = this.costForNextLevel(aptitude);
        if (cost == -1) {
            return false;
        }
        final AptitudeInventory aptitudeInventory = WakfuGameEntity.getInstance().getLocalPlayer().getAptitudeInventory();
        final AptitudeType type = aptitude.getReferenceAptitude().getType();
        final int available = aptitudeInventory.getAvailablePoints(type) - this.m_totalAddedPoints.get(type.getId());
        return cost <= available;
    }
    
    private boolean hasSpellToIncrease(final Aptitude aptitude) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final int linkedSpellId = aptitude.getReferenceAptitude().getLinkedSpellId();
        if (linkedSpellId != 0) {
            final ArrayList<? extends AbstractSpellLevel> spells = localPlayer.getSpellInventory().getAllWithReferenceId(linkedSpellId);
            return !spells.isEmpty();
        }
        return true;
    }
    
    public boolean canBeIncreased(final Aptitude aptitude) {
        return this.hasLevelToIncrease(aptitude) && this.hasSpellToIncrease(aptitude) && this.hasUnlockedLevel(aptitude);
    }
    
    public void resetAptitudeLevelModifications() {
        for (final AptitudeType type : AptitudeType.values()) {
            this.resetAptitudeLevelModifications(type);
        }
    }
    
    public void resetAptitudeLevelModifications(final AptitudeType type) {
        this.resetAptitudeLevelModifications(type, true);
    }
    
    public void resetAptitudeLevelModifications(final AptitudeType type, final boolean updateDisplay) {
        this.m_aptitudeLevelIncreaseMapsMap.get(type.getId()).clear();
        this.m_totalAddedPoints.remove(type.getId());
        if (!updateDisplay) {
            return;
        }
        switch (type) {
            case COMMON: {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "availableCommonPoints", "hasAptitudePoints");
                break;
            }
            case SPELL: {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "availableAptitudePoints", "hasAptitudePoints");
                break;
            }
        }
    }
    
    public void increaseAptitudeLevel(final Aptitude aptitude) {
        if (!this.canBeIncreased(aptitude)) {
            return;
        }
        final TShortShortHashMap increasesMap = this.m_aptitudeLevelIncreaseMapsMap.get(aptitude.getReferenceAptitude().getType().getId());
        final short levelIncrease = increasesMap.adjustOrPutValue(aptitude.getReferenceAptitude().getReferenceId(), (short)1, (short)1);
        final AptitudeType type = aptitude.getReferenceAptitude().getType();
        final int amount = aptitude.getPointsForLevel((short)(levelIncrease + aptitude.getLevel()), WakfuGameEntity.getInstance().getLocalPlayer());
        this.m_totalAddedPoints.adjustOrPutValue(type.getId(), amount, amount);
        PropertiesProvider.getInstance().firePropertyValueChanged(aptitude, "nameAndLevel", "levelValue", "levelColor", "level", "levelWithColor", "description", "characteristicValue", "canBeIncreased", "canBeDecreased", "isMaxedOut");
        switch (type) {
            case COMMON: {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "availableCommonPoints", "hasAptitudePoints");
                break;
            }
            case SPELL: {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "availableAptitudePoints", "hasAptitudePoints");
                break;
            }
        }
    }
    
    public void decreaseAptitudeLevel(final Aptitude aptitude) {
        if (!this.canBeDecreased(aptitude)) {
            return;
        }
        final TShortShortHashMap increasesMap = this.m_aptitudeLevelIncreaseMapsMap.get(aptitude.getReferenceAptitude().getType().getId());
        final short deltaLevel = increasesMap.adjustOrPutValue(aptitude.getReferenceAptitude().getReferenceId(), (short)(-1), (short)0);
        if (deltaLevel == 0) {
            increasesMap.remove(aptitude.getReferenceAptitude().getReferenceId());
        }
        final int amount = -aptitude.getPointsForLevel((short)(deltaLevel + 1 + aptitude.getLevel()), WakfuGameEntity.getInstance().getLocalPlayer());
        final AptitudeType type = aptitude.getReferenceAptitude().getType();
        this.m_totalAddedPoints.adjustOrPutValue(type.getId(), amount, 0);
        PropertiesProvider.getInstance().firePropertyValueChanged(aptitude, "nameAndLevel", "levelValue", "levelColor", "level", "levelWithColor", "description", "characteristicValue", "canBeIncreased", "canBeDecreased", "isMaxedOut");
        switch (type) {
            case COMMON: {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "availableCommonPoints", "hasAptitudePoints");
                break;
            }
            case SPELL: {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "availableAptitudePoints", "hasAptitudePoints");
                break;
            }
        }
    }
    
    public int getTotalAddedPoints(final AptitudeType type) {
        return this.m_totalAddedPoints.get(type.getId());
    }
    
    public TShortShortHashMap getAptitudeLevelIncreaseMap(final AptitudeType type) {
        return this.m_aptitudeLevelIncreaseMapsMap.get(type.getId());
    }
    
    static {
        AptitudeDisplayerImpl.m_instance = new AptitudeDisplayerImpl();
    }
}
