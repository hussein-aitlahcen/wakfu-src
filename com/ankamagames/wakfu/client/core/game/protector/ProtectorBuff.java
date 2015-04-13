package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.*;

public class ProtectorBuff extends com.ankamagames.wakfu.common.game.protector.ProtectorBuff implements FieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String ICON_URL_FIELD = "iconUrl";
    public static final String DESCRIPTION_FIELD = "description";
    private int m_gfxId;
    private String m_name;
    private String m_description;
    
    protected ProtectorBuff(final int id, final SimpleCriterion criterion, final byte origin, final ArrayList<WakfuStandardEffect> effects) {
        super(id, criterion, origin, effects);
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public void setGfxId(final int gfxId) {
        this.m_gfxId = gfxId;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public void setDescription(final String description) {
        this.m_description = description;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("iconUrl")) {
            return WakfuConfiguration.getInstance().getIconUrl("protectorBuffsIconsPath", "defaultIconPath", this.m_gfxId);
        }
        if (fieldName.equals("description")) {
            return this.m_description;
        }
        return null;
    }
    
    @Override
    public String[] getFields() {
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
}
