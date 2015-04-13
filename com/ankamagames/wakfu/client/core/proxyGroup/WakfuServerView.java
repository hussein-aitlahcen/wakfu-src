package com.ankamagames.wakfu.client.core.proxyGroup;

import com.ankamagames.wakfu.client.ui.component.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.account.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.dispatch.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.text.*;
import com.google.common.base.*;
import com.ankamagames.wakfu.common.account.admin.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.google.common.collect.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class WakfuServerView extends ImmutableFieldProvider
{
    public static final String ICON_URL = "iconUrl";
    public static final String NAME = "name";
    public static final String COMMUNITY = "community";
    public static final String STATUT = "statut";
    public static final String DESCRIPTION = "description";
    public static final String VERSION = "version";
    public static final String COMMUNITIES = "communities";
    public static final String FORCE_ACCOUNT_ENABLED = "forceAccountEnabled";
    private static final int DEFAULT_MAX_POPULATION = 8000;
    private static final float POPULATION_MEDIUM_GAP = 0.5f;
    private static final float POPULATION_CROWDED_GAP = 0.75f;
    private final Proxy m_proxy;
    private WorldInfo m_worldInfo;
    
    public WakfuServerView(final Proxy proxy, final WorldInfo worldInfo) {
        super();
        this.m_proxy = proxy;
        this.m_worldInfo = worldInfo;
    }
    
    @Nullable
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        final Community community = this.m_proxy.getCommunity();
        if (fieldName.equals("name")) {
            final String key = String.format("%s%s", "worldName.", this.m_proxy.getName().toLowerCase());
            if (WakfuTranslator.getInstance().containsKey(key)) {
                return WakfuTranslator.getInstance().getString(key);
            }
            return this.m_proxy.getName();
        }
        else {
            if (fieldName.equals("forceAccountEnabled")) {
                final Optional<Admin> admin = DispatchAccountInformation.getAdmin();
                final AdminRightsGroup group = AdminUtils.getAdminGroup(admin, this.m_worldInfo.getServerId());
                return AdminRightHelper.checkRight(group.getRights(), AdminRightsEnum.TAKE_ACCOUNT_CONTROL);
            }
            if (fieldName.equals("iconUrl")) {
                if (community == null) {
                    return null;
                }
                return WakfuTranslator.getCommunityIconUrl(community);
            }
            else if (fieldName.equals("community")) {
                if (community == null) {
                    return null;
                }
                return WakfuTranslator.getInstance().getString(String.format("%s%s", "worldCommunity.", community.getName()));
            }
            else if (fieldName.equals("description")) {
                final String originalKey = String.format("%s%s", "worldDescription.", this.m_proxy.getName().toLowerCase());
                if (WakfuTranslator.getInstance().containsKey(originalKey)) {
                    return WakfuTranslator.getInstance().getString(originalKey);
                }
                final String defaultKey = String.format("%s%s", "worldDescription.", "undefined");
                return WakfuTranslator.getInstance().getString(defaultKey);
            }
            else if (fieldName.equals("version")) {
                final byte[] version = this.m_worldInfo.getVersion();
                if (ArrayUtils.isEmpty(version)) {
                    return null;
                }
                return Version.format(version);
            }
            else {
                if (fieldName.equals("communities")) {
                    final ImmutableList<Community> communities = WorldInfoHelper.getCommunities(this.m_worldInfo);
                    final Collection<String> iconList = new ArrayList<String>();
                    for (final Community com : communities) {
                        iconList.add(WakfuTranslator.getCommunityIconUrl(com));
                    }
                    return iconList;
                }
                if (fieldName.equals("statut")) {
                    String text;
                    Color color;
                    if (this.m_worldInfo == null) {
                        text = WakfuTranslator.getInstance().getString("disconnected");
                        color = Color.GRAY;
                    }
                    else if (!com.ankamagames.baseImpl.common.clientAndServer.utils.Version.checkVersion(this.m_worldInfo.getVersion())) {
                        text = WakfuTranslator.getInstance().getString("badVersion");
                        color = Color.RED;
                    }
                    else if (this.m_worldInfo.isLocked()) {
                        text = WakfuTranslator.getInstance().getString("maintenance");
                        color = Color.GRAY;
                    }
                    else {
                        final int playerLimit = (this.m_worldInfo.getPlayerLimit() > 0) ? this.m_worldInfo.getPlayerLimit() : 8000;
                        final int playerCount = this.m_worldInfo.getPlayerCount();
                        if (playerCount > playerLimit * 0.75f) {
                            text = WakfuTranslator.getInstance().getString("population.high");
                            color = Color.RED;
                        }
                        else if (playerCount > playerLimit * 0.5f) {
                            text = WakfuTranslator.getInstance().getString("population.medium");
                            color = Color.ORANGE;
                        }
                        else {
                            text = WakfuTranslator.getInstance().getString("population.low");
                            color = Color.DARK_GREEN;
                        }
                    }
                    final TextWidgetFormater sb = new TextWidgetFormater();
                    sb.openText().addColor(color);
                    sb.append(text);
                    return sb.finishAndToString();
                }
                return null;
            }
        }
    }
    
    public Proxy getProxy() {
        return this.m_proxy;
    }
    
    public WorldInfo getWorldInfo() {
        return this.m_worldInfo;
    }
    
    public void setWorldInfo(final WorldInfo worldInfo) {
        this.m_worldInfo = worldInfo;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "statut");
    }
}
