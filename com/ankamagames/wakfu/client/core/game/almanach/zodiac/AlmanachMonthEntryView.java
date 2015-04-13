package com.ankamagames.wakfu.client.core.game.almanach.zodiac;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.almanach.*;
import com.ankamagames.framework.net.download.*;
import java.io.*;

public class AlmanachMonthEntryView extends ImmutableFieldProvider implements DownloadableFieldProviderListener
{
    public static final String MONTH_NAME = "monthName";
    public static final String PROTECTOR_NAME = "protectorName";
    public static final String PROTECTOR_DESC = "protectorDesc";
    public static final String PROTECTOR_ICON_URL = "protectorIconUrl";
    private AlmanachMonthEntry m_entry;
    private DownloadableFieldProvider m_protectorIconUrl;
    
    public void setEntry(final AlmanachMonthEntry entry) {
        this.m_entry = entry;
        this.checkEntry();
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "monthName", "protectorName", "protectorDesc", "protectorIconUrl");
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
        if (fieldName.equals("monthName")) {
            return WakfuTranslator.getInstance().getString("calendar.month." + this.m_entry.getMonth());
        }
        if (fieldName.equals("protectorName")) {
            return this.m_entry.getProtectorName();
        }
        if (fieldName.equals("protectorDesc")) {
            return this.m_entry.getProtectorDesc();
        }
        if (fieldName.equals("protectorIconUrl")) {
            return (this.m_protectorIconUrl == null) ? null : this.m_protectorIconUrl.getUrl();
        }
        return null;
    }
    
    private void checkEntry() {
        if (this.m_entry == null) {
            return;
        }
        final String protectorImageUrl = this.m_entry.getProtectorImageUrl();
        if (protectorImageUrl == null) {
            return;
        }
        final File file = AlmanachFileHelper.getCachedFilePathFromRemoteUrl(protectorImageUrl);
        final String cachedFileUrl = AlmanachFileHelper.fileToURL(file);
        this.m_protectorIconUrl = new DownloadableFieldProvider(null, cachedFileUrl, "protectorIconUrl", this);
        AlmanachDataDownloader.INSTANCE.add(protectorImageUrl, this.m_protectorIconUrl);
    }
    
    @Override
    public void onDownloaded(final String field, final String url) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, field);
    }
}
