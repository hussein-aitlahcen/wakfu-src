package com.ankamagames.wakfu.common.game.world.havenWorld.validator;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class InsertPartitionValidator extends ModificationValidator<AbstractHavenWorldTopology>
{
    private static final Logger m_logger;
    
    public InsertPartitionValidator(final AbstractHavenWorldTopology world) {
        super(world);
    }
    
    public boolean validate(final int mapX, final int mapY) {
        final int left = AbstractHavenWorldTopology.bottomLeftPatchCoordX(mapX);
        final int right = AbstractHavenWorldTopology.bottomRightPatchCoordX(mapX);
        final int top = AbstractHavenWorldTopology.topLeftPatchCoordY(mapY);
        final int bottom = AbstractHavenWorldTopology.bottomLeftPatchCoordY(mapY);
        final Rect worldBounds = ((AbstractHavenWorldTopology)this.m_dataProvider).getEditableWorldBounds();
        return worldBounds.contains(left, top) && worldBounds.contains(right, top) && worldBounds.contains(left, bottom) && worldBounds.contains(right, bottom) && !PartitionPatch.isEditable(((AbstractHavenWorldTopology)this.m_dataProvider).getPatchId(left, top)) && !PartitionPatch.isEditable(((AbstractHavenWorldTopology)this.m_dataProvider).getPatchId(right, top)) && !PartitionPatch.isEditable(((AbstractHavenWorldTopology)this.m_dataProvider).getPatchId(left, bottom)) && !PartitionPatch.isEditable(((AbstractHavenWorldTopology)this.m_dataProvider).getPatchId(right, bottom));
    }
    
    static {
        m_logger = Logger.getLogger((Class)InsertPartitionValidator.class);
    }
}
