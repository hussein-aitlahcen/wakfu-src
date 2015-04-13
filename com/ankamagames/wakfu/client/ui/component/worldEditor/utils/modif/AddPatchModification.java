package com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif;

import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;

public class AddPatchModification extends Modification<PatchItem>
{
    private static final Logger m_logger;
    private final short m_oldPatchId;
    
    public AddPatchModification(final PatchItem item, final short oldPatchId) {
        super(item);
        this.m_oldPatchId = oldPatchId;
    }
    
    @Override
    public Type getType() {
        return Type.ADD;
    }
    
    @Override
    public void apply(final HavenWorldTopology world) {
        world.setPatchId(((PatchItem)this.m_item).getPatchX(), ((PatchItem)this.m_item).getPatchY(), ((PatchItem)this.m_item).getPatchId());
    }
    
    @Override
    public void unapply(final WorldEditor worldEditor) {
        final PatchCatalogEntry entry = HavenWorldDefinitionManager.INSTANCE.getPatchCatalogEntry(this.m_oldPatchId);
        if (entry != null) {
            worldEditor.createGround(entry, ((PatchItem)this.m_item).getPatchX(), ((PatchItem)this.m_item).getPatchY());
            worldEditor.refresh();
        }
    }
    
    public short getOldPatchId() {
        return this.m_oldPatchId;
    }
    
    @Override
    public void onSuccess(final WorldEditor editor) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddPatchModification.class);
    }
}
