package com.ankamagames.wakfu.client.core.game.miniMap;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.component.mesh.mapHelper.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

class HavenWorldEntityCreator
{
    private static final Logger m_logger;
    private static final float[] COLOR;
    private static final int HW_BACKGROUND_GFXID = 93606;
    
    static void addBuildingsDisplayPoints(final HavenWorldTopology havenWorld, final ArrayList<DisplayableMapPoint> list) {
        havenWorld.foreachBuildings(new TObjectProcedure<AbstractBuildingStruct>() {
            @Override
            public boolean execute(final AbstractBuildingStruct b) {
                final AbstractBuildingDefinition def = b.getDefinition();
                if (!def.isDecoOnly()) {
                    final BuildingDefinition building = (BuildingDefinition)def;
                    final String name = getBuildingText(building);
                    final DisplayableMapPointIcon icon = getBuildingIcon(building);
                    final int centerX = b.getCellX() + b.getModel().getWidth() / 2;
                    final int centerY = b.getCellY() + b.getModel().getHeight() / 2;
                    list.add(new DisplayableMapPoint(centerX, centerY, 0.0f, havenWorld.getWorldId(), name, b, icon, HavenWorldEntityCreator.COLOR));
                }
                return true;
            }
        });
    }
    
    private static String getBuildingText(final BuildingDefinition building) {
        final String name = WakfuTranslator.getInstance().getString(126, building.getId(), new Object[0]);
        final TextWidgetFormater twf = new TextWidgetFormater();
        twf.append(name);
        final ArrayList<String> effectsDescription = new ArrayList<String>();
        final ArrayList<String> strings = BuildingDefinitionHelper.getEffectsDescription(BuildingDefinitionHelper.getLastBuildingFor(building));
        if (strings != null) {
            effectsDescription.addAll(strings);
        }
        building.forEachWorldEffect(new TIntProcedure() {
            @Override
            public boolean execute(final int value) {
                final String key = String.format("havenWorld.buildingWorldEffect%d", value);
                if (WakfuTranslator.getInstance().containsKey(key)) {
                    effectsDescription.add(WakfuTranslator.getInstance().getString(key));
                }
                return true;
            }
        });
        if (!effectsDescription.isEmpty()) {
            final boolean built = BuildingDefinitionHelper.getState(building) == BuildingDefinitionHelper.ConstructionState.DONE;
            if (!built) {
                twf.openText().addColor(TerritoryViewConstants.ENNEMY);
            }
            for (final String s : effectsDescription) {
                twf.newLine().append(s);
            }
        }
        return twf.finishAndToString();
    }
    
    private static DisplayableMapPointIcon getBuildingIcon(final BuildingDefinition building) {
        final BuildingDefinitionHelper.ConstructionState state = BuildingDefinitionHelper.getState(building);
        switch (state) {
            case STARTED: {
                return DisplayableMapPointIconFactory.HW_BUILDING_ICON_0;
            }
            case WIP: {
                return DisplayableMapPointIconFactory.HW_BUILDING_ICON_1;
            }
            case DONE: {
                return DisplayableMapPointIconFactory.HW_BUILDING_ICON_2;
            }
            default: {
                HavenWorldEntityCreator.m_logger.error((Object)("etat du batiement incorrect " + state));
                return null;
            }
        }
    }
    
    static void create(final HavenWorldTopology havenWorld, final MapOverlay widget) {
        final MultiEntityMapDisplayer displayer = new MultiEntityMapDisplayer();
        widget.setMapDisplayer(displayer);
        createWorldEditor(havenWorld, displayer.getMultiEntity());
        createBackground(displayer);
        setMapRect(havenWorld, widget);
    }
    
    private static void createWorldEditor(final HavenWorldTopology havenWorld, final EntityGroup group) {
        final HavenWorldImagesLibrary havenWorldImages = HavenWorldImagesLibrary.INSTANCE;
        final WorldEditor worldEditor = new WorldEditor();
        worldEditor.onCheckOut();
        worldEditor.initialize(havenWorld, havenWorldImages);
        worldEditor.setCurrentLayer(ItemLayer.NONE);
        worldEditor.getEntities(group);
        centerEntity(group, worldEditor);
        worldEditor.release();
    }
    
    private static void createBackground(final MultiEntityMapDisplayer displayer) {
        final Texture texture = AleaTextureManager.getInstance().getTexture(93606);
        final Pixmap pixmap = new Pixmap(texture, 0, 0, texture.getLayer(0).getWidth(), texture.getLayer(0).getHeight());
        final Color color = new Color(0, 0, 1, 1);
        displayer.setBackground(pixmap, color);
    }
    
    private static void centerEntity(final EntityGroup group, final WorldEditor worldEditor) {
        final Vector2 pos = worldEditor.getScreenCenter();
        final TransformerSRT srt = new TransformerSRT();
        srt.setTranslation(pos.getX(), pos.getY(), 0.0f);
        group.getTransformer().addTransformer(0, srt);
    }
    
    private static void setMapRect(final HavenWorldTopology havenWorld, final MapOverlay widget) {
        final int cellWidth = havenWorld.getWidth() * 9;
        final int cellHeight = havenWorld.getHeight() * 9;
        final int centerCellX = cellWidth / 2 + havenWorld.getOriginX() * 9;
        final int centerCellY = cellHeight / 2 + (havenWorld.getOriginY() - 3) * 9;
        widget.setForceDisplayEntity(true);
        final int minX = -(int)IsoWorldScene.convertScreenToIsoX(centerCellX, centerCellY);
        final int minY = -(int)IsoWorldScene.convertScreenToIsoY(centerCellX, centerCellY);
        final int length = Math.max(cellWidth, cellHeight);
        final int width = (int)(length * 86.0f);
        widget.setMapRect(minX, minY, width, width / 2);
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldEntityCreator.class);
        COLOR = Color.WHITE.getFloatRGBA();
    }
}
