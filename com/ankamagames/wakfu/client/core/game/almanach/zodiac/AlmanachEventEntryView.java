package com.ankamagames.wakfu.client.core.game.almanach.zodiac;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.almanach.*;
import com.ankamagames.framework.net.download.*;
import java.io.*;

public class AlmanachEventEntryView extends ImmutableFieldProvider implements DownloadableFieldProviderListener
{
    public static final String MERYD_NAME = "merydName";
    public static final String MERYD_DESC = "merydDesc";
    public static final String MERYD_ICON_URL = "merydIconUrl";
    private AlmanachEventEntry m_entry;
    private DownloadableFieldProvider m_ephemerisIconUrl;
    
    public void setEntry(final AlmanachEventEntry entry) {
        this.m_entry = entry;
        this.checkEntry();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "merydName", "merydDesc", "merydIconUrl");
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
        if (fieldName.equals("merydName")) {
            return this.m_entry.getBossName();
        }
        if (fieldName.equals("merydDesc")) {
            return this.m_entry.getBossText();
        }
        if (fieldName.equals("merydIconUrl")) {
            return (this.m_ephemerisIconUrl == null) ? null : this.m_ephemerisIconUrl.getUrl();
        }
        return null;
    }
    
    private void checkEntry() {
        if (this.m_entry == null) {
            return;
        }
        final String merydUrl = this.m_entry.getBossImageUrl();
        if (merydUrl == null) {
            return;
        }
        final File file = AlmanachFileHelper.getCachedFilePathFromRemoteUrl(merydUrl);
        final String cachedFileUrl = AlmanachFileHelper.fileToURL(file);
        this.m_ephemerisIconUrl = new DownloadableFieldProvider(null, cachedFileUrl, "merydIconUrl", this);
        AlmanachDataDownloader.INSTANCE.add(merydUrl, this.m_ephemerisIconUrl);
    }
    
    @Override
    public void onDownloaded(final String field, final String url) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, field);
    }
}
