package com.ankamagames.wakfu.client.core.game.miniMap;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.fileFormat.properties.*;
import java.net.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.xml.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.wakfu.client.core.world.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.component.map.*;

public class CompleteMapParentMapZoneDescription extends WakfuParentMapZoneDescription
{
    private static final Logger m_logger;
    private long m_soundId;
    
    public CompleteMapParentMapZoneDescription(final int id) {
        super(id, null, null, null);
        this.loadChildren();
    }
    
    private void loadChildren() {
        URL url = null;
        try {
            url = new URL(WakfuConfiguration.getContentPath("completeMapCoordsPath", this.getId()));
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
        final DocumentEntry soundIdEntry = rootNode.getParameterByName("soundId");
        this.m_soundId = ((soundIdEntry != null) ? soundIdEntry.getLongValue() : -1L);
        final ArrayList<DocumentEntry> coords = rootNode.getChildrenByName("coord");
        for (int i = 0, size = coords.size(); i < size; ++i) {
            final CompleteMapMapZoneDescription zoneDescription = createZoneDescription(coords.get(i));
            this.addZoneToManager(zoneDescription);
            this.addChild(zoneDescription);
        }
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
            CompleteMapParentMapZoneDescription.m_logger.error((Object)("Probl\u00e8me lors de la lecture du fichier de map d'url : " + url));
            return null;
        }
        return doc;
    }
    
    private static CompleteMapMapZoneDescription createZoneDescription(final DocumentEntry entry) {
        final int id = entry.getParameterByName("id").getIntValue();
        final int startX = entry.getParameterByName("startX").getIntValue();
        final int startY = entry.getParameterByName("startY").getIntValue();
        final int endX = entry.getParameterByName("endX").getIntValue();
        final int endY = entry.getParameterByName("endY").getIntValue();
        final DocumentEntry decoratorEntry = entry.getParameterByName("scrollDecorator");
        final byte scrollDecorator = (byte)((decoratorEntry != null) ? decoratorEntry.getByteValue() : 0);
        final DocumentEntry animStaticEntry = entry.getParameterByName("animName1");
        final String animStatic = (animStaticEntry != null) ? animStaticEntry.getStringValue() : null;
        final DocumentEntry animHighlightEntry = entry.getParameterByName("animName2");
        final String animHighlight = (animHighlightEntry != null) ? animHighlightEntry.getStringValue() : null;
        final DocumentEntry highlightSoundEntry = entry.getParameterByName("highlightSoundId");
        final long highlightSoundId = (highlightSoundEntry != null) ? highlightSoundEntry.getLongValue() : -1L;
        final DocumentEntry typeEntry = entry.getParameterByName("type");
        MapZoneManager.MapZoneType type = MapZoneManager.MapZoneType.INSTANCE;
        if (typeEntry != null) {
            final MapZoneManager.MapZoneType mapZoneType = MapZoneManager.MapZoneType.fromName(typeEntry.getStringValue());
            if (mapZoneType != null) {
                type = mapZoneType;
            }
        }
        String name = null;
        if (type == MapZoneManager.MapZoneType.INSTANCE) {
            name = ((id == 0) ? "???" : WakfuTranslator.getInstance().getString(77, id, new Object[0]));
        }
        else if (type == MapZoneManager.MapZoneType.FULL_MAP) {
            name = ((id == 0) ? "???" : WakfuTranslator.getInstance().getString(145, id, new Object[0]));
        }
        else if (type == MapZoneManager.MapZoneType.DECO && id >= 0) {
            name = ((id == 0) ? "???" : WakfuTranslator.getInstance().getString(144, id, new Object[0]));
        }
        return new CompleteMapMapZoneDescription(createList(startX, endX, startY, endY), id, type, name, scrollDecorator, animStatic, animHighlight, highlightSoundId);
    }
    
    private void addZoneToManager(final CompleteMapMapZoneDescription complete) {
        final MapZoneManager.MapZoneType mapZoneType = complete.getMapZoneType();
        final int id = complete.getId();
        WakfuParentMapZoneDescription zone = null;
        switch (mapZoneType) {
            case FULL_MAP: {
                zone = new CompleteMapParentMapZoneDescription(id);
                break;
            }
            case PARENT_MAP: {
                final short instanceId = (short)id;
                final TShortArrayList mapIds = WorldInfoManager.getInstance().getInfoWithParentWorld(instanceId);
                zone = new SplitMapParentMapZoneDescription(instanceId, mapIds);
                break;
            }
            case INSTANCE: {
                if (id != 0) {
                    zone = new InstanceParentMapZoneDescription(id, false);
                    break;
                }
                break;
            }
        }
        if (zone != null) {
            ((DefaultParentMapZoneDescription<CompleteMapParentMapZoneDescription>)zone).setParent(this);
            MapZoneManager.getInstance().addMapZone(mapZoneType, zone.getId(), zone);
        }
    }
    
    private static PartitionList createList(final int startX, final int endX, final int startY, final int endY) {
        final PartitionList list = new PartitionList();
        for (int x = startX; x <= endX; ++x) {
            for (int y = startY; y <= endY; ++y) {
                list.add(x, y);
            }
        }
        return list;
    }
    
    @Override
    public Color getZoneColor() {
        return Color.WHITE_ALPHA;
    }
    
    @Override
    public String getMapUrl() {
        try {
            return WakfuConfiguration.getContentPath("completeMapPath", this.getId());
        }
        catch (PropertyException e) {
            return null;
        }
    }
    
    public long getSoundId() {
        return this.m_soundId;
    }
    
    @Override
    public void onLoad(final AbstractMapManager manager) {
        manager.getLandMarkHandler().clearAllPoints();
    }
    
    @Override
    public boolean canZoomIn() {
        final WakfuParentMapZoneDescription childZone = this.findLocalPlayerChild();
        return childZone != null;
    }
    
    @Override
    public void zoomIn(final AbstractMapManager mapManager) {
        final WakfuParentMapZoneDescription zone = this.findLocalPlayerChild();
        if (zone != null) {
            MapManager.getInstance().setMap(zone);
        }
    }
    
    private WakfuParentMapZoneDescription findLocalPlayerChild() {
        WakfuParentMapZoneDescription zone;
        for (zone = MapZoneManager.getInstance().getZone(MapZoneManager.MapZoneType.INSTANCE, WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId()); zone != null && zone.getParent() != this; zone = zone.getParent()) {}
        return zone;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CompleteMapParentMapZoneDescription.class);
    }
}
