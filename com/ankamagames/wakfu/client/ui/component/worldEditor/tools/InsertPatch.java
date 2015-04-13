package com.ankamagames.wakfu.client.ui.component.worldEditor.tools;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;

public class InsertPatch extends Tool
{
    private static final Logger m_logger;
    private final PatchCatalogEntry m_havenCatalogEntry;
    
    public InsertPatch(final PatchCatalogEntry catalogEntry) {
        super(new DisplayOptions(true, true));
        this.m_havenCatalogEntry = catalogEntry;
    }
    
    @Override
    public ItemLayer getWorkingLayer() {
        return ItemLayer.GROUND;
    }
    
    @Override
    protected void execute(final int screenX, final int screenY) {
        final Point2i patchCoord = this.m_worldEditor.getPatchCoordFromMouse(screenX, screenY);
        final InsertPatchValidator validator = new InsertPatchValidator(this.m_worldEditor.getWorkingHavenWorld(), ClientPartitionPatchLibrary.INSTANCE);
        final short oldPatchId = this.removeOldPatch(screenX, screenY);
        final ModificationItem item = this.m_worldEditor.createGround(this.m_havenCatalogEntry, patchCoord.getX(), patchCoord.getY());
        this.m_modification = new AddPatchModification((PatchItem)item, oldPatchId);
        validator.validate(this.m_havenCatalogEntry.getPatchId(), patchCoord.getX(), patchCoord.getY());
        this.m_modification.addErrors(validator.getErrors());
    }
    
    private short removeOldPatch(final int screenX, final int screenY) {
        final PatchItem oldGround = this.m_worldEditor.getGroundUnderMouse(screenX, screenY);
        if (oldGround != null) {
            this.m_worldEditor.remove(oldGround);
            return oldGround.getPatchId();
        }
        return PartitionPatch.EMPTY;
    }
    
    static {
        m_logger = Logger.getLogger((Class)InsertPatch.class);
    }
}
