package com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif;

import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;

public class AddBuildingModification extends Modification<BuildingItem>
{
    public AddBuildingModification(final BuildingItem item) {
        super(item);
    }
    
    @Override
    public Type getType() {
        return Type.ADD;
    }
    
    @Override
    public void apply(final HavenWorldTopology world) {
        world.addBuilding(((BuildingItem)this.m_item).getBuildingInfo());
    }
    
    @Override
    public void unapply(final WorldEditor editor) {
        editor.remove(this.m_item);
    }
    
    @Override
    public void onSuccess(final WorldEditor editor) {
        final AbstractBuildingStruct info = ((BuildingItem)this.m_item).getBuildingInfo();
        editor.createBuilding(new BuildingStruct(info.getBuildingUid(), info.getBuildingDefinitionId(), info.getItemId(), info.getCellX(), info.getCellY()));
    }
}
