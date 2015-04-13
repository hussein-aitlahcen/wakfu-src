package com.ankamagames.wakfu.client.ui.component.worldEditor;

import com.ankamagames.xulor2.component.*;
import org.apache.log4j.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.material.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.xulor2.component.mesh.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.converter.*;
import com.ankamagames.framework.graphics.engine.fx.effets.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.tools.*;
import com.ankamagames.framework.graphics.engine.*;
import gnu.trove.*;

public class WorldEditor extends Widget
{
    private static final Logger m_logger;
    public static final String TAG = "WorldEditor";
    public static final int OFFSET_CENTER_Y = 3;
    private static final Color PARTITION_COLOR;
    private static final int REMOVED_TECHNIC;
    private static final Color VALID_COLOR;
    private static final Color ERROR_COLOR;
    private static final Material SELECTED_MATERIAL;
    private final IntObjectLightWeightMap<SpriteCatalogMap> m_spriteCatalogMap;
    private HavenWorldImagesLibrary m_havenWorldImages;
    private HavenWorldTopology m_workingHavenWorld;
    private WorldEditorMesh m_mesh;
    @NotNull
    private Tool m_tool;
    @NotNull
    private ItemLayer m_currentLayer;
    private int m_lastCellX;
    private int m_lastCellY;
    private EntitySprite m_selected;
    
    public WorldEditor() {
        super();
        this.m_spriteCatalogMap = new IntObjectLightWeightMap<SpriteCatalogMap>();
        this.m_currentLayer = ItemLayer.BUILDING;
        this.m_lastCellX = Integer.MAX_VALUE;
        this.m_lastCellY = Integer.MAX_VALUE;
        for (final ItemLayer layer : ItemLayer.values()) {
            this.m_spriteCatalogMap.put(layer.ordinal(), new SpriteCatalogMap());
        }
        (this.m_tool = new DoNothing()).setWorldEditor(this);
    }
    
    @Override
    public String getTag() {
        return "WorldEditor";
    }
    
    public HavenWorldImagesLibrary getHavenWorldImages() {
        return this.m_havenWorldImages;
    }
    
    public HavenWorldTopology getWorkingHavenWorld() {
        return this.m_workingHavenWorld;
    }
    
    public ItemLayer getCurrentLayer() {
        return this.m_currentLayer;
    }
    
    public void initialize(final HavenWorldTopology havenWorld, final HavenWorldImagesLibrary havenWorldImages) {
        this.m_workingHavenWorld = havenWorld.copy();
        this.m_havenWorldImages = havenWorldImages;
        this.createEntities(havenWorld);
        this.m_mesh.centerOnCell(0.0f, 9 * (havenWorld.getOriginY() + havenWorld.getMaxY()) / 2);
    }
    
    public void centerOnCell(final int cellX, final int cellY) {
        this.m_mesh.centerOnCell(cellX, cellY);
        this.refresh();
    }
    
    public float getZoomFactor() {
        return this.m_mesh.getZoomFactor();
    }
    
    public void zoomIn() {
        this.m_mesh.setZoomFactor(this.m_mesh.getZoomFactor() * 2.0f);
        this.refresh();
    }
    
    public void refresh() {
        this.setNeedsToResetMeshes();
    }
    
    public void zoomOut() {
        this.m_mesh.setZoomFactor(this.m_mesh.getZoomFactor() * 0.5f);
        this.refresh();
    }
    
    public void translate(final float x, final float y) {
        final HavenWorldTopology world = this.getWorkingHavenWorld();
        final Rect worldBounds = world.getEditableWorldBounds();
        final Vector2 centerCell = this.m_mesh.getCenterCell();
        final Vector2 movCell = this.m_mesh.getCellCoord(x, y);
        float dx;
        if (movCell.getX() > 0.0f) {
            final int maxCellX = (worldBounds.m_xMax + 1) * 9;
            dx = Math.min(maxCellX - centerCell.getX(), movCell.getX());
        }
        else {
            final int minCellX = worldBounds.m_xMin * 9;
            dx = Math.max(minCellX - centerCell.getX(), movCell.getX());
        }
        float dy;
        if (movCell.getY() > 0.0f) {
            final int maxCellY = (worldBounds.m_yMax + 1) * 9;
            dy = Math.min(maxCellY - centerCell.getY(), movCell.getY());
        }
        else {
            final int minCellY = worldBounds.m_yMin * 9;
            dy = Math.max(minCellY - centerCell.getY(), movCell.getY());
        }
        this.m_mesh.centerOnCell(centerCell.getX() + dx, centerCell.getY() + dy);
        this.refresh();
    }
    
