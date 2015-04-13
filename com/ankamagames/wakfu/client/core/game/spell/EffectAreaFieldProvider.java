package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class EffectAreaFieldProvider implements EffectFieldProvider
{
    public static final String NAME_WITH_FORMAT_FIELD = "nameWithFormat";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String EFFECT_DESCRIPTION_FIELD = "effectDescription";
    public static final String LEVEL_FIELD = "level";
    public static final String LEVEL_VALUE_FIELD = "levelValue";
    public static final String MAX_LEVEL_VALUE_FIELD = "maxLevelValue";
    public static final String[] FIELDS;
    private final AbstractEffectArea m_abstractEffectArea;
    private short m_level;
    
    public EffectAreaFieldProvider(final AbstractEffectArea abstractEffectArea, final short level) {
        super();
        this.m_abstractEffectArea = abstractEffectArea;
        this.m_level = level;
    }
    
    @Override
    public String[] getFields() {
        return EffectAreaFieldProvider.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("nameWithFormat")) {
            return WakfuTranslator.getInstance().getString(6, (short)this.m_abstractEffectArea.getBaseId(), new Object[0]);
        }
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getIconUrl("effectAreasIconsPath", null, (short)this.m_abstractEffectArea.getBaseId());
        }
        if (fieldName.equals("effectDescription")) {
            final EffectAreaWriter effectAreaWriter = new EffectAreaWriter(this.m_abstractEffectArea, this.m_level, 0);
            return effectAreaWriter.writeContainer();
        }
        if (fieldName.equals("level")) {
            return WakfuTranslator.getInstance().getString("levelShort.custom", this.m_level);
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
        final short maxLevel = (short)this.m_abstractEffectArea.getMaxLevel();
        return (short)((maxLevel == 0) ? 200 : maxLevel);
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
    
    @Override
    public short getBaseId() {
        return (short)this.m_abstractEffectArea.getBaseId();
    }
    
    @Override
    public void forceLevel(final short level) {
        this.m_level = level;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, EffectAreaFieldProvider.FIELDS);
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    static {
        FIELDS = new String[] { "nameWithFormat", "iconUrl", "effectDescription", "level", "levelValue", "maxLevelValue" };
    }
}
