package com.ankamagames.xulor2.component.mapOverlay;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.mesh.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.xulor2.component.map.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import gnu.trove.*;

public class MapWidgetZoneManager
{
    private static final Logger m_logger;
    public static final float HALF_CELL_WIDTH = 43.0f;
    public static final float HALF_CELL_HEIGHT = 21.5f;
    private final MapMesh m_mesh;
    private final TIntObjectHashMap<ParentMapZone> m_mapZones;
    private final TIntArrayList m_mapZonesToRemove;
    private MapZone m_selectedMapZone;
    private MapZone m_playerMapZone;
    
    public MapWidgetZoneManager(final MapMesh mesh) {
        super();
        this.m_mapZones = new TIntObjectHashMap<ParentMapZone>();
        this.m_mapZonesToRemove = new TIntArrayList();
        this.m_mesh = mesh;
    }
    
    public MapZone getMapZoneFromPartition(final int x, final int y) {
        final TIntObjectIterator<ParentMapZone> it = this.m_mapZones.iterator();
        while (it.hasNext()) {
            it.advance();
            final ParentMapZone parentMapZone = it.value();
            final ArrayList<MapZone> children = parentMapZone.getChildren();
            for (int i = 0, size = children.size(); i < size; ++i) {
                final MapZone candidateMapZone = children.get(i);
                if (candidateMapZone.contains(x, y)) {
                    return candidateMapZone;
                }
            }
        }
        return null;
    }
    
    public MapZone getMapZoneFromCell(final int x, final int y) {
        return this.getMapZoneFromPartition(PartitionConstants.getPartitionXFromCellX(x), PartitionConstants.getPartitionYFromCellY(y));
    }
    
    public void clear() {
        this.m_mapZones.forEachValue(new TObjectProcedure<ParentMapZone>() {
            @Override
            public boolean execute(final ParentMapZone mapZone) {
                mapZone.cleanUp();
                return true;
            }
        });
        this.setSelectedMapZone(null);
    }
    
    public void computeMapZones(final float zoom, final float originX, final float originY) {
        final int mapWidth = 774;
        final int mapHeight = 387;
        final TIntObjectIterator<ParentMapZone> it = this.m_mapZones.iterator();
        while (it.hasNext()) {
            it.advance();
            final ParentMapZone parentMapZone = it.value();
            parentMapZone.setZoomFactor(zoom);
            computeParentZone(originX, originY, 774, 387, parentMapZone);
        }
    }
    
    private static void computeParentZone(final float originX, final float originY, final int mapWidth, final int mapHeight, final ParentMapZone parentMapZone) {
        final ArrayList<MapZone> children = parentMapZone.getChildren();
        for (int j = children.size() - 1; j >= 0; --j) {
            final MapZone mapZone = children.get(j);
            final ArrayList<MapZone.MapZoneElement> elements = mapZone.getElements();
            for (int i = elements.size() - 1; i >= 0; --i) {
                final MapZone.MapZoneElement element = elements.get(i);
                final short mapX = element.getX();
                final short mapY = element.getY();
                final int isoLocalX = mapX * 18;
                final int isoLocalY = mapY * 18;
                final float screenX = (isoLocalX - isoLocalY) * 43.0f + originX;
                final float screenY = -(isoLocalX + isoLocalY) * 21.5f + originY;
                element.setX1(screenX - mapWidth);
                element.setY1(screenY - mapHeight);
                element.setX2(screenX);
                element.setY2(screenY);
                element.setX3(screenX + mapWidth);
                element.setY3(screenY - mapHeight);
                element.setX4(screenX);
                element.setY4(screenY - 2 * mapHeight);
            }
            mapZone.computeCenter();
        }
    }
    
    public MapZone getSelectedMapZone() {
        return this.m_selectedMapZone;
    }
    
    public void setSelectedMapZone(final int partX, final int partY) {
        if (this.m_selectedMapZone != null && this.m_selectedMapZone.contains(partX, partY)) {
            return;
        }
        this.setSelectedMapZone(this.getMapZoneFromPartition(partX, partY));
    }
    
    public void setSelectedMapZone(final MapZone selectedMapZone) {
        if (this.m_selectedMapZone == selectedMapZone) {
            return;
        }
        if (this.m_selectedMapZone != null) {
            this.m_selectedMapZone.setSelected(false);
        }
        this.m_selectedMapZone = selectedMapZone;
        if (this.m_selectedMapZone != null) {
            this.m_selectedMapZone.setSelected(true);
        }
        if (this.m_mesh != null) {
            this.m_mesh.setSelectedZone((this.m_selectedMapZone != null) ? this.m_selectedMapZone.getMapZoneDescription().getMaskIndex() : -1);
        }
    }
    
    public void removeAllMapZones() {
        final TIntObjectIterator<ParentMapZone> it = this.m_mapZones.iterator();
        while (it.hasNext()) {
            it.advance();
            this.m_mapZonesToRemove.add(it.key());
        }
    }
    
    public void removeMapZone(final int id) {
        this.m_mapZonesToRemove.add(id);
    }
    
    public void setPlayerMapZone(final int cellX, final int cellY) {
        final MapZone mapZone = this.getMapZoneFromCell(cellX, cellY);
        if (mapZone == this.m_playerMapZone) {
            return;
        }
        if (this.m_playerMapZone != null) {
            this.m_playerMapZone.setLineWidth(1.0f);
            this.m_mesh.setMapZoneLineWidth(this.m_playerMapZone.getGeometryIndex(), 1.0f);
        }
        this.m_playerMapZone = mapZone;
        if (this.m_playerMapZone != null) {
            this.m_playerMapZone.setLineWidth(3.0f);
            this.m_mesh.setMapZoneLineWidth(this.m_playerMapZone.getGeometryIndex(), 5.0f);
        }
    }
    
