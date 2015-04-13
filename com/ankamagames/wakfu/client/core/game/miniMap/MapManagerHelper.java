package com.ankamagames.wakfu.client.core.game.miniMap;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.properties.*;
import java.net.*;
import com.ankamagames.framework.fileFormat.xml.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.xulor2.util.*;

public class MapManagerHelper
{
    private static final Logger m_logger;
    
    public static void loadMapDefinition() {
        URL url;
        try {
            url = new URL(WakfuConfiguration.getContentPath("mapDefinitionPath"));
        }
        catch (PropertyException e) {
            return;
        }
        catch (MalformedURLException e2) {
            return;
        }
        final XMLDocumentContainer doc = loadXml(url);
        if (doc == null) {
            return;
        }
        final XMLDocumentNode rootNode = doc.getRootNode();
        final DocumentEntry idParam = rootNode.getParameterByName("id");
        final int id = idParam.getIntValue();
        final WakfuParentMapZoneDescription rootMap = new CompleteMapParentMapZoneDescription(id);
        MapZoneManager.getInstance().addMapZone(MapZoneManager.MapZoneType.FULL_MAP, id, rootMap);
    }
    
    private static XMLDocumentContainer loadXml(final URL url) {
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        final XMLDocumentContainer doc = new XMLDocumentContainer();
        try {
            final BufferedInputStream stream = new BufferedInputStream(url.openStream());
            accessor.open(stream);
            accessor.read(doc, new DocumentEntryParser[0]);
            accessor.close();
            stream.close();
        }
        catch (Exception e) {
            MapManagerHelper.m_logger.error((Object)("Probl\u00e8me lors de la lecture du fichier de map d'url : " + url));
            return null;
        }
        return doc;
    }
    
    public static void loadMap() {
        loadMap(true);
    }
    
