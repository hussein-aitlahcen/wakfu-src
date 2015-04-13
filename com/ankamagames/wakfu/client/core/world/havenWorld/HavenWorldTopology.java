package com.ankamagames.wakfu.client.core.world.havenWorld;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.client.core.world.dynamicElement.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import java.io.*;
import com.ankamagames.wakfu.client.core.havenWorld.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.game.DynamicElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import gnu.trove.*;

public class HavenWorldTopology extends AbstractHavenWorldTopology
{
    public static final int BORDER_SIZE = 2;
    private static final Logger m_logger;
    private ScreenWorldPatch m_screenWorld;
    private boolean m_fakeWorld;
    private final TLongObjectHashMap<TLongArrayList> m_buildingsVisuals;
    
    public HavenWorldTopology(final short worldId) {
        super(worldId);
        this.m_buildingsVisuals = new TLongObjectHashMap<TLongArrayList>();
    }
    
    private HavenWorldTopology(final HavenWorldTopology world) {
        super(world);
        this.m_buildingsVisuals = new TLongObjectHashMap<TLongArrayList>();
        this.m_fakeWorld = true;
    }
    
    public static HavenWorldTopology createFake(final byte[] rawTopology, final byte[] rawBuildings) {
        final HavenWorldTopology world = new HavenWorldTopology((short)0);
        world.fromRawTopology(rawTopology);
        world.fromRawBuildings(rawBuildings, false);
        world.m_fakeWorld = true;
        return world;
    }
    
    @Override
    public HavenWorldTopology copy() {
        return new HavenWorldTopology(this);
    }
    
    private void checkEditionWorld() {
        if (this.m_fakeWorld) {
            throw new UnsupportedOperationException("Ne doit pas \u00eatre appel\u00e9 avec un monde servant \u00e0 l'edition");
        }
    }
    
    @Override
    public ClientPartitionPatch getPatch(final int patchId) {
        return ClientPartitionPatchLibrary.INSTANCE.getPatch(patchId);
    }
    
    public void fromRawTopology(final byte... rawTopology) {
        this.checkEditionWorld();
        this.create(-4, -10, 8, 11);
        this.fill(PartitionPatch.EMPTY);
        this.createPatches(rawTopology);
        this.computeBorderPatchIds();
    }
    
    private void createPatches(final byte... rawTopology) {
        this.checkEditionWorld();
        final ByteBuffer buffer = ByteBuffer.wrap(rawTopology);
        while (buffer.hasRemaining()) {
            final short partitionX = buffer.getShort();
            final short partitionY = buffer.getShort();
            final short topLeftPatch = buffer.getShort();
            final short topRightPatch = buffer.getShort();
            final short bottomLeftPatch = buffer.getShort();
            final short bottomRightPatch = buffer.getShort();
            this.setPartitionPatchIds(partitionX, partitionY, topLeftPatch, topRightPatch, bottomLeftPatch, bottomRightPatch);
        }
    }
    
    public void onTopologyUpdate(final short partitionX, final short partitionY, final short topLeftPatch, final short topRightPatch, final short bottomLeftPatch, final short bottomRightPatch) {
        this.checkEditionWorld();
        this.setPartitionPatchIds(partitionX, partitionY, topLeftPatch, topRightPatch, bottomLeftPatch, bottomRightPatch);
        this.forcePartitionUpdate(partitionX, partitionY);
        this.computeBorderPatchIds();
        this.forceReloadScreenWorld();
    }
    
    public void onBuildingAdded(final AbstractBuildingStruct info) {
        this.checkEditionWorld();
        boolean changed = this.addBuilding(info, true);
        changed |= this.updatePartitionForEditorGroup(info);
        if (changed) {
            this.forceReloadScreenWorld();
        }
    }
    
    public void onBuildingEquipped(final long uid, final int itemId) {
        this.checkEditionWorld();
        final AbstractBuildingStruct info = this.equipBuilding(uid, itemId);
        if (info != null) {
            this.updatePartitionForEditorGroup(info);
            this.forceReloadScreenWorld();
        }
    }
    
