package com.ankamagames.wakfu.client.core.game.webShop;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.framework.net.download.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.soap.shop.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class Highlight extends ImmutableFieldProvider implements DownloadableFieldProviderListener
{
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ICON_URL = "iconUrl";
    public static final String ARTICLE = "article";
    public static final String HAS_CATEGORY = "hasCategory";
    public static final String GONDOLA_HEAD = "gondolaHead";
    private final int m_id;
    private final String m_name;
    private final String m_description;
    private final String m_link;
    private final String m_type;
    private final String m_mode;
    private final ArrayList<ImageData> m_imageData;
    private final Article m_article;
    private final GondolaHead m_gondolaHead;
    private final int m_categoryId;
    private DownloadableFieldProvider m_iconUrl;
    
    public Highlight(final int id, final String name, final String description, final String link, final String type, final String mode, final ArrayList<ImageData> images, final Article article, final GondolaHead gondolaHead, final int categoryId) {
        super();
        this.m_id = id;
        this.m_name = name;
        this.m_description = description;
        this.m_link = link;
        this.m_type = type;
        this.m_mode = mode;
        this.m_imageData = images;
        this.m_article = article;
        this.m_gondolaHead = gondolaHead;
        this.m_categoryId = categoryId;
        this.checkEntry();
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public String getLink() {
        return this.m_link;
    }
    
    public int getCategoryId() {
        return this.m_categoryId;
    }
    
    public String getType() {
        return this.m_type;
    }
    
    public String getMode() {
        return this.m_mode;
    }
    
    @Override
    public String[] getFields() {
        return Highlight.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("description")) {
            return this.m_description;
        }
        if (fieldName.equals("iconUrl")) {
            return (this.m_iconUrl == null) ? null : this.m_iconUrl.getUrl();
        }
        if (fieldName.equals("article")) {
            if (this.m_article != null && this.m_article.isServerRestrictionOK()) {
                return this.m_article;
            }
            return null;
        }
        else {
            if (fieldName.equals("gondolaHead")) {
                return this.m_gondolaHead;
            }
            if (fieldName.equals("hasCategory")) {
                return this.m_categoryId >= 0;
            }
            return null;
        }
    }
    
    private void checkEntry() {
        if (this.m_imageData == null) {
            return;
        }
        final ImageData imageData = ImageDataHelper.findImageData(this.m_imageData, 667, 240);
        if (imageData != null) {
            this.m_iconUrl = ImageDataHelper.load(imageData, this, "iconUrl");
        }
    }
    
    @Override
    public void onDownloaded(final String field, final String url) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, field);
    }
    
    @Override
    public String toString() {
        return "Highlight{m_id=" + this.m_id + ", m_name='" + this.m_name + '\'' + ", m_link='" + this.m_link + '\'' + ", m_type='" + this.m_type + '\'' + ", m_mode='" + this.m_mode + '\'' + ", m_iconUrl=" + this.m_iconUrl + '}';
    }
}
