package com.ankamagames.wakfu.client.core.game.almanach.zodiac;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.framework.net.download.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class AlmanachZodiacEntryView extends ImmutableFieldProvider implements DownloadableFieldProviderListener
{
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ICON_URL = "iconUrl";
    private final GameDateConst m_date;
    private AlmanachZodiacEntry m_entry;
    private DownloadableFieldProvider m_zodiakIconUrl;
    
    public AlmanachZodiacEntryView(final GameDateConst date) {
        super();
        this.m_date = new GameDate(date);
    }
    
    public void setEntry(final AlmanachZodiacEntry entry) {
        this.m_entry = entry;
        this.checkEntry();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "name", "description");
    }
    
    private void checkEntry() {
        if (this.m_entry == null) {
            return;
        }
    }
    
    public AlmanachZodiacEntry getEntry() {
        return this.m_entry;
    }
    
    public GameDateConst getDate() {
        return this.m_date;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (this.m_entry == null) {
            return null;
        }
        if (fieldName.equals("name")) {
            return this.m_entry.getName();
        }
        if (fieldName.equals("description")) {
            return this.m_entry.getDescription();
        }
        if (fieldName.equals("iconUrl")) {
            return (this.m_zodiakIconUrl != null) ? this.m_zodiakIconUrl.getUrl() : null;
        }
        return null;
    }
    
    @Override
    public void onDownloaded(final String field, final String url) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, field);
    }
}