    private void addBuildingVisuals(final AbstractBuildingStruct info) {
        this.checkEditionWorld();
        final HavenWorldBuildingVisualDefinition hwBuildingVisualDefinition = HavenWorldBuildingVisualDefinitionManager.INSTANCE.get(info.getBuildingDefinitionId());
        if (hwBuildingVisualDefinition == null) {
            return;
        }
        final DynamicElementType simpleType = DynamicElementTypeProviderFactory.getInstance().getFromId(WakfuDynamicElementType.SIMPLE.getId());
        final DynamicElementType guildType = DynamicElementTypeProviderFactory.getInstance().getFromId(WakfuDynamicElementType.HW_GUILD.getId());
        final ArrayList<HavenWorldBuildingVisualElement> elements = hwBuildingVisualDefinition.getElements();
        for (final HavenWorldBuildingVisualElement element : elements) {
            try {
                final DynamicElementType type = element.isHasGuildColor() ? guildType : simpleType;
                final DynamicElement dynamicElement = this.createDynamicElement(info, element, type);
                dynamicElement.initialize();
                SimpleAnimatedElementManager.getInstance().addAnimatedElement(dynamicElement);
                this.registerBuildingVisual(info.getBuildingUid(), dynamicElement.getId());
            }
            catch (IOException e) {
                HavenWorldTopology.m_logger.error((Object)"i/o exception", (Throwable)e);
            }
        }
    }
    
    private DynamicElement createDynamicElement(final AbstractBuildingStruct info, final HavenWorldBuildingVisualElement element, final DynamicElementType type) throws IOException {
        final long visualId = GUIDGenerator.getGUID();
        final Point3 relativePosition = element.getRelativePosition();
        return DynamicElement.build(visualId, element.getGfxId(), info.getCellX() + relativePosition.getX(), info.getCellY() + relativePosition.getY(), relativePosition.getZ(), element.isOccluder(), element.getHeight(), (byte)element.getDirection().getIndex(), type, element.getAnimationName());
    }
    
    private void registerBuildingVisual(final long buildingUid, final long visualId) {
        TLongArrayList visuals = this.m_buildingsVisuals.get(buildingUid);
        if (visuals == null) {
            visuals = new TLongArrayList();
            this.m_buildingsVisuals.put(buildingUid, visuals);
        }
        visuals.add(visualId);
    }
    
    private void clearBuildingVisuals(final long buildingUid) {
        final TLongArrayList visuals = this.m_buildingsVisuals.get(buildingUid);
        if (visuals == null) {
            return;
        }
        for (int i = 0; i < visuals.size(); ++i) {
            final long visualId = visuals.get(i);
            SimpleAnimatedElementManager.getInstance().removeAnimatedElement(visualId);
        }
        this.m_buildingsVisuals.remove(buildingUid);
    }
    
    public void onBuildingRemoved(final long buildingUid) {
        final AbstractBuildingStruct info = this.removeBuilding(buildingUid);
        if (info != null) {
            this.updatePartitionForEditorGroup(info);
            this.forceReloadScreenWorld();
        }
        this.clearBuildingVisuals(buildingUid);
    }
    
    private void forcePartitionUpdate(final short partitionX, final short partitionY) {
        MapManagerHelper.forceUpdate(partitionX, partitionY);
    }
    
    private void forceReloadScreenWorld() {
        this.checkEditionWorld();
        if (this.m_screenWorld != null) {
            this.m_screenWorld.clear();
        }
        WakfuClientInstance.getInstance().getWorldScene().getIsoCamera().resetMaskKey();
        final ScreenWorldPatch world = this.createScreenWorld();
        HavenWorldManager.INSTANCE.getMapLoader().resetScreenWorld(world);
        DisplayedScreenWorld.getInstance().rebuildCache();
        GroupLayerManager.getInstance().clear();
        this.resetAllMaskLayer();
    }
    
    private void resetAllMaskLayer() {
        MobileManager.getInstance().setUndefinedMaskLayer();
        AnimatedElementSceneViewManager.getInstance().setUndefinedMaskLayer();
        ResourceManager.getInstance().setUndefinedMaskLayer();
        SimpleAnimatedElementManager.getInstance().setUndefinedMaskLayer();
    }
    
    private boolean updatePartitionForEditorGroup(final AbstractBuildingStruct info) {
        final Rect rect = info.getPartitionBounds();
        for (int y = rect.m_yMin; y < rect.m_yMax; ++y) {
            for (int x = rect.m_xMin; x < rect.m_xMax; ++x) {
                this.forcePartitionUpdate((short)x, (short)y);
                MapManagerHelper.LoadMapsAroundCellPosition(PartitionConstants.getCellCenterXFromPartitionX(x), PartitionConstants.getCellCenterYFromPartitionY(y), 0);
            }
        }
        return true;
    }
    
    public void fromRawBuildings(final byte[] rawBuildings, final boolean addVisuals) {
        this.checkEditionWorld();
        final ByteBuffer buffer = ByteBuffer.wrap(rawBuildings);
        while (buffer.hasRemaining()) {
            final AbstractBuildingStruct info = BuildingStruct.fromRaw(buffer);
            this.addBuilding(info, addVisuals);
        }
    }
    
