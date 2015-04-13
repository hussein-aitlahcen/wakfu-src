package com.ankamagames.wakfu.client.core.havenWorld.loader;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;

public class HavenWorldLoader implements ContentInitializer
{
    private static final Logger m_logger;
    public static final HavenWorldLoader INSTANCE;
    
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        try {
            this.loadWorlds();
            this.loadBuildingDeco();
            this.loadBuildings();
            this.loadBuildingVisuals();
            this.loadBuildingCatalogs();
            this.loadEvolutions();
            this.loadPatchCatalogs();
            BuildingDefinitionHelper.buildCache();
            HavenWorldCatalogCategoryManager.getInstance().loadCategoriesOrder();
        }
        catch (Exception e) {
            HavenWorldLoader.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        finally {
            clientInstance.fireContentInitializerDone(this);
        }
    }
    
    @Override
    public String getName() {
        return null;
    }
    
    private void loadWorlds() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new HavenWorldDefinitionBinaryData(), new LoadProcedure<HavenWorldDefinitionBinaryData>() {
            @Override
            public void load(final HavenWorldDefinitionBinaryData data) throws Exception {
                final HavenWorldDefinition definition = new HavenWorldDefinition(data.getId(), data.getWorldId(), data.getWorkers());
                definition.setExit(data.getExitWorldId(), data.getExitCellX(), data.getExitCellY(), data.getExitCellZ());
                HavenWorldDefinitionManager.INSTANCE.registerWorld(definition);
            }
        });
    }
    
    private void loadBuildings() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new HavenWorldBuildingBinaryData(), new LoadProcedure<HavenWorldBuildingBinaryData>() {
            @Override
            public void load(final HavenWorldBuildingBinaryData data) throws Exception {
                final BuildingDefinition def = new BuildingDefinition((short)data.getId(), data.getCatalogEntryId(), data.getKamaCost(), data.getWorkers(), data.getWorkersGranted(), data.getEditorGroupId(), data.getRessourceCost(), data.isCanBeDestroyed());
                HavenWorldDefinitionManager.INSTANCE.registerBuilding(def);
                final HavenWorldBuildingBinaryData.Interactive[] arr$;
                final HavenWorldBuildingBinaryData.Interactive[] interactives = arr$ = data.getInteractives();
                for (final HavenWorldBuildingBinaryData.Interactive interactive : arr$) {
                    final BuildingIEDefinition ieDefinition = new BuildingIEDefinition(interactive.getTemplateId(), new Point3(interactive.getX(), interactive.getY(), interactive.getZ()));
                    def.addIE(ieDefinition);
                }
                HavenWorldLoader.this.setSkins(data, def);
                for (final int effectId : data.getEffectIds()) {
                    final WakfuEffect effect = EffectManager.getInstance().loadAndAddEffect(effectId);
                    if (effect != null) {
                        def.addEffect(effect);
                    }
                    else {
                        HavenWorldLoader.m_logger.error((Object)("Probl\u00e8me de chargmeent de Building " + data.getId()));
                    }
                }
                for (final HavenWorldBuildingBinaryData.WorldEffect worldEffect : data.getWorldEffects()) {
                    def.addBuff(worldEffect.getBuffId());
                }
            }
        });
    }
    
    private void loadBuildingDeco() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new HavenWorldBuildingDecoBinaryData(), new LoadProcedure<HavenWorldBuildingDecoBinaryData>() {
            @Override
            public void load(final HavenWorldBuildingDecoBinaryData data) throws Exception {
                final BuildingDecoDefinition def = new BuildingDecoDefinition((short)data.getId(), data.getCatalogEntryId(), data.getKamaCost(), data.getEditorGroupId(), data.getRessourceCost());
                HavenWorldDefinitionManager.INSTANCE.registerBuilding(def);
            }
        });
    }
    
    private void setSkins(final HavenWorldBuildingBinaryData data, final BuildingDefinition def) {
        final HavenWorldBuildingBinaryData.Skin[] skins = data.getSkins();
        if (skins.length != 0) {
            final IntIntLightWeightMap skinMap = new IntIntLightWeightMap();
            for (final HavenWorldBuildingBinaryData.Skin skin : skins) {
                skinMap.put(skin.getItemId(), skin.getEditorGroupId());
            }
            def.setSkinMap(skinMap);
        }
    }
    
    private void loadBuildingVisuals() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new HavenWorldBuildingVisualDefinitionBinaryData(), new LoadProcedure<HavenWorldBuildingVisualDefinitionBinaryData>() {
            @Override
            public void load(final HavenWorldBuildingVisualDefinitionBinaryData data) throws Exception {
                final HavenWorldBuildingVisualDefinition visual = new HavenWorldBuildingVisualDefinition(data.getBuildingId());
                HavenWorldBuildingVisualDefinitionManager.INSTANCE.register(visual);
                final HavenWorldBuildingVisualDefinitionBinaryData.VisualElement[] arr$;
                final HavenWorldBuildingVisualDefinitionBinaryData.VisualElement[] elements = arr$ = data.getElements();
                for (final HavenWorldBuildingVisualDefinitionBinaryData.VisualElement element : arr$) {
                    final Point3 position = new Point3(element.getX(), element.getY(), element.getZ());
                    visual.addElement(new HavenWorldBuildingVisualElement(element.getGfxId(), element.getAnimName(), element.hasGuildColor(), element.isOccluder(), element.getHeight(), position, Direction8.getDirectionFromIndex(element.getDirection())));
                }
            }
        });
    }
    
    private void loadBuildingCatalogs() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new HavenWorldBuildingCatalogBinaryData(), new LoadProcedure<HavenWorldBuildingCatalogBinaryData>() {
            @Override
            public void load(final HavenWorldBuildingCatalogBinaryData data) throws Exception {
                final BuildingCatalogEntry entry = new BuildingCatalogEntry((short)data.getId(), data.getCategoryId(), data.getOrder(), data.getBuildingType(), data.isBuyable(), data.getMaxQuantity());
                HavenWorldSoundLibrary.INSTANCE.registerBuilding(data.getId(), data.getBuildingSoundId());
                for (final HavenWorldBuildingCatalogBinaryData.BuildingCondition condition : data.getBuildingCondition()) {
                    entry.addCondition(condition.getBuildingTypeNeeded(), condition.getQuantity());
                }
                HavenWorldDefinitionManager.INSTANCE.registerCatalogEntry(entry);
            }
        });
    }
    
    private void loadPatchCatalogs() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new HavenWorldPatchDefinitionBinaryData(), new LoadProcedure<HavenWorldPatchDefinitionBinaryData>() {
            @Override
            public void load(final HavenWorldPatchDefinitionBinaryData data) throws Exception {
                final PatchCatalogEntry entry = new PatchCatalogEntry((short)data.getId(), (short)data.getPatchId(), data.getKamaCost(), data.getCategoryId(), (short)(-1));
                HavenWorldDefinitionManager.INSTANCE.registerCatalogEntry(entry);
                HavenWorldSoundLibrary.INSTANCE.registerPatch(data.getCategoryId(), data.getSoundId());
            }
        });
    }
    
    private void loadEvolutions() throws Exception {
        BinaryDocumentManager.getInstance().foreach(new HavenWorldBuildingEvolutionBinaryData(), new LoadProcedure<HavenWorldBuildingEvolutionBinaryData>() {
            @Override
            public void load(final HavenWorldBuildingEvolutionBinaryData data) throws Exception {
                final BuildingEvolution evolution = new BuildingEvolution(data.getCatalogEntryId(), (short)data.getFromId(), (short)data.getToId(), data.getDelay(), data.getOrder());
                HavenWorldDefinitionManager.INSTANCE.registerEvolution(evolution);
            }
        });
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldLoader.class);
        INSTANCE = new HavenWorldLoader();
    }
}
