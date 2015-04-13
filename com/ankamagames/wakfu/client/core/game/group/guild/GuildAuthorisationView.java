package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class GuildAuthorisationView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String CHECKED_FIELD = "checked";
    public static final String[] FIELDS;
    private final GuildRankAuthorisation m_rankAuthorisation;
    private boolean m_checked;
    
    public GuildAuthorisationView(final GuildRankAuthorisation rankAuthorisation) {
        super();
        this.m_rankAuthorisation = rankAuthorisation;
    }
    
    @Override
    public String[] getFields() {
        return GuildAuthorisationView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return WakfuTranslator.getInstance().getString(this.m_rankAuthorisation.name());
        }
        if (fieldName.equals("checked")) {
            return this.m_checked;
        }
        return null;
    }
    
    public void setChecked(final boolean checked) {
        this.m_checked = checked;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "checked");
    }
    
    public boolean isChecked() {
        return this.m_checked;
    }
    
    public GuildRankAuthorisation getRankAuthorisation() {
        return this.m_rankAuthorisation;
    }
    
    static {
        FIELDS = new String[] { "name", "checked" };
    }
}
