package com.ankamagames.wakfu.client.core.game.spell;

import org.apache.log4j.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class StateClient extends State implements EffectFieldProvider
{
    private static Logger m_logger;
    boolean m_showInTimeline;
    boolean m_displayCasterName;
    int m_gfxId;
    public static final String SHOW_IN_TIMELINE_FIELD = "showInTimeline";
    public static final String NAME_FIELD = "name";
    public static final String NAME_AND_LEVEL_FIELD = "nameAndLevel";
    public static final String TABLE_TURN_DURATION_FIELD = "tableTurnDuration";
    public static final String DURATION_FIELD = "duration";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String EFFECT_DESCRIPTION_FIELD = "effectDescription";
    public static final String IS_CUMULABLE = "isCumulable";
    public static final String LEVEL_FIELD = "level";
    public static final String LEVEL_VALUE_FIELD = "levelValue";
    public static final String MAX_LEVEL_VALUE_FIELD = "maxLevelValue";
    public static final String NAME_WITH_FORMAT_FIELD = "nameWithFormat";
    public static final String[] FIELDS;
    
    @Override
    public StateClient instanceAnother(final short level) {
        final StateClient state = new StateClient();
        this.copyParameters(level, state);
        return state;
    }
    
    @Override
    public void forceLevel(final short level) {
        this.m_level = (short)Math.min(level, this.m_maxlevel);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, StateClient.FIELDS);
    }
    
    protected void copyParameters(final short level, final StateClient state) {
        super.copyParameters(level, state);
        state.m_gfxId = this.m_gfxId;
        state.m_showInTimeline = this.m_showInTimeline;
        state.m_displayCasterName = this.m_displayCasterName;
        if (this.m_HMIActions != null && !this.m_HMIActions.isEmpty()) {
            state.m_HMIActions = new ArrayList<HMIAction>(this.m_HMIActions);
        }
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    public String getTypeColoredName() {
        String colorToHex = "";
        switch (StateType.getFromValue(this.m_stateType)) {
            case NEGATIF: {
                colorToHex = ItemRarity.ADMIN.getColor().getRGBtoHex();
                break;
            }
            case NEUTRAL: {
                colorToHex = Color.WHITE.getRGBtoHex();
                break;
            }
            case POSITIF: {
                colorToHex = ItemRarity.RARE.getColor().getRGBtoHex();
                break;
            }
        }
        return new TextWidgetFormater().openText().addColor(colorToHex).append(this.getName()).closeText().finishAndToString();
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equalsIgnoreCase("name")) {
            return this.getName(14);
        }
        if (fieldName.equalsIgnoreCase("nameWithFormat")) {
            return this.getTypeColoredName();
        }
        if (fieldName.equalsIgnoreCase("nameAndLevel")) {
            return this.getNameAndLevelText();
        }
        if (fieldName.equalsIgnoreCase("duration")) {
            return this.getTableTurnDuration();
        }
        if (fieldName.equalsIgnoreCase("tableTurnDuration")) {
            return this.getTableTurnDuration();
        }
        if (fieldName.equalsIgnoreCase("iconUrl")) {
            return this.getIconUrl();
        }
        if (fieldName.equalsIgnoreCase("showInTimeline")) {
            return this.isShownInTimeline();
        }
        if (fieldName.equals("description")) {
            return this.getDescription();
        }
        if (fieldName.equals("effectDescription")) {
            return CastableDescriptionGenerator.generateDescription(new StateWriter(this, CastableDescriptionGenerator.DescriptionMode.ALL));
        }
        if (fieldName.equals("isCumulable")) {
            return this.isCumulable();
        }
        if (fieldName.equals("level")) {
            return WakfuTranslator.getInstance().getString("levelShort.custom", this.getLevel());
        }
        if (fieldName.equals("levelValue")) {
            return this.m_level;
        }
        if (fieldName.equals("maxLevelValue")) {
            return this.getDisplayedMaxLevel();
        }
        return null;
    }
    
    @Override
    public short getDisplayedMaxLevel() {
        if (this.m_maxlevel <= 0) {
            return 200;
        }
        return this.m_maxlevel;
    }
    
    public String getDescription() {
        return WakfuTranslator.getInstance().getString(9, this.getStateBaseId(), new Object[0]);
    }
    
    private String getNameAndLevelText() {
        final String name = this.getName(14);
        final short level = this.getLevel();
        if (level == 1) {
            return name;
        }
        return name + " (" + WakfuTranslator.getInstance().getString("levelShort.custom", level) + ")";
    }
    
    @Override
    public String[] getFields() {
        return StateClient.FIELDS;
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public String getName() {
        return this.getName(-1);
    }
    
    public String getName(final int size) {
        String s = WakfuTranslator.getInstance().getString(8, this.getStateBaseId(), new Object[0]);
        s = WakfuTranslator.convertImageTags(s, size);
        return s;
    }
    
    public String getUnformatedName() {
        return WakfuTranslator.getInstance().getString(8, this.getStateBaseId(), new Object[0]);
    }
    
    public String getIconUrl() {
        try {
            return String.format(WakfuConfiguration.getInstance().getString("statesIconsPath"), this.m_gfxId);
        }
        catch (PropertyException e) {
            StateClient.m_logger.error((Object)("PropertyException pendant l'acc\u00e8s \u00e0 l'url de l'icone de l'\u00e9tat " + this.getStateBaseId()));
            return null;
        }
    }
    
    public boolean isShownInTimeline() {
        return this.m_showInTimeline;
    }
    
    public boolean isDisplayCasterName() {
        return this.m_displayCasterName;
    }
    
    @Override
    public short getBaseId() {
        return this.getStateBaseId();
    }
    
    static {
        StateClient.m_logger = Logger.getLogger((Class)StateClient.class);
        FIELDS = new String[] { "name", "nameAndLevel", "tableTurnDuration", "duration", "iconUrl", "showInTimeline", "description", "effectDescription", "isCumulable", "level", "nameWithFormat", "levelValue", "maxLevelValue" };
    }
}
