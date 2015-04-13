package com.ankamagames.wakfu.client.core.game.characterInfo.colors;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class CharacterColor implements FieldProvider
{
    public static final String INDEX_FIELD = "index";
    public static final String COLOR_FIELD = "color";
    public static final String FACTOR_FIELD = "factor";
    public static String[] FIELDS;
    private final int m_index;
    private final Color m_color;
    private int m_factor;
    
    public CharacterColor(final int index, final float red, final float green, final float blue) {
        super();
        this.m_index = index;
        this.m_color = new Color(red, green, blue, 1.0f);
    }
    
    public float getRed() {
        return this.m_color.getRed();
    }
    
    public float getGreen() {
        return this.m_color.getGreen();
    }
    
    public float getBlue() {
        return this.m_color.getBlue();
    }
    
    public Color getColor() {
        return this.m_color;
    }
    
    public void setFactor(final int factor) {
        this.m_factor = factor;
    }
    
    public float[] getCustomColor() {
        return new float[] { this.getRed() * 1.25f, this.getGreen() * 1.25f, this.getBlue() * 1.25f, 1.0f };
    }
    
    public int getIndex() {
        return this.m_index;
    }
    
    @Override
    public String[] getFields() {
        return CharacterColor.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("index")) {
            return this.m_index;
        }
        if (fieldName.equals("color")) {
            return this.m_color;
        }
        if (fieldName.equals("factor")) {
            return this.m_factor;
        }
        return null;
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
    
    public static void applyColor(final CharacterColor color, final CharacterActor actor, final int colorIndex) {
        final float[] c = (float[])((color != null) ? color.getCustomColor() : null);
        actor.setCustomColor(colorIndex, c);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final CharacterColor that = (CharacterColor)o;
        if (this.m_factor != that.m_factor) {
            return false;
        }
        if (this.m_index != that.m_index) {
            return false;
        }
        if (this.m_color != null) {
            if (this.m_color.equals(that.m_color)) {
                return true;
            }
        }
        else if (that.m_color == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.m_index;
        result = 31 * result + ((this.m_color != null) ? this.m_color.hashCode() : 0);
        result = 31 * result + this.m_factor;
        return result;
    }
    
    static {
        CharacterColor.FIELDS = new String[] { "index", "color", "factor" };
    }
}
