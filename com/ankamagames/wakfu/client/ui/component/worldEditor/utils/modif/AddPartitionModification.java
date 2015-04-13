package com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;

public class AddPartitionModification extends Modification<PartitionItem>
{
    private static final Logger m_logger;
    private final AddPatchModification[] m_createdPatches;
    
    public AddPartitionModification(final PartitionItem item, final AddPatchModification... createdPatches) {
        super(item);
        this.m_createdPatches = createdPatches;
    }
    
    @Override
    public Type getType() {
        return Type.ADD;
    }
    
    @Override
    public void apply(final HavenWorldTopology world) {
        for (final AddPatchModification modif : this.m_createdPatches) {
            modif.apply(world);
        }
    }
    
    @Override
    public void unapply(final WorldEditor worldEditor) {
        for (final AddPatchModification modif : this.m_createdPatches) {
            if (!isWater(modif.getItem().getPatchId()) || !isWater(modif.getOldPatchId())) {
                modif.unapply(worldEditor);
            }
        }
        final Point2i patchPos = this.getLocation();
        refreshBorderPatches(worldEditor, patchPos.getX(), patchPos.getY());
        worldEditor.refresh();
    }
    
    private static boolean isWater(final int patchId) {
        return PartitionPatch.getMapCoordFromPatchId(patchId).getX() == 1;
    }
    
    private static void refreshBorderPatches(final WorldEditor worldEditor, final int patchX, final int patchY) {
        final HavenWorldTopology world = worldEditor.getWorkingHavenWorld();
        final short[][] patchIds = world.getPatchIdsWithoutBorder();
        for (int j = patchY - 1; j <= patchY + 2; ++j) {
            for (int i = patchX - 1; i <= patchX + 2; ++i) {
                final short patchId = world.selectBorderPatchId(patchIds, i, j);
                if (patchId != 0) {
                    new AddPatchModification(new PatchItem(null, i, j), patchId).unapply(worldEditor);
                }
            }
        }
    }
    
    public void revertModif(final WorldEditor editor) {
        for (final AddPatchModification modif : this.m_createdPatches) {
            modif.revert(editor);
        }
        editor.refresh();
        this.unapply(editor);
    }
    
    @Override
    public void onSuccess(final WorldEditor editor) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddPartitionModification.class);
    }
}
