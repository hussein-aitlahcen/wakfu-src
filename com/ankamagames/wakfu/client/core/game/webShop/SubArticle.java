package com.ankamagames.wakfu.client.core.game.webShop;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.*;

public class SubArticle extends ImmutableFieldProvider
{
    public static final String RENT_TYPE = "rent.type";
    public static final String RENT_DURATION = "rent.duration";
    public static final String SERVER = "server.id";
    public static final String STATUS = "STATUS";
    public static final String WKCHARACTERS = "WKCHARACTERS";
    public static final String VAULTUPGRADE = "WKVAULTUP";
    public static final String TYPE = "TYPE";
    private ReferenceItem m_item;
    private final int m_contentId;
    private HashMap<String, String> m_metadata;
    private final ArticleType m_type;
    
    public SubArticle(final int contentId, final ArticleType type) {
        super();
        this.m_metadata = null;
        this.m_contentId = contentId;
        this.m_item = ReferenceItemManager.getInstance().getReferenceItem(contentId);
        this.m_type = type;
    }
    
    public void addData(final String key, final String value) {
        if (this.m_metadata == null) {
            this.m_metadata = new HashMap<String, String>();
        }
        this.m_metadata.put(key, value);
    }
    
    public int getMetaIntValue(final String key) {
        if (this.m_metadata == null) {
            return 0;
        }
        return PrimitiveConverter.getInteger(this.m_metadata.get(key));
    }
    
    @Nullable
    public String getMetaStringValue(final String key) {
        if (this.m_metadata == null) {
            return null;
        }
        return this.m_metadata.get(key);
    }
    
    public int getContentId() {
        return this.m_contentId;
    }
    
    public ReferenceItem getItem() {
        return this.m_item;
    }
    
    public ArticleType getType() {
        return this.m_type;
    }
    
    @Override
    public String[] getFields() {
        return SubArticle.NO_FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (this.m_item != null) {
            return this.m_item.getFieldValue(fieldName);
        }
        return null;
    }
    
    public boolean isARental() {
        return this.m_metadata != null && this.m_metadata.get("rent.type") != null;
    }
    
    public boolean isServerRestrictionOK() {
        if (this.m_metadata == null) {
            return true;
        }
        final String serverData = this.m_metadata.get("server.id");
        if (serverData == null || serverData.isEmpty()) {
            return true;
        }
        final int serverId = WakfuGameEntity.getInstance().getServerId();
        return Integer.parseInt(serverData) == serverId;
    }
    
    public String getRentalDescription() {
        final int type = this.getMetaIntValue("rent.type");
        final int duration = this.getMetaIntValue("rent.duration");
        switch (type) {
            case 2: {
                final GameInterval interval = new GameInterval(0, 0, 0, duration);
                return TimeUtils.getLongDescription(interval);
            }
            case 1: {
                return WakfuTranslator.getInstance().getString("desc.rentInfoType." + type, duration);
            }
            default: {
                return type + ", " + duration;
            }
        }
    }
}