    public Vector2 getScreenCenter() {
        float centerX = (this.m_workingHavenWorld.getOriginX() + this.m_workingHavenWorld.getMaxX()) / 2.0f;
        float centerY = (this.m_workingHavenWorld.getOriginY() + this.m_workingHavenWorld.getMaxY()) / 2.0f - 3.0f;
        centerX += 0.5f;
        centerY += 0.5f;
        return this.m_mesh.getTranslation(centerX * 9.0f, centerY * 9.0f);
    }
    
    @Override
    public void setSize(final int width, final int height, final boolean applyAtOnce) {
        super.setSize(width, height, applyAtOnce);
        this.m_mesh.setSize(width, height);
    }
    
    public Point2i getPatchCoordFromMouse(final int screenX, final int screenY) {
        final Point2i result = this.getCellCoordFromMouse(screenX, screenY);
        result.set(PartitionPatch.getPatchCoordFromCellX(result.getX()), PartitionPatch.getPatchCoordFromCellY(result.getY()));
        return result;
    }
    
    public Point2i getCellCoordFromMouse(final int screenX, final int screenY) {
        return this.m_mesh.getCellCoordFromMouse(screenX, screenY);
    }
    
    public void setCurrentLayer(final ItemLayer currentLayer) {
        if (this.m_currentLayer == currentLayer) {
            return;
        }
        this.doSetCurrentLayer(currentLayer);
        this.setSelectTool();
    }
    
    private void doSetCurrentLayer(final ItemLayer currentLayer) {
        this.m_currentLayer = currentLayer;
    }
    
    public void applyDisplayOptions(final DisplayOptions displayOptions) {
        if (this.m_mesh == null) {
            return;
        }
        this.m_mesh.setBuildingAlpha(displayOptions.m_maskBuilding ? 0.4f : 1.0f);
        this.m_mesh.setDrawGrid(displayOptions.m_drawGrid);
    }
    
    public Tool getTool() {
        return this.m_tool;
    }
    
    public void setTool(final Tool tool) {
        this.m_lastCellX = Integer.MAX_VALUE;
        this.m_lastCellY = Integer.MAX_VALUE;
        this.m_tool.clear();
        (this.m_tool = tool).setWorldEditor(this);
        this.doSetCurrentLayer(this.m_tool.getWorkingLayer());
        this.refresh();
    }
    
    public void temporaryUseTool(final int screenX, final int screenY) {
        final Point2i coord = this.getCellCoordFromMouse(screenX, screenY);
        if (this.m_lastCellX != coord.getX() || this.m_lastCellY != coord.getY()) {
            this.m_tool.clear();
            this.m_tool.tryExecute(screenX, screenY);
            this.refresh();
            this.m_lastCellX = coord.getX();
            this.m_lastCellY = coord.getY();
        }
    }
    
    public void useTool(final int screenX, final int screenY) {
        this.m_tool.clear();
        this.m_tool.executeAndApply(screenX, screenY);
        this.refresh();
    }
    
    private void createEntities(final HavenWorldTopology havenWorld) {
        this.m_mesh.clear();
        this.m_mesh.setImageScale(this.m_havenWorldImages.getPatchScale(), this.m_havenWorldImages.getBuildingScale());
        if (havenWorld == null) {
            WorldEditor.m_logger.error((Object)"Le havre monde ne doit pas \u00eatre null");
            return;
        }
        for (int y = havenWorld.getOriginY(); y <= havenWorld.getMaxY(); ++y) {
            for (int x = havenWorld.getOriginX(); x <= havenWorld.getMaxX(); ++x) {
                this.createGroundEntity(havenWorld.getPatchId(x, y), x, y);
            }
        }
        havenWorld.foreachBuildings(new TObjectProcedure<AbstractBuildingStruct>() {
            @Override
            public boolean execute(final AbstractBuildingStruct b) {
                WorldEditor.this.createBuildingEntity(b);
                return true;
            }
        });
        this.m_mesh.setGridBound(this.m_workingHavenWorld.getEditableWorldBounds());
        this.m_mesh.setDrawGrid(true);
    }
    
