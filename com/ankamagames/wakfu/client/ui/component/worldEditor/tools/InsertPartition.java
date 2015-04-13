package com.ankamagames.wakfu.client.ui.component.worldEditor.tools;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;

public class InsertPartition extends Tool
{
    private static final Logger m_logger;
    private final Tool m_lastTool;
    private final int m_partitionCost;
    
    public InsertPartition(final Tool lastTool, final int partitionCost) {
        super(new DisplayOptions(true, true));
        this.m_lastTool = lastTool;
        this.m_partitionCost = partitionCost;
    }
    
    @Override
    public ItemLayer getWorkingLayer() {
        return ItemLayer.PARTITION;
    }
    
    @Override
    protected void execute(final int screenX, final int screenY) {
        this.m_worldEditor.unhighlightPartition();
        final Point2i patch = this.m_worldEditor.getPatchCoordFromMouse(screenX, screenY);
        final int mapX = AbstractHavenWorldTopology.patchCoordXToPartition(patch.getX());
        final int mapY = AbstractHavenWorldTopology.patchCoordYToPartition(patch.getY());
        if (isEmptyPartition(this.m_worldEditor, mapX, mapY)) {
            patch.set(AbstractHavenWorldTopology.partitionToPatchCoordX(mapX), AbstractHavenWorldTopology.partitionToPatchCoordY(mapY));
            final PartitionItem item = this.m_worldEditor.createPartitionEntity(this.m_partitionCost, patch);
            final AddPatchModification[] patchItems = this.createPatches(mapX, mapY);
            this.m_modification = new AddPartitionModification(item, patchItems);
        }
        else {
            this.m_worldEditor.setTool(this.m_lastTool);
        }
    }
    
    private AddPatchModification[] createPatches(final int mapX, final int mapY) {
        final ArrayList<AddPatchModification> list = new ArrayList<AddPatchModification>();
        this.createDefaultPatches(mapX, mapY, list);
        this.createBorderPatches(mapX, mapY, list);
        final AddPatchModification[] patchItems = new AddPatchModification[list.size()];
        list.toArray(patchItems);
        return patchItems;
    }
    
    private void createBorderPatches(final int mapX, final int mapY, final ArrayList<AddPatchModification> list) {
        final int patchX = AbstractHavenWorldTopology.partitionToPatchCoordX(mapX);
        final int patchY = AbstractHavenWorldTopology.partitionToPatchCoordY(mapY);
        final HavenWorldTopology world = this.m_worldEditor.getWorkingHavenWorld();
        final short[][] patchIds = world.getPatchIdsWithoutBorder();
        for (int j = patchY - 1; j <= patchY + 2; ++j) {
            for (int i = patchX - 1; i <= patchX + 2; ++i) {
                if (i == patchX || i == patchX + 1) {
                    if (j == patchY) {
                        continue;
                    }
                    if (j == patchY + 1) {
                        continue;
                    }
                }
                final short patchId = world.selectBorderPatchId(patchIds, i, j);
                if (patchId != 0) {
                    final PatchCatalogEntry entry = HavenWorldDefinitionManager.INSTANCE.getPatchCatalogEntry(patchId);
                    list.add(this.createModif(entry, i, j));
                }
            }
        }
    }
    
    private void createDefaultPatches(final int mapX, final int mapY, final ArrayList<AddPatchModification> list) {
        final PatchCatalogEntry entry = HavenWorldDefinitionManager.INSTANCE.getPatchCatalogEntry((short)0);
        final int patchX = AbstractHavenWorldTopology.partitionToPatchCoordX(mapX);
        final int patchY = AbstractHavenWorldTopology.partitionToPatchCoordY(mapY);
        list.add(this.createModif(entry, patchX, patchY));
        list.add(this.createModif(entry, patchX + 1, patchY));
        list.add(this.createModif(entry, patchX, patchY + 1));
        list.add(this.createModif(entry, patchX + 1, patchY + 1));
    }
    
    private AddPatchModification createModif(final PatchCatalogEntry entry, final int patchX, final int patchY) {
        final PatchItem oldground = this.m_worldEditor.getGround(patchX, patchY);
        final PatchItem item = this.m_worldEditor.createGround(entry, patchX, patchY);
        final short oldPatchId = (oldground != null) ? oldground.getPatchId() : PartitionPatch.EMPTY;
        return new AddPatchModification(item, oldPatchId);
    }
    
    public static boolean isEmptyPartition(final WorldEditor worldEditor, final int mapX, final int mapY) {
        final HavenWorldTopology world = worldEditor.getWorkingHavenWorld();
        return new InsertPartitionValidator(world).validate(mapX, mapY);
    }
    
    static {
        m_logger = Logger.getLogger((Class)InsertPartition.class);
    }
}
