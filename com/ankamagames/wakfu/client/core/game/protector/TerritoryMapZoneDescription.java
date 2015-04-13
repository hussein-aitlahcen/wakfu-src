package com.ankamagames.wakfu.client.core.game.protector;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.fileFormat.xml.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.net.*;
import java.util.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.component.map.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;

public class TerritoryMapZoneDescription extends WakfuMapZoneDescription
{
    private static final Logger m_logger;
    private Territory m_territory;
    
    private static byte getMaskIndex(final Territory territory) {
        String path;
        try {
            path = String.format(WakfuConfiguration.getInstance().getString("fullSubMapPath"), territory.getInstanceId(), territory.getId() / 100);
            try {
                path = WorldMapFileHelper.getURL(path).toString();
            }
            catch (MalformedURLException e2) {
                return 1;
            }
        }
        catch (PropertyException e3) {
            return 1;
        }
        URL url;
        try {
            url = ContentFileHelper.getURL(path);
        }
        catch (MalformedURLException e4) {
            return 1;
        }
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        final XMLDocumentContainer doc = new XMLDocumentContainer();
        InputStream in;
        try {
            in = url.openStream();
        }
        catch (IOException e5) {
            return 1;
        }
        final BufferedInputStream is = new BufferedInputStream(in);
        try {
            accessor.open(is);
            accessor.read(doc, new DocumentEntryParser[0]);
            accessor.close();
        }
        catch (Exception e6) {
            return 1;
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                TerritoryMapZoneDescription.m_logger.warn((Object)e);
            }
        }
        final ArrayList<? extends DocumentEntry> children = doc.getRootNode().getChildrenByName("mapNavigatorBackgroundPart");
        final DocumentEntry entry = (DocumentEntry)children.get(0);
        final DocumentEntry level = entry.getParameterByName("maskLevel");
        return (byte)(level.getByteValue() + 1);
    }
    
    public TerritoryMapZoneDescription(final Territory territory) {
        super(territory.getPartitions(), territory.getId(), null, null, null, getMaskIndex(territory), null, null, -1L, true);
        this.m_territory = territory;
    }
    
    @Override
    public PartitionList getPartitionList() {
        return super.getPartitionList();
    }
    
    @Override
    public Color getZoneColor() {
        final Protector protector = (Protector)this.m_territory.getProtector();
        if (protector == null) {
            return TerritoryViewConstants.NONE;
        }
        final int protectorNationId = protector.getCurrentNationId();
        if (protectorNationId == -1) {
            return TerritoryViewConstants.NONE;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            return TerritoryViewConstants.ENNEMY;
        }
        final ClientCitizenComportment citizen = (ClientCitizenComportment)localPlayer.getCitizenComportment();
        return DiplomacyColorHelper.getColor(citizen, NationManager.INSTANCE.getNationById(protectorNationId));
    }
    
    @Override
    public String getIconUrl() {
        return null;
    }
    
    @Override
    public String getTextDescription() {
        return null;
    }
    
    public Territory getTerritory() {
        return this.m_territory;
    }
    
    @Override
    public boolean canZoomIn() {
        final ParentMapZoneDescription zone = MapZoneManager.getInstance().getZone(MapZoneManager.MapZoneType.SUB_INSTANCE, this.m_territory.getId());
        return zone != null;
    }
    
    @Override
    public void sendZoneToMap(final MapManager mapManager) {
        final WakfuParentMapZoneDescription zone = MapZoneManager.getInstance().getZone(MapZoneManager.MapZoneType.SUB_INSTANCE, this.m_territory.getId());
        mapManager.setMap(zone);
    }
    
    static {
        m_logger = Logger.getLogger((Class)TerritoryMapZoneDescription.class);
    }
}