    public PartitionItem createPartitionEntity(final int partitionCost, final Point2i patchCoord) {
        final Entity3D sprite = this.m_mesh.addPartitionEntity(patchCoord, WorldEditor.PARTITION_COLOR);
        final PartitionItem item = new PartitionItem(new PartitionCatalogEntry(partitionCost), patchCoord.getX(), patchCoord.getY());
        this.insertSprite(item, sprite);
        return item;
    }
    
    private PatchItem createGroundEntity(final int patchId, final int patchX, final int patchY) {
        final PatchCatalogEntry patchCatalogEntry = HavenWorldDefinitionManager.INSTANCE.getPatchCatalogEntry((short)patchId);
        if (patchCatalogEntry == null) {
            WorldEditor.m_logger.error((Object)("Pas de patch d'id " + patchId));
            return null;
        }
        return this.createGroundEntity(patchCatalogEntry, patchX, patchY);
    }
    
    private PatchItem createGroundEntity(final PatchCatalogEntry patchCatalogEntry, final int patchX, final int patchY) {
        final short patchId = patchCatalogEntry.getPatchId();
        final Texture texture = this.m_havenWorldImages.getPatchTexture(patchId);
        if (texture == null) {
            WorldEditor.m_logger.error((Object)("Pas de texture pour le patch " + patchId));
            return null;
        }
        final PatchItem item = new PatchItem(patchCatalogEntry, patchX, patchY);
        final Point2i center = this.m_havenWorldImages.getPatchCellOffset(patchId);
        final EntitySprite sprite = this.m_mesh.addGroundEntity(item.getCell(), center, texture, Color.WHITE);
        this.insertSprite(item, sprite);
        return item;
    }
    
    public PatchItem createGround(final PatchCatalogEntry entry, final int patchX, final int patchY) {
        final PatchItem elt = this.createGroundEntity(entry, patchX, patchY);
        if (elt != null) {
            this.m_workingHavenWorld.setPatchId(patchX, patchY, entry.getPatchId());
        }
        return elt;
    }
    
    private BuildingItem createBuildingEntity(final AbstractBuildingStruct info) {
        final AbstractBuildingDefinition definition = info.getDefinition();
        final AbstractBuildingDefinition lastDefinition = BuildingDefinitionHelper.getLastBuildingFor(definition);
        if (lastDefinition == null) {
            return null;
        }
        final int groupId = lastDefinition.getEditorGroupId(info.getItemId());
        final Texture texture = this.m_havenWorldImages.getBuildingTexture(groupId);
        if (texture == null) {
            WorldEditor.m_logger.error((Object)("Pas de texture pour le groupe " + groupId));
            return null;
        }
        final Point2i origin = new Point2i(info.getCellX(), info.getCellY());
        final Point2i center = this.m_havenWorldImages.getBuildingCellOffset(groupId);
        final EntitySprite sprite = this.m_mesh.addBuildingEntity(origin, center, texture, Color.WHITE);
        final BuildingItem item = new BuildingItem(info);
        this.insertSprite(item, sprite);
        return item;
    }
    
    public BuildingItem createBuilding(final BuildingStruct info) {
        final BuildingItem elt = this.createBuildingEntity(info);
        if (elt != null) {
            final AbstractBuildingDefinition lastBuilding = BuildingDefinitionHelper.getLastBuildingFor(info.getDefinition());
            this.m_workingHavenWorld.addBuilding(new BuildingStruct(info.getBuildingUid(), lastBuilding.getId(), info.getItemId(), info.getCellX(), info.getCellY()));
        }
        return elt;
    }
    
    public BuildingItem moveBuilding(final AbstractBuildingStruct info) {
        final BuildingItem elt = this.createBuildingEntity(info);
        this.m_workingHavenWorld.addBuilding(info);
        return elt;
    }
    
    private void insertSprite(final ModificationItem item, final Entity sprite) {
        final Entity removed = this.spriteMapInsert(sprite, item);
        this.removeEntity(removed);
    }
    
    private void removeEntity(final Entity removed) {
        this.m_mesh.removeEntity(removed);
        if (this.m_selected == removed) {
            this.m_selected = null;
        }
    }
    
