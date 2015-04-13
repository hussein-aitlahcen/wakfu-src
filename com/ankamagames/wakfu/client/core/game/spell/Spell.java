package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.spell.*;

public class Spell extends AbstractSpell implements FieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String SMALL_ICON_URL_FIELD = "smallIconUrl";
    public static final String BIG_ICON_URL_FIELD = "bigIconUrl";
    public static final String ID_FIELD = "id";
    public static final String[] FIELDS;
    private final int m_scriptId;
    private final boolean m_useAutomaticDescription;
    private final boolean m_showInTimeline;
    private final int m_gfxId;
    private final boolean m_isSramShadowSpell;
    private boolean m_selected;
    
    public Spell(final SpellParameters params) {
        super(params);
        this.m_scriptId = params.getScriptId();
        this.m_useAutomaticDescription = params.getUseAutomaticDescription();
        this.m_showInTimeline = params.isShowInTimeline();
        this.m_gfxId = params.getGfxId();
        this.m_isSramShadowSpell = params.isSramShadowSpell();
    }
    
    public int getScriptId() {
        return this.m_scriptId;
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(3, this.getId(), new Object[0]);
    }
    
    public String getBackgroundDescription() {
        return WakfuTranslator.getInstance().getString(4, this.getId(), new Object[0]);
    }
    
    public boolean isUseAutomaticDescription() {
        return this.m_useAutomaticDescription;
    }
    
    public boolean isShownInTimeline() {
        return this.m_showInTimeline;
    }
    
    public boolean isSramShadowSpell() {
        return this.m_isSramShadowSpell;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    @Override
    public String[] getFields() {
        return Spell.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("description")) {
            return this.getBackgroundDescription();
        }
        if (fieldName.equals("smallIconUrl")) {
            return WakfuConfiguration.getInstance().getSpellSmallIcon(this.m_gfxId);
        }
        if (fieldName.equals("bigIconUrl")) {
            return WakfuConfiguration.getInstance().getSpellBigIcon(this.m_gfxId);
        }
        if (fieldName.equals("id")) {
            return this.getId();
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
    
    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj) || (obj instanceof SpellLevel && this.equals(((AbstractSpellLevel<Object>)obj).getSpell()));
    }
    
    static {
        FIELDS = new String[] { "name", "description", "smallIconUrl", "bigIconUrl", "id" };
    }
}
