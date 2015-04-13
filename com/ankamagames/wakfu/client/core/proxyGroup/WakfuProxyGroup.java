package com.ankamagames.wakfu.client.core.proxyGroup;

import com.ankamagames.baseImpl.client.proxyclient.base.network.proxy.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;

public class WakfuProxyGroup extends ProxyGroup implements FieldProvider
{
    public static final String ICON_URL = "iconUrl";
    public static final String NAME = "name";
    public static final String COMMUNITY = "community";
    
    public WakfuProxyGroup(final int index, final String name, final Community language) {
        super(index, name, language);
    }
    
    @Nullable
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        final Community community = this.getCommunity();
        if (fieldName.equals("name")) {
            final String key = String.format("%s%s", "worldName.", this.getName().toLowerCase());
            if (WakfuTranslator.getInstance().containsKey(key)) {
                return WakfuTranslator.getInstance().getString(key);
            }
            return this.getName();
        }
        else if (fieldName.equals("iconUrl")) {
            if (community == null) {
                return null;
            }
            return WakfuTranslator.getCommunityIconUrl(community);
        }
        else {
            if (!fieldName.equals("community")) {
                return null;
            }
            if (community == null) {
                return null;
            }
            return WakfuTranslator.getInstance().getString(String.format("%s%s", "worldCommunity.", community.getName()));
        }
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
