package com.ankamagames.wakfu.client.core.game.aptitudenew;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.aptitudeNewVersion.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class AptitudeBonusView extends ImmutableFieldProvider
{
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
    private final InactiveAptitudeBonusInventory m_inventory;
    private final ClientAptitudeBonusModel m_bonus;
    
    AptitudeBonusView(final InactiveAptitudeBonusInventory inventory, final ClientAptitudeBonusModel bonus) {
        super();
        this.m_inventory = inventory;
        this.m_bonus = bonus;
    }
    
    public int getId() {
        return this.m_bonus.getId();
    }
    
    @Override
    public String[] getFields() {
        return AptitudeBonusView.NO_FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("nameAndLevel")) {
            final String levelDesc = this.getLevel() + ((this.m_bonus.getMax() == 0) ? "" : ("/" + this.m_bonus.getMax()));
            return this.getName() + " - " + WakfuTranslator.getInstance().getString("levelShort.custom", levelDesc);
        }
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getIconUrl("aptitudeIconsPath", "defaultIconPath", this.m_bonus.getGfxId());
        }
        if (fieldName.equals("description")) {
            final TextWidgetFormater format = new TextWidgetFormater();
            if (this.m_bonus.getMax() != 0 && this.getLevel() == this.m_bonus.getMax()) {
                format.b();
                format.append(WakfuTranslator.getInstance().getString("aptitudes.maxLevelReached"));
                format._b();
            }
            final FighterCharacteristicType charac = this.getCharacteristicType();
            if (charac != null) {
                format.newLine().newLine().append(WakfuTranslator.getInstance().getString(charac.name() + "Description"));
            }
            final short aptitudeLevel = this.getLevel();
            format.newLine().newLine().u().append(WakfuTranslator.getInstance().getString("aptitudes.currentLevelEffects"))._u().newLine();
            format.append(this.getEffectDescription(aptitudeLevel));
            if (!this.isMaxedOut()) {
                final short nextLevel = (short)(aptitudeLevel + 1);
                format.append("\n\n").u().append(WakfuTranslator.getInstance().getString("aptitudes.nextLevelEffects"))._u().append("\n");
                format.append(this.getEffectDescription(nextLevel));
            }
            return format.finishAndToString();
        }
        if (fieldName.equals("levelValue")) {
            return this.getLevel();
        }
        if (fieldName.equals("level")) {
            return this.getLevel();
        }
        if (fieldName.equals("levelWithColor")) {
            final boolean hasAptitudePointsLeft = this.m_inventory.hasPointsAvailableFor(this.m_bonus.getId(), 1);
            final boolean canUpdate = this.canBeIncreased();
            String color;
            if (this.isMaxedOut()) {
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
            sb.append(this.getLevel());
            sb.closeText();
            return sb.finishAndToString();
        }
        if (fieldName.equals("levelColor")) {
            final boolean hasAptitudePointsLeft2 = this.m_inventory.hasPointsAvailableFor(this.m_bonus.getId(), 1);
            final boolean canUpdate2 = this.canBeIncreased();
            if (this.isMaxedOut()) {
                return "1,.9,.1";
            }
            if (hasAptitudePointsLeft2 && canUpdate2) {
                return "0,1,0";
            }
            return "1,1,1";
        }
        else {
            if (fieldName.equals("canBeIncreased")) {
                return this.canBeIncreased();
            }
            if (fieldName.equals("canBeDecreased")) {
                return this.canBeDecreased();
            }
            if (fieldName.equals("isMaxedOut")) {
                return this.isMaxedOut();
            }
            if (!fieldName.equals("characteristicValue")) {
                if (fieldName.equals("characteristicName")) {
                    final FighterCharacteristicType charac2 = this.getCharacteristicType();
                    if (charac2 != null) {
                        return WakfuTranslator.getInstance().getString(charac2.name() + "Short");
                    }
                    return null;
                }
                else if (fieldName.equals("characteristicDescription")) {
                    final FighterCharacteristicType charac2 = this.getCharacteristicType();
                    if (charac2 != null) {
                        return WakfuTranslator.getInstance().getString(charac2.name() + "Description");
                    }
                    return null;
                }
            }
            return null;
        }
    }
    
    @Nullable
    private FighterCharacteristicType getCharacteristicType() {
        final WakfuEffect effect = EffectManager.getInstance().getEffect(this.m_bonus.getEffectId());
        if (effect == null) {
            return null;
        }
        final WakfuRunningEffect runningEffect = RunningEffectConstants.getInstance().getObjectFromId(effect.getActionId());
        if (!(runningEffect instanceof CharacGain)) {
            return null;
        }
        final CharacModification characGain = (CharacModification)runningEffect;
        return (FighterCharacteristicType)characGain.getCharacteristicType();
    }
    
    private short getLevel() {
        return this.m_inventory.getLevel(this.m_bonus.getId());
    }
    
    private String getName() {
        return WakfuTranslator.getInstance().getString(146, this.m_bonus.getId(), new Object[0]);
    }
    
    private String getEffectDescription(final short level) {
        final AptitudeBonusEffectContainer container = new AptitudeBonusEffectContainer(this.m_bonus, level);
        final ArrayList<String> list = CastableDescriptionGenerator.generateDescription(new DefaultContainerWriter<Object>(container, this.m_bonus.getId(), level));
        final TextWidgetFormater sb = new TextWidgetFormater();
        for (int i = 0, size = list.size(); i < size; ++i) {
            if (i > 0) {
                sb.newLine();
            }
            sb.append(list.get(i));
        }
        return sb.finishAndToString();
    }
    
    public void fireAvailablePointsChanged() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "levelWithColor", "canBeIncreased", "canBeDecreased");
    }
    
    public void fireLevelChanged() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "name", "nameAndLevel", "description", "characteristicValue", "characteristicDescription", "level", "levelColor", "levelWithColor", "isMaxedOut", "canBeIncreased", "canBeDecreased");
    }
    
    private boolean isMaxedOut() {
        return this.m_bonus.getMax() != 0 && this.getLevel() >= this.m_bonus.getMax();
    }
    
    public boolean canBeDecreased() {
        return this.m_inventory.getLevel(this.m_bonus.getId()) > this.m_inventory.getMinLevelForBonus(this.m_bonus.getId());
    }
    
    public boolean canBeIncreased() {
        return !this.isMaxedOut() && this.m_inventory.hasPointsAvailableFor(this.m_bonus.getId(), 1);
    }
}
