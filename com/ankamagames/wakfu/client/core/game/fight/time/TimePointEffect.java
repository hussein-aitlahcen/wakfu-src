package com.ankamagames.wakfu.client.core.game.fight.time;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.fight.time.buff.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import java.util.*;

public class TimePointEffect extends ImmutableFieldProvider
{
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String IS_AVAILABLE_FIELD = "isAvailable";
    public static final String IS_UNKNOWN_FIELD = "isUnknown";
    public static final String DIMENSION_FIELD = "dimension";
    public static final String STACK_FIELD = "stack";
    private final ArrayList<WakfuEffect> m_effect;
    private boolean m_unknown;
    private int m_stack;
    private Dimension m_dimension;
    
    public TimePointEffect(final boolean unknown) {
        this(null, unknown);
    }
    
    public TimePointEffect(final WakfuEffect effect) {
        this(effect, false);
    }
    
    public TimePointEffect(final WakfuEffect effect, final boolean unknown) {
        super();
        this.m_effect = new ArrayList<WakfuEffect>();
        this.m_stack = 0;
        this.m_dimension = new Dimension(32, 32);
        this.m_effect.clear();
        if (effect != null) {
            this.m_effect.add(effect);
        }
        this.m_unknown = unknown;
    }
    
    public WakfuEffect getEffect() {
        if (this.m_effect.isEmpty()) {
            return null;
        }
        return this.m_effect.get(0);
    }
    
    public void setEffect(final WakfuEffect effect) {
        this.m_effect.clear();
        if (effect != null) {
            this.m_effect.add(effect);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "iconUrl", "description");
    }
    
    public void incrementStack() {
        ++this.m_stack;
    }
    
    public void setDimension(final int width, final int height) {
        this.m_dimension.setSize(width, height);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "dimension");
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("iconUrl")) {
            final WakfuEffect effect = this.getEffect();
            if (effect != null) {
                return WakfuConfiguration.getInstance().getIconUrl("timePointBonusIconsPath", "defaultIconPath", TimelineBuffListManager.INSTANCE.getGfx(effect.getEffectId()));
            }
        }
        else if (fieldName.equals("description")) {
            if (this.m_unknown) {
                return WakfuTranslator.getInstance().getString("fight.timePointBar.unknownBonus");
            }
            if (this.m_effect.isEmpty()) {
                return WakfuTranslator.getInstance().getString("fight.timePointBar.noBonus");
            }
            final StringBuffer stringBuffer = new StringBuffer();
            final ArrayList<String> strings = CastableDescriptionGenerator.generateDescription(new DummyEffectContainerWriter(this.m_effect, 0, (short)1, true, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY, 0));
            boolean first = true;
            for (final String s : strings) {
                if (!first) {
                    stringBuffer.append("\n");
                }
                else {
                    first = false;
                }
                stringBuffer.append(s);
            }
            return stringBuffer.toString();
        }
        else {
            if (fieldName.equals("isUnknown")) {
                return this.m_unknown;
            }
            if (fieldName.equals("isAvailable")) {
                return this.m_stack != 0;
            }
            if (fieldName.equals("stack")) {
                return this.m_stack;
            }
            if (fieldName.equals("dimension")) {
                return this.m_dimension;
            }
        }
        return null;
    }
}
