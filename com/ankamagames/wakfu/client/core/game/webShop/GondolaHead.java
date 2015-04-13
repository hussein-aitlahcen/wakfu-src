package com.ankamagames.wakfu.client.core.game.webShop;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.framework.net.download.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.soap.shop.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class GondolaHead extends ImmutableFieldProvider implements DownloadableFieldProviderListener
{
    public static final String NAME = "name";
    public static final String ICON_URL = "iconUrl";
    public static final String BANNER_ICON_URL = "bannerIconUrl";
    public static final String ARTICLES = "articles";
    private final int m_id;
    private final String m_name;
    private final String m_link;
    private final String m_type;
    private final ArrayList<ImageData> m_imageDataList;
    private DownloadableFieldProvider m_iconUrl;
    private DownloadableFieldProvider m_bannerIconUrl;
    private WebShopSession.GondolaHeadListArticlesProvider m_provider;
    
    public GondolaHead(final int id, final String name, final String link, final String type, final ArrayList<ImageData> imageDataList, final int maxArticles) {
        super();
        this.m_id = id;
        this.m_name = name;
        this.m_link = link;
        this.m_type = type;
        this.m_imageDataList = imageDataList;
        this.checkEntry(maxArticles);
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
    
    public String getType() {
        return this.m_type;
    }
    
    public ArrayList<Article> getArticles() {
        return (this.m_provider != null) ? this.m_provider.getArticles() : null;
    }
    
    @Override
    public String[] getFields() {
        return GondolaHead.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_name;
        }
        if (fieldName.equals("iconUrl")) {
            return (this.m_iconUrl == null) ? null : this.m_iconUrl.getUrl();
        }
        if (fieldName.equals("bannerIconUrl")) {
            return (this.m_bannerIconUrl == null) ? null : this.m_bannerIconUrl.getUrl();
        }
        if (fieldName.equals("articles")) {
            return this.getArticles();
        }
        return null;
    }
    
    private void checkEntry(final int maxArticles) {
        if (this.m_imageDataList == null) {
            return;
        }
        final ImageData imageData = ImageDataHelper.findImageData(this.m_imageDataList, 48, 19);
        if (imageData != null) {
            this.m_iconUrl = ImageDataHelper.load(imageData, this, "iconUrl");
        }
        final ImageData bannerImageData = ImageDataHelper.findImageData(this.m_imageDataList, 90, 178);
        if (bannerImageData != null) {
            this.m_bannerIconUrl = ImageDataHelper.load(bannerImageData, this, "bannerIconUrl");
        }
        if (maxArticles > 0) {
            this.m_provider = new WebShopSession.GondolaHeadListArticlesProvider(this.m_id, maxArticles, 1, new WebShopSession.ArticlesListener() {
                @Override
                public void onArticlesList(final ArrayList<Article> articles, final int count) {
                    PropertiesProvider.getInstance().firePropertyValueChanged(GondolaHead.this, "articles");
                }
                
                @Override
                public void onError() {
                }
            });
        }
    }
    
    @Override
    public void onDownloaded(final String field, final String url) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, field);
    }
    
    @Override
    public String toString() {
        return "GondolaHead{m_id=" + this.m_id + ", m_name='" + this.m_name + '\'' + ", m_link='" + this.m_link + '\'' + ", m_type='" + this.m_type + '\'' + ", m_iconUrl=" + this.m_iconUrl + '}';
    }
}