    public void setPlayerMapZone() {
        if (this.m_playerMapZone != null) {
            this.m_mesh.setMapZoneLineWidth(this.m_playerMapZone.getGeometryIndex(), 5.0f);
        }
    }
    
    public boolean hasMapZone() {
        return !this.m_mapZones.isEmpty();
    }
    
    public void doRemoveMapZones() {
        for (int i = 0, size = this.m_mapZonesToRemove.size(); i < size; ++i) {
            final int id = this.m_mapZonesToRemove.getQuick(i);
            this.m_mesh.removeMapZone(id);
            final ParentMapZone mapZone = this.m_mapZones.remove(id);
            if (mapZone != null && mapZone.getPixmap() != null && mapZone.getPixmap().getTexture() != null) {
                mapZone.cleanUp();
            }
        }
        this.m_mapZonesToRemove.clear();
        this.setSelectedMapZone(null);
    }
    
    public void computeColorAndIndex(final float zoom, final TIntObjectHashMap<Color> colorById, final TIntByteHashMap indexById) {
        final TIntObjectIterator<ParentMapZone> it = this.m_mapZones.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().setZoomFactor(zoom);
            final ArrayList<MapZone> children = it.value().getChildren();
            for (int i = 0, size = children.size(); i < size; ++i) {
                final MapZone mapZone = children.get(i);
                final PartitionListMapZoneDescription mapZoneDescription = mapZone.getMapZoneDescription();
                final int id = mapZoneDescription.getId();
                final Color color = mapZoneDescription.getZoneColor();
                final byte maskIndex = mapZoneDescription.getMaskIndex();
                colorById.put(id, color);
                indexById.put(id, maskIndex);
            }
        }
    }
    
    public void put(final int id, final ParentMapZone parentMapZone) {
        this.m_mapZones.put(id, parentMapZone);
        TroveUtils.removeFirstValue(this.m_mapZonesToRemove, id);
    }
    
    public boolean setMapZoneVisibleById(final int id, final boolean visible) {
        final TIntObjectIterator<ParentMapZone> it = this.m_mapZones.iterator();
        while (it.hasNext()) {
            it.advance();
            final ParentMapZone parentMapZone = it.value();
            if (this.m_mapZonesToRemove.contains(it.key())) {
                continue;
            }
            final ArrayList<MapZone> children = parentMapZone.getChildren();
            for (int i = 0, size = children.size(); i < size; ++i) {
                final MapZone mapZone = children.get(i);
                if (mapZone.getMapZoneDescription().getId() == id) {
                    mapZone.setVisible(visible);
                    return true;
                }
            }
        }
        return false;
    }
    
    public static ParentMapZone createParentMapZone(final ParentMapZoneDescription parentMapZoneDesc, final float zoom, final float originX, final float originY, final String mapAnmPath) {
        final ParentMapZone parentMapZone = new ParentMapZone(parentMapZoneDesc);
        parentMapZone.setZoomFactor(zoom);
        final List<PartitionListMapZoneDescription> mapZoneDescList = parentMapZoneDesc.getChildren();
        for (int i = 0, size = mapZoneDescList.size(); i < size; ++i) {
            final PartitionListMapZoneDescription mapZoneDesc = mapZoneDescList.get(i);
            final MapZone zone = new MapZone();
            zone.setMapZoneDescription(mapZoneDesc);
            final PartitionList list = mapZoneDesc.getPartitionList();
            if (list != null) {
                final TIntIterator it = list.list().iterator();
                final int mapWidth = 774;
                final int mapHeight = 387;
                while (it.hasNext()) {
                    final int coordsHash = it.next();
                    final short mapX = AmbienceMap.getX(coordsHash);
                    final short mapY = AmbienceMap.getY(coordsHash);
                    final int isoLocalX = mapX * 18;
                    final int isoLocalY = mapY * 18;
                    final float screenX = (isoLocalX - isoLocalY) * 43.0f + originX;
                    final float screenY = -(isoLocalX + isoLocalY) * 21.5f + originY;
                    zone.addElement(mapX, mapY, screenX - 774.0f, screenY - 387.0f, screenX, screenY, screenX + 774.0f, screenY - 387.0f, screenX, screenY - 774.0f);
                }
                zone.computeCenter();
            }
            final String texturePath = mapZoneDesc.getIconUrl();
            if (texturePath != null) {
                Texture texture = null;
                try {
                    texture = TextureInfo.createTexturePowerOfTwo(texturePath);
                }
                catch (Exception e) {
                    MapWidgetZoneManager.m_logger.error((Object)"Probl\u00e8me lors de la r\u00e9cup\u00e9ration de la texture d'iconUrl", (Throwable)e);
                }
                if (texture != null) {
                    zone.setPixmap(new Pixmap(texture));
                }
            }
            zone.setAnmPath(mapAnmPath);
            zone.setAnimName1(mapZoneDesc.getAnim1());
            zone.setAnimName2(mapZoneDesc.getAnim2());
            zone.setHighlightSoundId(mapZoneDesc.getHighlightSoundId());
            zone.setEnableInteractions(mapZoneDesc.isInteractive());
            parentMapZone.addChild(zone);
        }
        parentMapZone.computeCenter();
        return parentMapZone;
    }
    
    public void process(final int deltaTime) {
        final TIntObjectIterator<ParentMapZone> it = this.m_mapZones.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().process(deltaTime);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)MapWidgetZoneManager.class);
    }
}
