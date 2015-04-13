package com.ankamagames.wakfu.client.core.game.webShop;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.net.download.*;
import java.io.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class Category extends ImmutableFieldProvider implements DownloadableFieldProviderListener
{
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ICON_URL = "iconUrl";
    public static final String DEPTH_MARGIN = "depthMargin";
    public static final String DISPLAY_MODE = "displayMode";
    public static final String CHILDREN = "children";
    private final int m_id;
    private final String m_name;
    private final String m_description;
    private final String m_imageUrl;
    private final DisplayMode m_displayMode;
    private final String m_key;
    private int m_depth;
    private DownloadableFieldProvider m_iconUrl;
    private Category m_parent;
    private final List<Category> m_children;
    
    public Category(final int id, final String name, final String description, final String imageUrl, final DisplayMode displayMode, final String key) {
        super();
        this.m_children = new ArrayList<Category>();
        this.m_id = id;
        this.m_name = name;
        this.m_description = description;
        this.m_imageUrl = imageUrl;
        this.m_displayMode = displayMode;
        this.m_key = key;
        this.checkEntry();
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public void addChild(final Category child) {
        this.m_children.add(child);
        child.setParent(this);
    }
    
    public void setParent(final Category category) {
        this.m_parent = category;
        this.m_depth = category.m_depth + 1;
    }
    
    public Category getParent() {
        return this.m_parent;
    }
    
    public String getKey() {
        return this.m_key;
    }
    
    @Override
    public String[] getFields() {
        return Category.NO_FIELDS;
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
        if (fieldName.equals("depthMargin")) {
            final int pixelMargin = this.m_depth * 10;
            return "0,0," + pixelMargin + ", 0";
        }
        if (fieldName.equals("displayMode")) {
            return this.m_displayMode.getType();
        }
        if (fieldName.equals("children")) {
            return this.m_children;
        }
        return null;
    }
    
    private void checkEntry() {
        if (this.m_imageUrl != null) {
            final File file = WebShopFileHelper.getCachedFilePathFromRemoteUrl(this.m_imageUrl);
            final String cachedFileUrl = WebShopFileHelper.fileToURL(file);
            this.m_iconUrl = new DownloadableFieldProvider(null, cachedFileUrl, "iconUrl", this);
            WebShopDataDownloader.INSTANCE.add(this.m_imageUrl, this.m_iconUrl);
        }
    }
    
    @Override
    public void onDownloaded(final String field, final String url) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, field);
    }
    
    public boolean forEachChild(final TObjectProcedure<Category> procedure) {
        if (!procedure.execute(this)) {
            return false;
        }
        for (int i = 0, size = this.m_children.size(); i < size; ++i) {
            final Category category = this.m_children.get(i);
            if (!category.forEachChild(procedure)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Category{m_depth=" + this.m_depth + ", m_imageUrl='" + this.m_imageUrl + '\'' + ", m_description='" + this.m_description + '\'' + ", m_name='" + this.m_name + '\'' + ", m_id=" + this.m_id + '}';
    }
    
    public enum DisplayMode
    {
        DEFAULT(0, "default"), 
        BANNER(1, "BANNER");
        
        private final byte m_type;
        private final String m_name;
        
        private DisplayMode(final int type, final String name) {
            this.m_type = MathHelper.ensureByte(type);
            this.m_name = name;
        }
        
        public byte getType() {
            return this.m_type;
        }
        
        public String getName() {
            return this.m_name;
        }
        
        public static DisplayMode getFromName(final String name) {
            if (name == null) {
                return DisplayMode.DEFAULT;
            }
            for (final DisplayMode mode : values()) {
                if (name.equals(mode.m_name)) {
                    return mode;
                }
            }
            return DisplayMode.DEFAULT;
        }
    }
}
