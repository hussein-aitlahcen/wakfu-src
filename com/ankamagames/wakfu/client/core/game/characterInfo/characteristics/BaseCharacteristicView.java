package com.ankamagames.wakfu.client.core.game.characterInfo.characteristics;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public abstract class BaseCharacteristicView extends ImmutableFieldProvider implements CharacteristicUpdateListener
{
    private static final Logger m_logger;
    public static final String NAME = "name";
    public static final String SHORT_NAME = "shortName";
    public static final String DESCRIPTION = "description";
    public static final String SHORT_DESCRIPTION = "shortDescription";
    public static final String ICON_STYLE = "iconStyle";
    public static final String VALUE = "value";
    public static final String VALUE_MAX = "max";
    public static final String VALUE_MIN_MAX = "minMax";
    public static final String PERCENT = "percent";
    public static final String VALUE_DESCRIPTION = "valueDescription";
    public static final String FORMATTED_VALUE_DESCRIPTION = "formattedValueDescription";
    public static final String PERCENT_DESCRIPTION = "percentDescription";
    public static final String FORMATTED_PERCENT_DESCRIPTION = "formattedPercentDescription";
    public static final String COLOR_FLOAT = "colorFloat";
    public static final String HIGHLIGHTED = "highlighted";
    public static final String[] UPDATABLE_FIELDS;
    protected FighterCharacteristic m_charac;
    protected byte m_iconId;
    protected CharacteristicViewProvider m_provider;
    private boolean m_highlighted;
    
    public BaseCharacteristicView(final FighterCharacteristic charac, final byte iconId, final CharacteristicViewProvider provider) {
        super();
        this.m_charac = charac;
        this.m_iconId = iconId;
        this.m_provider = provider;
    }
    
    @Override
    public String[] getFields() {
        return BaseCharacteristicView.UPDATABLE_FIELDS;
    }
    
    void setHighlighted(final boolean highlighted) {
        this.m_highlighted = highlighted;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("shortName")) {
            return this.getShortName();
        }
        if (fieldName.equals("description")) {
            return this.getDescription();
        }
        if (fieldName.equals("shortDescription")) {
            return this.getShortDescription();
        }
        if (fieldName.equals("iconStyle")) {
            return this.getIconStyle();
        }
        if (fieldName.equals("value")) {
            return this.getCharacValue();
        }
        if (fieldName.equals("max")) {
            return this.getCharacMax();
        }
        if (fieldName.equals("minMax")) {
            return this.getMinMax();
        }
        if (fieldName.equals("formattedValueDescription")) {
            return this.getFormattedCharacDescription();
        }
        if (fieldName.equals("valueDescription")) {
            return this.getValueDescription();
        }
        if (fieldName.equals("percent")) {
            return this.getPercentage();
        }
        if (fieldName.equals("formattedPercentDescription")) {
            return this.getFormattedPercentageDescription();
        }
        if (fieldName.equals("percentDescription")) {
            return this.getPercentageDescription();
        }
        if (fieldName.equals("colorFloat")) {
            return this.getColorFloat();
        }
        if (fieldName.equals("highlighted")) {
            return this.m_highlighted;
        }
        return null;
    }
    
    protected abstract int getCharacValue();
    
    protected abstract int getCharacMax();
    
    public FighterCharacteristicType getType() {
        return this.m_charac.getType();
    }
    
    protected Color getColor() {
        switch (this.m_charac.getType()) {
            case HP: {
                return WakfuClientConstants.HP_COLOR;
            }
            case AP: {
                return WakfuClientConstants.AP_COLOR;
            }
            case MP: {
                return WakfuClientConstants.MP_COLOR;
            }
            case WP: {
                return WakfuClientConstants.WP_COLOR;
            }
            default: {
                return Color.WHITE;
            }
        }
    }
    
    protected String getColorFloat() {
        final Color color = this.getColor();
        final StringBuilder sb = new StringBuilder();
        sb.append(color.getRed()).append(",");
        sb.append(color.getGreen()).append(",");
        sb.append(color.getBlue()).append(",");
        sb.append(color.getAlpha());
        return sb.toString();
    }
    
    protected Object getIconStyle() {
        return "icon" + this.m_charac.getType().name();
    }
    
    protected Object getShortDescription() {
        final String key = this.m_charac.getType().name() + "ShortDesc";
        return WakfuTranslator.getInstance().getString(key);
    }
    
    protected String getFormattedCharacDescription() {
        return CharacteristicsUtil.formatCharacteristic(this.getCharacValue());
    }
    
    protected Object getDescription() {
        final String key = this.m_charac.getType().name() + "Description";
        return WakfuTranslator.getInstance().getString(key);
    }
    
    protected Object getName() {
        return WakfuTranslator.getInstance().getString(this.m_charac.getType().name());
    }
    
    protected Object getShortName() {
        final String key = this.m_charac.getType().name() + "Short";
        return WakfuTranslator.getInstance().getString(key);
    }
    
    protected Object getPercentage() {
        final float characMax = this.getCharacMax();
        if (characMax == 0.0f) {
            return 0;
        }
        return this.getCharacValue() / characMax;
    }
    
    protected Object getFormattedPercentageDescription() {
        return CharacteristicsUtil.displayPercentage(this.getCharacValue(), this.getCharacMax(), true);
    }
    
    protected Object getPercentageDescription() {
        return CharacteristicsUtil.displayPercentage(this.getCharacValue(), this.getCharacMax(), false);
    }
    
    public String getValueDescription() {
        if (this.m_iconId != -1) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            try {
                sb.addImage(WakfuTextImageProvider._getIconUrl(this.m_iconId), 16, 16, null).append(" ");
            }
            catch (PropertyException e) {
                BaseCharacteristicView.m_logger.warn((Object)e.getMessage());
                sb.append(this.getShortName()).append(" : ");
            }
            sb.append(this.getMinMax());
            return sb.finishAndToString();
        }
        return this.getMinMax();
    }
    
    protected String getMinMax() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getCharacValue()).append('/').append(this.getCharacMax());
        return sb.toString();
    }
    
    public void updateView() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, BaseCharacteristicView.UPDATABLE_FIELDS);
    }
    
    @Override
    public void onCharacteristicUpdated(final AbstractCharacteristic charac) {
        this.updateView();
        this.m_provider.onCharacteristicUpdated(charac);
    }
    
    static {
        m_logger = Logger.getLogger((Class)BaseCharacteristicView.class);
        UPDATABLE_FIELDS = new String[] { "max", "minMax", "percent", "percentDescription", "value", "valueDescription", "formattedValueDescription", "formattedPercentDescription" };
    }
}