    public void remove(final ModificationItem element) {
        if (element != null) {
            switch (element.getLayer()) {
                case BUILDING: {
                    this.m_workingHavenWorld.removeBuilding(element.getUid());
                    break;
                }
                case PARTITION: {
                    this.m_mesh.removePartitionEntity();
                    break;
                }
            }
        }
        final Entity sprite = this.spriteMapRemove(element);
        this.removeEntity(sprite);
        this.refresh();
    }
    
    @Override
    protected void addInnerMeshes() {
        super.addInnerMeshes();
        this.m_entity.addChild(this.m_mesh.getEntity());
    }
    
    public EntityGroup getMapEntity() {
        return this.m_mesh.getEntity();
    }
    
    @Override
    public boolean isAppearanceCompatible(final DecoratorAppearance appearance) {
        return true;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        if (this.m_mesh != null) {
            this.m_mesh.dispose();
            this.m_mesh = null;
        }
        this.m_selected = null;
        for (int i = 0; i < this.m_spriteCatalogMap.size(); ++i) {
            this.m_spriteCatalogMap.getQuickValue(i).clear();
        }
        this.m_spriteCatalogMap.clear();
        this.m_tool = new DoNothing();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        (this.m_mesh = new WorldEditorMesh()).setZoomFactor(0.25f);
        final DecoratorAppearance app = DecoratorAppearance.checkOut();
        app.setWidget(this);
        this.add(app);
        this.setNeedsToPreProcess();
        this.setFocusable(true);
    }
    
    @Override
    public boolean setXMLAttribute(final int hash, final String value, final ConverterLibrary cl) {
        return super.setXMLAttribute(hash, value, cl);
    }
    
    @Override
    public boolean setPropertyAttribute(final int hash, final Object value) {
        return super.setPropertyAttribute(hash, value);
    }
    
    public PatchItem getGroundUnderMouse(final int screenX, final int screenY) {
        final Point2i patchCoord = this.getPatchCoordFromMouse(screenX, screenY);
        final int cellX = patchCoord.getX() * 9;
        final int cellY = patchCoord.getY() * 9;
        final EntitySprite sprite = this.m_mesh.findGroundEntity(cellX, cellY);
        return (PatchItem)this.getItem(ItemLayer.GROUND, sprite);
    }
    
    public PatchItem getGround(final int patchX, final int patchY) {
        final int cellX = patchX * 9;
        final int cellY = patchY * 9;
        final EntitySprite sprite = this.m_mesh.findGroundEntity(cellX, cellY);
        return (PatchItem)this.getItem(ItemLayer.GROUND, sprite);
    }
    
    public BuildingItem getBuildingUnderMouse(final int screenX, final int screenY) {
        final EntitySprite sprite = this.m_mesh.findBuildingEntityUnderMouse(screenX, screenY);
        return (BuildingItem)this.getItem(ItemLayer.BUILDING, sprite);
    }
    
    public BuildingItem getBuilding(final int cellX, final int cellY) {
        final EntitySprite entity = this.m_mesh.findBuildingEntity(cellX, cellY);
        return (BuildingItem)this.getItem(ItemLayer.BUILDING, entity);
    }
    
    public void highlightEntity(final ModificationItem element) {
        if (element instanceof PartitionItem) {
            this.createPartitionEntity(0, element.getCell());
            return;
        }
        this.unhighlightPartition();
        final Entity sprite = this.spriteMapGet(element);
        if (sprite instanceof EntitySprite || sprite == null) {
            this.selectSprite((EntitySprite)sprite);
        }
        this.refresh();
    }
    
    public void unhighlightPartition() {
        if (this.m_mesh != null) {
            this.m_mesh.removePartitionEntity();
        }
    }
    
    private void selectSprite(final EntitySprite sprite) {
        if (this.m_selected == sprite) {
            return;
        }
        if (this.m_selected != null && this.m_selected.exists()) {
            this.m_selected.setMaterial(Material.WHITE_NO_SPECULAR);
        }
        if (sprite != null) {
            sprite.setMaterial(WorldEditor.SELECTED_MATERIAL);
        }
        this.m_selected = sprite;
    }
    
    public void markAsRemoved(final ModificationItem element) {
        final Entity sprite = this.spriteMapGet(element);
        if (sprite == null) {
            return;
        }
        final Effect baseEffect = EffectManager.getInstance().getUiEffect();
        if (baseEffect.isTechniqueValide(WorldEditor.REMOVED_TECHNIC)) {
            sprite.setEffect(baseEffect, WorldEditor.REMOVED_TECHNIC, null);
        }
    }
    
