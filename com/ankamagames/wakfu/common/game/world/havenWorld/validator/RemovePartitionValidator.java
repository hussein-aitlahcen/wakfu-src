package com.ankamagames.wakfu.common.game.world.havenWorld.validator;

import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;

public class RemovePartitionValidator extends ModificationValidator<AbstractHavenWorldTopology>
{
    private static final Logger m_logger;
    
    public RemovePartitionValidator(final AbstractHavenWorldTopology world) {
        super(world);
    }
    
    public boolean validate(final int patchX, final int patchY) {
        final short mapX = AbstractHavenWorldTopology.patchCoordXToPartition(patchX);
        final short mapY = AbstractHavenWorldTopology.patchCoordYToPartition(patchY);
        for (final AbstractBuildingStruct b : ((AbstractHavenWorldTopology)this.m_dataProvider).getBuildingsInMap(mapX, mapY)) {
            this.addConflict(new BuildingItem(b));
        }
        this.checkPatchId(patchX, patchY);
        this.checkPatchId(patchX + 1, patchY);
        this.checkPatchId(patchX, patchY + 1);
        this.checkPatchId(patchX + 1, patchY + 1);
        return !this.hasErrors();
    }
    
    private void checkPatchId(final int patchX, final int patchY) {
        final short patchId = ((AbstractHavenWorldTopology)this.m_dataProvider).getPatchId(patchX, patchY);
        if (patchId != 0) {
            final PatchCatalogEntry catalogEntry = HavenWorldDefinitionManager.INSTANCE.getPatchCatalogEntry(patchId);
            this.addConflict(new PatchItem(catalogEntry, patchX, patchY));
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)RemovePartitionValidator.class);
    }
}