    private boolean addBuilding(final AbstractBuildingStruct info, final boolean addVisuals) {
        this.addBuilding(info);
        if (addVisuals) {
            this.addBuildingVisuals(info);
        }
        return true;
    }
    
    public void computeBorderPatchIds() {
        final short[][] copy = this.getPatchIdsWithoutBorder();
        for (int x = 0; x < this.getWidth(); ++x) {
            for (int y = 0; y < this.getHeight(); ++y) {
                final int patchCoordX = x + this.getOriginX();
                final int patchCoordY = y + this.getOriginY();
                if (this.isEditablePatch(patchCoordX, patchCoordY) && BorderHelper.isEmptyPatch(copy, x, y)) {
                    final short patchId = BorderHelper.selectBorderPatchIdFor(copy, x, y);
                    this.setPatchId(patchCoordX, patchCoordY, patchId);
                }
            }
        }
    }
    
    public LightningMapPatch createLightningMap(final short mapCoordX, final short mapCoordY) {
        this.checkEditionWorld();
        final LightningMapPatch map = new LightningMapPatch();
        map.setMapCoordinates(mapCoordX, mapCoordY);
        this.initializeMap(mapCoordX, mapCoordY, map);
        return map;
    }
    
    public WakfuClientEnvironmentMap createEnvironmentMap(final short mapCoordX, final short mapCoordY) {
        this.checkEditionWorld();
        final WakfuClientEnvironmentMapPatch map = new WakfuClientEnvironmentMapPatch();
        map.setCoord(mapCoordX, mapCoordY);
        this.initializeMap(mapCoordX, mapCoordY, map);
        return map;
    }
    
    public ScreenWorldPatch createScreenWorld() {
        this.checkEditionWorld();
        final ScreenWorldPatch world = new ScreenWorldPatch();
        for (int x = this.getOriginX(); x <= this.getMaxX(); ++x) {
            final int cellX = x * 9;
            for (int y = this.getOriginY(); y <= this.getMaxY(); ++y) {
                final ClientPartitionPatch patch = (ClientPartitionPatch)this.getPatch(x, y);
                if (patch != null) {
                    final int cellY = y * 9;
                    world.addPatch(cellX, cellY, patch);
                }
            }
        }
        this.foreachBuildings(new TObjectProcedure<AbstractBuildingStruct>() {
            @Override
            public boolean execute(final AbstractBuildingStruct object) {
                world.addBuilding((BuildingStruct)object);
                return true;
            }
        });
        world.onCreationDone();
        return this.m_screenWorld = world;
    }
    
    public short[][] getPatchIdsWithoutBorder() {
        final short[][] copy = this.createCopy();
        for (int i = 0; i < copy.length; ++i) {
            for (int j = 0; j < copy[i].length; ++j) {
                if (PartitionPatch.getMapCoordFromPatchId(copy[i][j]).getX() == 1) {
                    copy[i][j] = PartitionPatch.EMPTY;
                }
            }
        }
        return copy;
    }
    
    public short selectBorderPatchId(final short[][] patchIds, final int patchX, final int patchY) {
        final int x = patchX - this.getOriginX();
        final int y = patchY - this.getOriginY();
        if (this.isEditablePatch(patchX, patchY) && BorderHelper.isEmptyPatch(patchIds, x, y)) {
            return BorderHelper.selectBorderPatchIdFor(patchIds, x, y);
        }
        return 0;
    }
    
    @Override
    public boolean foreachBuildings(final TObjectProcedure<AbstractBuildingStruct> procedure) {
        final ArrayList<AbstractBuildingStruct> buildings = new ArrayList<AbstractBuildingStruct>();
        final ArrayList<AbstractBuildingStruct> deco = new ArrayList<AbstractBuildingStruct>();
        super.foreachBuildings(new TObjectProcedure<AbstractBuildingStruct>() {
            @Override
            public boolean execute(final AbstractBuildingStruct b) {
                if (b.getDefinition().isDecoOnly()) {
                    deco.add(b);
                }
                else {
                    buildings.add(b);
                }
                return true;
            }
        });
        for (final AbstractBuildingStruct b : buildings) {
            if (!procedure.execute(b)) {
                return false;
            }
        }
        for (final AbstractBuildingStruct b : deco) {
            if (!procedure.execute(b)) {
                return false;
            }
        }
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldTopology.class);
    }
}