    public void unmarkAsRemoved(final ModificationItem element) {
        final Entity sprite = this.spriteMapGet(element);
        if (sprite == null) {
            return;
        }
        sprite.removeEffectForUI();
    }
    
    public void markAsError(final ModificationItem element) {
        final Entity sprite = this.spriteMapGet(element);
        if (sprite == null) {
            return;
        }
        sprite.setColor(WorldEditor.ERROR_COLOR);
    }
    
    public void unmarkAsError(final ModificationItem element) {
        final Entity sprite = this.spriteMapGet(element);
        if (sprite == null) {
            return;
        }
        sprite.setColor(WorldEditor.VALID_COLOR);
    }
    
    public void unmarkAsError() {
        for (int i = 0; i < this.m_spriteCatalogMap.size(); ++i) {
            this.m_spriteCatalogMap.getQuickValue(i).m_sprites.forEachValue(new TObjectProcedure<Entity>() {
                @Override
                public boolean execute(final Entity object) {
                    if (object instanceof EntitySprite) {
                        object.setColor(WorldEditor.VALID_COLOR);
                    }
                    return true;
                }
            });
        }
    }
    
    public void setSelectTool() {
        if (this.m_currentLayer == ItemLayer.GROUND) {
            this.setTool(new SelectPatch());
            return;
        }
        if (this.m_currentLayer == ItemLayer.BUILDING) {
            this.setTool(new SelectBuilding());
            return;
        }
        this.setTool(new DoNothing());
    }
    
    private Entity spriteMapGet(final ModificationItem element) {
        if (element == null) {
            return null;
        }
        return this.getSpriteMap(element).getSprite(element);
    }
    
    private Entity spriteMapInsert(final Entity sprite, final ModificationItem element) {
        return this.getSpriteMap(element).put(sprite, element);
    }
    
    private Entity spriteMapRemove(final ModificationItem element) {
        return this.getSpriteMap(element).remove(element);
    }
    
    private ModificationItem getItem(final ItemLayer layer, final EntitySprite sprite) {
        return this.getSpriteMap(layer).getItem(sprite);
    }
    
    private SpriteCatalogMap getSpriteMap(final ModificationItem element) {
        return this.getSpriteMap(element.getLayer());
    }
    
    private SpriteCatalogMap getSpriteMap(final ItemLayer layer) {
        return this.m_spriteCatalogMap.get(layer.ordinal());
    }
    
    public void getEntities(final EntityGroup group) {
        this.m_mesh.getEntities(group);
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorldEditor.class);
        PARTITION_COLOR = new Color(255, 255, 255, 64);
        REMOVED_TECHNIC = Engine.getTechnic("HavenWorldBuildingRemoved");
        VALID_COLOR = Color.WHITE;
        ERROR_COLOR = new Color(255, 0, 0, 255);
        (SELECTED_MATERIAL = Material.Factory.newInstance()).setSpecularColor(0.2f, 0.2f, 0.2f);
    }
    
    private static class SpriteCatalogMap<T extends ModificationItem>
    {
        private final THashMap<Entity, T> m_elements;
        private final THashMap<T, Entity> m_sprites;
        
        private SpriteCatalogMap() {
            super();
            this.m_elements = new THashMap<Entity, T>();
            this.m_sprites = new THashMap<T, Entity>();
        }
        
        public Entity put(final Entity sprite, final T modif) {
            final Entity oldSprite = this.m_sprites.put(modif, sprite);
            if (oldSprite != null) {
                this.m_elements.remove(oldSprite);
            }
            this.m_elements.put(sprite, modif);
            return oldSprite;
        }
        
        public Entity getSprite(final T modif) {
            return this.m_sprites.get(modif);
        }
        
        public T getItem(final EntitySprite modif) {
            return this.m_elements.get(modif);
        }
        
        public Entity remove(final T element) {
            final Entity sprite = this.m_sprites.remove(element);
            this.m_elements.remove(sprite);
            assert this.m_sprites.size() == this.m_elements.size();
            return sprite;
        }
        
        public T remove(final Entity sprite) {
            final T element = this.m_elements.remove(sprite);
            this.m_sprites.remove(element);
            assert this.m_sprites.size() == this.m_elements.size();
            return element;
        }
        
        public void clear() {
            this.m_elements.clear();
            this.m_sprites.clear();
        }
    }
}
