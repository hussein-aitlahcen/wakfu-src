package com.ankamagames.wakfu.client.core.game.miniMap;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.jetbrains.annotations.*;

public class DisplayableMapPointIconFactory
{
    private static final Logger m_logger;
    public static final DisplayableMapPointIconFactory INSTANCE;
    private final TIntObjectHashMap<DisplayableMapPointIcon> m_icons;
    public static final DisplayableMapPointIcon ZAAP_ICON;
    public static final DisplayableMapPointIcon DRAGO_ICON;
    public static final DisplayableMapPointIcon CANNON_ICON;
    public static final DisplayableMapPointIcon BOAT_ICON;
    public static final DisplayableMapPointIcon DEFAULT_SMALL_ICON;
    public static final DisplayableMapPointIcon COMPASS_POINT_ICON;
    public static final DisplayableMapPointIcon PROTECTOR_ICON;
    public static final DisplayableMapPointIcon PROTECTOR_IN_CHAOS_ICON;
    public static final DisplayableMapPointIcon MINIMAP_POINT_ICON;
    public static final DisplayableMapPointIcon MINIMAP_POINT_BIG_ICON;
    public static final DisplayableMapPointIcon HW_BUILDING_ICON_0;
    public static final DisplayableMapPointIcon HW_BUILDING_ICON_1;
    public static final DisplayableMapPointIcon HW_BUILDING_ICON_2;
    public static final DisplayableMapPointIcon PARTY_MEMBER_ICON;
    public static final DisplayableMapPointIcon NEW_ICON;
    public static final short CRAFT_MACHINE_ICON_ID = 46;
    
    public DisplayableMapPointIconFactory() {
        super();
        this.m_icons = new TIntObjectHashMap<DisplayableMapPointIcon>();
    }
    
    @Nullable
    public DisplayableMapPointIcon getIcon(final int icon) {
        DisplayableMapPointIcon mapPointIcon = this.m_icons.get(icon);
        if (mapPointIcon != null || this.m_icons.containsKey(icon)) {
            return mapPointIcon;
        }
        final float yHotPoint = getHotPointY(icon);
        try {
            final String path = WakfuConfiguration.getInstance().getString("pointsOfInterestIconPath");
            final String texturePath = ContentFileHelper.getPath(path, icon);
            if (!URLUtils.urlExists(texturePath)) {
                DisplayableMapPointIconFactory.m_logger.warn((Object)("Impossible de trouver la texture " + texturePath));
            }
            else {
                mapPointIcon = new DisplayableMapPointIcon(texturePath, 0.5f, yHotPoint);
            }
        }
        catch (PropertyException e) {
            DisplayableMapPointIconFactory.m_logger.warn((Object)e.getMessage());
        }
        this.m_icons.put(icon, mapPointIcon);
        return mapPointIcon;
    }
    
    private static float getHotPointY(final int icon) {
        if (icon == 36) {
            return 1.0f;
        }
        return 0.66f;
    }
    
    private static DisplayableMapPointIcon createIcon(final String pathKey) {
        return createIcon(pathKey, 0.5f, 0.66f);
    }
    
    private static DisplayableMapPointIcon createIcon(final String pathKey, final float xHotPoint, final float yHotPoint) {
        try {
            final String path = WakfuConfiguration.getContentPath(pathKey);
            if (!URLUtils.urlExists(path)) {
                DisplayableMapPointIconFactory.m_logger.warn((Object)("Impossible de trouver la texture " + path + " key=" + pathKey));
                return null;
            }
            return new DisplayableMapPointIcon(path, xHotPoint, yHotPoint);
        }
        catch (PropertyException e) {
            DisplayableMapPointIconFactory.m_logger.error((Object)("Probl\u00e8me de chargement de l'icone pathKey=" + pathKey), (Throwable)e);
            return null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)DisplayableMapPointIconFactory.class);
        INSTANCE = new DisplayableMapPointIconFactory();
        NEW_ICON = createIcon("newPoiPath");
        ZAAP_ICON = createIcon("zaapPoiPath");
        DRAGO_ICON = createIcon("dragoPoiPath");
        BOAT_ICON = createIcon("boatPoiPath");
        CANNON_ICON = createIcon("cannonPoiPath");
        DEFAULT_SMALL_ICON = createIcon("pointsOfInterestDefaultSmallIconPath", 0.5f, 0.5f);
        COMPASS_POINT_ICON = createIcon("compassPointFile");
        MINIMAP_POINT_ICON = createIcon("miniMapPointFile", 0.5f, 0.5f);
        MINIMAP_POINT_BIG_ICON = createIcon("miniMapPointBigFile", 0.5f, 1.0f);
        PARTY_MEMBER_ICON = createIcon("partyMemberPoiPath", 0.5f, 1.0f);
        PROTECTOR_ICON = createIcon("pointsOfInterestProtectorIconPath");
        PROTECTOR_IN_CHAOS_ICON = createIcon("pointsOfInterestProtectorinChaosIconPath");
        HW_BUILDING_ICON_0 = createIcon("hwBuidingIconRedPath");
        HW_BUILDING_ICON_1 = createIcon("hwBuidingIconOrangePath");
        HW_BUILDING_ICON_2 = createIcon("hwBuidingIconGreenPath");
    }
}
