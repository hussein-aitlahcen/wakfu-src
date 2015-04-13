package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;

@XulorActionsTag
public class HavenWorldDialogsActions
{
    public static final String PACKAGE = "wakfu.havenWorld";
    
    public static void evolveBuilding(final Event event) {
        evolveBuilding(event, (HavenWorldElementView)PropertiesProvider.getInstance().getObjectProperty("selectedBuilding"));
    }
    
    public static void evolveBuilding(final Event event, final HavenWorldElementView element) {
        final AbstractBuildingDefinition buildingDefinition = element.getBuildingDefinitionView().getBuilding();
        final BuildingEvolution evolutionFromBuilding = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(buildingDefinition.getId());
        if (evolutionFromBuilding != null) {
            final AbstractBuildingDefinition building = HavenWorldDefinitionManager.INSTANCE.getBuilding(evolutionFromBuilding.getBuildingToId());
            final BuildingCatalogEntry buildingCatalogEntry = HavenWorldDefinitionManager.INSTANCE.getBuildingCatalogEntry(building.getCatalogEntryId());
            final HavenWorldBuildingEvolutionView havenWorldBuildingEvolutionView = new HavenWorldBuildingEvolutionView(element.getUniqueId(), buildingDefinition, buildingCatalogEntry, UIWorldEditorFrame.getInstance().getDataProvider());
            PropertiesProvider.getInstance().setPropertyValue("buildingEvolution", havenWorldBuildingEvolutionView);
            loadEvolveBuildingDialog(event);
        }
    }
    
    public static void loadEvolveBuildingDialog(final Event event) {
        Xulor.getInstance().load("buildingEvolutionDialog", Dialogs.getDialogPath("buildingEvolutionDialog"), 256L, (short)10005);
    }
    
    public static void payEvolution(final Event event) {
        final AbstractOccupation currentOccupation = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOccupation();
        if (!(currentOccupation instanceof ManageHavenWorldOccupation)) {
            return;
        }
        final HavenWorldBuildingEvolutionView havenWorldBuildingEvolutionView = (HavenWorldBuildingEvolutionView)PropertiesProvider.getInstance().getObjectProperty("buildingEvolution");
        if (havenWorldBuildingEvolutionView == null) {
            return;
        }
        final long baseBuildingUID = havenWorldBuildingEvolutionView.getBaseBuildingUID();
        final HavenWorldManageActionRequest havenWorldManageActionRequest = new HavenWorldManageActionRequest();
        final BuildingEvolve buildingCreate = new BuildingEvolve(baseBuildingUID);
        havenWorldManageActionRequest.addAction(buildingCreate);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(havenWorldManageActionRequest);
        if (WakfuGameEntity.getInstance().hasFrame(UIBuildingPanelFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIBuildingPanelFrame.getInstance());
        }
        else {
            Xulor.getInstance().unload("buildingEvolutionDialog");
        }
    }
}