    public static void loadMap(final boolean withTerritory) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        WakfuParentMapZoneDescription zone = checkMapZoneInstanceCreation(localPlayer.getInstanceId(), true);
        if (withTerritory) {
            final Territory territory = localPlayer.getCurrentTerritory();
            if (territory != null) {
                final WakfuParentMapZoneDescription subzone = checkMapZoneSubInstanceCreation(territory.getId(), localPlayer.getInstanceId());
                if (subzone != null) {
                    zone = subzone;
                }
            }
        }
        MapManager.getInstance().setMap(zone);
        MiniMapManager.getInstance().setMap(zone);
    }
    
    public static WakfuParentMapZoneDescription checkMapZoneInstanceCreation(final short instanceId, final boolean createSubZones) {
        InstanceParentMapZoneDescription zone = (InstanceParentMapZoneDescription)MapZoneManager.getInstance().getZone(MapZoneManager.MapZoneType.INSTANCE, instanceId);
        if (zone == null) {
            try {
                String path = WakfuConfiguration.getContentPath("fullMapPath", instanceId);
                try {
                    path = WorldMapFileHelper.getURL(path).toString();
                }
                catch (MalformedURLException e) {
                    return null;
                }
                if (!URLUtils.urlExists(path)) {
                    return null;
                }
            }
            catch (PropertyException e2) {
                return null;
            }
            zone = new InstanceParentMapZoneDescription(instanceId, createSubZones);
            MapZoneManager.getInstance().addMapZone(MapZoneManager.MapZoneType.INSTANCE, instanceId, zone);
        }
        if (createSubZones && !zone.isHasLoadedSubTerritories()) {
            zone.loadSubMaps();
        }
        return zone;
    }
    
    public static WakfuParentMapZoneDescription checkMapZoneSubInstanceCreation(final int territoryId, final short instanceId) {
        WakfuParentMapZoneDescription zone = MapZoneManager.getInstance().getZone(MapZoneManager.MapZoneType.SUB_INSTANCE, territoryId);
        if (zone != null) {
            return zone;
        }
        try {
            String path = String.format(WakfuConfiguration.getContentPath("fullSubMapPath"), instanceId, territoryId / 100);
            try {
                path = WorldMapFileHelper.getURL(path).toString();
            }
            catch (MalformedURLException e) {
                return null;
            }
            if (!URLUtils.urlExists(path)) {
                return null;
            }
        }
        catch (PropertyException e2) {
            return null;
        }
        zone = new SubInstanceParentMapZoneDescription(territoryId, instanceId);
        MapZoneManager.getInstance().addMapZone(MapZoneManager.MapZoneType.SUB_INSTANCE, territoryId, zone);
        return zone;
    }
    
    public static void addCharacterPoint(final CharacterInfo info, final AbstractMapManager manager) {
        assert info != null && manager != null;
        float[] color;
        if (info.getType() == 1) {
            color = WakfuClientConstants.MINI_MAP_POINT_COLOR_MONSTER;
        }
        else if (info.isLocalPlayer()) {
            color = WakfuClientConstants.MINI_MAP_POINT_COLOR_LOCAL_PLAYER;
        }
        else {
            final LocalPlayerCharacter lpc = WakfuGameEntity.getInstance().getLocalPlayer();
            if (lpc.getPartyComportment().isInParty() && lpc.getPartyComportment().getParty().getMember(info.getId()) != null) {
                color = WakfuClientConstants.MINI_MAP_POINT_COLOR_PARTY_MEMBER;
            }
            else {
                color = WakfuClientConstants.MINI_MAP_POINT_COLOR_PLAYER;
            }
        }
        addCharacterPoint(info, color, manager);
    }
    
    public static void addCharacterCompass(final CharacterInfo info, final AbstractMapManager manager) {
        addCharacterCompass(info, WakfuClientConstants.MINI_MAP_POINT_COLOR_COMPASS_DEFAULT, manager);
    }
    
    public static void addCharacterPoint(final CharacterInfo info, final float[] color, final AbstractMapManager manager) {
        addCharacter(info, true, false, color, manager);
    }
    
    public static void addCharacterCompass(final CharacterInfo info, final float[] color, final AbstractMapManager manager) {
        addCharacter(info, false, true, color, manager);
    }
    
    private static void addCharacter(final CharacterInfo info, final boolean addToPoints, final boolean addToCompass, final float[] color, final AbstractMapManager manager) {
        assert info != null && manager != null && color != null;
        if (addToPoints) {
            addPoint(info.getId(), 0, info.getWorldCellX(), info.getWorldCellY(), info.getWorldCellAltitude(), info.getInstanceId(), info, manager.getDefaultMapPoint(), info.getControllerName(), color, manager);
        }
        if (addToCompass) {
            addCompass(info.getId(), 0, info.getWorldCellX(), info.getWorldCellY(), info.getWorldCellAltitude(), info.getInstanceId(), info, manager.getDefaultMapPoint(), info.getControllerName(), color, manager);
        }
    }
    
    public static void addActorPoint(final Actor info, final float[] color, final AbstractMapManager manager) {
        addActor(info, color, true, false, manager);
    }
    
    public static void addActorCompass(final Actor info, final float[] color, final AbstractMapManager manager) {
        addActor(info, color, false, true, manager);
    }
    
    private static void addActor(final Actor info, final float[] color, final boolean addToPoints, final boolean addToCompass, final AbstractMapManager manager) {
        assert info != null && manager != null && color != null;
        if (addToPoints) {
            addPoint(info.getId(), 0, info.getWorldX(), info.getWorldY(), info.getAltitude(), WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId(), info, manager.getDefaultMapPoint(), null, color, manager);
        }
        if (addToCompass) {
            addCompass(info.getId(), 0, info.getWorldX(), info.getWorldY(), info.getAltitude(), WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId(), info, manager.getDefaultMapPoint(), null, color, manager);
        }
    }
    
    public static void addCompass(final long id, final int type, final float x, final float y, final float altitude, final short instanceId, final Object value, final DisplayableMapPointIcon icon, final String name, final float[] color, final AbstractMapManager manager) {
        manager.addCompassPoint(id, type, x, y, altitude, instanceId, name, value, icon, color);
    }
    
    public static void addPoint(final long id, final int type, final float x, final float y, final float altitude, final short instanceId, final Object value, final DisplayableMapPointIcon icon, final String name, final float[] color, final AbstractMapManager manager) {
        addPoint(id, type, x, y, altitude, instanceId, value, icon, null, name, color, manager);
    }
    
    public static void addPoint(final long id, final int type, final float x, final float y, final float altitude, final short instanceId, final Object value, final DisplayableMapPointIcon icon, final String particlePath, final String name, final float[] color, final AbstractMapManager manager) {
        manager.addPoint(id, type, x, y, altitude, instanceId, name, value, icon, particlePath, color);
    }
    
    public static void removePoint(final int type, final long id, final AbstractMapManager manager) {
        manager.removePoint(type, id);
    }
    
    public static void removeCompass(final int type, final long id, final AbstractMapManager manager) {
        manager.removeCompass(type, id);
    }
    
    public static void removeCharacterPoint(final CharacterInfo info, final AbstractMapManager manager) {
        removePoint(0, info.getId(), manager);
    }
    
    public static void removeCharacterCompass(final CharacterInfo info, final AbstractMapManager manager) {
        removeCompass(0, info.getId(), manager);
    }
    
    public static void removeActorPoint(final Actor info, final AbstractMapManager manager) {
        removePoint(0, info.getId(), manager);
    }
    
    public static void removeActorCompass(final Actor info, final AbstractMapManager manager) {
        removeCompass(0, info.getId(), manager);
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapManagerHelper.class);
    }
}
